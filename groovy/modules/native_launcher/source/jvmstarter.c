//  A library for easy creation of a native launcher for Java applications.
//
//  Copyright (c) 2006 Antti Karanta (Antti dot Karanta (at) iki dot fi) 
//
//  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
//  compliance with the License. You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software distributed under the License is
//  distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
//  implied. See the License for the specific language governing permissions and limitations under the
//  License.
//
//  Author:  Antti Karanta (Antti dot Karanta (at) iki dot fi) 
//  $Revision$
//  $Date$

#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include <assert.h>

#include <sys/types.h>
#include <sys/stat.h>

#include "jni.h"

#include "jvmstarter.h"

#if defined(_WIN32) || defined(_WIN64)

// as appended to JAVA_HOME + FILE_SEPARATOR
#  define PATHS_TO_SERVER_JVM "bin\\server\\jvm.dll", "bin\\jrockit\\jvm.dll"
#  define PATHS_TO_CLIENT_JVM "bin\\client\\jvm.dll"

#  include "Windows.h"
#    if defined(DIRSUPPORT)
         // uppercase win symbol expected by dirent.h we're using
#        ifndef(WIN)
#          define WIN
#        endif
#        include "dirent.h"

#    endif

//#  define DLHandle HINSTANCE
   typedef HINSTANCE DLHandle;
#  define openDynLib(path) LoadLibrary(path)
#  define findFunction(libraryhandle, funcname) GetProcAddress(libraryhandle, funcname)
#  define closeDynLib(handle) FreeLibrary(handle)

#else

#  if defined(__linux__) && defined(__i386__)
#    define PATHS_TO_SERVER_JVM "lib/i386/server/libjvm.so"
#    define PATHS_TO_CLIENT_JVM "lib/i386/client/libjvm.so"
#  elif defined(__sun__)
#    define PATHS_TO_SERVER_JVM "lib/sparc/server/libjvm.so", "lib/sparcv9/server/libjvm.so"
#    define PATHS_TO_CLIENT_JVM "lib/sparc/client/libjvm.so", "lib/sparc/libjvm.so"
#  elif defined(__CYGWIN__)
#    define PATHS_TO_SERVER_JVM "bin\\server\\jvm.dll", "bin\\jrockit\\jvm.dll"
#    define PATHS_TO_CLIENT_JVM "bin\\client\\jvm.dll"
#  else   
#    error "Either your OS and/or architecture is not currently supported. Support should be easy to add - please see the source (look for #if defined stuff)."
#  endif

#  include <dirent.h>

// stuff for loading a dynamic library
#  include <dlfcn.h>
#  include <link.h>
  
//#  define DLHandle void*
   typedef void *DLHandle;
#  define openDynLib(path) dlopen(path, RTLD_LAZY)
#  define closeDynLib(handle) dlclose(handle)
#  define findFunction(libraryhandle, funcname) dlsym(libraryhandle, funcname)

#endif


#define PATH_TO_JVM_DLL_MAX_LEN 2000


// The pointer to the JNI_CreateJavaVM function needs to be called w/ JNICALL calling convention. Using this typedef
// takes care of that.
typedef jint (JNICALL *JVMCreatorFunc)(JavaVM**,void**,void*);

typedef struct {
  JVMCreatorFunc creatorFunc;
  DLHandle       dynLibHandle;
} JavaDynLib;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

/** In case there are errors, the returned struct contains only NULLs. */
static JavaDynLib findJVMDynamicLibrary(char* java_home, jboolean useServerVm) {
  
  char path[PATH_TO_JVM_DLL_MAX_LEN], *mode;
  JavaDynLib rval;
  int i = 0, j = 0;
  DLHandle jvmLib = (DLHandle)0;
  char* potentialPathsToServerJVM[]   = { PATHS_TO_SERVER_JVM, NULL};
  char*   potentialPathsToClientJVM[] = { PATHS_TO_CLIENT_JVM, NULL};
  char** lookupDirs = NULL;
  char*  dynLibFile;

  rval.creatorFunc  = NULL;
  rval.dynLibHandle = NULL;

  if(useServerVm) {
    mode = "server";
    lookupDirs = potentialPathsToServerJVM;
  } else {
    mode = "client";
    lookupDirs = potentialPathsToClientJVM;
  }
  
  for(i = 0; i < 2; i++) { // try both jdk and jre style paths
    for(j = 0; (dynLibFile = lookupDirs[j]); j++) {
      strcpy(path, java_home);
      strcat(path, FILE_SEPARATOR);
      if(i == 0) { // on a jdk, we need to add jre at this point of the path
        strcat(path, "jre" FILE_SEPARATOR);
      }
      strcat(path, dynLibFile);
      if(fileExists(path)) {
        if(!(jvmLib = openDynLib(path)) )  {
          fprintf(stderr, "error: dynamic library %s exists but could not be loaded!\n", path);
        } 
        goto exitlookup; // just break out of 2 nested loops
      }
    }
  }
exitlookup:
  
  if(!jvmLib) {
    fprintf(stderr, "error: could not find %s jvm under %s\n"
                    "       please check that it is a valid jdk / jre containing the desired type of jvm\n", 
                    mode, java_home);
    return rval;
  }

  rval.creatorFunc = (JVMCreatorFunc)findFunction(jvmLib, "JNI_CreateJavaVM");

  // TODO: look into freeLibrary func and GetLastError to find out why path w/ spaces fails on windows

  if(rval.creatorFunc) {
    rval.dynLibHandle = jvmLib;
  } else {
    fprintf(stderr, "strange bug: jvm creator function not found in jvm dynamic library %s\n", path);
  }
  return rval;
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
/** Returns != 0 if the given file exists. */
extern int fileExists(const char* fileName) {
  struct stat buf;
  int i = stat ( fileName, &buf );

  return ( i == 0 );

}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

void setParameterDescription(ParamInfo* paramInfo, int index, int size, char* name, ParamClass type, short terminating) {
  assert(index < size);
  paramInfo[index].name = name;
  paramInfo[index].type = type;
  paramInfo[index].terminating = terminating;  
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

/** To be called when there is a pending exception that is the result of some
 * irrecoverable error in this startup program. Clears the exception and prints its description. */
void clearException(JNIEnv* env) {

  (*env)->ExceptionDescribe(env);
  (*env)->ExceptionClear(env);
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

#define INCREMENT 1000


/** Appends the given string to target. size param tells the current size of target (target must have been
 * dynamically allocated, i.e. not from stack). If necessary, target is reallocated into a bigger space. 
 * Return the new location of target, and modifies the size inout parameter accordingly. */
char* append(char* target, size_t* size, char* stringToAppend) {
  size_t targetLen, staLen, newLen;

  targetLen = strlen(target);
  staLen    = strlen(stringToAppend);
  newLen    = targetLen + staLen;

  while(((size_t)*size) <= newLen) {
    target = realloc(target, *size += INCREMENT);
    if(!target) {
      fprintf(stderr, "error: out of memory when allocating space for strings");
      return NULL;
    }
  }

  strcat(target, stringToAppend);
  return target;
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

/** returns JNI_FALSE on failure. May change the target to point to a new location */
jboolean appendJarsFromDir(char* dirName, char** targetPtr, size_t* targetSize) {

#if !(defined(_WIN32) || defined(_WIN64)) || defined(DIRSUPPORT)

  DIR           *dir;
  struct dirent *entry;
  size_t        len;
  jboolean      dirNameEndsWithSeparator, rval = JNI_FALSE;
  char          *target = *targetPtr;

  len = strlen(dirName);
  dirNameEndsWithSeparator = ( strcmp(dirName + len - strlen(FILE_SEPARATOR), FILE_SEPARATOR) == 0 );

  dir = opendir(dirName);
  if(!dir) {
    fprintf(stderr, "error: could not read directory %s to append jar files from\n", dirName);
    return JNI_FALSE;
  }

  while( (entry = readdir(dir)) ) {
    len = strlen(entry->d_name);
    if(len > 5 && (strcmp(".jar", (entry->d_name) + len - 4) == 0)) {
      if(!(target = append(target, targetSize, PATH_SEPARATOR))  // this if and the contained ||s are used so that if any of the
      || !(target = append(target, targetSize, dirName))         // calls fail, we jump to the end
      || (dirNameEndsWithSeparator ?  JNI_FALSE : !(target = append(target, targetSize, FILE_SEPARATOR)) ) 
      || !(target = append(target, targetSize, entry->d_name))) goto end;
    }
  }
  rval = JNI_TRUE;

end:
  if(target) *targetPtr = target;
  if(!rval) {
    fprintf(stderr, "error: out of memory when adding entries from %s to classpath\n", dirName);
  }
  closedir(dir);
  return rval;

#else
  fprintf(stderr, "To have reading jars from dirs supported on windows you need to compile w/ option -DDIRSUPPORT\n"
                  "and have the header dirent.h available. Please see the comments in the source / README file for details.\n");
  exit(1);
#endif

}


// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

typedef enum { PREFIX_SEARCH, SUFFIX_SEARCH, EXACT_SEARCH } SearchMode;

/** The first param may be NULL, it is considered an empty array. */
jboolean arrayContainsString(char** nullTerminatedArray, char* searchString, SearchMode mode) {
  int    i = 0;
  size_t sslen, len;
  char   *str;

  if(nullTerminatedArray) {
    switch(mode) {
      case PREFIX_SEARCH : 
        while(str = nullTerminatedArray[i++]) {
          len = strlen(str);
          if(memcmp(str, searchString, len) == 0) return JNI_TRUE;
        }
        break;
      case SUFFIX_SEARCH : 
        sslen = strlen(searchString);        
        while(str = nullTerminatedArray[i++]) {
          len = strlen(str);
          if(len <= sslen && memcmp(searchString + sslen - len, str, len) == 0) return JNI_TRUE;
        }
        break;
      case EXACT_SEARCH : 
        while(str = nullTerminatedArray[i++]) {
          if(strcmp(str, searchString) == 0) return JNI_TRUE;
        }
        break;
    }
  }
  return JNI_FALSE;
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

/** Returns true on error. */
jboolean addStringToJStringArray(JNIEnv* env, char *strToAdd, jobjectArray jstrArr, jint index) {
  jboolean rval = JNI_FALSE;
  jstring arg = (*env)->NewStringUTF(env, strToAdd);

  if(!arg) {
    fprintf(stderr, "error: could not convert %s to java string\n", strToAdd);
    clearException(env);
    return JNI_TRUE;        
  }

  (*env)->SetObjectArrayElement(env, jstrArr, index, arg);
  if((*env)->ExceptionCheck(env)) {
    fprintf(stderr, "error: error when writing %dth element %s to Java String[]\n", index, strToAdd);
    clearException(env);
    rval = JNI_TRUE;
  }
  (*env)->DeleteLocalRef(env, arg);
  return rval;
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

/** See the header file for information.
 */
extern int launchJavaApp(JavaLauncherOptions *options) {
  int            rval = -1;
  JavaVM         *javavm        = NULL;
  JNIEnv         *env           = NULL;
  JavaDynLib     javaLib;
  jint           result, optNr = 0;
  JavaVMInitArgs vm_args;
  JavaVMOption*  jvmOptions; 
  jobjectArray   launcheeJOptions = NULL;
  size_t         len;
  int            i, j, launcheeParamBeginIndex = options->numArguments; 
  jboolean  serverVMRequested = JNI_FALSE;
  jclass    launcheeMainClassHandle  = NULL;
  jclass    strClass;
  jmethodID launcheeMainMethodID = NULL;
  char      *userClasspath = NULL, *envCLASSPATH = NULL, *classpath = NULL, *dirName = NULL;
  size_t    cpsize      = 2000; // just the initial size, expanded if necessary
  jboolean  *isLauncheeOption;
  jint      launcheeParamCount = 0;
  char      *javaHome = NULL, *toolsJarD = NULL;
  char      toolsJarFile[2000];


  // here only numArguments is needed, but numArguments can be 0 and mallocing that is not good...
  isLauncheeOption = malloc((options->numArguments + 1) * sizeof(jboolean));
  // worst case : all command line options are jvm options -> thus numArguments + 2
  // we need space for at least -Djava.class.path
  jvmOptions       = malloc( ((options->numArguments) + (options->numJvmOptions) + 2) * sizeof(JavaVMOption) ); 
  if(!isLauncheeOption || !jvmOptions) {
    fprintf(stderr, "error: out of memory at startup!");
    goto end;
  }

  for(i = 0; i < options->numArguments; i++) isLauncheeOption[i] = JNI_FALSE;

  // find out the argument index after which all the params are launchee (prg being launched) params 
  for(i = 0; i < options->numArguments; i++) {
    char* arg = options->arguments[i];
    
    if((arg[0] == 0) || (arg[0] != '-') // empty strs and ones not beginning w/ - are considered to be terminating args to the launchee
     || arrayContainsString(options->terminatingSuffixes, arg, SUFFIX_SEARCH)) {
      launcheeParamBeginIndex = i;
      break;
    }

    for(j = 0; j < options->paramInfosCount; j++) {
      if(options->paramInfos[j].terminating) {
        switch(options->paramInfos[j].type) {
          case SINGLE_PARAM : 
          case DOUBLE_PARAM :
            if(strcmp(options->paramInfos[j].name, arg) == 0) {
              launcheeParamBeginIndex = i;
              goto endindexcheck; // break out of two nested for loops
            }
            break;
          case PREFIX_PARAM :
            len = strlen(options->paramInfos[j].name);
            if((strlen(arg) >= len) && (memcmp(options->paramInfos[j].name, arg, len) == 0)) {
              launcheeParamBeginIndex = i;
              goto endindexcheck;
            }
            break;
        } // switch
      } // if
    } // for

  }
endindexcheck:

  // classify the arguments
  for(i = 0; i < launcheeParamBeginIndex; i++) {
    char* argument = options->arguments[i];
    len = strlen(argument);

    if(strcmp("-cp", argument) == 0 
    || strcmp("-classpath", argument) == 0
    || strcmp("--classpath", argument) == 0
    ) {
      if(i == (options->numArguments - 1)) { // check that this is not the last param as it requires additional info
        fprintf(stderr, "erroneous use of %s\n", argument);
        goto end;
      }
      if((options->classpathHandling) & CP_PARAM_TO_APP) {
        isLauncheeOption[i]     = JNI_TRUE;
        isLauncheeOption[i + 1] = JNI_TRUE;
      } 
      if((options->classpathHandling) & CP_PARAM_TO_JVM) userClasspath = argument;
      i++;
      continue;
    }

    // check the param infos
    for(j = 0; j < options->paramInfosCount; j++) {
      switch(options->paramInfos[j].type) {
        case SINGLE_PARAM :
          if(strcmp(argument, options->paramInfos[j].name) == 0) {
            isLauncheeOption[i] = JNI_TRUE;
            goto next_arg;
          }
          break;
        case DOUBLE_PARAM :
          if(strcmp(argument, options->paramInfos[j].name) == 0) {
            isLauncheeOption[i] = JNI_TRUE;
            if(i == (options->numArguments - 1)) { // check that this is not the last param as it requires additional info
              fprintf(stderr, "erroneous use of %s\n", argument);
              goto end;
            }
            isLauncheeOption[++i] = JNI_TRUE;
            goto next_arg;
          }
          break;
        case PREFIX_PARAM :
          if(memcmp(argument, options->paramInfos[j].name, len) == 0) {
            isLauncheeOption[i] = JNI_TRUE;
            goto next_arg;            
          }
          break;
      } // switch
    } // for j

    if(strcmp("-server", argument) == 0) { // jvm client or server
      serverVMRequested = JNI_TRUE;
    } else if(strcmp("-client", argument) == 0) {
      // do nothing - client jvm is the default
    } else if((strcmp("-jh", argument) == 0)
           || (strcmp("--javahome", argument) == 0)
           // TODO: check what the javaHomeHandling contains and act accordingly
      ) {
        if(i == (options->numArguments - 1)) { // check that this is not the last param as it requires additional info
          fprintf(stderr, "erroneous use of %s\n", argument);
          goto end;
        }
        javaHome = options->arguments[++i];
    } else { // jvm option
      jvmOptions[optNr].optionString = argument;
      jvmOptions[optNr++].extraInfo  = NULL;
    }
// this label is needed to be able to break out of nested for and switch (with a goto)
next_arg: 
   ;  // at least w/ ms compiler, the tag needs a statement after it before the closing brace. Thus, an empty statement.
  } // end looping over arguments and classifying them for the jvm


  javaHome = options->java_home;
  if(!javaHome && ((options->javahomeHandling) & ALLOW_JH_ENV_VAR_LOOKUP)) javaHome = getenv("JAVA_HOME");

  if(!javaHome || strcmp(javaHome, "") == 0) {
    fprintf(stderr, ((options->javahomeHandling) & ALLOW_JH_ENV_VAR_LOOKUP) ? "error: JAVA_HOME not set\n" : 
                                                                              "error: java home not provided\n");
    goto end;
  }

  // TODO: support or raise error if -Djava.class.path=something is given as a param ???

//      IGNORE_GLOBAL_CP, IGNORE_GLOBAL_CP_IF_PARAM_GIVEN, 
  if(!(IGNORE_GLOBAL_CP & (options->classpathHandling))) { // first check if CLASSPATH is ignored altogether
    if(IGNORE_GLOBAL_CP_IF_PARAM_GIVEN & (options->classpathHandling)) { // use CLASSPATH only if -cp not provided
      if(!userClasspath) envCLASSPATH = getenv("CLASSPATH");
    } else {
      envCLASSPATH = getenv("CLASSPATH");
    }
  } 

  // count the params going to the launchee so we can construct the right size java String[]
  for(i = 0; i < options->numArguments; i++) {
    if(isLauncheeOption[i] || i >= launcheeParamBeginIndex) {
      isLauncheeOption[i] = JNI_TRUE;
      launcheeParamCount++;
    }
  }

  // fetch the pointer to jvm creator func
  javaLib = findJVMDynamicLibrary(javaHome, serverVMRequested);
  if(!javaLib.creatorFunc) goto end; // error message already printed
  
  if(!(classpath = malloc(cpsize))
  ||  (classpath[0] = 0)
  ||  !(classpath = append(classpath, &cpsize, "-Djava.class.path="))) {
    fprintf(stderr, "error: out of memory when allocating space for classpath\n");
    goto end;
  }

  // add the jars from the given dirs
  i = 0;
  if(options->jarDirs) {
    while(dirName = options->jarDirs[i++]) {
      if(!appendJarsFromDir(dirName, &classpath, &cpsize)) goto end; // error msg already printed
    }
  }

  if(userClasspath && ((options->classpathHandling) & CP_PARAM_TO_JVM)) {
    if(!(classpath = append(classpath, &cpsize, PATH_SEPARATOR))
    || !(classpath = append(classpath, &cpsize, userClasspath))
    ) {
      fprintf(stderr, "error: out of memory when adding usercp to classpath\n");
      goto end;
    }
  }

  if(envCLASSPATH && !(classpath = append(classpath, &cpsize, envCLASSPATH))) {
    fprintf(stderr, "error: out of memory when adding CLASSPATH to classpath\n");
    goto end;
  }

  if(options->jars) {
    char* jarName;
    i = 0; // FIXME add path separator
    while(jarName = options->jars[i++]) {
      if(!(classpath = append(classpath, &cpsize, jarName))) {
        fprintf(stderr, "out of memory when adding %s to classpath\n", jarName);
        goto end;
      }
    }
  }

  toolsJarFile[0] = 0;
  strcat(toolsJarFile, javaHome);
  strcat(toolsJarFile, FILE_SEPARATOR);
  strcat(toolsJarFile, "lib");
  strcat(toolsJarFile, FILE_SEPARATOR);
  strcat(toolsJarFile, "tools.jar");

  if(fileExists(toolsJarFile)) {
    // add as env property if requested
    if((options->toolsJarHandling) & TOOLS_JAR_TO_SYSPROP) {
      toolsJarD = malloc(strlen(toolsJarFile) + 12 + 1);
      if(!toolsJarD) {
        fprintf(stderr, "error: could not allocate memory for -Dtools.jar sys prop\n");
        goto end;
      }
      toolsJarD[0] = 0;
      strcat(toolsJarD, "-Dtools.jar=");
      strcat(toolsJarD, toolsJarFile);

      jvmOptions[optNr].optionString = toolsJarD; 
      jvmOptions[optNr++].extraInfo = NULL;
    }
    if(((options->toolsJarHandling) & TOOLS_JAR_TO_CLASSPATH) 
     && (
         !(classpath = append(classpath, &cpsize, javaHome))
      || !(classpath = append(classpath, &cpsize, FILE_SEPARATOR)) 
      || !(classpath = append(classpath, &cpsize, "lib")) 
      || !(classpath = append(classpath, &cpsize, FILE_SEPARATOR)) 
      || !(classpath = append(classpath, &cpsize, "tools.jar")) 
        )
     ) {
      fprintf(stderr, "out of memory when adding tools.jar to classpath\n");
      goto end;
    }

  }

  jvmOptions[optNr].optionString = classpath;
  jvmOptions[optNr++].extraInfo  = NULL;

  for(i = 0; i < options->numJvmOptions; i++) {
    jvmOptions[optNr].optionString = options->jvmOptions[i].optionString;
    jvmOptions[optNr++].extraInfo  = options->jvmOptions[i].extraInfo;
  }


  vm_args.version            = JNI_VERSION_1_4;
  vm_args.options            = jvmOptions;
  vm_args.nOptions           = optNr;
  vm_args.ignoreUnrecognized = JNI_FALSE;


  result = (*(javaLib.creatorFunc))(&javavm, (void**)&env, &vm_args);

  if(result) {
    char* errMsg;
    switch(result) {
      case JNI_ERR        : //  (-1)  unknown error 
        errMsg = "unknown error";
        break;
      case JNI_EDETACHED  : //  (-2)  thread detached from the VM 
        errMsg = "thread detachment";
        break;
      case JNI_EVERSION   : //  (-3)  JNI version error 
        errMsg = "JNI version problems";
        break;
      case JNI_ENOMEM     : //  (-4)  not enough memory 
        errMsg = "not enough memory";
        break;
      case JNI_EEXIST     : //  (-5)  VM already created 
        errMsg = "jvm already created";
        break;
      case JNI_EINVAL     : //  (-6)  invalid arguments
        errMsg = "invalid arguments to jvm creation";
        break;
      default: // should not happen
        errMsg = "unknown exit code";
        break;
    }
    fprintf(stderr, "jvm creation failed with code %d: %s\n", result, errMsg);
    rval = result;
    goto end;
  } 

  free(jvmOptions);
  free(classpath);
  jvmOptions = NULL;
  classpath  = NULL;

  // construct a java.lang.String[] to give program args in
  // find the groovy main class
  // find the startup method and call it

  if(options->extraProgramOptions) {
    i = 0;
    while(options->extraProgramOptions[i]) i++;
    launcheeParamCount += i;
  }
   
  if(result = (*env)->EnsureLocalCapacity(env, launcheeParamCount + 1)) { // + 1 for the String[] to hold the params
    clearException(env);
    fprintf(stderr, "error: could not allocate memory for groovy parameters (how much params did you give, dude?)\n");
    rval = result;
    goto end;
  }

  if(! (strClass = (*env)->FindClass(env, "java/lang/String")) ) {
    clearException(env);
    fprintf(stderr, "error: could not find java.lang.String class\n");
    goto end;
  }


  launcheeJOptions = (*env)->NewObjectArray(env, launcheeParamCount, strClass, NULL);
  if(!launcheeJOptions) {
    clearException(env);
    fprintf(stderr, "error: could not allocate memory for array to hold groovy parameters (how much params did you give, dude?)\n");
    goto end;
  }

  j = 0;
  if(options->extraProgramOptions) {
    char *carg;
    i = 0;
    while(carg = options->extraProgramOptions[i++]) {
      if(addStringToJStringArray(env, carg, launcheeJOptions, j++)
         ) {
        goto end; // error msg already printed
      }
    }
  }

  for(i = 0; i < options->numArguments; i++) {
    if(isLauncheeOption[i]
       && addStringToJStringArray(env, options->arguments[i], launcheeJOptions, j++)
         ) {
        goto end; // error msg already printed
    }
  }

  free(isLauncheeOption);
  isLauncheeOption = NULL;



  launcheeMainClassHandle = (*env)->FindClass(env, options->mainClassName);
  if(!launcheeMainClassHandle) {
    clearException(env);
    fprintf(stderr, "error: could not find groovy startup class %s\n", options->mainClassName);
    goto end;
  }
  launcheeMainMethodID = (*env)->GetStaticMethodID(env, launcheeMainClassHandle, options->mainMethodName, "([Ljava/lang/String;)V");
  if(!launcheeMainMethodID) {
    clearException(env);
    fprintf(stderr, "error: could not find groovy startup method \"%s\" in class %s\n", 
                    options->mainMethodName, options->mainClassName);
    goto end;
  }

  // finally: launch the java application!
  (*env)->CallStaticVoidMethod(env, launcheeMainClassHandle, launcheeMainMethodID, launcheeJOptions);
  // TODO: what happens if the called code calls System.exit?
  // I guess we just exit. It would be cleaner if we called a method returning an int and used that as exit status
  // The jvm side of the program could ensure that System.exit() does not end the process 
  // (by catching that throwable? there is some way)
  if((*env)->ExceptionCheck(env)) {
    // TODO: provide an option which allows the caller to indicate whether to print the stack trace
    (*env)->ExceptionClear(env);
  } else {
    rval = 0;
  }
  

end:
  // cleanup
  if(javavm) {
    if( (*javavm)->DetachCurrentThread(javavm) ) {
      fprintf(stderr, "Warning: could not detach main thread from the jvm at shutdown (please report this as a bug)\n");
    }
    (*javavm)->DestroyJavaVM(javavm);
  }
  if(javaLib.dynLibHandle) closeDynLib(javaLib.dynLibHandle);
  if(classpath)        free(classpath);
  if(isLauncheeOption) free(isLauncheeOption);
  if(jvmOptions)       free(jvmOptions);
  if(toolsJarD)        free(toolsJarD);

  return rval;

}

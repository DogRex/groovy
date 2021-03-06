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

#include <limits.h>

#if defined ( __APPLE__ )
#  include <TargetConditionals.h>
#endif

#include <jni.h>

#include "jvmstarter.h"

// NOTE: when compiling w/ gcc on cygwin, pass -mno-cygwin, which makes gcc define _WIN32 and handle the win headers ok
#if defined(_WIN32)

// as appended to JAVA_HOME + JST_FILE_SEPARATOR (when a jre) or JAVA_HOME + JST_FILE_SEPARATOR + "jre" + JST_FILE_SEPARATOR (when a jdk) 
#  define PATHS_TO_SERVER_JVM "bin\\server\\jvm.dll", "bin\\jrockit\\jvm.dll" 
#  define PATHS_TO_CLIENT_JVM "bin\\client\\jvm.dll"

#  include "Windows.h"

   typedef HINSTANCE DLHandle;
#  define openDynLib(path) LoadLibrary(path)
#  define findFunction(libraryhandle, funcname) GetProcAddress(libraryhandle, funcname)
#  define closeDynLib(handle) FreeLibrary(handle)

// windows' limits.h does not automatically define this:
# if !defined(PATH_MAX) 
#    define PATH_MAX 512
# endif

#else

#  if defined(__linux__) && defined(__i386__)

#    define PATHS_TO_SERVER_JVM "lib/i386/server/libjvm.so"
#    define PATHS_TO_CLIENT_JVM "lib/i386/client/libjvm.so"

#  elif defined(__sun__) 

#    if defined(__sparc__)

#      define PATHS_TO_SERVER_JVM "lib/sparc/server/libjvm.so", "lib/sparcv9/server/libjvm.so"
#      define PATHS_TO_CLIENT_JVM "lib/sparc/client/libjvm.so", "lib/sparc/libjvm.so"

#    elif defined(__i386__)
        // these are just educated guesses, I have no access to solaris running on x86...
#      define PATHS_TO_SERVER_JVM "lib/i386/server/libjvm.so"
#      define PATHS_TO_CLIENT_JVM "lib/i386/client/libjvm.so"

#    endif

#  elif defined ( __APPLE__ )

//  Assume that /System/Library/Frameworks/JavaVM.framework is the JAVA_HOME directory.

#    define PATHS_TO_SERVER_JVM "Libraries/libjvm.dylib"
#    define PATHS_TO_CLIENT_JVM "Libraries/libjvm.dylib"

#  else   
#    error "Either your OS and/or architecture is not currently supported. Support should be easy to add - please see the source (look for #if defined stuff)."
#  endif

   // for getpid()
#  include <unistd.h>

#  include <dirent.h>

// stuff for loading a dynamic library
#  include <dlfcn.h>
#  if ! defined ( __APPLE__ )
#    include <link.h>
#  endif
  
   typedef void *DLHandle;
#  define openDynLib(path) dlopen(path, RTLD_LAZY)
#  define closeDynLib(handle) dlclose(handle)
#  define findFunction(libraryhandle, funcname) dlsym(libraryhandle, funcname)

#endif

static jboolean _jst_debug = JNI_FALSE;

// The pointer to the JNI_CreateJavaVM function needs to be called w/ JNICALL calling convention. Using this typedef
// takes care of that.
typedef jint (JNICALL *JVMCreatorFunc)(JavaVM**,void**,void*);

typedef struct {
  JVMCreatorFunc creatorFunc;
  DLHandle       dynLibHandle;
} JavaDynLib;

// TODO:
// #include <sys/utsname.h>
// int uname(struct utsname *name)
// utsname.sysname

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

extern char* jst_getExecutableHome() {
  static char* _execHome = NULL;
  
  char   *execHome = NULL;
# if defined(__linux__) || defined(__sun__)
  char   *procSymlink;
# endif

# if defined(_WIN32)
  size_t currentBufSize = 0;
# else
  static char *g_emptyString = "";
# endif
  size_t len;
  
  if(_execHome) return _execHome;
  
# if defined(_WIN32)

  do {
    if(currentBufSize == 0) {
      currentBufSize = PATH_MAX + 1;
      execHome = malloc(currentBufSize * sizeof(char));
    } else {
      execHome = realloc(execHome, (currentBufSize += 100) * sizeof(char));
    }
    
    if(!execHome) {
      fprintf(stderr, "error: out of memory when figuring out executable home dir\n");
      return NULL; 
    }

    // reset the error state, just in case it has been left dangling somewhere and 
    // GetModuleFileNameA does not reset it (its docs don't tell). 
    // GetModuleFileName docs: http://msdn.microsoft.com/library/default.asp?url=/library/en-us/dllproc/base/getmodulefilename.asp
    SetLastError(0); 
    len = GetModuleFileNameA(NULL, execHome, currentBufSize);
  } while(GetLastError() == ERROR_INSUFFICIENT_BUFFER);
  // this works equally well but is a bit less readable
  // } while(len == currentBufSize);

  if(len == 0) {
    fprintf(stderr, "error: finding out executable location failed w/ error code %d\n", (int)GetLastError());
    free(execHome);
    return NULL; 
  }
  
# elif defined(__linux__) || defined(__sun__)

  procSymlink = malloc(40 * sizeof(char) ); // big enough
  execHome = malloc((PATH_MAX + 1) * sizeof(char));
  if( !procSymlink || !execHome ) {
    fprintf(stderr, "error: out of memory when finding out executable path\n");
    if(procSymlink) free(procSymlink); 
    if(execHome)    free(execHome);
    return NULL;
  }

  sprintf(procSymlink,
#   if defined(__linux__)
      // /proc/{pid}/exe is a symbolic link to the executable
      "/proc/%d/exe" 
#   elif defined(__sun__)
      // see above
      "/proc/%d/path/a.out"
#   endif
      , (int)getpid()
  );

  if(!jst_fileExists(procSymlink)) {
    free(procSymlink);
    free(execHome);
    return g_emptyString;
  }

  if(!realpath(procSymlink, execHome)) {
    fprintf(stderr, "error: error occurred when trying to find out executable location\n");
    free(procSymlink);
    free(execHome);
    return NULL;    
  }
  free(procSymlink);

# elif defined ( __APPLE__ )

// TODO

# endif

# if defined(_WIN32) || defined (__linux__) || defined(__sun__)
  // cut off the executable name
  *(strrchr(execHome, JST_FILE_SEPARATOR[0]) + 1) = '\0';   
  len = strlen(execHome);
  execHome = realloc(execHome, len + 1); // should not fail as we are shrinking the buffer
  assert(execHome);
  return _execHome = execHome;
  
# else
  // FIXME
  return g_emptyString;
# endif

}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

extern int jst_contains(char** args, int* numargs, const char* option, const jboolean removeIfFound) {
  int i       = 0, 
      foundAt = -1;
  for(; i < *numargs; i++) {
    if(strcmp(option, args[i]) == 0) {
      foundAt = i;
      break;
    }
  }
  if(foundAt != -1) return -1;
  if(removeIfFound) {
    (*numargs)--;
    for(; i < *numargs; i++) {
      args[i] = args[i + i];
    }
  }
  return foundAt;
}

extern char* jst_valueOfParam(char** args, int* numargs, int* checkUpto, const char* option, const JstParamClass paramType, const jboolean removeIfFound, jboolean* error) {
  int i    = 0, 
      step = 1;
  size_t len;
  char* retVal = NULL;

  switch(paramType) {
    case JST_SINGLE_PARAM :
      for(;i < *checkUpto; i++) {
        if(strcmp(option, args[i]) == 0) {
          retVal = args[i];
          break;
        }
      }
      break;
    case JST_DOUBLE_PARAM :
      step = 2;
      for(; i < *checkUpto; i++) {
        if(strcmp(option, args[i]) == 0) {
          if(i == (*numargs - 1)) {
            *error = JNI_TRUE;
            fprintf(stderr, "error: %s must have a value\n", option);
            return NULL;
          }
          retVal = args[i + 1];
          break;
        }
      }
      break;
    case JST_PREFIX_PARAM :
      len = strlen(option);
      for(; i < *checkUpto; i++) {
        if(memcmp(option, args[i], len) == 0) {
          retVal = args[i] + len;
          break;
        }
      }
      break;
  }
  
  if(retVal && removeIfFound) {
    for(;i < (*numargs - step); i++) {
      args[i] = args[i + step];
    }
    *numargs   -= step;
    *checkUpto -= step;
  }
  
  return retVal;  
  
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

/** In case there are errors, the returned struct contains only NULLs. */
static JavaDynLib findJVMDynamicLibrary(char* java_home, jboolean useServerVm) {
  
  char path[PATH_MAX + 1], *mode;
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
    for(j = 0; ( dynLibFile = lookupDirs[j] ); j++) {
      strcpy(path, java_home);
      strcat(path, JST_FILE_SEPARATOR);
      if(i == 0) { // on a jdk, we need to add jre at this point of the path
        strcat(path, "jre" JST_FILE_SEPARATOR);
      }
      strcat(path, dynLibFile);
      if(jst_fileExists(path)) {
        if(!( jvmLib = openDynLib(path) ) )  {
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

  if(rval.creatorFunc) {
    rval.dynLibHandle = jvmLib;
  } else {
    fprintf(stderr, "strange bug: jvm creator function not found in jvm dynamic library %s\n", path);
  }
  return rval;
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
/** Returns != 0 if the given file exists. */
extern int jst_fileExists(const char* fileName) {
  struct stat buf;
  int i = stat(fileName, &buf);

  return (i == 0);

}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

extern void jst_setParameterDescription(JstParamInfo* paramInfo, int ind, int size, char* name, JstParamClass type, short terminating) {
  assert(ind < size);
  paramInfo[ind].name = name;
  paramInfo[ind].type = type;
  paramInfo[ind].terminating = terminating;  
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

/** To be called when there is a pending exception that is the result of some
 * irrecoverable error in this startup program. Clears the exception and prints its description. */
static void clearException(JNIEnv* env) {

  (*env)->ExceptionDescribe(env);
  (*env)->ExceptionClear(env);
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

#define INCREMENT 50


/** Appends the given string to target. size param tells the current size of target (target must have been
 * dynamically allocated, i.e. not from stack). If necessary, target is reallocated into a bigger space. 
 * Return the new location of target, and modifies the size inout parameter accordingly. */
extern char* jst_append(char* target, size_t* size, const char* stringToAppend) {
  size_t targetLen, staLen, newLen, originalSize = *size;

  targetLen = strlen(target);
  staLen    = strlen(stringToAppend);
  newLen    = targetLen + staLen + 1;

  if(newLen > *size) *size = newLen + INCREMENT;
  
  if(*size != originalSize) {
    target = realloc(target, *size);
    if(!target) {
      fprintf(stderr, "error: out of memory when allocating space for strings\n");
      return NULL;
    }
  }

  return strcat(target, stringToAppend);
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

/** Appends the given entry to the jvm classpath being constructed (begins w/ "-Djava.class.path=", which the given
 * cp needs to contain before calling this func). Adds path separator
 * before the given entry, unless this is the first entry. Returns the cp buffer (which may be moved)
 * Returns 0 on error (err msg already printed). */
static char* appendCPEntry(char* cp, size_t* cpsize, const char* entry) {
  // "-Djava.class.path=" == 18 chars -> if 18th char is not a null char, we have more than that and need to append path separator
  if(cp[18]
    && !(cp = jst_append(cp, cpsize, JST_PATH_SEPARATOR)) ) return NULL;
 
  return jst_append(cp, cpsize, entry);
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

/** returns JNI_FALSE on failure. May change the target to point to a new location */
static jboolean appendJarsFromDir(char* dirName, char** target, size_t* targetSize) {

# if defined(_WIN32)

  HANDLE          fileHandle = INVALID_HANDLE_VALUE;
  WIN32_FIND_DATA fdata;
  char            *jarEntrySpecifier = NULL;
  int             lastError;
  jboolean        dirNameEndsWithSeparator, rval = JNI_FALSE;
  
  dirNameEndsWithSeparator = ( strcmp(dirName + strlen(dirName) - strlen(JST_FILE_SEPARATOR), JST_FILE_SEPARATOR) == 0 ) ? JNI_TRUE : JNI_FALSE;
  
  jarEntrySpecifier = malloc((strlen(dirName) + 15) * sizeof(char));
  if(!jarEntrySpecifier) {
    fprintf(stderr, "error: out of mem when accessing dir %s\n", dirName);
    return JNI_FALSE;
  }
  
  // this only works w/ FindFirstFileW. If need be, use that.
//  strcpy(jarEntrySpecifier, "\\\\?\\"); // to allow long paths, see documentation of FindFirstFile
  strcat(jarEntrySpecifier, dirName);
  if( !dirNameEndsWithSeparator ) strcat(jarEntrySpecifier, JST_FILE_SEPARATOR);
  strcat(jarEntrySpecifier, "*.jar");
  
  SetLastError(0);
  fileHandle = FindFirstFile(jarEntrySpecifier, &fdata);
  
  if(fileHandle == INVALID_HANDLE_VALUE) {
    fprintf(stderr, "error: opening directory %s failed w/ error code %d\n", dirName, (int)GetLastError());
    goto end;
  }
  
  lastError = GetLastError();
  if(!lastError) {
  
    do {
      // this if and the contained ||s are used so that if any of the
      // calls fail, we jump to the end
      if(    !( *target = appendCPEntry(*target, targetSize, dirName) )         
          ||  ( dirNameEndsWithSeparator ?  JNI_FALSE : !( *target = jst_append(*target, targetSize, JST_FILE_SEPARATOR) ) ) 
          || !( *target = jst_append(*target, targetSize, fdata.cFileName) )
        ) goto end;
    } while( FindNextFile(fileHandle, &fdata) );
    
  }
    
  if(!lastError) lastError = GetLastError();
  if(lastError != ERROR_NO_MORE_FILES) {
    fprintf(stderr, "error: error %d occurred when finding jars from %s\n", lastError, dirName);
    goto end;
  }
  
  rval = JNI_TRUE;
  
  end:
  if(fileHandle != INVALID_HANDLE_VALUE) FindClose(fileHandle);
  if(jarEntrySpecifier) free(jarEntrySpecifier);
  return rval;
      
# else      

  DIR           *dir;
  struct dirent *entry;
  size_t        len;
  jboolean      dirNameEndsWithSeparator, rval = JNI_FALSE;

  len = strlen(dirName);
  dirNameEndsWithSeparator = ( strcmp(dirName + len - strlen(JST_FILE_SEPARATOR), JST_FILE_SEPARATOR) == 0 );

  dir = opendir(dirName);
  if(!dir) {
    fprintf(stderr, "error: could not read directory %s to append jar files from\n", dirName);
    return JNI_FALSE;
  }

  while( (entry = readdir(dir)) ) {
    len = strlen(entry->d_name);
    if(len >= 5 && (strcmp(".jar", (entry->d_name) + len - 4) == 0)) {
      // this if and the contained ||s are used so that if any of the
      // calls fail, we jump to the end
      if(!(*target = appendCPEntry(*target, targetSize, dirName))         
      ||  (dirNameEndsWithSeparator ?  JNI_FALSE : !(*target = jst_append(*target, targetSize, JST_FILE_SEPARATOR)) ) 
      || !(*target = jst_append(*target, targetSize, entry->d_name))) goto end;
    }
  }
  rval = JNI_TRUE;

  end:
  if(!rval) {
    fprintf(stderr, "error: out of memory when adding entries from %s to classpath\n", dirName);
  }
  closedir(dir);
  return rval;

#  endif

}



// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

typedef enum { PREFIX_SEARCH, SUFFIX_SEARCH, EXACT_SEARCH } SearchMode;

/** The first param may be NULL, it is considered an empty array. */
static jboolean arrayContainsString(char** nullTerminatedArray, const char* searchString, SearchMode mode) {
  int    i = 0;
  size_t sslen, len;
  const char   *str;

  if(nullTerminatedArray) {
    switch(mode) {
      case PREFIX_SEARCH : 
        while( (str = nullTerminatedArray[i++]) ) {
          len = strlen(str);
          if(memcmp(str, searchString, len) == 0) return JNI_TRUE;
        }
        break;
      case SUFFIX_SEARCH : 
        sslen = strlen(searchString);        
        while( (str = nullTerminatedArray[i++]) ) {
          len = strlen(str);
          if(len <= sslen && memcmp(searchString + sslen - len, str, len) == 0) return JNI_TRUE;
        }
        break;
      case EXACT_SEARCH : 
        while( (str = nullTerminatedArray[i++]) ) {
          if(strcmp(str, searchString) == 0) return JNI_TRUE;
        }
        break;
    }
  }
  return JNI_FALSE;
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

/** Returns true on error. */
static jboolean addStringToJStringArray(JNIEnv* env, char *strToAdd, jobjectArray jstrArr, jint ind) {
  jboolean rval = JNI_FALSE;
  jstring  arg  = (*env)->NewStringUTF(env, strToAdd);

  if(!arg) {
    fprintf(stderr, "error: could not convert %s to java string\n", strToAdd);
    clearException(env);
    return JNI_TRUE;        
  }

  (*env)->SetObjectArrayElement(env, jstrArr, ind, arg);
  if((*env)->ExceptionCheck(env)) {
    fprintf(stderr, "error: error when writing %dth element %s to Java String[]\n", (int)ind, strToAdd);
    clearException(env);
    rval = JNI_TRUE;
  }
  (*env)->DeleteLocalRef(env, arg);
  return rval;
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

/** Info about these needs to be available to perform the parameter classification correctly. To be more precise,
 *  it is needed to find the first launchee param. */
static char* _builtinDoubleParams[] = {"-cp", "-classpath", "--classpath", "-jh", "--javahome", NULL};

extern int jst_findFirstLauncheeParamIndex(char** args, int numArgs, char** terminatingSuffixes, JstParamInfo* paramInfos, int paramInfosCount) {
  int    i, j;
  size_t len;
  
  for(i = 0; i < numArgs; i++) {
    char* arg = args[i];
    
    if((arg[0] == 0) || (arg[0] != '-') // empty strs and ones not beginning w/ - are considered to be terminating args to the launchee
     || arrayContainsString(terminatingSuffixes, arg, SUFFIX_SEARCH)) {
      return i;
    }

    for(j = 0; j < paramInfosCount; j++) {
      if(paramInfos[j].terminating) {
        switch(paramInfos[j].type) {
          case JST_SINGLE_PARAM : // deliberate fallthrough, no break
          case JST_DOUBLE_PARAM : 
            if(strcmp(paramInfos[j].name, arg) == 0) return i;
            break;
          case JST_PREFIX_PARAM :
            len = strlen(paramInfos[j].name);
            if((strlen(arg) >= len) && (memcmp(paramInfos[j].name, arg, len) == 0)) {
              return i;
            }
            break;
        } // switch
      } else if((paramInfos[j].type == JST_DOUBLE_PARAM)
        && (strcmp(paramInfos[j].name, arg) == 0) ) {
          i++;
        }
    } // for j
    // if we have one of the builtin double params, skip the value of the param
    if(arrayContainsString(_builtinDoubleParams, arg, EXACT_SEARCH)) i++;
  } // for i
  // not found - none of the params are launchee params
  return numArgs;
  
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

/** See the header file for information.
 */
extern int jst_launchJavaApp(JavaLauncherOptions *options) {
  int            rval    = -1;
  
  JavaVM         *javavm = NULL;
  JNIEnv         *env    = NULL;
  JavaDynLib     javaLib;
  jint           result, 
                 optNr   = 0;
  JavaVMInitArgs vm_args;
  JavaVMOption*  jvmOptions; 
  size_t         len;
  int            i, j, 
                 launcheeParamBeginIndex = options->numArguments; 

  jboolean     serverVMRequested        = JNI_FALSE;
  jclass       launcheeMainClassHandle  = NULL;
  jclass       strClass                 = NULL;
  jmethodID    launcheeMainMethodID     = NULL;
  jobjectArray launcheeJOptions         = NULL;
  
  char      *userClasspath = NULL, 
            *envCLASSPATH  = NULL, 
            *classpath     = NULL; 

  size_t    cpsize         = 1000; // just the initial size, expanded if necessary
  jboolean  *isLauncheeOption  = NULL;
  jint      launcheeParamCount = 0;
  char      *javaHome  = NULL, 
            *toolsJarD = NULL,
            *toolsJarFile = NULL;

  if(getenv("__JLAUNCHER_DEBUG")) _jst_debug = JNI_TRUE;
            
  javaLib.creatorFunc  = NULL;
  javaLib.dynLibHandle = NULL;  

  // calloc effectively sets all elems to JNI_FALSE.  
  if(options->numArguments) isLauncheeOption = calloc(options->numArguments, sizeof(jboolean));
  // worst case : all command line options are jvm options -> thus numArguments + 2
  // +2 as we need space for at least -Djava.class.path and possibly for -Dtools.jar
  jvmOptions = calloc( (options->numArguments) + (options->numJvmOptions) + 2, sizeof(JavaVMOption) ); 
  if((launcheeParamBeginIndex && !isLauncheeOption) || !jvmOptions) {
    fprintf(stderr, "error: out of memory at startup!\n");
    goto end;
  }

  // find out the argument index after which all the params are launchee (prg being launched) params 

  launcheeParamBeginIndex = jst_findFirstLauncheeParamIndex(options->arguments, options->numArguments, options->terminatingSuffixes, options->paramInfos, options->paramInfosCount);

  // - - - - - - - - - - - - - - - - - - - - - - - - - - - -  
  // classify the arguments as jvm or launchee params. Some are passed to neither as they are handled in this func.
  // An example is the -client / -server option that selects the type of jvm
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
      if((options->classpathHandling) & JST_CP_PARAM_TO_APP) {
        isLauncheeOption[i]     = JNI_TRUE;
        isLauncheeOption[i + 1] = JNI_TRUE;
      } 
      if((options->classpathHandling) & JST_CP_PARAM_TO_JVM) userClasspath = argument;
      i++;
      continue;
    }

    // check the param infos for params particular to the app we are launching
    for(j = 0; j < options->paramInfosCount; j++) {
      switch(options->paramInfos[j].type) {
        case JST_SINGLE_PARAM :
          if(strcmp(argument, options->paramInfos[j].name) == 0) {
            isLauncheeOption[i] = JNI_TRUE;
            goto next_arg;
          }
          break;
        case JST_DOUBLE_PARAM :
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
        case JST_PREFIX_PARAM :
          if(memcmp(argument, options->paramInfos[j].name, len) == 0) {
            isLauncheeOption[i] = JNI_TRUE;
            goto next_arg;            
          }
          break;
      } // switch
    } // for j

    if(strcmp("-server", argument) == 0) { // jvm client or server
      serverVMRequested = JNI_TRUE;
      continue;
    } else if(strcmp("-client", argument) == 0) {
      // do nothing - client jvm is the default
      continue;
    } else if( ((options->javahomeHandling) & JST_ALLOW_JH_PARAMETER) &&  
               ( (strcmp("-jh", argument) == 0)
              || (strcmp("--javahome", argument) == 0) 
               )
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
// this label is needed to be able to break out of nested for and switch (by jumping here w/ goto)
next_arg: 
   ;  // at least w/ ms compiler, the tag needs a statement after it before the closing brace. Thus, an empty statement here.
  } 

  // print debug if necessary
  if( _jst_debug && (options->numArguments != 0) ) {
    fprintf(stderr, "debug: param classication\n");
    for(i = 0; i < options->numArguments; i++) {
      fprintf(stderr, "%s\t: %s\n", options->arguments[i], (isLauncheeOption[i] || i >= launcheeParamBeginIndex) ? "launcheeparam" : "non launchee param");  
    }
  }
  
  // end classifying arguments
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - -  

  // handle java home
  // it is null if it was not given as a param
  if(!javaHome) javaHome = options->java_home;
  if(!javaHome && ((options->javahomeHandling) & JST_ALLOW_JH_ENV_VAR_LOOKUP)) javaHome = getenv("JAVA_HOME");

#if defined ( __APPLE__ )
  if ( ! javaHome || ! javaHome[0] ) { javaHome = "/System/Library/Frameworks/JavaVM.framework" ; }
#endif

  if(!javaHome || !javaHome[0]) { // not found or an empty string
    fprintf(stderr, ((options->javahomeHandling) & JST_ALLOW_JH_ENV_VAR_LOOKUP) ? "error: JAVA_HOME not set\n" : 
                                                                                  "error: java home not provided\n");
    goto end;
  }

  // - - - - - - - - - - - - - - - - - - - - - - - - - - - -  
  

  // count the params going to the launchee so we can construct the right size java String[] as param to the java main being invoked
  for(i = 0; i < options->numArguments; i++) {
    if(isLauncheeOption[i] || i >= launcheeParamBeginIndex) {
      isLauncheeOption[i] = JNI_TRUE;
      launcheeParamCount++;
    }
  }

  // - - - - - - - - - - - - - - - - - - - - - - - - - - - -  
  // construct classpath for the jvm

  // look up CLASSPATH env var if necessary  
  if( !( JST_IGNORE_GLOBAL_CP & (options->classpathHandling) ) ) { // first check if CLASSPATH is ignored altogether
    if(JST_IGNORE_GLOBAL_CP_IF_PARAM_GIVEN & (options->classpathHandling)) { // use CLASSPATH only if -cp not provided
      if(!userClasspath) envCLASSPATH = getenv("CLASSPATH");
    } else {
      envCLASSPATH = getenv("CLASSPATH");
    }
  } 
    
  if( !(classpath = malloc(cpsize)) ) {
    fprintf(stderr, "error: out of memory when allocating space for classpath\n");
    goto end;
  }

  strcpy(classpath, "-Djava.class.path=");
  
  // add the jars from the given dirs
  if(options->jarDirs) {

    char *dirName;

    for(i = 0; (dirName = options->jarDirs[i++]); ) {
      if(!appendJarsFromDir(dirName, &classpath, &cpsize)) goto end; // error msg already printed
    }
    
  }

  if(userClasspath && ((options->classpathHandling) & JST_CP_PARAM_TO_JVM)) {
    if( !( classpath = appendCPEntry(classpath, &cpsize, userClasspath) ) ) goto end;
  }

  if(envCLASSPATH && !( classpath = appendCPEntry(classpath, &cpsize, envCLASSPATH) ) ) goto end;

  // add the provided single jars
  
  if(options->jars) {
    char* jarName;
    
    for(i = 0; (jarName = options->jars[i++]); ) {
      if(!( classpath = appendCPEntry(classpath, &cpsize, jarName) ) ) goto end;
    }
    
  }

  // tools.jar handling
  toolsJarFile = malloc( strlen(javaHome) + 1 + 2 * strlen(JST_FILE_SEPARATOR) + 12 );
  if(!toolsJarFile) {
    fprintf(stderr, "error: out of memory when handling tools jar\n");
    goto end; 
  }
  strcpy(toolsJarFile, javaHome);
  strcat(toolsJarFile, JST_FILE_SEPARATOR "lib" JST_FILE_SEPARATOR "tools.jar");

  if(jst_fileExists(toolsJarFile)) { // tools.jar is not present on a jre
    // add as java env property if requested
    if((options->toolsJarHandling) & JST_TOOLS_JAR_TO_SYSPROP) {
      toolsJarD = malloc(strlen(toolsJarFile) + 12 + 1); // "-Dtools.jar=" == 12 chars + null char
      if(!toolsJarD) {
        fprintf(stderr, "error: could not allocate memory for -Dtools.jar sys prop\n");
        goto end;
      }
      strcpy(toolsJarD, "-Dtools.jar=");
      strcat(toolsJarD, toolsJarFile);

      jvmOptions[optNr].optionString = toolsJarD; 
      jvmOptions[optNr++].extraInfo = NULL;
    }
    // add tools.jar to startup classpath if requested
    if(((options->toolsJarHandling) & JST_TOOLS_JAR_TO_CLASSPATH) 
     && !( classpath = appendCPEntry(classpath, &cpsize, toolsJarFile) ) ) goto end;
  }
  
  free(toolsJarFile);
  toolsJarFile = NULL;
  
  
  jvmOptions[optNr].optionString = classpath;
  jvmOptions[optNr++].extraInfo  = NULL;

  // end constructing classpath
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - -  
  
  for(i = 0; i < options->numJvmOptions; i++) {
    jvmOptions[optNr].optionString = options->jvmOptions[i].optionString;
    jvmOptions[optNr++].extraInfo  = options->jvmOptions[i].extraInfo;
  }


  vm_args.version            = JNI_VERSION_1_4;
  vm_args.options            = jvmOptions;
  vm_args.nOptions           = optNr;
  vm_args.ignoreUnrecognized = JNI_FALSE;

  
  // fetch the pointer to jvm creator func and invoke it
  javaLib = findJVMDynamicLibrary(javaHome, serverVMRequested);
  if(!javaLib.creatorFunc) goto end; // error message already printed
  
  // the cast to void* before void** serves to remove a gcc warning
  // "dereferencing type-punned pointer will break strict-aliasing rules"
  // Found the fix from
  // http://mail.opensolaris.org/pipermail/tools-gcc/2005-August/000048.html
  result = (javaLib.creatorFunc)(&javavm, (void**)(void*)&env, &vm_args);

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
    fprintf(stderr, "error: jvm creation failed with code %d: %s\n", (int)result, errMsg);
    rval = result;
    goto end;
  } 

  free(toolsJarD);
  free(jvmOptions);
  free(classpath);
  toolsJarD  = NULL;
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
   
  if( (result = (*env)->EnsureLocalCapacity(env, launcheeParamCount + 1)) ) { // + 1 for the String[] to hold the params
    clearException(env);
    fprintf(stderr, "error: could not allocate memory for groovy parameters (how much params did you give, dude?)\n");
    rval = result;
    goto end;
  }

  if(! ( strClass = (*env)->FindClass(env, "java/lang/String") ) ) {
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

  j = 0; // index in java String[] (args to main)
  if(options->extraProgramOptions) {
    char *carg;
    i = 0;
    while( (carg = options->extraProgramOptions[i++]) ) {
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
  if(toolsJarFile)     free(toolsJarFile);
  if(toolsJarD)        free(toolsJarD);

  return rval;

}

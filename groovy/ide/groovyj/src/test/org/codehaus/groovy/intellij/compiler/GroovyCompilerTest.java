/*
 * $Id$
 *
 * Copyright (c) 2005-2006 The Codehaus - http://groovy.codehaus.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */


package org.codehaus.groovy.intellij.compiler;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.PathsList;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.intellij.EditorAPI;
import org.codehaus.groovy.intellij.GroovySupportLoader;
import org.codehaus.groovy.intellij.GroovyjTestCase;
import org.intellij.openapi.testing.MockApplicationManager;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class GroovyCompilerTest extends GroovyjTestCase {

    private final Mock mockEditorAPI = mock(EditorAPI.class);
    private final Mock mockCompilationUnitsFactory = mock(CompilationUnitsFactory.class);
    private final Mock mockFileTypeManager = mock(FileTypeManager.class);

    private final GroovyCompiler groovyCompiler = new GroovyCompiler((EditorAPI) mockEditorAPI.proxy(),
                                                                     (CompilationUnitsFactory) mockCompilationUnitsFactory.proxy());

    protected void setUp() {
        MockApplicationManager.getMockApplication().registerComponent(FileTypeManager.class, mockFileTypeManager.proxy());
    }

    protected void tearDown() {
        MockApplicationManager.getMockApplication().removeComponent(FileTypeManager.class);
    }

    public void testHasADescription() {
        assertEquals("description", "Groovy Compiler", groovyCompiler.getDescription());
    }

    public void testIsAlwaysReadyToParticipateInTheCompilationOfEitherProjectOrModule() {
        assertEquals("ready for compilation?", true, groovyCompiler.validateConfiguration(compileScope()));
    }

    public void testDeterminesThatAGivenFileIsCompilableWhenItsCorrespondingFileTypeIsTheGroovyOne() {
        VirtualFile file = new LightVirtualFile();
        mockFileTypeManager.expects(once()).method("getFileTypeByFile").with(same(file)).will(returnValue(GroovySupportLoader.GROOVY));
        assertEquals("is compilable?", true, groovyCompiler.isCompilableFile(file, compileContext()));
    }

    public void testDeterminesThatAGivenFileIsNotCompilableWhenItsCorrespondingFileTypeIsNotTheGroovyOne() {
        VirtualFile file = new LightVirtualFile(someString(), StdFileTypes.JAVA, new StringBuilder(), -1L);
        assertEquals("is compilable?", false, groovyCompiler.isCompilableFile(file, compileContext()));
    }

    public void testDoesNothingWhenThereAreNoFilesToCompile() {
        TranslatingCompiler.ExitStatus exitStatus = groovyCompiler.compile((CompileContext) newDummy(CompileContext.class), VirtualFile.EMPTY_ARRAY);
        assertExitStatusDoesNotIndicateWhichFilesCompiledSuccessfullyNorWhichFilesNeedRecompiling(exitStatus);
    }

    public void testBuildsAClassloaderLinkedToAUrlClassLoaderConfiguredToSearchTheClasspathOfACompilerConfiguration() throws MalformedURLException {
        CompilerConfiguration compilerConfiguration = aCompilerConfigurationWith(aUserDefinedClasspath());
        ClassLoader classLoader = groovyCompiler.buildClassLoaderFor(compilerConfiguration);
        URLClassLoader parentClassLoader = (URLClassLoader) classLoader.getParent();
        assertClasspathUrlsMatch(parentClassLoader.getURLs(), compilerConfiguration);
    }

    private CompilerConfiguration aCompilerConfigurationWith(String classpath) {
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setClasspath(classpath);
        return compilerConfiguration;
    }

    private String aUserDefinedClasspath() {
        return "/var/lib/acme/lib/acme.jar" + File.pathSeparator
               + "/home/foobar/.ant/lib/junit.jar" + File.pathSeparator
               + "/tmp/classes" + File.pathSeparator
               + "\\\\SHARE\\dev\\projects\\helloworld\\src\\groovy\\";
    }

    private void assertClasspathUrlsMatch(URL[] urls, CompilerConfiguration compilerConfiguration) throws MalformedURLException {
        List classpath = compilerConfiguration.getClasspath();
        assertEquals("number of classpath entries", classpath.size(), urls.length);

        for (int i = 0; i < classpath.size(); i++) {
            String classpathEntry = (String) classpath.get(i);
            assertEquals(new File(classpathEntry).toURL(), urls[i]);
        }
    }

    public void testDelegatesTheCompilationToAPairOfCompilationUnits() {
        VirtualFile[] filesToCompile = someFilesWithDifferentEncodingsToCompile();
        expectTheCreationOfCompilationUnitsForCompiling(filesToCompile);

        CompileContext compileContext = compilationContextFor(filesToCompile);
        TranslatingCompiler.ExitStatus exitStatus = groovyCompiler.compile(compileContext, filesToCompile);
        assertExitStatusDoesNotIndicateWhichFilesCompiledSuccessfullyNorWhichFilesNeedRecompiling(exitStatus);
    }

    private VirtualFile[] someFilesWithDifferentEncodingsToCompile() {
        return new VirtualFile[] {
                virtualFile().withCharset("UTF-8").withPath("/home/bar/src/main/Bar.groovy").build(),
                virtualFile().withCharset("US-ASCII").withPath("/home/foo/test/unit/FooTest.groovy").build(),
        };
    }

    private void expectTheCreationOfCompilationUnitsForCompiling(VirtualFile[] filesToCompile) {
        mockCompilationUnitsFactory.expects(once()).method("create")
                .with(isA(CompilationUnit.class), isA(CompilationUnit.class))
                .will(returnValue(compilationUnitsForCompiling(filesToCompile)));
    }

    private CompileContext compilationContextFor(VirtualFile[] filesToCompile) {
        return new CompileContextBuilder(this).linking(moduleFor(filesToCompile), filesToCompile).build();
    }

    private CompilationUnits compilationUnitsForCompiling(VirtualFile[] filesToCompile) {
        CompilationUnitsBuilder compilationUnitsBuilder = new CompilationUnitsBuilder(this);

        boolean inTestSourceFolder = false;
        for (VirtualFile file : filesToCompile) {
            compilationUnitsBuilder.expectsAddFile(file, inTestSourceFolder);
            inTestSourceFolder = !inTestSourceFolder;
        }

        return compilationUnitsBuilder.expectsCompile().build();
    }

    private CompileScope compileScope() {
        return (CompileScope) newDummy(CompileScope.class);
    }

    private CompileContext compileContext() {
        return (CompileContext) newDummy(CompileContext.class);
    }

    private Module moduleFor(VirtualFile[] files) {
        ModuleBuilder moduleBuilder = module()
                .withCompilerOutputPathUrl("build/classes")
                .withCompilerOutputPathForTestsUrl("build/test-classes")
                .withProjectJdk();

        boolean inTestSourceFolder = false;
        for (VirtualFile file : files) {
            moduleBuilder.forFileInSourceFolder(file, inTestSourceFolder);
            inTestSourceFolder = !inTestSourceFolder;
        }

        Module module = moduleBuilder.build();
        mockEditorAPI.stubs().method("getCompilationClasspath").with(same(module)).will(returnValue(aUserDefinedClasspath()));
        mockEditorAPI.stubs().method("getNonExcludedModuleSourceFolders").with(same(module)).will(returnValue(new PathsList()));
        return module;
    }

    private static class CompilationUnitsBuilder {
        private final MockObjectTestCase testCase;
        private final Mock mockCompilationUnits;

        CompilationUnitsBuilder(MockObjectTestCase testCase) {
            this.testCase = testCase;
            mockCompilationUnits = testCase.mock(CompilationUnits.class,
                                                 new Class[] { CompilationUnit.class, CompilationUnit.class },
                                                 new Object[] { null, null });
        }

        private CompilationUnitsBuilder expectsAddFile(VirtualFile file, boolean inTestSourceFolder) {
            mockCompilationUnits.expects(testCase.once()).method("add").with(testCase.same(file), testCase.eq(inTestSourceFolder));
            return this;
        }

        CompilationUnitsBuilder expectsCompile() {
            mockCompilationUnits.expects(testCase.once()).method("compile");
            return this;
        }

        CompilationUnits build() {
            return (CompilationUnits) mockCompilationUnits.proxy();
        }
    }

    private void assertExitStatusDoesNotIndicateWhichFilesCompiledSuccessfullyNorWhichFilesNeedRecompiling(TranslatingCompiler.ExitStatus exitStatus) {
        assertEquals("number of files to recompile", 0, exitStatus.getFilesToRecompile().length);
        assertEquals("number of files compiled", 0, exitStatus.getSuccessfullyCompiled().length);
    }
}

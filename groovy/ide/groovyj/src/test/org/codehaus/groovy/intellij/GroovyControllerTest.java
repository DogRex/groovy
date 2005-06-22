/*
 * $Id$
 *
 * Copyright (c) 2005 The Codehaus - http://groovy.codehaus.org
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


package org.codehaus.groovy.intellij;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleFileIndex;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.MockVirtualFile;

import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;

import org.jmock.Mock;

public class GroovyControllerTest extends GroovyjTestCase {

    private final Mock mockEditorAPI = mock(EditorAPI.class);
    private final Mock mockVirtualFile = Mocks.createVirtualFileMock(this);

    private final GroovyController groovyController = new GroovyController((EditorAPI) mockEditorAPI.proxy());

    public void testCreatesACompilationUnitConfiguredWithTheClassLoaderOfTheCurrentThreadBoundToTheGroovyClassLoader() throws IOException {
        String expectedCharsetName = "UTF-8";
        String expectedClasspathEntry = "some_lib.jar";
        String expectedTargetDirectoryPath = "/home/foobar/acme/classes";

        VirtualFile sourceFolder = new MockVirtualFile("home/foo/dev/projects/acme/src/groovy");
        Module module = createModuleForCreatingACompilerConfiguration(
                expectedCharsetName, expectedClasspathEntry, expectedTargetDirectoryPath, sourceFolder.getPath());

        CompilationUnit compilationUnit = groovyController.createCompilationUnit(module, (VirtualFile) mockVirtualFile.proxy());
        CompilerConfiguration configuration = compilationUnit.getConfiguration();

        assertEquals("source encoding", expectedCharsetName, configuration.getSourceEncoding());
        assertEquals("number of classpath items", 2, configuration.getClasspath().size());
        assertEquals("classpath item #1", expectedClasspathEntry, configuration.getClasspath().get(0));
        assertEquals("classpath item #2", sourceFolder.getPath(), configuration.getClasspath().get(1));
        assertEquals("target directory", new File(expectedTargetDirectoryPath), configuration.getTargetDirectory());
        assertSame("class loader", Thread.currentThread().getContextClassLoader(), compilationUnit.getClassLoader().getParent());
    }

    private Module createModuleForCreatingACompilerConfiguration(String charsetName, String classpath,
                                                                 String targetDirectoryPath,
                                                                 String sourceFolderFilesPath) {
        Mock stubModule = mock(Module.class, "stubModule");
        stubModule.stubs().method("getName").will(returnValue("stubbed module"));

        Module expectedModule = (Module) stubModule.proxy();
        mockVirtualFile.expects(once()).method("getCharset").will(returnValue(Charset.forName(charsetName)));
        mockEditorAPI.expects(once()).method("getCompilationClasspath").with(same(expectedModule)).will(returnValue(classpath));
        mockEditorAPI.expects(once()).method("getNonExcludedModuleSourceFolders").with(same(expectedModule))
                .will(returnValue(createPathsList(sourceFolderFilesPath)));

        Mock mockModuleRootManager = mock(ModuleRootManager.class);
        stubModule.stubs().method("getComponent").with(same(ModuleRootManager.class)).will(returnValue(mockModuleRootManager.proxy()));

        Mock stubModuleFileIndex = mock(ModuleFileIndex.class);
        mockModuleRootManager.stubs().method("getFileIndex").will(returnValue(stubModuleFileIndex.proxy()));

        stubModuleFileIndex.expects(once()).method("isInTestSourceContent").with(same(mockVirtualFile.proxy())).will(returnValue(false));
        mockModuleRootManager.expects(once()).method("getCompilerOutputPathUrl").will(returnValue("file://" + targetDirectoryPath));

        return expectedModule;
    }
}

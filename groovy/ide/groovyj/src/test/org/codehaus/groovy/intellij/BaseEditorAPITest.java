/*
 * $Id$
 *
 * Copyright (c) 2004 The Codehaus - http://groovy.codehaus.org
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
import java.util.jar.JarFile;

import junitx.framework.ArrayAssert;

import org.intellij.openapi.testing.MockApplication;
import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.openapi.command.CommandListener;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerAdapter;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiTreeChangeAdapter;
import com.intellij.psi.PsiTreeChangeListener;
import com.intellij.refactoring.listeners.RefactoringElementListenerProvider;
import com.intellij.refactoring.listeners.RefactoringListenerManager;

import org.jmock.cglib.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class BaseEditorAPITest extends MockObjectTestCase {

    private static final File[] EMPTY_CLASSPATH = new File[0];

    protected final Mock mockPsiManager = new Mock(PsiManager.class);
    protected final Mock mockFileEditorManager = new Mock(FileEditorManager.class);
    protected final Mock mockRefactoringListenerManager = new Mock(RefactoringListenerManager.class);
    protected final Mock mockProjectRootManager = new Mock(ProjectRootManager.class);
    protected final Mock mockModuleManager = new Mock(ModuleManager.class);
    protected final Mock mockModuleRootManager = new Mock(ModuleRootManager.class);
    protected final Mock mockCommandProcessor = new Mock(CommandProcessor.class);
    protected final Mock mockVirtualFileManager = new Mock(VirtualFileManager.class);
    protected final Mock mockWindowManager = new Mock(WindowManager.class);
    protected final Mock mockStatusBar = new Mock(StatusBar.class);

    protected final Mock mockLocalFileSystem = new Mock(LocalFileSystem.class);
    protected final Mock mockJarFileSystem = new Mock(JarFileSystem.class);

    protected final Mock mockProject = new Mock(Project.class);
    protected final Mock mockModule = new Mock(Module.class);
    protected final Mock mockVirtualFile = Mocks.createVirtualFileMock();

    protected final Module[] singletonModule = new Module[] { (Module) mockModule.proxy() };
    protected final VirtualFile[] moduleLibraries = new VirtualFile[] { (VirtualFile) mockVirtualFile.proxy(),
                                                                        (VirtualFile) mockVirtualFile.proxy() };

    protected BaseEditorAPI editorAPI;

    protected void setUp() throws Exception {
        super.setUp();

        editorAPI = new BaseEditorAPI((Project) mockProject.proxy()) {
            public void invokeLater(Runnable task) {}
        };

        mockProject.stubs().method("getComponent").with(eq(PsiManager.class)).will(returnValue(mockPsiManager.proxy()));
        mockProject.stubs().method("getComponent").with(eq(FileEditorManager.class)).will(returnValue(mockFileEditorManager.proxy()));
        mockProject.stubs().method("getComponent").with(eq(RefactoringListenerManager.class)).will(returnValue(mockRefactoringListenerManager.proxy()));
        mockProject.stubs().method("getComponent").with(eq(ProjectRootManager.class)).will(returnValue(mockProjectRootManager.proxy()));
        mockProject.stubs().method("getComponent").with(eq(ModuleManager.class)).will(returnValue(mockModuleManager.proxy()));
        mockModule.stubs().method("getComponent").with(eq(ModuleRootManager.class)).will(returnValue(mockModuleRootManager.proxy()));
        mockWindowManager.stubs().method("getStatusBar").will(returnValue(mockStatusBar.proxy()));

        MockApplication applicationMock = MockApplicationManager.getMockApplication();
        applicationMock.registerComponent(WindowManager.class, mockWindowManager.proxy());
        applicationMock.registerComponent(CommandProcessor.class, mockCommandProcessor.proxy());
        applicationMock.registerComponent(VirtualFileManager.class, mockVirtualFileManager.proxy());
        applicationMock.registerComponent(LocalFileSystem.class, mockLocalFileSystem.proxy());
        applicationMock.registerComponent(JarFileSystem.class, mockJarFileSystem.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();

        MockApplication applicationMock = MockApplicationManager.getMockApplication();
        applicationMock.removeComponent(PsiManager.class);
        applicationMock.removeComponent(ProjectRootManager.class);
        applicationMock.removeComponent(ModuleRootManager.class);
        applicationMock.removeComponent(FileEditorManager.class);
        applicationMock.removeComponent(RefactoringListenerManager.class);
        applicationMock.removeComponent(CommandProcessor.class);
    }

    public void testCanWriteAGivenMessageToTheStatusBar() {
        String message = "Groovy rocks!";
        mockStatusBar.expects(once()).method("setInfo").with(eq(message));
        editorAPI.writeMessageToStatusBar(message);
    }

    public void testReturnsAnEmptyArrayOfFilesIfTheClasspathOfTheCurrentProjectModuleIsEmpty() {
        mockModuleManager.expects(once()).method("getModules").will(returnValue(singletonModule));
        mockModuleRootManager.expects(once()).method("getFiles").with(eq(OrderRootType.CLASSES)).will(returnValue(VirtualFile.EMPTY_ARRAY));

        ArrayAssert.assertEquals(EMPTY_CLASSPATH, editorAPI.getCurrentModuleClasspath());
    }

    public void testReturnsAnEmptyArrayOfFilesIfTheClasspathOfTheCurrentProjectModuleOnlyContainsInvalidEntries() {
        mockModuleManager.expects(once()).method("getModules").will(returnValue(singletonModule));
        mockModuleRootManager.expects(once()).method("getFiles").with(eq(OrderRootType.CLASSES)).will(returnValue(moduleLibraries));
        mockVirtualFile.expects(atLeastOnce()).method("isValid").will(returnValue(false));

        ArrayAssert.assertEquals(EMPTY_CLASSPATH, editorAPI.getCurrentModuleClasspath());
    }

    public void testReturnsAnEmptyArrayOfFilesIfTheClasspathOfTheCurrentProjectModuleOnlyContainsAJarEntryFromAnUnsupportedFilesystem() {
        mockModuleManager.expects(once()).method("getModules").will(returnValue(singletonModule));
        mockModuleRootManager.expects(once()).method("getFiles").with(eq(OrderRootType.CLASSES))
                .will(returnValue(new VirtualFile[] { (VirtualFile) mockVirtualFile.proxy() }));

        mockVirtualFile.expects(once()).method("isValid").will(returnValue(true));
        mockVirtualFile.expects(once()).method("isDirectory").will(returnValue(false));
        mockVirtualFile.expects(once()).method("getFileSystem").will(returnValue(new Mock(VirtualFileSystem.class).proxy()));
        mockVirtualFile.expects(once()).method("getExtension").will(returnValue("jar"));

        ArrayAssert.assertEquals(EMPTY_CLASSPATH, editorAPI.getCurrentModuleClasspath());
    }

    public void testReturnsAnEmptyArrayOfFilesIfTheOnlyLibraryDefinedForTheCurrentProjectModuleCouldNotBeFound() {
        mockModuleManager.expects(once()).method("getModules").will(returnValue(singletonModule));
        mockModuleRootManager.expects(once()).method("getFiles").with(eq(OrderRootType.CLASSES))
                .will(returnValue(new VirtualFile[] { (VirtualFile) mockVirtualFile.proxy() }));

        mockVirtualFile.expects(once()).method("isValid").will(returnValue(true));
        mockVirtualFile.expects(once()).method("isDirectory").will(returnValue(false));
        mockVirtualFile.expects(once()).method("getFileSystem").will(returnValue(mockJarFileSystem.proxy()));
        mockVirtualFile.expects(once()).method("getExtension").will(returnValue("jAr"));
        mockJarFileSystem.expects(once()).method("getJarFile").with(isA(VirtualFile.class))
                .will(throwException(new IOException("module library not found")));

        ArrayAssert.assertEquals(EMPTY_CLASSPATH, editorAPI.getCurrentModuleClasspath());
    }

    public void testCanRetrieveTheClasspathOfTheCurrentProjectModuleAsAnArrayOfFiles() throws IOException {
        setExpectationsForExtractingCurrentModuleClasspath();

        File[] expectedClasspath = new File[] { new File("/usr/lib/foo"), new File("lib/compile/jdom.jar") };
        ArrayAssert.assertEquals(expectedClasspath, editorAPI.getCurrentModuleClasspath());
    }

    public void testCanRetrieveTheClasspathOfTheCurrentProjectModuleAsAString() throws IOException {
        setExpectationsForExtractingCurrentModuleClasspath();

        String expectedClasspath = "/usr/lib/foo;lib/compile/jdom.jar".replace('/', File.separatorChar);
        assertEquals(expectedClasspath, editorAPI.getCurrentModuleClasspathAsString());
    }

    private void setExpectationsForExtractingCurrentModuleClasspath() throws IOException {
        mockModuleManager.expects(once()).method("getModules").will(returnValue(singletonModule));
        mockModuleRootManager.expects(once()).method("getFiles").with(eq(OrderRootType.CLASSES)).will(returnValue(moduleLibraries));

        mockVirtualFile.expects(atLeastOnce()).method("isValid").will(returnValue(true));

        // Module library #1
        mockVirtualFile.expects(once()).method("isDirectory").will(returnValue(true))
                .id("is a directory");
        mockVirtualFile.expects(once()).method("getFileSystem")
                .after("is a directory").will(returnValue(mockLocalFileSystem.proxy()))
                .id("localFileSystem");
        mockVirtualFile.expects(once()).method("getPath")
                .after("localFileSystem").will(returnValue("/usr/lib/foo"))
                .id("first iteration");

        // Module library #2
        mockVirtualFile.expects(once()).method("isDirectory")
                .after("first iteration").will(returnValue(false))
                .id("not a directory");
        mockVirtualFile.expects(once()).method("getExtension")
                .after("not a directory").will(returnValue("JAR"))
                .id("jar extension");
        mockVirtualFile.expects(once()).method("getFileSystem")
                .after("jar extension").will(returnValue(mockJarFileSystem.proxy()))
                .id("JAR filesystem");
        mockJarFileSystem.expects(once()).method("getJarFile").with(isA(VirtualFile.class))
                .after(mockVirtualFile, "JAR filesystem").will(returnValue(new JarFile(new File("lib/compile/jdom.jar"))))
                .id("second iteration");
    }

    public void testHandlesTheRegistrationOfFileEditorManagerListeners() {
        FileEditorManagerListener listener = new FileEditorManagerAdapter() {};

        mockFileEditorManager.expects(once()).method("addFileEditorManagerListener").with(eq(listener));
        editorAPI.addFileEditorManagerListener(listener);

        mockFileEditorManager.expects(once()).method("removeFileEditorManagerListener").with(eq(listener));
        editorAPI.removeFileEditorManagerListener(listener);
    }

    public void testHandlesTheRegistrationOfRefactoringListenerProviders() {
        RefactoringElementListenerProvider listenerProvider =
                (RefactoringElementListenerProvider) new Mock(RefactoringElementListenerProvider.class).proxy();

        mockRefactoringListenerManager.expects(once()).method("addListenerProvider").with(eq(listenerProvider));
        editorAPI.addRefactoringElementListenerProvider(listenerProvider);

        mockRefactoringListenerManager.expects(once()).method("removeListenerProvider").with(eq(listenerProvider));
        editorAPI.removeRefactoringElementListenerProvider(listenerProvider);
    }

    public void testHandlesTheRegistrationOfPsiTreeChangeListeners() {
        PsiTreeChangeListener listener = new PsiTreeChangeAdapter() {};

        mockPsiManager.expects(once()).method("addPsiTreeChangeListener").with(eq(listener));
        editorAPI.addPsiTreeChangeListener(listener);

        mockPsiManager.expects(once()).method("removePsiTreeChangeListener").with(eq(listener));
        editorAPI.removePsiTreeChangeListener(listener);
    }

    public void testHandlesTheRegistrationOfCommandListeners() {
        CommandListener listener = (CommandListener) new Mock(CommandListener.class).proxy();

        mockCommandProcessor.expects(once()).method("addCommandListener").with(eq(listener));
        editorAPI.addCommandListener(listener);

        mockCommandProcessor.expects(once()).method("removeCommandListener").with(eq(listener));
        editorAPI.removeCommandListener(listener);
    }

    public void testHandlesTheRegistrationOfVirtualFileListeners() {
        VirtualFileListener listener = (VirtualFileListener) new Mock(VirtualFileListener.class).proxy();

        mockVirtualFileManager.expects(once()).method("addVirtualFileListener").with(eq(listener));
        editorAPI.addVirtualFileListener(listener);

        mockVirtualFileManager.expects(once()).method("removeVirtualFileListener").with(eq(listener));
        editorAPI.removeVirtualFileListener(listener);
    }
}

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

import java.util.List;

import org.intellij.openapi.testing.MockApplication;
import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.openapi.command.CommandListener;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerAdapter;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.SourceFolder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiTreeChangeAdapter;
import com.intellij.psi.PsiTreeChangeListener;
import com.intellij.refactoring.listeners.RefactoringElementListenerProvider;
import com.intellij.refactoring.listeners.RefactoringListenerManager;
import com.intellij.testFramework.MockVirtualFile;
import com.intellij.util.PathUtil;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public abstract class EditorApiTestCase extends MockObjectTestCase {

    protected final Mock mockProject = mock(Project.class);

    private final Mock stubModule = mock(Module.class);
    protected final Module stubbedModule = (Module) stubModule.proxy();
    protected final Mock mockModuleRootManager = mock(ModuleRootManager.class);

    private final Mock mockPsiManager = mock(PsiManager.class);
    private final Mock mockFileEditorManager = mock(FileEditorManager.class);
    private final Mock mockRefactoringListenerManager = mock(RefactoringListenerManager.class);
    private final Mock mockCommandProcessor = mock(CommandProcessor.class);
    private final Mock mockVirtualFileManager = mock(VirtualFileManager.class);
    private final Mock mockWindowManager = mock(WindowManager.class);
    private final Mock mockStatusBar = mock(StatusBar.class);

    protected EditorAPI editorApi;

    protected void setUp() {
        mockProject.stubs().method("getComponent").with(eq(PsiManager.class)).will(returnValue(mockPsiManager.proxy()));
        mockProject.stubs().method("getComponent").with(eq(FileEditorManager.class)).will(returnValue(mockFileEditorManager.proxy()));
        mockProject.stubs().method("getComponent").with(eq(RefactoringListenerManager.class)).will(returnValue(mockRefactoringListenerManager.proxy()));
        stubModule.stubs().method("getComponent").with(eq(ModuleRootManager.class)).will(returnValue(mockModuleRootManager.proxy()));
        mockWindowManager.stubs().method("getStatusBar").will(returnValue(mockStatusBar.proxy()));

        MockApplication applicationMock = MockApplicationManager.getMockApplication();
        applicationMock.registerComponent(CommandProcessor.class, mockCommandProcessor.proxy());
        applicationMock.registerComponent(VirtualFileManager.class, mockVirtualFileManager.proxy());
        applicationMock.registerComponent(WindowManager.class, mockWindowManager.proxy());
    }

    protected void tearDown() {
        MockApplication applicationMock = MockApplicationManager.getMockApplication();
        applicationMock.removeComponent(CommandProcessor.class);
        applicationMock.removeComponent(VirtualFileManager.class);
        applicationMock.removeComponent(WindowManager.class);
    }

    public void testReturnsAnEmptyArrayOfModulesAsTheDependentModulesOfAGivenNullModule() {
        Module[] moduleAndDependentModules = editorApi.getModuleAndDependentModules(null);
        assertSame("module and dependent modules", Module.EMPTY_ARRAY, moduleAndDependentModules);
    }

    public void testReturnsAnArrayContainingJustAGivenModuleWhenItHasNoDependentModules() {
        mockModuleRootManager.expects(once()).method("getDependencies").will(returnValue(Module.EMPTY_ARRAY));

        Module[] moduleAndDependentModules = editorApi.getModuleAndDependentModules(stubbedModule);
        assertEquals("number of modules", 1, moduleAndDependentModules.length);
        assertSame("module", stubbedModule, moduleAndDependentModules[0]);
    }

    public void testReturnsAnArrayOfModulesMadeOfTheModuleItselfFollowedByAllOfItsDependentModules() {
        Module[] moduleDependencies = new Module[] {
            (Module) mock(Module.class, "mockModuleDependency1").proxy(),
            (Module) mock(Module.class, "mockModuleDependency2").proxy(),
        };
        mockModuleRootManager.expects(once()).method("getDependencies").will(returnValue(moduleDependencies));

        Module[] moduleAndDependentModules = editorApi.getModuleAndDependentModules(stubbedModule);
        assertEquals("number of modules", moduleDependencies.length + 1, moduleAndDependentModules.length);
        assertSame("module", stubbedModule, moduleAndDependentModules[0]);

        for (int i = 0; i < moduleDependencies.length; i++) {
            int destinationIndex = i + 1;
            assertSame("module dependency #" + destinationIndex, moduleDependencies[i], moduleAndDependentModules[destinationIndex]);
        }
    }

    public void testReturnsAllNonExcludedSourceFoldersForAGivenModule() {
        Mock stubWritableSourceFolder = mock(SourceFolder.class, "stubWritableSourceFolder");
        Mock stubReadOnlySourceFolder = mock(SourceFolder.class, "stubReadOnlySourceFolder");
        Mock stubWritableSourceFile = mock(SourceFolder.class, "stubWritableSourceFile");
        Mock stubExcludedSourceFolder = mock(SourceFolder.class, "stubExcludedSourceFolder");

        Mock stubContentEntry = mock(ContentEntry.class, "stubContentEntry");
        VirtualFile excludedFolderFile = new VirtualFileStub(true, true);
        stubContentEntry.stubs().method("getExcludeFolderFiles").will(returnValue(new VirtualFile[] { excludedFolderFile }));

        VirtualFile expectedSourceFolder = new VirtualFileStub(true, true);
        stubWritableSourceFolder.stubs().method("getFile").will(returnValue(expectedSourceFolder));
        stubReadOnlySourceFolder.stubs().method("getFile").will(returnValue(new VirtualFileStub(true, false)));
        stubWritableSourceFile.stubs().method("getFile").will(returnValue(new VirtualFileStub(false, true)));
        stubExcludedSourceFolder.stubs().method("getFile").will(returnValue(excludedFolderFile));

        stubContentEntry.stubs().method("getSourceFolders").will(returnValue(new SourceFolder[] {
            (SourceFolder) stubWritableSourceFolder.proxy(),
            (SourceFolder) stubReadOnlySourceFolder.proxy(),
            (SourceFolder) stubWritableSourceFile.proxy(),
            (SourceFolder) stubExcludedSourceFolder.proxy(),
        }));

        ContentEntry[] allContentEntries = new ContentEntry[] { (ContentEntry) stubContentEntry.proxy() };
        mockModuleRootManager.expects(once()).method("getContentEntries").will(returnValue(allContentEntries));

        List<String> allSourceFolders = editorApi.getNonExcludedModuleSourceFolders(stubbedModule).getPathList();
        assertEquals("number of source folders", 1, allSourceFolders.size());
        assertEquals(PathUtil.getLocalPath(expectedSourceFolder), allSourceFolders.get(0));
    }

    private static class VirtualFileStub extends MockVirtualFile {
        private final boolean directory;
        private final boolean writable;

        public VirtualFileStub(boolean directory, boolean writable) {
            this.directory = directory;
            this.writable = writable;
        }

        public boolean isDirectory() {
            return directory;
        }

        public boolean isWritable() {
            return writable;
        }
    }

    public void testWritesAGivenMessageToTheStatusBar() {
        String message = "Groovy rocks!";
        mockStatusBar.expects(once()).method("setInfo").with(eq(message));
        editorApi.writeMessageToStatusBar(message);
    }

    public void testHandlesTheRegistrationOfFileEditorManagerListeners() {
        FileEditorManagerListener listener = new FileEditorManagerAdapter() {};

        mockFileEditorManager.expects(once()).method("addFileEditorManagerListener").with(eq(listener));
        editorApi.addFileEditorManagerListener(listener);

        mockFileEditorManager.expects(once()).method("removeFileEditorManagerListener").with(eq(listener));
        editorApi.removeFileEditorManagerListener(listener);
    }

    public void testHandlesTheRegistrationOfRefactoringListenerProviders() {
        RefactoringElementListenerProvider listenerProvider =
                (RefactoringElementListenerProvider) mock(RefactoringElementListenerProvider.class).proxy();

        mockRefactoringListenerManager.expects(once()).method("addListenerProvider").with(eq(listenerProvider));
        editorApi.addRefactoringElementListenerProvider(listenerProvider);

        mockRefactoringListenerManager.expects(once()).method("removeListenerProvider").with(eq(listenerProvider));
        editorApi.removeRefactoringElementListenerProvider(listenerProvider);
    }

    public void testHandlesTheRegistrationOfPsiTreeChangeListeners() {
        PsiTreeChangeListener listener = new PsiTreeChangeAdapter() {};

        mockPsiManager.expects(once()).method("addPsiTreeChangeListener").with(eq(listener));
        editorApi.addPsiTreeChangeListener(listener);

        mockPsiManager.expects(once()).method("removePsiTreeChangeListener").with(eq(listener));
        editorApi.removePsiTreeChangeListener(listener);
    }

    public void testHandlesTheRegistrationOfCommandListeners() {
        CommandListener listener = (CommandListener) mock(CommandListener.class).proxy();

        mockCommandProcessor.expects(once()).method("addCommandListener").with(eq(listener));
        editorApi.addCommandListener(listener);

        mockCommandProcessor.expects(once()).method("removeCommandListener").with(eq(listener));
        editorApi.removeCommandListener(listener);
    }

    public void testHandlesTheRegistrationOfVirtualFileListeners() {
        VirtualFileListener listener = (VirtualFileListener) mock(VirtualFileListener.class).proxy();

        mockVirtualFileManager.expects(once()).method("addVirtualFileListener").with(eq(listener));
        editorApi.addVirtualFileListener(listener);

        mockVirtualFileManager.expects(once()).method("removeVirtualFileListener").with(eq(listener));
        editorApi.removeVirtualFileListener(listener);
    }
}

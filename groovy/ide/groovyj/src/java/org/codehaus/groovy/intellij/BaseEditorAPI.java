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
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import com.intellij.openapi.command.CommandListener;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiTreeChangeListener;
import com.intellij.refactoring.listeners.RefactoringElementListenerProvider;
import com.intellij.refactoring.listeners.RefactoringListenerManager;

public abstract class BaseEditorAPI implements EditorAPI {

    private final Logger logger = Logger.getInstance(getClass().getName());

    protected final Project project;

    public BaseEditorAPI(Project project) {
        this.project = project;
    }

    public String getCurrentModuleClasspathAsString() {
        StringBuffer buffer = new StringBuffer();
        File[] moduleClasspath = getCurrentModuleClasspath();
        for (int i = 0; i < moduleClasspath.length; i++) {
            buffer.append(moduleClasspath[i].getPath());
            if (i < (moduleClasspath.length - 1)) {
                buffer.append(File.pathSeparatorChar);
            }
        }
        return buffer.toString();
    }

    public File[] getCurrentModuleClasspath() {
        List paths = new ArrayList();
        VirtualFile[] files = getModuleRootManager().getFiles(OrderRootType.CLASSES);

        for (int i = 0; i < files.length; i++) {
            VirtualFile classpathEntry = files[i];

            if (isValidClasspathEntry(classpathEntry)) {
                VirtualFileSystem fileSystem = classpathEntry.getFileSystem();

                if (fileSystem instanceof LocalFileSystem) {
                    paths.add(new File(classpathEntry.getPath().replace('/', File.separatorChar)));
                } else if (fileSystem instanceof JarFileSystem) {
                    try {
                        ZipFile jarFile = ((JarFileSystem) fileSystem).getJarFile(classpathEntry);
                        paths.add(new File(jarFile.getName()));
                    } catch (IOException e) {
                        logger.info("Error getting JAR file for virtual file: " + e);
                    }
                }
            }
        }

        return (File[]) paths.toArray(new File[paths.size()]);
    }

    private boolean isValidClasspathEntry(VirtualFile file) {
        return (file.isValid()) && (file.isDirectory() || file.getExtension().equalsIgnoreCase("jar"));
    }

    public void writeMessageToStatusBar(String message) {
        WindowManager.getInstance().getStatusBar(project).setInfo(message);
    }

    public void addFileEditorManagerListener(FileEditorManagerListener listener) {
        getFileEditorManager().addFileEditorManagerListener(listener);
    }

    public void removeFileEditorManagerListener(FileEditorManagerListener listener) {
        getFileEditorManager().removeFileEditorManagerListener(listener);
    }

    public void addRefactoringElementListenerProvider(RefactoringElementListenerProvider listener) {
        getRefactoringListenerManager().addListenerProvider(listener);
    }

    public void removeRefactoringElementListenerProvider(RefactoringElementListenerProvider listener) {
        getRefactoringListenerManager().removeListenerProvider(listener);
    }

    public void addPsiTreeChangeListener(PsiTreeChangeListener listener) {
        getPsiManager().addPsiTreeChangeListener(listener);
    }

    public void removePsiTreeChangeListener(PsiTreeChangeListener listener) {
        getPsiManager().removePsiTreeChangeListener(listener);
    }

    public void addCommandListener(CommandListener listener) {
        getCommandProcessor().addCommandListener(listener);
    }

    public void removeCommandListener(CommandListener listener) {
        getCommandProcessor().removeCommandListener(listener);
    }

    public void addVirtualFileListener(VirtualFileListener listener) {
        getVirtualFileManager().addVirtualFileListener(listener);
    }

    public void removeVirtualFileListener(VirtualFileListener listener) {
        getVirtualFileManager().removeVirtualFileListener(listener);
    }

    protected PsiManager getPsiManager() {
        return PsiManager.getInstance(project);
    }

    private FileEditorManager getFileEditorManager() {
        return FileEditorManager.getInstance(project);
    }

    private RefactoringListenerManager getRefactoringListenerManager() {
        return RefactoringListenerManager.getInstance(project);
    }

    private CommandProcessor getCommandProcessor() {
        return CommandProcessor.getInstance();
    }

    private VirtualFileManager getVirtualFileManager() {
        return VirtualFileManager.getInstance();
    }

    private ModuleRootManager getModuleRootManager() {
        return ModuleRootManager.getInstance(getModuleManager().getModules()[0]);
    }

    private ModuleManager getModuleManager() {
        return ModuleManager.getInstance(project);
    }
}

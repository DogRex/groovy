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

import com.intellij.openapi.command.CommandListener;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiTreeChangeListener;
import com.intellij.refactoring.listeners.RefactoringElementListenerProvider;

public interface EditorAPI {

    File[] getCurrentModuleClasspath();

    String getCurrentModuleClasspathAsString();

    void invokeLater(Runnable task);

    void writeMessageToStatusBar(String message);

    void addFileEditorManagerListener(FileEditorManagerListener listener);

    void removeFileEditorManagerListener(FileEditorManagerListener listener);

    void addRefactoringElementListenerProvider(RefactoringElementListenerProvider listener);

    void removeRefactoringElementListenerProvider(RefactoringElementListenerProvider listener);

    void addPsiTreeChangeListener(PsiTreeChangeListener listener);

    void removePsiTreeChangeListener(PsiTreeChangeListener listener);

    void addCommandListener(CommandListener listener);

    void removeCommandListener(CommandListener listener);

    void addVirtualFileListener(VirtualFileListener listener);

    void removeVirtualFileListener(VirtualFileListener listener);
}

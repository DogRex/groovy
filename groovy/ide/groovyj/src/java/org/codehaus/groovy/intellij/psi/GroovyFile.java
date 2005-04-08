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


package org.codehaus.groovy.intellij.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import org.codehaus.groovy.intellij.GroovySupportLoader;
import org.codehaus.groovy.intellij.language.GroovyLanguage;

public class GroovyFile extends PsiFileBase implements GroovyElement {

    public GroovyFile(Project project, VirtualFile file) {
        super(project, file, GroovyLanguage.findOrCreate());
    }

    public GroovyFile(Project project, String name, CharSequence text) {
        super(project, name, text, GroovyLanguage.findOrCreate());
    }

    public FileType getFileType() {
        return GroovySupportLoader.GROOVY;
    }

    public String toString() {
        return "[GroovyFile '" + getContainingFile().getVirtualFile().getPath() + "']";
    }

    // what is this for again? remove if possible...
/*
    public boolean processDeclarations(PsiScopeProcessor processor, PsiSubstitutor substitutor, PsiElement lastParent, PsiElement place) {
        PsiElement[] children = getChildren();
        for (int i = 0; i < children.length; i++) {
            PsiElement child = children[i];
            if (!child.processDeclarations(processor, substitutor, lastParent, place)) {
                return false;
            }
        }
        return true;
    }
*/
}

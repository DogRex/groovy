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


package org.codehaus.groovy.intellij.language.editor;

import com.intellij.codeFormatting.PseudoText;
import com.intellij.codeFormatting.PseudoTextBuilder;
import com.intellij.codeFormatting.TreeBasedPseudoText;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;

public class GroovyPseudoTextBuilder implements PseudoTextBuilder {

    public PseudoText build(Project project, CodeStyleSettings settings, PsiElement source) {
        ASTNode rootNode = source.getContainingFile().getNode();
        TreeBasedPseudoText pseudoText = new TreeBasedPseudoText(rootNode);
//        new GroovySpaceProcessor(pseudoText, settings).process(rootNode);
//        new GroovyAlignmentProcessor(pseudoText, settings).process(rootNode);
        return pseudoText;
    }
}

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


package org.codehaus.groovy.intellij.language;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import org.codehaus.groovy.intellij.psi.GroovyElementTypes;
import org.codehaus.groovy.intellij.psi.GroovyFile;
import org.codehaus.groovy.intellij.psi.GroovyTokenSets;

public class GroovyParserDefinition implements ParserDefinition {

    private final GroovyLanguageToolsFactory languageToolsFactory;

    public GroovyParserDefinition(GroovyLanguageToolsFactory languageToolsFactory) {
        this.languageToolsFactory = languageToolsFactory;
    }

    public Lexer createLexer(Project project) {
        return languageToolsFactory.createLexer();
    }

    public PsiParser createParser(Project project) {
        return languageToolsFactory.createParser();
    }

    public IElementType getFileNodeType() {
        return GroovyElementTypes.FILE;
    }

    public TokenSet getWhitespaceTokens() {
        return GroovyTokenSets.WHITESPACES;
    }

    public TokenSet getCommentTokens() {
        return GroovyTokenSets.COMMENTS;
    }

    public PsiElement createElement(ASTNode node) {
        return new ASTWrapperPsiElement(node);
    }

    public PsiFile createFile(Project project, VirtualFile file) {
        return new GroovyFile(project, file);
    }

    public PsiFile createFile(Project project, String name, CharSequence text) {
        return new GroovyFile(project, name, text);
    }
}

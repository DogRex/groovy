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


package org.codehaus.groovy.intellij.psi;

import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.psi.tree.IElementType;

import org.jetbrains.annotations.NotNull;

import org.codehaus.groovy.intellij.GroovySupportLoader;
import org.codehaus.groovy.intellij.language.GroovyLanguage;
import org.codehaus.groovy.intellij.language.GroovyPsiBuilder;

public class GroovyFile extends PsiFileImpl implements GroovyElement {

    private static final Logger LOGGER = Logger.getInstance("#org.codehaus.groovy.intellij.psi.GroovyFile");

    private static final IElementType FILE_TEXT_CHAMELEON = new IElementType("FILE_TEXT_CHAMELEON", null);

    private static final Language LANGUAGE = GroovyLanguage.findOrCreate();

    public GroovyFile(FileViewProvider fileViewProvider, Language language) {
        super(language.getParserDefinition().getFileNodeType(), FILE_TEXT_CHAMELEON, fileViewProvider);
    }

    @NotNull
    public FileType getFileType() {
        return GroovySupportLoader.GROOVY;
    }

    public String toString() {
        return "[GroovyFile '" + getContainingFile().getVirtualFile().getPath() + "']";
    }

    @NotNull
    public Language getLanguage() {
        return LANGUAGE;
    }

    public Lexer createLexer() {
        return LANGUAGE.getParserDefinition().createLexer(getProject());
    }

    protected final FileElement createFileElement(CharSequence text) {
        return createFileElement(getProject(), text);
    }

    private static FileElement createFileElement(Project project, CharSequence text) {
        ParserDefinition parserDefinition = LANGUAGE.getParserDefinition();
        IElementType root = parserDefinition.getFileNodeType();
        GroovyPsiBuilder builder = new GroovyPsiBuilder(LANGUAGE, project, null, text);
        FileElement fileElement = (FileElement) parserDefinition.createParser(project).parse(root, builder);
        LOGGER.assertTrue(fileElement.getElementType() == root, "Parsing file text returns rootElement with type different from declared in parser definition");
        return fileElement;
    }

    public void accept(PsiElementVisitor psiElementVisitor) {
        psiElementVisitor.visitFile(this);
    }
}

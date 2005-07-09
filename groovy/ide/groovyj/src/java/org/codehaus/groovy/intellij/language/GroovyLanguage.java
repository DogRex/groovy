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

import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.ide.highlighter.JavaFileHighlighter;
import com.intellij.lang.Commenter;
import com.intellij.lang.Language;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.tree.IElementType;

import org.jetbrains.annotations.NotNull;

import org.picocontainer.MutablePicoContainer;

import org.codehaus.groovy.intellij.language.editor.GroovyFoldingBuilder;
import org.codehaus.groovy.intellij.language.parser.GroovyTokenTypes;
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

public class GroovyLanguage extends Language {

    private static final String ID = "Groovy";

    public static GroovyLanguage findOrCreate() {
        GroovyLanguage language = Language.findInstance(GroovyLanguage.class);
        return (language == null) ? new GroovyLanguage() : language;
    }

    private final MutablePicoContainer picoContainer;

    private GroovyLanguage() {
        super(GroovyLanguage.ID);

        picoContainer = (MutablePicoContainer) ApplicationManager.getApplication().getPicoContainer();
        picoContainer.unregisterComponent(GroovyLanguageToolsFactory.class);
        picoContainer.registerComponentImplementation(GroovyLanguageToolsFactory.class);
        picoContainer.registerComponentImplementation(GroovyParserDefinition.class);
    }

    public ParserDefinition getParserDefinition() {
        Object languageToolsFactory = picoContainer.getComponentInstance(GroovyLanguageToolsFactory.class);
        return new GroovyParserDefinition((GroovyLanguageToolsFactory) languageToolsFactory);
    }

    @NotNull
    public SyntaxHighlighter getSyntaxHighlighter(Project project) {
        // TODO: restore once GroovyFileHighlighter is functionally usable
//        return new GroovyFileHighlighter();
        return new JavaFileHighlighter(LanguageLevel.HIGHEST);
    }

    public WordsScanner getWordsScanner() {
        return new GroovyWordsScanner(getParserDefinition().createLexer(null));
    }

    public boolean mayHaveReferences(IElementType token) {
        return token == GroovyTokenTypeMappings.getType(GroovyTokenTypes.IDENT);
    }

    public FoldingBuilder getFoldingBuilder() {
        return new GroovyFoldingBuilder();
    }

    public FormattingModelBuilder getFormattingModelBuilder() {
/*
        return new FormattingModelBuilder() {
            public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
                return new GroovyFormattingModel(element.getContainingFile(), settings, new GroovyBlock(element.getNode(), null, null, null, settings));
            }
        };
*/
        return null;
    }

    public PairedBraceMatcher getPairedBraceMatcher() {
        return new GroovyPairedBraceMatcher();
    }

    public Annotator getAnnotator() {
        return null;
    }

    public Commenter getCommenter() {
        return new GroovyCommenter();
    }
}

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

import com.intellij.codeFormatting.PseudoTextBuilder;
import com.intellij.lang.Language;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;

public class GroovyLanguage extends Language {

    public static final String ID = "Groovy";

    public static GroovyLanguage createLanguage() {
        GroovyLanguage language = (GroovyLanguage) Language.findByID(GroovyLanguage.ID);
        return (language == null) ? new GroovyLanguage() : language;
    }

    private GroovyLanguage() {
        super(ID);
    }

    public ParserDefinition getParserDefinition() {
        return null;
    }

    public SyntaxHighlighter getSyntaxHighlighter(Project project) {
        return null;
    }

    public WordsScanner getWordsScanner() {
        return null;
    }

    public boolean mayHaveReferences(IElementType token) {
        return false;
    }

    public FoldingBuilder getFoldingBuilder() {
        return null;
    }

    public PseudoTextBuilder getFormatter() {
        return null;
    }

    public PairedBraceMatcher getPairedBraceMatcher() {
        return null;
    }

    public Annotator getAnnotator() {
        return null;
    }
}

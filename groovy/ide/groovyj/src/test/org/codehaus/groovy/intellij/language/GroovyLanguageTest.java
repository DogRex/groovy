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

import junit.framework.TestCase;
import junitx.framework.Assert;
import junitx.framework.ObjectAssert;

import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.codeFormatting.PseudoTextBuilder;
import com.intellij.lang.Commenter;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.psi.tree.IElementType;

import org.codehaus.groovy.antlr.parser.GroovyTokenTypes;

import org.codehaus.groovy.intellij.Stubs;
import org.codehaus.groovy.intellij.language.editor.GroovyFileHighlighter;
import org.codehaus.groovy.intellij.language.editor.GroovyFoldingBuilder;
import org.codehaus.groovy.intellij.language.editor.GroovyPseudoTextBuilder;
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

public class GroovyLanguageTest extends TestCase {

    static {
        MockApplicationManager.reset();
    }

    private final GroovyLanguage language = GroovyLanguage.findOrCreate();

    public void testHasItsIdSetToGroovy() {
        assertEquals("Language: Groovy", language.toString());
    }

    public void testCreatesAParserDefinitionForGroovy() {
        ParserDefinition parserDefinition = language.getParserDefinition();
        Assert.assertNotEquals("parser definition", null, parserDefinition);
        ObjectAssert.assertInstanceOf("parser definition", GroovyParserDefinition.class, parserDefinition);
    }

    public void testProducesAProjectSpecificSyntaxHighlighterForGroovy() {
        StdFileTypes.JAVA = Stubs.LANGUAGE_FILE_TYPE;
        StdFileTypes.XML = Stubs.LANGUAGE_FILE_TYPE;

        SyntaxHighlighter syntaxHighlighter = language.getSyntaxHighlighter(null);
        Assert.assertNotEquals("project syntax highlighter", null, syntaxHighlighter);
        ObjectAssert.assertInstanceOf("project syntax highlighter", GroovyFileHighlighter.class, syntaxHighlighter);
    }

    public void testProducesAWordScannerForGroovy() {
        WordsScanner wordsScanner = language.getWordsScanner();
        Assert.assertNotEquals("word scanner", null, wordsScanner);
        ObjectAssert.assertInstanceOf("word scanner", GroovyWordsScanner.class, wordsScanner);
    }

    public void testDefinesThatGroovyIdentifiersMayHaveReferences() {
        IElementType groovyIdentifierElementType = GroovyTokenTypeMappings.getType(GroovyTokenTypes.IDENT);
        assertTrue("groovy identifiers may have references", language.mayHaveReferences(groovyIdentifierElementType));
    }

    public void testDefinesThatTokensThatDoNotRepresentGroovyIdentifiersMayNotHaveReferences() {
        IElementType notAGroovyIdentifierElementType = new IElementType(null, null);
        assertFalse("tokens that are not groovy identifiers may not have references", language.mayHaveReferences(notAGroovyIdentifierElementType));
    }

    public void testProducesACodeFoldingBuilderForGroovy() {
        FoldingBuilder foldingBuilder = language.getFoldingBuilder();
        Assert.assertNotEquals("folding builder", null, foldingBuilder);
        ObjectAssert.assertInstanceOf("folding builder", GroovyFoldingBuilder.class, foldingBuilder);
    }

    public void testProducesAFormatterForGroovy() {
        PseudoTextBuilder formatter = language.getFormatter();
        Assert.assertNotEquals("formatter", null, formatter);
        ObjectAssert.assertInstanceOf("formatter", GroovyPseudoTextBuilder.class, formatter);
    }

    public void testProducesAPairedBraceMatcherForGroovy() {
        PairedBraceMatcher pairedBraceMatcher = language.getPairedBraceMatcher();
        Assert.assertNotEquals("paired brace matcher", null, pairedBraceMatcher);
        ObjectAssert.assertInstanceOf("paired brace matcher", GroovyPairedBraceMatcher.class, pairedBraceMatcher);
    }

    public void testDoesNotProduceAnnotatorsYet() {
        assertEquals("annotator", null, language.getAnnotator());
    }

    public void testProducesACommenterForGroovy() {
        Commenter commenter = language.getCommenter();
        Assert.assertNotEquals("commenter", null, commenter);
        ObjectAssert.assertInstanceOf("groovy has its own commenter", GroovyCommenter.class, commenter);
    }
}

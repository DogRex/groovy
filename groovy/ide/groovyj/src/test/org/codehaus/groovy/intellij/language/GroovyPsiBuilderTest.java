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
import junitx.framework.ObjectAssert;

import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

import org.codehaus.groovy.antlr.parser.GroovyTokenTypes;

import org.codehaus.groovy.intellij.psi.GroovyElementTypes;
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

import antlr.TokenStreamException;

public class GroovyPsiBuilderTest extends TestCase {

    private GroovyPsiBuilder builder;

    private void startLexing(String input) {
        MockApplicationManager.reset();
        builder = new GroovyPsiBuilder(GroovyLanguage.findOrCreate(), null, null, input);
        builder.startLexer();
    }

    public void testReadsAnEmptyStringAndTerminatesTheLexicalAnalysis() {
        startLexing("");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAScriptHeaderCommentOnly() {
        startLexing("#!");
        assertEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingASingleLineCommentOnly() {
        startLexing("//");
        assertEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingTheOpeningOfAMultiLineCommentOnly() {
        try {
            startLexing("/*");                                   // TODO: report that this used to result in an OutOfMemoryError
            fail("Bad grammar - ANTLR lexer should have choked on EOF!");
        } catch (RuntimeException e) {
            ObjectAssert.assertInstanceOf("exception class", TokenStreamException.class, e.getCause());
            assertEquals("message", "line 1:3: expecting '*', found '<EOF>'", e.getMessage());
        }
    }

    public void testReadsTextContainingASingleTokenRepresentingAWhitespace() {
        startLexing(" ");
        assertEndOfFile();
    }

    public void testReadsTextContainingTwoNewLineTokens() {
        startLexing("\n\n");
        assertEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingATabCharacter() {
        startLexing("\t");
        assertEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAKeyword() {
        startLexing("def");
        assertTokenAttributes(GroovyTokenTypes.LITERAL_def, 0, "def");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAStringLiteralDelimitedBySingleQuotes() {
        String expectedString = "'some stuff'";
        startLexing(expectedString);
        assertTokenAttributes(GroovyTokenTypes.STRING_LITERAL, 0, expectedString);
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAStringLiteralDelimitedByQuotes() {
        String expectedString = "\"some stuff\"";
        startLexing(expectedString);
        assertTokenAttributes(GroovyTokenTypes.STRING_LITERAL, 0, expectedString);
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAPrimitiveDouble() {
        startLexing("0.1234D");
        assertTokenAttributes(GroovyTokenTypes.NUM_DOUBLE, 0, "0.1234D");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsThePositionOfAnIdentifierTokenConsecutiveToATabCharacter() {
        startLexing("\tfoo");
        assertTokenAttributes(GroovyTokenTypes.IDENT, 1, "foo");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleLineComment() {
        startLexing("//foo\nfoo");
        assertTokenAttributes(GroovyTokenTypes.IDENT, 6, "foo");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingAMultiLineComment() {
        startLexing("/*foo\nfoo*/foo");
        assertTokenAttributes(GroovyTokenTypes.IDENT, 11, "foo");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingVariousBlanks() {
        startLexing(" def\n ( ");
        assertTokenAttributes(GroovyTokenTypes.LITERAL_def, 1, "def");
        assertNextTokenAttributes(GroovyTokenTypes.LPAREN, 6, "(");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingAMethodDefinition() {
        startLexing("def bla  ( ){\n}");
        assertTokenAttributes(GroovyTokenTypes.LITERAL_def, 0, "def");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 4, "bla");
        assertNextTokenAttributes(GroovyTokenTypes.LPAREN, 9, "(");
        assertNextTokenAttributes(GroovyTokenTypes.RPAREN, 11, ")");
        assertNextTokenAttributes(GroovyTokenTypes.LCURLY, 12, "{");
        assertNextTokenAttributes(GroovyTokenTypes.RCURLY, 14, "}");
        assertNextTokenIsEndOfFile();
    }

    public void testBuildsAPsiTreeFromTextContainingAScriptHeaderCommentFollowedByImportStatementsAndAMultiLineCommentAndASingleLineComment() {
        /*
         * DO NOT attempt to break the following string into smaller chuncks BUT RATHER install the StringEditor plug-in
         * to get a more readable and editable view of this code snippet.
         */
        String expectedText = "#!/usr/bin/groovy\n\nimport javax.swing.JPanel\nimport groovy.swing.SwingBuilder\n\n/*\n * A multi-line comment.\n */\n\t// single-line comment...";
        startLexing(expectedText);

        PsiBuilder.Marker rootMarker = builder.mark();

        PsiBuilder.Marker importStatementMarker = builder.mark();
        assertTokenAttributes(GroovyTokenTypes.LITERAL_import, 19, "import");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 26, "javax");
        assertNextTokenAttributes(GroovyTokenTypes.DOT, 31, ".");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 32, "swing");
        assertNextTokenAttributes(GroovyTokenTypes.DOT, 37, ".");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 38, "JPanel");
        importStatementMarker.done(GroovyTokenTypeMappings.getType(GroovyTokenTypes.IMPORT));

        importStatementMarker = builder.mark();
        assertNextTokenAttributes(GroovyTokenTypes.LITERAL_import, 45, "import");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 52, "groovy");
        assertNextTokenAttributes(GroovyTokenTypes.DOT, 58, ".");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 59, "swing");
        assertNextTokenAttributes(GroovyTokenTypes.DOT, 64, ".");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 65, "SwingBuilder");
        importStatementMarker.done(GroovyTokenTypeMappings.getType(GroovyTokenTypes.IMPORT));

        rootMarker.done(GroovyElementTypes.FILE);
        assertNextTokenIsEndOfFile();

        assertEquals("PSI tree as text", expectedText, builder.getTreeBuilt().getText());
    }

    private void assertNextTokenAttributes(int tokenTypeIndex, int offset, String text)  {
        builder.advanceLexer();
        assertTokenAttributes(tokenTypeIndex, offset, text);
    }

    private void assertTokenAttributes(int tokenTypeIndex, int offset, String text) {
        GroovyPsiBuilder.Token currentToken = builder.getCurrentToken();
        IElementType elementType = GroovyTokenTypeMappings.getType(tokenTypeIndex);

        assertEquals("token type [GroovyPsiBuilder]", elementType, builder.getTokenType());
        assertEquals("token type [GroovyPsiBuilder.Token]", elementType, currentToken.getTokenType());
        assertEquals("token offset", offset, builder.getCurrentOffset());
        assertEquals("token text", text, currentToken.getTokenText());
    }

    private void assertNextTokenIsEndOfFile() {
        builder.advanceLexer();
        assertEndOfFile();
    }

    private void assertEndOfFile() {
        assertEquals("EOF token", null, builder.getTokenType());
        assertEquals("has PSI builder reached EOF?", true, builder.eof());
    }
}

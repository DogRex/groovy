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

    private void initialiseLexer(String input) {
        MockApplicationManager.reset();
        builder = new GroovyPsiBuilder(GroovyLanguage.findOrCreate(), null, null, input);
    }

    public void testReadsAnEmptyStringAndTerminatesTheLexicalAnalysis() {
        initialiseLexer("");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAScriptHeaderCommentOnly() {
        initialiseLexer("#!");
        assertNextTokenAttributes(GroovyTokenTypes.SH_COMMENT, 0, "#!");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingASingleLineCommentOnly() {
        initialiseLexer("//");
        assertNextTokenAttributes(GroovyTokenTypes.SL_COMMENT, 0, "//");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingTheOpeningOfAMultiLineCommentOnly() {
        initialiseLexer("/*");
        try {
            assertNextTokenAttributes(GroovyTokenTypes.ML_COMMENT, 0, "/*");
            initialiseLexer("/*");
            fail("Bad grammar - ANTLR lexer should have choked on EOF!");
        } catch (RuntimeException e) {
            ObjectAssert.assertInstanceOf("exception class", TokenStreamException.class, e.getCause());
            assertEquals("message", "line 1:3: expecting '*', found '<EOF>'", e.getMessage());
        }
    }

    public void testReadsTextContainingASingleTokenRepresentingWhitespaces() {
        initialiseLexer("   ");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 0, "   ");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingTwoNewLineTokens() {
        initialiseLexer("\n\n");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 0, "\n");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 1, "\n");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingATabCharacter() {
        initialiseLexer("\t");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 0, "\t");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAKeyword() {
        initialiseLexer("def");
        assertNextTokenAttributes(GroovyTokenTypes.LITERAL_def, 0, "def");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAStringLiteralDelimitedBySingleQuotes() {
        String expectedString = "'some stuff'";
        initialiseLexer(expectedString);
        assertNextTokenAttributes(GroovyTokenTypes.STRING_LITERAL, 0, expectedString);
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAStringLiteralDelimitedByQuotes() {
        String expectedString = "\"some stuff\"";
        initialiseLexer(expectedString);
        assertNextTokenAttributes(GroovyTokenTypes.STRING_LITERAL, 0, expectedString);
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAPrimitiveDouble() {
        initialiseLexer("0.1234D");
        assertNextTokenAttributes(GroovyTokenTypes.NUM_DOUBLE, 0, "0.1234D");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsThePositionOfAnIdentifierTokenConsecutiveToATabCharacter() {
        initialiseLexer("\tfoo");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 0, "\t");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 1, "foo");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleLineComment() {
        initialiseLexer("//foo\nfoo");
        assertNextTokenAttributes(GroovyTokenTypes.SL_COMMENT, 0, "//foo");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 5, "\n");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 6, "foo");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingAMultiLineComment() {
        initialiseLexer("/*foo\nfoo*/foo");
        assertNextTokenAttributes(GroovyTokenTypes.ML_COMMENT, 0, "/*foo\nfoo*/");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 11, "foo");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingVariousBlanks() {
        initialiseLexer(" def\n ( ");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 0, " ");
        assertNextTokenAttributes(GroovyTokenTypes.LITERAL_def, 1, "def");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 4, "\n");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 5, " ");
        assertNextTokenAttributes(GroovyTokenTypes.LPAREN, 6, "(");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 7, " ");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingAMethodDefinition() {
        initialiseLexer("def bla  ( ){\n}");
        assertNextTokenAttributes(GroovyTokenTypes.LITERAL_def, 0, "def");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 3, " ");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 4, "bla");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 7, "  ");
        assertNextTokenAttributes(GroovyTokenTypes.LPAREN, 9, "(");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 10, " ");
        assertNextTokenAttributes(GroovyTokenTypes.RPAREN, 11, ")");
        assertNextTokenAttributes(GroovyTokenTypes.LCURLY, 12, "{");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 13, "\n");
        assertNextTokenAttributes(GroovyTokenTypes.RCURLY, 14, "}");
        assertNextTokenIsEndOfFile();
    }

    public void testBuildsAPsiTreeFromTextContainingAScriptHeaderCommentFollowedByImportStatementsAndAMultiLineCommentAndASingleLineComment() {
        /*
         * DO NOT attempt to break the following string into smaller chuncks BUT RATHER install the StringEditor plug-in
         * to get a more readable and editable view of this code snippet.
         */
        String expectedText = "#!/usr/bin/groovy\n\nimport javax.swing.JPanel\rimport groovy.swing.SwingBuilder\r\r/*\n * A multi-line comment.\n */\n\t// single-line comment...\r";
        initialiseLexer(expectedText);

        PsiBuilder.Marker rootMarker = builder.mark();
        assertNextTokenAttributes(GroovyTokenTypes.SH_COMMENT, 0, "#!/usr/bin/groovy");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 17, "\n");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 18, "\n");

        PsiBuilder.Marker importStatementMarker = builder.mark();
        assertNextTokenAttributes(GroovyTokenTypes.LITERAL_import, 19, "import");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 25, " ");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 26, "javax");
        assertNextTokenAttributes(GroovyTokenTypes.DOT, 31, ".");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 32, "swing");
        assertNextTokenAttributes(GroovyTokenTypes.DOT, 37, ".");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 38, "JPanel");
        importStatementMarker.done(GroovyTokenTypeMappings.getType(GroovyTokenTypes.IMPORT));

        assertNextTokenAttributes(GroovyTokenTypes.NLS, 44, "\r");

        importStatementMarker = builder.mark();
        assertNextTokenAttributes(GroovyTokenTypes.LITERAL_import, 45, "import");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 51, " ");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 52, "groovy");
        assertNextTokenAttributes(GroovyTokenTypes.DOT, 58, ".");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 59, "swing");
        assertNextTokenAttributes(GroovyTokenTypes.DOT, 64, ".");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 65, "SwingBuilder");
        importStatementMarker.done(GroovyTokenTypeMappings.getType(GroovyTokenTypes.IMPORT));

        assertNextTokenAttributes(GroovyTokenTypes.NLS, 77, "\r");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 78, "\r");

        assertNextTokenAttributes(GroovyTokenTypes.ML_COMMENT, 79, "/*\n * A multi-line comment.\n */");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 110, "\n");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 111, "\t");

        assertNextTokenAttributes(GroovyTokenTypes.SL_COMMENT, 112, "// single-line comment...");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 137, "\r");

        rootMarker.done(GroovyElementTypes.FILE);
        assertNextTokenIsEndOfFile();

        assertEquals("PSI tree as text", expectedText, builder.getTreeBuilt().getText());
    }

    private void assertNextTokenAttributes(int tokenTypeIndex, int offset, String text)  {
        builder.advanceLexer();
        GroovyPsiBuilder.Token currentToken = builder.getCurrentToken();
        IElementType elementType = GroovyTokenTypeMappings.getType(tokenTypeIndex);

        assertEquals("token type [GroovyPsiBuilder]", elementType, builder.getTokenType());
        assertEquals("token type [GroovyPsiBuilder.Token]", elementType, currentToken.getTokenType());
        assertEquals("token offset", offset, builder.getCurrentOffset());
        assertEquals("token text", text, currentToken.getTokenText());
    }

    private void assertNextTokenIsEndOfFile() {
        builder.advanceLexer();
        assertEquals("EOF token", null, builder.getTokenType());
        assertEquals("has PSI builder reached EOF?", true, builder.eof());
    }
}

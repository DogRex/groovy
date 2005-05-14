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

import junitx.framework.ObjectAssert;

import org.codehaus.groovy.intellij.GroovyjTestCase;
import org.codehaus.groovy.intellij.language.parser.GroovyTokenTypes;
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

import antlr.Token;
import antlr.TokenStreamException;

public class GroovyLexerAdapterTest extends GroovyjTestCase {

    private GroovyLexerAdapter lexer;

    private void initialiseLexer(String input) {
        lexer = new GroovyPsiBuilder(GroovyLanguage.findOrCreate(), null, null, input).getLexer();
    }

    public void testReadsAnEmptyStringAndTerminatesTheLexicalAnalysis() {
        initialiseLexer("");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAScriptHeaderCommentOnly() {
        String input = "#!";
        initialiseLexer(input);
        assertNextTokenAttributes(GroovyTokenTypes.SH_COMMENT, 1, 1, input); // TODO: report that this used to result in an OutOfMemoryError?
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingASingleLineCommentOnly() {
        String input = "//";
        initialiseLexer(input);
        assertNextTokenAttributes(GroovyTokenTypes.SL_COMMENT, 1, 1, input);
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingTheOpeningOfAMultiLineCommentOnly() {
        initialiseLexer("/*");
        try {
            lexer.advance();                                                // TODO: report that this used to result in an OutOfMemoryError?
            fail("Bad grammar - ANTLR lexer should have choked on EOF!");
        } catch (RuntimeException e) {
            ObjectAssert.assertInstanceOf("exception class", TokenStreamException.class, e.getCause());
            assertEquals("message", "line 1:3: expecting '*', found '<EOF>'", e.getMessage());
        }
    }

    public void testReadsTextContainingASuccessionOfTokensThatResembleTheClosingOfAMultiLineCommentOnly() {
        initialiseLexer("*/");
        assertNextTokenAttributes(GroovyTokenTypes.STAR, 1, 1, "*");
        assertNextTokenAttributes(GroovyTokenTypes.DIV, 2, 1, "/");         // TODO: report that this used to result in an OutOfMemoryError?
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAWhitespace() {
        String input = "    ";
        initialiseLexer(input);
        assertNextTokenAttributes(GroovyTokenTypes.WS, 1, 1, input);
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingNewLineTokens() {
        String input = "\n\r\n\r\r";
        initialiseLexer(input);
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 1, 1, input);
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingATabCharacter() {
        String input = "\t";
        initialiseLexer(input);
        assertNextTokenAttributes(GroovyTokenTypes.WS, 1, 1, input);
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAKeyword() {
        String input = "def";
        initialiseLexer(input);
        assertNextTokenAttributes(GroovyTokenTypes.LITERAL_def, 1, 1, input);
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAStringLiteralDelimitedBySingleQuotes() {
        String input = "'some stuff'";
        initialiseLexer(input);
        assertNextTokenAttributes(GroovyTokenTypes.STRING_LITERAL, 1, 1, input);
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAStringLiteralDelimitedByQuotes() {
        String input = "\"some stuff\"";
        initialiseLexer(input);
        assertNextTokenAttributes(GroovyTokenTypes.STRING_LITERAL, 1, 1, input);
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAPrimitiveDouble() {
        String input = "0.1234D";
        initialiseLexer(input);
        assertNextTokenAttributes(GroovyTokenTypes.NUM_DOUBLE, 1, 1, input);
        assertNextTokenIsEndOfFile();
    }

    public void testReadsThePositionOfAnIdentifierTokenConsecutiveToATabCharacter() {
        initialiseLexer("\tfoo");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 1, 1, "\t");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 2, 1, "foo");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingASingleLineComment() {
        initialiseLexer("//foo\nfoo");
        assertNextTokenAttributes(GroovyTokenTypes.SL_COMMENT, 1, 1, "//foo");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 6, 1, "\n");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 1, 2, "foo");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingAMultiLineComment() {
        initialiseLexer("/*foo\nfoo*/foo");
        assertNextTokenAttributes(GroovyTokenTypes.ML_COMMENT, 1, 1, "/*foo\nfoo*/");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 6, 2, "foo");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingVariousBlanks() {
        initialiseLexer(" def\n ( ");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 1, 1, " ");
        assertNextTokenAttributes(GroovyTokenTypes.LITERAL_def, 2, 1, "def");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 5, 1, "\n ");
        assertNextTokenAttributes(GroovyTokenTypes.LPAREN, 2, 2, "(");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 3, 2, " ");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingAMethodDefinition() {
        initialiseLexer("def bla  ( ){\n}");
        assertNextTokenAttributes(GroovyTokenTypes.LITERAL_def, 1, 1, "def");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 4, 1, " ");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 5, 1, "bla");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 8, 1, "  ");
        assertNextTokenAttributes(GroovyTokenTypes.LPAREN, 10, 1, "(");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 11, 1, " ");
        assertNextTokenAttributes(GroovyTokenTypes.RPAREN, 12, 1, ")");
        assertNextTokenAttributes(GroovyTokenTypes.LCURLY, 13, 1, "{");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 14, 1, "\n");
        assertNextTokenAttributes(GroovyTokenTypes.RCURLY, 1, 2, "}");
        assertNextTokenIsEndOfFile();
    }

    public void testReadsTextContainingAScriptHeaderCommentFollowedByImportStatementsAndAMultiLineCommentAndASingleLineComment() {
        /*
         * DO NOT attempt to break the following string into smaller chuncks BUT RATHER install the StringEditor plug-in
         * to get a more readable and editable view of this code snippet.
         */
        initialiseLexer("#!/usr/bin/groovy\n\nimport javax.swing.JPanel\nimport groovy.swing.SwingBuilder\n\n/*\n * A multi-line comment.\n */\n\t// single-line comment...");

        assertNextTokenAttributes(GroovyTokenTypes.SH_COMMENT, 1, 1, "#!/usr/bin/groovy");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 18, 1, "\n\n");

        assertNextTokenAttributes(GroovyTokenTypes.LITERAL_import, 1, 3, "import");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 7, 3, " ");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 8, 3, "javax");
        assertNextTokenAttributes(GroovyTokenTypes.DOT, 13, 3, ".");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 14, 3, "swing");
        assertNextTokenAttributes(GroovyTokenTypes.DOT, 19, 3, ".");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 20, 3, "JPanel");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 26, 3, "\n");

        assertNextTokenAttributes(GroovyTokenTypes.LITERAL_import, 1, 4, "import");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 7, 4, " ");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 8, 4, "groovy");
        assertNextTokenAttributes(GroovyTokenTypes.DOT, 14, 4, ".");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 15, 4, "swing");
        assertNextTokenAttributes(GroovyTokenTypes.DOT, 20, 4, ".");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 21, 4, "SwingBuilder");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 33, 4, "\n\n");

        assertNextTokenAttributes(GroovyTokenTypes.ML_COMMENT, 1, 6, "/*\n * A multi-line comment.\n */");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 4, 8, "\n\t");
        assertNextTokenAttributes(GroovyTokenTypes.SL_COMMENT, 2, 9, "// single-line comment...");
        assertNextTokenIsEndOfFile();
    }

    private void assertNextTokenAttributes(int tokenTypeIndex, int column, int line, String text)  {
        lexer.advance();
        assertEquals("token type [from adapter]", GroovyTokenTypeMappings.getType(tokenTypeIndex), lexer.getTokenType());

        Token currentToken = lexer.getCurrentToken();
        assertEquals("token type", tokenTypeIndex, currentToken.getType());
        assertEquals("token text", text, currentToken.getText());
        assertEquals("column number", column, currentToken.getColumn());
        assertEquals("line number", line, currentToken.getLine());
    }

    private void assertNextTokenIsEndOfFile() {
        lexer.advance();
        assertEquals("EOF token", null, lexer.getTokenType());
    }
}

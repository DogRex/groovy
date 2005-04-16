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

import org.codehaus.groovy.antlr.parser.GroovyTokenTypes;

import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

import antlr.Token;
import antlr.TokenStreamException;

public class GroovyLexerAdapterTest extends TestCase {

    private GroovyPsiBuilder builder;
    private GroovyLexerAdapter lexer;

    private void startLexing(String input) {
        MockApplicationManager.reset();
        builder = new GroovyPsiBuilder(GroovyLanguage.findOrCreate(), null, null, input);
        lexer = builder.getLexer();
    }

    public void testReadsAnEmptyStringAndTerminatesTheLexicalAnalysis() {
        startLexing("");
        assertNull(lexer.getTokenType());
    }

    public void testReadsTextContainingASingleTokenRepresentingAScriptHeaderCommentOnly() {
        startLexing("#!");
        assertTokenAttributes(GroovyTokenTypes.SH_COMMENT, 1, 1, "#!");  // TODO: report that this used to result in an OutOfMemoryError
        assertEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingASingleLineCommentOnly() {
        startLexing("//");
        assertTokenAttributes(GroovyTokenTypes.SL_COMMENT, 1, 1, "//");
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

    public void testReadsTextContainingASuccessionOfTokensThatResembleTheClosingOfAMultiLineCommentOnly() {
        startLexing("*/");
        assertTokenAttributes(GroovyTokenTypes.STAR, 1, 1, "*");
//        assertNextTokenAttributes(lexer, GroovyTokenTypes.DIV, 1, 2, 1, "/"); // TODO: report that this results in an OutOfMemoryError
//        assertEndOfFile(lexer);
    }

    public void testReadsTextContainingASingleTokenRepresentingAWhitespace() {
        startLexing(" ");
        assertTokenAttributes(GroovyTokenTypes.WS, 1, 1, " ");
        assertEndOfFile();
    }

    public void testReadsTextContainingTwoNewLineTokens() {
        startLexing("\n\n");
        assertTokenAttributes(GroovyTokenTypes.NLS, 1, 1, "\n");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 1, 2, "\n");
        assertEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingATabCharacter() {
        startLexing("\t");
        assertTokenAttributes(GroovyTokenTypes.WS, 1, 1, "\t");
        assertEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAKeyword() {
        startLexing("def");
        assertTokenAttributes(GroovyTokenTypes.LITERAL_def, 1, 1, "def");
        assertEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAStringLiteralDelimitedBySingleQuotes() {
        String expectedString = "'some stuff'";
        startLexing(expectedString);
        assertTokenAttributes(GroovyTokenTypes.STRING_LITERAL, 1, 1, expectedString);
        assertEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAStringLiteralDelimitedByQuotes() {
        String expectedString = "\"some stuff\"";
        startLexing(expectedString);
        assertTokenAttributes(GroovyTokenTypes.STRING_LITERAL, 1, 1, expectedString);
        assertEndOfFile();
    }

    public void testReadsTextContainingASingleTokenRepresentingAPrimitiveDouble() {
        startLexing("0.1234D");
        assertTokenAttributes(GroovyTokenTypes.NUM_DOUBLE, 1, 1, "0.1234D");
        assertEndOfFile();
    }

    public void testReadsThePositionOfAnIdentifierTokenConsecutiveToATabCharacter() {
        startLexing("\tfoo");
        assertTokenAttributes(GroovyTokenTypes.WS, 1, 1, "\t");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 2, 1, "foo");
        assertEndOfFile();
    }

    public void testReadsTextContainingASingleLineComment() {
        startLexing("//foo\nfoo");
        assertTokenAttributes(GroovyTokenTypes.SL_COMMENT, 1, 1, "//foo");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 6, 1, "\n");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 1, 2, "foo");
        assertEndOfFile();
    }

    public void testReadsTextContainingAMultiLineComment() {
        startLexing("/*foo\nfoo*/foo");
        assertTokenAttributes(GroovyTokenTypes.ML_COMMENT, 1, 1, "/*foo\nfoo*/");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 6, 2, "foo");
        assertEndOfFile();
    }

    public void testReadsTextContainingVariousBlanks() {
        startLexing(" def\n ( ");
        assertTokenAttributes(GroovyTokenTypes.WS, 1, 1, " ");
        assertNextTokenAttributes(GroovyTokenTypes.LITERAL_def, 2, 1, "def");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 5, 1, "\n");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 1, 2, " ");
        assertNextTokenAttributes(GroovyTokenTypes.LPAREN, 2, 2, "(");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 3, 2, " ");
        assertEndOfFile();
    }

    public void testReadsTextContainingAMethodDefinition() {
        startLexing("def bla  ( ){\n}");
        assertTokenAttributes(GroovyTokenTypes.LITERAL_def, 1, 1, "def");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 4, 1, " ");
        assertNextTokenAttributes(GroovyTokenTypes.IDENT, 5, 1, "bla");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 8, 1, "  ");
        assertNextTokenAttributes(GroovyTokenTypes.LPAREN, 10, 1, "(");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 11, 1, " ");
        assertNextTokenAttributes(GroovyTokenTypes.RPAREN, 12, 1, ")");
        assertNextTokenAttributes(GroovyTokenTypes.LCURLY, 13, 1, "{");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 14, 1, "\n");
        assertNextTokenAttributes(GroovyTokenTypes.RCURLY, 1, 2, "}");
        assertEndOfFile();
    }

    public void testReadsTextContainingAScriptHeaderCommentFollowedByImportStatementsAndAMultiLineCommentAndASingleLineComment() {
        /*
         * DO NOT attempt to break the following string into smaller chuncks BUT RATHER install the StringEditor plug-in
         * to get a more readable and editable view of this code snippet.
         */
        startLexing("#!/usr/bin/groovy\n\nimport javax.swing.JPanel\nimport groovy.swing.SwingBuilder\n\n/*\n * A multi-line comment.\n */\n\t// single-line comment...");

        assertTokenAttributes(GroovyTokenTypes.SH_COMMENT, 1, 1, "#!/usr/bin/groovy");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 18, 1, "\n");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 1, 2, "\n");

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
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 33, 4, "\n");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 1, 5, "\n");

        assertNextTokenAttributes(GroovyTokenTypes.ML_COMMENT, 1, 6, "/*\n * A multi-line comment.\n */");
        assertNextTokenAttributes(GroovyTokenTypes.NLS, 4, 8, "\n");
        assertNextTokenAttributes(GroovyTokenTypes.WS, 1, 9, "\t");
        assertNextTokenAttributes(GroovyTokenTypes.SL_COMMENT, 2, 9, "// single-line comment...");
        assertEndOfFile();
    }

    /*
     * DO NOT attempt to break the following string into smaller chuncks BUT RATHER install the StringEditor plug-in
     * to get a more readable and editable view of this code snippet.
     */
    private static final String FULL_SCRIPT =
            "#!/usr/bin/groovy\n\nimport javax.swing.JPanel\nimport groovy.swing.SwingBuilder\n                               Bad characters: \\n #\n/**\n * Doc comments for <code>SomeClass</code>.\n *\n * @author The Codehaus\n *\n * @see GroovyTestCase#setUp\n */\n@Annotation (name=value)\npublic class SomeClass extends GroovyTestCase { // some comment\n\n    /* Block comment */\n    @Property def author = \"Joe Bloggs\"\n    @Property Long random = 1101001010010110L;\n\n    private String field = \"Hello World\";\n    private double unusedField = 12345.67890;\n    private UnknownType anotherString = \"Another\\nStrin\\g\";\n    private int[] array = new int[] { 1, 2, 3 };\n    public static int staticField = 0;\n\n    public SomeClass(AnInterface param) {\n        //TODO: something\n        int localVar = \"IntelliJ\"; // Error, incompatible types\n        System.out.println(anotherString + field + localVar);\n        long time = Date.parse(\"1.2.3\"); // Method is deprecated\n        int value = this.staticField; \n    }\n\n    interface AnInterface {\n        int CONSTANT = 2;\n    }\n\n    void testDoesANumberOfRandomThings(int count) {/*  block\n                     comment */\n        new SomeClass();\n\n        def quadraticClosure = { a, b :: Math.pow(a, 2) + (2 * a * b) + Math.pow(b, 2) }\n        quadraticClosure.applyTo(3, 5)\n\n        List aList = [ 3, 5 ]\n        assert [ 9, 25 ] == aList.collect { i :: Math.pow(i, 2) }, 'assertion failed'\n\n        count++\n    }\n}\n";

/*
 *  TODO: add a test for the following:
 *

#!/usr/bin/groovy

import javax.swing.JPanel
import groovy.swing.SwingBuilder
                               Bad characters: \n #
/**
 * Doc comments for <code>SomeClass</code>.
 *
 * @author The Codehaus
 *
 * @see GroovyTestCase#setUp
 * /
    @Annotation (name=value)
    public class SomeClass extends GroovyTestCase { // some comment

        /* Block comment * /
        @Property def author = "Joe Bloggs"
        @Property Long random = 1101001010010110L;

        private String field = "Hello World";
        private double unusedField = 12345.67890;
        private UnknownType anotherString = "Another\nStrin\g";
        private int[] array = new int[] { 1, 2, 3 };
        public static int staticField = 0;

        public SomeClass(AnInterface param) {
            //TODO: something
            int localVar = "IntelliJ"; // Error, incompatible types
            System.out.println(anotherString + field + localVar);
            long time = Date.parse("1.2.3"); // Method is deprecated
            int value = this.staticField;
        }

        interface AnInterface {
            int CONSTANT = 2;
        }

        void testDoesANumberOfRandomThings(int count) {/*  block
                         comment * /
            new SomeClass();

            def quadraticClosure = { a, b -> Math.pow(a, 2) + (2 * a * b) + Math.pow(b, 2) }
            quadraticClosure.applyTo(3, 5)

            List aList = [ 3, 5 ]
            assert [ 9, 25 ] == aList.collect { i -> Math.pow(i, 2) }, 'assertion failed'

            count++
        }
    }
*/

    private void assertNextTokenAttributes(int tokenTypeIndex, int column, int line, String text)  {
        lexer.advance();
        assertTokenAttributes(tokenTypeIndex, column, line, text);
    }

    private void assertTokenAttributes(int tokenTypeIndex, int column, int line, String text) {
        assertEquals("token type [from adapter]", GroovyTokenTypeMappings.getType(tokenTypeIndex), lexer.getTokenType());

        Token currentToken = lexer.getCurrentToken();
        assertEquals("token type", tokenTypeIndex, currentToken.getType());
        assertEquals("token text", text, currentToken.getText());
        assertEquals("column number", column, currentToken.getColumn());
        assertEquals("line number", line, currentToken.getLine());
    }

    private void assertEndOfFile() {
        lexer.advance();
        assertEquals("EOF token", null, lexer.getTokenType());
    }
}

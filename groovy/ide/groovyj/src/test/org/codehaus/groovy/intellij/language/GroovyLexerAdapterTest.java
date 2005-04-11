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

import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

import antlr.Token;
import antlr.TokenStreamException;

public class GroovyLexerAdapterTest extends TestCase {

    protected void setUp() {
        MockApplicationManager.reset();
    }

    public void testReadsAnEmptyStringAndTerminatesTheLexicalAnalysis() {
        GroovyLexerAdapter lexer = createLexer("");
        assertNull(lexer.getTokenType());
    }

    public void testReadsTextContainingASingleTokenRepresentingAScriptHeaderCommentOnly() {
        GroovyLexerAdapter lexer = createLexer("#!");
        assertType(lexer, GroovyTokenTypes.SH_COMMENT, 0, 1, 1, "#!");  // TODO: report that this used to result in an OutOfMemoryError
        assertEndOfFile(lexer);
    }

    public void testReadsTextContainingASingleTokenRepresentingASingleLineCommentOnly() {
        GroovyLexerAdapter lexer = createLexer("//");
        assertType(lexer, GroovyTokenTypes.SL_COMMENT, 0, 1, 1, "//");
        assertEndOfFile(lexer);
    }

    public void testReadsTextContainingASingleTokenRepresentingTheOpeningOfAMultiLineCommentOnly() {
        try {
            createLexer("/*");                                          // TODO: report that this used to result in an OutOfMemoryError
            fail("Bad grammar - ANTLR lexer should have choked on EOF!");
        } catch (RuntimeException e) {
            ObjectAssert.assertInstanceOf("exception class", TokenStreamException.class, e.getCause());
            assertEquals("message", "line 1:3: expecting '*', found '<EOF>'", e.getMessage());
        }
    }

    public void testReadsTextContainingASuccessionOfTokensThatResembleTheClosingOfAMultiLineCommentOnly() {
        GroovyLexerAdapter lexer = createLexer("*/");
        assertType(lexer, GroovyTokenTypes.STAR, 0, 1, 1, "*");
//        assertNext(lexer, GroovyTokenTypes.DIV, 1, 2, 1, "/");      // TODO: report that this results in an OutOfMemoryError
//        assertEndOfFile(lexer);
    }

    public void testReadsTextContainingASingleTokenRepresentingAWhitespace() {
        GroovyLexerAdapter lexer = createLexer(" ");
        assertType(lexer, GroovyTokenTypes.WS, 0, 1, 1, " ");
        assertEndOfFile(lexer);
    }

    public void testReadsTextContainingTwoNewLineTokens() {
        GroovyLexerAdapter lexer = createLexer("\n\n");
        assertType(lexer, GroovyTokenTypes.NLS, 0, 1, 1, "");
        assertNext(lexer, GroovyTokenTypes.NLS, 1, 1, 2, "");
        assertEndOfFile(lexer);
    }

    public void testReadsTextContainingASingleTokenRepresentingATabCharacter() {
        GroovyLexerAdapter lexer = createLexer("\t");
        assertType(lexer, GroovyTokenTypes.WS, 0, 1, 1, "\t");
        assertEndOfFile(lexer);
    }

    public void testReadsTextContainingASingleTokenRepresentingAKeyword() {
        GroovyLexerAdapter lexer = createLexer("def");
        assertType(lexer, GroovyTokenTypes.LITERAL_def, 0, 1, 1, "def");
        assertEndOfFile(lexer);
    }

    public void testReadsTextContainingASingleTokenRepresentingAStringLiteralDelimitedBySingleQuotes() {
        GroovyLexerAdapter lexer = createLexer("'some stuff'");
        assertType(lexer, GroovyTokenTypes.STRING_LITERAL, 0, 1, 1, "some stuff");
        assertEndOfFile(lexer);
    }

    public void testReadsTextContainingASingleTokenRepresentingAStringLiteralDelimitedByQuotes() {
        GroovyLexerAdapter lexer = createLexer("\"some stuff\"");
        assertType(lexer, GroovyTokenTypes.STRING_LITERAL, 0, 1, 1, "some stuff");
        assertEndOfFile(lexer);
    }

    public void testReadsTextContainingASingleTokenRepresentingAPrimitiveDouble() {
        GroovyLexerAdapter lexer = createLexer("0.1234D");
        assertType(lexer, GroovyTokenTypes.NUM_DOUBLE, 0, 1, 1, "0.1234D");
        assertEndOfFile(lexer);
    }

    public void testReadsThePositionOfAnIdentifierTokenConsecutiveToATabCharacter() {
        GroovyLexerAdapter lexer = createLexer("\tfoo");
        assertType(lexer, GroovyTokenTypes.WS, 0, 1, 1, "\t");
        assertNext(lexer, GroovyTokenTypes.IDENT, 1, 2, 1, "foo");
        assertEndOfFile(lexer);
    }

    public void testReadsTextContainingASingleLineComment() {
        GroovyLexerAdapter lexer = createLexer("//foo\nfoo");
        assertType(lexer, GroovyTokenTypes.SL_COMMENT, 0, 1, 1, "//foo");
        assertNext(lexer, GroovyTokenTypes.NLS, 5, 6, 1, "");
        assertNext(lexer, GroovyTokenTypes.IDENT, 6, 1, 2, "foo");
        assertEndOfFile(lexer);
    }

    public void testReadsTextContainingAMultiLineComment() {
        GroovyLexerAdapter lexer = createLexer("/*foo\nfoo*/foo");
        assertType(lexer, GroovyTokenTypes.ML_COMMENT, 0, 1, 1, "/*foo\nfoo*/");
        assertNext(lexer, GroovyTokenTypes.IDENT, 11, 6, 2, "foo");
        assertEndOfFile(lexer);
    }

    public void testReadsTextContainingVariousBlanks() {
        GroovyLexerAdapter lexer = createLexer(" def\n ( ");
        assertType(lexer, GroovyTokenTypes.WS, 0, 1, 1, " ");
        assertNext(lexer, GroovyTokenTypes.LITERAL_def, 1, 2, 1, "def");
        assertNext(lexer, GroovyTokenTypes.NLS, 4, 5, 1, "");
        assertNext(lexer, GroovyTokenTypes.WS, 5, 1, 2, " ");
        assertNext(lexer, GroovyTokenTypes.LPAREN, 6, 2, 2, "(");
        assertNext(lexer, GroovyTokenTypes.WS, 7, 3, 2, " ");
        assertEndOfFile(lexer);
    }

    public void testReadsTextContainingAMethodDefinition() {
        GroovyLexerAdapter lexer = createLexer("def bla  ( ){\n}");
        assertType(lexer, GroovyTokenTypes.LITERAL_def, 0, 1, 1, "def");
        assertNext(lexer, GroovyTokenTypes.WS, 3, 4, 1, " ");
        assertNext(lexer, GroovyTokenTypes.IDENT, 4, 5, 1, "bla");
        assertNext(lexer, GroovyTokenTypes.WS, 7, 8, 1, "  ");
        assertNext(lexer, GroovyTokenTypes.LPAREN, 9, 10, 1, "(");
        assertNext(lexer, GroovyTokenTypes.WS, 10, 11, 1, " ");
        assertNext(lexer, GroovyTokenTypes.RPAREN, 11, 12, 1, ")");
        assertNext(lexer, GroovyTokenTypes.LCURLY, 12, 13, 1, "{");
        assertNext(lexer, GroovyTokenTypes.NLS, 13, 14, 1, "");
        assertNext(lexer, GroovyTokenTypes.RCURLY, 14, 1, 2, "}");
        assertEndOfFile(lexer);
    }

    public void testReadsTextContainingAScriptHeaderCommentFollowedByImportStatementsAndAMultiLineComment() {
        /*
         * DO NOT attempt to break the following string into smaller chuncks BUT RATHER install the StringEditor plug-in
         * to get a more readable and editable view of this code snippet.
         */
        GroovyLexerAdapter lexer = createLexer("#!/usr/bin/groovy\n\nimport javax.swing.JPanel\nimport groovy.swing.SwingBuilder\n\n/*\n * A multi-line comment.\r\n */\n");

        assertType(lexer, GroovyTokenTypes.SH_COMMENT, 0, 1, 1, "#!/usr/bin/groovy");
        assertNext(lexer, GroovyTokenTypes.NLS, 17, 18, 1, "");
        assertNext(lexer, GroovyTokenTypes.NLS, 18, 1, 2, "");

        assertNext(lexer, GroovyTokenTypes.LITERAL_import, 19, 1, 3, "import");
        assertNext(lexer, GroovyTokenTypes.WS, 25, 7, 3, " ");
        assertNext(lexer, GroovyTokenTypes.IDENT, 26, 8, 3, "javax");
        assertNext(lexer, GroovyTokenTypes.DOT, 31, 13, 3, ".");
        assertNext(lexer, GroovyTokenTypes.IDENT, 32, 14, 3, "swing");
        assertNext(lexer, GroovyTokenTypes.DOT, 37, 19, 3, ".");
        assertNext(lexer, GroovyTokenTypes.IDENT, 38, 20, 3, "JPanel");
        assertNext(lexer, GroovyTokenTypes.NLS, 44, 26, 3, "");

        assertNext(lexer, GroovyTokenTypes.LITERAL_import, 45, 1, 4, "import");
        assertNext(lexer, GroovyTokenTypes.WS, 51, 7, 4, " ");
        assertNext(lexer, GroovyTokenTypes.IDENT, 52, 8, 4, "groovy");
        assertNext(lexer, GroovyTokenTypes.DOT, 58, 14, 4, ".");
        assertNext(lexer, GroovyTokenTypes.IDENT, 59, 15, 4, "swing");
        assertNext(lexer, GroovyTokenTypes.DOT, 64, 20, 4, ".");
        assertNext(lexer, GroovyTokenTypes.IDENT, 65, 21, 4, "SwingBuilder");
        assertNext(lexer, GroovyTokenTypes.NLS, 77, 33, 4, "");
        assertNext(lexer, GroovyTokenTypes.NLS, 78, 1, 5, "");

        assertNext(lexer, GroovyTokenTypes.ML_COMMENT, 79, 1, 6, "/*\n * A multi-line comment.\r\n */");
        assertNext(lexer, GroovyTokenTypes.NLS, 110, 4, 8, "");
        assertEndOfFile(lexer);
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

    private GroovyLexerAdapter createLexer(String input) {
        GroovyLexerAdapter lexer = (GroovyLexerAdapter) GroovyLanguage.findOrCreate().getParserDefinition().createLexer(null);
        PsiBuilder psiBuilder = new GroovyPsiBuilder(GroovyLanguage.findOrCreate(), null, null, input);
        lexer.start(psiBuilder);
        return lexer;
    }

    private void assertNext(GroovyLexerAdapter lexer, int type, int offset, int column, int line, String text)  {
        GroovyLexerAdapter.currentGroovyPsiLexer().getPsiBuilder().advanceLexer();
        assertType(lexer, type, offset, column, line, text);
    }

    private void assertType(GroovyLexerAdapter lexer, int type, int offset, int column, int line, String text) {
        IElementType elementType = GroovyTokenTypeMappings.getType(type);
        PsiBuilder psiBuilder = GroovyLexerAdapter.currentGroovyPsiLexer().getPsiBuilder();
        assertEquals("token type [GroovyPsiLexer]", elementType, lexer.getTokenType());
        assertEquals("token type [PsiBuilder]", elementType, psiBuilder.getTokenType());    // advances the underlying lexer!
        assertEquals("offset", offset, psiBuilder.getCurrentOffset());

        Token currentToken = lexer.getCurrentToken();
        assertEquals("column number", column, currentToken.getColumn());
        assertEquals("line number", line, currentToken.getLine());
        assertEquals("token", text, currentToken.getText());
    }

    private void assertEndOfFile(GroovyLexerAdapter lexer) {
        PsiBuilder psiBuilder = GroovyLexerAdapter.currentGroovyPsiLexer().getPsiBuilder();
        psiBuilder.advanceLexer();
        assertEquals("EOF token", null, lexer.getTokenType());
        assertEquals("has PSI builder reached EOF?", true, psiBuilder.eof());
    }
}

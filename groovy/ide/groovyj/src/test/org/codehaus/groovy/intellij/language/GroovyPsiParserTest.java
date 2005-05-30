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

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.psi.TokenType;

import org.codehaus.groovy.intellij.language.parser.GroovyTokenTypes;
import org.codehaus.groovy.intellij.psi.GroovyElementTypes;

public class GroovyPsiParserTest extends PsiTreeTestCase {

    public void testParsesATrivialScriptIntoATreeOfPsiNodes() {
        /*
         * DO NOT attempt to break the following string into smaller chuncks BUT RATHER install the StringEditor plug-in
         * to get a more readable and editable view of this code snippet.
         */
        String textToParse = "#!/usr/bin/groovy\n\nimport java.util.Collections\n\n";

        ASTNode rootNode = parse(textToParse);
        assertSame("file element type", GroovyElementTypes.FILE, rootNode.getElementType());

        // #!/usr/bin/groovy
        ASTNode rootChildNode = assertFirstChildNode(GroovyTokenTypes.SH_COMMENT, rootNode);
        rootChildNode = assertNextNode(TokenType.WHITE_SPACE, rootChildNode);
        rootChildNode = assertNextNode(GroovyTokenTypes.IMPORT, rootChildNode);

        // import java.util.Collections
        ASTNode importStatementChildNode = assertFirstChildNode(GroovyTokenTypes.LITERAL_import, rootChildNode);
        importStatementChildNode = assertNextNode(TokenType.WHITE_SPACE, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.IDENT, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.DOT, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.IDENT, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.DOT, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.IDENT, importStatementChildNode);
        assertNodeHasNoChildrenAndNoNextSiblings(importStatementChildNode);

        rootChildNode = assertNextNode(TokenType.WHITE_SPACE, rootChildNode);
        assertNodeHasNoNextSibling(rootChildNode);

        assertEquals("PSI tree as text", textToParse, rootNode.getText());
    }

    // TODO: use GroovyPsiBuilder in groovy.psi.g and start introducing lexical and grammatical errors
//    public void testParsesAComplexScriptWithErrorsIntoATreeOfPsiNodes() {
    public void testParsesAComplexScriptIntoATreeOfPsiNodes() {
        /*
         * DO NOT attempt to break the following string into smaller chuncks BUT RATHER install the StringEditor plug-in
         * to get a more readable and editable view of this code snippet.
         */
//        String textToParse = "#!/usr/bin/groovy\n\nimport javax.swing.JPanel;\nimport groovy.swing.SwingBuilder\n//                               Bad characters: \\n #\n/**\n * Doc comments for <code>SomeClass</code>.\n *\n * @author The Codehaus\n *\n * @see GroovyTestCase#setUp\n */\n// @Annotation (name=value)\npublic class SomeClass extends GroovyTestCase { // some comment\n\n    /* Block comment */\n    @Property def author = \"Joe Bloggs\"\n    @Property Long random = 1101001010010110L\n\n    private String field = \"Hello World\"\n    private double unusedField = 12345.67890;\n    private UnknownType anotherString = \"Another\\nStrin\\g\";\n    private int[] array = new int[] { 1, 2, 3 };\n    public static int staticField = 0\n\n    public SomeClass(AnInterface param) {\n        //TODO: something\n        int localVar = \"IntelliJ\"; // Error, incompatible types\n        println (anotherString + field + localVar)\n        long time = Date.parse(\"1.2.3\"); // Method is deprecated\n        int value = this.staticField\n    }\n\n    interface AnInterface {\n        int CONSTANT = 2\n    }\n\n    void testDoesANumberOfRandomThings(int count) {/*  block\n                     comment */\n        new SomeClass()\n\n        def quadraticClosure = { a, b :: Math.pow(a, 2) + (2 * a * b) + Math.pow(b, 2) }\n        quadraticClosure.applyTo(3, 5);\n\n        List aList = [ 3, 5 ]\n        assert [ 9, 25 ] == aList.collect { i -> Math.pow(i, 2) }, 'assertion failed'\n\n        count++\n    }\n}\n";
        String textToParse = "#!/usr/bin/groovy\n\nimport javax.swing.JPanel;\nimport groovy.swing.SwingBuilder\n//                               Bad characters: \\n #\n/**\n * Doc comments for <code>SomeClass</code>.\n *\n * @author The Codehaus\n *\n * @see GroovyTestCase#setUp\n */\n// @Annotation (name=value)\npublic class SomeClass extends GroovyTestCase { // some comment\n\n    /* Block comment */\n    @Property def author = \"Joe Bloggs\"\n    @Property Long random = 1101001010010110L\n\n    private String field = \"Hello World\"\n    private double unusedField = 12345.67890;\n    private UnknownType anotherString = \"Another\\nStrin|g\";\n    private int[] array = [ 1, 2, 3 ] as int[]\n    public static int staticField = 0\n\n    public SomeClass(AnInterface param) {\n        //TODO: something\n        int localVar = \"IntelliJ\"; // Error, incompatible types\n        println (anotherString + field + localVar)\n        long time = Date.parse(\"1.2.3\"); // Method is deprecated\n        int value = this.staticField\n    }\n\n    interface AnInterface {\n        int CONSTANT = 2\n    }\n\n    void testDoesANumberOfRandomThings(int count) {/*  block\n                     comment */\n        new SomeClass()\n\n        def quadraticClosure = { a, b -> Math.pow(a, 2) + (2 * a * b) + Math.pow(b, 2) }\n        quadraticClosure.applyTo(3, 5);\n\n        List aList = [ 3, 5 ]\n        assert [ 9, 25 ] == aList.collect { i -> Math.pow(i, 2) }, 'assertion failed'\n\n        count++\n    }\n}\n";

        ASTNode rootNode = parse(textToParse);
        assertSame("file element type", GroovyElementTypes.FILE, rootNode.getElementType());

        // #!/usr/bin/groovy
        ASTNode rootChildNode = assertFirstChildNode(GroovyTokenTypes.SH_COMMENT, rootNode);
        rootChildNode = assertNextNode(TokenType.WHITE_SPACE, rootChildNode);
        rootChildNode = assertNextNode(GroovyTokenTypes.IMPORT, rootChildNode);

        // import javax.swing.JPanel;
        ASTNode importStatementChildNode = assertFirstChildNode(GroovyTokenTypes.LITERAL_import, rootChildNode);
        importStatementChildNode = assertNextNode(TokenType.WHITE_SPACE, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.IDENT, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.DOT, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.IDENT, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.DOT, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.IDENT, importStatementChildNode);
        assertNodeHasNoChildrenAndNoNextSiblings(importStatementChildNode);

        rootChildNode = assertNextNode(GroovyTokenTypes.SEMI, rootChildNode);
        rootChildNode = assertNextNode(TokenType.WHITE_SPACE, rootChildNode);
        rootChildNode = assertNextNode(GroovyTokenTypes.IMPORT, rootChildNode);

        // import groovy.swing.SwingBuilder
        importStatementChildNode = assertFirstChildNode(GroovyTokenTypes.LITERAL_import, rootChildNode);
        importStatementChildNode = assertNextNode(TokenType.WHITE_SPACE, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.IDENT, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.DOT, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.IDENT, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.DOT, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.IDENT, importStatementChildNode);
        assertNodeHasNoChildrenAndNoNextSiblings(importStatementChildNode);

        rootChildNode = assertNextNode(TokenType.WHITE_SPACE, rootChildNode);
        //                               Bad characters: \n #
        rootChildNode = assertNextNode(GroovyTokenTypes.SL_COMMENT, rootChildNode);
        rootChildNode = assertNextNode(TokenType.WHITE_SPACE, rootChildNode);
        rootChildNode = assertNextNode(GroovyTokenTypes.ML_COMMENT, rootChildNode);
        rootChildNode = assertNextNode(TokenType.WHITE_SPACE, rootChildNode);
        // @Annotation (name=value)
        rootChildNode = assertNextNode(GroovyTokenTypes.SL_COMMENT, rootChildNode);
        rootChildNode = assertNextNode(TokenType.WHITE_SPACE, rootChildNode);

        // FIXME: modifiers should be included in the class block!
        rootChildNode = assertNextNode(GroovyTokenTypes.LITERAL_public, rootChildNode);
        rootChildNode = assertNextNode(TokenType.WHITE_SPACE, rootChildNode);
        rootChildNode = assertNextNode(GroovyTokenTypes.CLASS_DEF, rootChildNode);

        // public class SomeClass extends GroovyTestCase {
        ASTNode classDefinitionChildNode = assertFirstChildNode(GroovyTokenTypes.LITERAL_class, rootChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_extends, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LCURLY, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // some comment
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.SL_COMMENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        /* Block comment */
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ML_COMMENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // @Property def author = "Joe Bloggs"
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.AT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_def, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.STRING_LITERAL, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // @Property Long random = 1101001010010110L
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.AT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_LONG, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // private String field = "Hello World"
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_private, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.STRING_LITERAL, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // private double unusedField = 12345.67890;
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_private, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_double, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_DOUBLE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.SEMI, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // private UnknownType anotherString = "Another\nStrin|g";
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_private, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.STRING_LITERAL, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.SEMI, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // private int[] array = [ 1, 2, 3 ] as int[]
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_private, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_int, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LBRACK, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RBRACK, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LBRACK, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.COMMA, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.COMMA, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RBRACK, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_as, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_int, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LBRACK, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RBRACK, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // public static int staticField = 0
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_public, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_static, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_int, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // public SomeClass(AnInterface param) {
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_public, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LCURLY, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        //TODO: something
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.SL_COMMENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // int localVar = "IntelliJ"; // Error, incompatible types
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_int, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.STRING_LITERAL, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.SEMI, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.SL_COMMENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // println (anotherString + field + localVar)
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.PLUS, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.PLUS, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // long time = Date.parse("1.2.3"); // Method is deprecated
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_long, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.DOT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.STRING_LITERAL, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.SEMI, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.SL_COMMENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // int value = this.staticField
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_int, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_this, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.DOT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // }
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RCURLY, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // interface AnInterface {
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_interface, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LCURLY, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // int CONSTANT = 2
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_int, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // }
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RCURLY, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // void testDoesANumberOfRandomThings(int count) {
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_void, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_int, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LCURLY, classDefinitionChildNode);

        // /*  block\n                     comment */
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ML_COMMENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // new SomeClass()
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_new, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // def quadraticClosure = { a, b -> Math.pow(a, 2) + (2 * a * b) + Math.pow(b, 2) }
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_def, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LCURLY, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.COMMA, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.CLOSURE_OP, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.DOT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.COMMA, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.PLUS, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.STAR, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.STAR, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.PLUS, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.DOT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.COMMA, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RCURLY, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // quadraticClosure.applyTo(3, 5);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.DOT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.COMMA, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.SEMI, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // List aList = [ 3, 5 ]
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LBRACK, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.COMMA, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RBRACK, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // assert [ 9, 25 ] == aList.collect { i -> Math.pow(i, 2) }, 'assertion failed'
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_assert, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LBRACK, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.COMMA, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RBRACK, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.EQUAL, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.DOT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LCURLY, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.CLOSURE_OP, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.DOT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.COMMA, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RPAREN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RCURLY, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.COMMA, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.STRING_LITERAL, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // count++
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.INC, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // }
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RCURLY, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(TokenType.WHITE_SPACE, classDefinitionChildNode);

        // }
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RCURLY, classDefinitionChildNode);
        assertNodeHasNoChildrenAndNoNextSiblings(classDefinitionChildNode);

        rootChildNode = assertNextNode(TokenType.WHITE_SPACE, rootChildNode);
        assertNodeHasNoNextSibling(rootChildNode);

        assertEquals("PSI tree as text", textToParse, rootNode.getText());
    }

    private ASTNode parse(String textToParse) {
        GroovyLanguage groovyLanguage = GroovyLanguage.findOrCreate();
        ParserDefinition parserDefinition = groovyLanguage.getParserDefinition();
        GroovyPsiBuilder builder = new GroovyPsiBuilder(groovyLanguage, null, null, textToParse);
        return parserDefinition.createParser(null).parse(parserDefinition.getFileNodeType(), builder);
    }
}

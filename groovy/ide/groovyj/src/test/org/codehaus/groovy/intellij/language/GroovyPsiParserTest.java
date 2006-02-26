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
        rootChildNode = assertNextNodeIsAWhiteSpace(rootChildNode);

        // import java.util.Collections
        rootChildNode = assertNextStatementIsASimpleImportStatement(rootChildNode);

        rootChildNode = assertNextNodeIsAWhiteSpace(rootChildNode);
        assertNodeHasNoNextSibling(rootChildNode);

        assertEquals("PSI tree as text", textToParse, rootNode.getText());
    }

    public void testParsesAComplexScriptIntoATreeOfPsiNodes() {
        /*
         * DO NOT attempt to break the following string into smaller chuncks BUT RATHER install the StringEditor plug-in
         * to get a more readable and editable view of this code snippet.
         */
//        String textToParse = "#!/usr/bin/groovy\n\nimport javax.swing.JPanel;\nimport groovy.swing.SwingBuilder\n//                               Bad characters: \\n #\n/**\n * Doc comments for <code>SomeClass</code>.\n *\n * @author The Codehaus\n *\n * @see GroovyTestCase#setUp\n */\n// @Annotation (name=value)\npublic class SomeClass extends GroovyTestCase { // some comment\n\n    /* Block comment */\n    @Property def author = \"Joe Bloggs\"\n    @Property Long random = 1101001010010110L\n\n    private String field = \"Hello World\"\n    private double unusedField = 12345.67890;\n    private UnknownType anotherString = \"Another\\nStrin\\g\";\n    private int[] array = new int[] { 1, 2, 3 };\n    public static int staticField = 0\n\n    public SomeClass(AnInterface param) {\n        //TODO: something\n        int localVar = \"IntelliJ\"; // Error, incompatible types\n        println (anotherString + field + localVar)\n        long time = Date.parse(\"1.2.3\"); // Method is deprecated\n        int value = this.staticField\n    }\n\n    interface AnInterface {\n        int CONSTANT = 2\n    }\n\n    void testDoesANumberOfRandomThings(int count) {/*  block\n                     comment */\n        new SomeClass()\n\n        def quadraticClosure = { a, b :: Math.pow(a, 2) + (2 * a * b) + Math.pow(b, 2) }\n        quadraticClosure.applyTo(3, 5);\n\n        List aList = [ 3, 5 ]\n        assert [ 9, 25 ] == aList.collect { i -> Math.pow(i, 2) }, 'assertion failed'\n\n        count++\n    }\n}\n";
        String textToParse = "#!/usr/bin/groovy\n\nimport javax.swing.JPanel;\nimport groovy.swing.SwingBuilder\n//                               Bad characters: \\n #\n/**\n * Doc comments for <code>SomeClass</code>.\n *\n * @author The Codehaus\n *\n * @see GroovyTestCase#setUp\n */\n// @Annotation (name=value)\npublic class SomeClass extends GroovyTestCase { // some comment\n\n    /* Block comment */\n    @Property def author = \"Joe Bloggs\"\n    @Property Long random = 1101001010010110L\n\n    private String field = \"Hello World\"\n    private double unusedField = 12345.67890;\n    private UnknownType anotherString = \"Another\\nStrin|g\";\n    private int[] array = [ 1, 2, 3 ] as int[]\n    public static int staticField = 0\n\n    public SomeClass(AnInterface param) {\n        // TODO: something\n        int localVar = \"IntelliJ\";       // Error, incompatible types\n        println (anotherString + field + localVar)\n\n        long time = Date.parse(\"1/2/3\"); // Method is deprecated\n        int value = this.staticField\n    }\n\n    interface AnInterface {\n        int CONSTANT = 2\n    }\n\n    void testDoesANumberOfRandomThings() {/*  block\n                     comment */\n        new SomeClass()\n\n        def quadraticClosure = { a, b -> Math.pow(a, 2) + (2 * a * b) + Math.pow(b, 2) }\n        assert quadraticClosure.call(3, 5) == 64\n\n        List aList = [ 3, 5 ]\n        assert aList.collect { i -> return (int) Math.pow(i, 2) } == [ 9, 25 ]\n\n        staticField++\n    }\n}\n";

        ASTNode rootNode = parse(textToParse);
        assertSame("file element type", GroovyElementTypes.FILE, rootNode.getElementType());

        // #!/usr/bin/groovy
        ASTNode rootChildNode = assertFirstChildNode(GroovyTokenTypes.SH_COMMENT, rootNode);
        rootChildNode = assertNextNodeIsAWhiteSpace(rootChildNode);

        // import javax.swing.JPanel;
        rootChildNode = assertNextStatementIsASimpleImportStatement(rootChildNode);
        rootChildNode = assertNextNodeIsASemiColon(rootChildNode);
        rootChildNode = assertNextNodeIsAWhiteSpace(rootChildNode);

        // import groovy.swing.SwingBuilder
        rootChildNode = assertNextStatementIsASimpleImportStatement(rootChildNode);
        rootChildNode = assertNextNodeIsAWhiteSpace(rootChildNode);
        //                               Bad characters: \n #
        rootChildNode = assertNextNodeIsASingleLineComment(rootChildNode);
        rootChildNode = assertNextNodeIsAWhiteSpace(rootChildNode);
        rootChildNode = assertNextNodeIsAMultiLineComment(rootChildNode);
        rootChildNode = assertNextNodeIsAWhiteSpace(rootChildNode);
        // @Annotation (name=value)
        rootChildNode = assertNextNodeIsASingleLineComment(rootChildNode);
        rootChildNode = assertNextNodeIsAWhiteSpace(rootChildNode);

        // public class SomeClass extends GroovyTestCase {
        rootChildNode = assertNextNode(GroovyTokenTypes.CLASS_DEF, rootChildNode);

        ASTNode classDefinitionChildNode = assertFirstChildNode(GroovyTokenTypes.LITERAL_public, rootChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_class, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_extends, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LCURLY, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);

        // some comment
        classDefinitionChildNode = assertNextNodeIsASingleLineComment(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);

        /* Block comment */
        classDefinitionChildNode = assertNextNodeIsAMultiLineComment(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);

        // @Property def author = "Joe Bloggs"
        classDefinitionChildNode = assertNextStatementIsAPropertyDeclaration(GroovyTokenTypes.LITERAL_def, GroovyTokenTypes.STRING_LITERAL, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);

        // @Property Long random = 1101001010010110L
        classDefinitionChildNode = assertNextStatementIsAPropertyDeclaration(GroovyTokenTypes.IDENT, GroovyTokenTypes.NUM_LONG, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);

        // private String field = "Hello World"
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_private, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.STRING_LITERAL, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);

        // private double unusedField = 12345.67890;
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_private, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_double, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_DOUBLE, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsASemiColon(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);

        // private UnknownType anotherString = "Another\nStrin|g";
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_private, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.STRING_LITERAL, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsASemiColon(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);

        // private int[] array = [ 1, 2, 3 ] as int[]
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_private, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_int, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LBRACK, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RBRACK, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LBRACK, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.COMMA, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.COMMA, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RBRACK, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_as, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_int, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LBRACK, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RBRACK, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);

        // public static int staticField = 0
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_public, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_static, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.LITERAL_int, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.IDENT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.ASSIGN, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.NUM_INT, classDefinitionChildNode);
        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);

        // public SomeClass(AnInterface param) {
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.CTOR_IDENT, classDefinitionChildNode);

        ASTNode constructorNode = assertFirstChildNode(GroovyTokenTypes.LITERAL_public, classDefinitionChildNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.IDENT, constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.LPAREN, constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.IDENT, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.IDENT, constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.RPAREN, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.LCURLY, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);

        //TODO: something
        constructorNode = assertNextNodeIsASingleLineComment(constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);

        // int localVar = "IntelliJ"; // Error, incompatible types
        constructorNode = assertNextNode(GroovyTokenTypes.LITERAL_int, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.IDENT, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.ASSIGN, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.STRING_LITERAL, constructorNode);
        constructorNode = assertNextNodeIsASemiColon(constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNodeIsASingleLineComment(constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);

        // println (anotherString + field + localVar)
        constructorNode = assertNextNode(GroovyTokenTypes.IDENT, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.LPAREN, constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.IDENT, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.PLUS, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.IDENT, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.PLUS, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.IDENT, constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.RPAREN, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);

        // long time = Date.parse("1/2/3"); // Method is deprecated
        constructorNode = assertNextNode(GroovyTokenTypes.LITERAL_long, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.IDENT, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.ASSIGN, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.IDENT, constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.DOT, constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.IDENT, constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.LPAREN, constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.STRING_LITERAL, constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.RPAREN, constructorNode);
        constructorNode = assertNextNodeIsASemiColon(constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNodeIsASingleLineComment(constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);

        // int value = this.staticField
        constructorNode = assertNextNode(GroovyTokenTypes.LITERAL_int, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.IDENT, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.ASSIGN, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.LITERAL_this, constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.DOT, constructorNode);
        constructorNode = assertNextNode(GroovyTokenTypes.IDENT, constructorNode);
        constructorNode = assertNextNodeIsAWhiteSpace(constructorNode);

        // }
        constructorNode = assertNextNode(GroovyTokenTypes.RCURLY, constructorNode);
        assertNodeHasNoChildrenAndNoNextSiblings(constructorNode);

        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);

        // interface AnInterface {
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.INTERFACE_DEF, classDefinitionChildNode);

        ASTNode interfaceDefinitionNode = assertFirstChildNode(GroovyTokenTypes.LITERAL_interface, classDefinitionChildNode);
        interfaceDefinitionNode = assertNextNodeIsAWhiteSpace(interfaceDefinitionNode);
        interfaceDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, interfaceDefinitionNode);
        interfaceDefinitionNode = assertNextNodeIsAWhiteSpace(interfaceDefinitionNode);
        interfaceDefinitionNode = assertNextNode(GroovyTokenTypes.LCURLY, interfaceDefinitionNode);
        interfaceDefinitionNode = assertNextNodeIsAWhiteSpace(interfaceDefinitionNode);

        // int CONSTANT = 2
        interfaceDefinitionNode = assertNextNode(GroovyTokenTypes.LITERAL_int, interfaceDefinitionNode);
        interfaceDefinitionNode = assertNextNodeIsAWhiteSpace(interfaceDefinitionNode);
        interfaceDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, interfaceDefinitionNode);
        interfaceDefinitionNode = assertNextNodeIsAWhiteSpace(interfaceDefinitionNode);
        interfaceDefinitionNode = assertNextNode(GroovyTokenTypes.ASSIGN, interfaceDefinitionNode);
        interfaceDefinitionNode = assertNextNodeIsAWhiteSpace(interfaceDefinitionNode);
        interfaceDefinitionNode = assertNextNode(GroovyTokenTypes.NUM_INT, interfaceDefinitionNode);
        interfaceDefinitionNode = assertNextNodeIsAWhiteSpace(interfaceDefinitionNode);

        // }
        interfaceDefinitionNode = assertNextNode(GroovyTokenTypes.RCURLY, interfaceDefinitionNode);
        assertNodeHasNoChildrenAndNoNextSiblings(interfaceDefinitionNode);

        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);

        // void testDoesANumberOfRandomThings() {
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.METHOD_DEF, classDefinitionChildNode);

        ASTNode methodDefinitionNode = assertFirstChildNode(GroovyTokenTypes.LITERAL_void, classDefinitionChildNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LPAREN, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.RPAREN, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LCURLY, methodDefinitionNode);

        // /*  block\n                     comment */
        methodDefinitionNode = assertNextNodeIsAMultiLineComment(methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);

        // new SomeClass()
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LITERAL_new, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LPAREN, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.RPAREN, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);

        // def quadraticClosure = { a, b -> Math.pow(a, 2) + (2 * a * b) + Math.pow(b, 2) }
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LITERAL_def, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.ASSIGN, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LCURLY, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.COMMA, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.CLOSURE_OP, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.DOT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LPAREN, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.COMMA, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.NUM_INT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.RPAREN, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.PLUS, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LPAREN, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.NUM_INT, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.STAR, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.STAR, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.RPAREN, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.PLUS, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.DOT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LPAREN, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.COMMA, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.NUM_INT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.RPAREN, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.RCURLY, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);

        // assert quadraticClosure.call(3, 5) == 64
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LITERAL_assert, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.DOT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LPAREN, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.NUM_INT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.COMMA, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.NUM_INT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.RPAREN, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.EQUAL, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.NUM_INT, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);

        // List aList = [ 3, 5 ]
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.ASSIGN, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LBRACK, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.NUM_INT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.COMMA, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.NUM_INT, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.RBRACK, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);

        // assert aList.collect { i -> return (int) Math.pow(i, 2) } == [ 9, 25 ]
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LITERAL_assert, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.DOT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LCURLY, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.CLOSURE_OP, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LITERAL_return, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LPAREN, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LITERAL_int, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.RPAREN, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.DOT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LPAREN, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.COMMA, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.NUM_INT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.RPAREN, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.RCURLY, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.EQUAL, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.LBRACK, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.NUM_INT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.COMMA, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.NUM_INT, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.RBRACK, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);

        // staticField++
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.IDENT, methodDefinitionNode);
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.INC, methodDefinitionNode);
        methodDefinitionNode = assertNextNodeIsAWhiteSpace(methodDefinitionNode);

        // }
        methodDefinitionNode = assertNextNode(GroovyTokenTypes.RCURLY, methodDefinitionNode);
        assertNodeHasNoChildrenAndNoNextSiblings(methodDefinitionNode);

        classDefinitionChildNode = assertNextNodeIsAWhiteSpace(classDefinitionChildNode);

        // }
        classDefinitionChildNode = assertNextNode(GroovyTokenTypes.RCURLY, classDefinitionChildNode);
        assertNodeHasNoChildrenAndNoNextSiblings(classDefinitionChildNode);

        rootChildNode = assertNextNodeIsAWhiteSpace(rootChildNode);
        assertNodeHasNoNextSibling(rootChildNode);

        assertEquals("PSI tree as text", textToParse, rootNode.getText());
    }

    private ASTNode parse(String textToParse) {
        GroovyLanguage groovyLanguage = GroovyLanguage.findOrCreate();
        ParserDefinition parserDefinition = groovyLanguage.getParserDefinition();
        GroovyPsiBuilder builder = new GroovyPsiBuilder(groovyLanguage, null, null, textToParse);
        return parserDefinition.createParser(null).parse(parserDefinition.getFileNodeType(), builder);
    }

    private ASTNode assertNextStatementIsASimpleImportStatement(ASTNode node) {
        node = assertNextNode(GroovyTokenTypes.IMPORT, node);

        ASTNode importStatementChildNode = assertFirstChildNode(GroovyTokenTypes.LITERAL_import, node);
        importStatementChildNode = assertNextNodeIsAWhiteSpace(importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.IDENT, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.DOT, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.IDENT, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.DOT, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypes.IDENT, importStatementChildNode);
        assertNodeHasNoChildrenAndNoNextSiblings(importStatementChildNode);

        return node;
    }

    private ASTNode assertNextStatementIsAPropertyDeclaration(int propertyType, int propertyValue, ASTNode node) {
        node = assertNextNode(GroovyTokenTypes.AT, node);
        node = assertNextNode(GroovyTokenTypes.IDENT, node);
        node = assertNextNodeIsAWhiteSpace(node);
        node = assertNextNode(propertyType, node);
        node = assertNextNodeIsAWhiteSpace(node);
        node = assertNextNode(GroovyTokenTypes.IDENT, node);
        node = assertNextNodeIsAWhiteSpace(node);
        node = assertNextNode(GroovyTokenTypes.ASSIGN, node);
        node = assertNextNodeIsAWhiteSpace(node);
        node = assertNextNode(propertyValue, node);

        return node;
    }

    private ASTNode assertNextNodeIsAMultiLineComment(ASTNode node) {
        return assertNextNode(GroovyTokenTypes.ML_COMMENT, node);
    }

    private ASTNode assertNextNodeIsASingleLineComment(ASTNode node) {
        return assertNextNode(GroovyTokenTypes.SL_COMMENT, node);
    }

    private ASTNode assertNextNodeIsAWhiteSpace(ASTNode node) {
        return assertNextNode(TokenType.WHITE_SPACE, node);
    }

    private ASTNode assertNextNodeIsASemiColon(ASTNode node) {
        return assertNextNode(GroovyTokenTypes.SEMI, node);
    }
}

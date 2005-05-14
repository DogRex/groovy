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
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

public class GroovyPsiParserTest extends PsiTreeTestCase {

    public void testParsesAGivenScriptIntoATreeOfPsiNodes() {
        String textToParse = "#!/usr/bin/groovy\n\nimport java.util.Collections\n\n";

        ParserDefinition parserDefinition = GroovyLanguage.findOrCreate().getParserDefinition();
        GroovyPsiBuilder builder = new GroovyPsiBuilder(GroovyLanguage.findOrCreate(), null, null, textToParse);

        ASTNode rootNode = parserDefinition.createParser(null).parse(parserDefinition.getFileNodeType(), builder);
        assertSame("file element type", GroovyElementTypes.FILE, rootNode.getElementType());

        ASTNode rootChildNode = assertFirstChildNode(GroovyTokenTypeMappings.getType(GroovyTokenTypes.SH_COMMENT), rootNode);
        rootChildNode = assertNextNode(TokenType.WHITE_SPACE, rootChildNode);
        rootChildNode = assertNextNode(GroovyTokenTypeMappings.getType(GroovyTokenTypes.IMPORT), rootChildNode);

        ASTNode importStatementChildNode = assertFirstChildNode(GroovyTokenTypeMappings.getType(GroovyTokenTypes.LITERAL_import), rootChildNode);
        importStatementChildNode = assertNextNode(TokenType.WHITE_SPACE, importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypeMappings.getType(GroovyTokenTypes.IDENT), importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypeMappings.getType(GroovyTokenTypes.DOT), importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypeMappings.getType(GroovyTokenTypes.IDENT), importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypeMappings.getType(GroovyTokenTypes.DOT), importStatementChildNode);
        importStatementChildNode = assertNextNode(GroovyTokenTypeMappings.getType(GroovyTokenTypes.IDENT), importStatementChildNode);
        assertNodeHasNoChildrenAndNoNextSiblings(importStatementChildNode);

        rootChildNode = assertNextNode(TokenType.WHITE_SPACE, rootChildNode);
        assertNodeHasNoNextSibling(rootChildNode);

        assertEquals("PSI tree as text", textToParse, rootNode.getText());
    }

    /*
     * DO NOT attempt to break the following string into smaller chuncks BUT RATHER install the StringEditor plug-in
     * to get a more readable and editable view of this code snippet.
     */
    private static final String FULL_SCRIPT_WITH_ERRORS =
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
}

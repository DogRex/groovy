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

import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import org.codehaus.groovy.antlr.parser.GroovyTokenTypes;

import org.codehaus.groovy.intellij.psi.GroovyElementTypes;
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

public class GroovyPsiParserTest extends TestCase {

    // FIXME: work in progress!
    public void testParsesAGivenScriptIntoATreeOfPsiNodes() {
        /*
         * DO NOT attempt to break the following string into smaller chuncks BUT RATHER install the StringEditor plug-in
         * to get a more readable and editable view of this code snippet.
         */
//        String textToParse = "#!/usr/bin/groovy\n\nimport javax.swing.JPanel\nimport groovy.swing.SwingBuilder\n\n/*\n * A multi-line comment.\n */\n\ndef f(){\n\t// single-line comment...\n\treturn\n  }\n";
        String textToParse = "#!/usr/bin/groovy\n\n";

        MockApplicationManager.reset();
        ParserDefinition parserDefinition = GroovyLanguage.findOrCreate().getParserDefinition();
        GroovyPsiBuilder builder = new GroovyPsiBuilder(GroovyLanguage.findOrCreate(), null, null, textToParse);

        ASTNode rootNode = parserDefinition.createParser(null).parse(parserDefinition.getFileNodeType(), builder);
        assertSame("file element type", GroovyElementTypes.FILE, rootNode.getElementType());

        ASTNode node = assertFirstChildNode(GroovyTokenTypeMappings.getType(GroovyTokenTypes.SH_COMMENT), rootNode);
        node = assertNextNode(TokenType.WHITE_SPACE, node);
        node = assertNextNode(TokenType.WHITE_SPACE, node);
        assertNodeHasNoChildrenAndNoNextSiblings(node);

        assertEquals("PSI tree as text", textToParse, rootNode.getText());
    }

    private ASTNode assertFirstChildNode(IElementType elementType, ASTNode node) {
        ASTNode firstChild = node.getFirstChildNode();
        assertNodeAttributes(elementType, firstChild);
        return firstChild;
    }

    private ASTNode assertNextNode(IElementType elementType, ASTNode node) {
        ASTNode nextNode = node.getTreeNext();
        assertNodeAttributes(elementType, nextNode);
        return nextNode;
    }

    private void assertNodeAttributes(IElementType elementType, ASTNode node) {
        assertSame("node type", elementType, node.getElementType());
    }

    private void assertNodeHasNoChildrenAndNoNextSiblings(ASTNode node) {
        assertEquals("first child", null, node.getFirstChildNode());
        assertEquals("last child", null, node.getLastChildNode());
        assertEquals("next sibling", null, node.getTreeNext());
    }
}

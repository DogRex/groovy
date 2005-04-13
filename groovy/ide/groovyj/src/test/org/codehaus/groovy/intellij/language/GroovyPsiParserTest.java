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
import com.intellij.lang.PsiBuilder;

import org.codehaus.groovy.intellij.psi.GroovyElementTypes;
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

import antlr.RecognitionException;
import antlr.TokenStreamException;

public class GroovyPsiParserTest extends TestCase {

    public void testParsesAGivenTokenStreamIntoATreeOfPsiNodes() throws TokenStreamException, RecognitionException {
//        String textToParse = "#! A script-header comment.\r\r\tdef f(){\n\t  return\r\n    }\r\r\n";
        String textToParse = "#! A script-header comment.\r\r";

        MockApplicationManager.reset();
        ParserDefinition parserDefinition = GroovyLanguage.findOrCreate().getParserDefinition();

        GroovyLexerAdapter lexer = ((GroovyLexerAdapter) parserDefinition.createLexer(null));
        PsiBuilder psiBuilder = new GroovyPsiBuilder(GroovyLanguage.findOrCreate(), null, null, textToParse);
        lexer.start(psiBuilder);

        System.out.println("token type: " + psiBuilder.getTokenType());

        ASTNode rootNode = parserDefinition.createParser(null).parse(parserDefinition.getFileNodeType(), psiBuilder);
        assertSame("file element type", GroovyElementTypes.FILE, rootNode.getElementType());

        // FIXME: in progress!
/*
        ASTNode firstChild = assertFirstChildNode(GroovyTokenTypes.SH_COMMENT, rootNode, "#! This is a script-header comment.");
        ASTNode nextChild = assertNextNode(GroovyTokenTypes.NLS, firstChild, "\r");
        nextChild = assertNextNode(GroovyTokenTypes.NLS, nextChild, "\r");
        nextChild = assertNextNode(GroovyTokenTypes.WS, nextChild, "\t");
        nextChild = assertNextNode(GroovyTokenTypes.METHOD_DEF, nextChild, "def f(){\n\t  return\r\n    }");

        assertEquals("PSI tree as text", textToParse, rootNode.getText());
*/
    }

    private ASTNode assertFirstChildNode(int tokenTypeIndex, ASTNode astNode, String expectedText) {
        ASTNode firstChild = astNode.getFirstChildNode();
        assertNodeEquals(tokenTypeIndex, firstChild, expectedText);
        return firstChild;
    }

    private ASTNode assertNextNode(int tokenTypeIndex, ASTNode astNode, String expectedText) {
        ASTNode nextNode = astNode.getTreeNext();
        assertNodeEquals(tokenTypeIndex, nextNode, expectedText);
        return nextNode;
    }

    private void assertNodeEquals(int tokenTypeIndex, ASTNode astNode, String expectedText) {
        assertEquals("node type", GroovyTokenTypeMappings.getType(tokenTypeIndex), astNode.getElementType());
        assertEquals("node text", expectedText, astNode.getText());
    }
}

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

import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;

import antlr.RecognitionException;
import antlr.TokenStreamException;

public class GroovyPsiParserTest extends TestCase {

    public void testParsesAGivenTokenStreamIntoATreeOfPsiNodes() throws TokenStreamException, RecognitionException {
        String textToParse = "#! This is a script-header comment.\r\r\tdef f(){\n\t  return\r\n    }\r\r\n";

        MockApplicationManager.reset();
        ParserDefinition parserDefinition = GroovyLanguage.findOrCreate().getParserDefinition();
        PsiBuilder psiBuilder = new GroovyPsiBuilder(GroovyLanguage.findOrCreate(), null, null, textToParse);
        ((GroovyLexerAdapter) parserDefinition.createLexer(null)).start(psiBuilder);
/*
        FIXME: in progress!

        ASTNode astNode = parserDefinition.createParser(null).parse(parserDefinition.getFileNodeType(), psiBuilder);
        assertSame("file element type", GroovyElementTypes.FILE, astNode.getElementType());
        assertSame("script header element type", GroovyTokenTypeMappings.getType(GroovyTokenTypes.SH_COMMENT), astNode.getFirstChildNode().getElementType());
        assertEquals("PSI tree as text", textToParse, astNode.getText());
*/
    }
}

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
import com.intellij.lang.PsiParser;
import com.intellij.lang.impl.PsiBuilderImpl;
import com.intellij.psi.impl.source.CharTableImpl;

import antlr.RecognitionException;
import antlr.TokenStreamException;

public class GroovyPsiParserTest extends TestCase {

    public void testNoInformationKnownAboutThisTest() throws TokenStreamException, RecognitionException {
        MockApplicationManager.reset();

        ParserDefinition parserDefinition = GroovyLanguage.findOrCreate().getParserDefinition();
        PsiParser psiParser = parserDefinition.createParser(null);

        String textToParse = "def f(){return }";
        PsiBuilder psiBuilder = new PsiBuilderImpl(GroovyLanguage.findOrCreate(), null, new CharTableImpl(), textToParse);

//        ASTNode astNode = psiParser.parse(parserDefinition.getFileNodeType(), psiBuilder);
//        assertEquals("groovy idea ast", textToParse, astNode.getText());
    }
}

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

import junitx.framework.Assert;
import junitx.framework.ObjectAssert;

import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiParser;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.testFramework.MockVirtualFile;

import org.jmock.Mock;

import org.picocontainer.MutablePicoContainer;

import org.codehaus.groovy.intellij.GroovyjTestCase;
import org.codehaus.groovy.intellij.psi.GroovyElementTypes;
import org.codehaus.groovy.intellij.psi.GroovyFile;
import org.codehaus.groovy.intellij.psi.GroovyTokenSets;

public class GroovyParserDefinitionTest extends GroovyjTestCase {

    private final Mock mockLanguageToolsFactory = mock(GroovyLanguageToolsFactory.class);

    private final GroovyParserDefinition parserDefinition = new GroovyParserDefinition((GroovyLanguageToolsFactory) mockLanguageToolsFactory.proxy());

    private MutablePicoContainer picoContainer;

    protected void setUp() {
        MockApplicationManager.reset();

        picoContainer = (MutablePicoContainer) MockApplicationManager.getMockApplication().getPicoContainer();
        picoContainer.unregisterComponent(GroovyLanguageToolsFactory.class);
        picoContainer.registerComponentInstance(GroovyLanguageToolsFactory.class, mockLanguageToolsFactory.proxy());
    }

    protected void tearDown() {
        picoContainer.unregisterComponent(GroovyLanguageToolsFactory.class);
        picoContainer.registerComponentImplementation(GroovyLanguageToolsFactory.class);
    }

    public void testCreatesALexerForGroovy() {
        GroovyLexerAdapter expectedLexer = new GroovyLexerAdapter();
        mockLanguageToolsFactory.expects(once()).method("createLexer").will(returnValue(expectedLexer));
        assertSame("lexer", expectedLexer, parserDefinition.createLexer(null));
    }

    public void testCreatesAPsiParserForGroovy() {
        GroovyPsiParser expectedParser = new GroovyPsiParser();
        mockLanguageToolsFactory.expects(once()).method("createParser").will(returnValue(expectedParser));
        assertSame("PSI parser", expectedParser, parserDefinition.createParser(null));
    }

    public void testUsesTheGroovyFileElementTypeAsTheFileNodeType() {
        assertSame("file node type", GroovyElementTypes.FILE, parserDefinition.getFileNodeType());
    }

    public void testProducesWhitespaceTokensForGroovy() {
        assertSame("whitespace tokens", GroovyTokenSets.WHITESPACES, parserDefinition.getWhitespaceTokens());
    }

    public void testProducesCommentTokensForGroovy() {
        assertSame("comment tokens", GroovyTokenSets.COMMENTS, parserDefinition.getCommentTokens());
    }

    public void testCreatesAGroovyPsiElementFromAnAstNode() {
        ASTNode expectedAstNode = (ASTNode) mock(ASTNode.class).proxy();
        PsiElement element = parserDefinition.createElement(expectedAstNode);

        Assert.assertNotEquals("psi element", null, element);
        assertSame("psi element", expectedAstNode, element.getNode());
    }

    public void testCreatesAGroovyFileFromAProjectAndVirtualFile() {
        PsiFile psiFile = parserDefinition.createFile(createStubbedProject(), new MockVirtualFile());
        ObjectAssert.assertInstanceOf("groovy file", GroovyFile.class, psiFile);
    }

    public void testCreatesAGroovyFileFromAProjectANameAndACharacterSequence() {
        mockLanguageToolsFactory.expects(once()).method("createLexer").will(returnValue(new GroovyLexerAdapter()));

        Mock stubPsiParser = mock(PsiParser.class);
        mockLanguageToolsFactory.expects(once()).method("createParser").will(returnValue(stubPsiParser.proxy()));
        stubPsiParser.stubs().method("parse").withAnyArguments().will(returnValue(new FileElement(GroovyElementTypes.FILE)));

        PsiFile psiFile = parserDefinition.createFile(createStubbedProject(), "fileName", "fileContents");
        ObjectAssert.assertInstanceOf("groovy file", GroovyFile.class, psiFile);
    }
}

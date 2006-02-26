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


package org.codehaus.groovy.intellij.language.editor;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.tree.IElementType;

import org.jmock.Mock;

import org.codehaus.groovy.intellij.GroovyjTestCase;
import org.codehaus.groovy.intellij.language.parser.GroovyTokenTypes;
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

public class GroovyFoldingBuilderTest extends GroovyjTestCase {

    private final GroovyFoldingBuilder foldingBuilder = new GroovyFoldingBuilder();

    public void testBuildsDescriptorsOfFoldingRegionsForARootAstNodeIncludingCodeBlocksAndMultiLineComments() {
        ASTNode methodBodyNode = createAstNode(GroovyTokenTypes.BLOCK);
        ASTNode methodDefinitionNode = createAstNode(GroovyTokenTypes.METHOD_DEF, methodBodyNode);
        ASTNode methodNode = createAstNode(GroovyTokenTypes.ML_COMMENT, methodDefinitionNode);
        ASTNode classBodyNode = createAstNode(GroovyTokenTypes.BLOCK, methodNode);
        ASTNode classDefinitionNode = createAstNode(GroovyTokenTypes.ML_COMMENT, classBodyNode);

        FoldingDescriptor[] foldingDescriptors = foldingBuilder.buildFoldRegions(classDefinitionNode, null);
        assertNotNull("folding descriptors should not be null", foldingDescriptors);
        assertEquals("number of folding descriptors", 4, foldingDescriptors.length);

        assertSame("node of folding descriptor #1", classDefinitionNode, foldingDescriptors[0].getElement());
        assertSame("node of folding descriptor #2", classBodyNode,       foldingDescriptors[1].getElement());
        assertSame("node of folding descriptor #3", methodNode,          foldingDescriptors[2].getElement());
        assertSame("node of folding descriptor #4", methodBodyNode,      foldingDescriptors[3].getElement());
    }

    public void testDoesNotSuggestThatANodeBeCollapsedByDefault() {
        assertEquals("collapsed by default", false, foldingBuilder.isCollapsedByDefault(null));
    }

    public void testSuggestsAWellKnownPlaceholderForAnAstNodeRepresentingMultiLineComments() {
        ASTNode multiLineCommentsNode = createAstNode(GroovyTokenTypes.ML_COMMENT);
        assertEquals("placeholder for multi-line comments", "/*...*/", foldingBuilder.getPlaceholderText(multiLineCommentsNode));
    }

    public void testSuggestsAWellKnownPlaceholderForAnAstNodeRepresentingACodeBlock() {
        ASTNode codeBlockNode = createAstNode(GroovyTokenTypes.BLOCK);
        assertEquals("placeholder for code blocks", "{...}", foldingBuilder.getPlaceholderText(codeBlockNode));
    }

    public void testReturnsNullAsThePlaceholderForAnAstNodeWhichShouldNeverBeFolded() {
        ASTNode codeBlockNode = createAstNode(GroovyTokenTypes.NUM_DOUBLE);
        assertEquals("default placeholder", null, foldingBuilder.getPlaceholderText(codeBlockNode));
    }

    private ASTNode createAstNode(int tokenTypeIndex) {
        return createAstNode(tokenTypeIndex, null);
    }

    private ASTNode createAstNode(int tokenTypeIndex, ASTNode astNode) {
        Mock stubAstNode = mock(ASTNode.class);
        IElementType codeBlockElementType = GroovyTokenTypeMappings.getType(tokenTypeIndex);
        stubAstNode.stubs().method("getElementType").will(returnValue(codeBlockElementType));
        stubAstNode.stubs().method("getTextRange").will(returnValue(new TextRange(-1, -1)));
        stubAstNode.stubs().method("getFirstChildNode").will(returnValue(astNode));
        stubAstNode.stubs().method("getTreeNext").will(returnValue(null));
        return (ASTNode) stubAstNode.proxy();
    }
}

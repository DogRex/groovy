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
import com.intellij.psi.tree.IElementType;

import org.codehaus.groovy.intellij.GroovyjTestCase;
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

public abstract class PsiTreeTestCase extends GroovyjTestCase {

    protected ASTNode assertFirstChildNode(int tokenTypeIndex, ASTNode node) {
        return assertFirstChildNode(GroovyTokenTypeMappings.getType(tokenTypeIndex), node);
    }

    protected ASTNode assertFirstChildNode(IElementType elementType, ASTNode node) {
        ASTNode firstChild = node.getFirstChildNode();
        assertNodeAttributes(elementType, firstChild);
        return firstChild;
    }

    protected ASTNode assertNextNode(int tokenTypeIndex, ASTNode node) {
        return assertNextNode(GroovyTokenTypeMappings.getType(tokenTypeIndex), node);
    }

    protected ASTNode assertNextNode(IElementType elementType, ASTNode node) {
        ASTNode nextNode = node.getTreeNext();
        assertNodeAttributes(elementType, nextNode);
        return nextNode;
    }

    private void assertNodeAttributes(IElementType elementType, ASTNode node) {
        assertSame("node type", elementType, node.getElementType());
    }

    protected void assertNodeHasNoNextSibling(ASTNode node) {
        assertEquals("next sibling", null, node.getTreeNext());
    }

    protected void assertNodeHasNoChildrenAndNoNextSiblings(ASTNode node) {
        assertEquals("first child", null, node.getFirstChildNode());
        assertEquals("last child", null, node.getLastChildNode());
        assertNodeHasNoNextSibling(node);
    }
}

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


package org.codehaus.groovy.intellij.language.editor;

import java.util.ArrayList;
import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;

import org.codehaus.groovy.intellij.language.parser.GroovyTokenTypes;
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

public class GroovyFoldingBuilder implements FoldingBuilder {

    public FoldingDescriptor[] buildFoldRegions(ASTNode node, Document document) {
        List<FoldingDescriptor> descriptors = new ArrayList<FoldingDescriptor>();
        appendDescriptors(node, document, descriptors);
        return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
    }

    private void appendDescriptors(ASTNode node, Document document, List<FoldingDescriptor> descriptors) {
        if (isCodeBlock(node) || isMultiLineComment(node)) {
            descriptors.add(new FoldingDescriptor(node, node.getTextRange()));
        }

        for (ASTNode child = node.getFirstChildNode(); child != null; child = child.getTreeNext()) {
            appendDescriptors(child, document, descriptors);
        }
    }

    public String getPlaceholderText(ASTNode node) {
        if (isMultiLineComment(node)) {
            return "/*...*/";
        } else if (isCodeBlock(node)) {
            return "{...}";
        }
        return null;
    }

    public boolean isCollapsedByDefault(ASTNode node) {
        return false;
    }

    private boolean isMultiLineComment(ASTNode node) {
        return node.getElementType() == GroovyTokenTypeMappings.getType(GroovyTokenTypes.ML_COMMENT);
    }

    private boolean isCodeBlock(final ASTNode node) {
        return node.getElementType() == GroovyTokenTypeMappings.getType(GroovyTokenTypes.BLOCK);
    }
}

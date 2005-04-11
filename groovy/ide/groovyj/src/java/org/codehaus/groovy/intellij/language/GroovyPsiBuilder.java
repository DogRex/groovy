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

import java.util.ArrayList;
import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.impl.source.tree.PsiErrorElementImpl;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.CharTable;

public class GroovyPsiBuilder implements PsiBuilder {

    private static final Logger LOGGER = Logger.getInstance("#org.codehaus.groovy.intellij.language.GroovyPsiBuilder");

    private final List tokens = new ArrayList();
    private final ListWithRemovableRange markers = new ListWithRemovableRange();
    private final GroovyLexerAdapter lexer;
    private final boolean charTableNotAvailable;
    private final TokenSet whitespaceTokens;
    private final TokenSet commentTokens;
    private CharTable charTable;
    private int currentTokenIndex;
    private CharSequence originalText;

    public GroovyPsiBuilder(Language language, Project project, CharTable charTable, CharSequence originalText) {
        this.originalText = originalText;

        ParserDefinition parserDefinition = language.getParserDefinition();
        lexer = (GroovyLexerAdapter) parserDefinition.createLexer(project);
        whitespaceTokens = parserDefinition.getWhitespaceTokens();
        commentTokens = parserDefinition.getCommentTokens();
        this.charTable = charTable;
        charTableNotAvailable = charTable == null;
    }

    private Marker preceed(StartMarker startMarker) {
        int k = markers.lastIndexOf(startMarker);
        LOGGER.assertTrue(k >= 0, "Cannot preceed dropped or rolled-back marker");
        StartMarker startMarkerCopy = new StartMarker(startMarker.lexemIndex);
        markers.add(k, startMarkerCopy);
        return startMarkerCopy;
    }

    public Lexer getLexer() {
        return lexer;
    }

    public CharSequence getOriginalText() {
        return originalText;
    }

    public void advanceLexer() {
        currentTokenIndex++;
        lexer.advance();
    }

    public IElementType getTokenType() {
        Token token = currentToken();
        return token == null ? null : token.getTokenType();
    }

    public int getCurrentOffset() {
        Token token = currentToken();
        return (token == null) ? getOriginalText().length() : token.startOffset;
    }

    public Token getCurrentToken() {
        Token token = currentToken();
        return (token == null) ? null : token;
    }

    public Marker mark() {
        StartMarker startMarker = new StartMarker(currentTokenIndex);
        markers.add(startMarker);
        return startMarker;
    }

    public boolean eof() {
        if (currentTokenIndex + 1 < tokens.size()) {
            return false;
        }
        return currentToken() == null;
    }

    private Token currentToken() {
        if (currentTokenIndex >= tokens.size()) {
            if (lexer.getTokenType() == null) {
                return null;
            }
            tokens.add(new Token());
        }
        return (Token) tokens.get(currentTokenIndex);
    }

    public void rollbackTo(Marker marker) {
        currentTokenIndex = ((StartMarker) marker).lexemIndex;
        int markerIndex = markers.lastIndexOf(marker);
        LOGGER.assertTrue(markerIndex >= 0, "The marker must be added before rolled back to.");
        markers.removeRange(markerIndex, markers.size());
    }

    public void drop(Marker marker) {
        boolean markerRemoved = markers.remove(markers.lastIndexOf(marker)) == marker;
        LOGGER.assertTrue(markerRemoved, "The marker must be added before it is dropped.");
    }

    public void done(Marker marker) {
        LOGGER.assertTrue(((StartMarker) marker).doneMarker == null, "Marker already done.");

        int markerIndex = markers.lastIndexOf(marker);
        LOGGER.assertTrue(markerIndex >= 0, "Marker has never been added.");

        for (int i = markers.size() - 1; i > markerIndex; i--) {
            ProductionMarker productionMarker = (ProductionMarker) markers.get(i);
            if (!(productionMarker instanceof Marker)) {
                continue;
            }
            StartMarker startMarker = (StartMarker) productionMarker;
            if (startMarker.doneMarker == null) {
                LOGGER.error("Another not done marker of type [" + startMarker.elementType + "] added after this one. Must be done before this.");
            }
        }

        DoneMarker doneMarker = new DoneMarker((StartMarker) marker, currentTokenIndex);
        ((StartMarker) marker).doneMarker = doneMarker;
        markers.add(doneMarker);
    }

    public void error(String message) {
        if ((ProductionMarker) markers.get(markers.size() - 1) instanceof ErrorItem) {
            return;
        }
        markers.add(new ErrorItem(message, currentTokenIndex));
    }

    public ASTNode getTreeBuilt() {
        StartMarker startMarker = (StartMarker) markers.get(0);

        ASTNode node;
        if (charTableNotAvailable) {
            node = new FileElement(startMarker.elementType);
            charTable = ((FileElement) node).getCharTable();
        } else {
            node = new CompositeElement(startMarker.elementType);
            node.putUserData(CharTable.CHAR_TABLE_KEY, charTable);
        }

        for (int k = 1; k < markers.size() - 1; k++) {
            ProductionMarker productionMarker = (ProductionMarker) markers.get(k);
            if (productionMarker instanceof StartMarker) {
                for (; productionMarker.lexemIndex < tokens.size() && whitespaceTokens.isInSet(((Token) tokens.get(productionMarker.lexemIndex)).getTokenType()); productionMarker.lexemIndex++);
                continue;
            }

            if (!(productionMarker instanceof DoneMarker) && !(productionMarker instanceof ErrorItem)) {
                continue;
            }

            for (int i1 = ((ProductionMarker) markers.get(k - 1)).lexemIndex;
                 productionMarker.lexemIndex > i1 && productionMarker.lexemIndex < tokens.size() && whitespaceTokens.isInSet(((Token) tokens.get(productionMarker.lexemIndex - 1)).getTokenType());
                 productionMarker.lexemIndex--);
        }

        ASTNode nodeCopy = node;
        int l = 0;
        int j1 = -1;
        for (int k = 1; k < markers.size(); k++) {
            ProductionMarker productionMarker = (ProductionMarker) markers.get(k);
            LOGGER.assertTrue(nodeCopy != null, "Unexpected end of the production");
            int l1 = productionMarker.lexemIndex;
            if (productionMarker instanceof StartMarker) {
                StartMarker startMarker2 = (StartMarker) productionMarker;
                l = a(l, l1, nodeCopy);
                CompositeElement compositeElement = new CompositeElement(startMarker2.elementType);
                TreeUtil.addChildren((CompositeElement) nodeCopy, compositeElement);
                nodeCopy = compositeElement;
                continue;
            }
            if (productionMarker instanceof DoneMarker) {
                DoneMarker donemarker = (DoneMarker) productionMarker;
                l = a(l, l1, nodeCopy);
                LOGGER.assertTrue(donemarker.startMarker.elementType == nodeCopy.getElementType());
                nodeCopy = nodeCopy.getTreeParent();
                continue;
            }
            if (!(productionMarker instanceof ErrorItem)) {
                continue;
            }
            l = a(l, l1, nodeCopy);
            if (l != j1) {
                j1 = l;
                PsiErrorElementImpl psierrorelementimpl = new PsiErrorElementImpl();
                psierrorelementimpl.setErrorDescription(((ErrorItem) productionMarker).message);
                TreeUtil.addChildren((CompositeElement) nodeCopy, psierrorelementimpl);
            }
        }

        LOGGER.assertTrue(l == tokens.size(), "Not all of the tokens inserted to the tree");
        LOGGER.assertTrue(nodeCopy == null, "Unbalanced tree");
        return node;
    }

    private int a(int k, int index, ASTNode astnode) {
        for (index = Math.min(index, tokens.size()); k < index;) {
            Token token = (Token) tokens.get(k++);
            LeafPsiElement leafPsiElement = findLeafPsiElement(token);
            TreeUtil.addChildren((CompositeElement) astnode, leafPsiElement);
        }

        return k;
    }

    private LeafPsiElement findLeafPsiElement(Token token) {
        IElementType elementType = token.getTokenType();
        if (whitespaceTokens.isInSet(elementType)) {
            return new PsiWhiteSpaceImpl(lexer.getBuffer(), token.startOffset, token.endOffset, token.state, charTable);
        }
        if (commentTokens.isInSet(elementType)) {
            return new PsiCommentImpl(elementType, lexer.getBuffer(), token.startOffset, token.endOffset, token.state, charTable);
        }
        return new LeafPsiElement(elementType, lexer.getBuffer(), token.startOffset, token.endOffset, token.state, charTable);
    }

    private static class ListWithRemovableRange extends ArrayList {

        public void removeRange(int fromIndex, int toIndex) {
            super.removeRange(fromIndex, toIndex);
        }
    }

    public class Token {

        private final IElementType tokenType;
        private final int startOffset;
        private final int endOffset;
        private final int state;

        public Token() {
            tokenType = lexer.getTokenType();
            startOffset = lexer.getTokenStart();
            endOffset = lexer.getTokenEnd();
            state = lexer.getState();
        }

        public IElementType getTokenType() {
            return tokenType;
        }

        public String getTokenText() {
            return new String(lexer.getBuffer(), startOffset, endOffset - startOffset);
        }
    }

    private static class ProductionMarker {

        int lexemIndex;

        public ProductionMarker(int k) {
            lexemIndex = k;
        }
    }

    private class StartMarker extends ProductionMarker implements Marker {

        public IElementType elementType;
        public DoneMarker doneMarker;

        public Marker preceed() {
            return GroovyPsiBuilder.this.preceed(this);
        }

        public void drop() {
            GroovyPsiBuilder.this.drop(this);
        }

        public void rollbackTo() {
            GroovyPsiBuilder.this.rollbackTo(this);
        }

        public void done(IElementType elementType) {
            this.elementType = elementType;
            GroovyPsiBuilder.this.done(this);
        }

        public StartMarker(int k) {
            super(k);
            doneMarker = null;
        }
    }

    private static class ErrorItem extends ProductionMarker {

        private final String message;

        public ErrorItem(String message, int k) {
            super(k);
            this.message = message;
        }
    }

    private static class DoneMarker extends ProductionMarker {

        public final StartMarker startMarker;

        public DoneMarker(StartMarker startMarker, int k) {
            super(k);
            this.startMarker = startMarker;
        }
    }
}

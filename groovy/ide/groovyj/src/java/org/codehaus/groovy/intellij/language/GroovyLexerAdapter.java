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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.text.CharArrayUtil;

import org.codehaus.groovy.antlr.SourceBuffer;
import org.codehaus.groovy.antlr.UnicodeEscapingReader;

import org.codehaus.groovy.intellij.language.parser.GroovyLexer;
import org.codehaus.groovy.intellij.language.parser.GroovyTokenTypes;
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

import antlr.CommonHiddenStreamToken;
import antlr.LexerSharedInputState;
import antlr.TokenStreamException;

public class GroovyLexerAdapter extends LexerBase {

    private GroovyPsiBuilder builder;
    private GroovyLexer adaptee;
    private char[] buffer;
    private int currentTokenStartOffset;
    private int bufferEndOffset;
    private SourceBuffer sourceBuffer;
    private List<String> processedTokens;

    private int currentLineStartPosition;
    private CommonHiddenStreamToken currentToken;

    void bind(GroovyPsiBuilder builder) {
        this.builder = builder;
        adaptee = new GroovyLexer(builder);
        adaptee.setCaseSensitive(true);
        adaptee.setWhitespaceIncluded(true);

        sourceBuffer = new SourceBuffer();
        processedTokens = new ArrayList<String>();
        start(CharArrayUtil.fromSequence(builder.getOriginalText()));
    }

    public void start(char[] buffer) {
        start(buffer, 0, buffer.length);
    }

    public void start(char[] buffer, int startOffset, int endOffset) {
        start(buffer, startOffset, endOffset, 0);
    }

    public void start(char[] buffer, int startOffset, int endOffset, int initialState) {
        this.buffer = buffer;
        currentTokenStartOffset = startOffset;  // index of the first character of the current token relative to the full buffer
        bufferEndOffset = endOffset;            // index of the last character in the full buffer
        currentLineStartPosition = startOffset; // index of the first character on the current line

        String input = new String(buffer, startOffset, endOffset - startOffset);
        UnicodeEscapingReader reader = new UnicodeEscapingReader(new StringReader(input), getSourceBuffer());
        reader.setLexer(getAdaptedLexer());
        getAdaptedLexer().setInputState(new LexerSharedInputState(reader));
    }

    public GroovyLexer getAdaptedLexer() {
        return adaptee;
    }

    public SourceBuffer getSourceBuffer() {
        return sourceBuffer;
    }

    public char[] getBuffer() {
        return buffer;
    }

    public int getBufferEnd() {
        return bufferEndOffset;
    }

    // Never used - possibly a leftover from JFlex...
    public int getState() {
        return 0;
    }

    public IElementType getTokenType() {
        CommonHiddenStreamToken token = currentToken;
        if (token == null || token.getType() == GroovyTokenTypes.EOF) {
            return null;
        }

        return GroovyTokenTypeMappings.getType(token.getType());
    }

    public int getTokenStart() {
        return currentTokenStartOffset;
    }

    public int getTokenEnd() {
        return currentTokenStartOffset + currentToken.getText().length();
    }

    public CommonHiddenStreamToken getCurrentToken() {
        return currentToken;
    }

    public void advance() {
        try {
            lexerAdvanced((CommonHiddenStreamToken) getAdaptedLexer().nextToken());
        } catch (TokenStreamException e) {
            throw new RuntimeException(e);
        }
    }

    public void lexerAdvanced(CommonHiddenStreamToken nextToken) {
        if (!processedTokens.contains(nextToken.toString())) {
            processedTokens.add(nextToken.toString());
            updateStartingLineNumber();
            updateStartOffset(nextToken);
            addToPsiBuilder(nextToken);
            processHiddenTokens(nextToken);
        }
    }

    private void updateStartingLineNumber() {
        if (currentToken != null) {
            if (currentToken.getType() == GroovyTokenTypes.NLS) {
                advanceNewLine();
            } else if (currentToken.getType() == GroovyTokenTypes.ML_COMMENT) {
                advanceMultiLineComment();
            }
        }
    }

    private void updateStartOffset(CommonHiddenStreamToken nextToken) {
        currentToken = nextToken;
        // columns start at 1, currentTokenStartOffset starts at 0
        currentTokenStartOffset = currentLineStartPosition + currentToken.getColumn() - 1;
    }

    private void addToPsiBuilder(CommonHiddenStreamToken token) {
        if (token.getType() != GroovyTokenTypes.EOF) {
            builder.addToken(getTokenType(), getTokenStart(), getTokenEnd(), token.getText());
        }
    }

    private void processHiddenTokens(CommonHiddenStreamToken significantToken) {
        for (CommonHiddenStreamToken token = significantToken.getHiddenAfter(); token != null; token = token.getHiddenAfter()) {
            lexerAdvanced(token);
        }
    }

    private void advanceNewLine() {
        int lastLineSeparatorIndex = findLastIndexOfALineSeparator(currentToken.getText());
        currentLineStartPosition = currentTokenStartOffset + lastLineSeparatorIndex + 1;
    }

    private void advanceMultiLineComment() {
        // ML_COMMENT may contain any number of NLS tokens and may end with one
        int lastLineSeparatorIndex = findLastIndexOfALineSeparator(currentToken.getText());
        if (lastLineSeparatorIndex > -1) {
            currentLineStartPosition = currentTokenStartOffset + lastLineSeparatorIndex + 1;
        }
    }

    private int findLastIndexOfALineSeparator(String text) {
        int lastIndexOfALineSeparator = text.lastIndexOf("\r\n");    // Windows
        if (lastIndexOfALineSeparator == -1) {
            lastIndexOfALineSeparator = text.lastIndexOf("\r");      // Macintosh
        }
        if (lastIndexOfALineSeparator == -1) {
            lastIndexOfALineSeparator = text.lastIndexOf("\n");      // Unix
        }
        return lastIndexOfALineSeparator;
    }
}

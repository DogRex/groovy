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

import java.io.Reader;
import java.io.StringReader;

import com.intellij.lang.PsiBuilder;
import com.intellij.lexer.Lexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.text.CharArrayUtil;

import org.codehaus.groovy.antlr.parser.GroovyTokenTypes;

import org.codehaus.groovy.intellij.language.parser.GroovyPsiLexer;
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

import antlr.LexerSharedInputState;
import antlr.Token;
import antlr.TokenStream;
import antlr.TokenStreamException;

public class GroovyLexerAdapter implements Lexer {

    private GroovyPsiLexer adaptee;
    private char[] buffer;
    private int currentTokenStartOffset;
    private int bufferEndOffset;

    private int currentLineStartPosition;
    private Token currentToken;

    void bind(PsiBuilder builder) {
        adaptee = new GroovyPsiLexer(builder);
        adaptee.setCaseSensitive(true);
        adaptee.setWhitespaceIncluded(true);
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
/*
        UnicodeEscapingReader reader = new UnicodeEscapingReader(new StringReader(new String(buffer, startOffset, endOffset - startOffset)));
        reader.setLexer(groovyLexer);
*/
        Reader reader = new StringReader(new String(buffer, startOffset, endOffset - startOffset));
        adaptee.setInputState(new LexerSharedInputState(reader));
        advance();
    }

    public TokenStream stream() {
        return adaptee.plumb();
    }

    public char[] getBuffer() {
        return buffer;
    }

    public int getBufferEnd() {
        return bufferEndOffset;
    }

    /*
     * Seems to be called after getTokenEnd() and before advance().
     * This may be to allow a pushback mechanism. Not used here.
     */
    public int getState() {                     // leftover from JFlex it seems...
        return 0;
    }

    public IElementType getTokenType() {
        Token token = currentToken;
        if (token == null || token.getType() == GroovyTokenTypes.EOF) {
            return null;
        }

        return GroovyTokenTypeMappings.getType(token.getType());
    }

    public int getTokenStart() {
        return currentTokenStartOffset;
    }

    public int getTokenEnd() {
        return currentToken.getType() == GroovyTokenTypes.NLS
               ? currentTokenStartOffset + 1
               : currentTokenStartOffset + currentToken.getText().length();
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public void advance() {
        if (currentToken != null) {
            if (currentToken.getType() == GroovyTokenTypes.NLS) {
                advanceNewLine();
            } else if (currentToken.getType() == GroovyTokenTypes.ML_COMMENT) {
                advanceMultiLineComment();
            }
        }

        try {
            currentToken = adaptee.nextToken();
            // columns start at 1, currentTokenStartOffset starts at 0
            currentTokenStartOffset = currentLineStartPosition + currentToken.getColumn() - 1;
        } catch (TokenStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private void advanceNewLine() {
        currentLineStartPosition = currentTokenStartOffset + 1;
    }

    private void advanceMultiLineComment() {
        // ML_COMMENTS may contain any number of NLS and may end with one
        int lastLineSeparatorIndex = findLastIndexOfALineSeparator(currentToken.getText());
        if (lastLineSeparatorIndex > -1) {
            currentLineStartPosition = currentTokenStartOffset + lastLineSeparatorIndex + 1;
        }
    }

    private int findLastIndexOfALineSeparator(String text) {
        int lastIndexOfALineSeparator = text.lastIndexOf("\r\r\n");  // legacy Netscape line separator
        if (lastIndexOfALineSeparator == -1) {
            lastIndexOfALineSeparator = text.lastIndexOf("\r\n");    // Windows
        }
        if (lastIndexOfALineSeparator == -1) {
            lastIndexOfALineSeparator = text.lastIndexOf("\r");      // Macintosh
        }
        if (lastIndexOfALineSeparator == -1) {
            lastIndexOfALineSeparator = text.lastIndexOf("\n");      // Unix
        }
        return lastIndexOfALineSeparator;
    }
}

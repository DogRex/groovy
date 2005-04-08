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

import com.intellij.lexer.Lexer;
import com.intellij.psi.tree.IElementType;

import org.codehaus.groovy.antlr.UnicodeEscapingReader;
import org.codehaus.groovy.antlr.parser.GroovyLexer;
import org.codehaus.groovy.antlr.parser.GroovyTokenTypes;

import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

import antlr.LexerSharedInputState;
import antlr.Token;
import antlr.TokenStreamException;

public class GroovyLexerAdapter implements Lexer {

    // newlines seem to be normalized to \n in virtual files
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public final GroovyLexer groovyLexer;

    private char[] buffer;
    private int bufferStart;
    private int bufferEnd;

    private int currentLineStartPosition;
    private int currentLineNumber;              // has yet to be used?
    private Token currentToken;

    public GroovyLexerAdapter() {
        groovyLexer = new GroovyLexer((Reader) null);
        groovyLexer.setCaseSensitive(true);
        groovyLexer.setWhitespaceIncluded(true);
    }

    public void start(char[] buffer) {
        start(buffer, 0, buffer.length);
    }

    public void start(char[] buffer, int startOffset, int endOffset) {
        start(buffer, startOffset, endOffset, 0);
    }

    public void start(char[] buffer, int startOffset, int endOffset, int initialState) {
        System.out.println("in start(char[] buffer, int startOffset, int endOffset, int initialState)");
        System.out.println("buffer        = " + new String(buffer).toString());
        System.out.println("buffer.length = " + buffer.length);
        System.out.println("startOffset   = " + startOffset);
        System.out.println("endOffset     = " + endOffset);
        System.out.println("initialState  = " + initialState);

        this.buffer = buffer;
        bufferStart = startOffset;              // index of the first character of the current token relative to the full buffer
        bufferEnd = endOffset;                  // index of the last character of the current token relative to the full buffer
        currentLineStartPosition = startOffset; // index of the first character on the current line
        currentLineNumber = 1;

        try {
            UnicodeEscapingReader reader = new UnicodeEscapingReader(new StringReader(new String(buffer, startOffset, endOffset - startOffset)));
            reader.setLexer(groovyLexer);

            groovyLexer.setInputState(new LexerSharedInputState(reader));
            advance();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public char[] getBuffer() {
        return buffer;
    }

    public int getBufferEnd() {
        return bufferEnd;
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
        return bufferStart;
    }

    public int getTokenEnd() {
        int result;
        if (currentToken.getType() == GroovyTokenTypes.NLS) {
            result = bufferStart + LINE_SEPARATOR.length();
        } else {
            result = bufferStart + currentToken.getText().length();
        }
        return result;
    }

    public void advance() {
        if (currentToken != null && currentToken.getType() == GroovyTokenTypes.NLS) {
            currentLineStartPosition = bufferStart + LINE_SEPARATOR.length();
        }

        if (currentToken != null && currentToken.getType() == GroovyTokenTypes.ML_COMMENT) {
            // ML_COMMENTS may contain any number of NLS and may end with one
            String text = currentToken.getText();
            int lastLineSeparatorPosition = text.lastIndexOf(LINE_SEPARATOR);
            if (lastLineSeparatorPosition > -1) {
                currentLineStartPosition = bufferStart + lastLineSeparatorPosition + LINE_SEPARATOR.length();
            }
        }

        try {
            currentToken = groovyLexer.nextToken();
            currentLineNumber = currentToken.getLine();
            // columns start at 1, bufferStart starts at 0
            bufferStart = currentLineStartPosition + currentToken.getColumn() - 1;
        } catch (TokenStreamException e) {
            e.printStackTrace();
        }
    }

    Token getCurrentToken() {
        return currentToken;
    }
}

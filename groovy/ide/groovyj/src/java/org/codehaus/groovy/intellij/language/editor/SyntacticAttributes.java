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

import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

public final class SyntacticAttributes {

    public static final TextAttributesKey GROOVY_SCRIPT_HEADER_COMMENT = TextAttributesKey.createTextAttributesKey("GROOVY_SCRIPT_HEADER_COMMENT", HighlighterColors.JAVA_LINE_COMMENT.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_LINE_COMMENT = TextAttributesKey.createTextAttributesKey("GROOVY_LINE_COMMENT", HighlighterColors.JAVA_LINE_COMMENT.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey("GROOVY_BLOCK_COMMENT", HighlighterColors.JAVA_BLOCK_COMMENT.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_DOC_COMMENT = TextAttributesKey.createTextAttributesKey("GROOVY_DOC_COMMENT", HighlighterColors.JAVA_DOC_COMMENT.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_DOC_TAG = TextAttributesKey.createTextAttributesKey("GROOVY_DOC_TAG", HighlighterColors.JAVA_DOC_TAG.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_DOC_MARKUP = TextAttributesKey.createTextAttributesKey("GROOVY_DOC_MARKUP", HighlighterColors.JAVA_DOC_MARKUP.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_KEYWORD = TextAttributesKey.createTextAttributesKey("GROOVY_KEYWORD", HighlighterColors.JAVA_KEYWORD.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_NUMBER = TextAttributesKey.createTextAttributesKey("GROOVY_NUMBER", HighlighterColors.JAVA_NUMBER.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_REGEXP = TextAttributesKey.createTextAttributesKey("GROOVY_REGEXP", HighlighterColors.JAVA_STRING.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_STRING = TextAttributesKey.createTextAttributesKey("GROOVY_STRING", HighlighterColors.JAVA_STRING.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_VALID_STRING_ESCAPE = TextAttributesKey.createTextAttributesKey("GROOVY_VALID_STRING_ESCAPE", HighlighterColors.JAVA_VALID_STRING_ESCAPE.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_INVALID_STRING_ESCAPE = TextAttributesKey.createTextAttributesKey("GROOVY_INVALID_STRING_ESCAPE", HighlighterColors.JAVA_INVALID_STRING_ESCAPE.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_OPERATION_SIGN = TextAttributesKey.createTextAttributesKey("GROOVY_OPERATION_SIGN", HighlighterColors.JAVA_OPERATION_SIGN.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_PARENTHESES = TextAttributesKey.createTextAttributesKey("GROOVY_PARENTH", HighlighterColors.JAVA_PARENTHS.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_BRACKETS = TextAttributesKey.createTextAttributesKey("GROOVY_BRACKETS", HighlighterColors.JAVA_BRACKETS.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_BRACES = TextAttributesKey.createTextAttributesKey("GROOVY_BRACES", HighlighterColors.JAVA_BRACES.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_COMMA = TextAttributesKey.createTextAttributesKey("GROOVY_COMMA", HighlighterColors.JAVA_COMMA.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_DOT = TextAttributesKey.createTextAttributesKey("GROOVY_DOT", HighlighterColors.JAVA_DOT.getDefaultAttributes());
    public static final TextAttributesKey GROOVY_SEMICOLON = TextAttributesKey.createTextAttributesKey("GROOVY_SEMICOLON", HighlighterColors.JAVA_SEMICOLON.getDefaultAttributes());
}

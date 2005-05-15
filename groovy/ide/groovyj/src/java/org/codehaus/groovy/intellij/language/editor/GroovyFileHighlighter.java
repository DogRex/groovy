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

import java.util.HashMap;
import java.util.Map;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.JavaDocTokenType;
import com.intellij.psi.StringEscapesTokenTypes;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.java.IJavaDocElementType;
import com.intellij.psi.xml.XmlTokenType;

import org.codehaus.groovy.antlr.parser.GroovyTokenTypes;

import org.codehaus.groovy.intellij.psi.GroovyTokenSets;
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

public class GroovyFileHighlighter extends SyntaxHighlighterBase {

    private static final Map<IElementType, TextAttributesKey> LEVEL_1_ELEMENT_TYPE_TO_TEXT_ATTRIBUTE_KEY_MAP = new HashMap<IElementType, TextAttributesKey>();
    private static final Map<IElementType, TextAttributesKey> LEVEL_2_ELEMENT_TYPE_TO_TEXT_ATTRIBUTE_KEY_MAP = new HashMap<IElementType, TextAttributesKey>();

    static {
        fillElementTypeToTextAttributeKeyMaps(LEVEL_1_ELEMENT_TYPE_TO_TEXT_ATTRIBUTE_KEY_MAP, LEVEL_2_ELEMENT_TYPE_TO_TEXT_ATTRIBUTE_KEY_MAP);
    }

    public TextAttributesKey[] getTokenHighlights(IElementType elementType) {
        return pack(LEVEL_1_ELEMENT_TYPE_TO_TEXT_ATTRIBUTE_KEY_MAP.get(elementType),
                    LEVEL_2_ELEMENT_TYPE_TO_TEXT_ATTRIBUTE_KEY_MAP.get(elementType));
    }

    public Lexer getHighlightingLexer() {
        return new GroovyHighlightingLexer();
    }

    private static void fillElementTypeToTextAttributeKeyMaps(Map<IElementType, TextAttributesKey> levelOneElementTypeToTextAttributeKeyMap,
                                                              Map<IElementType, TextAttributesKey> levelTwoElementTypeToTextAttributeKeyMap) {
        fillMap(levelOneElementTypeToTextAttributeKeyMap, GroovyTokenSets.KEYWORDS, SyntacticAttributes.GROOVY_KEYWORD);
        fillMap(levelOneElementTypeToTextAttributeKeyMap, GroovyTokenSets.ASSIGNMENT_OPERATIONS, SyntacticAttributes.GROOVY_OPERATION_SIGN);
        fillMap(levelOneElementTypeToTextAttributeKeyMap, GroovyTokenSets.OPERATIONS, SyntacticAttributes.GROOVY_OPERATION_SIGN);
        fillMap(levelOneElementTypeToTextAttributeKeyMap, GroovyTokenSets.EQUALITY_OPERATIONS, SyntacticAttributes.GROOVY_OPERATION_SIGN);
        fillMap(levelOneElementTypeToTextAttributeKeyMap, GroovyTokenSets.EQUALITY_OPERATIONS, SyntacticAttributes.GROOVY_OPERATION_SIGN);

        fillMap(levelOneElementTypeToTextAttributeKeyMap, GroovyTokenSets.NUMBERS, SyntacticAttributes.GROOVY_NUMBER);

        levelOneElementTypeToTextAttributeKeyMap.put(StringEscapesTokenTypes.VALID_STRING_ESCAPE_TOKEN, SyntacticAttributes.GROOVY_VALID_STRING_ESCAPE);
        levelOneElementTypeToTextAttributeKeyMap.put(StringEscapesTokenTypes.INVALID_CHARACTER_ESCAPE_TOKEN, SyntacticAttributes.GROOVY_INVALID_STRING_ESCAPE);
        levelOneElementTypeToTextAttributeKeyMap.put(StringEscapesTokenTypes.INVALID_UNICODE_ESCAPE_TOKEN, SyntacticAttributes.GROOVY_INVALID_STRING_ESCAPE);

        levelOneElementTypeToTextAttributeKeyMap.put(GroovyTokenTypeMappings.getType(GroovyTokenTypes.STRING_CH), SyntacticAttributes.GROOVY_STRING);
        levelOneElementTypeToTextAttributeKeyMap.put(GroovyTokenTypeMappings.getType(GroovyTokenTypes.STRING_LITERAL), SyntacticAttributes.GROOVY_STRING);

        fillMap(levelOneElementTypeToTextAttributeKeyMap, GroovyTokenSets.PARENTHESES, SyntacticAttributes.GROOVY_PARENTHESES);
        fillMap(levelOneElementTypeToTextAttributeKeyMap, GroovyTokenSets.BRACKETS, SyntacticAttributes.GROOVY_BRACKETS);
        fillMap(levelOneElementTypeToTextAttributeKeyMap, GroovyTokenSets.BRACES, SyntacticAttributes.GROOVY_BRACES);

        levelOneElementTypeToTextAttributeKeyMap.put(GroovyTokenTypeMappings.getType(GroovyTokenTypes.DOT), SyntacticAttributes.GROOVY_DOT);
        levelOneElementTypeToTextAttributeKeyMap.put(GroovyTokenTypeMappings.getType(GroovyTokenTypes.COMMA), SyntacticAttributes.GROOVY_COMMA);
        levelOneElementTypeToTextAttributeKeyMap.put(GroovyTokenTypeMappings.getType(GroovyTokenTypes.SEMI), SyntacticAttributes.GROOVY_SEMICOLON);

        levelOneElementTypeToTextAttributeKeyMap.put(GroovyTokenTypeMappings.getType(GroovyTokenTypeMappings.BAD_CHARACTER), HighlighterColors.BAD_CHARACTER);

        levelOneElementTypeToTextAttributeKeyMap.put(GroovyTokenTypeMappings.getType(GroovyTokenTypes.SH_COMMENT), SyntacticAttributes.GROOVY_SCRIPT_HEADER_COMMENT);
        levelOneElementTypeToTextAttributeKeyMap.put(GroovyTokenTypeMappings.getType(GroovyTokenTypes.SL_COMMENT), SyntacticAttributes.GROOVY_LINE_COMMENT);
        levelOneElementTypeToTextAttributeKeyMap.put(GroovyTokenTypeMappings.getType(GroovyTokenTypes.ML_COMMENT), SyntacticAttributes.GROOVY_BLOCK_COMMENT);

        IElementType[] elementTypes = IElementType.enumerate(new IElementType.Predicate() {
            public boolean matches(IElementType elementType) {
                return elementType instanceof IJavaDocElementType;
            }
        });

        for (int i = 0; i < elementTypes.length; i++) {
            IElementType elementType = elementTypes[i];
            levelOneElementTypeToTextAttributeKeyMap.put(elementType, SyntacticAttributes.GROOVY_DOC_COMMENT);
        }

        levelOneElementTypeToTextAttributeKeyMap.put(XmlTokenType.XML_DATA_CHARACTERS, SyntacticAttributes.GROOVY_DOC_COMMENT);
        levelOneElementTypeToTextAttributeKeyMap.put(XmlTokenType.XML_REAL_WHITE_SPACE, SyntacticAttributes.GROOVY_DOC_COMMENT);
        levelOneElementTypeToTextAttributeKeyMap.put(XmlTokenType.TAG_WHITE_SPACE, SyntacticAttributes.GROOVY_DOC_COMMENT);

        levelOneElementTypeToTextAttributeKeyMap.put(JavaDocTokenType.DOC_TAG_NAME, SyntacticAttributes.GROOVY_DOC_COMMENT);
        levelTwoElementTypeToTextAttributeKeyMap.put(JavaDocTokenType.DOC_TAG_NAME, SyntacticAttributes.GROOVY_DOC_TAG);

        elementTypes = new IElementType[] {
            XmlTokenType.XML_START_TAG_START,
            XmlTokenType.XML_END_TAG_START,
            XmlTokenType.XML_TAG_END,
            XmlTokenType.XML_EMPTY_ELEMENT_END,
            XmlTokenType.TAG_WHITE_SPACE,
            XmlTokenType.XML_TAG_NAME,
            XmlTokenType.XML_NAME,
            XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN,
            XmlTokenType.XML_ATTRIBUTE_VALUE_START_DELIMITER,
            XmlTokenType.XML_ATTRIBUTE_VALUE_END_DELIMITER,
            XmlTokenType.XML_EQ
        };

        for (int j = 0; j < elementTypes.length; j++) {
            IElementType elementType = elementTypes[j];
            levelOneElementTypeToTextAttributeKeyMap.put(elementType, SyntacticAttributes.GROOVY_DOC_COMMENT);
            levelTwoElementTypeToTextAttributeKeyMap.put(elementType, SyntacticAttributes.GROOVY_DOC_MARKUP);
        }
    }
}

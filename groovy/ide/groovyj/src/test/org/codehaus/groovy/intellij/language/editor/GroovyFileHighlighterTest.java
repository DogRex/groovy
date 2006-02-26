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

import junitx.framework.Assert;
import junitx.framework.ObjectAssert;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.psi.tree.IElementType;

import org.codehaus.groovy.intellij.GroovyjTestCase;
import org.codehaus.groovy.intellij.Stubs;
import org.codehaus.groovy.intellij.language.parser.GroovyTokenTypes;
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

public class GroovyFileHighlighterTest extends GroovyjTestCase {

    private GroovyFileHighlighter fileHighlighter;

    protected void setUp() {
        StdFileTypes.XML = Stubs.LANGUAGE_FILE_TYPE;
        StdFileTypes.JAVA = Stubs.LANGUAGE_FILE_TYPE;

        fileHighlighter = new GroovyFileHighlighter();
    }

    public void testReturnsANonEmptyArrayOfTextAttributeKeysAsTheTokenHighlightsForAMappedElementType() {
        IElementType mappedElementType = GroovyTokenTypeMappings.getType(GroovyTokenTypes.LPAREN);
        TextAttributesKey[] tokenHighlights = fileHighlighter.getTokenHighlights(mappedElementType);

        Assert.assertNotEquals("token highlights", null, tokenHighlights);
        assertEquals("number of token highlights", 1, tokenHighlights.length);
        assertSame("text attributes key at level #1", SyntacticAttributes.GROOVY_PARENTHESES, tokenHighlights[0]);
    }

    public void testReturnsAnEmptyArrayOfTextAttributeKeysAsTheTokenHighlightsForAnUnmappedElementType() {
        IElementType unmappedElementType = GroovyTokenTypeMappings.getType(GroovyTokenTypes.NLS);
        TextAttributesKey[] tokenHighlights = fileHighlighter.getTokenHighlights(unmappedElementType);

        Assert.assertNotEquals("token highlights", null, tokenHighlights);
        assertEquals("number of token highlights", 0, tokenHighlights.length);
    }

    public void testProducesAHighlightingLexerForGroovy() {
        Lexer highlightingLexer = fileHighlighter.getHighlightingLexer();
        Assert.assertNotEquals("highlighting lexer", null, highlightingLexer);
        ObjectAssert.assertInstanceOf("highlighting lexer", GroovyHighlightingLexer.class, highlightingLexer);
    }
}

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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junitx.framework.Assert;
import junitx.framework.ListAssert;
import junitx.framework.ObjectAssert;

import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.ide.highlighter.JavaFileHighlighter;
import com.intellij.openapi.editor.colors.ColorKey;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.pages.JavaColorSettingsPage;

import org.jmock.Mock;

import org.codehaus.groovy.intellij.GroovySupportLoader;
import org.codehaus.groovy.intellij.GroovyjTestCase;
import org.codehaus.groovy.intellij.Stubs;

public class GroovyColourSettingsPageTest extends GroovyjTestCase {

    private final JavaColorSettingsPage javaColourSettingsPage = new JavaColorSettingsPage();

    private GroovyColourSettingsPage groovyColourSettingsPage;

    protected void setUp() {
        MockApplicationManager.getMockApplication().registerComponent(EditorColorsManager.class, createdStubbedEditorColorsManager());
        groovyColourSettingsPage = new GroovyColourSettingsPage();
    }

    protected void tearDown() {
        MockApplicationManager.getMockApplication().removeComponent(EditorColorsManager.class);
    }

    public void testDefinesADisplayName() {
        assertEquals("display name", "Groovy", groovyColourSettingsPage.getDisplayName());
    }

    public void testUsesTheIconOfTheGroovyFileType() {
        assertSame("icon", GroovySupportLoader.GROOVY.getIcon(), groovyColourSettingsPage.getIcon());
    }

    public void testHasTextToBeUsedForDemonstrationPurposes() {
        Assert.assertNotEquals("demo text", null, groovyColourSettingsPage.getDemoText());
    }

    public void testUsesAttributeDescriptorsDerivedFromTheOnesDefinedOnTheJavaColourSettingsPage() {
        AttributesDescriptor[] groovyAttributesDescriptors = groovyColourSettingsPage.getAttributeDescriptors();
        AttributesDescriptor[] javaAttributesDescriptors = javaColourSettingsPage.getAttributeDescriptors();

        assertTrue("should have a greater or equal number of attribute descriptors than the Java page",
                   groovyAttributesDescriptors.length >= javaAttributesDescriptors.length);

        List<TextAttributes> groovyAttributeDescriptorAttributes = new ArrayList<TextAttributes>();
        for (AttributesDescriptor attributesDescriptor : groovyAttributesDescriptors) {
            groovyAttributeDescriptorAttributes.add(attributesDescriptor.getKey().getDefaultAttributes());
        }

        for (int i = 0; i < javaAttributesDescriptors.length; i++) {
            ListAssert.assertContains("attribute descriptor attributes #" + i,
                    groovyAttributeDescriptorAttributes,
                    javaAttributesDescriptors[i].getKey().getDefaultAttributes());
        }
    }

    public void testUsesColourDescriptorsEquivalentToTheOnesDefinedOnTheJavaColourSettingsPage() {
        ColorDescriptor[] groovyColourDescriptors = groovyColourSettingsPage.getColorDescriptors();
        ColorDescriptor[] javaColourDescriptors = javaColourSettingsPage.getColorDescriptors();

        assertEquals("number of colour descriptors", javaColourDescriptors.length, groovyColourDescriptors.length);

        for (int i = 0; i < javaColourDescriptors.length; i++) {
            ColorDescriptor colourDescriptor = javaColourDescriptors[i];
            assertSame("key for colour descriptor #" + i, colourDescriptor.getKey(), groovyColourDescriptors[i].getKey());
        }
    }

    public void testUsesAMapOfAdditionalHighlightingTagsEquivalentToTheOnesDefinedOnTheJavaColourSettingsPage() {
        Map<String, TextAttributesKey> groovyAdditionalHighlightingTagToDescriptorMap = groovyColourSettingsPage.getAdditionalHighlightingTagToDescriptorMap();
        Map<String, TextAttributesKey> javaAdditionalHighlightingTagToDescriptorMap = javaColourSettingsPage.getAdditionalHighlightingTagToDescriptorMap();

        for (String key : javaAdditionalHighlightingTagToDescriptorMap.keySet()) {
            TextAttributesKey expectedAttributes = javaAdditionalHighlightingTagToDescriptorMap.get(key);
            TextAttributesKey actualAttributes = groovyAdditionalHighlightingTagToDescriptorMap.get(key);
            assertNotNull(key + " tag is missing in GroovyJ", actualAttributes);
            assertEquals(key + " tag", expectedAttributes.getDefaultAttributes(), actualAttributes.getDefaultAttributes());
        }

        assertEquals("number of additional highlighting tags",
                     javaAdditionalHighlightingTagToDescriptorMap.size(),
                     groovyAdditionalHighlightingTagToDescriptorMap.size());
    }

    public void testUsesTheGroovyFileHighlighterAsItsHighlighter() {
        StdFileTypes.XML = Stubs.LANGUAGE_FILE_TYPE;
        StdFileTypes.JAVA = Stubs.LANGUAGE_FILE_TYPE;

        // TODO: restore once GroovyFileHighlighter is functionally usable
//        ObjectAssert.assertInstanceOf("highlighter", GroovyFileHighlighter.class, groovyColourSettingsPage.getHighlighter());
        ObjectAssert.assertInstanceOf("highlighter", JavaFileHighlighter.class, groovyColourSettingsPage.getHighlighter());
    }

    private EditorColorsManager createdStubbedEditorColorsManager() {
        Mock stubEditorColorsManager = mock(EditorColorsManager.class);
        stubEditorColorsManager.stubs().method("getGlobalScheme").will(returnValue(createStubbedEditorColorsScheme()));
        return (EditorColorsManager) stubEditorColorsManager.proxy();
    }

    private EditorColorsScheme createStubbedEditorColorsScheme() {
        Mock stubEditorColorsScheme = mock(EditorColorsScheme.class);
        stubEditorColorsScheme.stubs().method("getColor").with(isA(ColorKey.class)).will(returnValue(Color.LIGHT_GRAY));
        stubEditorColorsScheme.stubs().method("getAttributes").with(isA(TextAttributesKey.class)).will(returnValue(Stubs.TEXT_ATTRIBUTES));
        return (EditorColorsScheme) stubEditorColorsScheme.proxy();
    }
}

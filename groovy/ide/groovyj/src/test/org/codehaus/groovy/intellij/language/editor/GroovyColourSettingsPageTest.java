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

import java.awt.Color;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Icon;

import junitx.framework.Assert;
import junitx.framework.ObjectAssert;

import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.ide.highlighter.JavaFileHighlighter;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.colors.ColorKey;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.FileTypeSupportCapabilities;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.pages.JavaColorSettingsPage;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

import org.codehaus.groovy.intellij.GroovySupportLoader;

public class GroovyColourSettingsPageTest extends MockObjectTestCase {

    private static final TextAttributes DUMMY_TEXT_ATTRIBUTES = new TextAttributes();

    private static final LanguageFileType DUMMY_LANGUAGE_FILE_TYPE = new LanguageFileType(Language.ANY) {
        public String getName() {
            return null;
        }

        public String getDescription() {
            return null;
        }

        public String getDefaultExtension() {
            return null;
        }

        public Icon getIcon() {
            return null;
        }

        public FileTypeSupportCapabilities getSupportCapabilities() {
            return null;
        }
    };

    private final JavaColorSettingsPage javaColourSettingsPage = new JavaColorSettingsPage();
    private GroovyColourSettingsPage groovyColourSettingsPage;

    protected void setUp() {
        MockApplicationManager.getMockApplication().registerComponent(EditorColorsManager.class, createdStubbedEditorColorsManager());
        groovyColourSettingsPage = new GroovyColourSettingsPage();
    }

    protected void tearDown() {
        MockApplicationManager.getMockApplication().removeComponent(EditorColorsManager.class);
    }

    public void testUsesTheIconOfTheGroovyFileType() {
        assertSame("icon", GroovySupportLoader.GROOVY.getIcon(), groovyColourSettingsPage.getIcon());
    }

    public void testDefinesADisplayName() {
        assertEquals("display name", "Groovy", groovyColourSettingsPage.getDisplayName());
    }

    public void testHasTextToBeUsedForDemonstrationPurposes() {
        Assert.assertNotEquals("demo text", null, groovyColourSettingsPage.getDemoText());
    }

    public void testUsesAttributeDescriptorsDerivedFromTheOnesDefinedOnTheJavaColourSettingsPage() {
        AttributesDescriptor[] groovyAttributeDescriptors = groovyColourSettingsPage.getAttributeDescriptors();
        AttributesDescriptor[] javaAttributeDescriptors = javaColourSettingsPage.getAttributeDescriptors();

        assertEquals("number of attribute descriptors", javaAttributeDescriptors.length, groovyAttributeDescriptors.length);

        for (int i = 0; i < javaAttributeDescriptors.length; i++) {
            TextAttributesKey expectedAttributesKey = javaAttributeDescriptors[i].getKey();
            TextAttributesKey actualAttributesKey = groovyAttributeDescriptors[i].getKey();
            assertEquals("key for attribute descriptor #" + i, expectedAttributesKey.getDefaultAttributes(), actualAttributesKey.getDefaultAttributes());
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
        Map groovyAdditionalHighlightingTagToDescriptorMap = groovyColourSettingsPage.getAdditionalHighlightingTagToDescriptorMap();
        Map javaAdditionalHighlightingTagToDescriptorMap = javaColourSettingsPage.getAdditionalHighlightingTagToDescriptorMap();

        assertEquals("number of additional highlighting tags", javaAdditionalHighlightingTagToDescriptorMap.size(), groovyAdditionalHighlightingTagToDescriptorMap.size());

        for (Iterator iterator = javaAdditionalHighlightingTagToDescriptorMap.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            TextAttributesKey expectedAttributes = (TextAttributesKey) javaAdditionalHighlightingTagToDescriptorMap.get(key);
            TextAttributesKey actualAttributes = (TextAttributesKey) groovyAdditionalHighlightingTagToDescriptorMap.get(key);
            assertEquals(key + " tag", expectedAttributes.getDefaultAttributes(), actualAttributes.getDefaultAttributes());
        }
    }

    public void testUsesTheJavaFileHighlighterAsItsHighlighter() {
        StdFileTypes.XML = DUMMY_LANGUAGE_FILE_TYPE;
        StdFileTypes.JAVA = DUMMY_LANGUAGE_FILE_TYPE;

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
        stubEditorColorsScheme.stubs().method("getAttributes").with(isA(TextAttributesKey.class)).will(returnValue(DUMMY_TEXT_ATTRIBUTES));
        return (EditorColorsScheme) stubEditorColorsScheme.proxy();
    }
}

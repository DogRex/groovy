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


package org.codehaus.groovy.intellij;

import junit.framework.TestCase;

import com.intellij.openapi.util.IconLoader;

import org.codehaus.groovy.intellij.language.GroovyLanguage;

public class GroovyFileTypeTest extends TestCase {

    private GroovyFileType groovyFileType = new GroovyFileType(GroovyLanguage.createLanguage());

    public void testDefinesAName() {
        assertEquals("name", "Groovy", groovyFileType.getName());
    }

    public void testHasADescription() {
        assertEquals("description", "Groovy Scripts and Classes", groovyFileType.getDescription());
    }

    public void testDefinesADefaultExtension() {
        assertEquals("default extension", "groovy", groovyFileType.getDefaultExtension());
    }

    public void testHasTheGroovyFileTypeIcon() {
        assertSame("icon", IconLoader.getIcon("/icons/groovy_fileType.png"), groovyFileType.getIcon());
    }

    public void testDoesNotRepresentABinaryFile() {
        assertEquals("is binary file", false, groovyFileType.isBinary());
    }

    public void testIsNotAReadOnlyFileType() {
        assertEquals("is binary file", false, groovyFileType.isReadOnly());
    }

    public void testSupportsKeywordCompletion() {
        assertEquals("supports keyword completion", true, groovyFileType.getSupportCapabilities().hasCompletion());
    }

    public void testSupportsSyntaxValidation() {
        assertEquals("supports syntax validation", true, groovyFileType.getSupportCapabilities().hasValidation());
    }

    public void testSupportsTheNavigationToGroovyElements() {
        assertEquals("supports navigation", true, groovyFileType.getSupportCapabilities().hasNavigation());
    }

    public void testSupportsFindUsagesOnGroovyElements() {
        assertEquals("supports find usages", true, groovyFileType.getSupportCapabilities().hasFindUsages());
    }

    public void testSupportsTheRenamingOfGroovyElements() {
        assertEquals("supports renaming", true, groovyFileType.getSupportCapabilities().hasRename());
    }

    public void testDoesNotDefineSepcificCharacterSetsYet() {
        assertSame("character set", null, groovyFileType.getCharset(null));
    }

    public void testDoesNotHaveARepresentationInTheStructuralTree() {
        assertSame("structural view model", null, groovyFileType.getStructureViewBuilder(null, null));
    }
}

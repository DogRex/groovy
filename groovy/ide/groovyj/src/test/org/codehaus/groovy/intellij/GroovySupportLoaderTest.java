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

import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.openapi.fileTypes.FileTypeManager;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class GroovySupportLoaderTest extends MockObjectTestCase {

    private final GroovySupportLoader groovySupportLoader = new GroovySupportLoader();

    protected void tearDown() {
        System.getProperties().remove("groovy.jsr");
    }

    public void testDefinesAComponentName() {
        assertEquals("component name", "groovy.integration", groovySupportLoader.getComponentName());
    }

    public void testRegistersTheGroovyFileTypeWhenInitialisedByIntellijIdea() {
        setExpectationsForInitialisationByIntellijIdea();
        groovySupportLoader.initComponent();

        MockApplicationManager.getMockApplication().removeComponent(FileTypeManager.class);
    }

    public void testEnablesGroovyJsrWhenInitialisedByIntellijIdea() {
        setExpectationsForInitialisationByIntellijIdea();
        assertEquals("groovy.jsr", null, System.getProperty("groovy.jsr"));

        groovySupportLoader.initComponent();
        assertEquals("groovy.jsr", "true", System.getProperty("groovy.jsr"));

        MockApplicationManager.getMockApplication().removeComponent(FileTypeManager.class);
    }

    private void setExpectationsForInitialisationByIntellijIdea() {
        MockApplicationManager.reset();

        Mock mockFileTypeManager = mock(FileTypeManager.class);
        MockApplicationManager.getMockApplication().registerComponent(FileTypeManager.class, mockFileTypeManager.proxy());
        mockFileTypeManager.expects(once()).method("registerFileType").with(same(GroovySupportLoader.GROOVY), eq(new String[] { "groovy" }));
    }

    public void testRemovesTheGroovyJsrSystemPropertyWhenDisposedByIntellijIdea() {
        groovySupportLoader.disposeComponent();
        assertEquals("groovy.jsr", null, System.getProperty("groovy.jsr"));
    }
}

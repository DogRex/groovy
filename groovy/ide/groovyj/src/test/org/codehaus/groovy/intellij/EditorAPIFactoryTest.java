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

import junitx.framework.StringAssert;

import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.openapi.application.ApplicationInfo;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class EditorAPIFactoryTest extends MockObjectTestCase {

    protected final Mock mockApplicationInfo = mock(ApplicationInfo.class);
    protected final EditorAPIFactory editorApiFactory = new EditorAPIFactory();

    protected void setUp() {
        mockApplicationInfo.stubs().method("getMajorVersion");
        mockApplicationInfo.stubs().method("getMinorVersion");
        MockApplicationManager.getMockApplication().registerComponent(ApplicationInfo.class, mockApplicationInfo.proxy());
    }

    protected void tearDown() {
        MockApplicationManager.getMockApplication().removeComponent(ApplicationInfo.class);
    }

    public void testDefinesAComponentName() {
        assertEquals("component name", "groovy.editorApi.factory", editorApiFactory.getComponentName());
    }

    public void testDoesNothingWhenInitialisedByIdea() {
        editorApiFactory.initComponent();
    }

    public void testDoesNothingWhenDisposedByIdea() {
        editorApiFactory.disposeComponent();
    }

    public void testThrowsARuntimeExceptionWhenAttemptingToCreateEditorApiWithInvalidBuildNumber() {
        mockApplicationInfo.expects(once()).method("getVersionName").will(returnValue("IDEA version name"));
        mockApplicationInfo.expects(once()).method("getBuildNumber").will(returnValue("invalid build number"));
        try {
            editorApiFactory.createEditorAPI(null);
            fail("A RuntimeException should have been thrown for this unsupported version of IntelliJ IDEA!");
        } catch (RuntimeException expected) {
            StringAssert.assertContains("Could not create editor API for", expected.getMessage());
        }
    }

    public void testThrowsARuntimeExceptionWhenAttemptingToCreateEditorApiUnderTheAriadnaVersionOfIntellijIdea() {
        mockApplicationInfo.expects(once()).method("getVersionName").will(returnValue("a string that contains the name AriAdNa..."));
        mockApplicationInfo.expects(once()).method("getBuildNumber").will(returnValue("700"));
        try {
            editorApiFactory.createEditorAPI(null);
            fail("A RuntimeException should have been thrown for this unsupported version of IntelliJ IDEA!");
        } catch (RuntimeException expected) {
            StringAssert.assertContains("Could not create editor API for", expected.getMessage());
        }
    }

    public void testThrowsARuntimeExceptionWhenAttemptingToCreateEditorApiUnderTheAuroraVersionOfIntellijIdea() {
        mockApplicationInfo.expects(once()).method("getVersionName").will(returnValue("a string that contains the name AuRorA..."));
        mockApplicationInfo.expects(once()).method("getBuildNumber").will(returnValue("1130"));
        try {
            editorApiFactory.createEditorAPI(null);
            fail("A RuntimeException should have been thrown for this unsupported version of IntelliJ IDEA!");
        } catch (RuntimeException expected) {
            StringAssert.assertContains("Could not create editor API for", expected.getMessage());
        }
    }

    public void testThrowsARuntimeExceptionWhenAttemptingToCreateEditorApiUnderThePalladaVersionOfIntellijIdea() {
        mockApplicationInfo.expects(once()).method("getVersionName").will(returnValue("a string that contains the name AuRorA..."));
        mockApplicationInfo.expects(once()).method("getBuildNumber").will(returnValue("1130"));
        try {
            editorApiFactory.createEditorAPI(null);
            fail("A RuntimeException should have been thrown for this unsupported version of IntelliJ IDEA!");
        } catch (RuntimeException expected) {
            StringAssert.assertContains("Could not create editor API for", expected.getMessage());
        }
    }
}

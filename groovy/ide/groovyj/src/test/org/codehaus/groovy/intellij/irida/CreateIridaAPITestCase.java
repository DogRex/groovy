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


package org.codehaus.groovy.intellij.irida;

import junitx.framework.StringAssert;

import org.codehaus.groovy.intellij.EditorAPI;
import org.codehaus.groovy.intellij.EditorAPIFactory;
import org.codehaus.groovy.intellij.EditorAPIFactoryTest;

public class CreateIridaAPITestCase extends EditorAPIFactoryTest {

    public void testCanCreateEditorApiUnderIridaWithinEapBuildsBoundaries() {
        mockApplicationInfo.expects(once()).method("getVersionName").will(returnValue("a string containing the name iRIdA..."));
        mockApplicationInfo.expects(once()).method("getBuildNumber").will(returnValue("3212"));

        EditorAPI editorApi = editorApiFactory.createEditorAPI(null);
        StringAssert.assertContains("IridaAPI", editorApi.getClass().getName());

        mockApplicationInfo.expects(once()).method("getVersionName").will(returnValue("a string containing the name IrIDa..."));
        mockApplicationInfo.expects(once()).method("getBuildNumber").will(returnValue("3300"));

        editorApi = new EditorAPIFactory().createEditorAPI(null);
        StringAssert.assertContains("IridaAPI", editorApi.getClass().getName());
    }
}

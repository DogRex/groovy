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

import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.project.Project;

import org.jmock.Mock;

import org.codehaus.groovy.intellij.BaseEditorAPITest;

public class IridaAPITest extends BaseEditorAPITest {

    protected void setUp() {
        super.setUp();
        editorAPI = new IridaAPI((Project) mockProject.proxy());
    }

    protected void tearDown() {
        super.tearDown();
        MockApplicationManager.reset();
    }

    public void testCanRunATaskAsynchronouslyUsingAnApplicationInstance() {
        Mock mockApplication = mock(Application.class);
        MockApplicationManager.setApplication((Application) mockApplication.proxy());
        mockApplication.expects(once()).method("invokeLater").with(isA(Runnable.class));

        editorAPI.invokeLater(new Runnable() {
            public void run() {}
        });
    }
}

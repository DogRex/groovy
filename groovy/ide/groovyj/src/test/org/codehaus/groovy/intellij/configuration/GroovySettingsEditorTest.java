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


package org.codehaus.groovy.intellij.configuration;

import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.project.Project;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class GroovySettingsEditorTest extends MockObjectTestCase {

    private final Mock mockActionManager = mock(ActionManager.class);
    private final Mock mockProject = mock(Project.class);

    protected void setUp() {
        MockApplicationManager.reset();
        MockApplicationManager.getMockApplication().registerComponent(ActionManager.class, mockActionManager.proxy());

        Mock mockActionPopupMenu = mock(ActionPopupMenu.class);
        mockActionManager.stubs().method("createActionPopupMenu").with(eq(ActionPlaces.UNKNOWN), isA(ActionGroup.class))
                .will(returnValue(mockActionPopupMenu.proxy()));
    }

    public void testConstructsASettingsEditorForADefaultProject() {
        mockProject.stubs().method("isDefault").withNoArguments().will(returnValue(true));
        assertNotNull("settings editor component", new GroovySettingsEditor((Project) mockProject.proxy()).createEditor());
    }

    public void testConstructsASettingsEditorForANonDefaultProject() {
        mockProject.stubs().method("isDefault").withNoArguments().will(returnValue(false));
        assertNotNull("settings editor component", new GroovySettingsEditor((Project) mockProject.proxy()).createEditor());
    }

    public void testDoesNothingWhenDisposedByIdea() {
        mockProject.stubs().method("isDefault").withNoArguments().will(returnValue(false));
        new GroovySettingsEditor((Project) mockProject.proxy()).disposeEditor();
    }

    public void testDoesNothingWhenInstructedToResetItsContentsFromAGivenSource() {
        mockProject.stubs().method("isDefault").withNoArguments().will(returnValue(false));
        new GroovySettingsEditor((Project) mockProject.proxy()). resetEditorFrom(null);
    }

    public void testDoesNothingWhenInstructedToApplyItsContentsToAGivenTarget() {
        mockProject.stubs().method("isDefault").withNoArguments().will(returnValue(false));
        new GroovySettingsEditor((Project) mockProject.proxy()). applyEditorTo(null);
    }

    public void testDoesNotDefineAHelpTopicYet() {
        mockProject.stubs().method("isDefault").withNoArguments().will(returnValue(false));
        assertEquals("help topic", null, new GroovySettingsEditor((Project) mockProject.proxy()).getHelpTopic());
    }
}

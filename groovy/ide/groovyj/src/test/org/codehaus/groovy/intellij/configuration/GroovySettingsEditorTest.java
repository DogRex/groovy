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
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;

import org.jmock.Mock;

public class GroovySettingsEditorTest extends GroovyConfigurationTestCase {

    private final GroovyRunConfiguration groovyRunConfiguration = createRunConfiguration("-showversion", "../foo.groovy", "", "/home/acme");

    private GroovySettingsEditor groovySettingsEditor;

    protected void setUp() {
        MockApplicationManager.reset();

        Mock mockActionManager = mock(ActionManager.class);
        MockApplicationManager.getMockApplication().registerComponent(ActionManager.class, mockActionManager.proxy());

        Mock mockActionPopupMenu = mock(ActionPopupMenu.class);
        mockActionManager.stubs().method("createActionPopupMenu").with(eq(ActionPlaces.UNKNOWN), isA(ActionGroup.class))
                .will(returnValue(mockActionPopupMenu.proxy()));

        groovySettingsEditor = new GroovySettingsEditor(groovyRunConfiguration.getProject());
    }

    public void testCopiesSettingsFromAGivenGroovyRunConfigurationWhenInstructedToResetItsContents() {
        assertEquals("script path", "", groovySettingsEditor.scriptPathTextField.getText());
        assertEquals("VM parameters", "", groovySettingsEditor.vmParameterEditor.getText());
        assertEquals("scripit parameters", "", groovySettingsEditor.scriptParametersEditor.getText());
        assertEquals("working directory path", "", groovySettingsEditor.workingDirectoryPathTextField.getText());

        Module[] allProjectModules = ModuleManager.getInstance(groovyRunConfiguration.getProject()).getSortedModules();
        assertSame("module", allProjectModules[0], groovySettingsEditor.moduleComboBox.getSelectedItem());

        groovyRunConfiguration.setModule(allProjectModules[1]);
        groovySettingsEditor.resetEditorFrom(groovyRunConfiguration);

        assertEquals("script path", groovyRunConfiguration.getScriptPath(), groovySettingsEditor.scriptPathTextField.getText());
        assertEquals("VM parameters", groovyRunConfiguration.getVmParameters(), groovySettingsEditor.vmParameterEditor.getText());
        assertEquals("scripit parameters", groovyRunConfiguration.getScriptParameters(), groovySettingsEditor.scriptParametersEditor.getText());
        assertEquals("working directory path", groovyRunConfiguration.getWorkingDirectoryPath(), groovySettingsEditor.workingDirectoryPathTextField.getText());
        assertSame("module", groovyRunConfiguration.getModule(), groovySettingsEditor.moduleComboBox.getSelectedItem());
    }

    public void testUpdatesAGivenGroovyRunConfigurationWhenInstructedToDoSo() {
        groovySettingsEditor.resetEditorFrom(groovyRunConfiguration);

        GroovyRunConfiguration runConfiguration = createRunConfiguration("", "", "", "");
        groovySettingsEditor.applyEditorTo(runConfiguration);

        assertEquals("script path", groovySettingsEditor.scriptPathTextField.getText(), runConfiguration.getScriptPath());
        assertEquals("VM parameters", groovySettingsEditor.vmParameterEditor.getText(), runConfiguration.getVmParameters());
        assertEquals("scripit parameters", groovySettingsEditor.scriptParametersEditor.getText(), runConfiguration.getScriptParameters());
        assertEquals("working directory path", groovySettingsEditor.workingDirectoryPathTextField.getText(), runConfiguration.getWorkingDirectoryPath());
        assertSame("module", groovySettingsEditor.moduleComboBox.getSelectedItem(), runConfiguration.getModule());
    }

    public void testConstructsASettingsEditorForAGivenProject() {
        assertSame("settings editor component", groovySettingsEditor.editor, groovySettingsEditor.createEditor());
    }

    public void testLosesItsSettingsEditorComponentWhenDisposedByIdea() {
        assertNotNull("settings editor component", groovySettingsEditor.editor);

        groovySettingsEditor.disposeEditor();
        assertEquals("settings editor component", null, groovySettingsEditor.editor);
    }

    public void testDoesNotDefineAHelpTopicYet() {
        assertEquals("help topic", null, groovySettingsEditor.getHelpTopic());
    }
}

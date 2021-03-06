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


package org.codehaus.groovy.intellij.configuration;

import javax.swing.Icon;
import javax.swing.JList;

import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleType;

import org.jmock.Mock;

import org.codehaus.groovy.intellij.Icons;
import org.codehaus.groovy.intellij.Mocks;

public class GroovySettingsEditorTest extends GroovyConfigurationTestCase {

    private final GroovyRunConfiguration runConfiguration = createRunConfiguration(null, "-showversion", "../foo.groovy", "", "/home/acme");

    private GroovySettingsEditor settingsEditor;
    private GroovySettingsEditor.ModuleComboBoxRenderer moduleComboBoxRenderer;

    protected void setUp() {
        Mock mockActionManager = mock(ActionManager.class);
        MockApplicationManager.getMockApplication().registerComponent(ActionManager.class, mockActionManager.proxy());

        Mock mockActionPopupMenu = mock(ActionPopupMenu.class);
        mockActionManager.stubs().method("createActionPopupMenu").with(eq(ActionPlaces.UNKNOWN), isA(ActionGroup.class))
                .will(returnValue(mockActionPopupMenu.proxy()));

        settingsEditor = new GroovySettingsEditor(runConfiguration.getProject());
        moduleComboBoxRenderer = (GroovySettingsEditor.ModuleComboBoxRenderer) settingsEditor.moduleComboBox.getRenderer();
    }

    public void testCopiesSettingsFromAGivenGroovyRunConfigurationWhenInstructedToResetItsContents() {
        assertEquals("script path", "", settingsEditor.scriptPathTextField.getText());
        assertEquals("VM parameters", "", settingsEditor.vmParameterEditor.getText());
        assertEquals("script parameters", "", settingsEditor.scriptParametersEditor.getText());
        assertEquals("working directory path", "", settingsEditor.workingDirectoryPathTextField.getText());

        Module[] allProjectModules = ModuleManager.getInstance(runConfiguration.getProject()).getSortedModules();
        assertSame("module", allProjectModules[0], settingsEditor.moduleComboBox.getSelectedItem());

        runConfiguration.setModule(allProjectModules[1]);
        settingsEditor.resetEditorFrom(runConfiguration);

        assertEquals("script path", runConfiguration.getScriptPath(), settingsEditor.scriptPathTextField.getText());
        assertEquals("VM parameters", runConfiguration.getVmParameters(), settingsEditor.vmParameterEditor.getText());
        assertEquals("script parameters", runConfiguration.getScriptParameters(), settingsEditor.scriptParametersEditor.getText());
        assertEquals("working directory path", runConfiguration.getWorkingDirectoryPath(), settingsEditor.workingDirectoryPathTextField.getText());
        assertSame("module", runConfiguration.getModule(), settingsEditor.moduleComboBox.getSelectedItem());
    }

    public void testUpdatesAGivenGroovyRunConfigurationWhenInstructedToDoSo() {
        settingsEditor.resetEditorFrom(runConfiguration);

        String expectedScriptPath = "~/scripts/bar.groovy";
        String expectedVmParameters = "-Xms128m -Xmx256m";
        String expectedScriptParameters = "";
        String expectedWorkingDirectoryPath = "..";

        settingsEditor.scriptPathTextField.setText(expectedScriptPath);
        settingsEditor.vmParameterEditor.setText(expectedVmParameters);
        settingsEditor.scriptParametersEditor.setText(expectedScriptParameters);
        settingsEditor.workingDirectoryPathTextField.setText(expectedWorkingDirectoryPath);

        Module[] allProjectModules = ModuleManager.getInstance(runConfiguration.getProject()).getSortedModules();
        assertSame("module", allProjectModules[0], runConfiguration.getModule());

        settingsEditor.moduleComboBox.setSelectedItem(allProjectModules[1]);
        settingsEditor.applyEditorTo(runConfiguration);

        assertEquals("script path", expectedScriptPath, runConfiguration.getScriptPath());
        assertEquals("VM parameters", expectedVmParameters, runConfiguration.getVmParameters());
        assertEquals("script parameters", expectedScriptParameters, runConfiguration.getScriptParameters());
        assertEquals("working directory path", expectedWorkingDirectoryPath, runConfiguration.getWorkingDirectoryPath());
        assertSame("module", allProjectModules[1], runConfiguration.getModule());
    }

    public void testConstructsASettingsEditorForAGivenProject() {
        assertSame("settings editor component", settingsEditor.editor, settingsEditor.createEditor());
    }

    public void testLosesItsSettingsEditorComponentWhenDisposedByIdea() {
        assertNotNull("settings editor component", settingsEditor.editor);

        settingsEditor.disposeEditor();
        assertEquals("settings editor component", null, settingsEditor.editor);
    }

    public void testDoesNotDefineAHelpTopicYet() {
        assertEquals("help topic", null, settingsEditor.getHelpTopic());
    }

    public void testUsesAListCellRendererToRenderAGivenModuleInAComboBox() {
        Mock mockModuleType = Mocks.createModuleTypeMock(this);
        Module stubbedModule = module().isA((ModuleType) mockModuleType.proxy()).build();

        boolean nodeIconOpened = true;
        Icon expectedNodeIcon = Icons.SMALLEST;
        mockModuleType.expects(once()).method("getNodeIcon").with(eq(nodeIconOpened)).will(returnValue(expectedNodeIcon));

        moduleComboBoxRenderer.getListCellRendererComponent(new JList(), stubbedModule, -1, false, false);
        assertSame("module icon", expectedNodeIcon, moduleComboBoxRenderer.getIcon());
        assertEquals("module name", stubbedModule.getName(), moduleComboBoxRenderer.getText());
    }

    public void testUsesAListCellRendererThatCanAlsoRenderANullModuleInAComboBox() {
        moduleComboBoxRenderer.getListCellRendererComponent(new JList(), null, -1, false, false);
        assertEquals("module icon", null, moduleComboBoxRenderer.getIcon());
        assertEquals("module name", "", moduleComboBoxRenderer.getText());
    }
}

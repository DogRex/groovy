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

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.RawCommandLineEditor;

public class GroovySettingsEditor extends SettingsEditor<GroovyRunConfiguration> {

    private final Project project;

    final TextFieldWithBrowseButton scriptPathTextField = new TextFieldWithBrowseButton();
    final RawCommandLineEditor vmParameterEditor = new RawCommandLineEditor();
    final RawCommandLineEditor scriptParametersEditor = new RawCommandLineEditor();
    final TextFieldWithBrowseButton workingDirectoryPathTextField = new TextFieldWithBrowseButton();
    final JComboBox moduleComboBox;

    JComponent editor;

    public GroovySettingsEditor(Project project) {
        this.project = project;
        moduleComboBox = new JComboBox(ModuleManager.getInstance(project).getSortedModules());
        editor = createSettingsEditor();
    }

    protected void resetEditorFrom(GroovyRunConfiguration runConfiguration) {
        scriptPathTextField.setText(runConfiguration.getScriptPath());
        vmParameterEditor.setText(runConfiguration.getVmParameters());
        scriptParametersEditor.setText(runConfiguration.getScriptParameters());
        workingDirectoryPathTextField.setText(runConfiguration.getWorkingDirectoryPath());
        moduleComboBox.setSelectedItem(runConfiguration.getModule());
    }

    protected void applyEditorTo(GroovyRunConfiguration runConfiguration) {
        runConfiguration.setScriptPath(scriptPathTextField.getText());
        runConfiguration.setVmParameters(vmParameterEditor.getText());
        runConfiguration.setScriptParameters(scriptParametersEditor.getText());
        runConfiguration.setWorkingDirectoryPath(workingDirectoryPathTextField.getText());
        runConfiguration.setModule((Module) moduleComboBox.getSelectedItem());
    }

    private JComponent createSettingsEditor() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createScriptPathComponent());
        panel.add(Box.createVerticalStrut(10));
        panel.add(createVmParametersComponent());
        panel.add(Box.createVerticalStrut(10));
        panel.add(createScriptParametersComponent());
        panel.add(Box.createVerticalStrut(10));
        panel.add(createWorkingDirectoryPathComponent());
        panel.add(Box.createVerticalStrut(10));
        panel.add(createModuleClasspathComponent());
        return panel;
    }

    private LabeledComponent createScriptPathComponent() {
        FileChooserDescriptor scriptChooserDescriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor();

        String title = "Select Groovy Script";
        scriptChooserDescriptor.setTitle(title);
        scriptPathTextField.addBrowseFolderListener(title, null, project, scriptChooserDescriptor);

        LabeledComponent<TextFieldWithBrowseButton> scriptPathComponent = new LabeledComponent<TextFieldWithBrowseButton>();
        scriptPathComponent.setComponent(scriptPathTextField);
        scriptPathComponent.setText("Groovy &Script:");
        return scriptPathComponent;
    }

    private LabeledComponent createVmParametersComponent() {
        vmParameterEditor.setDialodCaption("VM Parameters");

        LabeledComponent<RawCommandLineEditor> vmParametersComponent = new LabeledComponent<RawCommandLineEditor>();
        vmParametersComponent.setText("&VM Parameters:");
        vmParametersComponent.setComponent(vmParameterEditor);
        return vmParametersComponent;
    }

    private LabeledComponent createScriptParametersComponent() {
        scriptParametersEditor.setDialodCaption("Script Parameters");

        LabeledComponent<RawCommandLineEditor> programParametersComponent = new LabeledComponent<RawCommandLineEditor>();
        programParametersComponent.setText("Script Pa&rameters:");
        programParametersComponent.setComponent(scriptParametersEditor);
        return programParametersComponent;
    }

    private LabeledComponent createWorkingDirectoryPathComponent() {
        FileChooserDescriptor workingDirectoryChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();

        String title = "Select Working Directory";
        workingDirectoryChooserDescriptor.setTitle(title);
        workingDirectoryPathTextField.addBrowseFolderListener(title, null, project, workingDirectoryChooserDescriptor);

        LabeledComponent<TextFieldWithBrowseButton> workingDirectoryPathComponent = new LabeledComponent<TextFieldWithBrowseButton>();
        workingDirectoryPathComponent.setComponent(workingDirectoryPathTextField);
        workingDirectoryPathComponent.setOpaque(true);
        workingDirectoryPathComponent.setText("&Working Directory:");
        return workingDirectoryPathComponent;
    }

    private LabeledComponent createModuleClasspathComponent() {
        moduleComboBox.setRenderer(new ModuleComboBoxRenderer());

        LabeledComponent<JComboBox> moduleClasspathComponent = new LabeledComponent<JComboBox>();
        moduleClasspathComponent.setComponent(moduleComboBox);
        moduleClasspathComponent.setText("Use classpath and JDK of m&odule:");
        return moduleClasspathComponent;
    }

    protected JComponent createEditor() {
        return editor;
    }

    protected void disposeEditor() {
        editor = null;
    }

    public String getHelpTopic() {
        return null;
    }

    static class ModuleComboBoxRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            Module module = (Module) value;
            if (module != null) {
                setIcon(module.getModuleType().getNodeIcon(true));
                setText(module.getName());
            }

            return this;
        }
    }
}

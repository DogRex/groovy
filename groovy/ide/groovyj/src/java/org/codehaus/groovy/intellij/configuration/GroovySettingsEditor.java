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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.intellij.execution.junit2.configuration.ClassBrowser;
import com.intellij.execution.junit2.configuration.CommonJavaParameters;
import com.intellij.execution.junit2.configuration.ConfigurationModuleSelector;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;

public class GroovySettingsEditor extends SettingsEditor {

    private final JComponent editor;

    public GroovySettingsEditor(Project project) {
        editor = createSettingsEditor(project);
    }

    protected void resetEditorFrom(Object source) {}

    protected void applyEditorTo(Object target) {}

    protected JComponent createEditor() {
        return editor;
    }

    private JComponent createSettingsEditor(Project project) {
        CommonJavaParameters commonJavaParameters = createCommonJavaParameters();
        LabeledComponent groovyRunnableComponent = createGroovyRunnableComponent();
        LabeledComponent moduleClasspathComponent = createModuleClasspathComponent();
        createAndLinkUpMainClassToModuleClasspath(project, groovyRunnableComponent, moduleClasspathComponent);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(groovyRunnableComponent);
        panel.add(Box.createVerticalStrut(10));
        panel.add(commonJavaParameters);
        panel.add(Box.createVerticalStrut(10));
        panel.add(moduleClasspathComponent);
        return panel;
    }

    private CommonJavaParameters createCommonJavaParameters() {
        CommonJavaParameters commonJavaParameters = new CommonJavaParameters();
        commonJavaParameters.setProgramParametersText("Program pa&rameters:");
        return commonJavaParameters;
    }

    private LabeledComponent createGroovyRunnableComponent() {
        LabeledComponent groovyRunnableComponent = new LabeledComponent();
        groovyRunnableComponent.setComponent(new TextFieldWithBrowseButton());
        groovyRunnableComponent.setText("&Groovy script:");
        return groovyRunnableComponent;
    }

    private LabeledComponent createModuleClasspathComponent() {
        LabeledComponent moduleClasspathComponent = new LabeledComponent();
        moduleClasspathComponent.setComponent(new JComboBox());
        moduleClasspathComponent.setText("Use classpath and JDK of m&odule:");
        return moduleClasspathComponent;
    }

    private void createAndLinkUpMainClassToModuleClasspath(Project project,
                                                           LabeledComponent groovyRunnableComponent,
                                                           LabeledComponent moduleClasspathComponent) {
        JComboBox moduleClasspathComboBox = (JComboBox) moduleClasspathComponent.getComponent();
        ConfigurationModuleSelector configurationModuleSelector = new ConfigurationModuleSelector(project, moduleClasspathComboBox);
        ClassBrowser applicationClassBrowser = ClassBrowser.createApplicationClassBrowser(project, configurationModuleSelector);
        applicationClassBrowser.setField((TextFieldWithBrowseButton) groovyRunnableComponent.getComponent());
    }

    protected void disposeEditor() {}

    public String getHelpTopic() {
        return null;
    }
}

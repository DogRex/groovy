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

import junitx.framework.ObjectAssert;

import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.filters.TextConsoleBuidlerFactory;
import com.intellij.execution.filters.TextConsoleBuidlerFactoryImpl;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.SettingsEditor;

import org.jdom.Element;

import org.jmock.Mock;

import org.codehaus.groovy.intellij.EditorAPI;

public class GroovyRunConfigurationTest extends GroovyConfigurationTestCase {

    private final Mock mockEditorApi = mock(EditorAPI.class);
    private final Mock mockGroovyRunConfigurationExternalizer = mock(GroovyRunConfigurationExternaliser.class);
    private GroovyRunConfiguration runConfiguration;

    protected void setUp() {
        GroovyRunConfigurationExternaliser runConfigurationExternaliser = (GroovyRunConfigurationExternaliser) mockGroovyRunConfigurationExternalizer.proxy();
        GroovyConfigurationFactory configurationFactory = new GroovyConfigurationFactory(null, null, runConfigurationExternaliser);
        runConfiguration = new GroovyRunConfiguration(null, createStubbedProject(), configurationFactory, (EditorAPI) mockEditorApi.proxy());
    }

    public void testCreatesAConfigurationEditorForAnyGivenProject() {
        MockApplicationManager.reset();

        Mock mockActionManager = mock(ActionManager.class);
        MockApplicationManager.getMockApplication().registerComponent(ActionManager.class, mockActionManager.proxy());
        mockActionManager.expects(atLeastOnce()).method("createActionPopupMenu").withAnyArguments().will(returnValue(null));

        SettingsEditor configurationEditor = runConfiguration.getConfigurationEditor();
        assertNotNull("configuration editor", configurationEditor);
    }

    public void testThrowsARunConfigurationErrorWhenValidatedAndThePathToAGroovyScriptIsNotDefined() {
        runConfiguration.setScriptPath(null);
        try {
            runConfiguration.checkConfiguration();
            fail("The path to a Groovy script must be defined!");
        } catch (RuntimeConfigurationException e) {
            assertEquals("exception message", "Groovy script not specified", e.getMessage());
        }
    }

    public void testThrowsARunConfigurationErrorWhenValidatedAndThePathToAGroovyScriptIsBlank() {
        runConfiguration.setScriptPath("    \t   ");
        try {
            runConfiguration.checkConfiguration();
            fail("The path to a Groovy script must be defined!");
        } catch (RuntimeConfigurationException e) {
            assertEquals("exception message", "Groovy script not specified", e.getMessage());
        }
    }

    public void testThrowsARunConfigurationErrorWhenValidatedAndNoModuleIsSelected() {
        runConfiguration.setScriptPath("/home/foo/acme/scripts/bar.groovy");
        runConfiguration.setModule(null);
        try {
            runConfiguration.checkConfiguration();
            fail("A module must be selected!");
        } catch (RuntimeConfigurationException e) {
            assertEquals("exception message", "Module not selected", e.getMessage());
        }
    }

    public void testDoesNotReportAnyErrorsWhenValidatedWithAValidPathAndModule() throws RuntimeConfigurationException {
        runConfiguration.setScriptPath("/home/foo/acme/scripts/bar.groovy");
        runConfiguration.setModule(createStubbedModule());
        runConfiguration.checkConfiguration();
    }

    public void testCreatesACommandLineStateAsTheRunProfileState() {
        MockApplicationManager.reset();
        MockApplicationManager.getMockApplication().registerComponent(TextConsoleBuidlerFactory.class, new TextConsoleBuidlerFactoryImpl());

        mockEditorApi.expects(once()).method("getModuleAndDependentModules").withAnyArguments().will(returnValue(Module.EMPTY_ARRAY));

        ObjectAssert.assertInstanceOf("run profile state", GroovyComandLineState.class, runConfiguration.getState(null, null, null, null));
    }

    public void testSerialisesAGivenElementUsingItsAssociatedExternaliser() {
        Element expectedElement = new Element("dummy");
        mockGroovyRunConfigurationExternalizer.expects(once()).method("writeExternal").with(same(runConfiguration), same(expectedElement));

        runConfiguration.writeExternal(expectedElement);
    }

    public void testDeserialisesAGivenElementUsingItsAssociatedExternaliser() {
        Element expectedElement = new Element("dummy");
        mockGroovyRunConfigurationExternalizer.expects(once()).method("readExternal").with(same(runConfiguration), same(expectedElement));

        runConfiguration.readExternal(expectedElement);
    }
}

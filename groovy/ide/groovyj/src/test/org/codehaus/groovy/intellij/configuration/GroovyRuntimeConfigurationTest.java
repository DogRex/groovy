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

import com.intellij.execution.filters.TextConsoleBuidlerFactory;
import com.intellij.execution.filters.TextConsoleBuidlerFactoryImpl;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

import org.codehaus.groovy.intellij.EditorAPI;
import org.codehaus.groovy.intellij.Mocks;

public class GroovyRuntimeConfigurationTest extends MockObjectTestCase {

    private final Mock mockProject = mock(Project.class);
    private final Mock mockEditorApi = mock(EditorAPI.class);
    private GroovyRuntimeConfiguration runtimeConfiguration;

    protected void setUp() {
        assertWorkingDirectoryIsInitialisedAtConstructionTime();
        runtimeConfiguration = new GroovyRuntimeConfiguration(null, (Project) mockProject.proxy(), null, (EditorAPI) mockEditorApi.proxy());
    }

    private void assertWorkingDirectoryIsInitialisedAtConstructionTime() {
        Mock mockProjectFile = Mocks.createVirtualFileMock(this, "mockProjectFile");
        mockProject.expects(once()).method("getProjectFile").will(returnValue(mockProjectFile.proxy()));

        Mock mockProjectFileParentDirectory = Mocks.createVirtualFileMock(this, "mockProjectFileParentDirectory");
        mockProjectFile.expects(once()).method("getParent").will(returnValue(mockProjectFileParentDirectory.proxy()));
        mockProjectFileParentDirectory.expects(once()).method("getPath").will(returnValue("somewhere on the filesystem"));
    }

    public void testCreatesAConfigurationEditorForAnyGivenProject() {
        MockApplicationManager.reset();

        Mock mockActionManager = mock(ActionManager.class);
        MockApplicationManager.getMockApplication().registerComponent(ActionManager.class, mockActionManager.proxy());
        mockActionManager.expects(atLeastOnce()).method("createActionPopupMenu").withAnyArguments().will(returnValue(null));
        mockProject.expects(once()).method("isDefault").withNoArguments().will(returnValue(false));

        SettingsEditor configurationEditor = runtimeConfiguration.getConfigurationEditor();
        assertNotNull("configuration editor", configurationEditor);
    }

    public void testDoesNotProvideARunProfileStateYet() {
        MockApplicationManager.reset();
        MockApplicationManager.getMockApplication().registerComponent(TextConsoleBuidlerFactory.class, new TextConsoleBuidlerFactoryImpl());
        mockEditorApi.expects(once()).method("getModuleAndDependentModules").withAnyArguments().will(returnValue(Module.EMPTY_ARRAY));
        ObjectAssert.assertInstanceOf("run profile state", GroovyComandLineState.class, runtimeConfiguration.getState(null, null, null, null));
    }

    public void testDoesNothingWhenSerialisedToDisk() {
        runtimeConfiguration.writeExternal(null);
    }

    public void testDoesNothingWhenDeserialisedFromDisk() {
        runtimeConfiguration.readExternal(null);
    }
}

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

import javax.swing.Icon;

import junitx.framework.ObjectAssert;

import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.execution.Location;
import com.intellij.execution.PsiLocation;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiClass;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

import org.codehaus.groovy.intellij.Mocks;

public class GroovyConfigurationTypeTest extends MockObjectTestCase {

    private final GroovyConfigurationType configurationType = new GroovyConfigurationType(null);

    public void testHasAComponentName() {
        assertEquals("groovy.configuration.type", configurationType.getComponentName());
    }

    public void testDoesNothingWhenInitialisedByIdea() {
        configurationType.initComponent();
    }

    public void testDoesNothingWhenDisposedByIdea() {
        configurationType.disposeComponent();
    }

    public void testHasADisplayName() {
        assertEquals("Groovy", configurationType.getDisplayName());
    }

    public void testHasADescription() {
        assertEquals("Groovy configuration", configurationType.getConfigurationTypeDescription());
    }

    public void testHasAnIcon() {
        assertNotNull(configurationType.getIcon());
        ObjectAssert.assertInstanceOf(Icon.class, configurationType.getIcon());
    }

    public void testHasExactlyOneConfigurationFactory() {
        ConfigurationFactory[] configurationFactories = configurationType.getConfigurationFactories();
        assertNotNull(configurationFactories);
        assertEquals(1, configurationFactories.length);
        assertSame(configurationType, configurationFactories[0].getType());
    }

    public void testReturnsANullConfigurationWhenTheGivenLocationIsNull() {
        assertEquals("runner and configuration settings", null, configurationType.createConfigurationByLocation(null));
    }

    public void testReturnsANullConfigurationWhenTheGivenLocationIsNotBoundToAPsiElement() {
        Mock mockLocation = mock(Location.class);
        mockLocation.expects(once()).method("getPsiElement").will(returnValue(null));

        RunnerAndConfigurationSettings configuration = configurationType.createConfigurationByLocation((Location) mockLocation.proxy());
        assertEquals("runner and configuration settings", null, configuration);
    }

    public void testReturnsANullConfigurationWhenTheGivenLocationDoesNotCorrespondToARunnablePsiGroovyElement() {
        MockApplicationManager.reset();

        Mock mockProject = mock(Project.class);
        Mock mockPsiElement = mock(PsiElement.class);
        Location psiLocation = new PsiLocation((Project) mockProject.proxy(), (PsiElement) mockPsiElement.proxy());

        RunnerAndConfigurationSettings configuration = configurationType.createConfigurationByLocation(psiLocation);
        assertEquals("runner and configuration settings", null, configuration);
    }

    public void testDoesNotOfferToConfigureByElementWhenTheGivenConfigurationIsNotAGroovyOne() {
        assertEquals("configure by element", false, configurationType.isConfigurationByElement(null, null, null));
    }

    public void testDoesNotOfferToConfigureByElementWhenTheCurrentPsiElementDoesNotComeFromAGroovyScript() {
        MockApplicationManager.reset();

        Mock mockProject = mock(Project.class);
        Mock mockProjectFile = Mocks.createVirtualFileMock(this, "mockProjectFile");
        mockProject.expects(once()).method("getProjectFile").will(returnValue(mockProjectFile.proxy()));

        Mock mockProjectFileParentDirectory = Mocks.createVirtualFileMock(this, "mockProjectFileParentDirectory");
        mockProjectFile.expects(once()).method("getParent").will(returnValue(mockProjectFileParentDirectory.proxy()));
        mockProjectFileParentDirectory.expects(once()).method("getPath").will(returnValue("somewhere on the filesystem"));

        GroovyRuntimeConfiguration groovyRuntimeConfiguration = new GroovyRuntimeConfiguration(null, (Project) mockProject.proxy(), null, null);
        assertEquals("configure by element", false, configurationType.isConfigurationByElement(groovyRuntimeConfiguration, null, null));
    }
}

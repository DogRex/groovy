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

import com.intellij.execution.Location;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.css.CssFileType;
import junitx.framework.ObjectAssert;
import org.codehaus.groovy.intellij.GroovySupportLoader;
import org.jmock.Mock;

import javax.swing.*;

public class GroovyConfigurationTypeTest extends GroovyConfigurationTestCase {

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
        Mock stubLocation = mock(Location.class);
        stubLocation.stubs().method("getPsiElement").will(returnValue(null));

        RunnerAndConfigurationSettings settings = configurationType.createConfigurationByLocation((Location) stubLocation.proxy());
        assertEquals("runner and configuration settings", null, settings);
    }

    public void testReturnsANullConfigurationWhenTheGivenLocationIsNotBoundToAnOpenFileDescriptor() {
        Mock stubLocation = mock(Location.class);
        stubLocation.stubs().method("getPsiElement").will(returnValue(mock(PsiElement.class).proxy()));
        stubLocation.stubs().method("getOpenFileDescriptor").will(returnValue(null));

        RunnerAndConfigurationSettings settings = configurationType.createConfigurationByLocation((Location) stubLocation.proxy());
        assertEquals("runner and configuration settings", null, settings);
    }

    public void testReturnsANullConfigurationWhenTheGivenLocationDoesNotPointToAGroovyScript() {
        Mock stubLocation = mock(Location.class);
        stubLocation.stubs().method("getPsiElement").will(returnValue(mock(PsiElement.class).proxy()));

        VirtualFile file = virtualFile().withFileType(StdFileTypes.XML).build();
        OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(null, file, -1);
        stubLocation.stubs().method("getOpenFileDescriptor").will(returnValue(openFileDescriptor));

        RunnerAndConfigurationSettings settings = configurationType.createConfigurationByLocation((Location) stubLocation.proxy());
        assertEquals("runner and configuration settings", null, settings);
    }

    public void testCreatesAConfigurationWhenTheGivenLocationPointsToAGroovyScript() {
        Location<PsiElement> stubbedLocation = createStubbedLocationPretendingToPointToAGroovyScript();
        RunnerAndConfigurationSettings settings = configurationType.createConfigurationByLocation(stubbedLocation);
        assertNotNull("runner and configuration settings should have been created", settings.getConfiguration());
    }

    public void testDoesNotOfferToConfigureByElementWhenTheGivenConfigurationIsNotAGroovyOne() {
        assertEquals("configure by element", false, configurationType.isConfigurationByElement(null, null, null));
    }

    public void testDoesNotOfferToConfigureByElementWhenTheCurrentPsiElementDoesNotComeFromAGroovyScript() {
        GroovyConfigurationFactory configurationFactory = new GroovyConfigurationFactory(null, configurationType, null);
        GroovyRunConfiguration runConfiguration = new GroovyRunConfiguration(null, project().withProjectFile().build(), configurationFactory, null);

        VirtualFile file = virtualFile().withFileType(new CssFileType()).build();
        PsiElement psiElement = createStubbedPsiElementFor(file);
        assertEquals("configure by element", false, configurationType.isConfigurationByElement(runConfiguration, null, psiElement));
    }

    public void testOffersToConfigureByElementWhenTheCurrentPsiElementComesFromAGroovyScript() {
        GroovyConfigurationFactory configurationFactory = new GroovyConfigurationFactory(null, configurationType, null);
        GroovyRunConfiguration runConfiguration = new GroovyRunConfiguration(null, project().withProjectFile().build(), configurationFactory, null);
        runConfiguration.setScriptPath("/home/foo/acme/src/scripts/bar.groovy");

        PsiElement psiElement = createStubbedPsiElementFromGroovyScript(runConfiguration.getScriptPath());
        assertEquals("configure by element", true, configurationType.isConfigurationByElement(runConfiguration, null, psiElement));
    }

    private Location<PsiElement> createStubbedLocationPretendingToPointToAGroovyScript() {
        Mock stubLocation = mock(Location.class);

        Mock mockProjectFileIndex = mock(ProjectFileIndex.class);
        Project stubbedProject = project()
                .withProjectFile((ProjectFileIndex) mockProjectFileIndex.proxy())
                .canCreateRunConfigurations()
                .build();

        stubLocation.stubs().method("getProject").will(returnValue(stubbedProject));

        Mock stubPsiElement = mock(PsiElement.class);
        stubLocation.stubs().method("getPsiElement").will(returnValue(stubPsiElement.proxy()));

        Mock stubPsiManager = mock(PsiManager.class);
        stubPsiElement.stubs().method("getManager").will(returnValue(stubPsiManager.proxy()));
        stubPsiManager.stubs().method("getProject").will(returnValue(stubbedProject));

        VirtualFile groovyScriptFile = createGroovyScriptFile();
        mockProjectFileIndex.expects(once()).method("getModuleForFile").with(same(groovyScriptFile)).will(returnValue(module().build()));;
        stubLocation.stubs().method("getOpenFileDescriptor").will(returnValue(new OpenFileDescriptor(stubbedProject, groovyScriptFile, -1)));

        return (Location<PsiElement>) stubLocation.proxy();
    }

    private PsiElement createStubbedPsiElementFromGroovyScript(String scriptPath) {
        VirtualFile script = virtualFile().withPath(scriptPath).withFileType(GroovySupportLoader.GROOVY).build();
        return createStubbedPsiElementFor(script);
    }

    private PsiElement createStubbedPsiElementFor(VirtualFile virtualFile) {
        Mock stubPsiFile = mock(PsiFile.class);
        stubPsiFile.stubs().method("getVirtualFile").will(returnValue(virtualFile));

        Mock stubPsiElement = mock(PsiElement.class);
        stubPsiElement.stubs().method("getContainingFile").will(returnValue(stubPsiFile.proxy()));

        return (PsiElement) stubPsiElement.proxy();
    }

    private VirtualFile createGroovyScriptFile() {
        return virtualFile()
                .withPath("/home/foo/acme/src/scripts/FooBar.groovy")
                .withFileType(GroovySupportLoader.GROOVY)
                .build();
    }
}

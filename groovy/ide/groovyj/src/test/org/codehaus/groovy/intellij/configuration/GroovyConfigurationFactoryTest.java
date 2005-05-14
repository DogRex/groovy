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

import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import com.intellij.testFramework.MockVirtualFile;

import org.jmock.Mock;

import org.codehaus.groovy.intellij.EditorAPIFactory;
import org.codehaus.groovy.intellij.Mocks;

public class GroovyConfigurationFactoryTest extends GroovyConfigurationTestCase {

    private final Mock mockEditorApiFactory = mock(EditorAPIFactory.class);

    private final GroovyConfigurationFactory configurationFactory = new GroovyConfigurationFactory((EditorAPIFactory) mockEditorApiFactory.proxy(),
                                                                                                   new GroovyConfigurationType(null),
                                                                                                   null);

    public void testCreatesARuntimeConfigurationForAGivenProject() {
        Mock mockProject = mock(Project.class);
        Mock mockProjectFile = Mocks.createVirtualFileMock(this, "mockProjectFile");
        mockProject.expects(once()).method("getProjectFile").withNoArguments().will(returnValue(mockProjectFile.proxy()));
        mockProjectFile.expects(once()).method("getParent").withNoArguments().will(returnValue(new MockVirtualFile()));

        mockEditorApiFactory.expects(once()).method("createEditorAPI").with(same(mockProject.proxy())).will(returnValue(null));

        RunConfiguration templateConfiguration = configurationFactory.createTemplateConfiguration((Project) mockProject.proxy());
        assertSame("project", mockProject.proxy(), templateConfiguration.getProject());
        assertSame("configuration factory", configurationFactory, templateConfiguration.getFactory());
    }
}

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


package org.codehaus.groovy.intellij;

import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.project.Project;

import org.jmock.Mock;

import org.codehaus.groovy.intellij.compiler.GroovyCompiler;

public class GroovyJProjectComponentTest extends GroovyjTestCase {

    private final Mock mockCompilerManager = mock(CompilerManager.class);
    private final Mock mockProject = mock(Project.class);
    private final Project selectedProject = (Project) mockProject.proxy();

    private GroovyJProjectComponent projectComponent;

    protected void setUp() {
        assertNull(GroovyJProjectComponent.getInstance(selectedProject));

        Mock mockEditorAPIFactory = mock(EditorAPIFactory.class);
        projectComponent = new GroovyJProjectComponent(selectedProject, (EditorAPIFactory) mockEditorAPIFactory.proxy());
        assertSame(projectComponent, GroovyJProjectComponent.getInstance(selectedProject));

        Mock mockEditorAPI = mock(EditorAPI.class);
        mockEditorAPIFactory.stubs().method("createEditorAPI").withAnyArguments().will(returnValue(mockEditorAPI.proxy()));

        mockProject.stubs().method("getComponent").with(same(CompilerManager.class)).will(returnValue(mockCompilerManager.proxy()));
    }

    protected void tearDown() {
        GroovyJProjectComponent.setInstance(selectedProject, null);
    }

    public void testInitialisesGroovyControllerAndCompilerWithEditorApiWhenTheProjectIsOpened() {
        assertNull("reference to EditorAPI should not have been initialised", projectComponent.getEditorApi());
        assertNull("reference to GroovyController should not have been initialised", projectComponent.getGroovyController());

        mockCompilerManager.expects(once()).method("addCompiler").with(isA(GroovyCompiler.class));

        projectComponent.projectOpened();
        assertNotNull("reference to EditorAPI should have been initialised", projectComponent.getEditorApi());
        assertNotNull("reference to GroovyController should have been initialised", projectComponent.getGroovyController());
    }

    public void testRemovesReferencesToEditorApiAndGroovyControllerAndCompilerWhenTheProjectIsClosed() {
        mockCompilerManager.expects(once()).method("addCompiler").with(isA(GroovyCompiler.class));

        projectComponent.projectOpened();
        assertNotNull("reference to EditorAPI should have been initialised", projectComponent.getEditorApi());
        assertNotNull("reference to GroovyController should have been initialised", projectComponent.getGroovyController());

        mockCompilerManager.expects(once()).method("removeCompiler").with(isA(GroovyCompiler.class));

        projectComponent.projectClosed();
        assertNull("reference to EditorAPI should have been removed", projectComponent.getEditorApi());
        assertNull("reference to GroovyController should have been removed", projectComponent.getGroovyController());
    }

    public void testDoesNothingWhenInitialisedByIdea() {
        projectComponent.initComponent();
    }

    public void testDoesNothingWhenDisposedByIdea() {
        projectComponent.disposeComponent();
    }

    public void testHasAComponentName() {
        assertEquals("groovyj.project.plugin", projectComponent.getComponentName());
    }
}

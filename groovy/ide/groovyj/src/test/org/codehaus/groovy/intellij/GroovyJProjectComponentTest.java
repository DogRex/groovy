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

import com.intellij.openapi.project.Project;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class GroovyJProjectComponentTest extends MockObjectTestCase {

    private final Project projectMock = (Project) mock(Project.class).proxy();

    private GroovyJProjectComponent projectComponent;

    protected void setUp() {
        assertNull(GroovyJProjectComponent.getInstance(projectMock));

        Mock mockEditorAPIFactory = mock(EditorAPIFactory.class);
        projectComponent = new GroovyJProjectComponent(projectMock, (EditorAPIFactory) mockEditorAPIFactory.proxy());
        assertSame(projectComponent, GroovyJProjectComponent.getInstance(projectMock));

        Mock mockEditorAPI = mock(EditorAPI.class);
        mockEditorAPIFactory.stubs().method("createEditorAPI").withAnyArguments().will(returnValue(mockEditorAPI.proxy()));
    }

    protected void tearDown() {
        GroovyJProjectComponent.setInstance(projectMock, null);
    }

    public void testInitialisesEditorApiAndGroovyControllerReferencesWhenTheProjectIsOpened() {
        assertNull("reference to EditorAPI should not have been initialised", projectComponent.getEditorApi());
        assertNull("reference to GroovyController should not have been initialised", projectComponent.getGroovyController());

        projectComponent.projectOpened();

        assertNotNull("reference to EditorAPI should have been initialised", projectComponent.getEditorApi());
        assertNotNull("reference to GroovyController should have been initialised", projectComponent.getGroovyController());
    }

    public void testRemovesReferencesToEditorApiAndGroovyControllerWhenTheProjectIsClosed() {
        projectComponent.projectOpened();
        assertNotNull("reference to EditorAPI should have been initialised", projectComponent.getEditorApi());
        assertNotNull("reference to GroovyController should have been initialised", projectComponent.getGroovyController());

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

/*
 * $Id$
 *
 * Copyright (c) 2004 The Codehaus - http://groovy.codehaus.org
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


package org.codehaus.groovy.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;

import org.jmock.cglib.Mock;
import org.jmock.cglib.MockObjectTestCase;

import org.codehaus.groovy.intellij.GroovyJProjectComponent;
import org.codehaus.groovy.intellij.Mocks;

public class ActionEventsTest extends MockObjectTestCase {

    private final Mock mockDataContext = new Mock(DataContext.class);
    private final Mock mockVirtualFile = Mocks.createVirtualFileMock();
    private final Mock mockProject = new Mock(Project.class);
    private final Mock mockGroovyJProjectComponent = Mocks.createGroovyJProjectComponentMock();

    private Project projectMock = (Project) mockProject.proxy();
    private GroovyJProjectComponent projectComponentMock = (GroovyJProjectComponent) mockGroovyJProjectComponent.proxy();

    private AnActionEvent anActionEvent;
    private final ActionEvents actionEvents = new ActionEvents();

    protected void setUp() throws Exception {
        super.setUp();

        AnAction action = new AnAction() {
            public void actionPerformed(AnActionEvent event) {}
        };

        anActionEvent = new AnActionEvent(null, (DataContext) mockDataContext.proxy(), "", action.getTemplatePresentation(), null, -1);

        mockDataContext.stubs().method("getData").with(eq(DataConstants.VIRTUAL_FILE)).will(returnValue(mockVirtualFile.proxy()));
        mockDataContext.stubs().method("getData").with(eq(DataConstants.PROJECT)).will(returnValue(projectMock));
        GroovyJProjectComponent.setInstance(projectMock, projectComponentMock);
    }

    public void testRetrievesTheVirtualFileFromWhichAGivenActionEventOriginated() {
        assertSame(mockVirtualFile.proxy(), actionEvents.getVirtualFile(anActionEvent));
    }

    public void testRetrievesTheProjectFromWhichAGivenActionEventOriginated() {
        assertSame(projectMock, actionEvents.getProject(anActionEvent));
    }

    public void testRetrievesTheGroovyjProjectComponentInstanceAssociatedToTheProjectFromWhichAGivenActionEventOriginated() {
        assertSame(projectComponentMock, actionEvents.getGroovyJProjectComponent(anActionEvent));
    }

    public void testDeterminesThatASelectedFileIsAGroovyFileIfItHasTheGroovyExtension() {
        mockVirtualFile.expects(once()).method("getExtension").will(returnValue("groovy"));
        assertTrue(actionEvents.isGroovyFile(anActionEvent));
    }

    public void testDeterminesThatASelectedFileIsNotAGroovyFileIfItDoesNotHaveTheGroovyExtension() {
        mockVirtualFile.expects(once()).method("getExtension").will(returnValue("java"));
        assertFalse(actionEvents.isGroovyFile(anActionEvent));
    }

    public void testDeterminesThatNoSelectedFileMeansThatThereIsNoGroovyFileSelected() {
        mockDataContext.expects(once()).method("getData").with(eq(DataConstants.VIRTUAL_FILE));
        assertFalse(actionEvents.isGroovyFile(anActionEvent));
    }
}

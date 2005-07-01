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


package org.codehaus.groovy.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;

import org.jmock.Mock;

import org.codehaus.groovy.intellij.GroovyJProjectComponent;
import org.codehaus.groovy.intellij.GroovyjTestCase;
import org.codehaus.groovy.intellij.Mocks;

public class ActionEventsTest extends GroovyjTestCase {

    private final Mock mockDataContext = mock(DataContext.class);
    private final VirtualFileBuilder virtualFileBuilder = virtualFile();

    private final Project projectMock = (Project) mock(Project.class).proxy();
    private final Module moduleMock = (Module) mock(Module.class).proxy();
    private final GroovyJProjectComponent projectComponentMock = (GroovyJProjectComponent) Mocks.createGroovyJProjectComponentMock(this).proxy();
    private final ActionEvents actionEvents = new ActionEvents();

    private AnActionEvent anActionEvent;

    protected void setUp() {
        AnAction action = new AnAction() {
            public void actionPerformed(AnActionEvent event) {}
        };

        anActionEvent = new AnActionEvent(null, (DataContext) mockDataContext.proxy(), "", action.getTemplatePresentation(), null, -1);

        mockDataContext.stubs().method("getData").with(eq(DataConstants.VIRTUAL_FILE)).will(returnValue(virtualFileBuilder.build()));
        mockDataContext.stubs().method("getData").with(eq(DataConstants.PROJECT)).will(returnValue(projectMock));
        mockDataContext.stubs().method("getData").with(eq(DataConstants.MODULE)).will(returnValue(moduleMock));

        GroovyJProjectComponent.setInstance(projectMock, projectComponentMock);
    }

    public void testRetrievesTheVirtualFileFromWhichAGivenActionEventOriginated() {
        assertSame(virtualFileBuilder.build(), actionEvents.getVirtualFile(anActionEvent));
    }

    public void testRetrievesTheProjectFromWhichAGivenActionEventOriginated() {
        assertSame(projectMock, actionEvents.getProject(anActionEvent));
    }

    public void testRetrievesTheModuleFromWhichAGivenActionEventOriginated() {
        assertSame(moduleMock, actionEvents.getModule(anActionEvent));
    }

    public void testRetrievesTheGroovyjProjectComponentInstanceAssociatedToTheProjectFromWhichAGivenActionEventOriginated() {
        assertSame(projectComponentMock, actionEvents.getGroovyJProjectComponent(anActionEvent));
    }

    public void testDeterminesThatASelectedFileIsAGroovyFileIfItHasTheGroovyExtension() {
        virtualFileBuilder.withExtension("groovy");
        assertTrue(actionEvents.isGroovyFile(anActionEvent));
    }

    public void testDeterminesThatASelectedFileIsAGroovyFileIfItHasTheGshExtension() {
        virtualFileBuilder.withExtension("gsh");
        assertTrue(actionEvents.isGroovyFile(anActionEvent));
    }

    public void testDeterminesThatASelectedFileIsAGroovyFileIfItHasTheGvyExtension() {
        virtualFileBuilder.withExtension("gvy");
        assertTrue(actionEvents.isGroovyFile(anActionEvent));
    }

    public void testDeterminesThatASelectedFileIsAGroovyFileIfItHasTheGyExtension() {
        virtualFileBuilder.withExtension("gy");
        assertTrue(actionEvents.isGroovyFile(anActionEvent));
    }

    public void testDeterminesThatASelectedFileIsNotAGroovyFileIfItDoesNotHaveTheGroovyExtension() {
        virtualFileBuilder.withExtension("java");
        assertFalse(actionEvents.isGroovyFile(anActionEvent));
    }

    public void testDeterminesThatNoSelectedFileMeansThatThereIsNoGroovyFileSelected() {
        mockDataContext.expects(once()).method("getData").with(eq(DataConstants.VIRTUAL_FILE));
        assertFalse(actionEvents.isGroovyFile(anActionEvent));
    }
}

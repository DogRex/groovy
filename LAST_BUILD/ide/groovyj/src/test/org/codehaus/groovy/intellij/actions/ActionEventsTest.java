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


package org.codehaus.groovy.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import org.codehaus.groovy.intellij.GroovyJProjectComponent;
import org.codehaus.groovy.intellij.GroovySupportLoader;
import org.codehaus.groovy.intellij.GroovyjTestCase;
import org.codehaus.groovy.intellij.Mocks;
import org.jmock.Mock;

public class ActionEventsTest extends GroovyjTestCase {

    private final Mock mockDataContext = mock(DataContext.class);
    private final VirtualFileBuilder virtualFileBuilder = virtualFile();

    private final Project projectMock = (Project) mock(Project.class).proxy();
    private final Module moduleMock = (Module) mock(Module.class).proxy();
    private final GroovyJProjectComponent projectComponentMock = (GroovyJProjectComponent) Mocks.createGroovyJProjectComponentMock(this).proxy();
    private final ActionEvents actionEvents = new ActionEvents();

    private AnActionEvent actionEvent;

    protected void setUp() {
        AnAction action = new AnAction() {
            public void actionPerformed(AnActionEvent event) {}
        };

        actionEvent = new AnActionEvent(null, (DataContext) mockDataContext.proxy(), "", action.getTemplatePresentation(), null, -1);

        mockDataContext.stubs().method("getData").with(eq(DataConstants.VIRTUAL_FILE)).will(returnValue(virtualFileBuilder.build()));
        mockDataContext.stubs().method("getData").with(eq(DataConstants.PROJECT)).will(returnValue(projectMock));
        mockDataContext.stubs().method("getData").with(eq(DataConstants.MODULE)).will(returnValue(moduleMock));

        GroovyJProjectComponent.setInstance(projectMock, projectComponentMock);
    }

    public void testRetrievesTheVirtualFileFromWhichAGivenActionEventOriginated() {
        assertSame(virtualFileBuilder.build(), actionEvents.getVirtualFile(actionEvent));
    }

    public void testRetrievesTheProjectFromWhichAGivenActionEventOriginated() {
        assertSame(projectMock, actionEvents.getProject(actionEvent));
    }

    public void testRetrievesTheModuleFromWhichAGivenActionEventOriginated() {
        assertSame(moduleMock, actionEvents.getModule(actionEvent));
    }

    public void testRetrievesTheGroovyjProjectComponentInstanceAssociatedToTheProjectFromWhichAGivenActionEventOriginated() {
        assertSame(projectComponentMock, actionEvents.getGroovyJProjectComponent(actionEvent));
    }

    public void testDeterminesThatAnEventOriginatedFromAGroovyFileIfItHasTheGroovyFileType() {
        virtualFileBuilder.withFileType(GroovySupportLoader.GROOVY);
        assertTrue(actionEvents.isEventOriginatingFromGroovyFile(actionEvent));
    }

    public void testDeterminesThatAnEventDidNotOriginateFromAGroovyFileIfItDoesNotHaveTheGroovyFileType() {
        virtualFileBuilder.withFileType(StdFileTypes.JAVA);
        assertFalse(actionEvents.isEventOriginatingFromGroovyFile(actionEvent));
    }

    public void testDeterminesThatAnEventDidNotOriginateFromAGroovyFileWhenNoFileWasSelected() {
        mockDataContext.expects(once()).method("getData").with(eq(DataConstants.VIRTUAL_FILE)).will(returnValue(null));;
        assertFalse(actionEvents.isEventOriginatingFromGroovyFile(actionEvent));
    }
}

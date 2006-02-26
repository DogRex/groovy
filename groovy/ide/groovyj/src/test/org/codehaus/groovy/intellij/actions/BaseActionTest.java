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

import java.awt.Container;
import java.awt.event.KeyEvent;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;

import org.jmock.Mock;

import org.codehaus.groovy.intellij.GroovyjTestCase;
import org.codehaus.groovy.intellij.Mocks;

public class BaseActionTest extends GroovyjTestCase {

    private static final KeyEvent NULL_KEY_EVENT = new KeyEvent(new Container(), 0, 0, 0, 0, ' ');

    protected final Mock mockActionEvents = mock(ActionEvents.class);
    private final Mock mockGroovyJProjectComponent = Mocks.createGroovyJProjectComponentMock(this);

    protected void setUp() {
        ActionEvents.instance = (ActionEvents) mockActionEvents.proxy();
    }

    protected void tearDown() {
        ActionEvents.instance = new ActionEvents();
    }

    protected BaseAction createAction() {
        return new BaseAction() {
            public void actionPerformed(AnActionEvent e) {}
        };
    }

    protected void executeAction() {
        mockActionEvents.expects(once()).method("getGroovyJProjectComponent").with(isA(AnActionEvent.class)).
                will(returnValue(mockGroovyJProjectComponent.proxy()));

        BaseAction action = createAction();
        action.actionPerformed(createAnActionEvent(action));
    }

    public void testIsBothEnabledAndVisibleIfEventOriginatedFromAGroovyFile() {
        assertActionEnabledAndVisibleIfEventOriginatedFromGroovFileInProject(createAction(), true);
    }

    public void testIsNotEnabledButRemainsVisibleIfEventDidNotOriginateFromAGroovyFile() {
        assertActionEnabledAndVisibleIfEventOriginatedFromGroovFileInProject(createAction(), false);
    }

    protected void assertActionEnabledAndVisibleIfEventOriginatedFromGroovFileInProject(AnAction action, boolean isGroovyFile) {
        Presentation presentation = action.getTemplatePresentation();
        presentation.setEnabled(!isGroovyFile);
        assertEquals("action enabled?", !isGroovyFile, presentation.isEnabled());
        assertEquals("action visible?", true, presentation.isVisible());

        mockActionEvents.expects(once()).method("isGroovyFile").with(isA(AnActionEvent.class)).will(returnValue(isGroovyFile));

        AnActionEvent actionEvent = createAnActionEvent(action);
        action.update(actionEvent);
        assertEquals("action enabled?", isGroovyFile, actionEvent.getPresentation().isEnabled());
        assertEquals("action visible?", true, actionEvent.getPresentation().isVisible());
    }

    protected AnActionEvent createAnActionEvent(AnAction action) {
        DataContext dataContextMock = (DataContext) mock(DataContext.class).proxy();
        return new AnActionEvent(NULL_KEY_EVENT, dataContextMock, "", action.getTemplatePresentation(), null, -1);
    }
}

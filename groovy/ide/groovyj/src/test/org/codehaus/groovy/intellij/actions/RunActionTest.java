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

import com.intellij.openapi.actionSystem.AnActionEvent;

import org.jmock.Mock;

import org.codehaus.groovy.intellij.Mocks;

public class RunActionTest extends BaseActionTest {

    protected BaseAction createAction() {
        return new RunAction();
    }

    public void testUsesTheGroovyControllerAssociatedToTheCurrentProjectToRunTheSelectedFileAsAGroovyScript() {
        Mock mockGroovyController = Mocks.createGroovyControllerMock(this);
        mockActionEvents.expects(once()).method("getVirtualFile").with(isA(AnActionEvent.class));
        mockGroovyJProjectComponent.expects(once()).method("getGroovyController").will(returnValue(mockGroovyController.proxy()));
        mockGroovyController.expects(once()).method("runAsGroovyScript");

        executeAction();
    }
}

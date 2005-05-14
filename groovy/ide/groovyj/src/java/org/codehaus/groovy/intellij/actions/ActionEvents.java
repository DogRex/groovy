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

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import org.codehaus.groovy.intellij.GroovyJProjectComponent;

public class ActionEvents {

    static ActionEvents instance = new ActionEvents();

    public GroovyJProjectComponent getGroovyJProjectComponent(AnActionEvent event) {
        return GroovyJProjectComponent.getInstance(getProject(event));
    }

    public boolean isGroovyFile(AnActionEvent event) {
        VirtualFile file = getVirtualFile(event);
        if (file != null) {
            return "groovy".equals(file.getExtension());
        }
        return false;
    }

    public VirtualFile getVirtualFile(AnActionEvent event) {
        return (VirtualFile) event.getDataContext().getData(DataConstants.VIRTUAL_FILE);
    }

    public Project getProject(AnActionEvent event) {
        return (Project) event.getDataContext().getData(DataConstants.PROJECT);
    }

    public Module getModule(AnActionEvent event) {
        return (Module) event.getDataContext().getData(DataConstants.MODULE);
    }
}

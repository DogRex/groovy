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


package org.codehaus.groovy.intellij.pallada;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;

import org.codehaus.groovy.intellij.BaseEditorAPI;

public class PalladaAPI extends BaseEditorAPI {

    public PalladaAPI(Project project) {
        super(project);
    }

    public void invokeLater(Runnable task) {
        ApplicationManager.getApplication().invokeLater(task);
    }
}

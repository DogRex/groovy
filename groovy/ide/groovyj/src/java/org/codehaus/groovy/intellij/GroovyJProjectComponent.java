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

import java.util.HashMap;
import java.util.Map;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;

public class GroovyJProjectComponent implements ProjectComponent {

    private static final Map INSTANCES = new HashMap();

    private Project project;
    private EditorAPIFactory editorAPIFactory;
    private EditorAPI editorAPI;
    private GroovyController groovyController;

    public GroovyJProjectComponent(Project project) {
        this(project, new EditorAPIFactory());
    }

    protected GroovyJProjectComponent(Project project, EditorAPIFactory editorAPIFactory) {
        this.project = project;
        this.editorAPIFactory = editorAPIFactory;

        GroovyJProjectComponent.setInstance(project, this);
    }

    public static void setInstance(Project project, GroovyJProjectComponent projectComponent) {
        INSTANCES.put(project, projectComponent);
    }

    public static GroovyJProjectComponent getInstance(Project project) {
        return (GroovyJProjectComponent) INSTANCES.get(project);
    }

    public void projectOpened() {
        editorAPI = editorAPIFactory.getEditorAPI(project);
        groovyController = new GroovyController(editorAPI);
    }

    public void projectClosed() {
        editorAPI = null;
        groovyController = null;
    }

    public void initComponent() {}

    public void disposeComponent() {}

    public String getComponentName() {
        return "groovyj.project.plugin";
    }

    public EditorAPI getEditorAPI() {
        return editorAPI;
    }

    public GroovyController getGroovyController() {
        return groovyController;
    }
}

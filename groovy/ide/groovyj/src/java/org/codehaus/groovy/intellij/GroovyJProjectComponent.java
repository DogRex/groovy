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

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;

import org.codehaus.groovy.intellij.compiler.GroovyCompiler;

public class GroovyJProjectComponent implements ProjectComponent {

    private static final Map<Project, GroovyJProjectComponent> INSTANCES = new HashMap<Project, GroovyJProjectComponent>();

    private Project project;
    private EditorAPIFactory editorApiFactory;
    private EditorAPI editorApi;
    private GroovyController groovyController;
    private GroovyCompiler groovyCompiler;
    GroovyLibraryModuleListener groovyLibraryModuleListener;

    protected GroovyJProjectComponent(Project project, EditorAPIFactory editorApiFactory) {
        this.project = project;
        this.editorApiFactory = editorApiFactory;

        GroovyJProjectComponent.setInstance(project, this);
    }

    public static void setInstance(Project project, GroovyJProjectComponent projectComponent) {
        INSTANCES.put(project, projectComponent);
    }

    public static GroovyJProjectComponent getInstance(Project project) {
        return INSTANCES.get(project);
    }

    public void projectOpened() {
        editorApi = editorApiFactory.createEditorAPI(project);
        groovyController = new GroovyController(editorApi);
        groovyCompiler = new GroovyCompiler(groovyController);
        CompilerManager.getInstance(project).addCompiler(groovyCompiler);

        groovyLibraryModuleListener = createGroovyLibraryModuleListener();
        ModuleManager.getInstance(project).addModuleListener(groovyLibraryModuleListener);
    }

    protected GroovyLibraryModuleListener createGroovyLibraryModuleListener() {
        return new GroovyLibraryModuleListener(PluginManager.getPlugin("GroovyJ").getVersion());
    }

    public void projectClosed() {
        CompilerManager.getInstance(project).removeCompiler(groovyCompiler);
        groovyCompiler = null;
        groovyController = null;
        editorApi = null;

        ModuleManager.getInstance(project).removeModuleListener(groovyLibraryModuleListener);
        groovyLibraryModuleListener = null;
    }

    public void initComponent() {}

    public void disposeComponent() {}

    public String getComponentName() {
        return "groovyj.project.plugin";
    }

    public GroovyController getGroovyController() {
        return groovyController;
    }

    EditorAPI getEditorApi() {
        return editorApi;
    }
}

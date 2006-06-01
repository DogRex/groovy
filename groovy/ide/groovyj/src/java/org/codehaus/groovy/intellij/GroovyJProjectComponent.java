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


package org.codehaus.groovy.intellij;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import org.codehaus.groovy.intellij.compiler.CompilationUnitsFactory;
import org.codehaus.groovy.intellij.compiler.GroovyCompiler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class GroovyJProjectComponent implements ProjectComponent {

    private static final Map<Project, GroovyJProjectComponent> INSTANCES = new HashMap<Project, GroovyJProjectComponent>();

    private Project project;
    private EditorAPIFactory editorApiFactory;
    private GroovyCompiler groovyCompiler;
    EditorAPI editorApi;
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
        groovyCompiler = new GroovyCompiler(editorApi, new CompilationUnitsFactory());
        addCompilationSupport();

        groovyLibraryModuleListener = new GroovyLibraryModuleListener(getPluginVersion());
        ModuleManager.getInstance(project).addModuleListener(groovyLibraryModuleListener);
    }

    protected String getPluginVersion() {
        IdeaPluginDescriptor pluginDescriptor = PluginManager.getPlugin(PluginId.getId("GroovyJ"));
        return pluginDescriptor != null ? pluginDescriptor.getVersion() : "";
    }

    public void projectClosed() {
        removeCompilationSupport();
        groovyCompiler = null;
        editorApi = null;

        ModuleManager.getInstance(project).removeModuleListener(groovyLibraryModuleListener);
        groovyLibraryModuleListener = null;
    }

    private void addCompilationSupport() {
        CompilerManager compilerManager = CompilerManager.getInstance(project);
        compilerManager.addCompiler(groovyCompiler);
        compilerManager.addCompilableFileType(GroovySupportLoader.GROOVY);
    }

    private void removeCompilationSupport() {
        CompilerManager compilerManager = CompilerManager.getInstance(project);
        compilerManager.removeCompiler(groovyCompiler);
        compilerManager.removeCompilableFileType(GroovySupportLoader.GROOVY);
    }

    public void initComponent() {}

    public void disposeComponent() {}

    @NotNull
    public String getComponentName() {
        return "groovyj.project.plugin";
    }
}

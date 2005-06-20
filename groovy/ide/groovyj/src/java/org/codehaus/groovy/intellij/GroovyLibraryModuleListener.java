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

import java.util.List;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.ModuleListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar;

public class GroovyLibraryModuleListener implements ModuleListener {

    private final Library groovyLibrary;

    public GroovyLibraryModuleListener() {
        String libraryName = "Groovy from GroovyJ " + PluginManager.getPlugin("GroovyJ").getVersion();
        groovyLibrary = LibraryTablesRegistrar.getInstance().getLibraryTable().getLibraryByName(libraryName);
    }

    public void moduleAdded(Project project, Module module) {
        ModifiableRootModel moduleRootModel = ModuleRootManager.getInstance(module).getModifiableModel();
        moduleRootModel.addLibraryEntry(groovyLibrary);
        moduleRootModel.commit();
    }

    public void beforeModuleRemoved(Project project, Module module) {}

    public void moduleRemoved(Project project, Module module) {}

    public void modulesRenamed(Project project, List<Module> modules) {}
}

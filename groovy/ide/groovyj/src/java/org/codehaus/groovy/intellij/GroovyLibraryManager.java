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

import java.io.File;
import java.io.FileFilter;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;

public class GroovyLibraryManager {

    public void installOrUpgradeGroovyRuntimeAsAGlobalLibraryIfNecessary() {
        IdeaPluginDescriptor pluginDescriptor = getPluginDescriptor();
        String libraryNamePrefix = "Groovy from GroovyJ ";
        String libraryName = libraryNamePrefix + pluginDescriptor.getVersion();

        LibraryTable libraryTable = LibraryTablesRegistrar.getInstance().getLibraryTable();
        if (matchingGroovyRuntimeNotFound(libraryTable, libraryName)) {
            Library.ModifiableModel libraryModel = findOrCreateEmptyLibrary(libraryTable.getModifiableModel(), libraryNamePrefix, libraryName);
            addJarFilesToLibrary(pluginDescriptor, libraryModel);
        }
    }

    protected IdeaPluginDescriptor getPluginDescriptor() {
        return PluginManager.getPlugin(PluginId.getId("GroovyJ"));
    }

    private boolean matchingGroovyRuntimeNotFound(LibraryTable libraryTable, String libraryName) {
        return libraryTable.getLibraryByName(libraryName) == null;
    }

    private Library.ModifiableModel findOrCreateEmptyLibrary(LibraryTable.ModifiableModel libraryTableModel,
                                                             String libraryNamePrefix, String libraryName) {
        for (Library library : libraryTableModel.getLibraries()) {
            if (currentLibraryNeedsReplacing(library, libraryNamePrefix, libraryName)) {
                return renameAndResetLibrary(library, libraryName);
            }
        }
        return createLibrary(libraryTableModel, libraryName);
    }

    private boolean currentLibraryNeedsReplacing(Library library, String libraryNamePrefix, String libraryName) {
        return library.getName().startsWith(libraryNamePrefix) && !library.getName().equals(libraryName);
    }

    private Library.ModifiableModel renameAndResetLibrary(Library library, String libraryName) {
        Library.ModifiableModel libraryModel = library.getModifiableModel();
        libraryModel.setName(libraryName);

        for (VirtualFile file : libraryModel.getFiles(OrderRootType.CLASSES)) {
            libraryModel.removeRoot(file.getUrl(), OrderRootType.CLASSES);
        }
        return libraryModel;
    }

    private Library.ModifiableModel createLibrary(LibraryTable.ModifiableModel libraryTableModel, String libraryName) {
        Library library = libraryTableModel.createLibrary(libraryName);
        libraryTableModel.commit();
        return library.getModifiableModel();
    }

    private void addJarFilesToLibrary(IdeaPluginDescriptor pluginDescriptor, Library.ModifiableModel libraryModel) {
        for (File jarFile : findJarFiles(pluginDescriptor)) {
            addJarFileToLibrary(jarFile, libraryModel);
        }
        libraryModel.commit();
    }

    private File[] findJarFiles(IdeaPluginDescriptor pluginDescriptor) {
        File pluginLibraryPath = new File(pluginDescriptor.getPath(), "lib");
        return pluginLibraryPath.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().endsWith(".jar") && !file.getName().startsWith("groovyj");
            }
        });
    }

    private void addJarFileToLibrary(File jarFile, Library.ModifiableModel libraryModel) {
        String url = VirtualFileManager.constructUrl(JarFileSystem.PROTOCOL, jarFile.getAbsolutePath() + JarFileSystem.JAR_SEPARATOR);
        libraryModel.addRoot(url, OrderRootType.CLASSES);
    }
}

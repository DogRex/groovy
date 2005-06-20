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

import org.intellij.openapi.testing.MockApplication;
import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.impl.libraries.LibraryTablesRegistrarImpl;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import org.jmock.Mock;

public class GroovyLibraryManagerTest extends GroovyjTestCase {

    private final GroovyLibraryManager groovyLibraryManager = new GroovyLibraryManager();

    private final Mock mockLibraryTable = mock(LibraryTable.class);
    private final Mock mockLibraryTableModel = mock(LibraryTable.ModifiableModel.class, "mockLibraryTableModel");

    protected void setUp() {
        MockApplication application = MockApplicationManager.getMockApplication();
        application.registerComponent(LibraryTablesRegistrar.class, new LibraryTablesRegistrarImpl());
        application.registerComponent(LibraryTable.class, mockLibraryTable.proxy());
    }

    public void testDoesNotModifyTheCurrentGlobalGroovyLibraryWhenItIsAlreadyUpToDate() {
        mockLibraryTable.expects(once()).method("getLibraryByName").with(startsWith("Groovy from GroovyJ "))
                .will(returnValue(mock(Library.class).proxy()));
        groovyLibraryManager.installOrUpgradeGroovyRuntimeAsAGlobalLibraryIfNecessary();
    }

    public void testInstallsAGroovyRuntimeAsAGlobalLibraryWhenNoPreviousLibraryCouldBeFound() {
        configureLibraryTableWithoutAnUpToDateGroovyLibrary();
        configureLibraryTableWithANonMatchingLibrary();

        Mock mockLibrary = mock(Library.class, "mockNewGroovyLibrary");
        createGroovyLibrary((Library) mockLibrary.proxy());

        Mock mockLibraryModel = mock(Library.ModifiableModel.class, "mockLibraryModel");
        mockLibrary.stubs().method("getModifiableModel").will(returnValue(mockLibraryModel.proxy()));

        configureGroovyLibraryWithPluginJarFiles(mockLibraryModel);
        groovyLibraryManager.installOrUpgradeGroovyRuntimeAsAGlobalLibraryIfNecessary();
    }

    public void testUpgradesTheCurrentGlobalGroovyLibraryWhenOneCouldBeFound() {
        configureLibraryTableWithoutAnUpToDateGroovyLibrary();

        Mock mockLibrary = mock(Library.class, "mockUpgradedGroovyLibrary");
        configureLibraryTableWithAGroovyLibraryRequiringAnUpgrade(mockLibrary);

        Mock mockLibraryModel = mock(Library.ModifiableModel.class, "mockLibraryModel");
        mockLibrary.stubs().method("getModifiableModel").will(returnValue(mockLibraryModel.proxy()));

        renameAndEmptyOldGroovyLibrary(mockLibraryModel);
        configureGroovyLibraryWithPluginJarFiles(mockLibraryModel);
        groovyLibraryManager.installOrUpgradeGroovyRuntimeAsAGlobalLibraryIfNecessary();
    }

    private void renameAndEmptyOldGroovyLibrary(Mock mockLibraryModel) {
        mockLibraryModel.expects(once()).method("setName").with(startsWith("Groovy from GroovyJ "));

        Mock stubVirtualJarFile = Mocks.createVirtualFileMock(this, "mockVirtualJarFile");
        stubVirtualJarFile.stubs().method("getUrl").will(returnValue("jar file url"));

        VirtualFile[] libraryJarFiles = new VirtualFile[]{(VirtualFile) stubVirtualJarFile.proxy()};
        mockLibraryModel.stubs().method("getFiles").with(same(OrderRootType.CLASSES)).will(returnValue(libraryJarFiles));
        mockLibraryModel.expects(exactly(libraryJarFiles.length)).method("removeRoot")
                .with(isA(String.class), same(OrderRootType.CLASSES)).will(returnValue(true));;
    }

    private void configureLibraryTableWithoutAnUpToDateGroovyLibrary() {
        mockLibraryTable.stubs().method("getLibraryByName").with(startsWith("Groovy from GroovyJ ")).will(returnValue(null));
        mockLibraryTable.stubs().method("getModifiableModel").will(returnValue(mockLibraryTableModel.proxy()));
    }

    private void configureLibraryTableWithANonMatchingLibrary() {
        Mock stubNonMatchingLibrary = mock(Library.class, "mockNonMatchingLibrary");
        mockLibraryTableModel.expects(once()).method("getLibraries")
                .will(returnValue(new Library[] { (Library) stubNonMatchingLibrary.proxy() }));
        stubNonMatchingLibrary.stubs().method("getName").will(returnValue("ACME Library"));
    }

    private void configureLibraryTableWithAGroovyLibraryRequiringAnUpgrade(Mock mockLibrary) {
        mockLibraryTableModel.expects(once()).method("getLibraries")
                .will(returnValue(new Library[] { (Library) mockLibrary.proxy() }));
        mockLibrary.stubs().method("getName").will(returnValue("Groovy from GroovyJ 0." + TestUtil.nextAbsRandomInt()));
    }

    private void createGroovyLibrary(Library library) {
        mockLibraryTableModel.expects(once()).method("createLibrary").with(startsWith("Groovy from GroovyJ ")).will(returnValue(library));
        mockLibraryTableModel.expects(once()).method("commit");
    }

    private void configureGroovyLibraryWithPluginJarFiles(Mock mockLibraryModel) {
        mockLibraryModel.expects(atLeastOnce()).method("addRoot").with(startsWith(JarFileSystem.PROTOCOL), same(OrderRootType.CLASSES));
        mockLibraryModel.expects(once()).method("commit");
    }
}

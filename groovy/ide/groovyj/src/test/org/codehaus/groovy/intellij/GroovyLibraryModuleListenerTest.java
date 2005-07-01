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

import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.impl.libraries.LibraryTablesRegistrarImpl;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar;

import org.jmock.Mock;

public class GroovyLibraryModuleListenerTest extends GroovyjTestCase {

    private GroovyLibraryModuleListener groovyLibraryModuleListener;

    protected void setUp() throws Exception {
        Mock mockLibraryTable = mock(LibraryTable.class);

        MockApplication application = MockApplicationManager.getMockApplication();
        application.registerComponent(LibraryTablesRegistrar.class, new LibraryTablesRegistrarImpl());
        application.registerComponent(LibraryTable.class, mockLibraryTable.proxy());

        mockLibraryTable.stubs().method("getLibraryByName").with(startsWith("Groovy from GroovyJ "));
        groovyLibraryModuleListener = new GroovyLibraryModuleListener("1.0." + nextPositiveRandomInt());
    }

    public void testAddsTheGroovyLibraryToANewlyAddedModule() {
        Mock mockModifiableRootModel = mock(ModifiableRootModel.class);
        mockModifiableRootModel.expects(once()).method("addLibraryEntry");
        mockModifiableRootModel.expects(once()).method("commit");

        ModifiableRootModel moduleRootModel = (ModifiableRootModel) mockModifiableRootModel.proxy();
        groovyLibraryModuleListener.moduleAdded(project().build(), module().withModuleRootModel(moduleRootModel).build());
    }

    public void testDoesNothingBeforeAModuleIsRemoved() {
        groovyLibraryModuleListener.beforeModuleRemoved(null, null);
    }

    public void testDoesNothingWhenAModuleIsRemoved() {
        groovyLibraryModuleListener.moduleRemoved(null, null);
    }

    public void testDoesNothingWhenSeveralModulesAreRenamed() {
        groovyLibraryModuleListener.modulesRenamed(null, null);
    }
}

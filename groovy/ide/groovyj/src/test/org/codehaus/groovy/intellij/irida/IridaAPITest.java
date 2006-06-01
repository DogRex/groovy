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


package org.codehaus.groovy.intellij.irida;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import org.codehaus.groovy.intellij.EditorApiTestCase;
import org.intellij.openapi.testing.MockApplicationManager;
import org.jmock.Mock;

import java.io.File;

public class IridaAPITest extends EditorApiTestCase {

    protected void setUp() {
        super.setUp();
        editorApi = new IridaAPI((Project) mockProject.proxy());
    }

    protected void tearDown() {
        super.tearDown();
        MockApplicationManager.reset();
    }

    public void testReturnsAnEmptyStringAsTheCompilationClasspathWhenTheGivenModuleHasNoLibraryDependencies() {
        String expectedCompilationClasspath = "";

        mockModuleRootManager.expects(once()).method("getOrderEntries").will(returnValue(OrderEntry.EMPTY_ARRAY));

        String compilationClasspath = editorApi.getCompilationClasspath(stubbedModule);
        assertEquals("compilation classpath", expectedCompilationClasspath, compilationClasspath);
    }

    public void testReturnsTheCompilationClasspathAsAStringContainingTheLibraryDependenciesForAGivenModule() {
        String expectedFileName = "stuff";

        Mock mockOrderEntry = mock(OrderEntry.class);
        mockOrderEntry.expects(once()).method("getFiles").with(same(OrderRootType.COMPILATION_CLASSES))
                .will(returnValue(new VirtualFile[] { new LightVirtualFile(expectedFileName) }));

        OrderEntry[] orderEntries = new OrderEntry[] { (OrderEntry) mockOrderEntry.proxy() };
        mockModuleRootManager.expects(once()).method("getOrderEntries").will(returnValue(orderEntries));

        String compilationClasspath = editorApi.getCompilationClasspath(stubbedModule);
        assertEquals("compilation classpath", File.separatorChar + expectedFileName, compilationClasspath);
    }

    public void testRunsATaskAsynchronouslyUsingAnApplicationInstance() {
        Mock mockApplication = mock(Application.class);
        MockApplicationManager.setApplication((Application) mockApplication.proxy());
        mockApplication.expects(once()).method("invokeLater").with(isA(Runnable.class));

        editorApi.invokeLater(new Runnable() {
            public void run() {}
        });
    }
}

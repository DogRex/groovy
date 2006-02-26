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

import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public final class Mocks {

    private Mocks() {}

    public static Mock createVirtualFileMock(MockObjectTestCase testCase, String roleName) {
        return testCase.mock(VirtualFile.class, roleName);
    }

    public static Mock createModuleTypeMock(MockObjectTestCase testCase) {
        return testCase.mock(ModuleType.class, new Class[] { String.class }, new Object[] { null });
    }

    public static Mock createGroovyJProjectComponentMock(MockObjectTestCase testCase) {
        return testCase.mock(GroovyJProjectComponent.class, new Class[] { Project.class, EditorAPIFactory.class }, new Object[] { null, null });
    }
}

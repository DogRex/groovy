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

import com.intellij.openapi.vfs.VirtualFile;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public final class Mocks {

    /** Sole constructor hidden to enfore non-instantiability. */
    private Mocks() { }

    // Factory methods -------------------------------------------------------------------------------------------------

    public static Mock createVirtualFileMock(MockObjectTestCase testCase) {
        return createMock(testCase, MockableVirtualFile.class, "mockVirtualFile");
    }

    public static Mock createVirtualFileMock(MockObjectTestCase testCase, String roleName) {
        return createMock(testCase, MockableVirtualFile.class, roleName);
    }

    public static Mock createGroovyJProjectComponentMock(MockObjectTestCase testCase) {
        return createMock(testCase, MockableGroovyJProjectComponent.class, "mockGroovyJProjectComponent");
    }

    public static Mock createGroovyControllerMock(MockObjectTestCase testCase) {
        return createMock(testCase, MockableGroovyController.class, "mockGroovyController");
    }

    private static Mock createMock(MockObjectTestCase testCase, Class mockedType, String roleName) {
        return testCase.mock(mockedType, roleName);
    }

    // Mockable classes ------------------------------------------------------------------------------------------------

    private static abstract class MockableVirtualFile extends VirtualFile {

        public MockableVirtualFile() {
            super();
        }
    }

    private static class MockableGroovyJProjectComponent extends GroovyJProjectComponent {

        public MockableGroovyJProjectComponent() {
            super(null, null);
        }
    }

    private static class MockableGroovyController extends GroovyController {

        public MockableGroovyController() {
            super(null);
        }
    }
}

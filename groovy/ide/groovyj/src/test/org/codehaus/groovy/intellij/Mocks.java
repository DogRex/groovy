/*
 * $Id$
 *
 * Copyright (c) 2004 The Codehaus - http://groovy.codehaus.org
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

import org.jmock.cglib.Mock;

public final class Mocks {

    /**
     * Sole constructor hidden to enfore non-instantiability.
     */
    private Mocks() {}

    // Factory methods -------------------------------------------------------------------------------------------------

    public static Mock createVirtualFileMock() {
        return new Mock(MockableVirtualFile.class);
    }

    public static Mock createGroovyJProjectComponentMock() {
        return new Mock(MockableGroovyJProjectComponent.class);
    }

    public static Mock createGroovyControllerMock() {
        return new Mock(MockableGroovyController.class);
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

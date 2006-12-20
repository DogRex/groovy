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


package org.codehaus.groovy.intellij.psi;

import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.SingleRootFileViewProvider;
import com.intellij.testFramework.LightVirtualFile;
import org.codehaus.groovy.intellij.GroovySupportLoader;
import org.codehaus.groovy.intellij.GroovyjTestCase;
import org.codehaus.groovy.intellij.language.GroovyLanguage;
import org.intellij.openapi.testing.MockApplicationManager;
import org.jmock.Mock;

public class GroovyFileTest extends GroovyjTestCase {

    private GroovyFile groovyFile;

    protected void setUp() throws Exception {
        Mock mockFileTypeManager = mock(FileTypeManager.class);
        mockFileTypeManager.expects(once()).method("getFileTypeByFile").with(isA(VirtualFile.class)).will(returnValue(GroovySupportLoader.GROOVY));

        MockApplicationManager.getMockApplication().registerComponent(FileTypeManager.class, mockFileTypeManager.proxy());
        groovyFile = new GroovyFile(new SingleRootFileViewProvider(null, new LightVirtualFile("path", "contents")), GroovyLanguage.findOrCreate());
    }

    public void testHasTheGroovyFileType() {
        assertSame("file type", GroovySupportLoader.GROOVY, groovyFile.getFileType());
    }

    public void testHasATextualRepresentationContainingThePathToTheUnderlyingFile() {
        assertEquals("textual representation", "[GroovyFile '/path']", groovyFile.toString());
    }
}

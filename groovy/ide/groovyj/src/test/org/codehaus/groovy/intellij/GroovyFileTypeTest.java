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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.LightVirtualFile;
import org.codehaus.groovy.intellij.language.GroovyLanguage;
import org.jmock.Mock;

public class GroovyFileTypeTest extends GroovyjTestCase {

    private final GroovyFileType fileType = new GroovyFileType(GroovyLanguage.findOrCreate());

    public void testDefinesAName() {
        assertEquals("name", "Groovy", fileType.getName());
    }

    public void testHasADescription() {
        assertEquals("description", "Groovy Scripts and Classes", fileType.getDescription());
    }

    public void testDefinesADefaultExtension() {
        assertEquals("default extension", "groovy", fileType.getDefaultExtension());
    }

    public void testHasTheGroovyFileTypeIcon() {
        assertSame("icon", Icons.SMALLEST, fileType.getIcon());
    }

    public void testDoesNotRepresentABinaryFile() {
        assertEquals("is binary file", false, fileType.isBinary());
    }

    public void testIsNotAReadOnlyFileType() {
        assertEquals("is binary file", false, fileType.isReadOnly());
    }

    public void testSupportsJvmDebugging() {
        assertEquals("is JVM debugging supported", true, fileType.isJVMDebuggingSupported());
    }

    public void testReturnsTheCharacterSetOfAGivenFileAsItsCharacterSet() {
        String expectedCharacterSetName = "UTF-8";
        VirtualFile file = virtualFile().withCharset(expectedCharacterSetName).build();
        assertSame("character set", expectedCharacterSetName, fileType.getCharset(file));
    }

    public void testDoesNotCreateABuilderForTheStructuralTreeViewForAFileOutsideOfTheCurrentProject() {
        Mock mockPsiManager = mock(PsiManager.class);

        Project exectedProject = createStubbedProject((PsiManager) mockPsiManager.proxy());
        VirtualFile expectedVirtualFile = new LightVirtualFile();

        mockPsiManager.expects(once()).method("findFile").with(same(expectedVirtualFile)).will(returnValue(null));

        assertSame("structural view builder", null, fileType.getStructureViewBuilder(expectedVirtualFile, exectedProject));
    }

    public void testDoesNotCreateABuilderForTheStructuralTreeViewForAFileInsideTheCurrentProject() {
        Mock mockPsiManager = mock(PsiManager.class);
        Project exectedProject = createStubbedProject((PsiManager) mockPsiManager.proxy());
        VirtualFile expectedVirtualFile = new LightVirtualFile();

        mockPsiManager.expects(once()).method("findFile").with(same(expectedVirtualFile)).will(returnValue(mock(PsiFile.class).proxy()));

        assertSame("structural view builder", null, fileType.getStructureViewBuilder(expectedVirtualFile, exectedProject));
    }

    private Project createStubbedProject(PsiManager psiManager) {
        Mock stubProject = mock(Project.class, "stubProject");
        stubProject.stubs().method("getComponent").with(same(PsiManager.class)).will(returnValue(psiManager));
        return (Project) stubProject.proxy();
    }
}

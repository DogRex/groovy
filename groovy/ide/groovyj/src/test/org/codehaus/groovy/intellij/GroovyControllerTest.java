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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.intellij.openapi.vfs.VirtualFile;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.Phases;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

import groovy.lang.GroovyShell;

public class GroovyControllerTest extends MockObjectTestCase {

    private static final String FILE_NAME = "foo.groovy";

    private final Mock mockEditorAPI = mock(EditorAPI.class);
    private final Mock mockVirtualFile = Mocks.createVirtualFileMock(this);
    private final Mock mockGroovyShell = mock(GroovyShell.class);

    private GroovyController groovyController;

    protected void setUp() {
        groovyController = new GroovyController((EditorAPI) mockEditorAPI.proxy()) {
            protected GroovyShell createGroovyShellForScript(VirtualFile selectedFile) {
                return (GroovyShell) mockGroovyShell.proxy();
            }
        };
    }

    public void testCreatesAGroovyShellWithAnEmptyContext() {
        groovyController = new GroovyController((EditorAPI) mockEditorAPI.proxy());

        mockEditorAPI.expects(once()).method("getCurrentModuleClasspathAsString").will(returnValue("some_lib.jar"));
        mockVirtualFile.expects(once()).method("getCharset").will(returnValue(Charset.forName("UTF-8")));
        mockVirtualFile.expects(once()).method("getPath").will(returnValue("/dev/null/foo.groovy"));

        GroovyShell shell = groovyController.createGroovyShellForScript((VirtualFile) mockVirtualFile.proxy());
        assertEquals(0, shell.getContext().getVariables().size());
    }

    public void testDoesNotAttemptToRunAGroovyScriptIfTheGivenFileReferenceIsNull() {
        groovyController.runAsGroovyScript(null);
    }

    public void testDoesNotAttemptToRunAGivenFileAsAGroovyScriptIfTheFileIsNotValid() {
        mockVirtualFile.expects(once()).method("isValid").will(returnValue(false));
        groovyController.runAsGroovyScript((VirtualFile) mockVirtualFile.proxy());
    }

    public void testDoesNotAttemptToRunAGivenFileAsAGroovyScriptIfTheFileIsActuallyADirectory() {
        mockVirtualFile.expects(once()).method("isValid").will(returnValue(true));
        mockVirtualFile.expects(once()).method("isDirectory").will(returnValue(true));

        groovyController.runAsGroovyScript((VirtualFile) mockVirtualFile.proxy());
    }

    public void testRunsTheCurrentlySelectedFileAsAGroovyScriptUsingTheEditorApi() {
        setExpectationsForRunningAFileAsAGroovyScript(FILE_NAME);
        mockGroovyShell.expects(once()).method("run").with(isA(InputStream.class), eq(FILE_NAME), isA(String[].class));

        groovyController.runAsGroovyScript((VirtualFile) mockVirtualFile.proxy());
    }

    public void testDoesNothingWhenRunningAGroovyScriptFailsDueToACompilationError() {
        setExpectationsForRunningAFileAsAGroovyScript(FILE_NAME);
        mockGroovyShell.expects(once()).method("run").with(isA(InputStream.class), eq(FILE_NAME), isA(String[].class))
                .will(throwException(new CompilationFailedException(Phases.SEMANTIC_ANALYSIS, new CompilationUnit())));

        groovyController.runAsGroovyScript((VirtualFile) mockVirtualFile.proxy());
    }

    public void testDoesNothingWhenRunningAGroovyScriptFailsBecauseTheScriptCouldNotBeFound() {
        setExpectationsForRunningAFileAsAGroovyScript(FILE_NAME);
        mockGroovyShell.expects(once()).method("run").with(isA(InputStream.class), eq(FILE_NAME), isA(String[].class))
                .will(throwException(new IOException("script not found")));

        groovyController.runAsGroovyScript((VirtualFile) mockVirtualFile.proxy());
    }

    private String setExpectationsForRunningAFileAsAGroovyScript(String fileName) {
        mockVirtualFile.expects(once()).method("isValid").will(returnValue(true));
        mockVirtualFile.expects(once()).method("isDirectory").will(returnValue(false));
        mockVirtualFile.expects(atLeastOnce()).method("getName").will(returnValue(fileName));
        mockVirtualFile.expects(once()).method("getInputStream").will(returnValue(System.in));

        mockEditorAPI.expects(once()).method("writeMessageToStatusBar").with(isA(String.class));
        return fileName;
    }
}

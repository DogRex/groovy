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


package org.codehaus.groovy.intellij.compiler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.ErrorCollector;
import org.codehaus.groovy.control.Janitor;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.ExceptionMessage;
import org.codehaus.groovy.control.messages.Message;
import org.codehaus.groovy.control.messages.SimpleMessage;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.control.messages.WarningMessage;
import org.codehaus.groovy.syntax.SyntaxException;

import org.jmock.Mock;

import org.codehaus.groovy.intellij.GroovyjTestCase;

import groovy.lang.MissingClassException;

public class CompilationUnitsTest extends GroovyjTestCase {

    private final DummyCompilationUnit sourceCompilationUnit = new DummyCompilationUnit();
    private final DummyCompilationUnit testCompilationUnit = new DummyCompilationUnit();
    private final CompilationUnits compilationUnits = new CompilationUnits(sourceCompilationUnit, testCompilationUnit);

    private final CompileContextBuilder compileContextBuilder = new CompileContextBuilder(this);

    protected void setUp() {
        sourceCompilationUnit.getConfiguration().setTargetDirectory("/home/foo/acme/classes");
        testCompilationUnit.getConfiguration().setTargetDirectory("/home/foo/acme/test-classes");
        MockApplicationManager.getMockApplication().registerComponent(LocalFileSystem.class, localFileSystem());
    }

    protected void tearDown() {
        MockApplicationManager.getMockApplication().removeComponent(LocalFileSystem.class);
    }

    public void testAddsSyntaxExceptionsWithErrorLocationToTheCompileContext() {
        String filePath = "/home/foo/acme/src/bar.groovy";
        SyntaxException syntaxException = syntaxException("occurred because of blah...");
        SourceUnit sourceUnit = sourceUnit(filePath, testCompilationUnit);
        ErrorCollector errorCollector = errorCollectorWith(new SyntaxErrorMessage(syntaxException, sourceUnit));

        VirtualFile fileToCompile = virtualFile().withPath(filePath).build();
        compilationUnits.add(fileToCompile, true);
        compileContextBuilder.expectsAddMessage(CompilerMessageCategory.ERROR, fileToCompile, syntaxException);

        compileSourceAndTestUnitsWithContext();
        assertEquals("number of errors", 1, errorCollector.getErrorCount());
        assertSame("exception", syntaxException, errorCollector.getException(0));
        assertEquals("number of warnings", 0, errorCollector.getWarningCount());
    }

    public void testAddsGroovyRuntimeExceptionsWithErrorLocationToTheCompileContext() {
        ASTNode astNode = astNodeWithRandomLineAndColumnNumbers();
        MissingClassException missingClassException = new MissingClassException("MadeUpType", astNode, "in blah blah");
        missingClassException.setModule(moduleNodeBoundTo(sourceCompilationUnit));
        ErrorCollector errorCollector = errorCollectorWith(new ExceptionMessage(missingClassException, false, sourceCompilationUnit));

        VirtualFile fileToCompile = virtualFile().withPath("/home/foo/acme/src/bar.groovy").build();
        compilationUnits.add(fileToCompile, false);
        compileContextBuilder.expectsAddMessage(CompilerMessageCategory.ERROR, missingClassException, astNode);

        compileSourceAndTestUnitsWithContext();
        assertEquals("number of errors", 1, errorCollector.getErrorCount());
        assertSame("exception", missingClassException, errorCollector.getException(0));
        assertEquals("number of warnings", 0, errorCollector.getWarningCount());
    }

    public void testAddsOtherExceptionsAsErrorsToTheCompileContext() {
        Exception anotherException = new IOException("exceptional I/O stuff");
        ErrorCollector errorCollector = errorCollectorWith(new ExceptionMessage(anotherException, false, testCompilationUnit));

        VirtualFile fileToCompile = virtualFile("/home/foo/acme/src/bar.groovy").build();
        compilationUnits.add(fileToCompile, true);
        compileContextBuilder.expectsAddMessage(CompilerMessageCategory.ERROR, anotherException);

        compileSourceAndTestUnitsWithContext();
        assertEquals("number of errors", 1, errorCollector.getErrorCount());
        assertSame("exception", anotherException, errorCollector.getException(0));
        assertEquals("number of warnings", 0, errorCollector.getWarningCount());
    }

    public void testAddsSimpleErrorMessagesAsErrorsToTheCompileContext() {
        SimpleMessage message = new SimpleMessage(someString(), testCompilationUnit);
        ErrorCollector errorCollector = errorCollectorWith(message);

        VirtualFile fileToCompile = virtualFile("/home/foo/acme/src/bar.groovy").build();
        compilationUnits.add(fileToCompile, true);
        compileContextBuilder.expectsAddMessage(CompilerMessageCategory.ERROR, message);

        compileSourceAndTestUnitsWithContext();
        assertEquals("number of errors", 1, errorCollector.getErrorCount());
        assertSame("error", message, errorCollector.getError(0));
        assertEquals("number of warnings", 0, errorCollector.getWarningCount());
    }

    public void testAddsOtherMessagesAsEmptyErrorMessagesToTheCompileContext() {
        Message message = new Message() {
            public void write(PrintWriter writer, Janitor janitor) {}
        };
        ErrorCollector errorCollector = errorCollectorWith(message);

        VirtualFile fileToCompile = virtualFile("/home/foo/acme/src/bar.groovy").build();
        compilationUnits.add(fileToCompile, true);
        compileContextBuilder.expectsAddDefaultErrorMessage();

        compileSourceAndTestUnitsWithContext();
        assertEquals("number of errors", 1, errorCollector.getErrorCount());
        assertSame("error", message, errorCollector.getError(0));
        assertEquals("number of warnings", 0, errorCollector.getWarningCount());
    }

    public void testCompilesASingleErrorFreeGroovyScriptAndAddsWarningsToTheCompileContext() {
        WarningMessage expectedWarningMessage = new WarningMessage(WarningMessage.LIKELY_ERRORS, "a warning", null, null);
        ErrorCollector errorCollector = sourceCompilationUnit.getErrorCollector();
        errorCollector.addWarning(expectedWarningMessage);

        VirtualFile fileToCompile = virtualFile("/home/foo/acme/src/bar.groovy").build();
        compilationUnits.add(fileToCompile, false);
        compileContextBuilder.expectsAddMessage(CompilerMessageCategory.WARNING, expectedWarningMessage);

        compileSourceAndTestUnitsWithContext();
        assertEquals("number of errors", 0, errorCollector.getErrorCount());
        assertEquals("number of warnings", 1, errorCollector.getWarningCount());
        assertSame("warnings", expectedWarningMessage, errorCollector.getWarning(0));
    }

    private void compileSourceAndTestUnitsWithContext() {
        compilationUnits.compile(compileContextBuilder.build(), new ArrayList<TranslatingCompiler.OutputItem>(), new ArrayList<VirtualFile>());
    }

    private SyntaxException syntaxException(String message) {
        return new SyntaxException(message, nextRandomInt(), nextRandomInt());
    }

    private ErrorCollector errorCollectorWith(Message message) {
        ErrorCollector errorCollector = sourceCompilationUnit.getErrorCollector();
        errorCollector.addErrorAndContinue(message);
        sourceCompilationUnit.exception = new MultipleCompilationErrorsException(errorCollector);
        return errorCollector;
    }

    private ASTNode astNodeWithRandomLineAndColumnNumbers() {
        ASTNode astNode = new ASTNode();
        astNode.setLineNumber(nextPositiveRandomInt());
        astNode.setColumnNumber(nextPositiveRandomInt());
        return astNode;
    }

    private ModuleNode moduleNodeBoundTo(CompilationUnit compilationUnit) {
        SourceUnit context = new SourceUnit(someString(), (String) null, compilationUnit.getConfiguration(), null, null);
        return new ModuleNode(context);
    }

    private static class DummyCompilationUnit extends CompilationUnit {

        private CompilationFailedException exception;

        public DummyCompilationUnit() {
            super(new CompilerConfiguration());
        }

        public void compile() throws CompilationFailedException {
            if (exception != null) {
                throw exception;
            }
            dequeued();
        }
    }

    private LocalFileSystem localFileSystem() {
        Mock stubLocalFileSystem = mock(LocalFileSystem.class);
        stubLocalFileSystem.stubs().method("findFileByPath").with(isA(String.class)).will(returnValue(virtualFile().build()));
        return (LocalFileSystem) stubLocalFileSystem.proxy();
    }

    private SourceUnit sourceUnit(String name, CompilationUnit compilationUnit) {
        return new SourceUnit(name, (String) null, compilationUnit.getConfiguration(), null, compilationUnit.getErrorCollector());
    }
}

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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.intellij.compiler.impl.javaCompiler.OutputItemImpl;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PathUtil;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.ErrorCollector;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.ExceptionMessage;
import org.codehaus.groovy.control.messages.Message;
import org.codehaus.groovy.control.messages.SimpleMessage;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.control.messages.WarningMessage;
import org.codehaus.groovy.syntax.SyntaxException;

import groovy.lang.GroovyRuntimeException;

class CompilationUnits {

    final CompilationUnit sourceCompilationUnit;
    final CompilationUnit testCompilationUnit;

    private final List<SourceUnit> sourceFilesToCompile = new ArrayList<SourceUnit>();
    private final List<SourceUnit> testFilesToCompile = new ArrayList<SourceUnit>();

    CompilationUnits(CompilationUnit sourceCompilationUnit, CompilationUnit testCompilationUnit) {
        this.sourceCompilationUnit = sourceCompilationUnit;
        this.testCompilationUnit = testCompilationUnit;
    }

    public void add(VirtualFile fileToCompile, boolean inTestSourceFolder) {
        if (inTestSourceFolder) {
            addTest(fileToCompile);
        } else {
            addSource(fileToCompile);
        }
    }

    private void addSource(VirtualFile file) {
        sourceFilesToCompile.add(sourceCompilationUnit.addSource(new File(file.getPath())));
    }

    private void addTest(VirtualFile file) {
        testFilesToCompile.add(testCompilationUnit.addSource(new File(file.getPath())));
    }

    public void compile(CompileContext context, List<TranslatingCompiler.OutputItem> compiledFiles, List<VirtualFile> filesToRecompile) {
        compile(context, sourceCompilationUnit, compiledFiles, filesToRecompile);
        compile(context, testCompilationUnit, compiledFiles, filesToRecompile);
    }

    void compile(CompileContext context, CompilationUnit compilationUnit, List<TranslatingCompiler.OutputItem> compiledFiles, List<VirtualFile> filesToRecompile) {
        try {
            compilationUnit.compile();
            addCompiledFiles(compilationUnit, compiledFiles);
        } catch (CompilationFailedException e) {
            processCompilationException(e, context);
        } catch (IOException e) {
            processException(e, context);
        } finally {
            addWarnings(compilationUnit.getErrorCollector(), context);
        }
    }

    private void addCompiledFiles(CompilationUnit compilationUnit, List<TranslatingCompiler.OutputItem> compiledFiles) throws IOException {
        File targetDirectory = compilationUnit.getConfiguration().getTargetDirectory();
        String outputRootDirectory = targetDirectory.getParentFile().getCanonicalPath();
        String outputPath = targetDirectory.getCanonicalPath();

        LocalFileSystem fileSystem = LocalFileSystem.getInstance();
        for (Iterator iterator = compilationUnit.iterator(); iterator.hasNext(); ) {
            SourceUnit sourceUnit = (SourceUnit) iterator.next();
            VirtualFile file = fileSystem.findFileByPath(canonicalPath(sourceUnit));
            compiledFiles.add(new OutputItemImpl(outputRootDirectory, outputPath, file));
        }
    }

    private String canonicalPath(SourceUnit sourceUnit) {
        return PathUtil.getCanonicalPath(sourceUnit.getName());
    }

    private void addWarnings(ErrorCollector errorCollector, CompileContext context) {
        for (int i = 0; i < errorCollector.getWarningCount(); i++) {
            WarningMessage warning = errorCollector.getWarning(i);
            context.addMessage(CompilerMessageCategory.WARNING, warning.getMessage(), null, -1, -1);
        }
    }

    private void processCompilationException(Exception exception, CompileContext context) {
        if (exception instanceof MultipleCompilationErrorsException) {
            MultipleCompilationErrorsException multipleCompilationErrorsException = (MultipleCompilationErrorsException) exception;
            ErrorCollector errorCollector = multipleCompilationErrorsException.getErrorCollector();
            for (int i = 0; i < errorCollector.getErrorCount(); i++) {
                processException(errorCollector.getError(i), context);
            }
        } else {
            processException(exception, context);
        }
    }

    private void processException(Message message, CompileContext context) {
        if (message instanceof SyntaxErrorMessage) {
            SyntaxErrorMessage syntaxErrorMessage = (SyntaxErrorMessage) message;
            addErrorMessage(syntaxErrorMessage.getCause(), context);
        } else if (message instanceof ExceptionMessage) {
            ExceptionMessage exceptionMessage = (ExceptionMessage) message;
            processException(exceptionMessage.getCause(), context);
        } else if (message instanceof SimpleMessage) {
            addErrorMessage((SimpleMessage) message, context);
        } else {
            context.addMessage(CompilerMessageCategory.ERROR, "An unknown error occurred.", null, -1, -1);
        }
    }

    private void processException(Exception exception, CompileContext context) {
        if (exception instanceof GroovyRuntimeException) {
            addErrorMessage((GroovyRuntimeException) exception, context);
        } else {
            context.addMessage(CompilerMessageCategory.ERROR, exception.getMessage(), null, -1, -1);
        }
    }

    private void addErrorMessage(SyntaxException exception, CompileContext context) {
        context.addMessage(CompilerMessageCategory.ERROR, exception.getMessage(), pathToUrl(exception.getSourceLocator()),
                           exception.getLine(), exception.getStartColumn());
    }

    private void addErrorMessage(GroovyRuntimeException exception, CompileContext context) {
        ASTNode astNode = exception.getNode();
        context.addMessage(CompilerMessageCategory.ERROR, exception.getMessageWithoutLocationText(),
                           exception.getModule().getDescription(),
                           astNode.getLineNumber(), astNode.getColumnNumber());
    }

    private void addErrorMessage(SimpleMessage message, CompileContext context) {
        context.addMessage(CompilerMessageCategory.ERROR, message.getMessage(), null, -1, -1);
    }

    private String pathToUrl(String filePath) {
        try {
            return new File(filePath).toURL().toExternalForm();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}

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
import java.util.ArrayList;
import java.util.List;

import com.intellij.compiler.impl.javaCompiler.OutputItemImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;

import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.ErrorCollector;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.messages.WarningMessage;
import org.codehaus.groovy.syntax.SyntaxException;

import org.codehaus.groovy.intellij.GroovyController;
import org.codehaus.groovy.intellij.GroovySupportLoader;

public class GroovyCompiler implements TranslatingCompiler {

    private final GroovyController controller;

    public GroovyCompiler(GroovyController controller) {
        this.controller = controller;
    }

    public String getDescription() {
        return "Groovy Compiler";
    }

    public boolean isCompilableFile(VirtualFile file, CompileContext context) {
        return GroovySupportLoader.GROOVY == file.getFileType();
    }

    public ExitStatus compile(final CompileContext context, final VirtualFile[] filesToCompile) {
        final List<OutputItem> compiledFiles = new ArrayList<OutputItem>();
        final List<VirtualFile> filesToRecompile = new ArrayList<VirtualFile>();

        ApplicationManager.getApplication().runReadAction(new Runnable() {
            public void run() {
                compile(context, filesToCompile, compiledFiles, filesToRecompile);
            }
        });

        return new ExitStatusImpl(compiledFiles.toArray(TranslatingCompiler.EMPTY_OUTPUT_ITEM_ARRAY),
                                  filesToRecompile.toArray(VirtualFile.EMPTY_ARRAY));
    }

    private void compile(CompileContext context, VirtualFile[] filesToCompile, List<OutputItem> compiledFiles, List<VirtualFile> filesToRecompile) {
        for (int i = 0; i < filesToCompile.length; i++) {
            if (context.getProgressIndicator().isCanceled()) {
                break;
            }

            context.getProgressIndicator().setFraction((double) i / (double) filesToCompile.length);
            compile(context, filesToCompile[i], compiledFiles, filesToRecompile);
        }
    }

    private void compile(CompileContext context, VirtualFile fileToCompile, List<OutputItem> compiledFiles, List<VirtualFile> filesToRecompile) {
        Module module = context.getModuleByFile(fileToCompile);
        CompilationUnit compilationUnit = controller.createCompilationUnit(fileToCompile, module);
        compilationUnit.addSource(new File(fileToCompile.getPath()));

        try {
            compilationUnit.compile();
            addCompiledFile(fileToCompile, compiledFiles, compilationUnit.getConfiguration().getTargetDirectory());
        } catch (Exception e) {
            processCompilationException(e, fileToCompile, filesToRecompile, context);
        } finally {
            addWarnings(compilationUnit.getErrorCollector(), context);
        }
    }

    private void addCompiledFile(VirtualFile compiledFile, List<OutputItem> compiledFiles, File targetDirectory) throws IOException {
        String outputRootDirectory = targetDirectory.getParentFile().getCanonicalPath();
        String outputPath = targetDirectory.getCanonicalPath();
        compiledFiles.add(new OutputItemImpl(outputRootDirectory, outputPath, compiledFile));
    }

    private void addWarnings(ErrorCollector errorCollector, CompileContext context) {
        for (int i = 0; i < errorCollector.getWarningCount(); i++) {
            WarningMessage warning = errorCollector.getWarning(i);
            context.addMessage(CompilerMessageCategory.WARNING, warning.getMessage(), null, -1, -1);
        }
    }

    private void processCompilationException(Exception exception, VirtualFile fileToCompile, List<VirtualFile> filesToRecompile, CompileContext context) {
        if (exception instanceof MultipleCompilationErrorsException) {
            MultipleCompilationErrorsException compilationFailureException = (MultipleCompilationErrorsException) exception;
            ErrorCollector errorCollector = compilationFailureException.getErrorCollector();
            for (int i = 0; i < errorCollector.getErrorCount(); i++) {
                processException(errorCollector.getException(i), context, filesToRecompile, fileToCompile);
            }
        } else {
            processException(exception, context, filesToRecompile, fileToCompile);
        }
    }

    private void processException(Exception exception, CompileContext context, List<VirtualFile> filesToRecompile, VirtualFile fileToCompile) {
        if (exception instanceof SyntaxException) {
            SyntaxException syntaxException = (SyntaxException) exception;
            context.addMessage(CompilerMessageCategory.ERROR, syntaxException.getMessage(), fileToCompile.getUrl(),
                               syntaxException.getLine(), syntaxException.getStartColumn());
        } else {
            context.addMessage(CompilerMessageCategory.ERROR, exception.getMessage(), fileToCompile.getUrl(), -1, -1);
        }

        filesToRecompile.add(fileToCompile);
    }

    public boolean validateConfiguration(CompileScope scope) {
        return true;
    }

    private static class ExitStatusImpl implements TranslatingCompiler.ExitStatus {

        private final OutputItem[] compiledFiles;
        private final VirtualFile[] filesToRecompile;

        private ExitStatusImpl(OutputItem[] compiledFiles, VirtualFile[] filesToRecompile) {
            this.compiledFiles = compiledFiles;
            this.filesToRecompile = filesToRecompile;
        }

        public TranslatingCompiler.OutputItem[] getSuccessfullyCompiled() {
            return compiledFiles;
        }

        public VirtualFile[] getFilesToRecompile() {
            return filesToRecompile;
        }
    }
}

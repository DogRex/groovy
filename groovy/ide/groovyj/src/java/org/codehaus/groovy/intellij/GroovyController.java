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

import java.io.IOException;
import java.io.PrintWriter;

import com.intellij.openapi.compiler.CompilerPaths;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.messages.WarningMessage;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class GroovyController {

    private final EditorAPI editorApi;

    public GroovyController(EditorAPI editorApi) {
        this.editorApi = editorApi;
    }

    public void runAsGroovyScriptInModule(VirtualFile selectedFile, Module module) {
        if (isValidFile(selectedFile)) {
            editorApi.writeMessageToStatusBar("Running " + selectedFile.getName() + "...");
            try {
                GroovyShell groovyShellForScript = createGroovyShellForScript(selectedFile, module);
                groovyShellForScript.run(selectedFile.getInputStream(), selectedFile.getName(), new String[0]);
            } catch (CompilationFailedException e) {
                System.out.println("Run failed: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Run failed: " + e.getMessage());
            }
        }
    }

    private boolean isValidFile(VirtualFile selectedFile) {
        return (selectedFile != null) && selectedFile.isValid() && !selectedFile.isDirectory();
    }

    GroovyShell createGroovyShellForScript(VirtualFile fileToRun, Module module) {
        CompilerConfiguration compilerConfiguration = createCompilerConfiguration(fileToRun, module);
        return new GroovyShell(getClass().getClassLoader(), new Binding(), compilerConfiguration);
    }

    public CompilationUnit createCompilationUnit(VirtualFile fileToCompile, Module module) {
        CompilerConfiguration compilerConfiguration = createCompilerConfiguration(fileToCompile, module);
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        return new CompilationUnit(compilerConfiguration, null, contextClassLoader);
    }

    private CompilerConfiguration createCompilerConfiguration(VirtualFile fileToCompile, Module module) {
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setSourceEncoding(fileToCompile.getCharset().name());
        compilerConfiguration.setClasspath(editorApi.getCompilationClasspath(module));
        compilerConfiguration.setOutput(new PrintWriter(System.out));
        compilerConfiguration.setWarningLevel(WarningMessage.PARANOIA);

        ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
        boolean isInTestSourceContent = moduleRootManager.getFileIndex().isInTestSourceContent(fileToCompile);
        String outputPath = CompilerPaths.getModuleOutputPath(module, isInTestSourceContent);
        compilerConfiguration.setTargetDirectory(outputPath);

        return compilerConfiguration;
    }
}

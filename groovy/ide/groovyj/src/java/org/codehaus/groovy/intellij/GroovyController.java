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

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

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

    protected GroovyShell createGroovyShellForScript(VirtualFile selectedFile, Module module) {
        ClassLoader projectClassLoader = getClass().getClassLoader();
        CompilerConfiguration compilerConfiguration = createCompilerConfigurationForScript(selectedFile, module);
        return new GroovyShell(projectClassLoader, new Binding(), compilerConfiguration);
    }

    private CompilerConfiguration createCompilerConfigurationForScript(VirtualFile selectedFile, Module module) {
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setClasspath(editorApi.getCompilationClasspath(module));
        compilerConfiguration.setSourceEncoding(selectedFile.getCharset().name());
        compilerConfiguration.setTargetDirectory(selectedFile.getPath());
        compilerConfiguration.setOutput(new PrintWriter(System.out));
        return compilerConfiguration;
    }
}

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

import java.io.File;
import java.io.PrintWriter;

import com.intellij.openapi.compiler.CompilerPaths;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;

import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.messages.WarningMessage;

public class GroovyController {

    private final EditorAPI editorApi;

    public GroovyController(EditorAPI editorApi) {
        this.editorApi = editorApi;
    }

    public CompilationUnit createCompilationUnit(Module module, VirtualFile fileToCompile) {
        boolean inTestSourceContent = isInTestSourceContent(module, fileToCompile);
        String characterEncoding = fileToCompile.getCharset().name();
        return createCompilationUnit(module, characterEncoding, inTestSourceContent);
    }

    private boolean isInTestSourceContent(Module module, VirtualFile file) {
        ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
        return moduleRootManager.getFileIndex().isInTestSourceContent(file);
    }

    private CompilationUnit createCompilationUnit(Module module, String characterEncoding, boolean inTestSourceContent) {
        return new CompilationUnit(createCompilerConfiguration(module, characterEncoding, inTestSourceContent));
    }

    private CompilerConfiguration createCompilerConfiguration(Module module, String characterEncoding, boolean inTestSourceContent) {
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setSourceEncoding(characterEncoding);
        compilerConfiguration.setOutput(new PrintWriter(System.out));
        compilerConfiguration.setWarningLevel(WarningMessage.PARANOIA);

        String moduleClasspath = editorApi.getCompilationClasspath(module);
        String sourceFoldersPath = editorApi.getNonExcludedModuleSourceFolders(module).getPathsString();
        compilerConfiguration.setClasspath(moduleClasspath + File.pathSeparator + sourceFoldersPath);
        compilerConfiguration.setTargetDirectory(CompilerPaths.getModuleOutputPath(module, inTestSourceContent));

        return compilerConfiguration;
    }
}

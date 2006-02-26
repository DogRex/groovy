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


package org.codehaus.groovy.intellij.compiler;

import java.io.File;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerPaths;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;

import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.messages.WarningMessage;

import org.codehaus.groovy.intellij.EditorAPI;
import org.codehaus.groovy.intellij.GroovySupportLoader;

import groovy.lang.GroovyClassLoader;

public class GroovyCompiler implements TranslatingCompiler {

    private final EditorAPI editorApi;
    private final CompilationUnitsFactory factory;

    public GroovyCompiler(EditorAPI editorApi, CompilationUnitsFactory factory) {
        this.editorApi = editorApi;
        this.factory = factory;
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
        Map<Module, CompilationUnits> modulesToCompilationUnits = mapModulesToSourceAndTestCompilationUnits(context, filesToCompile);

        for (Module module : modulesToCompilationUnits.keySet()) {
            CompilationUnits compilationUnits = modulesToCompilationUnits.get(module);
            compilationUnits.compile(context, compiledFiles, filesToRecompile);
        }
    }

    private Map<Module,CompilationUnits> mapModulesToSourceAndTestCompilationUnits(CompileContext context, VirtualFile[] filesToCompile) {
        Map<Module, CompilationUnits> modulesToCompilationUnits = new HashMap<Module, CompilationUnits>();

        for (VirtualFile fileToCompile : filesToCompile) {
            Module module = context.getModuleByFile(fileToCompile);
            String characterEncoding = fileToCompile.getCharset().name();
            CompilationUnits compilationUnits = findOrCreateCompilationUnits(module, characterEncoding, modulesToCompilationUnits);
            compilationUnits.add(fileToCompile, isFileInTestSourceFolder(fileToCompile, module));
        }

        return modulesToCompilationUnits;
    }

    private boolean isFileInTestSourceFolder(VirtualFile file, Module module) {
        return moduleRootManager(module).getFileIndex().isInTestSourceContent(file);
    }

    private CompilationUnits findOrCreateCompilationUnits(Module module, String characterEncoding,
                                                          Map<Module, CompilationUnits> modulesToCompilationUnits) {
        CompilationUnits compilationUnits = modulesToCompilationUnits.get(module);
        return (compilationUnits == null)
               ? createCompilationUnits(module, characterEncoding, modulesToCompilationUnits)
               : compilationUnits;
    }

    private CompilationUnits createCompilationUnits(Module module, String characterEncoding, Map<Module, CompilationUnits> modulesToCompilationUnits) {
        CompilationUnits compilationUnits = factory.create(createCompilationUnit(module, characterEncoding, false),
                                                           createCompilationUnit(module, characterEncoding, true));
        modulesToCompilationUnits.put(module, compilationUnits);
        return compilationUnits;
    }

    private ModuleRootManager moduleRootManager(Module module) {
        return ModuleRootManager.getInstance(module);
    }

    private CompilationUnit createCompilationUnit(Module module, String characterEncoding, boolean forTestSourceFolders) {
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setSourceEncoding(characterEncoding);
        compilerConfiguration.setOutput(new PrintWriter(System.err));
        compilerConfiguration.setWarningLevel(WarningMessage.PARANOIA);

        String moduleClasspath = editorApi.getCompilationClasspath(module);
        String sourceFoldersPath = editorApi.getNonExcludedModuleSourceFolders(module).getPathsString();
        compilerConfiguration.setClasspath(moduleClasspath + File.pathSeparator + sourceFoldersPath);
        compilerConfiguration.setTargetDirectory(CompilerPaths.getModuleOutputPath(module, forTestSourceFolders));

        return new CompilationUnit(compilerConfiguration, null, buildClassLoaderFor(compilerConfiguration));
    }

    GroovyClassLoader buildClassLoaderFor(CompilerConfiguration compilerConfiguration) {
        URLClassLoader urlClassLoader = new URLClassLoader(convertClasspathToUrls(compilerConfiguration));
        return new GroovyClassLoader(urlClassLoader, compilerConfiguration);
    }

    private URL[] convertClasspathToUrls(CompilerConfiguration compilerConfiguration) {
        try {
            return classpathAsUrls(compilerConfiguration);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private URL[] classpathAsUrls(CompilerConfiguration compilerConfiguration) throws MalformedURLException {
        List classpath = compilerConfiguration.getClasspath();
        URL[] classpathUrls = new URL[classpath.size()];
        for (int i = 0; i < classpathUrls.length; i++) {
            String classpathEntry = (String) classpath.get(i);
            classpathUrls[i] = new File(classpathEntry).toURL();
        }
        return classpathUrls;
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

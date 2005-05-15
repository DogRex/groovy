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


package org.codehaus.groovy.intellij.configuration;

import com.intellij.execution.RuntimeConfiguration;
import com.intellij.execution.configurations.ConfigurationPerRunnerSettings;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.configurations.RuntimeConfigurationError;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.filters.TextConsoleBuidlerFactory;
import com.intellij.execution.runners.RunnerInfo;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.util.PathUtil;

import org.jdom.Element;

import org.codehaus.groovy.intellij.EditorAPI;

public class GroovyRunConfiguration extends RuntimeConfiguration {

    private final EditorAPI editorApi;
    private final GroovyRunConfigurationExternaliser runConfigurationExternaliser;

    private String scriptPath;
    private String vmParameters;
    private String scriptParameters;
    private String workingDirectoryPath;
    private String moduleName;

    public GroovyRunConfiguration(String name, Project project, GroovyConfigurationFactory configurationFactory, EditorAPI editorApi) {
        super(name, project, configurationFactory);

        this.editorApi = editorApi;
        runConfigurationExternaliser = configurationFactory.getRunConfigurationExternalizer();
        setWorkingDirectoryPath("");
        setModuleName("");
    }

    public String getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    public String getVmParameters() {
        return vmParameters;
    }

    public void setVmParameters(String vmParameters) {
        this.vmParameters = vmParameters;
    }

    public String getScriptParameters() {
        return scriptParameters;
    }

    public void setScriptParameters(String programParameters) {
        this.scriptParameters = programParameters;
    }

    public String getWorkingDirectoryPath() {
        return workingDirectoryPath;
    }

    public void setWorkingDirectoryPath(String workingDirectoryPath) {
        if (workingDirectoryPath == null || workingDirectoryPath.trim().length() == 0) {
            this.workingDirectoryPath = PathUtil.getCanonicalPath(getProject().getProjectFile().getParent().getPath());
        } else {
            this.workingDirectoryPath = workingDirectoryPath;
        }
    }

    public Module getModule() {
        // For some really odd reason, the following always returns null and therefore cannot be used reliably!
//        return ModuleManager.getInstance(getProject()).findModuleByName(getModuleName());

        Module[] modules = ModuleManager.getInstance(getProject()).getModules();
        for (int i = 0; i < modules.length; i++) {
            Module module = modules[i];
            if (module.getName().equals(getModuleName())) {
                return module;
            }
        }
        return null;
        }

    public void setModule(Module module) {
        this.moduleName = (module == null) ? "" : module.getName();
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    // RunConfiguration ------------------------------------------------------------------------------------------------

    public SettingsEditor<GroovyRunConfiguration> getConfigurationEditor() {
        return new GroovySettingsEditor(getProject());
    }

    // RunProfile ------------------------------------------------------------------------------------------------------

    public void checkConfiguration() throws RuntimeConfigurationException {
        if (getScriptPath() == null || getScriptPath().trim().length() == 0) {
            throw new RuntimeConfigurationError("Groovy script not specified");
        }

        if (getModuleName() == null || getModuleName().trim().length() == 0) {
            throw new RuntimeConfigurationError("Module not selected");
        }
    }

    public RunProfileState getState(DataContext context, RunnerInfo runnerInfo, RunnerSettings runnerSettings,
                                    ConfigurationPerRunnerSettings configurationSettings) {
        GroovyCommandLineState commandLineState = new GroovyCommandLineState(this, runnerSettings, configurationSettings);
        commandLineState.setConsoleBuilder(TextConsoleBuidlerFactory.getInstance().createBuilder(getProject()));
        commandLineState.setModulesToCompile(editorApi.getModuleAndDependentModules(getModule()));
        return commandLineState;
    }

    // JDOMExternalizable ----------------------------------------------------------------------------------------------

    public void readExternal(Element element) {
        runConfigurationExternaliser.readExternal(this, element);
    }

    public void writeExternal(Element element) {
        runConfigurationExternaliser.writeExternal(this, element);
    }
}

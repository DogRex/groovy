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


package org.codehaus.groovy.intellij.configuration;

import com.intellij.execution.configurations.ConfigurationInfoProvider;
import com.intellij.execution.configurations.ConfigurationPerRunnerSettings;
import com.intellij.execution.configurations.LocatableConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.configurations.RuntimeConfigurationError;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.filters.TextConsoleBuidlerFactory;
import com.intellij.execution.runners.JavaProgramRunner;
import com.intellij.execution.runners.RunnerInfo;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.util.PathUtil;
import org.codehaus.groovy.intellij.EditorAPI;
import org.jdom.Element;

public class GroovyRunConfiguration extends RunConfigurationBase implements LocatableConfiguration {

    private final EditorAPI editorApi;
    private final GroovyRunConfigurationExternaliser runConfigurationExternaliser;

    private String scriptPath;
    private String vmParameters;
    private String scriptParameters;
    private String workingDirectoryPath;
    private String moduleName;

    public GroovyRunConfiguration(String name, Project project, GroovyConfigurationFactory configurationFactory, EditorAPI editorApi) {
        super(project, configurationFactory, name);

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
        for (Module module : modules) {
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

    String flattenModuleSourceFoldersAsPath() {
        return editorApi.getNonExcludedModuleSourceFolders(getModule()).getPathsString();
    }

    // RunConfiguration ------------------------------------------------------------------------------------------------

    public SettingsEditor<GroovyRunConfiguration> getConfigurationEditor() {
        return new GroovySettingsEditor(getProject());
    }

    public JDOMExternalizable createRunnerSettings(ConfigurationInfoProvider provider) {
        return null;
    }

    public SettingsEditor<JDOMExternalizable> getRunnerSettingsEditor(JavaProgramRunner runner) {
        return null;
    }

    // RunProfile ------------------------------------------------------------------------------------------------------

    public void checkConfiguration() throws RuntimeConfigurationException {
        assertValid(getScriptPath(), "Groovy script not specified");
        assertValid(getModuleName(), "Module not selected");
    }

    private void assertValid(String value, String message) throws RuntimeConfigurationError {
        if (value == null || value.trim().length() == 0) {
            throw new RuntimeConfigurationError(message);
        }
    }

    // return modules to compile before run. Null or empty list to make project
    public Module[] getModules() {
        return new Module[0];
    }

    public RunProfileState getState(DataContext dataContext, RunnerInfo runnerInfo, RunnerSettings runnerSettings,
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

    // LocatableConfiguration ------------------------------------------------------------------------------------------

    public boolean isGeneratedName() {
        return false;
    }

    public String suggestedName() {
        return "Unnamed";
    }
}

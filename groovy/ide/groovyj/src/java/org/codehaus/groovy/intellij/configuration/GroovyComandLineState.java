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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.ConfigurationPerRunnerSettings;
import com.intellij.execution.configurations.JavaCommandLineState;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.RunnerSettings;

import groovy.lang.GroovyShell;

public class GroovyComandLineState extends JavaCommandLineState {

    private final GroovyRunConfiguration runConfiguration;

    public GroovyComandLineState(GroovyRunConfiguration runConfiguration,
                                 RunnerSettings runnerSettings,
                                 ConfigurationPerRunnerSettings configurationSettings) {
        super(runnerSettings, configurationSettings);
        this.runConfiguration = runConfiguration;
    }

    protected JavaParameters createJavaParameters() throws ExecutionException {
        JavaParameters parameters = new JavaParameters();
        parameters.setMainClass(GroovyShell.class.getName());
        parameters.getVMParametersList().addParametersString(runConfiguration.getVmParameters());
        parameters.getProgramParametersList().addParametersString(runConfiguration.getScriptPath());
        parameters.getProgramParametersList().addParametersString(runConfiguration.getScriptParameters());
        parameters.configureByModule(runConfiguration.getModule(), JavaParameters.JDK_AND_CLASSES_AND_TESTS);
        parameters.setWorkingDirectory(runConfiguration.getWorkingDirectoryPath());
        return parameters;
    }
}

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

import com.intellij.execution.configurations.ConfigurationPerRunnerSettings;
import com.intellij.execution.configurations.JavaCommandLineState;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.RunnerSettings;

public class GroovyComandLineState extends JavaCommandLineState {

    private final GroovyRuntimeConfiguration runtimeConfiguration;

    public GroovyComandLineState(GroovyRuntimeConfiguration runtimeConfiguration,
                                 RunnerSettings runnerSettings,
                                 ConfigurationPerRunnerSettings configurationSettings) {
        super(runnerSettings, configurationSettings);
        this.runtimeConfiguration = runtimeConfiguration;
    }

    protected JavaParameters createJavaParameters() {
/*
        JavaParameters javaparameters = new JavaParameters();

        javaparameters.setMainClass(DevelopmentRunnerWrapper.class.getName());

        String pathToPlugin = PathUtil.getJarPathForClass(DevelopmentRunnerWrapper.class);
        javaparameters.getClassPath().add(pathToPlugin);

        if (fULCRunConfiguration.getUseGui()) {
            javaparameters.getProgramParametersList().addParametersString("-useGui");
        }

        Project project = fULCRunConfiguration.getProject();

        if (fULCRunConfiguration.getApplicationClass() != null) {
            javaparameters.getProgramParametersList().addParametersString("-applicationClass " + convertTo$NotationForInnerClasses(fULCRunConfiguration.getApplicationClass()));
        }

        if (fULCRunConfiguration.getConnectionType() != null) {
            javaparameters.getProgramParametersList().addParametersString("-connectionType " + fULCRunConfiguration.getConnectionType().getID());
        }

        if (fULCRunConfiguration.getLogLevel() != null) {
            javaparameters.getProgramParametersList().addParametersString("-logLevel " + fULCRunConfiguration.getLogLevel().getName());
        }

        NameValuePair[] initParameters = fULCRunConfiguration.getInitParameters();
        for (int i = 0; i < initParameters.length; i++) {
            NameValuePair initParameter = initParameters[i];
            javaparameters.getProgramParametersList().addParametersString("-initParameter " + initParameter.getName() + "=" + initParameter.getValue());
        }

        NameValuePair[] userParameters = fULCRunConfiguration.getUserParameters();
        for (int i = 0; i < userParameters.length; i++) {
            NameValuePair userParameter = userParameters[i];
            javaparameters.getProgramParametersList().addParametersString("-userParameter " + userParameter.getName() + "=" + userParameter.getValue());
        }

        javaparameters.getProgramParametersList().addParametersString(fULCRunConfiguration.getProgramParameters());
        javaparameters.getVMParametersList().addParametersString(fULCRunConfiguration.getVMParameters());
        javaparameters.configureByModule(fULCRunConfiguration.getModule(), JavaParameters.JDK_AND_CLASSES_AND_TESTS);

        String workingDirectory = fULCRunConfiguration.getWorkingDirectory();
        if (workingDirectory == null || workingDirectory.trim().length() == 0) {
            workingDirectory = PathUtil.getLocalPath(project.getProjectFile().getParent());
        }
        javaparameters.setWorkingDirectory(workingDirectory);

        return javaparameters;
*/
        return null;
    }
}

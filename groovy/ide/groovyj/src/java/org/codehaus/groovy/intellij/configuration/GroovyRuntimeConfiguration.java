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
import com.intellij.execution.RuntimeConfiguration;
import com.intellij.execution.configurations.ConfigurationPerRunnerSettings;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.runners.RunnerInfo;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;

import org.jdom.Element;

public class GroovyRuntimeConfiguration extends RuntimeConfiguration {

    public GroovyRuntimeConfiguration(String name, Project project, GroovyConfigurationFactory configurationFactory) {
        super(name, project, configurationFactory);
    }

    // RunConfiguration ------------------------------------------------------------------------------------------------

    public SettingsEditor getConfigurationEditor() {
        return new GroovySettingsEditor(getProject());
    }

    // RunProfile ------------------------------------------------------------------------------------------------------

    public RunProfileState getState(DataContext context,
                                    RunnerInfo runnerInfo,
                                    RunnerSettings runnerSettings,
                                    ConfigurationPerRunnerSettings configurationSettings) {
        return null;
    }

    // JDOMExternalizable ----------------------------------------------------------------------------------------------

    public void readExternal(Element element) {}

    public void writeExternal(Element element) {}
}

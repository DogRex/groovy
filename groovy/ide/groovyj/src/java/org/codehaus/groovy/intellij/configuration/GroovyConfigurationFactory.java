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

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTemplate;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.Project;

public class GroovyConfigurationFactory extends ConfigurationFactory implements ApplicationComponent {

    public GroovyConfigurationFactory(GroovyConfigurationType configurationType) {
        super(configurationType);
    }

    // ApplicationComponent --------------------------------------------------------------------------------------------

    public String getComponentName() {
        return "groovyj.configuration.factory";
    }

    public void initComponent() {}

    public void disposeComponent() {}

    // ConfigurationFactory  -------------------------------------------------------------------------------------------

    public RunConfiguration createTemplateConfiguration(Project project) {
        return new GroovyRuntimeConfiguration("Unnamed", project, this);
    }

    public ConfigurationTemplate createConfigurationTemplate(Project project) {
        return null;
    }
}

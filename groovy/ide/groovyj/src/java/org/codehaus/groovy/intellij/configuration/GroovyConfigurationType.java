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

import javax.swing.Icon;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.openapi.util.IconLoader;

public class GroovyConfigurationType implements ConfigurationType {

    private static final Icon ICON = IconLoader.getIcon("/icons/groovy_16x16.png");

    // ConfigurationType -----------------------------------------------------------------------------------------------

    public String getDisplayName() {
        return "Groovy";
    }

    public String getConfigurationTypeDescription() {
        return "Groovy script/class configuration";
    }

    public Icon getIcon() {
        return ICON;
    }

    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[] { new GroovyConfigurationFactory(this) };
    }

    // ApplicationComponent --------------------------------------------------------------------------------------------

    public String getComponentName() {
        return "groovyj.configuration.type";
    }

    public void initComponent() {}

    public void disposeComponent() {}
}

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

import java.util.List;
import java.util.Map;

import junitx.framework.Assert;

import com.intellij.execution.ExternalizablePath;

import org.jdom.Element;

public class GroovyRunConfigurationExternaliserTest extends GroovyConfigurationTestCase {

    private final GroovyRunConfigurationExternaliser runConfigurationExternaliser = new GroovyRunConfigurationExternaliser();

    private final GroovyRunConfiguration runConfiguration = createRunConfiguration("-showversion", "src/scripts/foo.groovy",
                                                                                   "-ignoreWarnings -scanOnly", "/home/acme");

    public void testSerialisesAGivenUninitialisedConfigurationToAnExternalisableElement() {
        Element rootElement = new Element("configuration");
        assertEquals("number of modules", 0, rootElement.getChildren("module").size());
        assertEquals("number of options", 0, rootElement.getChildren("option").size());

        GroovyRunConfiguration uninitialisedRunConfiguration = createRunConfiguration(null, null, null, null);
        uninitialisedRunConfiguration.setModuleName("");

        runConfigurationExternaliser.writeExternal(uninitialisedRunConfiguration, rootElement);

        List options = rootElement.getChildren("option");
        assertEquals("number of modules", 1, rootElement.getChildren("module").size());
        assertEquals("number of options", 4, options.size());

        Map optionsByName = GroovyRunConfigurationExternaliser.buildOptionsByName(options);
        assertEquals("script path", "", optionsByName.get("SCRIPT_PATH"));
        assertEquals("VM parameters", "", optionsByName.get("VM_PARAMETERS"));
        assertEquals("scripit parameters", "", optionsByName.get("SCRIPT_PARAMETERS"));
        assertEquals("working directory path", "", optionsByName.get("WORKING_DIRECTORY_PATH"));

        Element moduleElement = rootElement.getChild("module");
        assertEquals("module name", "", moduleElement.getAttribute("name").getValue());
    }

    public void testSerialisesAGivenConfigurationToAnExternalisableElement() {
        Element rootElement = new Element("configuration");
        assertEquals("number of modules", 0, rootElement.getChildren("module").size());
        assertEquals("number of options", 0, rootElement.getChildren("option").size());

        runConfigurationExternaliser.writeExternal(runConfiguration, rootElement);

        List options = rootElement.getChildren("option");
        assertEquals("number of modules", 1, rootElement.getChildren("module").size());
        assertEquals("number of options", 4, options.size());

        Map optionsByName = GroovyRunConfigurationExternaliser.buildOptionsByName(options);
        assertEquals("script path", runConfiguration.getScriptPath(), optionsByName.get("SCRIPT_PATH"));
        assertEquals("VM parameters", runConfiguration.getVmParameters(), optionsByName.get("VM_PARAMETERS"));
        assertEquals("scripit parameters", runConfiguration.getScriptParameters(), optionsByName.get("SCRIPT_PARAMETERS"));
        assertEquals("working directory path", ExternalizablePath.urlValue(runConfiguration.getWorkingDirectoryPath()), optionsByName.get("WORKING_DIRECTORY_PATH"));

        Element moduleElement = rootElement.getChild("module");
        assertEquals("module name", runConfiguration.getModuleName(), moduleElement.getAttribute("name").getValue());
    }

    public void testDeserialisesAnExternalisedElementIntoAGivenConfiguration() {
        Element rootElement = new Element("configuration");
        runConfigurationExternaliser.writeExternal(runConfiguration, rootElement);

        GroovyRunConfiguration anotherRunConfiguration = createRunConfiguration("", "", "", "");
        Assert.assertNotEquals("script path", runConfiguration.getScriptPath(), anotherRunConfiguration.getScriptPath());
        Assert.assertNotEquals("VM parameters", runConfiguration.getVmParameters(), anotherRunConfiguration.getVmParameters());
        Assert.assertNotEquals("scripit parameters", runConfiguration.getScriptParameters(), anotherRunConfiguration.getScriptParameters());
        Assert.assertNotEquals("working directory path", ExternalizablePath.localPathValue(runConfiguration.getWorkingDirectoryPath()), anotherRunConfiguration.getWorkingDirectoryPath());
        Assert.assertNotEquals("module name", runConfiguration.getModuleName(), anotherRunConfiguration.getModuleName());

        runConfigurationExternaliser.readExternal(anotherRunConfiguration, rootElement);

        assertEquals("script path", runConfiguration.getScriptPath(), anotherRunConfiguration.getScriptPath());
        assertEquals("VM parameters", runConfiguration.getVmParameters(), anotherRunConfiguration.getVmParameters());
        assertEquals("scripit parameters", runConfiguration.getScriptParameters(), anotherRunConfiguration.getScriptParameters());
        assertEquals("working directory path", ExternalizablePath.localPathValue(runConfiguration.getWorkingDirectoryPath()), anotherRunConfiguration.getWorkingDirectoryPath());
        assertEquals("module name", runConfiguration.getModuleName(), anotherRunConfiguration.getModuleName());
    }
}

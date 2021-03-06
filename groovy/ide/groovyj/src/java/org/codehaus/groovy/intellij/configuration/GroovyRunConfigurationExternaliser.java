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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intellij.execution.ExternalizablePath;

import org.jdom.Element;

public class GroovyRunConfigurationExternaliser {

    private static final String MODULE_ELEMENT = "module";
    private static final String OPTION_ELEMENT = "option";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String VALUE_ATTRIBUTE = "value";

    private static final String SCRIPT_PATH = "SCRIPT_PATH";
    private static final String VM_PARAMETERS = "VM_PARAMETERS";
    private static final String SCRIPT_PARAMETERS = "SCRIPT_PARAMETERS";
    private static final String WORKING_DIRECTORY_PATH = "WORKING_DIRECTORY_PATH";

    public void writeExternal(GroovyRunConfiguration runConfiguration, Element element) {
        writeOption(SCRIPT_PATH, guard(runConfiguration.getScriptPath()), element);
        writeOption(VM_PARAMETERS, guard(runConfiguration.getVmParameters()), element);
        writeOption(SCRIPT_PARAMETERS, guard(runConfiguration.getScriptParameters()), element);
        writeOption(WORKING_DIRECTORY_PATH, guard(ExternalizablePath.urlValue(runConfiguration.getWorkingDirectoryPath())), element);
        writeModule(runConfiguration.getModuleName(), element);
    }

    public void readExternal(GroovyRunConfiguration runConfiguration, Element element) {
        Map<String, String> optionsByName = buildOptionsByName(element.getChildren(OPTION_ELEMENT));

        runConfiguration.setScriptPath(optionsByName.get(SCRIPT_PATH));
        runConfiguration.setVmParameters(optionsByName.get(VM_PARAMETERS));
        runConfiguration.setScriptParameters(optionsByName.get(SCRIPT_PARAMETERS));
        runConfiguration.setWorkingDirectoryPath(ExternalizablePath.localPathValue(optionsByName.get(WORKING_DIRECTORY_PATH)));
        runConfiguration.setModuleName(element.getChild(MODULE_ELEMENT).getAttribute(NAME_ATTRIBUTE).getValue());
    }

    private static void writeOption(String name, String value, Element element) {
        Element optionElement = new Element(OPTION_ELEMENT);
        optionElement.setAttribute(NAME_ATTRIBUTE, name);
        optionElement.setAttribute(VALUE_ATTRIBUTE, value);
        element.addContent(optionElement);
    }

    private static void writeModule(String moduleName, Element element) {
        Element moduleElement = new Element(MODULE_ELEMENT);
        moduleElement.setAttribute(NAME_ATTRIBUTE, moduleName);
        element.addContent(moduleElement);
    }

    static Map<String, String> buildOptionsByName(List<Element> listOfOptionElements) {
        Map<String, String> optionMap = new HashMap<String, String>();

        for (Element optionElement : listOfOptionElements) {
            optionMap.put(optionElement.getAttribute(NAME_ATTRIBUTE).getValue(),
                          optionElement.getAttribute(VALUE_ATTRIBUTE).getValue());
        }

        return optionMap;
    }

    private static String guard(String value) {
        return (value == null) ? "" : value;
    }
}

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
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.ParametersList;

import groovy.lang.GroovyShell;

public class GroovyCommandLineStateTest extends GroovyConfigurationTestCase {

    public void testDoesNotCreateJavaParametersWhenAGivenGroovyRunConfigurationIsNotValid() {
        GroovyRunConfiguration invalidRunConfiguration = createRunConfiguration(null, "~/scripts/foobar.groovy", null, null);
        invalidRunConfiguration.setModuleName("");

        try {
            new GroovyCommandLineState(invalidRunConfiguration, null, null).createJavaParameters();
            fail("Java parameters cannot be created with an invalid run configuration!");
        } catch (ExecutionException expected) {
            assertEquals("message", "Module not selected", expected.getMessage());
        }
    }

    public void testCreatesJavaParametersFromAGivenGroovyRunConfiguration() throws ExecutionException {
        GroovyRunConfiguration runConfiguration =
                createRunConfiguration("-showversion -Xms128m -Xmx512m",
                                       "C:\\Documents and Settings\\Foo Bar\\.groovy\\scripts\\foobar.groovy",
                                       "-dir C:\\WINDOWS -enableWarnings",
                                       "C:\\Documents and Settings\\All Users\\.groovy");

        GroovyCommandLineState commandLineState = new GroovyCommandLineState(runConfiguration, null, null);
        JavaParameters javaParameters = commandLineState.createJavaParameters();

        assertEquals("main class", GroovyShell.class.getName(), javaParameters.getMainClass());
        assertEquals("VM parameters", runConfiguration.getVmParameters(), javaParameters.getVMParametersList().getParametersString().trim());

        ParametersList groovyShellParameters = javaParameters.getProgramParametersList();
        assertEquals("number of program parameters", 4, groovyShellParameters.getList().size());
        assertEquals("script path", runConfiguration.getScriptPath(), groovyShellParameters.getList().get(0));
        assertEquals("all program parameters",
                     "\"" + runConfiguration.getScriptPath() + "\"" + " " + runConfiguration.getScriptParameters(),
                     groovyShellParameters.getParametersString().trim());

        assertEquals("working directory path", runConfiguration.getWorkingDirectoryPath(), javaParameters.getWorkingDirectory());
    }
}

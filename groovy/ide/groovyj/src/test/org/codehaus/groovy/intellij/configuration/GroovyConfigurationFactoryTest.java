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

import junit.framework.TestCase;

import com.intellij.execution.configurations.RunConfiguration;

public class GroovyConfigurationFactoryTest extends TestCase {

    private final GroovyConfigurationFactory configurationFactory = new GroovyConfigurationFactory(new GroovyConfigurationType());

    public void testHasAComponentName() {
        assertEquals("groovyj.configuration.factory", configurationFactory.getComponentName());
    }

    public void testDoesNothingWhenInitialisedByIdea() {
        configurationFactory.initComponent();
    }

    public void testDoesNothingWhenDisposedByIdea() {
        configurationFactory.disposeComponent();
    }

    public void testCreatesARunConfigurationForAnyGivenProject() {
        RunConfiguration templateConfiguration = configurationFactory.createTemplateConfiguration(null);
        assertNull(templateConfiguration.getProject());
        assertSame("configuration factory", configurationFactory, templateConfiguration.getFactory());
    }

    public void testReturnsNullWhenAskedToCreateAConfigurationTemplate() {
        assertNull(configurationFactory.createConfigurationTemplate(null));
    }
}

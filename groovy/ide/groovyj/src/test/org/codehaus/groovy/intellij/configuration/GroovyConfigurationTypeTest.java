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

import junit.framework.TestCase;
import junitx.framework.ObjectAssert;

import com.intellij.execution.configurations.ConfigurationFactory;

public class GroovyConfigurationTypeTest extends TestCase {

    private final GroovyConfigurationType configurationType = new GroovyConfigurationType();

    public void testHasADisplayName() {
        assertEquals("Groovy", configurationType.getDisplayName());
    }

    public void testHasADescription() {
        assertEquals("Groovy script/class configuration", configurationType.getConfigurationTypeDescription());
    }

    public void testHasAnIcon() {
        assertNotNull(configurationType.getIcon());
        ObjectAssert.assertInstanceOf(Icon.class, configurationType.getIcon());
    }

    public void testHasJustOneConfigurationFactory() {
        ConfigurationFactory[] configurationFactories = configurationType.getConfigurationFactories();
        assertNotNull(configurationFactories);
        assertEquals(1, configurationFactories.length);
        assertSame(configurationType, configurationFactories[0].getType());
    }

    public void testHasAComponentName() {
        assertEquals("groovyj.configuration.type", configurationType.getComponentName());
    }

    public void testDoesNothingWhenInitialisedByIdea() {
        configurationType.initComponent();
    }

    public void testDoesNothingWhenDisposedByIdea() {
        configurationType.disposeComponent();
    }
}

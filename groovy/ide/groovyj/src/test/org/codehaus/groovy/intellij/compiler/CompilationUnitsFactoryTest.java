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


package org.codehaus.groovy.intellij.compiler;

import org.codehaus.groovy.control.CompilationUnit;

import org.codehaus.groovy.intellij.GroovyjTestCase;

public class CompilationUnitsFactoryTest extends GroovyjTestCase {

    public void testJoinsOneSourceAndOneTestCompilationUnitsAsOne() {
        CompilationUnit sourceCompilationUnit = new CompilationUnit();
        CompilationUnit testCompilationUnit = new CompilationUnit();

        CompilationUnitsFactory factory = new CompilationUnitsFactory();
        CompilationUnits actualCompilationUnits = factory.create(sourceCompilationUnit, testCompilationUnit);
        assertSame("source compilation unit", sourceCompilationUnit, actualCompilationUnits.sourceCompilationUnit);
        assertSame("test compilation unit", testCompilationUnit, actualCompilationUnits.testCompilationUnit);
    }
}

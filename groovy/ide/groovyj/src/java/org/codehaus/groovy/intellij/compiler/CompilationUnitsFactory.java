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

import com.intellij.openapi.projectRoots.ProjectJdk;

import org.codehaus.groovy.control.CompilationUnit;

public class CompilationUnitsFactory {

    public CompilationUnits create(CompilationUnit sourceCompilationUnit, CompilationUnit testCompilationUnit, ProjectJdk jdk) {
        return new CompilationUnits(sourceCompilationUnit, testCompilationUnit, jdk);
    }
}

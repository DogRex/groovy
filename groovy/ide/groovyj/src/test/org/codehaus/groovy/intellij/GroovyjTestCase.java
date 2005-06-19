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


package org.codehaus.groovy.intellij;

import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.MockVirtualFile;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

import org.codehaus.groovy.intellij.configuration.GroovyConfigurationFactory;
import org.codehaus.groovy.intellij.configuration.GroovyRunConfiguration;

public abstract class GroovyjTestCase extends MockObjectTestCase {

    static {
        MockApplicationManager.reset();
    }

    protected Project createStubbedProject() {
        return createStubbedProject(mock(Project.class, "stubProject#" + TestUtil.nextAbsRandomInt()));
    }

    protected Project createStubbedProject(Mock stubProject) {
        Mock stubProjectFile = Mocks.createVirtualFileMock(this, "stubProjectFile");
        stubProject.stubs().method("getName").will(returnValue(stubProjectFile.toString()));
        stubProject.stubs().method("getProjectFile").will(returnValue(stubProjectFile.proxy()));

        Mock stubProjectFileParentDirectory = Mocks.createVirtualFileMock(this, "stubProjectFileParentDirectory");
        stubProjectFile.stubs().method("getParent").will(returnValue(stubProjectFileParentDirectory.proxy()));
        stubProjectFileParentDirectory.stubs().method("getPath").will(returnValue(null));

        stubProject.stubs().method("getComponent").with(same(PsiManager.class)).will(returnValue(null));
        addRunConfigurationCreationCapability(stubProject);
        addModules(stubProject);

        return (Project) stubProject.proxy();
    }

    private void addRunConfigurationCreationCapability(Mock stubProject) {
        Mock stubRunManager = mock(RunManager.class, "stubRunManager");
        stubProject.stubs().method("getComponent").with(same(RunManager.class)).will(returnValue(stubRunManager.proxy()));

        Mock stubRunnerAndConfigurationSettings = mock(RunnerAndConfigurationSettings.class, "stubRunnerAndConfigurationSettings");
        stubRunManager.stubs().method("createRunConfiguration").withAnyArguments().will(returnValue(stubRunnerAndConfigurationSettings.proxy()));

        GroovyConfigurationFactory configurationFactory = new GroovyConfigurationFactory(null, null, null);
        GroovyRunConfiguration runConfiguration = new GroovyRunConfiguration(null, (Project) stubProject.proxy(), configurationFactory, null);
        stubRunnerAndConfigurationSettings.stubs().method("getConfiguration").will(returnValue(runConfiguration));
    }

    private void addModules(Mock stubProject) {
        Mock stubModuleManager = mock(ModuleManager.class, "stubModuleManager");
        stubProject.stubs().method("getComponent").with(same(ModuleManager.class)).will(returnValue(stubModuleManager.proxy()));

        Module[] allProjectModules = new Module[] { createStubbedModule(), createStubbedModule() };
        stubModuleManager.stubs().method("getModules").will(returnValue(allProjectModules));
        stubModuleManager.stubs().method("getSortedModules").will(returnValue(allProjectModules));

        for (Module projectModule : allProjectModules) {
            stubModuleManager.stubs().method("findModuleByName").with(eq(projectModule.getName())).will(returnValue(projectModule));
            stubModuleManager.stubs().method("findModuleByName").with(not(eq(projectModule.getName()))).will(returnValue(null));
        }
    }

    protected Module createStubbedModule() {
        return createStubbedModule(new JavaModuleType());
    }

    protected Module createStubbedModule(ModuleType moduleType) {
        String moduleName = "stubModule#" + TestUtil.nextAbsRandomInt();

        Mock stubModule = mock(Module.class, moduleName);
        stubModule.stubs().method("getName").will(returnValue(moduleName));
        stubModule.stubs().method("getModuleType").will(returnValue(moduleType));

        Mock stubModuleRootManager = mock(ModuleRootManager.class, "stubModuleRootManager");
        stubModule.stubs().method("getComponent").with(same(ModuleRootManager.class)).will(returnValue(stubModuleRootManager.proxy()));

        Mock stubProjectJdk = mock(ProjectJdk.class, "stubProjectJdk");
        stubModuleRootManager.stubs().method("getJdk").will(returnValue(stubProjectJdk.proxy()));
        stubProjectJdk.stubs().method("getHomeDirectory").will(returnValue(new MockVirtualFile()));

        stubModuleRootManager.stubs().method("processOrder").withAnyArguments();
        return (Module) stubModule.proxy();
    }
}

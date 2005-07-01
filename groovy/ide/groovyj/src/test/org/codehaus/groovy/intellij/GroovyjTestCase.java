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

import java.io.File;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.Random;

import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdk;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleFileIndex;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.MockVirtualFile;
import com.intellij.util.PathsList;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.jmock.core.Constraint;
import org.jmock.core.constraint.IsEqual;

import org.codehaus.groovy.intellij.configuration.GroovyConfigurationFactory;
import org.codehaus.groovy.intellij.configuration.GroovyRunConfiguration;

import com.thoughtworks.xstream.XStream;

public abstract class GroovyjTestCase extends MockObjectTestCase {

    static {
        MockApplicationManager.reset();
    }

    private final Random random = new Random();

    public int nextPositiveRandomInt() {
        return Math.abs(nextRandomInt());
    }

    protected int nextRandomInt() {
        return random.nextInt();
    }

    protected String someString() {
        return String.valueOf(Math.random() * System.currentTimeMillis());
    }

    protected PathsList createPathsList(String path) {
        PathsList pathsList = new PathsList();
        pathsList.add(path);
        return pathsList;
    }

    protected Constraint startsWith(final String text) {
        return new Constraint() {
            public boolean eval(Object o) {
                return (o instanceof String) && ((String) o).startsWith(text);
            }

            public StringBuffer describeTo(StringBuffer buffer) {
                return buffer.append("a string starting with \"").append(text).append("\"");
            }
        };
    }

    protected Constraint isSemanticallyEqualTo(Object operand) {
        return new IsSemanticallyEqual(new XStream(), operand);
    }

    private static class IsSemanticallyEqual extends IsEqual {

        private final XStream xStream;

        public IsSemanticallyEqual(XStream xStream, Object expected) {
            super(xStream.toXML(expected));
            this.xStream = xStream;
        }

        public boolean eval(Object o) {
            return super.eval(xStream.toXML(o));
        }
    }

    protected ProjectBuilder project() {
        return projectNamed("stubProject#" + nextPositiveRandomInt());
    }

    protected ProjectBuilder projectNamed(String projectName) {
        return new ProjectBuilder(this).projectNamed(projectName);
    }

    protected ModuleBuilder module() {
        return moduleNamed("stubModule#" + nextPositiveRandomInt());
    }

    protected ModuleBuilder moduleNamed(String moduleName) {
        return new ModuleBuilder(this).moduleNamed(moduleName);
    }

    protected VirtualFileBuilder virtualFile() {
        return new VirtualFileBuilder(this);
    }

    protected VirtualFileBuilder virtualFile(String path) {
        return virtualFile().withCharset("UTF-8").withPath(path);
    }

    protected static class ProjectBuilder {

        private final MockObjectTestCase testCase;
        private final Mock stubProjectFile;
        private final Mock stubProjectRootManager;
        private final Mock stubModuleManager;
        private Mock stubProject;

        private ProjectBuilder(MockObjectTestCase testCase) {
            this.testCase = testCase;
            stubProjectFile = Mocks.createVirtualFileMock(testCase, "stubProjectFile");
            stubProjectRootManager = testCase.mock(ProjectRootManager.class);
            stubModuleManager = testCase.mock(ModuleManager.class, "stubModuleManager");
        }

        private ProjectBuilder projectNamed(String projectName) {
            stubProject = testCase.mock(Project.class, projectName);
            stubProject.stubs().method("getName").will(testCase.returnValue(projectName));
            stubProject.stubs().method("getComponent").with(testCase.same(PsiManager.class)).will(testCase.returnValue(null));
            stubProject.stubs().method("getComponent").with(testCase.same(ProjectRootManager.class)).will(testCase.returnValue(stubProjectRootManager.proxy()));
            stubProject.stubs().method("getComponent").with(testCase.same(ModuleManager.class)).will(testCase.returnValue(stubModuleManager.proxy()));
            return this;
        }

        public ProjectBuilder withProjectFile() {
            return withProjectFile((ProjectFileIndex) testCase.newDummy(ProjectFileIndex.class));
        }

        public ProjectBuilder withProjectFile(ProjectFileIndex projectFileIndex) {
            stubProject.stubs().method("getProjectFile").will(testCase.returnValue(stubProjectFile.proxy()));

            Mock stubProjectFileParentDirectory = Mocks.createVirtualFileMock(testCase, "stubProjectFileParentDirectory");
            stubProjectFile.stubs().method("getParent").will(testCase.returnValue(stubProjectFileParentDirectory.proxy()));
            stubProjectFileParentDirectory.stubs().method("getPath").will(testCase.returnValue(null));

            stubProjectRootManager.stubs().method("getFileIndex").will(testCase.returnValue(projectFileIndex));
            return this;
        }

        public ProjectBuilder withModules(Module[] allProjectModules) {
            stubModuleManager.stubs().method("getModules").will(testCase.returnValue(allProjectModules));
            stubModuleManager.stubs().method("getSortedModules").will(testCase.returnValue(allProjectModules));

            for (Module module : allProjectModules) {
                stubModuleManager.stubs().method("findModuleByName").with(testCase.eq(module.getName())).will(testCase.returnValue(module));
                stubModuleManager.stubs().method("findModuleByName").with(testCase.not(testCase.eq(module.getName()))).will(testCase.returnValue(null));
            }

            return this;
        }

        public ProjectBuilder canCreateRunConfigurations() {
            Mock stubRunManager = testCase.mock(RunManager.class, "stubRunManager");
            stubProject.stubs().method("getComponent").with(testCase.same(RunManager.class)).will(testCase.returnValue(stubRunManager.proxy()));

            Mock stubRunnerAndConfigurationSettings = testCase.mock(RunnerAndConfigurationSettings.class, "stubRunnerAndConfigurationSettings");
            stubRunManager.stubs().method("createRunConfiguration").withAnyArguments().will(testCase.returnValue(stubRunnerAndConfigurationSettings.proxy()));

            GroovyConfigurationFactory configurationFactory = new GroovyConfigurationFactory(null, null, null);
            GroovyRunConfiguration runConfiguration = new GroovyRunConfiguration(null, (Project) stubProject.proxy(), configurationFactory, null);
            stubRunnerAndConfigurationSettings.stubs().method("getConfiguration").will(testCase.returnValue(runConfiguration));
            return this;
        }

        public Project build() {
            return (Project) stubProject.proxy();
        }
    }

    protected static class ModuleBuilder {

        private final MockObjectTestCase testCase;
        private final Mock stubModuleRootManager;
        private final Mock stubModuleFileIndex;
        private Mock stubModule;

        private ModuleBuilder(MockObjectTestCase testCase) {
            this.testCase = testCase;
            stubModuleRootManager = testCase.mock(ModuleRootManager.class, "stubModuleRootManager");
            stubModuleFileIndex = testCase.mock(ModuleFileIndex.class, "stubModuleFileIndex");
            stubModuleRootManager.stubs().method("getFileIndex").will(testCase.returnValue(stubModuleFileIndex.proxy()));
        }

        private ModuleBuilder moduleNamed(String moduleName) {
            stubModule = testCase.mock(Module.class, moduleName);
            stubModule.stubs().method("getName").will(testCase.returnValue(moduleName));
            stubModule.stubs().method("getComponent").with(testCase.same(ModuleRootManager.class)).will(testCase.returnValue(stubModuleRootManager.proxy()));
            return this;
        }

        public ModuleBuilder isA(ModuleType moduleType) {
            stubModule.stubs().method("getModuleType").will(testCase.returnValue(moduleType));
            return this;
        }

        public ModuleBuilder forFileInSourceFolder(VirtualFile file, boolean inTestSourceFolder) {
            stubModuleFileIndex.stubs().method("isInTestSourceContent").with(testCase.same(file)).will(testCase.returnValue(inTestSourceFolder));
            return this;
        }

        public ModuleBuilder withModuleRootModel(ModifiableRootModel moduleRootModel) {
            stubModuleRootManager.stubs().method("getModifiableModel").will(testCase.returnValue(moduleRootModel));
            return this;
        }

        public ModuleBuilder withCompilerOutputPathUrl(String compilerOutputPathUrl) {
            stubModuleRootManager.stubs().method("getCompilerOutputPathUrl").will(testCase.returnValue(compilerOutputPathUrl));
            return this;
        }

        public ModuleBuilder withCompilerOutputPathForTestsUrl(String compilerOutputPathForTestsUrl) {
            stubModuleRootManager.stubs().method("getCompilerOutputPathForTestsUrl").will(testCase.returnValue(compilerOutputPathForTestsUrl));
            return this;
        }

        public ModuleBuilder withProjectJdk() {
            Mock stubProjectJdk = testCase.mock(ProjectJdk.class, "stubProjectJdk");
            stubProjectJdk.stubs().method("getHomeDirectory").will(testCase.returnValue(new MockVirtualFile()));
            stubModuleRootManager.stubs().method("getJdk").will(testCase.returnValue(stubProjectJdk.proxy()));
            stubModuleRootManager.stubs().method("processOrder").withAnyArguments();
            return this;
        }

        public Module build() {
            return (Module) stubModule.proxy();
        }
    }

    protected static class VirtualFileBuilder {

        private final MockObjectTestCase testCase;
        private final Mock mockVirtualFile;

        public VirtualFileBuilder(MockObjectTestCase testCase) {
            this.testCase = testCase;
            mockVirtualFile = testCase.mock(VirtualFile.class);
        }

        public VirtualFileBuilder withCharset(String charsetName) {
            mockVirtualFile.stubs().method("getCharset").will(testCase.returnValue(Charset.forName("UTF-8")));
            return this;
        }

        public VirtualFileBuilder withPath(String path) {
            mockVirtualFile.stubs().method("getPath").will(testCase.returnValue(path));
            mockVirtualFile.stubs().method("getNameWithoutExtension").will(testCase.returnValue(nameWithoutExtension(path)));
            mockVirtualFile.stubs().method("getUrl").will(testCase.returnValue(pathToUrl(path)));
            return this;
        }

        private String nameWithoutExtension(String path) {
            return new MockVirtualFile(path).getNameWithoutExtension();
        }

        private String pathToUrl(String path) {
            try {
                return new File(path).toURL().toExternalForm();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        public VirtualFileBuilder withExtension(String fileExtension) {
            mockVirtualFile.stubs().method("getExtension").will(testCase.returnValue(fileExtension));
            return this;
        }

        public VirtualFileBuilder withFileType(FileType fileType) {
            mockVirtualFile.stubs().method("getFileType").will(testCase.returnValue(fileType));
            return this;
        }

        public VirtualFile build() {
            return (VirtualFile) mockVirtualFile.proxy();
        }
    }
}

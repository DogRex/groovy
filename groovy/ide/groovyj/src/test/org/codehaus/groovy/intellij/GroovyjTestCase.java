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


package org.codehaus.groovy.intellij;

import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdk;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.PathUtil;
import com.intellij.util.PathsList;
import com.thoughtworks.xstream.XStream;
import org.codehaus.groovy.intellij.configuration.GroovyConfigurationFactory;
import org.codehaus.groovy.intellij.configuration.GroovyRunConfiguration;
import org.intellij.openapi.testing.MockApplicationManager;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.jmock.core.Constraint;
import org.jmock.core.constraint.IsEqual;

import java.nio.charset.Charset;
import java.util.Random;

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

    protected Constraint equivalent(Object operand) {
        return new IsEquivalent(new XStream(), operand);
    }

    private static class IsEquivalent extends IsEqual {

        private final XStream xStream;

        private IsEquivalent(XStream xStream, Object expected) {
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
            stubProjectJdk.stubs().method("getHomeDirectory").will(testCase.returnValue(new LightVirtualFile()));
            return withProjectJdk((ProjectJdk) stubProjectJdk.proxy());
        }

        public ModuleBuilder withProjectJdk(ProjectJdk jdk) {
            stubModuleRootManager.stubs().method("getJdk").will(testCase.returnValue(jdk));
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

        private VirtualFileBuilder(MockObjectTestCase testCase) {
            this.testCase = testCase;
            mockVirtualFile = testCase.mock(VirtualFile.class);
        }

        public VirtualFileBuilder withCharset(String charsetName) {
            mockVirtualFile.stubs().method("getCharset").will(testCase.returnValue(Charset.forName(charsetName)));
            return this;
        }

        public VirtualFileBuilder withPath(String path) {
            mockVirtualFile.stubs().method("getPath").will(testCase.returnValue(path));
            mockVirtualFile.stubs().method("getNameWithoutExtension").will(testCase.returnValue(nameWithoutExtension(path)));
            mockVirtualFile.stubs().method("getUrl").will(testCase.returnValue(pathToUrl(path)));
            return this;
        }

        private String nameWithoutExtension(String path) {
            return new LightVirtualFile(path).getNameWithoutExtension();
        }

        private String pathToUrl(String path) {
            return VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, PathUtil.getCanonicalPath(path));
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

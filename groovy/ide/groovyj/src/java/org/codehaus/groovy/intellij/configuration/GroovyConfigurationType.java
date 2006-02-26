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

import javax.swing.Icon;

import com.intellij.execution.LocatableConfigurationType;
import com.intellij.execution.Location;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.util.PathUtil;

import org.codehaus.groovy.intellij.EditorAPIFactory;
import org.codehaus.groovy.intellij.GroovySupportLoader;
import org.codehaus.groovy.intellij.Icons;

public class GroovyConfigurationType implements LocatableConfigurationType {

    private final GroovyConfigurationFactory configurationFactory;

    // ApplicationComponent --------------------------------------------------------------------------------------------

    public GroovyConfigurationType(EditorAPIFactory editorApiFactory) {
        configurationFactory = new GroovyConfigurationFactory(editorApiFactory, this, new GroovyRunConfigurationExternaliser());
    }

    public String getComponentName() {
        return "groovy.configuration.type";
    }

    public void initComponent() {}

    public void disposeComponent() {}

    // LocatableConfigurationType --------------------------------------------------------------------------------------

    public String getDisplayName() {
        return "Groovy";
    }

    public String getConfigurationTypeDescription() {
        return "Groovy configuration";
    }

    public Icon getIcon() {
        return Icons.SMALLEST;
    }

    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[] { configurationFactory };
    }

    public RunnerAndConfigurationSettings createConfigurationByLocation(Location location) {
        if (location == null || location.getPsiElement() == null || location.getOpenFileDescriptor() == null) {
            return null;
        }

        return createRunnerAndConfigurationSettings(location);
    }

    private RunnerAndConfigurationSettings createRunnerAndConfigurationSettings(Location location) {
        VirtualFile file = location.getOpenFileDescriptor().getFile();
        if (GroovySupportLoader.GROOVY != file.getFileType()) {
            return null;
        }

        RunManager runManager = RunManager.getInstance(location.getProject());
        RunnerAndConfigurationSettings settings = runManager.createRunConfiguration(file.getNameWithoutExtension(), configurationFactory);

        GroovyRunConfiguration runConfiguration = (GroovyRunConfiguration) settings.getConfiguration();
        runConfiguration.setScriptPath(PathUtil.getCanonicalPath(file.getPath()));

        Project project = location.getPsiElement().getManager().getProject();
        ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(project).getFileIndex();
        runConfiguration.setModule(projectFileIndex.getModuleForFile(file));

        return settings;
    }

    public boolean isConfigurationByElement(RunConfiguration runConfiguration, Project project, PsiElement psiElement) {
        if (runConfiguration instanceof GroovyRunConfiguration) {
            VirtualFile file = psiElement.getContainingFile().getVirtualFile();
            if (GroovySupportLoader.GROOVY == file.getFileType()) {
                String scriptPath = ((GroovyRunConfiguration) runConfiguration).getScriptPath();
                return PathUtil.getCanonicalPath(file.getPath()).equals(scriptPath);
            }
        }
        return false;
    }
}

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

import com.intellij.execution.LocatableConfigurationType;
import com.intellij.execution.Location;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiElement;

import org.codehaus.groovy.intellij.EditorAPIFactory;

public class GroovyConfigurationType implements LocatableConfigurationType {

    private static final Icon ICON = IconLoader.getIcon("/icons/groovy_16x16.png");

    private final GroovyConfigurationFactory configurationFactory;

    // ApplicationComponent --------------------------------------------------------------------------------------------

    public GroovyConfigurationType(EditorAPIFactory editorApiFactory) {
        configurationFactory = new GroovyConfigurationFactory(this, editorApiFactory);
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
        return ICON;
    }

    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[] { configurationFactory };
    }

    public RunnerAndConfigurationSettings createConfigurationByLocation(Location location) {
        if ((location == null) || (location.getPsiElement() == null)) {
            return null;
        }
/*
        PsiClass groovyScript = findGroovyScript(location.getPsiElement());
        return (groovyScript != null) ? createRunnerAndConfigurationSettings(location, groovyScript) : null;
*/
        return null;
    }

/*
    private RunnerAndConfigurationSettings createRunnerAndConfigurationSettings(Location location, PsiClass groovyScript) {
        RunManager runManager = RunManager.getInstance(location.getProject());
        RunnerAndConfigurationSettings runnerAndConfigurationSettings = runManager.createRunConfiguration("", configurationFactory);
        GroovyRuntimeConfiguration runtimeConfiguration = (GroovyRuntimeConfiguration) runnerAndConfigurationSettings.getConfiguration();

        runtimeConfiguration.setName(groovyScript.getName());
        runtimeConfiguration.setQualifiedNameForScript(groovyScript.getQualifiedName());

        Project project = groovyScript.getManager().getProject();
        ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();
        VirtualFile virtualFile = groovyScript.getContainingFile().getVirtualFile();
        Module module = fileIndex.getModuleForFile(virtualFile);
        runtimeConfiguration.setSelectedModule(module);

        return runnerAndConfigurationSettings;
    }
*/

    public boolean isConfigurationByElement(RunConfiguration runConfiguration, Project project, PsiElement psiElement) {
        if (runConfiguration instanceof GroovyRuntimeConfiguration) {
/*
            PsiElement selectedGroovyScript = findGroovyScript(psiElement);
            if (selectedGroovyScript != null) {
                String absolutePathToScript = ((GroovyRuntimeConfiguration) runConfiguration).getAbsolutePathToScript();
                return selectedGroovyScript.getContainingFile().getVirtualFile().getPath().equals(absolutePathToScript);
            }
*/
        }
        return false;
    }

/*
    private PsiClass findGroovyScript(PsiElement psiElement) {
        return null;
    }
*/
}

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

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;

public class EditorAPIFactory implements ApplicationComponent {

    private static final String IRIDA_EDITOR_API = "org.codehaus.groovy.intellij.irida.IridaAPI";

    // ApplicationComponent --------------------------------------------------------------------------------------------

    @NotNull
    public String getComponentName() {
        return "groovy.editorApi.factory";
    }

    public void initComponent() {}

    public void disposeComponent() {}

    // Factory implementation ------------------------------------------------------------------------------------------

    private static interface ClassSelector {
        String getClass(String ideaName, int buildNumber);
    }

    public EditorAPI createEditorAPI(Project project) {
        return (EditorAPI) createEditorAPI(new Class[] { Project.class }, new Object[] { project }, new ClassSelector() {
            public String getClass(String ideaName, int buildNumber) {
                if ((ideaName.toLowerCase().matches(".*irida.*")) || (buildNumber >= 3000)) {
                    return IRIDA_EDITOR_API;
                }
                throw new IllegalArgumentException("Unknown IntelliJ IDEA name and build number combination!");
            }
        });
    }

    private Object createEditorAPI(Class[] parameterTypes, Object[] parameterValues, ClassSelector closure) {
        ApplicationInfo applicationInfo = ApplicationInfo.getInstance();
        String ideaName = applicationInfo.getVersionName();
        String buildNumber = applicationInfo.getBuildNumber();

        try {
            int build = Integer.parseInt(buildNumber);
            Constructor constructor = Class.forName(closure.getClass(ideaName, build)).getConstructor(parameterTypes);
            return constructor.newInstance(parameterValues);
        } catch (NumberFormatException e) {
            throw newFactoryRuntimeException(ideaName, buildNumber, e);
        } catch (Exception e) {
            throw newFactoryRuntimeException(ideaName, buildNumber, e);
        }
    }

    private RuntimeException newFactoryRuntimeException(String ideaName, String buildNumber, Exception e) {
        return new RuntimeException("Could not create editor API for " + ideaName + " build #" + buildNumber, e);
    }
}

/*
 * $Id$
 *
 * Copyright (c) 2004 The Codehaus - http://groovy.codehaus.org
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

import java.lang.reflect.Constructor;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.project.Project;

public class EditorAPIFactory {

    private static final String PALLADA = "org.codehaus.groovy.intellij.pallada.PalladaAPI";
    private static final String IRIDA = "org.codehaus.groovy.intellij.irida.IridaAPI";

    private static final Class[] CONSTRUCTOR_PARAMETER_TYPES = new Class[] { Project.class };

    public EditorAPI getEditorAPI(Project project) {
        ApplicationInfo applicationInfo = ApplicationInfo.getInstance();
        String ideaName = applicationInfo.getVersionName();
        String buildNumber = applicationInfo.getBuildNumber();

        System.out.print("[Groovy IDEA Integration] ideaName = " + ideaName);
        System.out.print("; buildNumber = " + buildNumber);
        System.out.print("; majorVersion = " + applicationInfo.getMajorVersion());
        System.out.println("; minorVersion = " + applicationInfo.getMinorVersion());

        try {
            int build = Integer.parseInt(buildNumber);
            Constructor constructor = Class.forName(getClassForIDEA(ideaName, build)).getConstructor(CONSTRUCTOR_PARAMETER_TYPES);
            return (EditorAPI) constructor.newInstance(new Object[] { project });
        } catch (NumberFormatException e) {
            throw newFactoryRuntimeException(ideaName, buildNumber, e);
        } catch (Exception e) {
            throw newFactoryRuntimeException(ideaName, buildNumber, e);
        }
    }

    private String getClassForIDEA(String ideaName, int buildNumber) {
        if ((ideaName.toLowerCase().matches(".*pallada.*")) || ((buildNumber >= 1200) && (buildNumber < 3000))) {
            return PALLADA;
        }
        if ((ideaName.toLowerCase().matches(".*irida.*")) || (buildNumber >= 3000)) {
            return IRIDA;
        }
        throw new RuntimeException("Could not load API connector for " + ideaName + "build #" + buildNumber);
    }

    private RuntimeException newFactoryRuntimeException(String ideaName, String buildNumber, Exception e) {
        return new RuntimeException("Could not load API connector for " + ideaName + " build #" + buildNumber, e);
    }
}

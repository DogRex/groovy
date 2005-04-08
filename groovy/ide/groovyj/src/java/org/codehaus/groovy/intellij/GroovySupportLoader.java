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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.options.colors.ColorSettingsPages;

import org.codehaus.groovy.intellij.language.GroovyLanguage;
import org.codehaus.groovy.intellij.language.editor.GroovyColourSettingsPage;

public class GroovySupportLoader implements ApplicationComponent {

    public static final FileType GROOVY = new GroovyFileType(GroovyLanguage.findOrCreate());

    private static final String[] DEFAULT_ASSOCIATED_EXTENSIONS = new String[] { "groovy", "gvy", "gy", "gsh" };

    public String getComponentName() {
        return "groovy.support.loader";
    }

    public void initComponent() {
        System.setProperty("groovy.jsr", "true");

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            public void run() {
                FileTypeManager.getInstance().registerFileType(GROOVY, DEFAULT_ASSOCIATED_EXTENSIONS);
            }
        });

        ColorSettingsPages.getInstance().registerPage(new GroovyColourSettingsPage());
    }

    public void disposeComponent() {
        System.getProperties().remove("groovy.jsr");
    }
}

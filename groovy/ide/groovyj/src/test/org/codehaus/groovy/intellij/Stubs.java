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

import javax.swing.Icon;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.FileTypeSupportCapabilities;
import com.intellij.openapi.fileTypes.LanguageFileType;

public final class Stubs {

    public static final TextAttributes TEXT_ATTRIBUTES = new TextAttributes();

    public static final LanguageFileType LANGUAGE_FILE_TYPE = new LanguageFileType(Language.ANY) {
        public String getName() {
            return null;
        }

        public String getDescription() {
            return null;
        }

        public String getDefaultExtension() {
            return null;
        }

        public Icon getIcon() {
            return null;
        }

        public FileTypeSupportCapabilities getSupportCapabilities() {
            return null;
        }
    };

    /** Sole constructor hidden to enfore non-instantiability. */
    private Stubs() { }
}

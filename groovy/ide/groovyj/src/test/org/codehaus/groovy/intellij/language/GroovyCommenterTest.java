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


package org.codehaus.groovy.intellij.language;

import org.codehaus.groovy.intellij.GroovyjTestCase;

public class GroovyCommenterTest extends GroovyjTestCase {

    private final GroovyCommenter commenter = new GroovyCommenter();

    public void testDefinesTheLineCommentPrefixForGroovy() {
        assertEquals("line comment prefix", "//", commenter.getLineCommentPrefix());
    }

    public void testDefinesThatALineCommentPrefixIsNeverOnTheZeroColumn() {
        assertEquals("line comment prefix on zero column?", false, commenter.isLineCommentPrefixOnZeroColumn());
    }

    public void testDefinesTheBlockCommentPrefixForGroovy() {
        assertEquals("block comment prefix", "/*", commenter.getBlockCommentPrefix());
    }

    public void testDefinesTheBlockCommentSuffixForGroovy() {
        assertEquals("block comment suffix", "*/", commenter.getBlockCommentSuffix());
    }
}

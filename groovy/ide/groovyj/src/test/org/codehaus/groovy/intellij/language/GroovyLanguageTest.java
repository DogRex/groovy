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

import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;

import org.jmock.cglib.MockObjectTestCase;

public class GroovyLanguageTest extends MockObjectTestCase {

    private final GroovyLanguage language = GroovyLanguage.createLanguage();

    public void testHasItsIdSetToGroovy() {
        assertEquals("Language: Groovy", language.toString());
    }

    public void testDoesNotHaveAParserDefinitionYet() {
        assertEquals("parser definition", null, language.getParserDefinition());
    }

    public void testDoesNotProduceProjectSpecificSyntaxHighlightersYet() {
        assertEquals("project syntax highlighter", null, language.getSyntaxHighlighter((Project) mock(Project.class).proxy()));
    }

    public void testDoesNotProduceWordScannersYet() {
        assertEquals("word scanner", null, language.getWordsScanner());
    }

    public void testDoesNotFindReferencesToTokensYet() {
        assertEquals("token references", false, language.mayHaveReferences(new IElementType(null, null)));
    }

    public void testDoesNotProduceFoldingBuildersYet() {
        assertEquals("folding builder", null, language.getFoldingBuilder());
    }

    public void testDoesNotProduceFormattersYet() {
        assertEquals("formatter", null, language.getFormatter());
    }

    public void testDoesNotProducePairedBraceMatchersYet() {
        assertEquals("paired brace matcher", null, language.getPairedBraceMatcher());
    }

    public void testDoesNotProduceAnnotatorsYet() {
        assertEquals("annotator", null, language.getAnnotator());
    }
}

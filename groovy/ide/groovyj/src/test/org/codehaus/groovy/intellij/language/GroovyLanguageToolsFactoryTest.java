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

import junit.framework.TestCase;
import junitx.framework.ObjectAssert;

public class GroovyLanguageToolsFactoryTest extends TestCase {

    private final GroovyLanguageToolsFactory languageToolsFactory = new GroovyLanguageToolsFactory();

    public void testCreatesALexerForGroovy() {
        ObjectAssert.assertInstanceOf("lexer", GroovyLexerAdapter.class, languageToolsFactory.createLexer());
    }

    public void testCreatesAPsiParserForGroovy() {
        ObjectAssert.assertInstanceOf("PSI parser", GroovyPsiParser.class, languageToolsFactory.createParser());
    }
}

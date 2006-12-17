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


package org.codehaus.groovy.intellij.language;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;

import org.codehaus.groovy.intellij.language.parser.GroovyTokenTypes;
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

public class GroovyPairedBraceMatcher implements PairedBraceMatcher {

    private static final BracePair[] BRACE_PAIRS = new BracePair[] {
        new BracePair('(', GroovyTokenTypeMappings.getType(GroovyTokenTypes.LPAREN), ')', GroovyTokenTypeMappings.getType(GroovyTokenTypes.RPAREN), false),
        new BracePair('[', GroovyTokenTypeMappings.getType(GroovyTokenTypes.LBRACK), ']', GroovyTokenTypeMappings.getType(GroovyTokenTypes.RBRACK), false),
        new BracePair('{', GroovyTokenTypeMappings.getType(GroovyTokenTypes.LCURLY), '}', GroovyTokenTypeMappings.getType(GroovyTokenTypes.RCURLY), true)
    };

    public BracePair[] getPairs() {
        return BRACE_PAIRS;
    }
}
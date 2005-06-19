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

import junitx.framework.Assert;

import com.intellij.lang.BracePair;

import org.codehaus.groovy.intellij.GroovyjTestCase;
import org.codehaus.groovy.intellij.language.parser.GroovyTokenTypes;
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

public class GroovyPairedBraceMatcherTest extends GroovyjTestCase {

    private final BracePair[] actualBracePairs = new GroovyPairedBraceMatcher().getPairs();

    public void testProducesAllKnownGroovyBracePairs() {
        assertContainsBracePair('(', ')', GroovyTokenTypes.LPAREN, GroovyTokenTypes.RPAREN);
        assertContainsBracePair('[', ']', GroovyTokenTypes.LBRACK, GroovyTokenTypes.RBRACK);
        assertContainsBracePair('{', '}', GroovyTokenTypes.LCURLY, GroovyTokenTypes.RCURLY);

        assertEquals("number of brace pairs", 3, actualBracePairs.length);
    }

    private void assertContainsBracePair(char leftBraceCharacter, char rightBraceCharacter, int leftBraceTokenTypeIndex, int rightBraceTokenTypeIndex) {
        BracePair actualBracePair = findBracePair(leftBraceCharacter);
        Assert.assertNotEquals("brace pair", null, actualBracePair);

        assertEquals("left brace", leftBraceCharacter, actualBracePair.getLeftBraceChar());
        assertEquals("right brace", rightBraceCharacter, actualBracePair.getRightBraceChar());

        assertSame("left brace type", GroovyTokenTypeMappings.getType(leftBraceTokenTypeIndex), actualBracePair.getLeftBraceType());
        assertSame("right brace type", GroovyTokenTypeMappings.getType(rightBraceTokenTypeIndex), actualBracePair.getRightBraceType());
    }

    private BracePair findBracePair(char leftBraceCharacter) {
        for (BracePair bracePair : actualBracePairs) {
            if (bracePair.getLeftBraceChar() == leftBraceCharacter) {
                return bracePair;
            }
        }
        return null;
    }
}

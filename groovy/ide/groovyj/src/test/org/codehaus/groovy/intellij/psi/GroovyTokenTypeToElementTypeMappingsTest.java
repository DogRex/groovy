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


package org.codehaus.groovy.intellij.psi;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import junitx.framework.Assert;
import junitx.framework.ListAssert;
import junitx.framework.StringAssert;

import com.intellij.psi.tree.IElementType;

import org.codehaus.groovy.antlr.parser.GroovyTokenTypes;

public class GroovyTokenTypeToElementTypeMappingsTest extends TestCase {

    public void testIncludesTheBadCharacterFromAntlrAsAGroovyElementType() {
        assertEquals("element type index", -1, GroovyTokenTypeToElementTypeMappings.BAD_CHARACTER);

        IElementType elementType = GroovyTokenTypeToElementTypeMappings.getType(GroovyTokenTypeToElementTypeMappings.BAD_CHARACTER);
        StringAssert.assertContains("element type name", "BAD_CHARACTER", elementType.toString());
    }

    public void testHoldsAllGroovyAssignmentTypes() {
        IElementType[] elementTypes = GroovyTokenTypeToElementTypeMappings.getAssignmentTypes();
        assertElementTypesAreCorrectlyDetected("ASSIGN", elementTypes);
    }

    public void testHoldsAllGroovyLiteralTypes() {
        IElementType[] elementTypes = GroovyTokenTypeToElementTypeMappings.getLiteralTypes();
        assertElementTypesAreCorrectlyDetected("LITERAL", elementTypes);
    }

    public void testHoldsAllGroovyCommentTypes() {
        IElementType[] elementTypes = GroovyTokenTypeToElementTypeMappings.getCommentTypes();
        assertElementTypesAreCorrectlyDetected("COMMENT", elementTypes);

        List listOfElementTypes = Arrays.asList(elementTypes);
        ListAssert.assertContains(listOfElementTypes, GroovyTokenTypeToElementTypeMappings.getType(GroovyTokenTypes.SH_COMMENT));
        ListAssert.assertContains(listOfElementTypes, GroovyTokenTypeToElementTypeMappings.getType(GroovyTokenTypes.ML_COMMENT));
        ListAssert.assertContains(listOfElementTypes, GroovyTokenTypeToElementTypeMappings.getType(GroovyTokenTypes.SL_COMMENT));
    }

    private void assertElementTypesAreCorrectlyDetected(String keyword, IElementType[] literalTypes) {
        Assert.assertNotEquals("number of " + keyword.toLowerCase() + " types", 0, literalTypes.length);

        for (int i = 0; i < literalTypes.length; i++) {
            IElementType literalType = literalTypes[i];
            String elementTypeName = literalType.toString();
            StringAssert.assertContains("element type name", keyword, elementTypeName);
        }
    }
}

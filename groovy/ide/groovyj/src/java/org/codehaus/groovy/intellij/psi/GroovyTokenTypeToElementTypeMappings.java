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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.intellij.psi.tree.IElementType;

import org.codehaus.groovy.antlr.parser.GroovyTokenTypes;

import gnu.trove.TIntObjectHashMap;

public final class GroovyTokenTypeToElementTypeMappings {

    /** Internal ANTLR index. */
    public static final int BAD_CHARACTER = -1;

    private static final GroovyTokenTypes GROOVY_TOKEN_TYPES = new GroovyTokenTypes() {};

    private static final TIntObjectHashMap TOKEN_TYPES = new TIntObjectHashMap();

    private static IElementType[] ASSIGNMENT_TYPES = IElementType.EMPTY_ARRAY;
    private static IElementType[] LITERAL_TYPES = IElementType.EMPTY_ARRAY;

    static {
        try {
            loadGroovyTokenTypeMappings();
        } catch (IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        }
        addBadCharacterTokenTypeMapping();
    }

    public static IElementType[] getAssignmentTypes() {
        return ASSIGNMENT_TYPES;
    }

    public static IElementType[] getLiteralTypes() {
        return LITERAL_TYPES;
    }

    public static IElementType getType(int tokenTypeIndex) {
        return (IElementType) TOKEN_TYPES.get(tokenTypeIndex);
    }

    private static void loadGroovyTokenTypeMappings() throws IllegalAccessException {
        Field[] fields = GROOVY_TOKEN_TYPES.getClass().getFields();
        List assignmentTypes = new ArrayList();
        List literalTypes = new ArrayList();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (isPublicAndStatic(field)) {
                addTokenTypeMapping(field, assignmentTypes, literalTypes);
            }
        }

        ASSIGNMENT_TYPES = (IElementType[]) assignmentTypes.toArray(IElementType.EMPTY_ARRAY);
        LITERAL_TYPES = (IElementType[]) literalTypes.toArray(IElementType.EMPTY_ARRAY);
    }

    private static boolean isPublicAndStatic(Field field) {
        return Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers());
    }

    private static void addTokenTypeMapping(Field field, List assignmentTypes, List literalTypes) throws IllegalAccessException {
        int tokenTypeIndex = field.getInt(GROOVY_TOKEN_TYPES);
        String tokenTypeName = field.getName();
        IElementType elementType = new GroovyElementType(tokenTypeName);

        TOKEN_TYPES.put(tokenTypeIndex, elementType);

        if (tokenTypeName != null && tokenTypeName.endsWith("ASSIGN")) {
            assignmentTypes.add(elementType);
        }

        if (tokenTypeName != null && tokenTypeName.startsWith("LITERAL_")) {
            literalTypes.add(elementType);
        }
    }

    private static void addBadCharacterTokenTypeMapping() {
        TOKEN_TYPES.put(BAD_CHARACTER, new GroovyElementType("BAD_CHARACTER"));
    }
}

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

import com.intellij.psi.tree.TokenSet;

import org.codehaus.groovy.intellij.language.parser.GroovyTokenTypes;

public final class GroovyTokenSets {

    public static final TokenSet KEYWORDS = TokenSet.create(GroovyTokenTypeMappings.getLiteralTypes());

    public static final TokenSet ASSIGNMENT_OPERATIONS = TokenSet.create(GroovyTokenTypeMappings.getAssignmentTypes());

    public static final TokenSet OPERATIONS = TokenSet.create(
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.QUESTION),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.COLON),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.COMMA),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.COMPARE_TO),           // <=>
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.CLOSURE_OP),           // ->
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.LNOT),                 // !    logical not
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.BNOT),                 // ~    binary not
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.DIV),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.PLUS),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.INC),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.MINUS),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.DEC),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.STAR),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.MOD),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.SR),                   // >>   shift right
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.BSR),                  // >>>  binary shift right
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.GE),                   // >=   greater or equal
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.GT),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.SL),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.LE),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.LT),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.BXOR),                 // ^    binary xor
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.BOR),                  // |    binary or
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.BAND),                 // &    binary and
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.LAND),                 // &&   logical and
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.LOR),                  // ||   logical or
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.RANGE_INCLUSIVE),      // ..
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.RANGE_EXCLUSIVE),      // ..<
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.TRIPLE_DOT),           // ...
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.DOT),                  // .
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.SPREAD_DOT),           // *.   as in b*.a
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.OPTIONAL_DOT),         // ?.
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.MEMBER_POINTER),       // .&
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.REGEX_FIND),           // =~
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.REGEX_MATCH),          // ==~
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.STAR_STAR),            // **
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.POST_INC),             // todo: add to groovy.g comment
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.POST_DEC),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.DOLLAR)                // scope escape
    );

    public static final TokenSet EQUALITY_OPERATIONS = TokenSet.create(
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.EQUAL),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.NOT_EQUAL),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.COMPARE_TO)
    );

    public static final TokenSet RELATIONAL_OPERATIONS = TokenSet.create(
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.LT),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.GT),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.LE),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.GE)

        // same precedence level as above, but with a different meaning
//        GroovyTokenTypeMappings.getType(GroovyTokenTypes.LITERAL_instanceof),
//        GroovyTokenTypeMappings.getType(GroovyTokenTypes.LITERAL_in),
//        GroovyTokenTypeMappings.getType(GroovyTokenTypes.LITERAL_as)
    );

    public static final TokenSet ADDITIVE_OPERATIONS = TokenSet.create(
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.PLUS),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.MINUS)
    );

    public static final TokenSet MULTIPLICATIVE_OPERATIONS = TokenSet.create(
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.STAR),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.DIV),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.MOD)
    );

    public static final TokenSet SHIFT_OPERATIONS = TokenSet.create(
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.SL),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.SR),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.BSR)
    );

    public static final TokenSet UNARY_OPERATIONS = TokenSet.create(
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.PLUS),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.MINUS),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.INC),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.DEC),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.DOLLAR)            // scope escape
    );

    public static final TokenSet NUMBERS = TokenSet.create(
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.NUM_INT),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.NUM_FLOAT),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.NUM_LONG),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.NUM_DOUBLE),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.NUM_BIG_INT),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.NUM_BIG_DECIMAL)
    );

    public static final TokenSet PARENTHESES = TokenSet.create(
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.LPAREN),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.RPAREN)
    );

    public static final TokenSet BRACKETS = TokenSet.create(
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.LBRACK),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.RBRACK)
    );

    public static final TokenSet BRACES = TokenSet.create(
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.LCURLY),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.RCURLY)
    );

    public static final TokenSet WHITESPACES = TokenSet.create(
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.WS),
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.NLS)
    );

    public static final TokenSet IDENTIFIERS = TokenSet.create(
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.IDENT)
    );

    public static final TokenSet STRING_LITERALS = TokenSet.create(
        GroovyTokenTypeMappings.getType(GroovyTokenTypes.STRING_LITERAL)
    );

    public static final TokenSet COMMENTS = TokenSet.create(
        GroovyTokenTypeMappings.getCommentTypes()
    );
}

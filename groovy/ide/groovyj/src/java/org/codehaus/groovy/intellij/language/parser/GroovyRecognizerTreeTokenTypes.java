// $ANTLR 2.7.5 (20050128): "GroovyRecognizerTree.patched.g" -> "GroovyRecognizerTree.java"$

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


package org.codehaus.groovy.intellij.language.parser;

public interface GroovyRecognizerTreeTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int SH_COMMENT = 4;
	int PACKAGE_DEF = 5;
	int STATIC_IMPORT = 6;
	int IMPORT = 7;
	int LITERAL_def = 8;
	int AT = 9;
	int IDENT = 10;
	int LBRACK = 11;
	int RBRACK = 12;
	int DOT = 13;
	int LITERAL_class = 14;
	int LITERAL_interface = 15;
	int LITERAL_enum = 16;
	int TYPE_ARGUMENT = 17;
	int WILDCARD_TYPE = 18;
	int TYPE_ARGUMENTS = 19;
	int LITERAL_extends = 20;
	int LITERAL_super = 21;
	int LITERAL_void = 22;
	int LITERAL_boolean = 23;
	int LITERAL_byte = 24;
	int LITERAL_char = 25;
	int LITERAL_short = 26;
	int LITERAL_int = 27;
	int LITERAL_float = 28;
	int LITERAL_long = 29;
	int LITERAL_double = 30;
	int LITERAL_any = 31;
	int LITERAL_as = 32;
	int MODIFIERS = 33;
	int LITERAL_private = 34;
	int LITERAL_public = 35;
	int LITERAL_protected = 36;
	int LITERAL_static = 37;
	int LITERAL_transient = 38;
	int LITERAL_final = 39;
	int LITERAL_abstract = 40;
	int LITERAL_native = 41;
	int LITERAL_threadsafe = 42;
	int LITERAL_synchronized = 43;
	int LITERAL_volatile = 44;
	int LITERAL_strictfp = 45;
	int ANNOTATION = 46;
	int ANNOTATIONS = 47;
	int ANNOTATION_MEMBER_VALUE_PAIR = 48;
	int EXTENDS_CLAUSE = 49;
	int CLASS_DEF = 50;
	int INTERFACE_DEF = 51;
	int ENUM_DEF = 52;
	int ANNOTATION_DEF = 53;
	int TYPE_PARAMETERS = 54;
	int TYPE_PARAMETER = 55;
	int TYPE_UPPER_BOUNDS = 56;
	int OBJBLOCK = 57;
	int ANNOTATION_FIELD_DEF = 58;
	int LITERAL_default = 59;
	int ENUM_CONSTANT_DEF = 60;
	int METHOD_DEF = 61;
	int INSTANCE_INIT = 62;
	int IMPLEMENTS_CLAUSE = 63;
	int SLIST = 64;
	int CTOR_CALL = 65;
	int SUPER_CTOR_CALL = 66;
	int CTOR_IDENT = 67;
	int VARIABLE_DEF = 68;
	int ARRAY_DECLARATOR = 69;
	int ASSIGN = 70;
	int PARAMETER_DEF = 71;
	int LITERAL_throws = 72;
	int PARAMETERS = 73;
	int VARIABLE_PARAMETER_DEF = 74;
	int TRIPLE_DOT = 75;
	int CLOSED_BLOCK = 76;
	int LCURLY = 77;
	int LITERAL_while = 78;
	int LITERAL_with = 79;
	int SPREAD_ARG = 80;
	int LITERAL_for = 81;
	int FOR_IN_ITERABLE = 82;
	int LITERAL_break = 83;
	int LITERAL_continue = 84;
	int LITERAL_throw = 85;
	int LITERAL_assert = 86;
	int LITERAL_return = 87;
	int LABELED_STAT = 88;
	int EXPR = 89;
	int CASE_GROUP = 90;
	int LITERAL_case = 91;
	int FOR_INIT = 92;
	int FOR_CONDITION = 93;
	int FOR_ITERATOR = 94;
	int LITERAL_try = 95;
	int LITERAL_finally = 96;
	int LITERAL_catch = 97;
	int METHOD_CALL = 98;
	int ELIST = 99;
	int SPREAD_DOT = 100;
	int OPTIONAL_DOT = 101;
	int MEMBER_POINTER = 102;
	int STRING_LITERAL = 103;
	int LITERAL_in = 104;
	int LITERAL_if = 105;
	int LITERAL_else = 106;
	int LITERAL_do = 107;
	int LITERAL_switch = 108;
	int DYNAMIC_MEMBER = 109;
	int INDEX_OP = 110;
	int PLUS_ASSIGN = 111;
	int MINUS_ASSIGN = 112;
	int STAR_ASSIGN = 113;
	int DIV_ASSIGN = 114;
	int MOD_ASSIGN = 115;
	int SR_ASSIGN = 116;
	int BSR_ASSIGN = 117;
	int SL_ASSIGN = 118;
	int BAND_ASSIGN = 119;
	int BXOR_ASSIGN = 120;
	int BOR_ASSIGN = 121;
	int STAR_STAR_ASSIGN = 122;
	int QUESTION = 123;
	int LOR = 124;
	int LAND = 125;
	int BOR = 126;
	int BXOR = 127;
	int BAND = 128;
	int REGEX_FIND = 129;
	int REGEX_MATCH = 130;
	int NOT_EQUAL = 131;
	int EQUAL = 132;
	int COMPARE_TO = 133;
	int LT = 134;
	int GT = 135;
	int LE = 136;
	int GE = 137;
	int LITERAL_instanceof = 138;
	int SL = 139;
	int SR = 140;
	int BSR = 141;
	int RANGE_INCLUSIVE = 142;
	int RANGE_EXCLUSIVE = 143;
	int PLUS = 144;
	int MINUS = 145;
	int INC = 146;
	int STAR = 147;
	int DIV = 148;
	int MOD = 149;
	int DEC = 150;
	int STAR_STAR = 151;
	int UNARY_MINUS = 152;
	int UNARY_PLUS = 153;
	int MEMBER_POINTER_DEFAULT = 154;
	int BNOT = 155;
	int LNOT = 156;
	int TYPECAST = 157;
	int POST_INC = 158;
	int POST_DEC = 159;
	int LITERAL_this = 160;
	int SCOPE_ESCAPE = 161;
	int STRING_CTOR_START = 162;
	int STRING_CTOR_MIDDLE = 163;
	int STRING_CTOR_END = 164;
	int MAP_CONSTRUCTOR = 165;
	int LIST_CONSTRUCTOR = 166;
	int COLON = 167;
	int LITERAL_new = 168;
	int LABELED_ARG = 169;
	int SPREAD_MAP_ARG = 170;
	int LITERAL_true = 171;
	int LITERAL_false = 172;
	int LITERAL_null = 173;
	int NUM_INT = 174;
	int NUM_FLOAT = 175;
	int NUM_LONG = 176;
	int NUM_DOUBLE = 177;
	int NUM_BIG_INT = 178;
	int NUM_BIG_DECIMAL = 179;
	int LPAREN = 180;
	int RPAREN = 181;
	int RCURLY = 182;
	int NLS = 183;
}

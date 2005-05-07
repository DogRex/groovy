// ANTLR generated tree grammar

header {
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
}

class GroovyRecognizerTree extends TreeParser;
options {
	defaultErrorHandler = false;
	buildAST = true;
}

compilationUnit
	:
	(
		SH_COMMENT
	)?
	nls
	(
		packageDefinition
	)?
	(
		statement
	)*
	;

snippetUnit
	:
	blockBody
	;

packageDefinition
	:
	#( PACKAGE_DEF annotationsOpt identifier )
	;

importStatement
	:
		#( STATIC_IMPORT identifierStar )
	|
		#( IMPORT identifierStar )
	;

typeDefinitionInternal
	:
		classDefinition
	|
		interfaceDefinition
	|
		enumDefinition
	|
		annotationDefinition
	;

declaration
	:
	(
		modifiers
	)?
	typeSpec
	(
		variableDefinition
	)+
	;

singleDeclaration
	:
	singleVariable
	varInitializer
	;

declarationStart
	:
		"def"
	|
		modifier
	|
		AT
		IDENT
	|
		(
				builtInType
			|
				qualifiedTypeName
		)
		(
			LBRACK
			balancedTokens
			RBRACK
		)*
		IDENT
	;

qualifiedTypeName
	:
	IDENT
	(
		DOT
		IDENT
	)*
	;

constructorStart
	:
	modifiersOpt
	IDENT
	;

typeDeclarationStart
	:
	modifiersOpt
	(
		"class"
		|
			"interface"
		|
			"enum"
		|
			AT
			"interface"
	)
	;

typeSpec
	:
	classTypeSpec
	|
		builtInTypeSpec
	;

classTypeSpec
	:
	classOrInterfaceType
	declaratorBrackets
	;

classOrInterfaceType0a
	:
		#( IDENT (typeArguments)? )
	|
		#( DOT classOrInterfaceType0a IDENT (typeArguments)? )
	;

classOrInterfaceType
	:
	classOrInterfaceType0a
	;

typeArgumentSpec
	:
	classTypeSpec
	|
		builtInTypeArraySpec
	;

typeArgument
	:
	#(
		TYPE_ARGUMENT
		(
			typeArgumentSpec
				|
			wildcardType
		)
	)
	;

wildcardType
	:
	#( WILDCARD_TYPE (typeArgumentBounds)? )
	;

typeArguments
	:
	#( TYPE_ARGUMENTS (typeArgument)+ )
	;

typeArgumentBounds
	:
	(
			"extends"
		|
			"super"
	)
	classOrInterfaceType
	;

builtInTypeArraySpec
	:
	builtInType
	(
		declaratorBrackets
/*
		|
			// add warning?
*/
	)
	;

builtInTypeSpec
	:
	builtInType
	declaratorBrackets
	;

type
	:
		classOrInterfaceType
	|
		builtInType
	;

builtInType
	:
		"void"
	|
		"boolean"
	|
		"byte"
	|
		"char"
	|
		"short"
	|
		"int"
	|
		"float"
	|
		"long"
	|
		"double"
	|
		"any"
	;

identifier0a
	:
	IDENT
	|
		#(
			DOT
			identifier0a
			IDENT
		)
	;

identifier
	:
	#(
		DOT
		identifier0a
		IDENT
	)
	|
		IDENT
	;

identifierStar0a
	:
		IDENT
	|
		#( DOT identifierStar0a IDENT )
	;

identifierStar
	:
		identifierStar0a
	|
		#( "as" identifierStar0a IDENT )
	;

modifiersInternal
	:
	(
			"def"
		|
			modifier
		|
			annotation
	)+
	;

modifiers
	:
	#( MODIFIERS modifiersInternal )
	;

modifiersOpt
	:
	#( MODIFIERS (modifiersInternal)? )
	;

modifier
	:
		"private"
	|
		"public"
	|
		"protected"
	|
		"static"
	|
		"transient"
	|
		"final"
	|
		"abstract"
	|
		"native"
	|
		"threadsafe"
	|
		"synchronized"
	|
		"volatile"
	|
		"strictfp"
	;

annotation
	:
	#( ANNOTATION identifier (annotationArguments)? )
	;

annotationsOpt
	:
	#( ANNOTATIONS (annotation)* )
	;

annotationArguments
	:
	annotationMemberValueInitializer
	|
		anntotationMemberValuePairs
	;

anntotationMemberValuePairs
	:
	(
		annotationMemberValuePair
	)+
	;

annotationMemberValuePair
	:
	#( ANNOTATION_MEMBER_VALUE_PAIR IDENT annotationMemberValueInitializer )
	;

annotationMemberValueInitializer
	:
	conditionalExpression
	|
		annotation
	;

annotationMemberArrayValueInitializer
	:
	conditionalExpression
	|
		annotation
	;

superClassClause
	:
	#( EXTENDS_CLAUSE classOrInterfaceType )
	;

classDefinition
	:
	#( CLASS_DEF modifiers IDENT (typeParameters)? superClassClause implementsClause classBlock )
	;

interfaceDefinition
	:
	#( INTERFACE_DEF modifiers IDENT (typeParameters)? interfaceExtends interfaceBlock )
	;

enumDefinition
	:
	#(ENUM_DEF modifiers IDENT implementsClause enumBlock )
	;

annotationDefinition
	:
	#(ANNOTATION_DEF modifiers IDENT annotationBlock )
	;

typeParameters
	:
	#( TYPE_PARAMETERS (typeParameter)+ )
	;

typeParameter
	:
	#( TYPE_PARAMETER IDENT (typeParameterBounds)? )
	;

typeParameterBounds
	:
	#( TYPE_UPPER_BOUNDS (classOrInterfaceType)+ )
	;

classBlock
	:
	#( OBJBLOCK (classField)* )
	;

interfaceBlock
	:
	#( OBJBLOCK (interfaceField)* )
	;

annotationBlock
	:
	#( OBJBLOCK (annotationField)* )
	;

enumBlock
	:
	#( OBJBLOCK (enumConstants)? (classField)* )
	;

enumConstants
	:
	(
		enumConstant
	)+
	;

annotationField
	:
		typeDefinitionInternal
	|
		#( ANNOTATION_FIELD_DEF modifiersOpt typeSpec IDENT ( "default" annotationMemberValueInitializer )? )
	|
		variableDefinition
	;

enumConstant
	:
	#( ENUM_CONSTANT_DEF annotationsOpt IDENT argList (enumConstantBlock)? )
	;

enumConstantBlock
	:
	#( OBJBLOCK (enumConstantField)* )
	;

enumConstantField
	:
		typeDefinitionInternal
	|
		(
			options {
				generateAmbigWarnings = false;
			}
		:
			#(
				METHOD_DEF
				modifiersOpt
				(
					typeParameters
				)?
				typeSpec
				IDENT
				parameterDeclarationList
				(
					throwsClause
				)?
				(
					compoundStatement
				)?
			)
		|
			variableDefinition
		)
	|
		#( INSTANCE_INIT compoundStatement )
	;

interfaceExtends
	:
	#( EXTENDS_CLAUSE (classOrInterfaceType)* )
	;

implementsClause
	:
	#( IMPLEMENTS_CLAUSE (classOrInterfaceType)* )
	;

classField
	:
		constructorDefinition
	|
		declaration
	|
		typeDefinitionInternal
	|
		"static"
		compoundStatement
	|
		compoundStatement
	;

interfaceField
	:
		declaration
	|
		typeDefinitionInternal
	;

constructorBody
	:
	#( SLIST explicitConstructorInvocation blockBody )
	;

explicitConstructorInvocation
	:
		#( CTOR_CALL (typeArguments)? argList )
	|
		#( SUPER_CTOR_CALL (typeArguments)? argList )
	;

constructorDefinition
	:
	#( CTOR_IDENT IDENT parameterDeclarationList (throwsClause)? constructorBody )
	;

variableDefinition
	:
	#( VARIABLE_DEF modifiers typeSpec variableDeclarator varInitializer )
	;

variableDeclarator
	:
		IDENT
	|
		LBRACK variableDeclarator
	;

singleVariable
	:
	#( VARIABLE_DEF modifiers typeSpec IDENT )
	;

declaratorBrackets0a
	:
	#( ARRAY_DECLARATOR declaratorBrackets0a )
	;

declaratorBrackets
	:
	declaratorBrackets0a
	;

varInitializer
	:
	#( ASSIGN expression )
	;

parameterDefinition
	:
	#( PARAMETER_DEF modifiers typeSpec IDENT )
	;

throwsClause
	:
	#( "throws" (identifier)+ )
	;

parameterDeclarationList
	:
	#( PARAMETERS (parameterDeclaration)* )
	;

parameterDeclaration
	:
		#( VARIABLE_PARAMETER_DEF parameterModifiersOpt typeSpec (TRIPLE_DOT)? IDENT varInitializer )
	|
		#( PARAMETER_DEF parameterModifiersOpt typeSpec (TRIPLE_DOT)? IDENT varInitializer )
	;

simpleParameterDeclaration
	:
	#( PARAMETER_DEF modifiers typeSpec IDENT )
	;

simpleParameterDeclarationList
	:
	#( PARAMETERS (simpleParameterDeclaration)+ )
	;

parameterModifiersOpt
	:
	#(
		MODIFIERS
		(
			"def"
		|
			"final"
		|
			annotation
		)*
	)
	;

closureParametersOpt
	:
		parameterDeclarationList
//	|
//		oldClosureParameters
	;

/*
oldClosureParameters
	:
		parameterDeclarationList
	|
		simpleParameterDeclarationList
	;
*/

closureParameter
	:
	#( PARAMETER_DEF IDENT )
	;

compoundStatement
	:
	openBlock
	;

openBlock
	:
	#( SLIST blockBody )
	;

blockBody
	:
	(
		statement
	)*
	;

closedBlock
	:
	#( CLOSED_BLOCK closureParametersOpt blockBody )
	;

openOrClosedBlock
	:
	#( LCURLY closureParametersOpt blockBody )
	;

statementList
	:
	#( SLIST (statement)* )
	;

statement
	:   openOrClosedBlock
	|
		expressionStatement
	|
		typeDefinitionInternal
	|
		forStatement
	|
		#( "while" strictContextExpression compatibleBodyStatement )
	|
		#( "with" strictContextExpression compoundStatement )
	|
		#( SPREAD_ARG expressionStatement )
	|
		importStatement
	|
		tryBlock
	|
		#( "synchronized" strictContextExpression compoundStatement )
	|
		branchStatement
	|
		declaration
	;

forStatement
	:
	#(
		"for"
		(
				traditionalForClause
			|
				forInClause
		)
		compatibleBodyStatement
	)
	;

traditionalForClause
	:
	forInit forCond forIter
	;

forInClause
	:
	#( FOR_IN_ITERABLE variableDefinition expression )
	;

compatibleBodyStatement
	:
		compoundStatement
	|
		statement
	;

branchStatement
	:
		#( "break" (statementLabelPrefix)? (expression)? )
	|
		#( "continue" (statementLabelPrefix)? (expression)? )
	|
		#( "throw" expression )
	|
		#( "assert" expression (expression)? )
	|
		#( "return" (expression)? )
	;

statementLabelPrefix
	:
	#( LABELED_STAT IDENT )
	;

expressionStatement
	:
	#( EXPR expression (commandArguments)? )
	;

casesGroup
	:
	#( CASE_GROUP (aCase)+ caseSList )
	;

aCase
	:
		#( "case" expression )
	|
		"default"
	;

caseSList
	:
	#( SLIST (statement)+ )
	;

forInit
	:
	#( FOR_INIT ( declaration | (controlExpressionList)? ) )
	;

forCond
	:
	#( FOR_CONDITION (strictContextExpression)? )
	;

forIter
	:
	#( FOR_ITERATOR (controlExpressionList)? )
	;

tryBlock
	:
	#( "try" compoundStatement (handler)* (finallyClause)? )
	;

finallyClause
	:
	#( "finally" compoundStatement )
	;

handler
	:
	#( "catch" parameterDeclaration compoundStatement )
	;

commandArguments
	:
	#( METHOD_CALL #( ELIST (expression)+ ) )
	;

expression
	:
	assignmentExpression
	;

controlExpressionList
	:
	#( ELIST (strictContextExpression)+ )
	;

pathExpression
	:
	primaryExpression
	(
		pathElement
	)*
	;

pathElement
	:
		methodCallArgs
	|
		appendedBlock
	|
		indexPropertyArgs
	|
		#( SPREAD_DOT (typeArguments)? namePart )
	|
		#( OPTIONAL_DOT (typeArguments)? namePart )
	|
		#( MEMBER_POINTER (typeArguments)? namePart )
	|
		#( DOT (typeArguments)? namePart )
	;

namePart
	:
		#(
			AT
			(
					IDENT
				|
					STRING_LITERAL
				|
					dynamicMemberName
				|
					openBlock
				|
					keywordPropertyNames
			)
		)
	|
		(
			IDENT
			|
				STRING_LITERAL
			|
				dynamicMemberName
			|
				openBlock
			|
				keywordPropertyNames
		)
	;

keywordPropertyNames
	:
	(
		"class"
		|
			"in"
		|
			"as"
		|
			"def"
		|
			"if"
		|
			"else"
		|
			"for"
		|
			"while"
		|
			"do"
		|
			"switch"
		|
			"try"
		|
			"catch"
		|
			"finally"
		|
			builtInType
	)
	;

dynamicMemberName
	:
	#( DYNAMIC_MEMBER ( parenthesizedExpression | stringConstructorExpression ) )
	;

methodCallArgs
	:
	#( METHOD_CALL argList )
	;

appendedBlock
	:
	closedBlock
	;

indexPropertyArgs
	:
	#( INDEX_OP argList )
	;

assignmentExpression
	:
		#( ASSIGN conditionalExpression assignmentExpression )
	|
		#( PLUS_ASSIGN conditionalExpression assignmentExpression )
	|
		#( MINUS_ASSIGN conditionalExpression assignmentExpression )
	|
		#( STAR_ASSIGN conditionalExpression assignmentExpression )
	|
		#( DIV_ASSIGN conditionalExpression assignmentExpression )
	|
		#( MOD_ASSIGN conditionalExpression assignmentExpression )
	|
		#( SR_ASSIGN conditionalExpression assignmentExpression )
	|
		#( BSR_ASSIGN conditionalExpression assignmentExpression )
	|
		#( SL_ASSIGN conditionalExpression assignmentExpression )
	|
		#( BAND_ASSIGN conditionalExpression assignmentExpression )
	|
		#( BXOR_ASSIGN conditionalExpression assignmentExpression )
	|
		#( BOR_ASSIGN conditionalExpression assignmentExpression )
	|
		#( STAR_STAR_ASSIGN conditionalExpression assignmentExpression )
	|
		conditionalExpression
	;

conditionalExpression
	:
		#( QUESTION logicalOrExpression assignmentExpression conditionalExpression )
	|
		logicalOrExpression
	;

logicalOrExpression0a
	:
		logicalAndExpression
	|
		#(
			LOR
			logicalOrExpression0a
			logicalAndExpression
		)
	;

logicalOrExpression
	:
		#(
			LOR
			logicalOrExpression0a
			logicalAndExpression
		)
	|
		logicalAndExpression
	;

logicalAndExpression0a
	:
		inclusiveOrExpression
	|
		#(
			LAND
			logicalAndExpression0a
			inclusiveOrExpression
		)
	;

logicalAndExpression
	:
		#(
			LAND
			logicalAndExpression0a
			inclusiveOrExpression
		)
	|
		inclusiveOrExpression
	;

inclusiveOrExpression0a
	:
		exclusiveOrExpression
	|
		#(
			BOR
			inclusiveOrExpression0a
			exclusiveOrExpression
		)
	;

inclusiveOrExpression
	:
		#(
			BOR
			inclusiveOrExpression0a
			exclusiveOrExpression
		)
	|
		exclusiveOrExpression
	;

exclusiveOrExpression0a
	:
		andExpression
	|
		#(
			BXOR
			exclusiveOrExpression0a
			andExpression
		)
	;

exclusiveOrExpression
	:
		#(
			BXOR
			exclusiveOrExpression0a
			andExpression
		)
	|
		andExpression
	;

andExpression0a
	:
		regexExpression
	|
		#(
			BAND
			andExpression0a
			regexExpression
		)
	;

andExpression
	:
		#(
			BAND
			andExpression0a
			regexExpression
		)
	|
		regexExpression
	;

regexExpression0a
	:
		equalityExpression
	|
		#(
			REGEX_FIND
			regexExpression0a
			equalityExpression
		)
	|
		#(
			REGEX_MATCH
			regexExpression0b
			equalityExpression
		)
	;

regexExpression0b
	:
		equalityExpression
	|
		#(
			REGEX_FIND
			regexExpression0a
			equalityExpression
		)
	|
		#(
			REGEX_MATCH
			regexExpression0b
			equalityExpression
		)
	;

regexExpression
	:
		#(
			REGEX_FIND
			regexExpression0a
			equalityExpression
		)
	|
		#(
			REGEX_MATCH
			regexExpression0b
			equalityExpression
		)
	|
		equalityExpression
	;

equalityExpression0a
	:
		relationalExpression
	|
		#(
			NOT_EQUAL
			equalityExpression0a
			relationalExpression
		)
	|
		#(
			EQUAL
			equalityExpression0b
			relationalExpression
		)
	|
		#(
			COMPARE_TO
			equalityExpression0c
			relationalExpression
		)
	;

equalityExpression0b
	:
		relationalExpression
	|
		#(
			NOT_EQUAL
			equalityExpression0a
			relationalExpression
		)
	|
		#(
			EQUAL
			equalityExpression0b
			relationalExpression
		)
	|
		#(
			COMPARE_TO
			equalityExpression0c
			relationalExpression
		)
	;

equalityExpression0c
	:
		relationalExpression
	|
		#(
			NOT_EQUAL
			equalityExpression0a
			relationalExpression
		)
	|
		#(
			EQUAL
			equalityExpression0b
			relationalExpression
		)
	|
		#(
			COMPARE_TO
			equalityExpression0c
			relationalExpression
		)
	;

equalityExpression
	:
		#(
			NOT_EQUAL
			equalityExpression0a
			relationalExpression
		)
	|
		#(
			EQUAL
			equalityExpression0b
			relationalExpression
		)
	|
		#(
			COMPARE_TO
			equalityExpression0c
			relationalExpression
		)
	|
		relationalExpression
	;

relationalExpression
	:
		#( LT shiftExpression shiftExpression )
	|
		#( GT shiftExpression shiftExpression )
	|
		#( LE shiftExpression shiftExpression )
	|
		#( GE shiftExpression shiftExpression )
	|
		#( "in" shiftExpression shiftExpression )
	|
		shiftExpression
	|
		#( "instanceof" shiftExpression typeSpec )
	|
		#( "as" shiftExpression typeSpec )
	;

shiftExpression0a
	:
		additiveExpression
	|
		#(
			SL
			shiftExpression0a
			additiveExpression
		)
	|
		#(
			SR
			shiftExpression0b
			additiveExpression
		)
	|
		#(
			BSR
			shiftExpression0c
			additiveExpression
		)
	|
		#(
			RANGE_INCLUSIVE
			shiftExpression0d
			additiveExpression
		)
	|
		#(
			RANGE_EXCLUSIVE
			shiftExpression0e
			additiveExpression
		)
	;

shiftExpression0b
	:
		additiveExpression
	|
		#(
			SL
			shiftExpression0a
			additiveExpression
		)
	|
		#(
			SR
			shiftExpression0b
			additiveExpression
		)
	|
		#(
			BSR
			shiftExpression0c
			additiveExpression
		)
	|
		#(
			RANGE_INCLUSIVE
			shiftExpression0d
			additiveExpression
		)
	|
		#(
			RANGE_EXCLUSIVE
			shiftExpression0e
			additiveExpression
		)
	;

shiftExpression0c
	:
		additiveExpression
	|
		#(
			SL
			shiftExpression0a
			additiveExpression
		)
	|
		#(
			SR
			shiftExpression0b
			additiveExpression
		)
	|
		#(
			BSR
			shiftExpression0c
			additiveExpression
		)
	|
		#(
			RANGE_INCLUSIVE
			shiftExpression0d
			additiveExpression
		)
	|
		#(
			RANGE_EXCLUSIVE
			shiftExpression0e
			additiveExpression
		)
	;

shiftExpression0d
	:
		additiveExpression
	|
		#(
			SL
			shiftExpression0a
			additiveExpression
		)
	|
		#(
			SR
			shiftExpression0b
			additiveExpression
		)
	|
		#(
			BSR
			shiftExpression0c
			additiveExpression
		)
	|
		#(
			RANGE_INCLUSIVE
			shiftExpression0d
			additiveExpression
		)
	|
		#(
			RANGE_EXCLUSIVE
			shiftExpression0e
			additiveExpression
		)
	;

shiftExpression0e
	:
		additiveExpression
	|
		#(
			SL
			shiftExpression0a
			additiveExpression
		)
	|
		#(
			SR
			shiftExpression0b
			additiveExpression
		)
	|
		#(
			BSR
			shiftExpression0c
			additiveExpression
		)
	|
		#(
			RANGE_INCLUSIVE
			shiftExpression0d
			additiveExpression
		)
	|
		#(
			RANGE_EXCLUSIVE
			shiftExpression0e
			additiveExpression
		)
	;

shiftExpression
	:
		additiveExpression
	|
		#(
			SL
			shiftExpression0a
			additiveExpression
		)
	|
		#(
			SR
			shiftExpression0b
			additiveExpression
		)
	|
		#(
			BSR
			shiftExpression0c
			additiveExpression
		)
	|
		#(
			RANGE_INCLUSIVE
			shiftExpression0d
			additiveExpression
		)
	|
		#(
			RANGE_EXCLUSIVE
			shiftExpression0e
			additiveExpression
		)
	;

additiveExpression0a
	:
		multiplicativeExpression
	|
		#(
			PLUS
			additiveExpression0a
			multiplicativeExpression
		)
	|
		#(
			MINUS
			additiveExpression0b
			multiplicativeExpression
		)
	;

additiveExpression0b
	:
		multiplicativeExpression
	|
		#(
			PLUS
			additiveExpression0a
			multiplicativeExpression
		)
	|
		#(
			MINUS
			additiveExpression0b
			multiplicativeExpression
		)
	;

additiveExpression
	:
		#(
			PLUS
			additiveExpression0a
			multiplicativeExpression
		)
	|
		#(
			MINUS
			additiveExpression0b
			multiplicativeExpression
		)
	|
		multiplicativeExpression
	;

multiplicativeExpression0a
	:
		#( INC powerExpression )
	|
		#(
			STAR
			multiplicativeExpression0a
			powerExpression
		)
	|
		#(
			DIV
			multiplicativeExpression0b
			powerExpression
		)
	|
		#(
			MOD
			multiplicativeExpression0c
			powerExpression
		)
	;

multiplicativeExpression0b
	:
		#( INC powerExpression )
	|
		#(
			STAR
			multiplicativeExpression0a
			powerExpression
		)
	|
		#(
			DIV
			multiplicativeExpression0b
			powerExpression
		)
	|
		#(
			MOD
			multiplicativeExpression0c
			powerExpression
		)
	;

multiplicativeExpression0c
	:
		#( INC powerExpression )
	|
		#(
			STAR
			multiplicativeExpression0a
			powerExpression
		)
	|
		#(
			DIV
			multiplicativeExpression0b
			powerExpression
		)
	|
		#(
			MOD
			multiplicativeExpression0c
			powerExpression
		)
	;

multiplicativeExpression
	:
		#(
			STAR
			multiplicativeExpression0a
			powerExpression
		)
	|
		#(
			DIV
			multiplicativeExpression0b
			powerExpression
		)
	|
		#(
			MOD
			multiplicativeExpression0c
			powerExpression
		)
	|
		#( DEC powerExpression )
	|
		powerExpression
	|
		#( INC powerExpression )
	;

powerExpression0a
	:
		unaryExpressionNotPlusMinus
	|
		#( STAR_STAR powerExpression0a unaryExpression )
	;

powerExpression
	:
		#( STAR_STAR powerExpression0a unaryExpression )
	|
		unaryExpressionNotPlusMinus
	;

unaryExpression
	:
		#( DEC unaryExpression )
	|
		#( UNARY_MINUS unaryExpression )
	|
		#( UNARY_PLUS unaryExpression )
	|
		unaryExpressionNotPlusMinus
	|
		#( INC unaryExpression )
	;

unaryExpressionNotPlusMinus
	:
		#( MEMBER_POINTER_DEFAULT namePart )
	|
		#( BNOT unaryExpression )
	|
		#( LNOT unaryExpression )
	|
		(
			options {
				generateAmbigWarnings = false;
			}
		:
				#( TYPECAST builtInTypeSpec unaryExpression )
			|
				#( TYPECAST classTypeSpec unaryExpressionNotPlusMinus )
		)
	;

postfixExpression
	:
		#( POST_INC pathExpression )
	|
		#( POST_DEC pathExpression )
	|
		pathExpression
	;

primaryExpression
	:
		IDENT
	|
		constant
	|
		newExpression
	|
		"this"
	|
		"super"
	|
		parenthesizedExpression
	|
		closureConstructorExpression
	|
		listOrMapConstructorExpression
	|
		stringConstructorExpression
	|
		scopeEscapeExpression
	|
		builtInType
	;

parenthesizedExpression
	:
	strictContextExpression
	;

scopeEscapeExpression
	:
	#(
		SCOPE_ESCAPE
		(
				IDENT
			|
				scopeEscapeExpression
		)
	)
	;

strictContextExpression
	:
	(
			singleDeclaration
		|
			expression
		|
			branchStatement
		|
			annotation
	)
	;

closureConstructorExpression
	:
	closedBlock
	;

stringConstructorExpression
	:
	STRING_CTOR_START
	stringConstructorValuePart
	(
		STRING_CTOR_MIDDLE
		stringConstructorValuePart
	)*
	STRING_CTOR_END
	;

stringConstructorValuePart
	:
		#(
			SPREAD_ARG
			(
					identifier
				|
					openOrClosedBlock
			)
		)
	|
		(
				identifier
			|
				openOrClosedBlock
		)
	;

listOrMapConstructorExpression
	:
	(
		options {
			generateAmbigWarnings = false;
		}
		:
			#( MAP_CONSTRUCTOR argList )
		|
			#( LIST_CONSTRUCTOR argList )
		|
			#( MAP_CONSTRUCTOR COLON )
	)
	;

newExpression
	:
	#(
		"new"
		(
			typeArguments
		)?
		type
		(
			methodCallArgs
			(
				appendedBlock
			)?
			|
				appendedBlock
			|
				newArrayDeclarator
		)
	)
	;

argList
	:
	(
		argument
	)*
	;

argument
	:
		#( LABELED_ARG argumentLabel strictContextExpression )
	|
		#( SPREAD_ARG strictContextExpression )
	|
		#( SPREAD_MAP_ARG COLON strictContextExpression )
	|
		strictContextExpression
	;

argumentLabel
	:
	primaryExpression
	;

newArrayDeclarator0a
	:
	#( ARRAY_DECLARATOR newArrayDeclarator0a (expression)? )
	;

newArrayDeclarator
	:
	newArrayDeclarator0a
	;

constant
	:
		constantNumber
	|
		STRING_LITERAL
	|
		"true"
	|
		"false"
	|
		"null"
	;

constantNumber
	:
		NUM_INT
	|
		NUM_FLOAT
	|
		NUM_LONG
	|
		NUM_DOUBLE
	|
		NUM_BIG_INT
	|
		NUM_BIG_DECIMAL
	;

balancedBrackets
	:
		LPAREN
		balancedTokens
		RPAREN
	|
		LBRACK
		balancedTokens
		RBRACK
	|
		LCURLY
		balancedTokens
		RCURLY
	|
		STRING_CTOR_START
		balancedTokens
		STRING_CTOR_END
	;

balancedTokens
	:
	(
		balancedBrackets
		|   ~(LPAREN|LBRACK|LCURLY | STRING_CTOR_START
			 |RPAREN|RBRACK|RCURLY | STRING_CTOR_END)
	)*
	;

nls
	:
	(
		NLS
	)?
	;

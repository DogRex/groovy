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

import antlr.ASTPair;
import antlr.NoViableAltException;
import antlr.RecognitionException;
import antlr.collections.AST;
import antlr.collections.impl.BitSet;


public class GroovyRecognizerTree extends antlr.TreeParser       implements GroovyRecognizerTreeTokenTypes
 {
public GroovyRecognizerTree() {
	tokenNames = _tokenNames;
}

	public final void compilationUnit(AST _t) throws RecognitionException {

		AST compilationUnit_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST compilationUnit_AST = null;

		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case SH_COMMENT:
		{
			AST tmp1_AST = null;
			AST tmp1_AST_in = null;
			tmp1_AST = astFactory.create((AST)_t);
			tmp1_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp1_AST);
			match(_t,SH_COMMENT);
			_t = _t.getNextSibling();
			break;
		}
		case 3:
		case PACKAGE_DEF:
		case STATIC_IMPORT:
		case IMPORT:
		case IDENT:
		case DOT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		case MODIFIERS:
		case LITERAL_synchronized:
		case CLASS_DEF:
		case INTERFACE_DEF:
		case ENUM_DEF:
		case ANNOTATION_DEF:
		case LCURLY:
		case LITERAL_while:
		case LITERAL_with:
		case SPREAD_ARG:
		case LITERAL_for:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		case LITERAL_return:
		case EXPR:
		case LITERAL_try:
		case NLS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		nls(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case PACKAGE_DEF:
		{
			packageDefinition(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case 3:
		case STATIC_IMPORT:
		case IMPORT:
		case IDENT:
		case DOT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		case MODIFIERS:
		case LITERAL_synchronized:
		case CLASS_DEF:
		case INTERFACE_DEF:
		case ENUM_DEF:
		case ANNOTATION_DEF:
		case LCURLY:
		case LITERAL_while:
		case LITERAL_with:
		case SPREAD_ARG:
		case LITERAL_for:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		case LITERAL_return:
		case EXPR:
		case LITERAL_try:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		{
		_loop5:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_0.member(_t.getType()))) {
				statement(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop5;
			}

		} while (true);
		}
		compilationUnit_AST = (AST)currentAST.root;
		returnAST = compilationUnit_AST;
		_retTree = _t;
	}

	public final void nls(AST _t) throws RecognitionException {

		AST nls_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST nls_AST = null;

		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NLS:
		{
			AST tmp2_AST = null;
			AST tmp2_AST_in = null;
			tmp2_AST = astFactory.create((AST)_t);
			tmp2_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp2_AST);
			match(_t,NLS);
			_t = _t.getNextSibling();
			break;
		}
		case 3:
		case PACKAGE_DEF:
		case STATIC_IMPORT:
		case IMPORT:
		case IDENT:
		case DOT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		case MODIFIERS:
		case LITERAL_synchronized:
		case CLASS_DEF:
		case INTERFACE_DEF:
		case ENUM_DEF:
		case ANNOTATION_DEF:
		case LCURLY:
		case LITERAL_while:
		case LITERAL_with:
		case SPREAD_ARG:
		case LITERAL_for:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		case LITERAL_return:
		case EXPR:
		case LITERAL_try:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		nls_AST = (AST)currentAST.root;
		returnAST = nls_AST;
		_retTree = _t;
	}

	public final void packageDefinition(AST _t) throws RecognitionException {

		AST packageDefinition_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST packageDefinition_AST = null;

		AST __t8 = _t;
		AST tmp3_AST = null;
		AST tmp3_AST_in = null;
		tmp3_AST = astFactory.create((AST)_t);
		tmp3_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp3_AST);
		ASTPair __currentAST8 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,PACKAGE_DEF);
		_t = _t.getFirstChild();
		annotationsOpt(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		identifier(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST8;
		_t = __t8;
		_t = _t.getNextSibling();
		packageDefinition_AST = (AST)currentAST.root;
		returnAST = packageDefinition_AST;
		_retTree = _t;
	}

	public final void statement(AST _t) throws RecognitionException {

		AST statement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST statement_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LCURLY:
		{
			openOrClosedBlock(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			statement_AST = (AST)currentAST.root;
			break;
		}
		case EXPR:
		{
			expressionStatement(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			statement_AST = (AST)currentAST.root;
			break;
		}
		case CLASS_DEF:
		case INTERFACE_DEF:
		case ENUM_DEF:
		case ANNOTATION_DEF:
		{
			typeDefinitionInternal(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_for:
		{
			forStatement(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_while:
		{
			AST __t219 = _t;
			AST tmp4_AST = null;
			AST tmp4_AST_in = null;
			tmp4_AST = astFactory.create((AST)_t);
			tmp4_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp4_AST);
			ASTPair __currentAST219 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_while);
			_t = _t.getFirstChild();
			strictContextExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			compatibleBodyStatement(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST219;
			_t = __t219;
			_t = _t.getNextSibling();
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_with:
		{
			AST __t220 = _t;
			AST tmp5_AST = null;
			AST tmp5_AST_in = null;
			tmp5_AST = astFactory.create((AST)_t);
			tmp5_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp5_AST);
			ASTPair __currentAST220 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_with);
			_t = _t.getFirstChild();
			strictContextExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			compoundStatement(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST220;
			_t = __t220;
			_t = _t.getNextSibling();
			statement_AST = (AST)currentAST.root;
			break;
		}
		case SPREAD_ARG:
		{
			AST __t221 = _t;
			AST tmp6_AST = null;
			AST tmp6_AST_in = null;
			tmp6_AST = astFactory.create((AST)_t);
			tmp6_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp6_AST);
			ASTPair __currentAST221 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SPREAD_ARG);
			_t = _t.getFirstChild();
			expressionStatement(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST221;
			_t = __t221;
			_t = _t.getNextSibling();
			statement_AST = (AST)currentAST.root;
			break;
		}
		case STATIC_IMPORT:
		case IMPORT:
		{
			importStatement(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_try:
		{
			tryBlock(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_synchronized:
		{
			AST __t222 = _t;
			AST tmp7_AST = null;
			AST tmp7_AST_in = null;
			tmp7_AST = astFactory.create((AST)_t);
			tmp7_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp7_AST);
			ASTPair __currentAST222 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_synchronized);
			_t = _t.getFirstChild();
			strictContextExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			compoundStatement(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST222;
			_t = __t222;
			_t = _t.getNextSibling();
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		case LITERAL_return:
		{
			branchStatement(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			statement_AST = (AST)currentAST.root;
			break;
		}
		case IDENT:
		case DOT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		case MODIFIERS:
		{
			declaration(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			statement_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = statement_AST;
		_retTree = _t;
	}

	public final void snippetUnit(AST _t) throws RecognitionException {

		AST snippetUnit_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST snippetUnit_AST = null;

		blockBody(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		snippetUnit_AST = (AST)currentAST.root;
		returnAST = snippetUnit_AST;
		_retTree = _t;
	}

	public final void blockBody(AST _t) throws RecognitionException {

		AST blockBody_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST blockBody_AST = null;

		{
		_loop209:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_0.member(_t.getType()))) {
				statement(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop209;
			}

		} while (true);
		}
		blockBody_AST = (AST)currentAST.root;
		returnAST = blockBody_AST;
		_retTree = _t;
	}

	public final void annotationsOpt(AST _t) throws RecognitionException {

		AST annotationsOpt_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationsOpt_AST = null;

		AST __t75 = _t;
		AST tmp8_AST = null;
		AST tmp8_AST_in = null;
		tmp8_AST = astFactory.create((AST)_t);
		tmp8_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp8_AST);
		ASTPair __currentAST75 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,ANNOTATIONS);
		_t = _t.getFirstChild();
		{
		_loop77:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==ANNOTATION)) {
				annotation(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop77;
			}

		} while (true);
		}
		currentAST = __currentAST75;
		_t = __t75;
		_t = _t.getNextSibling();
		annotationsOpt_AST = (AST)currentAST.root;
		returnAST = annotationsOpt_AST;
		_retTree = _t;
	}

	public final void identifier(AST _t) throws RecognitionException {

		AST identifier_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST identifier_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case DOT:
		{
			AST __t57 = _t;
			AST tmp9_AST = null;
			AST tmp9_AST_in = null;
			tmp9_AST = astFactory.create((AST)_t);
			tmp9_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp9_AST);
			ASTPair __currentAST57 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,DOT);
			_t = _t.getFirstChild();
			identifier0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp10_AST = null;
			AST tmp10_AST_in = null;
			tmp10_AST = astFactory.create((AST)_t);
			tmp10_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp10_AST);
			match(_t,IDENT);
			_t = _t.getNextSibling();
			currentAST = __currentAST57;
			_t = __t57;
			_t = _t.getNextSibling();
			identifier_AST = (AST)currentAST.root;
			break;
		}
		case IDENT:
		{
			AST tmp11_AST = null;
			AST tmp11_AST_in = null;
			tmp11_AST = astFactory.create((AST)_t);
			tmp11_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp11_AST);
			match(_t,IDENT);
			_t = _t.getNextSibling();
			identifier_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = identifier_AST;
		_retTree = _t;
	}

	public final void importStatement(AST _t) throws RecognitionException {

		AST importStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST importStatement_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case STATIC_IMPORT:
		{
			AST __t10 = _t;
			AST tmp12_AST = null;
			AST tmp12_AST_in = null;
			tmp12_AST = astFactory.create((AST)_t);
			tmp12_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp12_AST);
			ASTPair __currentAST10 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,STATIC_IMPORT);
			_t = _t.getFirstChild();
			identifierStar(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST10;
			_t = __t10;
			_t = _t.getNextSibling();
			importStatement_AST = (AST)currentAST.root;
			break;
		}
		case IMPORT:
		{
			AST __t11 = _t;
			AST tmp13_AST = null;
			AST tmp13_AST_in = null;
			tmp13_AST = astFactory.create((AST)_t);
			tmp13_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp13_AST);
			ASTPair __currentAST11 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,IMPORT);
			_t = _t.getFirstChild();
			identifierStar(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST11;
			_t = __t11;
			_t = _t.getNextSibling();
			importStatement_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = importStatement_AST;
		_retTree = _t;
	}

	public final void identifierStar(AST _t) throws RecognitionException {

		AST identifierStar_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST identifierStar_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case IDENT:
		case DOT:
		{
			identifierStar0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			identifierStar_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_as:
		{
			AST __t61 = _t;
			AST tmp14_AST = null;
			AST tmp14_AST_in = null;
			tmp14_AST = astFactory.create((AST)_t);
			tmp14_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp14_AST);
			ASTPair __currentAST61 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_as);
			_t = _t.getFirstChild();
			identifierStar0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp15_AST = null;
			AST tmp15_AST_in = null;
			tmp15_AST = astFactory.create((AST)_t);
			tmp15_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp15_AST);
			match(_t,IDENT);
			_t = _t.getNextSibling();
			currentAST = __currentAST61;
			_t = __t61;
			_t = _t.getNextSibling();
			identifierStar_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = identifierStar_AST;
		_retTree = _t;
	}

	public final void typeDefinitionInternal(AST _t) throws RecognitionException {

		AST typeDefinitionInternal_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeDefinitionInternal_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case CLASS_DEF:
		{
			classDefinition(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			typeDefinitionInternal_AST = (AST)currentAST.root;
			break;
		}
		case INTERFACE_DEF:
		{
			interfaceDefinition(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			typeDefinitionInternal_AST = (AST)currentAST.root;
			break;
		}
		case ENUM_DEF:
		{
			enumDefinition(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			typeDefinitionInternal_AST = (AST)currentAST.root;
			break;
		}
		case ANNOTATION_DEF:
		{
			annotationDefinition(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			typeDefinitionInternal_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = typeDefinitionInternal_AST;
		_retTree = _t;
	}

	public final void classDefinition(AST _t) throws RecognitionException {

		AST classDefinition_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classDefinition_AST = null;

		AST __t89 = _t;
		AST tmp16_AST = null;
		AST tmp16_AST_in = null;
		tmp16_AST = astFactory.create((AST)_t);
		tmp16_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp16_AST);
		ASTPair __currentAST89 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,CLASS_DEF);
		_t = _t.getFirstChild();
		modifiers(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		AST tmp17_AST = null;
		AST tmp17_AST_in = null;
		tmp17_AST = astFactory.create((AST)_t);
		tmp17_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp17_AST);
		match(_t,IDENT);
		_t = _t.getNextSibling();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case TYPE_PARAMETERS:
		{
			typeParameters(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case EXTENDS_CLAUSE:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		superClassClause(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		implementsClause(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		classBlock(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST89;
		_t = __t89;
		_t = _t.getNextSibling();
		classDefinition_AST = (AST)currentAST.root;
		returnAST = classDefinition_AST;
		_retTree = _t;
	}

	public final void interfaceDefinition(AST _t) throws RecognitionException {

		AST interfaceDefinition_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST interfaceDefinition_AST = null;

		AST __t92 = _t;
		AST tmp18_AST = null;
		AST tmp18_AST_in = null;
		tmp18_AST = astFactory.create((AST)_t);
		tmp18_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp18_AST);
		ASTPair __currentAST92 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,INTERFACE_DEF);
		_t = _t.getFirstChild();
		modifiers(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		AST tmp19_AST = null;
		AST tmp19_AST_in = null;
		tmp19_AST = astFactory.create((AST)_t);
		tmp19_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp19_AST);
		match(_t,IDENT);
		_t = _t.getNextSibling();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case TYPE_PARAMETERS:
		{
			typeParameters(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case EXTENDS_CLAUSE:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		interfaceExtends(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		interfaceBlock(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST92;
		_t = __t92;
		_t = _t.getNextSibling();
		interfaceDefinition_AST = (AST)currentAST.root;
		returnAST = interfaceDefinition_AST;
		_retTree = _t;
	}

	public final void enumDefinition(AST _t) throws RecognitionException {

		AST enumDefinition_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumDefinition_AST = null;

		AST __t95 = _t;
		AST tmp20_AST = null;
		AST tmp20_AST_in = null;
		tmp20_AST = astFactory.create((AST)_t);
		tmp20_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp20_AST);
		ASTPair __currentAST95 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,ENUM_DEF);
		_t = _t.getFirstChild();
		modifiers(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		AST tmp21_AST = null;
		AST tmp21_AST_in = null;
		tmp21_AST = astFactory.create((AST)_t);
		tmp21_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp21_AST);
		match(_t,IDENT);
		_t = _t.getNextSibling();
		implementsClause(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		enumBlock(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST95;
		_t = __t95;
		_t = _t.getNextSibling();
		enumDefinition_AST = (AST)currentAST.root;
		returnAST = enumDefinition_AST;
		_retTree = _t;
	}

	public final void annotationDefinition(AST _t) throws RecognitionException {

		AST annotationDefinition_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationDefinition_AST = null;

		AST __t97 = _t;
		AST tmp22_AST = null;
		AST tmp22_AST_in = null;
		tmp22_AST = astFactory.create((AST)_t);
		tmp22_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp22_AST);
		ASTPair __currentAST97 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,ANNOTATION_DEF);
		_t = _t.getFirstChild();
		modifiers(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		AST tmp23_AST = null;
		AST tmp23_AST_in = null;
		tmp23_AST = astFactory.create((AST)_t);
		tmp23_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp23_AST);
		match(_t,IDENT);
		_t = _t.getNextSibling();
		annotationBlock(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST97;
		_t = __t97;
		_t = _t.getNextSibling();
		annotationDefinition_AST = (AST)currentAST.root;
		returnAST = annotationDefinition_AST;
		_retTree = _t;
	}

	public final void declaration(AST _t) throws RecognitionException {

		AST declaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST declaration_AST = null;

		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case MODIFIERS:
		{
			modifiers(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case IDENT:
		case DOT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		typeSpec(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		int _cnt16=0;
		_loop16:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==VARIABLE_DEF)) {
				variableDefinition(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt16>=1 ) { break _loop16; } else {throw new NoViableAltException(_t);}
			}

			_cnt16++;
		} while (true);
		}
		declaration_AST = (AST)currentAST.root;
		returnAST = declaration_AST;
		_retTree = _t;
	}

	public final void modifiers(AST _t) throws RecognitionException {

		AST modifiers_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST modifiers_AST = null;

		AST __t66 = _t;
		AST tmp24_AST = null;
		AST tmp24_AST_in = null;
		tmp24_AST = astFactory.create((AST)_t);
		tmp24_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp24_AST);
		ASTPair __currentAST66 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,MODIFIERS);
		_t = _t.getFirstChild();
		modifiersInternal(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST66;
		_t = __t66;
		_t = _t.getNextSibling();
		modifiers_AST = (AST)currentAST.root;
		returnAST = modifiers_AST;
		_retTree = _t;
	}

	public final void typeSpec(AST _t) throws RecognitionException {

		AST typeSpec_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeSpec_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case IDENT:
		case DOT:
		{
			classTypeSpec(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			typeSpec_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		{
			builtInTypeSpec(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			typeSpec_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = typeSpec_AST;
		_retTree = _t;
	}

	public final void variableDefinition(AST _t) throws RecognitionException {

		AST variableDefinition_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST variableDefinition_AST = null;

		AST __t167 = _t;
		AST tmp25_AST = null;
		AST tmp25_AST_in = null;
		tmp25_AST = astFactory.create((AST)_t);
		tmp25_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp25_AST);
		ASTPair __currentAST167 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,VARIABLE_DEF);
		_t = _t.getFirstChild();
		modifiers(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		typeSpec(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		variableDeclarator(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		varInitializer(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST167;
		_t = __t167;
		_t = _t.getNextSibling();
		variableDefinition_AST = (AST)currentAST.root;
		returnAST = variableDefinition_AST;
		_retTree = _t;
	}

	public final void singleDeclaration(AST _t) throws RecognitionException {

		AST singleDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST singleDeclaration_AST = null;

		singleVariable(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		varInitializer(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		singleDeclaration_AST = (AST)currentAST.root;
		returnAST = singleDeclaration_AST;
		_retTree = _t;
	}

	public final void singleVariable(AST _t) throws RecognitionException {

		AST singleVariable_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST singleVariable_AST = null;

		AST __t170 = _t;
		AST tmp26_AST = null;
		AST tmp26_AST_in = null;
		tmp26_AST = astFactory.create((AST)_t);
		tmp26_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp26_AST);
		ASTPair __currentAST170 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,VARIABLE_DEF);
		_t = _t.getFirstChild();
		modifiers(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		typeSpec(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		AST tmp27_AST = null;
		AST tmp27_AST_in = null;
		tmp27_AST = astFactory.create((AST)_t);
		tmp27_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp27_AST);
		match(_t,IDENT);
		_t = _t.getNextSibling();
		currentAST = __currentAST170;
		_t = __t170;
		_t = _t.getNextSibling();
		singleVariable_AST = (AST)currentAST.root;
		returnAST = singleVariable_AST;
		_retTree = _t;
	}

	public final void varInitializer(AST _t) throws RecognitionException {

		AST varInitializer_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST varInitializer_AST = null;

		AST __t175 = _t;
		AST tmp28_AST = null;
		AST tmp28_AST_in = null;
		tmp28_AST = astFactory.create((AST)_t);
		tmp28_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp28_AST);
		ASTPair __currentAST175 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,ASSIGN);
		_t = _t.getFirstChild();
		expression(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST175;
		_t = __t175;
		_t = _t.getNextSibling();
		varInitializer_AST = (AST)currentAST.root;
		returnAST = varInitializer_AST;
		_retTree = _t;
	}

	public final void declarationStart(AST _t) throws RecognitionException {

		AST declarationStart_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST declarationStart_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_def:
		{
			AST tmp29_AST = null;
			AST tmp29_AST_in = null;
			tmp29_AST = astFactory.create((AST)_t);
			tmp29_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp29_AST);
			match(_t,LITERAL_def);
			_t = _t.getNextSibling();
			declarationStart_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_static:
		case LITERAL_transient:
		case LITERAL_final:
		case LITERAL_abstract:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case LITERAL_strictfp:
		{
			modifier(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			declarationStart_AST = (AST)currentAST.root;
			break;
		}
		case AT:
		{
			AST tmp30_AST = null;
			AST tmp30_AST_in = null;
			tmp30_AST = astFactory.create((AST)_t);
			tmp30_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp30_AST);
			match(_t,AT);
			_t = _t.getNextSibling();
			AST tmp31_AST = null;
			AST tmp31_AST_in = null;
			tmp31_AST = astFactory.create((AST)_t);
			tmp31_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp31_AST);
			match(_t,IDENT);
			_t = _t.getNextSibling();
			declarationStart_AST = (AST)currentAST.root;
			break;
		}
		case IDENT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		{
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_any:
			{
				builtInType(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case IDENT:
			{
				qualifiedTypeName(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			_loop21:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==LBRACK)) {
					AST tmp32_AST = null;
					AST tmp32_AST_in = null;
					tmp32_AST = astFactory.create((AST)_t);
					tmp32_AST_in = (AST)_t;
					astFactory.addASTChild(currentAST, tmp32_AST);
					match(_t,LBRACK);
					_t = _t.getNextSibling();
					balancedTokens(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					AST tmp33_AST = null;
					AST tmp33_AST_in = null;
					tmp33_AST = astFactory.create((AST)_t);
					tmp33_AST_in = (AST)_t;
					astFactory.addASTChild(currentAST, tmp33_AST);
					match(_t,RBRACK);
					_t = _t.getNextSibling();
				}
				else {
					break _loop21;
				}

			} while (true);
			}
			AST tmp34_AST = null;
			AST tmp34_AST_in = null;
			tmp34_AST = astFactory.create((AST)_t);
			tmp34_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp34_AST);
			match(_t,IDENT);
			_t = _t.getNextSibling();
			declarationStart_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = declarationStart_AST;
		_retTree = _t;
	}

	public final void modifier(AST _t) throws RecognitionException {

		AST modifier_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST modifier_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_private:
		{
			AST tmp35_AST = null;
			AST tmp35_AST_in = null;
			tmp35_AST = astFactory.create((AST)_t);
			tmp35_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp35_AST);
			match(_t,LITERAL_private);
			_t = _t.getNextSibling();
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_public:
		{
			AST tmp36_AST = null;
			AST tmp36_AST_in = null;
			tmp36_AST = astFactory.create((AST)_t);
			tmp36_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp36_AST);
			match(_t,LITERAL_public);
			_t = _t.getNextSibling();
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_protected:
		{
			AST tmp37_AST = null;
			AST tmp37_AST_in = null;
			tmp37_AST = astFactory.create((AST)_t);
			tmp37_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp37_AST);
			match(_t,LITERAL_protected);
			_t = _t.getNextSibling();
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_static:
		{
			AST tmp38_AST = null;
			AST tmp38_AST_in = null;
			tmp38_AST = astFactory.create((AST)_t);
			tmp38_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp38_AST);
			match(_t,LITERAL_static);
			_t = _t.getNextSibling();
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_transient:
		{
			AST tmp39_AST = null;
			AST tmp39_AST_in = null;
			tmp39_AST = astFactory.create((AST)_t);
			tmp39_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp39_AST);
			match(_t,LITERAL_transient);
			_t = _t.getNextSibling();
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_final:
		{
			AST tmp40_AST = null;
			AST tmp40_AST_in = null;
			tmp40_AST = astFactory.create((AST)_t);
			tmp40_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp40_AST);
			match(_t,LITERAL_final);
			_t = _t.getNextSibling();
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_abstract:
		{
			AST tmp41_AST = null;
			AST tmp41_AST_in = null;
			tmp41_AST = astFactory.create((AST)_t);
			tmp41_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp41_AST);
			match(_t,LITERAL_abstract);
			_t = _t.getNextSibling();
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_native:
		{
			AST tmp42_AST = null;
			AST tmp42_AST_in = null;
			tmp42_AST = astFactory.create((AST)_t);
			tmp42_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp42_AST);
			match(_t,LITERAL_native);
			_t = _t.getNextSibling();
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_threadsafe:
		{
			AST tmp43_AST = null;
			AST tmp43_AST_in = null;
			tmp43_AST = astFactory.create((AST)_t);
			tmp43_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp43_AST);
			match(_t,LITERAL_threadsafe);
			_t = _t.getNextSibling();
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_synchronized:
		{
			AST tmp44_AST = null;
			AST tmp44_AST_in = null;
			tmp44_AST = astFactory.create((AST)_t);
			tmp44_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp44_AST);
			match(_t,LITERAL_synchronized);
			_t = _t.getNextSibling();
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_volatile:
		{
			AST tmp45_AST = null;
			AST tmp45_AST_in = null;
			tmp45_AST = astFactory.create((AST)_t);
			tmp45_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp45_AST);
			match(_t,LITERAL_volatile);
			_t = _t.getNextSibling();
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_strictfp:
		{
			AST tmp46_AST = null;
			AST tmp46_AST_in = null;
			tmp46_AST = astFactory.create((AST)_t);
			tmp46_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp46_AST);
			match(_t,LITERAL_strictfp);
			_t = _t.getNextSibling();
			modifier_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = modifier_AST;
		_retTree = _t;
	}

	public final void builtInType(AST _t) throws RecognitionException {

		AST builtInType_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST builtInType_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_void:
		{
			AST tmp47_AST = null;
			AST tmp47_AST_in = null;
			tmp47_AST = astFactory.create((AST)_t);
			tmp47_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp47_AST);
			match(_t,LITERAL_void);
			_t = _t.getNextSibling();
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_boolean:
		{
			AST tmp48_AST = null;
			AST tmp48_AST_in = null;
			tmp48_AST = astFactory.create((AST)_t);
			tmp48_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp48_AST);
			match(_t,LITERAL_boolean);
			_t = _t.getNextSibling();
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_byte:
		{
			AST tmp49_AST = null;
			AST tmp49_AST_in = null;
			tmp49_AST = astFactory.create((AST)_t);
			tmp49_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp49_AST);
			match(_t,LITERAL_byte);
			_t = _t.getNextSibling();
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_char:
		{
			AST tmp50_AST = null;
			AST tmp50_AST_in = null;
			tmp50_AST = astFactory.create((AST)_t);
			tmp50_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp50_AST);
			match(_t,LITERAL_char);
			_t = _t.getNextSibling();
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_short:
		{
			AST tmp51_AST = null;
			AST tmp51_AST_in = null;
			tmp51_AST = astFactory.create((AST)_t);
			tmp51_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp51_AST);
			match(_t,LITERAL_short);
			_t = _t.getNextSibling();
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_int:
		{
			AST tmp52_AST = null;
			AST tmp52_AST_in = null;
			tmp52_AST = astFactory.create((AST)_t);
			tmp52_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp52_AST);
			match(_t,LITERAL_int);
			_t = _t.getNextSibling();
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_float:
		{
			AST tmp53_AST = null;
			AST tmp53_AST_in = null;
			tmp53_AST = astFactory.create((AST)_t);
			tmp53_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp53_AST);
			match(_t,LITERAL_float);
			_t = _t.getNextSibling();
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_long:
		{
			AST tmp54_AST = null;
			AST tmp54_AST_in = null;
			tmp54_AST = astFactory.create((AST)_t);
			tmp54_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp54_AST);
			match(_t,LITERAL_long);
			_t = _t.getNextSibling();
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_double:
		{
			AST tmp55_AST = null;
			AST tmp55_AST_in = null;
			tmp55_AST = astFactory.create((AST)_t);
			tmp55_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp55_AST);
			match(_t,LITERAL_double);
			_t = _t.getNextSibling();
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_any:
		{
			AST tmp56_AST = null;
			AST tmp56_AST_in = null;
			tmp56_AST = astFactory.create((AST)_t);
			tmp56_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp56_AST);
			match(_t,LITERAL_any);
			_t = _t.getNextSibling();
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = builtInType_AST;
		_retTree = _t;
	}

	public final void qualifiedTypeName(AST _t) throws RecognitionException {

		AST qualifiedTypeName_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST qualifiedTypeName_AST = null;

		AST tmp57_AST = null;
		AST tmp57_AST_in = null;
		tmp57_AST = astFactory.create((AST)_t);
		tmp57_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp57_AST);
		match(_t,IDENT);
		_t = _t.getNextSibling();
		{
		_loop24:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==DOT)) {
				AST tmp58_AST = null;
				AST tmp58_AST_in = null;
				tmp58_AST = astFactory.create((AST)_t);
				tmp58_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp58_AST);
				match(_t,DOT);
				_t = _t.getNextSibling();
				AST tmp59_AST = null;
				AST tmp59_AST_in = null;
				tmp59_AST = astFactory.create((AST)_t);
				tmp59_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp59_AST);
				match(_t,IDENT);
				_t = _t.getNextSibling();
			}
			else {
				break _loop24;
			}

		} while (true);
		}
		qualifiedTypeName_AST = (AST)currentAST.root;
		returnAST = qualifiedTypeName_AST;
		_retTree = _t;
	}

	public final void balancedTokens(AST _t) throws RecognitionException {

		AST balancedTokens_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST balancedTokens_AST = null;

		{
		_loop509:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_1.member(_t.getType()))) {
				balancedBrackets(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((_tokenSet_2.member(_t.getType()))) {
				{
				AST tmp60_AST = null;
				AST tmp60_AST_in = null;
				tmp60_AST = astFactory.create((AST)_t);
				tmp60_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp60_AST);
				match(_t,_tokenSet_2);
				_t = _t.getNextSibling();
				}
			}
			else {
				break _loop509;
			}

		} while (true);
		}
		balancedTokens_AST = (AST)currentAST.root;
		returnAST = balancedTokens_AST;
		_retTree = _t;
	}

	public final void constructorStart(AST _t) throws RecognitionException {

		AST constructorStart_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constructorStart_AST = null;

		modifiersOpt(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		AST tmp61_AST = null;
		AST tmp61_AST_in = null;
		tmp61_AST = astFactory.create((AST)_t);
		tmp61_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp61_AST);
		match(_t,IDENT);
		_t = _t.getNextSibling();
		constructorStart_AST = (AST)currentAST.root;
		returnAST = constructorStart_AST;
		_retTree = _t;
	}

	public final void modifiersOpt(AST _t) throws RecognitionException {

		AST modifiersOpt_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST modifiersOpt_AST = null;

		AST __t68 = _t;
		AST tmp62_AST = null;
		AST tmp62_AST_in = null;
		tmp62_AST = astFactory.create((AST)_t);
		tmp62_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp62_AST);
		ASTPair __currentAST68 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,MODIFIERS);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_def:
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_static:
		case LITERAL_transient:
		case LITERAL_final:
		case LITERAL_abstract:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case LITERAL_strictfp:
		case ANNOTATION:
		{
			modifiersInternal(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case 3:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST68;
		_t = __t68;
		_t = _t.getNextSibling();
		modifiersOpt_AST = (AST)currentAST.root;
		returnAST = modifiersOpt_AST;
		_retTree = _t;
	}

	public final void typeDeclarationStart(AST _t) throws RecognitionException {

		AST typeDeclarationStart_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeDeclarationStart_AST = null;

		modifiersOpt(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_class:
		{
			AST tmp63_AST = null;
			AST tmp63_AST_in = null;
			tmp63_AST = astFactory.create((AST)_t);
			tmp63_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp63_AST);
			match(_t,LITERAL_class);
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_interface:
		{
			AST tmp64_AST = null;
			AST tmp64_AST_in = null;
			tmp64_AST = astFactory.create((AST)_t);
			tmp64_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp64_AST);
			match(_t,LITERAL_interface);
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_enum:
		{
			AST tmp65_AST = null;
			AST tmp65_AST_in = null;
			tmp65_AST = astFactory.create((AST)_t);
			tmp65_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp65_AST);
			match(_t,LITERAL_enum);
			_t = _t.getNextSibling();
			break;
		}
		case AT:
		{
			AST tmp66_AST = null;
			AST tmp66_AST_in = null;
			tmp66_AST = astFactory.create((AST)_t);
			tmp66_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp66_AST);
			match(_t,AT);
			_t = _t.getNextSibling();
			AST tmp67_AST = null;
			AST tmp67_AST_in = null;
			tmp67_AST = astFactory.create((AST)_t);
			tmp67_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp67_AST);
			match(_t,LITERAL_interface);
			_t = _t.getNextSibling();
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		typeDeclarationStart_AST = (AST)currentAST.root;
		returnAST = typeDeclarationStart_AST;
		_retTree = _t;
	}

	public final void classTypeSpec(AST _t) throws RecognitionException {

		AST classTypeSpec_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classTypeSpec_AST = null;

		classOrInterfaceType(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		declaratorBrackets(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		classTypeSpec_AST = (AST)currentAST.root;
		returnAST = classTypeSpec_AST;
		_retTree = _t;
	}

	public final void builtInTypeSpec(AST _t) throws RecognitionException {

		AST builtInTypeSpec_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST builtInTypeSpec_AST = null;

		builtInType(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		declaratorBrackets(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		builtInTypeSpec_AST = (AST)currentAST.root;
		returnAST = builtInTypeSpec_AST;
		_retTree = _t;
	}

	public final void classOrInterfaceType(AST _t) throws RecognitionException {

		AST classOrInterfaceType_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classOrInterfaceType_AST = null;

		classOrInterfaceType0a(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		classOrInterfaceType_AST = (AST)currentAST.root;
		returnAST = classOrInterfaceType_AST;
		_retTree = _t;
	}

	public final void declaratorBrackets(AST _t) throws RecognitionException {

		AST declaratorBrackets_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST declaratorBrackets_AST = null;

		declaratorBrackets0a(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		declaratorBrackets_AST = (AST)currentAST.root;
		returnAST = declaratorBrackets_AST;
		_retTree = _t;
	}

	public final void classOrInterfaceType0a(AST _t) throws RecognitionException {

		AST classOrInterfaceType0a_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classOrInterfaceType0a_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case IDENT:
		{
			AST __t31 = _t;
			AST tmp68_AST = null;
			AST tmp68_AST_in = null;
			tmp68_AST = astFactory.create((AST)_t);
			tmp68_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp68_AST);
			ASTPair __currentAST31 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,IDENT);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TYPE_ARGUMENTS:
			{
				typeArguments(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST31;
			_t = __t31;
			_t = _t.getNextSibling();
			classOrInterfaceType0a_AST = (AST)currentAST.root;
			break;
		}
		case DOT:
		{
			AST __t33 = _t;
			AST tmp69_AST = null;
			AST tmp69_AST_in = null;
			tmp69_AST = astFactory.create((AST)_t);
			tmp69_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp69_AST);
			ASTPair __currentAST33 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,DOT);
			_t = _t.getFirstChild();
			classOrInterfaceType0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp70_AST = null;
			AST tmp70_AST_in = null;
			tmp70_AST = astFactory.create((AST)_t);
			tmp70_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp70_AST);
			match(_t,IDENT);
			_t = _t.getNextSibling();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TYPE_ARGUMENTS:
			{
				typeArguments(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST33;
			_t = __t33;
			_t = _t.getNextSibling();
			classOrInterfaceType0a_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = classOrInterfaceType0a_AST;
		_retTree = _t;
	}

	public final void typeArguments(AST _t) throws RecognitionException {

		AST typeArguments_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeArguments_AST = null;

		AST __t44 = _t;
		AST tmp71_AST = null;
		AST tmp71_AST_in = null;
		tmp71_AST = astFactory.create((AST)_t);
		tmp71_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp71_AST);
		ASTPair __currentAST44 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,TYPE_ARGUMENTS);
		_t = _t.getFirstChild();
		{
		int _cnt46=0;
		_loop46:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==TYPE_ARGUMENT)) {
				typeArgument(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt46>=1 ) { break _loop46; } else {throw new NoViableAltException(_t);}
			}

			_cnt46++;
		} while (true);
		}
		currentAST = __currentAST44;
		_t = __t44;
		_t = _t.getNextSibling();
		typeArguments_AST = (AST)currentAST.root;
		returnAST = typeArguments_AST;
		_retTree = _t;
	}

	public final void typeArgumentSpec(AST _t) throws RecognitionException {

		AST typeArgumentSpec_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeArgumentSpec_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case IDENT:
		case DOT:
		{
			classTypeSpec(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			typeArgumentSpec_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		{
			builtInTypeArraySpec(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			typeArgumentSpec_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = typeArgumentSpec_AST;
		_retTree = _t;
	}

	public final void builtInTypeArraySpec(AST _t) throws RecognitionException {

		AST builtInTypeArraySpec_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST builtInTypeArraySpec_AST = null;

		builtInType(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		declaratorBrackets(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		}
		builtInTypeArraySpec_AST = (AST)currentAST.root;
		returnAST = builtInTypeArraySpec_AST;
		_retTree = _t;
	}

	public final void typeArgument(AST _t) throws RecognitionException {

		AST typeArgument_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeArgument_AST = null;

		AST __t38 = _t;
		AST tmp72_AST = null;
		AST tmp72_AST_in = null;
		tmp72_AST = astFactory.create((AST)_t);
		tmp72_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp72_AST);
		ASTPair __currentAST38 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,TYPE_ARGUMENT);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case IDENT:
		case DOT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		{
			typeArgumentSpec(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case WILDCARD_TYPE:
		{
			wildcardType(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST38;
		_t = __t38;
		_t = _t.getNextSibling();
		typeArgument_AST = (AST)currentAST.root;
		returnAST = typeArgument_AST;
		_retTree = _t;
	}

	public final void wildcardType(AST _t) throws RecognitionException {

		AST wildcardType_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST wildcardType_AST = null;

		AST __t41 = _t;
		AST tmp73_AST = null;
		AST tmp73_AST_in = null;
		tmp73_AST = astFactory.create((AST)_t);
		tmp73_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp73_AST);
		ASTPair __currentAST41 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,WILDCARD_TYPE);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_extends:
		case LITERAL_super:
		{
			typeArgumentBounds(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case 3:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST41;
		_t = __t41;
		_t = _t.getNextSibling();
		wildcardType_AST = (AST)currentAST.root;
		returnAST = wildcardType_AST;
		_retTree = _t;
	}

	public final void typeArgumentBounds(AST _t) throws RecognitionException {

		AST typeArgumentBounds_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeArgumentBounds_AST = null;

		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_extends:
		{
			AST tmp74_AST = null;
			AST tmp74_AST_in = null;
			tmp74_AST = astFactory.create((AST)_t);
			tmp74_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp74_AST);
			match(_t,LITERAL_extends);
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_super:
		{
			AST tmp75_AST = null;
			AST tmp75_AST_in = null;
			tmp75_AST = astFactory.create((AST)_t);
			tmp75_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp75_AST);
			match(_t,LITERAL_super);
			_t = _t.getNextSibling();
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		classOrInterfaceType(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		typeArgumentBounds_AST = (AST)currentAST.root;
		returnAST = typeArgumentBounds_AST;
		_retTree = _t;
	}

	public final void type(AST _t) throws RecognitionException {

		AST type_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST type_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case IDENT:
		case DOT:
		{
			classOrInterfaceType(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			type_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		{
			builtInType(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			type_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = type_AST;
		_retTree = _t;
	}

	public final void identifier0a(AST _t) throws RecognitionException {

		AST identifier0a_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST identifier0a_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case IDENT:
		{
			AST tmp76_AST = null;
			AST tmp76_AST_in = null;
			tmp76_AST = astFactory.create((AST)_t);
			tmp76_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp76_AST);
			match(_t,IDENT);
			_t = _t.getNextSibling();
			identifier0a_AST = (AST)currentAST.root;
			break;
		}
		case DOT:
		{
			AST __t55 = _t;
			AST tmp77_AST = null;
			AST tmp77_AST_in = null;
			tmp77_AST = astFactory.create((AST)_t);
			tmp77_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp77_AST);
			ASTPair __currentAST55 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,DOT);
			_t = _t.getFirstChild();
			identifier0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp78_AST = null;
			AST tmp78_AST_in = null;
			tmp78_AST = astFactory.create((AST)_t);
			tmp78_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp78_AST);
			match(_t,IDENT);
			_t = _t.getNextSibling();
			currentAST = __currentAST55;
			_t = __t55;
			_t = _t.getNextSibling();
			identifier0a_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = identifier0a_AST;
		_retTree = _t;
	}

	public final void identifierStar0a(AST _t) throws RecognitionException {

		AST identifierStar0a_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST identifierStar0a_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case IDENT:
		{
			AST tmp79_AST = null;
			AST tmp79_AST_in = null;
			tmp79_AST = astFactory.create((AST)_t);
			tmp79_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp79_AST);
			match(_t,IDENT);
			_t = _t.getNextSibling();
			identifierStar0a_AST = (AST)currentAST.root;
			break;
		}
		case DOT:
		{
			AST __t59 = _t;
			AST tmp80_AST = null;
			AST tmp80_AST_in = null;
			tmp80_AST = astFactory.create((AST)_t);
			tmp80_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp80_AST);
			ASTPair __currentAST59 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,DOT);
			_t = _t.getFirstChild();
			identifierStar0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp81_AST = null;
			AST tmp81_AST_in = null;
			tmp81_AST = astFactory.create((AST)_t);
			tmp81_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp81_AST);
			match(_t,IDENT);
			_t = _t.getNextSibling();
			currentAST = __currentAST59;
			_t = __t59;
			_t = _t.getNextSibling();
			identifierStar0a_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = identifierStar0a_AST;
		_retTree = _t;
	}

	public final void modifiersInternal(AST _t) throws RecognitionException {

		AST modifiersInternal_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST modifiersInternal_AST = null;

		{
		int _cnt64=0;
		_loop64:
		do {
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_def:
			{
				AST tmp82_AST = null;
				AST tmp82_AST_in = null;
				tmp82_AST = astFactory.create((AST)_t);
				tmp82_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp82_AST);
				match(_t,LITERAL_def);
				_t = _t.getNextSibling();
				break;
			}
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_static:
			case LITERAL_transient:
			case LITERAL_final:
			case LITERAL_abstract:
			case LITERAL_native:
			case LITERAL_threadsafe:
			case LITERAL_synchronized:
			case LITERAL_volatile:
			case LITERAL_strictfp:
			{
				modifier(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case ANNOTATION:
			{
				annotation(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				if ( _cnt64>=1 ) { break _loop64; } else {throw new NoViableAltException(_t);}
			}
			}
			_cnt64++;
		} while (true);
		}
		modifiersInternal_AST = (AST)currentAST.root;
		returnAST = modifiersInternal_AST;
		_retTree = _t;
	}

	public final void annotation(AST _t) throws RecognitionException {

		AST annotation_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotation_AST = null;

		AST __t72 = _t;
		AST tmp83_AST = null;
		AST tmp83_AST_in = null;
		tmp83_AST = astFactory.create((AST)_t);
		tmp83_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp83_AST);
		ASTPair __currentAST72 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,ANNOTATION);
		_t = _t.getFirstChild();
		identifier(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case ANNOTATION:
		case ANNOTATION_MEMBER_VALUE_PAIR:
		case LITERAL_in:
		case QUESTION:
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			annotationArguments(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case 3:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST72;
		_t = __t72;
		_t = _t.getNextSibling();
		annotation_AST = (AST)currentAST.root;
		returnAST = annotation_AST;
		_retTree = _t;
	}

	public final void annotationArguments(AST _t) throws RecognitionException {

		AST annotationArguments_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationArguments_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case ANNOTATION:
		case LITERAL_in:
		case QUESTION:
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			annotationMemberValueInitializer(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			annotationArguments_AST = (AST)currentAST.root;
			break;
		}
		case ANNOTATION_MEMBER_VALUE_PAIR:
		{
			anntotationMemberValuePairs(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			annotationArguments_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = annotationArguments_AST;
		_retTree = _t;
	}

	public final void annotationMemberValueInitializer(AST _t) throws RecognitionException {

		AST annotationMemberValueInitializer_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationMemberValueInitializer_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case LITERAL_in:
		case QUESTION:
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			annotationMemberValueInitializer_AST = (AST)currentAST.root;
			break;
		}
		case ANNOTATION:
		{
			annotation(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			annotationMemberValueInitializer_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = annotationMemberValueInitializer_AST;
		_retTree = _t;
	}

	public final void anntotationMemberValuePairs(AST _t) throws RecognitionException {

		AST anntotationMemberValuePairs_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST anntotationMemberValuePairs_AST = null;

		{
		int _cnt81=0;
		_loop81:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==ANNOTATION_MEMBER_VALUE_PAIR)) {
				annotationMemberValuePair(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt81>=1 ) { break _loop81; } else {throw new NoViableAltException(_t);}
			}

			_cnt81++;
		} while (true);
		}
		anntotationMemberValuePairs_AST = (AST)currentAST.root;
		returnAST = anntotationMemberValuePairs_AST;
		_retTree = _t;
	}

	public final void annotationMemberValuePair(AST _t) throws RecognitionException {

		AST annotationMemberValuePair_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationMemberValuePair_AST = null;

		AST __t83 = _t;
		AST tmp84_AST = null;
		AST tmp84_AST_in = null;
		tmp84_AST = astFactory.create((AST)_t);
		tmp84_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp84_AST);
		ASTPair __currentAST83 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,ANNOTATION_MEMBER_VALUE_PAIR);
		_t = _t.getFirstChild();
		AST tmp85_AST = null;
		AST tmp85_AST_in = null;
		tmp85_AST = astFactory.create((AST)_t);
		tmp85_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp85_AST);
		match(_t,IDENT);
		_t = _t.getNextSibling();
		annotationMemberValueInitializer(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST83;
		_t = __t83;
		_t = _t.getNextSibling();
		annotationMemberValuePair_AST = (AST)currentAST.root;
		returnAST = annotationMemberValuePair_AST;
		_retTree = _t;
	}

	public final void conditionalExpression(AST _t) throws RecognitionException {

		AST conditionalExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST conditionalExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case QUESTION:
		{
			AST __t327 = _t;
			AST tmp86_AST = null;
			AST tmp86_AST_in = null;
			tmp86_AST = astFactory.create((AST)_t);
			tmp86_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp86_AST);
			ASTPair __currentAST327 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,QUESTION);
			_t = _t.getFirstChild();
			logicalOrExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			assignmentExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST327;
			_t = __t327;
			_t = _t.getNextSibling();
			conditionalExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_as:
		case LITERAL_in:
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			logicalOrExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			conditionalExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = conditionalExpression_AST;
		_retTree = _t;
	}

	public final void annotationMemberArrayValueInitializer(AST _t) throws RecognitionException {

		AST annotationMemberArrayValueInitializer_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationMemberArrayValueInitializer_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case LITERAL_in:
		case QUESTION:
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			annotationMemberArrayValueInitializer_AST = (AST)currentAST.root;
			break;
		}
		case ANNOTATION:
		{
			annotation(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			annotationMemberArrayValueInitializer_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = annotationMemberArrayValueInitializer_AST;
		_retTree = _t;
	}

	public final void superClassClause(AST _t) throws RecognitionException {

		AST superClassClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST superClassClause_AST = null;

		AST __t87 = _t;
		AST tmp87_AST = null;
		AST tmp87_AST_in = null;
		tmp87_AST = astFactory.create((AST)_t);
		tmp87_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp87_AST);
		ASTPair __currentAST87 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,EXTENDS_CLAUSE);
		_t = _t.getFirstChild();
		classOrInterfaceType(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST87;
		_t = __t87;
		_t = _t.getNextSibling();
		superClassClause_AST = (AST)currentAST.root;
		returnAST = superClassClause_AST;
		_retTree = _t;
	}

	public final void typeParameters(AST _t) throws RecognitionException {

		AST typeParameters_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeParameters_AST = null;

		AST __t99 = _t;
		AST tmp88_AST = null;
		AST tmp88_AST_in = null;
		tmp88_AST = astFactory.create((AST)_t);
		tmp88_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp88_AST);
		ASTPair __currentAST99 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,TYPE_PARAMETERS);
		_t = _t.getFirstChild();
		{
		int _cnt101=0;
		_loop101:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==TYPE_PARAMETER)) {
				typeParameter(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt101>=1 ) { break _loop101; } else {throw new NoViableAltException(_t);}
			}

			_cnt101++;
		} while (true);
		}
		currentAST = __currentAST99;
		_t = __t99;
		_t = _t.getNextSibling();
		typeParameters_AST = (AST)currentAST.root;
		returnAST = typeParameters_AST;
		_retTree = _t;
	}

	public final void implementsClause(AST _t) throws RecognitionException {

		AST implementsClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST implementsClause_AST = null;

		AST __t151 = _t;
		AST tmp89_AST = null;
		AST tmp89_AST_in = null;
		tmp89_AST = astFactory.create((AST)_t);
		tmp89_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp89_AST);
		ASTPair __currentAST151 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,IMPLEMENTS_CLAUSE);
		_t = _t.getFirstChild();
		{
		_loop153:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==IDENT||_t.getType()==DOT)) {
				classOrInterfaceType(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop153;
			}

		} while (true);
		}
		currentAST = __currentAST151;
		_t = __t151;
		_t = _t.getNextSibling();
		implementsClause_AST = (AST)currentAST.root;
		returnAST = implementsClause_AST;
		_retTree = _t;
	}

	public final void classBlock(AST _t) throws RecognitionException {

		AST classBlock_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classBlock_AST = null;

		AST __t110 = _t;
		AST tmp90_AST = null;
		AST tmp90_AST_in = null;
		tmp90_AST = astFactory.create((AST)_t);
		tmp90_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp90_AST);
		ASTPair __currentAST110 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,OBJBLOCK);
		_t = _t.getFirstChild();
		{
		_loop112:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_3.member(_t.getType()))) {
				classField(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop112;
			}

		} while (true);
		}
		currentAST = __currentAST110;
		_t = __t110;
		_t = _t.getNextSibling();
		classBlock_AST = (AST)currentAST.root;
		returnAST = classBlock_AST;
		_retTree = _t;
	}

	public final void interfaceExtends(AST _t) throws RecognitionException {

		AST interfaceExtends_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST interfaceExtends_AST = null;

		AST __t147 = _t;
		AST tmp91_AST = null;
		AST tmp91_AST_in = null;
		tmp91_AST = astFactory.create((AST)_t);
		tmp91_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp91_AST);
		ASTPair __currentAST147 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,EXTENDS_CLAUSE);
		_t = _t.getFirstChild();
		{
		_loop149:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==IDENT||_t.getType()==DOT)) {
				classOrInterfaceType(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop149;
			}

		} while (true);
		}
		currentAST = __currentAST147;
		_t = __t147;
		_t = _t.getNextSibling();
		interfaceExtends_AST = (AST)currentAST.root;
		returnAST = interfaceExtends_AST;
		_retTree = _t;
	}

	public final void interfaceBlock(AST _t) throws RecognitionException {

		AST interfaceBlock_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST interfaceBlock_AST = null;

		AST __t114 = _t;
		AST tmp92_AST = null;
		AST tmp92_AST_in = null;
		tmp92_AST = astFactory.create((AST)_t);
		tmp92_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp92_AST);
		ASTPair __currentAST114 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,OBJBLOCK);
		_t = _t.getFirstChild();
		{
		_loop116:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_4.member(_t.getType()))) {
				interfaceField(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop116;
			}

		} while (true);
		}
		currentAST = __currentAST114;
		_t = __t114;
		_t = _t.getNextSibling();
		interfaceBlock_AST = (AST)currentAST.root;
		returnAST = interfaceBlock_AST;
		_retTree = _t;
	}

	public final void enumBlock(AST _t) throws RecognitionException {

		AST enumBlock_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumBlock_AST = null;

		AST __t122 = _t;
		AST tmp93_AST = null;
		AST tmp93_AST_in = null;
		tmp93_AST = astFactory.create((AST)_t);
		tmp93_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp93_AST);
		ASTPair __currentAST122 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,OBJBLOCK);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ENUM_CONSTANT_DEF:
		{
			enumConstants(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case 3:
		case IDENT:
		case DOT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		case MODIFIERS:
		case LITERAL_static:
		case CLASS_DEF:
		case INTERFACE_DEF:
		case ENUM_DEF:
		case ANNOTATION_DEF:
		case SLIST:
		case CTOR_IDENT:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		{
		_loop125:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_3.member(_t.getType()))) {
				classField(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop125;
			}

		} while (true);
		}
		currentAST = __currentAST122;
		_t = __t122;
		_t = _t.getNextSibling();
		enumBlock_AST = (AST)currentAST.root;
		returnAST = enumBlock_AST;
		_retTree = _t;
	}

	public final void annotationBlock(AST _t) throws RecognitionException {

		AST annotationBlock_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationBlock_AST = null;

		AST __t118 = _t;
		AST tmp94_AST = null;
		AST tmp94_AST_in = null;
		tmp94_AST = astFactory.create((AST)_t);
		tmp94_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp94_AST);
		ASTPair __currentAST118 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,OBJBLOCK);
		_t = _t.getFirstChild();
		{
		_loop120:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_5.member(_t.getType()))) {
				annotationField(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop120;
			}

		} while (true);
		}
		currentAST = __currentAST118;
		_t = __t118;
		_t = _t.getNextSibling();
		annotationBlock_AST = (AST)currentAST.root;
		returnAST = annotationBlock_AST;
		_retTree = _t;
	}

	public final void typeParameter(AST _t) throws RecognitionException {

		AST typeParameter_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeParameter_AST = null;

		AST __t103 = _t;
		AST tmp95_AST = null;
		AST tmp95_AST_in = null;
		tmp95_AST = astFactory.create((AST)_t);
		tmp95_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp95_AST);
		ASTPair __currentAST103 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,TYPE_PARAMETER);
		_t = _t.getFirstChild();
		AST tmp96_AST = null;
		AST tmp96_AST_in = null;
		tmp96_AST = astFactory.create((AST)_t);
		tmp96_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp96_AST);
		match(_t,IDENT);
		_t = _t.getNextSibling();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case TYPE_UPPER_BOUNDS:
		{
			typeParameterBounds(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case 3:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST103;
		_t = __t103;
		_t = _t.getNextSibling();
		typeParameter_AST = (AST)currentAST.root;
		returnAST = typeParameter_AST;
		_retTree = _t;
	}

	public final void typeParameterBounds(AST _t) throws RecognitionException {

		AST typeParameterBounds_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeParameterBounds_AST = null;

		AST __t106 = _t;
		AST tmp97_AST = null;
		AST tmp97_AST_in = null;
		tmp97_AST = astFactory.create((AST)_t);
		tmp97_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp97_AST);
		ASTPair __currentAST106 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,TYPE_UPPER_BOUNDS);
		_t = _t.getFirstChild();
		{
		int _cnt108=0;
		_loop108:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==IDENT||_t.getType()==DOT)) {
				classOrInterfaceType(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt108>=1 ) { break _loop108; } else {throw new NoViableAltException(_t);}
			}

			_cnt108++;
		} while (true);
		}
		currentAST = __currentAST106;
		_t = __t106;
		_t = _t.getNextSibling();
		typeParameterBounds_AST = (AST)currentAST.root;
		returnAST = typeParameterBounds_AST;
		_retTree = _t;
	}

	public final void classField(AST _t) throws RecognitionException {

		AST classField_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classField_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case CTOR_IDENT:
		{
			constructorDefinition(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			classField_AST = (AST)currentAST.root;
			break;
		}
		case IDENT:
		case DOT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		case MODIFIERS:
		{
			declaration(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			classField_AST = (AST)currentAST.root;
			break;
		}
		case CLASS_DEF:
		case INTERFACE_DEF:
		case ENUM_DEF:
		case ANNOTATION_DEF:
		{
			typeDefinitionInternal(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			classField_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_static:
		{
			AST tmp98_AST = null;
			AST tmp98_AST_in = null;
			tmp98_AST = astFactory.create((AST)_t);
			tmp98_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp98_AST);
			match(_t,LITERAL_static);
			_t = _t.getNextSibling();
			compoundStatement(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			classField_AST = (AST)currentAST.root;
			break;
		}
		case SLIST:
		{
			compoundStatement(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			classField_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = classField_AST;
		_retTree = _t;
	}

	public final void interfaceField(AST _t) throws RecognitionException {

		AST interfaceField_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST interfaceField_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case IDENT:
		case DOT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		case MODIFIERS:
		{
			declaration(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			interfaceField_AST = (AST)currentAST.root;
			break;
		}
		case CLASS_DEF:
		case INTERFACE_DEF:
		case ENUM_DEF:
		case ANNOTATION_DEF:
		{
			typeDefinitionInternal(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			interfaceField_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = interfaceField_AST;
		_retTree = _t;
	}

	public final void annotationField(AST _t) throws RecognitionException {

		AST annotationField_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationField_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case CLASS_DEF:
		case INTERFACE_DEF:
		case ENUM_DEF:
		case ANNOTATION_DEF:
		{
			typeDefinitionInternal(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			annotationField_AST = (AST)currentAST.root;
			break;
		}
		case ANNOTATION_FIELD_DEF:
		{
			AST __t130 = _t;
			AST tmp99_AST = null;
			AST tmp99_AST_in = null;
			tmp99_AST = astFactory.create((AST)_t);
			tmp99_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp99_AST);
			ASTPair __currentAST130 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,ANNOTATION_FIELD_DEF);
			_t = _t.getFirstChild();
			modifiersOpt(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			typeSpec(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp100_AST = null;
			AST tmp100_AST_in = null;
			tmp100_AST = astFactory.create((AST)_t);
			tmp100_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp100_AST);
			match(_t,IDENT);
			_t = _t.getNextSibling();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_default:
			{
				AST tmp101_AST = null;
				AST tmp101_AST_in = null;
				tmp101_AST = astFactory.create((AST)_t);
				tmp101_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp101_AST);
				match(_t,LITERAL_default);
				_t = _t.getNextSibling();
				annotationMemberValueInitializer(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST130;
			_t = __t130;
			_t = _t.getNextSibling();
			annotationField_AST = (AST)currentAST.root;
			break;
		}
		case VARIABLE_DEF:
		{
			variableDefinition(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			annotationField_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = annotationField_AST;
		_retTree = _t;
	}

	public final void enumConstants(AST _t) throws RecognitionException {

		AST enumConstants_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumConstants_AST = null;

		{
		int _cnt128=0;
		_loop128:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==ENUM_CONSTANT_DEF)) {
				enumConstant(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt128>=1 ) { break _loop128; } else {throw new NoViableAltException(_t);}
			}

			_cnt128++;
		} while (true);
		}
		enumConstants_AST = (AST)currentAST.root;
		returnAST = enumConstants_AST;
		_retTree = _t;
	}

	public final void enumConstant(AST _t) throws RecognitionException {

		AST enumConstant_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumConstant_AST = null;

		AST __t133 = _t;
		AST tmp102_AST = null;
		AST tmp102_AST_in = null;
		tmp102_AST = astFactory.create((AST)_t);
		tmp102_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp102_AST);
		ASTPair __currentAST133 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,ENUM_CONSTANT_DEF);
		_t = _t.getFirstChild();
		annotationsOpt(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		AST tmp103_AST = null;
		AST tmp103_AST_in = null;
		tmp103_AST = astFactory.create((AST)_t);
		tmp103_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp103_AST);
		match(_t,IDENT);
		_t = _t.getNextSibling();
		argList(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case OBJBLOCK:
		{
			enumConstantBlock(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case 3:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST133;
		_t = __t133;
		_t = _t.getNextSibling();
		enumConstant_AST = (AST)currentAST.root;
		returnAST = enumConstant_AST;
		_retTree = _t;
	}

	public final void argList(AST _t) throws RecognitionException {

		AST argList_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST argList_AST = null;

		{
		_loop493:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_6.member(_t.getType()))) {
				argument(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop493;
			}

		} while (true);
		}
		argList_AST = (AST)currentAST.root;
		returnAST = argList_AST;
		_retTree = _t;
	}

	public final void enumConstantBlock(AST _t) throws RecognitionException {

		AST enumConstantBlock_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumConstantBlock_AST = null;

		AST __t136 = _t;
		AST tmp104_AST = null;
		AST tmp104_AST_in = null;
		tmp104_AST = astFactory.create((AST)_t);
		tmp104_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp104_AST);
		ASTPair __currentAST136 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,OBJBLOCK);
		_t = _t.getFirstChild();
		{
		_loop138:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_7.member(_t.getType()))) {
				enumConstantField(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop138;
			}

		} while (true);
		}
		currentAST = __currentAST136;
		_t = __t136;
		_t = _t.getNextSibling();
		enumConstantBlock_AST = (AST)currentAST.root;
		returnAST = enumConstantBlock_AST;
		_retTree = _t;
	}

	public final void enumConstantField(AST _t) throws RecognitionException {

		AST enumConstantField_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumConstantField_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case CLASS_DEF:
		case INTERFACE_DEF:
		case ENUM_DEF:
		case ANNOTATION_DEF:
		{
			typeDefinitionInternal(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			enumConstantField_AST = (AST)currentAST.root;
			break;
		}
		case METHOD_DEF:
		case VARIABLE_DEF:
		{
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case METHOD_DEF:
			{
				AST __t141 = _t;
				AST tmp105_AST = null;
				AST tmp105_AST_in = null;
				tmp105_AST = astFactory.create((AST)_t);
				tmp105_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp105_AST);
				ASTPair __currentAST141 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,METHOD_DEF);
				_t = _t.getFirstChild();
				modifiersOpt(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case TYPE_PARAMETERS:
				{
					typeParameters(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case IDENT:
				case DOT:
				case LITERAL_void:
				case LITERAL_boolean:
				case LITERAL_byte:
				case LITERAL_char:
				case LITERAL_short:
				case LITERAL_int:
				case LITERAL_float:
				case LITERAL_long:
				case LITERAL_double:
				case LITERAL_any:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				typeSpec(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				AST tmp106_AST = null;
				AST tmp106_AST_in = null;
				tmp106_AST = astFactory.create((AST)_t);
				tmp106_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp106_AST);
				match(_t,IDENT);
				_t = _t.getNextSibling();
				parameterDeclarationList(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case LITERAL_throws:
				{
					throwsClause(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case 3:
				case SLIST:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case SLIST:
				{
					compoundStatement(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				currentAST = __currentAST141;
				_t = __t141;
				_t = _t.getNextSibling();
				break;
			}
			case VARIABLE_DEF:
			{
				variableDefinition(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			enumConstantField_AST = (AST)currentAST.root;
			break;
		}
		case INSTANCE_INIT:
		{
			AST __t145 = _t;
			AST tmp107_AST = null;
			AST tmp107_AST_in = null;
			tmp107_AST = astFactory.create((AST)_t);
			tmp107_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp107_AST);
			ASTPair __currentAST145 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,INSTANCE_INIT);
			_t = _t.getFirstChild();
			compoundStatement(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST145;
			_t = __t145;
			_t = _t.getNextSibling();
			enumConstantField_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = enumConstantField_AST;
		_retTree = _t;
	}

	public final void parameterDeclarationList(AST _t) throws RecognitionException {

		AST parameterDeclarationList_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parameterDeclarationList_AST = null;

		AST __t183 = _t;
		AST tmp108_AST = null;
		AST tmp108_AST_in = null;
		tmp108_AST = astFactory.create((AST)_t);
		tmp108_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp108_AST);
		ASTPair __currentAST183 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,PARAMETERS);
		_t = _t.getFirstChild();
		{
		_loop185:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==PARAMETER_DEF||_t.getType()==VARIABLE_PARAMETER_DEF)) {
				parameterDeclaration(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop185;
			}

		} while (true);
		}
		currentAST = __currentAST183;
		_t = __t183;
		_t = _t.getNextSibling();
		parameterDeclarationList_AST = (AST)currentAST.root;
		returnAST = parameterDeclarationList_AST;
		_retTree = _t;
	}

	public final void throwsClause(AST _t) throws RecognitionException {

		AST throwsClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST throwsClause_AST = null;

		AST __t179 = _t;
		AST tmp109_AST = null;
		AST tmp109_AST_in = null;
		tmp109_AST = astFactory.create((AST)_t);
		tmp109_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp109_AST);
		ASTPair __currentAST179 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,LITERAL_throws);
		_t = _t.getFirstChild();
		{
		int _cnt181=0;
		_loop181:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==IDENT||_t.getType()==DOT)) {
				identifier(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt181>=1 ) { break _loop181; } else {throw new NoViableAltException(_t);}
			}

			_cnt181++;
		} while (true);
		}
		currentAST = __currentAST179;
		_t = __t179;
		_t = _t.getNextSibling();
		throwsClause_AST = (AST)currentAST.root;
		returnAST = throwsClause_AST;
		_retTree = _t;
	}

	public final void compoundStatement(AST _t) throws RecognitionException {

		AST compoundStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST compoundStatement_AST = null;

		openBlock(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		compoundStatement_AST = (AST)currentAST.root;
		returnAST = compoundStatement_AST;
		_retTree = _t;
	}

	public final void constructorDefinition(AST _t) throws RecognitionException {

		AST constructorDefinition_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constructorDefinition_AST = null;

		AST __t164 = _t;
		AST tmp110_AST = null;
		AST tmp110_AST_in = null;
		tmp110_AST = astFactory.create((AST)_t);
		tmp110_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp110_AST);
		ASTPair __currentAST164 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,CTOR_IDENT);
		_t = _t.getFirstChild();
		AST tmp111_AST = null;
		AST tmp111_AST_in = null;
		tmp111_AST = astFactory.create((AST)_t);
		tmp111_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp111_AST);
		match(_t,IDENT);
		_t = _t.getNextSibling();
		parameterDeclarationList(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_throws:
		{
			throwsClause(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case SLIST:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		constructorBody(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST164;
		_t = __t164;
		_t = _t.getNextSibling();
		constructorDefinition_AST = (AST)currentAST.root;
		returnAST = constructorDefinition_AST;
		_retTree = _t;
	}

	public final void constructorBody(AST _t) throws RecognitionException {

		AST constructorBody_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constructorBody_AST = null;

		AST __t157 = _t;
		AST tmp112_AST = null;
		AST tmp112_AST_in = null;
		tmp112_AST = astFactory.create((AST)_t);
		tmp112_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp112_AST);
		ASTPair __currentAST157 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,SLIST);
		_t = _t.getFirstChild();
		explicitConstructorInvocation(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		blockBody(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST157;
		_t = __t157;
		_t = _t.getNextSibling();
		constructorBody_AST = (AST)currentAST.root;
		returnAST = constructorBody_AST;
		_retTree = _t;
	}

	public final void explicitConstructorInvocation(AST _t) throws RecognitionException {

		AST explicitConstructorInvocation_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST explicitConstructorInvocation_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case CTOR_CALL:
		{
			AST __t159 = _t;
			AST tmp113_AST = null;
			AST tmp113_AST_in = null;
			tmp113_AST = astFactory.create((AST)_t);
			tmp113_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp113_AST);
			ASTPair __currentAST159 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,CTOR_CALL);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TYPE_ARGUMENTS:
			{
				typeArguments(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			case LITERAL_as:
			case ANNOTATION:
			case VARIABLE_DEF:
			case ASSIGN:
			case SPREAD_ARG:
			case LITERAL_break:
			case LITERAL_continue:
			case LITERAL_throw:
			case LITERAL_assert:
			case LITERAL_return:
			case LITERAL_in:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case STAR_STAR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case REGEX_FIND:
			case REGEX_MATCH:
			case NOT_EQUAL:
			case EQUAL:
			case COMPARE_TO:
			case LT:
			case GT:
			case LE:
			case GE:
			case LITERAL_instanceof:
			case SL:
			case SR:
			case BSR:
			case RANGE_INCLUSIVE:
			case RANGE_EXCLUSIVE:
			case PLUS:
			case MINUS:
			case INC:
			case STAR:
			case DIV:
			case MOD:
			case DEC:
			case STAR_STAR:
			case MEMBER_POINTER_DEFAULT:
			case BNOT:
			case LNOT:
			case TYPECAST:
			case LABELED_ARG:
			case SPREAD_MAP_ARG:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			argList(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST159;
			_t = __t159;
			_t = _t.getNextSibling();
			explicitConstructorInvocation_AST = (AST)currentAST.root;
			break;
		}
		case SUPER_CTOR_CALL:
		{
			AST __t161 = _t;
			AST tmp114_AST = null;
			AST tmp114_AST_in = null;
			tmp114_AST = astFactory.create((AST)_t);
			tmp114_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp114_AST);
			ASTPair __currentAST161 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SUPER_CTOR_CALL);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TYPE_ARGUMENTS:
			{
				typeArguments(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			case LITERAL_as:
			case ANNOTATION:
			case VARIABLE_DEF:
			case ASSIGN:
			case SPREAD_ARG:
			case LITERAL_break:
			case LITERAL_continue:
			case LITERAL_throw:
			case LITERAL_assert:
			case LITERAL_return:
			case LITERAL_in:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case STAR_STAR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case REGEX_FIND:
			case REGEX_MATCH:
			case NOT_EQUAL:
			case EQUAL:
			case COMPARE_TO:
			case LT:
			case GT:
			case LE:
			case GE:
			case LITERAL_instanceof:
			case SL:
			case SR:
			case BSR:
			case RANGE_INCLUSIVE:
			case RANGE_EXCLUSIVE:
			case PLUS:
			case MINUS:
			case INC:
			case STAR:
			case DIV:
			case MOD:
			case DEC:
			case STAR_STAR:
			case MEMBER_POINTER_DEFAULT:
			case BNOT:
			case LNOT:
			case TYPECAST:
			case LABELED_ARG:
			case SPREAD_MAP_ARG:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			argList(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST161;
			_t = __t161;
			_t = _t.getNextSibling();
			explicitConstructorInvocation_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = explicitConstructorInvocation_AST;
		_retTree = _t;
	}

	public final void variableDeclarator(AST _t) throws RecognitionException {

		AST variableDeclarator_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST variableDeclarator_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case IDENT:
		{
			AST tmp115_AST = null;
			AST tmp115_AST_in = null;
			tmp115_AST = astFactory.create((AST)_t);
			tmp115_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp115_AST);
			match(_t,IDENT);
			_t = _t.getNextSibling();
			variableDeclarator_AST = (AST)currentAST.root;
			break;
		}
		case LBRACK:
		{
			AST tmp116_AST = null;
			AST tmp116_AST_in = null;
			tmp116_AST = astFactory.create((AST)_t);
			tmp116_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp116_AST);
			match(_t,LBRACK);
			_t = _t.getNextSibling();
			variableDeclarator(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			variableDeclarator_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = variableDeclarator_AST;
		_retTree = _t;
	}

	public final void declaratorBrackets0a(AST _t) throws RecognitionException {

		AST declaratorBrackets0a_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST declaratorBrackets0a_AST = null;

		AST __t172 = _t;
		AST tmp117_AST = null;
		AST tmp117_AST_in = null;
		tmp117_AST = astFactory.create((AST)_t);
		tmp117_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp117_AST);
		ASTPair __currentAST172 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,ARRAY_DECLARATOR);
		_t = _t.getFirstChild();
		declaratorBrackets0a(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST172;
		_t = __t172;
		_t = _t.getNextSibling();
		declaratorBrackets0a_AST = (AST)currentAST.root;
		returnAST = declaratorBrackets0a_AST;
		_retTree = _t;
	}

	public final void expression(AST _t) throws RecognitionException {

		AST expression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expression_AST = null;

		assignmentExpression(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		expression_AST = (AST)currentAST.root;
		returnAST = expression_AST;
		_retTree = _t;
	}

	public final void parameterDefinition(AST _t) throws RecognitionException {

		AST parameterDefinition_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parameterDefinition_AST = null;

		AST __t177 = _t;
		AST tmp118_AST = null;
		AST tmp118_AST_in = null;
		tmp118_AST = astFactory.create((AST)_t);
		tmp118_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp118_AST);
		ASTPair __currentAST177 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,PARAMETER_DEF);
		_t = _t.getFirstChild();
		modifiers(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		typeSpec(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		AST tmp119_AST = null;
		AST tmp119_AST_in = null;
		tmp119_AST = astFactory.create((AST)_t);
		tmp119_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp119_AST);
		match(_t,IDENT);
		_t = _t.getNextSibling();
		currentAST = __currentAST177;
		_t = __t177;
		_t = _t.getNextSibling();
		parameterDefinition_AST = (AST)currentAST.root;
		returnAST = parameterDefinition_AST;
		_retTree = _t;
	}

	public final void parameterDeclaration(AST _t) throws RecognitionException {

		AST parameterDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parameterDeclaration_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case VARIABLE_PARAMETER_DEF:
		{
			AST __t187 = _t;
			AST tmp120_AST = null;
			AST tmp120_AST_in = null;
			tmp120_AST = astFactory.create((AST)_t);
			tmp120_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp120_AST);
			ASTPair __currentAST187 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,VARIABLE_PARAMETER_DEF);
			_t = _t.getFirstChild();
			parameterModifiersOpt(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			typeSpec(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TRIPLE_DOT:
			{
				AST tmp121_AST = null;
				AST tmp121_AST_in = null;
				tmp121_AST = astFactory.create((AST)_t);
				tmp121_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp121_AST);
				match(_t,TRIPLE_DOT);
				_t = _t.getNextSibling();
				break;
			}
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			AST tmp122_AST = null;
			AST tmp122_AST_in = null;
			tmp122_AST = astFactory.create((AST)_t);
			tmp122_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp122_AST);
			match(_t,IDENT);
			_t = _t.getNextSibling();
			varInitializer(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST187;
			_t = __t187;
			_t = _t.getNextSibling();
			parameterDeclaration_AST = (AST)currentAST.root;
			break;
		}
		case PARAMETER_DEF:
		{
			AST __t189 = _t;
			AST tmp123_AST = null;
			AST tmp123_AST_in = null;
			tmp123_AST = astFactory.create((AST)_t);
			tmp123_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp123_AST);
			ASTPair __currentAST189 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,PARAMETER_DEF);
			_t = _t.getFirstChild();
			parameterModifiersOpt(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			typeSpec(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TRIPLE_DOT:
			{
				AST tmp124_AST = null;
				AST tmp124_AST_in = null;
				tmp124_AST = astFactory.create((AST)_t);
				tmp124_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp124_AST);
				match(_t,TRIPLE_DOT);
				_t = _t.getNextSibling();
				break;
			}
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			AST tmp125_AST = null;
			AST tmp125_AST_in = null;
			tmp125_AST = astFactory.create((AST)_t);
			tmp125_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp125_AST);
			match(_t,IDENT);
			_t = _t.getNextSibling();
			varInitializer(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST189;
			_t = __t189;
			_t = _t.getNextSibling();
			parameterDeclaration_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = parameterDeclaration_AST;
		_retTree = _t;
	}

	public final void parameterModifiersOpt(AST _t) throws RecognitionException {

		AST parameterModifiersOpt_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parameterModifiersOpt_AST = null;

		AST __t198 = _t;
		AST tmp126_AST = null;
		AST tmp126_AST_in = null;
		tmp126_AST = astFactory.create((AST)_t);
		tmp126_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp126_AST);
		ASTPair __currentAST198 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,MODIFIERS);
		_t = _t.getFirstChild();
		{
		_loop200:
		do {
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_def:
			{
				AST tmp127_AST = null;
				AST tmp127_AST_in = null;
				tmp127_AST = astFactory.create((AST)_t);
				tmp127_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp127_AST);
				match(_t,LITERAL_def);
				_t = _t.getNextSibling();
				break;
			}
			case LITERAL_final:
			{
				AST tmp128_AST = null;
				AST tmp128_AST_in = null;
				tmp128_AST = astFactory.create((AST)_t);
				tmp128_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp128_AST);
				match(_t,LITERAL_final);
				_t = _t.getNextSibling();
				break;
			}
			case ANNOTATION:
			{
				annotation(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				break _loop200;
			}
			}
		} while (true);
		}
		currentAST = __currentAST198;
		_t = __t198;
		_t = _t.getNextSibling();
		parameterModifiersOpt_AST = (AST)currentAST.root;
		returnAST = parameterModifiersOpt_AST;
		_retTree = _t;
	}

	public final void simpleParameterDeclaration(AST _t) throws RecognitionException {

		AST simpleParameterDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST simpleParameterDeclaration_AST = null;

		AST __t192 = _t;
		AST tmp129_AST = null;
		AST tmp129_AST_in = null;
		tmp129_AST = astFactory.create((AST)_t);
		tmp129_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp129_AST);
		ASTPair __currentAST192 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,PARAMETER_DEF);
		_t = _t.getFirstChild();
		modifiers(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		typeSpec(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		AST tmp130_AST = null;
		AST tmp130_AST_in = null;
		tmp130_AST = astFactory.create((AST)_t);
		tmp130_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp130_AST);
		match(_t,IDENT);
		_t = _t.getNextSibling();
		currentAST = __currentAST192;
		_t = __t192;
		_t = _t.getNextSibling();
		simpleParameterDeclaration_AST = (AST)currentAST.root;
		returnAST = simpleParameterDeclaration_AST;
		_retTree = _t;
	}

	public final void simpleParameterDeclarationList(AST _t) throws RecognitionException {

		AST simpleParameterDeclarationList_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST simpleParameterDeclarationList_AST = null;

		AST __t194 = _t;
		AST tmp131_AST = null;
		AST tmp131_AST_in = null;
		tmp131_AST = astFactory.create((AST)_t);
		tmp131_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp131_AST);
		ASTPair __currentAST194 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,PARAMETERS);
		_t = _t.getFirstChild();
		{
		int _cnt196=0;
		_loop196:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==PARAMETER_DEF)) {
				simpleParameterDeclaration(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt196>=1 ) { break _loop196; } else {throw new NoViableAltException(_t);}
			}

			_cnt196++;
		} while (true);
		}
		currentAST = __currentAST194;
		_t = __t194;
		_t = _t.getNextSibling();
		simpleParameterDeclarationList_AST = (AST)currentAST.root;
		returnAST = simpleParameterDeclarationList_AST;
		_retTree = _t;
	}

	public final void closureParametersOpt(AST _t) throws RecognitionException {

		AST closureParametersOpt_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST closureParametersOpt_AST = null;

		parameterDeclarationList(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		closureParametersOpt_AST = (AST)currentAST.root;
		returnAST = closureParametersOpt_AST;
		_retTree = _t;
	}

	public final void closureParameter(AST _t) throws RecognitionException {

		AST closureParameter_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST closureParameter_AST = null;

		AST __t203 = _t;
		AST tmp132_AST = null;
		AST tmp132_AST_in = null;
		tmp132_AST = astFactory.create((AST)_t);
		tmp132_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp132_AST);
		ASTPair __currentAST203 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,PARAMETER_DEF);
		_t = _t.getFirstChild();
		AST tmp133_AST = null;
		AST tmp133_AST_in = null;
		tmp133_AST = astFactory.create((AST)_t);
		tmp133_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp133_AST);
		match(_t,IDENT);
		_t = _t.getNextSibling();
		currentAST = __currentAST203;
		_t = __t203;
		_t = _t.getNextSibling();
		closureParameter_AST = (AST)currentAST.root;
		returnAST = closureParameter_AST;
		_retTree = _t;
	}

	public final void openBlock(AST _t) throws RecognitionException {

		AST openBlock_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST openBlock_AST = null;

		AST __t206 = _t;
		AST tmp134_AST = null;
		AST tmp134_AST_in = null;
		tmp134_AST = astFactory.create((AST)_t);
		tmp134_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp134_AST);
		ASTPair __currentAST206 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,SLIST);
		_t = _t.getFirstChild();
		blockBody(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST206;
		_t = __t206;
		_t = _t.getNextSibling();
		openBlock_AST = (AST)currentAST.root;
		returnAST = openBlock_AST;
		_retTree = _t;
	}

	public final void closedBlock(AST _t) throws RecognitionException {

		AST closedBlock_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST closedBlock_AST = null;

		AST __t211 = _t;
		AST tmp135_AST = null;
		AST tmp135_AST_in = null;
		tmp135_AST = astFactory.create((AST)_t);
		tmp135_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp135_AST);
		ASTPair __currentAST211 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,CLOSED_BLOCK);
		_t = _t.getFirstChild();
		closureParametersOpt(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		blockBody(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST211;
		_t = __t211;
		_t = _t.getNextSibling();
		closedBlock_AST = (AST)currentAST.root;
		returnAST = closedBlock_AST;
		_retTree = _t;
	}

	public final void openOrClosedBlock(AST _t) throws RecognitionException {

		AST openOrClosedBlock_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST openOrClosedBlock_AST = null;

		AST __t213 = _t;
		AST tmp136_AST = null;
		AST tmp136_AST_in = null;
		tmp136_AST = astFactory.create((AST)_t);
		tmp136_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp136_AST);
		ASTPair __currentAST213 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,LCURLY);
		_t = _t.getFirstChild();
		closureParametersOpt(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		blockBody(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST213;
		_t = __t213;
		_t = _t.getNextSibling();
		openOrClosedBlock_AST = (AST)currentAST.root;
		returnAST = openOrClosedBlock_AST;
		_retTree = _t;
	}

	public final void statementList(AST _t) throws RecognitionException {

		AST statementList_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST statementList_AST = null;

		AST __t215 = _t;
		AST tmp137_AST = null;
		AST tmp137_AST_in = null;
		tmp137_AST = astFactory.create((AST)_t);
		tmp137_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp137_AST);
		ASTPair __currentAST215 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,SLIST);
		_t = _t.getFirstChild();
		{
		_loop217:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_0.member(_t.getType()))) {
				statement(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop217;
			}

		} while (true);
		}
		currentAST = __currentAST215;
		_t = __t215;
		_t = _t.getNextSibling();
		statementList_AST = (AST)currentAST.root;
		returnAST = statementList_AST;
		_retTree = _t;
	}

	public final void expressionStatement(AST _t) throws RecognitionException {

		AST expressionStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expressionStatement_AST = null;

		AST __t245 = _t;
		AST tmp138_AST = null;
		AST tmp138_AST_in = null;
		tmp138_AST = astFactory.create((AST)_t);
		tmp138_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp138_AST);
		ASTPair __currentAST245 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,EXPR);
		_t = _t.getFirstChild();
		expression(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case METHOD_CALL:
		{
			commandArguments(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case 3:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST245;
		_t = __t245;
		_t = _t.getNextSibling();
		expressionStatement_AST = (AST)currentAST.root;
		returnAST = expressionStatement_AST;
		_retTree = _t;
	}

	public final void forStatement(AST _t) throws RecognitionException {

		AST forStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forStatement_AST = null;

		AST __t224 = _t;
		AST tmp139_AST = null;
		AST tmp139_AST_in = null;
		tmp139_AST = astFactory.create((AST)_t);
		tmp139_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp139_AST);
		ASTPair __currentAST224 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,LITERAL_for);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case FOR_INIT:
		{
			traditionalForClause(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case FOR_IN_ITERABLE:
		{
			forInClause(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		compatibleBodyStatement(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST224;
		_t = __t224;
		_t = _t.getNextSibling();
		forStatement_AST = (AST)currentAST.root;
		returnAST = forStatement_AST;
		_retTree = _t;
	}

	public final void strictContextExpression(AST _t) throws RecognitionException {

		AST strictContextExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST strictContextExpression_AST = null;

		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case VARIABLE_DEF:
		{
			singleDeclaration(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case LITERAL_as:
		case ASSIGN:
		case LITERAL_in:
		case PLUS_ASSIGN:
		case MINUS_ASSIGN:
		case STAR_ASSIGN:
		case DIV_ASSIGN:
		case MOD_ASSIGN:
		case SR_ASSIGN:
		case BSR_ASSIGN:
		case SL_ASSIGN:
		case BAND_ASSIGN:
		case BXOR_ASSIGN:
		case BOR_ASSIGN:
		case STAR_STAR_ASSIGN:
		case QUESTION:
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			expression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		case LITERAL_return:
		{
			branchStatement(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case ANNOTATION:
		{
			annotation(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		strictContextExpression_AST = (AST)currentAST.root;
		returnAST = strictContextExpression_AST;
		_retTree = _t;
	}

	public final void compatibleBodyStatement(AST _t) throws RecognitionException {

		AST compatibleBodyStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST compatibleBodyStatement_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case SLIST:
		{
			compoundStatement(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			compatibleBodyStatement_AST = (AST)currentAST.root;
			break;
		}
		case STATIC_IMPORT:
		case IMPORT:
		case IDENT:
		case DOT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		case MODIFIERS:
		case LITERAL_synchronized:
		case CLASS_DEF:
		case INTERFACE_DEF:
		case ENUM_DEF:
		case ANNOTATION_DEF:
		case LCURLY:
		case LITERAL_while:
		case LITERAL_with:
		case SPREAD_ARG:
		case LITERAL_for:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		case LITERAL_return:
		case EXPR:
		case LITERAL_try:
		{
			statement(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			compatibleBodyStatement_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = compatibleBodyStatement_AST;
		_retTree = _t;
	}

	public final void tryBlock(AST _t) throws RecognitionException {

		AST tryBlock_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST tryBlock_AST = null;

		AST __t268 = _t;
		AST tmp140_AST = null;
		AST tmp140_AST_in = null;
		tmp140_AST = astFactory.create((AST)_t);
		tmp140_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp140_AST);
		ASTPair __currentAST268 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,LITERAL_try);
		_t = _t.getFirstChild();
		compoundStatement(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop270:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==LITERAL_catch)) {
				handler(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop270;
			}

		} while (true);
		}
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_finally:
		{
			finallyClause(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case 3:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST268;
		_t = __t268;
		_t = _t.getNextSibling();
		tryBlock_AST = (AST)currentAST.root;
		returnAST = tryBlock_AST;
		_retTree = _t;
	}

	public final void branchStatement(AST _t) throws RecognitionException {

		AST branchStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST branchStatement_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_break:
		{
			AST __t231 = _t;
			AST tmp141_AST = null;
			AST tmp141_AST_in = null;
			tmp141_AST = astFactory.create((AST)_t);
			tmp141_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp141_AST);
			ASTPair __currentAST231 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_break);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LABELED_STAT:
			{
				statementLabelPrefix(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			case LITERAL_as:
			case ASSIGN:
			case LITERAL_in:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case STAR_STAR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case REGEX_FIND:
			case REGEX_MATCH:
			case NOT_EQUAL:
			case EQUAL:
			case COMPARE_TO:
			case LT:
			case GT:
			case LE:
			case GE:
			case LITERAL_instanceof:
			case SL:
			case SR:
			case BSR:
			case RANGE_INCLUSIVE:
			case RANGE_EXCLUSIVE:
			case PLUS:
			case MINUS:
			case INC:
			case STAR:
			case DIV:
			case MOD:
			case DEC:
			case STAR_STAR:
			case MEMBER_POINTER_DEFAULT:
			case BNOT:
			case LNOT:
			case TYPECAST:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_as:
			case ASSIGN:
			case LITERAL_in:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case STAR_STAR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case REGEX_FIND:
			case REGEX_MATCH:
			case NOT_EQUAL:
			case EQUAL:
			case COMPARE_TO:
			case LT:
			case GT:
			case LE:
			case GE:
			case LITERAL_instanceof:
			case SL:
			case SR:
			case BSR:
			case RANGE_INCLUSIVE:
			case RANGE_EXCLUSIVE:
			case PLUS:
			case MINUS:
			case INC:
			case STAR:
			case DIV:
			case MOD:
			case DEC:
			case STAR_STAR:
			case MEMBER_POINTER_DEFAULT:
			case BNOT:
			case LNOT:
			case TYPECAST:
			{
				expression(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST231;
			_t = __t231;
			_t = _t.getNextSibling();
			branchStatement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_continue:
		{
			AST __t234 = _t;
			AST tmp142_AST = null;
			AST tmp142_AST_in = null;
			tmp142_AST = astFactory.create((AST)_t);
			tmp142_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp142_AST);
			ASTPair __currentAST234 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_continue);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LABELED_STAT:
			{
				statementLabelPrefix(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			case LITERAL_as:
			case ASSIGN:
			case LITERAL_in:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case STAR_STAR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case REGEX_FIND:
			case REGEX_MATCH:
			case NOT_EQUAL:
			case EQUAL:
			case COMPARE_TO:
			case LT:
			case GT:
			case LE:
			case GE:
			case LITERAL_instanceof:
			case SL:
			case SR:
			case BSR:
			case RANGE_INCLUSIVE:
			case RANGE_EXCLUSIVE:
			case PLUS:
			case MINUS:
			case INC:
			case STAR:
			case DIV:
			case MOD:
			case DEC:
			case STAR_STAR:
			case MEMBER_POINTER_DEFAULT:
			case BNOT:
			case LNOT:
			case TYPECAST:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_as:
			case ASSIGN:
			case LITERAL_in:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case STAR_STAR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case REGEX_FIND:
			case REGEX_MATCH:
			case NOT_EQUAL:
			case EQUAL:
			case COMPARE_TO:
			case LT:
			case GT:
			case LE:
			case GE:
			case LITERAL_instanceof:
			case SL:
			case SR:
			case BSR:
			case RANGE_INCLUSIVE:
			case RANGE_EXCLUSIVE:
			case PLUS:
			case MINUS:
			case INC:
			case STAR:
			case DIV:
			case MOD:
			case DEC:
			case STAR_STAR:
			case MEMBER_POINTER_DEFAULT:
			case BNOT:
			case LNOT:
			case TYPECAST:
			{
				expression(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST234;
			_t = __t234;
			_t = _t.getNextSibling();
			branchStatement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_throw:
		{
			AST __t237 = _t;
			AST tmp143_AST = null;
			AST tmp143_AST_in = null;
			tmp143_AST = astFactory.create((AST)_t);
			tmp143_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp143_AST);
			ASTPair __currentAST237 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_throw);
			_t = _t.getFirstChild();
			expression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST237;
			_t = __t237;
			_t = _t.getNextSibling();
			branchStatement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_assert:
		{
			AST __t238 = _t;
			AST tmp144_AST = null;
			AST tmp144_AST_in = null;
			tmp144_AST = astFactory.create((AST)_t);
			tmp144_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp144_AST);
			ASTPair __currentAST238 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_assert);
			_t = _t.getFirstChild();
			expression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_as:
			case ASSIGN:
			case LITERAL_in:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case STAR_STAR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case REGEX_FIND:
			case REGEX_MATCH:
			case NOT_EQUAL:
			case EQUAL:
			case COMPARE_TO:
			case LT:
			case GT:
			case LE:
			case GE:
			case LITERAL_instanceof:
			case SL:
			case SR:
			case BSR:
			case RANGE_INCLUSIVE:
			case RANGE_EXCLUSIVE:
			case PLUS:
			case MINUS:
			case INC:
			case STAR:
			case DIV:
			case MOD:
			case DEC:
			case STAR_STAR:
			case MEMBER_POINTER_DEFAULT:
			case BNOT:
			case LNOT:
			case TYPECAST:
			{
				expression(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST238;
			_t = __t238;
			_t = _t.getNextSibling();
			branchStatement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_return:
		{
			AST __t240 = _t;
			AST tmp145_AST = null;
			AST tmp145_AST_in = null;
			tmp145_AST = astFactory.create((AST)_t);
			tmp145_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp145_AST);
			ASTPair __currentAST240 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_return);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_as:
			case ASSIGN:
			case LITERAL_in:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case STAR_STAR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case REGEX_FIND:
			case REGEX_MATCH:
			case NOT_EQUAL:
			case EQUAL:
			case COMPARE_TO:
			case LT:
			case GT:
			case LE:
			case GE:
			case LITERAL_instanceof:
			case SL:
			case SR:
			case BSR:
			case RANGE_INCLUSIVE:
			case RANGE_EXCLUSIVE:
			case PLUS:
			case MINUS:
			case INC:
			case STAR:
			case DIV:
			case MOD:
			case DEC:
			case STAR_STAR:
			case MEMBER_POINTER_DEFAULT:
			case BNOT:
			case LNOT:
			case TYPECAST:
			{
				expression(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST240;
			_t = __t240;
			_t = _t.getNextSibling();
			branchStatement_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = branchStatement_AST;
		_retTree = _t;
	}

	public final void traditionalForClause(AST _t) throws RecognitionException {

		AST traditionalForClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST traditionalForClause_AST = null;

		forInit(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		forCond(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		forIter(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		traditionalForClause_AST = (AST)currentAST.root;
		returnAST = traditionalForClause_AST;
		_retTree = _t;
	}

	public final void forInClause(AST _t) throws RecognitionException {

		AST forInClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forInClause_AST = null;

		AST __t228 = _t;
		AST tmp146_AST = null;
		AST tmp146_AST_in = null;
		tmp146_AST = astFactory.create((AST)_t);
		tmp146_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp146_AST);
		ASTPair __currentAST228 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,FOR_IN_ITERABLE);
		_t = _t.getFirstChild();
		variableDefinition(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		expression(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST228;
		_t = __t228;
		_t = _t.getNextSibling();
		forInClause_AST = (AST)currentAST.root;
		returnAST = forInClause_AST;
		_retTree = _t;
	}

	public final void forInit(AST _t) throws RecognitionException {

		AST forInit_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forInit_AST = null;

		AST __t258 = _t;
		AST tmp147_AST = null;
		AST tmp147_AST_in = null;
		tmp147_AST = astFactory.create((AST)_t);
		tmp147_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp147_AST);
		ASTPair __currentAST258 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,FOR_INIT);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case IDENT:
		case DOT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		case MODIFIERS:
		{
			declaration(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case 3:
		case ELIST:
		{
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ELIST:
			{
				controlExpressionList(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST258;
		_t = __t258;
		_t = _t.getNextSibling();
		forInit_AST = (AST)currentAST.root;
		returnAST = forInit_AST;
		_retTree = _t;
	}

	public final void forCond(AST _t) throws RecognitionException {

		AST forCond_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forCond_AST = null;

		AST __t262 = _t;
		AST tmp148_AST = null;
		AST tmp148_AST_in = null;
		tmp148_AST = astFactory.create((AST)_t);
		tmp148_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp148_AST);
		ASTPair __currentAST262 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,FOR_CONDITION);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case ANNOTATION:
		case VARIABLE_DEF:
		case ASSIGN:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		case LITERAL_return:
		case LITERAL_in:
		case PLUS_ASSIGN:
		case MINUS_ASSIGN:
		case STAR_ASSIGN:
		case DIV_ASSIGN:
		case MOD_ASSIGN:
		case SR_ASSIGN:
		case BSR_ASSIGN:
		case SL_ASSIGN:
		case BAND_ASSIGN:
		case BXOR_ASSIGN:
		case BOR_ASSIGN:
		case STAR_STAR_ASSIGN:
		case QUESTION:
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			strictContextExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case 3:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST262;
		_t = __t262;
		_t = _t.getNextSibling();
		forCond_AST = (AST)currentAST.root;
		returnAST = forCond_AST;
		_retTree = _t;
	}

	public final void forIter(AST _t) throws RecognitionException {

		AST forIter_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forIter_AST = null;

		AST __t265 = _t;
		AST tmp149_AST = null;
		AST tmp149_AST_in = null;
		tmp149_AST = astFactory.create((AST)_t);
		tmp149_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp149_AST);
		ASTPair __currentAST265 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,FOR_ITERATOR);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ELIST:
		{
			controlExpressionList(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case 3:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST265;
		_t = __t265;
		_t = _t.getNextSibling();
		forIter_AST = (AST)currentAST.root;
		returnAST = forIter_AST;
		_retTree = _t;
	}

	public final void statementLabelPrefix(AST _t) throws RecognitionException {

		AST statementLabelPrefix_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST statementLabelPrefix_AST = null;

		AST __t243 = _t;
		AST tmp150_AST = null;
		AST tmp150_AST_in = null;
		tmp150_AST = astFactory.create((AST)_t);
		tmp150_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp150_AST);
		ASTPair __currentAST243 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,LABELED_STAT);
		_t = _t.getFirstChild();
		AST tmp151_AST = null;
		AST tmp151_AST_in = null;
		tmp151_AST = astFactory.create((AST)_t);
		tmp151_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp151_AST);
		match(_t,IDENT);
		_t = _t.getNextSibling();
		currentAST = __currentAST243;
		_t = __t243;
		_t = _t.getNextSibling();
		statementLabelPrefix_AST = (AST)currentAST.root;
		returnAST = statementLabelPrefix_AST;
		_retTree = _t;
	}

	public final void commandArguments(AST _t) throws RecognitionException {

		AST commandArguments_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST commandArguments_AST = null;

		AST __t277 = _t;
		AST tmp152_AST = null;
		AST tmp152_AST_in = null;
		tmp152_AST = astFactory.create((AST)_t);
		tmp152_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp152_AST);
		ASTPair __currentAST277 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,METHOD_CALL);
		_t = _t.getFirstChild();
		AST __t278 = _t;
		AST tmp153_AST = null;
		AST tmp153_AST_in = null;
		tmp153_AST = astFactory.create((AST)_t);
		tmp153_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp153_AST);
		ASTPair __currentAST278 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,ELIST);
		_t = _t.getFirstChild();
		{
		int _cnt280=0;
		_loop280:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_8.member(_t.getType()))) {
				expression(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt280>=1 ) { break _loop280; } else {throw new NoViableAltException(_t);}
			}

			_cnt280++;
		} while (true);
		}
		currentAST = __currentAST278;
		_t = __t278;
		_t = _t.getNextSibling();
		currentAST = __currentAST277;
		_t = __t277;
		_t = _t.getNextSibling();
		commandArguments_AST = (AST)currentAST.root;
		returnAST = commandArguments_AST;
		_retTree = _t;
	}

	public final void casesGroup(AST _t) throws RecognitionException {

		AST casesGroup_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST casesGroup_AST = null;

		AST __t248 = _t;
		AST tmp154_AST = null;
		AST tmp154_AST_in = null;
		tmp154_AST = astFactory.create((AST)_t);
		tmp154_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp154_AST);
		ASTPair __currentAST248 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,CASE_GROUP);
		_t = _t.getFirstChild();
		{
		int _cnt250=0;
		_loop250:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==LITERAL_default||_t.getType()==LITERAL_case)) {
				aCase(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt250>=1 ) { break _loop250; } else {throw new NoViableAltException(_t);}
			}

			_cnt250++;
		} while (true);
		}
		caseSList(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST248;
		_t = __t248;
		_t = _t.getNextSibling();
		casesGroup_AST = (AST)currentAST.root;
		returnAST = casesGroup_AST;
		_retTree = _t;
	}

	public final void aCase(AST _t) throws RecognitionException {

		AST aCase_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST aCase_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_case:
		{
			AST __t252 = _t;
			AST tmp155_AST = null;
			AST tmp155_AST_in = null;
			tmp155_AST = astFactory.create((AST)_t);
			tmp155_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp155_AST);
			ASTPair __currentAST252 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_case);
			_t = _t.getFirstChild();
			expression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST252;
			_t = __t252;
			_t = _t.getNextSibling();
			aCase_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_default:
		{
			AST tmp156_AST = null;
			AST tmp156_AST_in = null;
			tmp156_AST = astFactory.create((AST)_t);
			tmp156_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp156_AST);
			match(_t,LITERAL_default);
			_t = _t.getNextSibling();
			aCase_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = aCase_AST;
		_retTree = _t;
	}

	public final void caseSList(AST _t) throws RecognitionException {

		AST caseSList_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST caseSList_AST = null;

		AST __t254 = _t;
		AST tmp157_AST = null;
		AST tmp157_AST_in = null;
		tmp157_AST = astFactory.create((AST)_t);
		tmp157_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp157_AST);
		ASTPair __currentAST254 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,SLIST);
		_t = _t.getFirstChild();
		{
		int _cnt256=0;
		_loop256:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_0.member(_t.getType()))) {
				statement(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt256>=1 ) { break _loop256; } else {throw new NoViableAltException(_t);}
			}

			_cnt256++;
		} while (true);
		}
		currentAST = __currentAST254;
		_t = __t254;
		_t = _t.getNextSibling();
		caseSList_AST = (AST)currentAST.root;
		returnAST = caseSList_AST;
		_retTree = _t;
	}

	public final void controlExpressionList(AST _t) throws RecognitionException {

		AST controlExpressionList_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST controlExpressionList_AST = null;

		AST __t283 = _t;
		AST tmp158_AST = null;
		AST tmp158_AST_in = null;
		tmp158_AST = astFactory.create((AST)_t);
		tmp158_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp158_AST);
		ASTPair __currentAST283 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,ELIST);
		_t = _t.getFirstChild();
		{
		int _cnt285=0;
		_loop285:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_9.member(_t.getType()))) {
				strictContextExpression(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt285>=1 ) { break _loop285; } else {throw new NoViableAltException(_t);}
			}

			_cnt285++;
		} while (true);
		}
		currentAST = __currentAST283;
		_t = __t283;
		_t = _t.getNextSibling();
		controlExpressionList_AST = (AST)currentAST.root;
		returnAST = controlExpressionList_AST;
		_retTree = _t;
	}

	public final void handler(AST _t) throws RecognitionException {

		AST handler_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST handler_AST = null;

		AST __t275 = _t;
		AST tmp159_AST = null;
		AST tmp159_AST_in = null;
		tmp159_AST = astFactory.create((AST)_t);
		tmp159_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp159_AST);
		ASTPair __currentAST275 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,LITERAL_catch);
		_t = _t.getFirstChild();
		parameterDeclaration(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		compoundStatement(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST275;
		_t = __t275;
		_t = _t.getNextSibling();
		handler_AST = (AST)currentAST.root;
		returnAST = handler_AST;
		_retTree = _t;
	}

	public final void finallyClause(AST _t) throws RecognitionException {

		AST finallyClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST finallyClause_AST = null;

		AST __t273 = _t;
		AST tmp160_AST = null;
		AST tmp160_AST_in = null;
		tmp160_AST = astFactory.create((AST)_t);
		tmp160_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp160_AST);
		ASTPair __currentAST273 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,LITERAL_finally);
		_t = _t.getFirstChild();
		compoundStatement(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST273;
		_t = __t273;
		_t = _t.getNextSibling();
		finallyClause_AST = (AST)currentAST.root;
		returnAST = finallyClause_AST;
		_retTree = _t;
	}

	public final void assignmentExpression(AST _t) throws RecognitionException {

		AST assignmentExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST assignmentExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ASSIGN:
		{
			AST __t313 = _t;
			AST tmp161_AST = null;
			AST tmp161_AST_in = null;
			tmp161_AST = astFactory.create((AST)_t);
			tmp161_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp161_AST);
			ASTPair __currentAST313 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,ASSIGN);
			_t = _t.getFirstChild();
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			assignmentExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST313;
			_t = __t313;
			_t = _t.getNextSibling();
			assignmentExpression_AST = (AST)currentAST.root;
			break;
		}
		case PLUS_ASSIGN:
		{
			AST __t314 = _t;
			AST tmp162_AST = null;
			AST tmp162_AST_in = null;
			tmp162_AST = astFactory.create((AST)_t);
			tmp162_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp162_AST);
			ASTPair __currentAST314 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,PLUS_ASSIGN);
			_t = _t.getFirstChild();
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			assignmentExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST314;
			_t = __t314;
			_t = _t.getNextSibling();
			assignmentExpression_AST = (AST)currentAST.root;
			break;
		}
		case MINUS_ASSIGN:
		{
			AST __t315 = _t;
			AST tmp163_AST = null;
			AST tmp163_AST_in = null;
			tmp163_AST = astFactory.create((AST)_t);
			tmp163_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp163_AST);
			ASTPair __currentAST315 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,MINUS_ASSIGN);
			_t = _t.getFirstChild();
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			assignmentExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST315;
			_t = __t315;
			_t = _t.getNextSibling();
			assignmentExpression_AST = (AST)currentAST.root;
			break;
		}
		case STAR_ASSIGN:
		{
			AST __t316 = _t;
			AST tmp164_AST = null;
			AST tmp164_AST_in = null;
			tmp164_AST = astFactory.create((AST)_t);
			tmp164_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp164_AST);
			ASTPair __currentAST316 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,STAR_ASSIGN);
			_t = _t.getFirstChild();
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			assignmentExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST316;
			_t = __t316;
			_t = _t.getNextSibling();
			assignmentExpression_AST = (AST)currentAST.root;
			break;
		}
		case DIV_ASSIGN:
		{
			AST __t317 = _t;
			AST tmp165_AST = null;
			AST tmp165_AST_in = null;
			tmp165_AST = astFactory.create((AST)_t);
			tmp165_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp165_AST);
			ASTPair __currentAST317 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,DIV_ASSIGN);
			_t = _t.getFirstChild();
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			assignmentExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST317;
			_t = __t317;
			_t = _t.getNextSibling();
			assignmentExpression_AST = (AST)currentAST.root;
			break;
		}
		case MOD_ASSIGN:
		{
			AST __t318 = _t;
			AST tmp166_AST = null;
			AST tmp166_AST_in = null;
			tmp166_AST = astFactory.create((AST)_t);
			tmp166_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp166_AST);
			ASTPair __currentAST318 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,MOD_ASSIGN);
			_t = _t.getFirstChild();
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			assignmentExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST318;
			_t = __t318;
			_t = _t.getNextSibling();
			assignmentExpression_AST = (AST)currentAST.root;
			break;
		}
		case SR_ASSIGN:
		{
			AST __t319 = _t;
			AST tmp167_AST = null;
			AST tmp167_AST_in = null;
			tmp167_AST = astFactory.create((AST)_t);
			tmp167_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp167_AST);
			ASTPair __currentAST319 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SR_ASSIGN);
			_t = _t.getFirstChild();
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			assignmentExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST319;
			_t = __t319;
			_t = _t.getNextSibling();
			assignmentExpression_AST = (AST)currentAST.root;
			break;
		}
		case BSR_ASSIGN:
		{
			AST __t320 = _t;
			AST tmp168_AST = null;
			AST tmp168_AST_in = null;
			tmp168_AST = astFactory.create((AST)_t);
			tmp168_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp168_AST);
			ASTPair __currentAST320 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BSR_ASSIGN);
			_t = _t.getFirstChild();
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			assignmentExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST320;
			_t = __t320;
			_t = _t.getNextSibling();
			assignmentExpression_AST = (AST)currentAST.root;
			break;
		}
		case SL_ASSIGN:
		{
			AST __t321 = _t;
			AST tmp169_AST = null;
			AST tmp169_AST_in = null;
			tmp169_AST = astFactory.create((AST)_t);
			tmp169_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp169_AST);
			ASTPair __currentAST321 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SL_ASSIGN);
			_t = _t.getFirstChild();
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			assignmentExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST321;
			_t = __t321;
			_t = _t.getNextSibling();
			assignmentExpression_AST = (AST)currentAST.root;
			break;
		}
		case BAND_ASSIGN:
		{
			AST __t322 = _t;
			AST tmp170_AST = null;
			AST tmp170_AST_in = null;
			tmp170_AST = astFactory.create((AST)_t);
			tmp170_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp170_AST);
			ASTPair __currentAST322 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BAND_ASSIGN);
			_t = _t.getFirstChild();
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			assignmentExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST322;
			_t = __t322;
			_t = _t.getNextSibling();
			assignmentExpression_AST = (AST)currentAST.root;
			break;
		}
		case BXOR_ASSIGN:
		{
			AST __t323 = _t;
			AST tmp171_AST = null;
			AST tmp171_AST_in = null;
			tmp171_AST = astFactory.create((AST)_t);
			tmp171_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp171_AST);
			ASTPair __currentAST323 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BXOR_ASSIGN);
			_t = _t.getFirstChild();
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			assignmentExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST323;
			_t = __t323;
			_t = _t.getNextSibling();
			assignmentExpression_AST = (AST)currentAST.root;
			break;
		}
		case BOR_ASSIGN:
		{
			AST __t324 = _t;
			AST tmp172_AST = null;
			AST tmp172_AST_in = null;
			tmp172_AST = astFactory.create((AST)_t);
			tmp172_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp172_AST);
			ASTPair __currentAST324 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BOR_ASSIGN);
			_t = _t.getFirstChild();
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			assignmentExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST324;
			_t = __t324;
			_t = _t.getNextSibling();
			assignmentExpression_AST = (AST)currentAST.root;
			break;
		}
		case STAR_STAR_ASSIGN:
		{
			AST __t325 = _t;
			AST tmp173_AST = null;
			AST tmp173_AST_in = null;
			tmp173_AST = astFactory.create((AST)_t);
			tmp173_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp173_AST);
			ASTPair __currentAST325 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,STAR_STAR_ASSIGN);
			_t = _t.getFirstChild();
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			assignmentExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST325;
			_t = __t325;
			_t = _t.getNextSibling();
			assignmentExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_as:
		case LITERAL_in:
		case QUESTION:
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			conditionalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			assignmentExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = assignmentExpression_AST;
		_retTree = _t;
	}

	public final void pathExpression(AST _t) throws RecognitionException {

		AST pathExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST pathExpression_AST = null;

		primaryExpression(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop288:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_10.member(_t.getType()))) {
				pathElement(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop288;
			}

		} while (true);
		}
		pathExpression_AST = (AST)currentAST.root;
		returnAST = pathExpression_AST;
		_retTree = _t;
	}

	public final void primaryExpression(AST _t) throws RecognitionException {

		AST primaryExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST primaryExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case IDENT:
		{
			AST tmp174_AST = null;
			AST tmp174_AST_in = null;
			tmp174_AST = astFactory.create((AST)_t);
			tmp174_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp174_AST);
			match(_t,IDENT);
			_t = _t.getNextSibling();
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case STRING_LITERAL:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case NUM_INT:
		case NUM_FLOAT:
		case NUM_LONG:
		case NUM_DOUBLE:
		case NUM_BIG_INT:
		case NUM_BIG_DECIMAL:
		{
			constant(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_new:
		{
			newExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_this:
		{
			AST tmp175_AST = null;
			AST tmp175_AST_in = null;
			tmp175_AST = astFactory.create((AST)_t);
			tmp175_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp175_AST);
			match(_t,LITERAL_this);
			_t = _t.getNextSibling();
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_super:
		{
			AST tmp176_AST = null;
			AST tmp176_AST_in = null;
			tmp176_AST = astFactory.create((AST)_t);
			tmp176_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp176_AST);
			match(_t,LITERAL_super);
			_t = _t.getNextSibling();
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_as:
		case ANNOTATION:
		case VARIABLE_DEF:
		case ASSIGN:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		case LITERAL_return:
		case LITERAL_in:
		case PLUS_ASSIGN:
		case MINUS_ASSIGN:
		case STAR_ASSIGN:
		case DIV_ASSIGN:
		case MOD_ASSIGN:
		case SR_ASSIGN:
		case BSR_ASSIGN:
		case SL_ASSIGN:
		case BAND_ASSIGN:
		case BXOR_ASSIGN:
		case BOR_ASSIGN:
		case STAR_STAR_ASSIGN:
		case QUESTION:
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			parenthesizedExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case CLOSED_BLOCK:
		{
			closureConstructorExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case MAP_CONSTRUCTOR:
		case LIST_CONSTRUCTOR:
		{
			listOrMapConstructorExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case STRING_CTOR_START:
		{
			stringConstructorExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case SCOPE_ESCAPE:
		{
			scopeEscapeExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		{
			builtInType(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = primaryExpression_AST;
		_retTree = _t;
	}

	public final void pathElement(AST _t) throws RecognitionException {

		AST pathElement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST pathElement_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case METHOD_CALL:
		{
			methodCallArgs(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pathElement_AST = (AST)currentAST.root;
			break;
		}
		case CLOSED_BLOCK:
		{
			appendedBlock(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pathElement_AST = (AST)currentAST.root;
			break;
		}
		case INDEX_OP:
		{
			indexPropertyArgs(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pathElement_AST = (AST)currentAST.root;
			break;
		}
		case SPREAD_DOT:
		{
			AST __t290 = _t;
			AST tmp177_AST = null;
			AST tmp177_AST_in = null;
			tmp177_AST = astFactory.create((AST)_t);
			tmp177_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp177_AST);
			ASTPair __currentAST290 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SPREAD_DOT);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TYPE_ARGUMENTS:
			{
				typeArguments(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_def:
			case AT:
			case IDENT:
			case LITERAL_class:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_any:
			case LITERAL_as:
			case SLIST:
			case LITERAL_while:
			case LITERAL_for:
			case LITERAL_try:
			case LITERAL_finally:
			case LITERAL_catch:
			case STRING_LITERAL:
			case LITERAL_in:
			case LITERAL_if:
			case LITERAL_else:
			case LITERAL_do:
			case LITERAL_switch:
			case DYNAMIC_MEMBER:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			namePart(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST290;
			_t = __t290;
			_t = _t.getNextSibling();
			pathElement_AST = (AST)currentAST.root;
			break;
		}
		case OPTIONAL_DOT:
		{
			AST __t292 = _t;
			AST tmp178_AST = null;
			AST tmp178_AST_in = null;
			tmp178_AST = astFactory.create((AST)_t);
			tmp178_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp178_AST);
			ASTPair __currentAST292 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,OPTIONAL_DOT);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TYPE_ARGUMENTS:
			{
				typeArguments(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_def:
			case AT:
			case IDENT:
			case LITERAL_class:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_any:
			case LITERAL_as:
			case SLIST:
			case LITERAL_while:
			case LITERAL_for:
			case LITERAL_try:
			case LITERAL_finally:
			case LITERAL_catch:
			case STRING_LITERAL:
			case LITERAL_in:
			case LITERAL_if:
			case LITERAL_else:
			case LITERAL_do:
			case LITERAL_switch:
			case DYNAMIC_MEMBER:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			namePart(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST292;
			_t = __t292;
			_t = _t.getNextSibling();
			pathElement_AST = (AST)currentAST.root;
			break;
		}
		case MEMBER_POINTER:
		{
			AST __t294 = _t;
			AST tmp179_AST = null;
			AST tmp179_AST_in = null;
			tmp179_AST = astFactory.create((AST)_t);
			tmp179_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp179_AST);
			ASTPair __currentAST294 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,MEMBER_POINTER);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TYPE_ARGUMENTS:
			{
				typeArguments(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_def:
			case AT:
			case IDENT:
			case LITERAL_class:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_any:
			case LITERAL_as:
			case SLIST:
			case LITERAL_while:
			case LITERAL_for:
			case LITERAL_try:
			case LITERAL_finally:
			case LITERAL_catch:
			case STRING_LITERAL:
			case LITERAL_in:
			case LITERAL_if:
			case LITERAL_else:
			case LITERAL_do:
			case LITERAL_switch:
			case DYNAMIC_MEMBER:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			namePart(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST294;
			_t = __t294;
			_t = _t.getNextSibling();
			pathElement_AST = (AST)currentAST.root;
			break;
		}
		case DOT:
		{
			AST __t296 = _t;
			AST tmp180_AST = null;
			AST tmp180_AST_in = null;
			tmp180_AST = astFactory.create((AST)_t);
			tmp180_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp180_AST);
			ASTPair __currentAST296 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,DOT);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TYPE_ARGUMENTS:
			{
				typeArguments(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_def:
			case AT:
			case IDENT:
			case LITERAL_class:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_any:
			case LITERAL_as:
			case SLIST:
			case LITERAL_while:
			case LITERAL_for:
			case LITERAL_try:
			case LITERAL_finally:
			case LITERAL_catch:
			case STRING_LITERAL:
			case LITERAL_in:
			case LITERAL_if:
			case LITERAL_else:
			case LITERAL_do:
			case LITERAL_switch:
			case DYNAMIC_MEMBER:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			namePart(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST296;
			_t = __t296;
			_t = _t.getNextSibling();
			pathElement_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = pathElement_AST;
		_retTree = _t;
	}

	public final void methodCallArgs(AST _t) throws RecognitionException {

		AST methodCallArgs_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST methodCallArgs_AST = null;

		AST __t308 = _t;
		AST tmp181_AST = null;
		AST tmp181_AST_in = null;
		tmp181_AST = astFactory.create((AST)_t);
		tmp181_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp181_AST);
		ASTPair __currentAST308 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,METHOD_CALL);
		_t = _t.getFirstChild();
		argList(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST308;
		_t = __t308;
		_t = _t.getNextSibling();
		methodCallArgs_AST = (AST)currentAST.root;
		returnAST = methodCallArgs_AST;
		_retTree = _t;
	}

	public final void appendedBlock(AST _t) throws RecognitionException {

		AST appendedBlock_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST appendedBlock_AST = null;

		closedBlock(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		appendedBlock_AST = (AST)currentAST.root;
		returnAST = appendedBlock_AST;
		_retTree = _t;
	}

	public final void indexPropertyArgs(AST _t) throws RecognitionException {

		AST indexPropertyArgs_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST indexPropertyArgs_AST = null;

		AST __t311 = _t;
		AST tmp182_AST = null;
		AST tmp182_AST_in = null;
		tmp182_AST = astFactory.create((AST)_t);
		tmp182_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp182_AST);
		ASTPair __currentAST311 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,INDEX_OP);
		_t = _t.getFirstChild();
		argList(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST311;
		_t = __t311;
		_t = _t.getNextSibling();
		indexPropertyArgs_AST = (AST)currentAST.root;
		returnAST = indexPropertyArgs_AST;
		_retTree = _t;
	}

	public final void namePart(AST _t) throws RecognitionException {

		AST namePart_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST namePart_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case AT:
		{
			AST __t299 = _t;
			AST tmp183_AST = null;
			AST tmp183_AST_in = null;
			tmp183_AST = astFactory.create((AST)_t);
			tmp183_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp183_AST);
			ASTPair __currentAST299 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,AT);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case IDENT:
			{
				AST tmp184_AST = null;
				AST tmp184_AST_in = null;
				tmp184_AST = astFactory.create((AST)_t);
				tmp184_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp184_AST);
				match(_t,IDENT);
				_t = _t.getNextSibling();
				break;
			}
			case STRING_LITERAL:
			{
				AST tmp185_AST = null;
				AST tmp185_AST_in = null;
				tmp185_AST = astFactory.create((AST)_t);
				tmp185_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp185_AST);
				match(_t,STRING_LITERAL);
				_t = _t.getNextSibling();
				break;
			}
			case DYNAMIC_MEMBER:
			{
				dynamicMemberName(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SLIST:
			{
				openBlock(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_def:
			case LITERAL_class:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_any:
			case LITERAL_as:
			case LITERAL_while:
			case LITERAL_for:
			case LITERAL_try:
			case LITERAL_finally:
			case LITERAL_catch:
			case LITERAL_in:
			case LITERAL_if:
			case LITERAL_else:
			case LITERAL_do:
			case LITERAL_switch:
			{
				keywordPropertyNames(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST299;
			_t = __t299;
			_t = _t.getNextSibling();
			namePart_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_def:
		case IDENT:
		case LITERAL_class:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		case LITERAL_as:
		case SLIST:
		case LITERAL_while:
		case LITERAL_for:
		case LITERAL_try:
		case LITERAL_finally:
		case LITERAL_catch:
		case STRING_LITERAL:
		case LITERAL_in:
		case LITERAL_if:
		case LITERAL_else:
		case LITERAL_do:
		case LITERAL_switch:
		case DYNAMIC_MEMBER:
		{
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case IDENT:
			{
				AST tmp186_AST = null;
				AST tmp186_AST_in = null;
				tmp186_AST = astFactory.create((AST)_t);
				tmp186_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp186_AST);
				match(_t,IDENT);
				_t = _t.getNextSibling();
				break;
			}
			case STRING_LITERAL:
			{
				AST tmp187_AST = null;
				AST tmp187_AST_in = null;
				tmp187_AST = astFactory.create((AST)_t);
				tmp187_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp187_AST);
				match(_t,STRING_LITERAL);
				_t = _t.getNextSibling();
				break;
			}
			case DYNAMIC_MEMBER:
			{
				dynamicMemberName(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SLIST:
			{
				openBlock(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_def:
			case LITERAL_class:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_any:
			case LITERAL_as:
			case LITERAL_while:
			case LITERAL_for:
			case LITERAL_try:
			case LITERAL_finally:
			case LITERAL_catch:
			case LITERAL_in:
			case LITERAL_if:
			case LITERAL_else:
			case LITERAL_do:
			case LITERAL_switch:
			{
				keywordPropertyNames(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			namePart_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = namePart_AST;
		_retTree = _t;
	}

	public final void dynamicMemberName(AST _t) throws RecognitionException {

		AST dynamicMemberName_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dynamicMemberName_AST = null;

		AST __t305 = _t;
		AST tmp188_AST = null;
		AST tmp188_AST_in = null;
		tmp188_AST = astFactory.create((AST)_t);
		tmp188_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp188_AST);
		ASTPair __currentAST305 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,DYNAMIC_MEMBER);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case ANNOTATION:
		case VARIABLE_DEF:
		case ASSIGN:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		case LITERAL_return:
		case LITERAL_in:
		case PLUS_ASSIGN:
		case MINUS_ASSIGN:
		case STAR_ASSIGN:
		case DIV_ASSIGN:
		case MOD_ASSIGN:
		case SR_ASSIGN:
		case BSR_ASSIGN:
		case SL_ASSIGN:
		case BAND_ASSIGN:
		case BXOR_ASSIGN:
		case BOR_ASSIGN:
		case STAR_STAR_ASSIGN:
		case QUESTION:
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			parenthesizedExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case STRING_CTOR_START:
		{
			stringConstructorExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST305;
		_t = __t305;
		_t = _t.getNextSibling();
		dynamicMemberName_AST = (AST)currentAST.root;
		returnAST = dynamicMemberName_AST;
		_retTree = _t;
	}

	public final void keywordPropertyNames(AST _t) throws RecognitionException {

		AST keywordPropertyNames_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST keywordPropertyNames_AST = null;

		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_class:
		{
			AST tmp189_AST = null;
			AST tmp189_AST_in = null;
			tmp189_AST = astFactory.create((AST)_t);
			tmp189_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp189_AST);
			match(_t,LITERAL_class);
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_in:
		{
			AST tmp190_AST = null;
			AST tmp190_AST_in = null;
			tmp190_AST = astFactory.create((AST)_t);
			tmp190_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp190_AST);
			match(_t,LITERAL_in);
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_as:
		{
			AST tmp191_AST = null;
			AST tmp191_AST_in = null;
			tmp191_AST = astFactory.create((AST)_t);
			tmp191_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp191_AST);
			match(_t,LITERAL_as);
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_def:
		{
			AST tmp192_AST = null;
			AST tmp192_AST_in = null;
			tmp192_AST = astFactory.create((AST)_t);
			tmp192_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp192_AST);
			match(_t,LITERAL_def);
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_if:
		{
			AST tmp193_AST = null;
			AST tmp193_AST_in = null;
			tmp193_AST = astFactory.create((AST)_t);
			tmp193_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp193_AST);
			match(_t,LITERAL_if);
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_else:
		{
			AST tmp194_AST = null;
			AST tmp194_AST_in = null;
			tmp194_AST = astFactory.create((AST)_t);
			tmp194_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp194_AST);
			match(_t,LITERAL_else);
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_for:
		{
			AST tmp195_AST = null;
			AST tmp195_AST_in = null;
			tmp195_AST = astFactory.create((AST)_t);
			tmp195_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp195_AST);
			match(_t,LITERAL_for);
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_while:
		{
			AST tmp196_AST = null;
			AST tmp196_AST_in = null;
			tmp196_AST = astFactory.create((AST)_t);
			tmp196_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp196_AST);
			match(_t,LITERAL_while);
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_do:
		{
			AST tmp197_AST = null;
			AST tmp197_AST_in = null;
			tmp197_AST = astFactory.create((AST)_t);
			tmp197_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp197_AST);
			match(_t,LITERAL_do);
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_switch:
		{
			AST tmp198_AST = null;
			AST tmp198_AST_in = null;
			tmp198_AST = astFactory.create((AST)_t);
			tmp198_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp198_AST);
			match(_t,LITERAL_switch);
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_try:
		{
			AST tmp199_AST = null;
			AST tmp199_AST_in = null;
			tmp199_AST = astFactory.create((AST)_t);
			tmp199_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp199_AST);
			match(_t,LITERAL_try);
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_catch:
		{
			AST tmp200_AST = null;
			AST tmp200_AST_in = null;
			tmp200_AST = astFactory.create((AST)_t);
			tmp200_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp200_AST);
			match(_t,LITERAL_catch);
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_finally:
		{
			AST tmp201_AST = null;
			AST tmp201_AST_in = null;
			tmp201_AST = astFactory.create((AST)_t);
			tmp201_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp201_AST);
			match(_t,LITERAL_finally);
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		{
			builtInType(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		keywordPropertyNames_AST = (AST)currentAST.root;
		returnAST = keywordPropertyNames_AST;
		_retTree = _t;
	}

	public final void parenthesizedExpression(AST _t) throws RecognitionException {

		AST parenthesizedExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parenthesizedExpression_AST = null;

		strictContextExpression(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		parenthesizedExpression_AST = (AST)currentAST.root;
		returnAST = parenthesizedExpression_AST;
		_retTree = _t;
	}

	public final void stringConstructorExpression(AST _t) throws RecognitionException {

		AST stringConstructorExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST stringConstructorExpression_AST = null;

		AST tmp202_AST = null;
		AST tmp202_AST_in = null;
		tmp202_AST = astFactory.create((AST)_t);
		tmp202_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp202_AST);
		match(_t,STRING_CTOR_START);
		_t = _t.getNextSibling();
		stringConstructorValuePart(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop476:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==STRING_CTOR_MIDDLE)) {
				AST tmp203_AST = null;
				AST tmp203_AST_in = null;
				tmp203_AST = astFactory.create((AST)_t);
				tmp203_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp203_AST);
				match(_t,STRING_CTOR_MIDDLE);
				_t = _t.getNextSibling();
				stringConstructorValuePart(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop476;
			}

		} while (true);
		}
		AST tmp204_AST = null;
		AST tmp204_AST_in = null;
		tmp204_AST = astFactory.create((AST)_t);
		tmp204_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp204_AST);
		match(_t,STRING_CTOR_END);
		_t = _t.getNextSibling();
		stringConstructorExpression_AST = (AST)currentAST.root;
		returnAST = stringConstructorExpression_AST;
		_retTree = _t;
	}

	public final void logicalOrExpression(AST _t) throws RecognitionException {

		AST logicalOrExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalOrExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LOR:
		{
			AST __t331 = _t;
			AST tmp205_AST = null;
			AST tmp205_AST_in = null;
			tmp205_AST = astFactory.create((AST)_t);
			tmp205_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp205_AST);
			ASTPair __currentAST331 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LOR);
			_t = _t.getFirstChild();
			logicalOrExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			logicalAndExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST331;
			_t = __t331;
			_t = _t.getNextSibling();
			logicalOrExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_as:
		case LITERAL_in:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			logicalAndExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			logicalOrExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = logicalOrExpression_AST;
		_retTree = _t;
	}

	public final void logicalOrExpression0a(AST _t) throws RecognitionException {

		AST logicalOrExpression0a_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalOrExpression0a_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case LITERAL_in:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			logicalAndExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			logicalOrExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case LOR:
		{
			AST __t329 = _t;
			AST tmp206_AST = null;
			AST tmp206_AST_in = null;
			tmp206_AST = astFactory.create((AST)_t);
			tmp206_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp206_AST);
			ASTPair __currentAST329 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LOR);
			_t = _t.getFirstChild();
			logicalOrExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			logicalAndExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST329;
			_t = __t329;
			_t = _t.getNextSibling();
			logicalOrExpression0a_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = logicalOrExpression0a_AST;
		_retTree = _t;
	}

	public final void logicalAndExpression(AST _t) throws RecognitionException {

		AST logicalAndExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalAndExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LAND:
		{
			AST __t335 = _t;
			AST tmp207_AST = null;
			AST tmp207_AST_in = null;
			tmp207_AST = astFactory.create((AST)_t);
			tmp207_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp207_AST);
			ASTPair __currentAST335 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LAND);
			_t = _t.getFirstChild();
			logicalAndExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			inclusiveOrExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST335;
			_t = __t335;
			_t = _t.getNextSibling();
			logicalAndExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_as:
		case LITERAL_in:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			inclusiveOrExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			logicalAndExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = logicalAndExpression_AST;
		_retTree = _t;
	}

	public final void logicalAndExpression0a(AST _t) throws RecognitionException {

		AST logicalAndExpression0a_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalAndExpression0a_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case LITERAL_in:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			inclusiveOrExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			logicalAndExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case LAND:
		{
			AST __t333 = _t;
			AST tmp208_AST = null;
			AST tmp208_AST_in = null;
			tmp208_AST = astFactory.create((AST)_t);
			tmp208_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp208_AST);
			ASTPair __currentAST333 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LAND);
			_t = _t.getFirstChild();
			logicalAndExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			inclusiveOrExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST333;
			_t = __t333;
			_t = _t.getNextSibling();
			logicalAndExpression0a_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = logicalAndExpression0a_AST;
		_retTree = _t;
	}

	public final void inclusiveOrExpression(AST _t) throws RecognitionException {

		AST inclusiveOrExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST inclusiveOrExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case BOR:
		{
			AST __t339 = _t;
			AST tmp209_AST = null;
			AST tmp209_AST_in = null;
			tmp209_AST = astFactory.create((AST)_t);
			tmp209_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp209_AST);
			ASTPair __currentAST339 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BOR);
			_t = _t.getFirstChild();
			inclusiveOrExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			exclusiveOrExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST339;
			_t = __t339;
			_t = _t.getNextSibling();
			inclusiveOrExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_as:
		case LITERAL_in:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			exclusiveOrExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			inclusiveOrExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = inclusiveOrExpression_AST;
		_retTree = _t;
	}

	public final void inclusiveOrExpression0a(AST _t) throws RecognitionException {

		AST inclusiveOrExpression0a_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST inclusiveOrExpression0a_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case LITERAL_in:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			exclusiveOrExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			inclusiveOrExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case BOR:
		{
			AST __t337 = _t;
			AST tmp210_AST = null;
			AST tmp210_AST_in = null;
			tmp210_AST = astFactory.create((AST)_t);
			tmp210_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp210_AST);
			ASTPair __currentAST337 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BOR);
			_t = _t.getFirstChild();
			inclusiveOrExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			exclusiveOrExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST337;
			_t = __t337;
			_t = _t.getNextSibling();
			inclusiveOrExpression0a_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = inclusiveOrExpression0a_AST;
		_retTree = _t;
	}

	public final void exclusiveOrExpression(AST _t) throws RecognitionException {

		AST exclusiveOrExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST exclusiveOrExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case BXOR:
		{
			AST __t343 = _t;
			AST tmp211_AST = null;
			AST tmp211_AST_in = null;
			tmp211_AST = astFactory.create((AST)_t);
			tmp211_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp211_AST);
			ASTPair __currentAST343 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BXOR);
			_t = _t.getFirstChild();
			exclusiveOrExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			andExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST343;
			_t = __t343;
			_t = _t.getNextSibling();
			exclusiveOrExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_as:
		case LITERAL_in:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			andExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			exclusiveOrExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = exclusiveOrExpression_AST;
		_retTree = _t;
	}

	public final void exclusiveOrExpression0a(AST _t) throws RecognitionException {

		AST exclusiveOrExpression0a_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST exclusiveOrExpression0a_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case LITERAL_in:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			andExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			exclusiveOrExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case BXOR:
		{
			AST __t341 = _t;
			AST tmp212_AST = null;
			AST tmp212_AST_in = null;
			tmp212_AST = astFactory.create((AST)_t);
			tmp212_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp212_AST);
			ASTPair __currentAST341 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BXOR);
			_t = _t.getFirstChild();
			exclusiveOrExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			andExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST341;
			_t = __t341;
			_t = _t.getNextSibling();
			exclusiveOrExpression0a_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = exclusiveOrExpression0a_AST;
		_retTree = _t;
	}

	public final void andExpression(AST _t) throws RecognitionException {

		AST andExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST andExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case BAND:
		{
			AST __t347 = _t;
			AST tmp213_AST = null;
			AST tmp213_AST_in = null;
			tmp213_AST = astFactory.create((AST)_t);
			tmp213_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp213_AST);
			ASTPair __currentAST347 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BAND);
			_t = _t.getFirstChild();
			andExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			regexExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST347;
			_t = __t347;
			_t = _t.getNextSibling();
			andExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_as:
		case LITERAL_in:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			regexExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			andExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = andExpression_AST;
		_retTree = _t;
	}

	public final void andExpression0a(AST _t) throws RecognitionException {

		AST andExpression0a_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST andExpression0a_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case LITERAL_in:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			regexExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			andExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case BAND:
		{
			AST __t345 = _t;
			AST tmp214_AST = null;
			AST tmp214_AST_in = null;
			tmp214_AST = astFactory.create((AST)_t);
			tmp214_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp214_AST);
			ASTPair __currentAST345 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BAND);
			_t = _t.getFirstChild();
			andExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			regexExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST345;
			_t = __t345;
			_t = _t.getNextSibling();
			andExpression0a_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = andExpression0a_AST;
		_retTree = _t;
	}

	public final void regexExpression(AST _t) throws RecognitionException {

		AST regexExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST regexExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case REGEX_FIND:
		{
			AST __t355 = _t;
			AST tmp215_AST = null;
			AST tmp215_AST_in = null;
			tmp215_AST = astFactory.create((AST)_t);
			tmp215_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp215_AST);
			ASTPair __currentAST355 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,REGEX_FIND);
			_t = _t.getFirstChild();
			regexExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			equalityExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST355;
			_t = __t355;
			_t = _t.getNextSibling();
			regexExpression_AST = (AST)currentAST.root;
			break;
		}
		case REGEX_MATCH:
		{
			AST __t356 = _t;
			AST tmp216_AST = null;
			AST tmp216_AST_in = null;
			tmp216_AST = astFactory.create((AST)_t);
			tmp216_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp216_AST);
			ASTPair __currentAST356 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,REGEX_MATCH);
			_t = _t.getFirstChild();
			regexExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			equalityExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST356;
			_t = __t356;
			_t = _t.getNextSibling();
			regexExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_as:
		case LITERAL_in:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			equalityExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			regexExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = regexExpression_AST;
		_retTree = _t;
	}

	public final void regexExpression0a(AST _t) throws RecognitionException {

		AST regexExpression0a_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST regexExpression0a_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case LITERAL_in:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			equalityExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			regexExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case REGEX_FIND:
		{
			AST __t349 = _t;
			AST tmp217_AST = null;
			AST tmp217_AST_in = null;
			tmp217_AST = astFactory.create((AST)_t);
			tmp217_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp217_AST);
			ASTPair __currentAST349 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,REGEX_FIND);
			_t = _t.getFirstChild();
			regexExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			equalityExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST349;
			_t = __t349;
			_t = _t.getNextSibling();
			regexExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case REGEX_MATCH:
		{
			AST __t350 = _t;
			AST tmp218_AST = null;
			AST tmp218_AST_in = null;
			tmp218_AST = astFactory.create((AST)_t);
			tmp218_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp218_AST);
			ASTPair __currentAST350 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,REGEX_MATCH);
			_t = _t.getFirstChild();
			regexExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			equalityExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST350;
			_t = __t350;
			_t = _t.getNextSibling();
			regexExpression0a_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = regexExpression0a_AST;
		_retTree = _t;
	}

	public final void equalityExpression(AST _t) throws RecognitionException {

		AST equalityExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST equalityExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NOT_EQUAL:
		{
			AST __t370 = _t;
			AST tmp219_AST = null;
			AST tmp219_AST_in = null;
			tmp219_AST = astFactory.create((AST)_t);
			tmp219_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp219_AST);
			ASTPair __currentAST370 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NOT_EQUAL);
			_t = _t.getFirstChild();
			equalityExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			relationalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST370;
			_t = __t370;
			_t = _t.getNextSibling();
			equalityExpression_AST = (AST)currentAST.root;
			break;
		}
		case EQUAL:
		{
			AST __t371 = _t;
			AST tmp220_AST = null;
			AST tmp220_AST_in = null;
			tmp220_AST = astFactory.create((AST)_t);
			tmp220_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp220_AST);
			ASTPair __currentAST371 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,EQUAL);
			_t = _t.getFirstChild();
			equalityExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			relationalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST371;
			_t = __t371;
			_t = _t.getNextSibling();
			equalityExpression_AST = (AST)currentAST.root;
			break;
		}
		case COMPARE_TO:
		{
			AST __t372 = _t;
			AST tmp221_AST = null;
			AST tmp221_AST_in = null;
			tmp221_AST = astFactory.create((AST)_t);
			tmp221_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp221_AST);
			ASTPair __currentAST372 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,COMPARE_TO);
			_t = _t.getFirstChild();
			equalityExpression0c(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			relationalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST372;
			_t = __t372;
			_t = _t.getNextSibling();
			equalityExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_as:
		case LITERAL_in:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			relationalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			equalityExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = equalityExpression_AST;
		_retTree = _t;
	}

	public final void regexExpression0b(AST _t) throws RecognitionException {

		AST regexExpression0b_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST regexExpression0b_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case LITERAL_in:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			equalityExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			regexExpression0b_AST = (AST)currentAST.root;
			break;
		}
		case REGEX_FIND:
		{
			AST __t352 = _t;
			AST tmp222_AST = null;
			AST tmp222_AST_in = null;
			tmp222_AST = astFactory.create((AST)_t);
			tmp222_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp222_AST);
			ASTPair __currentAST352 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,REGEX_FIND);
			_t = _t.getFirstChild();
			regexExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			equalityExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST352;
			_t = __t352;
			_t = _t.getNextSibling();
			regexExpression0b_AST = (AST)currentAST.root;
			break;
		}
		case REGEX_MATCH:
		{
			AST __t353 = _t;
			AST tmp223_AST = null;
			AST tmp223_AST_in = null;
			tmp223_AST = astFactory.create((AST)_t);
			tmp223_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp223_AST);
			ASTPair __currentAST353 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,REGEX_MATCH);
			_t = _t.getFirstChild();
			regexExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			equalityExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST353;
			_t = __t353;
			_t = _t.getNextSibling();
			regexExpression0b_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = regexExpression0b_AST;
		_retTree = _t;
	}

	public final void equalityExpression0a(AST _t) throws RecognitionException {

		AST equalityExpression0a_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST equalityExpression0a_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case LITERAL_in:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			relationalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			equalityExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case NOT_EQUAL:
		{
			AST __t358 = _t;
			AST tmp224_AST = null;
			AST tmp224_AST_in = null;
			tmp224_AST = astFactory.create((AST)_t);
			tmp224_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp224_AST);
			ASTPair __currentAST358 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NOT_EQUAL);
			_t = _t.getFirstChild();
			equalityExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			relationalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST358;
			_t = __t358;
			_t = _t.getNextSibling();
			equalityExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case EQUAL:
		{
			AST __t359 = _t;
			AST tmp225_AST = null;
			AST tmp225_AST_in = null;
			tmp225_AST = astFactory.create((AST)_t);
			tmp225_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp225_AST);
			ASTPair __currentAST359 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,EQUAL);
			_t = _t.getFirstChild();
			equalityExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			relationalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST359;
			_t = __t359;
			_t = _t.getNextSibling();
			equalityExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case COMPARE_TO:
		{
			AST __t360 = _t;
			AST tmp226_AST = null;
			AST tmp226_AST_in = null;
			tmp226_AST = astFactory.create((AST)_t);
			tmp226_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp226_AST);
			ASTPair __currentAST360 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,COMPARE_TO);
			_t = _t.getFirstChild();
			equalityExpression0c(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			relationalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST360;
			_t = __t360;
			_t = _t.getNextSibling();
			equalityExpression0a_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = equalityExpression0a_AST;
		_retTree = _t;
	}

	public final void relationalExpression(AST _t) throws RecognitionException {

		AST relationalExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST relationalExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LT:
		{
			AST __t374 = _t;
			AST tmp227_AST = null;
			AST tmp227_AST_in = null;
			tmp227_AST = astFactory.create((AST)_t);
			tmp227_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp227_AST);
			ASTPair __currentAST374 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LT);
			_t = _t.getFirstChild();
			shiftExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			shiftExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST374;
			_t = __t374;
			_t = _t.getNextSibling();
			relationalExpression_AST = (AST)currentAST.root;
			break;
		}
		case GT:
		{
			AST __t375 = _t;
			AST tmp228_AST = null;
			AST tmp228_AST_in = null;
			tmp228_AST = astFactory.create((AST)_t);
			tmp228_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp228_AST);
			ASTPair __currentAST375 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,GT);
			_t = _t.getFirstChild();
			shiftExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			shiftExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST375;
			_t = __t375;
			_t = _t.getNextSibling();
			relationalExpression_AST = (AST)currentAST.root;
			break;
		}
		case LE:
		{
			AST __t376 = _t;
			AST tmp229_AST = null;
			AST tmp229_AST_in = null;
			tmp229_AST = astFactory.create((AST)_t);
			tmp229_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp229_AST);
			ASTPair __currentAST376 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LE);
			_t = _t.getFirstChild();
			shiftExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			shiftExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST376;
			_t = __t376;
			_t = _t.getNextSibling();
			relationalExpression_AST = (AST)currentAST.root;
			break;
		}
		case GE:
		{
			AST __t377 = _t;
			AST tmp230_AST = null;
			AST tmp230_AST_in = null;
			tmp230_AST = astFactory.create((AST)_t);
			tmp230_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp230_AST);
			ASTPair __currentAST377 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,GE);
			_t = _t.getFirstChild();
			shiftExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			shiftExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST377;
			_t = __t377;
			_t = _t.getNextSibling();
			relationalExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_in:
		{
			AST __t378 = _t;
			AST tmp231_AST = null;
			AST tmp231_AST_in = null;
			tmp231_AST = astFactory.create((AST)_t);
			tmp231_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp231_AST);
			ASTPair __currentAST378 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_in);
			_t = _t.getFirstChild();
			shiftExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			shiftExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST378;
			_t = __t378;
			_t = _t.getNextSibling();
			relationalExpression_AST = (AST)currentAST.root;
			break;
		}
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			shiftExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			relationalExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_instanceof:
		{
			AST __t379 = _t;
			AST tmp232_AST = null;
			AST tmp232_AST_in = null;
			tmp232_AST = astFactory.create((AST)_t);
			tmp232_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp232_AST);
			ASTPair __currentAST379 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_instanceof);
			_t = _t.getFirstChild();
			shiftExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			typeSpec(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST379;
			_t = __t379;
			_t = _t.getNextSibling();
			relationalExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_as:
		{
			AST __t380 = _t;
			AST tmp233_AST = null;
			AST tmp233_AST_in = null;
			tmp233_AST = astFactory.create((AST)_t);
			tmp233_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp233_AST);
			ASTPair __currentAST380 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_as);
			_t = _t.getFirstChild();
			shiftExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			typeSpec(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST380;
			_t = __t380;
			_t = _t.getNextSibling();
			relationalExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = relationalExpression_AST;
		_retTree = _t;
	}

	public final void equalityExpression0b(AST _t) throws RecognitionException {

		AST equalityExpression0b_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST equalityExpression0b_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case LITERAL_in:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			relationalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			equalityExpression0b_AST = (AST)currentAST.root;
			break;
		}
		case NOT_EQUAL:
		{
			AST __t362 = _t;
			AST tmp234_AST = null;
			AST tmp234_AST_in = null;
			tmp234_AST = astFactory.create((AST)_t);
			tmp234_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp234_AST);
			ASTPair __currentAST362 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NOT_EQUAL);
			_t = _t.getFirstChild();
			equalityExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			relationalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST362;
			_t = __t362;
			_t = _t.getNextSibling();
			equalityExpression0b_AST = (AST)currentAST.root;
			break;
		}
		case EQUAL:
		{
			AST __t363 = _t;
			AST tmp235_AST = null;
			AST tmp235_AST_in = null;
			tmp235_AST = astFactory.create((AST)_t);
			tmp235_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp235_AST);
			ASTPair __currentAST363 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,EQUAL);
			_t = _t.getFirstChild();
			equalityExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			relationalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST363;
			_t = __t363;
			_t = _t.getNextSibling();
			equalityExpression0b_AST = (AST)currentAST.root;
			break;
		}
		case COMPARE_TO:
		{
			AST __t364 = _t;
			AST tmp236_AST = null;
			AST tmp236_AST_in = null;
			tmp236_AST = astFactory.create((AST)_t);
			tmp236_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp236_AST);
			ASTPair __currentAST364 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,COMPARE_TO);
			_t = _t.getFirstChild();
			equalityExpression0c(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			relationalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST364;
			_t = __t364;
			_t = _t.getNextSibling();
			equalityExpression0b_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = equalityExpression0b_AST;
		_retTree = _t;
	}

	public final void equalityExpression0c(AST _t) throws RecognitionException {

		AST equalityExpression0c_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST equalityExpression0c_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case LITERAL_in:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			relationalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			equalityExpression0c_AST = (AST)currentAST.root;
			break;
		}
		case NOT_EQUAL:
		{
			AST __t366 = _t;
			AST tmp237_AST = null;
			AST tmp237_AST_in = null;
			tmp237_AST = astFactory.create((AST)_t);
			tmp237_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp237_AST);
			ASTPair __currentAST366 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NOT_EQUAL);
			_t = _t.getFirstChild();
			equalityExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			relationalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST366;
			_t = __t366;
			_t = _t.getNextSibling();
			equalityExpression0c_AST = (AST)currentAST.root;
			break;
		}
		case EQUAL:
		{
			AST __t367 = _t;
			AST tmp238_AST = null;
			AST tmp238_AST_in = null;
			tmp238_AST = astFactory.create((AST)_t);
			tmp238_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp238_AST);
			ASTPair __currentAST367 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,EQUAL);
			_t = _t.getFirstChild();
			equalityExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			relationalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST367;
			_t = __t367;
			_t = _t.getNextSibling();
			equalityExpression0c_AST = (AST)currentAST.root;
			break;
		}
		case COMPARE_TO:
		{
			AST __t368 = _t;
			AST tmp239_AST = null;
			AST tmp239_AST_in = null;
			tmp239_AST = astFactory.create((AST)_t);
			tmp239_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp239_AST);
			ASTPair __currentAST368 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,COMPARE_TO);
			_t = _t.getFirstChild();
			equalityExpression0c(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			relationalExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST368;
			_t = __t368;
			_t = _t.getNextSibling();
			equalityExpression0c_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = equalityExpression0c_AST;
		_retTree = _t;
	}

	public final void shiftExpression(AST _t) throws RecognitionException {

		AST shiftExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST shiftExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			shiftExpression_AST = (AST)currentAST.root;
			break;
		}
		case SL:
		{
			AST __t412 = _t;
			AST tmp240_AST = null;
			AST tmp240_AST_in = null;
			tmp240_AST = astFactory.create((AST)_t);
			tmp240_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp240_AST);
			ASTPair __currentAST412 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SL);
			_t = _t.getFirstChild();
			shiftExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST412;
			_t = __t412;
			_t = _t.getNextSibling();
			shiftExpression_AST = (AST)currentAST.root;
			break;
		}
		case SR:
		{
			AST __t413 = _t;
			AST tmp241_AST = null;
			AST tmp241_AST_in = null;
			tmp241_AST = astFactory.create((AST)_t);
			tmp241_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp241_AST);
			ASTPair __currentAST413 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SR);
			_t = _t.getFirstChild();
			shiftExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST413;
			_t = __t413;
			_t = _t.getNextSibling();
			shiftExpression_AST = (AST)currentAST.root;
			break;
		}
		case BSR:
		{
			AST __t414 = _t;
			AST tmp242_AST = null;
			AST tmp242_AST_in = null;
			tmp242_AST = astFactory.create((AST)_t);
			tmp242_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp242_AST);
			ASTPair __currentAST414 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BSR);
			_t = _t.getFirstChild();
			shiftExpression0c(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST414;
			_t = __t414;
			_t = _t.getNextSibling();
			shiftExpression_AST = (AST)currentAST.root;
			break;
		}
		case RANGE_INCLUSIVE:
		{
			AST __t415 = _t;
			AST tmp243_AST = null;
			AST tmp243_AST_in = null;
			tmp243_AST = astFactory.create((AST)_t);
			tmp243_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp243_AST);
			ASTPair __currentAST415 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,RANGE_INCLUSIVE);
			_t = _t.getFirstChild();
			shiftExpression0d(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST415;
			_t = __t415;
			_t = _t.getNextSibling();
			shiftExpression_AST = (AST)currentAST.root;
			break;
		}
		case RANGE_EXCLUSIVE:
		{
			AST __t416 = _t;
			AST tmp244_AST = null;
			AST tmp244_AST_in = null;
			tmp244_AST = astFactory.create((AST)_t);
			tmp244_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp244_AST);
			ASTPair __currentAST416 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,RANGE_EXCLUSIVE);
			_t = _t.getFirstChild();
			shiftExpression0e(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST416;
			_t = __t416;
			_t = _t.getNextSibling();
			shiftExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = shiftExpression_AST;
		_retTree = _t;
	}

	public final void shiftExpression0a(AST _t) throws RecognitionException {

		AST shiftExpression0a_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST shiftExpression0a_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			shiftExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case SL:
		{
			AST __t382 = _t;
			AST tmp245_AST = null;
			AST tmp245_AST_in = null;
			tmp245_AST = astFactory.create((AST)_t);
			tmp245_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp245_AST);
			ASTPair __currentAST382 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SL);
			_t = _t.getFirstChild();
			shiftExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST382;
			_t = __t382;
			_t = _t.getNextSibling();
			shiftExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case SR:
		{
			AST __t383 = _t;
			AST tmp246_AST = null;
			AST tmp246_AST_in = null;
			tmp246_AST = astFactory.create((AST)_t);
			tmp246_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp246_AST);
			ASTPair __currentAST383 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SR);
			_t = _t.getFirstChild();
			shiftExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST383;
			_t = __t383;
			_t = _t.getNextSibling();
			shiftExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case BSR:
		{
			AST __t384 = _t;
			AST tmp247_AST = null;
			AST tmp247_AST_in = null;
			tmp247_AST = astFactory.create((AST)_t);
			tmp247_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp247_AST);
			ASTPair __currentAST384 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BSR);
			_t = _t.getFirstChild();
			shiftExpression0c(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST384;
			_t = __t384;
			_t = _t.getNextSibling();
			shiftExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case RANGE_INCLUSIVE:
		{
			AST __t385 = _t;
			AST tmp248_AST = null;
			AST tmp248_AST_in = null;
			tmp248_AST = astFactory.create((AST)_t);
			tmp248_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp248_AST);
			ASTPair __currentAST385 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,RANGE_INCLUSIVE);
			_t = _t.getFirstChild();
			shiftExpression0d(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST385;
			_t = __t385;
			_t = _t.getNextSibling();
			shiftExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case RANGE_EXCLUSIVE:
		{
			AST __t386 = _t;
			AST tmp249_AST = null;
			AST tmp249_AST_in = null;
			tmp249_AST = astFactory.create((AST)_t);
			tmp249_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp249_AST);
			ASTPair __currentAST386 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,RANGE_EXCLUSIVE);
			_t = _t.getFirstChild();
			shiftExpression0e(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST386;
			_t = __t386;
			_t = _t.getNextSibling();
			shiftExpression0a_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = shiftExpression0a_AST;
		_retTree = _t;
	}

	public final void additiveExpression(AST _t) throws RecognitionException {

		AST additiveExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST additiveExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case PLUS:
		{
			AST __t424 = _t;
			AST tmp250_AST = null;
			AST tmp250_AST_in = null;
			tmp250_AST = astFactory.create((AST)_t);
			tmp250_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp250_AST);
			ASTPair __currentAST424 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,PLUS);
			_t = _t.getFirstChild();
			additiveExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			multiplicativeExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST424;
			_t = __t424;
			_t = _t.getNextSibling();
			additiveExpression_AST = (AST)currentAST.root;
			break;
		}
		case MINUS:
		{
			AST __t425 = _t;
			AST tmp251_AST = null;
			AST tmp251_AST_in = null;
			tmp251_AST = astFactory.create((AST)_t);
			tmp251_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp251_AST);
			ASTPair __currentAST425 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,MINUS);
			_t = _t.getFirstChild();
			additiveExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			multiplicativeExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST425;
			_t = __t425;
			_t = _t.getNextSibling();
			additiveExpression_AST = (AST)currentAST.root;
			break;
		}
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			multiplicativeExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = additiveExpression_AST;
		_retTree = _t;
	}

	public final void shiftExpression0b(AST _t) throws RecognitionException {

		AST shiftExpression0b_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST shiftExpression0b_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			shiftExpression0b_AST = (AST)currentAST.root;
			break;
		}
		case SL:
		{
			AST __t388 = _t;
			AST tmp252_AST = null;
			AST tmp252_AST_in = null;
			tmp252_AST = astFactory.create((AST)_t);
			tmp252_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp252_AST);
			ASTPair __currentAST388 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SL);
			_t = _t.getFirstChild();
			shiftExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST388;
			_t = __t388;
			_t = _t.getNextSibling();
			shiftExpression0b_AST = (AST)currentAST.root;
			break;
		}
		case SR:
		{
			AST __t389 = _t;
			AST tmp253_AST = null;
			AST tmp253_AST_in = null;
			tmp253_AST = astFactory.create((AST)_t);
			tmp253_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp253_AST);
			ASTPair __currentAST389 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SR);
			_t = _t.getFirstChild();
			shiftExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST389;
			_t = __t389;
			_t = _t.getNextSibling();
			shiftExpression0b_AST = (AST)currentAST.root;
			break;
		}
		case BSR:
		{
			AST __t390 = _t;
			AST tmp254_AST = null;
			AST tmp254_AST_in = null;
			tmp254_AST = astFactory.create((AST)_t);
			tmp254_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp254_AST);
			ASTPair __currentAST390 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BSR);
			_t = _t.getFirstChild();
			shiftExpression0c(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST390;
			_t = __t390;
			_t = _t.getNextSibling();
			shiftExpression0b_AST = (AST)currentAST.root;
			break;
		}
		case RANGE_INCLUSIVE:
		{
			AST __t391 = _t;
			AST tmp255_AST = null;
			AST tmp255_AST_in = null;
			tmp255_AST = astFactory.create((AST)_t);
			tmp255_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp255_AST);
			ASTPair __currentAST391 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,RANGE_INCLUSIVE);
			_t = _t.getFirstChild();
			shiftExpression0d(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST391;
			_t = __t391;
			_t = _t.getNextSibling();
			shiftExpression0b_AST = (AST)currentAST.root;
			break;
		}
		case RANGE_EXCLUSIVE:
		{
			AST __t392 = _t;
			AST tmp256_AST = null;
			AST tmp256_AST_in = null;
			tmp256_AST = astFactory.create((AST)_t);
			tmp256_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp256_AST);
			ASTPair __currentAST392 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,RANGE_EXCLUSIVE);
			_t = _t.getFirstChild();
			shiftExpression0e(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST392;
			_t = __t392;
			_t = _t.getNextSibling();
			shiftExpression0b_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = shiftExpression0b_AST;
		_retTree = _t;
	}

	public final void shiftExpression0c(AST _t) throws RecognitionException {

		AST shiftExpression0c_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST shiftExpression0c_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			shiftExpression0c_AST = (AST)currentAST.root;
			break;
		}
		case SL:
		{
			AST __t394 = _t;
			AST tmp257_AST = null;
			AST tmp257_AST_in = null;
			tmp257_AST = astFactory.create((AST)_t);
			tmp257_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp257_AST);
			ASTPair __currentAST394 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SL);
			_t = _t.getFirstChild();
			shiftExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST394;
			_t = __t394;
			_t = _t.getNextSibling();
			shiftExpression0c_AST = (AST)currentAST.root;
			break;
		}
		case SR:
		{
			AST __t395 = _t;
			AST tmp258_AST = null;
			AST tmp258_AST_in = null;
			tmp258_AST = astFactory.create((AST)_t);
			tmp258_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp258_AST);
			ASTPair __currentAST395 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SR);
			_t = _t.getFirstChild();
			shiftExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST395;
			_t = __t395;
			_t = _t.getNextSibling();
			shiftExpression0c_AST = (AST)currentAST.root;
			break;
		}
		case BSR:
		{
			AST __t396 = _t;
			AST tmp259_AST = null;
			AST tmp259_AST_in = null;
			tmp259_AST = astFactory.create((AST)_t);
			tmp259_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp259_AST);
			ASTPair __currentAST396 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BSR);
			_t = _t.getFirstChild();
			shiftExpression0c(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST396;
			_t = __t396;
			_t = _t.getNextSibling();
			shiftExpression0c_AST = (AST)currentAST.root;
			break;
		}
		case RANGE_INCLUSIVE:
		{
			AST __t397 = _t;
			AST tmp260_AST = null;
			AST tmp260_AST_in = null;
			tmp260_AST = astFactory.create((AST)_t);
			tmp260_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp260_AST);
			ASTPair __currentAST397 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,RANGE_INCLUSIVE);
			_t = _t.getFirstChild();
			shiftExpression0d(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST397;
			_t = __t397;
			_t = _t.getNextSibling();
			shiftExpression0c_AST = (AST)currentAST.root;
			break;
		}
		case RANGE_EXCLUSIVE:
		{
			AST __t398 = _t;
			AST tmp261_AST = null;
			AST tmp261_AST_in = null;
			tmp261_AST = astFactory.create((AST)_t);
			tmp261_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp261_AST);
			ASTPair __currentAST398 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,RANGE_EXCLUSIVE);
			_t = _t.getFirstChild();
			shiftExpression0e(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST398;
			_t = __t398;
			_t = _t.getNextSibling();
			shiftExpression0c_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = shiftExpression0c_AST;
		_retTree = _t;
	}

	public final void shiftExpression0d(AST _t) throws RecognitionException {

		AST shiftExpression0d_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST shiftExpression0d_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			shiftExpression0d_AST = (AST)currentAST.root;
			break;
		}
		case SL:
		{
			AST __t400 = _t;
			AST tmp262_AST = null;
			AST tmp262_AST_in = null;
			tmp262_AST = astFactory.create((AST)_t);
			tmp262_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp262_AST);
			ASTPair __currentAST400 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SL);
			_t = _t.getFirstChild();
			shiftExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST400;
			_t = __t400;
			_t = _t.getNextSibling();
			shiftExpression0d_AST = (AST)currentAST.root;
			break;
		}
		case SR:
		{
			AST __t401 = _t;
			AST tmp263_AST = null;
			AST tmp263_AST_in = null;
			tmp263_AST = astFactory.create((AST)_t);
			tmp263_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp263_AST);
			ASTPair __currentAST401 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SR);
			_t = _t.getFirstChild();
			shiftExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST401;
			_t = __t401;
			_t = _t.getNextSibling();
			shiftExpression0d_AST = (AST)currentAST.root;
			break;
		}
		case BSR:
		{
			AST __t402 = _t;
			AST tmp264_AST = null;
			AST tmp264_AST_in = null;
			tmp264_AST = astFactory.create((AST)_t);
			tmp264_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp264_AST);
			ASTPair __currentAST402 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BSR);
			_t = _t.getFirstChild();
			shiftExpression0c(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST402;
			_t = __t402;
			_t = _t.getNextSibling();
			shiftExpression0d_AST = (AST)currentAST.root;
			break;
		}
		case RANGE_INCLUSIVE:
		{
			AST __t403 = _t;
			AST tmp265_AST = null;
			AST tmp265_AST_in = null;
			tmp265_AST = astFactory.create((AST)_t);
			tmp265_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp265_AST);
			ASTPair __currentAST403 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,RANGE_INCLUSIVE);
			_t = _t.getFirstChild();
			shiftExpression0d(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST403;
			_t = __t403;
			_t = _t.getNextSibling();
			shiftExpression0d_AST = (AST)currentAST.root;
			break;
		}
		case RANGE_EXCLUSIVE:
		{
			AST __t404 = _t;
			AST tmp266_AST = null;
			AST tmp266_AST_in = null;
			tmp266_AST = astFactory.create((AST)_t);
			tmp266_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp266_AST);
			ASTPair __currentAST404 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,RANGE_EXCLUSIVE);
			_t = _t.getFirstChild();
			shiftExpression0e(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST404;
			_t = __t404;
			_t = _t.getNextSibling();
			shiftExpression0d_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = shiftExpression0d_AST;
		_retTree = _t;
	}

	public final void shiftExpression0e(AST _t) throws RecognitionException {

		AST shiftExpression0e_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST shiftExpression0e_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			shiftExpression0e_AST = (AST)currentAST.root;
			break;
		}
		case SL:
		{
			AST __t406 = _t;
			AST tmp267_AST = null;
			AST tmp267_AST_in = null;
			tmp267_AST = astFactory.create((AST)_t);
			tmp267_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp267_AST);
			ASTPair __currentAST406 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SL);
			_t = _t.getFirstChild();
			shiftExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST406;
			_t = __t406;
			_t = _t.getNextSibling();
			shiftExpression0e_AST = (AST)currentAST.root;
			break;
		}
		case SR:
		{
			AST __t407 = _t;
			AST tmp268_AST = null;
			AST tmp268_AST_in = null;
			tmp268_AST = astFactory.create((AST)_t);
			tmp268_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp268_AST);
			ASTPair __currentAST407 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SR);
			_t = _t.getFirstChild();
			shiftExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST407;
			_t = __t407;
			_t = _t.getNextSibling();
			shiftExpression0e_AST = (AST)currentAST.root;
			break;
		}
		case BSR:
		{
			AST __t408 = _t;
			AST tmp269_AST = null;
			AST tmp269_AST_in = null;
			tmp269_AST = astFactory.create((AST)_t);
			tmp269_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp269_AST);
			ASTPair __currentAST408 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BSR);
			_t = _t.getFirstChild();
			shiftExpression0c(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST408;
			_t = __t408;
			_t = _t.getNextSibling();
			shiftExpression0e_AST = (AST)currentAST.root;
			break;
		}
		case RANGE_INCLUSIVE:
		{
			AST __t409 = _t;
			AST tmp270_AST = null;
			AST tmp270_AST_in = null;
			tmp270_AST = astFactory.create((AST)_t);
			tmp270_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp270_AST);
			ASTPair __currentAST409 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,RANGE_INCLUSIVE);
			_t = _t.getFirstChild();
			shiftExpression0d(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST409;
			_t = __t409;
			_t = _t.getNextSibling();
			shiftExpression0e_AST = (AST)currentAST.root;
			break;
		}
		case RANGE_EXCLUSIVE:
		{
			AST __t410 = _t;
			AST tmp271_AST = null;
			AST tmp271_AST_in = null;
			tmp271_AST = astFactory.create((AST)_t);
			tmp271_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp271_AST);
			ASTPair __currentAST410 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,RANGE_EXCLUSIVE);
			_t = _t.getFirstChild();
			shiftExpression0e(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST410;
			_t = __t410;
			_t = _t.getNextSibling();
			shiftExpression0e_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = shiftExpression0e_AST;
		_retTree = _t;
	}

	public final void additiveExpression0a(AST _t) throws RecognitionException {

		AST additiveExpression0a_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST additiveExpression0a_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			multiplicativeExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case PLUS:
		{
			AST __t418 = _t;
			AST tmp272_AST = null;
			AST tmp272_AST_in = null;
			tmp272_AST = astFactory.create((AST)_t);
			tmp272_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp272_AST);
			ASTPair __currentAST418 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,PLUS);
			_t = _t.getFirstChild();
			additiveExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			multiplicativeExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST418;
			_t = __t418;
			_t = _t.getNextSibling();
			additiveExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case MINUS:
		{
			AST __t419 = _t;
			AST tmp273_AST = null;
			AST tmp273_AST_in = null;
			tmp273_AST = astFactory.create((AST)_t);
			tmp273_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp273_AST);
			ASTPair __currentAST419 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,MINUS);
			_t = _t.getFirstChild();
			additiveExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			multiplicativeExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST419;
			_t = __t419;
			_t = _t.getNextSibling();
			additiveExpression0a_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = additiveExpression0a_AST;
		_retTree = _t;
	}

	public final void multiplicativeExpression(AST _t) throws RecognitionException {

		AST multiplicativeExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST multiplicativeExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case STAR:
		{
			AST __t442 = _t;
			AST tmp274_AST = null;
			AST tmp274_AST_in = null;
			tmp274_AST = astFactory.create((AST)_t);
			tmp274_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp274_AST);
			ASTPair __currentAST442 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,STAR);
			_t = _t.getFirstChild();
			multiplicativeExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST442;
			_t = __t442;
			_t = _t.getNextSibling();
			multiplicativeExpression_AST = (AST)currentAST.root;
			break;
		}
		case DIV:
		{
			AST __t443 = _t;
			AST tmp275_AST = null;
			AST tmp275_AST_in = null;
			tmp275_AST = astFactory.create((AST)_t);
			tmp275_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp275_AST);
			ASTPair __currentAST443 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,DIV);
			_t = _t.getFirstChild();
			multiplicativeExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST443;
			_t = __t443;
			_t = _t.getNextSibling();
			multiplicativeExpression_AST = (AST)currentAST.root;
			break;
		}
		case MOD:
		{
			AST __t444 = _t;
			AST tmp276_AST = null;
			AST tmp276_AST_in = null;
			tmp276_AST = astFactory.create((AST)_t);
			tmp276_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp276_AST);
			ASTPair __currentAST444 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,MOD);
			_t = _t.getFirstChild();
			multiplicativeExpression0c(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST444;
			_t = __t444;
			_t = _t.getNextSibling();
			multiplicativeExpression_AST = (AST)currentAST.root;
			break;
		}
		case DEC:
		{
			AST __t445 = _t;
			AST tmp277_AST = null;
			AST tmp277_AST_in = null;
			tmp277_AST = astFactory.create((AST)_t);
			tmp277_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp277_AST);
			ASTPair __currentAST445 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,DEC);
			_t = _t.getFirstChild();
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST445;
			_t = __t445;
			_t = _t.getNextSibling();
			multiplicativeExpression_AST = (AST)currentAST.root;
			break;
		}
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			multiplicativeExpression_AST = (AST)currentAST.root;
			break;
		}
		case INC:
		{
			AST __t446 = _t;
			AST tmp278_AST = null;
			AST tmp278_AST_in = null;
			tmp278_AST = astFactory.create((AST)_t);
			tmp278_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp278_AST);
			ASTPair __currentAST446 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,INC);
			_t = _t.getFirstChild();
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST446;
			_t = __t446;
			_t = _t.getNextSibling();
			multiplicativeExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = multiplicativeExpression_AST;
		_retTree = _t;
	}

	public final void additiveExpression0b(AST _t) throws RecognitionException {

		AST additiveExpression0b_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST additiveExpression0b_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			multiplicativeExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			additiveExpression0b_AST = (AST)currentAST.root;
			break;
		}
		case PLUS:
		{
			AST __t421 = _t;
			AST tmp279_AST = null;
			AST tmp279_AST_in = null;
			tmp279_AST = astFactory.create((AST)_t);
			tmp279_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp279_AST);
			ASTPair __currentAST421 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,PLUS);
			_t = _t.getFirstChild();
			additiveExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			multiplicativeExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST421;
			_t = __t421;
			_t = _t.getNextSibling();
			additiveExpression0b_AST = (AST)currentAST.root;
			break;
		}
		case MINUS:
		{
			AST __t422 = _t;
			AST tmp280_AST = null;
			AST tmp280_AST_in = null;
			tmp280_AST = astFactory.create((AST)_t);
			tmp280_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp280_AST);
			ASTPair __currentAST422 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,MINUS);
			_t = _t.getFirstChild();
			additiveExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			multiplicativeExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST422;
			_t = __t422;
			_t = _t.getNextSibling();
			additiveExpression0b_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = additiveExpression0b_AST;
		_retTree = _t;
	}

	public final void multiplicativeExpression0a(AST _t) throws RecognitionException {

		AST multiplicativeExpression0a_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST multiplicativeExpression0a_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case INC:
		{
			AST __t427 = _t;
			AST tmp281_AST = null;
			AST tmp281_AST_in = null;
			tmp281_AST = astFactory.create((AST)_t);
			tmp281_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp281_AST);
			ASTPair __currentAST427 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,INC);
			_t = _t.getFirstChild();
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST427;
			_t = __t427;
			_t = _t.getNextSibling();
			multiplicativeExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case STAR:
		{
			AST __t428 = _t;
			AST tmp282_AST = null;
			AST tmp282_AST_in = null;
			tmp282_AST = astFactory.create((AST)_t);
			tmp282_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp282_AST);
			ASTPair __currentAST428 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,STAR);
			_t = _t.getFirstChild();
			multiplicativeExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST428;
			_t = __t428;
			_t = _t.getNextSibling();
			multiplicativeExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case DIV:
		{
			AST __t429 = _t;
			AST tmp283_AST = null;
			AST tmp283_AST_in = null;
			tmp283_AST = astFactory.create((AST)_t);
			tmp283_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp283_AST);
			ASTPair __currentAST429 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,DIV);
			_t = _t.getFirstChild();
			multiplicativeExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST429;
			_t = __t429;
			_t = _t.getNextSibling();
			multiplicativeExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case MOD:
		{
			AST __t430 = _t;
			AST tmp284_AST = null;
			AST tmp284_AST_in = null;
			tmp284_AST = astFactory.create((AST)_t);
			tmp284_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp284_AST);
			ASTPair __currentAST430 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,MOD);
			_t = _t.getFirstChild();
			multiplicativeExpression0c(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST430;
			_t = __t430;
			_t = _t.getNextSibling();
			multiplicativeExpression0a_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = multiplicativeExpression0a_AST;
		_retTree = _t;
	}

	public final void powerExpression(AST _t) throws RecognitionException {

		AST powerExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST powerExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case STAR_STAR:
		{
			AST __t450 = _t;
			AST tmp285_AST = null;
			AST tmp285_AST_in = null;
			tmp285_AST = astFactory.create((AST)_t);
			tmp285_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp285_AST);
			ASTPair __currentAST450 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,STAR_STAR);
			_t = _t.getFirstChild();
			powerExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			unaryExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST450;
			_t = __t450;
			_t = _t.getNextSibling();
			powerExpression_AST = (AST)currentAST.root;
			break;
		}
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			unaryExpressionNotPlusMinus(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			powerExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = powerExpression_AST;
		_retTree = _t;
	}

	public final void multiplicativeExpression0b(AST _t) throws RecognitionException {

		AST multiplicativeExpression0b_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST multiplicativeExpression0b_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case INC:
		{
			AST __t432 = _t;
			AST tmp286_AST = null;
			AST tmp286_AST_in = null;
			tmp286_AST = astFactory.create((AST)_t);
			tmp286_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp286_AST);
			ASTPair __currentAST432 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,INC);
			_t = _t.getFirstChild();
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST432;
			_t = __t432;
			_t = _t.getNextSibling();
			multiplicativeExpression0b_AST = (AST)currentAST.root;
			break;
		}
		case STAR:
		{
			AST __t433 = _t;
			AST tmp287_AST = null;
			AST tmp287_AST_in = null;
			tmp287_AST = astFactory.create((AST)_t);
			tmp287_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp287_AST);
			ASTPair __currentAST433 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,STAR);
			_t = _t.getFirstChild();
			multiplicativeExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST433;
			_t = __t433;
			_t = _t.getNextSibling();
			multiplicativeExpression0b_AST = (AST)currentAST.root;
			break;
		}
		case DIV:
		{
			AST __t434 = _t;
			AST tmp288_AST = null;
			AST tmp288_AST_in = null;
			tmp288_AST = astFactory.create((AST)_t);
			tmp288_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp288_AST);
			ASTPair __currentAST434 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,DIV);
			_t = _t.getFirstChild();
			multiplicativeExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST434;
			_t = __t434;
			_t = _t.getNextSibling();
			multiplicativeExpression0b_AST = (AST)currentAST.root;
			break;
		}
		case MOD:
		{
			AST __t435 = _t;
			AST tmp289_AST = null;
			AST tmp289_AST_in = null;
			tmp289_AST = astFactory.create((AST)_t);
			tmp289_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp289_AST);
			ASTPair __currentAST435 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,MOD);
			_t = _t.getFirstChild();
			multiplicativeExpression0c(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST435;
			_t = __t435;
			_t = _t.getNextSibling();
			multiplicativeExpression0b_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = multiplicativeExpression0b_AST;
		_retTree = _t;
	}

	public final void multiplicativeExpression0c(AST _t) throws RecognitionException {

		AST multiplicativeExpression0c_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST multiplicativeExpression0c_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case INC:
		{
			AST __t437 = _t;
			AST tmp290_AST = null;
			AST tmp290_AST_in = null;
			tmp290_AST = astFactory.create((AST)_t);
			tmp290_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp290_AST);
			ASTPair __currentAST437 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,INC);
			_t = _t.getFirstChild();
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST437;
			_t = __t437;
			_t = _t.getNextSibling();
			multiplicativeExpression0c_AST = (AST)currentAST.root;
			break;
		}
		case STAR:
		{
			AST __t438 = _t;
			AST tmp291_AST = null;
			AST tmp291_AST_in = null;
			tmp291_AST = astFactory.create((AST)_t);
			tmp291_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp291_AST);
			ASTPair __currentAST438 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,STAR);
			_t = _t.getFirstChild();
			multiplicativeExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST438;
			_t = __t438;
			_t = _t.getNextSibling();
			multiplicativeExpression0c_AST = (AST)currentAST.root;
			break;
		}
		case DIV:
		{
			AST __t439 = _t;
			AST tmp292_AST = null;
			AST tmp292_AST_in = null;
			tmp292_AST = astFactory.create((AST)_t);
			tmp292_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp292_AST);
			ASTPair __currentAST439 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,DIV);
			_t = _t.getFirstChild();
			multiplicativeExpression0b(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST439;
			_t = __t439;
			_t = _t.getNextSibling();
			multiplicativeExpression0c_AST = (AST)currentAST.root;
			break;
		}
		case MOD:
		{
			AST __t440 = _t;
			AST tmp293_AST = null;
			AST tmp293_AST_in = null;
			tmp293_AST = astFactory.create((AST)_t);
			tmp293_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp293_AST);
			ASTPair __currentAST440 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,MOD);
			_t = _t.getFirstChild();
			multiplicativeExpression0c(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			powerExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST440;
			_t = __t440;
			_t = _t.getNextSibling();
			multiplicativeExpression0c_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = multiplicativeExpression0c_AST;
		_retTree = _t;
	}

	public final void powerExpression0a(AST _t) throws RecognitionException {

		AST powerExpression0a_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST powerExpression0a_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			unaryExpressionNotPlusMinus(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			powerExpression0a_AST = (AST)currentAST.root;
			break;
		}
		case STAR_STAR:
		{
			AST __t448 = _t;
			AST tmp294_AST = null;
			AST tmp294_AST_in = null;
			tmp294_AST = astFactory.create((AST)_t);
			tmp294_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp294_AST);
			ASTPair __currentAST448 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,STAR_STAR);
			_t = _t.getFirstChild();
			powerExpression0a(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			unaryExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST448;
			_t = __t448;
			_t = _t.getNextSibling();
			powerExpression0a_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = powerExpression0a_AST;
		_retTree = _t;
	}

	public final void unaryExpressionNotPlusMinus(AST _t) throws RecognitionException {

		AST unaryExpressionNotPlusMinus_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST unaryExpressionNotPlusMinus_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case MEMBER_POINTER_DEFAULT:
		{
			AST __t457 = _t;
			AST tmp295_AST = null;
			AST tmp295_AST_in = null;
			tmp295_AST = astFactory.create((AST)_t);
			tmp295_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp295_AST);
			ASTPair __currentAST457 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,MEMBER_POINTER_DEFAULT);
			_t = _t.getFirstChild();
			namePart(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST457;
			_t = __t457;
			_t = _t.getNextSibling();
			unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
			break;
		}
		case BNOT:
		{
			AST __t458 = _t;
			AST tmp296_AST = null;
			AST tmp296_AST_in = null;
			tmp296_AST = astFactory.create((AST)_t);
			tmp296_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp296_AST);
			ASTPair __currentAST458 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BNOT);
			_t = _t.getFirstChild();
			unaryExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST458;
			_t = __t458;
			_t = _t.getNextSibling();
			unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
			break;
		}
		case LNOT:
		{
			AST __t459 = _t;
			AST tmp297_AST = null;
			AST tmp297_AST_in = null;
			tmp297_AST = astFactory.create((AST)_t);
			tmp297_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp297_AST);
			ASTPair __currentAST459 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LNOT);
			_t = _t.getFirstChild();
			unaryExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST459;
			_t = __t459;
			_t = _t.getNextSibling();
			unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
			break;
		}
		case TYPECAST:
		{
			{
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==TYPECAST)) {
				AST __t461 = _t;
				AST tmp298_AST = null;
				AST tmp298_AST_in = null;
				tmp298_AST = astFactory.create((AST)_t);
				tmp298_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp298_AST);
				ASTPair __currentAST461 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,TYPECAST);
				_t = _t.getFirstChild();
				builtInTypeSpec(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST461;
				_t = __t461;
				_t = _t.getNextSibling();
			}
			else if ((_t.getType()==TYPECAST)) {
				AST __t462 = _t;
				AST tmp299_AST = null;
				AST tmp299_AST_in = null;
				tmp299_AST = astFactory.create((AST)_t);
				tmp299_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp299_AST);
				ASTPair __currentAST462 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,TYPECAST);
				_t = _t.getFirstChild();
				classTypeSpec(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpressionNotPlusMinus(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST462;
				_t = __t462;
				_t = _t.getNextSibling();
			}
			else {
				throw new NoViableAltException(_t);
			}

			}
			unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = unaryExpressionNotPlusMinus_AST;
		_retTree = _t;
	}

	public final void unaryExpression(AST _t) throws RecognitionException {

		AST unaryExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST unaryExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case DEC:
		{
			AST __t452 = _t;
			AST tmp300_AST = null;
			AST tmp300_AST_in = null;
			tmp300_AST = astFactory.create((AST)_t);
			tmp300_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp300_AST);
			ASTPair __currentAST452 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,DEC);
			_t = _t.getFirstChild();
			unaryExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST452;
			_t = __t452;
			_t = _t.getNextSibling();
			unaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case UNARY_MINUS:
		{
			AST __t453 = _t;
			AST tmp301_AST = null;
			AST tmp301_AST_in = null;
			tmp301_AST = astFactory.create((AST)_t);
			tmp301_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp301_AST);
			ASTPair __currentAST453 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,UNARY_MINUS);
			_t = _t.getFirstChild();
			unaryExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST453;
			_t = __t453;
			_t = _t.getNextSibling();
			unaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case UNARY_PLUS:
		{
			AST __t454 = _t;
			AST tmp302_AST = null;
			AST tmp302_AST_in = null;
			tmp302_AST = astFactory.create((AST)_t);
			tmp302_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp302_AST);
			ASTPair __currentAST454 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,UNARY_PLUS);
			_t = _t.getFirstChild();
			unaryExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST454;
			_t = __t454;
			_t = _t.getNextSibling();
			unaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			unaryExpressionNotPlusMinus(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			unaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case INC:
		{
			AST __t455 = _t;
			AST tmp303_AST = null;
			AST tmp303_AST_in = null;
			tmp303_AST = astFactory.create((AST)_t);
			tmp303_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp303_AST);
			ASTPair __currentAST455 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,INC);
			_t = _t.getFirstChild();
			unaryExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST455;
			_t = __t455;
			_t = _t.getNextSibling();
			unaryExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = unaryExpression_AST;
		_retTree = _t;
	}

	public final void postfixExpression(AST _t) throws RecognitionException {

		AST postfixExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST postfixExpression_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case POST_INC:
		{
			AST __t464 = _t;
			AST tmp304_AST = null;
			AST tmp304_AST_in = null;
			tmp304_AST = astFactory.create((AST)_t);
			tmp304_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp304_AST);
			ASTPair __currentAST464 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,POST_INC);
			_t = _t.getFirstChild();
			pathExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST464;
			_t = __t464;
			_t = _t.getNextSibling();
			postfixExpression_AST = (AST)currentAST.root;
			break;
		}
		case POST_DEC:
		{
			AST __t465 = _t;
			AST tmp305_AST = null;
			AST tmp305_AST_in = null;
			tmp305_AST = astFactory.create((AST)_t);
			tmp305_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp305_AST);
			ASTPair __currentAST465 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,POST_DEC);
			_t = _t.getFirstChild();
			pathExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST465;
			_t = __t465;
			_t = _t.getNextSibling();
			postfixExpression_AST = (AST)currentAST.root;
			break;
		}
		case IDENT:
		case LITERAL_super:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		case LITERAL_as:
		case ANNOTATION:
		case VARIABLE_DEF:
		case ASSIGN:
		case CLOSED_BLOCK:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		case LITERAL_return:
		case STRING_LITERAL:
		case LITERAL_in:
		case PLUS_ASSIGN:
		case MINUS_ASSIGN:
		case STAR_ASSIGN:
		case DIV_ASSIGN:
		case MOD_ASSIGN:
		case SR_ASSIGN:
		case BSR_ASSIGN:
		case SL_ASSIGN:
		case BAND_ASSIGN:
		case BXOR_ASSIGN:
		case BOR_ASSIGN:
		case STAR_STAR_ASSIGN:
		case QUESTION:
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		case LITERAL_this:
		case SCOPE_ESCAPE:
		case STRING_CTOR_START:
		case MAP_CONSTRUCTOR:
		case LIST_CONSTRUCTOR:
		case LITERAL_new:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case NUM_INT:
		case NUM_FLOAT:
		case NUM_LONG:
		case NUM_DOUBLE:
		case NUM_BIG_INT:
		case NUM_BIG_DECIMAL:
		{
			pathExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			postfixExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = postfixExpression_AST;
		_retTree = _t;
	}

	public final void constant(AST _t) throws RecognitionException {

		AST constant_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NUM_INT:
		case NUM_FLOAT:
		case NUM_LONG:
		case NUM_DOUBLE:
		case NUM_BIG_INT:
		case NUM_BIG_DECIMAL:
		{
			constantNumber(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			constant_AST = (AST)currentAST.root;
			break;
		}
		case STRING_LITERAL:
		{
			AST tmp306_AST = null;
			AST tmp306_AST_in = null;
			tmp306_AST = astFactory.create((AST)_t);
			tmp306_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp306_AST);
			match(_t,STRING_LITERAL);
			_t = _t.getNextSibling();
			constant_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_true:
		{
			AST tmp307_AST = null;
			AST tmp307_AST_in = null;
			tmp307_AST = astFactory.create((AST)_t);
			tmp307_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp307_AST);
			match(_t,LITERAL_true);
			_t = _t.getNextSibling();
			constant_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_false:
		{
			AST tmp308_AST = null;
			AST tmp308_AST_in = null;
			tmp308_AST = astFactory.create((AST)_t);
			tmp308_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp308_AST);
			match(_t,LITERAL_false);
			_t = _t.getNextSibling();
			constant_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_null:
		{
			AST tmp309_AST = null;
			AST tmp309_AST_in = null;
			tmp309_AST = astFactory.create((AST)_t);
			tmp309_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp309_AST);
			match(_t,LITERAL_null);
			_t = _t.getNextSibling();
			constant_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = constant_AST;
		_retTree = _t;
	}

	public final void newExpression(AST _t) throws RecognitionException {

		AST newExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST newExpression_AST = null;

		AST __t487 = _t;
		AST tmp310_AST = null;
		AST tmp310_AST_in = null;
		tmp310_AST = astFactory.create((AST)_t);
		tmp310_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp310_AST);
		ASTPair __currentAST487 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,LITERAL_new);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case TYPE_ARGUMENTS:
		{
			typeArguments(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case IDENT:
		case DOT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_any:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		type(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case METHOD_CALL:
		{
			methodCallArgs(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case CLOSED_BLOCK:
			{
				appendedBlock(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			break;
		}
		case CLOSED_BLOCK:
		{
			appendedBlock(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case ARRAY_DECLARATOR:
		{
			newArrayDeclarator(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST487;
		_t = __t487;
		_t = _t.getNextSibling();
		newExpression_AST = (AST)currentAST.root;
		returnAST = newExpression_AST;
		_retTree = _t;
	}

	public final void closureConstructorExpression(AST _t) throws RecognitionException {

		AST closureConstructorExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST closureConstructorExpression_AST = null;

		closedBlock(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		closureConstructorExpression_AST = (AST)currentAST.root;
		returnAST = closureConstructorExpression_AST;
		_retTree = _t;
	}

	public final void listOrMapConstructorExpression(AST _t) throws RecognitionException {

		AST listOrMapConstructorExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST listOrMapConstructorExpression_AST = null;

		{
		if (_t==null) _t=ASTNULL;
		if ((_t.getType()==MAP_CONSTRUCTOR)) {
			AST __t483 = _t;
			AST tmp311_AST = null;
			AST tmp311_AST_in = null;
			tmp311_AST = astFactory.create((AST)_t);
			tmp311_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp311_AST);
			ASTPair __currentAST483 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,MAP_CONSTRUCTOR);
			_t = _t.getFirstChild();
			argList(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST483;
			_t = __t483;
			_t = _t.getNextSibling();
		}
		else if ((_t.getType()==LIST_CONSTRUCTOR)) {
			AST __t484 = _t;
			AST tmp312_AST = null;
			AST tmp312_AST_in = null;
			tmp312_AST = astFactory.create((AST)_t);
			tmp312_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp312_AST);
			ASTPair __currentAST484 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LIST_CONSTRUCTOR);
			_t = _t.getFirstChild();
			argList(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST484;
			_t = __t484;
			_t = _t.getNextSibling();
		}
		else if ((_t.getType()==MAP_CONSTRUCTOR)) {
			AST __t485 = _t;
			AST tmp313_AST = null;
			AST tmp313_AST_in = null;
			tmp313_AST = astFactory.create((AST)_t);
			tmp313_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp313_AST);
			ASTPair __currentAST485 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,MAP_CONSTRUCTOR);
			_t = _t.getFirstChild();
			AST tmp314_AST = null;
			AST tmp314_AST_in = null;
			tmp314_AST = astFactory.create((AST)_t);
			tmp314_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp314_AST);
			match(_t,COLON);
			_t = _t.getNextSibling();
			currentAST = __currentAST485;
			_t = __t485;
			_t = _t.getNextSibling();
		}
		else {
			throw new NoViableAltException(_t);
		}

		}
		listOrMapConstructorExpression_AST = (AST)currentAST.root;
		returnAST = listOrMapConstructorExpression_AST;
		_retTree = _t;
	}

	public final void scopeEscapeExpression(AST _t) throws RecognitionException {

		AST scopeEscapeExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST scopeEscapeExpression_AST = null;

		AST __t469 = _t;
		AST tmp315_AST = null;
		AST tmp315_AST_in = null;
		tmp315_AST = astFactory.create((AST)_t);
		tmp315_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp315_AST);
		ASTPair __currentAST469 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,SCOPE_ESCAPE);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case IDENT:
		{
			AST tmp316_AST = null;
			AST tmp316_AST_in = null;
			tmp316_AST = astFactory.create((AST)_t);
			tmp316_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp316_AST);
			match(_t,IDENT);
			_t = _t.getNextSibling();
			break;
		}
		case SCOPE_ESCAPE:
		{
			scopeEscapeExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST469;
		_t = __t469;
		_t = _t.getNextSibling();
		scopeEscapeExpression_AST = (AST)currentAST.root;
		returnAST = scopeEscapeExpression_AST;
		_retTree = _t;
	}

	public final void stringConstructorValuePart(AST _t) throws RecognitionException {

		AST stringConstructorValuePart_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST stringConstructorValuePart_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case SPREAD_ARG:
		{
			AST __t478 = _t;
			AST tmp317_AST = null;
			AST tmp317_AST_in = null;
			tmp317_AST = astFactory.create((AST)_t);
			tmp317_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp317_AST);
			ASTPair __currentAST478 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SPREAD_ARG);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case IDENT:
			case DOT:
			{
				identifier(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LCURLY:
			{
				openOrClosedBlock(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST478;
			_t = __t478;
			_t = _t.getNextSibling();
			stringConstructorValuePart_AST = (AST)currentAST.root;
			break;
		}
		case IDENT:
		case DOT:
		case LCURLY:
		{
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case IDENT:
			case DOT:
			{
				identifier(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LCURLY:
			{
				openOrClosedBlock(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			stringConstructorValuePart_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = stringConstructorValuePart_AST;
		_retTree = _t;
	}

	public final void newArrayDeclarator(AST _t) throws RecognitionException {

		AST newArrayDeclarator_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST newArrayDeclarator_AST = null;

		newArrayDeclarator0a(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		newArrayDeclarator_AST = (AST)currentAST.root;
		returnAST = newArrayDeclarator_AST;
		_retTree = _t;
	}

	public final void argument(AST _t) throws RecognitionException {

		AST argument_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST argument_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LABELED_ARG:
		{
			AST __t495 = _t;
			AST tmp318_AST = null;
			AST tmp318_AST_in = null;
			tmp318_AST = astFactory.create((AST)_t);
			tmp318_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp318_AST);
			ASTPair __currentAST495 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LABELED_ARG);
			_t = _t.getFirstChild();
			argumentLabel(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			strictContextExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST495;
			_t = __t495;
			_t = _t.getNextSibling();
			argument_AST = (AST)currentAST.root;
			break;
		}
		case SPREAD_ARG:
		{
			AST __t496 = _t;
			AST tmp319_AST = null;
			AST tmp319_AST_in = null;
			tmp319_AST = astFactory.create((AST)_t);
			tmp319_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp319_AST);
			ASTPair __currentAST496 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SPREAD_ARG);
			_t = _t.getFirstChild();
			strictContextExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST496;
			_t = __t496;
			_t = _t.getNextSibling();
			argument_AST = (AST)currentAST.root;
			break;
		}
		case SPREAD_MAP_ARG:
		{
			AST __t497 = _t;
			AST tmp320_AST = null;
			AST tmp320_AST_in = null;
			tmp320_AST = astFactory.create((AST)_t);
			tmp320_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp320_AST);
			ASTPair __currentAST497 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SPREAD_MAP_ARG);
			_t = _t.getFirstChild();
			AST tmp321_AST = null;
			AST tmp321_AST_in = null;
			tmp321_AST = astFactory.create((AST)_t);
			tmp321_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp321_AST);
			match(_t,COLON);
			_t = _t.getNextSibling();
			strictContextExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST497;
			_t = __t497;
			_t = _t.getNextSibling();
			argument_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_as:
		case ANNOTATION:
		case VARIABLE_DEF:
		case ASSIGN:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		case LITERAL_return:
		case LITERAL_in:
		case PLUS_ASSIGN:
		case MINUS_ASSIGN:
		case STAR_ASSIGN:
		case DIV_ASSIGN:
		case MOD_ASSIGN:
		case SR_ASSIGN:
		case BSR_ASSIGN:
		case SL_ASSIGN:
		case BAND_ASSIGN:
		case BXOR_ASSIGN:
		case BOR_ASSIGN:
		case STAR_STAR_ASSIGN:
		case QUESTION:
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			strictContextExpression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			argument_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = argument_AST;
		_retTree = _t;
	}

	public final void argumentLabel(AST _t) throws RecognitionException {

		AST argumentLabel_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST argumentLabel_AST = null;

		primaryExpression(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		argumentLabel_AST = (AST)currentAST.root;
		returnAST = argumentLabel_AST;
		_retTree = _t;
	}

	public final void newArrayDeclarator0a(AST _t) throws RecognitionException {

		AST newArrayDeclarator0a_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST newArrayDeclarator0a_AST = null;

		AST __t500 = _t;
		AST tmp322_AST = null;
		AST tmp322_AST_in = null;
		tmp322_AST = astFactory.create((AST)_t);
		tmp322_AST_in = (AST)_t;
		astFactory.addASTChild(currentAST, tmp322_AST);
		ASTPair __currentAST500 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,ARRAY_DECLARATOR);
		_t = _t.getFirstChild();
		newArrayDeclarator0a(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		case ASSIGN:
		case LITERAL_in:
		case PLUS_ASSIGN:
		case MINUS_ASSIGN:
		case STAR_ASSIGN:
		case DIV_ASSIGN:
		case MOD_ASSIGN:
		case SR_ASSIGN:
		case BSR_ASSIGN:
		case SL_ASSIGN:
		case BAND_ASSIGN:
		case BXOR_ASSIGN:
		case BOR_ASSIGN:
		case STAR_STAR_ASSIGN:
		case QUESTION:
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LT:
		case GT:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case SR:
		case BSR:
		case RANGE_INCLUSIVE:
		case RANGE_EXCLUSIVE:
		case PLUS:
		case MINUS:
		case INC:
		case STAR:
		case DIV:
		case MOD:
		case DEC:
		case STAR_STAR:
		case MEMBER_POINTER_DEFAULT:
		case BNOT:
		case LNOT:
		case TYPECAST:
		{
			expression(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case 3:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST500;
		_t = __t500;
		_t = _t.getNextSibling();
		newArrayDeclarator0a_AST = (AST)currentAST.root;
		returnAST = newArrayDeclarator0a_AST;
		_retTree = _t;
	}

	public final void constantNumber(AST _t) throws RecognitionException {

		AST constantNumber_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constantNumber_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NUM_INT:
		{
			AST tmp323_AST = null;
			AST tmp323_AST_in = null;
			tmp323_AST = astFactory.create((AST)_t);
			tmp323_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp323_AST);
			match(_t,NUM_INT);
			_t = _t.getNextSibling();
			constantNumber_AST = (AST)currentAST.root;
			break;
		}
		case NUM_FLOAT:
		{
			AST tmp324_AST = null;
			AST tmp324_AST_in = null;
			tmp324_AST = astFactory.create((AST)_t);
			tmp324_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp324_AST);
			match(_t,NUM_FLOAT);
			_t = _t.getNextSibling();
			constantNumber_AST = (AST)currentAST.root;
			break;
		}
		case NUM_LONG:
		{
			AST tmp325_AST = null;
			AST tmp325_AST_in = null;
			tmp325_AST = astFactory.create((AST)_t);
			tmp325_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp325_AST);
			match(_t,NUM_LONG);
			_t = _t.getNextSibling();
			constantNumber_AST = (AST)currentAST.root;
			break;
		}
		case NUM_DOUBLE:
		{
			AST tmp326_AST = null;
			AST tmp326_AST_in = null;
			tmp326_AST = astFactory.create((AST)_t);
			tmp326_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp326_AST);
			match(_t,NUM_DOUBLE);
			_t = _t.getNextSibling();
			constantNumber_AST = (AST)currentAST.root;
			break;
		}
		case NUM_BIG_INT:
		{
			AST tmp327_AST = null;
			AST tmp327_AST_in = null;
			tmp327_AST = astFactory.create((AST)_t);
			tmp327_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp327_AST);
			match(_t,NUM_BIG_INT);
			_t = _t.getNextSibling();
			constantNumber_AST = (AST)currentAST.root;
			break;
		}
		case NUM_BIG_DECIMAL:
		{
			AST tmp328_AST = null;
			AST tmp328_AST_in = null;
			tmp328_AST = astFactory.create((AST)_t);
			tmp328_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp328_AST);
			match(_t,NUM_BIG_DECIMAL);
			_t = _t.getNextSibling();
			constantNumber_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = constantNumber_AST;
		_retTree = _t;
	}

	public final void balancedBrackets(AST _t) throws RecognitionException {

		AST balancedBrackets_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST balancedBrackets_AST = null;

		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LPAREN:
		{
			AST tmp329_AST = null;
			AST tmp329_AST_in = null;
			tmp329_AST = astFactory.create((AST)_t);
			tmp329_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp329_AST);
			match(_t,LPAREN);
			_t = _t.getNextSibling();
			balancedTokens(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp330_AST = null;
			AST tmp330_AST_in = null;
			tmp330_AST = astFactory.create((AST)_t);
			tmp330_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp330_AST);
			match(_t,RPAREN);
			_t = _t.getNextSibling();
			balancedBrackets_AST = (AST)currentAST.root;
			break;
		}
		case LBRACK:
		{
			AST tmp331_AST = null;
			AST tmp331_AST_in = null;
			tmp331_AST = astFactory.create((AST)_t);
			tmp331_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp331_AST);
			match(_t,LBRACK);
			_t = _t.getNextSibling();
			balancedTokens(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp332_AST = null;
			AST tmp332_AST_in = null;
			tmp332_AST = astFactory.create((AST)_t);
			tmp332_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp332_AST);
			match(_t,RBRACK);
			_t = _t.getNextSibling();
			balancedBrackets_AST = (AST)currentAST.root;
			break;
		}
		case LCURLY:
		{
			AST tmp333_AST = null;
			AST tmp333_AST_in = null;
			tmp333_AST = astFactory.create((AST)_t);
			tmp333_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp333_AST);
			match(_t,LCURLY);
			_t = _t.getNextSibling();
			balancedTokens(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp334_AST = null;
			AST tmp334_AST_in = null;
			tmp334_AST = astFactory.create((AST)_t);
			tmp334_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp334_AST);
			match(_t,RCURLY);
			_t = _t.getNextSibling();
			balancedBrackets_AST = (AST)currentAST.root;
			break;
		}
		case STRING_CTOR_START:
		{
			AST tmp335_AST = null;
			AST tmp335_AST_in = null;
			tmp335_AST = astFactory.create((AST)_t);
			tmp335_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp335_AST);
			match(_t,STRING_CTOR_START);
			_t = _t.getNextSibling();
			balancedTokens(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp336_AST = null;
			AST tmp336_AST_in = null;
			tmp336_AST = astFactory.create((AST)_t);
			tmp336_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp336_AST);
			match(_t,STRING_CTOR_END);
			_t = _t.getNextSibling();
			balancedBrackets_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = balancedBrackets_AST;
		_retTree = _t;
	}


	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"SH_COMMENT",
		"PACKAGE_DEF",
		"STATIC_IMPORT",
		"IMPORT",
		"\"def\"",
		"AT",
		"IDENT",
		"LBRACK",
		"RBRACK",
		"DOT",
		"\"class\"",
		"\"interface\"",
		"\"enum\"",
		"TYPE_ARGUMENT",
		"WILDCARD_TYPE",
		"TYPE_ARGUMENTS",
		"\"extends\"",
		"\"super\"",
		"\"void\"",
		"\"boolean\"",
		"\"byte\"",
		"\"char\"",
		"\"short\"",
		"\"int\"",
		"\"float\"",
		"\"long\"",
		"\"double\"",
		"\"any\"",
		"\"as\"",
		"MODIFIERS",
		"\"private\"",
		"\"public\"",
		"\"protected\"",
		"\"static\"",
		"\"transient\"",
		"\"final\"",
		"\"abstract\"",
		"\"native\"",
		"\"threadsafe\"",
		"\"synchronized\"",
		"\"volatile\"",
		"\"strictfp\"",
		"ANNOTATION",
		"ANNOTATIONS",
		"ANNOTATION_MEMBER_VALUE_PAIR",
		"EXTENDS_CLAUSE",
		"CLASS_DEF",
		"INTERFACE_DEF",
		"ENUM_DEF",
		"ANNOTATION_DEF",
		"TYPE_PARAMETERS",
		"TYPE_PARAMETER",
		"TYPE_UPPER_BOUNDS",
		"OBJBLOCK",
		"ANNOTATION_FIELD_DEF",
		"\"default\"",
		"ENUM_CONSTANT_DEF",
		"METHOD_DEF",
		"INSTANCE_INIT",
		"IMPLEMENTS_CLAUSE",
		"SLIST",
		"CTOR_CALL",
		"SUPER_CTOR_CALL",
		"CTOR_IDENT",
		"VARIABLE_DEF",
		"ARRAY_DECLARATOR",
		"ASSIGN",
		"PARAMETER_DEF",
		"\"throws\"",
		"PARAMETERS",
		"VARIABLE_PARAMETER_DEF",
		"TRIPLE_DOT",
		"CLOSED_BLOCK",
		"LCURLY",
		"\"while\"",
		"\"with\"",
		"SPREAD_ARG",
		"\"for\"",
		"FOR_IN_ITERABLE",
		"\"break\"",
		"\"continue\"",
		"\"throw\"",
		"\"assert\"",
		"\"return\"",
		"LABELED_STAT",
		"EXPR",
		"CASE_GROUP",
		"\"case\"",
		"FOR_INIT",
		"FOR_CONDITION",
		"FOR_ITERATOR",
		"\"try\"",
		"\"finally\"",
		"\"catch\"",
		"METHOD_CALL",
		"ELIST",
		"SPREAD_DOT",
		"OPTIONAL_DOT",
		"MEMBER_POINTER",
		"STRING_LITERAL",
		"\"in\"",
		"\"if\"",
		"\"else\"",
		"\"do\"",
		"\"switch\"",
		"DYNAMIC_MEMBER",
		"INDEX_OP",
		"PLUS_ASSIGN",
		"MINUS_ASSIGN",
		"STAR_ASSIGN",
		"DIV_ASSIGN",
		"MOD_ASSIGN",
		"SR_ASSIGN",
		"BSR_ASSIGN",
		"SL_ASSIGN",
		"BAND_ASSIGN",
		"BXOR_ASSIGN",
		"BOR_ASSIGN",
		"STAR_STAR_ASSIGN",
		"QUESTION",
		"LOR",
		"LAND",
		"BOR",
		"BXOR",
		"BAND",
		"REGEX_FIND",
		"REGEX_MATCH",
		"NOT_EQUAL",
		"EQUAL",
		"COMPARE_TO",
		"LT",
		"GT",
		"LE",
		"GE",
		"\"instanceof\"",
		"SL",
		"SR",
		"BSR",
		"RANGE_INCLUSIVE",
		"RANGE_EXCLUSIVE",
		"PLUS",
		"MINUS",
		"INC",
		"STAR",
		"DIV",
		"MOD",
		"DEC",
		"STAR_STAR",
		"UNARY_MINUS",
		"UNARY_PLUS",
		"MEMBER_POINTER_DEFAULT",
		"BNOT",
		"LNOT",
		"TYPECAST",
		"POST_INC",
		"POST_DEC",
		"\"this\"",
		"SCOPE_ESCAPE",
		"STRING_CTOR_START",
		"STRING_CTOR_MIDDLE",
		"STRING_CTOR_END",
		"MAP_CONSTRUCTOR",
		"LIST_CONSTRUCTOR",
		"COLON",
		"\"new\"",
		"LABELED_ARG",
		"SPREAD_MAP_ARG",
		"\"true\"",
		"\"false\"",
		"\"null\"",
		"NUM_INT",
		"NUM_FLOAT",
		"NUM_LONG",
		"NUM_DOUBLE",
		"NUM_BIG_INT",
		"NUM_BIG_DECIMAL",
		"LPAREN",
		"RPAREN",
		"RCURLY",
		"NLS"
	};

	private static final long[] mk_tokenSet_0() {
		long[] data = { 16897307576378560L, 2197544960L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 2048L, 8192L, 4503616807239680L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = new long[12];
		data[0]=-6160L;
		data[1]=-8193L;
		data[2]=40532310746988543L;
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 16888648922309632L, 9L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 16888511483356160L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 305118874754351104L, 16L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 70373039144960L, -139637960409008L, 6598093176831L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 6934417526243721216L, 16L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 4294967296L, -139637976727488L, 1023410175L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 70373039144960L, -139637960474544L, 1023410175L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 8192L, 70866960388096L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	}


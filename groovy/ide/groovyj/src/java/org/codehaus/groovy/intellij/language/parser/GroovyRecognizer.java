// $ANTLR 2.8.0 (20050214): "groovy.patched.g" -> "GroovyRecognizer.java"$

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
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.antlr.GroovySourceAST;

import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.InputBuffer;
import antlr.LexerSharedInputState;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.SemanticException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.AST;
import antlr.collections.impl.ASTArray;
import antlr.collections.impl.BitSet;

/** JSR-241 Groovy Recognizer
 *
 * Run 'java Main [-showtree] directory-full-of-groovy-files'
 *
 * [The -showtree option pops up a Swing frame that shows
 *  the AST constructed from the parser.]
 *
 * Contributing authors:
 *              John Mitchell           johnm@non.net
 *              Terence Parr            parrt@magelang.com
 *              John Lilley             jlilley@empathy.com
 *              Scott Stanchfield       thetick@magelang.com
 *              Markus Mohnen           mohnen@informatik.rwth-aachen.de
 *              Peter Williams          pete.williams@sun.com
 *              Allan Jacobs            Allan.Jacobs@eng.sun.com
 *              Steve Messick           messick@redhills.com
 *              James Strachan          jstrachan@protique.com
 *              John Pybus              john@pybus.org
 *              John Rose               rose00@mac.com
 *              Jeremy Rayner           groovy@ross-rayner.com
 *
 * Version 1.00 December 9, 1997 -- initial release
 * Version 1.01 December 10, 1997
 *              fixed bug in octal def (0..7 not 0..8)
 * Version 1.10 August 1998 (parrt)
 *              added tree construction
 *              fixed definition of WS,comments for mac,pc,unix newlines
 *              added unary plus
 * Version 1.11 (Nov 20, 1998)
 *              Added "shutup" option to turn off last ambig warning.
 *              Fixed inner class def to allow named class defs as statements
 *              synchronized requires compound not simple statement
 *              add [] after builtInType DOT class in primaryExpression
 *              "const" is reserved but not valid..removed from modifiers
 * Version 1.12 (Feb 2, 1999)
 *              Changed LITERAL_xxx to xxx in tree grammar.
 *              Updated java.g to use tokens {...} now for 2.6.0 (new feature).
 *
 * Version 1.13 (Apr 23, 1999)
 *              Didn't have (stat)? for else clause in tree parser.
 *              Didn't gen ASTs for interface extends.  Updated tree parser too.
 *              Updated to 2.6.0.
 * Version 1.14 (Jun 20, 1999)
 *              Allowed final/abstract on local classes.
 *              Removed local interfaces from methods
 *              Put instanceof precedence where it belongs...in relationalExpr
 *                      It also had expr not type as arg; fixed it.
 *              Missing ! on SEMI in classBlock
 *              fixed: (expr) + "string" was parsed incorrectly (+ as unary plus).
 *              fixed: didn't like Object[].class in parser or tree parser
 * Version 1.15 (Jun 26, 1999)
 *              Screwed up rule with instanceof in it. :(  Fixed.
 *              Tree parser didn't like (expr).something; fixed.
 *              Allowed multiple inheritance in tree grammar. oops.
 * Version 1.16 (August 22, 1999)
 *              Extending an interface built a wacky tree: had extra EXTENDS.
 *              Tree grammar didn't allow multiple superinterfaces.
 *              Tree grammar didn't allow empty var initializer: {}
 * Version 1.17 (October 12, 1999)
 *              ESC lexer rule allowed 399 max not 377 max.
 *              java.tree.g didn't handle the expression of synchronized
 *              statements.
 * Version 1.18 (August 12, 2001)
 *              Terence updated to Java 2 Version 1.3 by
 *              observing/combining work of Allan Jacobs and Steve
 *              Messick.  Handles 1.3 src.  Summary:
 *              o  primary didn't include boolean.class kind of thing
 *              o  constructor calls parsed explicitly now:
 *                 see explicitConstructorInvocation
 *              o  add strictfp modifier
 *              o  missing objBlock after new expression in tree grammar
 *              o  merged local class definition alternatives, moved after declaration
 *              o  fixed problem with ClassName.super.field
 *              o  reordered some alternatives to make things more efficient
 *              o  long and double constants were not differentiated from int/float
 *              o  whitespace rule was inefficient: matched only one char
 *              o  add an examples directory with some nasty 1.3 cases
 *              o  made Main.java use buffered IO and a Reader for Unicode support
 *              o  supports UNICODE?
 *                 Using Unicode charVocabulay makes code file big, but only
 *                 in the bitsets at the end. I need to make ANTLR generate
 *                 unicode bitsets more efficiently.
 * Version 1.19 (April 25, 2002)
 *              Terence added in nice fixes by John Pybus concerning floating
 *              constants and problems with super() calls.  John did a nice
 *              reorg of the primary/postfix expression stuff to read better
 *              and makes f.g.super() parse properly (it was METHOD_CALL not
 *              a SUPER_CTOR_CALL).  Also:
 *
 *              o  "finally" clause was a root...made it a child of "try"
 *              o  Added stuff for asserts too for Java 1.4, but *commented out*
 *                 as it is not backward compatible.
 *
 * Version 1.20 (October 27, 2002)
 *
 *        Terence ended up reorging John Pybus' stuff to
 *        remove some nondeterminisms and some syntactic predicates.
 *        Note that the grammar is stricter now; e.g., this(...) must
 *      be the first statement.
 *
 *        Trinary ?: operator wasn't working as array name:
 *                (isBig ? bigDigits : digits)[i];
 *
 *        Checked parser/tree parser on source for
 *                Resin-2.0.5, jive-2.1.1, jdk 1.3.1, Lucene, antlr 2.7.2a4,
 *              and the 110k-line jGuru server source.
 *
 * Version 1.21 (October 17, 2003)
 *  Fixed lots of problems including:
 *  Ray Waldin: add typeDefinition to interfaceBlock in java.tree.g
 *  He found a problem/fix with floating point that start with 0
 *  Ray also fixed problem that (int.class) was not recognized.
 *  Thorsten van Ellen noticed that \n are allowed incorrectly in strings.
 *  TJP fixed CHAR_LITERAL analogously.
 *
 * Version 1.21.2 (March, 2003)
 *        Changes by Matt Quail to support generics (as per JDK1.5/JSR14)
 *        Notes:
 *        o We only allow the "extends" keyword and not the "implements"
 *              keyword, since thats what JSR14 seems to imply.
 *        o Thanks to Monty Zukowski for his help on the antlr-interest
 *              mail list.
 *        o Thanks to Alan Eliasen for testing the grammar over his
 *              Fink source base
 *
 * Version 1.22 (July, 2004)
 *        Changes by Michael Studman to support Java 1.5 language extensions
 *        Notes:
 *        o Added support for annotations types
 *        o Finished off Matt Quail's generics enhancements to support bound type arguments
 *        o Added support for new for statement syntax
 *        o Added support for static import syntax
 *        o Added support for enum types
 *        o Tested against JDK 1.5 source base and source base of jdigraph project
 *        o Thanks to Matt Quail for doing the hard part by doing most of the generics work
 *
 * Version 1.22.1 (July 28, 2004)
 *        Bug/omission fixes for Java 1.5 language support
 *        o Fixed tree structure bug with classOrInterface - thanks to Pieter Vangorpto for
 *              spotting this
 *        o Fixed bug where incorrect handling of SR and BSR tokens would cause type
 *              parameters to be recognised as type arguments.
 *        o Enabled type parameters on constructors, annotations on enum constants
 *              and package definitions
 *        o Fixed problems when parsing if ((char.class.equals(c))) {} - solution by Matt Quail at Cenqua
 *
 * Version 1.22.2 (July 28, 2004)
 *        Slight refactoring of Java 1.5 language support
 *        o Refactored for/"foreach" productions so that original literal "for" literal
 *          is still used but the for sub-clauses vary by token type
 *        o Fixed bug where type parameter was not included in generic constructor's branch of AST
 *
 * Version 1.22.3 (August 26, 2004)
 *        Bug fixes as identified by Michael Stahl; clean up of tabs/spaces
 *        and other refactorings
 *        o Fixed typeParameters omission in identPrimary and newStatement
 *        o Replaced GT reconcilliation code with simple semantic predicate
 *        o Adapted enum/assert keyword checking support from Michael Stahl's java15 grammar
 *        o Refactored typeDefinition production and field productions to reduce duplication
 *
 * Version 1.22.4 (October 21, 2004)
 *    Small bux fixes
 *    o Added typeArguments to explicitConstructorInvocation, e.g. new <String>MyParameterised()
 *    o Added typeArguments to postfixExpression productions for anonymous inner class super
 *      constructor invocation, e.g. new Outer().<String>super()
 *    o Fixed bug in array declarations identified by Geoff Roy
 *
 * Version 1.22.4.g.1
 *    o I have taken java.g for Java1.5 from Michael Studman (1.22.4)
 *      and have applied the groovy.diff from java.g (1.22) by John Rose
 *      back onto the new root (1.22.4) - Jeremy Rayner (Jan 2005)
 *    o for a map of the task see...
 *      http://groovy.javanicus.com/java-g.png
 *
 * This grammar is in the PUBLIC DOMAIN
 */
public class GroovyRecognizer extends antlr.LLkParser       implements GroovyTokenTypes
 {

        /** This factory is the correct way to wire together a Groovy parser and lexer. */
    public static GroovyRecognizer make(GroovyLexer lexer) {
        GroovyRecognizer parser = new GroovyRecognizer(lexer.plumb());
        // TODO: set up a common error-handling control block, to avoid excessive tangle between these guys
        parser.lexer = lexer;
        lexer.parser = parser;
        parser.setASTNodeClass("org.codehaus.groovy.antlr.GroovySourceAST");
        parser.warningList = new ArrayList();
        return parser;
    }
    // Create a scanner that reads from the input stream passed to us...
    public static GroovyRecognizer make(InputStream in) { return make(new GroovyLexer(in)); }
    public static GroovyRecognizer make(Reader in) { return make(new GroovyLexer(in)); }
    public static GroovyRecognizer make(InputBuffer in) { return make(new GroovyLexer(in)); }
    public static GroovyRecognizer make(LexerSharedInputState in) { return make(new GroovyLexer(in)); }

    private static GroovySourceAST dummyVariableToforceClassLoaderToFindASTClass = new GroovySourceAST();

    List warningList;
    public List getWarningList() { return warningList; }

    boolean compatibilityMode = true;  // for now
    public boolean isCompatibilityMode() { return compatibilityMode; }
    public void setCompatibilityMode(boolean z) { compatibilityMode = z; }

    GroovyLexer lexer;
    public GroovyLexer getLexer() { return lexer; }
    public void setFilename(String f) { super.setFilename(f); lexer.setFilename(f); }

    // stuff to adjust ANTLR's tracing machinery
    public static boolean tracing = false;  // only effective if antlr.Tool is run with -traceParser
    public void traceIn(String rname) throws TokenStreamException {
        if (!GroovyRecognizer.tracing)  return;
        super.traceIn(rname);
    }
    public void traceOut(String rname) throws TokenStreamException {
        if (!GroovyRecognizer.tracing)  return;
        if (returnAST != null)  rname += returnAST.toStringList();
        super.traceOut(rname);
    }

    // Error handling.  This is a funnel through which parser errors go, when the parser can suggest a solution.
    public void requireFailed(String problem, String solution) throws SemanticException {
        // TODO: Needs more work.
        Token lt = null;
        try { lt = LT(1); }
        catch (TokenStreamException ee) { }
        if (lt == null)  lt = Token.badToken;
        throw new SemanticException(problem + ";\n   solution: " + solution,
                                    getFilename(), lt.getLine(), lt.getColumn());
    }

    public void addWarning(String warning, String solution) {
        Token lt = null;
        try { lt = LT(1); }
        catch (TokenStreamException ee) { }
        if (lt == null)  lt = Token.badToken;

        Map row = new HashMap();
        row.put("warning" ,warning);
        row.put("solution",solution);
        row.put("filename",getFilename());
        row.put("line"    ,new Integer(lt.getLine()));
        row.put("column"  ,new Integer(lt.getColumn()));
        // System.out.println(row);
        warningList.add(row);
    }

    // Convenience method for checking of expected error syndromes.
    private void require(boolean z, String problem, String solution) throws SemanticException {
        if (!z)  requireFailed(problem, solution);
    }


    // Query a name token to see if it begins with a capital letter.
    // This is used to tell the difference (w/o symbol table access) between {String x} and {println x}.
    private boolean isUpperCase(Token x) {
        if (x == null || x.getType() != IDENT)  return false;  // cannot happen?
        String xtext = x.getText();
        return (xtext.length() > 0 && Character.isUpperCase(xtext.charAt(0)));
    }

    private AST currentClass = null;  // current enclosing class (for constructor recognition)
    // Query a name token to see if it is identical with the current class name.
    // This is used to distinguish constructors from other methods.
    private boolean isConstructorIdent(Token x) {
        if (currentClass == null)  return false;
        if (currentClass.getType() != IDENT)  return false;  // cannot happen?
        String cname = currentClass.getText();

        if (x == null || x.getType() != IDENT)  return false;  // cannot happen?
        return cname.equals(x.getText());
    }

    // Scratch variable for last 'sep' token.
    // Written by the 'sep' rule, read only by immediate callers of 'sep'.
    // (Not entirely clean, but better than a million xx=sep occurrences.)
    private int sepToken = EOF;

    // Scratch variable for last argument list; tells whether there was a label.
    // Written by 'argList' rule, read only by immediate callers of 'argList'.
    private boolean argListHasLabels = false;

    // Scratch variable, holds most recently completed pathExpression.
    // Read only by immediate callers of 'pathExpression' and 'expression'.
    private AST lastPathExpression = null;

    // Inherited attribute pushed into most expression rules.
    // If not zero, it means that the left context of the expression
    // being parsed is a statement boundary or an initializer sign '='.
    // Only such expressions are allowed to reach across newlines
    // to pull in an LCURLY and appended block.
    private final int LC_STMT = 1, LC_INIT = 2;

    /**
     * Counts the number of LT seen in the typeArguments production.
     * It is used in semantic predicates to ensure we have seen
     * enough closing '>' characters; which actually may have been
     * either GT, SR or BSR tokens.
     */
    private int ltCounter = 0;

    /* This symbol is used to work around a known ANTLR limitation.
     * In a loop with syntactic predicate, ANTLR needs help knowing
     * that the loop exit is a second alternative.
     * Example usage:  ( (LCURLY)=> block | {ANTLR_LOOP_EXIT}? )*
     * Probably should be an ANTLR RFE.
     */
    ////// Original comment in Java grammar:
    // Unfortunately a syntactic predicate can only select one of
    // multiple alternatives on the same level, not break out of
    // an enclosing loop, which is why this ugly hack (a fake
    // empty alternative with always-false semantic predicate)
    // is necessary.
    private static final boolean ANTLR_LOOP_EXIT = false;

protected GroovyRecognizer(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public GroovyRecognizer(TokenBuffer tokenBuf) {
  this(tokenBuf,3);
}

protected GroovyRecognizer(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public GroovyRecognizer(TokenStream lexer) {
  this(lexer,3);
}

public GroovyRecognizer(ParserSharedInputState state) {
  super(state,3);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void compilationUnit() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST compilationUnit_AST = null;

		{
		switch ( LA(1)) {
		case SH_COMMENT:
		{
			AST tmp1_AST = null;
			if (inputState.guessing==0) {
				tmp1_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp1_AST);
			}
			match(SH_COMMENT);
			break;
		}
		case EOF:
		case FINAL:
		case ABSTRACT:
		case STRICTFP:
		case LITERAL_package:
		case LITERAL_import:
		case LITERAL_static:
		case LITERAL_def:
		case AT:
		case IDENT:
		case LBRACK:
		case LPAREN:
		case LITERAL_class:
		case LITERAL_interface:
		case LITERAL_enum:
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
		case STAR:
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case BAND:
		case LCURLY:
		case SEMI:
		case NLS:
		case LITERAL_this:
		case STRING_LITERAL:
		case LITERAL_if:
		case LITERAL_while:
		case LITERAL_with:
		case LITERAL_switch:
		case LITERAL_for:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		case PLUS:
		case MINUS:
		case LITERAL_try:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case DOLLAR:
		case STRING_CTOR_START:
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
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		nls();
		{
		boolean synPredMatched5 = false;
		if (((LA(1)==LITERAL_package||LA(1)==AT) && (LA(2)==IDENT) && (_tokenSet_0.member(LA(3))))) {
			int _m5 = mark();
			synPredMatched5 = true;
			inputState.guessing++;
			try {
				{
				annotationsOpt();
				match(LITERAL_package);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched5 = false;
			}
			rewind(_m5);
			inputState.guessing--;
		}
		if ( synPredMatched5 ) {
			packageDefinition();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((_tokenSet_1.member(LA(1))) && (_tokenSet_2.member(LA(2))) && (_tokenSet_3.member(LA(3)))) {
			{
			switch ( LA(1)) {
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case LITERAL_import:
			case LITERAL_static:
			case LITERAL_def:
			case AT:
			case IDENT:
			case LBRACK:
			case LPAREN:
			case LITERAL_class:
			case LITERAL_interface:
			case LITERAL_enum:
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
			case STAR:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_threadsafe:
			case LITERAL_synchronized:
			case LITERAL_volatile:
			case BAND:
			case LCURLY:
			case LITERAL_this:
			case STRING_LITERAL:
			case LITERAL_if:
			case LITERAL_while:
			case LITERAL_with:
			case LITERAL_switch:
			case LITERAL_for:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_continue:
			case LITERAL_throw:
			case LITERAL_assert:
			case PLUS:
			case MINUS:
			case LITERAL_try:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case DOLLAR:
			case STRING_CTOR_START:
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
				statement(EOF);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				break;
			}
			case EOF:
			case SEMI:
			case NLS:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		{
		_loop9:
		do {
			if ((LA(1)==SEMI||LA(1)==NLS)) {
				sep();
				{
				switch ( LA(1)) {
				case FINAL:
				case ABSTRACT:
				case STRICTFP:
				case LITERAL_import:
				case LITERAL_static:
				case LITERAL_def:
				case AT:
				case IDENT:
				case LBRACK:
				case LPAREN:
				case LITERAL_class:
				case LITERAL_interface:
				case LITERAL_enum:
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
				case STAR:
				case LITERAL_private:
				case LITERAL_public:
				case LITERAL_protected:
				case LITERAL_transient:
				case LITERAL_native:
				case LITERAL_threadsafe:
				case LITERAL_synchronized:
				case LITERAL_volatile:
				case BAND:
				case LCURLY:
				case LITERAL_this:
				case STRING_LITERAL:
				case LITERAL_if:
				case LITERAL_while:
				case LITERAL_with:
				case LITERAL_switch:
				case LITERAL_for:
				case LITERAL_return:
				case LITERAL_break:
				case LITERAL_continue:
				case LITERAL_throw:
				case LITERAL_assert:
				case PLUS:
				case MINUS:
				case LITERAL_try:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case DOLLAR:
				case STRING_CTOR_START:
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
					statement(sepToken);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case EOF:
				case SEMI:
				case NLS:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else {
				break _loop9;
			}

		} while (true);
		}
		match(Token.EOF_TYPE);
		if ( inputState.guessing==0 ) {
			compilationUnit_AST = (AST)currentAST.root;
		}
		returnAST = compilationUnit_AST;
	}

/** Zero or more insignificant newlines, all gobbled up and thrown away. */
	public final void nls() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST nls_AST = null;

		{
		if ((LA(1)==NLS) && (_tokenSet_4.member(LA(2))) && (_tokenSet_5.member(LA(3)))) {
			AST tmp3_AST = null;
			if (inputState.guessing==0) {
				tmp3_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp3_AST);
			}
			match(NLS);
		}
		else if ((_tokenSet_4.member(LA(1))) && (_tokenSet_5.member(LA(2))) && (_tokenSet_5.member(LA(3)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		if ( inputState.guessing==0 ) {
			nls_AST = (AST)currentAST.root;
		}
		returnAST = nls_AST;
	}

	public final void annotationsOpt() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationsOpt_AST = null;

		{
		_loop79:
		do {
			if ((LA(1)==AT)) {
				annotation();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				nls();
			}
			else {
				break _loop79;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			annotationsOpt_AST = (AST)currentAST.root;
			annotationsOpt_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(ANNOTATIONS,"ANNOTATIONS")).add(annotationsOpt_AST));
			currentAST.root = annotationsOpt_AST;
			currentAST.child = annotationsOpt_AST!=null &&annotationsOpt_AST.getFirstChild()!=null ?
				annotationsOpt_AST.getFirstChild() : annotationsOpt_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			annotationsOpt_AST = (AST)currentAST.root;
		}
		returnAST = annotationsOpt_AST;
	}

	public final void packageDefinition() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST packageDefinition_AST = null;

		annotationsOpt();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		match(LITERAL_package);
		if ( inputState.guessing==0 ) {
			astFactory.makeASTRoot(currentAST, astFactory.create(PACKAGE_DEF, "package"));
		}
		identifier();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			packageDefinition_AST = (AST)currentAST.root;
		}
		returnAST = packageDefinition_AST;
	}

/** A statement is an element of a block.
 *  Typical statements are declarations (which are scoped to the block)
 *  and expressions.
 */
	public final void statement(
		int prevToken
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST statement_AST = null;
		AST pfx_AST = null;
		AST m_AST = null;

		switch ( LA(1)) {
		case LITERAL_if:
		{
			AST tmp5_AST = null;
			if (inputState.guessing==0) {
				tmp5_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp5_AST);
			}
			match(LITERAL_if);
			match(LPAREN);
			strictContextExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			match(RPAREN);
			nlsWarn();
			compatibleBodyStatement();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			boolean synPredMatched263 = false;
			if (((_tokenSet_6.member(LA(1))) && (_tokenSet_7.member(LA(2))) && (_tokenSet_8.member(LA(3))))) {
				int _m263 = mark();
				synPredMatched263 = true;
				inputState.guessing++;
				try {
					{
					{
					switch ( LA(1)) {
					case SEMI:
					case NLS:
					{
						sep();
						break;
					}
					case LITERAL_else:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					match(LITERAL_else);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched263 = false;
				}
				rewind(_m263);
				inputState.guessing--;
			}
			if ( synPredMatched263 ) {
				{
				switch ( LA(1)) {
				case SEMI:
				case NLS:
				{
					sep();
					break;
				}
				case LITERAL_else:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(LITERAL_else);
				nlsWarn();
				compatibleBodyStatement();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else if ((_tokenSet_9.member(LA(1))) && (_tokenSet_10.member(LA(2))) && (_tokenSet_11.member(LA(3)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}

			}
			if ( inputState.guessing==0 ) {
				statement_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_for:
		{
			forStatement();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				statement_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_while:
		{
			AST tmp9_AST = null;
			if (inputState.guessing==0) {
				tmp9_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp9_AST);
			}
			match(LITERAL_while);
			match(LPAREN);
			strictContextExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			match(RPAREN);
			nlsWarn();
			compatibleBodyStatement();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				statement_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_with:
		{
			AST tmp12_AST = null;
			if (inputState.guessing==0) {
				tmp12_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp12_AST);
			}
			match(LITERAL_with);
			match(LPAREN);
			strictContextExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			match(RPAREN);
			nlsWarn();
			compoundStatement();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				statement_AST = (AST)currentAST.root;
			}
			break;
		}
		case STAR:
		{
			match(STAR);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(SPREAD_ARG, "*"));
			}
			nls();
			expressionStatement(EOF);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				statement_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_import:
		{
			importStatement();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				statement_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_switch:
		{
			AST tmp16_AST = null;
			if (inputState.guessing==0) {
				tmp16_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp16_AST);
			}
			match(LITERAL_switch);
			match(LPAREN);
			strictContextExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			match(RPAREN);
			nlsWarn();
			match(LCURLY);
			nls();
			{
			_loop266:
			do {
				if ((LA(1)==LITERAL_default||LA(1)==LITERAL_case)) {
					casesGroup();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
				}
				else {
					break _loop266;
				}

			} while (true);
			}
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				statement_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_try:
		{
			tryBlock();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				statement_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		{
			branchStatement();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				statement_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
			boolean synPredMatched254 = false;
			if (((_tokenSet_12.member(LA(1))) && (_tokenSet_13.member(LA(2))) && (_tokenSet_14.member(LA(3))))) {
				int _m254 = mark();
				synPredMatched254 = true;
				inputState.guessing++;
				try {
					{
					declarationStart();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched254 = false;
				}
				rewind(_m254);
				inputState.guessing--;
			}
			if ( synPredMatched254 ) {
				declaration();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				if ( inputState.guessing==0 ) {
					statement_AST = (AST)currentAST.root;
				}
			}
			else {
				boolean synPredMatched256 = false;
				if (((LA(1)==IDENT) && (LA(2)==COLON) && (_tokenSet_15.member(LA(3))))) {
					int _m256 = mark();
					synPredMatched256 = true;
					inputState.guessing++;
					try {
						{
						match(IDENT);
						match(COLON);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched256 = false;
					}
					rewind(_m256);
					inputState.guessing--;
				}
				if ( synPredMatched256 ) {
					statementLabelPrefix();
					if (inputState.guessing==0) {
						pfx_AST = (AST)returnAST;
					}
					if ( inputState.guessing==0 ) {
						statement_AST = (AST)currentAST.root;
						statement_AST = pfx_AST;
						currentAST.root = statement_AST;
						currentAST.child = statement_AST!=null &&statement_AST.getFirstChild()!=null ?
							statement_AST.getFirstChild() : statement_AST;
						currentAST.advanceChildToEnd();
					}
					{
					boolean synPredMatched259 = false;
					if (((LA(1)==LCURLY) && (_tokenSet_16.member(LA(2))) && (_tokenSet_17.member(LA(3))))) {
						int _m259 = mark();
						synPredMatched259 = true;
						inputState.guessing++;
						try {
							{
							match(LCURLY);
							}
						}
						catch (RecognitionException pe) {
							synPredMatched259 = false;
						}
						rewind(_m259);
						inputState.guessing--;
					}
					if ( synPredMatched259 ) {
						openOrClosedBlock();
						if (inputState.guessing==0) {
							astFactory.addASTChild(currentAST, returnAST);
						}
					}
					else if ((_tokenSet_15.member(LA(1))) && (_tokenSet_8.member(LA(2))) && (_tokenSet_18.member(LA(3)))) {
						statement(COLON);
						if (inputState.guessing==0) {
							astFactory.addASTChild(currentAST, returnAST);
						}
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

					}
					if ( inputState.guessing==0 ) {
						statement_AST = (AST)currentAST.root;
					}
				}
				else if ((_tokenSet_19.member(LA(1))) && (_tokenSet_8.member(LA(2))) && (_tokenSet_20.member(LA(3)))) {
					expressionStatement(prevToken);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					if ( inputState.guessing==0 ) {
						statement_AST = (AST)currentAST.root;
					}
				}
				else if ((_tokenSet_21.member(LA(1))) && (_tokenSet_22.member(LA(2))) && (_tokenSet_23.member(LA(3)))) {
					modifiersOpt();
					if (inputState.guessing==0) {
						m_AST = (AST)returnAST;
					}
					typeDefinitionInternal(m_AST);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					if ( inputState.guessing==0 ) {
						statement_AST = (AST)currentAST.root;
					}
				}
				else if ((LA(1)==LITERAL_synchronized) && (LA(2)==LPAREN)) {
					AST tmp21_AST = null;
					if (inputState.guessing==0) {
						tmp21_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp21_AST);
					}
					match(LITERAL_synchronized);
					match(LPAREN);
					strictContextExpression();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					match(RPAREN);
					nlsWarn();
					compoundStatement();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					if ( inputState.guessing==0 ) {
						statement_AST = (AST)currentAST.root;
					}
				}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}}
			returnAST = statement_AST;
		}

/** A statement separator is either a semicolon or a significant newline.
 *  Any number of additional (insignificant) newlines may accompany it.
 */
	public final void sep() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST sep_AST = null;

		switch ( LA(1)) {
		case SEMI:
		{
			match(SEMI);
			{
			_loop480:
			do {
				if ((LA(1)==NLS) && (_tokenSet_24.member(LA(2))) && (_tokenSet_20.member(LA(3)))) {
					match(NLS);
				}
				else {
					break _loop480;
				}

			} while (true);
			}
			if ( inputState.guessing==0 ) {
				sepToken = SEMI;
			}
			break;
		}
		case NLS:
		{
			match(NLS);
			if ( inputState.guessing==0 ) {
				sepToken = NLS;
			}
			{
			_loop484:
			do {
				if ((LA(1)==SEMI) && (_tokenSet_24.member(LA(2))) && (_tokenSet_20.member(LA(3)))) {
					match(SEMI);
					{
					_loop483:
					do {
						if ((LA(1)==NLS) && (_tokenSet_24.member(LA(2))) && (_tokenSet_20.member(LA(3)))) {
							match(NLS);
						}
						else {
							break _loop483;
						}

					} while (true);
					}
					if ( inputState.guessing==0 ) {
						sepToken = SEMI;
					}
				}
				else {
					break _loop484;
				}

			} while (true);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = sep_AST;
	}

/** A Groovy script or simple expression.  Can be anything legal inside {...}. */
	public final void snippetUnit() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST snippetUnit_AST = null;

		nls();
		blockBody(EOF);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			snippetUnit_AST = (AST)currentAST.root;
		}
		returnAST = snippetUnit_AST;
	}

/** A block body is a parade of zero or more statements or expressions. */
	public final void blockBody(
		int prevToken
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST blockBody_AST = null;

		{
		switch ( LA(1)) {
		case FINAL:
		case ABSTRACT:
		case STRICTFP:
		case LITERAL_import:
		case LITERAL_static:
		case LITERAL_def:
		case AT:
		case IDENT:
		case LBRACK:
		case LPAREN:
		case LITERAL_class:
		case LITERAL_interface:
		case LITERAL_enum:
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
		case STAR:
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case BAND:
		case LCURLY:
		case LITERAL_this:
		case STRING_LITERAL:
		case LITERAL_if:
		case LITERAL_while:
		case LITERAL_with:
		case LITERAL_switch:
		case LITERAL_for:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		case PLUS:
		case MINUS:
		case LITERAL_try:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case DOLLAR:
		case STRING_CTOR_START:
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
			statement(prevToken);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case EOF:
		case RCURLY:
		case SEMI:
		case NLS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		_loop248:
		do {
			if ((LA(1)==SEMI||LA(1)==NLS)) {
				sep();
				{
				switch ( LA(1)) {
				case FINAL:
				case ABSTRACT:
				case STRICTFP:
				case LITERAL_import:
				case LITERAL_static:
				case LITERAL_def:
				case AT:
				case IDENT:
				case LBRACK:
				case LPAREN:
				case LITERAL_class:
				case LITERAL_interface:
				case LITERAL_enum:
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
				case STAR:
				case LITERAL_private:
				case LITERAL_public:
				case LITERAL_protected:
				case LITERAL_transient:
				case LITERAL_native:
				case LITERAL_threadsafe:
				case LITERAL_synchronized:
				case LITERAL_volatile:
				case BAND:
				case LCURLY:
				case LITERAL_this:
				case STRING_LITERAL:
				case LITERAL_if:
				case LITERAL_while:
				case LITERAL_with:
				case LITERAL_switch:
				case LITERAL_for:
				case LITERAL_return:
				case LITERAL_break:
				case LITERAL_continue:
				case LITERAL_throw:
				case LITERAL_assert:
				case PLUS:
				case MINUS:
				case LITERAL_try:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case DOLLAR:
				case STRING_CTOR_START:
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
					statement(sepToken);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case EOF:
				case RCURLY:
				case SEMI:
				case NLS:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else {
				break _loop248;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			blockBody_AST = (AST)currentAST.root;
		}
		returnAST = blockBody_AST;
	}

	public final void identifier() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST identifier_AST = null;

		AST tmp29_AST = null;
		if (inputState.guessing==0) {
			tmp29_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp29_AST);
		}
		match(IDENT);
		{
		_loop62:
		do {
			if ((LA(1)==DOT)) {
				AST tmp30_AST = null;
				if (inputState.guessing==0) {
					tmp30_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp30_AST);
				}
				match(DOT);
				nls();
				AST tmp31_AST = null;
				if (inputState.guessing==0) {
					tmp31_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp31_AST);
				}
				match(IDENT);
			}
			else {
				break _loop62;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			identifier_AST = (AST)currentAST.root;
		}
		returnAST = identifier_AST;
	}

	public final void importStatement() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST importStatement_AST = null;
		boolean isStatic = false;

		match(LITERAL_import);
		if ( inputState.guessing==0 ) {
			astFactory.makeASTRoot(currentAST, astFactory.create(IMPORT, "import"));
		}
		{
		switch ( LA(1)) {
		case LITERAL_static:
		{
			match(LITERAL_static);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(STATIC_IMPORT, "import"));
			}
			break;
		}
		case IDENT:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		identifierStar();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			importStatement_AST = (AST)currentAST.root;
		}
		returnAST = importStatement_AST;
	}

	public final void identifierStar() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST identifierStar_AST = null;

		AST tmp34_AST = null;
		if (inputState.guessing==0) {
			tmp34_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp34_AST);
		}
		match(IDENT);
		{
		_loop65:
		do {
			if ((LA(1)==DOT) && (LA(2)==IDENT||LA(2)==NLS) && (_tokenSet_25.member(LA(3)))) {
				AST tmp35_AST = null;
				if (inputState.guessing==0) {
					tmp35_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp35_AST);
				}
				match(DOT);
				nls();
				AST tmp36_AST = null;
				if (inputState.guessing==0) {
					tmp36_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp36_AST);
				}
				match(IDENT);
			}
			else {
				break _loop65;
			}

		} while (true);
		}
		{
		switch ( LA(1)) {
		case DOT:
		{
			AST tmp37_AST = null;
			if (inputState.guessing==0) {
				tmp37_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp37_AST);
			}
			match(DOT);
			nls();
			AST tmp38_AST = null;
			if (inputState.guessing==0) {
				tmp38_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp38_AST);
			}
			match(STAR);
			break;
		}
		case LITERAL_as:
		{
			AST tmp39_AST = null;
			if (inputState.guessing==0) {
				tmp39_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp39_AST);
			}
			match(LITERAL_as);
			nls();
			AST tmp40_AST = null;
			if (inputState.guessing==0) {
				tmp40_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp40_AST);
			}
			match(IDENT);
			break;
		}
		case EOF:
		case RCURLY:
		case SEMI:
		case NLS:
		case LITERAL_default:
		case LITERAL_else:
		case LITERAL_case:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			identifierStar_AST = (AST)currentAST.root;
		}
		returnAST = identifierStar_AST;
	}

	protected final void typeDefinitionInternal(
		AST mods
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeDefinitionInternal_AST = null;
		AST cd_AST = null;
		AST id_AST = null;
		AST ed_AST = null;
		AST ad_AST = null;

		switch ( LA(1)) {
		case LITERAL_class:
		{
			classDefinition(mods);
			if (inputState.guessing==0) {
				cd_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				typeDefinitionInternal_AST = (AST)currentAST.root;
				typeDefinitionInternal_AST = cd_AST;
				currentAST.root = typeDefinitionInternal_AST;
				currentAST.child = typeDefinitionInternal_AST!=null &&typeDefinitionInternal_AST.getFirstChild()!=null ?
					typeDefinitionInternal_AST.getFirstChild() : typeDefinitionInternal_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				typeDefinitionInternal_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_interface:
		{
			interfaceDefinition(mods);
			if (inputState.guessing==0) {
				id_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				typeDefinitionInternal_AST = (AST)currentAST.root;
				typeDefinitionInternal_AST = id_AST;
				currentAST.root = typeDefinitionInternal_AST;
				currentAST.child = typeDefinitionInternal_AST!=null &&typeDefinitionInternal_AST.getFirstChild()!=null ?
					typeDefinitionInternal_AST.getFirstChild() : typeDefinitionInternal_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				typeDefinitionInternal_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_enum:
		{
			enumDefinition(mods);
			if (inputState.guessing==0) {
				ed_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				typeDefinitionInternal_AST = (AST)currentAST.root;
				typeDefinitionInternal_AST = ed_AST;
				currentAST.root = typeDefinitionInternal_AST;
				currentAST.child = typeDefinitionInternal_AST!=null &&typeDefinitionInternal_AST.getFirstChild()!=null ?
					typeDefinitionInternal_AST.getFirstChild() : typeDefinitionInternal_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				typeDefinitionInternal_AST = (AST)currentAST.root;
			}
			break;
		}
		case AT:
		{
			annotationDefinition(mods);
			if (inputState.guessing==0) {
				ad_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				typeDefinitionInternal_AST = (AST)currentAST.root;
				typeDefinitionInternal_AST = ad_AST;
				currentAST.root = typeDefinitionInternal_AST;
				currentAST.child = typeDefinitionInternal_AST!=null &&typeDefinitionInternal_AST.getFirstChild()!=null ?
					typeDefinitionInternal_AST.getFirstChild() : typeDefinitionInternal_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				typeDefinitionInternal_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = typeDefinitionInternal_AST;
	}

	public final void classDefinition(
		AST modifiers
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classDefinition_AST = null;
		AST tp_AST = null;
		AST sc_AST = null;
		AST ic_AST = null;
		AST cb_AST = null;
		AST prevCurrentClass = currentClass;

		AST tmp41_AST = null;
		if (inputState.guessing==0) {
			tmp41_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp41_AST);
		}
		match(LITERAL_class);
		AST tmp42_AST = null;
		if (inputState.guessing==0) {
			tmp42_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp42_AST);
		}
		match(IDENT);
		nls();
		if ( inputState.guessing==0 ) {
			currentClass = tmp42_AST;
		}
		{
		switch ( LA(1)) {
		case LT:
		{
			typeParameters();
			if (inputState.guessing==0) {
				tp_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case LITERAL_extends:
		case LCURLY:
		case LITERAL_implements:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		superClassClause();
		if (inputState.guessing==0) {
			sc_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		implementsClause();
		if (inputState.guessing==0) {
			ic_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		classBlock();
		if (inputState.guessing==0) {
			cb_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			classDefinition_AST = (AST)currentAST.root;
			classDefinition_AST = (AST)astFactory.make( (new ASTArray(7)).add(astFactory.create(CLASS_DEF,"CLASS_DEF")).add(modifiers).add(tmp42_AST).add(tp_AST).add(sc_AST).add(ic_AST).add(cb_AST));
			currentAST.root = classDefinition_AST;
			currentAST.child = classDefinition_AST!=null &&classDefinition_AST.getFirstChild()!=null ?
				classDefinition_AST.getFirstChild() : classDefinition_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			currentClass = prevCurrentClass;
		}
		if ( inputState.guessing==0 ) {
			classDefinition_AST = (AST)currentAST.root;
		}
		returnAST = classDefinition_AST;
	}

	public final void interfaceDefinition(
		AST modifiers
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST interfaceDefinition_AST = null;
		AST tp_AST = null;
		AST ie_AST = null;
		AST ib_AST = null;

		AST tmp43_AST = null;
		if (inputState.guessing==0) {
			tmp43_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp43_AST);
		}
		match(LITERAL_interface);
		AST tmp44_AST = null;
		if (inputState.guessing==0) {
			tmp44_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp44_AST);
		}
		match(IDENT);
		nls();
		{
		switch ( LA(1)) {
		case LT:
		{
			typeParameters();
			if (inputState.guessing==0) {
				tp_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case LITERAL_extends:
		case LCURLY:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		interfaceExtends();
		if (inputState.guessing==0) {
			ie_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		interfaceBlock();
		if (inputState.guessing==0) {
			ib_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			interfaceDefinition_AST = (AST)currentAST.root;
			interfaceDefinition_AST = (AST)astFactory.make( (new ASTArray(6)).add(astFactory.create(INTERFACE_DEF,"INTERFACE_DEF")).add(modifiers).add(tmp44_AST).add(tp_AST).add(ie_AST).add(ib_AST));
			currentAST.root = interfaceDefinition_AST;
			currentAST.child = interfaceDefinition_AST!=null &&interfaceDefinition_AST.getFirstChild()!=null ?
				interfaceDefinition_AST.getFirstChild() : interfaceDefinition_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			interfaceDefinition_AST = (AST)currentAST.root;
		}
		returnAST = interfaceDefinition_AST;
	}

	public final void enumDefinition(
		AST modifiers
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumDefinition_AST = null;
		AST ic_AST = null;
		AST eb_AST = null;

		AST tmp45_AST = null;
		if (inputState.guessing==0) {
			tmp45_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp45_AST);
		}
		match(LITERAL_enum);
		AST tmp46_AST = null;
		if (inputState.guessing==0) {
			tmp46_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp46_AST);
		}
		match(IDENT);
		implementsClause();
		if (inputState.guessing==0) {
			ic_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		enumBlock();
		if (inputState.guessing==0) {
			eb_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			enumDefinition_AST = (AST)currentAST.root;
			enumDefinition_AST = (AST)astFactory.make( (new ASTArray(5)).add(astFactory.create(ENUM_DEF,"ENUM_DEF")).add(modifiers).add(tmp46_AST).add(ic_AST).add(eb_AST));
			currentAST.root = enumDefinition_AST;
			currentAST.child = enumDefinition_AST!=null &&enumDefinition_AST.getFirstChild()!=null ?
				enumDefinition_AST.getFirstChild() : enumDefinition_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			enumDefinition_AST = (AST)currentAST.root;
		}
		returnAST = enumDefinition_AST;
	}

	public final void annotationDefinition(
		AST modifiers
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationDefinition_AST = null;
		AST ab_AST = null;

		AST tmp47_AST = null;
		if (inputState.guessing==0) {
			tmp47_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp47_AST);
		}
		match(AT);
		AST tmp48_AST = null;
		if (inputState.guessing==0) {
			tmp48_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp48_AST);
		}
		match(LITERAL_interface);
		AST tmp49_AST = null;
		if (inputState.guessing==0) {
			tmp49_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp49_AST);
		}
		match(IDENT);
		annotationBlock();
		if (inputState.guessing==0) {
			ab_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			annotationDefinition_AST = (AST)currentAST.root;
			annotationDefinition_AST = (AST)astFactory.make( (new ASTArray(4)).add(astFactory.create(ANNOTATION_DEF,"ANNOTATION_DEF")).add(modifiers).add(tmp49_AST).add(ab_AST));
			currentAST.root = annotationDefinition_AST;
			currentAST.child = annotationDefinition_AST!=null &&annotationDefinition_AST.getFirstChild()!=null ?
				annotationDefinition_AST.getFirstChild() : annotationDefinition_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			annotationDefinition_AST = (AST)currentAST.root;
		}
		returnAST = annotationDefinition_AST;
	}

/** A declaration is the creation of a reference or primitive-type variable,
 *  or (if arguments are present) of a method.
 *  Generically, this is called a 'variable' definition, even in the case of a class field or method.
 *  It may start with the modifiers and/or a declaration keyword "def".
 *  It may also start with the modifiers and a capitalized type name.
 *  <p>
 *  AST effect: Create a separate Type/Var tree for each var in the var list.
 *  Must be guarded, as in (declarationStart) => declaration.
 */
	public final void declaration() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST declaration_AST = null;
		AST m_AST = null;
		AST t_AST = null;
		AST v_AST = null;
		AST t2_AST = null;
		AST v2_AST = null;

		switch ( LA(1)) {
		case FINAL:
		case ABSTRACT:
		case STRICTFP:
		case LITERAL_static:
		case LITERAL_def:
		case AT:
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		{
			modifiers();
			if (inputState.guessing==0) {
				m_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			if ((_tokenSet_26.member(LA(1))) && (_tokenSet_27.member(LA(2)))) {
				typeSpec(false);
				if (inputState.guessing==0) {
					t_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else if ((LA(1)==IDENT||LA(1)==STRING_LITERAL) && (_tokenSet_28.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}

			}
			variableDefinitions(m_AST, t_AST);
			if (inputState.guessing==0) {
				v_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				declaration_AST = (AST)currentAST.root;
				declaration_AST = v_AST;
				currentAST.root = declaration_AST;
				currentAST.child = declaration_AST!=null &&declaration_AST.getFirstChild()!=null ?
					declaration_AST.getFirstChild() : declaration_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				declaration_AST = (AST)currentAST.root;
			}
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
			typeSpec(false);
			if (inputState.guessing==0) {
				t2_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			variableDefinitions(null,t2_AST);
			if (inputState.guessing==0) {
				v2_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				declaration_AST = (AST)currentAST.root;
				declaration_AST = v2_AST;
				currentAST.root = declaration_AST;
				currentAST.child = declaration_AST!=null &&declaration_AST.getFirstChild()!=null ?
					declaration_AST.getFirstChild() : declaration_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				declaration_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = declaration_AST;
	}

/** A list of one or more modifier, annotation, or "def". */
	public final void modifiers() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST modifiers_AST = null;

		modifiersInternal();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			modifiers_AST = (AST)currentAST.root;
			modifiers_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(MODIFIERS,"MODIFIERS")).add(modifiers_AST));
			currentAST.root = modifiers_AST;
			currentAST.child = modifiers_AST!=null &&modifiers_AST.getFirstChild()!=null ?
				modifiers_AST.getFirstChild() : modifiers_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			modifiers_AST = (AST)currentAST.root;
		}
		returnAST = modifiers_AST;
	}

	public final void typeSpec(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeSpec_AST = null;

		switch ( LA(1)) {
		case IDENT:
		{
			classTypeSpec(addImagNode);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				typeSpec_AST = (AST)currentAST.root;
			}
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
			builtInTypeSpec(addImagNode);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				typeSpec_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = typeSpec_AST;
	}

/** The tail of a declaration.
  * Either v1, v2, ... (with possible initializers) or else m(args){body}.
  * The two arguments are the modifier list (if any) and the declaration head (if any).
  * The declaration head is the variable type, or (for a method) the return type.
  * If it is missing, then the variable type is taken from its initializer (if there is one).
  * Otherwise, the variable type defaults to 'any'.
  * DECIDE:  Method return types default to the type of the method body, as an expression.
  */
	public final void variableDefinitions(
		AST mods, AST t
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST variableDefinitions_AST = null;
		Token  id = null;
		AST id_AST = null;
		Token  qid = null;
		AST qid_AST = null;
		AST param_AST = null;
		AST tc_AST = null;
		AST mb_AST = null;

		if ((LA(1)==IDENT) && (_tokenSet_29.member(LA(2)))) {
			variableDeclarator(getASTFactory().dupTree(mods),
                           getASTFactory().dupTree(t));
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			_loop188:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					nls();
					variableDeclarator(getASTFactory().dupTree(mods),
                               getASTFactory().dupTree(t));
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
				}
				else {
					break _loop188;
				}

			} while (true);
			}
			if ( inputState.guessing==0 ) {
				variableDefinitions_AST = (AST)currentAST.root;
			}
		}
		else if ((LA(1)==IDENT||LA(1)==STRING_LITERAL) && (LA(2)==LPAREN)) {
			{
			switch ( LA(1)) {
			case IDENT:
			{
				id = LT(1);
				if (inputState.guessing==0) {
					id_AST = astFactory.create(id);
					astFactory.addASTChild(currentAST, id_AST);
				}
				match(IDENT);
				break;
			}
			case STRING_LITERAL:
			{
				qid = LT(1);
				if (inputState.guessing==0) {
					qid_AST = astFactory.create(qid);
					astFactory.addASTChild(currentAST, qid_AST);
				}
				match(STRING_LITERAL);
				if ( inputState.guessing==0 ) {
					qid_AST.setType(IDENT);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(LPAREN);
			parameterDeclarationList();
			if (inputState.guessing==0) {
				param_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			match(RPAREN);
			{
			switch ( LA(1)) {
			case LITERAL_throws:
			{
				throwsClause();
				if (inputState.guessing==0) {
					tc_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
				break;
			}
			case EOF:
			case LCURLY:
			case RCURLY:
			case SEMI:
			case NLS:
			case LITERAL_default:
			case LITERAL_else:
			case LITERAL_case:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			nlsWarn();
			{
			switch ( LA(1)) {
			case LCURLY:
			{
				openBlock();
				if (inputState.guessing==0) {
					mb_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
				break;
			}
			case EOF:
			case RCURLY:
			case SEMI:
			case NLS:
			case LITERAL_default:
			case LITERAL_else:
			case LITERAL_case:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				variableDefinitions_AST = (AST)currentAST.root;
				if (qid_AST != null)  id_AST = qid_AST;
				variableDefinitions_AST =
				(AST)astFactory.make( (new ASTArray(7)).add(astFactory.create(METHOD_DEF,"METHOD_DEF")).add(mods).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(t))).add(id_AST).add(param_AST).add(tc_AST).add(mb_AST));

				currentAST.root = variableDefinitions_AST;
				currentAST.child = variableDefinitions_AST!=null &&variableDefinitions_AST.getFirstChild()!=null ?
					variableDefinitions_AST.getFirstChild() : variableDefinitions_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				variableDefinitions_AST = (AST)currentAST.root;
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		returnAST = variableDefinitions_AST;
	}

/** A declaration with one declarator and no initialization, like a parameterDeclaration.
 *  Used to parse loops like <code>for (int x in y)</code> (up to the <code>in</code> keyword).
 */
	public final void singleDeclarationNoInit() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST singleDeclarationNoInit_AST = null;
		AST m_AST = null;
		AST t_AST = null;
		AST v_AST = null;
		AST t2_AST = null;
		AST v2_AST = null;

		switch ( LA(1)) {
		case FINAL:
		case ABSTRACT:
		case STRICTFP:
		case LITERAL_static:
		case LITERAL_def:
		case AT:
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		{
			modifiers();
			if (inputState.guessing==0) {
				m_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			if ((_tokenSet_26.member(LA(1))) && (_tokenSet_30.member(LA(2)))) {
				typeSpec(false);
				if (inputState.guessing==0) {
					t_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else if ((LA(1)==IDENT) && (_tokenSet_31.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}

			}
			singleVariable(m_AST, t_AST);
			if (inputState.guessing==0) {
				v_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				singleDeclarationNoInit_AST = (AST)currentAST.root;
				singleDeclarationNoInit_AST = v_AST;
				currentAST.root = singleDeclarationNoInit_AST;
				currentAST.child = singleDeclarationNoInit_AST!=null &&singleDeclarationNoInit_AST.getFirstChild()!=null ?
					singleDeclarationNoInit_AST.getFirstChild() : singleDeclarationNoInit_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				singleDeclarationNoInit_AST = (AST)currentAST.root;
			}
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
			typeSpec(false);
			if (inputState.guessing==0) {
				t2_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			singleVariable(null,t2_AST);
			if (inputState.guessing==0) {
				v2_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				singleDeclarationNoInit_AST = (AST)currentAST.root;
				singleDeclarationNoInit_AST = v2_AST;
				currentAST.root = singleDeclarationNoInit_AST;
				currentAST.child = singleDeclarationNoInit_AST!=null &&singleDeclarationNoInit_AST.getFirstChild()!=null ?
					singleDeclarationNoInit_AST.getFirstChild() : singleDeclarationNoInit_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				singleDeclarationNoInit_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = singleDeclarationNoInit_AST;
	}

/** Used in cases where a declaration cannot have commas, or ends with the "in" operator instead of '='. */
	public final void singleVariable(
		AST mods, AST t
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST singleVariable_AST = null;
		AST id_AST = null;

		variableName();
		if (inputState.guessing==0) {
			id_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			singleVariable_AST = (AST)currentAST.root;
			singleVariable_AST = (AST)astFactory.make( (new ASTArray(4)).add(astFactory.create(VARIABLE_DEF,"VARIABLE_DEF")).add(mods).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(t))).add(id_AST));
			currentAST.root = singleVariable_AST;
			currentAST.child = singleVariable_AST!=null &&singleVariable_AST.getFirstChild()!=null ?
				singleVariable_AST.getFirstChild() : singleVariable_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			singleVariable_AST = (AST)currentAST.root;
		}
		returnAST = singleVariable_AST;
	}

/** A declaration with one declarator and optional initialization, like a parameterDeclaration.
 *  Used to parse declarations used for both binding and effect, in places like argument
 *  lists and <code>while</code> statements.
 */
	public final void singleDeclaration() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST singleDeclaration_AST = null;
		AST sd_AST = null;

		singleDeclarationNoInit();
		if (inputState.guessing==0) {
			sd_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			singleDeclaration_AST = (AST)currentAST.root;
			singleDeclaration_AST = sd_AST;
			currentAST.root = singleDeclaration_AST;
			currentAST.child = singleDeclaration_AST!=null &&singleDeclaration_AST.getFirstChild()!=null ?
				singleDeclaration_AST.getFirstChild() : singleDeclaration_AST;
			currentAST.advanceChildToEnd();
		}
		{
		switch ( LA(1)) {
		case ASSIGN:
		{
			varInitializer();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case RBRACK:
		case COMMA:
		case RPAREN:
		case SEMI:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			singleDeclaration_AST = (AST)currentAST.root;
		}
		returnAST = singleDeclaration_AST;
	}

/** An assignment operator '=' followed by an expression.  (Never empty.) */
	public final void varInitializer() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST varInitializer_AST = null;

		AST tmp53_AST = null;
		if (inputState.guessing==0) {
			tmp53_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp53_AST);
		}
		match(ASSIGN);
		nls();
		expression(LC_INIT);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			varInitializer_AST = (AST)currentAST.root;
		}
		returnAST = varInitializer_AST;
	}

/** Used only as a lookahead predicate, before diving in and parsing a declaration.
 *  A declaration can be unambiguously introduced with "def", an annotation or a modifier token like "final".
 *  It may also be introduced by a simple identifier whose first character is an uppercase letter,
 *  as in {String x}.  A declaration can also be introduced with a built in type like 'int' or 'void'.
 *  Brackets (array and generic) are allowed, as in {List[] x} or {int[][] y}.
 *  Anything else is parsed as a statement of some sort (expression or command).
 *  <p>
 *  (In the absence of explicit method-call parens, we assume a capitalized name is a type name.
 *  Yes, this is a little hacky.  Alternatives are to complicate the declaration or command
 *  syntaxes, or to have the parser query the symbol table.  Parse-time queries are evil.
 *  And we want both {String x} and {println x}.  So we need a syntactic razor-edge to slip
 *  between 'println' and 'String'.)
 *
 *   *TODO* The declarationStart production needs to be strengthened to recognize
 *  things like {List<String> foo}.
 *  Right now it only knows how to skip square brackets after the type, not
 *  angle brackets.
 *  This probably turns out to be tricky because of >> vs. > >. If so,
 *  just put a TODO comment in.
 */
	public final void declarationStart() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST declarationStart_AST = null;

		switch ( LA(1)) {
		case LITERAL_def:
		{
			AST tmp54_AST = null;
			if (inputState.guessing==0) {
				tmp54_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp54_AST);
			}
			match(LITERAL_def);
			if ( inputState.guessing==0 ) {
				declarationStart_AST = (AST)currentAST.root;
			}
			break;
		}
		case FINAL:
		case ABSTRACT:
		case STRICTFP:
		case LITERAL_static:
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		{
			modifier();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				declarationStart_AST = (AST)currentAST.root;
			}
			break;
		}
		case AT:
		{
			AST tmp55_AST = null;
			if (inputState.guessing==0) {
				tmp55_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp55_AST);
			}
			match(AT);
			AST tmp56_AST = null;
			if (inputState.guessing==0) {
				tmp56_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp56_AST);
			}
			match(IDENT);
			if ( inputState.guessing==0 ) {
				declarationStart_AST = (AST)currentAST.root;
			}
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
			if ((LA(1)==IDENT) && (LA(2)==IDENT||LA(2)==LBRACK)) {
				upperCaseIdent();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else if (((LA(1) >= LITERAL_void && LA(1) <= LITERAL_any))) {
				builtInType();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else if ((LA(1)==IDENT) && (LA(2)==DOT)) {
				qualifiedTypeName();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}

			}
			{
			_loop24:
			do {
				if ((LA(1)==LBRACK)) {
					AST tmp57_AST = null;
					if (inputState.guessing==0) {
						tmp57_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp57_AST);
					}
					match(LBRACK);
					balancedTokens();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					AST tmp58_AST = null;
					if (inputState.guessing==0) {
						tmp58_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp58_AST);
					}
					match(RBRACK);
				}
				else {
					break _loop24;
				}

			} while (true);
			}
			AST tmp59_AST = null;
			if (inputState.guessing==0) {
				tmp59_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp59_AST);
			}
			match(IDENT);
			if ( inputState.guessing==0 ) {
				declarationStart_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = declarationStart_AST;
	}

	public final void modifier() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST modifier_AST = null;

		switch ( LA(1)) {
		case LITERAL_private:
		{
			AST tmp60_AST = null;
			if (inputState.guessing==0) {
				tmp60_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp60_AST);
			}
			match(LITERAL_private);
			if ( inputState.guessing==0 ) {
				modifier_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_public:
		{
			AST tmp61_AST = null;
			if (inputState.guessing==0) {
				tmp61_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp61_AST);
			}
			match(LITERAL_public);
			if ( inputState.guessing==0 ) {
				modifier_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_protected:
		{
			AST tmp62_AST = null;
			if (inputState.guessing==0) {
				tmp62_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp62_AST);
			}
			match(LITERAL_protected);
			if ( inputState.guessing==0 ) {
				modifier_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_static:
		{
			AST tmp63_AST = null;
			if (inputState.guessing==0) {
				tmp63_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp63_AST);
			}
			match(LITERAL_static);
			if ( inputState.guessing==0 ) {
				modifier_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_transient:
		{
			AST tmp64_AST = null;
			if (inputState.guessing==0) {
				tmp64_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp64_AST);
			}
			match(LITERAL_transient);
			if ( inputState.guessing==0 ) {
				modifier_AST = (AST)currentAST.root;
			}
			break;
		}
		case FINAL:
		{
			AST tmp65_AST = null;
			if (inputState.guessing==0) {
				tmp65_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp65_AST);
			}
			match(FINAL);
			if ( inputState.guessing==0 ) {
				modifier_AST = (AST)currentAST.root;
			}
			break;
		}
		case ABSTRACT:
		{
			AST tmp66_AST = null;
			if (inputState.guessing==0) {
				tmp66_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp66_AST);
			}
			match(ABSTRACT);
			if ( inputState.guessing==0 ) {
				modifier_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_native:
		{
			AST tmp67_AST = null;
			if (inputState.guessing==0) {
				tmp67_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp67_AST);
			}
			match(LITERAL_native);
			if ( inputState.guessing==0 ) {
				modifier_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_threadsafe:
		{
			AST tmp68_AST = null;
			if (inputState.guessing==0) {
				tmp68_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp68_AST);
			}
			match(LITERAL_threadsafe);
			if ( inputState.guessing==0 ) {
				modifier_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_synchronized:
		{
			AST tmp69_AST = null;
			if (inputState.guessing==0) {
				tmp69_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp69_AST);
			}
			match(LITERAL_synchronized);
			if ( inputState.guessing==0 ) {
				modifier_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_volatile:
		{
			AST tmp70_AST = null;
			if (inputState.guessing==0) {
				tmp70_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp70_AST);
			}
			match(LITERAL_volatile);
			if ( inputState.guessing==0 ) {
				modifier_AST = (AST)currentAST.root;
			}
			break;
		}
		case STRICTFP:
		{
			AST tmp71_AST = null;
			if (inputState.guessing==0) {
				tmp71_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp71_AST);
			}
			match(STRICTFP);
			if ( inputState.guessing==0 ) {
				modifier_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = modifier_AST;
	}

/** An IDENT token whose spelling is required to start with an uppercase letter.
 *  In the case of a simple statement {UpperID name} the identifier is taken to be a type name, not a command name.
 */
	public final void upperCaseIdent() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST upperCaseIdent_AST = null;

		if (!(isUpperCase(LT(1))))
		  throw new SemanticException("isUpperCase(LT(1))");
		AST tmp72_AST = null;
		if (inputState.guessing==0) {
			tmp72_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp72_AST);
		}
		match(IDENT);
		if ( inputState.guessing==0 ) {
			upperCaseIdent_AST = (AST)currentAST.root;
		}
		returnAST = upperCaseIdent_AST;
	}

	public final void builtInType() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST builtInType_AST = null;

		switch ( LA(1)) {
		case LITERAL_void:
		{
			AST tmp73_AST = null;
			if (inputState.guessing==0) {
				tmp73_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp73_AST);
			}
			match(LITERAL_void);
			if ( inputState.guessing==0 ) {
				builtInType_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_boolean:
		{
			AST tmp74_AST = null;
			if (inputState.guessing==0) {
				tmp74_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp74_AST);
			}
			match(LITERAL_boolean);
			if ( inputState.guessing==0 ) {
				builtInType_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_byte:
		{
			AST tmp75_AST = null;
			if (inputState.guessing==0) {
				tmp75_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp75_AST);
			}
			match(LITERAL_byte);
			if ( inputState.guessing==0 ) {
				builtInType_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_char:
		{
			AST tmp76_AST = null;
			if (inputState.guessing==0) {
				tmp76_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp76_AST);
			}
			match(LITERAL_char);
			if ( inputState.guessing==0 ) {
				builtInType_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_short:
		{
			AST tmp77_AST = null;
			if (inputState.guessing==0) {
				tmp77_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp77_AST);
			}
			match(LITERAL_short);
			if ( inputState.guessing==0 ) {
				builtInType_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_int:
		{
			AST tmp78_AST = null;
			if (inputState.guessing==0) {
				tmp78_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp78_AST);
			}
			match(LITERAL_int);
			if ( inputState.guessing==0 ) {
				builtInType_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_float:
		{
			AST tmp79_AST = null;
			if (inputState.guessing==0) {
				tmp79_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp79_AST);
			}
			match(LITERAL_float);
			if ( inputState.guessing==0 ) {
				builtInType_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_long:
		{
			AST tmp80_AST = null;
			if (inputState.guessing==0) {
				tmp80_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp80_AST);
			}
			match(LITERAL_long);
			if ( inputState.guessing==0 ) {
				builtInType_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_double:
		{
			AST tmp81_AST = null;
			if (inputState.guessing==0) {
				tmp81_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp81_AST);
			}
			match(LITERAL_double);
			if ( inputState.guessing==0 ) {
				builtInType_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_any:
		{
			AST tmp82_AST = null;
			if (inputState.guessing==0) {
				tmp82_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp82_AST);
			}
			match(LITERAL_any);
			if ( inputState.guessing==0 ) {
				builtInType_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = builtInType_AST;
	}

/** Not yet used - but we could use something like this to look for fully qualified type names
 */
	public final void qualifiedTypeName() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST qualifiedTypeName_AST = null;

		AST tmp83_AST = null;
		if (inputState.guessing==0) {
			tmp83_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp83_AST);
		}
		match(IDENT);
		{
		_loop27:
		do {
			if ((LA(1)==DOT) && (LA(2)==IDENT) && (LA(3)==DOT)) {
				AST tmp84_AST = null;
				if (inputState.guessing==0) {
					tmp84_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp84_AST);
				}
				match(DOT);
				AST tmp85_AST = null;
				if (inputState.guessing==0) {
					tmp85_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp85_AST);
				}
				match(IDENT);
			}
			else {
				break _loop27;
			}

		} while (true);
		}
		AST tmp86_AST = null;
		if (inputState.guessing==0) {
			tmp86_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp86_AST);
		}
		match(DOT);
		upperCaseIdent();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			qualifiedTypeName_AST = (AST)currentAST.root;
		}
		returnAST = qualifiedTypeName_AST;
	}

	public final void balancedTokens() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST balancedTokens_AST = null;

		{
		_loop477:
		do {
			if ((_tokenSet_32.member(LA(1)))) {
				balancedBrackets();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else if ((_tokenSet_33.member(LA(1)))) {
				{
				AST tmp87_AST = null;
				if (inputState.guessing==0) {
					tmp87_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp87_AST);
				}
				match(_tokenSet_33);
				}
			}
			else {
				break _loop477;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			balancedTokens_AST = (AST)currentAST.root;
		}
		returnAST = balancedTokens_AST;
	}

/** Used to look ahead for a constructor
 */
	public final void constructorStart() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constructorStart_AST = null;
		Token  id = null;
		AST id_AST = null;

		modifiersOpt();
		id = LT(1);
		if (inputState.guessing==0) {
			id_AST = astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
		}
		match(IDENT);
		if (!(isConstructorIdent(id)))
		  throw new SemanticException("isConstructorIdent(id)");
		nls();
		match(LPAREN);
		if ( inputState.guessing==0 ) {
			constructorStart_AST = (AST)currentAST.root;
		}
		returnAST = constructorStart_AST;
	}

/** A list of zero or more modifiers, annotations, or "def". */
	public final void modifiersOpt() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST modifiersOpt_AST = null;

		{
		if ((_tokenSet_34.member(LA(1))) && (_tokenSet_35.member(LA(2))) && (_tokenSet_36.member(LA(3)))) {
			modifiersInternal();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((_tokenSet_37.member(LA(1))) && (_tokenSet_38.member(LA(2))) && (_tokenSet_39.member(LA(3)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		if ( inputState.guessing==0 ) {
			modifiersOpt_AST = (AST)currentAST.root;
			modifiersOpt_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(MODIFIERS,"MODIFIERS")).add(modifiersOpt_AST));
			currentAST.root = modifiersOpt_AST;
			currentAST.child = modifiersOpt_AST!=null &&modifiersOpt_AST.getFirstChild()!=null ?
				modifiersOpt_AST.getFirstChild() : modifiersOpt_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			modifiersOpt_AST = (AST)currentAST.root;
		}
		returnAST = modifiersOpt_AST;
	}

/** Used only as a lookahead predicate for nested type declarations. */
	public final void typeDeclarationStart() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeDeclarationStart_AST = null;

		modifiersOpt();
		{
		switch ( LA(1)) {
		case LITERAL_class:
		{
			AST tmp89_AST = null;
			if (inputState.guessing==0) {
				tmp89_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp89_AST);
			}
			match(LITERAL_class);
			break;
		}
		case LITERAL_interface:
		{
			AST tmp90_AST = null;
			if (inputState.guessing==0) {
				tmp90_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp90_AST);
			}
			match(LITERAL_interface);
			break;
		}
		case LITERAL_enum:
		{
			AST tmp91_AST = null;
			if (inputState.guessing==0) {
				tmp91_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp91_AST);
			}
			match(LITERAL_enum);
			break;
		}
		case AT:
		{
			AST tmp92_AST = null;
			if (inputState.guessing==0) {
				tmp92_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp92_AST);
			}
			match(AT);
			AST tmp93_AST = null;
			if (inputState.guessing==0) {
				tmp93_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp93_AST);
			}
			match(LITERAL_interface);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			typeDeclarationStart_AST = (AST)currentAST.root;
		}
		returnAST = typeDeclarationStart_AST;
	}

	public final void classTypeSpec(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classTypeSpec_AST = null;
		AST ct_AST = null;

		classOrInterfaceType(false);
		if (inputState.guessing==0) {
			ct_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		declaratorBrackets(ct_AST);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			classTypeSpec_AST = (AST)currentAST.root;

			if ( addImagNode ) {
			classTypeSpec_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(classTypeSpec_AST));
			}

			currentAST.root = classTypeSpec_AST;
			currentAST.child = classTypeSpec_AST!=null &&classTypeSpec_AST.getFirstChild()!=null ?
				classTypeSpec_AST.getFirstChild() : classTypeSpec_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			classTypeSpec_AST = (AST)currentAST.root;
		}
		returnAST = classTypeSpec_AST;
	}

	public final void builtInTypeSpec(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST builtInTypeSpec_AST = null;
		AST bt_AST = null;

		builtInType();
		if (inputState.guessing==0) {
			bt_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		declaratorBrackets(bt_AST);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			builtInTypeSpec_AST = (AST)currentAST.root;

			if ( addImagNode ) {
			builtInTypeSpec_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(builtInTypeSpec_AST));
			}

			currentAST.root = builtInTypeSpec_AST;
			currentAST.child = builtInTypeSpec_AST!=null &&builtInTypeSpec_AST.getFirstChild()!=null ?
				builtInTypeSpec_AST.getFirstChild() : builtInTypeSpec_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			builtInTypeSpec_AST = (AST)currentAST.root;
		}
		returnAST = builtInTypeSpec_AST;
	}

	public final void classOrInterfaceType(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classOrInterfaceType_AST = null;

		AST tmp94_AST = null;
		if (inputState.guessing==0) {
			tmp94_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp94_AST);
		}
		match(IDENT);
		{
		switch ( LA(1)) {
		case LT:
		{
			typeArguments();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case EOF:
		case UNUSED_DO:
		case LITERAL_def:
		case AT:
		case IDENT:
		case LBRACK:
		case RBRACK:
		case DOT:
		case LPAREN:
		case LITERAL_class:
		case QUESTION:
		case LITERAL_extends:
		case LITERAL_super:
		case COMMA:
		case GT:
		case SR:
		case BSR:
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
		case RPAREN:
		case ASSIGN:
		case BAND:
		case LCURLY:
		case RCURLY:
		case SEMI:
		case NLS:
		case LITERAL_default:
		case LITERAL_implements:
		case LITERAL_this:
		case STRING_LITERAL:
		case TRIPLE_DOT:
		case CLOSURE_OP:
		case LOR:
		case BOR:
		case COLON:
		case LITERAL_if:
		case LITERAL_else:
		case LITERAL_while:
		case LITERAL_switch:
		case LITERAL_for:
		case LITERAL_in:
		case PLUS:
		case MINUS:
		case LITERAL_case:
		case LITERAL_try:
		case LITERAL_finally:
		case LITERAL_catch:
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
		case LAND:
		case BXOR:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case DOLLAR:
		case STRING_CTOR_START:
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
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		_loop38:
		do {
			if ((LA(1)==DOT) && (LA(2)==IDENT) && (_tokenSet_40.member(LA(3)))) {
				AST tmp95_AST = null;
				if (inputState.guessing==0) {
					tmp95_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp95_AST);
				}
				match(DOT);
				AST tmp96_AST = null;
				if (inputState.guessing==0) {
					tmp96_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp96_AST);
				}
				match(IDENT);
				{
				switch ( LA(1)) {
				case LT:
				{
					typeArguments();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case EOF:
				case UNUSED_DO:
				case LITERAL_def:
				case AT:
				case IDENT:
				case LBRACK:
				case RBRACK:
				case DOT:
				case LPAREN:
				case LITERAL_class:
				case QUESTION:
				case LITERAL_extends:
				case LITERAL_super:
				case COMMA:
				case GT:
				case SR:
				case BSR:
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
				case RPAREN:
				case ASSIGN:
				case BAND:
				case LCURLY:
				case RCURLY:
				case SEMI:
				case NLS:
				case LITERAL_default:
				case LITERAL_implements:
				case LITERAL_this:
				case STRING_LITERAL:
				case TRIPLE_DOT:
				case CLOSURE_OP:
				case LOR:
				case BOR:
				case COLON:
				case LITERAL_if:
				case LITERAL_else:
				case LITERAL_while:
				case LITERAL_switch:
				case LITERAL_for:
				case LITERAL_in:
				case PLUS:
				case MINUS:
				case LITERAL_case:
				case LITERAL_try:
				case LITERAL_finally:
				case LITERAL_catch:
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
				case LAND:
				case BXOR:
				case REGEX_FIND:
				case REGEX_MATCH:
				case NOT_EQUAL:
				case EQUAL:
				case COMPARE_TO:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case DOLLAR:
				case STRING_CTOR_START:
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
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else {
				break _loop38;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			classOrInterfaceType_AST = (AST)currentAST.root;

			if ( addImagNode ) {
			classOrInterfaceType_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(classOrInterfaceType_AST));
			}

			currentAST.root = classOrInterfaceType_AST;
			currentAST.child = classOrInterfaceType_AST!=null &&classOrInterfaceType_AST.getFirstChild()!=null ?
				classOrInterfaceType_AST.getFirstChild() : classOrInterfaceType_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			classOrInterfaceType_AST = (AST)currentAST.root;
		}
		returnAST = classOrInterfaceType_AST;
	}

/** After some type names, where zero or more empty bracket pairs are allowed.
 *  We use ARRAY_DECLARATOR to represent this.
 *  TODO:  Is there some more Groovy way to view this in terms of the indexed property syntax?
 */
	public final void declaratorBrackets(
		AST typ
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST declaratorBrackets_AST = null;

		if ( inputState.guessing==0 ) {
			declaratorBrackets_AST = (AST)currentAST.root;
			declaratorBrackets_AST=typ;
			currentAST.root = declaratorBrackets_AST;
			currentAST.child = declaratorBrackets_AST!=null &&declaratorBrackets_AST.getFirstChild()!=null ?
				declaratorBrackets_AST.getFirstChild() : declaratorBrackets_AST;
			currentAST.advanceChildToEnd();
		}
		{
		_loop200:
		do {
			if ((LA(1)==LBRACK) && (LA(2)==RBRACK) && (_tokenSet_41.member(LA(3)))) {
				match(LBRACK);
				if ( inputState.guessing==0 ) {
					astFactory.makeASTRoot(currentAST, astFactory.create(ARRAY_DECLARATOR, "["));
				}
				match(RBRACK);
			}
			else {
				break _loop200;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			declaratorBrackets_AST = (AST)currentAST.root;
		}
		returnAST = declaratorBrackets_AST;
	}

	public final void typeArguments() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeArguments_AST = null;
		int currentLtLevel = 0;

		if ( inputState.guessing==0 ) {
			currentLtLevel = ltCounter;
		}
		match(LT);
		if ( inputState.guessing==0 ) {
			ltCounter++;
		}
		nls();
		typeArgument();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop48:
		do {
			if (((LA(1)==COMMA) && (_tokenSet_42.member(LA(2))) && (_tokenSet_40.member(LA(3))))&&(inputState.guessing !=0 || ltCounter == currentLtLevel + 1)) {
				match(COMMA);
				nls();
				typeArgument();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop48;
			}

		} while (true);
		}
		nls();
		{
		if (((LA(1) >= GT && LA(1) <= BSR)) && (_tokenSet_41.member(LA(2))) && (_tokenSet_5.member(LA(3)))) {
			typeArgumentsOrParametersEnd();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((_tokenSet_41.member(LA(1))) && (_tokenSet_5.member(LA(2))) && (_tokenSet_5.member(LA(3)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		if (!((currentLtLevel != 0) || ltCounter == currentLtLevel))
		  throw new SemanticException("(currentLtLevel != 0) || ltCounter == currentLtLevel");
		if ( inputState.guessing==0 ) {
			typeArguments_AST = (AST)currentAST.root;
			typeArguments_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE_ARGUMENTS,"TYPE_ARGUMENTS")).add(typeArguments_AST));
			currentAST.root = typeArguments_AST;
			currentAST.child = typeArguments_AST!=null &&typeArguments_AST.getFirstChild()!=null ?
				typeArguments_AST.getFirstChild() : typeArguments_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			typeArguments_AST = (AST)currentAST.root;
		}
		returnAST = typeArguments_AST;
	}

	public final void typeArgumentSpec() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeArgumentSpec_AST = null;

		switch ( LA(1)) {
		case IDENT:
		{
			classTypeSpec(true);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				typeArgumentSpec_AST = (AST)currentAST.root;
			}
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
			builtInTypeArraySpec(true);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				typeArgumentSpec_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = typeArgumentSpec_AST;
	}

	public final void builtInTypeArraySpec(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST builtInTypeArraySpec_AST = null;
		AST bt_AST = null;

		builtInType();
		if (inputState.guessing==0) {
			bt_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		boolean synPredMatched56 = false;
		if (((_tokenSet_41.member(LA(1))) && (_tokenSet_5.member(LA(2))) && (_tokenSet_5.member(LA(3))))) {
			int _m56 = mark();
			synPredMatched56 = true;
			inputState.guessing++;
			try {
				{
				match(LBRACK);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched56 = false;
			}
			rewind(_m56);
			inputState.guessing--;
		}
		if ( synPredMatched56 ) {
			declaratorBrackets(bt_AST);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((_tokenSet_41.member(LA(1))) && (_tokenSet_5.member(LA(2))) && (_tokenSet_5.member(LA(3)))) {
			if ( inputState.guessing==0 ) {
				require(false,
				"primitive type parameters not allowed here",
				"use the corresponding wrapper type, such as Integer for int"
				);
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		if ( inputState.guessing==0 ) {
			builtInTypeArraySpec_AST = (AST)currentAST.root;

			if ( addImagNode ) {
			builtInTypeArraySpec_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(builtInTypeArraySpec_AST));
			}

			currentAST.root = builtInTypeArraySpec_AST;
			currentAST.child = builtInTypeArraySpec_AST!=null &&builtInTypeArraySpec_AST.getFirstChild()!=null ?
				builtInTypeArraySpec_AST.getFirstChild() : builtInTypeArraySpec_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			builtInTypeArraySpec_AST = (AST)currentAST.root;
		}
		returnAST = builtInTypeArraySpec_AST;
	}

	public final void typeArgument() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeArgument_AST = null;

		{
		switch ( LA(1)) {
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
			typeArgumentSpec();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case QUESTION:
		{
			wildcardType();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			typeArgument_AST = (AST)currentAST.root;
			typeArgument_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE_ARGUMENT,"TYPE_ARGUMENT")).add(typeArgument_AST));
			currentAST.root = typeArgument_AST;
			currentAST.child = typeArgument_AST!=null &&typeArgument_AST.getFirstChild()!=null ?
				typeArgument_AST.getFirstChild() : typeArgument_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			typeArgument_AST = (AST)currentAST.root;
		}
		returnAST = typeArgument_AST;
	}

	public final void wildcardType() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST wildcardType_AST = null;

		match(QUESTION);
		if ( inputState.guessing==0 ) {
			astFactory.makeASTRoot(currentAST, astFactory.create(WILDCARD_TYPE, "?"));
		}
		{
		boolean synPredMatched45 = false;
		if (((LA(1)==LITERAL_extends||LA(1)==LITERAL_super) && (LA(2)==IDENT||LA(2)==NLS) && (_tokenSet_40.member(LA(3))))) {
			int _m45 = mark();
			synPredMatched45 = true;
			inputState.guessing++;
			try {
				{
				switch ( LA(1)) {
				case LITERAL_extends:
				{
					match(LITERAL_extends);
					break;
				}
				case LITERAL_super:
				{
					match(LITERAL_super);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			catch (RecognitionException pe) {
				synPredMatched45 = false;
			}
			rewind(_m45);
			inputState.guessing--;
		}
		if ( synPredMatched45 ) {
			typeArgumentBounds();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((_tokenSet_41.member(LA(1))) && (_tokenSet_5.member(LA(2))) && (_tokenSet_5.member(LA(3)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		if ( inputState.guessing==0 ) {
			wildcardType_AST = (AST)currentAST.root;
		}
		returnAST = wildcardType_AST;
	}

	public final void typeArgumentBounds() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeArgumentBounds_AST = null;
		boolean isUpperBounds = false;

		{
		switch ( LA(1)) {
		case LITERAL_extends:
		{
			AST tmp102_AST = null;
			if (inputState.guessing==0) {
				tmp102_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp102_AST);
			}
			match(LITERAL_extends);
			if ( inputState.guessing==0 ) {
				isUpperBounds=true;
			}
			break;
		}
		case LITERAL_super:
		{
			AST tmp103_AST = null;
			if (inputState.guessing==0) {
				tmp103_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp103_AST);
			}
			match(LITERAL_super);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		nls();
		classOrInterfaceType(false);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		nls();
		if ( inputState.guessing==0 ) {
			typeArgumentBounds_AST = (AST)currentAST.root;

			if (isUpperBounds)
			{
			typeArgumentBounds_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE_UPPER_BOUNDS,"TYPE_UPPER_BOUNDS")).add(typeArgumentBounds_AST));
			}
			else
			{
			typeArgumentBounds_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE_LOWER_BOUNDS,"TYPE_LOWER_BOUNDS")).add(typeArgumentBounds_AST));
			}

			currentAST.root = typeArgumentBounds_AST;
			currentAST.child = typeArgumentBounds_AST!=null &&typeArgumentBounds_AST.getFirstChild()!=null ?
				typeArgumentBounds_AST.getFirstChild() : typeArgumentBounds_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			typeArgumentBounds_AST = (AST)currentAST.root;
		}
		returnAST = typeArgumentBounds_AST;
	}

	protected final void typeArgumentsOrParametersEnd() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeArgumentsOrParametersEnd_AST = null;

		switch ( LA(1)) {
		case GT:
		{
			match(GT);
			if ( inputState.guessing==0 ) {
				ltCounter-=1;
			}
			nls();
			if ( inputState.guessing==0 ) {
				typeArgumentsOrParametersEnd_AST = (AST)currentAST.root;
			}
			break;
		}
		case SR:
		{
			match(SR);
			if ( inputState.guessing==0 ) {
				ltCounter-=2;
			}
			nls();
			if ( inputState.guessing==0 ) {
				typeArgumentsOrParametersEnd_AST = (AST)currentAST.root;
			}
			break;
		}
		case BSR:
		{
			match(BSR);
			if ( inputState.guessing==0 ) {
				ltCounter-=3;
			}
			nls();
			if ( inputState.guessing==0 ) {
				typeArgumentsOrParametersEnd_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = typeArgumentsOrParametersEnd_AST;
	}

	public final void type() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST type_AST = null;

		switch ( LA(1)) {
		case IDENT:
		{
			classOrInterfaceType(false);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				type_AST = (AST)currentAST.root;
			}
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
			builtInType();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				type_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = type_AST;
	}

	public final void modifiersInternal() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST modifiersInternal_AST = null;
		int seenDef = 0;

		{
		int _cnt69=0;
		_loop69:
		do {
			if (((LA(1)==LITERAL_def))&&(seenDef++ == 0)) {
				AST tmp107_AST = null;
				if (inputState.guessing==0) {
					tmp107_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp107_AST);
				}
				match(LITERAL_def);
				nls();
			}
			else if ((_tokenSet_43.member(LA(1)))) {
				modifier();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				nls();
			}
			else if (((LA(1)==AT) && (LA(2)==IDENT) && (_tokenSet_44.member(LA(3))))&&(LA(1)==AT && !LT(2).getText().equals("interface"))) {
				annotation();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				nls();
			}
			else {
				if ( _cnt69>=1 ) { break _loop69; } else {throw new NoViableAltException(LT(1), getFilename());}
			}

			_cnt69++;
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			modifiersInternal_AST = (AST)currentAST.root;
		}
		returnAST = modifiersInternal_AST;
	}

	public final void annotation() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotation_AST = null;
		AST i_AST = null;
		AST args_AST = null;

		match(AT);
		identifier();
		if (inputState.guessing==0) {
			i_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		switch ( LA(1)) {
		case LPAREN:
		{
			match(LPAREN);
			{
			switch ( LA(1)) {
			case AT:
			case IDENT:
			case LBRACK:
			case LPAREN:
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
			case BAND:
			case LCURLY:
			case LITERAL_this:
			case STRING_LITERAL:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case DOLLAR:
			case STRING_CTOR_START:
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
				annotationArguments();
				if (inputState.guessing==0) {
					args_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
				break;
			}
			case RPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RPAREN);
			break;
		}
		case EOF:
		case FINAL:
		case ABSTRACT:
		case STRICTFP:
		case LITERAL_package:
		case LITERAL_static:
		case LITERAL_def:
		case AT:
		case IDENT:
		case RBRACK:
		case LITERAL_class:
		case LITERAL_interface:
		case LITERAL_enum:
		case LT:
		case COMMA:
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
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case RPAREN:
		case RCURLY:
		case SEMI:
		case NLS:
		case STRING_LITERAL:
		case TRIPLE_DOT:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			annotation_AST = (AST)currentAST.root;
			annotation_AST = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(ANNOTATION,"ANNOTATION")).add(i_AST).add(args_AST));
			currentAST.root = annotation_AST;
			currentAST.child = annotation_AST!=null &&annotation_AST.getFirstChild()!=null ?
				annotation_AST.getFirstChild() : annotation_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			annotation_AST = (AST)currentAST.root;
		}
		returnAST = annotation_AST;
	}

	public final void annotationArguments() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationArguments_AST = null;

		if ((_tokenSet_45.member(LA(1))) && (_tokenSet_46.member(LA(2)))) {
			annotationMemberValueInitializer();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				annotationArguments_AST = (AST)currentAST.root;
			}
		}
		else if ((LA(1)==IDENT) && (LA(2)==ASSIGN)) {
			anntotationMemberValuePairs();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				annotationArguments_AST = (AST)currentAST.root;
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		returnAST = annotationArguments_AST;
	}

	public final void annotationMemberValueInitializer() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationMemberValueInitializer_AST = null;

		switch ( LA(1)) {
		case IDENT:
		case LBRACK:
		case LPAREN:
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
		case BAND:
		case LCURLY:
		case LITERAL_this:
		case STRING_LITERAL:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case DOLLAR:
		case STRING_CTOR_START:
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
			conditionalExpression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				annotationMemberValueInitializer_AST = (AST)currentAST.root;
			}
			break;
		}
		case AT:
		{
			annotation();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				annotationMemberValueInitializer_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = annotationMemberValueInitializer_AST;
	}

	public final void anntotationMemberValuePairs() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST anntotationMemberValuePairs_AST = null;

		annotationMemberValuePair();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop83:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				nls();
				annotationMemberValuePair();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop83;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			anntotationMemberValuePairs_AST = (AST)currentAST.root;
		}
		returnAST = anntotationMemberValuePairs_AST;
	}

	public final void annotationMemberValuePair() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationMemberValuePair_AST = null;
		Token  i = null;
		AST i_AST = null;
		AST v_AST = null;

		i = LT(1);
		if (inputState.guessing==0) {
			i_AST = astFactory.create(i);
			astFactory.addASTChild(currentAST, i_AST);
		}
		match(IDENT);
		match(ASSIGN);
		nls();
		annotationMemberValueInitializer();
		if (inputState.guessing==0) {
			v_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			annotationMemberValuePair_AST = (AST)currentAST.root;
			annotationMemberValuePair_AST = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(ANNOTATION_MEMBER_VALUE_PAIR,"ANNOTATION_MEMBER_VALUE_PAIR")).add(i_AST).add(v_AST));
			currentAST.root = annotationMemberValuePair_AST;
			currentAST.child = annotationMemberValuePair_AST!=null &&annotationMemberValuePair_AST.getFirstChild()!=null ?
				annotationMemberValuePair_AST.getFirstChild() : annotationMemberValuePair_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			annotationMemberValuePair_AST = (AST)currentAST.root;
		}
		returnAST = annotationMemberValuePair_AST;
	}

	public final void conditionalExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST conditionalExpression_AST = null;

		logicalOrExpression(lc_stmt);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		switch ( LA(1)) {
		case QUESTION:
		{
			AST tmp113_AST = null;
			if (inputState.guessing==0) {
				tmp113_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp113_AST);
			}
			match(QUESTION);
			nls();
			assignmentExpression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			match(COLON);
			nls();
			conditionalExpression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case EOF:
		case IDENT:
		case LBRACK:
		case RBRACK:
		case LPAREN:
		case LITERAL_super:
		case COMMA:
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
		case RPAREN:
		case ASSIGN:
		case BAND:
		case LCURLY:
		case RCURLY:
		case SEMI:
		case NLS:
		case LITERAL_default:
		case LITERAL_this:
		case STRING_LITERAL:
		case CLOSURE_OP:
		case COLON:
		case LITERAL_else:
		case PLUS:
		case MINUS:
		case LITERAL_case:
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
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case DOLLAR:
		case STRING_CTOR_START:
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
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			conditionalExpression_AST = (AST)currentAST.root;
		}
		returnAST = conditionalExpression_AST;
	}

	public final void annotationMemberArrayValueInitializer() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationMemberArrayValueInitializer_AST = null;

		switch ( LA(1)) {
		case IDENT:
		case LBRACK:
		case LPAREN:
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
		case BAND:
		case LCURLY:
		case LITERAL_this:
		case STRING_LITERAL:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case DOLLAR:
		case STRING_CTOR_START:
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
			conditionalExpression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				annotationMemberArrayValueInitializer_AST = (AST)currentAST.root;
			}
			break;
		}
		case AT:
		{
			annotation();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			nls();
			if ( inputState.guessing==0 ) {
				annotationMemberArrayValueInitializer_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = annotationMemberArrayValueInitializer_AST;
	}

	public final void superClassClause() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST superClassClause_AST = null;
		AST c_AST = null;

		{
		switch ( LA(1)) {
		case LITERAL_extends:
		{
			AST tmp115_AST = null;
			if (inputState.guessing==0) {
				tmp115_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp115_AST);
			}
			match(LITERAL_extends);
			nls();
			classOrInterfaceType(false);
			if (inputState.guessing==0) {
				c_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			nls();
			break;
		}
		case LCURLY:
		case LITERAL_implements:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			superClassClause_AST = (AST)currentAST.root;
			superClassClause_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(EXTENDS_CLAUSE,"EXTENDS_CLAUSE")).add(c_AST));
			currentAST.root = superClassClause_AST;
			currentAST.child = superClassClause_AST!=null &&superClassClause_AST.getFirstChild()!=null ?
				superClassClause_AST.getFirstChild() : superClassClause_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			superClassClause_AST = (AST)currentAST.root;
		}
		returnAST = superClassClause_AST;
	}

	public final void typeParameters() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeParameters_AST = null;
		int currentLtLevel = 0;

		if ( inputState.guessing==0 ) {
			currentLtLevel = ltCounter;
		}
		match(LT);
		if ( inputState.guessing==0 ) {
			ltCounter++;
		}
		nls();
		typeParameter();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop97:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				nls();
				typeParameter();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop97;
			}

		} while (true);
		}
		nls();
		{
		switch ( LA(1)) {
		case GT:
		case SR:
		case BSR:
		{
			typeArgumentsOrParametersEnd();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case IDENT:
		case LITERAL_extends:
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
		case LCURLY:
		case LITERAL_implements:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if (!((currentLtLevel != 0) || ltCounter == currentLtLevel))
		  throw new SemanticException("(currentLtLevel != 0) || ltCounter == currentLtLevel");
		if ( inputState.guessing==0 ) {
			typeParameters_AST = (AST)currentAST.root;
			typeParameters_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE_PARAMETERS,"TYPE_PARAMETERS")).add(typeParameters_AST));
			currentAST.root = typeParameters_AST;
			currentAST.child = typeParameters_AST!=null &&typeParameters_AST.getFirstChild()!=null ?
				typeParameters_AST.getFirstChild() : typeParameters_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			typeParameters_AST = (AST)currentAST.root;
		}
		returnAST = typeParameters_AST;
	}

	public final void implementsClause() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST implementsClause_AST = null;
		Token  i = null;
		AST i_AST = null;

		{
		switch ( LA(1)) {
		case LITERAL_implements:
		{
			i = LT(1);
			if (inputState.guessing==0) {
				i_AST = astFactory.create(i);
				astFactory.addASTChild(currentAST, i_AST);
			}
			match(LITERAL_implements);
			nls();
			classOrInterfaceType(false);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			_loop163:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					nls();
					classOrInterfaceType(false);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
				}
				else {
					break _loop163;
				}

			} while (true);
			}
			nls();
			break;
		}
		case LCURLY:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			implementsClause_AST = (AST)currentAST.root;
			implementsClause_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(IMPLEMENTS_CLAUSE,"IMPLEMENTS_CLAUSE")).add(implementsClause_AST));
			currentAST.root = implementsClause_AST;
			currentAST.child = implementsClause_AST!=null &&implementsClause_AST.getFirstChild()!=null ?
				implementsClause_AST.getFirstChild() : implementsClause_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			implementsClause_AST = (AST)currentAST.root;
		}
		returnAST = implementsClause_AST;
	}

	public final void classBlock() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classBlock_AST = null;

		match(LCURLY);
		{
		switch ( LA(1)) {
		case FINAL:
		case ABSTRACT:
		case STRICTFP:
		case LITERAL_static:
		case LITERAL_def:
		case AT:
		case IDENT:
		case LITERAL_class:
		case LITERAL_interface:
		case LITERAL_enum:
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
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case LCURLY:
		{
			classField();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case RCURLY:
		case SEMI:
		case NLS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		_loop109:
		do {
			if ((LA(1)==SEMI||LA(1)==NLS)) {
				sep();
				{
				switch ( LA(1)) {
				case FINAL:
				case ABSTRACT:
				case STRICTFP:
				case LITERAL_static:
				case LITERAL_def:
				case AT:
				case IDENT:
				case LITERAL_class:
				case LITERAL_interface:
				case LITERAL_enum:
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
				case LITERAL_private:
				case LITERAL_public:
				case LITERAL_protected:
				case LITERAL_transient:
				case LITERAL_native:
				case LITERAL_threadsafe:
				case LITERAL_synchronized:
				case LITERAL_volatile:
				case LCURLY:
				{
					classField();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case RCURLY:
				case SEMI:
				case NLS:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else {
				break _loop109;
			}

		} while (true);
		}
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			classBlock_AST = (AST)currentAST.root;
			classBlock_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(OBJBLOCK,"OBJBLOCK")).add(classBlock_AST));
			currentAST.root = classBlock_AST;
			currentAST.child = classBlock_AST!=null &&classBlock_AST.getFirstChild()!=null ?
				classBlock_AST.getFirstChild() : classBlock_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			classBlock_AST = (AST)currentAST.root;
		}
		returnAST = classBlock_AST;
	}

	public final void interfaceExtends() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST interfaceExtends_AST = null;
		Token  e = null;
		AST e_AST = null;

		{
		switch ( LA(1)) {
		case LITERAL_extends:
		{
			e = LT(1);
			if (inputState.guessing==0) {
				e_AST = astFactory.create(e);
				astFactory.addASTChild(currentAST, e_AST);
			}
			match(LITERAL_extends);
			nls();
			classOrInterfaceType(false);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			_loop159:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					nls();
					classOrInterfaceType(false);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
				}
				else {
					break _loop159;
				}

			} while (true);
			}
			nls();
			break;
		}
		case LCURLY:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			interfaceExtends_AST = (AST)currentAST.root;
			interfaceExtends_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(EXTENDS_CLAUSE,"EXTENDS_CLAUSE")).add(interfaceExtends_AST));
			currentAST.root = interfaceExtends_AST;
			currentAST.child = interfaceExtends_AST!=null &&interfaceExtends_AST.getFirstChild()!=null ?
				interfaceExtends_AST.getFirstChild() : interfaceExtends_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			interfaceExtends_AST = (AST)currentAST.root;
		}
		returnAST = interfaceExtends_AST;
	}

	public final void interfaceBlock() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST interfaceBlock_AST = null;

		match(LCURLY);
		{
		switch ( LA(1)) {
		case FINAL:
		case ABSTRACT:
		case STRICTFP:
		case LITERAL_static:
		case LITERAL_def:
		case AT:
		case IDENT:
		case LITERAL_class:
		case LITERAL_interface:
		case LITERAL_enum:
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
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		{
			interfaceField();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case RCURLY:
		case SEMI:
		case NLS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		_loop114:
		do {
			if ((LA(1)==SEMI||LA(1)==NLS)) {
				sep();
				{
				switch ( LA(1)) {
				case FINAL:
				case ABSTRACT:
				case STRICTFP:
				case LITERAL_static:
				case LITERAL_def:
				case AT:
				case IDENT:
				case LITERAL_class:
				case LITERAL_interface:
				case LITERAL_enum:
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
				case LITERAL_private:
				case LITERAL_public:
				case LITERAL_protected:
				case LITERAL_transient:
				case LITERAL_native:
				case LITERAL_threadsafe:
				case LITERAL_synchronized:
				case LITERAL_volatile:
				{
					interfaceField();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case RCURLY:
				case SEMI:
				case NLS:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else {
				break _loop114;
			}

		} while (true);
		}
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			interfaceBlock_AST = (AST)currentAST.root;
			interfaceBlock_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(OBJBLOCK,"OBJBLOCK")).add(interfaceBlock_AST));
			currentAST.root = interfaceBlock_AST;
			currentAST.child = interfaceBlock_AST!=null &&interfaceBlock_AST.getFirstChild()!=null ?
				interfaceBlock_AST.getFirstChild() : interfaceBlock_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			interfaceBlock_AST = (AST)currentAST.root;
		}
		returnAST = interfaceBlock_AST;
	}

	public final void enumBlock() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumBlock_AST = null;

		match(LCURLY);
		{
		boolean synPredMatched123 = false;
		if (((LA(1)==AT||LA(1)==IDENT) && (_tokenSet_47.member(LA(2))) && (_tokenSet_48.member(LA(3))))) {
			int _m123 = mark();
			synPredMatched123 = true;
			inputState.guessing++;
			try {
				{
				enumConstantsStart();
				}
			}
			catch (RecognitionException pe) {
				synPredMatched123 = false;
			}
			rewind(_m123);
			inputState.guessing--;
		}
		if ( synPredMatched123 ) {
			enumConstants();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((_tokenSet_49.member(LA(1))) && (_tokenSet_50.member(LA(2))) && (_tokenSet_18.member(LA(3)))) {
			{
			switch ( LA(1)) {
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case LITERAL_static:
			case LITERAL_def:
			case AT:
			case IDENT:
			case LITERAL_class:
			case LITERAL_interface:
			case LITERAL_enum:
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
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_threadsafe:
			case LITERAL_synchronized:
			case LITERAL_volatile:
			case LCURLY:
			{
				classField();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				break;
			}
			case RCURLY:
			case SEMI:
			case NLS:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		{
		_loop127:
		do {
			if ((LA(1)==SEMI||LA(1)==NLS)) {
				sep();
				{
				switch ( LA(1)) {
				case FINAL:
				case ABSTRACT:
				case STRICTFP:
				case LITERAL_static:
				case LITERAL_def:
				case AT:
				case IDENT:
				case LITERAL_class:
				case LITERAL_interface:
				case LITERAL_enum:
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
				case LITERAL_private:
				case LITERAL_public:
				case LITERAL_protected:
				case LITERAL_transient:
				case LITERAL_native:
				case LITERAL_threadsafe:
				case LITERAL_synchronized:
				case LITERAL_volatile:
				case LCURLY:
				{
					classField();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case RCURLY:
				case SEMI:
				case NLS:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else {
				break _loop127;
			}

		} while (true);
		}
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			enumBlock_AST = (AST)currentAST.root;
			enumBlock_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(OBJBLOCK,"OBJBLOCK")).add(enumBlock_AST));
			currentAST.root = enumBlock_AST;
			currentAST.child = enumBlock_AST!=null &&enumBlock_AST.getFirstChild()!=null ?
				enumBlock_AST.getFirstChild() : enumBlock_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			enumBlock_AST = (AST)currentAST.root;
		}
		returnAST = enumBlock_AST;
	}

	public final void annotationBlock() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationBlock_AST = null;

		match(LCURLY);
		{
		switch ( LA(1)) {
		case FINAL:
		case ABSTRACT:
		case STRICTFP:
		case LITERAL_static:
		case LITERAL_def:
		case AT:
		case IDENT:
		case LITERAL_class:
		case LITERAL_interface:
		case LITERAL_enum:
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
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		{
			annotationField();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case RCURLY:
		case SEMI:
		case NLS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		_loop119:
		do {
			if ((LA(1)==SEMI||LA(1)==NLS)) {
				sep();
				{
				switch ( LA(1)) {
				case FINAL:
				case ABSTRACT:
				case STRICTFP:
				case LITERAL_static:
				case LITERAL_def:
				case AT:
				case IDENT:
				case LITERAL_class:
				case LITERAL_interface:
				case LITERAL_enum:
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
				case LITERAL_private:
				case LITERAL_public:
				case LITERAL_protected:
				case LITERAL_transient:
				case LITERAL_native:
				case LITERAL_threadsafe:
				case LITERAL_synchronized:
				case LITERAL_volatile:
				{
					annotationField();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case RCURLY:
				case SEMI:
				case NLS:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else {
				break _loop119;
			}

		} while (true);
		}
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			annotationBlock_AST = (AST)currentAST.root;
			annotationBlock_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(OBJBLOCK,"OBJBLOCK")).add(annotationBlock_AST));
			currentAST.root = annotationBlock_AST;
			currentAST.child = annotationBlock_AST!=null &&annotationBlock_AST.getFirstChild()!=null ?
				annotationBlock_AST.getFirstChild() : annotationBlock_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			annotationBlock_AST = (AST)currentAST.root;
		}
		returnAST = annotationBlock_AST;
	}

	public final void typeParameter() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeParameter_AST = null;
		Token  id = null;
		AST id_AST = null;

		{
		id = LT(1);
		if (inputState.guessing==0) {
			id_AST = astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
		}
		match(IDENT);
		}
		{
		if ((LA(1)==LITERAL_extends) && (LA(2)==IDENT||LA(2)==NLS) && (_tokenSet_51.member(LA(3)))) {
			typeParameterBounds();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((_tokenSet_52.member(LA(1))) && (_tokenSet_53.member(LA(2))) && (_tokenSet_54.member(LA(3)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		if ( inputState.guessing==0 ) {
			typeParameter_AST = (AST)currentAST.root;
			typeParameter_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE_PARAMETER,"TYPE_PARAMETER")).add(typeParameter_AST));
			currentAST.root = typeParameter_AST;
			currentAST.child = typeParameter_AST!=null &&typeParameter_AST.getFirstChild()!=null ?
				typeParameter_AST.getFirstChild() : typeParameter_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			typeParameter_AST = (AST)currentAST.root;
		}
		returnAST = typeParameter_AST;
	}

	public final void typeParameterBounds() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeParameterBounds_AST = null;

		AST tmp128_AST = null;
		if (inputState.guessing==0) {
			tmp128_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp128_AST);
		}
		match(LITERAL_extends);
		nls();
		classOrInterfaceType(false);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop104:
		do {
			if ((LA(1)==BAND)) {
				match(BAND);
				nls();
				classOrInterfaceType(false);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop104;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			typeParameterBounds_AST = (AST)currentAST.root;
			typeParameterBounds_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE_UPPER_BOUNDS,"TYPE_UPPER_BOUNDS")).add(typeParameterBounds_AST));
			currentAST.root = typeParameterBounds_AST;
			currentAST.child = typeParameterBounds_AST!=null &&typeParameterBounds_AST.getFirstChild()!=null ?
				typeParameterBounds_AST.getFirstChild() : typeParameterBounds_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			typeParameterBounds_AST = (AST)currentAST.root;
		}
		returnAST = typeParameterBounds_AST;
	}

	public final void classField() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classField_AST = null;
		AST mc_AST = null;
		AST ctor_AST = null;
		AST d_AST = null;
		AST mods_AST = null;
		AST td_AST = null;
		AST s3_AST = null;
		AST s4_AST = null;

		boolean synPredMatched166 = false;
		if (((_tokenSet_55.member(LA(1))) && (_tokenSet_56.member(LA(2))) && (_tokenSet_57.member(LA(3))))) {
			int _m166 = mark();
			synPredMatched166 = true;
			inputState.guessing++;
			try {
				{
				constructorStart();
				}
			}
			catch (RecognitionException pe) {
				synPredMatched166 = false;
			}
			rewind(_m166);
			inputState.guessing--;
		}
		if ( synPredMatched166 ) {
			modifiersOpt();
			if (inputState.guessing==0) {
				mc_AST = (AST)returnAST;
			}
			constructorDefinition(mc_AST);
			if (inputState.guessing==0) {
				ctor_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				classField_AST = (AST)currentAST.root;
				classField_AST = ctor_AST;
				currentAST.root = classField_AST;
				currentAST.child = classField_AST!=null &&classField_AST.getFirstChild()!=null ?
					classField_AST.getFirstChild() : classField_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				classField_AST = (AST)currentAST.root;
			}
		}
		else {
			boolean synPredMatched168 = false;
			if (((_tokenSet_12.member(LA(1))) && (_tokenSet_13.member(LA(2))) && (_tokenSet_58.member(LA(3))))) {
				int _m168 = mark();
				synPredMatched168 = true;
				inputState.guessing++;
				try {
					{
					declarationStart();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched168 = false;
				}
				rewind(_m168);
				inputState.guessing--;
			}
			if ( synPredMatched168 ) {
				declaration();
				if (inputState.guessing==0) {
					d_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
				if ( inputState.guessing==0 ) {
					classField_AST = (AST)currentAST.root;
					classField_AST = d_AST;
					currentAST.root = classField_AST;
					currentAST.child = classField_AST!=null &&classField_AST.getFirstChild()!=null ?
						classField_AST.getFirstChild() : classField_AST;
					currentAST.advanceChildToEnd();
				}
				if ( inputState.guessing==0 ) {
					classField_AST = (AST)currentAST.root;
				}
			}
			else {
				boolean synPredMatched170 = false;
				if (((_tokenSet_21.member(LA(1))) && (_tokenSet_22.member(LA(2))) && (_tokenSet_23.member(LA(3))))) {
					int _m170 = mark();
					synPredMatched170 = true;
					inputState.guessing++;
					try {
						{
						typeDeclarationStart();
						}
					}
					catch (RecognitionException pe) {
						synPredMatched170 = false;
					}
					rewind(_m170);
					inputState.guessing--;
				}
				if ( synPredMatched170 ) {
					modifiersOpt();
					if (inputState.guessing==0) {
						mods_AST = (AST)returnAST;
					}
					{
					typeDefinitionInternal(mods_AST);
					if (inputState.guessing==0) {
						td_AST = (AST)returnAST;
						astFactory.addASTChild(currentAST, returnAST);
					}
					if ( inputState.guessing==0 ) {
						classField_AST = (AST)currentAST.root;
						classField_AST = td_AST;
						currentAST.root = classField_AST;
						currentAST.child = classField_AST!=null &&classField_AST.getFirstChild()!=null ?
							classField_AST.getFirstChild() : classField_AST;
						currentAST.advanceChildToEnd();
					}
					}
					if ( inputState.guessing==0 ) {
						classField_AST = (AST)currentAST.root;
					}
				}
				else if ((LA(1)==LITERAL_static) && (LA(2)==LCURLY)) {
					AST tmp130_AST = null;
					if (inputState.guessing==0) {
						tmp130_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp130_AST);
					}
					match(LITERAL_static);
					compoundStatement();
					if (inputState.guessing==0) {
						s3_AST = (AST)returnAST;
						astFactory.addASTChild(currentAST, returnAST);
					}
					if ( inputState.guessing==0 ) {
						classField_AST = (AST)currentAST.root;
						classField_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(STATIC_INIT,"STATIC_INIT")).add(s3_AST));
						currentAST.root = classField_AST;
						currentAST.child = classField_AST!=null &&classField_AST.getFirstChild()!=null ?
							classField_AST.getFirstChild() : classField_AST;
						currentAST.advanceChildToEnd();
					}
					if ( inputState.guessing==0 ) {
						classField_AST = (AST)currentAST.root;
					}
				}
				else if ((LA(1)==LCURLY)) {
					compoundStatement();
					if (inputState.guessing==0) {
						s4_AST = (AST)returnAST;
						astFactory.addASTChild(currentAST, returnAST);
					}
					if ( inputState.guessing==0 ) {
						classField_AST = (AST)currentAST.root;
						classField_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(INSTANCE_INIT,"INSTANCE_INIT")).add(s4_AST));
						currentAST.root = classField_AST;
						currentAST.child = classField_AST!=null &&classField_AST.getFirstChild()!=null ?
							classField_AST.getFirstChild() : classField_AST;
						currentAST.advanceChildToEnd();
					}
					if ( inputState.guessing==0 ) {
						classField_AST = (AST)currentAST.root;
					}
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}}
				returnAST = classField_AST;
			}

	public final void interfaceField() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST interfaceField_AST = null;
		AST d_AST = null;
		AST mods_AST = null;
		AST td_AST = null;

		boolean synPredMatched174 = false;
		if (((_tokenSet_12.member(LA(1))) && (_tokenSet_13.member(LA(2))) && (_tokenSet_58.member(LA(3))))) {
			int _m174 = mark();
			synPredMatched174 = true;
			inputState.guessing++;
			try {
				{
				declarationStart();
				}
			}
			catch (RecognitionException pe) {
				synPredMatched174 = false;
			}
			rewind(_m174);
			inputState.guessing--;
		}
		if ( synPredMatched174 ) {
			declaration();
			if (inputState.guessing==0) {
				d_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				interfaceField_AST = (AST)currentAST.root;
				interfaceField_AST = d_AST;
				currentAST.root = interfaceField_AST;
				currentAST.child = interfaceField_AST!=null &&interfaceField_AST.getFirstChild()!=null ?
					interfaceField_AST.getFirstChild() : interfaceField_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				interfaceField_AST = (AST)currentAST.root;
			}
		}
		else {
			boolean synPredMatched176 = false;
			if (((_tokenSet_21.member(LA(1))) && (_tokenSet_22.member(LA(2))) && (_tokenSet_23.member(LA(3))))) {
				int _m176 = mark();
				synPredMatched176 = true;
				inputState.guessing++;
				try {
					{
					typeDeclarationStart();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched176 = false;
				}
				rewind(_m176);
				inputState.guessing--;
			}
			if ( synPredMatched176 ) {
				modifiersOpt();
				if (inputState.guessing==0) {
					mods_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
				{
				typeDefinitionInternal(mods_AST);
				if (inputState.guessing==0) {
					td_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
				if ( inputState.guessing==0 ) {
					interfaceField_AST = (AST)currentAST.root;
					interfaceField_AST = td_AST;
					currentAST.root = interfaceField_AST;
					currentAST.child = interfaceField_AST!=null &&interfaceField_AST.getFirstChild()!=null ?
						interfaceField_AST.getFirstChild() : interfaceField_AST;
					currentAST.advanceChildToEnd();
				}
				}
				if ( inputState.guessing==0 ) {
					interfaceField_AST = (AST)currentAST.root;
				}
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			returnAST = interfaceField_AST;
		}

	public final void annotationField() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationField_AST = null;
		AST mods_AST = null;
		AST td_AST = null;
		AST t_AST = null;
		Token  i = null;
		AST i_AST = null;
		AST amvi_AST = null;
		AST v_AST = null;

		modifiersOpt();
		if (inputState.guessing==0) {
			mods_AST = (AST)returnAST;
		}
		{
		switch ( LA(1)) {
		case AT:
		case LITERAL_class:
		case LITERAL_interface:
		case LITERAL_enum:
		{
			typeDefinitionInternal(mods_AST);
			if (inputState.guessing==0) {
				td_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				annotationField_AST = (AST)currentAST.root;
				annotationField_AST = td_AST;
				currentAST.root = annotationField_AST;
				currentAST.child = annotationField_AST!=null &&annotationField_AST.getFirstChild()!=null ?
					annotationField_AST.getFirstChild() : annotationField_AST;
				currentAST.advanceChildToEnd();
			}
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
			typeSpec(false);
			if (inputState.guessing==0) {
				t_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			boolean synPredMatched138 = false;
			if (((LA(1)==IDENT) && (LA(2)==LPAREN) && (LA(3)==RPAREN))) {
				int _m138 = mark();
				synPredMatched138 = true;
				inputState.guessing++;
				try {
					{
					match(IDENT);
					match(LPAREN);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched138 = false;
				}
				rewind(_m138);
				inputState.guessing--;
			}
			if ( synPredMatched138 ) {
				i = LT(1);
				if (inputState.guessing==0) {
					i_AST = astFactory.create(i);
					astFactory.addASTChild(currentAST, i_AST);
				}
				match(IDENT);
				match(LPAREN);
				match(RPAREN);
				{
				switch ( LA(1)) {
				case LITERAL_default:
				{
					AST tmp133_AST = null;
					if (inputState.guessing==0) {
						tmp133_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp133_AST);
					}
					match(LITERAL_default);
					nls();
					annotationMemberValueInitializer();
					if (inputState.guessing==0) {
						amvi_AST = (AST)returnAST;
						astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case RCURLY:
				case SEMI:
				case NLS:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					annotationField_AST = (AST)currentAST.root;
					annotationField_AST =
					(AST)astFactory.make( (new ASTArray(5)).add(astFactory.create(ANNOTATION_FIELD_DEF,"ANNOTATION_FIELD_DEF")).add(mods_AST).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(t_AST))).add(i_AST).add(amvi_AST));
					currentAST.root = annotationField_AST;
					currentAST.child = annotationField_AST!=null &&annotationField_AST.getFirstChild()!=null ?
						annotationField_AST.getFirstChild() : annotationField_AST;
					currentAST.advanceChildToEnd();
				}
			}
			else if ((LA(1)==IDENT||LA(1)==STRING_LITERAL) && (_tokenSet_59.member(LA(2))) && (_tokenSet_60.member(LA(3)))) {
				variableDefinitions(mods_AST,t_AST);
				if (inputState.guessing==0) {
					v_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
				if ( inputState.guessing==0 ) {
					annotationField_AST = (AST)currentAST.root;
					annotationField_AST = v_AST;
					currentAST.root = annotationField_AST;
					currentAST.child = annotationField_AST!=null &&annotationField_AST.getFirstChild()!=null ?
						annotationField_AST.getFirstChild() : annotationField_AST;
					currentAST.advanceChildToEnd();
				}
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}

			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			annotationField_AST = (AST)currentAST.root;
		}
		returnAST = annotationField_AST;
	}

/** Guard for enumConstants.  */
	public final void enumConstantsStart() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumConstantsStart_AST = null;

		enumConstant();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		switch ( LA(1)) {
		case COMMA:
		{
			AST tmp134_AST = null;
			if (inputState.guessing==0) {
				tmp134_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp134_AST);
			}
			match(COMMA);
			break;
		}
		case SEMI:
		{
			AST tmp135_AST = null;
			if (inputState.guessing==0) {
				tmp135_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp135_AST);
			}
			match(SEMI);
			break;
		}
		case NLS:
		{
			AST tmp136_AST = null;
			if (inputState.guessing==0) {
				tmp136_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp136_AST);
			}
			match(NLS);
			break;
		}
		case RCURLY:
		{
			AST tmp137_AST = null;
			if (inputState.guessing==0) {
				tmp137_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp137_AST);
			}
			match(RCURLY);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			enumConstantsStart_AST = (AST)currentAST.root;
		}
		returnAST = enumConstantsStart_AST;
	}

/** Comma-separated list of one or more enum constant definitions.  */
	public final void enumConstants() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumConstants_AST = null;

		enumConstant();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop132:
		do {
			if ((LA(1)==COMMA) && (_tokenSet_61.member(LA(2))) && (_tokenSet_62.member(LA(3)))) {
				match(COMMA);
				nls();
				enumConstant();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop132;
			}

		} while (true);
		}
		{
		switch ( LA(1)) {
		case COMMA:
		{
			match(COMMA);
			nls();
			break;
		}
		case RCURLY:
		case SEMI:
		case NLS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			enumConstants_AST = (AST)currentAST.root;
		}
		returnAST = enumConstants_AST;
	}

	public final void enumConstant() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumConstant_AST = null;
		AST an_AST = null;
		Token  i = null;
		AST i_AST = null;
		AST a_AST = null;
		AST b_AST = null;

		annotationsOpt();
		if (inputState.guessing==0) {
			an_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		i = LT(1);
		if (inputState.guessing==0) {
			i_AST = astFactory.create(i);
			astFactory.addASTChild(currentAST, i_AST);
		}
		match(IDENT);
		{
		switch ( LA(1)) {
		case LPAREN:
		{
			match(LPAREN);
			argList();
			if (inputState.guessing==0) {
				a_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			match(RPAREN);
			break;
		}
		case COMMA:
		case LCURLY:
		case RCURLY:
		case SEMI:
		case NLS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case LCURLY:
		{
			enumConstantBlock();
			if (inputState.guessing==0) {
				b_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case COMMA:
		case RCURLY:
		case SEMI:
		case NLS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			enumConstant_AST = (AST)currentAST.root;
			enumConstant_AST = (AST)astFactory.make( (new ASTArray(5)).add(astFactory.create(ENUM_CONSTANT_DEF,"ENUM_CONSTANT_DEF")).add(an_AST).add(i_AST).add(a_AST).add(b_AST));
			currentAST.root = enumConstant_AST;
			currentAST.child = enumConstant_AST!=null &&enumConstant_AST.getFirstChild()!=null ?
				enumConstant_AST.getFirstChild() : enumConstant_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			enumConstant_AST = (AST)currentAST.root;
		}
		returnAST = enumConstant_AST;
	}

	public final void argList() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST argList_AST = null;
		boolean hl = false, hl2;

		{
		switch ( LA(1)) {
		case FINAL:
		case ABSTRACT:
		case UNUSED_DO:
		case STRICTFP:
		case LITERAL_static:
		case LITERAL_def:
		case AT:
		case IDENT:
		case LBRACK:
		case LPAREN:
		case LITERAL_class:
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
		case STAR:
		case LITERAL_as:
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case BAND:
		case LCURLY:
		case LITERAL_this:
		case STRING_LITERAL:
		case LITERAL_if:
		case LITERAL_else:
		case LITERAL_while:
		case LITERAL_switch:
		case LITERAL_for:
		case LITERAL_in:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		case PLUS:
		case MINUS:
		case LITERAL_try:
		case LITERAL_finally:
		case LITERAL_catch:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case DOLLAR:
		case STRING_CTOR_START:
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
			hl=argument();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			_loop451:
			do {
				if ((LA(1)==COMMA) && (_tokenSet_63.member(LA(2))) && (_tokenSet_64.member(LA(3)))) {
					match(COMMA);
					hl2=argument();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					if ( inputState.guessing==0 ) {
						hl |= hl2;
					}
				}
				else {
					break _loop451;
				}

			} while (true);
			}
			if ( inputState.guessing==0 ) {
				argList_AST = (AST)currentAST.root;
				argList_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(ELIST,"ELIST")).add(argList_AST));
				currentAST.root = argList_AST;
				currentAST.child = argList_AST!=null &&argList_AST.getFirstChild()!=null ?
					argList_AST.getFirstChild() : argList_AST;
				currentAST.advanceChildToEnd();
			}
			break;
		}
		case RBRACK:
		case COMMA:
		case RPAREN:
		{
			if ( inputState.guessing==0 ) {
				argList_AST = (AST)currentAST.root;
				argList_AST = astFactory.create(ELIST,"ELIST");
				currentAST.root = argList_AST;
				currentAST.child = argList_AST!=null &&argList_AST.getFirstChild()!=null ?
					argList_AST.getFirstChild() : argList_AST;
				currentAST.advanceChildToEnd();
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case COMMA:
		{
			match(COMMA);
			break;
		}
		case RBRACK:
		case RPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			argListHasLabels = hl;
		}
		if ( inputState.guessing==0 ) {
			argList_AST = (AST)currentAST.root;
		}
		returnAST = argList_AST;
	}

	public final void enumConstantBlock() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumConstantBlock_AST = null;

		match(LCURLY);
		{
		switch ( LA(1)) {
		case FINAL:
		case ABSTRACT:
		case STRICTFP:
		case LITERAL_static:
		case LITERAL_def:
		case AT:
		case IDENT:
		case LITERAL_class:
		case LITERAL_interface:
		case LITERAL_enum:
		case LT:
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
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case LCURLY:
		{
			enumConstantField();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case RCURLY:
		case SEMI:
		case NLS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		_loop147:
		do {
			if ((LA(1)==SEMI||LA(1)==NLS)) {
				sep();
				{
				switch ( LA(1)) {
				case FINAL:
				case ABSTRACT:
				case STRICTFP:
				case LITERAL_static:
				case LITERAL_def:
				case AT:
				case IDENT:
				case LITERAL_class:
				case LITERAL_interface:
				case LITERAL_enum:
				case LT:
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
				case LITERAL_private:
				case LITERAL_public:
				case LITERAL_protected:
				case LITERAL_transient:
				case LITERAL_native:
				case LITERAL_threadsafe:
				case LITERAL_synchronized:
				case LITERAL_volatile:
				case LCURLY:
				{
					enumConstantField();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case RCURLY:
				case SEMI:
				case NLS:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else {
				break _loop147;
			}

		} while (true);
		}
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			enumConstantBlock_AST = (AST)currentAST.root;
			enumConstantBlock_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(OBJBLOCK,"OBJBLOCK")).add(enumConstantBlock_AST));
			currentAST.root = enumConstantBlock_AST;
			currentAST.child = enumConstantBlock_AST!=null &&enumConstantBlock_AST.getFirstChild()!=null ?
				enumConstantBlock_AST.getFirstChild() : enumConstantBlock_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			enumConstantBlock_AST = (AST)currentAST.root;
		}
		returnAST = enumConstantBlock_AST;
	}

	public final void enumConstantField() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumConstantField_AST = null;
		AST mods_AST = null;
		AST td_AST = null;
		AST tp_AST = null;
		AST t_AST = null;
		AST param_AST = null;
		AST tc_AST = null;
		AST s2_AST = null;
		AST v_AST = null;
		AST s4_AST = null;

		switch ( LA(1)) {
		case FINAL:
		case ABSTRACT:
		case STRICTFP:
		case LITERAL_static:
		case LITERAL_def:
		case AT:
		case IDENT:
		case LITERAL_class:
		case LITERAL_interface:
		case LITERAL_enum:
		case LT:
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
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		{
			modifiersOpt();
			if (inputState.guessing==0) {
				mods_AST = (AST)returnAST;
			}
			{
			switch ( LA(1)) {
			case AT:
			case LITERAL_class:
			case LITERAL_interface:
			case LITERAL_enum:
			{
				typeDefinitionInternal(mods_AST);
				if (inputState.guessing==0) {
					td_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
				if ( inputState.guessing==0 ) {
					enumConstantField_AST = (AST)currentAST.root;
					enumConstantField_AST = td_AST;
					currentAST.root = enumConstantField_AST;
					currentAST.child = enumConstantField_AST!=null &&enumConstantField_AST.getFirstChild()!=null ?
						enumConstantField_AST.getFirstChild() : enumConstantField_AST;
					currentAST.advanceChildToEnd();
				}
				break;
			}
			case IDENT:
			case LT:
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
				switch ( LA(1)) {
				case LT:
				{
					typeParameters();
					if (inputState.guessing==0) {
						tp_AST = (AST)returnAST;
						astFactory.addASTChild(currentAST, returnAST);
					}
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
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				typeSpec(false);
				if (inputState.guessing==0) {
					t_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
				{
				boolean synPredMatched153 = false;
				if (((LA(1)==IDENT) && (LA(2)==LPAREN) && (_tokenSet_65.member(LA(3))))) {
					int _m153 = mark();
					synPredMatched153 = true;
					inputState.guessing++;
					try {
						{
						match(IDENT);
						match(LPAREN);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched153 = false;
					}
					rewind(_m153);
					inputState.guessing--;
				}
				if ( synPredMatched153 ) {
					AST tmp146_AST = null;
					if (inputState.guessing==0) {
						tmp146_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp146_AST);
					}
					match(IDENT);
					match(LPAREN);
					parameterDeclarationList();
					if (inputState.guessing==0) {
						param_AST = (AST)returnAST;
						astFactory.addASTChild(currentAST, returnAST);
					}
					match(RPAREN);
					{
					switch ( LA(1)) {
					case LITERAL_throws:
					{
						throwsClause();
						if (inputState.guessing==0) {
							tc_AST = (AST)returnAST;
							astFactory.addASTChild(currentAST, returnAST);
						}
						break;
					}
					case LCURLY:
					case RCURLY:
					case SEMI:
					case NLS:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					{
					switch ( LA(1)) {
					case LCURLY:
					{
						compoundStatement();
						if (inputState.guessing==0) {
							s2_AST = (AST)returnAST;
							astFactory.addASTChild(currentAST, returnAST);
						}
						break;
					}
					case RCURLY:
					case SEMI:
					case NLS:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					if ( inputState.guessing==0 ) {
						enumConstantField_AST = (AST)currentAST.root;
						enumConstantField_AST = (AST)astFactory.make( (new ASTArray(8)).add(astFactory.create(METHOD_DEF,"METHOD_DEF")).add(mods_AST).add(tp_AST).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(t_AST))).add(tmp146_AST).add(param_AST).add(tc_AST).add(s2_AST));
						currentAST.root = enumConstantField_AST;
						currentAST.child = enumConstantField_AST!=null &&enumConstantField_AST.getFirstChild()!=null ?
							enumConstantField_AST.getFirstChild() : enumConstantField_AST;
						currentAST.advanceChildToEnd();
					}
				}
				else if ((LA(1)==IDENT||LA(1)==STRING_LITERAL) && (_tokenSet_59.member(LA(2))) && (_tokenSet_66.member(LA(3)))) {
					variableDefinitions(mods_AST,t_AST);
					if (inputState.guessing==0) {
						v_AST = (AST)returnAST;
						astFactory.addASTChild(currentAST, returnAST);
					}
					if ( inputState.guessing==0 ) {
						enumConstantField_AST = (AST)currentAST.root;
						enumConstantField_AST = v_AST;
						currentAST.root = enumConstantField_AST;
						currentAST.child = enumConstantField_AST!=null &&enumConstantField_AST.getFirstChild()!=null ?
							enumConstantField_AST.getFirstChild() : enumConstantField_AST;
						currentAST.advanceChildToEnd();
					}
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}

				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				enumConstantField_AST = (AST)currentAST.root;
			}
			break;
		}
		case LCURLY:
		{
			compoundStatement();
			if (inputState.guessing==0) {
				s4_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				enumConstantField_AST = (AST)currentAST.root;
				enumConstantField_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(INSTANCE_INIT,"INSTANCE_INIT")).add(s4_AST));
				currentAST.root = enumConstantField_AST;
				currentAST.child = enumConstantField_AST!=null &&enumConstantField_AST.getFirstChild()!=null ?
					enumConstantField_AST.getFirstChild() : enumConstantField_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				enumConstantField_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = enumConstantField_AST;
	}

/** A list of zero or more formal parameters.
 *  If a parameter is variable length (e.g. String... myArg) it should be
 *  to the right of any other parameters of the same kind.
 *  General form:  (req, ..., opt, ..., [rest], key, ..., [restKeys], [block]
 *  This must be sorted out after parsing, since the various declaration forms
 *  are impossible to tell apart without backtracking.
 */
	public final void parameterDeclarationList() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parameterDeclarationList_AST = null;

		{
		switch ( LA(1)) {
		case FINAL:
		case LITERAL_def:
		case AT:
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
		case TRIPLE_DOT:
		{
			parameterDeclaration();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			_loop208:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					nls();
					parameterDeclaration();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
				}
				else {
					break _loop208;
				}

			} while (true);
			}
			break;
		}
		case RPAREN:
		case NLS:
		case CLOSURE_OP:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			parameterDeclarationList_AST = (AST)currentAST.root;
			parameterDeclarationList_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(PARAMETERS,"PARAMETERS")).add(parameterDeclarationList_AST));
			currentAST.root = parameterDeclarationList_AST;
			currentAST.child = parameterDeclarationList_AST!=null &&parameterDeclarationList_AST.getFirstChild()!=null ?
				parameterDeclarationList_AST.getFirstChild() : parameterDeclarationList_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			parameterDeclarationList_AST = (AST)currentAST.root;
		}
		returnAST = parameterDeclarationList_AST;
	}

	public final void throwsClause() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST throwsClause_AST = null;

		AST tmp150_AST = null;
		if (inputState.guessing==0) {
			tmp150_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp150_AST);
		}
		match(LITERAL_throws);
		nls();
		identifier();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop204:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				nls();
				identifier();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop204;
			}

		} while (true);
		}
		nls();
		if ( inputState.guessing==0 ) {
			throwsClause_AST = (AST)currentAST.root;
		}
		returnAST = throwsClause_AST;
	}

	public final void compoundStatement() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST compoundStatement_AST = null;

		openBlock();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			compoundStatement_AST = (AST)currentAST.root;
		}
		returnAST = compoundStatement_AST;
	}

/** I've split out constructors separately; we could maybe integrate back into variableDefinitions
 *  later on if we maybe simplified 'def' to be a type declaration?
 */
	public final void constructorDefinition(
		AST mods
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constructorDefinition_AST = null;
		Token  id = null;
		AST id_AST = null;
		AST param_AST = null;
		AST tc_AST = null;
		AST cb_AST = null;

		id = LT(1);
		if (inputState.guessing==0) {
			id_AST = astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
		}
		match(IDENT);
		match(LPAREN);
		parameterDeclarationList();
		if (inputState.guessing==0) {
			param_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		match(RPAREN);
		{
		switch ( LA(1)) {
		case LITERAL_throws:
		{
			throwsClause();
			if (inputState.guessing==0) {
				tc_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case LCURLY:
		case NLS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		nlsWarn();
		if ( inputState.guessing==0 ) {
			isConstructorIdent(id);
		}
		constructorBody();
		if (inputState.guessing==0) {
			cb_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			constructorDefinition_AST = (AST)currentAST.root;
			constructorDefinition_AST =  (AST)astFactory.make( (new ASTArray(5)).add(astFactory.create(CTOR_IDENT,"CTOR_IDENT")).add(mods).add(param_AST).add(tc_AST).add(cb_AST));

			currentAST.root = constructorDefinition_AST;
			currentAST.child = constructorDefinition_AST!=null &&constructorDefinition_AST.getFirstChild()!=null ?
				constructorDefinition_AST.getFirstChild() : constructorDefinition_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			constructorDefinition_AST = (AST)currentAST.root;
		}
		returnAST = constructorDefinition_AST;
	}

	public final void constructorBody() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constructorBody_AST = null;

		match(LCURLY);
		if ( inputState.guessing==0 ) {
			astFactory.makeASTRoot(currentAST, astFactory.create(SLIST, "{"));
		}
		nls();
		{
		boolean synPredMatched181 = false;
		if (((_tokenSet_67.member(LA(1))) && (_tokenSet_68.member(LA(2))) && (_tokenSet_69.member(LA(3))))) {
			int _m181 = mark();
			synPredMatched181 = true;
			inputState.guessing++;
			try {
				{
				explicitConstructorInvocation();
				}
			}
			catch (RecognitionException pe) {
				synPredMatched181 = false;
			}
			rewind(_m181);
			inputState.guessing--;
		}
		if ( synPredMatched181 ) {
			explicitConstructorInvocation();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			switch ( LA(1)) {
			case SEMI:
			case NLS:
			{
				sep();
				blockBody(sepToken);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				break;
			}
			case RCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		else if ((_tokenSet_70.member(LA(1))) && (_tokenSet_71.member(LA(2))) && (_tokenSet_18.member(LA(3)))) {
			blockBody(EOF);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			constructorBody_AST = (AST)currentAST.root;
		}
		returnAST = constructorBody_AST;
	}

/** Catch obvious constructor calls, but not the expr.super(...) calls */
	public final void explicitConstructorInvocation() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST explicitConstructorInvocation_AST = null;

		{
		switch ( LA(1)) {
		case LT:
		{
			typeArguments();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case LITERAL_super:
		case LITERAL_this:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case LITERAL_this:
		{
			match(LITERAL_this);
			match(LPAREN);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(CTOR_CALL, "("));
			}
			argList();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			match(RPAREN);
			break;
		}
		case LITERAL_super:
		{
			match(LITERAL_super);
			match(LPAREN);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(SUPER_CTOR_CALL, "("));
			}
			argList();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			match(RPAREN);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			explicitConstructorInvocation_AST = (AST)currentAST.root;
		}
		returnAST = explicitConstructorInvocation_AST;
	}

/** Declaration of a variable. This can be a class/instance variable,
 *  or a local variable in a method
 *  It can also include possible initialization.
 */
	public final void variableDeclarator(
		AST mods, AST t
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST variableDeclarator_AST = null;
		AST id_AST = null;
		AST v_AST = null;

		variableName();
		if (inputState.guessing==0) {
			id_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		switch ( LA(1)) {
		case ASSIGN:
		{
			varInitializer();
			if (inputState.guessing==0) {
				v_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case EOF:
		case COMMA:
		case RCURLY:
		case SEMI:
		case NLS:
		case LITERAL_default:
		case LITERAL_else:
		case LITERAL_case:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			variableDeclarator_AST = (AST)currentAST.root;
			variableDeclarator_AST = (AST)astFactory.make( (new ASTArray(5)).add(astFactory.create(VARIABLE_DEF,"VARIABLE_DEF")).add(mods).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(t))).add(id_AST).add(v_AST));
			currentAST.root = variableDeclarator_AST;
			currentAST.child = variableDeclarator_AST!=null &&variableDeclarator_AST.getFirstChild()!=null ?
				variableDeclarator_AST.getFirstChild() : variableDeclarator_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			variableDeclarator_AST = (AST)currentAST.root;
		}
		returnAST = variableDeclarator_AST;
	}

/** Zero or more insignificant newlines, all gobbled up and thrown away,
 *  but a warning message is left for the user, if there was a newline.
 */
	public final void nlsWarn() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST nlsWarn_AST = null;

		{
		boolean synPredMatched490 = false;
		if (((_tokenSet_72.member(LA(1))) && (_tokenSet_20.member(LA(2))) && (_tokenSet_5.member(LA(3))))) {
			int _m490 = mark();
			synPredMatched490 = true;
			inputState.guessing++;
			try {
				{
				match(NLS);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched490 = false;
			}
			rewind(_m490);
			inputState.guessing--;
		}
		if ( synPredMatched490 ) {
			if ( inputState.guessing==0 ) {
				addWarning(
				"A newline at this point does not follow the Groovy Coding Conventions.",
				"Keep this statement on one line, or use curly braces to break across multiple lines."
				);
			}
		}
		else if ((_tokenSet_72.member(LA(1))) && (_tokenSet_20.member(LA(2))) && (_tokenSet_5.member(LA(3)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		nls();
		returnAST = nlsWarn_AST;
	}

/** An open block is not allowed to have closure arguments. */
	public final void openBlock() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST openBlock_AST = null;

		match(LCURLY);
		if ( inputState.guessing==0 ) {
			astFactory.makeASTRoot(currentAST, astFactory.create(SLIST, "{"));
		}
		nls();
		blockBody(EOF);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			openBlock_AST = (AST)currentAST.root;
		}
		returnAST = openBlock_AST;
	}

	public final void variableName() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST variableName_AST = null;

		AST tmp164_AST = null;
		if (inputState.guessing==0) {
			tmp164_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp164_AST);
		}
		match(IDENT);
		if ( inputState.guessing==0 ) {
			variableName_AST = (AST)currentAST.root;
		}
		returnAST = variableName_AST;
	}

	public final void expression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expression_AST = null;

		assignmentExpression(lc_stmt);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			expression_AST = (AST)currentAST.root;
		}
		returnAST = expression_AST;
	}

/** A formal parameter for a method or closure. */
	public final void parameterDeclaration() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parameterDeclaration_AST = null;
		AST pm_AST = null;
		AST t_AST = null;
		Token  id = null;
		AST id_AST = null;
		AST exp_AST = null;
		boolean spreadParam = false;

		parameterModifiersOpt();
		if (inputState.guessing==0) {
			pm_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		if ((_tokenSet_26.member(LA(1))) && (_tokenSet_73.member(LA(2))) && (_tokenSet_74.member(LA(3)))) {
			typeSpec(false);
			if (inputState.guessing==0) {
				t_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((LA(1)==IDENT||LA(1)==TRIPLE_DOT) && (_tokenSet_75.member(LA(2))) && (_tokenSet_76.member(LA(3)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		{
		switch ( LA(1)) {
		case TRIPLE_DOT:
		{
			AST tmp165_AST = null;
			if (inputState.guessing==0) {
				tmp165_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp165_AST);
			}
			match(TRIPLE_DOT);
			if ( inputState.guessing==0 ) {
				spreadParam = true;
			}
			break;
		}
		case IDENT:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		id = LT(1);
		if (inputState.guessing==0) {
			id_AST = astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
		}
		match(IDENT);
		{
		switch ( LA(1)) {
		case ASSIGN:
		{
			varInitializer();
			if (inputState.guessing==0) {
				exp_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case COMMA:
		case RPAREN:
		case NLS:
		case CLOSURE_OP:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			parameterDeclaration_AST = (AST)currentAST.root;

			if (spreadParam) {
			parameterDeclaration_AST = (AST)astFactory.make( (new ASTArray(5)).add(astFactory.create(VARIABLE_PARAMETER_DEF,"VARIABLE_PARAMETER_DEF")).add(pm_AST).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(t_AST))).add(id_AST).add(exp_AST));
			} else {
			parameterDeclaration_AST = (AST)astFactory.make( (new ASTArray(5)).add(astFactory.create(PARAMETER_DEF,"PARAMETER_DEF")).add(pm_AST).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(t_AST))).add(id_AST).add(exp_AST));
			}

			currentAST.root = parameterDeclaration_AST;
			currentAST.child = parameterDeclaration_AST!=null &&parameterDeclaration_AST.getFirstChild()!=null ?
				parameterDeclaration_AST.getFirstChild() : parameterDeclaration_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			parameterDeclaration_AST = (AST)currentAST.root;
		}
		returnAST = parameterDeclaration_AST;
	}

	public final void parameterModifiersOpt() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parameterModifiersOpt_AST = null;
		int seenDef = 0;

		{
		_loop220:
		do {
			switch ( LA(1)) {
			case FINAL:
			{
				AST tmp166_AST = null;
				if (inputState.guessing==0) {
					tmp166_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp166_AST);
				}
				match(FINAL);
				nls();
				break;
			}
			case AT:
			{
				annotation();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				nls();
				break;
			}
			default:
				if (((LA(1)==LITERAL_def))&&(seenDef++ == 0)) {
					AST tmp167_AST = null;
					if (inputState.guessing==0) {
						tmp167_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp167_AST);
					}
					match(LITERAL_def);
					nls();
				}
			else {
				break _loop220;
			}
			}
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			parameterModifiersOpt_AST = (AST)currentAST.root;
			parameterModifiersOpt_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(MODIFIERS,"MODIFIERS")).add(parameterModifiersOpt_AST));
			currentAST.root = parameterModifiersOpt_AST;
			currentAST.child = parameterModifiersOpt_AST!=null &&parameterModifiersOpt_AST.getFirstChild()!=null ?
				parameterModifiersOpt_AST.getFirstChild() : parameterModifiersOpt_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			parameterModifiersOpt_AST = (AST)currentAST.root;
		}
		returnAST = parameterModifiersOpt_AST;
	}

/** A simplified formal parameter for closures, can occur outside parens.
 *  It is not confused by a lookahead of BOR.
 *  DECIDE:  Is thie necessary, or do we change the closure-bar syntax?
 */
	public final void simpleParameterDeclaration() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST simpleParameterDeclaration_AST = null;
		AST t_AST = null;
		Token  id = null;
		AST id_AST = null;

		{
		if ((_tokenSet_26.member(LA(1))) && (_tokenSet_30.member(LA(2)))) {
			typeSpec(false);
			if (inputState.guessing==0) {
				t_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((LA(1)==IDENT) && (_tokenSet_77.member(LA(2)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		id = LT(1);
		if (inputState.guessing==0) {
			id_AST = astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
		}
		match(IDENT);
		if ( inputState.guessing==0 ) {
			simpleParameterDeclaration_AST = (AST)currentAST.root;
			simpleParameterDeclaration_AST = (AST)astFactory.make( (new ASTArray(4)).add(astFactory.create(PARAMETER_DEF,"PARAMETER_DEF")).add((AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(MODIFIERS,"MODIFIERS")))).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(t_AST))).add(id_AST));
			currentAST.root = simpleParameterDeclaration_AST;
			currentAST.child = simpleParameterDeclaration_AST!=null &&simpleParameterDeclaration_AST.getFirstChild()!=null ?
				simpleParameterDeclaration_AST.getFirstChild() : simpleParameterDeclaration_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			simpleParameterDeclaration_AST = (AST)currentAST.root;
		}
		returnAST = simpleParameterDeclaration_AST;
	}

/** Simplified formal parameter list for closures.  Never empty. */
	public final void simpleParameterDeclarationList() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST simpleParameterDeclarationList_AST = null;

		simpleParameterDeclaration();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop217:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				nls();
				simpleParameterDeclaration();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop217;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			simpleParameterDeclarationList_AST = (AST)currentAST.root;
			simpleParameterDeclarationList_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(PARAMETERS,"PARAMETERS")).add(simpleParameterDeclarationList_AST));
			currentAST.root = simpleParameterDeclarationList_AST;
			currentAST.child = simpleParameterDeclarationList_AST!=null &&simpleParameterDeclarationList_AST.getFirstChild()!=null ?
				simpleParameterDeclarationList_AST.getFirstChild() : simpleParameterDeclarationList_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			simpleParameterDeclarationList_AST = (AST)currentAST.root;
		}
		returnAST = simpleParameterDeclarationList_AST;
	}

/** Closure parameters are exactly like method parameters,
 *  except that they are not enclosed in parentheses, but rather
 *  are prepended to the front of a block, just after the brace.
 *  They are separated from the closure body by a CLOSURE_OP token '->'.
 */
	public final void closureParametersOpt(
		boolean addImplicit
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST closureParametersOpt_AST = null;

		boolean synPredMatched223 = false;
		if (((_tokenSet_78.member(LA(1))) && (_tokenSet_79.member(LA(2))) && (_tokenSet_20.member(LA(3))))) {
			int _m223 = mark();
			synPredMatched223 = true;
			inputState.guessing++;
			try {
				{
				parameterDeclarationList();
				nls();
				match(CLOSURE_OP);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched223 = false;
			}
			rewind(_m223);
			inputState.guessing--;
		}
		if ( synPredMatched223 ) {
			parameterDeclarationList();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			nls();
			match(CLOSURE_OP);
			nls();
			if ( inputState.guessing==0 ) {
				closureParametersOpt_AST = (AST)currentAST.root;
			}
		}
		else {
			boolean synPredMatched225 = false;
			if ((((_tokenSet_80.member(LA(1))) && (_tokenSet_81.member(LA(2))) && (_tokenSet_20.member(LA(3))))&&(compatibilityMode))) {
				int _m225 = mark();
				synPredMatched225 = true;
				inputState.guessing++;
				try {
					{
					oldClosureParametersStart();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched225 = false;
				}
				rewind(_m225);
				inputState.guessing--;
			}
			if ( synPredMatched225 ) {
				oldClosureParameters();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				if ( inputState.guessing==0 ) {
					closureParametersOpt_AST = (AST)currentAST.root;
				}
			}
			else if (((_tokenSet_70.member(LA(1))) && (_tokenSet_20.member(LA(2))) && (_tokenSet_5.member(LA(3))))&&(addImplicit)) {
				implicitParameters();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				if ( inputState.guessing==0 ) {
					closureParametersOpt_AST = (AST)currentAST.root;
				}
			}
			else if ((_tokenSet_70.member(LA(1))) && (_tokenSet_20.member(LA(2))) && (_tokenSet_5.member(LA(3)))) {
				if ( inputState.guessing==0 ) {
					closureParametersOpt_AST = (AST)currentAST.root;
				}
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			returnAST = closureParametersOpt_AST;
		}

/** Lookahead for oldClosureParameters. */
	public final void oldClosureParametersStart() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST oldClosureParametersStart_AST = null;

		switch ( LA(1)) {
		case BOR:
		{
			AST tmp170_AST = null;
			if (inputState.guessing==0) {
				tmp170_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp170_AST);
			}
			match(BOR);
			if ( inputState.guessing==0 ) {
				oldClosureParametersStart_AST = (AST)currentAST.root;
			}
			break;
		}
		case LOR:
		{
			AST tmp171_AST = null;
			if (inputState.guessing==0) {
				tmp171_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp171_AST);
			}
			match(LOR);
			if ( inputState.guessing==0 ) {
				oldClosureParametersStart_AST = (AST)currentAST.root;
			}
			break;
		}
		case LPAREN:
		{
			AST tmp172_AST = null;
			if (inputState.guessing==0) {
				tmp172_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp172_AST);
			}
			match(LPAREN);
			balancedTokens();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp173_AST = null;
			if (inputState.guessing==0) {
				tmp173_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp173_AST);
			}
			match(RPAREN);
			nls();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp174_AST = null;
			if (inputState.guessing==0) {
				tmp174_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp174_AST);
			}
			match(BOR);
			if ( inputState.guessing==0 ) {
				oldClosureParametersStart_AST = (AST)currentAST.root;
			}
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
			simpleParameterDeclarationList();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp175_AST = null;
			if (inputState.guessing==0) {
				tmp175_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp175_AST);
			}
			match(BOR);
			if ( inputState.guessing==0 ) {
				oldClosureParametersStart_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = oldClosureParametersStart_AST;
	}

/** Provisional definition of old-style closure params based on BOR '|'.
 *  Going away soon, perhaps... */
	public final void oldClosureParameters() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST oldClosureParameters_AST = null;

		if ((LA(1)==LOR)) {
			AST tmp176_AST = null;
			if (inputState.guessing==0) {
				tmp176_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp176_AST);
			}
			match(LOR);
			nls();
			if ( inputState.guessing==0 ) {
				oldClosureParameters_AST = (AST)currentAST.root;
				oldClosureParameters_AST = (AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(PARAMETERS,"PARAMETERS")));
				currentAST.root = oldClosureParameters_AST;
				currentAST.child = oldClosureParameters_AST!=null &&oldClosureParameters_AST.getFirstChild()!=null ?
					oldClosureParameters_AST.getFirstChild() : oldClosureParameters_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				oldClosureParameters_AST = (AST)currentAST.root;
			}
		}
		else {
			boolean synPredMatched231 = false;
			if (((LA(1)==BOR) && (LA(2)==NLS||LA(2)==BOR) && (_tokenSet_82.member(LA(3))))) {
				int _m231 = mark();
				synPredMatched231 = true;
				inputState.guessing++;
				try {
					{
					match(BOR);
					nls();
					match(BOR);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched231 = false;
				}
				rewind(_m231);
				inputState.guessing--;
			}
			if ( synPredMatched231 ) {
				AST tmp177_AST = null;
				if (inputState.guessing==0) {
					tmp177_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp177_AST);
				}
				match(BOR);
				nls();
				AST tmp178_AST = null;
				if (inputState.guessing==0) {
					tmp178_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp178_AST);
				}
				match(BOR);
				nls();
				if ( inputState.guessing==0 ) {
					oldClosureParameters_AST = (AST)currentAST.root;
					oldClosureParameters_AST = (AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(PARAMETERS,"PARAMETERS")));
					currentAST.root = oldClosureParameters_AST;
					currentAST.child = oldClosureParameters_AST!=null &&oldClosureParameters_AST.getFirstChild()!=null ?
						oldClosureParameters_AST.getFirstChild() : oldClosureParameters_AST;
					currentAST.advanceChildToEnd();
				}
				if ( inputState.guessing==0 ) {
					oldClosureParameters_AST = (AST)currentAST.root;
				}
			}
			else {
				boolean synPredMatched234 = false;
				if (((LA(1)==LPAREN||LA(1)==BOR) && (_tokenSet_83.member(LA(2))) && (_tokenSet_84.member(LA(3))))) {
					int _m234 = mark();
					synPredMatched234 = true;
					inputState.guessing++;
					try {
						{
						{
						switch ( LA(1)) {
						case BOR:
						{
							match(BOR);
							nls();
							break;
						}
						case LPAREN:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						match(LPAREN);
						parameterDeclarationList();
						match(RPAREN);
						nls();
						match(BOR);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched234 = false;
					}
					rewind(_m234);
					inputState.guessing--;
				}
				if ( synPredMatched234 ) {
					{
					switch ( LA(1)) {
					case BOR:
					{
						match(BOR);
						nls();
						break;
					}
					case LPAREN:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					match(LPAREN);
					parameterDeclarationList();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					match(RPAREN);
					nls();
					match(BOR);
					nls();
					if ( inputState.guessing==0 ) {
						oldClosureParameters_AST = (AST)currentAST.root;
					}
				}
				else {
					boolean synPredMatched238 = false;
					if (((_tokenSet_85.member(LA(1))) && (_tokenSet_86.member(LA(2))) && (_tokenSet_87.member(LA(3))))) {
						int _m238 = mark();
						synPredMatched238 = true;
						inputState.guessing++;
						try {
							{
							{
							switch ( LA(1)) {
							case BOR:
							{
								match(BOR);
								nls();
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
								break;
							}
							default:
							{
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
							}
							simpleParameterDeclarationList();
							nls();
							match(BOR);
							}
						}
						catch (RecognitionException pe) {
							synPredMatched238 = false;
						}
						rewind(_m238);
						inputState.guessing--;
					}
					if ( synPredMatched238 ) {
						{
						switch ( LA(1)) {
						case BOR:
						{
							match(BOR);
							nls();
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
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						simpleParameterDeclarationList();
						if (inputState.guessing==0) {
							astFactory.addASTChild(currentAST, returnAST);
						}
						nls();
						match(BOR);
						nls();
						if ( inputState.guessing==0 ) {
							oldClosureParameters_AST = (AST)currentAST.root;
						}
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}}}
					returnAST = oldClosureParameters_AST;
				}

/** A block known to be a closure, but which omits its arguments, is given this placeholder.
 *  A subsequent pass is responsible for deciding if there is an implicit 'it' parameter,
 *  or if the parameter list should be empty.
 */
	public final void implicitParameters() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST implicitParameters_AST = null;

		if ( inputState.guessing==0 ) {
			implicitParameters_AST = (AST)currentAST.root;
			implicitParameters_AST = (AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(IMPLICIT_PARAMETERS,"IMPLICIT_PARAMETERS")));
			currentAST.root = implicitParameters_AST;
			currentAST.child = implicitParameters_AST!=null &&implicitParameters_AST.getFirstChild()!=null ?
				implicitParameters_AST.getFirstChild() : implicitParameters_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			implicitParameters_AST = (AST)currentAST.root;
		}
		returnAST = implicitParameters_AST;
	}

/** Lookahead to check whether a block begins with explicit closure arguments. */
	public final void closureParametersStart() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST closureParametersStart_AST = null;

		boolean synPredMatched228 = false;
		if ((((_tokenSet_80.member(LA(1))) && (_tokenSet_88.member(LA(2))) && (_tokenSet_89.member(LA(3))))&&(compatibilityMode))) {
			int _m228 = mark();
			synPredMatched228 = true;
			inputState.guessing++;
			try {
				{
				oldClosureParametersStart();
				}
			}
			catch (RecognitionException pe) {
				synPredMatched228 = false;
			}
			rewind(_m228);
			inputState.guessing--;
		}
		if ( synPredMatched228 ) {
			oldClosureParametersStart();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				closureParametersStart_AST = (AST)currentAST.root;
			}
		}
		else if ((_tokenSet_78.member(LA(1))) && (_tokenSet_90.member(LA(2))) && (_tokenSet_91.member(LA(3)))) {
			parameterDeclarationList();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			nls();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp185_AST = null;
			if (inputState.guessing==0) {
				tmp185_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp185_AST);
			}
			match(CLOSURE_OP);
			if ( inputState.guessing==0 ) {
				closureParametersStart_AST = (AST)currentAST.root;
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		returnAST = closureParametersStart_AST;
	}

/** Simple names, as in {x|...}, are completely equivalent to {(def x)|...}.  Build the right AST. */
	public final void closureParameter() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST closureParameter_AST = null;
		Token  id = null;
		AST id_AST = null;

		id = LT(1);
		if (inputState.guessing==0) {
			id_AST = astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
		}
		match(IDENT);
		if ( inputState.guessing==0 ) {
			closureParameter_AST = (AST)currentAST.root;
			closureParameter_AST = (AST)astFactory.make( (new ASTArray(4)).add(astFactory.create(PARAMETER_DEF,"PARAMETER_DEF")).add((AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(MODIFIERS,"MODIFIERS")))).add((AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(TYPE,"TYPE")))).add(id_AST));
			currentAST.root = closureParameter_AST;
			currentAST.child = closureParameter_AST!=null &&closureParameter_AST.getFirstChild()!=null ?
				closureParameter_AST.getFirstChild() : closureParameter_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			closureParameter_AST = (AST)currentAST.root;
		}
		returnAST = closureParameter_AST;
	}

/** A block which is known to be a closure, even if it has no apparent arguments.
 *  A block inside an expression or after a method call is always assumed to be a closure.
 *  Only labeled, unparameterized blocks which occur directly as substatements are kept open.
 */
	public final void closedBlock() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST closedBlock_AST = null;

		match(LCURLY);
		if ( inputState.guessing==0 ) {
			astFactory.makeASTRoot(currentAST, astFactory.create(CLOSED_BLOCK, "{"));
		}
		nls();
		closureParametersOpt(true);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		blockBody(EOF);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			closedBlock_AST = (AST)currentAST.root;
		}
		returnAST = closedBlock_AST;
	}

/** A sub-block of a block can be either open or closed.
 *  It is closed if and only if there are explicit closure arguments.
 *  Compare this to a block which is appended to a method call,
 *  which is given closure arguments, even if they are not explicit in the code.
 */
	public final void openOrClosedBlock() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST openOrClosedBlock_AST = null;
		Token  lc = null;
		AST lc_AST = null;
		AST cp_AST = null;

		lc = LT(1);
		if (inputState.guessing==0) {
			lc_AST = astFactory.create(lc);
			astFactory.makeASTRoot(currentAST, lc_AST);
		}
		match(LCURLY);
		nls();
		closureParametersOpt(false);
		if (inputState.guessing==0) {
			cp_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			if (cp_AST == null)    lc_AST.setType(SLIST);
			else                lc_AST.setType(CLOSED_BLOCK);

		}
		blockBody(EOF);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			openOrClosedBlock_AST = (AST)currentAST.root;
		}
		returnAST = openOrClosedBlock_AST;
	}

/** A labeled statement, consisting of a vanilla identifier followed by a colon. */
	public final void statementLabelPrefix() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST statementLabelPrefix_AST = null;

		AST tmp189_AST = null;
		if (inputState.guessing==0) {
			tmp189_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp189_AST);
		}
		match(IDENT);
		match(COLON);
		if ( inputState.guessing==0 ) {
			astFactory.makeASTRoot(currentAST, astFactory.create(LABELED_STAT, ":"));
		}
		if ( inputState.guessing==0 ) {
			statementLabelPrefix_AST = (AST)currentAST.root;
		}
		returnAST = statementLabelPrefix_AST;
	}

/** An expression statement can be any general expression.
 *  <p>
 *  An expression statement can also be a <em>command</em>,
 *  which is a simple method call in which the outermost parentheses are omitted.
 *  <p>
 *  Certain "suspicious" looking forms are flagged for the user to disambiguate.
 */
	public final void expressionStatement(
		int prevToken
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expressionStatement_AST = null;
		AST head_AST = null;
		AST cmd_AST = null;
		boolean isPathExpr = false;

		{
		boolean synPredMatched293 = false;
		if (((_tokenSet_19.member(LA(1))) && (_tokenSet_8.member(LA(2))) && (_tokenSet_20.member(LA(3))))) {
			int _m293 = mark();
			synPredMatched293 = true;
			inputState.guessing++;
			try {
				{
				suspiciousExpressionStatementStart();
				}
			}
			catch (RecognitionException pe) {
				synPredMatched293 = false;
			}
			rewind(_m293);
			inputState.guessing--;
		}
		if ( synPredMatched293 ) {
			checkSuspiciousExpressionStatement(prevToken);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((_tokenSet_19.member(LA(1))) && (_tokenSet_8.member(LA(2))) && (_tokenSet_20.member(LA(3)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		expression(LC_STMT);
		if (inputState.guessing==0) {
			head_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			isPathExpr = (head_AST == lastPathExpression);
		}
		{
		if (((_tokenSet_19.member(LA(1))))&&(isPathExpr)) {
			commandArguments(head_AST);
			if (inputState.guessing==0) {
				cmd_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				expressionStatement_AST = (AST)currentAST.root;
				expressionStatement_AST = cmd_AST;
				currentAST.root = expressionStatement_AST;
				currentAST.child = expressionStatement_AST!=null &&expressionStatement_AST.getFirstChild()!=null ?
					expressionStatement_AST.getFirstChild() : expressionStatement_AST;
				currentAST.advanceChildToEnd();
			}
		}
		else if ((_tokenSet_9.member(LA(1)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		if ( inputState.guessing==0 ) {
			expressionStatement_AST = (AST)currentAST.root;
			expressionStatement_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(EXPR,"EXPR")).add(expressionStatement_AST));
			currentAST.root = expressionStatement_AST;
			currentAST.child = expressionStatement_AST!=null &&expressionStatement_AST.getFirstChild()!=null ?
				expressionStatement_AST.getFirstChild() : expressionStatement_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			expressionStatement_AST = (AST)currentAST.root;
		}
		returnAST = expressionStatement_AST;
	}

/** Things that can show up as expressions, but only in strict
 *  contexts like inside parentheses, argument lists, and list constructors.
 */
	public final void strictContextExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST strictContextExpression_AST = null;

		{
		boolean synPredMatched435 = false;
		if (((_tokenSet_12.member(LA(1))) && (_tokenSet_92.member(LA(2))) && (_tokenSet_93.member(LA(3))))) {
			int _m435 = mark();
			synPredMatched435 = true;
			inputState.guessing++;
			try {
				{
				declarationStart();
				}
			}
			catch (RecognitionException pe) {
				synPredMatched435 = false;
			}
			rewind(_m435);
			inputState.guessing--;
		}
		if ( synPredMatched435 ) {
			singleDeclaration();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((_tokenSet_19.member(LA(1))) && (_tokenSet_64.member(LA(2))) && (_tokenSet_20.member(LA(3)))) {
			expression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if (((LA(1) >= LITERAL_return && LA(1) <= LITERAL_assert))) {
			branchStatement();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((LA(1)==AT) && (LA(2)==IDENT) && (_tokenSet_94.member(LA(3)))) {
			annotation();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		if ( inputState.guessing==0 ) {
			strictContextExpression_AST = (AST)currentAST.root;
			strictContextExpression_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(EXPR,"EXPR")).add(strictContextExpression_AST));
			currentAST.root = strictContextExpression_AST;
			currentAST.child = strictContextExpression_AST!=null &&strictContextExpression_AST.getFirstChild()!=null ?
				strictContextExpression_AST.getFirstChild() : strictContextExpression_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			strictContextExpression_AST = (AST)currentAST.root;
		}
		returnAST = strictContextExpression_AST;
	}

/** In Java, "if", "while", and "for" statements can take random, non-braced statements as their bodies.
 *  Support this practice, even though it isn't very Groovy.
 */
	public final void compatibleBodyStatement() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST compatibleBodyStatement_AST = null;

		boolean synPredMatched279 = false;
		if (((LA(1)==LCURLY) && (_tokenSet_70.member(LA(2))) && (_tokenSet_8.member(LA(3))))) {
			int _m279 = mark();
			synPredMatched279 = true;
			inputState.guessing++;
			try {
				{
				match(LCURLY);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched279 = false;
			}
			rewind(_m279);
			inputState.guessing--;
		}
		if ( synPredMatched279 ) {
			compoundStatement();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				compatibleBodyStatement_AST = (AST)currentAST.root;
			}
		}
		else if ((_tokenSet_15.member(LA(1))) && (_tokenSet_8.member(LA(2))) && (_tokenSet_18.member(LA(3)))) {
			statement(EOF);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				compatibleBodyStatement_AST = (AST)currentAST.root;
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		returnAST = compatibleBodyStatement_AST;
	}

	public final void forStatement() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forStatement_AST = null;
		Token  f = null;
		AST f_AST = null;

		f = LT(1);
		if (inputState.guessing==0) {
			f_AST = astFactory.create(f);
			astFactory.makeASTRoot(currentAST, f_AST);
		}
		match(LITERAL_for);
		match(LPAREN);
		{
		boolean synPredMatched270 = false;
		if (((_tokenSet_95.member(LA(1))) && (_tokenSet_71.member(LA(2))) && (_tokenSet_96.member(LA(3))))) {
			int _m270 = mark();
			synPredMatched270 = true;
			inputState.guessing++;
			try {
				{
				forInit();
				match(SEMI);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched270 = false;
			}
			rewind(_m270);
			inputState.guessing--;
		}
		if ( synPredMatched270 ) {
			traditionalForClause();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((_tokenSet_12.member(LA(1))) && (_tokenSet_97.member(LA(2))) && (_tokenSet_98.member(LA(3)))) {
			forInClause();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		match(RPAREN);
		nlsWarn();
		compatibleBodyStatement();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			forStatement_AST = (AST)currentAST.root;
		}
		returnAST = forStatement_AST;
	}

	public final void casesGroup() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST casesGroup_AST = null;

		{
		int _cnt305=0;
		_loop305:
		do {
			if ((LA(1)==LITERAL_default||LA(1)==LITERAL_case)) {
				aCase();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				if ( _cnt305>=1 ) { break _loop305; } else {throw new NoViableAltException(LT(1), getFilename());}
			}

			_cnt305++;
		} while (true);
		}
		caseSList();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			casesGroup_AST = (AST)currentAST.root;
			casesGroup_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(CASE_GROUP,"CASE_GROUP")).add(casesGroup_AST));
			currentAST.root = casesGroup_AST;
			currentAST.child = casesGroup_AST!=null &&casesGroup_AST.getFirstChild()!=null ?
				casesGroup_AST.getFirstChild() : casesGroup_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			casesGroup_AST = (AST)currentAST.root;
		}
		returnAST = casesGroup_AST;
	}

	public final void tryBlock() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST tryBlock_AST = null;

		AST tmp193_AST = null;
		if (inputState.guessing==0) {
			tmp193_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp193_AST);
		}
		match(LITERAL_try);
		nlsWarn();
		compoundStatement();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop322:
		do {
			if ((LA(1)==NLS||LA(1)==LITERAL_catch) && (LA(2)==LPAREN||LA(2)==LITERAL_catch) && (_tokenSet_99.member(LA(3)))) {
				nls();
				handler();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop322;
			}

		} while (true);
		}
		{
		if ((LA(1)==NLS||LA(1)==LITERAL_finally) && (_tokenSet_100.member(LA(2))) && (_tokenSet_70.member(LA(3)))) {
			nls();
			finallyClause();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((_tokenSet_9.member(LA(1))) && (_tokenSet_10.member(LA(2))) && (_tokenSet_11.member(LA(3)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		if ( inputState.guessing==0 ) {
			tryBlock_AST = (AST)currentAST.root;
		}
		returnAST = tryBlock_AST;
	}

/** In Groovy, return, break, continue, throw, and assert can be used in a parenthesized expression context.
 *  Example:  println (x || (return));  println assert x, "won't print a false value!"
 *  If an optional expression is missing, its value is void (this coerces to null when a value is required).
 */
	public final void branchStatement() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST branchStatement_AST = null;

		switch ( LA(1)) {
		case LITERAL_return:
		{
			AST tmp194_AST = null;
			if (inputState.guessing==0) {
				tmp194_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp194_AST);
			}
			match(LITERAL_return);
			{
			switch ( LA(1)) {
			case IDENT:
			case LBRACK:
			case LPAREN:
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
			case BAND:
			case LCURLY:
			case LITERAL_this:
			case STRING_LITERAL:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case DOLLAR:
			case STRING_CTOR_START:
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
				expression(0);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				break;
			}
			case EOF:
			case RBRACK:
			case COMMA:
			case RPAREN:
			case RCURLY:
			case SEMI:
			case NLS:
			case LITERAL_default:
			case LITERAL_else:
			case LITERAL_case:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				branchStatement_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_break:
		case LITERAL_continue:
		{
			{
			switch ( LA(1)) {
			case LITERAL_break:
			{
				AST tmp195_AST = null;
				if (inputState.guessing==0) {
					tmp195_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp195_AST);
				}
				match(LITERAL_break);
				break;
			}
			case LITERAL_continue:
			{
				AST tmp196_AST = null;
				if (inputState.guessing==0) {
					tmp196_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp196_AST);
				}
				match(LITERAL_continue);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			boolean synPredMatched285 = false;
			if (((LA(1)==IDENT) && (LA(2)==COLON) && (_tokenSet_101.member(LA(3))))) {
				int _m285 = mark();
				synPredMatched285 = true;
				inputState.guessing++;
				try {
					{
					match(IDENT);
					match(COLON);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched285 = false;
				}
				rewind(_m285);
				inputState.guessing--;
			}
			if ( synPredMatched285 ) {
				statementLabelPrefix();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else if ((_tokenSet_101.member(LA(1))) && (_tokenSet_20.member(LA(2))) && (_tokenSet_11.member(LA(3)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}

			}
			{
			switch ( LA(1)) {
			case IDENT:
			case LBRACK:
			case LPAREN:
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
			case BAND:
			case LCURLY:
			case LITERAL_this:
			case STRING_LITERAL:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case DOLLAR:
			case STRING_CTOR_START:
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
				expression(0);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				break;
			}
			case EOF:
			case RBRACK:
			case COMMA:
			case RPAREN:
			case RCURLY:
			case SEMI:
			case NLS:
			case LITERAL_default:
			case LITERAL_else:
			case LITERAL_case:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				branchStatement_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_throw:
		{
			AST tmp197_AST = null;
			if (inputState.guessing==0) {
				tmp197_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp197_AST);
			}
			match(LITERAL_throw);
			expression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				branchStatement_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_assert:
		{
			AST tmp198_AST = null;
			if (inputState.guessing==0) {
				tmp198_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp198_AST);
			}
			match(LITERAL_assert);
			expression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			if ((LA(1)==COMMA||LA(1)==COLON) && (_tokenSet_19.member(LA(2))) && (_tokenSet_17.member(LA(3)))) {
				{
				switch ( LA(1)) {
				case COMMA:
				{
					match(COMMA);
					break;
				}
				case COLON:
				{
					match(COLON);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				expression(0);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else if ((_tokenSet_102.member(LA(1))) && (_tokenSet_20.member(LA(2))) && (_tokenSet_11.member(LA(3)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}

			}
			if ( inputState.guessing==0 ) {
				branchStatement_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = branchStatement_AST;
	}

	public final void forInit() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forInit_AST = null;

		boolean synPredMatched314 = false;
		if (((_tokenSet_12.member(LA(1))) && (_tokenSet_13.member(LA(2))) && (_tokenSet_103.member(LA(3))))) {
			int _m314 = mark();
			synPredMatched314 = true;
			inputState.guessing++;
			try {
				{
				declarationStart();
				}
			}
			catch (RecognitionException pe) {
				synPredMatched314 = false;
			}
			rewind(_m314);
			inputState.guessing--;
		}
		if ( synPredMatched314 ) {
			declaration();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				forInit_AST = (AST)currentAST.root;
			}
		}
		else if ((_tokenSet_95.member(LA(1))) && (_tokenSet_71.member(LA(2))) && (_tokenSet_96.member(LA(3)))) {
			{
			switch ( LA(1)) {
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case LITERAL_static:
			case LITERAL_def:
			case AT:
			case IDENT:
			case LBRACK:
			case LPAREN:
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
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_threadsafe:
			case LITERAL_synchronized:
			case LITERAL_volatile:
			case BAND:
			case LCURLY:
			case LITERAL_this:
			case STRING_LITERAL:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_continue:
			case LITERAL_throw:
			case LITERAL_assert:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case DOLLAR:
			case STRING_CTOR_START:
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
				controlExpressionList();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				break;
			}
			case SEMI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				forInit_AST = (AST)currentAST.root;
				forInit_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(FOR_INIT,"FOR_INIT")).add(forInit_AST));
				currentAST.root = forInit_AST;
				currentAST.child = forInit_AST!=null &&forInit_AST.getFirstChild()!=null ?
					forInit_AST.getFirstChild() : forInit_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				forInit_AST = (AST)currentAST.root;
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		returnAST = forInit_AST;
	}

	public final void traditionalForClause() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST traditionalForClause_AST = null;

		forInit();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		match(SEMI);
		forCond();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		match(SEMI);
		forIter();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			traditionalForClause_AST = (AST)currentAST.root;
		}
		returnAST = traditionalForClause_AST;
	}

	public final void forInClause() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forInClause_AST = null;
		AST decl_AST = null;

		{
		boolean synPredMatched275 = false;
		if (((_tokenSet_12.member(LA(1))) && (_tokenSet_92.member(LA(2))))) {
			int _m275 = mark();
			synPredMatched275 = true;
			inputState.guessing++;
			try {
				{
				declarationStart();
				}
			}
			catch (RecognitionException pe) {
				synPredMatched275 = false;
			}
			rewind(_m275);
			inputState.guessing--;
		}
		if ( synPredMatched275 ) {
			singleDeclarationNoInit();
			if (inputState.guessing==0) {
				decl_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((LA(1)==IDENT) && (LA(2)==COLON||LA(2)==LITERAL_in)) {
			AST tmp203_AST = null;
			if (inputState.guessing==0) {
				tmp203_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp203_AST);
			}
			match(IDENT);
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		{
		switch ( LA(1)) {
		case LITERAL_in:
		{
			match(LITERAL_in);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(FOR_IN_ITERABLE, "in"));
			}
			shiftExpression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case COLON:
		{
			if ( inputState.guessing==0 ) {
				addWarning(
				"A colon at this point is legal Java but not recommended in Groovy.",
				"Use the 'in' keyword."
				);
				require(decl_AST != null,
				"Java-style for-each statement requires a type declaration."
				,
				"Use the 'in' keyword, as for (x in y) {...}"
				);

			}
			match(COLON);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(FOR_IN_ITERABLE, ":"));
			}
			expression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			forInClause_AST = (AST)currentAST.root;
		}
		returnAST = forInClause_AST;
	}

	public final void forCond() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forCond_AST = null;

		{
		switch ( LA(1)) {
		case FINAL:
		case ABSTRACT:
		case STRICTFP:
		case LITERAL_static:
		case LITERAL_def:
		case AT:
		case IDENT:
		case LBRACK:
		case LPAREN:
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
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case BAND:
		case LCURLY:
		case LITERAL_this:
		case STRING_LITERAL:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case DOLLAR:
		case STRING_CTOR_START:
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
			strictContextExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case SEMI:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			forCond_AST = (AST)currentAST.root;
			forCond_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(FOR_CONDITION,"FOR_CONDITION")).add(forCond_AST));
			currentAST.root = forCond_AST;
			currentAST.child = forCond_AST!=null &&forCond_AST.getFirstChild()!=null ?
				forCond_AST.getFirstChild() : forCond_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			forCond_AST = (AST)currentAST.root;
		}
		returnAST = forCond_AST;
	}

	public final void forIter() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forIter_AST = null;

		{
		switch ( LA(1)) {
		case FINAL:
		case ABSTRACT:
		case STRICTFP:
		case LITERAL_static:
		case LITERAL_def:
		case AT:
		case IDENT:
		case LBRACK:
		case LPAREN:
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
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case BAND:
		case LCURLY:
		case LITERAL_this:
		case STRING_LITERAL:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_throw:
		case LITERAL_assert:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case DOLLAR:
		case STRING_CTOR_START:
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
			controlExpressionList();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case RPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			forIter_AST = (AST)currentAST.root;
			forIter_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(FOR_ITERATOR,"FOR_ITERATOR")).add(forIter_AST));
			currentAST.root = forIter_AST;
			currentAST.child = forIter_AST!=null &&forIter_AST.getFirstChild()!=null ?
				forIter_AST.getFirstChild() : forIter_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			forIter_AST = (AST)currentAST.root;
		}
		returnAST = forIter_AST;
	}

	public final void shiftExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST shiftExpression_AST = null;

		additiveExpression(lc_stmt);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop390:
		do {
			if ((_tokenSet_104.member(LA(1)))) {
				{
				switch ( LA(1)) {
				case SR:
				case BSR:
				case SL:
				{
					{
					switch ( LA(1)) {
					case SL:
					{
						AST tmp206_AST = null;
						if (inputState.guessing==0) {
							tmp206_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp206_AST);
						}
						match(SL);
						break;
					}
					case SR:
					{
						AST tmp207_AST = null;
						if (inputState.guessing==0) {
							tmp207_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp207_AST);
						}
						match(SR);
						break;
					}
					case BSR:
					{
						AST tmp208_AST = null;
						if (inputState.guessing==0) {
							tmp208_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp208_AST);
						}
						match(BSR);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					break;
				}
				case RANGE_INCLUSIVE:
				{
					AST tmp209_AST = null;
					if (inputState.guessing==0) {
						tmp209_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp209_AST);
					}
					match(RANGE_INCLUSIVE);
					break;
				}
				case RANGE_EXCLUSIVE:
				{
					AST tmp210_AST = null;
					if (inputState.guessing==0) {
						tmp210_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp210_AST);
					}
					match(RANGE_EXCLUSIVE);
					break;
				}
				case TRIPLE_DOT:
				{
					match(TRIPLE_DOT);
					if ( inputState.guessing==0 ) {
						astFactory.makeASTRoot(currentAST, astFactory.create(RANGE_EXCLUSIVE, "..."));
					}
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				nls();
				additiveExpression(0);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop390;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			shiftExpression_AST = (AST)currentAST.root;
		}
		returnAST = shiftExpression_AST;
	}

/** Lookahead for suspicious statement warnings and errors. */
	public final void suspiciousExpressionStatementStart() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST suspiciousExpressionStatementStart_AST = null;

		{
		switch ( LA(1)) {
		case PLUS:
		case MINUS:
		{
			{
			switch ( LA(1)) {
			case PLUS:
			{
				AST tmp212_AST = null;
				if (inputState.guessing==0) {
					tmp212_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp212_AST);
				}
				match(PLUS);
				break;
			}
			case MINUS:
			{
				AST tmp213_AST = null;
				if (inputState.guessing==0) {
					tmp213_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp213_AST);
				}
				match(MINUS);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case LBRACK:
		case LPAREN:
		case LCURLY:
		{
			{
			switch ( LA(1)) {
			case LBRACK:
			{
				AST tmp214_AST = null;
				if (inputState.guessing==0) {
					tmp214_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp214_AST);
				}
				match(LBRACK);
				break;
			}
			case LPAREN:
			{
				AST tmp215_AST = null;
				if (inputState.guessing==0) {
					tmp215_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp215_AST);
				}
				match(LPAREN);
				break;
			}
			case LCURLY:
			{
				AST tmp216_AST = null;
				if (inputState.guessing==0) {
					tmp216_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp216_AST);
				}
				match(LCURLY);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			suspiciousExpressionStatementStart_AST = (AST)currentAST.root;
		}
		returnAST = suspiciousExpressionStatementStart_AST;
	}

/**
 *  If two statements are separated by newline (not SEMI), the second had
 *  better not look like the latter half of an expression.  If it does, issue a warning.
 *  <p>
 *  Also, if the expression starts with a closure, it needs to
 *  have an explicit parameter list, in order to avoid the appearance of a
 *  compound statement.  This is a hard error.
 *  <p>
 *  These rules are different from Java's "dumb expression" restriction.
 *  Unlike Java, Groovy blocks can end with arbitrary (even dumb) expressions,
 *  as a consequence of optional 'return' and 'continue' tokens.
 * <p>
 *  To make the programmer's intention clear, a leading closure must have an
 *  explicit parameter list, and must not follow a previous statement separated
 *  only by newlines.
 */
	public final void checkSuspiciousExpressionStatement(
		int prevToken
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST checkSuspiciousExpressionStatement_AST = null;

		boolean synPredMatched297 = false;
		if (((_tokenSet_19.member(LA(1))) && (_tokenSet_8.member(LA(2))) && (_tokenSet_20.member(LA(3))))) {
			int _m297 = mark();
			synPredMatched297 = true;
			inputState.guessing++;
			try {
				{
				if ((_tokenSet_105.member(LA(1)))) {
					matchNot(LCURLY);
				}
				else if ((LA(1)==LCURLY)) {
					match(LCURLY);
					closureParametersStart();
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}

				}
			}
			catch (RecognitionException pe) {
				synPredMatched297 = false;
			}
			rewind(_m297);
			inputState.guessing--;
		}
		if ( synPredMatched297 ) {
			{
			if (((_tokenSet_19.member(LA(1))) && (_tokenSet_8.member(LA(2))) && (_tokenSet_20.member(LA(3))))&&(prevToken == NLS)) {
				if ( inputState.guessing==0 ) {
					addWarning(
					"Expression statement looks like it may continue a previous statement.",
					"Either remove previous newline, or add an explicit semicolon ';'.");

				}
			}
			else if ((_tokenSet_19.member(LA(1))) && (_tokenSet_8.member(LA(2))) && (_tokenSet_20.member(LA(3)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}

			}
			if ( inputState.guessing==0 ) {
				checkSuspiciousExpressionStatement_AST = (AST)currentAST.root;
			}
		}
		else if (((_tokenSet_19.member(LA(1))) && (_tokenSet_8.member(LA(2))) && (_tokenSet_20.member(LA(3))))&&(prevToken == NLS)) {
			if ( inputState.guessing==0 ) {
				require(false,
				"Closure expression looks like it may be an isolated open block, "+
				"or it may continue a previous statement."
				,
				"Add an explicit parameter list, as in {it -> ...}, or label it as L:{...}, "+
				"and also either remove previous newline, or add an explicit semicolon ';'."
				);

			}
			if ( inputState.guessing==0 ) {
				checkSuspiciousExpressionStatement_AST = (AST)currentAST.root;
			}
		}
		else if (((_tokenSet_19.member(LA(1))) && (_tokenSet_8.member(LA(2))) && (_tokenSet_20.member(LA(3))))&&(prevToken != NLS)) {
			if ( inputState.guessing==0 ) {
				require(false,
				"Closure expression looks like it may be an isolated open block.",
				"Add an explicit parameter list, as in {it -> ...}, or label it as L:{...}.");

			}
			if ( inputState.guessing==0 ) {
				checkSuspiciousExpressionStatement_AST = (AST)currentAST.root;
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		returnAST = checkSuspiciousExpressionStatement_AST;
	}

/** A member name (x.y) or element name (x[y]) can serve as a command name,
 *  which may be followed by a list of arguments.
 *  Unlike parenthesized arguments, these must be plain expressions,
 *  without labels or spread operators.
 */
	public final void commandArguments(
		AST head
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST commandArguments_AST = null;

		expression(0);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop328:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				nls();
				expression(0);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop328;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			commandArguments_AST = (AST)currentAST.root;

			AST elist = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(ELIST,"ELIST")).add(commandArguments_AST));
			AST headid = getASTFactory().dup(head);
			// TODO: possibly use ^[METHOD_CALL, ???] tree construction here?
			headid.setType(METHOD_CALL);
			headid.setText("<command>");
			commandArguments_AST = (AST)astFactory.make( (new ASTArray(3)).add(headid).add(head).add(elist));

			currentAST.root = commandArguments_AST;
			currentAST.child = commandArguments_AST!=null &&commandArguments_AST.getFirstChild()!=null ?
				commandArguments_AST.getFirstChild() : commandArguments_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			commandArguments_AST = (AST)currentAST.root;
		}
		returnAST = commandArguments_AST;
	}

	public final void aCase() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST aCase_AST = null;

		{
		switch ( LA(1)) {
		case LITERAL_case:
		{
			AST tmp218_AST = null;
			if (inputState.guessing==0) {
				tmp218_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp218_AST);
			}
			match(LITERAL_case);
			expression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case LITERAL_default:
		{
			AST tmp219_AST = null;
			if (inputState.guessing==0) {
				tmp219_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp219_AST);
			}
			match(LITERAL_default);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(COLON);
		nls();
		if ( inputState.guessing==0 ) {
			aCase_AST = (AST)currentAST.root;
		}
		returnAST = aCase_AST;
	}

	public final void caseSList() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST caseSList_AST = null;

		statement(COLON);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop311:
		do {
			if ((LA(1)==SEMI||LA(1)==NLS)) {
				sep();
				{
				switch ( LA(1)) {
				case FINAL:
				case ABSTRACT:
				case STRICTFP:
				case LITERAL_import:
				case LITERAL_static:
				case LITERAL_def:
				case AT:
				case IDENT:
				case LBRACK:
				case LPAREN:
				case LITERAL_class:
				case LITERAL_interface:
				case LITERAL_enum:
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
				case STAR:
				case LITERAL_private:
				case LITERAL_public:
				case LITERAL_protected:
				case LITERAL_transient:
				case LITERAL_native:
				case LITERAL_threadsafe:
				case LITERAL_synchronized:
				case LITERAL_volatile:
				case BAND:
				case LCURLY:
				case LITERAL_this:
				case STRING_LITERAL:
				case LITERAL_if:
				case LITERAL_while:
				case LITERAL_with:
				case LITERAL_switch:
				case LITERAL_for:
				case LITERAL_return:
				case LITERAL_break:
				case LITERAL_continue:
				case LITERAL_throw:
				case LITERAL_assert:
				case PLUS:
				case MINUS:
				case LITERAL_try:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case DOLLAR:
				case STRING_CTOR_START:
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
					statement(sepToken);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case RCURLY:
				case SEMI:
				case NLS:
				case LITERAL_default:
				case LITERAL_case:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else {
				break _loop311;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			caseSList_AST = (AST)currentAST.root;
			caseSList_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(SLIST,"SLIST")).add(caseSList_AST));
			currentAST.root = caseSList_AST;
			currentAST.child = caseSList_AST!=null &&caseSList_AST.getFirstChild()!=null ?
				caseSList_AST.getFirstChild() : caseSList_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			caseSList_AST = (AST)currentAST.root;
		}
		returnAST = caseSList_AST;
	}

	public final void controlExpressionList() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST controlExpressionList_AST = null;

		strictContextExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop332:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				nls();
				strictContextExpression();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop332;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			controlExpressionList_AST = (AST)currentAST.root;
			controlExpressionList_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(ELIST,"ELIST")).add(controlExpressionList_AST));
			currentAST.root = controlExpressionList_AST;
			currentAST.child = controlExpressionList_AST!=null &&controlExpressionList_AST.getFirstChild()!=null ?
				controlExpressionList_AST.getFirstChild() : controlExpressionList_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			controlExpressionList_AST = (AST)currentAST.root;
		}
		returnAST = controlExpressionList_AST;
	}

	public final void handler() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST handler_AST = null;

		AST tmp222_AST = null;
		if (inputState.guessing==0) {
			tmp222_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp222_AST);
		}
		match(LITERAL_catch);
		match(LPAREN);
		parameterDeclaration();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		match(RPAREN);
		nlsWarn();
		compoundStatement();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			handler_AST = (AST)currentAST.root;
		}
		returnAST = handler_AST;
	}

	public final void finallyClause() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST finallyClause_AST = null;

		AST tmp225_AST = null;
		if (inputState.guessing==0) {
			tmp225_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp225_AST);
		}
		match(LITERAL_finally);
		nlsWarn();
		compoundStatement();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			finallyClause_AST = (AST)currentAST.root;
		}
		returnAST = finallyClause_AST;
	}

	public final void assignmentExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST assignmentExpression_AST = null;

		conditionalExpression(lc_stmt);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		switch ( LA(1)) {
		case ASSIGN:
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
		{
			{
			switch ( LA(1)) {
			case ASSIGN:
			{
				AST tmp226_AST = null;
				if (inputState.guessing==0) {
					tmp226_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp226_AST);
				}
				match(ASSIGN);
				break;
			}
			case PLUS_ASSIGN:
			{
				AST tmp227_AST = null;
				if (inputState.guessing==0) {
					tmp227_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp227_AST);
				}
				match(PLUS_ASSIGN);
				break;
			}
			case MINUS_ASSIGN:
			{
				AST tmp228_AST = null;
				if (inputState.guessing==0) {
					tmp228_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp228_AST);
				}
				match(MINUS_ASSIGN);
				break;
			}
			case STAR_ASSIGN:
			{
				AST tmp229_AST = null;
				if (inputState.guessing==0) {
					tmp229_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp229_AST);
				}
				match(STAR_ASSIGN);
				break;
			}
			case DIV_ASSIGN:
			{
				AST tmp230_AST = null;
				if (inputState.guessing==0) {
					tmp230_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp230_AST);
				}
				match(DIV_ASSIGN);
				break;
			}
			case MOD_ASSIGN:
			{
				AST tmp231_AST = null;
				if (inputState.guessing==0) {
					tmp231_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp231_AST);
				}
				match(MOD_ASSIGN);
				break;
			}
			case SR_ASSIGN:
			{
				AST tmp232_AST = null;
				if (inputState.guessing==0) {
					tmp232_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp232_AST);
				}
				match(SR_ASSIGN);
				break;
			}
			case BSR_ASSIGN:
			{
				AST tmp233_AST = null;
				if (inputState.guessing==0) {
					tmp233_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp233_AST);
				}
				match(BSR_ASSIGN);
				break;
			}
			case SL_ASSIGN:
			{
				AST tmp234_AST = null;
				if (inputState.guessing==0) {
					tmp234_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp234_AST);
				}
				match(SL_ASSIGN);
				break;
			}
			case BAND_ASSIGN:
			{
				AST tmp235_AST = null;
				if (inputState.guessing==0) {
					tmp235_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp235_AST);
				}
				match(BAND_ASSIGN);
				break;
			}
			case BXOR_ASSIGN:
			{
				AST tmp236_AST = null;
				if (inputState.guessing==0) {
					tmp236_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp236_AST);
				}
				match(BXOR_ASSIGN);
				break;
			}
			case BOR_ASSIGN:
			{
				AST tmp237_AST = null;
				if (inputState.guessing==0) {
					tmp237_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp237_AST);
				}
				match(BOR_ASSIGN);
				break;
			}
			case STAR_STAR_ASSIGN:
			{
				AST tmp238_AST = null;
				if (inputState.guessing==0) {
					tmp238_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp238_AST);
				}
				match(STAR_STAR_ASSIGN);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			nls();
			assignmentExpression(lc_stmt == LC_STMT? LC_INIT: 0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case EOF:
		case IDENT:
		case LBRACK:
		case RBRACK:
		case LPAREN:
		case LITERAL_super:
		case COMMA:
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
		case RPAREN:
		case BAND:
		case LCURLY:
		case RCURLY:
		case SEMI:
		case NLS:
		case LITERAL_default:
		case LITERAL_this:
		case STRING_LITERAL:
		case CLOSURE_OP:
		case COLON:
		case LITERAL_else:
		case PLUS:
		case MINUS:
		case LITERAL_case:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case DOLLAR:
		case STRING_CTOR_START:
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
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			assignmentExpression_AST = (AST)currentAST.root;
		}
		returnAST = assignmentExpression_AST;
	}

/** A "path expression" is a name or other primary, possibly qualified by various
 *  forms of dot, and/or followed by various kinds of brackets.
 *  It can be used for value or assigned to, or else further qualified, indexed, or called.
 *  It is called a "path" because it looks like a linear path through a data structure.
 *  Examples:  x.y, x?.y, x*.y, x.@y; x[], x[y], x[y,z]; x(), x(y), x(y,z); x{s}; a.b[n].c(x).d{s}
 *  (Compare to a C lvalue, or LeftHandSide in the JLS section 15.26.)
 *  General expressions are built up from path expressions, using operators like '+' and '='.
 */
	public final void pathExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST pathExpression_AST = null;
		AST pre_AST = null;
		AST pe_AST = null;
		AST apb_AST = null;
		AST prefix = null;

		primaryExpression();
		if (inputState.guessing==0) {
			pre_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			prefix = pre_AST;
		}
		{
		_loop339:
		do {
			boolean synPredMatched336 = false;
			if (((_tokenSet_106.member(LA(1))) && (_tokenSet_107.member(LA(2))) && (_tokenSet_17.member(LA(3))))) {
				int _m336 = mark();
				synPredMatched336 = true;
				inputState.guessing++;
				try {
					{
					pathElementStart();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched336 = false;
				}
				rewind(_m336);
				inputState.guessing--;
			}
			if ( synPredMatched336 ) {
				pathElement(prefix);
				if (inputState.guessing==0) {
					pe_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
				if ( inputState.guessing==0 ) {
					prefix = pe_AST;
				}
			}
			else {
				boolean synPredMatched338 = false;
				if ((((LA(1)==LCURLY||LA(1)==NLS) && (_tokenSet_16.member(LA(2))) && (_tokenSet_17.member(LA(3))))&&(lc_stmt == LC_STMT || lc_stmt == LC_INIT))) {
					int _m338 = mark();
					synPredMatched338 = true;
					inputState.guessing++;
					try {
						{
						nls();
						match(LCURLY);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched338 = false;
					}
					rewind(_m338);
					inputState.guessing--;
				}
				if ( synPredMatched338 ) {
					nlsWarn();
					appendedBlock(prefix);
					if (inputState.guessing==0) {
						apb_AST = (AST)returnAST;
						astFactory.addASTChild(currentAST, returnAST);
					}
					if ( inputState.guessing==0 ) {
						prefix = apb_AST;
					}
				}
				else {
					break _loop339;
				}
				}
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				pathExpression_AST = (AST)currentAST.root;

				pathExpression_AST = prefix;
				lastPathExpression = pathExpression_AST;

				currentAST.root = pathExpression_AST;
				currentAST.child = pathExpression_AST!=null &&pathExpression_AST.getFirstChild()!=null ?
					pathExpression_AST.getFirstChild() : pathExpression_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				pathExpression_AST = (AST)currentAST.root;
			}
			returnAST = pathExpression_AST;
		}

	public final void primaryExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST primaryExpression_AST = null;

		switch ( LA(1)) {
		case IDENT:
		{
			AST tmp239_AST = null;
			if (inputState.guessing==0) {
				tmp239_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp239_AST);
			}
			match(IDENT);
			if ( inputState.guessing==0 ) {
				primaryExpression_AST = (AST)currentAST.root;
			}
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
			constant();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				primaryExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_new:
		{
			newExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				primaryExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_this:
		{
			AST tmp240_AST = null;
			if (inputState.guessing==0) {
				tmp240_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp240_AST);
			}
			match(LITERAL_this);
			if ( inputState.guessing==0 ) {
				primaryExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_super:
		{
			AST tmp241_AST = null;
			if (inputState.guessing==0) {
				tmp241_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp241_AST);
			}
			match(LITERAL_super);
			if ( inputState.guessing==0 ) {
				primaryExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		case LPAREN:
		{
			parenthesizedExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				primaryExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		case LCURLY:
		{
			closureConstructorExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				primaryExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		case LBRACK:
		{
			listOrMapConstructorExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				primaryExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		case STRING_CTOR_START:
		{
			stringConstructorExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				primaryExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		case DOLLAR:
		{
			scopeEscapeExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				primaryExpression_AST = (AST)currentAST.root;
			}
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
			builtInType();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				primaryExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = primaryExpression_AST;
	}

	public final void pathElementStart() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST pathElementStart_AST = null;

		switch ( LA(1)) {
		case DOT:
		{
			AST tmp242_AST = null;
			if (inputState.guessing==0) {
				tmp242_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp242_AST);
			}
			match(DOT);
			if ( inputState.guessing==0 ) {
				pathElementStart_AST = (AST)currentAST.root;
			}
			break;
		}
		case SPREAD_DOT:
		{
			AST tmp243_AST = null;
			if (inputState.guessing==0) {
				tmp243_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp243_AST);
			}
			match(SPREAD_DOT);
			if ( inputState.guessing==0 ) {
				pathElementStart_AST = (AST)currentAST.root;
			}
			break;
		}
		case OPTIONAL_DOT:
		{
			AST tmp244_AST = null;
			if (inputState.guessing==0) {
				tmp244_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp244_AST);
			}
			match(OPTIONAL_DOT);
			if ( inputState.guessing==0 ) {
				pathElementStart_AST = (AST)currentAST.root;
			}
			break;
		}
		case MEMBER_POINTER_DEFAULT:
		{
			AST tmp245_AST = null;
			if (inputState.guessing==0) {
				tmp245_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp245_AST);
			}
			match(MEMBER_POINTER_DEFAULT);
			if ( inputState.guessing==0 ) {
				pathElementStart_AST = (AST)currentAST.root;
			}
			break;
		}
		case MEMBER_POINTER:
		{
			AST tmp246_AST = null;
			if (inputState.guessing==0) {
				tmp246_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp246_AST);
			}
			match(MEMBER_POINTER);
			if ( inputState.guessing==0 ) {
				pathElementStart_AST = (AST)currentAST.root;
			}
			break;
		}
		case LBRACK:
		{
			AST tmp247_AST = null;
			if (inputState.guessing==0) {
				tmp247_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp247_AST);
			}
			match(LBRACK);
			if ( inputState.guessing==0 ) {
				pathElementStart_AST = (AST)currentAST.root;
			}
			break;
		}
		case LPAREN:
		{
			AST tmp248_AST = null;
			if (inputState.guessing==0) {
				tmp248_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp248_AST);
			}
			match(LPAREN);
			if ( inputState.guessing==0 ) {
				pathElementStart_AST = (AST)currentAST.root;
			}
			break;
		}
		case LCURLY:
		{
			AST tmp249_AST = null;
			if (inputState.guessing==0) {
				tmp249_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp249_AST);
			}
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				pathElementStart_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = pathElementStart_AST;
	}

	public final void pathElement(
		AST prefix
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST pathElement_AST = null;
		AST mca_AST = null;
		AST apb_AST = null;
		AST ipa_AST = null;

		switch ( LA(1)) {
		case DOT:
		case SPREAD_DOT:
		case OPTIONAL_DOT:
		case MEMBER_POINTER:
		{
			if ( inputState.guessing==0 ) {
				pathElement_AST = (AST)currentAST.root;
				pathElement_AST = prefix;
				currentAST.root = pathElement_AST;
				currentAST.child = pathElement_AST!=null &&pathElement_AST.getFirstChild()!=null ?
					pathElement_AST.getFirstChild() : pathElement_AST;
				currentAST.advanceChildToEnd();
			}
			{
			switch ( LA(1)) {
			case SPREAD_DOT:
			{
				AST tmp250_AST = null;
				if (inputState.guessing==0) {
					tmp250_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp250_AST);
				}
				match(SPREAD_DOT);
				break;
			}
			case OPTIONAL_DOT:
			{
				AST tmp251_AST = null;
				if (inputState.guessing==0) {
					tmp251_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp251_AST);
				}
				match(OPTIONAL_DOT);
				break;
			}
			case MEMBER_POINTER:
			{
				AST tmp252_AST = null;
				if (inputState.guessing==0) {
					tmp252_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp252_AST);
				}
				match(MEMBER_POINTER);
				break;
			}
			case DOT:
			{
				AST tmp253_AST = null;
				if (inputState.guessing==0) {
					tmp253_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp253_AST);
				}
				match(DOT);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			nls();
			{
			switch ( LA(1)) {
			case LT:
			{
				typeArguments();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				break;
			}
			case UNUSED_DO:
			case LITERAL_def:
			case AT:
			case IDENT:
			case LPAREN:
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
			case LCURLY:
			case STRING_LITERAL:
			case LITERAL_if:
			case LITERAL_else:
			case LITERAL_while:
			case LITERAL_switch:
			case LITERAL_for:
			case LITERAL_in:
			case LITERAL_try:
			case LITERAL_finally:
			case LITERAL_catch:
			case STRING_CTOR_START:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			namePart();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				pathElement_AST = (AST)currentAST.root;
			}
			break;
		}
		case LPAREN:
		{
			methodCallArgs(prefix);
			if (inputState.guessing==0) {
				mca_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				pathElement_AST = (AST)currentAST.root;
				pathElement_AST = mca_AST;
				currentAST.root = pathElement_AST;
				currentAST.child = pathElement_AST!=null &&pathElement_AST.getFirstChild()!=null ?
					pathElement_AST.getFirstChild() : pathElement_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				pathElement_AST = (AST)currentAST.root;
			}
			break;
		}
		case LCURLY:
		{
			appendedBlock(prefix);
			if (inputState.guessing==0) {
				apb_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				pathElement_AST = (AST)currentAST.root;
				pathElement_AST = apb_AST;
				currentAST.root = pathElement_AST;
				currentAST.child = pathElement_AST!=null &&pathElement_AST.getFirstChild()!=null ?
					pathElement_AST.getFirstChild() : pathElement_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				pathElement_AST = (AST)currentAST.root;
			}
			break;
		}
		case LBRACK:
		{
			indexPropertyArgs(prefix);
			if (inputState.guessing==0) {
				ipa_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				pathElement_AST = (AST)currentAST.root;
				pathElement_AST = ipa_AST;
				currentAST.root = pathElement_AST;
				currentAST.child = pathElement_AST!=null &&pathElement_AST.getFirstChild()!=null ?
					pathElement_AST.getFirstChild() : pathElement_AST;
				currentAST.advanceChildToEnd();
			}
			if ( inputState.guessing==0 ) {
				pathElement_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = pathElement_AST;
	}

/** An appended block follows any expression.
 *  If the expression is not a method call, it is given an empty argument list.
 */
	public final void appendedBlock(
		AST callee
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST appendedBlock_AST = null;

		if ( inputState.guessing==0 ) {
			appendedBlock_AST = (AST)currentAST.root;

			// If the callee is itself a call, flatten the AST.
			if (callee != null && callee.getType() == METHOD_CALL) {
			appendedBlock_AST = callee;
			} else {
			AST lbrace = getASTFactory().create(LT(1));
			lbrace.setType(METHOD_CALL);
			if (callee != null)  lbrace.addChild(callee);
			appendedBlock_AST = lbrace;
			}

			currentAST.root = appendedBlock_AST;
			currentAST.child = appendedBlock_AST!=null &&appendedBlock_AST.getFirstChild()!=null ?
				appendedBlock_AST.getFirstChild() : appendedBlock_AST;
			currentAST.advanceChildToEnd();
		}
		closedBlock();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			appendedBlock_AST = (AST)currentAST.root;
		}
		returnAST = appendedBlock_AST;
	}

/** This is the grammar for what can follow a dot:  x.a, x.@a, x.&a, x.'a', etc.
 *  Note: <code>typeArguments</code> is handled by the caller of <code>namePart</code>.
 */
	public final void namePart() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST namePart_AST = null;
		Token  ats = null;
		AST ats_AST = null;
		Token  sl = null;
		AST sl_AST = null;
		AST dn_AST = null;

		{
		switch ( LA(1)) {
		case AT:
		{
			ats = LT(1);
			if (inputState.guessing==0) {
				ats_AST = astFactory.create(ats);
				astFactory.makeASTRoot(currentAST, ats_AST);
			}
			match(AT);
			if ( inputState.guessing==0 ) {
				ats_AST.setType(SELECT_SLOT);
			}
			break;
		}
		case UNUSED_DO:
		case LITERAL_def:
		case IDENT:
		case LPAREN:
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
		case LCURLY:
		case STRING_LITERAL:
		case LITERAL_if:
		case LITERAL_else:
		case LITERAL_while:
		case LITERAL_switch:
		case LITERAL_for:
		case LITERAL_in:
		case LITERAL_try:
		case LITERAL_finally:
		case LITERAL_catch:
		case STRING_CTOR_START:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case IDENT:
		{
			AST tmp254_AST = null;
			if (inputState.guessing==0) {
				tmp254_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp254_AST);
			}
			match(IDENT);
			break;
		}
		case STRING_LITERAL:
		{
			sl = LT(1);
			if (inputState.guessing==0) {
				sl_AST = astFactory.create(sl);
				astFactory.addASTChild(currentAST, sl_AST);
			}
			match(STRING_LITERAL);
			if ( inputState.guessing==0 ) {
				sl_AST.setType(IDENT);
			}
			break;
		}
		case LPAREN:
		case STRING_CTOR_START:
		{
			dynamicMemberName();
			if (inputState.guessing==0) {
				dn_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				namePart_AST = (AST)currentAST.root;
				namePart_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(DYNAMIC_MEMBER,"DYNAMIC_MEMBER")).add(dn_AST));
				currentAST.root = namePart_AST;
				currentAST.child = namePart_AST!=null &&namePart_AST.getFirstChild()!=null ?
					namePart_AST.getFirstChild() : namePart_AST;
				currentAST.advanceChildToEnd();
			}
			break;
		}
		case LCURLY:
		{
			openBlock();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case UNUSED_DO:
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
		case LITERAL_if:
		case LITERAL_else:
		case LITERAL_while:
		case LITERAL_switch:
		case LITERAL_for:
		case LITERAL_in:
		case LITERAL_try:
		case LITERAL_finally:
		case LITERAL_catch:
		{
			keywordPropertyNames();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			namePart_AST = (AST)currentAST.root;
		}
		returnAST = namePart_AST;
	}

/** An expression may be followed by one or both of (...) and {...}.
 *  Note: If either is (...) or {...} present, it is a method call.
 *  The {...} is appended to the argument list, and matches a formal of type Closure.
 *  If there is no method member, a property (or field) is used instead, and must itself be callable.
 *  <p>
 *  If the methodCallArgs are absent, it is a property reference.
 *  If there is no property, it is treated as a field reference, but never a method reference.
 *  <p>
 *  Arguments in the (...) can be labeled, and the appended block can be labeled also.
 *  If there is a mix of unlabeled and labeled arguments,
 *  all the labeled arguments must follow the unlabeled arguments,
 *  except that the closure (labeled or not) is always a separate final argument.
 *  Labeled arguments are collected up and passed as a single argument to a formal of type Map.
 *  <p>
 *  Therefore, f(x,y, a:p, b:q) {s} is equivalent in all ways to f(x,y, [a:p,b:q], {s}).
 *  Spread arguments of sequence type count as unlabeled arguments,
 *  while spread arguments of map type count as labeled arguments.
 *  (This distinction must sometimes be checked dynamically.)
 *
 *  A plain unlabeled argument is allowed to match a trailing Map or Closure argument:
 *  f(x, a:p) {s}  ===  f(*[ x, [a:p], {s} ])
 */
	public final void methodCallArgs(
		AST callee
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST methodCallArgs_AST = null;

		if ( inputState.guessing==0 ) {
			methodCallArgs_AST = (AST)currentAST.root;
			methodCallArgs_AST = callee;
			currentAST.root = methodCallArgs_AST;
			currentAST.child = methodCallArgs_AST!=null &&methodCallArgs_AST.getFirstChild()!=null ?
				methodCallArgs_AST.getFirstChild() : methodCallArgs_AST;
			currentAST.advanceChildToEnd();
		}
		match(LPAREN);
		if ( inputState.guessing==0 ) {
			astFactory.makeASTRoot(currentAST, astFactory.create(METHOD_CALL, "("));
		}
		argList();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		match(RPAREN);
		if ( inputState.guessing==0 ) {
			methodCallArgs_AST = (AST)currentAST.root;
		}
		returnAST = methodCallArgs_AST;
	}

/** An expression may be followed by [...].
 *  Unlike Java, these brackets may contain a general argument list,
 *  which is passed to the array element operator, which can make of it what it wants.
 *  The brackets may also be empty, as in T[].  This is how Groovy names array types.
 *  <p>Returned AST is [INDEX_OP, indexee, ELIST].
 */
	public final void indexPropertyArgs(
		AST indexee
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST indexPropertyArgs_AST = null;

		if ( inputState.guessing==0 ) {
			indexPropertyArgs_AST = (AST)currentAST.root;
			indexPropertyArgs_AST = indexee;
			currentAST.root = indexPropertyArgs_AST;
			currentAST.child = indexPropertyArgs_AST!=null &&indexPropertyArgs_AST.getFirstChild()!=null ?
				indexPropertyArgs_AST.getFirstChild() : indexPropertyArgs_AST;
			currentAST.advanceChildToEnd();
		}
		match(LBRACK);
		if ( inputState.guessing==0 ) {
			astFactory.makeASTRoot(currentAST, astFactory.create(INDEX_OP, "["));
		}
		argList();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		match(RBRACK);
		if ( inputState.guessing==0 ) {
			indexPropertyArgs_AST = (AST)currentAST.root;
		}
		returnAST = indexPropertyArgs_AST;
	}

/** If a dot is followed by a parenthesized or quoted expression, the member is computed dynamically,
 *  and the member selection is done only at runtime.  This forces a statically unchecked member access.
 */
	public final void dynamicMemberName() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dynamicMemberName_AST = null;

		{
		switch ( LA(1)) {
		case LPAREN:
		{
			parenthesizedExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case STRING_CTOR_START:
		{
			stringConstructorExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			dynamicMemberName_AST = (AST)currentAST.root;
			dynamicMemberName_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(DYNAMIC_MEMBER,"DYNAMIC_MEMBER")).add(dynamicMemberName_AST));
			currentAST.root = dynamicMemberName_AST;
			currentAST.child = dynamicMemberName_AST!=null &&dynamicMemberName_AST.getFirstChild()!=null ?
				dynamicMemberName_AST.getFirstChild() : dynamicMemberName_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			dynamicMemberName_AST = (AST)currentAST.root;
		}
		returnAST = dynamicMemberName_AST;
	}

/** Allowed keywords after dot (as a member name) and before colon (as a label).
 *  TODO: What's the rationale for these?
 */
	public final void keywordPropertyNames() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST keywordPropertyNames_AST = null;

		{
		switch ( LA(1)) {
		case LITERAL_class:
		{
			AST tmp259_AST = null;
			if (inputState.guessing==0) {
				tmp259_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp259_AST);
			}
			match(LITERAL_class);
			break;
		}
		case LITERAL_in:
		{
			AST tmp260_AST = null;
			if (inputState.guessing==0) {
				tmp260_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp260_AST);
			}
			match(LITERAL_in);
			break;
		}
		case LITERAL_as:
		{
			AST tmp261_AST = null;
			if (inputState.guessing==0) {
				tmp261_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp261_AST);
			}
			match(LITERAL_as);
			break;
		}
		case LITERAL_def:
		{
			AST tmp262_AST = null;
			if (inputState.guessing==0) {
				tmp262_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp262_AST);
			}
			match(LITERAL_def);
			break;
		}
		case LITERAL_if:
		{
			AST tmp263_AST = null;
			if (inputState.guessing==0) {
				tmp263_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp263_AST);
			}
			match(LITERAL_if);
			break;
		}
		case LITERAL_else:
		{
			AST tmp264_AST = null;
			if (inputState.guessing==0) {
				tmp264_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp264_AST);
			}
			match(LITERAL_else);
			break;
		}
		case LITERAL_for:
		{
			AST tmp265_AST = null;
			if (inputState.guessing==0) {
				tmp265_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp265_AST);
			}
			match(LITERAL_for);
			break;
		}
		case LITERAL_while:
		{
			AST tmp266_AST = null;
			if (inputState.guessing==0) {
				tmp266_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp266_AST);
			}
			match(LITERAL_while);
			break;
		}
		case UNUSED_DO:
		{
			AST tmp267_AST = null;
			if (inputState.guessing==0) {
				tmp267_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp267_AST);
			}
			match(UNUSED_DO);
			break;
		}
		case LITERAL_switch:
		{
			AST tmp268_AST = null;
			if (inputState.guessing==0) {
				tmp268_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp268_AST);
			}
			match(LITERAL_switch);
			break;
		}
		case LITERAL_try:
		{
			AST tmp269_AST = null;
			if (inputState.guessing==0) {
				tmp269_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp269_AST);
			}
			match(LITERAL_try);
			break;
		}
		case LITERAL_catch:
		{
			AST tmp270_AST = null;
			if (inputState.guessing==0) {
				tmp270_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp270_AST);
			}
			match(LITERAL_catch);
			break;
		}
		case LITERAL_finally:
		{
			AST tmp271_AST = null;
			if (inputState.guessing==0) {
				tmp271_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp271_AST);
			}
			match(LITERAL_finally);
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
			builtInType();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			keywordPropertyNames_AST = (AST)currentAST.root;
			keywordPropertyNames_AST.setType(IDENT);
		}
		if ( inputState.guessing==0 ) {
			keywordPropertyNames_AST = (AST)currentAST.root;
		}
		returnAST = keywordPropertyNames_AST;
	}

	public final void parenthesizedExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parenthesizedExpression_AST = null;

		match(LPAREN);
		strictContextExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		match(RPAREN);
		if ( inputState.guessing==0 ) {
			parenthesizedExpression_AST = (AST)currentAST.root;
		}
		returnAST = parenthesizedExpression_AST;
	}

	public final void stringConstructorExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST stringConstructorExpression_AST = null;
		Token  cs = null;
		AST cs_AST = null;
		Token  cm = null;
		AST cm_AST = null;
		Token  ce = null;
		AST ce_AST = null;

		cs = LT(1);
		if (inputState.guessing==0) {
			cs_AST = astFactory.create(cs);
			astFactory.addASTChild(currentAST, cs_AST);
		}
		match(STRING_CTOR_START);
		if ( inputState.guessing==0 ) {
			cs_AST.setType(STRING_LITERAL);
		}
		stringConstructorValuePart();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop439:
		do {
			if ((LA(1)==STRING_CTOR_MIDDLE)) {
				cm = LT(1);
				if (inputState.guessing==0) {
					cm_AST = astFactory.create(cm);
					astFactory.addASTChild(currentAST, cm_AST);
				}
				match(STRING_CTOR_MIDDLE);
				if ( inputState.guessing==0 ) {
					cm_AST.setType(STRING_LITERAL);
				}
				stringConstructorValuePart();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop439;
			}

		} while (true);
		}
		ce = LT(1);
		if (inputState.guessing==0) {
			ce_AST = astFactory.create(ce);
			astFactory.addASTChild(currentAST, ce_AST);
		}
		match(STRING_CTOR_END);
		if ( inputState.guessing==0 ) {
			stringConstructorExpression_AST = (AST)currentAST.root;
			ce_AST.setType(STRING_LITERAL);
			stringConstructorExpression_AST =
			(AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(STRING_CONSTRUCTOR,"STRING_CONSTRUCTOR")).add(stringConstructorExpression_AST));

			currentAST.root = stringConstructorExpression_AST;
			currentAST.child = stringConstructorExpression_AST!=null &&stringConstructorExpression_AST.getFirstChild()!=null ?
				stringConstructorExpression_AST.getFirstChild() : stringConstructorExpression_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			stringConstructorExpression_AST = (AST)currentAST.root;
		}
		returnAST = stringConstructorExpression_AST;
	}

	public final void logicalOrExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalOrExpression_AST = null;

		logicalAndExpression(lc_stmt);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop361:
		do {
			if ((LA(1)==LOR)) {
				AST tmp274_AST = null;
				if (inputState.guessing==0) {
					tmp274_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp274_AST);
				}
				match(LOR);
				nls();
				logicalAndExpression(0);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop361;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			logicalOrExpression_AST = (AST)currentAST.root;
		}
		returnAST = logicalOrExpression_AST;
	}

	public final void logicalAndExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalAndExpression_AST = null;

		inclusiveOrExpression(lc_stmt);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop364:
		do {
			if ((LA(1)==LAND)) {
				AST tmp275_AST = null;
				if (inputState.guessing==0) {
					tmp275_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp275_AST);
				}
				match(LAND);
				nls();
				inclusiveOrExpression(0);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop364;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			logicalAndExpression_AST = (AST)currentAST.root;
		}
		returnAST = logicalAndExpression_AST;
	}

	public final void inclusiveOrExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST inclusiveOrExpression_AST = null;

		exclusiveOrExpression(lc_stmt);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop367:
		do {
			if ((LA(1)==BOR)) {
				AST tmp276_AST = null;
				if (inputState.guessing==0) {
					tmp276_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp276_AST);
				}
				match(BOR);
				nls();
				exclusiveOrExpression(0);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop367;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			inclusiveOrExpression_AST = (AST)currentAST.root;
		}
		returnAST = inclusiveOrExpression_AST;
	}

	public final void exclusiveOrExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST exclusiveOrExpression_AST = null;

		andExpression(lc_stmt);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop370:
		do {
			if ((LA(1)==BXOR)) {
				AST tmp277_AST = null;
				if (inputState.guessing==0) {
					tmp277_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp277_AST);
				}
				match(BXOR);
				nls();
				andExpression(0);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop370;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			exclusiveOrExpression_AST = (AST)currentAST.root;
		}
		returnAST = exclusiveOrExpression_AST;
	}

	public final void andExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST andExpression_AST = null;

		regexExpression(lc_stmt);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop373:
		do {
			if ((LA(1)==BAND) && (_tokenSet_108.member(LA(2))) && (_tokenSet_17.member(LA(3)))) {
				AST tmp278_AST = null;
				if (inputState.guessing==0) {
					tmp278_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp278_AST);
				}
				match(BAND);
				nls();
				regexExpression(0);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop373;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			andExpression_AST = (AST)currentAST.root;
		}
		returnAST = andExpression_AST;
	}

	public final void regexExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST regexExpression_AST = null;

		equalityExpression(lc_stmt);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop377:
		do {
			if ((LA(1)==REGEX_FIND||LA(1)==REGEX_MATCH)) {
				{
				switch ( LA(1)) {
				case REGEX_FIND:
				{
					AST tmp279_AST = null;
					if (inputState.guessing==0) {
						tmp279_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp279_AST);
					}
					match(REGEX_FIND);
					break;
				}
				case REGEX_MATCH:
				{
					AST tmp280_AST = null;
					if (inputState.guessing==0) {
						tmp280_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp280_AST);
					}
					match(REGEX_MATCH);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				nls();
				equalityExpression(0);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop377;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			regexExpression_AST = (AST)currentAST.root;
		}
		returnAST = regexExpression_AST;
	}

	public final void equalityExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST equalityExpression_AST = null;

		relationalExpression(lc_stmt);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop381:
		do {
			if (((LA(1) >= NOT_EQUAL && LA(1) <= COMPARE_TO))) {
				{
				switch ( LA(1)) {
				case NOT_EQUAL:
				{
					AST tmp281_AST = null;
					if (inputState.guessing==0) {
						tmp281_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp281_AST);
					}
					match(NOT_EQUAL);
					break;
				}
				case EQUAL:
				{
					AST tmp282_AST = null;
					if (inputState.guessing==0) {
						tmp282_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp282_AST);
					}
					match(EQUAL);
					break;
				}
				case COMPARE_TO:
				{
					AST tmp283_AST = null;
					if (inputState.guessing==0) {
						tmp283_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp283_AST);
					}
					match(COMPARE_TO);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				nls();
				relationalExpression(0);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop381;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			equalityExpression_AST = (AST)currentAST.root;
		}
		returnAST = equalityExpression_AST;
	}

	public final void relationalExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST relationalExpression_AST = null;

		shiftExpression(lc_stmt);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		switch ( LA(1)) {
		case EOF:
		case IDENT:
		case LBRACK:
		case RBRACK:
		case LPAREN:
		case QUESTION:
		case LITERAL_super:
		case LT:
		case COMMA:
		case GT:
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
		case RPAREN:
		case ASSIGN:
		case BAND:
		case LCURLY:
		case RCURLY:
		case SEMI:
		case NLS:
		case LITERAL_default:
		case LITERAL_this:
		case STRING_LITERAL:
		case CLOSURE_OP:
		case LOR:
		case BOR:
		case COLON:
		case LITERAL_else:
		case LITERAL_in:
		case PLUS:
		case MINUS:
		case LITERAL_case:
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
		case LAND:
		case BXOR:
		case REGEX_FIND:
		case REGEX_MATCH:
		case NOT_EQUAL:
		case EQUAL:
		case COMPARE_TO:
		case LE:
		case GE:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case DOLLAR:
		case STRING_CTOR_START:
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
			{
			switch ( LA(1)) {
			case LT:
			case GT:
			case LITERAL_in:
			case LE:
			case GE:
			{
				{
				switch ( LA(1)) {
				case LT:
				{
					AST tmp284_AST = null;
					if (inputState.guessing==0) {
						tmp284_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp284_AST);
					}
					match(LT);
					break;
				}
				case GT:
				{
					AST tmp285_AST = null;
					if (inputState.guessing==0) {
						tmp285_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp285_AST);
					}
					match(GT);
					break;
				}
				case LE:
				{
					AST tmp286_AST = null;
					if (inputState.guessing==0) {
						tmp286_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp286_AST);
					}
					match(LE);
					break;
				}
				case GE:
				{
					AST tmp287_AST = null;
					if (inputState.guessing==0) {
						tmp287_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp287_AST);
					}
					match(GE);
					break;
				}
				case LITERAL_in:
				{
					AST tmp288_AST = null;
					if (inputState.guessing==0) {
						tmp288_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp288_AST);
					}
					match(LITERAL_in);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				nls();
				shiftExpression(0);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				break;
			}
			case EOF:
			case IDENT:
			case LBRACK:
			case RBRACK:
			case LPAREN:
			case QUESTION:
			case LITERAL_super:
			case COMMA:
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
			case RPAREN:
			case ASSIGN:
			case BAND:
			case LCURLY:
			case RCURLY:
			case SEMI:
			case NLS:
			case LITERAL_default:
			case LITERAL_this:
			case STRING_LITERAL:
			case CLOSURE_OP:
			case LOR:
			case BOR:
			case COLON:
			case LITERAL_else:
			case PLUS:
			case MINUS:
			case LITERAL_case:
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
			case LAND:
			case BXOR:
			case REGEX_FIND:
			case REGEX_MATCH:
			case NOT_EQUAL:
			case EQUAL:
			case COMPARE_TO:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case DOLLAR:
			case STRING_CTOR_START:
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
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case LITERAL_instanceof:
		{
			AST tmp289_AST = null;
			if (inputState.guessing==0) {
				tmp289_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp289_AST);
			}
			match(LITERAL_instanceof);
			nls();
			typeSpec(true);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case LITERAL_as:
		{
			AST tmp290_AST = null;
			if (inputState.guessing==0) {
				tmp290_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp290_AST);
			}
			match(LITERAL_as);
			nls();
			typeSpec(true);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			relationalExpression_AST = (AST)currentAST.root;
		}
		returnAST = relationalExpression_AST;
	}

	public final void additiveExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST additiveExpression_AST = null;

		multiplicativeExpression(lc_stmt);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop394:
		do {
			if ((LA(1)==PLUS||LA(1)==MINUS) && (_tokenSet_108.member(LA(2))) && (_tokenSet_17.member(LA(3)))) {
				{
				switch ( LA(1)) {
				case PLUS:
				{
					AST tmp291_AST = null;
					if (inputState.guessing==0) {
						tmp291_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp291_AST);
					}
					match(PLUS);
					break;
				}
				case MINUS:
				{
					AST tmp292_AST = null;
					if (inputState.guessing==0) {
						tmp292_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp292_AST);
					}
					match(MINUS);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				nls();
				multiplicativeExpression(0);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop394;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			additiveExpression_AST = (AST)currentAST.root;
		}
		returnAST = additiveExpression_AST;
	}

	public final void multiplicativeExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST multiplicativeExpression_AST = null;

		switch ( LA(1)) {
		case INC:
		{
			{
			AST tmp293_AST = null;
			if (inputState.guessing==0) {
				tmp293_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp293_AST);
			}
			match(INC);
			nls();
			powerExpression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			_loop399:
			do {
				if ((_tokenSet_109.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case STAR:
					{
						AST tmp294_AST = null;
						if (inputState.guessing==0) {
							tmp294_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp294_AST);
						}
						match(STAR);
						break;
					}
					case DIV:
					{
						AST tmp295_AST = null;
						if (inputState.guessing==0) {
							tmp295_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp295_AST);
						}
						match(DIV);
						break;
					}
					case MOD:
					{
						AST tmp296_AST = null;
						if (inputState.guessing==0) {
							tmp296_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp296_AST);
						}
						match(MOD);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					nls();
					powerExpression(0);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
				}
				else {
					break _loop399;
				}

			} while (true);
			}
			}
			if ( inputState.guessing==0 ) {
				multiplicativeExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		case DEC:
		{
			{
			AST tmp297_AST = null;
			if (inputState.guessing==0) {
				tmp297_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp297_AST);
			}
			match(DEC);
			nls();
			powerExpression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			_loop403:
			do {
				if ((_tokenSet_109.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case STAR:
					{
						AST tmp298_AST = null;
						if (inputState.guessing==0) {
							tmp298_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp298_AST);
						}
						match(STAR);
						break;
					}
					case DIV:
					{
						AST tmp299_AST = null;
						if (inputState.guessing==0) {
							tmp299_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp299_AST);
						}
						match(DIV);
						break;
					}
					case MOD:
					{
						AST tmp300_AST = null;
						if (inputState.guessing==0) {
							tmp300_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp300_AST);
						}
						match(MOD);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					nls();
					powerExpression(0);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
				}
				else {
					break _loop403;
				}

			} while (true);
			}
			}
			if ( inputState.guessing==0 ) {
				multiplicativeExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		case MINUS:
		{
			{
			match(MINUS);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(UNARY_MINUS, "-"));
			}
			nls();
			powerExpression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			_loop407:
			do {
				if ((_tokenSet_109.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case STAR:
					{
						AST tmp302_AST = null;
						if (inputState.guessing==0) {
							tmp302_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp302_AST);
						}
						match(STAR);
						break;
					}
					case DIV:
					{
						AST tmp303_AST = null;
						if (inputState.guessing==0) {
							tmp303_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp303_AST);
						}
						match(DIV);
						break;
					}
					case MOD:
					{
						AST tmp304_AST = null;
						if (inputState.guessing==0) {
							tmp304_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp304_AST);
						}
						match(MOD);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					nls();
					powerExpression(0);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
				}
				else {
					break _loop407;
				}

			} while (true);
			}
			}
			if ( inputState.guessing==0 ) {
				multiplicativeExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		case PLUS:
		{
			{
			match(PLUS);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(UNARY_PLUS,  "+"));
			}
			nls();
			powerExpression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			_loop411:
			do {
				if ((_tokenSet_109.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case STAR:
					{
						AST tmp306_AST = null;
						if (inputState.guessing==0) {
							tmp306_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp306_AST);
						}
						match(STAR);
						break;
					}
					case DIV:
					{
						AST tmp307_AST = null;
						if (inputState.guessing==0) {
							tmp307_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp307_AST);
						}
						match(DIV);
						break;
					}
					case MOD:
					{
						AST tmp308_AST = null;
						if (inputState.guessing==0) {
							tmp308_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp308_AST);
						}
						match(MOD);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					nls();
					powerExpression(0);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
				}
				else {
					break _loop411;
				}

			} while (true);
			}
			}
			if ( inputState.guessing==0 ) {
				multiplicativeExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		case IDENT:
		case LBRACK:
		case LPAREN:
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
		case BAND:
		case LCURLY:
		case LITERAL_this:
		case STRING_LITERAL:
		case BNOT:
		case LNOT:
		case DOLLAR:
		case STRING_CTOR_START:
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
			{
			powerExpression(lc_stmt);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			_loop415:
			do {
				if ((_tokenSet_109.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case STAR:
					{
						AST tmp309_AST = null;
						if (inputState.guessing==0) {
							tmp309_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp309_AST);
						}
						match(STAR);
						break;
					}
					case DIV:
					{
						AST tmp310_AST = null;
						if (inputState.guessing==0) {
							tmp310_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp310_AST);
						}
						match(DIV);
						break;
					}
					case MOD:
					{
						AST tmp311_AST = null;
						if (inputState.guessing==0) {
							tmp311_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp311_AST);
						}
						match(MOD);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					nls();
					powerExpression(0);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
				}
				else {
					break _loop415;
				}

			} while (true);
			}
			}
			if ( inputState.guessing==0 ) {
				multiplicativeExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = multiplicativeExpression_AST;
	}

	public final void powerExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST powerExpression_AST = null;

		unaryExpressionNotPlusMinus(lc_stmt);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop418:
		do {
			if ((LA(1)==STAR_STAR)) {
				AST tmp312_AST = null;
				if (inputState.guessing==0) {
					tmp312_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp312_AST);
				}
				match(STAR_STAR);
				nls();
				unaryExpression(0);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop418;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			powerExpression_AST = (AST)currentAST.root;
		}
		returnAST = powerExpression_AST;
	}

	public final void unaryExpressionNotPlusMinus(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST unaryExpressionNotPlusMinus_AST = null;
		Token  lpb = null;
		AST lpb_AST = null;
		Token  lp = null;
		AST lp_AST = null;

		switch ( LA(1)) {
		case BAND:
		{
			match(BAND);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(MEMBER_POINTER_DEFAULT, "&"));
			}
			nls();
			namePart();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
			}
			break;
		}
		case BNOT:
		{
			AST tmp314_AST = null;
			if (inputState.guessing==0) {
				tmp314_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp314_AST);
			}
			match(BNOT);
			nls();
			unaryExpression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
			}
			break;
		}
		case LNOT:
		{
			AST tmp315_AST = null;
			if (inputState.guessing==0) {
				tmp315_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp315_AST);
			}
			match(LNOT);
			nls();
			unaryExpression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
			}
			break;
		}
		case IDENT:
		case LBRACK:
		case LPAREN:
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
		case LCURLY:
		case LITERAL_this:
		case STRING_LITERAL:
		case DOLLAR:
		case STRING_CTOR_START:
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
			{
			boolean synPredMatched423 = false;
			if (((LA(1)==LPAREN) && ((LA(2) >= LITERAL_void && LA(2) <= LITERAL_any)) && (LA(3)==LBRACK||LA(3)==RPAREN))) {
				int _m423 = mark();
				synPredMatched423 = true;
				inputState.guessing++;
				try {
					{
					match(LPAREN);
					builtInTypeSpec(true);
					match(RPAREN);
					unaryExpression(0);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched423 = false;
				}
				rewind(_m423);
				inputState.guessing--;
			}
			if ( synPredMatched423 ) {
				lpb = LT(1);
				if (inputState.guessing==0) {
					lpb_AST = astFactory.create(lpb);
				}
				match(LPAREN);
				if ( inputState.guessing==0 ) {
					astFactory.makeASTRoot(currentAST, astFactory.create(TYPECAST, "("));
				}
				builtInTypeSpec(true);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				match(RPAREN);
				unaryExpression(0);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				boolean synPredMatched425 = false;
				if (((LA(1)==LPAREN) && (LA(2)==IDENT) && (_tokenSet_110.member(LA(3))))) {
					int _m425 = mark();
					synPredMatched425 = true;
					inputState.guessing++;
					try {
						{
						match(LPAREN);
						classTypeSpec(true);
						match(RPAREN);
						unaryExpressionNotPlusMinus(0);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched425 = false;
					}
					rewind(_m425);
					inputState.guessing--;
				}
				if ( synPredMatched425 ) {
					lp = LT(1);
					if (inputState.guessing==0) {
						lp_AST = astFactory.create(lp);
					}
					match(LPAREN);
					if ( inputState.guessing==0 ) {
						astFactory.makeASTRoot(currentAST, astFactory.create(TYPECAST, "("));
					}
					classTypeSpec(true);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					match(RPAREN);
					unaryExpressionNotPlusMinus(0);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
				}
				else if ((_tokenSet_111.member(LA(1))) && (_tokenSet_17.member(LA(2))) && (_tokenSet_11.member(LA(3)))) {
					postfixExpression(lc_stmt);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			returnAST = unaryExpressionNotPlusMinus_AST;
		}

	public final void unaryExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST unaryExpression_AST = null;

		switch ( LA(1)) {
		case INC:
		{
			AST tmp318_AST = null;
			if (inputState.guessing==0) {
				tmp318_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp318_AST);
			}
			match(INC);
			nls();
			unaryExpression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				unaryExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		case DEC:
		{
			AST tmp319_AST = null;
			if (inputState.guessing==0) {
				tmp319_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp319_AST);
			}
			match(DEC);
			nls();
			unaryExpression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				unaryExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		case MINUS:
		{
			match(MINUS);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(UNARY_MINUS, "-"));
			}
			nls();
			unaryExpression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				unaryExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		case PLUS:
		{
			match(PLUS);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(UNARY_PLUS,  "+"));
			}
			nls();
			unaryExpression(0);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				unaryExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		case IDENT:
		case LBRACK:
		case LPAREN:
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
		case BAND:
		case LCURLY:
		case LITERAL_this:
		case STRING_LITERAL:
		case BNOT:
		case LNOT:
		case DOLLAR:
		case STRING_CTOR_START:
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
			unaryExpressionNotPlusMinus(lc_stmt);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				unaryExpression_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = unaryExpression_AST;
	}

	public final void postfixExpression(
		int lc_stmt
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST postfixExpression_AST = null;

		pathExpression(lc_stmt);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		if ((LA(1)==INC) && (_tokenSet_112.member(LA(2))) && (_tokenSet_11.member(LA(3)))) {
			match(INC);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(POST_INC, "++"));
			}
		}
		else if ((LA(1)==DEC) && (_tokenSet_112.member(LA(2))) && (_tokenSet_11.member(LA(3)))) {
			match(DEC);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(POST_DEC, "--"));
			}
		}
		else if ((_tokenSet_112.member(LA(1))) && (_tokenSet_11.member(LA(2))) && (_tokenSet_11.member(LA(3)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		if ( inputState.guessing==0 ) {
			postfixExpression_AST = (AST)currentAST.root;
		}
		returnAST = postfixExpression_AST;
	}

/** Numeric, string, regexp, boolean, or null constant. */
	public final void constant() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_AST = null;

		switch ( LA(1)) {
		case NUM_INT:
		case NUM_FLOAT:
		case NUM_LONG:
		case NUM_DOUBLE:
		case NUM_BIG_INT:
		case NUM_BIG_DECIMAL:
		{
			constantNumber();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				constant_AST = (AST)currentAST.root;
			}
			break;
		}
		case STRING_LITERAL:
		{
			AST tmp324_AST = null;
			if (inputState.guessing==0) {
				tmp324_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp324_AST);
			}
			match(STRING_LITERAL);
			if ( inputState.guessing==0 ) {
				constant_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_true:
		{
			AST tmp325_AST = null;
			if (inputState.guessing==0) {
				tmp325_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp325_AST);
			}
			match(LITERAL_true);
			if ( inputState.guessing==0 ) {
				constant_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_false:
		{
			AST tmp326_AST = null;
			if (inputState.guessing==0) {
				tmp326_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp326_AST);
			}
			match(LITERAL_false);
			if ( inputState.guessing==0 ) {
				constant_AST = (AST)currentAST.root;
			}
			break;
		}
		case LITERAL_null:
		{
			AST tmp327_AST = null;
			if (inputState.guessing==0) {
				tmp327_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp327_AST);
			}
			match(LITERAL_null);
			if ( inputState.guessing==0 ) {
				constant_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = constant_AST;
	}

/** object instantiation.
 *  Trees are built as illustrated by the following input/tree pairs:
 *
 *  new T()
 *
 *  new
 *   |
 *   T --  ELIST
 *                 |
 *                arg1 -- arg2 -- .. -- argn
 *
 *  new int[]
 *
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR
 *
 *  new int[] {1,2}
 *
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR -- ARRAY_INIT
 *                                                                |
 *                                                              EXPR -- EXPR
 *                                                                |   |
 *                                                                1       2
 *
 *  new int[3]
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR
 *                              |
 *                        EXPR
 *                              |
 *                              3
 *
 *  new int[1][2]
 *
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR
 *                         |
 *               ARRAY_DECLARATOR -- EXPR
 *                         |                  |
 *                       EXPR                    1
 *                         |
 *                         2
 *
 */
	public final void newExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST newExpression_AST = null;
		AST mca_AST = null;
		AST apb1_AST = null;
		AST apb_AST = null;

		AST tmp328_AST = null;
		if (inputState.guessing==0) {
			tmp328_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp328_AST);
		}
		match(LITERAL_new);
		{
		switch ( LA(1)) {
		case LT:
		{
			typeArguments();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
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
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		type();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		switch ( LA(1)) {
		case LPAREN:
		{
			methodCallArgs(null);
			if (inputState.guessing==0) {
				mca_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			if ((LA(1)==LCURLY) && (_tokenSet_16.member(LA(2))) && (_tokenSet_17.member(LA(3)))) {
				appendedBlock(mca_AST);
				if (inputState.guessing==0) {
					apb1_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
				if ( inputState.guessing==0 ) {
					mca_AST = apb1_AST;
				}
			}
			else if ((_tokenSet_113.member(LA(1))) && (_tokenSet_11.member(LA(2))) && (_tokenSet_11.member(LA(3)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}

			}
			if ( inputState.guessing==0 ) {
				newExpression_AST = (AST)currentAST.root;
				newExpression_AST.addChild(mca_AST.getFirstChild());
			}
			break;
		}
		case LCURLY:
		{
			appendedBlock(null);
			if (inputState.guessing==0) {
				apb_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				newExpression_AST = (AST)currentAST.root;
				newExpression_AST.addChild(apb_AST.getFirstChild());
			}
			break;
		}
		case LBRACK:
		{
			newArrayDeclarator();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			newExpression_AST = (AST)currentAST.root;
		}
		returnAST = newExpression_AST;
	}

	public final void closureConstructorExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST closureConstructorExpression_AST = null;

		closedBlock();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			closureConstructorExpression_AST = (AST)currentAST.root;
		}
		returnAST = closureConstructorExpression_AST;
	}

/**
 * A list constructor is a argument list enclosed in square brackets, without labels.
 * Any argument can be decorated with a spread operator (*x), but not a label (a:x).
 * Examples:  [], [1], [1,2], [1,*l1,2], [*l1,*l2].
 * (The l1, l2 must be a sequence or null.)
 * <p>
 * A map constructor is an argument list enclosed in square brackets, with labels everywhere,
 * except on spread arguments, which stand for whole maps spliced in.
 * A colon alone between the brackets also forces the expression to be an empty map constructor.
 * Examples: [:], [a:1], [a:1,b:2], [a:1,*:m1,b:2], [*:m1,*:m2]
 * (The m1, m2 must be a map or null.)
 * Values associated with identical keys overwrite from left to right:
 * [a:1,a:2]  ===  [a:2]
 * <p>
 * Some malformed constructor expressions are not detected in the parser, but in a post-pass.
 * Bad examples: [1,b:2], [a:1,2], [:1].
 * (Note that method call arguments, by contrast, can be a mix of keyworded and non-keyworded arguments.)
 */
	public final void listOrMapConstructorExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST listOrMapConstructorExpression_AST = null;
		Token  lcon = null;
		AST lcon_AST = null;
		boolean hasLabels = false;

		if ((LA(1)==LBRACK) && (_tokenSet_114.member(LA(2)))) {
			lcon = LT(1);
			if (inputState.guessing==0) {
				lcon_AST = astFactory.create(lcon);
				astFactory.makeASTRoot(currentAST, lcon_AST);
			}
			match(LBRACK);
			argList();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				hasLabels |= argListHasLabels;
			}
			match(RBRACK);
			if ( inputState.guessing==0 ) {
				lcon_AST.setType(hasLabels ? MAP_CONSTRUCTOR : LIST_CONSTRUCTOR);
			}
			if ( inputState.guessing==0 ) {
				listOrMapConstructorExpression_AST = (AST)currentAST.root;
			}
		}
		else if ((LA(1)==LBRACK) && (LA(2)==COLON)) {
			match(LBRACK);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(MAP_CONSTRUCTOR, "["));
			}
			match(COLON);
			match(RBRACK);
			if ( inputState.guessing==0 ) {
				listOrMapConstructorExpression_AST = (AST)currentAST.root;
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		returnAST = listOrMapConstructorExpression_AST;
	}

	public final void scopeEscapeExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST scopeEscapeExpression_AST = null;

		match(DOLLAR);
		if ( inputState.guessing==0 ) {
			astFactory.makeASTRoot(currentAST, astFactory.create(SCOPE_ESCAPE, "$"));
		}
		{
		switch ( LA(1)) {
		case IDENT:
		{
			AST tmp334_AST = null;
			if (inputState.guessing==0) {
				tmp334_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp334_AST);
			}
			match(IDENT);
			break;
		}
		case DOLLAR:
		{
			scopeEscapeExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			scopeEscapeExpression_AST = (AST)currentAST.root;
		}
		returnAST = scopeEscapeExpression_AST;
	}

	public final void stringConstructorValuePart() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST stringConstructorValuePart_AST = null;

		{
		switch ( LA(1)) {
		case STAR:
		{
			match(STAR);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(SPREAD_ARG, "*"));
			}
			break;
		}
		case IDENT:
		case LCURLY:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case IDENT:
		{
			identifier();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case LCURLY:
		{
			openOrClosedBlock();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			stringConstructorValuePart_AST = (AST)currentAST.root;
		}
		returnAST = stringConstructorValuePart_AST;
	}

	public final void newArrayDeclarator() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST newArrayDeclarator_AST = null;

		{
		int _cnt470=0;
		_loop470:
		do {
			if ((LA(1)==LBRACK) && (_tokenSet_115.member(LA(2))) && (_tokenSet_17.member(LA(3)))) {
				match(LBRACK);
				if ( inputState.guessing==0 ) {
					astFactory.makeASTRoot(currentAST, astFactory.create(ARRAY_DECLARATOR, "["));
				}
				{
				switch ( LA(1)) {
				case IDENT:
				case LBRACK:
				case LPAREN:
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
				case BAND:
				case LCURLY:
				case LITERAL_this:
				case STRING_LITERAL:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case DOLLAR:
				case STRING_CTOR_START:
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
					expression(0);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case RBRACK:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(RBRACK);
			}
			else {
				if ( _cnt470>=1 ) { break _loop470; } else {throw new NoViableAltException(LT(1), getFilename());}
			}

			_cnt470++;
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			newArrayDeclarator_AST = (AST)currentAST.root;
		}
		returnAST = newArrayDeclarator_AST;
	}

/** A single argument in (...) or [...].  Corresponds to to a method or closure parameter.
 *  May be labeled.  May be modified by the spread operator '*' ('*:' for keywords).
 */
	public final boolean  argument() throws RecognitionException, TokenStreamException {
		boolean hasLabel = false;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST argument_AST = null;
		Token  sp = null;
		AST sp_AST = null;

		{
		boolean synPredMatched456 = false;
		if (((_tokenSet_116.member(LA(1))) && (_tokenSet_117.member(LA(2))) && (_tokenSet_96.member(LA(3))))) {
			int _m456 = mark();
			synPredMatched456 = true;
			inputState.guessing++;
			try {
				{
				argumentLabelStart();
				}
			}
			catch (RecognitionException pe) {
				synPredMatched456 = false;
			}
			rewind(_m456);
			inputState.guessing--;
		}
		if ( synPredMatched456 ) {
			argumentLabel();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			match(COLON);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(LABELED_ARG, ":"));
			}
			if ( inputState.guessing==0 ) {
				hasLabel = true;
			}
		}
		else if ((LA(1)==STAR)) {
			sp = LT(1);
			if (inputState.guessing==0) {
				sp_AST = astFactory.create(sp);
			}
			match(STAR);
			if ( inputState.guessing==0 ) {
				astFactory.makeASTRoot(currentAST, astFactory.create(SPREAD_ARG, "*"));
			}
			{
			switch ( LA(1)) {
			case COLON:
			{
				AST tmp339_AST = null;
				if (inputState.guessing==0) {
					tmp339_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp339_AST);
				}
				match(COLON);
				if ( inputState.guessing==0 ) {
					sp_AST.setType(SPREAD_MAP_ARG);
				}
				if ( inputState.guessing==0 ) {
					hasLabel = true;
				}
				break;
			}
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case LITERAL_static:
			case LITERAL_def:
			case AT:
			case IDENT:
			case LBRACK:
			case LPAREN:
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
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_threadsafe:
			case LITERAL_synchronized:
			case LITERAL_volatile:
			case BAND:
			case LCURLY:
			case LITERAL_this:
			case STRING_LITERAL:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_continue:
			case LITERAL_throw:
			case LITERAL_assert:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case DOLLAR:
			case STRING_CTOR_START:
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
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		else if ((_tokenSet_118.member(LA(1))) && (_tokenSet_64.member(LA(2))) && (_tokenSet_20.member(LA(3)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		strictContextExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			argument_AST = (AST)currentAST.root;
		}
		returnAST = argument_AST;
		return hasLabel;
	}

/** For lookahead only.  Fast approximate parse of an argumentLabel followed by a colon. */
	public final void argumentLabelStart() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST argumentLabelStart_AST = null;

		{
		switch ( LA(1)) {
		case IDENT:
		{
			AST tmp340_AST = null;
			if (inputState.guessing==0) {
				tmp340_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp340_AST);
			}
			match(IDENT);
			break;
		}
		case UNUSED_DO:
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
		case LITERAL_if:
		case LITERAL_else:
		case LITERAL_while:
		case LITERAL_switch:
		case LITERAL_for:
		case LITERAL_in:
		case LITERAL_try:
		case LITERAL_finally:
		case LITERAL_catch:
		{
			keywordPropertyNames();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case NUM_INT:
		case NUM_FLOAT:
		case NUM_LONG:
		case NUM_DOUBLE:
		case NUM_BIG_INT:
		case NUM_BIG_DECIMAL:
		{
			constantNumber();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case STRING_LITERAL:
		{
			AST tmp341_AST = null;
			if (inputState.guessing==0) {
				tmp341_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp341_AST);
			}
			match(STRING_LITERAL);
			break;
		}
		case LBRACK:
		case LPAREN:
		case LCURLY:
		case STRING_CTOR_START:
		{
			balancedBrackets();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		AST tmp342_AST = null;
		if (inputState.guessing==0) {
			tmp342_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp342_AST);
		}
		match(COLON);
		if ( inputState.guessing==0 ) {
			argumentLabelStart_AST = (AST)currentAST.root;
		}
		returnAST = argumentLabelStart_AST;
	}

/** A label for an argument is of the form a:b, 'a':b, "a":b, (a):b, etc..
 *      The labels in (a:b), ('a':b), and ("a":b) are in all ways equivalent,
 *      except that the quotes allow more spellings.
 *  Equivalent dynamically computed labels are (('a'):b) and ("${'a'}":b)
 *  but not ((a):b) or "$a":b, since the latter cases evaluate (a) as a normal identifier.
 *      Bottom line:  If you want a truly variable label, use parens and say ((a):b).
 */
	public final void argumentLabel() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST argumentLabel_AST = null;
		Token  id = null;
		AST id_AST = null;
		AST kw_AST = null;

		boolean synPredMatched460 = false;
		if (((LA(1)==IDENT) && (LA(2)==COLON) && (_tokenSet_118.member(LA(3))))) {
			int _m460 = mark();
			synPredMatched460 = true;
			inputState.guessing++;
			try {
				{
				match(IDENT);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched460 = false;
			}
			rewind(_m460);
			inputState.guessing--;
		}
		if ( synPredMatched460 ) {
			id = LT(1);
			if (inputState.guessing==0) {
				id_AST = astFactory.create(id);
				astFactory.addASTChild(currentAST, id_AST);
			}
			match(IDENT);
			if ( inputState.guessing==0 ) {
				id_AST.setType(STRING_LITERAL);
			}
			if ( inputState.guessing==0 ) {
				argumentLabel_AST = (AST)currentAST.root;
			}
		}
		else {
			boolean synPredMatched462 = false;
			if (((_tokenSet_119.member(LA(1))) && (LA(2)==COLON) && (_tokenSet_118.member(LA(3))))) {
				int _m462 = mark();
				synPredMatched462 = true;
				inputState.guessing++;
				try {
					{
					keywordPropertyNames();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched462 = false;
				}
				rewind(_m462);
				inputState.guessing--;
			}
			if ( synPredMatched462 ) {
				keywordPropertyNames();
				if (inputState.guessing==0) {
					kw_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
				if ( inputState.guessing==0 ) {
					kw_AST.setType(STRING_LITERAL);
				}
				if ( inputState.guessing==0 ) {
					argumentLabel_AST = (AST)currentAST.root;
				}
			}
			else if ((_tokenSet_111.member(LA(1))) && (_tokenSet_117.member(LA(2))) && (_tokenSet_96.member(LA(3)))) {
				primaryExpression();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				if ( inputState.guessing==0 ) {
					argumentLabel_AST = (AST)currentAST.root;
				}
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			returnAST = argumentLabel_AST;
		}

/** Numeric constant. */
	public final void constantNumber() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constantNumber_AST = null;

		switch ( LA(1)) {
		case NUM_INT:
		{
			AST tmp343_AST = null;
			if (inputState.guessing==0) {
				tmp343_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp343_AST);
			}
			match(NUM_INT);
			if ( inputState.guessing==0 ) {
				constantNumber_AST = (AST)currentAST.root;
			}
			break;
		}
		case NUM_FLOAT:
		{
			AST tmp344_AST = null;
			if (inputState.guessing==0) {
				tmp344_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp344_AST);
			}
			match(NUM_FLOAT);
			if ( inputState.guessing==0 ) {
				constantNumber_AST = (AST)currentAST.root;
			}
			break;
		}
		case NUM_LONG:
		{
			AST tmp345_AST = null;
			if (inputState.guessing==0) {
				tmp345_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp345_AST);
			}
			match(NUM_LONG);
			if ( inputState.guessing==0 ) {
				constantNumber_AST = (AST)currentAST.root;
			}
			break;
		}
		case NUM_DOUBLE:
		{
			AST tmp346_AST = null;
			if (inputState.guessing==0) {
				tmp346_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp346_AST);
			}
			match(NUM_DOUBLE);
			if ( inputState.guessing==0 ) {
				constantNumber_AST = (AST)currentAST.root;
			}
			break;
		}
		case NUM_BIG_INT:
		{
			AST tmp347_AST = null;
			if (inputState.guessing==0) {
				tmp347_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp347_AST);
			}
			match(NUM_BIG_INT);
			if ( inputState.guessing==0 ) {
				constantNumber_AST = (AST)currentAST.root;
			}
			break;
		}
		case NUM_BIG_DECIMAL:
		{
			AST tmp348_AST = null;
			if (inputState.guessing==0) {
				tmp348_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp348_AST);
			}
			match(NUM_BIG_DECIMAL);
			if ( inputState.guessing==0 ) {
				constantNumber_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = constantNumber_AST;
	}

/** Fast lookahead across balanced brackets of all sorts. */
	public final void balancedBrackets() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST balancedBrackets_AST = null;

		switch ( LA(1)) {
		case LPAREN:
		{
			AST tmp349_AST = null;
			if (inputState.guessing==0) {
				tmp349_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp349_AST);
			}
			match(LPAREN);
			balancedTokens();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp350_AST = null;
			if (inputState.guessing==0) {
				tmp350_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp350_AST);
			}
			match(RPAREN);
			if ( inputState.guessing==0 ) {
				balancedBrackets_AST = (AST)currentAST.root;
			}
			break;
		}
		case LBRACK:
		{
			AST tmp351_AST = null;
			if (inputState.guessing==0) {
				tmp351_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp351_AST);
			}
			match(LBRACK);
			balancedTokens();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp352_AST = null;
			if (inputState.guessing==0) {
				tmp352_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp352_AST);
			}
			match(RBRACK);
			if ( inputState.guessing==0 ) {
				balancedBrackets_AST = (AST)currentAST.root;
			}
			break;
		}
		case LCURLY:
		{
			AST tmp353_AST = null;
			if (inputState.guessing==0) {
				tmp353_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp353_AST);
			}
			match(LCURLY);
			balancedTokens();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp354_AST = null;
			if (inputState.guessing==0) {
				tmp354_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp354_AST);
			}
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				balancedBrackets_AST = (AST)currentAST.root;
			}
			break;
		}
		case STRING_CTOR_START:
		{
			AST tmp355_AST = null;
			if (inputState.guessing==0) {
				tmp355_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp355_AST);
			}
			match(STRING_CTOR_START);
			balancedTokens();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp356_AST = null;
			if (inputState.guessing==0) {
				tmp356_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp356_AST);
			}
			match(STRING_CTOR_END);
			if ( inputState.guessing==0 ) {
				balancedBrackets_AST = (AST)currentAST.root;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = balancedBrackets_AST;
	}


	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"BLOCK",
		"MODIFIERS",
		"OBJBLOCK",
		"SLIST",
		"METHOD_DEF",
		"VARIABLE_DEF",
		"INSTANCE_INIT",
		"STATIC_INIT",
		"TYPE",
		"CLASS_DEF",
		"INTERFACE_DEF",
		"PACKAGE_DEF",
		"ARRAY_DECLARATOR",
		"EXTENDS_CLAUSE",
		"IMPLEMENTS_CLAUSE",
		"PARAMETERS",
		"PARAMETER_DEF",
		"LABELED_STAT",
		"TYPECAST",
		"INDEX_OP",
		"POST_INC",
		"POST_DEC",
		"METHOD_CALL",
		"EXPR",
		"IMPORT",
		"UNARY_MINUS",
		"UNARY_PLUS",
		"CASE_GROUP",
		"ELIST",
		"FOR_INIT",
		"FOR_CONDITION",
		"FOR_ITERATOR",
		"EMPTY_STAT",
		"\"final\"",
		"\"abstract\"",
		"\"goto\"",
		"\"const\"",
		"\"do\"",
		"\"strictfp\"",
		"SUPER_CTOR_CALL",
		"CTOR_CALL",
		"CTOR_IDENT",
		"VARIABLE_PARAMETER_DEF",
		"STRING_CONSTRUCTOR",
		"STRING_CTOR_MIDDLE",
		"CLOSED_BLOCK",
		"IMPLICIT_PARAMETERS",
		"SELECT_SLOT",
		"DYNAMIC_MEMBER",
		"LABELED_ARG",
		"SPREAD_ARG",
		"SPREAD_MAP_ARG",
		"SCOPE_ESCAPE",
		"LIST_CONSTRUCTOR",
		"MAP_CONSTRUCTOR",
		"FOR_IN_ITERABLE",
		"STATIC_IMPORT",
		"ENUM_DEF",
		"ENUM_CONSTANT_DEF",
		"FOR_EACH_CLAUSE",
		"ANNOTATION_DEF",
		"ANNOTATIONS",
		"ANNOTATION",
		"ANNOTATION_MEMBER_VALUE_PAIR",
		"ANNOTATION_FIELD_DEF",
		"ANNOTATION_ARRAY_INIT",
		"TYPE_ARGUMENTS",
		"TYPE_ARGUMENT",
		"TYPE_PARAMETERS",
		"TYPE_PARAMETER",
		"WILDCARD_TYPE",
		"TYPE_UPPER_BOUNDS",
		"TYPE_LOWER_BOUNDS",
		"a script header",
		"\"package\"",
		"\"import\"",
		"\"static\"",
		"\"def\"",
		"'@'",
		"an identifier",
		"'['",
		"']'",
		"'.'",
		"'('",
		"\"class\"",
		"\"interface\"",
		"\"enum\"",
		"'?'",
		"\"extends\"",
		"\"super\"",
		"'<'",
		"','",
		"'>'",
		"'>>'",
		"'>>>'",
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
		"'*'",
		"\"as\"",
		"\"private\"",
		"\"public\"",
		"\"protected\"",
		"\"transient\"",
		"\"native\"",
		"\"threadsafe\"",
		"\"synchronized\"",
		"\"volatile\"",
		"')'",
		"'='",
		"'&'",
		"'{'",
		"'}'",
		"';'",
		"some newlines, whitespace or comments",
		"\"default\"",
		"\"implements\"",
		"\"this\"",
		"a string literal",
		"\"throws\"",
		"'...'",
		"'->'",
		"'||'",
		"'|'",
		"':'",
		"\"if\"",
		"\"else\"",
		"\"while\"",
		"\"with\"",
		"\"switch\"",
		"\"for\"",
		"\"in\"",
		"\"return\"",
		"\"break\"",
		"\"continue\"",
		"\"throw\"",
		"\"assert\"",
		"'+'",
		"'-'",
		"\"case\"",
		"\"try\"",
		"\"finally\"",
		"\"catch\"",
		"'*.'",
		"'?.'",
		"'.&'",
		"MEMBER_POINTER_DEFAULT",
		"'+='",
		"'-='",
		"'*='",
		"'/='",
		"'%='",
		"'>>='",
		"'>>>='",
		"'<<='",
		"'&='",
		"'^='",
		"'|='",
		"'**='",
		"'&&'",
		"'^'",
		"'=~'",
		"'==~'",
		"'!='",
		"'=='",
		"'<=>'",
		"'<='",
		"'>='",
		"\"instanceof\"",
		"'<<'",
		"'..'",
		"'..<'",
		"'++'",
		"'/'",
		"'%'",
		"'--'",
		"'**'",
		"'~'",
		"'!'",
		"'$'",
		"STRING_CTOR_START",
		"a string literal end",
		"\"new\"",
		"\"true\"",
		"\"false\"",
		"\"null\"",
		"a numeric literal",
		"NUM_FLOAT",
		"NUM_LONG",
		"NUM_DOUBLE",
		"NUM_BIG_INT",
		"NUM_BIG_DECIMAL",
		"whitespace",
		"a newline",
		"a single line comment",
		"a comment",
		"a string character",
		"a regular expression literal",
		"a regular expression literal end",
		"a regular expression character",
		"an escape sequence",
		"a newline inside a string",
		"a hexadecimal digit",
		"a character",
		"a letter",
		"a digit",
		"an exponent",
		"a float or double suffix",
		"a big decimal suffix"
	};

	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};

	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 3458764513833402368L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = new long[8];
		data[0]=4810363371522L;
		data[1]=3927068472627920896L;
		data[2]=-828662331423605501L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = new long[8];
		data[0]=7009386627074L;
		data[1]=4575657221139955712L;
		data[2]=-541065221L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = new long[8];
		data[0]=288484363337730L;
		data[1]=-4611686018427420672L;
		data[2]=-541065221L;
		data[3]=2047L;
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = new long[8];
		data[0]=7009386627074L;
		data[1]=-16384L;
		data[2]=-828099382489382917L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = new long[8];
		data[0]=288484363337730L;
		data[1]=-16384L;
		data[2]=-536870913L;
		data[3]=2047L;
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 0L, 3458764513820540928L, 512L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = new long[8];
		data[0]=4810363371520L;
		data[1]=3927068472627920896L;
		data[2]=-828662331423604989L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = new long[8];
		data[0]=7009386627074L;
		data[1]=9187343239567343616L;
		data[2]=-536870917L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 2L, 8646911284551352320L, 4194816L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = new long[8];
		data[0]=286285340082178L;
		data[1]=9223372036586307584L;
		data[2]=-536870917L;
		data[3]=2047L;
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = new long[8];
		data[0]=288484363337730L;
		data[1]=9223372036586323968L;
		data[2]=-536870913L;
		data[3]=2047L;
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 4810363371520L, 35923209543942144L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { 4810363371520L, 2341766219836620800L, 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 4810363371522L, 8754892091504394240L, 4194818L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = new long[8];
		data[0]=4810363371520L;
		data[1]=468303958807379968L;
		data[2]=-828662331423605501L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = new long[8];
		data[0]=4810363371520L;
		data[1]=4503529224931344384L;
		data[2]=-828662331423605381L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = new long[8];
		data[0]=7009386627074L;
		data[1]=9223372036586307584L;
		data[2]=-536870917L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = new long[8];
		data[0]=288484363337730L;
		data[1]=-32768L;
		data[2]=-536870917L;
		data[3]=2047L;
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = new long[8];
		data[1]=432380714786750464L;
		data[2]=-828662331433025533L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	private static final long[] mk_tokenSet_20() {
		long[] data = new long[8];
		data[0]=288484363337730L;
		data[1]=9223372036586307584L;
		data[2]=-536870917L;
		data[3]=2047L;
		return data;
	}
	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
	private static final long[] mk_tokenSet_21() {
		long[] data = { 4810363371520L, 35888059648507904L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
	private static final long[] mk_tokenSet_22() {
		long[] data = { 4810363371520L, 2341731068862726144L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());
	private static final long[] mk_tokenSet_23() {
		long[] data = { 4810363371520L, -6593410590485577728L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());
	private static final long[] mk_tokenSet_24() {
		long[] data = new long[8];
		data[0]=4810363371522L;
		data[1]=9115215244432474112L;
		data[2]=-828662331419410685L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());
	private static final long[] mk_tokenSet_25() {
		long[] data = { 2L, 8646981653300248576L, 4194816L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());
	private static final long[] mk_tokenSet_26() {
		long[] data = { 0L, 35150012874752L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());
	private static final long[] mk_tokenSet_27() {
		long[] data = { 0L, 1079508992L, 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_27 = new BitSet(mk_tokenSet_27());
	private static final long[] mk_tokenSet_28() {
		long[] data = { 2L, 8718968880745152512L, 4194816L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_28 = new BitSet(mk_tokenSet_28());
	private static final long[] mk_tokenSet_29() {
		long[] data = { 2L, 8718968880736763904L, 4194816L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_29 = new BitSet(mk_tokenSet_29());
	private static final long[] mk_tokenSet_30() {
		long[] data = { 0L, 1079508992L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_30 = new BitSet(mk_tokenSet_30());
	private static final long[] mk_tokenSet_31() {
		long[] data = { 0L, 1261007897813319680L, 16512L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_31 = new BitSet(mk_tokenSet_31());
	private static final long[] mk_tokenSet_32() {
		long[] data = { 0L, 288230376161148928L, -9223372036854775808L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_32 = new BitSet(mk_tokenSet_32());
	private static final long[] mk_tokenSet_33() {
		long[] data = new long[16];
		data[0]=-16L;
		data[1]=-900719925485633537L;
		data[2]=9223372036854775807L;
		data[3]=268435454L;
		return data;
	}
	public static final BitSet _tokenSet_33 = new BitSet(mk_tokenSet_33());
	private static final long[] mk_tokenSet_34() {
		long[] data = { 4810363371520L, 35888059531067392L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_34 = new BitSet(mk_tokenSet_34());
	private static final long[] mk_tokenSet_35() {
		long[] data = { 4810363371520L, 2341766219948818432L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_35 = new BitSet(mk_tokenSet_35());
	private static final long[] mk_tokenSet_36() {
		long[] data = { 4810363371522L, 2341766219962449920L, 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_36 = new BitSet(mk_tokenSet_36());
	private static final long[] mk_tokenSet_37() {
		long[] data = { 0L, 35151204319232L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_37 = new BitSet(mk_tokenSet_37());
	private static final long[] mk_tokenSet_38() {
		long[] data = { 2L, 2305843010335145984L, 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_38 = new BitSet(mk_tokenSet_38());
	private static final long[] mk_tokenSet_39() {
		long[] data = { 137438953474L, -4791794819809804288L, 8L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_39 = new BitSet(mk_tokenSet_39());
	private static final long[] mk_tokenSet_40() {
		long[] data = new long[8];
		data[0]=2199023255554L;
		data[1]=-35923244003491840L;
		data[2]=-828099382490400773L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_40 = new BitSet(mk_tokenSet_40());
	private static final long[] mk_tokenSet_41() {
		long[] data = new long[8];
		data[0]=2199023255554L;
		data[1]=-35923245077233664L;
		data[2]=-828099382490400773L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_41 = new BitSet(mk_tokenSet_41());
	private static final long[] mk_tokenSet_42() {
		long[] data = { 0L, 2305878159360786432L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_42 = new BitSet(mk_tokenSet_42());
	private static final long[] mk_tokenSet_43() {
		long[] data = { 4810363371520L, 35888059530674176L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_43 = new BitSet(mk_tokenSet_43());
	private static final long[] mk_tokenSet_44() {
		long[] data = { 4810363371520L, 2341766219961401344L, 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_44 = new BitSet(mk_tokenSet_44());
	private static final long[] mk_tokenSet_45() {
		long[] data = new long[8];
		data[1]=432380714787012608L;
		data[2]=-828662331433025533L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_45 = new BitSet(mk_tokenSet_45());
	private static final long[] mk_tokenSet_46() {
		long[] data = new long[8];
		data[0]=7009386627072L;
		data[1]=4539628424120991744L;
		data[2]=-4397513834501L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_46 = new BitSet(mk_tokenSet_46());
	private static final long[] mk_tokenSet_47() {
		long[] data = { 0L, 4323455644432072704L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_47 = new BitSet(mk_tokenSet_47());
	private static final long[] mk_tokenSet_48() {
		long[] data = new long[8];
		data[0]=7009386627074L;
		data[1]=9151314412347260928L;
		data[2]=-828662331369064701L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_48 = new BitSet(mk_tokenSet_48());
	private static final long[] mk_tokenSet_49() {
		long[] data = { 4810363371520L, 4359378851937058816L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_49 = new BitSet(mk_tokenSet_49());
	private static final long[] mk_tokenSet_50() {
		long[] data = new long[8];
		data[0]=4810363371522L;
		data[1]=9115215244436668416L;
		data[2]=-828662331419410685L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_50 = new BitSet(mk_tokenSet_50());
	private static final long[] mk_tokenSet_51() {
		long[] data = { 0L, -6485148279842013184L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_51 = new BitSet(mk_tokenSet_51());
	private static final long[] mk_tokenSet_52() {
		long[] data = { 0L, -6629263468995805184L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_52 = new BitSet(mk_tokenSet_52());
	private static final long[] mk_tokenSet_53() {
		long[] data = { 4810363371520L, -4863993153505525760L, 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_53 = new BitSet(mk_tokenSet_53());
	private static final long[] mk_tokenSet_54() {
		long[] data = new long[8];
		data[0]=4810363371522L;
		data[1]=-36099165763174400L;
		data[2]=-828662331419410685L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_54 = new BitSet(mk_tokenSet_54());
	private static final long[] mk_tokenSet_55() {
		long[] data = { 4810363371520L, 35888059531591680L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_55 = new BitSet(mk_tokenSet_55());
	private static final long[] mk_tokenSet_56() {
		long[] data = { 4810363371520L, 2341731068753674240L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_56 = new BitSet(mk_tokenSet_56());
	private static final long[] mk_tokenSet_57() {
		long[] data = { 4810363371520L, 2377795015789182976L, 8L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_57 = new BitSet(mk_tokenSet_57());
	private static final long[] mk_tokenSet_58() {
		long[] data = { 4810363371520L, 4143206073077006336L, 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_58 = new BitSet(mk_tokenSet_58());
	private static final long[] mk_tokenSet_59() {
		long[] data = { 0L, 4107282862317764608L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_59 = new BitSet(mk_tokenSet_59());
	private static final long[] mk_tokenSet_60() {
		long[] data = new long[8];
		data[0]=4810363371522L;
		data[1]=9151208856005574656L;
		data[2]=-828662331428830709L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_60 = new BitSet(mk_tokenSet_60());
	private static final long[] mk_tokenSet_61() {
		long[] data = { 0L, 2305843009214480384L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_61 = new BitSet(mk_tokenSet_61());
	private static final long[] mk_tokenSet_62() {
		long[] data = { 0L, 4323455644432334848L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_62 = new BitSet(mk_tokenSet_62());
	private static final long[] mk_tokenSet_63() {
		long[] data = new long[8];
		data[0]=7009386627072L;
		data[1]=468374327450861568L;
		data[2]=-828662331373259005L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_63 = new BitSet(mk_tokenSet_63());
	private static final long[] mk_tokenSet_64() {
		long[] data = new long[8];
		data[0]=7009386627072L;
		data[1]=4611686018158919680L;
		data[2]=-541065221L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_64 = new BitSet(mk_tokenSet_64());
	private static final long[] mk_tokenSet_65() {
		long[] data = { 137438953472L, 36063947032231936L, 8L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_65 = new BitSet(mk_tokenSet_65());
	private static final long[] mk_tokenSet_66() {
		long[] data = new long[8];
		data[0]=4810363371520L;
		data[1]=4539522840799412224L;
		data[2]=-828662331433025525L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_66 = new BitSet(mk_tokenSet_66());
	private static final long[] mk_tokenSet_67() {
		long[] data = { 0L, 1610612736L, 1L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_67 = new BitSet(mk_tokenSet_67());
	private static final long[] mk_tokenSet_68() {
		long[] data = { 0L, 2305878159369175040L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_68 = new BitSet(mk_tokenSet_68());
	private static final long[] mk_tokenSet_69() {
		long[] data = new long[8];
		data[0]=7009386627072L;
		data[1]=2810246167376363520L;
		data[2]=-828662331373259005L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_69 = new BitSet(mk_tokenSet_69());
	private static final long[] mk_tokenSet_70() {
		long[] data = new long[8];
		data[0]=4810363371520L;
		data[1]=4503529224931344384L;
		data[2]=-828662331423605501L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_70 = new BitSet(mk_tokenSet_70());
	private static final long[] mk_tokenSet_71() {
		long[] data = new long[8];
		data[0]=7009386627072L;
		data[1]=4575657221139955712L;
		data[2]=-541065221L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_71 = new BitSet(mk_tokenSet_71());
	private static final long[] mk_tokenSet_72() {
		long[] data = new long[8];
		data[0]=4810363371522L;
		data[1]=9115215243358732288L;
		data[2]=-828662331419410685L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_72 = new BitSet(mk_tokenSet_72());
	private static final long[] mk_tokenSet_73() {
		long[] data = { 0L, 1079508992L, 8L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_73 = new BitSet(mk_tokenSet_73());
	private static final long[] mk_tokenSet_74() {
		long[] data = { 0L, 2413964552567259136L, 16L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_74 = new BitSet(mk_tokenSet_74());
	private static final long[] mk_tokenSet_75() {
		long[] data = { 0L, 2413929402418593792L, 16L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_75 = new BitSet(mk_tokenSet_75());
	private static final long[] mk_tokenSet_76() {
		long[] data = new long[8];
		data[0]=4810363371522L;
		data[1]=9223301636563107840L;
		data[2]=-828662331419410593L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_76 = new BitSet(mk_tokenSet_76());
	private static final long[] mk_tokenSet_77() {
		long[] data = { 0L, 2305843011361177600L, 64L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_77 = new BitSet(mk_tokenSet_77());
	private static final long[] mk_tokenSet_78() {
		long[] data = { 137438953472L, 2305878159226961920L, 24L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_78 = new BitSet(mk_tokenSet_78());
	private static final long[] mk_tokenSet_79() {
		long[] data = new long[8];
		data[0]=4810363371520L;
		data[1]=4575586822194692096L;
		data[2]=-828662331423605477L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_79 = new BitSet(mk_tokenSet_79());
	private static final long[] mk_tokenSet_80() {
		long[] data = { 0L, 35150021263360L, 96L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_80 = new BitSet(mk_tokenSet_80());
	private static final long[] mk_tokenSet_81() {
		long[] data = new long[8];
		data[0]=4810363371520L;
		data[1]=4539558025175728128L;
		data[2]=-828662331423605429L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_81 = new BitSet(mk_tokenSet_81());
	private static final long[] mk_tokenSet_82() {
		long[] data = new long[8];
		data[0]=4810363371520L;
		data[1]=4503529224931344384L;
		data[2]=-828662331423605437L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_82 = new BitSet(mk_tokenSet_82());
	private static final long[] mk_tokenSet_83() {
		long[] data = { 137438953472L, 2341906956254314496L, 8L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_83 = new BitSet(mk_tokenSet_83());
	private static final long[] mk_tokenSet_84() {
		long[] data = { 137438953472L, 2413964553518710784L, 72L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_84 = new BitSet(mk_tokenSet_84());
	private static final long[] mk_tokenSet_85() {
		long[] data = { 0L, 35150012874752L, 64L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_85 = new BitSet(mk_tokenSet_85());
	private static final long[] mk_tokenSet_86() {
		long[] data = { 0L, 2305878162453037056L, 64L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_86 = new BitSet(mk_tokenSet_86());
	private static final long[] mk_tokenSet_87() {
		long[] data = new long[8];
		data[0]=4810363371520L;
		data[1]=4503529228293079040L;
		data[2]=-828662331423605437L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_87 = new BitSet(mk_tokenSet_87());
	private static final long[] mk_tokenSet_88() {
		long[] data = new long[16];
		data[0]=-14L;
		data[1]=-576460752305520641L;
		data[2]=-1L;
		data[3]=268435454L;
		return data;
	}
	public static final BitSet _tokenSet_88 = new BitSet(mk_tokenSet_88());
	private static final long[] mk_tokenSet_89() {
		long[] data = new long[16];
		data[0]=-14L;
		for (int i = 1; i<=2; i++) { data[i]=-1L; }
		data[3]=268435455L;
		return data;
	}
	public static final BitSet _tokenSet_89 = new BitSet(mk_tokenSet_89());
	private static final long[] mk_tokenSet_90() {
		long[] data = { 137438953474L, 2377935756491358208L, 24L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_90 = new BitSet(mk_tokenSet_90());
	private static final long[] mk_tokenSet_91() {
		long[] data = new long[8];
		data[0]=137438953474L;
		data[1]=2810281321400500224L;
		data[2]=-828662331433025509L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_91 = new BitSet(mk_tokenSet_91());
	private static final long[] mk_tokenSet_92() {
		long[] data = { 4810363371520L, 2341766219836620800L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_92 = new BitSet(mk_tokenSet_92());
	private static final long[] mk_tokenSet_93() {
		long[] data = { 4810363371520L, 3602774117792546816L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_93 = new BitSet(mk_tokenSet_93());
	private static final long[] mk_tokenSet_94() {
		long[] data = { 0L, 1188950303787974656L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_94 = new BitSet(mk_tokenSet_94());
	private static final long[] mk_tokenSet_95() {
		long[] data = new long[8];
		data[0]=4810363371520L;
		data[1]=1621190278924664832L;
		data[2]=-828662331432009725L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_95 = new BitSet(mk_tokenSet_95());
	private static final long[] mk_tokenSet_96() {
		long[] data = new long[8];
		data[0]=288484363337728L;
		data[1]=4611686018158919680L;
		data[2]=-541065221L;
		data[3]=2047L;
		return data;
	}
	public static final BitSet _tokenSet_96 = new BitSet(mk_tokenSet_96());
	private static final long[] mk_tokenSet_97() {
		long[] data = { 4810363371520L, 2341766219836620800L, 16512L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_97 = new BitSet(mk_tokenSet_97());
	private static final long[] mk_tokenSet_98() {
		long[] data = new long[8];
		data[0]=4810363371520L;
		data[1]=2774111784745762816L;
		data[2]=-828662331433009021L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_98 = new BitSet(mk_tokenSet_98());
	private static final long[] mk_tokenSet_99() {
		long[] data = { 137438953472L, 35150021656576L, 8L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_99 = new BitSet(mk_tokenSet_99());
	private static final long[] mk_tokenSet_100() {
		long[] data = { 0L, 2594073385365405696L, 16777216L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_100 = new BitSet(mk_tokenSet_100());
	private static final long[] mk_tokenSet_101() {
		long[] data = new long[8];
		data[0]=2L;
		data[1]=9115320798506647552L;
		data[2]=-828662331428830717L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_101 = new BitSet(mk_tokenSet_101());
	private static final long[] mk_tokenSet_102() {
		long[] data = { 2L, 8682940083719897088L, 4194816L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_102 = new BitSet(mk_tokenSet_102());
	private static final long[] mk_tokenSet_103() {
		long[] data = { 4810363371520L, 3566745320773582848L, 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_103 = new BitSet(mk_tokenSet_103());
	private static final long[] mk_tokenSet_104() {
		long[] data = { 0L, 25769803776L, 31525197391593480L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_104 = new BitSet(mk_tokenSet_104());
	private static final long[] mk_tokenSet_105() {
		long[] data = new long[8];
		data[0]=-16L;
		data[1]=-288230376151711745L;
		data[2]=-1L;
		data[3]=268435455L;
		return data;
	}
	public static final BitSet _tokenSet_105 = new BitSet(mk_tokenSet_105());
	private static final long[] mk_tokenSet_106() {
		long[] data = { 0L, 288230376165343232L, 469762048L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_106 = new BitSet(mk_tokenSet_106());
	private static final long[] mk_tokenSet_107() {
		long[] data = new long[8];
		data[0]=7009386627072L;
		data[1]=4539628393917808640L;
		data[2]=-828662331373256837L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_107 = new BitSet(mk_tokenSet_107());
	private static final long[] mk_tokenSet_108() {
		long[] data = new long[8];
		data[1]=2738223724000444416L;
		data[2]=-828662331433025533L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_108 = new BitSet(mk_tokenSet_108());
	private static final long[] mk_tokenSet_109() {
		long[] data = { 0L, 35184372088832L, 216172782113783808L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_109 = new BitSet(mk_tokenSet_109());
	private static final long[] mk_tokenSet_110() {
		long[] data = { 0L, 36028798097948672L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_110 = new BitSet(mk_tokenSet_110());
	private static final long[] mk_tokenSet_111() {
		long[] data = new long[8];
		data[1]=288265526710894592L;
		data[2]=-4611686018427387901L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_111 = new BitSet(mk_tokenSet_111());
	private static final long[] mk_tokenSet_112() {
		long[] data = new long[8];
		data[0]=2L;
		data[1]=9187483976933572608L;
		data[2]=-1066384645L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_112 = new BitSet(mk_tokenSet_112());
	private static final long[] mk_tokenSet_113() {
		long[] data = new long[8];
		data[0]=2L;
		data[1]=9187483976937766912L;
		data[2]=-596622597L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_113 = new BitSet(mk_tokenSet_113());
	private static final long[] mk_tokenSet_114() {
		long[] data = new long[8];
		data[0]=7009386627072L;
		data[1]=468374329600442368L;
		data[2]=-828662331373259005L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_114 = new BitSet(mk_tokenSet_114());
	private static final long[] mk_tokenSet_115() {
		long[] data = new long[8];
		data[1]=432380714788847616L;
		data[2]=-828662331433025533L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_115 = new BitSet(mk_tokenSet_115());
	private static final long[] mk_tokenSet_116() {
		long[] data = new long[8];
		data[0]=2199023255552L;
		data[1]=288335895471980544L;
		data[2]=-4611686018368637181L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_116 = new BitSet(mk_tokenSet_116());
	private static final long[] mk_tokenSet_117() {
		long[] data = new long[8];
		data[0]=7009386627072L;
		data[1]=4503599596898844672L;
		data[2]=-828662331373256709L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_117 = new BitSet(mk_tokenSet_117());
	private static final long[] mk_tokenSet_118() {
		long[] data = new long[8];
		data[0]=4810363371520L;
		data[1]=468268774317817856L;
		data[2]=-828662331432009725L;
		data[3]=2046L;
		return data;
	}
	public static final BitSet _tokenSet_118 = new BitSet(mk_tokenSet_118());
	private static final long[] mk_tokenSet_119() {
		long[] data = { 2199023255552L, 105518773436416L, 58750720L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_119 = new BitSet(mk_tokenSet_119());

	}

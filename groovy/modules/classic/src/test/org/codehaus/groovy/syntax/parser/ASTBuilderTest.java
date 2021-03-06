/*
 * $Id$
 *
 * Copyright 2003 (C) James Strachan and Bob Mcwhirter. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met: 1. Redistributions of source code must retain
 * copyright statements and notices. Redistributions must also contain a copy
 * of this document. 2. Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the distribution. 3.
 * The name "groovy" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Codehaus. For
 * written permission, please contact info@codehaus.org. 4. Products derived
 * from this Software may not be called "groovy" nor may "groovy" appear in
 * their names without prior written permission of The Codehaus. "groovy" is a
 * registered trademark of The Codehaus. 5. Due credit should be given to The
 * Codehaus - http://groovy.codehaus.org/
 *
 * THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 *
 */
package org.codehaus.groovy.syntax.parser;

import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.syntax.Types;
import org.codehaus.groovy.syntax.lexer.UnexpectedCharacterException;

/**
 * Test case for the AST builder
 *
 * @author <a href="mailto:james@coredevelopers.net">James Strachan</a>
 * @version $Revision$
 */
public class ASTBuilderTest extends TestParserSupport {

    public void testStatementParsing() throws Exception {
        ModuleNode module =
            parse("import cheddar.cheese.Toast as Bread\n x = [1, 2, 3]; System.out.println(x)", "foo/Cheese.groovy");

        BlockStatement block = module.getStatementBlock();
        assertTrue("Contains some statements", !block.getStatements().isEmpty());

        //System.out.println("Statements: " + block.getStatements());
    }

    public void testBlock() throws Exception {
        ModuleNode module =
            parse("class Foo { void testMethod() { x = someMethod(); callMethod(x) } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 2, statement.getStatements().size());

        System.out.println(statement.getStatements());
    }

    public void testSubscript() throws Exception {
        ModuleNode module =
            parse("class Foo { void testMethod() { x = 1\n [1].each { println(it) }} }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 2, statement.getStatements().size());

        for (Iterator iter = statement.getStatements().iterator(); iter.hasNext();) {
            System.out.println(iter.next());
        }
    }

    public void testNewlinesInsideExpresssions() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { x = 1 +\n 5 * \n 2 / \n 5 } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        for (Iterator iter = statement.getStatements().iterator(); iter.hasNext();) {
            System.out.println(iter.next());
        }
    }

    public void testMethodCalls() throws Exception {
        ModuleNode module =
            parse(
                "class Foo { void testMethod() { def array = getMockArguments()\n \n dummyMethod(array) } }",
                "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 2, statement.getStatements().size());

        for (Iterator iter = statement.getStatements().iterator(); iter.hasNext();) {
            System.out.println(iter.next());
        }
    }

/*  todo should we or shouldn't we support the jdk 1.5 for loop, with the colon in lieu of the groovy 'in' keyword?
    public void testJdk15ForLoop() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { for (x : foo) { println x } } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        ForStatement stmt = (ForStatement) statement.getStatements().get(0);
        assertEquals("x", stmt.getVariable());
        assertTrue(stmt.getVariableType().isDynamic());
        System.out.println(stmt);
    }

    public void testJdk15ForLoopWithType() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { for (Integer x : foo) { println x } } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        ForStatement stmt = (ForStatement) statement.getStatements().get(0);
        assertEquals("x", stmt.getVariable());
        System.out.println( stmt.getVariableType().getName() );
        assertEquals("java.lang.Integer", stmt.getVariableType().getName());
        assertFalse(stmt.getVariableType().isDynamic());
        System.out.println(stmt);
    }
*/

    public void testForLoopWithType() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { for (Foo x in foo) { println x } } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        ForStatement stmt = (ForStatement) statement.getStatements().get(0);
        assertEquals("x", stmt.getVariable());
        assertEquals("Foo", stmt.getVariableType().getName());
        assertFalse(stmt.getVariableType().isDynamic());
        System.out.println(stmt);
    }

    public void testSubscriptAssignment() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { x[12] = 'abc' } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        ExpressionStatement exprStmt = (ExpressionStatement) statement.getStatements().get(0);
        Expression exp = exprStmt.getExpression();
        System.out.println(exp);

        assertTrue(exp instanceof BinaryExpression);
        BinaryExpression binExpr = (BinaryExpression) exp;
        assertTrue("RHS is constant", binExpr.getRightExpression() instanceof ConstantExpression);

        Expression lhs = binExpr.getLeftExpression();
        assertTrue("LHS is binary expression", lhs instanceof BinaryExpression);

        BinaryExpression lhsBinExpr = (BinaryExpression) lhs;
        assertEquals(Types.LEFT_SQUARE_BRACKET, lhsBinExpr.getOperation().getType());

        assertTrue("Left of LHS is a variable", lhsBinExpr.getLeftExpression() instanceof VariableExpression);
        assertTrue("Right of LHS is a constant", lhsBinExpr.getRightExpression() instanceof ConstantExpression);

    }

    public void testNoReturn() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { x += 5 } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());

        ExpressionStatement exprStmt = (ExpressionStatement) statement.getStatements().get(0);
        Expression exp = exprStmt.getExpression();

        System.out.println("expr: " + exp);
    }

    public void testCastExpression() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { x = (Short) 5 } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());

        ExpressionStatement exprStmt = (ExpressionStatement) statement.getStatements().get(0);
        Expression exp = exprStmt.getExpression();

        System.out.println("expr: " + exp);
    }

    public void testTernaryExpression() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { foo() ? 'a' : 'b' } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());

        ExpressionStatement exprStmt = (ExpressionStatement) statement.getStatements().get(0);
        Expression exp = exprStmt.getExpression();

        System.out.println("expr: " + exp);
    }

    public void testClosureWithJustIdentifierBug() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { return {a} } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());

        ReturnStatement returnStmt = (ReturnStatement)statement.getStatements().get(0);
        Expression exp = returnStmt.getExpression();

        System.out.println("expr: " + exp);
    }

    public void testClosureWithJustIdentifierInMapBug() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { ['x':{a}, 'd':123] } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());

        ExpressionStatement exprStmt = (ExpressionStatement) statement.getStatements().get(0);

        MapExpression mapExp = (MapExpression) exprStmt.getExpression();
        MapEntryExpression entryExp = (MapEntryExpression) mapExp.getMapEntryExpressions().get(0);
        ClosureExpression closureExp = (ClosureExpression) entryExp.getValueExpression();
        assertEquals("Parameters on closure", 0, closureExp.getParameters().length);
        System.out.println("expr: " + closureExp);
    }

    public void testArrayExpression() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { def foo = ['a', 'b', 'c']  as String[]\n assert foo != null } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 2, statement.getStatements().size());

        System.out.println(statement.getStatements());

        ExpressionStatement exprStmt = (ExpressionStatement) statement.getStatements().get(0);
        Expression exp = exprStmt.getExpression();

        System.out.println("expr: " + exp);
    }

    public void testArrayExpression2() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { String[] foo = ['a', 'b', 'c']\n assert foo != null } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 2, statement.getStatements().size());

        System.out.println(statement.getStatements());

        ExpressionStatement exprStmt = (ExpressionStatement) statement.getStatements().get(0);
        Expression exp = exprStmt.getExpression();

        System.out.println("expr: " + exp);
    }

    public void testTypedVariableExpression() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { Short x = 5 } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());

        ExpressionStatement exprStmt = (ExpressionStatement) statement.getStatements().get(0);
        Expression exp = exprStmt.getExpression();

        System.out.println("expr: " + exp);
    }

    public void testFullyQualifiedType() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { com.acme.Foo } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());

        ExpressionStatement exprStmt = (ExpressionStatement) statement.getStatements().get(0);
        Expression exp = exprStmt.getExpression();

        System.out.println("expr: " + exp);

        System.out.println("text: " + exp.getText());
    }

    public void testDoubleSubscript() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { x = foo[0][0] } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());

        ExpressionStatement exprStmt = (ExpressionStatement) statement.getStatements().get(0);
        Expression exp = exprStmt.getExpression();

        System.out.println("expr: " + exp);

        System.out.println("text: " + exp.getText());
    }

    public void testMethodCallWithDotAndNoParenthesis() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { foo.someMethod 1 } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());

        ExpressionStatement exprStmt = (ExpressionStatement) statement.getStatements().get(0);
        Expression exp = exprStmt.getExpression();

        System.out.println("expr: " + exp);

        System.out.println("text: " + exp.getText());
    }

    public void testMethodCallWithNoParenthesis() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { someMethod 1 } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());

        ExpressionStatement exprStmt = (ExpressionStatement) statement.getStatements().get(0);
        Expression exp = exprStmt.getExpression();

        System.out.println("expr: " + exp);

        System.out.println("text: " + exp.getText());
    }

    public void testScriptMethodCallWithNoParenthesis() throws Exception {
        ModuleNode module = parse("someMethod 1", "Dummy.groovy");
        BlockStatement statement = getCode(module, "run");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());

        ExpressionStatement exprStmt = (ExpressionStatement) statement.getStatements().get(0);
        Expression exp = exprStmt.getExpression();

        System.out.println("expr: " + exp);

        System.out.println("text: " + exp.getText());
    }

    public void testScriptWithMethodDeclaration() throws Exception {
        ModuleNode module = parse("def foo(a) { return a + 1}\n foo(123)", "Dummy.groovy");
        BlockStatement statement = getCode(module, "run");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());

        ExpressionStatement exprStmt = (ExpressionStatement) statement.getStatements().get(0);
        Expression exp = exprStmt.getExpression();

        System.out.println("expr: " + exp);

        System.out.println("text: " + exp.getText());
    }

    public void testSubscriptThenMethod() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { x = foo[0].foo() } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());

        ExpressionStatement exprStmt = (ExpressionStatement) statement.getStatements().get(0);
        Expression exp = exprStmt.getExpression();

        System.out.println("expr: " + exp);

        System.out.println("text: " + exp.getText());
    }

    public void testSubscriptThenOperation() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { foo[0] += 5 } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());

        ExpressionStatement exprStmt = (ExpressionStatement) statement.getStatements().get(0);
        Expression exp = exprStmt.getExpression();

        System.out.println("expr: " + exp);

        System.out.println("text: " + exp.getText());
    }


    public void testRodsBug() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { if (x) { String n = 'foo' } } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());

        IfStatement ifStmt = (IfStatement) statement.getStatements().get(0);
        BlockStatement trueStmt = (BlockStatement) ifStmt.getIfBlock();

        System.out.println("trueStmt: " + trueStmt);

        // ideally there would be 1 statement; though we're handling that in the verifier
        assertEquals(1, trueStmt.getStatements().size());
    }

    public void testStaticMethodCallBug() throws Exception {
        ModuleNode module =
            parse("class Foo { void testMethod() { ASTBuilderTest.mockHelperMethod() } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());
    }

    public void testInstanceofBug() throws Exception {
        ModuleNode module =
        parse("class Foo { void testMethod() { if (foo instanceof java.util.List) { println('hello') } } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());

        IfStatement ifStmt = (IfStatement) statement.getStatements().get(0);
        BinaryExpression exp = (BinaryExpression) ifStmt.getBooleanExpression().getExpression();

        System.out.println("exp: " + exp);

        Expression rhs = exp.getRightExpression();
        assertTrue("RHS should be a class expression", rhs instanceof ClassExpression);

        ClassExpression classExp = (ClassExpression) rhs;
        assertEquals("java.util.List", classExp.getType());
    }

    public void testMethodCallWithoutParensBug() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { println 3, 5 } }", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());
    }

    public void testReturnMethodClosure() throws Exception {
        ModuleNode module = parse("class Foo { void testMethod() { System.out.println\n}}", "Dummy.groovy");
        BlockStatement statement = getCode(module, "testMethod");

        assertEquals("Statements size: " + statement.getStatements(), 1, statement.getStatements().size());

        System.out.println(statement.getStatements());
    }

    public void testDionsTypo() throws Exception {
        String script = "class Foo { void testMethod() { println ${foo}\n}}";
        try {
            ModuleNode module = parse(script, "Dummy.groovy");
        } catch (Exception e) {
            // todo problem of antlr thrown exception
            //SyntaxException cause = e.getUnit().getSyntaxError(0);
            //if( cause != null && cause instanceof ParserException) {
                return;
            //}
            //fail (script+" should fail with a ParserException: "+e.getMessage());
        }
        fail(script+" should fail because the { was unexpected after the dollar sign.");
    }

    public void testMethodWithArrayTypeParam() throws Exception {
        ModuleNode module = parse("class Foo { void main(String[] args) { println(args) } }", "Dummy.groovy");

        MethodNode method = getMethod(module, "main");

        System.out.println("Parameters: " + InvokerHelper.toString(method.getParameters()));
    }

    private void ensureOutOfRange(String script) throws Exception {
        try {
            ModuleNode module = parse(script, "Dummy.groovy");
        } catch (Exception e) {
            // todo problem of antlr thrown exception
            // SyntaxException cause = e.getUnit().getSyntaxError(0);
            // if( cause != null && cause instanceof ParserException && cause.getMessage().indexOf("out of range") >= 0) {
                return;
            //}
            //fail (script+" should fail with a ParserException: "+e.getMessage());
        }
        fail(script+" should fail because the number is out of range.");
    }

    private void ensureInRange(String script) throws Exception {
        ModuleNode module = parse(script, "Dummy.groovy");
    }

    public void testLiteralIntegerRange() throws Exception {
        ensureInRange(   "def x =  2147483647I;");
        ensureOutOfRange("def x =  2147483648I;");

        ensureInRange(   "def x = -2147483648I;");
        ensureOutOfRange("def x = -2147483649I;");
    }

    public void testLiteralLongRange() throws Exception {
        ensureInRange(   "def x =  9223372036854775807L;");
        ensureOutOfRange("def x =  9223372036854775808L;");

        ensureInRange(   "def x = -9223372036854775808L;");
        ensureOutOfRange("def x = -9223372036854775809L;");
    }

    public void testLiteralDoubleRange() throws Exception {
        ensureInRange(   "def x =  1.7976931348623157E308D;");
        ensureOutOfRange("def x =  1.7976931348623167E308D;");

        ensureInRange(   "def x = -1.7976931348623157E308D;");
        ensureOutOfRange("def x = -1.7976931348623167E308D;");
    }

    public void testLiteralFloatRange() throws Exception {
        ensureInRange(   "def x =  3.4028235e+38f;");
        ensureOutOfRange("def x =  3.4028236e+38f;");

        ensureInRange(   "def x = -3.4028235e+38f;");
        ensureOutOfRange("def x = -3.4028236e+38f;");
    }

    public void testLiteralIntegerBadSuffix() throws Exception {
        try {
            ModuleNode module = parse("def x = 2147483648J;", "Dummy.groovy");
        } catch (Exception e) {
            // todo problem of antlr thrown exception
            //SyntaxException cause = e.getUnit().getSyntaxError(0);
            //if (cause instanceof UnexpectedCharacterException) {
                return;
            //}
            //fail ("x = 2147483648J should fail with an UnexpectedCharacterException");
        }
        fail("x = 2147483648J, should fail because J is an invalid numeric literal suffix.");
    }

    public void testLiteralBadExponent() throws Exception {
        try {
            ModuleNode module = parse("def x = 2.3e;", "Dummy.groovy");
        } catch (Exception e) {
            // todo problem of antlr thrown exception
            //SyntaxException cause = e.getUnit().getSyntaxError(0);
            //if (cause instanceof UnexpectedCharacterException) {
                return;
            //}
            //fail ("x = 2.3e should fail with an UnexpectedCharacterException");
        }
        fail("x = 2.3e, should fail because no exponent is specified.");
    }

    public static Object mockHelperMethod() {
        return "cheese";
    }

    protected BlockStatement getCode(ModuleNode module, String name) {
        MethodNode method = getMethod(module, name);

        BlockStatement statement = (BlockStatement) method.getCode();
        assertNotNull(statement);
        return statement;
    }

    protected MethodNode getMethod(ModuleNode module, String name) {
        assertEquals("class count", 1, module.getClasses().size());

        ClassNode node = (ClassNode) module.getClasses().get(0);

        assertNotNull(node);

        List methods = node.getDeclaredMethods(name);
        assertTrue(methods.size() > 0);
        return (MethodNode) methods.get(0);
    }
}

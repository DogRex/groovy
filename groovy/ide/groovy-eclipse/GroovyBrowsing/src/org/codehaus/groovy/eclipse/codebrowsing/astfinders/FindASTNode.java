package org.codehaus.groovy.eclipse.codebrowsing.astfinders;

import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.MethodPointerExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;

/**
 * Given an identifier and its location in the document, find the ASTNode that
 * corresponds to the editifier.
 * 
 * @author emp
 */
public class FindASTNode extends ClassCodeVisitorSupport {
	ModuleNode moduleNode;

	ClassNode classNode;

	private String identifier;

	private int line;

	private int column;

	protected SourceUnit getSourceUnit() {
		// Nothing to do.
		return null;
	}

	public void doFind(ModuleNode node, String identifier, int line, int column)
			throws ASTNodeFoundException {
		moduleNode = node;
		this.identifier = identifier;
		this.line = line;
		this.column = column;

		List classes = node.getClasses();
		for (Iterator iter = classes.iterator(); iter.hasNext();) {
			classNode = (ClassNode) iter.next();
			classNode.visitContents(this);
		}
	}

	public void visitFieldExpression(FieldExpression expr) {
		if (validCoords(expr) && identifier.equals(expr.getFieldName())) {
			System.out.println("Field: " + expr.getFieldName());
			testForMatch(expr);
		}
		super.visitFieldExpression(expr);
	}

	public void visitField(FieldNode node) {
		if (validCoords(node)) {
			System.out.println("FieldNode: " + node.getName());
		}
		super.visitField(node);
	}

	public void visitPropertyExpression(PropertyExpression expr) {
		if (validCoords(expr)) {
			if (identifier.equals(expr.getProperty())) {
				System.out.println("Property: " + expr.getProperty());
				testForMatch(expr);
			} else if (expr.getObjectExpression() instanceof ClassExpression) {
				patchClassExpressionLineColumn(expr, expr.getObjectExpression());
			}
		}
		super.visitPropertyExpression(expr);
	}

	public void visitVariableExpression(VariableExpression expr) {
		if (validCoords(expr) && identifier.equals(expr.getName())) {
			System.out.println("Variable: " + expr.getName());
			testForMatch(expr);
		}
		super.visitVariableExpression(expr);
	}

	public void visitMethodCallExpression(MethodCallExpression call) {
		if (validCoords(call)) {
			if (identifier.equals(call.getMethod())) {
				System.out.println("Method call: " + call.getMethod());
				// Strange - columns can be equal sometimes
				if (call.getLineNumber() == call.getLastLineNumber()
						&& call.getColumnNumber() == call.getLastColumnNumber()) {
					call.setColumnNumber(call.getColumnNumber()
							- identifier.length());
				}
				testForMatch(call);
			} else if (call.getObjectExpression() instanceof ClassExpression) {
				patchClassExpressionLineColumn(call, call.getObjectExpression());
			}
		}
		super.visitMethodCallExpression(call);
	}

	/**
	 * For some reason, ClassExpression does not contain line/column
	 * information. This method is called to patch this info from another node.
	 * It is used in cases like StaticClass.methodCall() or
	 * StaticClass.propertyAccess.
	 * 
	 * @param node
	 * @param expr
	 */
	private void patchClassExpressionLineColumn(ASTNode node, Expression expr) {
		expr.setLineNumber(node.getLineNumber());
		expr.setLastLineNumber(node.getLastLineNumber());
		expr.setColumnNumber(node.getColumnNumber());
		expr.setLastColumnNumber(node.getLastColumnNumber());
	}

	public void visitClassExpression(ClassExpression expr) {
		if (validCoords(expr)) {
			System.out.println("Class expr: " + expr.getText());
			testForMatch(expr);
		}
		super.visitClassExpression(expr);
	}

	public void visitMethodPointerExpression(MethodPointerExpression expr) {
		if (validCoords(expr) && identifier.equals(expr.getMethodName())) {
			System.out.println("Method Pointer: " + expr.getMethodName());
			testForMatch(expr);
		}
		super.visitMethodPointerExpression(expr);
	}

	public void visitStaticMethodCallExpression(StaticMethodCallExpression call) {
		if (validCoords(call) && identifier.equals(call.getMethod())) {
			System.out.println("Static method call: " + call.getMethod());
			testForMatch(call);
		}
		super.visitStaticMethodCallExpression(call);
	}

	private boolean validCoords(ASTNode node) {
		boolean ret = node.getLineNumber() > 0 && node.getColumnNumber() > 0;
		if (ret) {
			printNodeText(node);
		}
		return ret;
	}

	private void printNodeText(ASTNode node) {
		System.out.println("Text [" + node.getLineNumber() + ","
				+ node.getColumnNumber() + "]: " + node.getText());
	}

	private void testForMatch(ASTNode astNode) {
		if ((astNode.getLineNumber() == line
				&& astNode.getLastLineNumber() == line
				&& astNode.getColumnNumber() <= column && column <= astNode
				.getLastColumnNumber())
				|| (astNode.getLineNumber() == line
						&& line < astNode.getLastLineNumber() && astNode
						.getColumnNumber() <= column)
				|| (astNode.getLineNumber() < line && line < astNode
						.getLastLineNumber())
				|| (astNode.getLastLineNumber() == line && column <= astNode
						.getLastColumnNumber())) {
			throw new ASTNodeFoundException(moduleNode, classNode, astNode,
					identifier, line, column);
		}
	}
}
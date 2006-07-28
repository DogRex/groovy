package org.codehaus.groovy.eclipse.codebrowsing.astfinders;

import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.eclipse.codebrowsing.ASTUtils;

/**
 * Given some lineNumber, columnNumber, find the MethodNode surrounding the
 * lineNumber, columnNumber.
 * 
 * @author emp
 */
public class FindSurroundingMethod extends ClassCodeVisitorSupport {
	private ModuleNode moduleNode;

	private ClassNode classNode;

	private int lineNumber;

	private int columnNumber;

	protected SourceUnit getSourceUnit() {
		// Nothing to do.
		return null;
	}

	/**
	 * Find a MethodNode surrounding the given lineNumber, columnNumber. If a
	 * MethodNode is found, the identifier field is set to 'method node', and
	 * the lineNumber, columnNumber is set to the found MethodNode AST
	 * lineNumber, columnNumber.
	 * 
	 * @param moduleNode
	 * @param lineNumber
	 * @param columnNumber
	 * @throws ASTNodeFoundException
	 */
	public void doFind(ModuleNode moduleNode, int lineNumber, int columnNumber) {
		this.moduleNode = moduleNode;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;

		List classes = moduleNode.getClasses();
		for (Iterator iter = classes.iterator(); iter.hasNext();) {
			classNode = (ClassNode) iter.next();
			classNode.visitContents(this);
		}
	}

	public void visitMethod(MethodNode node) {
		if (ASTUtils.isSurroundingNode(node, lineNumber, columnNumber)) {
			throw new ASTNodeFoundException(moduleNode, classNode, node,
					"method node", node.getLineNumber(), node.getColumnNumber());
		}
		super.visitMethod(node);
	}
}
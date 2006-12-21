package org.codehaus.groovy.eclipse.codebrowsing.astfinders;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ModuleNode;

/**
 * Exception thrown when the ASTNodeFinder finds an ASTNode.
 * 
 * @author emp
 */
public class ASTNodeFoundException extends RuntimeException {
	private static final long serialVersionUID = -4475120092640994581L;

	ModuleNode moduleNode;

	ClassNode classNode;

	ASTNode astNode;

	private String identifier;

	private int line;

	private int column;

	public ASTNodeFoundException(ModuleNode moduleNode, ClassNode classNode,
			ASTNode astNode, String identifier, int line, int column) {
		this.moduleNode = moduleNode;
		this.classNode = classNode;
		this.astNode = astNode;
		this.identifier = identifier;
		this.line = line;
		this.column = column;
	}

	public ASTNode getASTNode() {
		return astNode;
	}

	public ClassNode getClassNode() {
		return classNode;
	}

	public ModuleNode getModuleNode() {
		return moduleNode;
	}

	public int getColumn() {
		return column;
	}

	public String getIdentifier() {
		return identifier;
	}

	public int getLine() {
		return line;
	}
}

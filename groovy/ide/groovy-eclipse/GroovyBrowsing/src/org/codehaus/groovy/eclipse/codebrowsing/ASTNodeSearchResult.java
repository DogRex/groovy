package org.codehaus.groovy.eclipse.codebrowsing;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.eclipse.codebrowsing.astfinders.ASTNodeFoundException;

/**
 * Object encapsulating the result of an ASTNode search.
 * 
 * @author emp
 */
public class ASTNodeSearchResult {
	private ASTNodeFoundException exception;

	public ASTNodeSearchResult(ASTNodeFoundException e) {
		this.exception = e;
	}

	public ASTNode getASTNode() {
		return exception.getASTNode();
	}

	public ClassNode getClassNode() {
		return exception.getClassNode();
	}

	public int getColumn() {
		return exception.getColumn();
	}

	public String getIdentifier() {
		return exception.getIdentifier();
	}

	public int getLine() {
		return exception.getLine();
	}

	public ModuleNode getModuleNode() {
		return exception.getModuleNode();
	}
}

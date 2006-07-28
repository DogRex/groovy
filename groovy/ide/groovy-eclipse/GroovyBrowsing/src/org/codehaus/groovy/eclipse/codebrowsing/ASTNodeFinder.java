package org.codehaus.groovy.eclipse.codebrowsing;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.eclipse.codebrowsing.astfinders.ASTNodeFoundException;
import org.codehaus.groovy.eclipse.codebrowsing.astfinders.FindASTNode;
import org.codehaus.groovy.eclipse.codebrowsing.astfinders.FindSurroundingClosure;
import org.codehaus.groovy.eclipse.codebrowsing.astfinders.FindSurroundingMethod;

/**
 * Utility class to find different AST nodes.
 * 
 * @author emp
 */
public class ASTNodeFinder {
	/**
	 * Given an identifier and its coordinates, find its ASTNode.
	 * @param moduleNode
	 * @param identifier
	 * @param line
	 * @param column
	 * @return The search result, or null if no ASTNode was found.
	 */
	public static ASTNodeSearchResult findASTNode(ModuleNode moduleNode,
			String identifier, int line, int column) {
		try {
			new FindASTNode().doFind(moduleNode, identifier, line, column);
		} catch (ASTNodeFoundException e) {
			return new ASTNodeSearchResult(e);
		}
		return null;
	}

	/**
	 * Given a node, find the surrounding ClosureExpression if there is one.
	 * @param moduleNode
	 * @param reference
	 * @return The search result, or null if no ASTNode was found.
	 */
	public static ASTNodeSearchResult findSurroundingClosure(
			ModuleNode moduleNode, ASTNode reference) {
		try {
			new FindSurroundingClosure().doFind(moduleNode, reference
					.getLineNumber(), reference.getColumnNumber());
		} catch (ASTNodeFoundException e) {
			return new ASTNodeSearchResult(e);
		}
		return null;
	}

	/**
	 * Given a node, find the surrounding MethodNode if there is one.
	 * @param moduleNode
	 * @param reference
	 * @return The search result, or null if no ASTNode was found.
	 */
	public static ASTNodeSearchResult findSurroundingMethod(
			ModuleNode moduleNode, ASTNode reference) {
		try {
			new FindSurroundingMethod().doFind(moduleNode, reference
					.getLineNumber(), reference.getColumnNumber());
		} catch (ASTNodeFoundException e) {
			return new ASTNodeSearchResult(e);
		}
		return null;
	}
}
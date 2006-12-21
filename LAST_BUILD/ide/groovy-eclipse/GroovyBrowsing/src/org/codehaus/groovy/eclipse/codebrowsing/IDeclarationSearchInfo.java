package org.codehaus.groovy.eclipse.codebrowsing;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorPart;

/**
 * Interface containing information required to start a declaration search.
 * 
 * @author emp
 */
public interface IDeclarationSearchInfo {
	/**
	 * @return The identifier. Required.
	 */
	public String getIdentifier();

	/**
	 * @return The editor which contains the identifier. Required.
	 */
	public IEditorPart getEditor();

	/**
	 * @return The region the identifier occupies. Required.
	 */
	public IRegion getRegion();

	/**
	 * @return The ModuleNode, which may be null, or out of date.
	 * @see #getASTNode()
	 */
	public ModuleNode getModuleNode();

	/**
	 * @return The ClassNode, which may be null if an ASTNode cannot be found.
	 * @see #getASTNode()
	 */
	public ClassNode getClassNode();

	/**
	 * @return The ASTNode may be null if one cannot not be found. This occurs
	 *         when a file could not be compiled.
	 */
	public ASTNode getASTNode();
}

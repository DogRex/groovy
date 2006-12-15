package org.codehaus.groovy.eclipse.codebrowsing;

import org.eclipse.jface.text.IRegion;

/**
 * The proposal returned by a search processor. A proposal is simply a target
 * resource which contains the proposed declaration, and a highlight region than
 * can be used to highlight the declarattion. Each proposal is categorized. If
 * there is more than one proposal, they can be sorted by category for display.
 * 
 * @author emp
 */
public interface IDeclarationProposal {
	/**
	 * @return The category of the proposal. Custom categories are defined by
	 *         extensions which require custom categories.
	 */
	public String getCategory();

	/**
	 * @return The source code containing the declaration, usually an IFile.
	 */
	public ISourceCode getSourceCode();

	/**
	 * A region of interst is used to select, say, an class name, after opening
	 * a file in an editor.
	 * 
	 * @return The region of interst, or null if there is none.
	 */
	public IRegion getRegionOfInterest();

	/**
	 * @return Text suitable for display in a UI.
	 */
	public String getDisplayText();

	/**
	 * Open the proposal in some view.
	 */
	public boolean open();
}

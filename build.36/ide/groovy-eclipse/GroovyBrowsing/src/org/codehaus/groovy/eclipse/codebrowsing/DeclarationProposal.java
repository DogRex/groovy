package org.codehaus.groovy.eclipse.codebrowsing;

import org.eclipse.jface.text.IRegion;

/**
 * Default implementation of IDeclarationProposal.
 * 
 * @author emp
 */
public class DeclarationProposal implements IDeclarationProposal {
	/**
	 * Default empty array result when there is no proposal.
	 */
	public static IDeclarationProposal[] NONE = new IDeclarationProposal[0];

	String category;

	String displayName;
	
	ISourceCode sourceCode;

	public DeclarationProposal(String category, String displayName,
			ISourceCode sourceCode) {
		this.category = category;
		this.displayName = displayName;
		this.sourceCode = sourceCode;
	}

	public String getCategory() {
		return category;
	}

	public String getDisplayText() {
		return displayName;
	}
	
	public ISourceCode getSourceCode() {
		return sourceCode;
	}

	public IRegion getRegionOfInterest() {
		return sourceCode.getRegionOfInterest();
	}

	public boolean open() {
		return sourceCode.open();
	}
}
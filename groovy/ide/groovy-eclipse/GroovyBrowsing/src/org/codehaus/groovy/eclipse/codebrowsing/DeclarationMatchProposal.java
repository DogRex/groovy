package org.codehaus.groovy.eclipse.codebrowsing;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.IRegion;

/**
 * Default implementation of IDeclarationMatchProposal.
 * @author emp
 */
public class DeclarationMatchProposal implements IDeclarationMatchProposal {
	String category;
	String displayName;
	IResource target;
	IRegion highlight;
	
	public DeclarationMatchProposal(String category, String displayName, IResource target, IRegion highlight) {
		this.category = category;
		this.displayName = displayName;
		this.target = target;
		this.highlight = highlight;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public IResource getTarget() {
		return target;
	}

	public IRegion getHighlightRegion() {
		return highlight;
	}
}
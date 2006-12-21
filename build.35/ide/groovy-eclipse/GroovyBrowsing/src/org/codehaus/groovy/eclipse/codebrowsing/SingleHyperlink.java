package org.codehaus.groovy.eclipse.codebrowsing;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;

public class SingleHyperlink implements IHyperlink {
	private IDeclarationProposal proposal;
	private IRegion hyperlinkRegion;

	public SingleHyperlink(IDeclarationProposal proposal, IRegion hyperlinkRegion) {
		this.proposal = proposal;
		this.hyperlinkRegion = hyperlinkRegion;
	}

	public IRegion getHyperlinkRegion() {
		return hyperlinkRegion;
	}

	public String getTypeLabel() {
		return null;
	}

	public String getHyperlinkText() {
		return proposal.getDisplayText();
	}

	public void open() {
		proposal.open();
	}
}

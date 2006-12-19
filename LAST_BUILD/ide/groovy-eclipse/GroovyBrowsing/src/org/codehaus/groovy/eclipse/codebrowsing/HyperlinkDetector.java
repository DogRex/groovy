package org.codehaus.groovy.eclipse.codebrowsing;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

public class HyperlinkDetector implements IHyperlinkDetector {
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {
		IDocument doc = textViewer.getDocument();

		// Get the region of the identifier.
		int offset;
		int length;
		String text;
		try {
			int line = doc.getLineOfOffset(region.getOffset());
			offset = doc.getLineOffset(line);
			length = doc.getLineInformation(line).getLength();
			text = doc.get(offset, length);
			region = TextUtils.getIdentifier(text, region.getOffset() - offset);
		} catch (BadLocationException e1) {
			// LOG: Unlikely - an Eclipse internal bug.
			return null;
		}

		if (region == null) {
			return null;
		}

		region = new Region(offset + region.getOffset(), region.getLength());

		// Get proposals and pack them in hyperlinks.
		try {
			IEditorPart part = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();

			IDeclarationProposal[] proposals = DeclarationSearchAssistant
					.getInstance().getProposals(part, region);
			if (proposals.length > 0) {
				if (canShowMultipleHyperlinks) {
					SingleHyperlink[] links = new SingleHyperlink[proposals.length];
					for (int i = 0; i < links.length; ++i) {
						links[i] = new SingleHyperlink(proposals[i], region);
					}
					return links;
				} else {
					// Need to implement a hyperlink with a UI.
					return new IHyperlink[] { new SingleHyperlink(proposals[0],
							region) };
				}
			}
		} catch (NullPointerException e) {
			// Ignore - can this even happen? No workbench or no active window
			// or active page or active editor. How did the hyperlink detection
			// start in the first place?
		}

		return null;
	}
}

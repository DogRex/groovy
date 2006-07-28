package org.codehaus.groovy.eclipse.codebrowsing.actions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationMatchProposal;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationSearchAssistant;
import org.codehaus.groovy.eclipse.editor.actions.EditingAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.graphics.Point;

/**
 * The action that attempts to open the declaration of the identifier under the
 * caret.
 * 
 * @author emp
 */
public class OpenDeclarationAction extends EditingAction {
	public void doAction(IAction action) throws BadLocationException {
		super.doAction(action);

		IRegion region = getIdentifier();
		if (region != null) {
			List proposals = DeclarationSearchAssistant.getInstance()
					.getProposals(editor, region);
			// TODO: UI for multiple proposals.
			// Just take the first one for now.
			if (proposals.size() > 0) {
				IDeclarationMatchProposal proposal = (IDeclarationMatchProposal) proposals
						.get(0);
				IRegion hregion = proposal.getHighlightRegion();
				select(hregion.getOffset(), hregion.getLength());
			}
		}
	}

	public IRegion getIdentifier() throws BadLocationException {
		ITextSelection sel = getTextSelection();
		if (sel == null)
			return null;

		Point pt = getRowCol(sel.getOffset());
		String line = getLine(pt.y);

		Pattern pattern = Pattern.compile("[_a-zA-Z][_a-zA-Z0-9]*");
		Matcher matcher = pattern.matcher(line);
		while (matcher.find()) {
			int ix = matcher.start();
			String match = matcher.group();
			if (ix <= pt.x && pt.x < ix + match.length()) {
				return new Region(getOffset(pt.y, ix), match.length());
			}
		}

		// Caret may be at the end of the identifier, so nudge back one and try
		// again.
		if (pt.x > 0) {
			--pt.x;
			matcher.reset();
			while (matcher.find()) {
				int ix = matcher.start();
				String match = matcher.group();
				if (ix <= pt.x && pt.x < ix + match.length()) {
					return new Region(getOffset(pt.y, ix), match.length());
				}
			}
		}

		return null;
	}
}

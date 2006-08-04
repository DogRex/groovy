package org.codehaus.groovy.eclipse.codebrowsing.actions;

import org.codehaus.groovy.eclipse.codebrowsing.DeclarationSearchAssistant;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationProposal;
import org.codehaus.groovy.eclipse.codebrowsing.TextUtils;
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
			IDeclarationProposal[] proposals = DeclarationSearchAssistant
					.getInstance().getProposals(editor, region);
			// TODO: UI for multiple proposals.
			// Just take the first one for now.
			if (proposals.length > 0) {
				IDeclarationProposal proposal = proposals[0];
				proposal.open();
			}
		}
	}

	public IRegion getIdentifier() throws BadLocationException {
		ITextSelection sel = getTextSelection();
		if (sel == null)
			return null;

		Point pt = getRowCol(sel.getOffset());
		String line = getLine(pt.y);

		IRegion region = TextUtils.getIdentifier(line, pt.x);
		if (region != null) {
			return new Region(getOffset(pt.y, region.getOffset()), region
					.getLength());
		}

		return null;
	}
}
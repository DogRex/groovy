package org.codehaus.groovy.eclipse.codebrowsing.actions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.groovy.eclipse.codebrowsing.OpenDeclarationAssistant;
import org.codehaus.groovy.eclipse.editor.actions.EditingAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.graphics.Point;

/**
 * Starts the attempt to open the declaration of the identifier under the caret.
 * 
 * @author emp
 */
public class OpenDeclaration extends EditingAction {
	@Override
	public void doAction(IAction action) throws BadLocationException {
		super.doAction(action);

		IRegion region = getIdentifier();
		if (region != null) {
			select(region.getOffset(), region.getLength());
			List proposals = OpenDeclarationAssistant.getInstance()
					.getProposals(editor, region);
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

		return null;
	}
}

package org.codehaus.groovy.eclipse.codebrowsing.actions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationSearchAssistant;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationMatchProposal;
import org.codehaus.groovy.eclipse.editor.actions.EditingAction;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

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
				if (proposal.getTarget() instanceof IFile) {
					IFile file = (IFile) proposal.getTarget();
					IWorkbenchPage page = editor.getSite().getWorkbenchWindow()
							.getActivePage();
					IEditorDescriptor desc = PlatformUI.getWorkbench()
							.getEditorRegistry().getDefaultEditor(
									file.getName());
					try {
						IEditorPart part = page.openEditor(new FileEditorInput(
								file), desc.getId());
						IRegion hregion = proposal.getHighlightRegion();
						ITextEditor editor = (ITextEditor) part;
						editor.selectAndReveal(hregion.getOffset(), hregion
								.getLength());
					} catch (PartInitException e) {
						GroovyPlugin.getPlugin().logException(
								"Should not happen.", e);
						e.printStackTrace();
					}
				}
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
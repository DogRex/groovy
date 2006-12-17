package org.codehaus.groovy.eclipse.editor.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRewriteTarget;
import org.eclipse.jface.text.ITextSelection;

/*
 * Note:
 * The comment/uncomment actions are a little naive in that they don't look
 * at the partitions - they just comment/uncomment everything! For most cases
 * this is ok, and all I had time for. When there is time (or complaints :) )
 * I will make them emulate the Java commenting as closely as possible.
 * -emp
 */

/**
 * Base class of comment actions. Preserves the selection and batches their
 * changes into one large change, so undo undoes the entire comment action in
 * one step.
 * 
 * @author emp  
 */
abstract class CommentAction extends EditingAction {
	private boolean requiresSelection;

	public CommentAction(boolean requiresSelection) {
		this.requiresSelection = requiresSelection;
	}

	public void doAction(IAction action) throws BadLocationException {
		IRewriteTarget target = (IRewriteTarget) editor
				.getAdapter(IRewriteTarget.class);
		if (target != null) {
			target.beginCompoundChange();
		}
		SelectionKeeper keeper = new SelectionKeeper(getTextEditor());
		try {
			ITextSelection sel = keeper.getSelection();
			if (requiresSelection && !isValidSelection(sel))
				return;
			doComment(sel);
		} finally {
			if (target != null) {
				target.endCompoundChange();
			}
			keeper.restoreSelection();
		}
	}

	private boolean isValidSelection(ITextSelection sel) {
		return sel != null && !sel.isEmpty() && sel.getLength() > 0;
	}

	abstract void doComment(ITextSelection sel) throws BadLocationException;
}
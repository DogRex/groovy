package org.codehaus.groovy.eclipse.editor.actions;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.editor.GroovyEditor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Utility to keep track of and restore a selection and the caret location. An
 * editing action may use a text selection, and typically should restore the
 * text selection when it completes. This is a little more involved than
 * expected.
 * 
 * SelectionKeeper makes this easy, restoring the selection and the caret
 * location, including modifications to the selection due to changes to the
 * editor content.
 * 
 * Note that this class may be redundant: I suspect that the editor should be
 * able to preserve the selection regardless of changes to the buffer. As soon
 * as I figure it out, this class will have no reason to exist.
 * 
 * @author emp
 */
public class SelectionKeeper {
	private static final String SELECT_POSITION_CATEGORY = "select";

	ITextEditor editor;

	IDocument doc;

	ITextSelection sel;

	IPositionUpdater updater;

	Position pos;

	String category;

	boolean caretIsAtEnd;

	public SelectionKeeper(ITextEditor editor) throws BadLocationException {
		try {
			this.editor = editor;
			this.doc = editor.getDocumentProvider().getDocument(
					editor.getEditorInput());
			this.sel = (ITextSelection) editor.getSelectionProvider()
					.getSelection();
			createUniqueCategory();
			updater = new DefaultPositionUpdater(category);
			doc.addPositionUpdater(updater);
			pos = new Position(sel.getOffset(), sel.getLength());
			doc.addPosition(category, pos);
			caretIsAtEnd = ((GroovyEditor) editor).getCaretOffset() == sel
					.getOffset()
					+ sel.getLength();
		} catch (BadPositionCategoryException e) {
			GroovyPlugin.getPlugin().logException("Should not happen", e);
		}
	}

	public ITextSelection getSelection() {
		return sel;
	}

	/**
	 * Restores the selection. This method implicity calls release().
	 * 
	 * @return
	 */
	public boolean restoreSelection() {
		boolean res = false;
		if (pos.isDeleted() == false) {
			try {
				doc.removePosition(category, pos);
				// - length sets cursor to beginning of selection.
				int offset = pos.getOffset();
				int length = pos.getLength();
				if (caretIsAtEnd == false) {
					offset += length;
					length = -length;
				}
				editor.getSelectionProvider().setSelection(
						new TextSelection(offset, length));
				res = true;
			} catch (BadPositionCategoryException e) {
				GroovyPlugin.getPlugin().logException("Should not happen", e);
			}
		}
		release();
		return res;
	}

	/**
	 * Releases resources acquired by the selection. This method should be
	 * called inside of a finally block.
	 */
	public void release() {
		doc.removePositionUpdater(updater);
		try {
			doc.removePositionCategory(category);
		} catch (BadPositionCategoryException e) {
			GroovyPlugin.getPlugin().logException("Should not happen", e);
		}
		doc = null;
		updater = null;
	}

	/**
	 * Creates a unique category if necessary. This should only happen if
	 * changes are multithreaded (which is probably a no no in any case) or if
	 * an action forgets to call restoreSelection or release.
	 */
	private void createUniqueCategory() {
		synchronized (doc) {
			category = SELECT_POSITION_CATEGORY;
			int i = 0;
			while (doc.containsPositionCategory(category))
				category = SELECT_POSITION_CATEGORY + i;
			doc.addPositionCategory(category);
		}
	}
}

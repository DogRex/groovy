package org.codehaus.groovy.eclipse.editor.actions;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * The base of all GroovyEclipse editing actions.
 * 
 * The term <i>editing action</i> is used broadly - any task to do with a
 * programmer editing source code is included. So changes to the document,
 * navigation, etc.
 * 
 * For convenience is has a doRun() method that catches and logs any
 * BadLocationException. it is simply called by IEditorActionDelegate#run which
 * can be implemented as normal if so desired. One of these methods must be
 * implemented to implement the editing action.
 * 
 * In addition this class contains many utility methods to easy the getting of
 * and setting of text in the Groovy source file being edited. With these
 * utility methods, the concrete actions are easily implemented in Groovy.
 * 
 * @author emp
 */
public abstract class EditingAction implements IEditorActionDelegate {
	protected IEditorPart editor;

	protected ISelection selection;

	public EditingAction() {
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		editor = targetEditor;
	}

	public ITextEditor getTextEditor() {
		try {
			return (ITextEditor) editor;
		} catch (ClassCastException e) {
			throw new IllegalStateException("Expecting a text editor, found "
					+ editor.getClass().getName(), e);
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	/**
	 * Override this if you want to implement the IEditorActionDelegate#run
	 * method yourself.
	 */
	public void run(IAction action) {
		try {
			doAction(action);
		} catch (BadLocationException e) {
			GroovyPlugin.getPlugin().logException("Editing action error.", e);
		}
	}

	/**
	 * Override this if you don't want to deal with BadLocationException
	 * logging.
	 * 
	 * @param action
	 * @throws BadLocationException
	 */
	public void doAction(IAction action) throws BadLocationException {
	}

	public Shell getShell() {
		return editor.getEditorSite().getShell();
	}

	/**
	 * Shows a message dialog. Handy while developing.
	 * 
	 * @param title
	 * @param message
	 */
	public void showMessage(String message) {
		showMessage("", message);
	}

	public void showMessage(String title, String message) {
		MessageDialog.openInformation(getShell(), title, message);
	}

	public IDocument getDocument() {
		IDocumentProvider provider = getTextEditor().getDocumentProvider();
		if (provider != null)
			return provider.getDocument(getTextEditor().getEditorInput());
		return null;
	}

	public String getDelimiter() {
		return TextUtilities.getDefaultLineDelimiter(getDocument());
	}

	public int getDelimiterLength() {
		return TextUtilities.getDefaultLineDelimiter(getDocument()).length();
	}

	public ITextSelection getTextSelection() {
		return (ITextSelection) getTextEditor().getSelectionProvider()
				.getSelection();
	}

	public int getLineOffset(int row) throws BadLocationException {
		return getDocument().getLineOffset(row);
	}

	public int getOffset(int row, int col) throws BadLocationException {
		return getDocument().getLineOffset(row) + col;
	}

	public int[] getRowCol(int offset) throws BadLocationException {
		int row = getDocument().getLineOfOffset(offset);
		int col = offset - getLineOffset(row);
		return new int[] { row, col };
	}

	public String getText(int offset, int len) throws BadLocationException {
		return getDocument().get(offset, len);
	}

	public void replaceText(int offset, int len, String text)
			throws BadLocationException {
		getDocument().replace(offset, len, text);
	}

	public void removeText(int offset, int len) throws BadLocationException {
		replaceText(offset, len, "");
	}

	public void insertText(int offset, String text) throws BadLocationException {
		getDocument().replace(offset, 0, text);
	}

	public int getLineLength(int row) throws BadLocationException {
		return getDocument().getLineInformation(row).getLength();
	}

	public String getLine(int row) throws BadLocationException {
		IRegion region = getDocument().getLineInformation(row);
		return getDocument().get(region.getOffset(), region.getLength());
	}

	public List getLines(int startRow, int endRow) throws BadLocationException {
		List list = new ArrayList();
		for (int i = startRow; i <= endRow; ++i) {
			list.add(getLine(i));
		}
		return list;
	}

	public String removeLine(int row) throws BadLocationException {
		IRegion region = getDocument().getLineInformation(row);
		String ret = getLine(row);
		removeText(region.getOffset(), region.getLength()
				+ getDelimiterLength());
		return ret;
	}

	public List removeLines(int startRow, int endRow)
			throws BadLocationException {
		List list = new ArrayList();
		for (int i = startRow; i <= endRow; ++i) {
			list.add(removeLine(i));
		}
		return list;
	}

	public void insertLine(int row, String line) throws BadLocationException {
		int offset = getLineOffset(row);
		replaceText(offset, 0, line + getDelimiterLength());
	}

	public void insertLines(int row, List lines) throws BadLocationException {
		for (int i = row; i < row + lines.size(); ++i)
			insertLine(i, (String) lines.get(i));
	}

	/**
	 * If there is any partial selection, get the complete lines that the
	 * selection partially covers.
	 * 
	 * @return A list of String of selected lines, which is empty if there is no
	 *         selection.
	 * @throws BadLocationException
	 */
	public List getSelectedLines() throws BadLocationException {
		ITextSelection sel = getTextSelection();
		return getLines(sel.getStartLine(), sel.getEndLine());
	}

	public String getCurrentLine() throws BadLocationException {
		ITextSelection sel = getTextSelection();
		return getLine(sel.getStartLine());
	}

	public void select(int offset, int length) {
		ISelection sel = new TextSelection(offset, length);
		getTextEditor().getSelectionProvider().setSelection(sel);
	}
}
package org.codehaus.groovy.eclipse.editor.actions;


import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

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
 * @author emp
 */
public abstract class EditingAction extends EditorPartFacade implements IEditorActionDelegate {
	protected ISelection selection;

	public EditingAction() {
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		super.setEditor(targetEditor);
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
}
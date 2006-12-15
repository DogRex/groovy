package org.codehaus.groovy.eclipse.actions;

import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

/**
 * @see IEditorActionDelegate
 */
public class RunGroovy implements IEditorActionDelegate {
	private IEditorPart activeEditor;

	/**
	 *
	 */
	public RunGroovy() {
	}

	/**
	 * @see IEditorActionDelegate#run
	 */
	public void run(IAction action)  {
		IFile file = (IFile) activeEditor.getEditorInput().getAdapter(IFile.class);
		if(file != null){
			GroovyModel.getModel().runGroovyMain(file);
		}

	}

	/**
	 * @see IEditorActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection)  {
	}

	/**
	 * @see IEditorActionDelegate#setActiveEditor
	 */
	public void setActiveEditor(IAction action, IEditorPart targetEditor)  {
		activeEditor = targetEditor;
	}
}

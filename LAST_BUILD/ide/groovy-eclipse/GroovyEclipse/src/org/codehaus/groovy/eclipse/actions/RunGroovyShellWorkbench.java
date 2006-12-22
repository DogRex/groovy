package org.codehaus.groovy.eclipse.actions;

import org.codehaus.groovy.eclipse.model.GroovyConsole;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Provides a project action to execute the Groovy console.
 * @see IObjectActionDelegate
 */
public class RunGroovyShellWorkbench implements IObjectActionDelegate {
	private ISelection selection;

	public RunGroovyShellWorkbench() {}

	/**
	 * @see IEditorActionDelegate#run
	 */
	public void run(IAction action)  {
        final IStructuredSelection s = ( IStructuredSelection )selection;
        final Object selected = s.getFirstElement();
        if( selected instanceof IJavaProject ) {
			GroovyConsole.getConsole().runGroovyShell((IJavaProject)selected);
		}
		//else GroovyPlugin.trace( "No IJavaProject selected" );
	}

	/**
	 * @see IEditorActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection)  {
        // StructuredSelection.elements[0] should be a JavaProject
        this.selection = selection;
	}

	public void setActivePart( IAction action, IWorkbenchPart targetPart ) {}
}

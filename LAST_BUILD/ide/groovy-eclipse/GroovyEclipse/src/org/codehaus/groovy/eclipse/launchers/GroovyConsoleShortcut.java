package org.codehaus.groovy.eclipse.launchers;

import org.codehaus.groovy.eclipse.model.GroovyConsole;
import org.eclipse.core.resources.IFile;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

/**
 * @see ILaunchShortcut
 */
public class GroovyConsoleShortcut implements ILaunchShortcut {

	public GroovyConsoleShortcut() {}

	/**
	 * @see ILaunchShortcut#launch
	 */
	public void launch(ISelection selection, String mode)  {
		// at the mo asume we get a structureselection get the file and presto
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structredSelection = (IStructuredSelection) selection;
			IFile file = (IFile)structredSelection.getFirstElement(); 
			GroovyConsole.getConsole().runGroovyConsole(file);
		}
	}

	/**
	 * @see ILaunchShortcut#launch
	 */
	public void launch(IEditorPart editor, String mode)  {
		// make sure we are saved as we run the groove from the file
		editor.getEditorSite().getPage().saveEditor(editor,false);
		IEditorInput input = editor.getEditorInput();
		IFile file = (IFile) input.getAdapter(IFile.class);
		GroovyConsole.getConsole().runGroovyConsole(file);
	}
}
/*
 * Created on Jan 19, 2004
 *  
 */
package org.codehaus.groovy.eclipse.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author zohar melamed
 *  
 */
public class GroovyDialogProvider {
	public boolean doesUserWantGroovySupport() {
		return MessageDialog.openQuestion(
			getShell(),
			"Groovy",
			"Add runtime groovy support and auto build to project ?");
	}
	
	public void errorRunningGroovyFile(IFile file,Exception e){
		MessageDialog.openError(getShell(),"Groovy Runner Error ","Error running "+file.getName()+" "+e.getMessage());
	}
	
	Shell getShell(){
		IWorkbenchWindow window= PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
			return null;
		return window.getShell();
	}
}

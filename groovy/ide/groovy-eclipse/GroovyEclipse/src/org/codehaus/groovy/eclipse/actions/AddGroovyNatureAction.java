package org.codehaus.groovy.eclipse.actions;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.builder.GroovyNature;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.codehaus.groovy.eclipse.model.GroovyProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class AddGroovyNatureAction implements IObjectActionDelegate {
	/**
	 * @see IEditorActionDelegate#run
	 */
	ISelection selection ;
	IWorkbenchPart targetPart ;
	IAction action;
	public void run(IAction action) {
		GroovyPlugin.trace("AddGroovySupportActoin.run()");
		GroovyPlugin plugin = GroovyPlugin.getPlugin();
		IStructuredSelection s = (IStructuredSelection)selection;
		IProject targetProject = (IProject)s.getFirstElement();
		try {
			if (! targetProject.hasNature(GroovyNature.GROOVY_NATURE)){
				GroovyModel model = GroovyModel.getModel();
				GroovyProject groovyProject = model.getProject(targetProject);
				groovyProject.addGrovyExclusionFilter(targetProject);
				plugin.addGroovyRuntime(targetProject);
				groovyProject.addGroovyNature(targetProject);		
			}
		} catch (CoreException e) {
			plugin.logException("failed to add groovy support", e);
		} finally {
			plugin.listenToChanges();
		}
	}
	/**
	 * @see IEditorActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		//StructuredSelection.elements[0] is JavaProject
		this.selection = selection;
	}
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		//org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart
		//targetPart.
		this.targetPart =  targetPart;
		this.action = action;
		// TODO Auto-generated method stub
	}
}

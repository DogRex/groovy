/*
 * Created on 17-Dec-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.codehaus.groovy.eclipse.launchers;


import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.DefaultLabelProvider;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaMainTab;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ListDialog;



/**
 * @author MelamedZ
 *
 * 
To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GroovyLauncherTab extends JavaMainTab {

	

	protected void handleSearchButtonSelected() {
		
		IJavaProject javaProject = getJavaProject();
		ListDialog dialog = new ListDialog(getShell());
		dialog.setBlockOnOpen(true);
		dialog.setMessage("Select a Groovy class to run");
		dialog.setTitle("Choose Groovy Class");
		final String [] availableClasses = GroovyModel.getModel().findAllRunnableClasses(javaProject);
		dialog.setContentProvider(new ArrayContentProvider());
		dialog.setLabelProvider(new DefaultLabelProvider());
		dialog.setInput(availableClasses);
		if (dialog.open() == Window.CANCEL) {
			return;
		}
		
		Object[] results = dialog.getResult();
		if(results == null || results.length == 0){
			return;
		}
		
		fMainText.setText(results[0].toString());
		
	}
	
	
	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return "Groovy Main"; //$NON-NLS-1$
	}	
	
	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getImage()
	 */
	public Image getImage() {
		return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_CLASS);
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#activated(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void activated(ILaunchConfigurationWorkingCopy workingCopy) {
		// TODO Auto-generated method stub
		super.activated(workingCopy);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		fSearchExternalJarsCheckButton.setVisible(false);
		fStopInMainCheckButton.setVisible(false);
	}

    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
     */
    public void setDefaults(ILaunchConfigurationWorkingCopy config) {
        super.setDefaults(config);
        config.setAttribute(ILaunchConfiguration.ATTR_SOURCE_LOCATOR_ID, GroovySourceLocator.ID);
    }
}

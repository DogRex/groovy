/*
 * Created on 17-Dec-2003
 */
package org.codehaus.groovy.eclipse.launchers;


import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaMainTab;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ListDialog;



/**
 * Dialog for selecting the groovy class to run.
 *
 * @author MelamedZ
 */
public class GroovyLauncherTab extends JavaMainTab {

	/**
	 * Dialog for selecting the groovy class to run.
	 */
	protected void handleSearchButtonSelected() {
		IJavaProject javaProject = getJavaProject();
		/*
		 * Note that the set of available classes may be zero and hence the
         * dialog will obviously not display any classes; in which case the
		 * project needs to be compiled.
		 */
		final String [] availableClasses = GroovyModel.getModel().findAllRunnableClasses(javaProject);
		if (availableClasses.length == 0) {
			MessageDialog.openWarning(getShell(), "No Groovy classes to run",
					"There are no compiled groovy classes to run in this project");
			return;
		}
		ListDialog dialog = new ListDialog(getShell());
		dialog.setBlockOnOpen(true);
		dialog.setMessage("Select a Groovy class to run");
		dialog.setTitle("Choose Groovy Class");
		dialog.setContentProvider(new ArrayContentProvider());
		dialog.setLabelProvider(new LabelProvider());
		dialog.setInput(availableClasses);
		if (dialog.open() == Window.CANCEL) {
			return;
		}
		
		Object[] results = dialog.getResult();
		if (results == null || results.length == 0){
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

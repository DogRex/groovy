package org.codehaus.groovy.eclipse.wizards;

import java.lang.reflect.InvocationTargetException;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.actions.WorkbenchRunnableAdapter;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;




/**
 * @see Wizard
 */
public class NewTestWizard extends NewElementWizard  {
	private NewTestWizardPage fPage;

	public NewTestWizard () {
		super();
		setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWCLASS);
		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
		setWindowTitle("Create a new Groovy class"); 
	}

	/*
	 * @see Wizard#createPages
	 */	
	public void addPages() {
		super.addPages();
		fPage= new NewTestWizardPage();
		addPage(fPage);
		fPage.init(getSelection());
	}	


	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.wizards.NewElementWizard#finishPage(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void finishPage(IProgressMonitor monitor){
		IFile file;
		try {
			file = fPage.createGroovyTest(monitor);
			openResource(file);
		} catch (Exception e) {
			GroovyPlugin.getPlugin().logException("while creating a groovy test class",e);
		} 
	}
	
	public boolean performFinish() {
		IWorkspaceRunnable op = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException,
					OperationCanceledException {
				finishPage(monitor);
			}
		};
		try {
			getContainer().run(false, true, new WorkbenchRunnableAdapter(op));
		} catch (InvocationTargetException e) {
			handleFinishException(getShell(), e);
			return false;
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.wizards.NewElementWizard#getSchedulingRule()
	 */
	protected ISchedulingRule getSchedulingRule() {
		return new ISchedulingRule() {
			public boolean contains(ISchedulingRule rule) {
				return false;
			}
			public boolean isConflicting(ISchedulingRule rule) {
				return false;
			}};
	}
}


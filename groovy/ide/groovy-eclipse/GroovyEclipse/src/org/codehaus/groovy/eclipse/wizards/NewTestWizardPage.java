/*
 * Created on Jan 28, 2004
 *  
 */
package org.codehaus.groovy.eclipse.wizards;

/**
 * @author zohar melamed
 *  
 */
import java.io.IOException;
import java.net.MalformedURLException;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author MelamedZ
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class NewTestWizardPage extends NewTypeWizardPage {

	/**
	 * Creates a new <code>NewClassWizardPage</code>
	 */
	public NewTestWizardPage() {
		super(true, "New Groovy Test Settings");

		setTitle("Groovy Test Class");
		setDescription("Create a new Groovy unit test class");
	}

	// -------- Initialization ---------

	/**
	 * The wizard owning this page is responsible for calling this method with
	 * the current selection. The selection is used to initialize the fields of
	 * the wizard page.
	 * 
	 * @param selection
	 *            used to initialize the fields
	 */
	public void init(IStructuredSelection selection) {
		IJavaElement jelem = getInitialJavaElement(selection);
		initContainerPage(jelem);
		initTypePage(jelem);
		doStatusUpdate();
	}

	// ------ validation --------
	private void doStatusUpdate() {
		// status of all used components
		IStatus[] status =
			new IStatus[] {
				fContainerStatus,
				isEnclosingTypeSelected() ? fEnclosingTypeStatus : fPackageStatus,
				fTypeNameStatus,
				fModifierStatus,
				fSuperClassStatus,
				fSuperInterfacesStatus };

		// the mode severe status will be displayed and the ok button
		// enabled/disabled.
		updateStatus(status);
	}

	/*
	 * @see NewContainerWizardPage#handleFieldChanged
	 */
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);

		doStatusUpdate();
	}

	// ------ ui --------

	/*
	 * @see WizardPage#createControl
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		// pick & choose the wanted UI components

		createContainerControls(composite, nColumns);
		createPackageControls(composite, nColumns);

		createSeparator(composite, nColumns);

		createTypeNameControls(composite, nColumns);

		setControl(composite);

		Dialog.applyDialogFont(composite);
	}

	/*
	 * @see WizardPage#becomesVisible
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			setFocus();
		}
	}

	public IFile createGroovyTest(IProgressMonitor monitor) throws CoreException, MalformedURLException, JavaModelException, IOException {
		monitor.beginTask("Creating Groovy Test...",2);
		IPackageFragment packageFragment = getPackageFragment();
		// add junit if required
		GroovyPlugin.getPlugin().addJunitSupprt(packageFragment.getJavaProject());
		StringBuffer src = new StringBuffer();
		monitor.worked(1);
		src.append("import  groovy.util.GroovyTestCase");
		src.append("\n\n\n");
		src.append("class ");
		src.append(getTypeName());
		src.append(" extends GroovyTestCase");
		src.append("{\n\n\t void testSomething() {\n\t}\n}");
		monitor.worked(1);
		monitor.done();
		return WizardUtil.createGroovyType(packageFragment, getTypeName() + ".groovy", src.toString());
	}

}

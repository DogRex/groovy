/*
 * Created on 27-Jan-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.codehaus.groovy.eclipse.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author MelamedZ
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class NewClassWizardPage extends NewTypeWizardPage {
	
	
	/**
	 * Creates a new <code>NewClassWizardPage</code>
	 */
	public NewClassWizardPage() {
		super(true, "New Groovy Class Settings");
		
		setTitle("Groovy Class"); 
		setDescription("Create a new Groovy class"); 
	}
	
	// -------- Initialization ---------
	
	/**
	 * The wizard owning this page is responsible for calling this method with the
	 * current selection. The selection is used to initialize the fields of the wizard 
	 * page.
	 * 
	 * @param selection used to initialize the fields
	 */
	public void init(IStructuredSelection selection) {
		IJavaElement jelem= getInitialJavaElement(selection);
		initContainerPage(jelem);
		initTypePage(jelem);
		doStatusUpdate();
	}
	
	// ------ validation --------
	private void doStatusUpdate() {
		// status of all used components
		IStatus[] status= new IStatus[] {
									  fContainerStatus,
									  		isEnclosingTypeSelected() ? fEnclosingTypeStatus : fPackageStatus,
																	  fTypeNameStatus,
																	  fModifierStatus,
																	  fSuperClassStatus,
																	  fSuperInterfacesStatus
		};
		
		// the mode severe status will be displayed and the ok button enabled/disabled.
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
		
		Composite composite= new Composite(parent, SWT.NONE);
		
		int nColumns= 4;
		
		GridLayout layout= new GridLayout();
		layout.numColumns= nColumns;		
		composite.setLayout(layout);
		
		// pick & choose the wanted UI components
		
		createContainerControls(composite, nColumns);	
		createPackageControls(composite, nColumns);	
		
		createSeparator(composite, nColumns);
		
		createTypeNameControls(composite, nColumns);
		createSuperClassControls(composite, nColumns);
		
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
	
	
	public IFile createGroovyType(IProgressMonitor monitor) throws CoreException, InterruptedException {
		String superClass = getSuperClass();
		IPackageFragment packageFragment = getPackageFragment();
		String shortSuperName = WizardUtil.getSuperName(packageFragment,superClass);
		StringBuffer src = new StringBuffer();
		if(shortSuperName.length() >0){
			src.append("import  ");
			src.append(superClass);
			src.append("\n\n\n");
		}
		
		src.append("class ");
		src.append(getTypeName());
		if(shortSuperName.length() >0){
			src.append(" extends "+shortSuperName);
		}
		src.append("{\n\n\t static void main(args) {\n\t}\n}");
		return WizardUtil.createGroovyType(packageFragment,getTypeName()+".groovy",src.toString());
	}

}

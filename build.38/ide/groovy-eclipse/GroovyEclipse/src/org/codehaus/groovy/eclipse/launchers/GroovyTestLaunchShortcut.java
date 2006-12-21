package org.codehaus.groovy.eclipse.launchers;

import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.codehaus.groovy.eclipse.model.GroovyProject;
import org.codehaus.groovy.eclipse.model.GroovyProjectModel;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.junit.launcher.JUnitLaunchDescription;
import org.eclipse.jdt.internal.junit.launcher.JUnitLaunchShortcut;
import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

/**
 * This shortcut will launch JUnit tests that are in .groovy files
 * 
 * @author David Kerber
 * @see ILaunchShortcut
 */
public class GroovyTestLaunchShortcut extends JUnitLaunchShortcut {

	public void launch(IEditorPart editor, String mode) {
		// make sure we are saved as we run the groovy from the file	
		editor.getEditorSite().getPage().saveEditor(editor,false); 
		IEditorInput input = editor.getEditorInput();
		IFile file = (IFile) input.getAdapter(IFile.class);
		launchJUnit(file, mode) ;	
	}

	public void launch(ISelection selection, String mode) {
		if( selection instanceof IStructuredSelection ) {
			IStructuredSelection structured = (IStructuredSelection) selection ;
			File file = (File)structured.getFirstElement();
			launchJUnit(file, mode) ;
		}
	}

	/**
	 * Launches JUnit.
	 * 
	 * @param file - The file to launch JUnit for
	 * @param mode - The run mode
	 */
	public void launchJUnit(final IFile file, final String mode ) {
		try {
			IJavaProject javaProject = JavaCore.create(file.getProject()) ;
			GroovyProject project = GroovyModel.getModel().getProject(javaProject.getProject());
			List classNodeList = project.getModel().getClassesForFile( file );

			if( classNodeList == null || classNodeList.isEmpty() ) {
				GroovyPlugin.getPlugin().getDialogProvider().errorRunningGroovyFile(file,
						new Exception("failed to find a classNode, try rebuilding"));
				return ; 
			}

			ClassNode node = (ClassNode) classNodeList.get(0) ;

			if( !GroovyProjectModel.isTestCaseClass( node ) ) {
				MessageDialog.openInformation(JUnitPlugin.getActiveWorkbenchShell(), JUnitMessages.LaunchTestAction_dialog_title,
						JUnitMessages.LaunchTestAction_message_notests);
				return ; 
			}

			IClassFile classFile = (IClassFile) javaProject.findElement(new Path(node.getName() + ".class"));
			IType theClass = classFile.getType() ; 

			JUnitLaunchDescription description = describeTypeLaunch(theClass) ; 
			ILaunchConfiguration config= findOrCreateLaunchConfiguration(mode, this, description);

			if (config != null) {
				DebugUITools.launch(config, mode);
			}

		} catch (JavaModelException e) {
			//Don't know what to do here
			e.printStackTrace();
		} catch (LaunchCancelledByUserException e) {
			//No Big Deal
		}		
	}

	/**
	 * This method intercepts the JUnitLaunchShortcut method to ensure that we run in JUnit 3, for 
	 * some reason, doesn't work with JUnit 4.
	 * 
	 * @see org.eclipse.jdt.internal.junit.launcher.JUnitLaunchShortcut#findOrCreateLaunchConfiguration(java.lang.String, org.eclipse.jdt.internal.junit.launcher.JUnitLaunchShortcut, org.eclipse.jdt.internal.junit.launcher.JUnitLaunchDescription)
	 */
	public ILaunchConfiguration findOrCreateLaunchConfiguration ( String mode, JUnitLaunchShortcut registry, JUnitLaunchDescription description)
	throws LaunchCancelledByUserException {
		ILaunchConfiguration config = super.findOrCreateLaunchConfiguration(mode, registry, description) ;
		try {
			ILaunchConfigurationWorkingCopy workingConfig = config.getWorkingCopy() ; 
			workingConfig.setAttribute("org.eclipse.jdt.junit.TEST_KIND", "org.eclipse.jdt.junit.loader.junit3");
			workingConfig.doSave() ;
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return config ; 
	}
}
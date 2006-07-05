package org.codehaus.groovy.eclipse.launchers;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.codehaus.groovy.eclipse.model.GroovyProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.JavaLaunchDelegate;

/**
 * @see ILaunchConfigurationDelegate
 */
public class GroovyLaunchConfigurationDelegate extends JavaLaunchDelegate  {

	/**
	 * @see ILaunchConfigurationDelegate#launch
	 */
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		IJavaProject project = getJavaProject(configuration);
		GroovyModel gm = GroovyModel.getModel();
		GroovyProject gp = gm.getProject(project.getProject());
		String className = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, (String)null);
		ClassNode classNode = gp.getClassNodeForName(className);
		ILaunchConfigurationWorkingCopy config = null;
		if (!gp.hasRunnableMain(classNode) && gp.isTestCaseClass(classNode)) {
			config = configuration.getWorkingCopy();
			config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "junit.textui.TestRunner");
			config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, className);
			config.doSave();
		}
        super.launch(configuration, mode, launch, monitor);
        IJavaDebugTarget target = (IJavaDebugTarget)launch.getDebugTarget();
        if (target != null) {
            target.setDefaultStratum("Groovy");
        }
        if (config != null) {
			config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, className);
			config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, "");
        	config.doSave();
        }
	}	
}

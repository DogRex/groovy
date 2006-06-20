package org.codehaus.groovy.eclipse.launchers;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.launching.JavaLaunchDelegate;

/**
 * @see ILaunchConfigurationDelegate
 */
public class GroovyLaunchConfigurationDelegate extends JavaLaunchDelegate  {

	/**
	 * @see ILaunchConfigurationDelegate#launch
	 */
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
        super.launch(configuration, mode, launch, monitor);
        IJavaDebugTarget target = (IJavaDebugTarget)launch.getDebugTarget();
        if (target != null) {
            target.setDefaultStratum("Groovy");
        }
	}
	
}

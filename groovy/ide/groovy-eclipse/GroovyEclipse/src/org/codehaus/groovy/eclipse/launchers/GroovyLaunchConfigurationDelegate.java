package org.codehaus.groovy.eclipse.launchers;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

/**
 * @see ILaunchConfigurationDelegate
 */
public class GroovyLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {
	/**
	 *
	 */
	public GroovyLaunchConfigurationDelegate() {
	}

	/**
	 * @see ILaunchConfigurationDelegate#launch
	 */
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		String projectName = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, "");
		String className = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,"");
		String [] args = getProgramArguments(configuration);
			
		GroovyModel.getModel().runGroovyMain(projectName,className,args);
	}
	
	public String[] getProgramArguments(ILaunchConfiguration configuration) throws CoreException {
		String arguments= configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, ""); //$NON-NLS-1$
		StringTokenizer tokenizer = new StringTokenizer(arguments);
		List result = new ArrayList();
		while(tokenizer.hasMoreTokens()){
			result.add(tokenizer.nextToken());
		}
		return (String[]) result.toArray(new String[result.size()]);
	}
}

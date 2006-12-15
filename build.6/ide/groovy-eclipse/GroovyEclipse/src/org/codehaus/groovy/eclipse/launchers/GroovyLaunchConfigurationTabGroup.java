package org.codehaus.groovy.eclipse.launchers;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;


/**
 * @see AbstractLaunchConfigurationTabGroup
 */
public class GroovyLaunchConfigurationTabGroup
	extends AbstractLaunchConfigurationTabGroup {
	/**
	 *  
	 */
	public GroovyLaunchConfigurationTabGroup() {
	}

	/**
	 * @see AbstractLaunchConfigurationTabGroup#createTabs
	 */
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs =
			new ILaunchConfigurationTab[] {
				new GroovyLauncherTab(),
				new JavaArgumentsTab()};
//				new JavaJRETab(),
//				new JavaClasspathTab(),
//				new CommonTab()};

		setTabs(tabs);
	}
}

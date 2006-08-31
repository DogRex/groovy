/**
*
*
*
*/

package org.codehaus.groovy.eclipse.launchers;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;


/**
 * @author MelamedZ
 *
 *	run a GroovyClass - based on the TestRunner from 
 *  "Contributing to Eclipse: Principles, Patterns, and Plug-Ins"
 */
public class GroovyRunner {

		private IJavaProject project;

		public GroovyRunner run(String classToLaunch, String args[], IJavaProject javaProject) throws CoreException {
			GroovyPlugin.trace("running "+classToLaunch);
			this.project = javaProject;
			
			IVMInstall vmInstall= getVMInstall();
			if (vmInstall == null)
				return this;
			IVMRunner vmRunner= vmInstall.getVMRunner(ILaunchManager.RUN_MODE);
			if (vmRunner == null)
				return this;

			String[] classPath = JavaRuntime.computeDefaultRuntimeClassPath(project);
			VMRunnerConfiguration vmConfig= new VMRunnerConfiguration(classToLaunch, classPath);

			vmConfig.setProgramArguments(args);

			ILaunch launch= new Launch(null, ILaunchManager.RUN_MODE, null);
			
			vmRunner.run(vmConfig, launch,null);
			DebugPlugin.getDefault().getLaunchManager().addLaunch(launch);
			return this;
		}

		private IVMInstall getVMInstall() throws CoreException {
			IVMInstall vmInstall= JavaRuntime.getVMInstall(project);
			if (vmInstall == null)
				vmInstall= JavaRuntime.getDefaultVMInstall();
			return vmInstall;
		}
	}	



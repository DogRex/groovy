/**
*
*
*
*/

package org.codehaus.groovy.eclipse.launchers;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILibrary;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
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

		private int port;
		private IJavaProject project;
		private BufferedReader reader;

		public void run(String classToLaunch, String args[], IJavaProject project) throws CoreException {
			GroovyPlugin.trace("running "+classToLaunch);
			this.project = project;
			
			IVMInstall vmInstall= getVMInstall();
			if (vmInstall == null)
				return;
			IVMRunner vmRunner= vmInstall.getVMRunner(ILaunchManager.RUN_MODE);
			if (vmRunner == null)
				return;

			String[] classPath= computeClasspath();
			VMRunnerConfiguration vmConfig= new VMRunnerConfiguration(classToLaunch, classPath);

			vmConfig.setProgramArguments(args);

			ILaunch launch= new Launch(null, ILaunchManager.RUN_MODE, null);
			
			vmRunner.run(vmConfig, launch,null);
			DebugPlugin.getDefault().getLaunchManager().addLaunch(launch);

		}

		private String[] computeClasspath() throws CoreException {
			// build a class path including all the jars needed to run groovy 
			// present in the lib dir of the plugin 
			GroovyPlugin plugin= GroovyPlugin.getPlugin();
			URL url= plugin.getDescriptor().getInstallURL();
			ILibrary[] libraries = plugin.getDescriptor().getRuntimeLibraries();
			int additionalLibCount = libraries.length; 
			String[] defaultPath= JavaRuntime.computeDefaultRuntimeClassPath(project);
			String[] classPath= new String[defaultPath.length + additionalLibCount];
			System.arraycopy(defaultPath, 0, classPath, additionalLibCount, defaultPath.length);
			try{
				for (int i = 0; i < libraries.length; i++) {
					ILibrary library = libraries[i];
					IPath path = library.getPath();
					classPath[i] = Platform.asLocalURL(new URL(url, path.toString())).getFile();  //$NON-NLS-1$
				}
			} catch (IOException e) {
				IStatus status= new Status(IStatus.ERROR, 
						plugin.getDescriptor().getUniqueIdentifier(), 
						IStatus.OK, "Could not determine path", e); //$NON-NLS-1$
				throw new CoreException(status);
			}			
			
			return classPath;
		}
		

		private IVMInstall getVMInstall() throws CoreException {
			IVMInstall vmInstall= JavaRuntime.getVMInstall(project);
			if (vmInstall == null)
				vmInstall= JavaRuntime.getDefaultVMInstall();
			return vmInstall;
		}
	}	



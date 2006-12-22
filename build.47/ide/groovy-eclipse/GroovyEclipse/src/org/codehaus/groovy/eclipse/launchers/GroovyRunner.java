/**
*
*
*
*/

package org.codehaus.groovy.eclipse.launchers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.osgi.util.ManifestElement;

/**
 * @author MelamedZ
 * @author <a href="mailto:tomstrummer@gmail.com">Tom Nichols</a>
 *
 *	run a GroovyClass - based on the TestRunner from 
 *  "Contributing to Eclipse: Principles, Patterns, and Plug-Ins"
 */
public class GroovyRunner {

	/** This will be defined on the first call to 
	 * {@link #findGroovyClasspath()} */
	private static String groovyClasspath = null;
	
	//private IJavaProject project;
	
	public GroovyRunner run(String classToLaunch, String args[], 
			IJavaProject javaProject ) throws CoreException {

		String[] classPath = JavaRuntime.computeDefaultRuntimeClassPath(javaProject);

		return run( classToLaunch, args, javaProject, classPath );
	}
	
	public GroovyRunner run( String classToLaunch, String args[], 
			IJavaProject javaProject, String[] classPath ) throws CoreException {
	
		GroovyPlugin.trace("running "+classToLaunch);
		//this.project = javaProject;
		
		IVMInstall vmInstall= getVMInstall(javaProject);
		if (vmInstall == null)
			return this;
		IVMRunner vmRunner= vmInstall.getVMRunner(ILaunchManager.RUN_MODE);
		if (vmRunner == null)
			return this;

		VMRunnerConfiguration vmConfig= new VMRunnerConfiguration(classToLaunch, classPath );
		
		vmConfig.setProgramArguments(args);

		ILaunch launch= new Launch(null, ILaunchManager.RUN_MODE, null);
		
		vmRunner.run(vmConfig, launch,null);
		DebugPlugin.getDefault().getLaunchManager().addLaunch(launch);
		return this;
	}
	
	/**
	 * Same as {@link #run(String, String[], IJavaProject)}, but adds the 
	 * internal groovy jar to the project's classpath. 
	 * @param classToLaunch
	 * @param args
	 * @param javaProject
	 * @return
	 * @throws Exception
	 */
	public GroovyRunner runNonGroovy( String classToLaunch, String args[], 
			IJavaProject javaProject ) throws Exception {
		
		String[] classPath = JavaRuntime.computeDefaultRuntimeClassPath(javaProject);

		String groovyPath = findGroovyClasspath();
		List l = new ArrayList();
		l.addAll( Arrays.asList( classPath ) );
		l.add( groovyPath );
		classPath = (String[])l.toArray( classPath );

/*		StringBuilder sb = new StringBuilder("Classpath:\n");
		for( int i=0; i<classPath.length; i++ ) 
			sb.append( classPath[i] + "\n" );
		GroovyPlugin.trace( sb.toString() );
*/		
		return run( classToLaunch, args, javaProject, classPath );
	}
	
	/**
	 * Locates & returns the file path of this plugin's Groovy jar.
	 * @return the filesystem path of the groovy jar.
	 * @throws Exception
	 */
	protected String findGroovyClasspath() throws Exception {
		synchronized( this.getClass() ) {
			if( groovyClasspath == null ) {
				String pathStr = null;
				ManifestElement[] classes = GroovyPlugin.getBundleClasspath();
				for( int i=0; i<classes.length; i++ ) {
					if( classes[i].getValue().indexOf("groovy-all" ) > -1 )
						pathStr = classes[i].getValue();
				}
				Path path = new Path( pathStr ); 
						//"lib/groovy-all-1.0-RC-01.jar");
				
				URL fileURL = FileLocator.find( 
						GroovyPlugin.getPlugin().getBundle(), path, null );
				fileURL = FileLocator.toFileURL(fileURL); 
				//path = new Path( fileURL. );
				groovyClasspath = new File( fileURL.getFile() ).getAbsolutePath();
			}
			return groovyClasspath;
		}
	}
	
	private IVMInstall getVMInstall(IJavaProject project) throws CoreException {
		IVMInstall vmInstall= JavaRuntime.getVMInstall(project);
		if (vmInstall == null)
			vmInstall= JavaRuntime.getDefaultVMInstall();
		return vmInstall;
	}
}
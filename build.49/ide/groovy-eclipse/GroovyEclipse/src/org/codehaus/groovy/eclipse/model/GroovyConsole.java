package org.codehaus.groovy.eclipse.model;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.launchers.GroovyRunner;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * This will run the {@link groovy.ui.Console} or 
 * {@link groovy.ui.InteractiveShell} and include all project dependencies.
 *
 * @author <a href="mailto:tomstrummer@gmail.com">Tom Nichols</a>
 */
public class GroovyConsole {
	private static GroovyConsole theConsole  = null;
	
	protected static final String CONSOLE_CLASS_NAME = //"groovy.ui.Console";
		groovy.ui.Console.class.getName(); //gives compile-time safety.
	protected static final String SHELL_CLASS_NAME = //"groovy.ui.InteractiveShell";
		groovy.ui.InteractiveShell.class.getName();
	
	protected static String groovyClasspath = null;

	/**
	 * @return the Console class.
	 */
	public static GroovyConsole getConsole() {
		if( theConsole == null ) 
			synchronized ( GroovyConsole.class ) {
				if( theConsole == null ) 
					theConsole = new GroovyConsole();
			}
		return theConsole;
	}
	
	/**
	 * Uses the {@link GroovyRunner} to run the given className given 
	 * the project
	 * TODO also adds the Groovy JAR to the classpath if it is not in the 
	 * project already.
	 * @param className
	 * @param proj
	 */
	protected void run( String className, IJavaProject proj ) {
		try {
			if( proj.findType( className ) == null ) {
				new GroovyRunner().runNonGroovy( className, new String[0], proj );
			}
			else new GroovyRunner().run( className, new String[0], proj );
		}
		catch( Exception ex ) {
			GroovyPlugin.getPlugin().logException( 
					"Failed to launch Groovy class: " + className, ex );
			GroovyPlugin.getPlugin().getDialogProvider().errorRunningGroovy( ex );
		}
	}
	
	/**
	 * Launches the {@link groovy.ui.Console} with the given file's project 
	 * classpath.
	 * @param file
	 */
	public void runGroovyConsole( IFile file ) {
		runGroovyConsole( JavaCore.create(file.getProject()) );
	}
	
	/**
	 * Launches the {@link groovy.ui.Console} with the given project's classpath.
	 * @param file
	 */
	public void runGroovyConsole( IJavaProject proj ) {
		run( CONSOLE_CLASS_NAME, proj );
	}
	
	/**
	 * Launches the {@link groovy.ui.InteractiveShell} with the given file's 
	 * project classpath.
	 * @param file
	 */
	public void runGroovyShell( IFile file ) {
		runGroovyShell( JavaCore.create(file.getProject()) );
	}
		
	/**
	 * Launches the {@link groovy.ui.InteractiveShell} with the given project's 
	 * classpath.
	 * @param file
	 */
	public void runGroovyShell( IJavaProject proj ) {
		run( SHELL_CLASS_NAME, proj );
	}
}
/*
 * Created on 19-Jan-2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.codehaus.groovy.eclipse.test;

import groovy.GroovyRuntimePlugin;

import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author MelamedZ
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class GroovyPluginTestCase extends EclipseTestCase {

	public void testNatureAddAndRemove() throws CoreException {
		GroovyPlugin.getPlugin().addGroovyNature(testProject.getProject());

		assertTrue(hasGroovyNature());
		assertTrue(hasGroovyBuilder());

		GroovyPlugin.getPlugin().removeGroovyNature(testProject.getProject());

		assertFalse(hasGroovyNature());
		assertFalse(hasGroovyBuilder());
	}

	public void testManualAddGroovyRuntime() throws JavaModelException {

		List groovyRuntimeJars = verifyNoGroovyRuntime();

		plugin.addGroovyRuntime(testProject.getProject());

		for (Iterator iter = groovyRuntimeJars.iterator(); iter.hasNext();) {
			String jarName = (String) iter.next();
			assertTrue("expecting groovy runtime libs", testProject.hasJar(jarName));
		}
	}

	private List verifyNoGroovyRuntime() throws JavaModelException {
		List groovyRuntimeJars = GroovyRuntimePlugin.getPlugin().getGroovyRuntimeJars();
		for (Iterator iter = groovyRuntimeJars.iterator(); iter.hasNext();) {
			String jarName = (String) iter.next();
			assertFalse(
				"initialy none of the groovy runtime libs should be part of the project",
				testProject.hasJar(jarName));
		}

		return groovyRuntimeJars;
	}

	private void verifyGroovyRuntime(List groovyRuntimeJars) throws JavaModelException {
		for (Iterator iter = groovyRuntimeJars.iterator(); iter.hasNext();) {
			String jarName = (String) iter.next();
			assertTrue("all groovy runtime libs should be part of the project", testProject.hasJar(jarName));
		}
	}


	private boolean hasGroovyBuilder() throws CoreException {
		ICommand[] commands = testProject.getProject().getDescription().getBuildSpec();
		boolean found = false;
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(GroovyPlugin.GROOVY_BUILDER)) {
				found = true;
			}
		}
		return found;
	}

	private boolean hasGroovyNature() throws CoreException {
		return testProject.getProject().hasNature(GroovyPlugin.GROOVY_NATURE);
	}

}

/*
 * Created on 15-Jan-2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.codehaus.groovy.eclipse.test;

import junit.framework.TestCase;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.codehaus.groovy.eclipse.ui.GroovyDialogProvider;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IPackageFragment;

/**
 * @author MelamedZ
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class EclipseTestCase extends TestCase {
	protected TestProject testProject;
	protected IPackageFragment pack;
	protected GroovyPlugin plugin;
	protected GroovyModel model;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		testProject = new TestProject();
		plugin = GroovyPlugin.getPlugin();
		model = GroovyModel.getModel();
		plugin.setDialogProvider(new GroovyDialogProvider() {
			public boolean doesUserWantGroovySupport() {
				return true;
			}

			public void errorRunningGroovyFile(IFile file, Exception e) {
			}

		});
	}

	protected void disableAutoGroovySupport() {
		plugin.setDialogProvider(new GroovyDialogProvider() {
			public boolean doesUserWantGroovySupport() {
				return false;
			}

			public void errorRunningGroovyFile(IFile file, Exception e) {
			}

		});
	}
	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		testProject.dispose();
	}

}

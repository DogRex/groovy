/*
 * Created on Jan 24, 2004
 *
 */
package org.codehaus.groovy.eclipse.test;

import java.io.IOException;

import org.codehaus.groovy.eclipse.editor.contentoutline.GroovyASTContentProvider;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

/**
 * @author zohar melamed
 *
 */
public class GroovyContentOutlineTestCase extends EclipseTestCase {
	
	private GroovyASTContentProvider provider;
	private Object[] roots;

	public void testASTContentProvider() throws CoreException, IOException {
		// 2 top level objects : package/class
		assertEquals(2,roots.length);
		// the package has no children
		assertFalse(provider.hasChildren(roots[0]));
		//import has one child
		//assertEquals(1,provider.getChildren(roots[1]).length);
		//TestClass has 6 children ( 2 fields 1 ctor 3 methods)
		assertEquals(6,provider.getChildren(roots[1]).length);
	}

	public void testASTLableProvider() {
		
	}
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		disableAutoGroovySupport();
		String className = "TestClass.groovy";
		IFile file = testProject.createGroovyTypeAndPackage("pack1",className,
				getClass().getResource("groovyfiles/"+className).openStream());
		
		plugin.addGroovyRuntime(testProject.getProject());
		model.buildGroovyContent(testProject.getJavaProject());
		provider = new GroovyASTContentProvider();
		roots = provider.getElements(file);
		
	}

}

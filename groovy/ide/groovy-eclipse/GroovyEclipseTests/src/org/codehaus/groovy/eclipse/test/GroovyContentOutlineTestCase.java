/*
 * Created on Jan 24, 2004
 *
 */
package org.codehaus.groovy.eclipse.test;

import java.io.IOException;

import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.eclipse.editor.contentoutline.GroovyASTContentProvider;
import org.codehaus.groovy.eclipse.editor.contentoutline.GroovyASTLabelProvider;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * @author zohar melamed
 *
 */
public class GroovyContentOutlineTestCase extends EclipseTestCase {
	
	private GroovyASTContentProvider provider;
	private Object[] roots;
	private GroovyASTLabelProvider labelProvider;

	public void testASTContentProvider() throws CoreException, IOException {
		// 3 top level objects : package/class/imports
		assertEquals(3,roots.length);
		// the package has no children
		assertFalse(provider.hasChildren(roots[0]));
		//import has one child
		//assertEquals(1,provider.getChildren(roots[1]).length);
		//TestClass has 6 children ( 2 fields 1 ctor 3 methods)
		assertEquals(6,provider.getChildren(roots[2]).length);
	}

	public void testASTLableProvider() {
		// package name is pack1
		assertEquals("pack1",labelProvider.getText(roots[0]));
		// import node just says "import declerations"
		assertEquals("import declerations",labelProvider.getText(roots[1]));
		// the class is MyClass
		assertEquals("MyClass",labelProvider.getText(roots[2]));
		// now chcek the class members
		Object[] members = provider.getChildren(roots[2]);
		assertEquals("name",labelProvider.getText(members[0]));
		assertEquals("number",labelProvider.getText(members[1]));
		assertEquals("MyClass()",labelProvider.getText(members[2]));
		assertEquals("voidMethod()",labelProvider.getText(members[3]));
		assertEquals("intMethod()",labelProvider.getText(members[4]));
		assertEquals("staticMethod(args)",labelProvider.getText(members[5]));

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
		model.buildGroovyContent(testProject.getJavaProject(), new NullProgressMonitor(),
								IncrementalProjectBuilder.FULL_BUILD);
		GroovyModel model = GroovyModel.getModel();
		CompileUnit unit = model.getCompilationUnit(file);
		provider = new GroovyASTContentProvider(null);
		roots = provider.getElements(unit);
		labelProvider = new GroovyASTLabelProvider();
		
	}

}

package org.codehaus.groovy.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import groovy.util.GroovyTestCase;

/**
 * Unit tests for the {@link Groovy} ant task.
 * Caution: the *.groovy files used by this test should not get compiled with the rest of the
 * test classes compilation process otherwiser they would be available in the classpath
 * and the tests here would be meaningless (tested by testClasspath_missing).
 * @author Marc Guillemot
 */
public class GroovyTest extends GroovyTestCase {
	public static String FLAG = null;
    private final File antFile = new File("src/test/org/codehaus/groovy/ant/GroovyTest.xml");
	private Project project;

	protected void setUp() throws Exception
	{
		super.setUp();
		project = new Project();
		project.init();
		ProjectHelper.getProjectHelper().parse(project, antFile);
		FLAG = null;
	}

	public void testGroovyCodeWithinTag() {
		assertNull(FLAG);
		project.executeTarget("groovyCodeWithinTask");
		assertEquals("from groovy inlined in ant", FLAG);
	}

	public void testGroovyCodeExternalFile() {
		assertNull(FLAG);
		project.executeTarget("groovyCodeInExternalFile");
		assertEquals("from groovy file called from ant", FLAG);
	}

	public void testGroovyCodeInExternalFileWithOtherClass() {
		assertNull(FLAG);
		project.executeTarget("groovyCodeInExternalFileWithOtherClass");
		assertEquals("from GroovyTest2Class.doSomething()", FLAG);
	}

	public void testClasspath_missing() {
		try
		{
			project.executeTarget("groovyClasspath_missing");
			fail();
		}
		catch (final Exception e)
		{
			assertEquals(BuildException.class, e.getClass());
		}
		
	}

	public void testClasspath_classpathAttribute() {
		assertNull(FLAG);
		project.executeTarget("groovyClasspath_classpathAttribute");
		assertEquals("from groovytest3.GroovyTest3Class.doSomething()", FLAG);
	}

	public void testClasspath_classpathrefAttribute() {
		assertNull(FLAG);
		project.executeTarget("groovyClasspath_classpathrefAttribute");
		assertEquals("from groovytest3.GroovyTest3Class.doSomething()", FLAG);
	}

	public void testClasspath_nestedclasspath() {
		assertNull(FLAG);
		project.executeTarget("groovyClasspath_nestedClasspath");
		assertEquals("from groovytest3.GroovyTest3Class.doSomething()", FLAG);
	}
}

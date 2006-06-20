package org.codehaus.groovy.eclipse.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import junit.framework.AssertionFailedError;

import org.apache.commons.io.IOUtil;
import org.codehaus.groovy.eclipse.model.ChangeSet;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.codehaus.groovy.eclipse.model.GroovyProject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author MelamedZ
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class GroovyModelTestCase extends EclipseTestCase {

	protected void setUp() 
    throws Exception
    {
        super.setUp();
    }
    protected void tearDown() 
    throws Exception
    {
        super.tearDown();
    }
    public void testFindsAllClassesWithMain() 
    throws Exception 
    {
		createAndBuildGroovyClassWithMain();
		String[] classes = GroovyModel.getModel().findAllRunnableClasses(testProject.getJavaProject());
		for (int i = 0; i < classes.length; i++) {
			System.out.println(classes[i]);
		}
		
		assertEquals("expecting a single class with main", 1, classes.length);
		assertEquals("called MainClass", "MainClass", classes[0]);
	}
	public void testModelBuildGroovy() 
    throws Exception 
    {
		createAndBuildGroovyClassWithMain();
		IProject project = testProject.getProject();
		IPath outputLocation = testProject.getJavaProject().getOutputLocation();
		String outputPath =
			project.getLocation().toString() + Path.SEPARATOR + outputLocation.removeFirstSegments(1).toString();
		assertTrue(
			"expecting the compiled class file MainClass.class to be present",
			new File(outputPath + Path.SEPARATOR + "MainClass.class").exists());
	}
	/**
     *  Testing that .class files are cleaned up if a script is removed. 
     * @throws Exception
	 */
    public void testModelCleanGroovy() 
    throws Exception
    {
        testModelBuildGroovy();
        final IResource script = deleteScript();
        assertFalse( new File( script.getLocation().toString() ).exists() );
        fullProjectBuild();
        final String outputPath = outputLocation();
        assertFalse( "expecting the compiled class file MainClass.class to be gone: " + outputPath,
                     new File( outputPath + Path.SEPARATOR + "MainClass.class" ).exists() );
    }
    /**
     * This test tries to assure us that if you rename a class within a script,
     * the old .class file is removed.
     * @throws Exception
     */
    public void testModelRenameGroovy()
    throws Exception
    {
        testModelBuildGroovy();
        deleteScript();
        testProject.createGroovyTypeAndPackage( "pack1",
                                                "MainClass.groovy",
                                                "class MainClass1 { static void main(args){}}");
        fullProjectBuild();
        final String outputPath = outputLocation();
        assertFalse( "expecting the compiled class file MainClass.class to be gone: " + outputPath,
                     new File( outputPath + Path.SEPARATOR + "MainClass.class" ).exists() );
        assertTrue( "expecting the compiled class file MainClass1.class to be there: " + outputPath,
                    new File( outputPath + Path.SEPARATOR + "MainClass1.class" ).exists() );
    }

    private String outputLocation() 
    throws JavaModelException
    {
        final String outputPath = testProject.getProject().getLocation().toString() 
                                + Path.SEPARATOR 
                                + testProject.getJavaProject().getOutputLocation().removeFirstSegments( 1 ).toString();
        return outputPath;
    }

    private IResource deleteScript() 
    throws CoreException
    {
        final IResource script = testProject.getProject().findMember( "src/pack1/MainClass.groovy" );
        script.delete( true, null );
        return script;
    }
    private void fullProjectBuild()
    throws Exception
    {
        final ChangeSet changeSet = model.getProject( testProject.getProject() ).filesForFullBuild();
        model.buildGroovyContent( testProject.getJavaProject(),
                                  new NullProgressMonitor(),
                                  IncrementalProjectBuilder.FULL_BUILD, 
                                  changeSet, 
                                  true );
        Thread.sleep( 2000 );
    }
	private void createAndBuildGroovyClassWithMain() 
    throws Exception 
    {
		disableAutoGroovySupport();
		testProject.createGroovyTypeAndPackage(
			"pack1",
			"MainClass.groovy",
			"class MainClass { static void main(args){}}");
        final IResource script = testProject.getProject().findMember( "src/pack1/MainClass.groovy" );
        assertNotNull( script );
        assertTrue( script.exists() );
		plugin.addGroovyRuntime(testProject.getProject());
        GroovyProject.addGroovyNature( testProject.getProject() );
		fullProjectBuild();
	}

	public void testRunGroovyMainWithArgs() 
    throws Exception 
    {
		final String tempFileName = getTempFileName();
		disableAutoGroovySupport();
		IFile groovyFile =
			testProject.createGroovyTypeAndPackage(
				"pack1",
				"MainClass.groovy",
				getClass().getResource("groovyfiles/write-args-to-file.groovy").openStream());

		plugin.addGroovyRuntime( testProject.getProject() );
        GroovyProject.addGroovyNature( testProject.getProject() );
		fullProjectBuild();
		String[] args = new String[] { tempFileName, "zohar", "james", "jon" };
		model.runGroovyMain(groovyFile, args);
		String expectedText = tempFileName + "zoharjamesjonthe end";
		assertFileContentsEquals(tempFileName, expectedText);
	}

	public void testRunGroovyMainWithArgsBySpecifyingProjectAndClassName() 
    throws Exception
    {
		disableAutoGroovySupport();
		testProject.createGroovyTypeAndPackage(
				"pack1",
				"MainClass.groovy",
				getClass().getResource("groovyfiles/write-args-to-file.groovy").openStream() );
		plugin.addGroovyRuntime(testProject.getProject());
        GroovyProject.addGroovyNature( testProject.getProject() );
		fullProjectBuild();
		final String tempFileName = getTempFileName();
		String[] args = new String[] { tempFileName, "zohar", "james", "jon" };
		model.runGroovyMain("TestProject", "pack1.MainClass", args);
		String expectedText = tempFileName + "zoharjamesjonthe end";
		assertFileContentsEquals(tempFileName, expectedText);
	}
	
	private void assertFileContentsEquals( final String tempFileName, 
                                           final String expectedText ) 
    throws Exception 
    {
        AssertionFailedError error = null;
        for( int i = 0; i < 10; i++ )
        {
            try
            {
                checkContentsEqual( tempFileName, expectedText );
                return;
            }
            catch( final AssertionFailedError afe )
            {
                error = afe;
            }
        }
        if( error != null )
            throw error;
        fail();
	}
    private void checkContentsEqual( final String tempFileName, 
                                     final String expectedText ) 
    throws Exception
    {
        try {
			Thread.sleep(2000); 
			// we have a timing issue with file creation by
			// 'other' vm
			//TODO find a way to sunc with the groovy running vm
			String text = IOUtil.toString(new FileReader(tempFileName));
			System.out.println("["+expectedText+"]");
			System.out.println("[" + text + "]");
			String s1 = new String(expectedText);
			String s2 = new String(text);
			System.out.println(s1.equals(s2));
			System.out.println(expectedText.equals(text));
			//assertEquals(expectedText, text);
			assertTrue(expectedText.equals(text));
		} catch (FileNotFoundException x) { // The file may not exist.
			fail("file not found, expecting text file:" + tempFileName + " to be created by the groovy class ");
		}
    }

	private String getTempFileName() {
		String tempDir = System.getProperty("java.io.tmpdir");
		final String tempFileName = tempDir + "run_groovy_test.groove";
		System.out.println(tempFileName);
		File tempFile = new File(tempFileName);
		if (tempFile.exists()) {
			tempFile.delete();
		}
		return tempFileName;
	}

	
}

/*
 * Created on 15-Jan-2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.codehaus.groovy.eclipse.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtil;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

/**
 * @author MelamedZ
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class GroovyModelTestCase extends EclipseTestCase {

	public void testFindsAllClassesWithMain() throws CoreException {
		createAndBuildGroovyClassWithMain();
		String[] classes = GroovyModel.getModel().findAllRunnableClasses(testProject.getJavaProject());
		for (int i = 0; i < classes.length; i++) {
			System.out.println(classes[i]);
		}
		
		assertEquals("expecting a single class with main", 1, classes.length);
		assertEquals("called MainClass", "MainClass", classes[0]);
	}

	public void testModelBuildGroovy() throws CoreException {
		createAndBuildGroovyClassWithMain();
		IProject project = testProject.getProject();
		IPath outputLocation = testProject.getJavaProject().getOutputLocation();
		String outputPath =
			project.getLocation().toString() + Path.SEPARATOR + outputLocation.removeFirstSegments(1).toString();
		assertTrue(
			"expecting the compiled class file MainClass.class to be present",
			new File(outputPath + Path.SEPARATOR + "MainClass.class").exists());
	}

	private void createAndBuildGroovyClassWithMain() throws CoreException {
		disableAutoGroovySupport();
		testProject.createGroovyTypeAndPackage(
			"pack1",
			"MainClass.groovy",
			"class MainClass { static void main(args){}}");
		plugin.addGroovyRuntime(testProject.getProject());
		model.buildGroovyContent(testProject.getJavaProject(), new NullProgressMonitor(),
				IncrementalProjectBuilder.FULL_BUILD);
	}

	public void testRunGroovyMainWithArgs() throws CoreException, IOException, InterruptedException {
		final String tempFileName = getTempFileName();
		disableAutoGroovySupport();
		IFile groovyFile =
			testProject.createGroovyTypeAndPackage(
				"pack1",
				"MainClass.groovy",
				getClass().getResource("groovyfiles/write-args-to-file.groovy").openStream());

		plugin.addGroovyRuntime(testProject.getProject());
		model.buildGroovyContent(testProject.getJavaProject(), new NullProgressMonitor(),
				IncrementalProjectBuilder.FULL_BUILD);
		String[] args = new String[] { tempFileName, "zohar", "james", "jon" };
		model.runGroovyMain(groovyFile, args);
		String expectedText = tempFileName + "zoharjamesjonthe end";
		assertFileContentsEquals(tempFileName, expectedText);
	}

	public void testRunGroovyMainWithArgsBySpecifyingProjectAndClassName() throws CoreException, IOException , InterruptedException{
		disableAutoGroovySupport();
		testProject.createGroovyTypeAndPackage(
				"pack1",
				"MainClass.groovy",
				getClass().getResource("groovyfiles/write-args-to-file.groovy").openStream());
		
		plugin.addGroovyRuntime(testProject.getProject());
		model.buildGroovyContent(testProject.getJavaProject(), new NullProgressMonitor(),
				IncrementalProjectBuilder.FULL_BUILD);
		final String tempFileName = getTempFileName();
		String[] args = new String[] { tempFileName, "zohar", "james", "jon" };
		model.runGroovyMain("TestProject", "pack1.MainClass", args);
		String expectedText = tempFileName + "zoharjamesjonthe end";
		assertFileContentsEquals(tempFileName, expectedText);
	}
	
	private void assertFileContentsEquals(final String tempFileName, String expectedText) throws InterruptedException, IOException {
		try {
			Thread.sleep(1000); 
			// we have a timing issue with file creation by
			// 'other' vm
			//TODO find a way to sunc with the groovy running vm
			String text = IOUtil.toString(new FileReader(tempFileName));
			
			assertEquals(expectedText, text);
		} catch (FileNotFoundException x) { // The file may not exist.
			fail("expecting text file:" + tempFileName + " to be created by the groovy class ");
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


/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Common Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: 
 * Erich Gamma (erich_gamma@ch.ibm.com) 
 * Kent Beck   (kent@threeriversinstitute.org) 
 * Zohar Mealmed
 ******************************************************************************/
package org.codehaus.groovy.eclipse.test;

import org.codehaus.groovy.eclipse.model.ChangeSet;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

public class GroovyMarkerTestCase extends EclipseTestCase {


	protected void setUp() throws Exception {
		super.setUp();
		testProject.createGroovyTypeAndPackage(
			"pack1",
			"MainClass.groovy",
			"class MainClass { static void main(args){}");
		//wont compile - missing }

	}

	public void testErrorMarker() throws Exception {
		ChangeSet changeSet = model.getProject(testProject.getProject()).filesForFullBuild(); 
		model.buildGroovyContent(testProject.getJavaProject(), new NullProgressMonitor(),
								IncrementalProjectBuilder.FULL_BUILD, changeSet, true);
		IMarker[] markers = getFailureMarkers();
		assertEquals(1,markers.length);
		IMarker marker = markers[0];
		System.out.println(marker);
		System.out.println(marker.getClass());
		// it's just an IMarker 
		//assertTrue(marker.isSubtypeOf(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER));
		assertEquals(IMarker.SEVERITY_ERROR, marker.getAttribute(IMarker.SEVERITY, -1));
	}

	public void testMarkerClearing() throws Exception {
		ChangeSet changeSet = model.getProject(testProject.getProject()).filesForFullBuild(); 
		model.buildGroovyContent(testProject.getJavaProject(), new NullProgressMonitor(),
				IncrementalProjectBuilder.FULL_BUILD, changeSet, true);
		model.buildGroovyContent(testProject.getJavaProject(), new NullProgressMonitor(),
				IncrementalProjectBuilder.FULL_BUILD, changeSet, false);
		IMarker[] markers = getFailureMarkers();
		assertEquals(1, markers.length);
	}

	private IMarker[] getFailureMarkers() throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return root.findMarkers("org.codehaus.groovy.eclipse.groovyFailure", false, IResource.DEPTH_INFINITE);
	}

}

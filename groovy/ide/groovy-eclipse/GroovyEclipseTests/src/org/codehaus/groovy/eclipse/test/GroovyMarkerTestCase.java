
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

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaModelMarker;

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
		model.buildGroovyContent(testProject.getJavaProject());
		IMarker[] markers = getFailureMarkers();
		assertEquals(1,markers.length);
		IMarker marker = markers[0];
		assertTrue(marker.isSubtypeOf(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER));
		assertEquals(IMarker.SEVERITY_ERROR, marker.getAttribute(IMarker.SEVERITY, -1));
	}

	public void testMarkerClearing() throws Exception {
		model.buildGroovyContent(testProject.getJavaProject());
		model.buildGroovyContent(testProject.getJavaProject());
		IMarker[] markers = getFailureMarkers();
		assertEquals(1, markers.length);
	}

	private IMarker[] getFailureMarkers() throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return root.findMarkers("org.codehaus.groovy.eclipse.groovyFailure", false, IResource.DEPTH_INFINITE);
	}

}

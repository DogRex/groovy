package org.codehaus.groovy.eclipse.builder;

import java.util.Map;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.model.ChangeSet;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.codehaus.groovy.eclipse.model.GroovyProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * @see IncrementalProjectBuilder
 */
public class GroovyBuilder extends IncrementalProjectBuilder {
	private IJavaProject javaProject;

	public static final String GROOVY_BUILDER = "org.codehaus.groovy.eclipse.groovyBuilder"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#startupOnInitialize()
	 */
	protected void startupOnInitialize() {
		super.startupOnInitialize();
		javaProject = JavaCore.create(getProject());
		GroovyPlugin.trace("GroovyBuilder.startupOnInitialize got a Java project : " + javaProject.getProject().getName());
		/*
		IProgressMonitor monitor = new NullProgressMonitor();
		boolean generateClassFiles = false;
		GroovyModel.getModel().buildGroovyContent(javaProject, monitor, IncrementalProjectBuilder.FULL_BUILD, fullBuild(), generateClassFiles);
		*/
	}
	/**
	 * @see IncrementalProjectBuilder#build
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) {
		GroovyPlugin.trace("GroovyBuilder.build() - args: " + args);
		GroovyProject groovyProject = GroovyModel.getModel().getProject(javaProject.getProject());
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		GroovyPlugin.trace("GroovyBuilder.build project: " + javaProject.getProject().getName() + 
				"  kind:" + decodeBuildKind(kind));
		ChangeSet changeSet = null;
		if (kind == IncrementalProjectBuilder.FULL_BUILD /* cause endless loop ||
			kind == IncrementalProjectBuilder.AUTO_BUILD */ ) {
			changeSet = groovyProject.filesForFullBuild();
		} else {
			IResourceDelta delta = getDelta(getProject());
			changeSet = groovyProject.filesForIncrementalBuild(delta, monitor);
		}
		GroovyModel.getModel().buildGroovyContent(javaProject, monitor, kind, changeSet);
		return null;
	}
	private String decodeBuildKind(int kind){
		String kindDesc = "";
		switch (kind){
			case(IncrementalProjectBuilder.AUTO_BUILD):
				kindDesc = "AUTO_BUILD";
				break;
			case IncrementalProjectBuilder.CLEAN_BUILD:
				kindDesc = "CLEAN_BUILD";
				break;
			case IncrementalProjectBuilder.FULL_BUILD:
				kindDesc = "FULL_BUILD";
				break;
			case IncrementalProjectBuilder.INCREMENTAL_BUILD:
				kindDesc = "INCREMENTAL_BUILD";
				break;
		}
		return kindDesc;
	}
}

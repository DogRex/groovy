/*
 * Created on Dec 18, 2003
 *  
 */
package org.codehaus.groovy.eclipse.model;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
/**
 * @author zohar melamed
 *  
 */
public class GroovyModel {
	private static GroovyModel dasModel;
	private Map projects = new HashMap();
	/**
	 * @param dasModel
	 */
	public static GroovyModel getModel() {
		if (dasModel == null) {
			dasModel = new GroovyModel();
		}
		return dasModel;
	}
	/**
	 * @param javaProject
	 */
	public String[] findAllRunnableClasses(IJavaProject javaProject) {
		GroovyProject gp = getGroovyProject(javaProject);
		return gp.findAllRunnableClasses();
	}
	public void runGroovyMain(String projectName, String className, String[] args) throws CoreException {
		for (Iterator iter = projects.values().iterator(); iter.hasNext();) {
			GroovyProject groovyProject = (GroovyProject) iter.next();
			if (groovyProject.getJavaProject().getProject().getName().equals(projectName)) {
				groovyProject.runGroovyMain(className, args);
				return;
			}
		}
		// we only get here if we fail to match a project to the name
		GroovyPlugin.getPlugin().logException(
				"can not find project :" + projectName + " while trying to run :" + className, new Exception());
	}
	public void runGroovyMain(IFile file) {
		try {
			runGroovyMain(file, new String[]{});
		} catch (CoreException e) {
			GroovyPlugin.getPlugin().logException("error trying to run groovy file " + file.getName(), e);
		}
	}
	public void runGroovyMain(IFile file, String[] args) throws CoreException {
		GroovyProject gp = getGroovyProject(JavaCore.create(file.getProject()));
		gp.runGroovyMain(file, args);
	}
	/**
	 * @param javaProject
	 */
	public void buildGroovyContent(IJavaProject javaProject, IProgressMonitor monitor, int kind) {
		GroovyProject gp = getGroovyProject(javaProject);
		gp.buildGroovyContent(monitor, kind);
	}
	/**
	 * @param javaProject
	 * @return
	 */
	private GroovyProject getGroovyProject(IJavaProject javaProject) {
		GroovyProject gp = (GroovyProject) projects.get(javaProject);
		if (gp == null) {
			gp = new GroovyProject(javaProject);
			projects.put(javaProject, gp);
		}
		return gp;
	}
	public void addBuildListener(GroovyBuildListner listener) {
		for (Iterator iter = projects.values().iterator(); iter.hasNext();) {
			GroovyProject groovyProject = (GroovyProject) iter.next();
			groovyProject.addBuildListener(listener);
		}
	}
	public void removeBuildListener(GroovyBuildListner listener) {
		for (Iterator iter = projects.values().iterator(); iter.hasNext();) {
			GroovyProject groovyProject = (GroovyProject) iter.next();
			groovyProject.removeBuildListener(listener);
		}
	}
	/**
	 * @param file
	 * @return
	 */
	public CompileUnit getCompilationUnit(IFile file) {
		GroovyProject project = getGroovyProject(JavaCore.create(file.getProject()));
		return project.getCompilationUnit(file);
	}
	/**
	 *  
	 */
public void updateProjects() throws CoreException {
		IProject[] allProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (int i = 0; i < allProjects .length; i++) {
			IProject project = allProjects[i];
			if(project.isAccessible()) {
				if(project.hasNature(GroovyPlugin.GROOVY_NATURE)){
					getGroovyProject(JavaCore.create(project));
				}
			}
		}
	}}

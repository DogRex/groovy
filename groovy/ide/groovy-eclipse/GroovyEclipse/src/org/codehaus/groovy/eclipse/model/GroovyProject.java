/*
 * Created on 21-Jan-2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.codehaus.groovy.eclipse.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.launchers.GroovyRunner;
import org.codehaus.groovy.eclipse.tools.EclipseFileSystemCompiler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Display;
/**
 * @author MelamedZ
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class GroovyProject{
	private IJavaProject javaProject;
	private Map compilationUnits = new HashMap();
	private EclipseFileSystemCompiler compiler = new EclipseFileSystemCompiler();
	public static final String GROOVY_ERROR_MARKER = "org.codehaus.groovy.eclipse.groovyFailure";
	List listeners = new ArrayList();
	List filesToBuild = new ArrayList();
	/**
	 * @param javaProject
	 */
	public GroovyProject(IJavaProject javaProject) {
		super();
		this.javaProject = javaProject;
	}
	public void buildGroovyContent(IProgressMonitor monitor, int kind) {
		try {
			
			long start = System.currentTimeMillis();
			setClassPath(javaProject);
			setOutputDirectory(javaProject);
			
			filesToBuild.clear();
			if(kind == IncrementalProjectBuilder.FULL_BUILD)
				javaProject.getProject().accept(
						new FullGroovyBuilder(filesToBuild));
			else{
				javaProject.getProject().accept(
						new IncrementalGroovyBuilder(javaProject, compilationUnits, this,filesToBuild));
				
			}
			monitor.beginTask("Compiling Groovy Files",filesToBuild.size());
			for (Iterator iter = filesToBuild.iterator(); iter.hasNext();) {
				IFile file = (IFile) iter.next();
				monitor.setTaskName("Compiling "+file.getName());
				GroovyPlugin.trace("Compiling "+file.getName());
				compileGroovyFile(file,true);
				monitor.worked(1);
			}
			System.out.println("compile of " + compilationUnits.size() + " took "
					+ (System.currentTimeMillis() - start));
		} catch (Exception e) {
			monitor.worked(1);
			GroovyPlugin.getPlugin().logException("error building groovy files", e);
		}
	}
	
	private void setOutputDirectory(IJavaProject javaProject) throws JavaModelException {
		String outputPath = getOutputPath(javaProject);
		GroovyPlugin.trace("groovy output = " + outputPath);
		compiler.setOutputDir(outputPath);
	}
	private String getOutputPath(IJavaProject jProject) throws JavaModelException {
		String outputPath = jProject.getProject().getLocation().toString() + IPath.SEPARATOR
				+ javaProject.getOutputLocation().removeFirstSegments(1).toString();
		return outputPath;
	}
	private void setClassPath(IJavaProject javaProject) throws JavaModelException, Exception {
		IWorkspaceRoot root = javaProject.getProject().getWorkspace().getRoot(); 
		IClasspathEntry[] cpEntries = javaProject.getResolvedClasspath(false);
		StringBuffer classPath = new StringBuffer();
		for (int i = 0; i < cpEntries.length; i++) {
			IClasspathEntry entry = cpEntries[i];
			if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
				if(entry.getPath().getDevice()== null) {
				 	IResource resource = root.findMember(entry.getPath());
				 	if(resource != null)
				 		classPath.append(resource.getLocation().toString() + ";");
				 	} else {
				 		classPath.append(entry.getPath().toString() + ";");
				 	} 	
			}
		}
		classPath.append(getOutputPath(javaProject) + ";");
		GroovyPlugin.trace("groovy cp = " + classPath.toString());
		compiler.setClasspath(classPath.toString());
	}
	/**
	 * build and save compilationUnit
	 * 
	 * @param file
	 * @param fullPath
	 * @throws CoreException
	 * @throws Exception
	 */
	void compileGroovyFile(IFile file, boolean generateClassFiles) {
		try {
			file.deleteMarkers(GROOVY_ERROR_MARKER, false, IResource.DEPTH_INFINITE); //$NON-NLS-1$
			GroovyPlugin.trace("deleted markers from " + file.getFullPath());
			GroovyPlugin.trace(generateClassFiles ? " " : "fast " + "compiling -" + file.getFullPath());
			CompileUnit unit = compiler.compile(file.getLocation().toFile(), generateClassFiles);
			String key = file.getFullPath().toString();
			compilationUnits.put(key, unit);
			fireGroovyFileBuilt(file, unit);
		} catch (Exception e) {
			handleCompilationError(file, e);
		}
	}
	/**
	 * @param e
	 */
	private void handleCompilationError(IResource resource, Exception e) {
		GroovyPlugin.trace("compilation error : " + e.getMessage());
		try {
			resource.getWorkspace().run(new AddErrorMarker(resource, e), null);
		} catch (CoreException ce) {
			GroovyPlugin.getPlugin().logException("error compiling " + resource.getName(), ce);
		}
	}
	/**
	 * @return Returns the compiler.
	 */
	public EclipseFileSystemCompiler getCompiler() {
		return compiler;
	}
	/**
	 * @param compiler
	 *            The compiler to set.
	 */
	public void setCompiler(EclipseFileSystemCompiler compiler) {
		this.compiler = compiler;
	}
	public void runGroovyMain(IFile file, String[] args) throws CoreException {
		CompileUnit compileUnit = (CompileUnit) compilationUnits.get(file.getFullPath().toString());
		if (compileUnit == null) {
			GroovyPlugin.trace("failed to run " + file.getFullPath().toString() + "due to missing compilation unit ");
			GroovyPlugin.getPlugin().getDialogProvider().errorRunningGroovyFile(file,
					new Exception("failed to find a compilation unit, try rebuilding"));
			return;
		}
		runGroovyMain(compileUnit, args);
	}
	/*
	 *  
	 */
	private void runGroovyMain(CompileUnit compileUnit, String[] args) throws CoreException {
		GroovyRunner runner = new GroovyRunner();
		ClassNode classNode = (ClassNode) compileUnit.getClasses().get(0);
		String elementName = "";
		if (isTestCaseClass(classNode)) {
			elementName = "junit.textui.TestRunner";
			args = new String[]{classNode.getName()};
		} else {
			elementName = classNode.getName();
		}
		runner.run(elementName, args, javaProject);
	}
	/**
	 * @param classNode
	 * @return
	 */
	private boolean isTestCaseClass(ClassNode classNode) {
		ClassNode parent = classNode.getSuperClassNode();
		while (parent != null) {
			if (parent.getNameWithoutPackage().equals("TestCase")) {
				return true;
			}
			parent = parent.getSuperClassNode();
		}
		return false;
	}
	/**
	 * @param className
	 * @param args
	 */
	public void runGroovyMain(String className, String[] args) throws CoreException {
		for (Iterator iter = compilationUnits.values().iterator(); iter.hasNext();) {
			CompileUnit unit = (CompileUnit) iter.next();
			List classes = unit.getClasses();
			for (Iterator iterator = classes.iterator(); iterator.hasNext();) {
				ClassNode classNode = (ClassNode) iterator.next();
				if (classNode.getName().equals(className)) {
					runGroovyMain(unit, args);
					return;
				}
			}
		}
		GroovyPlugin.trace("failed to run " + className + "due to missing compilation unit ");
	}
	public String[] findAllRunnableClasses() {
		List results = new ArrayList();
		for (Iterator iter = compilationUnits.values().iterator(); iter.hasNext();) {
			CompileUnit unit = (CompileUnit) iter.next();
			List classes = unit.getClasses();
			for (Iterator iterator = classes.iterator(); iterator.hasNext();) {
				ClassNode classNode = (ClassNode) iterator.next();
				List mainMethods = classNode.getDeclaredMethods("main");
				for (Iterator methoodIterator = mainMethods.iterator(); methoodIterator.hasNext();) {
					MethodNode methodNode = (MethodNode) methoodIterator.next();
					if (methodNode != null && methodNode.isStatic() && methodNode.isVoidMethod()) {
						results.add(classNode.getName());
					}
				}
				if (isTestCaseClass(classNode)) {
					results.add(classNode.getName());
				}
			}
		}
		return (String[]) results.toArray(new String[results.size()]);
	}
	/**
	 * @param listener
	 */
	public void addBuildListener(GroovyBuildListner listener) {
		listeners.add(listener);
	}
	/**
	 * @param listener
	 */
	public void removeBuildListener(GroovyBuildListner listener) {
		listeners.remove(listener);
	}
	class FireFileBuiltAction implements Runnable {
		private IFile file;
		CompileUnit unit;
		public void run() {
			for (Iterator iter = listeners.iterator(); iter.hasNext();) {
				GroovyBuildListner buildListner = (GroovyBuildListner) iter.next();
				buildListner.fileBuilt(file, unit);
			}
		}
		public FireFileBuiltAction(IFile file, CompileUnit unit) {
			this.file = file;
			this.unit = unit;
		}
	}
	private void fireGroovyFileBuilt(IFile file, CompileUnit unit) {
		Display.getDefault().asyncExec(new FireFileBuiltAction(file, unit));
	}
	/**
	 * @return Returns the javaProject.
	 */
	public IJavaProject getJavaProject() {
		return javaProject;
	}
	/**
	 * @param file
	 * @return
	 */
	public CompileUnit getCompilationUnit(IFile file) {
		return (CompileUnit) compilationUnits.get(file.getFullPath().toString());
	}
}

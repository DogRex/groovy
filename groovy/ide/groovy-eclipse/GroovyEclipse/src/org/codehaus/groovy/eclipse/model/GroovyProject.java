/*
 * Created on 21-Jan-2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.codehaus.groovy.eclipse.model;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.Phases;
import org.codehaus.groovy.control.ProcessingUnit;
import org.codehaus.groovy.control.CompilationUnit.ProgressCallback;
import org.codehaus.groovy.control.messages.WarningMessage;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.builder.GroovyNature;
import org.codehaus.groovy.eclipse.launchers.GroovyRunner;
import org.codehaus.groovy.eclipse.preferences.PreferenceConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Display;

/**
 * The main groovy project class used to configure the project settings,
 * and do the compiling.
 * 
 * @author MelamedZ
 * @author Hein Meling
 */
public class GroovyProject {

	private IJavaProject javaProject;
	private Map compilationUnits = new HashMap();
	private CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
	public static final String GROOVY_ERROR_MARKER = "org.codehaus.groovy.eclipse.groovyFailure";
	private List listeners = new ArrayList();
	private List filesToBuild = new ArrayList();

	// True if we are waiting for the user to respond to question to add groovy support to project
	private volatile boolean pendingResponse = false;


	class AddGroovySupport implements Runnable {
		final IProject project;

		public AddGroovySupport(IProject project) {
			trace("AddGroovySupport.AddGroovySupport()");
			this.project = project;
		}

		public void run() {
			GroovyPlugin plugin = GroovyPlugin.getPlugin();
			try {
				trace("AddGroovySupport.run()");
				if (plugin.getDialogProvider().doesUserWantGroovySupport()) {
					addGrovyExclusionFilter(project);
					plugin.addGroovyRuntime(project);
					addGroovyNature(project);					
				} else {
					Preferences preferences = plugin.getPluginPreferences();
					preferences.setValue(project.getName() + "NoSupport", true);
					plugin.savePluginPreferences();
				}
			} catch (CoreException e) {
				plugin.logException("failed to add groovy support", e);
			} finally {
				synchronized (GroovyProject.this) {
					pendingResponse = false;
				}
				plugin.listenToChanges();
			}
		}

	}


	/**
	 * Construct new groovy project.
	 * 
	 * @param javaProject
	 */
	public GroovyProject(IJavaProject javaProject) {
		this.javaProject = javaProject;
		// Note that enabling debug will disable class generation
		//compilerConfiguration.setDebug(true);
		try {
			setClassPath(javaProject);
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
	 * 
	 * @param monitor
	 * @param kind
	 */
	public void buildGroovyContent(IProgressMonitor monitor, int kind) {
		try {
			long start = System.currentTimeMillis();
			setClassPath(javaProject);
			setOutputDirectory(javaProject);
			
			filesToBuild.clear();
			if (kind == IncrementalProjectBuilder.FULL_BUILD) {
				trace("FULL BUILD");
				javaProject.getProject().accept(
						new FullGroovyBuilder(filesToBuild));
			} else {
				trace("INCREMENTAL BUILD");
				javaProject.getProject().accept(
						new IncrementalGroovyBuilder(javaProject, compilationUnits, this, filesToBuild));
			}
			monitor.beginTask("Compiling Groovy Files", filesToBuild.size());
			compileGroovyFiles(monitor);
			for (Iterator iter = filesToBuild.iterator(); iter.hasNext();) {
				IFile file = (IFile) iter.next();
				monitor.setTaskName("Compiling "+file.getName());
				GroovyPlugin.trace("Compiling "+file.getName());
				Preferences prefs = GroovyPlugin.getDefault().getPluginPreferences();
				compileGroovyFile(file, 
					prefs.getBoolean(PreferenceConstants.GROOVY_GENERATE_CLASS_FILES));
				monitor.worked(1);
			}
			System.out.println("compile of " + filesToBuild.size() + " took "
					+ (System.currentTimeMillis() - start));
		} catch (Exception e) {
			monitor.worked(1);
			GroovyPlugin.getPlugin().logException("error building groovy files", e);
		}
	}
	
	/**
	 * @param monitor
	 */
	private void compileGroovyFiles(IProgressMonitor monitor) {
		CompilationUnit compilationUnit = createCompilationUnit("");
		Thread.currentThread().setContextClassLoader(compilationUnit.getClassLoader());
		compilationUnit.setProgressCallback(new ProgressCallback() {
			public void call(ProcessingUnit context, int phase) throws CompilationFailedException {
				System.out.println("GroovyProject.compileGroovyFiles() "+ context
						+ " phase " + Phases.getDescription(phase));
				if (phase == Phases.OUTPUT) {
					//CompilationUnit unit = (CompilationUnit) context;
					//String key = unit.
				}
			}
		});
//		IFile file = null;
//		for (Iterator iter = filesToBuild.iterator(); iter.hasNext();) {
//			file = (IFile) iter.next();
//			try {
//				file.deleteMarkers(GROOVY_ERROR_MARKER, false, IResource.DEPTH_INFINITE); //$NON-NLS-1$
//			} catch (CoreException e1) {
//				e1.printStackTrace();
//			}
//			GroovyPlugin.trace("deleted markers from " + file.getFullPath());
//			trace("Adding source: " + file.getLocation().toFile());
//			compilationUnit.addSource(file.getLocation().toFile());
//		}
//		try {
//			compilationUnit.compile();
//		} catch (Exception e) {
//			handleCompilationError(file, e);
//		}
	}

	private void setOutputDirectory(IJavaProject javaProject) throws JavaModelException {
		String outputPath = getOutputPath(javaProject);
		GroovyPlugin.trace("groovy output = " + outputPath);
		compilerConfiguration.setTargetDirectory(outputPath);
	}

	private String getOutputPath(IJavaProject jProject) throws JavaModelException {
		String outputPath = jProject.getProject().getLocation().toString() + IPath.SEPARATOR
				+ javaProject.getOutputLocation().removeFirstSegments(1).toString();
		return outputPath;
	}
	
	private void setClassPath(IJavaProject javaProject) throws JavaModelException {
		IWorkspaceRoot root = javaProject.getProject().getWorkspace().getRoot();
		IClasspathEntry[] cpEntries = javaProject.getResolvedClasspath(false);
		StringBuffer classPath = new StringBuffer();
		for (int i = 0; i < cpEntries.length; i++) {
			IClasspathEntry entry = cpEntries[i];
			if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
				//if (entry.getPath().getDevice() == null) {
				 	IResource resource = root.findMember(entry.getPath());
				 	if (resource != null) {
				 		classPath.append(resource.getLocation().toString());
				 	} else {
				 		classPath.append(entry.getPath().toString());
				 	}
				 	classPath.append(File.pathSeparator);
				//}
			}
		}
		classPath.append(getOutputPath(javaProject));
		GroovyPlugin.trace("groovy cp = " + classPath.toString());
		compilerConfiguration.setClasspath(classPath.toString());
	}

	/**
	 * Create and store the compilationUnit
	 * 
	 * @param file
	 * @param generateClassFiles
	 */
	public void compileGroovyFile(IFile file, boolean generateClassFiles) {
		try {
			file.deleteMarkers(GROOVY_ERROR_MARKER, false, IResource.DEPTH_INFINITE); //$NON-NLS-1$
			GroovyPlugin.trace("deleted markers from " + file.getFullPath());
			GroovyPlugin.trace((generateClassFiles ? "" : "fast ") + "compiling " + file.getFullPath());
			CompilationUnit compilationUnit = createCompilationUnit("");
			compilationUnit.addSource(file.getLocation().toFile());
			compilationUnit.compile(generateClassFiles ? Phases.ALL : Phases.CANONICALIZATION);
			CompileUnit unit = compilationUnit.getAST();
			String key = file.getFullPath().toString();
			compilationUnits.put(key, unit);
			fireGroovyFileBuilt(file, unit);
		} catch (Exception e) {
			handleCompilationError(file, e);
		}
	}

    private CompilationUnit createCompilationUnit(String characterEncoding) {
//        compilerConfiguration.setSourceEncoding(characterEncoding);
        compilerConfiguration.setOutput(new PrintWriter(System.err));
        compilerConfiguration.setWarningLevel(WarningMessage.PARANOIA);

        return new CompilationUnit(compilerConfiguration, null, buildClassLoaderFor());
    }

    private GroovyClassLoader buildClassLoaderFor() {
        URLClassLoader urlClassLoader = new URLClassLoader(convertClasspathToUrls());
        return new GroovyClassLoader(urlClassLoader, compilerConfiguration);
    }

    private URL[] convertClasspathToUrls() {
        try {
            return classpathAsUrls();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private URL[] classpathAsUrls() throws MalformedURLException {
        List classpath = compilerConfiguration.getClasspath();
        URL[] classpathUrls = new URL[classpath.size()];
        for (int i = 0; i < classpathUrls.length; i++) {
            String classpathEntry = (String) classpath.get(i);
            classpathUrls[i] = new File(classpathEntry).toURL();
         }
        return classpathUrls;
    }

    /**
	 * 
	 * @param file
	 * @param e
	 */
	private void handleCompilationError(IFile file, Exception e) {
		GroovyPlugin.trace("compilation error : " + e.getMessage());
		try {
			file.getWorkspace().run(new AddErrorMarker(file, e), null);
		} catch (CoreException ce) {
			GroovyPlugin.getPlugin().logException("error compiling " + file.getName(), ce);
		}
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

	/**
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
		ClassNode parent = classNode.getSuperClass();
		while (parent != null) {
			if (parent.getNameWithoutPackage().equals("TestCase")) {
				return true;
			}
			parent = parent.getSuperClass();
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
		private CompileUnit unit;
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
		trace(file.getName() + " has been built");
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

	/**
	 * @param resource
	 * @throws CoreException
	 */
	public synchronized void groovyFileAdded(IResource resource) throws CoreException {
		GroovyPlugin plugin = GroovyPlugin.getPlugin();
		Preferences preferences = plugin.getPluginPreferences();
		IProject project = resource.getProject();
		boolean alreadyAsked = preferences.getBoolean(project.getName()+ "NoSupport");
		if (pendingResponse || !project.exists()
				|| project.hasNature(GroovyNature.GROOVY_NATURE) || alreadyAsked)
			return;
		pendingResponse = true;
		AddGroovySupport support = new AddGroovySupport(project);
		Display.getDefault().asyncExec(support);
	}
	
	public void addGrovyExclusionFilter(IProject project) {
		// make sure .groovy files are not copied to the output
		// dir
		String excludedResources = javaProject.getOption(
				"org.eclipse.jdt.core.builder.resourceCopyExclusionFilter",
				true);
		if (excludedResources.indexOf("*.groovy") == -1) {
			excludedResources = excludedResources.length() == 0 ? "*.groovy"
					: excludedResources + ",*.groovy";
			javaProject.setOption(
					"org.eclipse.jdt.core.builder.resourceCopyExclusionFilter",
					excludedResources);
		}
	}
	
	/**
	 *  
	 */
	public void addGroovyNature(IProject project) throws CoreException {
		trace("GroovyPlugin.addGroovyNature()");
		if (project.hasNature(GroovyNature.GROOVY_NATURE))
			return;

		IProjectDescription description = project.getDescription();
		String[] ids = description.getNatureIds();
		String[] newIds = new String[ids.length + 1];
		System.arraycopy(ids, 0, newIds, 0, ids.length);
		newIds[ids.length] = GroovyNature.GROOVY_NATURE;
		description.setNatureIds(newIds);
		project.setDescription(description, null);
	}

	public void removeGroovyNature(IProject project) throws CoreException {
		trace("GroovyPlugin.removeGroovyNature()");
		IProjectDescription description = project.getDescription();
		String[] ids = description.getNatureIds();
		for (int i = 0; i < ids.length; ++i) {
			if (ids[i].equals(GroovyNature.GROOVY_NATURE)) {
				String[] newIds = new String[ids.length - 1];
				System.arraycopy(ids, 0, newIds, 0, i);
				System.arraycopy(ids, i + 1, newIds, i, ids.length - i - 1);
				description.setNatureIds(newIds);
				project.setDescription(description, null);
				return;
			}
		}
	}

	private void trace(String string) {
		GroovyPlugin.trace(string);
	}

}

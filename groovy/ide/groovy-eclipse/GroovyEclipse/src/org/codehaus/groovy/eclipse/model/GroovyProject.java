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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
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
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Display;

/**
 * The main groovy project class used to configure the project settings, and do
 * the compiling.
 * 
 * @author MelamedZ
 * @author Hein Meling
 */
public class GroovyProject {

	private IJavaProject javaProject;

	// maps class name to a list of org.codehaus.groovy.ast.ModuleNode
	private Map classNameModuleNodeMap = new HashMap();
	
	private CompilerConfiguration compilerConfiguration = new CompilerConfiguration();

	public static final String GROOVY_ERROR_MARKER = "org.codehaus.groovy.eclipse.groovyFailure";

	private List listeners = new ArrayList();
	// TODO: (by Scott Hickey - I'm not sure that it makes sense to have this as an
	// instance variables since the files to build are really passed in by the Groovy Builder
	// for full and incremental builds.
	private List filesToBuild = new ArrayList();

	// TODO: We need to keep track if a full build has occurred without errors or not.
	// If it hasn't, methods that do things like return all runnable classes with a main
	// won't work correctly unless a full build occurrs to create Module Nodes for all the 
	// source files in the project.
	// private boolean hasFullBuildHappened = false;
	
	// True if we are waiting for the user to respond to question to add groovy
	// support to project
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
		trace("constructing Groovy Project " + javaProject.getElementName());
		// Note that enabling debug will disable class generation
		// compilerConfiguration.setDebug(true);
	}

	/**
	 *  Overloaded method used by builders to compile groovy files that
	 *  defaults to the variable for generating .class files to 
	 *  what's set in the Groovy prefs page
	 *  
	 * @param monitor
	 * @param kind
	 */
	public void buildGroovyContent(IProgressMonitor monitor, int kind, List filesToBuild) {
		Preferences prefs = GroovyPlugin.getDefault().getPluginPreferences();
		boolean generateClassFiles = prefs.getBoolean(PreferenceConstants.GROOVY_GENERATE_CLASS_FILES);
		buildGroovyContent(monitor, kind, filesToBuild, generateClassFiles);
	}
	/**
	 * Used by builders to compile Groovy source 
	 * 
	 * @param monitor
	 * @param kind
	 * @param filesToBuild
	 * @param generateClassFiles
	 */
	public void buildGroovyContent(IProgressMonitor monitor, int kind, List filesToBuild, boolean generateClassFiles) {
		this.filesToBuild.clear();
		this.filesToBuild.addAll(filesToBuild);
		try {
			setClassPath(javaProject);
			setOutputDirectory(javaProject);
			compileGroovyFiles(monitor);
			if (kind == IncrementalProjectBuilder.FULL_BUILD) {
				trace("beginning FULL BUILD - GroovyProject.buildGroovyContent()");
				// get a list of all the groovy files in the project
				classNameModuleNodeMap.clear();
			}else {
				trace("beginning INCREMENTAL BUILD - GroovyProject.buildGroovyContent()");
			}
			for (Iterator it = filesToBuild.iterator(); it.hasNext();) {
				IFile f = (IFile) it.next();
				deleteErrorMarkers(f);
			}
			String[] fileNames = createFileNameArrayFromIFileList(filesToBuild);
			trace("filesToBuild:" + filesToBuild);
			compileGroovyFiles(fileNames, generateClassFiles);
			monitor.worked(1);
			// TODO: Do we really want to do this on a full build
			for (Iterator it = filesToBuild.iterator(); it.hasNext();) {
				IFile f = (IFile) it.next();
				fireGroovyFileBuilt(f);
			}
		} catch (Exception e) {
			monitor.worked(1);
			GroovyPlugin.getPlugin().logException("error building groovy files", e);
		}
	}
	
	/**
	 * Invokes the Groovy compiler and updates the map that
	 * contains the AST information for the compiled classes.
	 * 
	 * TODO: Rename this method to something that reflects what it really does
	 * 
	 * @param fileNames
	 * @param generateClassFiles
	 */
	private void compileGroovyFiles(String[] fileNames, boolean generateClassFiles) {
		CompilationUnit compilationUnit = createCompilationUnit("");
		compilationUnit.addSources(fileNames);
		try {
			// call the compiler
			compilationUnit.compile(generateClassFiles ? Phases.ALL : Phases.CANONICALIZATION);
			// update project info that maps source files to compiled class info
			CompileUnit compileUnit = compilationUnit.getAST();
			updateClassNameModuleNodeMap(compileUnit.getModules());
		} catch (Exception e) {
			handleCompilationError(this.filesToBuild, e);
		}
	}

	/**
	 * passes the eclipse monitor to the Groovy compiler progress callback
	 * 
	 * @param monitor
	 */
	private void compileGroovyFiles(IProgressMonitor monitor) {
		CompilationUnit compilationUnit = createCompilationUnit("");
		Thread.currentThread().setContextClassLoader(compilationUnit.getClassLoader());
		compilationUnit.setProgressCallback(new ProgressCallback() {
			public void call(ProcessingUnit context, int phase) throws CompilationFailedException {
				System.out.println("GroovyProject.compileGroovyFiles() " + context + " phase " + Phases.getDescription(phase));
				if (phase == Phases.OUTPUT) {
					// CompilationUnit unit = (CompilationUnit) context;
					// String key = unit.
				}
			}
		});
	}
	/**
	 * Recompile a source file without creating a .class file. This method is useful
	 * for recreating the ModuleNode from the groovy compiler if the ModuleNode has
	 * yet to be created during the current working session.
	 * @param file
	 */
	public void compileGroovyFile(IFile file){
		boolean generateClassFiles = false;
		compileGroovyFile(file, generateClassFiles);
	}
	/**
	 * Recompile a source file and specify the creation of a .class file. 
	 */
	public void compileGroovyFile(IFile file, boolean generateClassFiles){
		List fileList = new ArrayList();
		fileList.add(file);
		IProgressMonitor monitor = new NullProgressMonitor();
		GroovyModel.getModel().buildGroovyContent(javaProject, monitor, IncrementalProjectBuilder.INCREMENTAL_BUILD, fileList, generateClassFiles);
	}
	/**
	 * Recompile a list of source file and specify the creation of a .class file. 
	 */
	public void compileGroovyFiles(List listOfIFiles, boolean generateClassFiles){
		IProgressMonitor monitor = new NullProgressMonitor();
		GroovyModel.getModel().buildGroovyContent(javaProject, monitor, IncrementalProjectBuilder.INCREMENTAL_BUILD, listOfIFiles, generateClassFiles);
	}

	/**
	 * sets Eclipse project output directory to the 
	 * instance compilerConfiguration
	 * 
	 * @param javaProject
	 * @throws JavaModelException
	 */
	private void setOutputDirectory(IJavaProject javaProject) throws JavaModelException {
		String outputPath = getOutputPath(javaProject);
		GroovyPlugin.trace("groovy output = " + outputPath);
		compilerConfiguration.setTargetDirectory(outputPath);
	}
	/**
	 * Returns the Eclipse project output path
	 * @param project
	 * @return
	 * @throws JavaModelException
	 */
	private static String getOutputPath(IJavaProject project) throws JavaModelException {
		String outputPath = project.getProject().getParent().getLocation().toString() 
				+ project.getOutputLocation().toString();
		return outputPath;
	}
	/**
	 *  Sets the classpath on the project's compiler configuration 
	 *  instance variable, this.compilerConfiguration
	 * @param javaProject
	 * @throws JavaModelException
	 */
	private void setClassPath(IJavaProject javaProject) throws JavaModelException {
		StringBuffer classPath = new StringBuffer();
        final Set set = getClasspath( javaProject, new ArrayList() );
        final Iterator iterator = set.iterator();
        while( iterator.hasNext() )
        {
            classPath.append( iterator.next().toString() );
            if( iterator.hasNext() )
                classPath.append( File.pathSeparator );
        }
		GroovyPlugin.trace("groovy cp = " + classPath.toString());
		compilerConfiguration.setClasspath(classPath.toString());
	}
	private Set getClasspath( final IJavaProject project,
                              final List visited ) 
    throws JavaModelException
    {
        final Set set = new LinkedHashSet();
        if( visited.contains( project ) )
            return set;
        visited.add( project );
        final IPackageFragmentRoot[] fragRoots = project.getPackageFragmentRoots();
        for( int i = 0; i < fragRoots.length; i++ )
        {
            final IPackageFragmentRoot fragRoot = fragRoots[ i ];
            final IResource resource = fragRoot.getCorrespondingResource();
            if( resource != null )
                set.add( resource.getLocation().toString() );
            else
                set.add( fragRoot.getPath().toString() );
        }
        IWorkspaceRoot root = javaProject.getProject().getWorkspace().getRoot();
        IClasspathEntry[] cpEntries = javaProject.getResolvedClasspath(false);
        StringBuffer classPath = new StringBuffer();
        for (int i = 0; i < cpEntries.length; i++) {
            IClasspathEntry entry = cpEntries[i];
            IResource resource = root.findMember(entry.getPath());
            if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
                if (resource != null) {
                    set.add( resource.getLocation().toString() );
                } else {
                    set.add( entry.getPath().toString() );
                }
            } else if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
                IJavaProject referencedProject = JavaCore.create((IProject) resource);
                set.addAll( getClasspath( referencedProject, visited ) );
            }
        }
        String outputPath = getOutputPath( project );
        if( !outputPath.trim().equals( "" ) )
            set.add( outputPath );
        return set;
    }
	/**
	 * creates a new compiler configuration
	 * @param characterEncoding
	 * @return
	 */
	private CompilationUnit createCompilationUnit(String characterEncoding) {
		// compilerConfiguration.setSourceEncoding(characterEncoding);
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
	 * Processing the exception that is thrown when there are compiler
	 * errors and creates errors markers for the files that have errors.
	 * 
	 * @param file - list of IFiles to compile for a full build
	 * @param e
	 */
	private void handleCompilationError(List fileList, Exception e) {
		GroovyPlugin.trace("compilation error : " + e.getMessage());
		IFile file = (IFile)(fileList.get(0));
		try {
			file.getWorkspace().run(new AddErrorMarker(fileList, e), null);
		} catch (CoreException ce) {
			GroovyPlugin.getPlugin().logException("error compiling " + file.getName(), ce);
		}
	}
	
	/**
	 * Runs a Groovy file. This is invoked when right clicking on a 
	 * file in package explorer, navigator or the open file in an
	 * editor and selecting Run->Groovy
	 * 
	 * @param file
	 * @param args
	 * @throws CoreException
	 */
	public void runGroovyMain(IFile file, String[] args) throws CoreException {
		List classNodeList = getClassesForModules( getModuleNodes(file));
		if (classNodeList == null) {
			GroovyPlugin.trace("failed to run " + file.getFullPath().toString() + "due to missing compilation unit ");
			GroovyPlugin.getPlugin().getDialogProvider().errorRunningGroovyFile(file,
				new Exception("failed to find a classNode, try rebuilding"));
			return;
		}
		runGroovyMain( (ClassNode) classNodeList.get(0), args);
	}

	/**
	 * Inspects the class to run and modifies the class and arguments
	 * that are run if it is Junit test case
	 * 
	 */
	private void runGroovyMain(ClassNode classNode, String[] args) throws CoreException {
		String className = "";
		if (isTestCaseClass(classNode)) {
			className = "junit.textui.TestRunner";
			args = new String[] { classNode.getName() };
		} else {
			className = classNode.getName();
		}
		runGroovyMain(className,args);
	}

	/**
	 * Evaluates the class to determine if is an JUnit test
	 * 
	 * TODO: Subclasses of these two don't seem work with this logic.
	 * Parent is returning Object instead of the superclass.
	 * 
	 * @param classNode
	 * @return
	 */
	private boolean isTestCaseClass(ClassNode classNode) {
		ClassNode parent = classNode.getSuperClass();
		while (parent != null) {
			// TODO: classes that extend GroovyTestCase have parent object
			// instead of TestCase
			if (parent.getNameWithoutPackage().equals("TestCase") ||
				parent.getNameWithoutPackage().equals("GroovyTestCase")) {
				return true;
			}
			parent = parent.getSuperClass();
		}
		return false;
	}

	/**
	 * 
	 * Run Groovy class
	 * 
	 * @param className
	 * @param args
	 */
	public void runGroovyMain(String className, String[] args) throws CoreException {
		GroovyRunner runner = new GroovyRunner();
		runner.run(className, args, javaProject);
	}
	/**
	 * Find every class that can be run to support the drop down lists
	 * used in creating a run configuration.
	 * 
	 * TODO: This method won't return everything until a full build has been 
	 * invoked and ModuleNodes have been created for every file in the project.
	 * 
	 * @return
	 */
	public String[] findAllRunnableClasses() {
		List results = new ArrayList();
		for (Iterator iter = classNameModuleNodeMap.values().iterator(); iter.hasNext();) {
			List moduleList = (List) iter.next();
			List classes = getClassesForModules(moduleList);
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
		public void run() {
			for (Iterator iter = listeners.iterator(); iter.hasNext();) {
				GroovyBuildListner buildListner = (GroovyBuildListner) iter.next();
				buildListner.fileBuilt(file);
			}
		}

		public FireFileBuiltAction(IFile file) {
			this.file = file;
		}
	}

	private void fireGroovyFileBuilt(IFile file) {
		Display.getDefault().asyncExec(new FireFileBuiltAction(file));
	}

	/**
	 * @return Returns the javaProject.
	 */
	public IJavaProject getJavaProject() {
		return javaProject;
	}

	private List getModuleNodes (String className) {
		return (List) classNameModuleNodeMap.get(className);
	}
	public List getModuleNodes (IFile file) {
		String className = getSourceFileKey(file);
		List l = getModuleNodes(className);
		if (l == null || l.isEmpty()) {		
			// If there are error markers on the file, then it means that the file
			// has been compiled already and there is no ModuleNode info available
			// from a successful compile
			IMarker[] markers = null;
			try {
				markers = file.findMarkers(GROOVY_ERROR_MARKER, false, IResource.DEPTH_INFINITE);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (markers == null || markers.length ==  0) {
				trace("GroovyProject.getModuleNodes() - starting compile for file:"+file);
				//List files = fullBuild();
				compileGroovyFile(file);
				l = getModuleNodes(className);
			}
		}
		return l;
	}
	/** Overloaded method to return 
	 * a list of ClassNodes for a given IFile
	 * 
	 * @param file
	 * @return
	 */
	public List getClassesForFile(IFile file){
		return getClassesForModules(getModuleNodes(file));
	}
	/**
	 * Drill into a list of ModuleNodes and return
	 * a list of ClassNodes.
	 * 
	 * @param moduleList
	 * @return
	 */
	public List getClassesForModules(List moduleList){
		List l = new ArrayList();
		if (moduleList != null) {
			for(Iterator iter = moduleList.iterator(); iter.hasNext();){
				ModuleNode m = (ModuleNode)iter.next();
				l.addAll(m.getClasses());
			}
		}
		return l;
	}

	/**
	 * @param resource
	 * @throws CoreException
	 */
	public synchronized void groovyFileAdded(IResource resource) throws CoreException {
		GroovyPlugin plugin = GroovyPlugin.getPlugin();
		Preferences preferences = plugin.getPluginPreferences();
		IProject project = resource.getProject();
		boolean alreadyAsked = preferences.getBoolean(project.getName() + "NoSupport");
		if (pendingResponse || !project.exists() || project.hasNature(GroovyNature.GROOVY_NATURE) || alreadyAsked)
			return;
		pendingResponse = true;
		AddGroovySupport support = new AddGroovySupport(project);
		Display.getDefault().asyncExec(support);
	}

	public void addGrovyExclusionFilter(IProject project) {
		// make sure .groovy files are not copied to the output
		// dir
		String excludedResources = javaProject.getOption("org.eclipse.jdt.core.builder.resourceCopyExclusionFilter", true);
		if (excludedResources.indexOf("*.groovy") == -1) {
			excludedResources = excludedResources.length() == 0 ? "*.groovy" : excludedResources + ",*.groovy";
			javaProject.setOption("org.eclipse.jdt.core.builder.resourceCopyExclusionFilter", excludedResources);
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
	private void updateClassNameModuleNodeMap(List moduleNodes) {
		Map tmpMap = new HashMap();
		for(Iterator iter = moduleNodes.iterator(); iter.hasNext();){
			ModuleNode moduleNode = (ModuleNode) iter.next();
			String key = moduleNode.getDescription();
			List moduleNodeList = (List) tmpMap.get(key);
			if (moduleNodeList == null) { 
				moduleNodeList = new ArrayList();
				tmpMap.put(key,moduleNodeList);
			}
			moduleNodeList.add(moduleNode);
		}
		classNameModuleNodeMap.putAll(tmpMap);
	}
	/**
	 * create array of filenames from list of IFiles
	 * This is primarily used to take the IFiles passed in for compiling
	 * and convert them to strings that can be passed in to the 
	 * Groovy compiler. 
	 * 
	 * @param iFileList
	 * @return
	 */
	private String[] createFileNameArrayFromIFileList(List iFileList) {
		String[] fileNames = new String[iFileList.size()];
		for(int i=0; i < iFileList.size(); i++ ){
			IFile f = (IFile) iFileList.get(i);
			fileNames[i] = (f.getLocation().toPortableString());
		}
		return fileNames;
	}
	/**
	 * This returns a string for the IFile that is used to generate the keys
	 * for classNode maps and moduleNode maps.
	 * @param file
	 * @return
	 */
	public String getSourceFileKey(IFile file){
		String key = file.getRawLocation().toOSString();
		return key;
	}
	/**
	 * Remove the GROOOVY error markers from a groovy file (IFile) in the project
	 * @param file
	 */
	private void deleteErrorMarkers(IFile file){
		try {
			file.deleteMarkers(GROOVY_ERROR_MARKER, false, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //$NON-NLS-1$
	}
	private void trace(String string) {
		GroovyPlugin.trace(string);
	}
	/**
	 * Get a list of IFiles for all the groovy source files in the project.
	 * This is used by the GroovyBuilder when invoking a full build.
	 * It is also required when we want to fully populate the
	 * ModuleNode map.
	 * 
	 * @return
	 */
	public List filesForFullBuild() {
		FullGroovyBuilder visitor = new FullGroovyBuilder();
		try {
			javaProject.getProject().accept(visitor);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return visitor.getFilesToBuild();
	}
	
	/**
	 * This functionality should probably be in the GroovyBuilder
	 * since it is the only place (that I know about) that provides
	 * an IResourceDelta object.
	 * 
	 * @param delta
	 * @param monitor
	 * @return
	 */
	public List filesForIncrementalBuild(IResourceDelta delta, IProgressMonitor monitor) {
		List filesToBuild = null;
		IncrementalGroovyBuilder v = new IncrementalGroovyBuilder();
		try {
			delta.accept(v);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		if (v.doFullBuild == true) {
			filesToBuild = filesForFullBuild();
		} else {
			filesToBuild = v.getFilesToBuild();
		}
		return filesToBuild;
	}

}

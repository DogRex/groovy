package org.codehaus.groovy.eclipse;

import groovy.GroovyRuntimePlugin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.codehaus.groovy.eclipse.ui.GroovyDialogProvider;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class GroovyPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static GroovyPlugin plugin;
	//Resource bundle.

	private ResourceBundle resourceBundle;
	public static final String GROOVY_BUILDER = "org.codehaus.groovy.eclipse.groovyBuilder"; //$NON-NLS-1$
	public static final String GROOVY_NATURE = "org.codehaus.groovy.eclipse.groovyNature"; //$NON-NLS-1$
	private GroovyDialogProvider dialogProvider = new GroovyDialogProvider();
	private static boolean trace;
	static {
		String value = Platform.getDebugOption("org.codehaus.groovy.eclipse/trace"); //$NON-NLS-1$
		if (value != null && value.equalsIgnoreCase("true")) //$NON-NLS-1$
			GroovyPlugin.trace = true;
	}

	class GroovyFilesChangeListner implements IResourceChangeListener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
		 */
		public void resourceChanged(IResourceChangeEvent event) {
			if (event.getType() != IResourceChangeEvent.POST_CHANGE) {
				return;
			}

			IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {
				public boolean visit(IResourceDelta delta) {
					if (delta.getKind() != IResourceDelta.ADDED)
						return true;
					//only interested in content changes
					IResource resource = delta.getResource();

					if (resource.getType() == IResource.FILE
						&& "groovy".equalsIgnoreCase(resource.getFileExtension())) {
						trace("GroovyFilesChangeListner new groovy file detected : " + resource.getName());
						trace(delta.toString());
						addGrovyExclusionFilter(resource.getProject());
						groovyFileAdded(resource.getProject());
					}
					return true;
				}
			};

			try {
				event.getDelta().accept(visitor);
			} catch (CoreException e) {
				logException("while respondoing to a resource change", e);
			}
		}
	}
	/**
	 * The constructor.
	 */
	public GroovyPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
		try {
			resourceBundle =
				ResourceBundle.getBundle("org.codehaus.groovy.eclipse.TestNatureAndBuilderPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	class AddGroovySupport implements Runnable {
		final IProject project;
		public AddGroovySupport(IProject project) {
			trace("AddGroovySupport.AddGroovySupport()");
			this.project = project;
		}

		public void run() {
			try {
				trace("AddGroovySupport.run()");
				if (!project.exists() || project.hasNature(GROOVY_NATURE))
					return;

				if (dialogProvider.doesUserWantGroovySupport()) {
					addGrovyExclusionFilter(project);
					addGroovyNature(project);
					addGroovyRuntime(project);
				}
			} catch (CoreException e) {
				logException("failed to add groovy support", e);
			}
		}

	}

	public void addGrovyExclusionFilter(IProject project) {
		// make sure .groovy files are not copied to the output
		// dir
		IJavaProject javaProject = JavaCore.create(project);
		String excludedResources =
			javaProject.getOption("org.eclipse.jdt.core.builder.resourceCopyExclusionFilter", true);
		if (excludedResources.indexOf("*.groovy") == -1) {
			excludedResources = excludedResources.length() == 0 ? "*.groovy" : excludedResources + ",*.groovy";
			javaProject.setOption("org.eclipse.jdt.core.builder.resourceCopyExclusionFilter", excludedResources);
		}
	}

	/**
	 * @param project
	 */
	protected void groovyFileAdded(IProject project) {
		AddGroovySupport support = new AddGroovySupport(project);
		Display.getDefault().asyncExec(support);
	}

	/**
	 *  
	 */
	public void addGroovyNature(IProject project) throws CoreException {
		trace("GroovyPlugin.addGroovyNature()");
		if (project.hasNature(GROOVY_NATURE))
			return;

		IProjectDescription description = project.getDescription();
		String[] ids = description.getNatureIds();
		String[] newIds = new String[ids.length + 1];
		System.arraycopy(ids, 0, newIds, 0, ids.length);
		newIds[ids.length] = GROOVY_NATURE;
		description.setNatureIds(newIds);
		project.setDescription(description, null);
	}

	public void removeGroovyNature(IProject project) throws CoreException {
		trace("GroovyPlugin.removeGroovyNature()");
		IProjectDescription description = project.getDescription();
		String[] ids = description.getNatureIds();
		for (int i = 0; i < ids.length; ++i) {
			if (ids[i].equals(GROOVY_NATURE)) {
				String[] newIds = new String[ids.length - 1];
				System.arraycopy(ids, 0, newIds, 0, i);
				System.arraycopy(ids, i + 1, newIds, i, ids.length - i - 1);
				description.setNatureIds(newIds);
				project.setDescription(description, null);
				return;
			}
		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static GroovyPlugin getPlugin() {
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = GroovyPlugin.getPlugin().getResourceBundle();
		try {
			return (bundle != null ? bundle.getString(key) : key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	/**
	 * @param project
	 */
	public void addGroovyRuntime(IProject project) {
		trace("GroovyPlugin.addGroovyRuntime()");
		List groovyRuntimeJars = GroovyRuntimePlugin.getPlugin().getGroovyRuntimeJars();
		for (Iterator iter = groovyRuntimeJars.iterator(); iter.hasNext();) {
			String jarName = (String) iter.next();
			try {
				addJar(JavaCore.create(project), GroovyRuntimePlugin.PLUGIN_ID, jarName);
			} catch (Exception e) {
				logException("failed to add jar :" + jarName, e);
			}
		}
	}

	public void addJunitSupprt(IJavaProject project) throws MalformedURLException, JavaModelException, IOException {
		trace("GroovyPlugin.addJunitSupprt()");
		IClasspathEntry[] entries = project.getRawClasspath();
		boolean found = false;
		for (int i = 0; i < entries.length; i++) {
			IClasspathEntry entry = entries[i];
			if (entry.getPath().lastSegment().equals("junit.jar")) {
				found = true;
			}
		}
		if (!found) {
			addJar(project, "org.junit", "junit.jar");
		}
	}

	private void addJar(IJavaProject javaProject, String plugin, String jar)
		throws MalformedURLException, IOException, JavaModelException {
		Path result = findFileInPlugin(plugin, jar);
		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[oldEntries.length] = JavaCore.newLibraryEntry(result, null, null);
		javaProject.setRawClasspath(newEntries, null);
	}

	private Path findFileInPlugin(String plugin, String file) throws MalformedURLException, IOException {
		IPluginRegistry registry = Platform.getPluginRegistry();
		IPluginDescriptor descriptor = registry.getPluginDescriptor(plugin);
		URL pluginURL = descriptor.getInstallURL();
		URL jarURL = new URL(pluginURL, file);
		URL localJarURL = Platform.asLocalURL(jarURL);
		return new Path(localJarURL.getPath());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#startup()
	 */
	public void startup() throws CoreException {
		super.startup();
		getWorkspace().addResourceChangeListener(new GroovyFilesChangeListner());
		GroovyModel.getModel().updateProjects();

	}

	public void logException(String message, Exception e) {
		IStatus status = new Status(IStatus.ERROR, getDescriptor().getUniqueIdentifier(), 0, message, e); //$NON-NLS-1$
		getLog().log(status);
	}
	/**
	 * @return Returns the dialogProvider.
	 */
	public GroovyDialogProvider getDialogProvider() {
		return dialogProvider;
	}

	/**
	 * @param dialogProvider
	 *            The dialogProvider to set.
	 */
	public void setDialogProvider(GroovyDialogProvider dialogProvider) {
		this.dialogProvider = dialogProvider;
	}

	public static void trace(String message) {
		if (trace) {
			System.out.println(message);
		}
	}
}

package org.codehaus.groovy.eclipse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.codehaus.groovy.eclipse.editor.GroovyPartitionScanner;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.codehaus.groovy.eclipse.ui.GroovyDialogProvider;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.osgi.util.ManifestElement;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

/**
 * The main plugin class to be used in the desktop.
 */
public class GroovyPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static GroovyPlugin plugin;
	
	//Resource bundle.
	public final static String GROOVY_PARTITIONING = "__groovy_partitioning"; //$NON-NLS-1$

	private ResourceBundle resourceBundle;


	GroovyDialogProvider dialogProvider = new GroovyDialogProvider();

	static boolean trace;

	private IPartitionTokenScanner partitionScanner;

	private static final String PLUGIN_ID = "org.codehaus.groovy.eclipse";

	private GroovyFilesChangeListner groovyFilesChangeListner;

	static {
		String value = Platform
				.getDebugOption("org.codehaus.groovy.eclipse/trace"); //$NON-NLS-1$
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
							&& "groovy".equalsIgnoreCase(resource
									.getFileExtension())) {
						trace("GroovyFilesChangeListner new groovy file detected : "
								+ resource.getName());
						trace(delta.toString());
						try {
							plugin.stopListeningToChanges();
							GroovyModel.getModel().groovyFileAdded(resource);
						} catch (CoreException e) {
							logException("failed to add groovy runtime support",e);			
						}
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
	public GroovyPlugin() {
		super();
		plugin = this;
		groovyFilesChangeListner = new GroovyFilesChangeListner();
		try {
			resourceBundle = ResourceBundle
					.getBundle("org.codehaus.groovy.eclipse.TestNatureAndBuilderPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
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
		String requires = (String) getBundle().getHeaders().get(
				org.osgi.framework.Constants.BUNDLE_CLASSPATH);
		ManifestElement[] elements;
		try {
			elements = ManifestElement.parseHeader(Constants.BUNDLE_CLASSPATH,
					requires);
			// add all jars exported by this plugin apart from Groovy.jar which
			// contains this class..
			for (int i = 0; i < elements.length; i++) {
				String libName = elements[i].getValue();
				if (!libName.endsWith("groovy-eclipse.jar")) {
					addJar(JavaCore.create(project), PLUGIN_ID, libName);
				}

			}
		} catch (Exception e) {
			logException("Failed to add groovy runtime support",e);
		}

	}

	public void addJunitSupprt(IJavaProject project)
			throws MalformedURLException, JavaModelException, IOException {
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

	private void addJar(IJavaProject javaProject, String srcPlugin, String jar)
			throws MalformedURLException, IOException, JavaModelException {
		Path result = findFileInPlugin(srcPlugin, jar);
		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[oldEntries.length] = JavaCore.newLibraryEntry(result, null,
				null);
		javaProject.setRawClasspath(newEntries, null);
	}

	private Path findFileInPlugin(String srcPlugin, String file)
			throws MalformedURLException, IOException {
		Bundle bundle = Platform.getBundle(srcPlugin);
		URL pluginURL = bundle.getEntry("/");
		URL jarURL = new URL(pluginURL, file);
		URL localJarURL = Platform.asLocalURL(jarURL);
		return new Path(localJarURL.getPath());
	}

	public void logException(String message, Exception e) {
		IStatus status = new Status(IStatus.ERROR, getBundle()
				.getSymbolicName(), 0, message, e); //$NON-NLS-1$
		getLog().log(status);
	}
	public void logTraceMessage(String message) {
		IStatus status = new Status(IStatus.INFO, getBundle()
				.getSymbolicName(), 0, message, null); //$NON-NLS-1$
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
			// Can be hard to get at output in standard out
			// TODO: this should be controlled by the preferences page
			getPlugin().logTraceMessage("trace: " + message);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		listenToChanges();
		GroovyModel.getModel().updateProjects();
	}

	/**
	 * @return
	 */
	public IPartitionTokenScanner getGroovyPartitionScanner() {
		if (partitionScanner == null) {
			partitionScanner = new GroovyPartitionScanner();
		}
		return partitionScanner;
	}

	/**
	 * 
	 */
	protected void stopListeningToChanges() {
		trace("stopListenToChanges");
		getWorkspace().removeResourceChangeListener(groovyFilesChangeListner);
	}

	/**
	 * 
	 */
	public void listenToChanges() {
		trace("listenToChanges");
		getWorkspace().addResourceChangeListener(groovyFilesChangeListner);
	}
	/**
	 * 
	 */
	public static AbstractUIPlugin getDefault() {
		return plugin;
	}
}
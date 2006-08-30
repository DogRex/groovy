package org.codehaus.groovy.eclipse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.eclipse.editor.GroovyPartitionScanner;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.codehaus.groovy.eclipse.model.GroovyProject;
import org.codehaus.groovy.eclipse.preferences.PreferenceConstants;
import org.codehaus.groovy.eclipse.ui.GroovyDialogProvider;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.osgi.util.ManifestElement;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;

/**
 * The main plugin class to be used in the desktop.
 */
public class GroovyPlugin 
extends AbstractUIPlugin
implements IStartup
{
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

    private boolean started = false;

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
	public void addGroovyRuntime( final IProject project )
    {
        trace( "GroovyPlugin.addGroovyRuntime()" );
        try
        {
            if( project == null || !project.hasNature( JavaCore.NATURE_ID ))
                return;
            final IJavaProject javaProject = JavaCore.create( project );
            final ManifestElement[] elements = getBundleClasspath();
            // add all jars exported by this plugin apart from Groovy.jar which
            // contains this class..
            for( int i = 0; i < elements.length; i++ )
            {
                final String libName = elements[ i ].getValue();
                if( !libName.endsWith( "groovy-eclipse.jar" ) )
                    addJar( javaProject, PLUGIN_ID, libName );
            }
            final IFolder folder = project.getFolder( getPreferenceStore().getString( PreferenceConstants.GROOVY_COMPILER_OUTPUT_PATH ) );
            if( !folder.exists() )
                folder.create( false, true, null );
            GroovyProject.addGroovyNature( project );
            final IPersistentPreferenceStore preferenceStore = GroovyModel.getModel().getProject( project ).getPreferenceStore();
            preferenceStore.setValue( PreferenceConstants.GROOVY_COMPILER_OUTPUT_PATH, folder.getProjectRelativePath().toString() );
            preferenceStore.save();
            addLibrary( javaProject, folder.getFullPath() );
        }
        catch( Exception e )
        {
            logException( "Failed to add groovy runtime support", e );
        }
    }
    private ManifestElement[] getBundleClasspath() 
    throws BundleException
    {
        return getBundleClasspath( "" + getBundle().getHeaders().get( Constants.BUNDLE_CLASSPATH ) );
    }
    private ManifestElement[] getBundleClasspath( final String requires ) 
    throws BundleException
    {
        if( StringUtils.isBlank( requires ) )
            return new ManifestElement[ 0 ];
        return ManifestElement.parseHeader( Constants.BUNDLE_CLASSPATH, requires );
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

	public static void addJar( final IJavaProject javaProject, 
                               final String srcPlugin, 
                               final String jar ) 
    throws MalformedURLException, IOException, JavaModelException
    {
        addLibrary( javaProject, findFileInPlugin( srcPlugin, jar ) );
    }
    public static void addLibrary( final IJavaProject javaProject, 
                                   final IPath libraryPath ) 
    throws JavaModelException
    {
        final IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
        // Checking to see that duplicate libs are not added to the JavaProject.
        //  This is a basic check, if the jar names are the same, then ignore.
        //  The jars could be different in version number, but then this check 
        //  would let it go.
        for( int i = 0; i < oldEntries.length; i++ )
        {
            final IClasspathEntry entry = oldEntries[ i ];
            if( entry.getPath().lastSegment().equals( libraryPath.lastSegment() ) )
                return;
        }
        final IClasspathEntry[] newEntries = ( IClasspathEntry[] )ArrayUtils.add( oldEntries, JavaCore.newLibraryEntry( libraryPath, null, null, true ) );
        javaProject.setRawClasspath( newEntries, null );
    }
    public static void removeLibrary( final IJavaProject javaProject, 
                                      final IPath libraryPath ) 
    throws JavaModelException
    {
        final IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
        for( int i = 0; i < oldEntries.length; i++ )
        {
            final IClasspathEntry entry = oldEntries[ i ];
            if( entry.getPath().equals( libraryPath ) )
            {
                final IClasspathEntry[] newEntries = ( IClasspathEntry[] )ArrayUtils.remove( oldEntries, i );
                javaProject.setRawClasspath( newEntries, null );
                return;
            }
        }
    }
	public static IPath findFileInPlugin(String srcPlugin, String file)
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
    public void logWarning( final String message ) 
    {
        final IStatus status = new Status( IStatus.WARNING, getBundle().getSymbolicName(), 0, message, null ); //$NON-NLS-1$
        getLog().log( status );
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
        earlyStartup();
	}

    public void earlyStartup()
    {
        if( started  )
            return;
        started = true;
        listenToChanges();
        new Job( "Rebuilding Groovy Classes" )
        {
            protected IStatus run( final IProgressMonitor monitor )
            {
                GroovyModel.getModel().updateProjects( monitor );
                return Status.OK_STATUS;
            }            
        }.schedule();
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

    public IPreferenceStore getPreferenceStore()
    {
        return super.getPreferenceStore();
    }
    
}
package org.codehaus.groovy.eclipse;
import java.io.File;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.eclipse.editor.GroovyEditor;
import org.codehaus.groovy.eclipse.editor.GroovyPartitionScanner;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.codehaus.groovy.eclipse.ui.GroovyDialogProvider;
import org.eclipse.core.resources.IFile;
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
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.osgi.util.ManifestElement;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;
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

	public static final String PLUGIN_ID = "org.codehaus.groovy.eclipse";

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

    public static ManifestElement[] getBundleClasspath() 
    throws BundleException
    {
        return getBundleClasspath( "" + getPlugin().getBundle().getHeaders().get( Constants.BUNDLE_CLASSPATH ) );
    }
    private static ManifestElement[] getBundleClasspath( final String requires ) 
    throws BundleException
    {
        if( StringUtils.isBlank( requires ) )
            return new ManifestElement[ 0 ];
        return ManifestElement.parseHeader( Constants.BUNDLE_CLASSPATH, requires );
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
    public static IEditorPart doOpenEditor(IFile f, boolean activate) {
    	if (f == null)
    		return null;
    	
    	try {
    		FileEditorInput file = new FileEditorInput(f);
    		return openEditorInput(file);
    		
    	} catch (Exception e) {
    		trace("Unexpected error opening path " + f.toString() + "-" + e.getMessage());
    		return null;
    	}
    }
    
    /**
     * Utility function that opens an editor on a given path.
     * 
     * @return part that is the editor
     */
    public static IEditorPart doOpenEditor(IPath path, boolean activate) {
        if (path == null)
            return null;

        try {
    		IEditorInput file = createEditorInput(path);
	        return openEditorInput(file);
            
        } catch (Exception e) {
            trace("Unexpected error opening path " + path.toString() + " - " + e.getMessage());
            return null;
        }
    }
    
    
	private static IEditorPart openEditorInput(IEditorInput file) throws PartInitException {
		final IWorkbench workbench = plugin.getWorkbench();
		if(workbench == null){
			throw new RuntimeException("workbench cannot be null");
		}

		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		if(activeWorkbenchWindow == null){
			throw new RuntimeException("activeWorkbenchWindow cannot be null (we have to be in a ui thread for this to work)");
		}
		
		IWorkbenchPage wp = activeWorkbenchWindow.getActivePage();
      
		// File is inside the workspace
		return IDE.openEditor(wp, file, GroovyEditor.EDITOR_ID);
	}
//  =====================
//  ===================== ALL BELOW IS COPIED FROM org.eclipse.ui.internal.editors.text.OpenExternalFileAction
//  =====================

    public static IEditorInput createEditorInput(IPath path) {
        return createEditorInput(path, true);
    }
    /**
     * @param path
     * @return
     */
    private static IEditorInput createEditorInput(IPath path, boolean askIfDoesNotExist) {
        IEditorInput edInput = null;
        IWorkspace w = ResourcesPlugin.getWorkspace();      

        //let's start with the 'easy' way
    	IFile fileForLocation = w.getRoot().getFileForLocation(path);
    	//if(fileForLocation != null){
    		return new FileEditorInput(fileForLocation);
    	//}

        /*
        
        IFile files[] = w.getRoot().findFilesForLocation(path);
        if (files == null  || files.length == 0 || !files[0].exists()){
            //it is probably an external file
            File systemFile = path.toFile();
            if(systemFile.exists()){
                edInput = createEditorInput(systemFile);
            }
//            else if(askIfDoesNotExist){
//                //this is the last resort... First we'll try to check for a 'good' match,
//                //and if there's more than one we'll ask it to the user
//                List likelyFiles = getLikelyFiles(path, w);
//                IFile iFile = selectWorkspaceFile(likelyFiles.toArray(new IFile[0]));
//                if(iFile != null){
//                    return new FileEditorInput(iFile);
//                }
//                
//                //ok, ask the user for any file in the computer
//                IEditorInput input = selectFilesystemFileForPath(path);
//                if(input != null){
//                    return input;
//                }
//            }
        }else{ //file exists
            edInput = doFileEditorInput(selectWorkspaceFile(files));
        }
        return edInput;
        */
    }

    private static IEditorInput doFileEditorInput(IFile file) {
        if(file == null){
            return null;
        }
        return new FileEditorInput(file);
    }

    
    
}
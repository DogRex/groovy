/*
 * Created on Jan 18, 2004
 *
 */
package groovy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.ILibrary;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Plugin;

/**
 * @author zohar melamed
 *
 */
public class GroovyRuntimePlugin extends Plugin{
	private static GroovyRuntimePlugin plugin;
	private List rtLibs = new ArrayList(); 
	public final static String PLUGIN_ID = "org.codehaus.groovy";
	/**
	 * @param descriptor
	 */
	public GroovyRuntimePlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
		ILibrary[] libraries = descriptor.getRuntimeLibraries();
		// add all jars exported by this plugin apart from Groovy.jar which contains this class..
		for (int i = 0; i < libraries.length; i++) {
			ILibrary library = libraries[i];
			String libName = library.getPath().lastSegment();
			if(!libName.equals("Groovy.jar"))
				rtLibs.add(libName);
		}
	}
	
	/**
	 * Returns the shared instance.
	 */
	public static GroovyRuntimePlugin getPlugin() {
		return plugin;
	}
	
	public List getGroovyRuntimeJars(){
		return rtLibs;
	}
}

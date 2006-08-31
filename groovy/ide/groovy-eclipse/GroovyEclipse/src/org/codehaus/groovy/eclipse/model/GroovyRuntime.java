package org.codehaus.groovy.eclipse.model;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.lang.ArrayUtils;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.builder.GroovyNature;
import org.codehaus.groovy.eclipse.preferences.PreferenceConstants;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.Bundle;

/**
 * This class contains all the utility methods used in adding the Groovy Runtime
 * to a Java project.
 */
public class GroovyRuntime
{
    public static void addGroovyRuntime( final IProject project )
    {
        GroovyPlugin.trace( "GroovyRuntime.addGroovyRuntime()" );
        try
        {
            if( project == null || !project.hasNature( JavaCore.NATURE_ID ))
                return;
            final IJavaProject javaProject = JavaCore.create( project );
            final ManifestElement[] elements = GroovyPlugin.getBundleClasspath();
            // add all jars exported by this plugin apart from Groovy.jar which
            // contains this class..
            for( int i = 0; i < elements.length; i++ )
            {
                final String libName = elements[ i ].getValue();
                if( !libName.endsWith( "groovy-eclipse.jar" ) )
                    addJar( javaProject, GroovyPlugin.PLUGIN_ID, libName );
            }
            final IFolder folder = project.getFolder( GroovyPlugin.getPlugin().getPreferenceStore().getString( PreferenceConstants.GROOVY_COMPILER_OUTPUT_PATH ) );
            if( !folder.exists() )
                folder.create( false, true, null );
            addGroovyNature( project );
            final IPersistentPreferenceStore preferenceStore = GroovyModel.getModel().getProject( project ).getPreferenceStore();
            preferenceStore.setValue( PreferenceConstants.GROOVY_COMPILER_OUTPUT_PATH, folder.getProjectRelativePath().toString() );
            preferenceStore.save();
            addLibrary( javaProject, folder.getFullPath() );
        }
        catch( Exception e )
        {
            GroovyPlugin.getPlugin().logException( "Failed to add groovy runtime support", e );
        }
    }
    public static void addGroovyNature( final IProject project ) 
    throws CoreException
    {
        GroovyPlugin.trace( "GroovyRuntime.addGroovyNature()" );
        if( project.hasNature( GroovyNature.GROOVY_NATURE ) )
            return;
        final IProjectDescription description = project.getDescription();
        final String[] ids = description.getNatureIds();
        final String[] newIds = ( String[] )ArrayUtils.add( ids, GroovyNature.GROOVY_NATURE );
        description.setNatureIds( newIds );
        project.setDescription( description, null );
    }
    public static void removeGroovyNature( final IProject project ) 
    throws CoreException
    {
        GroovyPlugin.trace( "GroovyRuntime.removeGroovyNature()" );
        final IProjectDescription description = project.getDescription();
        final String[] ids = description.getNatureIds();
        for( int i = 0; i < ids.length; ++i )
        {
            if( ids[ i ].equals( GroovyNature.GROOVY_NATURE ) )
            {
                final String[] newIds = ( String[] )ArrayUtils.remove( ids, i );
                description.setNatureIds( newIds );
                project.setDescription( description, null );
                return;
            }
        }
    }
    public static void addJunitSupport( final IJavaProject project ) 
    throws MalformedURLException, JavaModelException, IOException
    {
        GroovyPlugin.trace( "GroovyRuntime.addJunitSupprt()" );
        final IClasspathEntry[] entries = project.getRawClasspath();
        boolean found = false;
        for( int i = 0; i < entries.length; i++ )
        {
            IClasspathEntry entry = entries[ i ];
            if( entry.getPath().lastSegment().equals( "junit.jar" ) )
                found = true;
        }
        if( !found )
            addJar( project, "org.junit", "junit.jar" );
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
        // This is a basic check, if the jar names are the same, then ignore.
        // The jars could be different in version number, but then this check
        // would let it go.
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
    public static IPath findFileInPlugin( final String srcPlugin, 
                                          final String file ) 
    throws MalformedURLException, IOException
    {
        final Bundle bundle = Platform.getBundle( srcPlugin );
        final URL pluginURL = bundle.getEntry( "/" );
        final URL jarURL = new URL( pluginURL, file );
        final URL localJarURL = Platform.asLocalURL( jarURL );
        return new Path( localJarURL.getPath() );
    }
}

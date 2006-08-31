package org.codehaus.groovy.eclipse.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class GroovyProjectModel
{
    // maps class name to a list of org.codehaus.groovy.ast.ModuleNode
    //  ModuleNodes then map to ClassNodes
    private final Map scriptPathModuleNodeMap = new HashMap();
    private final GroovyProject project;
    
    /**
     * Evaluates the class to determine if is an JUnit test
     * 
     * TODO: Subclasses of these two don't seem work with this logic.
     * Parent is returning Object instead of the superclass.
     * 
     * @param classNode
     * @return
     */
    public static boolean isTestCaseClass( final ClassNode classNode )
    {
        ClassNode parent = classNode.getSuperClass();
        while( parent != null )
        {
            // TODO: classes that extend GroovyTestCase have parent object
            // instead of TestCase
            if( parent.getNameWithoutPackage().equals( "TestCase" ) || parent.getNameWithoutPackage().equals( "GroovyTestCase" ) )
                return true;
            parent = parent.getSuperClass();
        }
        return false;
    }
    /**
     * This method returns a list of class nodes that are in module but not in updated,
     * i.e. the set of ClassNodes that has been removed.
     * @param updated
     * @param module
     * @return
     */
    public static List compareModuleNodes( final ModuleNode updated, 
                                           final ModuleNode module )
    {
        final List list = new ArrayList();
        if( updated == null )
            return module.getClasses();
        if( module == null )
            return list;
        final List updatedClasses = updated.getClasses();
        for( final Iterator modIterator = module.getClasses().iterator(); modIterator.hasNext(); )
        {
            final ClassNode clase = ( ClassNode )modIterator.next();
            if( !updatedClasses.contains( clase ) )
                list.add( clase );
        }
        return list;
    }
    /**
     * Drill into a list of ModuleNodes and return a list of ClassNodes.
     * 
     * @param moduleList
     * @return
     */
    public static List getClassesForModules( final List moduleList )
    {
        final List list = new ArrayList();
        if( moduleList == null )
            return list;
        for( final Iterator iter = moduleList.iterator(); iter.hasNext(); )
            list.addAll( (( ModuleNode )iter.next()).getClasses() );
        return list;
    }
    /**
     * This returns a string for the IFile that is used to generate the keys
     * for classNode maps and moduleNode maps.
     * @param file
     * @return
     */
    public static String getSourceFileKey( final IFile file )
    {
        return file.getRawLocation().toOSString();
    }
    
    public GroovyProjectModel( final GroovyProject project )
    {
        this.project = project;
    }
    public Set scriptPaths()
    {
        return scriptPathModuleNodeMap.keySet();
    }
    public GroovyProjectModel clear()
    {
        scriptPathModuleNodeMap.clear();
        return this;
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
    public String[] findAllRunnableClasses() 
    {
        final List results = new ArrayList();
        for( Iterator iter = scriptPathModuleNodeMap.values().iterator(); iter.hasNext(); )
        {
            final List moduleList = ( List )iter.next();
            final List classes = getClassesForModules( moduleList );
            for( final Iterator iterator = classes.iterator(); iterator.hasNext(); )
            {
                final ClassNode classNode = ( ClassNode )iterator.next();
                if( hasRunnableMain( classNode ) || isTestCaseClass( classNode ) )
                    results.add( classNode.getName() );
            }
        }
        return ( String[] )results.toArray( new String[ results.size() ] );
    }
    public static boolean hasRunnableMain( final ClassNode classNode )
    {
        final List mainMethods = classNode.getDeclaredMethods( "main" );
        for( final Iterator methodIterator = mainMethods.iterator(); methodIterator.hasNext(); )
        {
            final MethodNode methodNode = ( MethodNode )methodIterator.next();
            if( methodNode != null && methodNode.isStatic() && methodNode.isVoidMethod() && hasAppropriateArrayArgsForMain( methodNode.getParameters() ) )
                return true;
        }
        return false;
    }
    private static boolean hasAppropriateArrayArgsForMain( final Parameter[] params )
    {
        if( params.length != 1 )
            return false;
        final ClassNode first = params[ 0 ].getType();
        if( !first.isArray() )
            return false;
        final String componentName = first.getComponentType().getName();
        return componentName.equals( "java.lang.String" ) || componentName.equals( "java.lang.Object" );
    }
    /** Overloaded method to return 
     * a list of ClassNodes for a given IFile
     * 
     * @param file
     * @return
     */
    public List getClassesForFile( final IFile file )
    {
        return getClassesForModules( getModuleNodes( file ) );
    }
    public List getModuleNodes( final IFile file ) 
    {
        final String className = getSourceFileKey( file );
        final List list = getModuleNodes(className);
        if( list == null || list.isEmpty() ) 
        {     
            // If there are error markers on the file, then it means that the file
            // has been compiled already and there is no ModuleNode info available
            // from a successful compile
            IMarker[] markers = null;
            try 
            {
                markers = file.findMarkers( GroovyProject.GROOVY_ERROR_MARKER, false, IResource.DEPTH_INFINITE );
            } 
            catch( final CoreException e ) 
            {
                GroovyPlugin.getPlugin().logException( e.getMessage(), e );
            }
            if( ( markers == null || markers.length ==  0 ) && FullGroovyBuilder.isInSourceFolder( file, project.getJavaProject() ) ) 
            {
                GroovyPlugin.trace( "ProjectModel.getModuleNodes() - starting compile for file:" + file );
                //List files = fullBuild();
                project.compileGroovyFile( file );
               	list.clear();
                list.addAll( getModuleNodes( className ) );
            }
        }
        return list;
    }
    public List getModuleNodes( final String scriptPath ) 
    {
        	List l = ( List ) scriptPathModuleNodeMap.get( scriptPath );
        	if (l == null) {
        		l = new ArrayList();
        		
        	}
    	return  l;
    }
    public List removeModuleNodes( final String scriptPath ) 
    {
        return ( List )scriptPathModuleNodeMap.remove( scriptPath );
    }
    public ClassNode getClassNodeForName( final String name ) 
    {
        for( final Iterator iter = scriptPathModuleNodeMap.values().iterator(); iter.hasNext(); ) 
        {
            final List moduleList = ( List )iter.next();
            final List classes = getClassesForModules( moduleList );
            for( final Iterator iterator = classes.iterator(); iterator.hasNext(); ) 
            {
                final ClassNode classNode = ( ClassNode )iterator.next();
                if( classNode.getName().equals( name ) ) 
                    return classNode;
            }
        }
        return null;
    }
    public void updateClassNameModuleNodeMap( final List moduleNodes ) 
    {
        final Map updateMap = new HashMap();
        for( final Iterator iter = moduleNodes.iterator(); iter.hasNext();)
        {
            final ModuleNode moduleNode = ( ModuleNode )iter.next();
            final String key = moduleNode.getDescription();
            List moduleNodeList = ( List )updateMap.get( key );
            if( moduleNodeList == null ) 
            { 
                moduleNodeList = new ArrayList();
                updateMap.put( key, moduleNodeList );
            }
            moduleNodeList.add( moduleNode );
        }
        updateRemoved( updateMap );
        scriptPathModuleNodeMap.putAll( updateMap );
        project.refreshOutput();
    }
    public void updateRemoved( final Map updateMap )
    {
        // Going to check for any removed/renamed class/module nodes.
        for( final Iterator iterator = updateMap.keySet().iterator(); iterator.hasNext(); )
        {
            final String scriptPath = ( String )iterator.next();
            if( !scriptPathModuleNodeMap.containsKey( scriptPath ) )
                continue;
            final List modules = ( List )scriptPathModuleNodeMap.get( scriptPath );
            final List updatedModules = ( List )updateMap.get( scriptPath );
            for( final Iterator modulesIterator = modules.iterator(); modulesIterator.hasNext(); )
            {   
                final ModuleNode module = ( ModuleNode )modulesIterator.next();
                boolean found = false;
                for( final Iterator updatedIterator = updatedModules.iterator(); updatedIterator.hasNext(); )
                {
                    final ModuleNode updated = ( ModuleNode )updatedIterator.next();
                    if( updated.getDescription().equals( module.getDescription() ) )
                    {
                        final List removedClasses = compareModuleNodes( updated, module );
                        for( final Iterator removedIterator = removedClasses.iterator(); removedIterator.hasNext(); )
                            project.removeClassFiles( ( ClassNode )removedIterator.next(), true );
                        found = true;
                        break;
                    }
                }
                if( !found )
                    project.removeClassFiles( module, true );
            }
        }
    }
}

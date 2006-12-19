package org.codehaus.groovy.eclipse.model;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;

public class ChangeSet
{
    private final List filesToBuild = new ArrayList();
    private final List filesToRemove = new ArrayList();
    private boolean fullBuild = false;
    
    public boolean isFullBuild()
    {
        return fullBuild;
    }
    public ChangeSet setFullBuild( final boolean fullBuild )
    {
        this.fullBuild = fullBuild;
        return this;
    }
    public ChangeSet addFileToBuild( final IFile file )
    {
        filesToBuild.add( file );
        return this;
    }
    public List getFilesToBuild()
    {
        return filesToBuild;
    }
    public IFile[] filesToBuild()
    {
        return ( IFile[] )filesToBuild.toArray( new IFile[ 0 ] );
    }
    public List fileNamesToBuildList()
    {
        final List list = new ArrayList();
        for( final Iterator iterator = filesToBuild.iterator(); iterator.hasNext(); )
            list.add( (( IFile )iterator.next()).getLocation().toString() );
        return list;
    }
    public String[] fileNamesToBuild()
    {
        return ( String[] )fileNamesToBuildList().toArray( new String[ 0 ] );
    }
    public ChangeSet addFileToRemove( final IFile file )
    {
        filesToRemove.add( file );
        return this;
    }
    public List getFilesToRemove()
    {
        return filesToRemove;
    }
    public IFile[] filesToRemove()
    {
        return ( IFile[] )filesToRemove.toArray( new IFile[ 0 ] );
    }
    public List fileNamesToRemoveList()
    {
        final List list = new ArrayList();
        for( final Iterator iterator = filesToRemove.iterator(); iterator.hasNext(); )
            list.add( (( IFile )iterator.next()).getLocation().toString() );
        return list;
    }
    public String[] fileNamesToRemove()
    {
        return ( String[] )fileNamesToRemoveList().toArray( new String[ 0 ] );
    }
    public String toString()
    {
        if( isFullBuild() )
            return "ChangeSet: FULL BUILD";
        final StringBuffer buffer = new StringBuffer();
        buffer.append( "ChangeSet: ");
        if( filesToRemove.size() != 0 )
            buffer.append( "remove: " + fileNamesToRemoveList() );
        if( filesToBuild.size() != 0 )
            buffer.append( "build: " + fileNamesToBuildList() );
        return buffer.toString();
    }
}

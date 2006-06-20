/*
 * Copyright 2003(c)  Zohar Melamed
 * All rights reserved.


 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
 statements and notices.  Redistributions must also contain a
 copy of this document.

 2. Redistributions in binary form must reproduce the
 above copyright notice, this list of conditions and the
 following disclaimer in the documentation and/or other
 materials provided with the distribution.

 3. Due credit should be given to The Codehaus and Contributors
 http://timtam.codehaus.org/

 THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 *
 * Created on Mar 23, 2004
 *
 */

package org.codehaus.groovy.eclipse.model;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author Sarah
 */
public class FullGroovyBuilder 
implements IResourceVisitor
{
    private final ChangeSet changeSet = new ChangeSet().setFullBuild( true );
    private final IJavaProject project;

    public FullGroovyBuilder( final IJavaProject project )
    {
        this.project = project;
    }
    public boolean visit( final IResource resource )
    {
        if( resource.getType() != IResource.FILE )
            return true;
        final IFile file = ( IFile )resource.getAdapter( IFile.class );
        if( isGroovyScript( resource ) && isInSourceFolder( file, project ) )
            changeSet.addFileToBuild( file );
        return true;
    }
    public static boolean isGroovyScript( final IResource resource )
    {
        if( resource == null )
            return false;
        final String extension = resource.getFileExtension();
        if( extension == null )
            return false;
        if( !extension.equals( "groovy" ) )
            return false;
        return true;
    }
    public static boolean isInSourceFolder( final IFile file,
                                            final IJavaProject project )
    {
        if( file == null || project == null )
            return false;
        try
        {
            final IClasspathEntry[] entries = project.getResolvedClasspath( false );
            for( int i = 0; i < entries.length; i++ )
            {
                final IClasspathEntry entry = entries[ i ];
                if( entry.getEntryKind() != IClasspathEntry.CPE_SOURCE )
                    continue;
                if( entry.getPath().isPrefixOf( file.getFullPath() ) )
                    return true;
            }
        }
        catch( final JavaModelException e )
        {
            GroovyPlugin.getPlugin().logException( "Error getting resolved classpath from: " + project.getElementName() + ". " + e, e );
        }
        return false;
    }
    public ChangeSet getChangeSet()
    {
        return changeSet;
    }
}

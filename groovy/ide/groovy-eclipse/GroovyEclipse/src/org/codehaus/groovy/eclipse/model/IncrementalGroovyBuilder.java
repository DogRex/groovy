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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;

/**
 * @author Sarah
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class IncrementalGroovyBuilder implements IResourceDeltaVisitor {
    private final ChangeSet changeSet = new ChangeSet().setFullBuild( false );
	
	public boolean visit(IResourceDelta delta) {
		String kind = null;
		switch (delta.getKind()) {
		case IResourceDelta.ADDED:
			kind = "ADDED";
			addFileToBuild(delta); // compile newly added files
			break;
		case IResourceDelta.CHANGED:
			kind = "CHANGED";
			addFileToBuild(delta); // compile changes source
			break;
		case IResourceDelta.TYPE:
			kind = "TYPE"; // TODO: don't know what this is
			break;
		case IResourceDelta.ADDED_PHANTOM:
			kind = "ADDED_PHANTOM"; // TODO: don't know that this is
            break;
		case IResourceDelta.ALL_WITH_PHANTOMS:
			kind = "ALL_WITH_PHANTOMS"; // TODO: don't know that this is
            break;
		case IResourceDelta.CONTENT:
			kind = "CONTENT"; // TODO: don't know that this is
            break;
		case IResourceDelta.DESCRIPTION:
			kind = "DESCRIPTION"; // TODO: don't know what this is
            break;
		case IResourceDelta.ENCODING:
			kind = "ENCODING";   // TODO: don't know what to do
            break;
		case IResourceDelta.MARKERS:
			kind = "MARKERS";   // TODO: don't know what to do 
            break;
		case IResourceDelta.MOVED_FROM:
			kind = "TYPE MOVED_FROM"; // TODO: should delete existing .class files and clean or just build new file
            break;
		case IResourceDelta.MOVED_TO:
			kind = "TYPE MOVED_TO";
			addFileToBuild(delta);
            break;
		case IResourceDelta.NO_CHANGE:
			kind = "NO_CHANGE";
            break;
		case IResourceDelta.OPEN:
			kind = "OPEN";
            break;
		case IResourceDelta.REMOVED:
			kind = "REMOVED";       // TODO: delete existing class files for source file
            addFileToRemove( delta );
            break;
		case IResourceDelta.REMOVED_PHANTOM:
			kind = "REMOVED_PHANTOM"; // TODO: don't know what this is
            break;
		case IResourceDelta.REPLACED:
			kind = "REPLACED";		// recompile file
			addFileToBuild(delta);
            break;
		case IResourceDelta.SYNC:
			kind = "SYNC";			// TODO: full build ?
            break;
		}
		if (".classpath".equals(delta.getResource().getName())){
            changeSet.setFullBuild( true );
		}
		System.out.println("incremental groovy builder resource:" + delta.getResource().getName() +
				" change kind:" + kind);
		return true; // visit children too
	}
	
	private void addFileToBuild(IResourceDelta delta){
		String fileExtension = delta.getResource().getFileExtension();
		if (fileExtension != null && fileExtension.equalsIgnoreCase("groovy")){
			IFile file = (IFile) delta.getResource().getAdapter(IFile.class);
            changeSet.addFileToBuild( file );
		}
	}
    private void addFileToRemove(IResourceDelta delta){
        String fileExtension = delta.getResource().getFileExtension();
        if (fileExtension != null && fileExtension.equalsIgnoreCase("groovy")){
            IFile file = (IFile) delta.getResource().getAdapter(IFile.class);
            changeSet.addFileToRemove( file );
        }
    }
	public ChangeSet getChangeSet()
    {
	    return changeSet;
    }

}

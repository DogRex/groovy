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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.ProcessingUnit;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.syntax.SyntaxException;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;



/**
 * @author MelamedZ
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
class AddErrorMarker implements IWorkspaceRunnable {
	private IResource resource;
	private Exception e;
	/**
	 * @param resource
	 */
	public AddErrorMarker(IResource resource, Exception e) {
		super();
		this.resource = resource;
		this.e = e;
	}
	public void run(IProgressMonitor monitor) throws CoreException {
		if (e instanceof CompilationFailedException) {
			CompilationFailedException compilationFailuresException = (CompilationFailedException) e;
			ProcessingUnit unit = compilationFailuresException.getUnit();
			for(int i =0; i < unit.getErrorCount(); ++i){
				markerFromException(unit.getException(i));
			}
		} else {
			markerFromException(e);
		}
	}
	/**
	 * @param exception
	 */
	private void markerFromException(Exception exception) throws CoreException {
		int line = 0, startCol = 0, endCol = 0;
		if (exception instanceof SyntaxException) {
			SyntaxException se = (SyntaxException) exception;
			line = se.getLine();
			startCol = se.getStartColumn();
			endCol = se.getEndColumn();
		}
		StringWriter trace = new StringWriter();
		e.printStackTrace(new PrintWriter(trace));
		createMarker(IMarker.SEVERITY_ERROR, exception.getMessage(), trace.toString(), line, startCol, endCol);
	}

	private void createMarker(int severity, String message, String stackTrace, int line, int startCol, int endCol)
			throws CoreException {
		GroovyPlugin.trace("creating marker in " + resource.getName());
		IMarker marker = resource.createMarker(GroovyProject.GROOVY_ERROR_MARKER); //$NON-NLS-1$
		Map map = new HashMap(3);
		map.put(IMarker.SEVERITY, new Integer(severity));
		map.put(IMarker.LINE_NUMBER, new Integer(line));
		map.put(IMarker.MESSAGE, message);
		//			map.put(IMarker.CHAR_START, new Integer(startCol));
		//			map.put(IMarker.CHAR_END, new Integer(endCol));
		map.put("trace", stackTrace); //$NON-NLS-1$
		marker.setAttributes(map);
	}
}
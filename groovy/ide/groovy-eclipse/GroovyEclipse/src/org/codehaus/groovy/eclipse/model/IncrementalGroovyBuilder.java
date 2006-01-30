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
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
/**
 * @author Sarah
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class IncrementalGroovyBuilder implements IResourceVisitor {
	private IJavaProject javaProject;
	private Map compilationUnits;
	private GroovyProject groovyPrject;
	private List filesToBuild;

	/**
	 * @param javaProject
	 * @param compilationUnits
	 * @param groovyPrject
	 * @param filesToBuild
	 */
	public IncrementalGroovyBuilder(IJavaProject javaProject, Map compilationUnits, GroovyProject groovyPrject,
			List filesToBuild) {

		this.javaProject = javaProject;
		this.compilationUnits = compilationUnits;
		this.groovyPrject = groovyPrject;
		this.filesToBuild = filesToBuild;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core.resources.IResource)
	 */
	public boolean visit(IResource resource) {
		if (resource.getType() == IResource.FILE) {
			String extension = resource.getFileExtension();
			IFile file = (IFile) resource;
			if (extension != null && extension.equalsIgnoreCase("groovy")) {
				/*
				 * If we dont have a compilation unit, do a fast compile
				 * without generating any class files to get the class info
				 */
				String key = file.getFullPath().toString();
				GroovyPlugin.trace("Looking for matching class file: " + key);
				if (!compilationUnits.containsKey(key)) {
					groovyPrject.compileGroovyFile(file, false);
				}
				CompileUnit compileUnit = (CompileUnit) compilationUnits.get(key);

				//FIXME this is a temporary solution to avoid that scripts prevent the
				// compilation of classes.
				List classes = compileUnit.getClasses();
				if (classes.isEmpty()) {
					GroovyPlugin.trace("No classes found in compile unit for: " + key);
					// skip this resource's members
					return false;
				}

				// find a class file
				ClassNode classNode = (ClassNode) compileUnit.getClasses().get(0);
				String className = classNode.getName();
				try {
					IPath outputLocation = javaProject.getOutputLocation();
					outputLocation = outputLocation.removeFirstSegments(1);
					StringTokenizer tokenizer = new StringTokenizer(className, ".");
					while (tokenizer.hasMoreElements()) {
						String token = tokenizer.nextToken();
						outputLocation = outputLocation.append(token);
					}
					outputLocation = outputLocation.addFileExtension("class");
					IFile ifileClassFile = javaProject.getProject().getFile(outputLocation);
					File classFile = ifileClassFile.getLocation().toFile();
					long srcLastModified = file.getLocation().toFile().lastModified();
					long classLastModified = classFile.lastModified();
					if (classLastModified < srcLastModified) {
						GroovyPlugin.trace("SOURCE FILE " + key + " NEEDS RECOMPILE");
						filesToBuild.add(file);
					} else {
						GroovyPlugin.trace(" class file " + classFile.getAbsolutePath() + " is up to date");
					}
				} catch (JavaModelException e) {
					GroovyPlugin.getPlugin().logException("Faild during an incrementtal build",e);
				}
			}
		}
		return true;
	}
}

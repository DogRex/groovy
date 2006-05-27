/*
 * Created on 21-Jan-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.codehaus.groovy.eclipse.model;

import org.eclipse.core.resources.IFile;

/**
 * @author MelamedZ
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface GroovyBuildListner {
	public void fileBuilt(IFile file);
}

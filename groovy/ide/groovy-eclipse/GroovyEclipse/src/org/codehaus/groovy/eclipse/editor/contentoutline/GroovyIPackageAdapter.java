/*
 * Created on 26-Jan-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.codehaus.groovy.eclipse.editor.contentoutline;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author MelamedZ
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GroovyIPackageAdapter extends JavaElementAdapter implements IPackageDeclaration{

	/**
	 * @param elementType
	 * @param name
	 */
	public GroovyIPackageAdapter(String name) {
		super(IJavaElement.PACKAGE_DECLARATION, name);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.ISourceReference#getSource()
	 */
	public String getSource() throws JavaModelException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.ISourceReference#getSourceRange()
	 */
	public ISourceRange getSourceRange() throws JavaModelException {
		return null;
	}

}

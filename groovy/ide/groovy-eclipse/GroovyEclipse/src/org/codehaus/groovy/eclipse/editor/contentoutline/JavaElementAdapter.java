/*
 * Created on 26-Jan-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.codehaus.groovy.eclipse.editor.contentoutline;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author MelamedZ
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class JavaElementAdapter implements IJavaElement{
	private int elementType;
	private String name;
   /*
	* @param elementType
	*/
	public JavaElementAdapter(int elementType,String name) {
		super();
		this.elementType = elementType;
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IJavaElement#exists()
	 */
	public boolean exists() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IJavaElement#getAncestor(int)
	 */
	public IJavaElement getAncestor(int ancestorType) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IJavaElement#getCorrespondingResource()
	 */
	public IResource getCorrespondingResource() throws JavaModelException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IJavaElement#getElementName()
	 */
	public String getElementName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IJavaElement#getElementType()
	 */
	public int getElementType() {
		return elementType;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IJavaElement#getHandleIdentifier()
	 */
	public String getHandleIdentifier() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IJavaElement#getJavaModel()
	 */
	public IJavaModel getJavaModel() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IJavaElement#getJavaProject()
	 */
	public IJavaProject getJavaProject() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IJavaElement#getOpenable()
	 */
	public IOpenable getOpenable() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IJavaElement#getParent()
	 */
	public IJavaElement getParent() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IJavaElement#getPath()
	 */
	public IPath getPath() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IJavaElement#getPrimaryElement()
	 */
	public IJavaElement getPrimaryElement() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IJavaElement#getResource()
	 */
	public IResource getResource() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IJavaElement#getUnderlyingResource()
	 */
	public IResource getUnderlyingResource() throws JavaModelException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IJavaElement#isReadOnly()
	 */
	public boolean isReadOnly() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IJavaElement#isStructureKnown()
	 */
	public boolean isStructureKnown() throws JavaModelException {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}

	
}

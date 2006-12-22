package org.codehaus.groovy.eclipse.astviews

/**
 * Interface to implement to be a tree node.
 * @author emp
 */
interface ITreeNode {
	ITreeNode getParent()

	/**
	 * Gets the value this tree node represents. This is the wrapped value.  
	 * @return
	 */
	Object getValue()
	
	String getDisplayName()
	
	ITreeNode[] getChildren()
	
	boolean isLeaf()
}

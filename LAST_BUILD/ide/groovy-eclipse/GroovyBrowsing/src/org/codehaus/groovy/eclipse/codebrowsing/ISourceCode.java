package org.codehaus.groovy.eclipse.codebrowsing;

import org.eclipse.jface.text.IRegion;

/**
 * A handle to some source code.
 * 
 * @author emp
 */
public interface ISourceCode {
	/**
	 * The handle to the source code. For example, an IFile.
	 * 
	 * @return
	 */
	public Object getSourceHandle();

	/**
	 * A region of interest in the file. For example, some identifier name.
	 * 
	 * @return The region, or null if there is no region.
	 */
	public IRegion getRegionOfInterest();

	/**
	 * Open the source code in an editor.
	 * 
	 * @return True if the source code was successfully opened in an editor.
	 */
	public boolean open();
}

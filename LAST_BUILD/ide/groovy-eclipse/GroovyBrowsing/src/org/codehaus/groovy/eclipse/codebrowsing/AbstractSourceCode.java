package org.codehaus.groovy.eclipse.codebrowsing;

import org.eclipse.jface.text.IRegion;

/**
 * @see org.codehaus.groovy.eclipse.codebrowsing.ISourceCode
 * @author emp
 */
public abstract class AbstractSourceCode implements ISourceCode {
	private Object handle;
	private IRegion region;

	public AbstractSourceCode(Object handle, IRegion region) {
		this.handle = handle;
		this.region = region;
	}
	
	public Object getSourceHandle() {
		return handle;
	}
	
	public IRegion getRegionOfInterest() {
		return region;
	}
}

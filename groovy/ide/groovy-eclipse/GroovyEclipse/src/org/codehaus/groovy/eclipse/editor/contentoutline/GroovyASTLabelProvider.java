/*
 * Created on Jan 25, 2004
 *
 */
package org.codehaus.groovy.eclipse.editor.contentoutline;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author zohar melamed
 *
 */
public class GroovyASTLabelProvider extends LabelProvider{
	public Image getImage(Object element) {
		TreeAdapter adapter = (TreeAdapter) element; 
		return adapter.getImage();
	}


	public String getText(Object element) {
		TreeAdapter adapter = (TreeAdapter) element;
		return adapter.getText();
	}

}

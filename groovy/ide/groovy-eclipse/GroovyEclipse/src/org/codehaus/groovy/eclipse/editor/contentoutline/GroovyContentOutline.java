/*
 * Created on 21-Jan-2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.codehaus.groovy.eclipse.editor.contentoutline;

import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.eclipse.model.GroovyBuildListner;
import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * @author MelamedZ
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class GroovyContentOutline extends ContentOutlinePage implements GroovyBuildListner {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		getTreeViewer().setContentProvider(new GroovyASTContentProvider());
		getTreeViewer().setLabelProvider(new GroovyASTLabelProvider());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.codehaus.groovy.eclipse.model.GroovyBuildListner#fileBuilt(org.eclipse.core.resources.IFile)
	 */
	public void fileBuilt(IFile fileBuilt, CompileUnit compilationUnit) {
		getTreeViewer().setInput(compilationUnit);
	}

}

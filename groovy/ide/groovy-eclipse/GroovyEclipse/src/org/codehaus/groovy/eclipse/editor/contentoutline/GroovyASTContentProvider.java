/*
 * Created on 22-Jan-2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.codehaus.groovy.eclipse.editor.contentoutline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author MelamedZ
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class GroovyASTContentProvider implements ITreeContentProvider {

	private CompileUnit compilationUnit;
	
	public Object[] getChildren(Object parentElement) {
		TreeAdapter adapter = TreeAdapter.adapter(parentElement.getClass()); 
		return adapter.getChildren(parentElement);
	}

	public Object getParent(Object element) {
		TreeAdapter adapter = TreeAdapter.adapter(element.getClass());
		return adapter.getParent(element);
	}

	public boolean hasChildren(Object element) {
		TreeAdapter adapter = TreeAdapter.adapter(element.getClass());
		return adapter.hasChildren(element);
	}

	public Object[] getElements(Object inputElement) {
		IFile file = (IFile) inputElement;
		compilationUnit = GroovyModel.getModel().getCompilationUnit(file);
		if (compilationUnit == null
			|| compilationUnit.getModules() == null
			|| compilationUnit.getModules().size() == 0) {
			return null;
		}

		List rootElements = new ArrayList();
		createPackageAndImportElements(compilationUnit, rootElements);
		createClassElements(compilationUnit, rootElements);
		
		return rootElements.toArray();
	}

	/**
	 * @param compilationUnit2
	 * @param rootElements
	 */
	private void createClassElements(CompileUnit compilationUnit, List rootElements) {
		List classes = compilationUnit.getClasses();
		for (Iterator iter = classes.iterator(); iter.hasNext();) {
			ClassNode classNode = (ClassNode) iter.next();
			rootElements.add(classNode);
		}
	}

	/**
	 * @param compilationUnit2
	 * @param rootElements
	 *  
	 */

	private void createPackageAndImportElements(CompileUnit compilationUnit, List rootElements) {
		ModuleNode module = (ModuleNode) compilationUnit.getModules().get(0);
		String packageName = module.getPackageName();
		rootElements.add(packageName);
		//TODO add imports once we can do that
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		compilationUnit = (CompileUnit) newInput;
		// for know this is a bit iffy as we use the visitor support in the
		// groovy ast to build a static model

	}

}

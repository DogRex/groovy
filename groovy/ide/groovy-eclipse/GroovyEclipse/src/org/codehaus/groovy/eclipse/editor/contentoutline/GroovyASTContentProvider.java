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
		TreeAdapter adapter = (TreeAdapter) parentElement;
		return adapter.getChildren();
	}

	public Object getParent(Object element) {
		System.out.println("GroovyASTContentProvider.getParent() "+element.getClass().getName());
		TreeAdapter adapter = (TreeAdapter) element;
		return adapter.getParent();
	}

	public boolean hasChildren(Object element) {
		TreeAdapter adapter = (TreeAdapter) element;
		return adapter.hasChildren();
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof CompileUnit) {
			compilationUnit = (CompileUnit) inputElement;
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
		
		TreeAdapter adapter = (TreeAdapter) inputElement;
		return adapter.getChildren();
	}

	/**
	 * @param compilationUnit2
	 * @param rootElements
	 */
	private void createClassElements(CompileUnit compilationUnit, List rootElements) {
		List classes = compilationUnit.getClasses();
		for (Iterator iter = classes.iterator(); iter.hasNext();) {
			ClassNode classNode = (ClassNode) iter.next();
			rootElements.add(new ClassAdapter(classNode));
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
		rootElements.add(new PackageAdapter(packageName,null));
		rootElements.add(new ImportContainer(module));
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

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
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.eclipse.model.GroovyBuildListener;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.codehaus.groovy.eclipse.model.GroovyProject;
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
	private GroovyBuildListener listner;
	private IFile file;


	/**
	 * @param outline
	 */
	public GroovyASTContentProvider(GroovyBuildListener listner) {
		this.listner = listner;
	}

	public Object[] getChildren(Object parentElement) {
		TreeAdapter adapter = (TreeAdapter) parentElement;
		return adapter.getChildren();
	}

	public Object getParent(Object element) {
		TreeAdapter adapter = (TreeAdapter) element;
		return adapter.getParent();
	}

	public boolean hasChildren(Object element) {
		TreeAdapter adapter = (TreeAdapter) element;
		return adapter.hasChildren();
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof IFile){
			file = (IFile) inputElement;
			List rootElements = new ArrayList();
			GroovyModel model = GroovyModel.getModel();
			GroovyProject project = model.getProject(file.getProject());
			createPackageAndImportElements(project.model().getModuleNodes(file), rootElements);
			createClassElements(project.model().getClassesForFile(file), rootElements);
			return rootElements.toArray();
		}
		TreeAdapter adapter = (TreeAdapter) inputElement;
		return adapter.getChildren();
	}

	private void createClassElements(List classes, List rootElements) {
		if (classes != null) {
			for (Iterator iter = classes.iterator(); iter.hasNext();) {
				ClassNode classNode = (ClassNode) iter.next();
				ClassAdapter ca = new ClassAdapter(classNode);
				rootElements.add(ca);
			}
		}
	}

	private void createPackageAndImportElements(List moduleNodes, List rootElements) {
		if (moduleNodes != null && moduleNodes.size() > 0 ) {
			ModuleNode module = (ModuleNode) moduleNodes.get(0);
			String packageName = module.getPackageName();
			rootElements.add(new PackageAdapter(packageName,null));
			rootElements.add(new ImportContainer(module));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		GroovyModel.getModel().removeBuildListener(listner);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		file = (IFile) newInput;
		if(listner != null && newInput == null){
			GroovyModel.getModel().removeBuildListener(listner);
		}
	}

}

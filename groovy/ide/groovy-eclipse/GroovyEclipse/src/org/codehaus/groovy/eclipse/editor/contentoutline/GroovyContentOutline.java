/*
 * Created on 21-Jan-2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.codehaus.groovy.eclipse.editor.contentoutline;

import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.model.GroovyBuildListner;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * @author MelamedZ
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class GroovyContentOutline extends ContentOutlinePage implements GroovyBuildListner {

	private IFile file;

	/**
	 * @param file
	 */
	public GroovyContentOutline(IFile file) {
		this.file = file;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new GroovyASTContentProvider());
		viewer.setLabelProvider(new GroovyASTLabelProvider());
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				TreeAdapter adapter = (TreeAdapter) selection.getFirstElement();
				int line = adapter.getLineNumber();
				navigateToEditor(line);
			}

		});
		
		GroovyModel model = GroovyModel.getModel();
		model.addBuildListener(this);
		CompileUnit unit = model.getCompilationUnit(file);
		getTreeViewer().setInput(unit);
		
	}
	private void navigateToEditor(int line) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart part = page.getActiveEditor();
		ITextEditor editor = (ITextEditor) part;
		IDocument document = editor.getDocumentProvider().getDocument(part.getEditorInput());
		try {
			editor.selectAndReveal(document.getLineOffset(line), document.getLineLength(line));
		} catch (BadLocationException e) {
			GroovyPlugin.getPlugin().logException("failed to navigate to editor line from content outline tree", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.codehaus.groovy.eclipse.model.GroovyBuildListner#fileBuilt(org.eclipse.core.resources.IFile)
	 */
	public void fileBuilt(IFile fileBuilt, CompileUnit compilationUnit) {
		if (fileBuilt.equals(file)) {
			getTreeViewer().setInput(compilationUnit);
			// TODO this is naff , nee to manage change abit better
			getTreeViewer().expandAll();
		}
	}

}

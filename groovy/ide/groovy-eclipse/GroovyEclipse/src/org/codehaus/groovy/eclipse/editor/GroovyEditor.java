package org.codehaus.groovy.eclipse.editor;

import org.codehaus.groovy.eclipse.editor.contentoutline.GroovyContentOutline;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.texteditor.ExtendedTextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class GroovyEditor extends ExtendedTextEditor {

	private ColorManager colorManager;
	private GroovyContentOutline contentOutline;
	public GroovyEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new GroovyConfiguration(colorManager));
		setDocumentProvider(new GroovyDocumentProvider());
	}

	private IContentOutlinePage getContentOutline(){
		if(contentOutline == null){
			contentOutline  = new GroovyContentOutline((IFile) getEditorInput().getAdapter(IFile.class));
		}
		return contentOutline;
	}
	/*
	 * @see IAdaptable#getAdapter(java.lang.Class)
	 * @since 2.0
	 */
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IContentOutlinePage.class)) {
			return getContentOutline(); 
		}

		return super.getAdapter(adapter);
	}

	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}

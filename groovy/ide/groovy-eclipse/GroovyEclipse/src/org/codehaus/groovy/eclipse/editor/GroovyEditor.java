package org.codehaus.groovy.eclipse.editor;

import org.codehaus.groovy.eclipse.editor.contentoutline.GroovyContentOutline;
import org.codehaus.groovy.eclipse.model.IGroovyElement;
import org.eclipse.ui.texteditor.ExtendedTextEditor;

public class GroovyEditor extends ExtendedTextEditor {

	private IGroovyElement groovyElement;
	private ColorManager colorManager;
	private GroovyContentOutline contentOutline = new GroovyContentOutline();
	public GroovyEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new GroovyConfiguration(colorManager));
		setDocumentProvider(new GroovyDocumentProvider());
		//GroovyModel.getModel().addBuildListener(contentOutline);
	}

	/*
	 * @see IAdaptable#getAdapter(java.lang.Class)
	 * @since 2.0
	 */
	public Object getAdapter(Class adapter) {
//		if (adapter.equals(IContentOutlinePage.class)) {
//			return contentOutline; 
//		}

		return super.getAdapter(adapter);
	}

	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}

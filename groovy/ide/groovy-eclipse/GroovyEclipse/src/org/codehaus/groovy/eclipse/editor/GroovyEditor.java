package org.codehaus.groovy.eclipse.editor;
import org.codehaus.groovy.eclipse.editor.contentoutline.GroovyContentOutline;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.texteditor.ExtendedTextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
public class GroovyEditor extends ExtendedTextEditor {
	private ColorManager colorManager;
	public GroovyEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new GroovyConfiguration(colorManager));
		setDocumentProvider(new GroovyDocumentProvider());
	}
	/*
	 * @see IAdaptable#getAdapter(java.lang.Class)
	 * @since 2.0
	 */
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IContentOutlinePage.class)) {
			return new GroovyContentOutline((IFile) getEditorInput()
					.getAdapter(IFile.class));
		}
		return super.getAdapter(adapter);
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}
	protected void editorContextMenuAboutToShow(IMenuManager menu) {
		super.editorContextMenuAboutToShow(menu);
		addAction(menu, "org.codehaus.groovy.eclipse.actions.RunGroovy");
	}
}

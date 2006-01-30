package org.codehaus.groovy.eclipse.editor;
import org.codehaus.groovy.eclipse.editor.contentoutline.GroovyContentOutline;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;



public class GroovyEditor extends AbstractDecoratedTextEditor{
	private ColorManager colorManager;
	public GroovyEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new GroovyConfiguration(colorManager));
	}
	
	/*
	 * @see IAdaptable#getAdapter(java.lang.Class)
	 * @since 2.0
	 */
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IContentOutlinePage.class)) {
		    IFile file = (IFile) getEditorInput().getAdapter(IFile.class);
		    if (file!=null) return new GroovyContentOutline(file);
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

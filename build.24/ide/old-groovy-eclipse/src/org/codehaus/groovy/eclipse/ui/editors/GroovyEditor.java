package org.codehaus.groovy.eclipse.ui.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class GroovyEditor extends TextEditor {

	private ColorManager colorManager;

	public GroovyEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new GroovyConfiguration(colorManager));
		setDocumentProvider(new GroovyDocumentProvider());
        setEditorContextMenuId("#GroovyEditorContext");
	}

    public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

    protected void createActions() {
        super.createActions();
        
        //setAction("EvaluateGroovyText", new EvaluateGroovyTextAction());
    }
}

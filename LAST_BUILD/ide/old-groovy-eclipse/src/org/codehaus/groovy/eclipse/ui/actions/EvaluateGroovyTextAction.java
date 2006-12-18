package org.codehaus.groovy.eclipse.ui.actions;

import org.codehaus.groovy.eclipse.ui.editors.GroovyEditorMessages;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;

/**
 * A toolbar action which toggles the presentation model of the
 * connected text editor. The editor shows either the highlight range
 * only or always the whole document.
 */
public class EvaluateGroovyTextAction extends TextEditorAction {

	public EvaluateGroovyTextAction() {
		super(GroovyEditorMessages.getResourceBundle(), "EvaluateGroovyText.", null); //$NON-NLS-1$
		update();
	}
	
	public void run() {
		ITextEditor editor= getTextEditor();

        //String text = editor.getDocumentProvider().().get();
        Shell shell = new Shell();
        MessageDialog.openInformation(shell, "Groovy Plug-in", "Evaluating the text as a Groovy script.");

	}
	
	public void update() {
        /*
		setChecked(true);
		setEnabled(true);
        */
	}
}

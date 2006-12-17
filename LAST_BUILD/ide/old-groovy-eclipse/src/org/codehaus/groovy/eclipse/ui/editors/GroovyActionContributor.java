package org.codehaus.groovy.eclipse.ui.editors;

import org.codehaus.groovy.eclipse.ui.actions.EvaluateGroovy;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.editors.text.TextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Contributes interesting Groovy actions to the desktop's Edit menu and the toolbar.
 */
public class GroovyActionContributor extends TextEditorActionContributor {

	protected EvaluateGroovy evaluateGroovyAction;

	public GroovyActionContributor() {
		super();
        //evaluateGroovyAction= new EvaluateGroovyTextAction();
        //evaluateGroovyAction= new EvaluateGroovy();
	}
	
    /*
    public TextEditorAction getEvaluateGroovyAction() {
        return evaluateGroovyAction;
    }
    */

	/*
	 * @see IEditorActionBarContributor#init(IActionBars)
	 */
	public void init(IActionBars bars) {
		super.init(bars);
		
        /*
        IMenuManager menuManager= bars.getMenuManager();
        IMenuManager editMenu= menuManager.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
        if (editMenu != null) {
            editMenu.add(new Separator());
            editMenu.add(evaluateGroovyAction);
        }
        */
		
        /*
		IToolBarManager toolBarManager= bars.getToolBarManager();
		if (toolBarManager != null) {
			toolBarManager.add(new Separator());
			toolBarManager.add(evaluateGroovyAction);
		}
        */
	}
	
	private void doSetActiveEditor(IEditorPart part) {
		super.setActiveEditor(part);

		ITextEditor editor= null;
		if (part instanceof ITextEditor)
			editor= (ITextEditor) part;

        /*
		evaluateGroovyAction.setEditor(editor);
		evaluateGroovyAction.update();
        */
	}
	
	/*
	 * @see IEditorActionBarContributor#setActiveEditor(IEditorPart)
	 */
	public void setActiveEditor(IEditorPart part) {
		super.setActiveEditor(part);
		doSetActiveEditor(part);
	}
	
	/*
	 * @see IEditorActionBarContributor#dispose()
	 */
	public void dispose() {
		doSetActiveEditor(null);
		super.dispose();
	}
}

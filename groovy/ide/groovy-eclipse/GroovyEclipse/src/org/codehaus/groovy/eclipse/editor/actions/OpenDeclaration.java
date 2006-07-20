package org.codehaus.groovy.eclipse.editor.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;

public class OpenDeclaration extends EditingAction {
	public void doAction(IAction action) throws BadLocationException {
		//showMessage("Open Declaration!");
		removeText(5, 1);
	}
}

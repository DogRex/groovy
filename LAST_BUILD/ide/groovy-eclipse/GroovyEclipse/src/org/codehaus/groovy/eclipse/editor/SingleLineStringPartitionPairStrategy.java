package org.codehaus.groovy.eclipse.editor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;

public class SingleLineStringPartitionPairStrategy extends
		AbstractPairInPartitionStrategy {
	public boolean isActive() {
		return true;
	}

	public void doInsert(IDocument document, DocumentCommand command) {
		// Complete multi line comment pair.
		String replace = null;
		if (command.offset > 0) {
			try {
				String text = document.get(command.offset - 1, 2);
				if (text.equals("\"\"") == false && text.equals("''") == false)
					return;
			} catch (BadLocationException e) {
			}
		}

		if (command.text.equals("'")) {
			replace = "''''";
		} else if (command.text.equals("\"")) {
			replace = "\"\"\"\"";
		}
		if (replace != null) {
//			command.text = replace;
			command.caretOffset = command.offset + 2;
			command.shiftsCaret = false;
		}
	}

	public void doRemove(IDocument document, DocumentCommand command) {
		// TODO Auto-generated method stub

	}
}

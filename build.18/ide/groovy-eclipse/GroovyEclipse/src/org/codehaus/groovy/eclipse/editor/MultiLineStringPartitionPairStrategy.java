package org.codehaus.groovy.eclipse.editor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;

public class MultiLineStringPartitionPairStrategy extends
		AbstractPairInPartitionStrategy {

	public boolean isActive() {
		return true;
	}

	public void doInsert(IDocument document, DocumentCommand command) {
	}

	public void doRemove(IDocument document, DocumentCommand command) {
		String pair;
		try {
			pair = document.get(command.offset - 2, 6);
			if (pair.equals("\"\"\"\"\"\"") || pair.equals("''''''")) {
				command.offset = command.offset - 2;
				command.length = 6;
				command.text = "";
			}
		} catch (BadLocationException e) {
		}
	}
}

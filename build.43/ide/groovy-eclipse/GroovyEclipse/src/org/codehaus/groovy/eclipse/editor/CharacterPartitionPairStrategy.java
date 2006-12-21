package org.codehaus.groovy.eclipse.editor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;

/**
 * Strategy to deal with possible pairs in the character (ie. normal code)
 * partition.
 * 
 * @author emp
 */
public class CharacterPartitionPairStrategy extends
		AbstractPairInPartitionStrategy {
	public boolean isActive() {
		return true;
	}

	public void doInsert(IDocument document, DocumentCommand command) {
		if (!completeTripleQuotes(document, command)) {
			if (!completeSingle(document, command)) {
				adjustForRedundantPress(document, command);
			}
		} else {
			adjustForRedundantPress(document, command);
		}
	}

	private boolean completeTripleQuotes(IDocument document,
			DocumentCommand command) {
		// Complete triple quotes.
		if (command.offset > 2
				&& (command.text.equals("'") || command.text.equals("\""))) {
			String replace = null;
			String triple;
			try {
				triple = document.get(command.offset - 3, 3);
				if (triple.equals("'''"))
					replace = "'''";
				else if (triple.equals("\"\"\""))
					replace = "\"\"\"";
				if (replace != null) {
					command.shiftsCaret = false;
					command.text = replace;
					command.caretOffset = command.offset;
					return true;
				}
			} catch (BadLocationException e) {
			}
		}

		return false;
	}

	private boolean completeSingle(IDocument document, DocumentCommand command) {
		String replace = null;

		if (command.text.equals("'")) {
			replace = "''";
		} else if (command.text.equals("\"")) {
			replace = "\"\"";
		} else if (command.text.equals("[")) {
			replace = "[]";
		} else if (command.text.equals("(")) {
			replace = "()";
		}

		if (replace != null) {
			command.shiftsCaret = false;
			command.text = replace;
			command.caretOffset = command.offset + 1;
			return true;
		}
		return false;
	}

	/**
	 * When tab, ), etc. is pressed, and the press is redundant because the
	 * closing character has been adden automatically, the cursor should jump to
	 * the end of the completion.
	 * 
	 * @param document
	 * @param command
	 */
	private void adjustForRedundantPress(IDocument document,
			DocumentCommand command) {
		String replace = null;

		if (matchesOne(command.text, new String[] { "\t", "]", ")" })) {
			try {
				if (command.offset > 0) {
					String pair = document.get(command.offset, 1);
					if (matchesOne(pair, new String[] { "]", ")" })) {
						// The comments below are too restrictive. I the long
						// run, this needs a good look at the Java editor does
						// it, it it really matters at all.
						// String pair = document.get(command.offset - 1, 2);
						// if (in(pair, new String[] { "[]", "()" })) {
						command.shiftsCaret = true;
						replace = "";
					}
				}
			} catch (BadLocationException e) {
			}
		}
		if (replace != null) {
			command.text = replace;
			command.caretOffset = command.offset + 1;
		}
	}

	public void doRemove(IDocument document, DocumentCommand command) {
		String pair;
		try {
			pair = document.get(command.offset, 2);
			if (pair.equals("\"\"") || pair.equals("''") || pair.equals("[]")
					|| pair.equals("()")) {
				command.length = 2;
				command.text = "";
				command.caretOffset = command.offset;
			}
		} catch (BadLocationException e) {
		}
	}
}

package org.codehaus.groovy.eclipse.codeassist;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

/**
 * A CharSequence wrapping an IDocument. Now java.util.regex is usable for
 * pattern matching in the document.
 * 
 * This class is to be used then thrown away in case the document changes.
 * 
 * @author emp
 */
public class DocumentCharSequence implements CharSequence {
	private IDocument doc;

	private int start;

	private int end;

	public DocumentCharSequence(IDocument doc, int start) throws IndexOutOfBoundsException {
		this(doc, start, doc.getLength() - 1);
	}

	public DocumentCharSequence(IDocument doc, int start, int end) throws IndexOutOfBoundsException {
		this.doc = doc;
		this.start = start;
		this.end = end;

		if (start < 0) {
			throw new IndexOutOfBoundsException(errorMessage());
		}

		if (start + end + 1 > doc.getLength()) {
			throw new IndexOutOfBoundsException(errorMessage());
		}
	}

	public char charAt(int index) {
		checkRange(index);
		try {
			return doc.getChar(start + index);
		} catch (BadLocationException e) {
			e.printStackTrace();
			checkRange(index);
			throw new IndexOutOfBoundsException(errorMessage());
		}
	}

	public int length() {
		return end - start + 1;
	}

	public CharSequence subSequence(int start, int end) {
		return new DocumentCharSequence(doc, this.start + start, this.start + end);
	}

	public String errorMessage() {
		return "DocumentCharSequence: doc.getLength() = " + doc.getLength() + ", offset = " + start + ", length = "
				+ end;
	}

	public String toString() {
		try {
			return doc.get(start, end - start + 1);
		} catch (BadLocationException e) {
			// LOG
			e.printStackTrace();
			throw new IndexOutOfBoundsException("Implementation error: " + errorMessage() + "\n" + e.getMessage());
		}
	}

	private void checkRange(int index) {
		if (index < start) {
			throw new IndexOutOfBoundsException("Index < 0: " + index);
		}

		if (index >= end) {
			throw new IndexOutOfBoundsException("Index >= length" + index);
		}
	}
}

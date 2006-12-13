package org.codehaus.groovy.eclipse.editor.contentAssist;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

/**
 * A CharSequence wrapping an IDocument. Now java.util.regex is usable for <em>reverse</em>
 * pattern matching in the document.
 * 
 * This class is to be used then thrown away in case the document changes.
 * 
 * @author emp
 */
public class DocumentReverseCharSequence implements CharSequence {
	private IDocument doc;
	private int start;
	private int end;

	public DocumentReverseCharSequence(IDocument doc, int start) {
		this(doc, start, start);
	}
	
	public DocumentReverseCharSequence(IDocument doc, int start, int end) {
		this.doc = doc;
		this.start = start;
		this.end = end;

		if (start < 0 || start >= doc.getLength()) {
			throw new IndexOutOfBoundsException(errorMessage());
		}
		
		if (end < 0 || end >= doc.getLength()) {
			throw new IndexOutOfBoundsException(errorMessage());
		}
	}
	
	public char charAt(int index) {
		checkRange(index);
		try {
			char ret =  doc.getChar(start - index);
			return ret;
		} catch (BadLocationException e) {
			e.printStackTrace();
			checkRange(index);
			throw new IndexOutOfBoundsException(errorMessage());
		}
	}

	public int length() {
		return start - end + 1;
	}

	public CharSequence subSequence(int start, int end) {
		return new DocumentReverseCharSequence(doc, this.start - start, this.start - end + 1);
	}
	
	public String errorMessage() {
		return "DocumentReverseCharSequence: doc.getLength() = " + doc.getLength() + ", start = " + start + ", end = " + end;
	}
	
	public String toString() {
		try {
			return reverse(doc.get(end, start - end + 1));
		} catch (BadLocationException e) {
			// LOG
			e.printStackTrace();
			throw new IndexOutOfBoundsException("Implementation error: " + errorMessage() + "\n" + e.getMessage());
		}
	}
	
	private String reverse(String string) {
		char[] reverse = string.toCharArray();
		for (int i = 0; i < reverse.length / 2; i++) {
			char tmp = reverse[i];
			reverse[i] = reverse[reverse.length - 1 - i];
			reverse[reverse.length - 1 - i] = tmp;
		}
		return new String(reverse);
	}
	
	private void checkRange(int index) {
		if (index > start) {
			throw new IndexOutOfBoundsException("Index < 0: " + index);
		}

		if (index < end) {
			throw new IndexOutOfBoundsException("Index >= length" + index);
		}
	}
}

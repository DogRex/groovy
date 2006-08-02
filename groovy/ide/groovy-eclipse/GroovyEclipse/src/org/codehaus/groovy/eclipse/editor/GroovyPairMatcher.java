package org.codehaus.groovy.eclipse.editor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ICharacterPairMatcher;

/**
 * @author Philip Savkin
 */
public class GroovyPairMatcher implements ICharacterPairMatcher {

	private int anchor;

	private char peerChar;

	private char currChar;

	private int innerCount;

	public void clear() {
	}

	public void dispose() {
	}

	public int getAnchor() {
		return anchor;
	}

	public IRegion match(IDocument iDocument, int offset) {
		if (offset <= 0 || iDocument == null)
			return null;

		try {
			currChar = iDocument.getChar(Math.max(offset - 1, 0));
			if (checkCurrChar()) {
				int peerPos;
				if (anchor == LEFT) {
					peerPos = findLeftPeer(iDocument, offset - 2);
				} else {
					peerPos = findRightPeer(iDocument, offset);
				}
				return new Region(peerPos, 1);
			}
		} catch (BadLocationException e) {
		}
		return null;
	}

	private boolean checkCurrChar() {
		switch (currChar) {
		case '{':
			peerChar = '}';
			anchor = RIGHT;
			break;
		case '[':
			peerChar = ']';
			anchor = RIGHT;
			break;
		case '(':
			peerChar = ')';
			anchor = RIGHT;
			break;
		case '}':
			peerChar = '{';
			anchor = LEFT;
			break;
		case ']':
			peerChar = '[';
			anchor = LEFT;
			break;
		case ')':
			peerChar = '(';
			anchor = LEFT;
			break;
		default:
			return false;
		}
		return true;
	}

	private int findRightPeer(IDocument iDocument, int start) {
		try {
			int end = iDocument.getLength() - 1;
			innerCount = 0;
			for (int i = start; i <= end; i++) {
				if (isMatchingPeer(iDocument.getChar(i)))
					return i;
			}
		} catch (BadLocationException e) {
		}
		return -1;
	}

	private int findLeftPeer(IDocument iDocument, int start) {
		try {
			innerCount = 0;
			for (int i = start; i >= 0; i--) {
				if (isMatchingPeer(iDocument.getChar(i)))
					return i;
			}
		} catch (BadLocationException e) {
		}
		return -1;
	}

	private boolean isMatchingPeer(char c) {
		if (c == currChar) {
			innerCount++;
		} else if (c == peerChar) {
			if (innerCount > 0)
				innerCount--;
			else
				return true;
		}
		return false;
	}
}

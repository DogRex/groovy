package org.codehaus.groovy.eclipse.editor;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.source.ICharacterPairMatcher;

/**
 * @author Philip Savkin
 */
public class GroovyPairMatcher implements ICharacterPairMatcher {

	private int anchor;

	private char peerChar;

	private char currChar;

	private int innerCount;
    
    private ITypedRegion partition;
    
    private boolean matchInCurrentPartitionOnly = false;
    
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
            int currPos = Math.max(offset - 1, 0);
            currChar = iDocument.getChar(currPos);
			if (checkCurrChar()) {
                if (!checkPartition(iDocument, currPos))
                    return null;

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
		    int end = matchInCurrentPartitionOnly ?
		            partition.getOffset() + partition.getLength() :
                        iDocument.getLength() - 1;

			innerCount = 0;
			for (int i = start; i <= end; i++) {
				if (isMatchingPeer(iDocument, i))
					return i;
			}
		} catch (BadLocationException e) {
		}
		return -1;
	}

	private int findLeftPeer(IDocument iDocument, int start) {
		try {
            int end = matchInCurrentPartitionOnly ? partition.getOffset() : 0;
            innerCount = 0;
			for (int i = start; i >= end; i--) {
				if (isMatchingPeer(iDocument, i))
					return i;
			}
		} catch (BadLocationException e) {
		}
		return -1;
	}

	private boolean isMatchingPeer(IDocument document, int offset) throws BadLocationException {
        char c = document.getChar(offset);
		if (c == currChar && isInCurrentPartition(document, offset)) {
			innerCount++;
		} else if (c == peerChar && isInCurrentPartition(document, offset)) {
			if (innerCount > 0)
				innerCount--;
			else
				return true;
		}
		return false;
	}
    
    private boolean checkPartition(IDocument iDocument, int offset) throws BadLocationException {
        partition = TextUtilities.getPartition(iDocument,
                GroovyPlugin.GROOVY_PARTITIONING, offset, false);

        String type = partition.getType();

        if (type.equals(GroovyPartitionScanner.GROOVY_SINGLELINE_COMMENT)
                || type.equals(GroovyPartitionScanner.GROOVY_MULTILINE_COMMENT)) {
            return false;
        }

        matchInCurrentPartitionOnly =
                type.equals(GroovyPartitionScanner.GROOVY_SINGLELINE_STRINGS)
                || type.equals(GroovyPartitionScanner.GROOVY_MULTILINE_STRINGS);
        
        return true;
    }

    private boolean isInCurrentPartition(IDocument iDocument, int offset)
            throws BadLocationException {
        String currentType = partition.getType();
        String checkedType = TextUtilities.getPartition(iDocument,
                GroovyPlugin.GROOVY_PARTITIONING, offset, false).getType();

        return checkedType.equals(currentType);
    }
}

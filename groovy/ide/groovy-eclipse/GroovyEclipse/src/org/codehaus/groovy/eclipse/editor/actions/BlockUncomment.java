package org.codehaus.groovy.eclipse.editor.actions;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextSelection;

/**
 * Uncomment a block of code.
 *
 * @author emp
 */
public class BlockUncomment extends CommentAction {
	public BlockUncomment() {
		super(true);
	}
	
	void doComment(ITextSelection sel) throws BadLocationException {
		// TODO: if inside a comment partition, find it and uncomment.
		String text = getText(sel.getOffset(), sel.getLength());
		CommentClassifier classifier = new CommentClassifier(text, sel.getOffset());
		
		if (classifier.status == CommentClassifier.SURROUNDING) {
			removeText(classifier.ixClose, 2);
			removeText(classifier.ixOpen, 2);
		}
	}
}
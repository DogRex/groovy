package org.codehaus.groovy.eclipse.editor.actions;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextSelection;

/**
 * Add block comment around the current selection.
 *
 * @author emp 
 */
public class BlockComment extends CommentAction {
	public int openComment;
	public int closeComment;
	
	public BlockComment() {
		super(true);
	}
	
	void doComment(ITextSelection sel) throws BadLocationException {
		//TODO: if (insideCommentPartition()) return

		String text = getText(sel.getOffset(), sel.getLength());
		CommentClassifier classifier = new CommentClassifier(text, sel.getOffset());
		switch (classifier.status) {
			case CommentClassifier.NONE:
				insertText(sel.getOffset() + sel.getLength(), "*/");
				insertText(sel.getOffset(), "/*");
				break;
			case CommentClassifier.SURROUNDING:
				insertText(sel.getOffset() + sel.getLength(), "*/");
				removeText(classifier.ixClose, 2);
				removeText(classifier.ixOpen, 2);
				insertText(sel.getOffset(), "/*");
				break;
			case CommentClassifier.NOT_CLOSED:
				removeText(classifier.ixOpen, 2);
				insertText(sel.getOffset(), "/*");
				break;
			case CommentClassifier.NOT_OPEN:
				insertText(sel.getOffset() + sel.getLength(), "*/");
				removeText(classifier.ixClose, 2);
				break;
			case CommentClassifier.STRADLING:
				removeText(classifier.ixOpen, 2);
				removeText(classifier.ixClose, 2);
				break;
		}
	}
}
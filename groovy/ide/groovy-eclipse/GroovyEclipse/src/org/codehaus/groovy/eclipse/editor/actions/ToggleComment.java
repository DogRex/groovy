package org.codehaus.groovy.eclipse.editor.actions;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextSelection;

public class ToggleComment extends CommentAction {
	public ToggleComment() {
		super(false);
	}

	public void doComment(ITextSelection sel) throws BadLocationException {
		if (sel == null)
			return;
		
		List lines = getLines(sel.getStartLine(), sel.getEndLine());

		// Check if adding or removing comments.
		boolean addComments = true;
		
		Pattern pat = Pattern.compile("\\s*\\/\\/.*");
		Matcher matcher = pat.matcher(""); 
		
		Iterator iter = lines.iterator();
		while (iter.hasNext()) {
			String line = (String)iter.next();
			matcher.reset(line);
			if (matcher.matches()) {
				addComments = false;
				break;
			}
		}
		
		// Insert or remove the '//'
		// Cache start and end as they might change when adding/removing comments.
		int start = sel.getStartLine();
		int end = sel.getEndLine();
		for (int row = start; row <= end; ++row) {
			int offset = getLineOffset(row);
			if (addComments) {
				insertText(offset, "//");
			} else {
				String line = getLine(row);
				int ix = line.indexOf("//");
				offset += ix;
				removeText(offset, 2);
			}
		}
	}

}

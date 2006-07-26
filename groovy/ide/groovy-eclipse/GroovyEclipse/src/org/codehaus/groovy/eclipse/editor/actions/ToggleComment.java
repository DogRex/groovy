package org.codehaus.groovy.eclipse.editor.actions;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Toggle comments action. The current line, or lines in the current selection
 * will have line comments added to them.
 * 
 * @author emp
 */
public class ToggleComment extends CommentAction {
	public ToggleComment() {
		super(false);
	}

	public void doComment(final ITextSelection sel) throws BadLocationException {
		if (sel == null)
			return;

		Shell shell = getShell();

		Display display = null;
		if (shell != null && !shell.isDisposed())
			display = shell.getDisplay();

		/**
		 * This can be pretty slow for a large block (say 500 lines). So
		 * show the busy indicator.
		 */
		BusyIndicator.showWhile(display, new Runnable() {
			public void run() {
				List lines;
				try {
					lines = getLines(sel.getStartLine(), sel.getEndLine());
				} catch (BadLocationException e) {
					GroovyPlugin.getPlugin().logException("Should not happen.", e);
					return;
				}

				// Check if adding or removing comments.
				boolean addComments = true;

				Pattern pat = Pattern.compile("\\s*\\/\\/.*");
				Matcher matcher = pat.matcher("");

				Iterator iter = lines.iterator();
				while (iter.hasNext()) {
					String line = (String) iter.next();
					matcher.reset(line);
					if (matcher.matches()) {
						addComments = false;
						break;
					}
				}

				if (addComments) {
					addComments(getDocument(), lines, sel.getStartLine(), sel.getEndLine());
				} else {
					removeComments(getDocument(), lines, sel.getStartLine(), sel.getEndLine());
				}
			}
		});
	}

	private void addComments(IDocument doc, List lines, int start, int end) {
		try {
			long t0 = System.currentTimeMillis();
			for (int row = end; row >= start; --row) {
				IRegion info = doc.getLineInformation(row);
				doc.replace(info.getOffset(), 0, "//");
			}
			
			System.out.println(System.currentTimeMillis() - t0);
		} catch (BadLocationException e) {
			GroovyPlugin.getPlugin().logException("Should not happen.", e);
		}
	}

	private void removeComments(IDocument doc, List lines, int start, int end) {
		try {
			for (int row = end; row >= start; --row) {
				IRegion info = doc.getLineInformation(row);
				int offset = info.getOffset();
				String line = doc.get(offset, info.getLength());
				int ix = line.indexOf("//");
				if (ix != -1) {
					offset += ix;
					doc.replace(offset, 2, "");
				}
			}
		} catch (BadLocationException e) {
			GroovyPlugin.getPlugin().logException("Should not happen.", e);
		}
	}
}
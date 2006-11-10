package org.codehaus.groovy.eclipse.launchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.actions.GroovyOpenAction;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.codehaus.groovy.eclipse.model.ItemPointer;
import org.eclipse.core.resources.IFile;
import org.eclipse.debug.ui.console.FileLink;
import org.eclipse.debug.ui.console.IConsole;
import org.eclipse.debug.ui.console.IConsoleLineTracker;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.console.IHyperlink;

/**
 * 
 * @author Scott Hickey
 * 	TODO: The hardcode ".groovy" in this file is not a good long term solution.
 *
 */
public class GroovyConsoleLineTracker implements IConsoleLineTracker {

	private IConsole console; 
	static Pattern linePattern = Pattern.compile("(.*)\\.groovy:(.*)");

	/**
	 * Opens up a file with a given line
	 */
	public class ConsoleLink implements IHyperlink {
	
		ItemPointer pointer;
	
		public ConsoleLink(ItemPointer pointer) {
			this.pointer = pointer;
		}
		
		public void linkEntered() {}
		public void linkExited() {}

		public void linkActivated() {
			GroovyOpenAction open = new GroovyOpenAction();
			open.run(pointer);
		}
	}
	
	public void init(IConsole console) {
		this.console = console;
	}

	/**
	 * Hyperlink error lines to the editor.
	 */
	public void lineAppended(IRegion line) {
		int lineOffset = line.getOffset();
		int lineLength = line.getLength();
		try {
			String consoleLine = console.getDocument().get(lineOffset, lineLength);
			GroovyPlugin.trace(consoleLine);
			Matcher m = linePattern.matcher(consoleLine);
			String groovyFileName = null;
			int lineNumber = 0;
			int openParenIndexAt = -1;
			int closeParenIndexAt = -1;
			// match
			if (m.matches()) {
				consoleLine = m.group(0);
				openParenIndexAt = consoleLine.indexOf("(");
				if (openParenIndexAt >= 0) {
					String groovyClassName = consoleLine.substring(openParenIndexAt + 1, consoleLine.indexOf(".groovy"));
					String groovyFilePath = consoleLine.substring(3, consoleLine.indexOf(groovyClassName)).trim().replace('.','/');
					groovyFileName = groovyFilePath + groovyClassName + ".groovy";
					int colonIndex = consoleLine.indexOf(":");
					// get the line number in groovy class
					if (colonIndex > 0) { 
						closeParenIndexAt = consoleLine.indexOf(")");  
						lineNumber = Integer.parseInt(consoleLine.substring(colonIndex + 1, closeParenIndexAt));
					}
					GroovyPlugin.trace("groovyFile=" + groovyFileName + " lineNumber:" + lineNumber);
				}
				// hyperlink if we found something
				if (groovyFileName != null) {
					GroovyModel model = GroovyModel.getModel();
					IFile f = model.getIFileForSrcFile(groovyFileName);
	                IHyperlink link = null;
	                if (f !=null) link = new FileLink(f, null, -1, -1, lineNumber);
					if (link != null)
						console.addLink(link, lineOffset + openParenIndexAt + 1, closeParenIndexAt - openParenIndexAt -1);
				}
			}
		} catch (BadLocationException e) {
			GroovyPlugin.trace("unexpected error:" +  e.getMessage());
		}
	}

	/**
	 * NOOP.
	 * @see org.eclipse.debug.ui.console.IConsoleLineTracker#dispose()
	 */
	public void dispose() {
	}

}

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
			String text = console.getDocument().get(lineOffset, lineLength);
			GroovyPlugin.trace(text);
			Matcher m = linePattern.matcher(text);
			String className = new String();
			String groovyFileName = null;
			String groovyFilePath = null;
			int startIndexAt = 0;
			int endIndexAt = 0;
			String groovyFile = null;
			int lineNumber = 0;
			int fileStart = -1;
			// match
			if (m.matches()) {
				className = m.group(0);
				if (className.indexOf(".groovy") >= 0 && className.indexOf("(")>= 0 ) {
					startIndexAt = className.indexOf("(");
					endIndexAt = className.indexOf(".groovy");
					groovyFileName = className.substring(
							startIndexAt + 1, endIndexAt);
					groovyFilePath = className.substring(3, className
							.indexOf(groovyFileName)).trim().replace('.','/');
					groovyFile = groovyFilePath + groovyFileName + ".groovy";
					int colonIndex = className.indexOf(":");
					if (colonIndex > 0) { // get the line number in groovy class
						lineNumber = Integer.parseInt(className.substring(
								colonIndex + 1, className.length() - 1));
					}
					GroovyPlugin.trace("groovyFile=" + groovyFile + " lineNumber:" + lineNumber);
				}
			}
			// hyperlink if we found something
			if (groovyFile != null) {
				GroovyModel model = GroovyModel.getModel();
				IFile f = model.getIFileForSrcFile(groovyFile);
                IHyperlink link = null;
                if (f !=null) link = new FileLink(f, null, -1, -1, lineNumber);
				if (link != null)
					console.addLink(link, lineOffset + startIndexAt + 1, lineLength - fileStart -1);
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

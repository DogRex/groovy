package org.codehaus.groovy.eclipse.editor;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class GroovyWhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return Character.isWhitespace(c);
	}
}

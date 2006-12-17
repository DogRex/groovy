package org.codehaus.groovy.eclipse.ui.editors;

import org.eclipse.jface.text.rules.IWordDetector;

public class GroovyWordDetector implements IWordDetector, IGroovySyntax {
	public boolean isWordStart(char c) {
		return Character.isJavaIdentifierStart(c);
	}

	public boolean isWordPart(char c) {
		return Character.isJavaIdentifierPart(c);
	}

}

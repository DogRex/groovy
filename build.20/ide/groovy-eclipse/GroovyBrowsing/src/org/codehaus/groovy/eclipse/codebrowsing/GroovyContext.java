package org.codehaus.groovy.eclipse.codebrowsing;

/**
 * The default Groovy declaration search context.
 * @author emp
 *
 */
public class GroovyContext implements IDeclarationSearchContext {
	public boolean isActiveContext() {
		return true;
	}
}

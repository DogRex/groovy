package org.codehaus.groovy.eclipse.editor;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;

public class GroovyTagScanner extends RuleBasedScanner {

	public GroovyTagScanner(ColorManager manager) {
		IToken string =
			new Token(
				new TextAttribute(manager.getColor(IGroovyColorConstants.STRING)));

		IRule[] rules = new IRule[4];
		int i = 0;
		// Add rule for double quotes
		rules[i++] = new SingleLineRule("\"", "\"", string, '\\');
		// Add a rule for single quotes
		rules[i++] = new SingleLineRule("'", "'", string, '\\');
		// Add generic whitespace rule.
		rules[i++] = new WhitespaceRule(new GroovyWhitespaceDetector());
		// add single comment rule
		IToken comment =
		new Token(
				new TextAttribute(manager.getColor(IGroovyColorConstants.COMMENT)));
		
		rules[i++] = new PatternRule("//", null, string, '\\', true);
		
		
		setRules(rules);
	}
}

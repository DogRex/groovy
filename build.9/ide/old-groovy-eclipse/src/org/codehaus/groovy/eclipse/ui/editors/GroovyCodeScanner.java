package org.codehaus.groovy.eclipse.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

public class GroovyCodeScanner extends RuleBasedScanner implements IGroovySyntax {
	public GroovyCodeScanner(ColorManager provider) {
		IToken keyword = new Token(
			new TextAttribute(provider.getColor(ColorManager.KEYWORD), null, SWT.BOLD));
		IToken string =	new Token(
			new TextAttribute(provider.getColor(ColorManager.STRING)));
		IToken comment = new Token(
			new TextAttribute(provider.getColor(ColorManager.COMMENT)));
		IToken other = new Token(
			new TextAttribute(provider.getColor(ColorManager.DEFAULT)));

		setDefaultReturnToken(other);
		
		List rules = new ArrayList();
//		rules.add(new EndOfLineRule("//", comment));
//		rules.add(new MultiLineRule("/*", "*/", comment));
//		rules.add(new SingleLineRule("\"", "\"", string, '\\'));
//		rules.add(new SingleLineRule("'", "'", string, '\\'));
		rules.add(new WhitespaceRule(new GroovyWhitespaceDetector()));

		// Add word rule for keywords, types, and constants.
		WordRule wordRule = new WordRule(new GroovyWordDetector(), other);
		for (int i = 0; i < keywords.length; i++) wordRule.addWord(keywords[i], keyword);
		rules.add(wordRule);

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);

	}

}

package org.codehaus.groovy.eclipse.editor;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.ui.text.IJavaColorConstants;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

public class GroovyTagScanner extends RuleBasedScanner {

	public GroovyTagScanner(ColorManager manager) {
		IToken string =
			new Token(
				new TextAttribute(manager.getColor(IJavaColorConstants.JAVA_STRING)));

		IRule[] rules = new IRule[6];
		int i = 0;
		// Add rule for double quotes
		rules[i++] = new SingleLineRule("\"", "\"", string, '\\');
		// Add a rule for single quotes
		rules[i++] = new SingleLineRule("'", "'", string, '\\');
		// Add generic whitespace rule.
		rules[i++] = new WhitespaceRule(new GroovyWhitespaceDetector());
		// add single comment rule
		IToken mlcomment =
		new Token(
				new TextAttribute(manager.getColor(IJavaColorConstants.JAVA_MULTI_LINE_COMMENT)));

		IToken slcomment =
		new Token(
				new TextAttribute(manager.getColor(IJavaColorConstants.JAVA_SINGLE_LINE_COMMENT)));
		
		rules[i++] = new PatternRule("//", null, slcomment, '`', true);
		// ml comments
		rules[i++] = new MultiLineRule("/*", "*/", mlcomment, '`');
		
		WordRule keywords = new WordRule(new IWordDetector(){

			public boolean isWordStart(char c) {
				return Character.isJavaIdentifierPart(c);
			}

			public boolean isWordPart(char c) {
				return isWordStart(c);
			}
		
		}); 
		
		IToken keywordToken =
		new Token(
				new TextAttribute(manager.getColor(IJavaColorConstants.JAVA_KEYWORD)));
		
		Map keywordMap  = org.codehaus.groovy.syntax.Token.getKeywordMap();
		Set keywordSet = keywordMap.keySet();
		for (Iterator iter = keywordSet.iterator(); iter.hasNext();) {
			String  keyword = (String ) iter.next();
			keywords.addWord(keyword,keywordToken);
		}
		
		
		rules[i++] = keywords; 
		setRules(rules);
	}
}

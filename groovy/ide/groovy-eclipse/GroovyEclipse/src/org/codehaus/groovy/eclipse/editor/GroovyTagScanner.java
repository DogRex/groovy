package org.codehaus.groovy.eclipse.editor;

import org.eclipse.jdt.ui.text.IJavaColorConstants;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

public class GroovyTagScanner extends RuleBasedScanner {
	private static String[] keywords = 
	{   
		"@Property",
		"abstract",
		"as",
		"assert",
		"break",
		"case",
		"catch",
		"class",
		"const",
		"continue",
		"def",
		"default",
		"do",
		"else",
		"extends",
		"final",
		"finally",
		"for",
		"goto",
		"if",
		"in",
		"implements",
		"import",
		"instanceof",
		"interface",
		"mixin",
		"native",
		"new",
		"package",
		"private",
		"property",
		"protected",
		"public",
		"return",
		"static",
		"super",
		"switch",
		"synchronized",
		"this",
		"throw",
		"throws",
		"transient",
		"try",
		"volatile",
		"while",
		"true",
		"false",
		"null",
		"void",
		"boolean",
		"byte",
		"int",
		"short",
		"long",
		"float",
		"double",
		"char"};

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

		IToken slcomment =
		new Token(
				new TextAttribute(manager.getColor(IJavaColorConstants.JAVA_SINGLE_LINE_COMMENT)));
		
		rules[i++] = new EndOfLineRule("//", slcomment);
		rules[i++] = new EndOfLineRule("#!", slcomment);

		IToken plainCode =
			new Token(
					new TextAttribute(manager.getColor(IJavaColorConstants.JAVA_DEFAULT)));
		
		WordRule keywordsRule = new WordRule(new IWordDetector(){
			
			public boolean isWordStart(char c) {
				return c == '@' || Character.isJavaIdentifierStart(c);
			}

			public boolean isWordPart(char c) {
				return Character.isJavaIdentifierPart(c);
			}
		
		},plainCode); 
		
		IToken keywordToken =
		new Token(
				new TextAttribute(
						manager.getColor(IJavaColorConstants.JAVA_KEYWORD), null, SWT.BOLD
				)
		);
		
		
		for (int j = 0; j < keywords.length; ++j) {
			String  keyword = keywords[j];
			keywordsRule.addWord(keyword,keywordToken);
		}
		
		
		rules[i++] = keywordsRule; 
		setRules(rules);
	}
}

package org.codehaus.groovy.eclipse.editor;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.preferences.PreferenceConstants;
import org.eclipse.core.runtime.Preferences;
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;

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
	private static String[] gjdkWords = {
		"abs",
		"any",
		"append",
		"asList",
		"asWritable",
		"call",
		"collect",
		"compareTo",
		"count",
		"div",
		"dump",
		"each",
		"eachByte",
		"eachFile",
		"eachLine",
		"every",
		"find",
		"findAll",
		"flatten",
		"getAt",
		"getErr",
		"getIn",
		"getOut",
		"getText",
		"grep",
		"immutable",
		"inject",
		"inspect",
		"intersect",
		"invokeMethods",
		"isCase",
		"join",
		"leftShift",
		"minus",
		"multiply",
		"newInputStream",
		"newOutputStream",
		"newPrintWriter",
		"newReader",
		"newWriter",
		"next",
		"plus",
		"pop",
		"power",
		"previous",
		"print",
		"println",
		"push",
		"putAt",
		"read",
		"readBytes",
		"readLines",
		"reverse",
		"reverseEach",
		"round",
		"size",
		"sort",
		"splitEachLine",
		"step",
		"subMap",
		"times",
		"toInteger",
		"toList",
		"tokenize",
		"upto",
		"waitForOrKill",
		"withPrintWriter",
		"withReader",
		"withStream",
		"withWriter",
		"withWriterAppend",
		"write",
		"writeLine"
	};

	public GroovyTagScanner(ColorManager manager) {
		IToken string =
			new Token(
				new TextAttribute(manager.getColor(IJavaColorConstants.JAVA_STRING)));
		List rules = new ArrayList();

//		IRule[] rules = new IRule[6];
//		int i = 0;
		// Add rule for double quotes
		rules.add( new SingleLineRule("\"", "\"", string, '\\'));
		// Add a rule for single quotes
		rules.add( new SingleLineRule("'", "'", string, '\\'));
		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new GroovyWhitespaceDetector()));
		// add single comment rule

		IToken slcomment =
		new Token(
				new TextAttribute(manager.getColor(IJavaColorConstants.JAVA_SINGLE_LINE_COMMENT)));
		
		rules.add( new EndOfLineRule("//", slcomment));
		rules.add( new EndOfLineRule("#!", slcomment));
		// add keywords rule
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
		// add gjdk to the java keyword rule
		Preferences prefs = GroovyPlugin.getDefault().getPluginPreferences();
		if (prefs.getBoolean(PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_GJDK_ENABLED)) {
			IPreferenceStore store = GroovyPlugin.getDefault().getPreferenceStore();
			RGB gjdkRGB = PreferenceConverter.getColor(store,PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_GJDK_COLOR);
			IToken gjdkToken = new Token(new TextAttribute(new Color(null,gjdkRGB), null, SWT.BOLD));
			for (int j = 0; j < gjdkWords.length; ++j) {
				keywordsRule.addWord(gjdkWords[j],gjdkToken);
			}
		}
		rules.add(keywordsRule); 
		// convert rules list to array and return
		IRule[] ruleArray = new IRule[rules.size()];
		rules.toArray(ruleArray);
		setRules(ruleArray);
	}
}

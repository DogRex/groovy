package org.codehaus.groovy.eclipse.editor;

import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

public class GroovyPartitionScanner extends RuleBasedPartitionScanner {
	public final static String XML_DEFAULT = "__xml_default";
	public final static String XML_COMMENT = "__xml_comment";
	public final static String XML_TAG = "__xml_tag";

	public GroovyPartitionScanner() {

//		IToken xmlComment = new Token(XML_COMMENT);
//		IToken tag = new Token(XML_TAG);
//
//		IPredicateRule[] rules = new IPredicateRule[2];
//
//		rules[0] = new MultiLineRule("<!--", "-->", xmlComment);
//		rules[1] = new StringRule(tag);
//
//		setPredicateRules(rules);
	}
}

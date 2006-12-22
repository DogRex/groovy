package org.codehaus.groovy.eclipse.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class GroovyPartitionScanner extends RuleBasedPartitionScanner {
	public final static String DEFAULT = "groovy_default"; 
	public final static String SINGLELINE_COMMENT = "groovy_singleline_comment"; 
	public final static String MULTILINE_COMMENT = "groovy_multiline_comment"; 
	public final static String STRING = "groovy_string"; 
	
	public GroovyPartitionScanner() {
		List rules = new ArrayList();
		
        rules.add(new EndOfLineRule("//", new Token(SINGLELINE_COMMENT)));
        rules.add(new EndOfLineRule("#", new Token(SINGLELINE_COMMENT)));
		rules.add(new MultiLineRule("/*", "*/", new Token(MULTILINE_COMMENT), (char)0, true));
		rules.add(new MultiLineRule("'", "'", new Token(STRING), '\\', true));
		rules.add(new MultiLineRule("\"", "\"", new Token(STRING), '\\', true));
		
		IPredicateRule[] result= new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
}

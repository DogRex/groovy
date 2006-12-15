package org.codehaus.groovy.eclipse.codebrowsing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Region;

public class TextUtils {
	/**
	 * Given some text, find the offset to the first match of some identifier in
	 * the text.
	 * 
	 * @param text
	 * @param identifier
	 * @return The offset, or -1 if there is no match.
	 */
	public static int findIdentifierOffset(String text, String identifier) {
		String notIdent = "[^a-zA-Z0-9_]";

		Pattern pattern = Pattern.compile(notIdent + "(" + identifier + ")"
				+ notIdent);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.start(1);
		}
		
		return -1;
	}
	
	/**
	 * Given an offset into some text, find the identifier that spans the
	 * offset. An identifier matches [_a-zA-Z][_a-zA-Z0-9]*
	 * 
	 * @param text
	 * @param offset
	 * @return A Region containing the offset and length of the identifier, or
	 *         null if an identifier was not found.
	 * @throws BadLocationException
	 */
	public static Region getIdentifier(String text, int offset)
			throws BadLocationException {
		while (offset > 0 && Character.isJavaIdentifierPart(text.charAt(offset))) {
			--offset;
		}
		++offset;
		Pattern pattern = Pattern.compile("[_a-zA-Z][_a-zA-Z0-9]*");
		Matcher matcher = pattern.matcher(text);
		if (matcher.find(offset)) {
			do {
				int ix = matcher.start();
				String match = matcher.group();
				if (ix <= offset && offset < ix + match.length()) {
					return new Region(ix, match.length());
				}
				offset = ix;
			} while (matcher.find());
		}

		return null;
	}
}

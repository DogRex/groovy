package org.codehaus.groovy.modules.pages;

/**
 * Created by IntelliJ IDEA.
 * Author: Troy Heninger
 * Date: Jan 10, 2004
 * Lexer for GroovyPages.
 */
class Scan implements Tokens {
	private String text;
	private int end1, begin1, end2, begin2, state = HTML, len, level;
	private boolean str1, str2;

	Scan(String text) {
		Strip strip = new Strip(text);
		strip.strip(0);
		this.text = strip.toString();
		len = this.text.length();
	} // Scan()

	private int found(int newState, int skip) {
		begin2 = begin1;
		end2 = --end1;
		begin1 = end1 += skip;
		int lastState = state;
		state = newState;
		return lastState;
	} // found()

	String getToken() {
		return text.substring(begin2, end2);
	} // getToken()

	int nextToken() {
		for (;;) {
			int left = len - end1;
			if (left == 0) {
				end1++; // in order to include the last letter
				return found(EOF, 0);
			}
			char c = text.charAt(end1++);
			char c1 = left > 1 ? text.charAt(end1) : 0;
			char c2 = left > 2 ? text.charAt(end1 + 1) : 0;

			if (str1) {
				if (c == '\\') end1++;
				else if (c == '\'') str1 = false;
				continue;
			} else if (str2) {
				if (c == '\\') end1++;
				else if (c == '"') str2 = false;
				continue;
			} else if (level > 0 && (c == ')' || c == '}' || c == ']')) {
				level--;
				continue;
			}

			switch (state) {
				case HTML:
					if (c == '<' && left > 3) {
						if (c1 == '%') {
							if (c2 == '=') {
								return found(JEXPR, 3);
							} else if (c2 == '@') {
								return found(JDIRECT, 3);
							} else if (c2 == '!') {
								return found(JDECLAR, 3);
							} else if (c2 == '-' && left > 3 && text.charAt(end1 + 2) == '-') {
								if (skipJComment()) continue;
							}
							return found(JSCRIPT, 2);
						}
					} else if (c == '$' && c1 == '{') {
						return found(GEXPR, 2);
					} else if (c == '%' && c1 == '{') {
						if (c2 == '-' && left > 3 && text.charAt(end1 + 2) == '-') {
							if (skipGComment()) continue;
						}
						return found(GSCRIPT, 2);
					} else if (c == '!' && c1 == '{') {
						return found(GDECLAR, 2);
					} else if (c == '@' && c1 == '{') {
						return found(GDIRECT, 2);
					}
					break;
				case JEXPR:
				case JSCRIPT:
				case JDIRECT:
				case JDECLAR:
					if (c == '%' && c1 == '>') {
						return found(HTML, 2);
					}
					break;
				case GEXPR:
				case GDIRECT:
					if (c == '}' && !str1 && !str2 && level == 0) {
						return found(HTML, 1);
					}
					break;
				case GSCRIPT:
					if (c == '}' && c1 == '%' && !str1 && !str2 && level == 0) {
						return found(HTML, 2);
					}
					break;
				case GDECLAR:
					if (c == '}' && (c1 == '!' || c1 == '%') && !str1 && !str2 && level == 0) {
						return found(HTML, 2);
					}
					break;
			}
		}
	} // nextToken()

	private boolean skipComment(char c3, char c4) {
		int ix = end1 + 3;
		for (int ixz = len - 4; ; ix++) {
			if (ix >= ixz) return false;
			if (text.charAt(ix) == '-' && text.charAt(ix + 1) == '-' && text.charAt(ix + 2) == c3
			        && text.charAt(ix + 3) == c4) break;
		}
		text = text.substring(0, --end1) + text.substring(ix + 4);
		len = text.length();
		return true;
	} // skipComment()

	private boolean skipGComment() {
		return skipComment('}', '%');
	} // skipGComment()

	private boolean skipJComment() {
		return skipComment('%', '>');
	} // skipJComment()

	void reset() {
		end1= begin1 = end2 = begin2 = level = 0;
		state = HTML;
	} // reset()

} // Scan


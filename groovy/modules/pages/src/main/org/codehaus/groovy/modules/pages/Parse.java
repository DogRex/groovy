package org.codehaus.groovy.modules.pages;

import groovy.modules.pages.GroovyPage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by IntelliJ IDEA.
 * Author: Troy Heninger
 * Date: Jan 10, 2004
 * Parser for GroovyPages.
 */
public class Parse implements Tokens {
	public static final boolean DEBUG = false;

	private static final Pattern paraBreak = Pattern.compile("/p>\\s*<p[^>]*>", Pattern.CASE_INSENSITIVE);
	private static final Pattern rowBreak = Pattern.compile("((/td>\\s*</tr>\\s*<)?tr[^>]*>\\s*<)?td[^>]*>", Pattern.CASE_INSENSITIVE);

	private Scan scan;
	private StringBuffer buf;
	private String className;
	private boolean finalPass = false;

	public Parse(String name, InputStream in) throws IOException {
		scan = new Scan(readStream(in));
		makeName(name);
	} // Parse()

	public InputStream parse() {
		buf = new StringBuffer();
		page();
		finalPass = true;
		scan.reset();
		page();
//		if (DEBUG) System.out.println(buf);
		InputStream out = new ByteArrayInputStream(buf.toString().getBytes());
		buf = null;
		scan = null;
		return out;
	} // parse()

	private void declare(boolean gsp) {
		if (finalPass) return;
		if (DEBUG) System.out.println("parse: declare");
		buf.append("\n");
		write(scan.getToken().trim(), gsp);
		buf.append("\n\n");
	} // declare()

	private void direct() {
		if (finalPass) return;
		if (DEBUG) System.out.println("parse: direct");
		String text = scan.getToken();
		text = text.trim();
//		System.out.println("direct(" + text + ')');
		if (text.startsWith("page ")) directPage(text);
	} // direct()

	private void directPage(String text) {
		text = text.substring(5).trim();
//		System.out.println("directPage(" + text + ')');
		Pattern pat = Pattern.compile("(\\w+)\\s*=\\s*\"([^\"]*)\"");
		Matcher mat = pat.matcher(text);
		for (int ix = 0;;) {
			if (!mat.find(ix)) return;
			String name = mat.group(1);
			String value = mat.group(2);
			if (name.equals("import")) pageImport(value);
			ix = mat.end();
		}
	} // directPage()

	private void expr() {
		if (!finalPass) return;
		if (DEBUG) System.out.println("parse: expr");
		buf.append("out.print(");
		String text = scan.getToken().trim();
		buf.append(GroovyPage.fromHtml(text));
		buf.append(")\n");
	} // expr()

	private void html() {
		if (!finalPass) return;
		if (DEBUG) System.out.println("parse: html");
		StringBuffer text = new StringBuffer(scan.getToken());
		while (text.length() > 80) {
			int end = 80;
				// don't end a line with a '\'
			while (text.charAt(end - 1) == '\\') end--;
			print(text.subSequence(0, end));
			text.delete(0, end);
		}
		if (text.length() > 0) {
			print(text);
		}
	} // html()

	private void makeName(String name) {
		int slash = name.lastIndexOf('/');
		if (slash >= 0) name = name.substring(slash + 1);
		StringBuffer buf = new StringBuffer(name.length());
		for (int ix = 0, ixz = name.length(); ix < ixz; ix++) {
			char c = name.charAt(ix);
			if (c < '0' || (c > '9' && c < '@') || (c > 'Z' && c < '_') || (c > '_' && c < 'a') || c > 'z') c = '_';
			else if (ix == 0 && c >= '0' && c <= '9') c = '_';
			buf.append(c);
		}
		className = buf.toString();
	} // makeName()

	private static boolean match(CharSequence pat, CharSequence text, int start) {
		int ix = start, ixz = text.length(), ixy = start + pat.length();
		if (ixz > ixy) ixz = ixy;
		if (pat.length() > ixz - start) return false;
		for (; ix < ixz; ix++) {
			if (Character.toLowerCase(text.charAt(ix)) != Character.toLowerCase(pat.charAt(ix - start))) {
				return false;
			}
		}
		return true;
	} // match()

	private static int match(Pattern pat, CharSequence text, int start) {
		Matcher mat = pat.matcher(text);
		if (mat.find(start) && mat.start() == start) {
			return mat.end();
		}
		return 0;
	} // match()

	private void page() {
		if (DEBUG) System.out.println("parse: page");
		if (finalPass) {
			buf.append("\nclass ");
			buf.append(className);
//			buf.append(" extends GroovyPage {\n");
			buf.append(" extends Script {\n");  //implements GroovyPage {\n");
			buf.append("run() {\n");
		} else {
			buf.append("import groovy.modules.pages.GroovyPage\n");
		}
		loop: for (;;) {
			int state = scan.nextToken();
			switch (state) {
				case EOF: break loop;
				case HTML: html(); break;
				case JEXPR: expr(); break;
				case JSCRIPT: script(false); break;
				case JDIRECT: direct(); break;
				case JDECLAR: declare(false); break;
				case GEXPR: expr(); break;
				case GSCRIPT: script(true); break;
				case GDIRECT: direct(); break;
				case GDECLAR: declare(true); break;
			}
		}
		if (finalPass) {
			buf.append("}\n}\n");
//			buf.append("} // run()\n");
		}
	} // page()

	private void pageImport(String value) {
//		System.out.println("pageImport(" + value + ')');
		String[] imports = Pattern.compile(";").split(value.subSequence(0, value.length()));
		for (int ix = 0; ix < imports.length; ix++) {
			buf.append("import ");
			buf.append(imports[ix]);
			buf.append('\n');
		}
	} // pageImport()

	private void print(CharSequence text) {
		buf.append("out.print('");
		for (int ix = 0, ixz = text.length(); ix < ixz; ix++) {
			char c = text.charAt(ix);
			String rep = null;
			if (c == '\n') rep = "\\n";
			else if (c == '\r') rep = "\\r";
			else if (c == '\t') rep = "\\t";
			else if (c == '\'') rep = "\\'";
			else if (c == '\\') rep = "\\\\";
			if (rep != null) buf.append(rep);
			else buf.append(c);
		}
		buf.append("')\n");
	} // print()

	private String readStream(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			byte[] buf = new byte[8192];
			for (;;) {
				int read = in.read(buf);
				if (read <= 0) break;
				out.write(buf, 0, read);
			}
			return out.toString();
		} finally {
			out.close();
			in.close();
		}
	} // readStream()

	private void script(boolean gsp) {
		if (!finalPass) return;
		if (DEBUG) System.out.println("parse: script");
		buf.append("\n");
		write(scan.getToken().trim(), gsp);
		buf.append("\n\n");
	} // script()

	private void write(CharSequence text, boolean gsp) {
		if (!gsp) {
			buf.append(text);
			return;
		}
		for (int ix = 0, ixz = text.length(); ix < ixz; ix++) {
			char c = text.charAt(ix);
			String rep = null;
			if (Character.isWhitespace(c)) {
				for (ix++; ix < ixz; ix++) {
					if (Character.isWhitespace(text.charAt(ix))) continue;
					ix--;
					rep = " ";
					break;
				}
			} else if (c == '&') {
				if (match("&semi;", text, ix)) {
					rep = ";";
					ix += 5;
				} else if (match("&amp;", text, ix)) {
					rep = "&";
					ix += 4;
				} else if (match("&lt;", text, ix)) {
					rep = "<";
					ix += 3;
				} else if (match("&gt;", text, ix)) {
					rep = ">";
					ix += 3;
				}
			} else if (c == '<') {
				if (match("<br>", text, ix) || match("<hr>", text, ix)) {
					rep = "\n";
					ix += 3;
				} else {
					int end = match(paraBreak, text, ix);
					if (end <= 0) end = match(rowBreak, text, ix);
					if (end > 0) {
						rep = "\n";
						ix = end;
					}
				}
			}
			if (rep != null) buf.append(rep);
			else buf.append(c);
		}
	} // write()

} // Parse

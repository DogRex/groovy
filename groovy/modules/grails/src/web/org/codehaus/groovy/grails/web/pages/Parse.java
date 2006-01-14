/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.groovy.grails.web.pages;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * NOTE: Based on work done by on the GSP standalone project (https://gsp.dev.java.net/)
 *
 * Parsing implementation for GSP files
 *
 * @author Troy Heninger
 * @author Graeme Rocher
 * 
 * Date: Jan 10, 2004
 *
 */
public class Parse implements Tokens {
    public static final Log LOG = LogFactory.getLog(Parse.class);

    private static final Pattern paraBreak = Pattern.compile("/p>\\s*<p[^>]*>", Pattern.CASE_INSENSITIVE);
    private static final Pattern rowBreak = Pattern.compile("((/td>\\s*</tr>\\s*<)?tr[^>]*>\\s*<)?td[^>]*>", Pattern.CASE_INSENSITIVE);

    private Scan scan;
    //private StringBuffer buf;
    private StringWriter sw;
    private GSPWriter out;
    private String className;
    private boolean finalPass = false;
    private int tagIndex;

    public Parse(String name, InputStream in) throws IOException {
        scan = new Scan(readStream(in));
        makeName(name);
    } // Parse()

    public InputStream parse() {

        sw = new StringWriter();
        out = new GSPWriter(sw);
        page();
        finalPass = true;
        scan.reset();
        page();
//		if (DEBUG) System.out.println(buf);
        InputStream in = new ByteArrayInputStream(sw.toString().getBytes());
        out = null;
        scan = null;
        return in;
    } // parse()

    private void declare(boolean gsp) {
        if (finalPass) return;
        if (LOG.isDebugEnabled()) LOG.debug("parse: declare");
        out.println();
        write(scan.getToken().trim(), gsp);
        out.println();
        out.println();
    } // declare()

    private void direct() {
        if (finalPass) return;
        if (LOG.isDebugEnabled()) LOG.debug("parse: direct");
        String text = scan.getToken();
        text = text.trim();
//		LOG.debug("direct(" + text + ')');
        if (text.startsWith("page ")) directPage(text);
    } // direct()

    private void directPage(String text) {
        text = text.substring(5).trim();
//		LOG.debug("directPage(" + text + ')');
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
        if (LOG.isDebugEnabled()) LOG.debug("parse: expr");

        String text = scan.getToken().trim();
        out.printlnToOut(GroovyPage.fromHtml(text));

    } // expr()

    private void html() {
        if (!finalPass) return;
        if (LOG.isDebugEnabled()) LOG.debug("parse: html");
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

    private void makeName(String uri) {
	    String name;
        int slash = uri.lastIndexOf('/');
        if (slash > -1) {
            name = uri.substring(slash + 1);
            uri = uri.substring(0,(uri.length() - 1) - name.length());
            while(uri.endsWith("/")) {
	            uri = uri.substring(0,uri.length() -1);
            }
            slash = uri.lastIndexOf('/');
            if(slash > -1) {
                    name = uri.substring(slash + 1) + '_' + name;                  
            }
        }
        else {
            name = uri;
        }
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
        if (LOG.isDebugEnabled()) LOG.debug("parse: page");
        if (finalPass) {
            out.println();
            out.print("class ");
            out.print(className);
            out.println(" extends GroovyPage {");
            out.println("public Object run() {");
        } else {
            out.println("import org.codehaus.groovy.grails.web.pages.GroovyPage");
            out.println("import org.codehaus.groovy.grails.web.taglib.*");
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
                case GSTART_TAG: startTag(); break;
                case GEND_TAG: endTag(); break;
            }
        }
        if (finalPass) {
            out.println("}");
            out.println("}");
        }
    } // page()

    private void endTag() {
        if (!finalPass) return;

       out.print("tag");
       out.print(tagIndex);
       out.println(".doEndTag()\n");
       tagIndex--;
    }

    private void startTag() {
        if (!finalPass) return;
        tagIndex++;
        String text = scan.getToken().trim();
        out.print("tag");
        out.print(tagIndex);
        out.print("= grailsTagRegistry.loadTag('");
        if(text.indexOf(' ') > -1) {
            String[] tagTokens = text.split( " ");
            String tagName = tagTokens[0].trim();
            out.print(tagName);
            out.println("',application,request,response,out)");

            for (int i = 1; i < tagTokens.length; i++) {
                if(tagTokens[i].indexOf('=') > -1) {
                    String[] attr = tagTokens[i].split("=");
                    String name = attr[0].trim();
                    String val = attr[1].trim().substring(1,attr[1].length() - 1);

                    out.print("tag");
                    out.print(tagIndex);
                    out.print(".setAttribute('");
                    out.print(name);
                    out.print("', resolveVariable(tag");
                    out.print(tagIndex);
                    out.print(",'");
                    out.print(name);
                    out.print("','");
                    out.print(val);
                    out.println("'))");
                }
            }
        } else {
            out.print(text);
            out.println("',application,request,response,out)");
        }



        out.print("tag");
        out.print(tagIndex);
        out.println(".doStartTag()");

    }

    private void pageImport(String value) {
//		LOG.debug("pageImport(" + value + ')');
        String[] imports = Pattern.compile(";").split(value.subSequence(0, value.length()));
        for (int ix = 0; ix < imports.length; ix++) {
            out.print("import ");
            out.print(imports[ix]);
            out.println();
        }
    } // pageImport()

    private void print(CharSequence text) {
        StringBuffer buf = new StringBuffer();
        buf.append('\'');
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
        buf.append('\'');
        out.printlnToOut(buf.toString());
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
        if (LOG.isDebugEnabled()) LOG.debug("parse: script");
        out.println();
        write(scan.getToken().trim(), gsp);
        out.println();
        out.println();
    } // script()

    private void write(CharSequence text, boolean gsp) {
        if (!gsp) {
            out.print(text);
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
            if (rep != null) out.print(rep);
            else out.print(c);
        }
    } // write()

} // Parse
/**
 * This will take a groovy test file and turn it into a Java TestCase
 * @author Jeremy Rayner
 */
package org.codehaus.groovy.tck
import java.io.*;
class TestGenerator{
    public String generate(targetDir, srcName,srcText) {
        resultWriter = new StringWriter()
        result = new PrintWriter(resultWriter)

        fileName = srcName
        fileStem = fileName.tokenize(".")[0]

        comments = scrape(srcText," * ",".")
        if (comments[0] != null) {
            behaviourDescription = comments[0].trim()

            //@todo - turn targetDir into gls.ch14.s4 etc...
            //result.println("package gls.ch14.s4;")
            result.println("import junit.framework.*;")
            result.println("import java.io.*;")
            result.println("public class ${fileStem}Test extends TestCase {")

            methodName = turnSentenceIntoJavaName(behaviourDescription)
            methodName = "test${methodName}"

            // test for the source 'as is'
            printCommonTestMethodStart(result, "${methodName}Pass",srcText)
            result.println("        parse(srcBuffer.toString());")
            result.println("    }")

            // test for each of the '@pass' alternatives
            passAlternatives = generateAlternatives(srcText,"@pass")
            passAlternatives.eachWithIndex{anAlternative,i|
                printCommonTestMethodStart(result, "${methodName}Pass${i+1}",anAlternative[0]);
                result.println("        parse(srcBuffer.toString());")
                result.println("    }")
            }

            // test for each of the '@fail:parse' alternatives
            failureToParseAlternatives = generateAlternatives(srcText,"@fail:parse")
            failureToParseAlternatives.eachWithIndex{anAlternative,i|
                printCommonTestMethodStart(result, "${methodName}FailParse${i+1}",anAlternative[0]);
                result.println("        try {")
                result.println("            parse(srcBuffer.toString());")
                result.println('            fail("This line did not fail to parse: ' + anAlternative[1].replaceAll('"','\\\\"') + '");')
                result.println("        } catch (Exception e) {")
                result.println("            // ignore an exception as that is what we're hoping for in this case.")
                result.println("        }")
                result.println("    }")
            }

            // test for each of the '@fail' alternatives, i.e. without being followed by a colon
            failureAlternatives = generateAlternatives(srcText,"@fail(?!:)")
            failureAlternatives.eachWithIndex{anAlternative,i|
                printCommonTestMethodStart(result, "${methodName}Fail${i+1}",anAlternative[0]);
                result.println("        try {")
                result.println("            evaluate(srcBuffer.toString());")
                result.println('            fail("This line did not fail to evaluate: ' + anAlternative[1].replaceAll('"','\\\\"') + '");')
                result.println("        } catch (Exception e) {")
                result.println("            // ignore an exception as that is what we're hoping for in this case.")
                result.println("        }")
                result.println("    }")
            }

            result.println('    public void evaluate(String theSrcText) throws Exception {')
            result.println("        parse(theSrcText);") //@todo - hook up evaluation of groovy src here!!!
            result.println("    }")
            result.println('    public void parse(String theSrcText) throws Exception {')
            result.println('        Reader reader = new StringReader(theSrcText);')
            result.println('        GroovyLexer lexer = new GroovyLexer(reader);')
            result.println('        GroovyRecognizer recognizer = new GroovyRecognizer(lexer);')
            result.println('        recognizer.compilationUnit();')
            result.println("    }")
            result.println('    protected String lineSep = System.getProperty("line.separator");')
            result.println("}")
        }
        return resultWriter.toString()
    }


    // -- useful stuff

    /**
     * Creates alternative versions of the given source, one for each end of line comment tag e.g. //@fail
     * will remove the double slash from the start of each of the matching line.
     * e.g. src text of...
     * <pre>
     *     // a = 1 // @fail
     *     // b = 2 // @fail
     * </pre>
     * will return
     * <pre>
     * [ "a = 1 // @fail NLS // b = 2 // @fail",
     *   "// a = 1 // @fail NLS b = 2 // @fail" ]
     * </pre>
     *
     */
    List generateAlternatives(String srcText, String tag) {
        alternatives = []
        m = java.util.regex.Pattern.compile("//(.*?//\\s*" + tag + "\\S*)\\s").matcher(srcText)
        while (m.find()) {
            foundText = m.group(1)
            uncommentedSrcText = (srcText.substring(0,m.start()) + srcText.substring(m.start() + 2))
            alternatives << [uncommentedSrcText, foundText]
        }
        return alternatives
    }


    /**
     * Common setup code for each test method
     */
    void printCommonTestMethodStart(result, fullMethodName,someSrcText) {
        buffer = new java.io.StringReader(someSrcText)

        result.println("    public void ${fullMethodName}() throws Exception {")
        result.println("        StringBuffer srcBuffer = new StringBuffer();")

        // append each line to the buffer
        buffer.eachLine {line|
            // escape double quotes
            line = line.replaceAll('"','\\\\"')
            result.println ('        srcBuffer.append("' + line + '").append(lineSep);')
        }
    }

    /**
     * Converts the given sentence into a Java style name like TheQuickBrownFox
     */
    String turnSentenceIntoJavaName(String sentence) {
        tokens = sentence.tokenize(" ,;");
        methodName = ""
        for (t in tokens) {
            if (t.size() > 1) {
                methodName += ( t[0].toUpperCase() + t[1...t.size()].toLowerCase() )
            } else if (t.size() == 1) {
                methodName += t[0].toUpperCase()
            }
        }
        return methodName
    }

    /**
     * Fetches a list of all the occurances of text between a string delimiter.
     */
    List scrape(String txt, String tag) {
        return scrape(txt,tag,tag)
    }

    /**
     * Fetches a list of all the occurances of text between two string delimiters (tags).
     */
    List scrape(String txt, String openTag, String closeTag) {
        i = 0; j = 0; k = 0;
        contents = []
        if (txt != null) {
            while (i> -1 && k > -1) {
              i = txt.indexOf(openTag,k)
                if (i > -1) {
                  j = i + openTag.length()
                    if (j > -1) {
                      k = txt.indexOf(closeTag,j)
                        if (k > -1) {
                          contents << txt.substring(j,k)
                        }
                    }
                }
            }
        }
        return contents
    }

}

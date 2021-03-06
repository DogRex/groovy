/*
 $Id$

 Copyright 2003 (C) James Strachan and Bob Mcwhirter. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "groovy" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Codehaus.  For written permission,
    please contact info@codehaus.org.

 4. Products derived from this Software may not be called "groovy"
    nor may "groovy" appear in their names without prior written
    permission of The Codehaus. "groovy" is a registered
    trademark of The Codehaus.

 5. Due credit should be given to The Codehaus -
    http://groovy.codehaus.org/

 THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */
package org.codehaus.groovy.bsf;

import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import org.apache.bsf.BSFDeclaredBean;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.bsf.util.BSFEngineImpl;
import org.apache.bsf.util.BSFFunctions;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.util.Vector;

/**
 * A BSF Engine for the <a href="http://groovy.codehaus.org/">Groovy</a>
 * scripting language.
 * <p/>
 * It's derived from the Jython / JPython engine
 *
 * @author James Strachan
 */
public class GroovyEngine extends BSFEngineImpl {
    protected GroovyShell shell;

    /*
     * Convert a non java class name to a java classname
     * This is used to convert a script name to a name
     * that can be used as a classname with the script is
     * loaded in GroovyClassloader#load()
     * The method simply replaces any invalid characters
     * with "_".
     */
    private String convertToValidJavaClassname(String inName) {
        if (inName == null || inName.equals("")) {
            return "_";
        }
        StringBuffer output = new StringBuffer(inName.length());
        boolean firstChar = true;
        for (int i = 0; i < inName.length(); ++i) {
            char ch = inName.charAt(i);
            if (firstChar && !Character.isJavaIdentifierStart(ch)) {
                ch = '_';
            } else if (!firstChar
                    && !(Character.isJavaIdentifierPart(ch) || ch == '.')) {
                ch = '_';
            }
            firstChar = (ch == '.');
            output.append(ch);
        }
        return output.toString();
    }

    /**
     * Allow an anonymous function to be declared and invoked
     */
    public Object apply(String source, int lineNo, int columnNo, Object funcBody, Vector paramNames,
                        Vector arguments) throws BSFException {
        Object object = eval(source, lineNo, columnNo, funcBody);
        if (object instanceof Closure) {
            // lets call the function
            Closure closure = (Closure) object;
            return closure.call(arguments.toArray());
        }
        return object;
    }

    /**
     * Call the named method of the given object.
     */
    public Object call(Object object, String method, Object[] args) throws BSFException {
        return InvokerHelper.invokeMethod(object, method, args);
    }

    /**
     * Evaluate an expression.
     */
    public Object eval(String source, int lineNo, int columnNo, Object script) throws BSFException {
        try {
            source = convertToValidJavaClassname(source);
            return getEvalShell().evaluate(script.toString(), source);
        } catch (Exception e) {
            throw new BSFException(BSFException.REASON_EXECUTION_ERROR, "exception from Groovy: " + e, e);
        }
    }

    /**
     * Execute a script.
     */
    public void exec(String source, int lineNo, int columnNo, Object script) throws BSFException {
        try {
            // use evaluate to pass in the BSF variables
            source = convertToValidJavaClassname(source);
            getEvalShell().evaluate(script.toString(), source);
        } catch (Exception e) {
            throw new BSFException(BSFException.REASON_EXECUTION_ERROR, "exception from Groovy: " + e, e);
        }
    }

    /**
     * Initialize the engine.
     */
    public void initialize(BSFManager mgr, String lang, Vector declaredBeans) throws BSFException {
        super.initialize(mgr, lang, declaredBeans);

        // create a shell
        shell = new GroovyShell(mgr.getClassLoader());

        // register the mgr with object name "bsf"
        shell.setVariable("bsf", new BSFFunctions(mgr, this));

        int size = declaredBeans.size();
        for (int i = 0; i < size; i++) {
            declareBean((BSFDeclaredBean) declaredBeans.elementAt(i));
        }
    }

    /**
     * Declare a bean
     */
    public void declareBean(BSFDeclaredBean bean) throws BSFException {
        shell.setVariable(bean.name, bean.bean);
    }

    /**
     * Undeclare a previously declared bean.
     */
    public void undeclareBean(BSFDeclaredBean bean) throws BSFException {
        shell.setVariable(bean.name, null);
    }

    /**
     * @return a newly created GroovyShell using the same variable scope but a new class loader
     */
    protected GroovyShell getEvalShell() {
        return new GroovyShell(shell);
    }
}

/*
 * $Id$
 * 
 * Copyright 2003 (C) James Strachan and Bob Mcwhirter. All Rights Reserved.
 * 
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *  1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *  2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *  3. The name "groovy" must not be used to endorse or promote products
 * derived from this Software without prior written permission of The Codehaus.
 * For written permission, please contact info@codehaus.org.
 *  4. Products derived from this Software may not be called "groovy" nor may
 * "groovy" appear in their names without prior written permission of The
 * Codehaus. "groovy" is a registered trademark of The Codehaus.
 *  5. Due credit should be given to The Codehaus - http://groovy.codehaus.org/
 * 
 * THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 *  
 */
package org.codehaus.groovy.ast;

import groovy.lang.GroovyClassLoader;

import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;

/**
 * Represents the entire contents of a compilation step which consists of one
 * or more {@link ModuleNode}instances
 * 
 * @author <a href="mailto:james@coredevelopers.net">James Strachan </a>
 * @version $Revision$
 */
public class CompileUnit {

    private List modules = new ArrayList();
    private Map classes = new HashMap();
    private CompilerConfiguration config;
    private GroovyClassLoader classLoader;
    private CodeSource codeSource;
    private Map classesToCompile = new HashMap();
    private Map classNameToSource = new HashMap();
    
    public CompileUnit(GroovyClassLoader classLoader, CompilerConfiguration config) {
    	this(classLoader, null, config);
    }
    
    public CompileUnit(GroovyClassLoader classLoader, CodeSource codeSource, CompilerConfiguration config) {
        this.classLoader = classLoader;
        this.config = config;
        this.codeSource = codeSource;
    }

    public List getModules() {
        return modules;
    }

    public void addModule(ModuleNode node) {
        // node==null means a compilation error prevented
        // groovy from building an ast
        if (node==null) return;
        modules.add(node);
        node.setUnit(this);
        addClasses(node.getClasses());
    }

    /**
     * @return the ClassNode for the given qualified name or returns null if
     *         the name does not exist in the current compilation unit
     *         (ignoring the .class files on the classpath)
     */
    public ClassNode getClass(String name) {
        ClassNode cn = (ClassNode) classes.get(name);
        if (cn!=null) return cn;
        return (ClassNode) classesToCompile.get(name);
    }

    /**
     * @return a list of all the classes in each module in the compilation unit
     */
    public List getClasses() {
        List answer = new ArrayList();
        for (Iterator iter = modules.iterator(); iter.hasNext();) {
            ModuleNode module = (ModuleNode) iter.next();
            answer.addAll(module.getClasses());
        }
        return answer;
    }

    public CompilerConfiguration getConfig() {
        return config;
    }

    public GroovyClassLoader getClassLoader() {
        return classLoader;
    }
    
    public CodeSource getCodeSource() {
    	return codeSource;
    }

    /**
     * Appends all of the fully qualified class names in this
     * module into the given map
     */
    void addClasses(List classList) {
        for (Iterator iter = classList.iterator(); iter.hasNext();) {
            addClass((ClassNode) iter.next());
        }
    }
    
    /**
     *  Adds a class to the unit.
     */
    public void addClass(ClassNode node) {
    	node = node.redirect();
        String name = node.getName();
        ClassNode stored = (ClassNode) classes.get(name);
        if (stored != null && stored != node) {
            // we have a duplicate class!
            // One possibility for this is, that we delcared a script and a 
            // class in the same file and named the class like the file
            SourceUnit nodeSource = node.getModule().getContext();
            SourceUnit storedSource = stored.getModule().getContext();
            String txt = "Invalid duplicate class definition of class "+node.getName()+" : ";
            if (nodeSource==storedSource) {
                // same class in same source
                txt += "The source "+nodeSource.getName()+" contains at last two defintions of the class "+node.getName()+".\n";
                if (node.isScriptBody() || stored.isScriptBody()) {
                    txt += "One of the classes is a explicit generated class using the class statement, the other is a class generated from"+
                           " the script body based on the file name. Solutions are to change the file name or to change the class name.\n";
                }
            } else {
                txt += "The sources "+nodeSource.getName()+" and "+storedSource.getName()+" are containing both a class of the name "+node.getName()+".\n";
            }
            nodeSource.getErrorCollector().addErrorAndContinue(
                    new SyntaxErrorMessage(new SyntaxException(txt, node.getLineNumber(), node.getColumnNumber()), nodeSource)
            );
        }
        classes.put(name, node);
        
        if (classesToCompile.containsKey(name)) {
            ClassNode cn = (ClassNode) classesToCompile.get(name);
            cn.setRedirect(node);
            classesToCompile.remove(name);
        }        
    }
     
    /**
     * this emthod actually does not compile a class. It's only
     * a marker that this type has to be compiled by the CompilationUnit
     * at the end of a parse step no node should be be left.
     */
    public void addClassNodeToCompile(ClassNode node, SourceUnit location) {
        classesToCompile.put(node.getName(),node);
        classNameToSource.put(node.getName(),location);
    }
    
    public SourceUnit getScriptSourceLocation(String className) {
        return (SourceUnit) classNameToSource.get(className);
    }

    public boolean hasClassNodeToCompile(){
        return classesToCompile.size()!=0;
    }
    
    public Iterator iterateClassNodeToCompile(){
        return classesToCompile.keySet().iterator();
    }
}

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
package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.Variable;

/**
 * Represents a local variable name, the simplest form of expression. e.g. "foo".
 * 
 * @author <a href="mailto:james@coredevelopers.net">James Strachan</a>
 * @version $Revision$
 */
public class VariableExpression extends Expression implements Variable {

    public static final VariableExpression THIS_EXPRESSION = new VariableExpression("this", ClassHelper.DYNAMIC_TYPE);
    public static final VariableExpression SUPER_EXPRESSION = new VariableExpression("super", ClassHelper.DYNAMIC_TYPE);

    private String variable;
    private boolean inStaticContext;
    private boolean isDynamicTyped=false;
    private Variable accessedVariable;
    boolean closureShare=false;

    public Variable getAccessedVariable() {
        return accessedVariable;
    }

    public void setAccessedVariable(Variable origin) {
        this.accessedVariable = origin;
    }

    public VariableExpression(String variable, ClassNode type) {
        this.variable = variable;
        super.setType(ClassHelper.getWrapper(type));
    }
    
    public VariableExpression(String variable) {
        this(variable, ClassHelper.DYNAMIC_TYPE);
    }
    
    public VariableExpression(Variable variable) {
        this(variable.getName(), variable.getType());
        setAccessedVariable(variable);
    }

    public void visit(GroovyCodeVisitor visitor) {
        visitor.visitVariableExpression(this);
    }

    public Expression transformExpression(ExpressionTransformer transformer) {
        return this;
    }

    public String getText() {
        return variable;
    }
    
    public String getName() {
        return variable;
    }

    /**
     * @return true if this variable is dynamically typed
     */
    public String toString() {
        return super.toString() + "[variable: " + variable + (this.isDynamicTyped() ? "" : " type: " + getType()) + "]";
    }

    public Expression getInitialExpression() {
        return null;
    }

    public boolean hasInitialExpression() {
        return false;
    }
    
    public boolean isInStaticContext() {
        if (accessedVariable!=null && accessedVariable!=this) return accessedVariable.isInStaticContext();
        return inStaticContext;
    }
    
    public void setInStaticContext(boolean inStaticContext) {
        this.inStaticContext = inStaticContext;
    }

    public void setType(ClassNode cn){
        super.setType(cn);
        isDynamicTyped |= ClassHelper.DYNAMIC_TYPE==cn;
    }
    
    public boolean isDynamicTyped() {
        return isDynamicTyped;
    }

    public boolean isClosureSharedVariable() {
        if (accessedVariable!=null && accessedVariable!=this) return accessedVariable.isClosureSharedVariable();
        return closureShare;
    }
    
    public void setClosureSharedVariable(boolean inClosure) {
        closureShare = inClosure;        
    }
    
    public ClassNode getType() {
        if (accessedVariable!=null && accessedVariable!=this) return accessedVariable.getType();
        return super.getType();
    }
}

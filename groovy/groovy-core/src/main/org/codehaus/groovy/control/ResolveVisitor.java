/*
 * $Id$
 *
 * Copyright 2003 (C) James Strachan and Bob Mcwhirter. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met: 1. Redistributions of source code must retain
 * copyright statements and notices. Redistributions must also contain a copy
 * of this document. 2. Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the distribution. 3.
 * The name "groovy" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Codehaus. For
 * written permission, please contact info@codehaus.org. 4. Products derived
 * from this Software may not be called "groovy" nor may "groovy" appear in
 * their names without prior written permission of The Codehaus. "groovy" is a
 * registered trademark of The Codehaus. 5. Due credit should be given to The
 * Codehaus - http://groovy.codehaus.org/
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
package org.codehaus.groovy.control;

import groovy.lang.GroovyClassLoader;

import java.io.IOException;
import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.net.URL;
import java.net.MalformedURLException;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.DynamicVariable;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.Variable;
import org.codehaus.groovy.ast.VariableScope;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ExpressionTransformer;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.AssertStatement;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.CaseStatement;
import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.codehaus.groovy.ast.stmt.DoWhileStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.SwitchStatement;
import org.codehaus.groovy.ast.stmt.SynchronizedStatement;
import org.codehaus.groovy.ast.stmt.ThrowStatement;
import org.codehaus.groovy.ast.stmt.WhileStatement;
import org.codehaus.groovy.ast.GroovyClassVisitor;
import org.codehaus.groovy.classgen.Verifier;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.syntax.Types;

/**
 * Visitor to resolve Types and convert VariableExpression to
 * ClassExpressions if needed.
 *
 * Note: the method to start the resolving is  startResolving(ClassNode, SourceUnit).
 *
 *
 * @author Jochen Theodorou
 */
public class ResolveVisitor extends CodeVisitorSupport implements ExpressionTransformer, GroovyClassVisitor {
    private ClassNode currentClass;
    // note: BidInteger and BigDecimal are also imported by default
    private static final String[] DEFAULT_IMPORTS = {"java.lang.", "java.io.", "java.net.", "java.util.", "groovy.lang.", "groovy.util."};
    private CompilationUnit compilationUnit;
    private Map cachedClasses = new HashMap();
    private static final Object NO_CLASS = new Object();
    private static final Object SCRIPT = new Object();
    private SourceUnit source;
    private VariableScope currentScope;

    private boolean isTopLevelProperty = true;

    public ResolveVisitor(CompilationUnit cu) {
        compilationUnit = cu;
    }

    public void startResolving(ClassNode node,SourceUnit source) {
        this.source = source;
        visitClass(node);
    }

    public void visitConstructor(ConstructorNode node) {
        VariableScope oldScope = currentScope;
        currentScope = node.getVariableScope();
        Parameter[] paras = node.getParameters();
        for (int i=0; i<paras.length; i++) {
            ClassNode t = paras[i].getType();
            resolveOrFail(t,node);
        }
        ClassNode[] exceptions = node.getExceptions();
        for (int i=0; i<exceptions.length; i++) {
            ClassNode t = exceptions[i];
            resolveOrFail(t,node);
        }
        Statement code = node.getCode();
        if (code!=null) code.visit(this);
        currentScope = oldScope;
    }

    public void visitSwitch(SwitchStatement statement) {
        Expression exp = statement.getExpression();
        statement.setExpression(transform(exp));
        List list = statement.getCaseStatements();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            CaseStatement caseStatement = (CaseStatement) iter.next();
            caseStatement.visit(this);
        }
        statement.getDefaultStatement().visit(this);
    }

    public void visitMethod(MethodNode node) {
        VariableScope oldScope = currentScope;
        currentScope = node.getVariableScope();
        Parameter[] paras = node.getParameters();
        for (int i=0; i<paras.length; i++) {
            ClassNode t = paras[i].getType();
            resolveOrFail(t,node);
        }
        ClassNode[] exceptions = node.getExceptions();
        for (int i=0; i<exceptions.length; i++) {
            ClassNode t = exceptions[i];
            resolveOrFail(t,node);
        }       
        resolveOrFail(node.getReturnType(),node);
        Statement code = node.getCode();
        if (code!=null) code.visit(this);
        currentScope = oldScope;
    }

    public void visitField(FieldNode node) {
        ClassNode t = node.getType();
        resolveOrFail(t,node);
        Expression init = node.getInitialExpression();
        node.setInitialValueExpression(transform(init));
    }

    public void visitProperty(PropertyNode node) {
        ClassNode t = node.getType();
        resolveOrFail(t,node);
        Statement code = node.getGetterBlock();
        if (code!=null) code.visit(this);
        code = node.getSetterBlock();
        if (code!=null) code.visit(this);
    }

    public void visitIfElse(IfStatement ifElse) {
        ifElse.setBooleanExpression((BooleanExpression) (transform(ifElse.getBooleanExpression())));
        super.visitIfElse(ifElse);
    }

    private void resolveOrFail(ClassNode type, String msg, ASTNode node) {
        if (resolve(type)) return;
        addError("unable to resolve class "+type.getName()+" "+msg,node);
    }

    private void resolveOrFail(ClassNode type, ASTNode node, boolean prefereImports) {
        if (prefereImports && resolveAliasFromModule(type)) return;
        resolveOrFail(type,node);
    }
    
    private void resolveOrFail(ClassNode type, ASTNode node) {
        resolveOrFail(type,"",node);
    }

    private boolean resolve(ClassNode type) {
        String name = type.getName();
        return resolve(type,true,true,true);
    }

    private boolean resolve(ClassNode type, boolean testModuleImports, boolean testDefaultImports, boolean testStaticInnerClasses) {
        if (type.isResolved()) return true;
        if (type.isArray()) {
            ClassNode element = type.getComponentType();
            boolean resolved = resolve(element,testModuleImports,testDefaultImports,testStaticInnerClasses);
            if (resolved) {
                ClassNode cn = element.makeArray();
                type.setRedirect(cn);
            }
            return resolved;
        }

        // test if vanilla name is current class name
        if (currentClass==type) return true;
        if (currentClass.getNameWithoutPackage().equals(type.getName())) {
            type.setRedirect(currentClass);
            return true;
        }

        return  resolveFromModule(type,testModuleImports) ||
                resolveFromCompileUnit(type) ||
                resovleFromDefaultImports(type,testDefaultImports) ||
                resolveFromStaticInnerClasses(type,testStaticInnerClasses) ||
                resolveFromClassCache(type) ||
                resolveToClass(type) ||
                resolveToScript(type);

    }

    private boolean resolveFromClassCache(ClassNode type) {
        String name = type.getName();
        Object val = cachedClasses.get(name);
        if (val==null || val==NO_CLASS){
            return false;
        } else {
            setClass(type,(Class) val);
            return true;
        }
    }

    // NOTE: copied from GroovyClassLoader
    private long getTimeStamp(Class cls) {
        Field field;
        Long o;
        try {
            field = cls.getField(Verifier.__TIMESTAMP);
            o = (Long) field.get(null);
        } catch (Exception e) {
            return Long.MAX_VALUE;
        }
        return o.longValue();
    }

    // NOTE: copied from GroovyClassLoader
    private boolean isSourceNewer(URL source, Class cls) {
        try {
            long lastMod;

            // Special handling for file:// protocol, as getLastModified() often reports
            // incorrect results (-1)
            if (source.getProtocol().equals("file")) {
                // Coerce the file URL to a File
                String path = source.getPath().replace('/', File.separatorChar).replace('|', ':');
                File file = new File(path);
                lastMod = file.lastModified();
            }
            else {
                lastMod = source.openConnection().getLastModified();
            }
            return lastMod > getTimeStamp(cls);            
        } catch (IOException e) {
            // if the stream can't be opened, let's keep the old reference
            return false;
        }
    }


    private boolean resolveToScript(ClassNode type) {
        String name = type.getName();
        if (cachedClasses.get(name)==NO_CLASS) return false;
        if (cachedClasses.get(name)==SCRIPT) cachedClasses.put(name,NO_CLASS);
        if (name.startsWith("java.")) return type.isResolved();
        //TODO: don't ignore inner static classes completly
        if (name.indexOf('$')!=-1) return type.isResolved();
        ModuleNode module = currentClass.getModule();
        if (module.hasPackageName() && name.indexOf('.')==-1) return type.isResolved();
        // try to find a script from classpath
        GroovyClassLoader gcl = compilationUnit.getClassLoader();
        URL url = null;
        try {
            url = gcl.getResourceLoader().loadGroovySource(name);
        } catch (MalformedURLException e) {
            // fall through and let the URL be null
        }
        if (url !=null) {
            if (type.isResolved()) {
                Class cls = type.getTypeClass();
                // if the file is not newer we don't want to recompile
                if (!isSourceNewer(url,cls)) return true;
                cachedClasses.remove(type.getName());
                type.setRedirect(null);
            }
            compilationUnit.addSource(url);
            currentClass.getCompileUnit().addClassNodeToCompile(type,url.toString());
            return true;
        }
        // type may be resolved through the classloader before
        return type.isResolved();
    }


    private boolean resolveFromStaticInnerClasses(ClassNode type, boolean testStaticInnerClasses) {
        // try to resolve a public static inner class' name
        testStaticInnerClasses &= type.hasPackageName();
        if (testStaticInnerClasses) {
            String name = type.getName();
            String replacedPointType = name;
            int lastPoint = replacedPointType.lastIndexOf('.');
            replacedPointType = new StringBuffer()
                .append(replacedPointType.substring(0, lastPoint))
                .append("$")
                .append(replacedPointType.substring(lastPoint + 1))
                .toString();
            type.setName(replacedPointType);
            if (resolve(type,false,false,true)) return true;
            type.setName(name);
        }
        return false;
    }

    private boolean resovleFromDefaultImports(ClassNode type, boolean testDefaultImports) {
        // test default imports
        testDefaultImports &= !type.hasPackageName();
        if (testDefaultImports) {
            for (int i = 0, size = DEFAULT_IMPORTS.length; i < size; i++) {
                String packagePrefix = DEFAULT_IMPORTS[i];
                String name = type.getName();
                String fqn = packagePrefix+name;
                type.setName(fqn);
                if (resolve(type,false,false,false)) return true;
                type.setName(name);
            }
            String name = type.getName();
            if (name.equals("BigInteger")) {
                type.setRedirect(ClassHelper.BigInteger_TYPE);
                return true;
            } else if (name.equals("BigDecimal")) {
                type.setRedirect(ClassHelper.BigDecimal_TYPE);
                return true;    
            }
        }
        return false;
    }

    private boolean resolveFromCompileUnit(ClassNode type) {
        // look into the compile unit if there is a class with that name
        CompileUnit compileUnit = currentClass.getCompileUnit();
        if (compileUnit == null) return false;
        ClassNode cuClass = compileUnit.getClass(type.getName());
        if (cuClass!=null) {
        	if (type!=cuClass) type.setRedirect(cuClass);
        	return true;
        }
        return false;
    }


    private void setClass(ClassNode n, Class cls) {
        ClassNode cn = ClassHelper.make(cls);
        n.setRedirect(cn);
    }

    private void ambigousClass(ClassNode type, ClassNode iType, String name, boolean resolved){
        if (resolved && !type.getName().equals(iType.getName())) {
            addError("reference to "+name+" is ambigous, both class "+type.getName()+" and "+iType.getName()+" match",type);
        } else {
            type.setRedirect(iType);
        }
    }
    
    private boolean resolveAliasFromModule(ClassNode type) {
        ModuleNode module = currentClass.getModule();
        if (module==null) return false;
        String name = type.getName();
        
        // check module node imports aliases
        // the while loop enables a check for inner classes which are not fully imported,
        // but visible as the surrounding class is imported and the inner class is public/protected static
        String pname = name;
        int index = name.length();
        /*
         * we have a name foo.bar and an import foo.foo. This means foo.bar is possibly
         * foo.foo.bar rather than foo.bar. This means to cut at the dot in foo.bar and
         * foo for import
         */
        while (true) {
            pname = name.substring(0,index);
            String aliased = module.getImport(pname);
            if (aliased!=null && !aliased.equals(name)) {
                if (pname.length()<name.length()){
                    aliased +=  name.substring(pname.length());
                }
                type.setName(aliased);
                if (resolve(type,true,true,true)) return true;
                type.setName(name);
            }
            index = pname.lastIndexOf('.');
            if (index==-1) break;
        }
         return false;
        
    }

    private boolean resolveFromModule(ClassNode type, boolean testModuleImports) {
        ModuleNode module = currentClass.getModule();
        if (module==null) return false;

        String name = type.getName();

        if (!type.hasPackageName() && module.hasPackageName()){
            type.setName(module.getPackageName()+name);
        }
        // look into the module node if there is a class with that name
        List moduleClasses = module.getClasses();
        for (Iterator iter = moduleClasses.iterator(); iter.hasNext();) {
            ClassNode mClass = (ClassNode) iter.next();
            if (mClass.getName().equals(type.getName())){
                if (mClass!=type) type.setRedirect(mClass);
                return true;
            }
        }
        type.setName(name);

        if (resolveAliasFromModule(type)) return true;

        if (testModuleImports) {
            boolean resolved = false;
            if (module.hasPackageName()) { 
                // check package this class is defined in
                type.setName(module.getPackageName()+name);
                resolved = resolve(type,false,false,false);
            }
            // check module node imports packages
            List packages = module.getImportPackages();
            ClassNode iType = ClassHelper.makeWithoutCaching(name);
            for (Iterator iter = packages.iterator(); iter.hasNext();) {
                String packagePrefix = (String) iter.next();
                String fqn = packagePrefix+name;
                iType.setName(fqn);
                if (resolve(iType,false,false,true)) {
                	ambigousClass(type,iType,name,resolved);
                    return true;
                }
                iType.setName(name);
            }
            if (!resolved) type.setName(name);
            return resolved;
        }
        return false;
    }

    private boolean resolveToClass(ClassNode type) {
        String name = type.getName();
        if (cachedClasses.get(name)==NO_CLASS) return false;
        if (currentClass.getModule().hasPackageName() && name.indexOf('.')==-1) return false;
        GroovyClassLoader loader  = compilationUnit.getClassLoader();
        Class cls = null;
        try {
            // NOTE: it's important to do no lookup against script files
            // here since the GroovyClassLoader would create a new
            // CompilationUnit
            cls = loader.loadClass(name,false,true);
        } catch (ClassNotFoundException cnfe) {
            cachedClasses.put(name,SCRIPT);
            return false;
        } 
        //TODO: the case of a NoClassDefFoundError needs a bit more research
        // a simple recompilation is not possible it seems. The current class
        // we are searching for is there, so we should mark that somehow. 
        // Basically the missing class needs to be completly compiled before
        // we can again search for the current name.
        /*catch (NoClassDefFoundError ncdfe) {
            cachedClasses.put(name,SCRIPT);
            return false;
        }*/
        if (cls==null) return false;
        cachedClasses.put(name,cls);
        setClass(type,cls);
        //NOTE: we return false here even if we found a class,
        //but we want to give a possible script a chance to recompile.
        //this can only be done if the loader was not the instance
        //defining the class.
        return cls.getClassLoader()==loader;
    }



    public Expression transform(Expression exp) {
        if (exp==null) return null;
        if (exp instanceof VariableExpression) {
            return transformVariableExpression((VariableExpression) exp);
        } else if (exp instanceof PropertyExpression) {
            return transformPropertyExpression((PropertyExpression) exp);
        } else if (exp instanceof DeclarationExpression) {
            return transformDeclarationExpression((DeclarationExpression)exp);
        } else if (exp instanceof BinaryExpression) {
            return transformBinaryExpression((BinaryExpression)exp);
        } else if (exp instanceof MethodCallExpression) {
            return transformMethodCallExpression((MethodCallExpression)exp);
        } else if (exp instanceof ClosureExpression) {
        	return transformClosureExpression((ClosureExpression) exp);
        } else if (exp instanceof ConstructorCallExpression) {
        	return transformConstructorCallExpression((ConstructorCallExpression) exp);
        } else {
            resolveOrFail(exp.getType(),exp);
            return exp.transformExpression(this);
        }
    }


    private String lookupClassName(PropertyExpression pe) {
        String name = "";
        for (Expression it = pe; it!=null; it = ((PropertyExpression)it).getObjectExpression()) {
            if (it instanceof VariableExpression) {
                VariableExpression ve = (VariableExpression) it;
                // stop at super and this
                if (ve==VariableExpression.SUPER_EXPRESSION || ve==VariableExpression.THIS_EXPRESSION) {
                    return null;
                }
                name= ve.getName()+"."+name;
                break;
            } 
            // anything other than PropertyExpressions, ClassExpression or
            // VariableExpressions will stop resolving
            else if (!(it instanceof PropertyExpression)) {
                return null;
            } else {
                PropertyExpression current = (PropertyExpression) it;
                String propertyPart = current.getProperty();
                // the class property stops resolving
                if (propertyPart.equals("class")) {
                    return null;
                }
                name = propertyPart+"."+name;
            }
        }
        if (name.length()>0) return name.substring(0,name.length()-1);
        return null;
    }

    // iterate from the inner most to the outer and check for classes
    // this check will ignore a .class property, for Exmaple Integer.class will be
    // a PropertyExpression with the ClassExpression of Integer as objectExpression
    // and class as property
    private Expression correctClassClassChain(PropertyExpression pe){
        LinkedList stack = new LinkedList();
        ClassExpression found = null;
        for (Expression it = pe; it!=null; it = ((PropertyExpression)it).getObjectExpression()) {
            if (it instanceof ClassExpression) {
                found = (ClassExpression) it;
                break;
            } else if (! (it instanceof PropertyExpression)) {
                return pe;
            }
            stack.addFirst(it);
        }
        if (found==null) return pe;

        if (stack.isEmpty()) return pe;
        Object stackElement = stack.removeFirst();
        if (!(stackElement instanceof PropertyExpression)) return pe;
        PropertyExpression classPropertyExpression = (PropertyExpression) stackElement;
        if (! classPropertyExpression.getProperty().equals("class")) return pe;

        if (stack.isEmpty()) return found;
        stackElement = stack.removeFirst();
        if (!(stackElement instanceof PropertyExpression)) return pe;
        PropertyExpression classPropertyExpressionContainer = (PropertyExpression) stackElement;

        classPropertyExpressionContainer.setObjectExpression(found);
        return pe;
    }

    protected Expression transformPropertyExpression(PropertyExpression pe) {
        boolean itlp = isTopLevelProperty;
        
        Expression objectExpression = pe.getObjectExpression();
        isTopLevelProperty = !(objectExpression instanceof PropertyExpression);
        objectExpression = transform(objectExpression);
        isTopLevelProperty = itlp;
        
        pe.setObjectExpression(objectExpression);
        
        String className = lookupClassName(pe);
        if (className!=null) {
            ClassNode type = ClassHelper.make(className);
            if (resolve(type)) return new ClassExpression(type);
        }  
        if (objectExpression instanceof ClassExpression ){
            // possibly a inner class
            ClassExpression ce = (ClassExpression) objectExpression;
            ClassNode type = ClassHelper.make(ce.getType().getName()+"$"+pe.getProperty());
            if (resolve(type,false,false,false)) return new ClassExpression(type);
        }
        if (isTopLevelProperty) return correctClassClassChain(pe);
        
        return pe;
    }
       
    protected Expression transformVariableExpression(VariableExpression ve) {
        if (ve.getName().equals("this"))  return VariableExpression.THIS_EXPRESSION;
        if (ve.getName().equals("super")) return VariableExpression.SUPER_EXPRESSION;
        Variable v = ve.getAccessedVariable();
        if (v instanceof DynamicVariable) {
            ClassNode t = ClassHelper.make(ve.getName());
            if (resolve(t)) {
                // the name is a type so remove it from the scoping
                // as it is only a classvariable, it is only in 
                // referencedClassVariables, but must be removed
                // for each parentscope too
                for (VariableScope scope = currentScope; scope!=null && !scope.isRoot(); scope = scope.getParent()) {
                    if (scope.isRoot()) break;
                    if (scope.getReferencedClassVariables().remove(ve.getName())==null) break;
                }
                return new ClassExpression(t);
            }
        }
        resolveOrFail(ve.getType(),ve);
        return ve;
    }
    
    protected Expression transformBinaryExpression(BinaryExpression be) {
        Expression left = transform(be.getLeftExpression());
        if (be.getOperation().getType()==Types.ASSIGNMENT_OPERATOR && left instanceof ClassExpression){
            ClassExpression  ce = (ClassExpression) left;
            addError("you tried to assign a value to "+ce.getType().getName(),be.getLeftExpression());
            return be;
        }
        Expression right = transform(be.getRightExpression());
        Expression ret = new BinaryExpression(left,be.getOperation(),right);
        ret.setSourcePosition(be);
        return ret;
    }
    
    protected Expression transformClosureExpression(ClosureExpression ce) {
        Parameter[] paras = ce.getParameters();
        if (paras!=null) {
	        for (int i=0; i<paras.length; i++) {
	            ClassNode t = paras[i].getType();
	            resolveOrFail(t,ce);
	        }
        }
        Statement code = ce.getCode();
        if (code!=null) code.visit(this);
    	ClosureExpression newCe= new ClosureExpression(paras,code);
        newCe.setVariableScope(ce.getVariableScope());
        newCe.setSourcePosition(ce);
        return newCe;
    }
    
    protected Expression transformConstructorCallExpression(ConstructorCallExpression cce){
    	ClassNode type = cce.getType();
    	resolveOrFail(type,cce);
    	Expression expr = cce.transformExpression(this);
        return expr;
    }
    
    protected Expression transformMethodCallExpression(MethodCallExpression mce) {
        Expression obj = mce.getObjectExpression();
        Expression newObject = transform(obj);
        Expression args = transform(mce.getArguments());
        /*if (! (newObject instanceof ClassExpression)) {
            obj=newObject;
        } else if (newObject!=obj) {
            return new StaticMethodCallExpression(newObject.getType(),mce.getMethod(),args);
        }
        return new MethodCallExpression(obj,mce.getMethod(),args);*/
        MethodCallExpression ret = new MethodCallExpression(newObject,mce.getMethod(),args);
        ret.setSafe(mce.isSafe());
        ret.setImplicitThis(mce.isImplicitThis());
        ret.setSpreadSafe(mce.isSpreadSafe());
        ret.setSourcePosition(mce);
        return ret;
    }
    
    protected Expression transformDeclarationExpression(DeclarationExpression de) {
        Expression oldLeft = de.getLeftExpression();
        Expression left = transform(oldLeft);
        if (left!=oldLeft){
            ClassExpression  ce = (ClassExpression) left;
            addError("you tried to assign a value to "+ce.getType().getName(),oldLeft);
            return de;
        }
        Expression right = transform(de.getRightExpression());
        if (right==de.getRightExpression()) return de;
        return new DeclarationExpression((VariableExpression) left,de.getOperation(),right);
    }
    
    public void visitAnnotations(AnnotatedNode node) {
        Map annotionMap = node.getAnnotations();
        if (annotionMap.isEmpty()) return;
        Iterator it = annotionMap.values().iterator(); 
        while (it.hasNext()) {
            AnnotationNode an = (AnnotationNode) it.next();
            //skip builtin properties
            if (an.isBuiltIn()) continue;
            ClassNode type = an.getClassNode();
            resolveOrFail(type,"unable to find class for annotation",an);
        }
    }

    public void visitClass(ClassNode node) {
        ClassNode oldNode = currentClass;
        currentClass = node;
        ClassNode sn = node.getUnresolvedSuperClass();
        if (sn!=null) resolveOrFail(sn,node,true);
        ClassNode[] interfaces = node.getInterfaces();
        for (int i=0; i<interfaces.length; i++) {
            resolveOrFail(interfaces[i],node,true);
        }        
        node.visitContents(this);
        currentClass = oldNode;        
    }
    
    public void visitReturnStatement(ReturnStatement statement) {
       statement.setExpression(transform(statement.getExpression()));
    }

    public void visitAssertStatement(AssertStatement as) {
        as.setBooleanExpression((BooleanExpression) (transform(as.getBooleanExpression())));
        as.setMessageExpression(transform(as.getMessageExpression()));
    }
    
    public void visitCaseStatement(CaseStatement statement) {
    	statement.setExpression(transform(statement.getExpression()));
    	statement.getCode().visit(this);
    }

    public void visitCatchStatement(CatchStatement cs) {
        resolveOrFail(cs.getExceptionType(),cs);
        super.visitCatchStatement(cs);
    }

    public void visitDoWhileLoop(DoWhileStatement loop) {
        loop.setBooleanExpression((BooleanExpression) (transform(loop.getBooleanExpression())));
        super.visitDoWhileLoop(loop);
    }
    
    public void visitForLoop(ForStatement forLoop) {
        forLoop.setCollectionExpression(transform(forLoop.getCollectionExpression()));
        resolveOrFail(forLoop.getVariableType(),forLoop);
        super.visitForLoop(forLoop);
    }
    
    public void visitSynchronizedStatement(SynchronizedStatement sync) {
        sync.setExpression(transform(sync.getExpression()));
        super.visitSynchronizedStatement(sync);
    }
    
    public void visitThrowStatement(ThrowStatement ts) {
        ts.setExpression(transform(ts.getExpression()));
    }
    
    public void visitWhileLoop(WhileStatement loop) {
    	loop.setBooleanExpression((BooleanExpression) transform(loop.getBooleanExpression()));
    	super.visitWhileLoop(loop);
    }
    
    public void visitExpressionStatement(ExpressionStatement es) {
        es.setExpression(transform(es.getExpression()));
    }
    
    private void addError(String msg, ASTNode expr) {
        int line = expr.getLineNumber();
        int col = expr.getColumnNumber();
        compilationUnit.getErrorCollector().addErrorAndContinue(
          new SyntaxErrorMessage(new SyntaxException(msg + '\n', line, col), source)
        );
    }
    
    public void visitBlockStatement(BlockStatement block) {
        VariableScope oldScope = currentScope;
        currentScope = block.getVariableScope();
        super.visitBlockStatement(block);
        currentScope = oldScope;
    }
}

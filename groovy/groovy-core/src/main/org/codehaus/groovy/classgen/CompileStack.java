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

package org.codehaus.groovy.classgen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.VariableScope;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * This class is a helper for AsmClassGenerator. It manages
 * different aspects of the code of a code block like 
 * handling labels, defining variables, and scopes. 
 * After a MethodNode is visited clear should be called, for 
 * initialization the method init should be used.
 * <p> 
 * Some Notes:
 * <ul>
 * <li> every push method will require a later pop call
 * <li> method parameters may define a category 2 variable, so
 *      don't ignore the type stored in the variable object
 * <li> the index of the variable may not be as assumed when
 *      the variable is a parameter of a method because the 
 *      parameter may be used in a closure, so don't ignore
 *      the stored variable index
 * <li> the names of temporary variables can be ignored. The names
 *      are only used for debugging and do not conflict with each 
 *      other or normal variables. For accessing the index of the
 *      variable must be used.
 * </ul>
 * 
 * 
 * @see org.codehaus.groovy.classgen.AsmClassGenerator
 * @author Jochen Theodorou
 */
public class CompileStack implements Opcodes {
    /**
     * @TODO remove optimization of this.foo -> this.@foo
     * 
     */
    
    // state flag
    private boolean clear=true;
    // current scope
    private VariableScope scope;
    // current label for continue
    private Label continueLabel;
    // current label for break
    private Label breakLabel;
    // available variables on stack
    private HashMap stackVariables = new HashMap();
    // index of the last variable on stack
    private int currentVariableIndex = 1;
    // index for the next variable on stack
    private int nextVariableIndex = 1;
    // currently temporary variables in use
    private LinkedList temporaryVariables = new LinkedList();
    // overall used variables for a method/constructor
    private LinkedList usedVariables = new LinkedList();
    // map containing named labels of parenting blocks
    private HashMap superBlockNamedLabels = new HashMap();
    // map containing named labels of current block
    private HashMap currentBlockNamedLabels = new HashMap();
    // list containing runnables representing a finally block
    // such a block is created by synchronized or finally and
    // must be called for break/continue/return
    private LinkedList finallyBlocks = new LinkedList();
    // a list of blocks already visiting. 
    private List visitedBlocks = new LinkedList();
    
    private Label thisStartLabel, thisEndLabel;

    // current class index
    private int currentClassIndex , currentMetaClassIndex;
    
    private MethodVisitor mv;
    private BytecodeHelper helper;
    
    // helper to handle different stack based variables    
    private LinkedList stateStack = new LinkedList();
    
    // defines the first variable index useable after
    // all parameters of a method 
    private int localVariableOffset;
    // this is used to store the goals for a "break foo" call
    // in a loop where foo is a label.
	private HashMap namedLoopBreakLabel = new HashMap();
	//this is used to store the goals for a "continue foo" call
    // in a loop where foo is a label.
	private HashMap namedLoopContinueLabel = new HashMap();
    private String className;
	
    private class StateStackElement {
        VariableScope _scope;
        Label _continueLabel;
        Label _breakLabel;
        Label _finallyLabel;
        int _lastVariableIndex;
        int _nextVariableIndex;
        HashMap _stackVariables;
        LinkedList _temporaryVariables = new LinkedList();
        LinkedList _usedVariables = new LinkedList();
        HashMap _superBlockNamedLabels;
        HashMap _currentBlockNamedLabels;
        LinkedList _finallyBlocks;
        
        StateStackElement() {
            _scope = CompileStack.this.scope;
            _continueLabel = CompileStack.this.continueLabel;
            _breakLabel = CompileStack.this.breakLabel;
            _lastVariableIndex = CompileStack.this.currentVariableIndex;
            _stackVariables = CompileStack.this.stackVariables;
            _temporaryVariables = CompileStack.this.temporaryVariables;
            _nextVariableIndex = nextVariableIndex;
            _superBlockNamedLabels = superBlockNamedLabels;
            _currentBlockNamedLabels = currentBlockNamedLabels;
            _finallyBlocks = finallyBlocks;
        }
    }
    
    private void pushState() {
        stateStack.add(new StateStackElement());
        stackVariables = new HashMap(stackVariables);
        finallyBlocks = new LinkedList(finallyBlocks);
    }
    
    private void popState() {
        if (stateStack.size()==0) {
             throw new GroovyBugError("Tried to do a pop on the compile stack without push.");
        }
        StateStackElement element = (StateStackElement) stateStack.removeLast();
        scope = element._scope;
        continueLabel = element._continueLabel;
        breakLabel = element._breakLabel;
        currentVariableIndex = element._lastVariableIndex;
        stackVariables = element._stackVariables;
        nextVariableIndex = element._nextVariableIndex;
        finallyBlocks = element._finallyBlocks;
    }
    
    public Label getContinueLabel() {
        return continueLabel;
    }

    public Label getBreakLabel() {
        return breakLabel;
    }

    public void removeVar(int tempIndex) {
        for (Iterator iter = temporaryVariables.iterator(); iter.hasNext();) {
            Variable element = (Variable) iter.next();
            if (element.getIndex()==tempIndex) {
                iter.remove();
                return;
            }
        }
        throw new GroovyBugError("CompileStack#removeVar: tried to remove a temporary variable with a non existent index");
    }

    private void setEndLabels(){
        Label endLabel = new Label();
        mv.visitLabel(endLabel);
        for (Iterator iter = stackVariables.values().iterator(); iter.hasNext();) {
            Variable var = (Variable) iter.next();
            var.setEndLabel(endLabel);
        }
        thisEndLabel = endLabel;
    }
    
    public void pop() {
        setEndLabels();
        popState();
    }

    public VariableScope getScope() {
        return scope;
    }

    /**
     * creates a temporary variable. 
     * 
     * @param var defines type and name
     * @param store defines if the toplevel argument of the stack should be stored
     * @return the index used for this temporary variable
     */
    public int defineTemporaryVariable(org.codehaus.groovy.ast.Variable var, boolean store) {
        return defineTemporaryVariable(var.getName(), var.getType(),store);
    }

    public Variable getVariable(String variableName ) {
        return getVariable(variableName,true);
    }
    
    public Variable getVariable(String variableName, boolean mustExist) {
        if (variableName.equals("this")) return Variable.THIS_VARIABLE;
        if (variableName.equals("super")) return Variable.SUPER_VARIABLE;
        Variable v = (Variable) stackVariables.get(variableName);
        if (v==null && mustExist)  throw new GroovyBugError("tried to get a variable with the name "+variableName+" as stack variable, but a variable with this name was not created");
        return v;
    }

    /**
     * creates a temporary variable. 
     * 
     * @param name defines type and name
     * @param store defines if the toplevel argument of the stack should be stored
     * @return the index used for this temporary variable
     */
    public int defineTemporaryVariable(String name,boolean store) {
        return defineTemporaryVariable(name, ClassHelper.DYNAMIC_TYPE,store);
    }

    /**
     * creates a temporary variable. 
     * 
     * @param name defines the name
     * @param node defines the node
     * @param store defines if the toplevel argument of the stack should be stored 
     * @return the index used for this temporary variable
     */
    public int defineTemporaryVariable(String name, ClassNode node, boolean store) {
        Variable answer = defineVar(name,node,false);
        temporaryVariables.add(answer);
        usedVariables.removeLast();
        
        if (store) mv.visitVarInsn(ASTORE, currentVariableIndex);
        
        return answer.getIndex();
    }
    
    private void resetVariableIndex(boolean isStatic) {
        if (!isStatic) {
            currentVariableIndex=1;
            nextVariableIndex=1;
        } else {
            currentVariableIndex=0;
            nextVariableIndex=0;
        }
    }
  
    /**
     * Clears the state of the class. This method should be called 
     * after a MethodNode is visited. Note that a call to init will
     * fail if clear is not called before
     */
    public void clear() {
        if (stateStack.size()>1) {
            int size = stateStack.size()-1;
            throw new GroovyBugError("the compile stack contains "+size+" more push instruction"+(size==1?"":"s")+" than pops.");
        }
        clear = true;
        // br experiment with local var table so debuggers can retrieve variable names
        if (true) {//AsmClassGenerator.CREATE_DEBUG_INFO) {
            if (thisEndLabel==null) setEndLabels();
            
            if (!scope.isInStaticContext()) {
                // write "this"
                mv.visitLocalVariable("this", className, null, thisStartLabel, thisEndLabel, 0);
            }
           
            for (Iterator iterator = usedVariables.iterator(); iterator.hasNext();) {
                Variable v = (Variable) iterator.next();
                String type = BytecodeHelper.getTypeDescription(v.getType());
                Label start = v.getStartLabel();
                Label end = v.getEndLabel();
                mv.visitLocalVariable(v.getName(), type, null, start, end, v.getIndex());
            }
        }
        pop();
        stackVariables.clear();
        usedVariables.clear();
        scope = null;
        mv=null;
        resetVariableIndex(false);
        superBlockNamedLabels.clear();
        currentBlockNamedLabels.clear();
        namedLoopBreakLabel.clear();
        namedLoopContinueLabel.clear();
        continueLabel=null;
        breakLabel=null;
        helper = null;
        thisStartLabel=null;
        thisEndLabel=null;
    }
    
    /**
     * initializes this class for a MethodNode. This method will
     * automatically define varibales for the method parameters
     * and will create references if needed. the created variables
     * can be get by getVariable
     * 
     */
    protected void init(VariableScope el, Parameter[] parameters, MethodVisitor mv, ClassNode cn) {
        if (!clear) throw new GroovyBugError("CompileStack#init called without calling clear before");
        clear=false;
        pushVariableScope(el);
        this.mv = mv;
        this.helper = new BytecodeHelper(mv);
        defineMethodVariables(parameters,el.isInStaticContext());
        this.className = BytecodeHelper.getTypeDescription(cn);
        currentClassIndex = -1; currentMetaClassIndex = -1;
    }

    /**
     * Causes the statestack to add an element and sets
     * the given scope as new current variable scope. Creates 
     * a element for the state stack so pop has to be called later
     */
    protected void pushVariableScope(VariableScope el) {
        pushState();
        scope = el;
        superBlockNamedLabels = new HashMap(superBlockNamedLabels);
        superBlockNamedLabels.putAll(currentBlockNamedLabels);
        currentBlockNamedLabels = new HashMap();
    }
    
    /**
     * Should be called when decending into a loop that defines
     * also a scope. Calls pushVariableScope and prepares labels 
     * for a loop structure. Creates a element for the state stack
     * so pop has to be called later 
     */
    protected void pushLoop(VariableScope el, String labelName) {
        pushVariableScope(el);
        initLoopLabels(labelName);
    }

    private void initLoopLabels(String labelName) {
        continueLabel = new Label();
        breakLabel = new Label();
        if (labelName!=null) {
        	namedLoopBreakLabel.put(labelName,breakLabel);
        	namedLoopContinueLabel.put(labelName,continueLabel);
        }
    }
    
    /**
     * Should be called when decending into a loop that does 
     * not define a scope. Creates a element for the state stack
     * so pop has to be called later
     */
    protected void pushLoop(String labelName) {
        pushState();
        initLoopLabels(labelName);
    }
    
    /**
     * Used for <code>break foo</code> inside a loop to end the
     * execution of the marked loop. This method will return the
     * break label of the loop if there is one found for the name.
     * If not, the current break label is returned.
     */
    protected Label getNamedBreakLabel(String name) {
    	Label label = getBreakLabel();
    	Label endLabel = null;
        if (name!=null) endLabel = (Label) namedLoopBreakLabel.get(name);
    	if (endLabel!=null) label = endLabel;
        return label;
    }
    
    /**
     * Used for <code>continue foo</code> inside a loop to continue
     * the execution of the marked loop. This method will return 
     * the break label of the loop if there is one found for the 
     * name. If not, getLabel is used.
     */
    protected Label getNamedContinueLabel(String name) {
    	Label label = getLabel(name);
    	Label endLabel = null;
        if (name!=null) endLabel = (Label) namedLoopContinueLabel.get(name);
    	if (endLabel!=null) label = endLabel;
        return label;
    }    
    
    /**
     * Creates a new break label and a element for the state stack
     * so pop has to be called later
     */
    protected Label pushSwitch(){
        pushState();
        breakLabel = new Label();
        return breakLabel;
    }
    
    /**
     * because a boolean Expression may not be evaluated completly
     * it is important to keep the registers clean
     */
    protected void pushBooleanExpression(){
        pushState();
    }
    
    private Variable defineVar(String name, ClassNode type, boolean methodParameterUsedInClosure) {
        makeNextVariableID(type);
        int index = currentVariableIndex;
        if (methodParameterUsedInClosure) {
            index = localVariableOffset++;
        }
        Variable answer = new Variable(index, type, name);
        usedVariables.add(answer);
        answer.setHolder(methodParameterUsedInClosure);
        return answer;
    }
    
    private void makeLocalVariablesOffset(Parameter[] paras,boolean isInStaticContext) {
        resetVariableIndex(isInStaticContext);
        
        for (int i = 0; i < paras.length; i++) {
            makeNextVariableID(paras[i].getType());
        }
        localVariableOffset = nextVariableIndex;
        
        resetVariableIndex(isInStaticContext);
    }
    
    private void defineMethodVariables(Parameter[] paras,boolean isInStaticContext) {
        Label startLabel  = new Label();
        thisStartLabel = startLabel;
        mv.visitLabel(startLabel);
        
        makeLocalVariablesOffset(paras,isInStaticContext);      
        
        boolean hasHolder = false;
        for (int i = 0; i < paras.length; i++) {
            String name = paras[i].getName();
            Variable answer;
            if (paras[i].isClosureSharedVariable()) {
                answer = defineVar(name, ClassHelper.getWrapper(paras[i].getType()), true);
                ClassNode type = paras[i].getType();
                helper.load(type,currentVariableIndex);
                helper.box(type);
                createReference(answer);
                hasHolder = true;
            } else {
                answer = defineVar(name,paras[i].getType(),false);
            }
            answer.setStartLabel(startLabel);
            stackVariables.put(name, answer);
        }
        
        if (hasHolder) {
            nextVariableIndex = localVariableOffset;
        }
    }

    private void createReference(Variable reference) {
        mv.visitTypeInsn(NEW, "groovy/lang/Reference");
        mv.visitInsn(DUP_X1);
        mv.visitInsn(SWAP);
        mv.visitMethodInsn(INVOKESPECIAL, "groovy/lang/Reference", "<init>", "(Ljava/lang/Object;)V");
        mv.visitVarInsn(ASTORE, reference.getIndex());
    }
    
    /**
     * Defines a new Variable using an AST variable.
     * @param initFromStack if true the last element of the 
     *                      stack will be used to initilize
     *                      the new variable. If false null
     *                      will be used.
     */
    public Variable defineVariable(org.codehaus.groovy.ast.Variable v, boolean initFromStack) {
        String name = v.getName();
        Variable answer = defineVar(name,v.getType(),false);
        if (v.isClosureSharedVariable()) answer.setHolder(true);
        stackVariables.put(name, answer);
        
        Label startLabel  = new Label();
        answer.setStartLabel(startLabel);
        if (answer.isHolder())  {
            if (!initFromStack) mv.visitInsn(ACONST_NULL);
            createReference(answer);
        } else {
            if (!initFromStack) mv.visitInsn(ACONST_NULL);
            mv.visitVarInsn(ASTORE, currentVariableIndex);            
        } 
        mv.visitLabel(startLabel);
        return answer;
    }

    /**
     * Returns true if a varibale is already defined
     */
    public boolean containsVariable(String name) {
        return stackVariables.containsKey(name);
    }
    
    /**
     * Calculates the index of the next free register stores ir
     * and sets the current variable index to the old value
     */
    private void makeNextVariableID(ClassNode type) {
        currentVariableIndex = nextVariableIndex;
        if (type==ClassHelper.long_TYPE || type==ClassHelper.double_TYPE) {
            nextVariableIndex++;
        }
        nextVariableIndex++;
    }
    
    /**
     * Returns the label for the given name 
     */
    public Label getLabel(String name) {
        if (name==null) return null;
        Label l = (Label) superBlockNamedLabels.get(name);
        if (l==null) l = createLocalLabel(name);
        return l;
    }
    
    /**
     * creates a new named label
     */
    public Label createLocalLabel(String name) {
        Label l = (Label) currentBlockNamedLabels.get(name);
        if (l==null) {
            l = new Label();
            currentBlockNamedLabels.put(name,l);
        }
        return l;
    }
    
    public int getCurrentClassIndex(){
        return currentClassIndex;
    }
    
    public void setCurrentClassIndex(int index){
        currentClassIndex=index;
    }
    
    public int getCurrentMetaClassIndex(){
        return currentMetaClassIndex;
    }
    
    public void setCurrentMetaClassIndex(int index){
        currentMetaClassIndex=index;
    }

    public void applyFinallyBlocks(Label label, boolean isBreakLabel) {
        // first find the state defining the label. That is the state
        // directly after the state not knowing this label. If no state
        // in the list knows that label, then the defining state is the
        // current state.
        StateStackElement result = null;
        for (ListIterator iter = stateStack.listIterator(stateStack.size()); iter.hasPrevious();) {
            StateStackElement element = (StateStackElement) iter.previous();
            if (!element._currentBlockNamedLabels.values().contains(label)) {
                if (isBreakLabel && element._breakLabel != label) {
                    result = element;
                    break;
                }
                if (!isBreakLabel && element._continueLabel != label) {
                    result = element;
                    break;
                }
            }
        }
        
        List blocksToRemove;
        if (result==null) {
            // all Blocks do know the label, so use all finally blocks
            blocksToRemove = Collections.EMPTY_LIST;
        } else {
            blocksToRemove = result._finallyBlocks;
        }
        
        ArrayList blocks = new ArrayList(finallyBlocks);
        blocks.removeAll(blocksToRemove);
        applyFinallyBlocks(blocks);
    }

    private void applyFinallyBlocks(List blocks) {
        for (Iterator iter = blocks.iterator(); iter.hasNext();) {
            Runnable block = (Runnable) iter.next();
            if (visitedBlocks.contains(block)) continue;
            visitedBlocks.add(block);
            block.run();
        }     
    }
    
    public void applyFinallyBlocks() {
        applyFinallyBlocks(finallyBlocks); 
    }

    public boolean hasFinallyBlocks() {
        return !finallyBlocks.isEmpty();
    }

    public void pushFinallyBlock(Runnable block) {
        finallyBlocks.addFirst(block);
        pushState();
    }

    public void popFinallyBlock() {
        popState();
        finallyBlocks.removeFirst();
    }
}

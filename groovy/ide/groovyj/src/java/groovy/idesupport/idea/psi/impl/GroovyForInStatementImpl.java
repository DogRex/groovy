package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyTokenTypes;
import groovy.idesupport.idea.psi.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 11:20:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyForInStatementImpl extends GroovyElementImpl implements GroovyForInStatement {
  public GroovyForInStatementImpl(final ASTNode node) {
    super(node);
  }

  public GroovyVarStatement getDeclarationStatement() {
    return (GroovyVarStatement)getNode().findChildByType(GroovyElementTypes.VAR_STATEMENT).getPsi();
  }

  public GroovyExpression getVariableExpression() {
    ASTNode child = getNode().getFirstChildNode();
    while (child != null) {
      if (child.getElementType() == GroovyTokenTypes.IN_KEYWORD) return null;
      if (GroovyElementTypes.EXPRESSIONS.isInSet(child.getElementType())) {
        return (GroovyExpression)child.getPsi();
      }
    }
    return null;
  }

  public GroovyExpression getCollectionExpression() {
    ASTNode child = ((ASTNode)getNode()).getFirstChildNode();
    boolean inPassed = false;
    while (child != null) {
      if (child.getElementType() == GroovyTokenTypes.IN_KEYWORD) {
        inPassed = true;
      }
      if (inPassed && GroovyElementTypes.EXPRESSIONS.isInSet(child.getElementType())) {
        return (GroovyExpression)child.getPsi();
      }
    }

    return null;
  }

  public GroovyStatement getBody() {
    ASTNode child = getNode().getFirstChildNode();
    boolean passedRParen = false;
    while (child != null) {
      if (child.getElementType() == GroovyTokenTypes.RPAR) {
        passedRParen = true;
      }
      else if (passedRParen && GroovyElementTypes.STATEMENTS.isInSet(child.getElementType())) {
        return (GroovyStatement)child.getPsi();
      }
    }

    return null;
  }

  public boolean processDeclarations(PsiScopeProcessor processor,
                                     PsiSubstitutor substitutor,
                                     PsiElement lastParent,
                                     PsiElement place) {
    if (lastParent != null) {
      final GroovyVarStatement statement = getDeclarationStatement();
      if (statement != null) return statement.processDeclarations(processor, substitutor, lastParent, place);
    }
    return true;
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSForInStatement(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyForInStatement";
  }
}

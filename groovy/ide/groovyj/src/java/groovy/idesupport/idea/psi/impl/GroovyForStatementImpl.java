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
 * Time: 10:17:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyForStatementImpl extends GroovyElementImpl implements GroovyForStatement {
  public GroovyForStatementImpl(final ASTNode node) {
    super(node);
  }

  public GroovyVarStatement getVarDeclaration() {
    final ASTNode node = ((ASTNode)getNode()).findChildByType(GroovyElementTypes.VAR_STATEMENT);
    return (GroovyVarStatement)(node != null ? ((com.intellij.psi.PsiElement)node.getPsi()) : null);
  }

  public GroovyExpression getInitialization() {
    ASTNode child = getNode().getFirstChildNode();
    while (child != null) {
      if (child.getElementType() == GroovyTokenTypes.SEMICOLON) return null;
      if (GroovyElementTypes.EXPRESSIONS.isInSet(child.getElementType())) return (GroovyExpression)child.getPsi();
      child = child.getTreeNext();
    }
    return null;
  }

  public GroovyExpression getCondition() {
    ASTNode child = getNode().getFirstChildNode();
    int semicolonCount = 0;
    while (child != null) {
      if (child.getElementType() == GroovyTokenTypes.SEMICOLON) {
        semicolonCount++;
        if (semicolonCount == 2) return null;
      }
      else if (semicolonCount == 1 && GroovyElementTypes.EXPRESSIONS.isInSet(child.getElementType())) {
        return (GroovyExpression)child.getPsi();
      }
      child = child.getTreeNext();
    }
    return null;
  }

  public GroovyExpression getUpdate() {
    ASTNode child = ((ASTNode)getNode()).getFirstChildNode();
    int semicolonCount = 0;
    while (child != null) {
      if (child.getElementType() == GroovyTokenTypes.SEMICOLON) {
        semicolonCount++;
      }
      else if (semicolonCount == 2 && GroovyElementTypes.EXPRESSIONS.isInSet(child.getElementType())) {
        return (GroovyExpression)child.getPsi();
      }
      child = child.getTreeNext();
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
      child = child.getTreeNext();
    }

    return null;
  }

  public boolean processDeclarations(PsiScopeProcessor processor,
                                     PsiSubstitutor substitutor,
                                     PsiElement lastParent,
                                     PsiElement place) {
    if (lastParent != null) {
      final GroovyVarStatement statement = getVarDeclaration();
      if (statement != null) return statement.processDeclarations(processor, substitutor, lastParent, place);
    }
    return true;
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSForStatement(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyForStatement";
  }
}

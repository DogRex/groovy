package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.psi.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 10:06:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyCatchBlockImpl extends GroovyElementImpl implements GroovyCatchBlock {
  public GroovyCatchBlockImpl(final ASTNode node) {
    super(node);
  }

  public GroovyParameter getParameter() {
    return (GroovyParameter)((ASTNode)getNode()).findChildByType(GroovyElementTypes.FORMAL_PARAMETER).getPsi();
  }

  public GroovyStatement getStatement() {
    final ASTNode[] nodes = getNode().findChildrenByFilter(GroovyElementTypes.STATEMENTS);
    return (GroovyStatement)(nodes.length == 1 ? ((com.intellij.psi.PsiElement)nodes[0].getPsi()) : null);
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSCatchBlock(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public boolean processDeclarations(PsiScopeProcessor processor,
                                     PsiSubstitutor substitutor,
                                     PsiElement lastParent,
                                     PsiElement place) {
    if (lastParent != null) {
      final GroovyParameter param = getParameter();
      if (param != null) return processor.execute(param, substitutor);
    }

    return true;
  }

  public String toString() {
    return "GroovyCatchBlock";
  }
}

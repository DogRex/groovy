package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.psi.*;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 31, 2005
 * Time: 12:02:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyCallExpressionImpl extends GroovyElementImpl implements GroovyCallExpression {
  public GroovyCallExpressionImpl(final ASTNode node) {
    super(node);
  }

  public GroovyExpression getMethodExpression() {
    final ASTNode[] nodes = ((ASTNode)getNode()).findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    return (GroovyReferenceExpression)(nodes.length == 1 ? ((com.intellij.psi.PsiElement)nodes[0].getPsi()) : null);
  }

  public GroovyArgumentList getArgumentList() {
    return (GroovyArgumentList)((ASTNode)getNode()).findChildByType(GroovyElementTypes.ARGUMENT_LIST).getPsi();
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSCallExpression(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyCallExpression";
  }
}

package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.psi.GroovyArgumentList;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyNewExpression;
import groovy.idesupport.idea.psi.GroovyReferenceExpression;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 11:57:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyNewExpressionImpl extends GroovyElementImpl implements GroovyNewExpression {
  public GroovyNewExpressionImpl(final ASTNode node) {
    super(node);
  }

  public GroovyReferenceExpression getExpression() {
    final ASTNode[] nodes = getNode().findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    return (GroovyReferenceExpression)(nodes.length == 1 ? ((com.intellij.psi.PsiElement)nodes[0].getPsi()) : null);
  }

  public GroovyArgumentList getArguments() {
    return (GroovyArgumentList)((ASTNode)getNode()).findChildByType(GroovyElementTypes.ARGUMENT_LIST).getPsi();
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSNewExpression(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyNewExpression";
  }
}

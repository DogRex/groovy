package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import groovy.idesupport.idea.psi.GroovyParenthesizedExpression;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 11:30:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyParenthesizedExpressionImpl extends GroovyElementImpl implements GroovyParenthesizedExpression{
  public GroovyParenthesizedExpressionImpl(final ASTNode node) {
    super(node);
  }

  public GroovyExpression getInnerExpression() {
    final ASTNode[] nodes = ((ASTNode)getNode()).findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    return (GroovyExpression)(nodes.length == 1 ? ((com.intellij.psi.PsiElement)nodes[0].getPsi()) : null);
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSParenthesizedExpression(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyParenthesizedExpression";
  }
}

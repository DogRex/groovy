package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyLiteralExpression;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 11:24:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyLiteralExpressionImpl extends GroovyElementImpl implements GroovyLiteralExpression{
  public GroovyLiteralExpressionImpl(final ASTNode node) {
    super(node);
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSLiteralExpression(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyLiteralExpression";
  }
}

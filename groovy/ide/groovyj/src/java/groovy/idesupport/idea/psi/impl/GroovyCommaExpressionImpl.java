package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.psi.GroovyCommaExpression;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 11:41:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyCommaExpressionImpl extends GroovyBinaryExpressionImpl implements GroovyCommaExpression {
  public GroovyCommaExpressionImpl(final ASTNode node) {
    super(node);
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSCommaExpression(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyCommaExpression";
  }
}

package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyFunction;
import groovy.idesupport.idea.psi.GroovyFunctionExpression;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 11:55:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyFunctionExpressionImpl extends GroovyFunctionImpl implements GroovyFunctionExpression {
  public GroovyFunctionExpressionImpl(final ASTNode node) {
    super(node);
  }

  public GroovyFunction getFunction() {
    return this;
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSFunctionExpression(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyFunctionExpression";
  }
}

package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.psi.GroovyAssignmentExpression;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 11:41:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyAssignmentExpressionImpl extends GroovyBinaryExpressionImpl implements GroovyAssignmentExpression {
  public GroovyAssignmentExpressionImpl(final ASTNode node) {
    super(node);
  }

  public String toString() {
    return "GroovyAssignmentExpression";
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSAssignmentExpression(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

}

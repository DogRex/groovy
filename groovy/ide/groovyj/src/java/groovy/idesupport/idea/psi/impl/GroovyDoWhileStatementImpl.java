package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.psi.GroovyDoWhileStatement;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import groovy.idesupport.idea.psi.GroovyStatement;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 10:15:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyDoWhileStatementImpl extends GroovyElementImpl implements GroovyDoWhileStatement {
  public GroovyDoWhileStatementImpl(final ASTNode node) {
    super(node);
  }

  public GroovyExpression getCondition() {
    final ASTNode[] nodes = ((ASTNode)getNode()).findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    return (GroovyExpression)(nodes.length == 1 ? nodes[0] : null);
  }

  public GroovyStatement getBody() {
    final ASTNode[] nodes = getNode().findChildrenByFilter(GroovyElementTypes.STATEMENTS);
    return (GroovyStatement)(nodes.length == 1 ? nodes[0] : null);
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSDoWhileStatement(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyDoWhileStatement";
  }
}

package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import groovy.idesupport.idea.psi.GroovyExpressionStatement;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 9:26:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyExpressionStatementImpl extends GroovyElementImpl implements GroovyExpressionStatement {
  public GroovyExpressionStatementImpl(final ASTNode node) {
    super(node);
  }

  public GroovyExpression getExpression() {
    final ASTNode[] expr = ((ASTNode)getNode()).findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    return (GroovyExpression)(expr.length == 1 ? ((com.intellij.psi.PsiElement)expr[0].getPsi()) : null);
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSExpressionStatement(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyExpressionStatement";
  }
}

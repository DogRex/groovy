package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyTokenTypes;
import groovy.idesupport.idea.psi.GroovyBinaryExpression;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 11:41:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyBinaryExpressionImpl extends GroovyElementImpl implements GroovyBinaryExpression {
  private static final Logger LOG = Logger.getInstance("#idea.idesupport.idea.psi.impl.GroovyBinaryExpressionImpl");

  public GroovyBinaryExpressionImpl(final ASTNode node) {
    super(node);
  }

  public GroovyExpression getLOperand() {
    final ASTNode[] nodes = getNode().findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    LOG.assertTrue(nodes.length >= 1);
    return (GroovyExpression)nodes[0].getPsi();
  }

  public GroovyExpression getROperand() {
    final ASTNode[] nodes = getNode().findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    return nodes.length == 2 ? (GroovyExpression)((com.intellij.psi.PsiElement)nodes[1].getPsi()) : null;
  }

  public IElementType getOperationSign() {
    final ASTNode[] nodes = ((ASTNode)getNode()).findChildrenByFilter(GroovyTokenTypes.OPERATIONS);
    LOG.assertTrue(nodes.length == 1);
    return nodes[0].getElementType();
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSBinaryEpxression(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyBinaryExpression";
  }
}

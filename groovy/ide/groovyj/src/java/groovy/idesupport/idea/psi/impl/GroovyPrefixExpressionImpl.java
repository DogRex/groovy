package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyTokenTypes;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import groovy.idesupport.idea.psi.GroovyPrefixExpression;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 11:52:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyPrefixExpressionImpl extends GroovyElementImpl implements GroovyPrefixExpression {
  public GroovyPrefixExpressionImpl(final ASTNode node) {
    super(node);
  }

  public GroovyExpression getExpression() {
    final ASTNode[] nodes = ((ASTNode)getNode()).findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    return (GroovyExpression)(nodes.length == 1 ? ((com.intellij.psi.PsiElement)nodes[0].getPsi()) : null);
  }

  public IElementType getOperationSign() {
    final ASTNode[] nodes = ((ASTNode)getNode()).findChildrenByFilter(GroovyTokenTypes.OPERATIONS);
    return nodes.length == 1 ? ((com.intellij.psi.tree.IElementType)nodes[0].getElementType()) : null;
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSPrefixExpression(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyPrefixExpression";
  }
}

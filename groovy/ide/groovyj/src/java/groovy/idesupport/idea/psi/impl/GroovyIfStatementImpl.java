package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import groovy.idesupport.idea.psi.GroovyIfStatement;
import groovy.idesupport.idea.psi.GroovyStatement;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 9:49:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyIfStatementImpl extends GroovyElementImpl implements GroovyIfStatement {
  public GroovyIfStatementImpl(final ASTNode node) {
    super(node);
  }

  public GroovyExpression getCondition() {
    final ASTNode[] condition = getNode().findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    return (GroovyExpression)(condition.length == 1 ? ((com.intellij.psi.PsiElement)condition[0].getPsi()) : null);
  }

  public GroovyStatement getThen() {
    final ASTNode[] nodes = ((ASTNode)getNode()).findChildrenByFilter(GroovyElementTypes.STATEMENTS);
    return (GroovyStatement)(nodes.length > 0 ? ((com.intellij.psi.PsiElement)nodes[0].getPsi()) : null);
  }

  public GroovyStatement getElse() {
    final ASTNode[] nodes = ((ASTNode)getNode()).findChildrenByFilter(GroovyElementTypes.STATEMENTS);
    return (GroovyStatement)(nodes.length == 2 ? ((com.intellij.psi.PsiElement)nodes[1].getPsi()) : null);
  }

  public void setThen(GroovyStatement statement) {
    throw new UnsupportedOperationException("TODO: implement");
  }

  public void setElse(GroovyStatement statement) {
    throw new UnsupportedOperationException("TODO: implement");
  }

  public void setCondition(GroovyExpression expr) {
    throw new UnsupportedOperationException("TODO: implement");
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSIfStatement(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyIfStatement";
  }
}

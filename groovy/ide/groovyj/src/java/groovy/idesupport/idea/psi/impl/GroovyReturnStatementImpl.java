package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import groovy.idesupport.idea.psi.GroovyReturnStatement;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 9:57:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyReturnStatementImpl extends GroovyElementImpl implements GroovyReturnStatement{
  public GroovyReturnStatementImpl(final ASTNode node) {
    super(node);
  }

  public GroovyExpression getExpression() {
    final ASTNode[] nodes = ((ASTNode)getNode()).findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    return (GroovyExpression)(nodes.length == 1 ? ((com.intellij.psi.PsiElement)nodes[0].getPsi()) : null);
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSReturnStatement(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyReturnStatement";
  }
}

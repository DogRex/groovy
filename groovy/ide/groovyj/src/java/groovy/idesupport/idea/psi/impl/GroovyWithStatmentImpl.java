package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import groovy.idesupport.idea.psi.GroovyStatement;
import groovy.idesupport.idea.psi.GroovyWithStatement;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 9:54:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyWithStatmentImpl extends GroovyElementImpl implements GroovyWithStatement{
  public GroovyWithStatmentImpl(final ASTNode node) {
    super(node);
  }

  public GroovyExpression getExpression() {
    final ASTNode[] nodes = ((ASTNode)getNode()).findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    return (GroovyExpression)(nodes.length == 1 ? ((com.intellij.psi.PsiElement)nodes[0].getPsi()) : null);
  }

  public GroovyStatement getStatement() {
    final ASTNode[] nodes = getNode().findChildrenByFilter(GroovyElementTypes.STATEMENTS);
    return (GroovyStatement)(nodes.length == 1 ? ((com.intellij.psi.PsiElement)nodes[0].getPsi()) : null);
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSWithStatement(this);
    }
    else {
      visitor.visitElement(this);
    }
  }  

  public String toString() {
    return "GroovyWithStatement";
  }
}

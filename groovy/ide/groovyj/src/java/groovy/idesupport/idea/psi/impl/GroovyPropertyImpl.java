package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyTokenTypes;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import groovy.idesupport.idea.psi.GroovyProperty;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 11:39:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyPropertyImpl extends GroovyElementImpl implements GroovyProperty {
  public GroovyPropertyImpl(final ASTNode node) {
    super(node);
  }

  public String getName() {
    final ASTNode node = ((ASTNode)getNode()).findChildByType(GroovyTokenTypes.IDENTIFIER);
    return node != null ? ((java.lang.String)node.getText()) : null;
  }

  public GroovyExpression getValue() {
    final ASTNode[] nodes = getNode().findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    return (GroovyExpression)(nodes.length == 1 ? ((com.intellij.psi.PsiElement)nodes[0].getPsi()) : null);
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSProperty(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyProperty";
  }
}

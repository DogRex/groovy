package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.psi.GroovyArgumentList;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 9:15:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyArgumentListImpl extends GroovyElementImpl implements GroovyArgumentList {
  public GroovyArgumentListImpl(final ASTNode node) {
    super(node);
  }

  public GroovyExpression[] getArguments() {
    final ASTNode[] nodes = ((ASTNode)getNode()).findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    final GroovyExpression[] exprs = new GroovyExpression[nodes.length];
    for (int i = 0; i < exprs.length; i++) {
      exprs[i] = (GroovyExpression)nodes[i].getPsi();
    }
    return exprs;
  }

  public String toString() {
    return "GroovyArgumentList";
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSArgumentList(this);
    }
    else {
      visitor.visitElement(this);
    }
  }
}

package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyObjectLiteralExpression;
import groovy.idesupport.idea.psi.GroovyProperty;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 11:36:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyObjectLiteralExpressionImpl extends GroovyElementImpl implements GroovyObjectLiteralExpression {
  private static final TokenSet PROPERTIES_FILTER = TokenSet.create(new IElementType[]{GroovyElementTypes.PROPERTY});

  public GroovyObjectLiteralExpressionImpl(final ASTNode node) {
    super(node);
  }

  public GroovyProperty[] getProperties() {
    final ASTNode[] nodes = ((ASTNode)getNode()).findChildrenByFilter(PROPERTIES_FILTER);
    final GroovyProperty[] properties = new GroovyProperty[nodes.length];
    for (int i = 0; i < properties.length; i++) {
      properties[i] = (GroovyProperty)nodes[i].getPsi();
    }
    return properties;
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSObjectLiteralExpression(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyObjectLiteralExpression";
  }
}

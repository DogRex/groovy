package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyParameter;
import groovy.idesupport.idea.psi.GroovyParameterList;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 8:41:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyParameterListImpl extends GroovyElementImpl implements GroovyParameterList {
  private static final TokenSet PARAMETER_FILTER = TokenSet.create(new IElementType[]{GroovyElementTypes.FORMAL_PARAMETER});

  public GroovyParameterListImpl(final ASTNode node) {
    super(node);
  }

  public GroovyParameter[] getParameters() {
    final ASTNode[] nodes = ((ASTNode)getNode()).findChildrenByFilter(PARAMETER_FILTER);
    final GroovyParameter[] params = new GroovyParameter[nodes.length];
    for (int i = 0; i < params.length; i++) {
      params[i] = (GroovyParameter)nodes[i].getPsi();
    }
    return params;
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSParameterList(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyParameterList";
  }
}

package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyTokenTypes;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import groovy.idesupport.idea.psi.GroovyIndexedPropertyAccessExpression;
import groovy.idesupport.idea.psi.GroovyReferenceExpression;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 11:59:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyIndexedPropertyAccessExpressionImpl extends GroovyElementImpl implements GroovyIndexedPropertyAccessExpression {
  public GroovyIndexedPropertyAccessExpressionImpl(final ASTNode node) {
    super(node);
  }

  public GroovyReferenceExpression getQualifier() {
    ASTNode child = ((ASTNode)getNode()).getFirstChildNode();
    while (child != null) {
      final IElementType type = child.getElementType();
      if (type == GroovyTokenTypes.LBRACKET) return null;
      if (GroovyElementTypes.EXPRESSIONS.isInSet(type)) return (GroovyReferenceExpression)child.getPsi();
      child = child.getTreeNext();
    }
    return null;
  }

  public GroovyExpression getIndexExpression() {
    ASTNode child = ((ASTNode)getNode()).getFirstChildNode();
    boolean bracketPassed = false;
    while (child != null) {
      final IElementType type = child.getElementType();
      if (type == GroovyTokenTypes.LBRACKET) {
        bracketPassed = true;
      }
      if (bracketPassed && GroovyElementTypes.EXPRESSIONS.isInSet(type)) return (GroovyReferenceExpression)child.getPsi();
      child = child.getTreeNext();
    }
    return null;
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSIndexedPropertyAccessExpression(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyIndexedPropertyAccessExpression";
  }
}

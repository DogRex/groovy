package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyTokenTypes;
import groovy.idesupport.idea.psi.GroovyConditionalExpression;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 11:47:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyConditionalExpressionImpl extends GroovyElementImpl implements GroovyConditionalExpression {
  public GroovyConditionalExpressionImpl(final ASTNode node) {
    super(node);
  }

  public GroovyExpression getCondition() {
    ASTNode child = ((ASTNode)getNode()).getFirstChildNode();
    while (child != null) {
      final IElementType type = child.getElementType();
      if (type == GroovyTokenTypes.QUEST) return null;
      if (GroovyElementTypes.EXPRESSIONS.isInSet(type)) return (GroovyExpression)child.getPsi();
      child = child.getTreeNext();
    }
    return null;
  }

  public GroovyExpression getThen() {
    boolean questPassed = false;
    ASTNode child = ((ASTNode)getNode()).getFirstChildNode();
    while (child != null) {
      final IElementType type = child.getElementType();
      if (type == GroovyTokenTypes.QUEST) {
        questPassed = true;
      }
      if (type == GroovyTokenTypes.COLON) {
        return null;
      }
      if (questPassed && GroovyElementTypes.EXPRESSIONS.isInSet(type)) {
        return (GroovyExpression)child.getPsi();
      }

      child = child.getTreeNext();
    }
    return null;
  }

  public GroovyExpression getElse() {
    boolean questPassed = false;
    boolean colonPassed = false;
    ASTNode child = getNode().getFirstChildNode();
    while (child != null) {
      final IElementType type = child.getElementType();
      if (type == GroovyTokenTypes.QUEST) {
        questPassed = true;
      }
      if (type == GroovyTokenTypes.COLON) {
        colonPassed = true;
      }
      if (questPassed && colonPassed && GroovyElementTypes.EXPRESSIONS.isInSet(type)) {
        return (GroovyExpression)child.getPsi();
      }

      child = child.getTreeNext();
    }
    return null;
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSConditionalExpression(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyConditionalExpression";
  }
}

package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyTokenTypes;
import groovy.idesupport.idea.psi.GroovyArrayLiteralExpression;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 11:32:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyArrayLiteralExpressionImpl extends GroovyElementImpl implements GroovyArrayLiteralExpression {
  public GroovyArrayLiteralExpressionImpl(final ASTNode node) {
    super(node);
  }

  public GroovyExpression[] getExpressions() {
    List result = new ArrayList();
    ASTNode child = getNode().getFirstChildNode();
    boolean wasExpression = false;
    while (child != null) {
      final IElementType type = child.getElementType();
      if (GroovyElementTypes.EXPRESSIONS.isInSet(type)) {
        result.add(child.getPsi());
        wasExpression = true;
      }
      else if (type == GroovyTokenTypes.COMMA) {
        if (wasExpression) {
          wasExpression = false;
        }
        else {
          result.add(null); // Skipped expression like [a,,b]
        }
      }
      child = child.getTreeNext();
    }

    return ((groovy.idesupport.idea.psi.GroovyExpression[])result.toArray(new GroovyExpression[result.size()]));
  }

  public String toString() {
    return "GroovyArrayLiteralExpression";
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSArrayLiteralExpression(this);
    }
    else {
      visitor.visitElement(this);
    }
  }
}

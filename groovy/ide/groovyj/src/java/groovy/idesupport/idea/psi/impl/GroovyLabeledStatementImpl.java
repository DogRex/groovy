package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyTokenTypes;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyLabeledStatement;
import groovy.idesupport.idea.psi.GroovyStatement;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 9:20:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyLabeledStatementImpl extends GroovyElementImpl implements GroovyLabeledStatement {
  public GroovyLabeledStatementImpl(final ASTNode node) {
    super(node);
  }

  public String getLabel() {
    return ((ASTNode)getNode()).findChildByType(GroovyTokenTypes.IDENTIFIER).getText();
  }

  public GroovyStatement getStatement() {
    final ASTNode[] statement = ((ASTNode)getNode()).findChildrenByFilter(GroovyElementTypes.STATEMENTS);
    return (GroovyStatement)(statement.length == 1 ? ((com.intellij.psi.PsiElement)statement[0].getPsi()) : null);
  }

  public GroovyStatement unlabel() {
    throw new UnsupportedOperationException("TODO: implement");
  }

  public GroovyLabeledStatement setLabel(String label) {
    throw new UnsupportedOperationException("TODO: implement");
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSLabeledStatement(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyLabeledStatement";
  }
}

package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyTokenTypes;
import groovy.idesupport.idea.psi.GroovyCaseClause;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import groovy.idesupport.idea.psi.GroovyStatement;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 10:11:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyCaseClauseImpl extends GroovyElementImpl implements GroovyCaseClause {
  public GroovyCaseClauseImpl(final ASTNode node) {
    super(node);
  }

  public boolean isDefault() {
    return getNode().findChildByType(GroovyTokenTypes.DEFAULT_KEYWORD) != null;
  }

  public GroovyExpression getCaseExpression() {
    if (isDefault()) return null;
    final ASTNode[] nodes = ((ASTNode)getNode()).findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    return (GroovyExpression)(nodes.length == 1 ? ((com.intellij.psi.PsiElement)nodes[0].getPsi()) : null);
  }

  public GroovyStatement[] getStatements() {
    final ASTNode[] nodes = getNode().findChildrenByFilter(GroovyElementTypes.STATEMENTS);
    final GroovyStatement[] statements = new GroovyStatement[nodes.length];
    for (int i = 0; i < statements.length; i++) {
      statements[i] = (GroovyStatement)nodes[i].getPsi();
    }
    return statements;
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSCaseClause(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyCaseClause";
  }
}

package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.psi.GroovyCaseClause;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import groovy.idesupport.idea.psi.GroovySwitchStatement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 10:08:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovySwitchStatementImpl extends GroovyElementImpl implements GroovySwitchStatement {
  private static final TokenSet CASE_CLAUSE_FILTER = TokenSet.create(new IElementType[]{GroovyElementTypes.CASE_CLAUSE});

  public GroovySwitchStatementImpl(final ASTNode node) {
    super(node);
  }

  public GroovyExpression getSwitchExpression() {
    final ASTNode[] nodes = getNode().findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    return (GroovyExpression)(nodes.length == 1 ? ((com.intellij.psi.PsiElement)nodes[0].getPsi()) : null);
  }

  public GroovyCaseClause[] getCaseClauses() {
    final ASTNode[] nodes = ((ASTNode)getNode()).findChildrenByFilter(CASE_CLAUSE_FILTER);
    final GroovyCaseClause[] clauses = new GroovyCaseClause[nodes.length];
    for (int i = 0; i < clauses.length; i++) {
      clauses[i] = (GroovyCaseClause)nodes[i].getPsi();
    }
    return clauses;
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSSwitchStatement(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovySwitchStatement";
  }
}

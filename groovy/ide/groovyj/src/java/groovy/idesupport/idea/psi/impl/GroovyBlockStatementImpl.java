package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.psi.GroovyBlockStatement;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyLabeledStatement;
import groovy.idesupport.idea.psi.GroovyStatement;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 9:17:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyBlockStatementImpl extends GroovyElementImpl implements GroovyBlockStatement {
  public GroovyBlockStatementImpl(final ASTNode node) {
    super(node);
  }

  public GroovyStatement[] getStatements() {
    final ASTNode[] nodes = getNode().findChildrenByFilter(GroovyElementTypes.STATEMENTS);
    final GroovyStatement[] statements = new GroovyStatement[nodes.length];
    for (int i = 0; i < statements.length; i++) {
      statements[i] = (GroovyStatement)nodes[i].getPsi();
    }
    return statements;
  }

  public GroovyLabeledStatement setLabel(String label) {
    throw new UnsupportedOperationException("TODO: implement");
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSBlock(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyBlockStatement";
  }
}

package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyEmptyStatement;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 9:48:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyEmptyStatementImpl extends GroovyElementImpl implements GroovyEmptyStatement {
  public GroovyEmptyStatementImpl(final ASTNode node) {
    super(node);
  }

  public String toString() {
    return "GroovyEmptyStatement";
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSEmptyStatement(this);
    }
    else {
      visitor.visitElement(this);
    }
  }
}

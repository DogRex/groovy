package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyTokenTypes;
import groovy.idesupport.idea.psi.GroovyContinueStatement;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyStatement;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 9:52:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyContinueStatementImpl extends GroovyElementImpl implements GroovyContinueStatement {
  public GroovyContinueStatementImpl(final ASTNode node) {
    super(node);
  }

  public String getLabel() {
    final ASTNode label = ((ASTNode)getNode()).findChildByType(GroovyTokenTypes.IDENTIFIER);
    return label != null ? ((java.lang.String)label.getText()) : null;
  }

  public GroovyStatement getStatementToContinue() {
    throw new UnsupportedOperationException("TODO: implement");
  }

  public String toString() {
    return "GroovyContinueStatement";
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSContinueStatement(this);
    }
    else {
      visitor.visitElement(this);
    }
  }
}

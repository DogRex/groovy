package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyTokenTypes;
import groovy.idesupport.idea.psi.GroovyCatchBlock;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyStatement;
import groovy.idesupport.idea.psi.GroovyTryStatement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 9:59:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyTryStatementImpl extends GroovyElementImpl implements GroovyTryStatement {
  public GroovyTryStatementImpl(final ASTNode node) {
    super(node);
  }

  public GroovyStatement getStatement() {
    ASTNode child = getNode().getFirstChildNode();
    while (child != null) {
      final IElementType type = child.getElementType();
      if (GroovyElementTypes.STATEMENTS.isInSet(type)) return (GroovyStatement)child.getPsi();
      if (type == GroovyTokenTypes.FINALLY_KEYWORD) break;
      child = child.getTreeNext();
    }
    return null;
  }

  public GroovyCatchBlock getCatchBlock() {
    return (GroovyCatchBlock)getNode().findChildByType(GroovyElementTypes.CATCH_BLOCK).getPsi();
  }

  public GroovyStatement getFinallyStatement() {
    ASTNode child = getNode().getFirstChildNode();
    boolean foundFinally = false;
    while (child != null) {
      final IElementType type = child.getElementType();
      if (foundFinally && GroovyElementTypes.STATEMENTS.isInSet(type)) return (GroovyStatement)child.getPsi();
      if (type == GroovyTokenTypes.FINALLY_KEYWORD) {
        foundFinally = true;
      }
      child = child.getTreeNext();
    }
    return null;
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSTryStatement(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyTryStatement";
  }
}

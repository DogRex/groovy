package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import groovy.idesupport.idea.psi.GroovyVarStatement;
import groovy.idesupport.idea.psi.GroovyVariable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 9:35:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyVarStatementImpl extends GroovyElementImpl implements GroovyVarStatement {
  private static final TokenSet VARIABLE_FILTER = TokenSet.create(new IElementType[] {GroovyElementTypes.VARIABLE});

  public GroovyVarStatementImpl(final ASTNode node) {
    super(node);
  }

  public GroovyVariable[] getVariables() {
    final ASTNode[] nodes = ((ASTNode)getNode()).findChildrenByFilter(VARIABLE_FILTER);
    GroovyVariable[] vars = new GroovyVariable[nodes.length];
    for (int i = 0; i < vars.length; i++) {
      vars[i] = (GroovyVariable)nodes[i].getPsi();
    }
    return vars;
  }

  public void declareVariable(String name, GroovyExpression initializer) {
    throw new UnsupportedOperationException("TODO: implement");
  }

  public String toString() {
    return "GroovyVarStatement";
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSVarStatement(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public boolean processDeclarations(PsiScopeProcessor processor,
                                     PsiSubstitutor substitutor,
                                     PsiElement lastParent,
                                     PsiElement place) {
    final GroovyVariable[] vars = getVariables();
    for (int i = 0; i < vars.length; i++) {
      GroovyVariable var = vars[i];
      if (!processor.execute(var, substitutor)) return false;
    }

    return true;
  }
}

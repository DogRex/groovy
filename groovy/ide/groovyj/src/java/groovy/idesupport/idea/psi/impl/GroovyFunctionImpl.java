package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyTokenTypes;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyFunction;
import groovy.idesupport.idea.psi.GroovyParameter;
import groovy.idesupport.idea.psi.GroovyParameterList;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.util.Icons;
import com.intellij.util.IncorrectOperationException;

import javax.swing.*;

public class GroovyFunctionImpl extends GroovyElementImpl implements GroovyFunction {
  public GroovyFunctionImpl(final ASTNode node) {
    super(node);
  }

  public GroovyParameterList getParameterList() {
    return (GroovyParameterList)((ASTNode)((ASTNode)getNode()).findChildByType(GroovyElementTypes.PARAMETER_LIST)).getPsi();
  }

  public PsiElement setName(String name) throws IncorrectOperationException {
    throw new IncorrectOperationException("not implemented");
  }

  public String getName() {
    final ASTNode name = findNameIdentifier();
    return name != null ? ((java.lang.String)name.getText()) : null;
  }

  private ASTNode findNameIdentifier() {
    return ((ASTNode)getNode()).findChildByType(GroovyTokenTypes.IDENTIFIER);
  }

  public int getTextOffset() {
    final ASTNode name = findNameIdentifier();
    return name != null ? name.getStartOffset() : super.getTextOffset();
  }

  public boolean processDeclarations(PsiScopeProcessor processor,
                                     PsiSubstitutor substitutor,
                                     PsiElement lastParent,
                                     PsiElement place) {
    if (lastParent != null) {
      final GroovyParameter[] params = getParameterList().getParameters();
      for (int i = 0; i < params.length; i++) {
        GroovyParameter param = params[i];
        if (!processor.execute(param, substitutor)) return false;
      }
    }

    return processor.execute(this, substitutor);
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSFunctionDeclaration(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyFunction";
  }

  public Icon getIcon(int flags) {
    return Icons.METHOD_ICON;
  }
}

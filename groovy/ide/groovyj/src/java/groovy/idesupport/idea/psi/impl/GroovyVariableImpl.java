package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyTokenTypes;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import groovy.idesupport.idea.psi.GroovyType;
import groovy.idesupport.idea.psi.GroovyVariable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.Icons;
import com.intellij.util.IncorrectOperationException;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 8:47:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyVariableImpl extends GroovyElementImpl implements GroovyVariable {
  public GroovyVariableImpl(final ASTNode node) {
    super(node);
  }

  public boolean hasInitializer() {
    return getInitializer() != null;
  }

  public GroovyExpression getInitializer() {
    final ASTNode[] initializer = ((ASTNode)getNode()).findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    return (GroovyExpression)(initializer.length == 1 ? ((com.intellij.psi.PsiElement)initializer[0].getPsi()) : null);
  }

  public String getName() {
    final ASTNode nameIdentifier = getNode().findChildByType(GroovyTokenTypes.IDENTIFIER);
    return nameIdentifier.getText();
  }

  public void setInitializer(GroovyExpression expr) throws IncorrectOperationException {
    throw new UnsupportedOperationException("TODO: implement");
  }

  public GroovyType getType() {
    return null; //TODO
  }

  public PsiElement setName(String name) throws IncorrectOperationException {
    final ASTNode nameElement = JSChangeUtil.createNameIdentifier(getProject(), name);
    getNode().replaceChild(((ASTNode)getNode()).getFirstChildNode(), nameElement);
    return this;
  }

  public String toString() {
    return "GroovyVariable";
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSVariable(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public Icon getIcon(int flags) {
    return Icons.VARIABLE_ICON;
  }
}

package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyFunction;
import groovy.idesupport.idea.psi.GroovyParameter;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.Icons;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 9:12:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyParameterImpl extends GroovyVariableImpl implements GroovyParameter {
  public GroovyParameterImpl(final ASTNode node) {
    super(node);
  }

  public GroovyFunction getDeclaringFunction() {
    return (GroovyFunction)((ASTNode)getNode().getTreeParent()).getTreeParent().getPsi();
  }

  public String toString() {
    return "GroovyParameter";
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSParameter(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public Icon getIcon(int flags) {
    return Icons.PARAMETER_ICON;
  }
}

package groovy.idesupport.idea.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import groovy.idesupport.idea.GroovySupportLoader;
import groovy.idesupport.idea.psi.GroovyElement;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 8:23:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyElementImpl extends ASTWrapperPsiElement implements GroovyElement {
  public GroovyElementImpl(final ASTNode node) {
    super(node);
  }

  public Language getLanguage() {
    return GroovySupportLoader.GROOVY.getLanguage();
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSElement(this);
    }
    else {
      visitor.visitElement(this);
    }
  }
}

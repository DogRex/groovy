package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.psi.*;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 31, 2005
 * Time: 12:02:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyEmbeddedContentImpl extends GroovyElementImpl {
  public GroovyEmbeddedContentImpl(final ASTNode node) {
    super(node);
  }

  public String toString() {
    return "JSEmbeddedContent";
  }
}

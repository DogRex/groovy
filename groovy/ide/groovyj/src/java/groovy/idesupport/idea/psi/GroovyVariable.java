package groovy.idesupport.idea.psi;

import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 6:43:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyVariable extends PsiNamedElement, GroovyElement {
  boolean hasInitializer();
  GroovyExpression getInitializer();
  void setInitializer(GroovyExpression expr) throws IncorrectOperationException;
  GroovyType getType();
}

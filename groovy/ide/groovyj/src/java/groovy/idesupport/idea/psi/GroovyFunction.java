package groovy.idesupport.idea.psi;

import com.intellij.psi.PsiNamedElement;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 6:40:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyFunction extends PsiNamedElement, GroovyElement {
  GroovyParameterList getParameterList();
}

package groovy.idesupport.idea.psi;

import com.intellij.psi.PsiReference;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:26:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyReferenceExpression extends GroovyExpression, PsiReference {
  GroovyExpression getQualifier();
  String getReferencedName();
}

package groovy.idesupport.idea.psi;

import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:41:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyPostfixExpression extends GroovyExpression {
  GroovyExpression getExpression();
  IElementType getOperationSign();
}

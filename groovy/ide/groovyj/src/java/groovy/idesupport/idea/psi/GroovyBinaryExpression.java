package groovy.idesupport.idea.psi;

import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 6:49:21 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyBinaryExpression extends GroovyExpression {
  GroovyExpression getLOperand();
  GroovyExpression getROperand();
  IElementType getOperationSign();
}

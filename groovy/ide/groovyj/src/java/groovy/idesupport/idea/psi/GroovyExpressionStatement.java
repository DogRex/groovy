package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 9:27:17 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyExpressionStatement extends GroovyStatement {
  GroovyExpression getExpression();
}

package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:01:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyReturnStatement extends GroovyStatement {
  GroovyExpression getExpression();
}

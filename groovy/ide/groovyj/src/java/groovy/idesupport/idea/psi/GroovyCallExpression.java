package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:46:07 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyCallExpression extends GroovyExpression {
  GroovyExpression getMethodExpression();
  GroovyArgumentList getArgumentList();
}

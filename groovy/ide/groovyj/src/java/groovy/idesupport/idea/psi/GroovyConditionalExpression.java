package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:40:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyConditionalExpression extends GroovyExpression {
  GroovyExpression getCondition();
  GroovyExpression getThen();
  GroovyExpression getElse();
}

package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:30:30 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyParenthesizedExpression extends GroovyExpression{
  GroovyExpression getInnerExpression();
}

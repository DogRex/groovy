package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:30:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyArrayLiteralExpression extends GroovyExpression {
  /**
   * @return nulls stand in the returned array for skipped values. This for [,1,] array of 3 elements to be returned with first and last
   * elements nulled
   */
  GroovyExpression[] getExpressions();
}

package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:44:26 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyNewExpression extends GroovyExpression {
  GroovyReferenceExpression getExpression();
  GroovyArgumentList getArguments();
}

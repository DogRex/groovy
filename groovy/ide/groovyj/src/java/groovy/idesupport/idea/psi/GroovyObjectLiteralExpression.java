package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:38:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyObjectLiteralExpression extends GroovyExpression {
  GroovyProperty[] getProperties();
}

package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:45:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyIndexedPropertyAccessExpression extends GroovyExpression {
  GroovyReferenceExpression getQualifier();
  GroovyExpression getIndexExpression();
}

package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:44:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyArgumentList extends GroovyElement {
  GroovyExpression[] getArguments();
}

package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 6:42:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyParameterList extends GroovyElement {
  GroovyParameter[] getParameters();
}

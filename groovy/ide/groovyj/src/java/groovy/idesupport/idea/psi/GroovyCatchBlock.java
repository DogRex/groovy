package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:04:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyCatchBlock extends GroovyElement {
  GroovyParameter getParameter();
  GroovyStatement getStatement();
}

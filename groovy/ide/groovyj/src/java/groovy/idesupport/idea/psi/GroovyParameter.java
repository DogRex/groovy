package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 6:43:02 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyParameter extends GroovyVariable {
  GroovyFunction getDeclaringFunction();
}

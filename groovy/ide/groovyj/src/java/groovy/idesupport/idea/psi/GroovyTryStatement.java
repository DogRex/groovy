package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:03:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyTryStatement extends GroovyStatement {
  GroovyStatement getStatement();
  GroovyCatchBlock getCatchBlock();
  GroovyStatement getFinallyStatement();
}
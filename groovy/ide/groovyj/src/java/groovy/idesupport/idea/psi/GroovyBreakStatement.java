package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 6:58:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyBreakStatement extends GroovyStatement {
  String getLabel();
  GroovyStatement getStatementToBreak();
}

package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:25:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyDoWhileStatement extends GroovyLoopStatement {
  GroovyExpression getCondition();
}

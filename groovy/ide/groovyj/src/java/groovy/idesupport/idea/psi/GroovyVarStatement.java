package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 6:55:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyVarStatement extends GroovyStatement {
  GroovyVariable[] getVariables();
  void declareVariable(String name, GroovyExpression initializer);
}

package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:21:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyForStatement extends GroovyLoopStatement {
  GroovyVarStatement getVarDeclaration();
  GroovyExpression getInitialization();

  GroovyExpression getCondition();
  GroovyExpression getUpdate();
}

package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:20:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyForInStatement extends GroovyLoopStatement {
  GroovyVarStatement getDeclarationStatement();
  GroovyExpression getVariableExpression();

  GroovyExpression getCollectionExpression();
}

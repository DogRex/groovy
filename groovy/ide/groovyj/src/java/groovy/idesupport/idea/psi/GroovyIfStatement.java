package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 6:56:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyIfStatement extends GroovyStatement{
  GroovyExpression getCondition();
  GroovyStatement getThen();
  GroovyStatement getElse();

  void setThen(GroovyStatement statement);
  void setElse(GroovyStatement statement);
  void setCondition(GroovyExpression expr);
}

package groovy.idesupport.idea.psi;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 30, 2005
 * Time: 7:16:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroovyCaseClause extends GroovyElement {
  boolean isDefault();
  GroovyExpression getCaseExpression();
  GroovyStatement[] getStatements();
}

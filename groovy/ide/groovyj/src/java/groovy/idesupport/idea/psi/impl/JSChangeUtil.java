package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import groovy.idesupport.idea.GroovySupportLoader;
import groovy.idesupport.idea.psi.GroovyExpressionStatement;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 31, 2005
 * Time: 7:56:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class JSChangeUtil {
  private static ParserDefinition ourParserDefinition = GroovySupportLoader.GROOVY.getLanguage().getParserDefinition();

  public static ASTNode createNameIdentifier(Project project, String name) {
    final PsiFile dummyFile = ourParserDefinition.createFile(project, "dummy." + ((String)GroovySupportLoader.GROOVY.getDefaultExtension()), name + ";");
    final GroovyExpressionStatement expressionStatement = (GroovyExpressionStatement)dummyFile.getFirstChild();
    final GroovyReferenceExpressionImpl refExpression = (GroovyReferenceExpressionImpl)expressionStatement.getFirstChild();
    return ((ASTNode)refExpression.getNode()).getFirstChildNode();
  }
}

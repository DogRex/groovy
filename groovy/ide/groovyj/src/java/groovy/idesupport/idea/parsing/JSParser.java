package groovy.idesupport.idea.parsing;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 28, 2005
 * Time: 12:29:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class JSParser implements PsiParser {
  public ASTNode parse(IElementType root, PsiBuilder builder) {
    final PsiBuilder.Marker rootMarker = builder.mark();
    while (!builder.eof()) {
      StatementParsing.parseSourceElement(builder);
    }
    rootMarker.done(root);
    return builder.getTreeBuilt();
  }
}

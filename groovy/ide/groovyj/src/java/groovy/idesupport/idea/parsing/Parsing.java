package groovy.idesupport.idea.parsing;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 28, 2005
 * Time: 7:03:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class Parsing {
  protected Parsing() { }

  protected static void checkMatches(final PsiBuilder builder, final IElementType token, final String message) {
    if (builder.getTokenType() == token) {
      builder.advanceLexer();
    }
    else {
      builder.error(message);
    }
  }
}

package groovy.idesupport.idea;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 31, 2005
 * Time: 9:34:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class JSWordsScanner extends DefaultWordsScanner {
  public JSWordsScanner() {
    super(new _JavaScriptLexer(), TokenSet.create(new IElementType[] {GroovyTokenTypes.IDENTIFIER}),
          GroovyTokenTypes.COMMENTS, TokenSet.create(new IElementType[] {GroovyTokenTypes.STRING_LITERAL}));
  }
}

package groovy.idesupport.idea;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IChameleonElementType;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 27, 2005
 * Time: 6:38:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class JSChameleonElementType extends IChameleonElementType {
  public JSChameleonElementType(String debugName) {
    super(debugName, GroovySupportLoader.GROOVY.getLanguage());
  }

  public String toString() {
    return "JS:" + super.toString();
  }
}

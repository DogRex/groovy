package groovy.idesupport.idea;

import com.intellij.psi.tree.IElementType;

public class GroovyElementType extends IElementType {
  public GroovyElementType(String debugName) {
    super(debugName, GroovySupportLoader.GROOVY.getLanguage());
  }

  public String toString() {
    return "Groovy: " + super.toString();
  }
}

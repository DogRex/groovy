package groovy.idesupport.idea;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;

public class GroovyBraceMatcher implements PairedBraceMatcher {
  private static final BracePair[] PAIRS = new BracePair[] {
    new BracePair('(',GroovyTokenTypes.LPAR, ')', GroovyTokenTypes.RPAR, false),
    new BracePair('[',GroovyTokenTypes.LBRACKET, ']', GroovyTokenTypes.RBRACKET, false),
    new BracePair('{',GroovyTokenTypes.LBRACE, '}', GroovyTokenTypes.RBRACE, true)
  };

  public BracePair[] getPairs() {
    return PAIRS;
  }
}

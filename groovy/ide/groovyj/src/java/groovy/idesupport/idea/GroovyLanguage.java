package groovy.idesupport.idea;

import com.intellij.codeFormatting.PseudoTextBuilder;
import com.intellij.lang.Language;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.folding.FoldingBuilder;
import groovy.idesupport.idea.folding.GroovyFoldingBuilder;
import groovy.idesupport.idea.formatter.GroovyPseudoTextBuilder;
import groovy.idesupport.idea.validation.GroovyAnnotatingVisitor;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;

public class GroovyLanguage extends Language {
  private static final GroovyAnnotatingVisitor ANNOTATOR = new GroovyAnnotatingVisitor();

  public GroovyLanguage() {
    super("Groovy");
  }

  public ParserDefinition getParserDefinition() {
    return new groovy.idesupport.idea.GroovyParserDefinition();
  }

  public SyntaxHighlighter getSyntaxHighlighter(Project project) {
    return new GroovyHighlighter();
  }

  public WordsScanner getWordsScanner() {
    return new JSWordsScanner();
  }

  public boolean mayHaveReferences(IElementType token) {
    return token == GroovyElementTypes.REFERENCE_EXPRESSION;
  }

  public FoldingBuilder getFoldingBuilder() {
    return new GroovyFoldingBuilder();
  }

  public PseudoTextBuilder getFormatter() {
    return new GroovyPseudoTextBuilder();
  }

  public PairedBraceMatcher getPairedBraceMatcher() {
    return new GroovyBraceMatcher();
  }

  public Annotator getAnnotator() {
    return ANNOTATOR;
  }
}

package groovy.idesupport.idea.formatter;

import com.intellij.codeFormatting.PseudoText;
import com.intellij.codeFormatting.PseudoTextBuilder;
import com.intellij.codeFormatting.TreeBasedPseudoText;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;

public class GroovyPseudoTextBuilder implements PseudoTextBuilder {
  private TreeBasedPseudoText myText;

  public PseudoText build(Project project,
                          CodeStyleSettings settings,
                          PsiElement source) {
    final ASTNode rootNode = source.getContainingFile().getNode();
    myText = new TreeBasedPseudoText(rootNode);
    new GroovySpaceProcessor(myText, settings).process(rootNode);
    new GroovyAlignmentProcessor(myText, settings).process(rootNode);
    return myText;
  }
}

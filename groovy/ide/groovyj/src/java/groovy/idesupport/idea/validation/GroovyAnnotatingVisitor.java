package groovy.idesupport.idea.validation;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import groovy.idesupport.idea.GroovyTokenTypes;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyReferenceExpression;
import com.intellij.psi.PsiElement;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Feb 3, 2005
 * Time: 3:11:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyAnnotatingVisitor extends GroovyElementVisitor implements Annotator {
  private AnnotationHolder myHolder;

  public void annotate(PsiElement psiElement, AnnotationHolder holder) {
    myHolder = holder;
    psiElement.accept(this);
  }

  public void visitJSReferenceExpression(final GroovyReferenceExpression node) {
    if (node.getQualifier() == null) {
      if (node.resolve() == null) {
        ASTNode nameIdentifier = node.getNode().findChildByType(GroovyTokenTypes.IDENTIFIER);
        final Annotation ann = myHolder.createErrorAnnotation(nameIdentifier, "cannot resolve '" + ((String)nameIdentifier.getText()) + "'");
        ann.setHighlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL);
      }
    }
  }
}

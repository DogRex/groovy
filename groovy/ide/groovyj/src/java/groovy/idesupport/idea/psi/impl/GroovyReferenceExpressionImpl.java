package groovy.idesupport.idea.psi.impl;

import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyTokenTypes;
import groovy.idesupport.idea.psi.GroovyElementVisitor;
import groovy.idesupport.idea.psi.GroovyExpression;
import groovy.idesupport.idea.psi.GroovyReferenceExpression;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.util.IncorrectOperationException;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: implement all reference stuff...
 */
public class GroovyReferenceExpressionImpl extends GroovyElementImpl implements GroovyReferenceExpression{
  public GroovyReferenceExpressionImpl(final ASTNode node) {
    super(node);
  }

  public GroovyExpression getQualifier() {
    final ASTNode[] nodes = getNode().findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    return (GroovyExpression)(nodes.length == 1 ? ((com.intellij.psi.PsiElement)nodes[0].getPsi()) : null);
  }

  public String getReferencedName() {
    return ((ASTNode)getNameElement()).getText();
  }

  public PsiElement getElement() {
    return this;
  }

  public PsiReference getReference() {
    return this;
  }

  public TextRange getRangeInElement() {
    final ASTNode nameElement = getNameElement();
    final int startOffset = nameElement != null ? nameElement.getStartOffset() : ((com.intellij.openapi.util.TextRange)((com.intellij.lang.ASTNode)getNode()).getTextRange()).getEndOffset();
    return new TextRange(startOffset - getNode().getStartOffset(), getTextLength());
  }

  private ASTNode getNameElement() {
    return getNode().findChildByType(GroovyTokenTypes.IDENTIFIER);
  }

  private static class ResolveProcessor implements PsiScopeProcessor {
    private String myName;
    private PsiElement myResult = null;

    public ResolveProcessor(final String name) {
      myName = name;
    }

    public PsiElement getResult() {
      return myResult;
    }

    public boolean execute(PsiElement element, PsiSubstitutor substitutor) {
      if (element instanceof PsiNamedElement) {
        if (myName.equals(((PsiNamedElement)element).getName())) {
          myResult = element;
          return false;
        }
      }

      return true;
    }

    public  java.lang.Object getHint(java.lang.Class hintClass) {
      return null;
    }

    public void handleEvent(Event event, Object associated) {
    }
  }

  private static class VariantsProcessor implements PsiScopeProcessor {
    private List myNames = new ArrayList();

    public VariantsProcessor() {
    }

    public PsiElement[] getResult() {
      return ((com.intellij.psi.PsiElement[])myNames.toArray(new PsiElement[myNames.size()]));
    }

    public boolean execute(PsiElement element, PsiSubstitutor substitutor) {
      if (element instanceof PsiNamedElement) {
        myNames.add(element);
      }

      return true;
    }

    public  java.lang.Object getHint(java.lang.Class hintClass) {
      return null;
    }

    public void handleEvent(Event event, Object associated) {
    }
  }

  private PsiElement treeWalkUp(PsiScopeProcessor processor, PsiElement elt, PsiElement lastParent) {
    if (elt == null) return null;

    PsiElement cur = elt;
    do {
      if (!cur.processDeclarations(processor, PsiSubstitutor.EMPTY, cur == elt ? lastParent : null, this)) {
        if (processor instanceof ResolveProcessor) {
          return ((ResolveProcessor)processor).getResult();
        }
      }
      cur = cur.getPrevSibling();
    } while (cur != null);

    return treeWalkUp(processor, ((PsiElement)elt.getContext()), elt);
  }

  public PsiElement resolve() {
    if (getQualifier() != null) {
      return null; // TODO?
    }
    return treeWalkUp(new ResolveProcessor(getReferencedName()), this, this);
  }

  public String getCanonicalText() {
    return null;
  }

  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    final ASTNode nameElement = JSChangeUtil.createNameIdentifier(((com.intellij.openapi.project.Project)getProject()), newElementName);
    getNode().replaceChild(getNameElement(), nameElement);
    return this;
  }

  public PsiElement bindToElement(PsiElement element) throws IncorrectOperationException {
    final ASTNode nameElement = JSChangeUtil.createNameIdentifier(getProject(), ((PsiNamedElement)element).getName());
    getNode().replaceChild(getNameElement(), nameElement);
    return this;
  }

  public boolean isReferenceTo(PsiElement element) {
    if (element instanceof PsiNamedElement) {
      if (getReferencedName().equals(((PsiNamedElement)element).getName())) return resolve() == element;
    }
    return false;
  }

  public Object[] getVariants() {
    if (getQualifier() != null) {
      return new Object[0]; // TODO?
    }

    final VariantsProcessor processor = new VariantsProcessor();
    treeWalkUp(processor, this, this);
    return processor.getResult();
  }

  public boolean isSoft() {
    return false;
  }

  public void accept(PsiElementVisitor visitor) {
    if (visitor instanceof GroovyElementVisitor) {
      ((GroovyElementVisitor)visitor).visitJSReferenceExpression(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  public String toString() {
    return "GroovyReferenceExpression";
  }
}

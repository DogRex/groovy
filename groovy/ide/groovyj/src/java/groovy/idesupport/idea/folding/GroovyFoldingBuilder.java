package groovy.idesupport.idea.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyTokenTypes;
import com.intellij.openapi.editor.Document;

import java.util.ArrayList;
import java.util.List;

public class GroovyFoldingBuilder implements FoldingBuilder {

  public FoldingDescriptor[] buildFoldRegions(ASTNode node, Document document) {
    List descriptors = new ArrayList();
    appendDescriptors(node, document, descriptors);
    return ((com.intellij.lang.folding.FoldingDescriptor[])descriptors.toArray(new FoldingDescriptor[descriptors.size()]));
  }

  private void appendDescriptors(final ASTNode node, final Document document, final List descriptors) {
    if (node.getElementType() == GroovyElementTypes.BLOCK) {
      descriptors.add(new FoldingDescriptor(node, node.getTextRange()));
    }
    else if (node.getElementType() == GroovyTokenTypes.DOC_COMMENT) {
      descriptors.add(new FoldingDescriptor(node, node.getTextRange()));
    }
    else if (node.getElementType() == GroovyTokenTypes.C_STYLE_COMMENT) {
      descriptors.add(new FoldingDescriptor(node, node.getTextRange()));
    }

    ASTNode child = node.getFirstChildNode();
    while (child != null) {
      appendDescriptors(child, document, descriptors);
      child = child.getTreeNext();
    }
  }

  public String getPlaceholderText(ASTNode node) {
    if (node.getElementType() == GroovyTokenTypes.DOC_COMMENT) {
      return "/**...*/";
    }
    else if (node.getElementType() == GroovyTokenTypes.C_STYLE_COMMENT) {
      return "/*...*/";
    }
    else if (node.getElementType() == GroovyElementTypes.BLOCK) {
      return "{...}";
    }
    return null;
  }

  public boolean isCollapsedByDefault(ASTNode node) {
    return node.getElementType() == GroovyTokenTypes.DOC_COMMENT;
  }
}

package groovy.idesupport.idea.formatter;

import com.intellij.codeFormatting.*;
import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyNodeVisitor;
import groovy.idesupport.idea.GroovyTokenTypes;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.IElementType;

import java.util.Stack;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Feb 1, 2005
 * Time: 10:40:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyAlignmentProcessor extends GroovyNodeVisitor {
  private TreeBasedPseudoText myText;
  private CodeStyleSettings mySettings;
  private Stack myAlignments = new Stack();
  private final static Alignment TOP = new Alignment(null, new IndentProperty(0, IndentProperty.Type.NONE, 0));

  public GroovyAlignmentProcessor(final TreeBasedPseudoText text, final CodeStyleSettings settings) {
    myText = text;
    mySettings = settings;
  }

  public void process(ASTNode node) {
    visit(node);
  }

  public void visitElement(final ASTNode node) {
    if (node.getElementType() == TokenType.NEW_LINE_INDENT) {
      setAlignment(node, getStatementAlignment());
    }
    else {
      ASTNode child = node.getFirstChildNode();
      while (child != null) {
        process(child);
        child = child.getTreeNext();
      }
    }
  }

  public void visitStatement(final ASTNode node) {
    setAlignment(node, getStatementAlignment());
    super.visitStatement(node);
  }

  public void visitWhileStatement(final ASTNode node) {
    setAlignment(node, getStatementAlignment());

    final ASTNode[] condition = node.findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    for (int i = 0; i < condition.length; i++) { // Should actually execute not more than once.
      process(condition[i]);
    }

    final ASTNode[] statements = node.findChildrenByFilter(GroovyElementTypes.STATEMENTS);
    if (statements.length == 0) return;
    processDependentStatement(statements[0]);
  }

  public void visitIfStatement(final ASTNode node) {
    setAlignment(node, getStatementAlignment());

    final ASTNode[] condition = node.findChildrenByFilter(GroovyElementTypes.EXPRESSIONS);
    for (int i = 0; i < condition.length; i++) { // Should actually execute not more than once.
      process(condition[i]);
    }

    final ASTNode[] statements = node.findChildrenByFilter(GroovyElementTypes.STATEMENTS);
    ASTNode thenNode = statements.length >= 1 ? statements[0] : null;
    ASTNode elseNode = statements.length == 2 ? statements[1] : null;
    ASTNode elseKeyword = node.findChildByType(GroovyTokenTypes.ELSE_KEYWORD);

    if (thenNode == null) return;

    if (elseKeyword != null) {
      breakLineBefore(elseKeyword, mySettings.ELSE_ON_NEW_LINE);
      setAlignment(elseKeyword, getStatementAlignment());
    }

    processDependentStatement(thenNode);

    if (elseNode != null) {
      final IElementType elseType = elseNode.getElementType();
      if (elseType  == GroovyElementTypes.IF_STATEMENT && mySettings.SPECIAL_ELSE_IF_TREATMENT) {
        process(elseNode);
      }
      else {
        processDependentStatement(elseNode);
      }
    }
  }

  private void processDependentStatement(final ASTNode stmt) {
    if (stmt.getElementType() != GroovyElementTypes.BLOCK) {
      myAlignments.push(createContinuationAlignment(getStatementAlignment()));
      process(stmt);
      myAlignments.pop();
    }
    else {
      breakLineBefore(stmt, !mySettings.KEEP_CONTROL_STATEMENT_IN_ONE_LINE);
      process(stmt);
    }
  }

  private Alignment createContinuationAlignment(final Alignment parentAlign) {
    return new Alignment(parentAlign, new IndentProperty(1, IndentProperty.Type.CONTINUATION, 0));
  }


  public void visitBlock(final ASTNode node) {
    final Alignment parentAlign = getStatementAlignment();

    myAlignments.push(new Alignment(parentAlign,
                                    new IndentProperty(mySettings.BRACE_STYLE == CodeStyleSettings.NEXT_LINE_SHIFTED2 ? 2 : 1,
                                                       IndentProperty.Type.NORMAL, 0), Alignment.Type.ALIGN));
    ASTNode child = node.getFirstChildNode();
    while (child != null) {
      process(child);
      child = child.getTreeNext();
    }
    myAlignments.pop();

    final ASTNode lbrace = node.findChildByType(GroovyTokenTypes.LBRACE);
    final IndentProperty.Type indentType =
        mySettings.BRACE_STYLE == CodeStyleSettings.NEXT_LINE_SHIFTED ||
        mySettings.BRACE_STYLE == CodeStyleSettings.NEXT_LINE_SHIFTED2
            ? IndentProperty.Type.NORMAL
            : IndentProperty.Type.NONE;

    final IndentProperty braceIndent = new IndentProperty(1, indentType, 0);
    final Alignment braceAlign = new Alignment(parentAlign, braceIndent, Alignment.Type.INHERIT);
    setAlignment(lbrace, braceAlign);

    boolean keepBlockInOneLine = mySettings.KEEP_SIMPLE_BLOCKS_IN_ONE_LINE && !node.textContains('\n');

    breakLineBefore(lbrace, !keepBlockInOneLine && mySettings.BRACE_STYLE != CodeStyleSettings.END_OF_LINE);

    breakLineBefore(lbrace.getTreeNext(), !keepBlockInOneLine);
    final ASTNode rbrace = node.findChildByType(GroovyTokenTypes.RBRACE);
    if (rbrace != null) {
      breakLineBefore(rbrace, !keepBlockInOneLine);
      setAlignment(rbrace, braceAlign);
    }
  }

  private void setAlignment(final ASTNode node, final Alignment align) {
    getElement(node).setAlignment(align);
  }

  private MutablePseudoTextElement getElement(final ASTNode node) {
    final MutablePseudoTextElement elt = myText.getFirstElement(node);
    SpaceProperty space = elt.getSpace();
    if (space == null) {
      space = new SpaceProperty(0, 1, 0, mySettings.KEEP_BLANK_LINES_IN_CODE + 1);
      elt.setSpaceProperty(space);
    }
    return elt;
  }

  private Alignment getStatementAlignment() {
    if (myAlignments.size() == 0) return TOP;
    return ((com.intellij.codeFormatting.Alignment)myAlignments.peek());
  }

  private void breakLineBefore(final ASTNode node, boolean newline) {
    final MutablePseudoTextElement elt = getElement(node);
    elt.setSpaceProperty(((SpaceProperty)elt.getSpace()).copyAndChangeMinLineFeeds(newline ? 1 : 0));
    if (!mySettings.KEEP_LINE_BREAKS && !newline) {
      elt.setSpaceProperty(((SpaceProperty)elt.getSpace()).copyAndChangeMaxLineFeeds(0));
    }
  }
}

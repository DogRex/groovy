package groovy.idesupport.idea.formatter;

import com.intellij.codeFormatting.MutablePseudoTextElement;
import com.intellij.codeFormatting.SpaceProperty;
import com.intellij.codeFormatting.TreeBasedPseudoText;
import com.intellij.lang.ASTNode;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyNodeVisitor;
import groovy.idesupport.idea.GroovyTokenTypes;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Feb 1, 2005
 * Time: 7:10:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovySpaceProcessor extends GroovyNodeVisitor {
  private TreeBasedPseudoText myText;
  private ASTNode child1;
  private IElementType type1;
  private ASTNode child2;
  private IElementType type2;
  private CodeStyleSettings mySettings;

  public GroovySpaceProcessor(final TreeBasedPseudoText text, CodeStyleSettings settings) {
    myText = text;
    mySettings = settings;
  }


  public void process(ASTNode node) {
    child1 = TreeBasedPseudoText.skipToNextNonSpace(node.getFirstChildNode());

    while (child1 != null) {
      child2 = TreeBasedPseudoText.skipToNextNonSpace(((ASTNode)child1.getTreeNext()));
      if (child2 == null) break;

      type1 = child1.getElementType();
      type2 = child2.getElementType();
      visit(node);

      child1 = child2;
    }

    ASTNode child = node.getFirstChildNode();
    while (child != null) {
      process(child);
      child = child.getTreeNext();
    }
  }

  public void visitParameterList(final ASTNode node) {
    if (type1 == GroovyTokenTypes.LPAR && type2 == GroovyTokenTypes.RPAR) {
      setSingleSpace(false);
    }
    else if (type1 == GroovyTokenTypes.LPAR || type2 == GroovyTokenTypes.RPAR) {
      setSingleSpace(mySettings.SPACE_WITHIN_METHOD_PARENTHESES);
    }
    else if (type1 == GroovyTokenTypes.COMMA) {
      setSingleSpace(mySettings.SPACE_AFTER_COMMA);
    }
    else if (type2 == GroovyTokenTypes.COMMA) {
      setSingleSpace(mySettings.SPACE_BEFORE_COMMA);
    }
  }

  public void visitFunctionDeclaration(final ASTNode node) {
    if (type1 == GroovyTokenTypes.FUNCTION_KEYWORD && type2 == GroovyTokenTypes.IDENTIFIER) {
      setSingleSpace(true);
    }
    else if (type1 == GroovyTokenTypes.IDENTIFIER && type2 == GroovyElementTypes.PARAMETER_LIST) {
      setSingleSpace(mySettings.SPACE_BEFORE_METHOD_PARENTHESES);
    }
    else if (type1 == GroovyElementTypes.PARAMETER_LIST) {
      setSingleSpace(mySettings.SPACE_BEFORE_METHOD_LBRACE);
    }
  }

  public void visitFunctionExpression(final ASTNode node) {
    visitFunctionDeclaration(node);
  }

  public void visitReferenceExpression(final ASTNode node) {
    setSingleSpace(false); // a.b should not have spaces before and after dot
  }

  public void visitIfStatement(final ASTNode node) {
    if (type1 == GroovyTokenTypes.IF_KEYWORD && type2 == GroovyTokenTypes.LPAR) {
      setSingleSpace(mySettings.SPACE_BEFORE_IF_PARENTHESES);
    }
    else if (type1 == GroovyTokenTypes.LPAR || type2 == GroovyTokenTypes.RPAR) {
      setSingleSpace(mySettings.SPACE_WITHIN_IF_PARENTHESES);
    }
    else if (type1 == GroovyTokenTypes.RPAR) {
      setSingleSpace(mySettings.SPACE_BEFORE_IF_LBRACE);
    }
    else if (type1 == GroovyTokenTypes.ELSE_KEYWORD || type2 == GroovyTokenTypes.ELSE_KEYWORD) {
      setSingleSpace(true);
    }
  }

  public void visitCallExpression(final ASTNode node) {
    if (type2 == GroovyElementTypes.ARGUMENT_LIST) {
      setSingleSpace(mySettings.SPACE_BEFORE_METHOD_CALL_PARENTHESES);
    }
  }

  public void visitNewExpression(final ASTNode node) {
    if (type1 == GroovyTokenTypes.NEW_KEYWORD) {
      setSingleSpace(true);
    }
    else if (type2 == GroovyElementTypes.ARGUMENT_LIST) {
      setSingleSpace(mySettings.SPACE_BEFORE_METHOD_CALL_PARENTHESES);
    }
  }

  public void visitForStatement(final ASTNode node) {
    if (type1 == GroovyTokenTypes.SEMICOLON) {
      setSingleSpace(true);
    }
    else if (type2 == GroovyTokenTypes.SEMICOLON) {
      setSingleSpace(mySettings.SPACE_BEFORE_SEMICOLON);
    }

    if (type1 == GroovyTokenTypes.FOR_KEYWORD && type2 == GroovyTokenTypes.LPAR) {
      setSingleSpace(mySettings.SPACE_BEFORE_FOR_PARENTHESES);
    }
    else if (type1 == GroovyTokenTypes.RPAR) {
      setSingleSpace(mySettings.SPACE_BEFORE_FOR_LBRACE);
    }
    else if (type1 == GroovyTokenTypes.LPAR || type2 == GroovyTokenTypes.RPAR) {
      setSingleSpace(mySettings.SPACE_WITHIN_FOR_PARENTHESES);
    }
  }

  public void visitForInStatement(final ASTNode node) {
    if (type1 == GroovyTokenTypes.VAR_KEYWORD || type2 == GroovyTokenTypes.VAR_KEYWORD) {
      setSingleSpace(true);
    }
    else if (type1 == GroovyTokenTypes.FOR_KEYWORD && type2 == GroovyTokenTypes.LPAR) {
      setSingleSpace(mySettings.SPACE_BEFORE_FOR_PARENTHESES);
    }
    else if (type1 == GroovyTokenTypes.RPAR) {
      setSingleSpace(mySettings.SPACE_BEFORE_FOR_LBRACE);
    }
    else if (type1 == GroovyTokenTypes.LPAR || type2 == GroovyTokenTypes.RPAR) {
      setSingleSpace(mySettings.SPACE_WITHIN_FOR_PARENTHESES);
    }
  }

  public void visitWhileStatement(final ASTNode node) {
    if (type1 == GroovyTokenTypes.WHILE_KEYWORD && type2 == GroovyTokenTypes.LPAR) {
      setSingleSpace(mySettings.SPACE_BEFORE_WHILE_PARENTHESES);
    }
    else if (type1 == GroovyTokenTypes.RPAR) {
      setSingleSpace(mySettings.SPACE_BEFORE_WHILE_LBRACE);
    }
    else if (type1 == GroovyTokenTypes.LPAR || type2 == GroovyTokenTypes.RPAR) {
      setSingleSpace(mySettings.SPACE_WITHIN_WHILE_PARENTHESES);
    }
  }

  public void visitWithStatement(final ASTNode node) {
    if (type1 == GroovyTokenTypes.WITH_KEYWORD && type2 == GroovyTokenTypes.LPAR) {
      setSingleSpace(mySettings.SPACE_BEFORE_WHILE_PARENTHESES);
    }
    else if (type1 == GroovyTokenTypes.RPAR) {
      setSingleSpace(mySettings.SPACE_BEFORE_WHILE_LBRACE);
    }
    else if (type1 == GroovyTokenTypes.LPAR || type2 == GroovyTokenTypes.RPAR) {
      setSingleSpace(mySettings.SPACE_WITHIN_WHILE_PARENTHESES);
    }
  }

  public void visitSwitchStatement(final ASTNode node) {
    if (type1 == GroovyTokenTypes.SWITCH_KEYWORD && type2 == GroovyTokenTypes.LPAR) {
      setSingleSpace(mySettings.SPACE_BEFORE_SWITCH_PARENTHESES);
    }
    else if (type1 == GroovyTokenTypes.RPAR) {
      setSingleSpace(mySettings.SPACE_BEFORE_SWITCH_LBRACE);
    }
    else if (type1 == GroovyTokenTypes.LPAR || type2 == GroovyTokenTypes.RPAR) {
      setSingleSpace(mySettings.SPACE_WITHIN_SWITCH_PARENTHESES);
    }
  }

  public void visitArgumentList(final ASTNode node) {
    if (type1 == GroovyTokenTypes.LPAR || type2 == GroovyTokenTypes.RPAR) {
      setSingleSpace(false);
    }
    else if (type1 == GroovyTokenTypes.COMMA) {
      setSingleSpace(mySettings.SPACE_AFTER_COMMA);
    }
    else if (type2 == GroovyTokenTypes.COMMA) {
      setSingleSpace(mySettings.SPACE_BEFORE_COMMA);
    }
  }

  public void visitStatement(final ASTNode node) {
    if (type2 == GroovyTokenTypes.SEMICOLON) {
      setSingleSpace(false);
    }
  }

  public void visitVarStatement(final ASTNode node) {
    super.visitVarStatement(node);
    if (type1 == GroovyTokenTypes.VAR_KEYWORD) {
      setSingleSpace(true);
    }
  }

  public void visitVariable(final ASTNode node) {
    if (type1 == GroovyTokenTypes.EQ || type2 == GroovyTokenTypes.EQ) { // Initializer
      setSingleSpace(mySettings.SPACE_AROUND_ASSIGNMENT_OPERATORS);
    }
  }

  public void visitBinaryEpxression(final ASTNode node) {
    IElementType opSign = null;
    if (GroovyTokenTypes.OPERATIONS.isInSet(type1)) {
      opSign = type1;
    }
    else if (GroovyTokenTypes.OPERATIONS.isInSet(type2)) {
      opSign = type2;
    }

    if (opSign != null) {
      setSingleSpace(getSpaceAroundOption(opSign));
    }
  }

  private boolean getSpaceAroundOption(final IElementType opSign) {
    boolean option = false;
    if (GroovyTokenTypes.ADDITIVE_OPERATIONS.isInSet(opSign)) {
      option = mySettings.SPACE_AROUND_ADDITIVE_OPERATORS;
    }
    else if (GroovyTokenTypes.MULTIPLICATIVE_OPERATIONS.isInSet(opSign)) {
      option = mySettings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS;
    }
    else if (GroovyTokenTypes.ASSIGNMENT_OPERATIONS.isInSet(opSign)) {
      option = mySettings.SPACE_AROUND_ASSIGNMENT_OPERATORS;
    }
    else if (GroovyTokenTypes.EQUALITY_OPERATIONS.isInSet(opSign)) {
      option = mySettings.SPACE_AROUND_EQUALITY_OPERATORS;
    }
    else if (GroovyTokenTypes.RELATIONAL_OPERATIONS.isInSet(opSign)) {
      option = mySettings.SPACE_AROUND_RELATIONAL_OPERATORS;
    }
    else if (GroovyTokenTypes.SHIFT_OPERATIONS.isInSet(opSign)) {
      option = mySettings.SPACE_AROUND_BITWISE_OPERATORS;
    }
    else if (opSign == GroovyTokenTypes.ANDAND || opSign == GroovyTokenTypes.OROR) {
      option = mySettings.SPACE_AROUND_LOGICAL_OPERATORS;
    }
    else if (opSign == GroovyTokenTypes.OR || opSign == GroovyTokenTypes.AND || opSign == GroovyTokenTypes.XOR) {
      option = mySettings.SPACE_AROUND_BITWISE_OPERATORS;
    }
    return option;
  }

  private void setSingleSpace(boolean needSpace) {
    final int spaces = needSpace ? 1 : 0;
    final MutablePseudoTextElement elt = myText.getFirstElement(child2);
    if (elt == null) return;
    
    SpaceProperty space = elt.getSpace();
    if (space == null) {
      space = new SpaceProperty(spaces, spaces, 0, mySettings.KEEP_BLANK_LINES_IN_CODE);
    }
    else {
      space = space.copyAndSetSpaces(spaces);
    }
    elt.setSpaceProperty(space);
  }
}

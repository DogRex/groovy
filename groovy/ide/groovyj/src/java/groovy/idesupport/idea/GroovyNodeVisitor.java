package groovy.idesupport.idea;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;

public abstract class GroovyNodeVisitor {
  public final void visit(ASTNode node) {
    final IElementType type = node.getElementType();
    if (type == GroovyElementTypes.FUNCTION_DECLARATION) {
      visitFunctionDeclaration(node);
    }
    else if (type == GroovyElementTypes.PARAMETER_LIST) {
      visitParameterList(node);
    }
    else if (type == GroovyElementTypes.VARIABLE) {
      visitVariable(node);
    }
    else if (type == GroovyElementTypes.FORMAL_PARAMETER) {
      visitParameter(node);
    }
    else if (type == GroovyElementTypes.ARGUMENT_LIST) {
      visitArgumentList(node);
    }
    else if (type == GroovyElementTypes.BLOCK) {
      visitBlock(node);
    }
    else if (type == GroovyElementTypes.LABELED_STATEMENT) {
      visitLabeledStatement(node);
    }
    else if (type == GroovyElementTypes.EXPRESSION_STATEMENT) {
      visitExpressionStatement(node);
    }
    else if (type == GroovyElementTypes.VAR_STATEMENT) {
      visitVarStatement(node);
    }
    else if (type == GroovyElementTypes.EMPTY_STATEMENT) {
      visitEmptyStatement(node);
    }
    else if (type == GroovyElementTypes.IF_STATEMENT) {
      visitIfStatement(node);
    }
    else if (type == GroovyElementTypes.CONTINUE_STATEMENT) {
      visitContinueStatement(node);
    }
    else if (type == GroovyElementTypes.BREAK_STATEMENT) {
      visitBreakStatement(node);
    }
    else if (type == GroovyElementTypes.WITH_STATEMENT) {
      visitWithStatement(node);
    }
    else if (type == GroovyElementTypes.RETURN_STATEMENT) {
      visitReturnStatement(node);
    }
    else if (type == GroovyElementTypes.THROW_STATEMENT) {
      visitThrowStatement(node);
    }
    else if (type == GroovyElementTypes.TRY_STATEMENT) {
      visitTryStatement(node);
    }
    else if (type == GroovyElementTypes.CATCH_BLOCK) {
      visitCatchBlock(node);
    }
    else if (type == GroovyElementTypes.SWITCH_STATEMENT) {
      visitSwitchStatement(node);
    }
    else if (type == GroovyElementTypes.CASE_CLAUSE) {
      visitCaseClause(node);
    }
    else if (type == GroovyElementTypes.WHILE_STATEMENT) {
      visitWhileStatement(node);
    }
    else if (type == GroovyElementTypes.DOWHILE_STATEMENT) {
      visitDoWhileStatement(node);
    }
    else if (type == GroovyElementTypes.FOR_STATEMENT) {
      visitForStatement(node);
    }
    else if (type == GroovyElementTypes.FOR_IN_STATEMENT) {
      visitForInStatement(node);
    }
    else if (type == GroovyElementTypes.THIS_EXPRESSION) {
      visitThisExpression(node);
    }
    else if (type == GroovyElementTypes.LITERAL_EXPRESSION) {
      visitLiteralExpression(node);
    }
    else if (type == GroovyElementTypes.REFERENCE_EXPRESSION) {
      visitReferenceExpression(node);
    }
    else if (type == GroovyElementTypes.PARENTHESIZED_EXPRESSION) {
      visitParenthesizedExpression(node);
    }
    else if (type == GroovyElementTypes.ARRAY_LITERAL_EXPRESSION) {
      visitArrayLiteralExpression(node);
    }
    else if (type == GroovyElementTypes.OBJECT_LITERAL_EXPRESSION) {
      visitObjectLiteralExpression(node);
    }
    else if (type == GroovyElementTypes.PROPERTY) {
      visitProperty(node);
    }
    else if (type == GroovyElementTypes.BINARY_EXPRESSION) {
      visitBinaryEpxression(node);
    }
    else if (type == GroovyElementTypes.ASSIGNMENT_EXPRESSION) {
      visitAssignmentExpression(node);
    }
    else if (type == GroovyElementTypes.COMMA_EXPRESSION) {
      visitCommaExpression(node);
    }
    else if (type == GroovyElementTypes.CONDITIONAL_EXPRESSION) {
      visitConditionalExpression(node);
    }
    else if (type == GroovyElementTypes.POSTFIX_EXPRESSION) {
      visitPostfixExpression(node);
    }
    else if (type == GroovyElementTypes.PREFIX_EXPRESSION) {
      visitPrefixExpression(node);
    }
    else if (type == GroovyElementTypes.FUNCTION_EXPRESSION) {
      visitFunctionExpression(node);
    }
    else if (type == GroovyElementTypes.NEW_EXPRESSION) {
      visitNewExpression(node);
    }
    else if (type == GroovyElementTypes.INDEXED_PROPERTY_ACCESS_EXPRESSION) {
      visitIndexedPropertyAccessExpression(node);
    }
    else if (type == GroovyElementTypes.CALL_EXPRESSION) {
      visitCallExpression(node);
    }
    else {
      visitElement(node);
    }
  }

  public void visitCallExpression(final ASTNode node) {
    visitExpression(node);
  }

  public void visitIndexedPropertyAccessExpression(final ASTNode node) {
    visitExpression(node);
  }

  public void visitNewExpression(final ASTNode node) {
    visitExpression(node);
  }

  public void visitFunctionExpression(final ASTNode node) {
    visitExpression(node);
  }

  public void visitPrefixExpression(final ASTNode node) {
    visitExpression(node);
  }

  public void visitPostfixExpression(final ASTNode node) {
    visitExpression(node);
  }

  public void visitConditionalExpression(final ASTNode node) {
    visitExpression(node);
  }

  public void visitCommaExpression(final ASTNode node) {
    visitBinaryEpxression(node);
  }

  public void visitAssignmentExpression(final ASTNode node) {
    visitBinaryEpxression(node);
  }

  public void visitBinaryEpxression(final ASTNode node) {
    visitExpression(node);
  }

  public void visitProperty(final ASTNode node) {
    visitElement(node);
  }

  public void visitObjectLiteralExpression(final ASTNode node) {
    visitExpression(node);
  }

  public void visitArrayLiteralExpression(final ASTNode node) {
    visitExpression(node);
  }

  public void visitParenthesizedExpression(final ASTNode node) {
    visitExpression(node);
  }

  public void visitReferenceExpression(final ASTNode node) {
    visitExpression(node);
  }

  public void visitLiteralExpression(final ASTNode node) {
    visitExpression(node);
  }

  public void visitThisExpression(final ASTNode node) {
    visitExpression(node);
  }

  public void visitForInStatement(final ASTNode node) {
    visitStatement(node);
  }

  public void visitForStatement(final ASTNode node) {
    visitStatement(node);
  }

  public void visitDoWhileStatement(final ASTNode node) {
    visitStatement(node);
  }

  public void visitWhileStatement(final ASTNode node) {
    visitStatement(node);
  }

  public void visitCaseClause(final ASTNode node) {
    visitElement(node);
  }

  public void visitSwitchStatement(final ASTNode node) {
    visitStatement(node);
  }

  public void visitCatchBlock(final ASTNode node) {
    visitElement(node);
  }

  public void visitTryStatement(final ASTNode node) {
    visitStatement(node);
  }

  public void visitThrowStatement(final ASTNode node) {
    visitStatement(node);
  }

  public void visitReturnStatement(final ASTNode node) {
    visitStatement(node);
  }

  public void visitWithStatement(final ASTNode node) {
    visitStatement(node);
  }

  public void visitBreakStatement(final ASTNode node) {
    visitStatement(node);
  }

  public void visitContinueStatement(final ASTNode node) {
    visitStatement(node);
  }

  public void visitIfStatement(final ASTNode node) {
    visitStatement(node);
  }

  public void visitEmptyStatement(final ASTNode node) {
    visitStatement(node);
  }

  public void visitVarStatement(final ASTNode node) {
    visitStatement(node);
  }

  public void visitExpressionStatement(final ASTNode node) {
    visitStatement(node);
  }

  public void visitLabeledStatement(final ASTNode node) {
    visitStatement(node);
  }

  public void visitBlock(final ASTNode node) {
    visitStatement(node);
  }

  public void visitArgumentList(final ASTNode node) {
    visitElement(node);
  }

  public void visitParameter(final ASTNode node) {
    visitVariable(node);
  }

  public void visitVariable(final ASTNode node) {
    visitElement(node);
  }

  public void visitParameterList(final ASTNode node) {
    visitElement(node);
  }

  public void visitElement(final ASTNode node) {

  }

  public void visitSourceElement(final ASTNode node) {
    visitElement(node);
  }

  public void visitFunctionDeclaration(final ASTNode node) {
    visitSourceElement(node);
  }

  public void visitStatement(final ASTNode node) {
    visitSourceElement(node);
  }

  public void visitExpression(final ASTNode node) {
    visitElement(node);
  }
}

package groovy.idesupport.idea.psi;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReferenceExpression;

public class GroovyElementVisitor extends PsiElementVisitor {
  public void visitReferenceExpression(PsiReferenceExpression expression) {}

  public void visitJSCallExpression(final GroovyCallExpression node) {
    visitJSExpression(node);
  }

  public void visitJSIndexedPropertyAccessExpression(final GroovyIndexedPropertyAccessExpression node) {
    visitJSExpression(node);
  }

  public void visitJSNewExpression(final GroovyNewExpression node) {
    visitJSExpression(node);
  }

  public void visitJSFunctionExpression(final GroovyFunctionExpression node) {
    visitJSExpression(node);
  }

  public void visitJSPrefixExpression(final GroovyPrefixExpression node) {
    visitJSExpression(node);
  }

  public void visitJSPostfixExpression(final GroovyPostfixExpression node) {
    visitJSExpression(node);
  }

  public void visitJSConditionalExpression(final GroovyConditionalExpression node) {
    visitJSExpression(node);
  }

  public void visitJSCommaExpression(final GroovyCommaExpression node) {
    visitJSBinaryEpxression(node);
  }

  public void visitJSAssignmentExpression(final GroovyAssignmentExpression node) {
    visitJSBinaryEpxression(node);
  }

  public void visitJSBinaryEpxression(final GroovyBinaryExpression node) {
    visitJSExpression(node);
  }

  public void visitJSProperty(final GroovyProperty node) {
    visitJSElement(node);
  }

  public void visitJSObjectLiteralExpression(final GroovyObjectLiteralExpression node) {
    visitJSExpression(node);
  }

  public void visitJSArrayLiteralExpression(final GroovyArrayLiteralExpression node) {
    visitJSExpression(node);
  }

  public void visitJSParenthesizedExpression(final GroovyParenthesizedExpression node) {
    visitJSExpression(node);
  }

  public void visitJSReferenceExpression(final GroovyReferenceExpression node) {
    visitJSExpression(node);
  }

  public void visitJSLiteralExpression(final GroovyLiteralExpression node) {
    visitJSExpression(node);
  }

  public void visitJSThisExpression(final GroovyThisExpression node) {
    visitJSExpression(node);
  }

  public void visitJSForInStatement(final GroovyForInStatement node) {
    visitJSStatement(node);
  }

  public void visitJSForStatement(final GroovyForStatement node) {
    visitJSStatement(node);
  }

  public void visitJSDoWhileStatement(final GroovyDoWhileStatement node) {
    visitJSStatement(node);
  }

  public void visitJSWhileStatement(final GroovyWhileStatement node) {
    visitJSStatement(node);
  }

  public void visitJSCaseClause(final GroovyCaseClause node) {
    visitJSElement(node);
  }

  public void visitJSSwitchStatement(final GroovySwitchStatement node) {
    visitJSStatement(node);
  }

  public void visitJSCatchBlock(final GroovyCatchBlock node) {
    visitJSElement(node);
  }

  public void visitJSTryStatement(final GroovyTryStatement node) {
    visitJSStatement(node);
  }

  public void visitJSThrowStatement(final GroovyThrowStatement node) {
    visitJSStatement(node);
  }

  public void visitJSReturnStatement(final GroovyReturnStatement node) {
    visitJSStatement(node);
  }

  public void visitJSWithStatement(final GroovyWithStatement node) {
    visitJSStatement(node);
  }

  public void visitJSBreakStatement(final GroovyBreakStatement node) {
    visitJSStatement(node);
  }

  public void visitJSContinueStatement(final GroovyContinueStatement node) {
    visitJSStatement(node);
  }

  public void visitJSIfStatement(final GroovyIfStatement node) {
    visitJSStatement(node);
  }

  public void visitJSEmptyStatement(final GroovyEmptyStatement node) {
    visitJSStatement(node);
  }

  public void visitJSVarStatement(final GroovyVarStatement node) {
    visitJSStatement(node);
  }

  public void visitJSExpressionStatement(final GroovyExpressionStatement node) {
    visitJSStatement(node);
  }

  public void visitJSLabeledStatement(final GroovyLabeledStatement node) {
    visitJSStatement(node);
  }

  public void visitJSBlock(final GroovyBlockStatement node) {
    visitJSStatement(node);
  }

  public void visitJSArgumentList(final GroovyArgumentList node) {
    visitJSElement(node);
  }

  public void visitJSParameter(final GroovyParameter node) {
    visitJSVariable(node);
  }

  public void visitJSVariable(final GroovyVariable node) {
    visitJSElement(node);
  }

  public void visitJSParameterList(final GroovyParameterList node) {
    visitJSElement(node);
  }

  public void visitJSElement(final GroovyElement node) {

  }

  public void visitJSSourceElement(final GroovyElement node) {
    visitJSElement(node);
  }

  public void visitJSFunctionDeclaration(final GroovyFunction node) {
    visitJSSourceElement(node);
  }

  public void visitJSStatement(final GroovyStatement node) {
    visitJSSourceElement(node);
  }

  public void visitJSExpression(final GroovyExpression node) {
    visitJSElement(node);
  }
}

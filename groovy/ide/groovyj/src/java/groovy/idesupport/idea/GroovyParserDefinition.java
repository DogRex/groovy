package groovy.idesupport.idea;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import groovy.idesupport.idea.parsing.JSParser;
import groovy.idesupport.idea.psi.GroovyFile;
import groovy.idesupport.idea.psi.impl.*;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

// todo: under construction. relay to the new Groovy JSR Parser.

public class GroovyParserDefinition implements ParserDefinition {
  private static final Logger LOG = Logger.getInstance("#idea.idesupport.idea.GroovyParserDefinition");

  public Lexer createLexer() {
    return new _JavaScriptLexer();
  }

  public IElementType getFileNodeType() {
    return GroovyElementTypes.FILE;
  }

  public TokenSet getWhitespaceTokens() {
    return TokenSet.create(new IElementType[] {GroovyTokenTypes.WHITE_SPACE});
  }

  public TokenSet getCommentTokens() {
    return GroovyTokenTypes.COMMENTS;
  }

  public PsiParser createParser() {
    return new JSParser();
  }

  public PsiElement createElement(ASTNode node) {
    final IElementType type = node.getElementType();
    if (type == GroovyElementTypes.FUNCTION_DECLARATION) {
      return new GroovyFunctionImpl(node);
    }
    else if (type == GroovyElementTypes.PARAMETER_LIST) {
      return new GroovyParameterListImpl(node);
    }
    else if (type == GroovyElementTypes.VARIABLE) {
      return new GroovyVariableImpl(node);
    }
    else if (type == GroovyElementTypes.FORMAL_PARAMETER) {
      return new GroovyParameterImpl(node);
    }
    else if (type == GroovyElementTypes.ARGUMENT_LIST) {
      return new GroovyArgumentListImpl(node);
    }
    else if (type == GroovyElementTypes.BLOCK) {
      return new GroovyBlockStatementImpl(node);
    }
    else if (type == GroovyElementTypes.LABELED_STATEMENT) {
      return new GroovyLabeledStatementImpl(node);
    }
    else if (type == GroovyElementTypes.EXPRESSION_STATEMENT) {
      return new GroovyExpressionStatementImpl(node);
    }
    else if (type == GroovyElementTypes.VAR_STATEMENT) {
      return new GroovyVarStatementImpl(node);
    }
    else if (type == GroovyElementTypes.EMPTY_STATEMENT) {
      return new GroovyEmptyStatementImpl(node);
    }
    else if (type == GroovyElementTypes.IF_STATEMENT) {
      return new GroovyIfStatementImpl(node);
    }
    else if (type == GroovyElementTypes.CONTINUE_STATEMENT) {
      return new GroovyContinueStatementImpl(node);
    }
    else if (type == GroovyElementTypes.BREAK_STATEMENT) {
      return new GroovyBreakStatementImpl(node);
    }
    else if (type == GroovyElementTypes.WITH_STATEMENT) {
      return new GroovyWithStatmentImpl(node);
    }
    else if (type == GroovyElementTypes.RETURN_STATEMENT) {
      return new GroovyReturnStatementImpl(node);
    }
    else if (type == GroovyElementTypes.THROW_STATEMENT) {
      return new GroovyThrowStatementImpl(node);
    }
    else if (type == GroovyElementTypes.TRY_STATEMENT) {
      return new GroovyTryStatementImpl(node);
    }
    else if (type == GroovyElementTypes.CATCH_BLOCK) {
      return new GroovyCatchBlockImpl(node);
    }
    else if (type == GroovyElementTypes.SWITCH_STATEMENT) {
      return new GroovySwitchStatementImpl(node);
    }
    else if (type == GroovyElementTypes.CASE_CLAUSE) {
      return new GroovyCaseClauseImpl(node);
    }
    else if (type == GroovyElementTypes.WHILE_STATEMENT) {
      return new GroovyWhileStatementImpl(node);
    }
    else if (type == GroovyElementTypes.DOWHILE_STATEMENT) {
      return new GroovyDoWhileStatementImpl(node);
    }
    else if (type == GroovyElementTypes.FOR_STATEMENT) {
      return new GroovyForStatementImpl(node);
    }
    else if (type == GroovyElementTypes.FOR_IN_STATEMENT) {
      return new GroovyForInStatementImpl(node);
    }
    else if (type == GroovyElementTypes.THIS_EXPRESSION) {
      return new GroovyThisExpressionImpl(node);
    }
    else if (type == GroovyElementTypes.LITERAL_EXPRESSION) {
      return new GroovyLiteralExpressionImpl(node);
    }
    else if (type == GroovyElementTypes.REFERENCE_EXPRESSION) {
      return new GroovyReferenceExpressionImpl(node);
    }
    else if (type == GroovyElementTypes.PARENTHESIZED_EXPRESSION) {
      return new GroovyParenthesizedExpressionImpl(node);
    }
    else if (type == GroovyElementTypes.ARRAY_LITERAL_EXPRESSION) {
      return new GroovyArrayLiteralExpressionImpl(node);
    }
    else if (type == GroovyElementTypes.OBJECT_LITERAL_EXPRESSION) {
      return new GroovyObjectLiteralExpressionImpl(node);
    }
    else if (type == GroovyElementTypes.PROPERTY) {
      return new GroovyPropertyImpl(node);
    }
    else if (type == GroovyElementTypes.BINARY_EXPRESSION) {
      return new GroovyBinaryExpressionImpl(node);
    }
    else if (type == GroovyElementTypes.ASSIGNMENT_EXPRESSION) {
      return new GroovyAssignmentExpressionImpl(node);
    }
    else if (type == GroovyElementTypes.COMMA_EXPRESSION) {
      return new GroovyCommaExpressionImpl(node);
    }
    else if (type == GroovyElementTypes.CONDITIONAL_EXPRESSION) {
      return new GroovyConditionalExpressionImpl(node);
    }
    else if (type == GroovyElementTypes.POSTFIX_EXPRESSION) {
      return new GroovyPostfixExpressionImpl(node);
    }
    else if (type == GroovyElementTypes.PREFIX_EXPRESSION) {
      return new GroovyPrefixExpressionImpl(node);
    }
    else if (type == GroovyElementTypes.FUNCTION_EXPRESSION) {
      return new GroovyFunctionExpressionImpl(node);
    }
    else if (type == GroovyElementTypes.NEW_EXPRESSION) {
      return new GroovyNewExpressionImpl(node);
    }
    else if (type == GroovyElementTypes.INDEXED_PROPERTY_ACCESS_EXPRESSION) {
      return new GroovyIndexedPropertyAccessExpressionImpl(node);
    }
    else if (type == GroovyElementTypes.CALL_EXPRESSION) {
      return new GroovyCallExpressionImpl(node);
    } else if (type == GroovyElementTypes.EMBEDDED_CONTENT) {
      return new GroovyEmbeddedContentImpl(node);
    }

    LOG.error("Alien element type [" + type + "]. Can't create Groovy PsiElement for that.");

    return new ASTWrapperPsiElement(node);
  }

  public PsiFile createFile(Project project, VirtualFile file) {
    return new GroovyFile(project, file);
  }

  public PsiFile createFile(Project project, String name, CharSequence text) {
    return new GroovyFile(project, name, text);
  }
}

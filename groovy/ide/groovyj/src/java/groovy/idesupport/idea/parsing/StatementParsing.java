package groovy.idesupport.idea.parsing;

import com.intellij.lang.PsiBuilder;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyTokenTypes;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 28, 2005
 * Time: 1:19:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatementParsing extends Parsing{
  private static final Logger LOG = Logger.getInstance("#idea.idesupport.idea.parsing.StatementParsing");
  private StatementParsing() { }

  public static void parseSourceElement(PsiBuilder builder) {
    if (builder.getTokenType() == GroovyTokenTypes.FUNCTION_KEYWORD) {
      FunctionParsing.parseFunctionDeclaration(builder);
    }
    else {
      parseStatement(builder);
    }
  }

  public static void parseStatement(PsiBuilder builder) {
    final IElementType firstToken = builder.getTokenType();

    if (firstToken == null) return;

    if (firstToken == GroovyTokenTypes.LBRACE) {
      parseBlock(builder);
      return;
    }

    if (firstToken == GroovyTokenTypes.VAR_KEYWORD) {
      parseVarStatement(builder, false);
      return;
    }

    if (firstToken == GroovyTokenTypes.SEMICOLON) {
      parseEmptyStatement(builder);
      return;
    }

    if (firstToken == GroovyTokenTypes.IF_KEYWORD) {
      parseIfStatement(builder);
      return;
    }

    if (firstToken == GroovyTokenTypes.DO_KEYWORD ||
        firstToken == GroovyTokenTypes.WHILE_KEYWORD ||
        firstToken == GroovyTokenTypes.FOR_KEYWORD) {
      parseIterationStatement(builder);
      return;
    }

    if (firstToken == GroovyTokenTypes.CONTINUE_KEYWORD) {
      parseContinueStatement(builder);
      return;
    }

    if (firstToken == GroovyTokenTypes.BREAK_KEYWORD) {
      parseBreakStatement(builder);
      return;
    }

    if (firstToken == GroovyTokenTypes.RETURN_KEYWORD) {
      parseReturnStatement(builder);
      return;
    }

    if (firstToken == GroovyTokenTypes.WITH_KEYWORD) {
      parseWithStatement(builder);
      return;
    }

    if (firstToken == GroovyTokenTypes.SWITCH_KEYWORD) {
      parseSwitchStatement(builder);
      return;
    }

    if (firstToken == GroovyTokenTypes.THROW_KEYWORD) {
      parseThrowStatement(builder);
      return;
    }

    if (firstToken == GroovyTokenTypes.TRY_KEYWORD) {
      parseTryStatement(builder);
      return;
    }


    if (firstToken == GroovyTokenTypes.IDENTIFIER) {
      // Try labeled statement:
      final PsiBuilder.Marker labeledStatement = builder.mark();
      builder.advanceLexer();
      if (builder.getTokenType() == GroovyTokenTypes.COLON) {
        builder.advanceLexer();
        parseStatement(builder);
        labeledStatement.done(GroovyElementTypes.LABELED_STATEMENT);
        return;
      }
      else {
        labeledStatement.rollbackTo();
      }
    }

    if (firstToken != GroovyTokenTypes.LBRACE && firstToken != GroovyTokenTypes.FUNCTION_KEYWORD) {
      // Try expression statement
      final PsiBuilder.Marker exprStatement = builder.mark();
      if (ExpressionParsing.parseExpressionOptional(builder)) {
        checkMatches(builder, GroovyTokenTypes.SEMICOLON, "';' expected");
        exprStatement.done(GroovyElementTypes.EXPRESSION_STATEMENT);
        return;
      }
      else {
        builder.advanceLexer();
        exprStatement.drop();
      }
    }

    builder.error("statement expected");
  }

  private static void parseTryStatement(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.TRY_KEYWORD);
    final PsiBuilder.Marker statement = builder.mark();
    builder.advanceLexer();
    parseBlock(builder);

    if (builder.getTokenType() == GroovyTokenTypes.CATCH_KEYWORD) {
      parseCatchBlock(builder);
    }

    if (builder.getTokenType() == GroovyTokenTypes.FINALLY_KEYWORD) {
      builder.advanceLexer();
      parseBlock(builder);
    }

    statement.done(GroovyElementTypes.TRY_STATEMENT);
  }

  private static void parseCatchBlock(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.CATCH_KEYWORD);
    final PsiBuilder.Marker block = builder.mark();
    builder.advanceLexer();
    checkMatches(builder, GroovyTokenTypes.LPAR, "( expected");

    if (builder.getTokenType() == GroovyTokenTypes.IDENTIFIER) {
      final PsiBuilder.Marker param = builder.mark();
      builder.advanceLexer();
      param.done(GroovyElementTypes.FORMAL_PARAMETER);
    }
    else {
      builder.error("parameter name expected");
    }

    checkMatches(builder, GroovyTokenTypes.RPAR, ") expected");

    parseBlock(builder);

    block.done(GroovyElementTypes.CATCH_BLOCK);
  }

  private static void parseThrowStatement(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.THROW_KEYWORD);
    final PsiBuilder.Marker statement = builder.mark();
    builder.advanceLexer();

    ExpressionParsing.parseExpressionOptional(builder);

    checkMatches(builder, GroovyTokenTypes.SEMICOLON, "; expected");
    statement.done(GroovyElementTypes.THROW_STATEMENT);
  }

  private static void parseSwitchStatement(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.WITH_KEYWORD);
    final PsiBuilder.Marker statement = builder.mark();
    builder.advanceLexer();

    checkMatches(builder, GroovyTokenTypes.LPAR, "( expected");
    ExpressionParsing.parseExpression(builder);
    checkMatches(builder, GroovyTokenTypes.RPAR, ") expected");

    checkMatches(builder, GroovyTokenTypes.LBRACE, "{ expected");
    while (builder.getTokenType() != GroovyTokenTypes.RBRACE) {
      if (builder.eof()) {
        builder.error("unexpected end of file");
        statement.done(GroovyElementTypes.SWITCH_STATEMENT);
        return;
      }
      parseCaseOrDefaultClause(builder);
    }

    builder.advanceLexer();
    statement.done(GroovyElementTypes.SWITCH_STATEMENT);
  }

  private static void parseCaseOrDefaultClause(final PsiBuilder builder) {
    final IElementType firstToken = builder.getTokenType();
    final PsiBuilder.Marker clause = builder.mark();
    if (firstToken != GroovyTokenTypes.CASE_KEYWORD && firstToken != GroovyTokenTypes.DEFAULT_KEYWORD) {
      builder.error("catch or default expected");
    }
    builder.advanceLexer();
    if (firstToken == GroovyTokenTypes.CASE_KEYWORD) {
      ExpressionParsing.parseExpression(builder);
    }
    checkMatches(builder, GroovyTokenTypes.COLON, ": expected");
    while (true) {
      IElementType token = builder.getTokenType();
      if (token == null || token == GroovyTokenTypes.CASE_KEYWORD || token == GroovyTokenTypes.DEFAULT_KEYWORD || token == GroovyTokenTypes.RBRACE) break;
      parseStatement(builder);
    }
    clause.done(GroovyElementTypes.CASE_CLAUSE);
  }

  private static void parseWithStatement(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.WITH_KEYWORD);
    final PsiBuilder.Marker statement = builder.mark();
    builder.advanceLexer();

    checkMatches(builder, GroovyTokenTypes.LPAR, "( expected");
    ExpressionParsing.parseExpression(builder);
    checkMatches(builder, GroovyTokenTypes.RPAR, ") expected");

    parseStatement(builder);

    statement.done(GroovyElementTypes.WITH_STATEMENT);
  }

  private static void parseReturnStatement(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.RETURN_KEYWORD);
    final PsiBuilder.Marker statement = builder.mark();
    builder.advanceLexer();

    ExpressionParsing.parseExpressionOptional(builder);

    checkMatches(builder, GroovyTokenTypes.SEMICOLON, "; expected");
    statement.done(GroovyElementTypes.RETURN_STATEMENT);
  }

  private static void parseBreakStatement(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.BREAK_KEYWORD);
    final PsiBuilder.Marker statement = builder.mark();
    builder.advanceLexer();

    if (builder.getTokenType() == GroovyTokenTypes.IDENTIFIER) {
      builder.advanceLexer();
    }

    checkMatches(builder, GroovyTokenTypes.SEMICOLON, "; expected");
    statement.done(GroovyElementTypes.BREAK_STATEMENT);
  }

  private static void parseContinueStatement(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.CONTINUE_KEYWORD);
    final PsiBuilder.Marker statement = builder.mark();
    builder.advanceLexer();
    if (builder.getTokenType() == GroovyTokenTypes.IDENTIFIER) {
      builder.advanceLexer();
    }

    checkMatches(builder, GroovyTokenTypes.SEMICOLON, "; expected");
    statement.done(GroovyElementTypes.CONTINUE_STATEMENT);
  }

  private static void parseIterationStatement(final PsiBuilder builder) {
    final IElementType tokenType = builder.getTokenType();
    if (tokenType == GroovyTokenTypes.DO_KEYWORD) {
      parseDoWhileStatement(builder);
    }
    else if (tokenType == GroovyTokenTypes.WHILE_KEYWORD) {
      parseWhileStatement(builder);
    }
    else if (tokenType == GroovyTokenTypes.FOR_KEYWORD) {
      parseForStatement(builder);
    }
    else {
      LOG.error("Unknown iteration statement");
    }
  }

  private static void parseForStatement(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.FOR_KEYWORD);
    final PsiBuilder.Marker statement = builder.mark();
    builder.advanceLexer();
    checkMatches(builder, GroovyTokenTypes.LPAR, "( expected");
    final boolean empty;
    if (builder.getTokenType() == GroovyTokenTypes.VAR_KEYWORD) {
      parseVarStatement(builder, true);
      empty = false;
    }
    else {
      empty = ExpressionParsing.parseExpressionOptional(builder);
    }

    boolean forin = false;
    if (builder.getTokenType() == GroovyTokenTypes.SEMICOLON) {
      builder.advanceLexer();
      ExpressionParsing.parseExpressionOptional(builder);
      checkMatches(builder, GroovyTokenTypes.SEMICOLON, "; expected");
      ExpressionParsing.parseExpressionOptional(builder);
    }
    else if (builder.getTokenType() == GroovyTokenTypes.IN_KEYWORD) {
      forin = true;
      if (empty) builder.error("left hand side expression or variable declaration expected before 'in'");
      builder.advanceLexer();
      ExpressionParsing.parseExpression(builder);
    }
    else {
      builder.error("in or ; expected");
    }

    checkMatches(builder, GroovyTokenTypes.RPAR, ") expected");

    parseStatement(builder);
    statement.done(forin ? GroovyElementTypes.FOR_IN_STATEMENT : GroovyElementTypes.FOR_STATEMENT);
  }

  private static void parseWhileStatement(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.WHILE_KEYWORD);
    final PsiBuilder.Marker statement = builder.mark();
    builder.advanceLexer();

    checkMatches(builder, GroovyTokenTypes.LPAR, "( expected");
    ExpressionParsing.parseExpression(builder);
    checkMatches(builder, GroovyTokenTypes.RPAR, ") expected");

    parseStatement(builder);
    statement.done(GroovyElementTypes.WHILE_STATEMENT);
  }

  private static void parseDoWhileStatement(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.DO_KEYWORD);
    final PsiBuilder.Marker statement = builder.mark();
    builder.advanceLexer();

    parseStatement(builder);
    checkMatches(builder, GroovyTokenTypes.WHILE_KEYWORD, "while expected");
    checkMatches(builder, GroovyTokenTypes.LPAR, "( expected");
    ExpressionParsing.parseExpression(builder);
    checkMatches(builder, GroovyTokenTypes.RPAR, ") expected");
    checkMatches(builder, GroovyTokenTypes.SEMICOLON, "; expected");

    statement.done(GroovyElementTypes.DOWHILE_STATEMENT);
  }

  private static void parseIfStatement(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.IF_KEYWORD);
    final PsiBuilder.Marker ifStatement = builder.mark();
    builder.advanceLexer();

    checkMatches(builder, GroovyTokenTypes.LPAR, "( expected");
    ExpressionParsing.parseExpression(builder);
    checkMatches(builder, GroovyTokenTypes.RPAR, ") expected");

    parseStatement(builder);

    if (builder.getTokenType() == GroovyTokenTypes.ELSE_KEYWORD) {
      builder.advanceLexer();
      parseStatement(builder);
    }

    ifStatement.done(GroovyElementTypes.IF_STATEMENT);
  }

  private static void parseEmptyStatement(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.SEMICOLON);
    final PsiBuilder.Marker statement = builder.mark();
    builder.advanceLexer();
    statement.done(GroovyElementTypes.EMPTY_STATEMENT);
  }

  private static void parseVarStatement(final PsiBuilder builder, final boolean inForInitializationContext) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.VAR_KEYWORD);
    final PsiBuilder.Marker var = builder.mark();
    builder.advanceLexer();
    boolean first = true;
    while (true) {
      if (first) {
        first = false;
      }
      else {
        checkMatches(builder, GroovyTokenTypes.COMMA, ", expected");
      }

      parseVarDeclaration(builder, !inForInitializationContext);

      if (builder.getTokenType() != GroovyTokenTypes.COMMA) {
        break;
      }
    }

    if (!inForInitializationContext) {
      checkMatches(builder, GroovyTokenTypes.SEMICOLON, "; expected");
    }

    var.done(GroovyElementTypes.VAR_STATEMENT);
  }

  private static void parseVarDeclaration(final PsiBuilder builder, boolean allowIn) {
    if (builder.getTokenType() != GroovyTokenTypes.IDENTIFIER) {
      builder.error("variable name expected");
      builder.advanceLexer();
      return;
    }
    final PsiBuilder.Marker var = builder.mark();
    builder.advanceLexer();
    if (builder.getTokenType() == GroovyTokenTypes.EQ) {
      builder.advanceLexer();
      if (allowIn) {
        if (!ExpressionParsing.parseAssignmentExpression(builder)) {
          builder.error("expression expected");
        }
      }
      else {
        if (!ExpressionParsing.parseAssignmentExpressionNoIn(builder)) {
          builder.error("expression expected");
        }
      }
    }
    var.done(GroovyElementTypes.VARIABLE);
  }

  public static void parseBlock(final PsiBuilder builder) {
    parseBlockOrFunctionBody(builder, false);
  }

  public static void parseFunctionBody(final PsiBuilder builder) {
    parseBlockOrFunctionBody(builder, true);
  }

  private static void parseBlockOrFunctionBody(final PsiBuilder builder, boolean functionBody) {
    if (builder.getTokenType() != GroovyTokenTypes.LBRACE) {
      builder.error("{ expected");
      return;
    }

    final PsiBuilder.Marker block = builder.mark();
    builder.advanceLexer();
    while (builder.getTokenType() != GroovyTokenTypes.RBRACE) {
      if (builder.eof()) {
        builder.error("missing }");
        block.done(GroovyElementTypes.BLOCK);
        return;
      }

      if (functionBody) {
        parseSourceElement(builder);
      }
      else {
        parseStatement(builder);
      }
    }

    builder.advanceLexer();
    block.done(GroovyElementTypes.BLOCK);
  }
}

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
 * Time: 1:18:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExpressionParsing extends Parsing{
  private static final Logger LOG = Logger.getInstance("#idea.idesupport.idea.parsing.ExpressionParsing");
  private ExpressionParsing() { }

  private static boolean parsePrimaryExpression(PsiBuilder builder) {
    final IElementType firstToken = builder.getTokenType();
    if (firstToken == GroovyTokenTypes.THIS_KEYWORD) {
      buildTokenElement(GroovyElementTypes.THIS_EXPRESSION, builder);
      return true;
    }
    else if (firstToken == GroovyTokenTypes.IDENTIFIER) {
      buildTokenElement(GroovyElementTypes.REFERENCE_EXPRESSION, builder);
      return true;
    }
    else if (firstToken == GroovyTokenTypes.NUMERIC_LITERAL ||
        firstToken == GroovyTokenTypes.STRING_LITERAL ||
        firstToken == GroovyTokenTypes.REGEXP_LITERAL ||
        firstToken == GroovyTokenTypes.NULL_KEYWORD ||
        firstToken == GroovyTokenTypes.FALSE_KEYWORD ||
        firstToken == GroovyTokenTypes.TRUE_KEYWORD) {
      buildTokenElement(GroovyElementTypes.LITERAL_EXPRESSION, builder);
      return true;
    }
    else if (firstToken == GroovyTokenTypes.LPAR) {
      parseParenthesizedExpression(builder);
      return true;
    }
    else if (firstToken == GroovyTokenTypes.LBRACKET) {
      parseArrayLiteralExpression(builder);
      return true;
    }
    else if (firstToken == GroovyTokenTypes.LBRACE) {
      parseObjectLiteralExpression(builder);
      return true;
    }
    else if (firstToken == GroovyTokenTypes.FUNCTION_KEYWORD) {
      FunctionParsing.parseFunctionExpression(builder);
      return true;
    }
    else {
      return false;
    }
  }

  private static void parseObjectLiteralExpression(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.LBRACE);
    final PsiBuilder.Marker expr = builder.mark();
    builder.advanceLexer();

    while (true) {
      parseProperty(builder);

      if (builder.getTokenType() != GroovyTokenTypes.COMMA) break;
      builder.advanceLexer();
    }

    checkMatches(builder, GroovyTokenTypes.RBRACE, "} expected");
    expr.done(GroovyElementTypes.OBJECT_LITERAL_EXPRESSION);
  }

  private static void parseProperty(final PsiBuilder builder) {
    final IElementType nameToken = builder.getTokenType();
    final PsiBuilder.Marker property = builder.mark();

    if (nameToken != GroovyTokenTypes.IDENTIFIER && nameToken != GroovyTokenTypes.STRING_LITERAL && nameToken != GroovyTokenTypes.NUMERIC_LITERAL) {
      builder.error("identifier or string literal or numeric literal expected");
    }
    builder.advanceLexer();

    checkMatches(builder, GroovyTokenTypes.COLON, ": expected");
    if (!parseAssignmentExpression(builder)) {
      builder.error("expression expected");
    }

    property.done(GroovyElementTypes.PROPERTY);
  }

  private static void parseArrayLiteralExpression(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.LBRACKET);
    final PsiBuilder.Marker expr = builder.mark();
    while (builder.getTokenType() != GroovyTokenTypes.RBRACKET) {
      if (builder.eof()) {
        builder.error("unexpected end of file");
        expr.done(GroovyElementTypes.ARRAY_LITERAL_EXPRESSION);
        return;
      }
      if (builder.getTokenType() != GroovyTokenTypes.COMMA) {
        builder.advanceLexer();
      }
      else {
        if (!parseAssignmentExpression(builder)) {
          builder.error("expression or , or ] expected");
        }
      }
    }
    checkMatches(builder, GroovyTokenTypes.RBRACKET, "] expected");
    expr.done(GroovyElementTypes.ARRAY_LITERAL_EXPRESSION);
  }

  private static void parseParenthesizedExpression(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.LPAR);
    final PsiBuilder.Marker expr = builder.mark();
    builder.advanceLexer();
    parseExpression(builder);
    checkMatches(builder, GroovyTokenTypes.RPAR, ") expected");
    expr.done(GroovyElementTypes.PARENTHESIZED_EXPRESSION);
  }

  private static boolean parseMemberExpression(PsiBuilder builder, boolean allowCallSyntax) {
    PsiBuilder.Marker expr = builder.mark();
    final boolean isNew;
    if (builder.getTokenType() == GroovyTokenTypes.NEW_KEYWORD) {
      isNew = true;
      parseNewExpression(builder);
    }
    else {
      isNew = false;
      if (!parsePrimaryExpression(builder)) {
        expr.drop();
        return false;
      }
    }

    while (true) {
      final IElementType tokenType = builder.getTokenType();
      if (tokenType == GroovyTokenTypes.DOT) {
        builder.advanceLexer();
        checkMatches(builder, GroovyTokenTypes.IDENTIFIER, "name expected");
        expr.done(GroovyElementTypes.REFERENCE_EXPRESSION);
        expr = expr.preceed();
      }
      else if (tokenType == GroovyTokenTypes.LBRACKET) {
        builder.advanceLexer();
        parseExpression(builder);
        checkMatches(builder, GroovyTokenTypes.RBRACKET, "] expected");
        expr.done(GroovyElementTypes.INDEXED_PROPERTY_ACCESS_EXPRESSION);
        expr = expr.preceed();
      }
      else if (allowCallSyntax && tokenType == GroovyTokenTypes.LPAR) {
        parseArgumentList(builder);
        expr.done(isNew ? GroovyElementTypes.NEW_EXPRESSION : GroovyElementTypes.CALL_EXPRESSION);
        expr = expr.preceed();
      }
      else {
        expr.drop();
        break;
      }
    }

    return true;
  }

  private static void parseNewExpression(PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.NEW_KEYWORD);
    builder.advanceLexer();

    if (!parseMemberExpression(builder, false)) {
      builder.error("Expression expected");
    }

    if (builder.getTokenType() == GroovyTokenTypes.LPAR) {
      parseArgumentList(builder);
    }
  }

  private static void parseArgumentList(final PsiBuilder builder) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.LPAR);
    final PsiBuilder.Marker arglist = builder.mark();
    builder.advanceLexer();
    boolean first = true;
    while (builder.getTokenType() != GroovyTokenTypes.RPAR) {
      if (first) {
        first = false;
      }
      else {
        if (builder.getTokenType() == GroovyTokenTypes.COMMA) {
          builder.advanceLexer();
        }
        else {
          builder.error(", or ) expected");
          break;
        }
      }
      if (!parseAssignmentExpression(builder)) {
        builder.error("expression expected");
      }
    }

    checkMatches(builder, GroovyTokenTypes.RPAR, ") expected");
    arglist.done(GroovyElementTypes.ARGUMENT_LIST);
  }

  public static void parseExpression(PsiBuilder builder) {
    if (!parseExpressionOptional(builder)) {
      builder.error("expression expected");
    }
  }

  public static boolean parseAssignmentExpressionNoIn(final PsiBuilder builder) {
    return parseAssignmentExpression(builder, false);
  }

  public static boolean parseAssignmentExpression(final PsiBuilder builder) {
    return parseAssignmentExpression(builder, true);
  }

  private static boolean parseAssignmentExpression(final PsiBuilder builder, boolean allowIn) {
    final PsiBuilder.Marker expr = builder.mark();
    if (!parseConditionalExpression(builder, allowIn)) {
      expr.drop();
      return false;
    }

    if (GroovyTokenTypes.ASSIGNMENT_OPERATIONS.isInSet(builder.getTokenType())) {
      builder.advanceLexer();
      if (!parseAssignmentExpression(builder, allowIn)) {
        builder.error("expression expected");
      }
      expr.done(GroovyElementTypes.ASSIGNMENT_EXPRESSION);
    }
    else {
      expr.drop();
    }
    return true;
  }

  private static boolean parseConditionalExpression(final PsiBuilder builder, final boolean allowIn) {
    final PsiBuilder.Marker expr = builder.mark();
    if (!parseORExpression(builder, allowIn)) {
      expr.drop();
      return false;
    }

    if (builder.getTokenType() == GroovyTokenTypes.QUEST) {
      builder.advanceLexer();
      if (!parseAssignmentExpression(builder, allowIn)) {
        builder.error("expression expected");
      }
      checkMatches(builder, GroovyTokenTypes.COLON, ": expected");
      if (!parseAssignmentExpression(builder, allowIn)) {
        builder.error("expression expected");
      }
      expr.done(GroovyElementTypes.CONDITIONAL_EXPRESSION);
    }
    else {
      expr.drop();
    }
    return true;
  }

  private static boolean parseORExpression(final PsiBuilder builder, final boolean allowIn) {
    PsiBuilder.Marker expr = builder.mark();
    if (!parseANDExpression(builder, allowIn)) {
      expr.drop();
      return false;
    }

    while (builder.getTokenType() == GroovyTokenTypes.OROR) {
      builder.advanceLexer();
      if (!parseANDExpression(builder, allowIn)) {
        builder.error("expression expected");
      }
      expr.done(GroovyElementTypes.BINARY_EXPRESSION);
      expr = expr.preceed();
    }

    expr.drop();
    return true;
  }

  private static boolean parseANDExpression(final PsiBuilder builder, final boolean allowIn) {
    PsiBuilder.Marker expr = builder.mark();
    if (!parseBitwiseORExpression(builder, allowIn)) {
      expr.drop();
      return false;
    }

    while (builder.getTokenType() == GroovyTokenTypes.ANDAND) {
      builder.advanceLexer();
      if (!parseBitwiseORExpression(builder, allowIn)) {
        builder.error("expression expected");
      }
      expr.done(GroovyElementTypes.BINARY_EXPRESSION);
      expr = expr.preceed();
    }

    expr.drop();
    return true;
  }

  private static boolean parseBitwiseORExpression(final PsiBuilder builder, final boolean allowIn) {
    PsiBuilder.Marker expr = builder.mark();
    if (!parseBitwiseXORExpression(builder, allowIn)) {
      expr.drop();
      return false;
    }

    while (builder.getTokenType() == GroovyTokenTypes.OR) {
      builder.advanceLexer();
      if (!parseBitwiseXORExpression(builder, allowIn)) {
        builder.error("expression expected");
      }
      expr.done(GroovyElementTypes.BINARY_EXPRESSION);
      expr = expr.preceed();
    }

    expr.drop();
    return true;
  }

  private static boolean parseBitwiseXORExpression(final PsiBuilder builder, final boolean allowIn) {
    PsiBuilder.Marker expr = builder.mark();
    if (!parseBitwiseANDExpression(builder, allowIn)) {
      expr.drop();
      return false;
    }

    while (builder.getTokenType() == GroovyTokenTypes.XOR) {
      builder.advanceLexer();
      if (!parseBitwiseANDExpression(builder, allowIn) ) {
        builder.error("expression expected");
      }
      expr.done(GroovyElementTypes.BINARY_EXPRESSION);
      expr = expr.preceed();
    }

    expr.drop();
    return true;
  }

  private static boolean parseBitwiseANDExpression(final PsiBuilder builder, final boolean allowIn) {
    PsiBuilder.Marker expr = builder.mark();
    if (!parseEqualityExpression(builder, allowIn)) {
      expr.drop();
      return false;
    }

    while (builder.getTokenType() == GroovyTokenTypes.AND) {
      builder.advanceLexer();
      if (!parseEqualityExpression(builder, allowIn)) {
        builder.error("expression expected");
      }
      expr.done(GroovyElementTypes.BINARY_EXPRESSION);
      expr = expr.preceed();
    }

    expr.drop();
    return true;
  }

  private static boolean parseEqualityExpression(final PsiBuilder builder, final boolean allowIn) {
    PsiBuilder.Marker expr = builder.mark();
    if (!parseRelationalExpression(builder, allowIn)) {
      expr.drop();
      return false;
    }

    while (GroovyTokenTypes.EQUALITY_OPERATIONS.isInSet(builder.getTokenType())) {
      builder.advanceLexer();
      if (!parseRelationalExpression(builder, allowIn)) {
        builder.error("expression expected");
      }
      expr.done(GroovyElementTypes.BINARY_EXPRESSION);
      expr = expr.preceed();
    }

    expr.drop();
    return true;
  }

  private static boolean parseRelationalExpression(final PsiBuilder builder, final boolean allowIn) {
    PsiBuilder.Marker expr = builder.mark();
    if (!parseShiftExpression(builder)) {
      expr.drop();
      return false;
    }
    while (GroovyTokenTypes.RELATIONAL_OPERATIONS.isInSet(builder.getTokenType()) &&
           (allowIn || builder.getTokenType() != GroovyTokenTypes.IN_KEYWORD)) {
      builder.advanceLexer();
      if (!parseShiftExpression(builder)) {
        builder.error("expression expected");
      }
      expr.done(GroovyElementTypes.BINARY_EXPRESSION);
      expr = expr.preceed();
    }

    expr.drop();
    return true;
  }

  private static boolean parseShiftExpression(final PsiBuilder builder) {
    PsiBuilder.Marker expr = builder.mark();
    if (!parseAdditiveExpression(builder)) {
      expr.drop();
      return false;
    }
    while (GroovyTokenTypes.SHIFT_OPERATIONS.isInSet(builder.getTokenType())) {
      builder.advanceLexer();
      if (!parseAdditiveExpression(builder)) {
        builder.error("expression expected");
      }
      expr.done(GroovyElementTypes.BINARY_EXPRESSION);
      expr = expr.preceed();
    }

    expr.drop();
    return true;
  }

  private static boolean parseAdditiveExpression(final PsiBuilder builder) {
    PsiBuilder.Marker expr = builder.mark();
    if (!parseMultiplicativeExpression(builder)) {
      expr.drop();
      return false;
    }
    while (GroovyTokenTypes.ADDITIVE_OPERATIONS.isInSet(builder.getTokenType())) {
      builder.advanceLexer();
      if (!parseMultiplicativeExpression(builder)) {
        builder.error("expression expected");
      }
      expr.done(GroovyElementTypes.BINARY_EXPRESSION);
      expr = expr.preceed();
    }

    expr.drop();
    return true;
  }

  private static boolean parseMultiplicativeExpression(final PsiBuilder builder) {
    PsiBuilder.Marker expr = builder.mark();
    if (!parseUnaryExpression(builder)) {
      expr.drop();
      return false;
    }

    while (GroovyTokenTypes.MULTIPLICATIVE_OPERATIONS.isInSet(builder.getTokenType())) {
      builder.advanceLexer();
      if (!parseUnaryExpression(builder)) {
        builder.error("expression expected");
      }
      expr.done(GroovyElementTypes.BINARY_EXPRESSION);
      expr = expr.preceed();
    }

    expr.drop();
    return true;
  }

  private static boolean parseUnaryExpression(final PsiBuilder builder) {
    final IElementType tokenType = builder.getTokenType();
    if (GroovyTokenTypes.UNARY_OPERATIONS.isInSet(tokenType)) {
      final PsiBuilder.Marker expr = builder.mark();
      builder.advanceLexer();
      if (!parseUnaryExpression(builder)) {
        builder.error("Expression expected");
      }
      expr.done(GroovyElementTypes.PREFIX_EXPRESSION);
      return true;
    }
    else {
      return parsePostfixExpression(builder);
    }
  }

  private static boolean parsePostfixExpression(PsiBuilder builder) {
    final PsiBuilder.Marker expr = builder.mark();
    if (!parseMemberExpression(builder, true)) {
      expr.drop();
      return false;
    }

    final IElementType tokenType = builder.getTokenType();
    if (tokenType == GroovyTokenTypes.PLUSPLUS || tokenType == GroovyTokenTypes.MINUSMINUS) {
      builder.advanceLexer();
      expr.done(GroovyElementTypes.POSTFIX_EXPRESSION);
    }
    else {
      expr.drop();
    }
    return true;
  }

  public static boolean parseExpressionOptional(final PsiBuilder builder) {
    PsiBuilder.Marker expr = builder.mark();
    if (!parseAssignmentExpression(builder)) {
      expr.drop();
      return false;
    }

    while (builder.getTokenType() == GroovyTokenTypes.COMMA) {
      builder.advanceLexer();
      if (!parseAssignmentExpression(builder)) {
        builder.error("expression expected");
      }

      expr.done(GroovyElementTypes.COMMA_EXPRESSION);
      expr = expr.preceed();
    }

    expr.drop();

    return true;
  }

  private static void buildTokenElement(IElementType type, PsiBuilder builder) {
    final PsiBuilder.Marker marker = builder.mark();
    builder.advanceLexer();
    marker.done(type);
  }
}

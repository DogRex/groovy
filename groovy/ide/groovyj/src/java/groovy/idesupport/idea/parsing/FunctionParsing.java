package groovy.idesupport.idea.parsing;

import com.intellij.lang.PsiBuilder;
import groovy.idesupport.idea.GroovyElementTypes;
import groovy.idesupport.idea.GroovyTokenTypes;
import com.intellij.openapi.diagnostic.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Jan 28, 2005
 * Time: 1:20:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class FunctionParsing {
  private static final Logger LOG = Logger.getInstance("#idea.idesupport.idea.parsing.FunctionParsing");

  private FunctionParsing() { }  

  public static void parseFunctionExpression(PsiBuilder builder) {
    parseFunction(builder, true);
  }

  public static void parseFunctionDeclaration(PsiBuilder builder) {
    parseFunction(builder, false);
  }

  private static void parseFunction(PsiBuilder builder, boolean expressionContext) {
    LOG.assertTrue(builder.getTokenType() == GroovyTokenTypes.FUNCTION_KEYWORD);

    final PsiBuilder.Marker functionMarker = builder.mark();
    builder.advanceLexer();

    // Function name
    if (builder.getTokenType() == GroovyTokenTypes.IDENTIFIER) {
      builder.advanceLexer();
    }
    else {
      if (!expressionContext) {
        builder.error("function name expected");
      }
    }

    parseParameterList(builder);

    StatementParsing.parseFunctionBody(builder);

    functionMarker.done(expressionContext ? GroovyElementTypes.FUNCTION_EXPRESSION : GroovyElementTypes.FUNCTION_DECLARATION);
  }

  private static void parseParameterList(final PsiBuilder builder) {
    final PsiBuilder.Marker parameterList;
    if (builder.getTokenType() != GroovyTokenTypes.LPAR) {
      builder.error("( expected");
      parameterList = builder.mark(); // To have non-empty parameters list at all the time.
      parameterList.done(GroovyElementTypes.PARAMETER_LIST);
      return;
    }
    else {
      parameterList = builder.mark();
      builder.advanceLexer();
    }

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

      final PsiBuilder.Marker parameter = builder.mark();
      if (builder.getTokenType() == GroovyTokenTypes.IDENTIFIER) {
        builder.advanceLexer();
        parameter.done(GroovyElementTypes.FORMAL_PARAMETER);
      }
      else {
        builder.error("formal parameter name expected");
        parameter.rollbackTo();
      }
    }

    if (builder.getTokenType() == GroovyTokenTypes.RPAR) {
      builder.advanceLexer();
    }

    parameterList.done(GroovyElementTypes.PARAMETER_LIST);
  }
}

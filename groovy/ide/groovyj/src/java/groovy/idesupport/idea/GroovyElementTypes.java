package groovy.idesupport.idea;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.tree.IChameleonElementType;

public interface GroovyElementTypes {
  IElementType FILE = new GroovyElementType("FILE");
  IElementType EMBEDDED_CONTENT = new JSChameleonElementType("EMBEDDED_CONTENT");
  IElementType FUNCTION_DECLARATION = new GroovyElementType("FUNCTION_DECLARATION");
  IElementType PARAMETER_LIST = new GroovyElementType("PARAMETER_LIST");
  IElementType FORMAL_PARAMETER = new GroovyElementType("FORMAL_PARAMETER");
  IElementType VARIABLE = new GroovyElementType("VARIABLE");
  IElementType ARGUMENT_LIST = new GroovyElementType("ARGUMENT_LIST");

  // Statements
  IElementType BLOCK = new GroovyElementType("BLOCK");
  IElementType LABELED_STATEMENT = new GroovyElementType("LABELED_STATEMENT");
  IElementType EXPRESSION_STATEMENT = new GroovyElementType("EXPRESSION_STATEMENT");
  IElementType VAR_STATEMENT = new GroovyElementType("VAR_STATEMENT");
  IElementType EMPTY_STATEMENT = new GroovyElementType("EMPTY_STATEMENT");
  IElementType IF_STATEMENT = new GroovyElementType("IF_STATEMENT");
  IElementType CONTINUE_STATEMENT = new GroovyElementType("CONTINUE_STATEMENT");
  IElementType BREAK_STATEMENT = new GroovyElementType("BREAK_STATEMENT");
  IElementType WITH_STATEMENT = new GroovyElementType("WITH_STATEMENT");
  IElementType RETURN_STATEMENT = new GroovyElementType("RETURN_STATEMENT");
  IElementType THROW_STATEMENT = new GroovyElementType("THROW_STATEMENT");
  IElementType TRY_STATEMENT = new GroovyElementType("TRY_STATEMENT");
  IElementType CATCH_BLOCK = new GroovyElementType("CATCH_BLOCK");
  IElementType SWITCH_STATEMENT = new GroovyElementType("SWITCH_STATEMENT");
  IElementType CASE_CLAUSE = new GroovyElementType("CASE_CLAUSE");
  IElementType FOR_STATEMENT = new GroovyElementType("FOR_STATEMENT");
  IElementType FOR_IN_STATEMENT = new GroovyElementType("FOR_IN_STATEMENT");
  IElementType WHILE_STATEMENT = new GroovyElementType("WHILE_STATEMENT");
  IElementType DOWHILE_STATEMENT = new GroovyElementType("DOWHILE_STATEMENT");

  TokenSet STATEMENTS = TokenSet.create(new IElementType[] {
    BLOCK, LABELED_STATEMENT, VAR_STATEMENT, EMPTY_STATEMENT, IF_STATEMENT, CONTINUE_STATEMENT, BREAK_STATEMENT, WITH_STATEMENT,
    RETURN_STATEMENT, THROW_STATEMENT, TRY_STATEMENT, SWITCH_STATEMENT, FOR_IN_STATEMENT, FOR_STATEMENT, WHILE_STATEMENT, DOWHILE_STATEMENT,
    EXPRESSION_STATEMENT
  });

  // Expressions
  IElementType THIS_EXPRESSION = new GroovyElementType("THIS_EXPRESSION");
  IElementType REFERENCE_EXPRESSION = new GroovyElementType("REFERENCE_EXPRESSION");
  IElementType LITERAL_EXPRESSION = new GroovyElementType("LITERAL_EXPRESSION");
  IElementType PARENTHESIZED_EXPRESSION = new GroovyElementType("PARENTHESIZED_EXPRESSION");
  IElementType ARRAY_LITERAL_EXPRESSION = new GroovyElementType("ARRAY_LITERAL_EXPRESSION");
  IElementType OBJECT_LITERAL_EXPRESSION = new GroovyElementType("OBJECT_LITERAL_EXPRESSION");
  IElementType PROPERTY = new GroovyElementType("PROPERTY");
  IElementType ASSIGNMENT_EXPRESSION = new GroovyElementType("ASSIGNMENT_EXPRESSION");
  IElementType CONDITIONAL_EXPRESSION = new GroovyElementType("CONDITIONAL_EXPRESSION");
  IElementType BINARY_EXPRESSION = new GroovyElementType("BINARY_EXPRESSION");
  IElementType PREFIX_EXPRESSION = new GroovyElementType("PREFIX_EXPRESSION");
  IElementType POSTFIX_EXPRESSION = new GroovyElementType("POSTFIX_EXPRESSION");
  IElementType COMMA_EXPRESSION = new GroovyElementType("COMMA_EXPRESSION");
  IElementType FUNCTION_EXPRESSION = new GroovyElementType("FUNCTION_EXPRESSION");
  IElementType NEW_EXPRESSION = new GroovyElementType("NEW_EXPRESSION");
  IElementType INDEXED_PROPERTY_ACCESS_EXPRESSION = new GroovyElementType("INDEXED_PROPERTY_ACCESS_EXPRESSION");
  IElementType CALL_EXPRESSION = new GroovyElementType("CALL_EXPRESSION");

  TokenSet EXPRESSIONS = TokenSet.create(new IElementType[] {
    THIS_EXPRESSION, REFERENCE_EXPRESSION, LITERAL_EXPRESSION, PARENTHESIZED_EXPRESSION, ARRAY_LITERAL_EXPRESSION,
    OBJECT_LITERAL_EXPRESSION, ASSIGNMENT_EXPRESSION, CONDITIONAL_EXPRESSION, BINARY_EXPRESSION, PREFIX_EXPRESSION,
    POSTFIX_EXPRESSION, COMMA_EXPRESSION, FUNCTION_EXPRESSION, NEW_EXPRESSION, INDEXED_PROPERTY_ACCESS_EXPRESSION,  CALL_EXPRESSION
  });
}

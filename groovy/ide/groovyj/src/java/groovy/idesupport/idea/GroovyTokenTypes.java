package groovy.idesupport.idea;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

// todo: not yet complete

public interface GroovyTokenTypes {
  IElementType IDENTIFIER = new GroovyElementType("IDENTIFIER");
  IElementType WHITE_SPACE = TokenType.WHITE_SPACE;
  IElementType BAD_CHARACTER = TokenType.BAD_CHARACTER;

  IElementType END_OF_LINE_COMMENT = new GroovyElementType("END_OF_LINE_COMMENT");
  IElementType C_STYLE_COMMENT = new GroovyElementType("C_STYLE_COMMENT");
  IElementType DOC_COMMENT = new GroovyElementType("DOC_COMMENT");

  // Keywords:
  IElementType BREAK_KEYWORD = new GroovyElementType("BREAK_KEYWORD");
  IElementType CASE_KEYWORD = new GroovyElementType("CASE_KEYWORD");
  IElementType CATCH_KEYWORD = new GroovyElementType("CATCH_KEYWORD");
  IElementType CONTINUE_KEYWORD = new GroovyElementType("CONTINUE_KEYWORD");
  IElementType DELETE_KEYWORD = new GroovyElementType("DELETE_KEYWORD");
  IElementType DEFAULT_KEYWORD = new GroovyElementType("DEFAULT_KEYWORD");
  IElementType DO_KEYWORD = new GroovyElementType("DO_KEYWORD");
  IElementType ELSE_KEYWORD = new GroovyElementType("ELSE_KEYWORD");
  IElementType FINALLY_KEYWORD = new GroovyElementType("FINALLY_KEYWORD");
  IElementType FOR_KEYWORD = new GroovyElementType("FOR_KEYWORD");
  IElementType FUNCTION_KEYWORD = new GroovyElementType("FUNCTION_KEYWORD");
  IElementType IF_KEYWORD = new GroovyElementType("IF_KEYWORD");
  IElementType IN_KEYWORD = new GroovyElementType("IN_KEYWORD");
  IElementType INSTANCEOF_KEYWORD = new GroovyElementType("INSTANCEOF_KEYWORD");
  IElementType NEW_KEYWORD = new GroovyElementType("NEW_KEYWORD");
  IElementType RETURN_KEYWORD = new GroovyElementType("RETURN_KEYWORD");
  IElementType SWITCH_KEYWORD = new GroovyElementType("SWITCH_KEYWORD");
  IElementType THIS_KEYWORD = new GroovyElementType("THIS_KEYWORD");
  IElementType THROW_KEYWORD = new GroovyElementType("THROW_KEYWORD");
  IElementType TRY_KEYWORD = new GroovyElementType("TRY_KEYWORD");
  IElementType TYPEOF_KEYWORD = new GroovyElementType("TYPEOF_KEYWORD");
  IElementType VAR_KEYWORD = new GroovyElementType("VAR_KEYWORD");
  IElementType VOID_KEYWORD = new GroovyElementType("VOID_KEYWORD");
  IElementType WHILE_KEYWORD = new GroovyElementType("WHILE_KEYWORD");
  IElementType WITH_KEYWORD = new GroovyElementType("WITH_KEYWORD");

  // Hardcoded literals
  IElementType TRUE_KEYWORD = new GroovyElementType("TRUE_KEYWORD");
  IElementType FALSE_KEYWORD = new GroovyElementType("FALSE_KEYWORD");
  IElementType NULL_KEYWORD = new GroovyElementType("NULL_KEYWORD");

  TokenSet KEYWORDS = TokenSet.create(
                        new IElementType[]{BREAK_KEYWORD, CASE_KEYWORD, CATCH_KEYWORD, CONTINUE_KEYWORD, DELETE_KEYWORD, DEFAULT_KEYWORD,
                            DO_KEYWORD, ELSE_KEYWORD, FINALLY_KEYWORD, FOR_KEYWORD, FUNCTION_KEYWORD, IF_KEYWORD, IN_KEYWORD,
                            INSTANCEOF_KEYWORD, NEW_KEYWORD, RETURN_KEYWORD, SWITCH_KEYWORD, THIS_KEYWORD, THROW_KEYWORD,
                            TRY_KEYWORD, TYPEOF_KEYWORD, VAR_KEYWORD, VOID_KEYWORD, WHILE_KEYWORD, WITH_KEYWORD, TRUE_KEYWORD, FALSE_KEYWORD, NULL_KEYWORD});

  // Literals
  IElementType NUMERIC_LITERAL = new GroovyElementType("NUMERIC_LITERAL");
  IElementType STRING_LITERAL = new GroovyElementType("STRING_LITERAL");
  IElementType REGEXP_LITERAL = new GroovyElementType("REGEXP_LITERAL");

  // Punctuators
  IElementType LBRACE = new GroovyElementType("LBRACE");// {
  IElementType RBRACE = new GroovyElementType("RBRACE");// }
  IElementType LPAR = new GroovyElementType("LPAR");// (
  IElementType RPAR = new GroovyElementType("RPAR");// )
  IElementType LBRACKET = new GroovyElementType("LBRACKET");// [
  IElementType RBRACKET = new GroovyElementType("RBRACKET");// ]
  IElementType DOT = new GroovyElementType("DOT");// .
  IElementType SEMICOLON = new GroovyElementType("SEMICOLON");// ;
  IElementType COMMA = new GroovyElementType("COMMA");// ,

  IElementType LT = new GroovyElementType("LT");// <
  IElementType GT = new GroovyElementType("GT");// >
  IElementType LE = new GroovyElementType("LE");// <=
  IElementType GE = new GroovyElementType("GE");// >=
  IElementType EQEQ = new GroovyElementType("EQEQ");// ==
  IElementType NE = new GroovyElementType("NE");// !=
  IElementType EQEQEQ = new GroovyElementType("EQEQEQ");// ===
  IElementType NEQEQ = new GroovyElementType("NEQEQ");// !==
  IElementType PLUS = new GroovyElementType("PLUS");// +
  IElementType MINUS = new GroovyElementType("MINUS");// -
  IElementType MULT = new GroovyElementType("MULT");// *
  IElementType PERC = new GroovyElementType("PERC");// %
  IElementType PLUSPLUS = new GroovyElementType("PLUSPLUS");// ++
  IElementType MINUSMINUS = new GroovyElementType("MINUSMINUS");// --
  IElementType LTLT = new GroovyElementType("LTLT");// <<
  IElementType GTGT = new GroovyElementType("GTGT");// >>
  IElementType GTGTGT = new GroovyElementType("GTGTGT");// >>>
  IElementType AND = new GroovyElementType("AND");// &
  IElementType OR = new GroovyElementType("OR");// |
  IElementType XOR = new GroovyElementType("XOR");// ^
  IElementType EXCL = new GroovyElementType("EXCL");// !
  IElementType TILDE = new GroovyElementType("TILDE");// ~
  IElementType ANDAND = new GroovyElementType("ANDAND");// &&
  IElementType OROR = new GroovyElementType("OROR");// ||
  IElementType QUEST = new GroovyElementType("QUEST");// ?
  IElementType COLON = new GroovyElementType("COLON");// :
  IElementType EQ = new GroovyElementType("EQ");// =
  IElementType PLUSEQ = new GroovyElementType("PLUSEQ");// +=
  IElementType MINUSEQ = new GroovyElementType("MINUSEQ");// -=
  IElementType MULTEQ = new GroovyElementType("MULTEQ");// *=
  IElementType PERCEQ = new GroovyElementType("PERCEQ");// %=
  IElementType LTLTEQ = new GroovyElementType("LTLTEQ");// <<=
  IElementType GTGTEQ = new GroovyElementType("GTGTEQ");// >>=
  IElementType GTGTGTEQ = new GroovyElementType("GTGTGTEQ");// >>>=
  IElementType ANDEQ = new GroovyElementType("ANDEQ");// &=
  IElementType OREQ = new GroovyElementType("OREQ");// |=
  IElementType XOREQ = new GroovyElementType("XOREQ");// ^=
  IElementType DIV = new GroovyElementType("DIV"); // /
  IElementType DIVEQ = new GroovyElementType("DIVEQ"); // /=

  TokenSet OPERATIONS = TokenSet.create(
    new IElementType[]{LT, GT, LE, GE, EQEQ, NE, EQEQEQ, NEQEQ, PLUS, MINUS, MULT, PERC, PLUSPLUS, MINUSMINUS, LTLT, GTGT, GTGTGT, AND, OR,
                       XOR, EXCL, TILDE, ANDAND, OROR, QUEST, COLON, EQ, PLUSEQ, MINUSEQ, MULTEQ, PERCEQ, LTLTEQ, GTGTEQ, GTGTGTEQ, ANDEQ,
                       OREQ, XOREQ, DIV, DIVEQ, COMMA
    });

  TokenSet ASSIGNMENT_OPERATIONS = TokenSet.create(new IElementType[] {
    EQ, PLUSEQ, MINUSEQ, MULTEQ, PERCEQ, LTLTEQ, GTGTEQ, GTGTGTEQ, ANDEQ,
    OREQ, XOREQ, DIVEQ
  });


  TokenSet EQUALITY_OPERATIONS = TokenSet.create(new IElementType[] {
    EQEQ, NE, EQEQEQ, NEQEQ
  });

  TokenSet RELATIONAL_OPERATIONS = TokenSet.create(new IElementType[] {
    LT, GT, LE, GE, INSTANCEOF_KEYWORD, IN_KEYWORD
  });

  TokenSet ADDITIVE_OPERATIONS = TokenSet.create(new IElementType[] {
    PLUS, MINUS
  });

  TokenSet MULTIPLICATIVE_OPERATIONS = TokenSet.create(new IElementType[] {
    MULT, DIV, PERC
  });

  TokenSet SHIFT_OPERATIONS = TokenSet.create(new IElementType[] {
    LTLT, GTGT, GTGTGT
  });

  TokenSet UNARY_OPERATIONS = TokenSet.create(new IElementType[] {
    PLUS, MINUS, PLUSPLUS, MINUSMINUS, TILDE, EXCL, TYPEOF_KEYWORD, VOID_KEYWORD, DELETE_KEYWORD
  });

  TokenSet COMMENTS = TokenSet.create(new IElementType[] {END_OF_LINE_COMMENT, DOC_COMMENT, C_STYLE_COMMENT});
}

/* It's an automatically generated code. Do not modify it. */
package com.intellij.lang.javascript;

import com.intellij.lexer.Lexer;
import com.intellij.psi.tree.IElementType;

%%

%init{
    myTokenType = null;
%init}

%{
  private IElementType myTokenType;
  private int myState;

  public _JavaScriptLexer(){
    this((java.io.Reader)null);
  }

  public final void start(char[] buffer){
    start(buffer, 0, buffer.length);
  }

  public final void start(char[] buffer, int startOffset, int endOffset){
    start(buffer, startOffset, endOffset, (short)YYINITIAL);
  }

  public final void start(char[] buffer, int startOffset, int endOffset, int initialState){
    zzBuffer = buffer;
    zzCurrentPos = zzMarkedPos = zzStartRead = startOffset;
    zzPushbackPos = 0;
    zzAtEOF  = false;
    zzEndRead = endOffset;
    myTokenType = null;
    yybegin(initialState);
  }

  public final int getState(){
    return myState;
  }

  public final int getLastState() {
    return 1;
  }

  public final IElementType getTokenType(){
    locateToken();
    return myTokenType;
  }

  public final int getTokenStart(){
    locateToken();
    return zzStartRead - zzPushbackPos;
  }

  public final int getTokenEnd(){
    return getTokenStart() + yylength();
  }

  public final void advance(){
    locateToken();
    myTokenType = null;
    myState = (short)yystate();
  }

  public final char[] getBuffer(){
    return zzBuffer;
  }

  public final int getBufferEnd(){
    return zzEndRead;
  }

  protected final void locateToken(){
    if (myTokenType != null) return;
    try{
    _locateToken();
    }
    catch(java.io.IOException ioe){}
  }

  public int getSmartUpdateShift() {
    return 10;
  }

  public Object clone() {
    try{
      return super.clone();
    }
    catch(CloneNotSupportedException e){
      return null;
    }
  }
%}

%class _JavaScriptLexer
%implements Lexer, Cloneable
%function _locateToken
%type void
%eof{  myTokenType = null; return;
%eof}

ALPHA=[A-Za-z_"$"]
DIGIT=[0-9]
OCTAL_DIGIT=[0-7]
HEX_DIGIT=[0-9A-Fa-f]
WHITE_SPACE_CHAR=[\ \n\r\t\f]

IDENTIFIER={ALPHA}({ALPHA}|{DIGIT})*

C_STYLE_COMMENT=("/*"[^"*"]{COMMENT_TAIL})|"/*"
DOC_COMMENT="/*""*"+("/"|([^"/""*"]{COMMENT_TAIL}))?
COMMENT_TAIL=([^"*"]*("*"+[^"*""/"])?)*("*"+"/")?
END_OF_LINE_COMMENT="/""/"[^\r\n]*

INTEGER_LITERAL={DECIMAL_INTEGER_LITERAL}|{HEX_INTEGER_LITERAL}
DECIMAL_INTEGER_LITERAL=(0|([1-9]({DIGIT})*))
HEX_INTEGER_LITERAL=0[Xx]({HEX_DIGIT})*

FLOAT_LITERAL=({FLOATING_POINT_LITERAL1})|({FLOATING_POINT_LITERAL2})|({FLOATING_POINT_LITERAL3})|({FLOATING_POINT_LITERAL4})
FLOATING_POINT_LITERAL1=({DIGIT})+"."({DIGIT})*({EXPONENT_PART})?
FLOATING_POINT_LITERAL2="."({DIGIT})+({EXPONENT_PART})?
FLOATING_POINT_LITERAL3=({DIGIT})+({EXPONENT_PART})
FLOATING_POINT_LITERAL4=({DIGIT})+
EXPONENT_PART=[Ee]["+""-"]?({DIGIT})*

STRING_LITERAL=({QUOTED_LITERAL})|({DOUBLE_QUOTED_LITERAL})
QUOTED_LITERAL="'"([^\\\'\r\n]|{ESCAPE_SEQUENCE})*("'"|\\)?
DOUBLE_QUOTED_LITERAL=\"([^\\\"\r\n]|{ESCAPE_SEQUENCE})*(\"|\\)?
ESCAPE_SEQUENCE=\\[^\r\n]

REGEXP_LITERAL="/"([^/\r\n]|{ESCAPE_SEQUENCE})*("/"[gim]*)?

%state DIV

%%

{WHITE_SPACE_CHAR}+   { myTokenType = JSElementTypes.WHITE_SPACE; return; }

{C_STYLE_COMMENT}     { myTokenType = JSElementTypes.C_STYLE_COMMENT; return; }
{END_OF_LINE_COMMENT} { myTokenType = JSElementTypes.END_OF_LINE_COMMENT; return; }
{DOC_COMMENT}         { myTokenType = JSElementTypes.DOC_COMMENT; return; }

{INTEGER_LITERAL}     { yybegin(DIV); myTokenType = JSElementTypes.NUMERIC_LITERAL; return; }
{FLOAT_LITERAL}       { yybegin(DIV); myTokenType = JSElementTypes.NUMERIC_LITERAL; return; }

{STRING_LITERAL}      { yybegin(DIV); myTokenType = JSElementTypes.STRING_LITERAL; return; }

"true"                { yybegin(DIV); myTokenType = JSElementTypes.TRUE_KEYWORD; return; }
"false"               { yybegin(DIV); myTokenType = JSElementTypes.FALSE_KEYWORD; return; }
"null"                { yybegin(DIV); myTokenType = JSElementTypes.NULL_KEYWORD; return; }

"break"               { yybegin(YYINITIAL); myTokenType = JSElementTypes.BREAK_KEYWORD; return; }
"case"                { yybegin(YYINITIAL); myTokenType = JSElementTypes.CASE_KEYWORD; return; }
"catch"               { yybegin(YYINITIAL); myTokenType = JSElementTypes.CATCH_KEYWORD; return; }
"continue"            { yybegin(YYINITIAL); myTokenType = JSElementTypes.CONTINUE_KEYWORD; return; }
"default"             { yybegin(YYINITIAL); myTokenType = JSElementTypes.DEFAULT_KEYWORD; return; }
"do"                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.DO_KEYWORD; return; }
"else"                { yybegin(YYINITIAL); myTokenType = JSElementTypes.ELSE_KEYWORD; return; }
"finally"             { yybegin(YYINITIAL); myTokenType = JSElementTypes.FINALLY_KEYWORD; return; }
"for"                 { yybegin(YYINITIAL); myTokenType = JSElementTypes.FOR_KEYWORD; return; }
"function"            { yybegin(YYINITIAL); myTokenType = JSElementTypes.FUNCTION_KEYWORD; return; }
"if"                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.IF_KEYWORD; return; }
"in"                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.IN_KEYWORD; return; }
"instanceof"          { yybegin(YYINITIAL); myTokenType = JSElementTypes.INSTANCEOF_KEYWORD; return; }
"new"                 { yybegin(YYINITIAL); myTokenType = JSElementTypes.NEW_KEYWORD; return; }
"return"              { yybegin(YYINITIAL); myTokenType = JSElementTypes.RETURN_KEYWORD; return; }
"switch"              { yybegin(YYINITIAL); myTokenType = JSElementTypes.SWITCH_KEYWORD; return; }
"this"                { yybegin(DIV);       myTokenType = JSElementTypes.THIS_KEYWORD; return; }
"throw"               { yybegin(YYINITIAL); myTokenType = JSElementTypes.THROW_KEYWORD; return; }
"try"                 { yybegin(YYINITIAL); myTokenType = JSElementTypes.TRY_KEYWORD; return; }
"typeof"              { yybegin(YYINITIAL); myTokenType = JSElementTypes.TYPEOF_KEYWORD; return; }
"var"                 { yybegin(YYINITIAL); myTokenType = JSElementTypes.VAR_KEYWORD; return; }
"void"                { yybegin(YYINITIAL); myTokenType = JSElementTypes.VOID_KEYWORD; return; }
"while"               { yybegin(YYINITIAL); myTokenType = JSElementTypes.WHILE_KEYWORD; return; }
"with"                { yybegin(YYINITIAL); myTokenType = JSElementTypes.WITH_KEYWORD; return; }

{IDENTIFIER}          { yybegin(DIV);       myTokenType = JSElementTypes.IDENTIFIER; return; }

"==="                 { yybegin(YYINITIAL); myTokenType = JSElementTypes.EQEQEQ; return; }
"!=="                 { yybegin(YYINITIAL); myTokenType = JSElementTypes.NEQEQ; return; }

"++"                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.PLUSPLUS; return; }
"--"                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.MINUSMINUS; return; }

"=="                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.EQEQ; return; }
"!="                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.NE; return; }
"<"                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.LT; return; }
">"                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.GT; return; }
"<="                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.LE; return; }
">="                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.GE; return; }

"<<"                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.LTLT; return; }
">>"                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.GTGT; return; }
">>>"                 { yybegin(YYINITIAL); myTokenType = JSElementTypes.GTGTGT; return; }

"&"                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.AND; return; }
"&&"                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.ANDAND; return; }
"|"                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.OR; return; }
"||"                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.OROR; return; }

"+="                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.PLUSEQ; return; }
"-="                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.MINUSEQ; return; }
"*="                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.MULTEQ; return; }
<DIV> "/="            { yybegin(YYINITIAL); myTokenType = JSElementTypes.DIVEQ; return; }
"&="                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.ANDEQ; return; }
"|="                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.OREQ; return; }
"^="                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.XOREQ; return; }
"%="                  { yybegin(YYINITIAL); myTokenType = JSElementTypes.PERCEQ; return; }
"<<="                 { yybegin(YYINITIAL); myTokenType = JSElementTypes.LTLTEQ; return; }
">>="                 { yybegin(YYINITIAL); myTokenType = JSElementTypes.GTGTEQ; return; }
">>>="                { yybegin(YYINITIAL); myTokenType = JSElementTypes.GTGTGTEQ; return; }

"("                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.LPAR; return; }
")"                   { yybegin(DIV);       myTokenType = JSElementTypes.RPAR; return; }
"{"                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.LBRACE; return; }
"}"                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.RBRACE; return; }
"["                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.LBRACKET; return; }
"]"                   { yybegin(DIV);       myTokenType = JSElementTypes.RBRACKET; return; }
";"                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.SEMICOLON; return; }
","                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.COMMA; return; }
"."                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.DOT; return; }

"="                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.EQ; return; }
"!"                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.EXCL; return; }
"~"                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.TILDE; return; }
"?"                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.QUEST; return; }
":"                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.COLON; return; }
"+"                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.PLUS; return; }
"-"                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.MINUS; return; }
"*"                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.MULT; return; }
<DIV> "/"             { yybegin(YYINITIAL); myTokenType = JSElementTypes.DIV; return; }
"^"                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.XOR; return; }
"%"                   { yybegin(YYINITIAL); myTokenType = JSElementTypes.PERC; return; }

<YYINITIAL> {REGEXP_LITERAL} { myTokenType = JSElementTypes.REGEXP_LITERAL; return; }

.                     { yybegin(YYINITIAL); myTokenType = JSElementTypes.BAD_CHARACTER; return; }
package groovy.idesupport.idea;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GroovyHighlighter extends SyntaxHighlighterBase {
    private static Map keys1;
    private static Map keys2;

    public Lexer getHighlightingLexer() {
        return new _JavaScriptLexer();
    }

    static final TextAttributesKey GROOVY_KEYWORD = TextAttributesKey.createTextAttributesKey(
                    "JS.KEYWORD",
                    HighlighterColors.JAVA_KEYWORD.getDefaultAttributes()
            );

    static final TextAttributesKey GROOVY_STRING = TextAttributesKey.createTextAttributesKey(
                    "JS.STRING",
                    HighlighterColors.JAVA_STRING.getDefaultAttributes()
            );

    static final TextAttributesKey GROOVY_NUMBER = TextAttributesKey.createTextAttributesKey(
                    "JS.NUMBER",
                    HighlighterColors.JAVA_NUMBER.getDefaultAttributes()
            );

    static final TextAttributesKey GROOVY_REGEXP = TextAttributesKey.createTextAttributesKey(
                    "JS.REGEXP",
                    new TextAttributes(Color.blue.brighter(), null, null, null, Font.PLAIN)
            );

    static final TextAttributesKey GROOVY_LINE_COMMENT = TextAttributesKey.createTextAttributesKey(
                    "JS.LINE_COMMENT",
                    HighlighterColors.JAVA_LINE_COMMENT.getDefaultAttributes()
            );

    static final TextAttributesKey GROOVY_BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey(
                    "JS.BLOCK_COMMENT",
                    HighlighterColors.JAVA_BLOCK_COMMENT.getDefaultAttributes()
            );

    static final TextAttributesKey GROOVY_DOC_COMMENT = TextAttributesKey.createTextAttributesKey(
                    "JS.DOC_COMMENT",
                    HighlighterColors.JAVA_DOC_COMMENT.getDefaultAttributes()
            );

    static final TextAttributesKey GROOVY_OPERATION_SIGN = TextAttributesKey.createTextAttributesKey(
                    "JS.OPERATION_SIGN",
                    HighlighterColors.JAVA_OPERATION_SIGN.getDefaultAttributes()
            );

    static final TextAttributesKey GROOVY_PARENTHS = TextAttributesKey.createTextAttributesKey(
                    "JS.PARENTHS",
                    HighlighterColors.JAVA_PARENTHS.getDefaultAttributes()
            );

    static final TextAttributesKey GROOVY_BRACKETS = TextAttributesKey.createTextAttributesKey(
                    "JS.BRACKETS",
                    HighlighterColors.JAVA_BRACKETS.getDefaultAttributes()
            );

    static final TextAttributesKey GROOVY_BRACES = TextAttributesKey.createTextAttributesKey(
                    "JS.BRACES",
                    HighlighterColors.JAVA_BRACES.getDefaultAttributes()
            );

    static final TextAttributesKey GROOVY_COMMA = TextAttributesKey.createTextAttributesKey(
                    "JS.COMMA",
                    HighlighterColors.JAVA_COMMA.getDefaultAttributes()
            );

    static final TextAttributesKey GROOVY_DOT = TextAttributesKey.createTextAttributesKey(
                    "JS.DOT",
                    HighlighterColors.JAVA_DOT.getDefaultAttributes()
            );

    static final TextAttributesKey GROOVY_SEMICOLON = TextAttributesKey.createTextAttributesKey(
                    "JS.SEMICOLON",
                    HighlighterColors.JAVA_SEMICOLON.getDefaultAttributes()
            );


    static {
        keys1 = new HashMap();
        keys2 = new HashMap();

        fillMap(keys1, GroovyTokenTypes.KEYWORDS, GROOVY_KEYWORD);
        fillMap(keys1, GroovyTokenTypes.OPERATIONS, GROOVY_OPERATION_SIGN);

        keys1.put(GroovyTokenTypes.NUMERIC_LITERAL, GROOVY_NUMBER);
        keys1.put(GroovyTokenTypes.STRING_LITERAL, GROOVY_STRING);
        keys1.put(GroovyTokenTypes.REGEXP_LITERAL, GROOVY_REGEXP);

        keys1.put(GroovyTokenTypes.LPAR, GROOVY_PARENTHS);
        keys1.put(GroovyTokenTypes.RPAR, GROOVY_PARENTHS);

        keys1.put(GroovyTokenTypes.LBRACE, GROOVY_BRACES);
        keys1.put(GroovyTokenTypes.RBRACE, GROOVY_BRACES);

        keys1.put(GroovyTokenTypes.LBRACKET, GROOVY_BRACKETS);
        keys1.put(GroovyTokenTypes.RBRACKET, GROOVY_BRACKETS);

        keys1.put(GroovyTokenTypes.COMMA, GROOVY_COMMA);
        keys1.put(GroovyTokenTypes.DOT, GROOVY_DOT);
        keys1.put(GroovyTokenTypes.SEMICOLON, GROOVY_SEMICOLON);

        keys1.put(GroovyTokenTypes.C_STYLE_COMMENT, GROOVY_BLOCK_COMMENT);
        keys1.put(GroovyTokenTypes.DOC_COMMENT, GROOVY_DOC_COMMENT);
        keys1.put(GroovyTokenTypes.END_OF_LINE_COMMENT, GROOVY_LINE_COMMENT);
        keys1.put(GroovyTokenTypes.BAD_CHARACTER, HighlighterColors.BAD_CHARACTER);
    }

    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(((TextAttributesKey) keys1.get(tokenType)), ((TextAttributesKey) keys2.get(tokenType)));
    }

    public Map getKeys1() {
        return (Map) ((HashMap) keys1).clone();
    }

    public Map getKeys2() {
        return (Map) ((HashMap) keys2).clone();
    }
}

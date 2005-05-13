/*
 * $Id$
 *
 * Copyright (c) 2005 The Codehaus - http://groovy.codehaus.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */


package org.codehaus.groovy.intellij.psi;

import com.intellij.psi.tree.IElementType;

public final class GroovyElementTypes {

    public static final IElementType FILE = new GroovyElementType("File");

/*
    public static final IElementType GROOVY_FILE = new IChameleonElementType("GROOVY_FILE_TEXT", GroovyLanguage.findOrCreate()) {
        public ASTNode parseContents(ASTNode astNode) {
            char[] characters = ((LeafElement) astNode).textToCharArray();
            PsiManager psiManager = getPsiManager(astNode);
            Lexer lexer = getLanguage().getParserDefinition().createLexer(psiManager.getProject());
            CharTable charTable = SharedImplUtil.findCharTableByTree(astNode);
            return FileTextParsing.parseFileText(psiManager, lexer, characters, 0, characters.length, charTable);
        }

        public boolean isParsable(CharSequence characterSequence, Project project) {
            return true;
        }

    };

    public static final IElementType IMPORT_LIST = new IChameleonElementType("IMPORT_LIST_TEXT", GroovyLanguage.findOrCreate()) {
        public ASTNode parseContents(ASTNode astNode) {
            char[] characters = ((LeafElement) astNode).textToCharArray();
            ParsingContext parsingContext = new ParsingContext(SharedImplUtil.findCharTableByTree(astNode));
            PsiManager psiManager = getPsiManager(astNode);
            Lexer lexer = getLanguage().getParserDefinition().createLexer(psiManager.getProject());
            return parsingContext.getImportsTextParsing().parseImportsText(psiManager, lexer, characters, 0, characters.length, ((LeafElement) astNode).getState());
        }

        public boolean isParsable(CharSequence characterSequence, Project project) {
            return false;
        }
    };

    public static final IElementType CODE_BLOCK = new IErrorCounterChameleonElementType("CODE_BLOCK", GroovyLanguage.findOrCreate()) {
        public ASTNode parseContents(ASTNode astNode) {
            char[] characters = ((LeafElement) astNode).textToCharArray();
            PsiManager psiManager = getPsiManager(astNode);
            Lexer lexer = getLanguage().getParserDefinition().createLexer(psiManager.getProject());
            CharTable charTable = SharedImplUtil.findCharTableByTree(astNode);
            return StatementParsing.parseCodeBlockText(psiManager, lexer, characters, 0, characters.length, ((LeafElement) astNode).getState(), charTable).getFirstChildNode();
        }

        public int getErrorsCount(CharSequence characterSequence, Project project) {
            Lexer lexer = getLanguage().getParserDefinition().createLexer(project);
            char[] characters = CharArrayUtil.fromSequence(characterSequence);
            lexer.startMarker(characters, 0, characters.length);

            int numberOfErrors = 0;
            for (IElementType elementType = lexer.getTokenType(); elementType != null; lexer.advance()) {
                if (elementType == GroovyTokenTypeMappings.getType(GroovyTokenTypes.LCURLY)) {
                    numberOfErrors++;
                } else if (elementType == GroovyTokenTypeMappings.getType(GroovyTokenTypes.RCURLY)) {
                    numberOfErrors--;
                }
            }
            return numberOfErrors;
        }
    };

    public static final IElementType EXPRESSION_STATEMENT = new IChameleonElementType("EXPRESSION_STATEMENT", GroovyLanguage.findOrCreate()) {
        public ASTNode parseContents(ASTNode astNode) {
            char[] characters = ((LeafElement) astNode).textToCharArray();
            ParsingContext parsingContext = new ParsingContext(SharedImplUtil.findCharTableByTree(astNode));
            PsiManager psiManager = getPsiManager(astNode);
            return parsingContext.getExpressionParsing().parseExpressionTextFragment(psiManager, characters, 0, characters.length, ((LeafElement) astNode).getState());
        }

        public boolean isParsable(CharSequence characterSequence, Project project) {
            return false;
        }
    };

    public static final IElementType STATEMENTS = new ICodeFragmentElementType("STATEMENTS", GroovyLanguage.findOrCreate()) {
        public ASTNode parseContents(ASTNode astNode) {
            char[] characters = ((LeafElement) astNode).textToCharArray();
            PsiManager psimanager = getPsiManager(astNode);
            return StatementParsing.parseStatements(psimanager, getLanguage().getParserDefinition().createLexer(psimanager.getProject()), characters, 0, characters.length, ((LeafElement) astNode).getState(), SharedImplUtil.findCharTableByTree(astNode));
        }

        public boolean isParsable(CharSequence characterSequence, Project project) {
            return false;
        }
    };

    public static final IElementType EXPRESSION_TEXT = new ICodeFragmentElementType("EXPRESSION_TEXT", GroovyLanguage.findOrCreate()) {
        public ASTNode parseContents(ASTNode astNode) {
            char ac[] = ((LeafElement) astNode).textToCharArray();
            ParsingContext parsingcontext = new ParsingContext(SharedImplUtil.findCharTableByTree(astNode));
            PsiManager psiManager = getPsiManager(astNode);
            return parsingcontext.getExpressionParsing().parseExpressionTextFragment(psiManager, ac, 0, ac.length, ((LeafElement) astNode).getState());
        }

        public boolean isParsable(CharSequence charsequence, Project project) {
            return false;
        }
    };

    public static final IElementType REFERENCE_TEXT = new ICodeFragmentElementType("REFERENCE_TEXT", GroovyLanguage.findOrCreate()) {
        public ASTNode parseContents(ASTNode astNode) {
            char[] characters = ((LeafElement) astNode).textToCharArray();
            CharTable charTable = SharedImplUtil.findCharTableByTree(astNode);
            PsiManager psiManager = getPsiManager(astNode);
            return Parsing.parseJavaCodeReferenceText(psiManager, characters, 0, characters.length, charTable, true);
        }

        public boolean isParsable(CharSequence characterSequence, Project project) {
            return false;
        }
    };

    public static final IElementType TYPE_TEXT = new ICodeFragmentElementType("TYPE_TEXT", GroovyLanguage.findOrCreate()) {
        public ASTNode parseContents(ASTNode astNode) {
            char[] characters = ((LeafElement) astNode).textToCharArray();
            PsiManager psiManager = getPsiManager(astNode);
            CharTable charTable = SharedImplUtil.findCharTableByTree(astNode);
            return Parsing.parseTypeText(psiManager, null, characters, 0, characters.length, 0, charTable);
        }

        public boolean isParsable(CharSequence characterSequence, Project project) {
            return false;
        }
    };

    private static final PsiManager getPsiManager(ASTNode astNode) {
        return astNode.getTreeParent().getPsi().getManager();
    }
*/
}

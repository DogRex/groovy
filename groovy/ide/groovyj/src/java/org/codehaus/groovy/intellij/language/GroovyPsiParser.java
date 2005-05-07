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


package org.codehaus.groovy.intellij.language;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.text.CharSequenceReader;

import org.codehaus.groovy.intellij.language.parser.GroovyLexer;
import org.codehaus.groovy.intellij.language.parser.GroovyRecognizer;

import antlr.RecognitionException;
import antlr.TokenStreamException;

public class GroovyPsiParser implements PsiParser {

    public ASTNode parse(IElementType rootElementType, PsiBuilder builder) {
//        UnicodeEscapingReader reader = new UnicodeEscapingReader(new CharSequenceReader(builder.getOriginalText()));
        CharSequenceReader reader = new CharSequenceReader(builder.getOriginalText());
        GroovyLexer lexer = new GroovyLexer(reader);
//        reader.setLexer(lexer);
        GroovyRecognizer parser = GroovyRecognizer.make(lexer);
//        GroovyRecognizerTree treeParser = new GroovyRecognizerTree(builder);

        try {
            parser.compilationUnit();
        } catch (TokenStreamException e) {
        } catch (RecognitionException e) {
        }
        return builder.getTreeBuilt();
    }
}

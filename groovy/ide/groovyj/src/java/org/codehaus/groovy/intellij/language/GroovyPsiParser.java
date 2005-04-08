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

import java.lang.reflect.Field;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.impl.PsiBuilderImpl;
import com.intellij.psi.tree.IElementType;

import org.codehaus.groovy.antlr.AntlrParserPlugin;

import org.codehaus.groovy.intellij.language.parser.GroovyPsiRecognizer;

import antlr.RecognitionException;
import antlr.TokenStreamException;

public class GroovyPsiParser extends AntlrParserPlugin implements PsiParser {
///CLOVER:OFF
    public ASTNode parse(IElementType root, final PsiBuilder builder) {
        System.out.println("in parse(IElementType " + root.toString() + ", PsiBuilder " + builder.getClass().getName() + ") ");

        try {
            Field lexerField = PsiBuilderImpl.class.getDeclaredField("d");
            lexerField.setAccessible(true);
            Object o = lexerField.get(builder);

            System.out.println("class: " + o.getClass().getName());

            GroovyLexerAdapter lexerAdapter = (GroovyLexerAdapter) o;
            new GroovyPsiRecognizer(builder, lexerAdapter.groovyLexer).compilationUnit();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (TokenStreamException e) {
            throw new RuntimeException(e);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }

        return builder.getTreeBuilt();
    }
///CLOVER:ON
}

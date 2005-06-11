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


package org.codehaus.groovy.intellij.language.editor;

import com.intellij.lexer.LayeredLexer;
import com.intellij.lexer.StringLiteralLexer;
import com.intellij.psi.tree.IElementType;

import org.codehaus.groovy.intellij.language.GroovyPsiBuilder;
import org.codehaus.groovy.intellij.language.parser.GroovyTokenTypes;
import org.codehaus.groovy.intellij.psi.GroovyTokenTypeMappings;

public class GroovyHighlightingLexer extends LayeredLexer {

    public GroovyHighlightingLexer(GroovyPsiBuilder groovyPsiBuilder) {
        super(groovyPsiBuilder.getLexer());

        IElementType stringLiteralElementType = GroovyTokenTypeMappings.getType(GroovyTokenTypes.STRING_LITERAL);
        registerSelfStoppingLayer(new StringLiteralLexer('"', stringLiteralElementType), new IElementType[] { stringLiteralElementType}, IElementType.EMPTY_ARRAY);
/*
        registerSelfStoppingLayer(new StringLiteralLexer('\'', stringLiteralElementType), new IElementType[] { stringLiteralElementType}, IElementType.EMPTY_ARRAY);

        LayeredLexer layeredLexer = new LayeredLexer(new JavaDocLexer());
        HtmlHighlightingLexer htmlHighlightingLexer = new HtmlHighlightingLexer();
        htmlHighlightingLexer.setHasNoEmbeddments(true);

        layeredLexer.registerLayer(htmlHighlightingLexer, new IElementType[]{JavaDocTokenType.DOC_COMMENT_DATA});
        registerSelfStoppingLayer(layeredLexer, new IElementType[]{JavaTokenType.DOC_COMMENT}, new IElementType[]{JavaDocTokenType.DOC_COMMENT_END});
*/
    }
}

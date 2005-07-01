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


package org.codehaus.groovy.intellij.compiler;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.control.messages.SimpleMessage;
import org.codehaus.groovy.control.messages.WarningMessage;
import org.codehaus.groovy.syntax.SyntaxException;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.jmock.core.Constraint;
import org.jmock.core.MockObjectSupportTestCase;

import groovy.lang.GroovyRuntimeException;

class CompileContextBuilder {

    private final MockObjectTestCase testCase;
    private final Mock mockCompileContext;

    CompileContextBuilder(MockObjectTestCase testCase) {
        this.testCase = testCase;
        mockCompileContext = testCase.mock(CompileContext.class);
    }

    CompileContextBuilder linking(Module module, VirtualFile[] files) {
        for (VirtualFile file : files) {
            mockCompileContext.expects(testCase.once()).method("getModuleByFile")
                    .with(testCase.same(file))
                    .will(testCase.returnValue(module));
        }
        return this;
    }

    CompileContextBuilder expectsAddMessage(CompilerMessageCategory messageCategory, VirtualFile file, SyntaxException syntaxException) {
        mockCompileContext.expects(testCase.once()).method("addMessage")
                .with(new Constraint[] { testCase.same(messageCategory),
                                         testCase.eq(syntaxException.getMessage()),
                                         testCase.eq(file.getUrl()),
                                         testCase.eq(syntaxException.getLine()),
                                         testCase.eq(syntaxException.getStartColumn()) });
        return this;
    }

    CompileContextBuilder expectsAddMessage(CompilerMessageCategory messageCategory,
                                            GroovyRuntimeException exception, ASTNode astNode) {
        mockCompileContext.expects(testCase.once()).method("addMessage")
                .with(new Constraint[] { testCase.same(messageCategory),
                                         testCase.eq(exception.getMessageWithoutLocationText()),
                                         testCase.eq(exception.getModule().getDescription()),
                                         testCase.eq(astNode.getLineNumber()),
                                         testCase.eq(astNode.getColumnNumber()) });
        return this;
    }

    CompileContextBuilder expectsAddMessage(CompilerMessageCategory messageCategory, Exception exception) {
        mockCompileContext.expects(testCase.once()).method("addMessage")
                .with(new Constraint[] { testCase.same(messageCategory), testCase.eq(exception.getMessage()),
                                         testCase.eq(null), testCase.eq(-1), testCase.eq(-1) });
        return this;
    }

    CompileContextBuilder expectsAddMessage(CompilerMessageCategory messageCategory, SimpleMessage message) {
        mockCompileContext.expects(testCase.once()).method("addMessage")
                .with(new Constraint[] { testCase.same(messageCategory), testCase.eq(message.getMessage()),
                                         MockObjectSupportTestCase.NULL, testCase.eq(-1), testCase.eq(-1) });
        return this;
    }

    CompileContextBuilder expectsAddMessage(CompilerMessageCategory messageCategory, WarningMessage warningMessage) {
        mockCompileContext.expects(testCase.once()).method("addMessage")
                .with(new Constraint[] { testCase.same(messageCategory), testCase.eq(warningMessage.getMessage()),
                                         MockObjectSupportTestCase.NULL, testCase.eq(-1), testCase.eq(-1) });
        return this;
    }

    CompileContextBuilder expectsAddDefaultErrorMessage(String message) {
        mockCompileContext.expects(testCase.once()).method("addMessage")
                .with(new Constraint[] { testCase.same(CompilerMessageCategory.ERROR), testCase.eq("An unknown error occurred."),
                                         MockObjectSupportTestCase.NULL, testCase.eq(-1), testCase.eq(-1) });
        return this;
    }

    CompileContext build() {
        return (CompileContext) mockCompileContext.proxy();
    }
}

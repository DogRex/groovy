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

import junitx.framework.Assert;
import junitx.framework.ObjectAssert;

import org.intellij.openapi.testing.MockApplication;
import org.intellij.openapi.testing.MockApplicationManager;

import com.intellij.codeFormatting.PseudoText;
import com.intellij.codeFormatting.PseudoTextElementFactory;
import com.intellij.codeFormatting.TreeBasedPseudoText;
import com.intellij.codeFormatting.general.PseudoTextElementFactoryImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.java.JavaFileElement;

import org.jmock.Mock;

import org.codehaus.groovy.intellij.GroovyjTestCase;

public class GroovyPseudoTextBuilderTest extends GroovyjTestCase {

    public void testBuildsAPseudoTextFrom() {
        MockApplication application = MockApplicationManager.getMockApplication();
        application.registerComponent(PseudoTextElementFactory.class, new PseudoTextElementFactoryImpl());

        PseudoText pseudoText = new GroovyPseudoTextBuilder().build(null, null, createStubbedSource());
        Assert.assertNotEquals("pseudo text", null, pseudoText);
        ObjectAssert.assertInstanceOf("pseudo text", TreeBasedPseudoText.class, pseudoText);

        application.removeComponent(PseudoTextElementFactory.class);
    }

    private PsiElement createStubbedSource() {
        Mock stubSource = mock(PsiElement.class, "stubSource");
        Mock stubContainingFile = mock(PsiFile.class, "stubContainingFile");
        stubSource.stubs().method("getContainingFile").will(returnValue(stubContainingFile.proxy()));
        stubContainingFile.stubs().method("getNode").will(returnValue(new JavaFileElement()));  // TODO: replace with GroovyFileElement later
        return (PsiElement) stubSource.proxy();
    }
}

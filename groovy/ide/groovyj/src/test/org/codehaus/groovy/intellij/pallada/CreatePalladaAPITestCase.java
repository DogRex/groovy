/*
 * $Id$
 *
 * Copyright (c) 2004 The Codehaus - http://groovy.codehaus.org
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


package org.codehaus.groovy.intellij.pallada;

import junitx.framework.StringAssert;

import org.codehaus.groovy.intellij.EditorAPI;
import org.codehaus.groovy.intellij.EditorAPIFactory;
import org.codehaus.groovy.intellij.EditorAPIFactoryTest;

public class CreatePalladaAPITestCase extends EditorAPIFactoryTest {

	public void testCanCreateEditorApiUnderPalladaWithinEapBuildsBoundaries() {
		mockApplicationInfo.expects(once()).method("getVersionName").will(returnValue("a string containing the name PaLlaDa..."));
		mockApplicationInfo.expects(once()).method("getBuildNumber").will(returnValue("2079"));
		EditorAPI editorAPI = new EditorAPIFactory().getEditorAPI(null);
		StringAssert.assertContains("PalladaAPI", editorAPI.getClass().getName());

		mockApplicationInfo.expects(once()).method("getVersionName").will(returnValue("a string containing the name PaLlaDa..."));
		mockApplicationInfo.expects(once()).method("getBuildNumber").will(returnValue("2239"));
		editorAPI = new EditorAPIFactory().getEditorAPI(null);
		StringAssert.assertContains("PalladaAPI", editorAPI.getClass().getName());
	}
}

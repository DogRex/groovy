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

import com.intellij.codeInsight.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

public final class CodeInsightAttributes {

    public static final TextAttributesKey WRONG_REFERENCE_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("WRONG_REFERENCE_ATTRIBUTES", CodeInsightColors.WRONG_REFERENCES_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey ERROR_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("ERROR_ATTRIBUTES", CodeInsightColors.ERRORS_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey WARNING_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("WARNING_ATTRIBUTES", CodeInsightColors.WARNINGS_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey DEPRECATED_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("DEPRECATED_ATTRIBUTES", CodeInsightColors.DEPRECATED_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey NOT_USED_ELEMENT_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("NOT_USED_ELEMENT_ATTRIBUTES", CodeInsightColors.NOT_USED_ELEMENT_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey CLASS_NAME_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("CLASS_NAME_ATTRIBUTES", CodeInsightColors.CLASS_NAME_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey INTERFACE_NAME_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("INTERFACE_NAME_ATTRIBUTES", CodeInsightColors.INTERFACE_NAME_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey LOCAL_VARIABLE_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("LOCAL_VARIABLE_ATTRIBUTES", CodeInsightColors.LOCAL_VARIABLE_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey MUTABLE_LOCAL_VARIABLE_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("MUTABLE_LOCAL_VARIABLE_ATTRIBUTES", CodeInsightColors.MUTABLE_LOCAL_VARIABLE_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey MUTABLE_PARAMETER_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("MUTABLE_PARAMETER_ATTRIBUTES", CodeInsightColors.MUTABLE_PARAMETER_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey INSTANCE_FIELD_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("INSTANCE_FIELD_ATTRIBUTES", CodeInsightColors.INSTANCE_FIELD_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey STATIC_FIELD_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("STATIC_FIELD_ATTRIBUTES", CodeInsightColors.STATIC_FIELD_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey PARAMETER_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("PARAMETER_ATTRIBUTES", CodeInsightColors.PARAMETER_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey METHOD_CALL_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("METHOD_CALL_ATTRIBUTES", CodeInsightColors.METHOD_CALL_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey METHOD_DECLARATION_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("METHOD_DECLARATION_ATTRIBUTES", CodeInsightColors.METHOD_DECLARATION_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey CONSTRUCTOR_CALL_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("CONSTRUCTOR_CALL_ATTRIBUTES", CodeInsightColors.CONSTRUCTOR_CALL_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey CONSTRUCTOR_DECLARATION_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("CONSTRUCTOR_DECLARATION_ATTRIBUTES", CodeInsightColors.CONSTRUCTOR_DECLARATION_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey STATIC_METHOD_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("STATIC_METHOD_ATTRIBUTES", CodeInsightColors.STATIC_METHOD_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey MATCHED_BRACE_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("MATCHED_BRACE_ATTRIBUTES", CodeInsightColors.MATCHED_BRACE_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey UNMATCHED_BRACE_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("UNMATCHED_BRACE_ATTRIBUTES", CodeInsightColors.UNMATCHED_BRACE_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey ANNOTATION_NAME_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("ANNOTATION_NAME_ATTRIBUTES", CodeInsightColors.ANNOTATION_NAME_ATTRIBUTES.getDefaultAttributes());
    public static final TextAttributesKey ANNOTATION_ATTRIBUTE_NAME_ATTRIBUTES = TextAttributesKey.createTextAttributesKey("ANNOTATION_ATTRIBUTE_NAME_ATTRIBUTES", CodeInsightColors.ANNOTATION_ATTRIBUTE_NAME_ATTRIBUTES.getDefaultAttributes());
}

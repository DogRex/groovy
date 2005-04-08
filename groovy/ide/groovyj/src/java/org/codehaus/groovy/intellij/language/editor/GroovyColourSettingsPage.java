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

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;

import com.intellij.codeInsight.CodeInsightColors;
import com.intellij.debugger.settings.DebuggerColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.ColorKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;

import org.codehaus.groovy.intellij.GroovySupportLoader;

public class GroovyColourSettingsPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] ATTRIBUTES_DESCRIPTORS;
    private static final ColorDescriptor[] COLOR_DESCRIPTORS;

    private static final Map ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP = new HashMap();

    /*
     * DO NOT attempt to break the following string into smaller chuncks BUT RATHER install the StringEditor plug-in
     * to get a more readable and editable view of this code snippet.
     */
    private static final String DEMO_TEXT =
            "#!/usr/bin/groovy\n\nimport <class>javax.swing.JPanel</class>\nimport <class>groovy.swing.SwingBuilder</class>\n                               Bad characters: \\n #\n/**\n * Doc comments for <code>SomeClass</code>.\n *\n * @author The Codehaus\n *\n * @see <class>GroovyTestCase</class>#<methodCall>setUp</methodCall>()\n */\n<annotationName>@Annotation</annotationName> (<annotationAttributeName>name</annotationAttributeName>=value)\npublic class <class>SomeClass</class> extends <class>GroovyTestCase</class> { // some comment\n\n    /* Block comment */\n    <annotationName>@Property</annotationName> def <field>author</field> = \"Joe Bloggs\"\n    <annotationName>@Property</annotationName> <class>Long</class> <field>random</field> = 1101001010010110L;\n\n    private <class>String</class> <field>field</field> = \"Hello World\";\n    private double <unusedField>unusedField</unusedField> = 12345.67890;\n    private <unknownType>UnknownType</unknownType> <field>anotherString</field> = \"Another\\nStrin\\g\";\n    private int[] <field>array</field> = new int[] { 1, 2, 3 };\n    public static int <static>staticField</static> = 0;\n\n    public <constructorDeclaration>SomeClass</constructorDeclaration>(<interface>AnInterface</interface> <param>param</param>) {\n        <todo>//TODO: something</todo>\n        <error>int <localVar>localVar</localVar> = \"IntelliJ\"</error>; // Error, incompatible types\n        <class>System</class>.<static>out</static>.<methodCall>println</methodCall>(<field>anotherString</field> + <field>field</field> + <localVar>localVar</localVar>);\n        long <localVar>time</localVar> = <class>Date</class>.<static_method><deprecated>parse</deprecated></static_method>(\"1.2.3\"); // Method is deprecated\n        int <localVar>value</localVar> = this.<warning>staticField</warning>; \n    }\n\n    interface <interface>AnInterface</interface> {\n        int <static>CONSTANT</static> = 2;\n    }\n\n    void <methodDeclaration>testDoesANumberOfRandomThings</methodDeclaration>(int <mutableParameter>count</mutableParameter>) {/*  block\n                     comment */\n        new <constructorCall>SomeClass</constructorCall>();\n\n        def <localVar>quadraticClosure</localVar> = { a, b :: <class>Math</class>.<static_method>pow</static_method>(a, 2) + (2 * a * b) + <class>Math</class>.<static_method>pow</static_method>(b, 2) }\n        <localVar>quadraticClosure</localVar>.<methodCall>applyTo</methodCall>(3, 5)\n\n        <class>List</class> <mutableLocalVar>aList</mutableLocalVar> = [ 3, 5 ]\n        assert [ 9, 25 ] == <mutableLocalVar>aList</mutableLocalVar>.<methodCall>collect</methodCall> { i :: <class>Math</class>.<static_method>pow</static_method>(i, 2) }, 'assertion failed'\n\n        <mutableParameter>count</mutableParameter>++\n    }\n}\n";

    private static final ColorKey METHOD_SEPARATORS_COLOR = ColorKey.createColorKey("METHOD_SEPARATORS_COLOR", CodeInsightColors.METHOD_SEPARATORS_COLOR.getDefaultColor());

    static {
        ATTRIBUTES_DESCRIPTORS = new AttributesDescriptor[] {
            new AttributesDescriptor("Keyword", SyntacticAttributes.GROOVY_KEYWORD),
            new AttributesDescriptor("Number", SyntacticAttributes.GROOVY_NUMBER),
            new AttributesDescriptor("String", SyntacticAttributes.GROOVY_STRING),
            new AttributesDescriptor("Valid escape in string", SyntacticAttributes.GROOVY_VALID_STRING_ESCAPE),
            new AttributesDescriptor("Invalid escape in string", SyntacticAttributes.GROOVY_INVALID_STRING_ESCAPE),
            new AttributesDescriptor("Operator sign", SyntacticAttributes.GROOVY_OPERATION_SIGN),
            new AttributesDescriptor("Parentheses", SyntacticAttributes.GROOVY_PARENTHESES),
            new AttributesDescriptor("Brackets", SyntacticAttributes.GROOVY_BRACKETS),
            new AttributesDescriptor("Braces", SyntacticAttributes.GROOVY_BRACES),
            new AttributesDescriptor("Dot", SyntacticAttributes.GROOVY_DOT),
            new AttributesDescriptor("Comma", SyntacticAttributes.GROOVY_COMMA),
            new AttributesDescriptor("Semicolon", SyntacticAttributes.GROOVY_SEMICOLON),
            new AttributesDescriptor("Script header comment", SyntacticAttributes.GROOVY_SCRIPT_HEADER_COMMENT),
            new AttributesDescriptor("Line comment", SyntacticAttributes.GROOVY_LINE_COMMENT),
            new AttributesDescriptor("Block comment", SyntacticAttributes.GROOVY_BLOCK_COMMENT),
            new AttributesDescriptor("Javadoc comment", SyntacticAttributes.GROOVY_DOC_COMMENT),
            new AttributesDescriptor("Javadoc tag", SyntacticAttributes.GROOVY_DOC_TAG),
            new AttributesDescriptor("Javadoc markup", SyntacticAttributes.GROOVY_DOC_MARKUP),
            new AttributesDescriptor("Unknown symbol", CodeInsightAttributes.WRONG_REFERENCE_ATTRIBUTES),
            new AttributesDescriptor("Error", CodeInsightAttributes.ERROR_ATTRIBUTES),
            new AttributesDescriptor("Warning", CodeInsightAttributes.WARNING_ATTRIBUTES),
            new AttributesDescriptor("Deprecated symbol", CodeInsightAttributes.DEPRECATED_ATTRIBUTES),
            new AttributesDescriptor("Unused symbol", CodeInsightAttributes.NOT_USED_ELEMENT_ATTRIBUTES),
            new AttributesDescriptor("Class", CodeInsightAttributes.CLASS_NAME_ATTRIBUTES),
            new AttributesDescriptor("Interface", CodeInsightAttributes.INTERFACE_NAME_ATTRIBUTES),
            new AttributesDescriptor("Local variable", CodeInsightAttributes.LOCAL_VARIABLE_ATTRIBUTES),
            new AttributesDescriptor("Mutable local variable", CodeInsightAttributes.MUTABLE_LOCAL_VARIABLE_ATTRIBUTES),
            new AttributesDescriptor("Mutable parameter", CodeInsightAttributes.MUTABLE_PARAMETER_ATTRIBUTES),
            new AttributesDescriptor("Instance field", CodeInsightAttributes.INSTANCE_FIELD_ATTRIBUTES),
            new AttributesDescriptor("Static field", CodeInsightAttributes.STATIC_FIELD_ATTRIBUTES),
            new AttributesDescriptor("Parameter", CodeInsightAttributes.PARAMETER_ATTRIBUTES),
            new AttributesDescriptor("Method call", CodeInsightAttributes.METHOD_CALL_ATTRIBUTES),
            new AttributesDescriptor("Method declaration", CodeInsightAttributes.METHOD_DECLARATION_ATTRIBUTES),
            new AttributesDescriptor("Constructor call", CodeInsightAttributes.CONSTRUCTOR_CALL_ATTRIBUTES),
            new AttributesDescriptor("Constructor declaration", CodeInsightAttributes.CONSTRUCTOR_DECLARATION_ATTRIBUTES),
            new AttributesDescriptor("Static method", CodeInsightAttributes.STATIC_METHOD_ATTRIBUTES),
            new AttributesDescriptor("Matched brace", CodeInsightAttributes.MATCHED_BRACE_ATTRIBUTES),
            new AttributesDescriptor("Unmatched brace", CodeInsightAttributes.UNMATCHED_BRACE_ATTRIBUTES),
            new AttributesDescriptor("Bad character", HighlighterColors.BAD_CHARACTER),
            new AttributesDescriptor("Breakpoint line", DebuggerColors.BREAKPOINT_ATTRIBUTES),
            new AttributesDescriptor("Execution point", DebuggerColors.EXECUTIONPOINT_ATTRIBUTES),
            new AttributesDescriptor("Annotation name", CodeInsightAttributes.ANNOTATION_NAME_ATTRIBUTES),
            new AttributesDescriptor("Annotation attribute name", CodeInsightAttributes.ANNOTATION_ATTRIBUTE_NAME_ATTRIBUTES)
        };

        COLOR_DESCRIPTORS = new ColorDescriptor[] {
            new ColorDescriptor("Method separator color", METHOD_SEPARATORS_COLOR, ColorDescriptor.Kind.FOREGROUND)
        };

        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("field", CodeInsightAttributes.INSTANCE_FIELD_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("unusedField", CodeInsightAttributes.NOT_USED_ELEMENT_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("error", CodeInsightAttributes.ERROR_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("warning", CodeInsightAttributes.WARNING_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("unknownType", CodeInsightAttributes.WRONG_REFERENCE_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("localVar", CodeInsightAttributes.LOCAL_VARIABLE_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("mutableLocalVar", CodeInsightAttributes.MUTABLE_LOCAL_VARIABLE_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("mutableParameter", CodeInsightAttributes.MUTABLE_PARAMETER_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("static", CodeInsightAttributes.STATIC_FIELD_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("deprecated", CodeInsightAttributes.DEPRECATED_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("constructorCall", CodeInsightAttributes.CONSTRUCTOR_CALL_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("constructorDeclaration", CodeInsightAttributes.CONSTRUCTOR_DECLARATION_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("methodCall", CodeInsightAttributes.METHOD_CALL_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("methodDeclaration", CodeInsightAttributes.METHOD_DECLARATION_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("static_method", CodeInsightAttributes.STATIC_METHOD_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("param", CodeInsightAttributes.PARAMETER_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("class", CodeInsightAttributes.CLASS_NAME_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("interface", CodeInsightAttributes.INTERFACE_NAME_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("annotationName", CodeInsightAttributes.ANNOTATION_NAME_ATTRIBUTES);
        ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP.put("annotationAttributeName", CodeInsightAttributes.ANNOTATION_ATTRIBUTE_NAME_ATTRIBUTES);
    }

    public Icon getIcon() {
        return GroovySupportLoader.GROOVY.getIcon();
    }

    public String getDisplayName() {
        return "Groovy";
    }

    public String getDemoText() {
        return DEMO_TEXT;
    }

    public AttributesDescriptor[] getAttributeDescriptors() {
        return ATTRIBUTES_DESCRIPTORS;
    }

    public ColorDescriptor[] getColorDescriptors() {
        return COLOR_DESCRIPTORS;
    }

    public Map getAdditionalHighlightingTagToDescriptorMap() {
        return ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP;
    }

    public SyntaxHighlighter getHighlighter() {
        return new GroovyFileHighlighter();
    }
}

/*
 * $Id$
 *
 * Copyright (c) 2005-2006 The Codehaus - http://groovy.codehaus.org
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

import com.intellij.debugger.settings.DebuggerColors;
import com.intellij.ide.highlighter.JavaFileHighlighter;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.pom.java.LanguageLevel;
import org.codehaus.groovy.intellij.GroovySupportLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class GroovyColourSettingsPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] ATTRIBUTES_DESCRIPTORS = createAttributeDescriptors();
    private static final Map<String, TextAttributesKey> ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP = createAdditionalAttributeMap();
    private static final ColorDescriptor[] COLOR_DESCRIPTORS = new ColorDescriptor[0];

    /*
     * DO NOT attempt to break the following string into smaller chuncks BUT RATHER install the StringEditor plug-in
     * to get a more readable and editable view of this code snippet.
     */
    private static final String DEMO_TEXT =
            "#!/usr/bin/groovy\n\nimport <class>javax.swing.JPanel</class>\nimport <class>groovy.swing.SwingBuilder</class>\n                               Bad characters: \\n #\n/**\n * Doc comments for <code>SomeClass</code>.\n *\n * @author The Codehaus\n *\n * @see <class>GroovyTestCase</class>#<methodCall>setUp</methodCall>()\n */\n<annotationName>@Annotation</annotationName> (<annotationAttributeName>name</annotationAttributeName>=value)\npublic class <class>SomeClass</class> extends <class>GroovyTestCase</class> { // some comment\n\n    /* Block comment */\n    def <field>author</field> = \"Joe Bloggs\"\n    <class>Long</class> <field>random</field> = 1101001010010110L;\n\n    private <class>String</class> <field>field</field> = \"Hello World\";\n    private double <unusedField>unusedField</unusedField> = 12345.67890;\n    private <unknownType>UnknownType</unknownType> <field>anotherString</field> = \"Another\\nStrin\\g\";\n    private int[] <field>array</field> = new int[] { 1, 2, 3 };\n    public static int <static>staticField</static> = 0;\n\n    public <constructorDeclaration>SomeClass</constructorDeclaration>(<interface>AnInterface</interface> <param>param</param>, int <reassignedParameter>count</reassignedParameter>) {\n        <todo>//TODO: something</todo>\n        <error>int <localVar>localVar</localVar> = \"IntelliJ\"</error>; // Error, incompatible types\n        <class>System</class>.<static>out</static>.<methodCall>println</methodCall>(<field>anotherString</field> + <field>field</field> + <localVar>localVar</localVar>);\n\n        long <localVar>time</localVar> = <class>Date</class>.<static_method><deprecated>parse</deprecated></static_method>(\"1/2/3\"); // Method is deprecated\n        int <localVar>value</localVar> = this.<warning>staticField</warning>; \n        <reassignedParameter>count</reassignedParameter>++\n    }\n\n    interface <interface>AnInterface</interface> {\n        int <static>CONSTANT</static> = 2;\n    }\n\n    void <methodDeclaration>testDoesANumberOfRandomThings</methodDeclaration>() {/*  block\n                     comment */\n        new <constructorCall>SomeClass</constructorCall>();\n\n        def <localVar>quadraticClosure</localVar> = { a, b -> <class>Math</class>.<static_method>pow</static_method>(a, 2) + (2 * a * b) + <class>Math</class>.<static_method>pow</static_method>(b, 2) }\n        assert <localVar>quadraticClosure</localVar>.<methodCall>call</methodCall>(3, 5) == 64\n\n        <class>List</class> <reassignedLocalVar>aList</reassignedLocalVar> = [ 3, 5 ]\n        assert <reassignedLocalVar>aList</reassignedLocalVar>.<methodCall>collect</methodCall> { i -> return (int) <class>Math</class>.<static_method>pow</static_method>(i, 2) } == [ 9, 25 ]\n\n        <static>staticField</static>++\n    }\n}\n";

    private static AttributesDescriptor[] createAttributeDescriptors() {
        return new AttributesDescriptor[] {
            new AttributesDescriptor("Keyword", SyntacticAttributes.GROOVY_KEYWORD),
            new AttributesDescriptor("Number", SyntacticAttributes.GROOVY_NUMBER),
            new AttributesDescriptor("String", SyntacticAttributes.GROOVY_STRING),
            new AttributesDescriptor("Valid escape in string", SyntacticAttributes.GROOVY_VALID_STRING_ESCAPE),
            new AttributesDescriptor("Invalid escape in string", SyntacticAttributes.GROOVY_INVALID_STRING_ESCAPE),
            new AttributesDescriptor("Regular expression", SyntacticAttributes.GROOVY_REGEXP),
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
            new AttributesDescriptor("Reassigned local variable", CodeInsightAttributes.REASSIGNED_LOCAL_VARIABLE_ATTRIBUTES),
            new AttributesDescriptor("Reassigned parameter", CodeInsightAttributes.REASSIGNED_PARAMETER_ATTRIBUTES),
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
    }

    private static Map<String, TextAttributesKey> createAdditionalAttributeMap() {
        HashMap<String, TextAttributesKey> additionalAttributes = new HashMap<String, TextAttributesKey>();
        additionalAttributes.put("field", CodeInsightAttributes.INSTANCE_FIELD_ATTRIBUTES);
        additionalAttributes.put("unusedField", CodeInsightAttributes.NOT_USED_ELEMENT_ATTRIBUTES);
        additionalAttributes.put("error", CodeInsightAttributes.ERROR_ATTRIBUTES);
        additionalAttributes.put("warning", CodeInsightAttributes.WARNING_ATTRIBUTES);
        additionalAttributes.put("server_problems", CodeInsightAttributes.GENERIC_SERVER_ERROR_OR_WARNING);
        additionalAttributes.put("info", CodeInsightAttributes.INFO_ATTRIBUTES);
        additionalAttributes.put("unknownType", CodeInsightAttributes.WRONG_REFERENCE_ATTRIBUTES);
        additionalAttributes.put("localVar", CodeInsightAttributes.LOCAL_VARIABLE_ATTRIBUTES);
        additionalAttributes.put("reassignedLocalVar", CodeInsightColors.REASSIGNED_LOCAL_VARIABLE_ATTRIBUTES);
        additionalAttributes.put("reassignedParameter", CodeInsightColors.REASSIGNED_PARAMETER_ATTRIBUTES);
        additionalAttributes.put("static", CodeInsightAttributes.STATIC_FIELD_ATTRIBUTES);
        additionalAttributes.put("deprecated", CodeInsightAttributes.DEPRECATED_ATTRIBUTES);
        additionalAttributes.put("constructorCall", CodeInsightAttributes.CONSTRUCTOR_CALL_ATTRIBUTES);
        additionalAttributes.put("constructorDeclaration", CodeInsightAttributes.CONSTRUCTOR_DECLARATION_ATTRIBUTES);
        additionalAttributes.put("methodCall", CodeInsightAttributes.METHOD_CALL_ATTRIBUTES);
        additionalAttributes.put("methodDeclaration", CodeInsightAttributes.METHOD_DECLARATION_ATTRIBUTES);
        additionalAttributes.put("static_method", CodeInsightAttributes.STATIC_METHOD_ATTRIBUTES);
        additionalAttributes.put("param", CodeInsightAttributes.PARAMETER_ATTRIBUTES);
        additionalAttributes.put("class", CodeInsightAttributes.CLASS_NAME_ATTRIBUTES);
        additionalAttributes.put("typeParameter", CodeInsightAttributes.TYPE_PARAMETER_NAME_ATTRIBUTES);
        additionalAttributes.put("abstractClass", CodeInsightAttributes.ABSTRACT_CLASS_NAME_ATTRIBUTES);
        additionalAttributes.put("interface", CodeInsightAttributes.INTERFACE_NAME_ATTRIBUTES);
        additionalAttributes.put("annotationName", CodeInsightAttributes.ANNOTATION_NAME_ATTRIBUTES);
        additionalAttributes.put("annotationAttributeName", CodeInsightAttributes.ANNOTATION_ATTRIBUTE_NAME_ATTRIBUTES);
        return additionalAttributes;
    }

    public Icon getIcon() {
        return GroovySupportLoader.GROOVY.getIcon();
    }

    @NotNull
    public String getDisplayName() {
        return "Groovy";
    }

    @NotNull
    public String getDemoText() {
        return DEMO_TEXT;
    }

    @NotNull
    public AttributesDescriptor[] getAttributeDescriptors() {
        return ATTRIBUTES_DESCRIPTORS;
    }

    @NotNull
    public ColorDescriptor[] getColorDescriptors() {
        return COLOR_DESCRIPTORS;
    }

    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return ADDITIONAL_HIGHLIGHTING_TAG_TO_DESCRIPTOR_MAP;
    }

    @NotNull
    public SyntaxHighlighter getHighlighter() {
        // TODO: restore once GroovyFileHighlighter is functionally usable
//        return new GroovyFileHighlighter();
        return new JavaFileHighlighter(LanguageLevel.HIGHEST);
    }
}

package org.codehaus.groovy.eclipse.codebrowsing;

/**
 * The following categories are the predefined categories. Other categories
 * can be defined by extensions. Categories are used to group multiple
 * declaration search proposals.
 */
public interface DeclarationCategory {
	public static final String LOCAL = "localCategory";

	public static final String FIELD = "fieldCategory";

	public static final String METHOD = "methodCategory";

	public static final String STATIC_METHOD = "staticMethodCategory";

	public static final String METHOD_PARAMETER = "methodParameterCategory";

	public static final String CLASS = "classCategory";

}
package org.codehaus.groovy.eclipse.codebrowsing;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.IRegion;

/**
 * The proposal returned by a search processor. A proposal is simply a target
 * resource which contains the proposed declaration, and a highlight region than
 * can be used to highlight the declarattion. Each proposal is categorized. If
 * there is more than one proposal, they can be sorted by category for display.
 * 
 * @author emp
 */
public interface IDeclarationMatchProposal {
	/*
	 * The following categories are the predefined categories. Other categories
	 * can be defined by extensions. Categories are used to group multiple
	 * declaration search proposals.
	 * 
	 */
	public static final String LOCAL_CATEGORY = "0_localCategory";

	public static final String FIELD_CATEGORY = "1_fieldCategory";

	public static final String METHOD_CATEGORY = "2_methodCategory";

	public static final String STATIC_METHOD_CATEGORY = "3_staticMethodCategory";

	public static final String METHOD_PARAMETER_CATEGORY = "4_methodParameterCategory";

	public static final String CLASS_CATEGORY = "5_classCategory";

	/**
	 * @return The category of the proposal. Custom categories are defined by
	 *         extensions which require custom categories.
	 */
	public String getCategory();

	/**
	 * @return The target containing the declaration, normally an IFile.
	 */
	public IResource getTarget();

	/**
	 * @return The highlight region, which may be null if the resource is not
	 *         something that can be highlighted.
	 */
	public IRegion getHighlightRegion();
}

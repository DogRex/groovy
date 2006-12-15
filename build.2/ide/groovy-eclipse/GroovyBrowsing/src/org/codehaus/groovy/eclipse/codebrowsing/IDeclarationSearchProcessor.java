package org.codehaus.groovy.eclipse.codebrowsing;


/**
 * Processes a search for a declaration. There are many such processors, some
 * specialized for searching for methods, other for variables, other which try
 * to infer declaration matches for dynamic types and so on.
 * 
 * @author emp
 */
public interface IDeclarationSearchProcessor {
	/**
	 * Get declaration match proposals, if any.
	 * @param info
	 * @return An array of proposals, or an empty array if there weren't any.
	 */
	public IDeclarationProposal[] getProposals(IDeclarationSearchInfo info);
}

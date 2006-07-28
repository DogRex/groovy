package org.codehaus.groovy.eclipse.codebrowsing;

import java.util.List;

/**
 * Processes a search for a declaration. There are many such processors, some
 * specialized for searching for methods, other for variables, other which try
 * to infer declaration matches for dynamic types and so on.
 * 
 * @author emp
 */
public interface IDeclarationSearchProcessor {
	public List getProposals(IDeclarationSearchInfo info);
}

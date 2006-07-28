package org.codehaus.groovy.eclipse.codebrowsing;

import java.util.List;

import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorPart;

/**
 * The interface to get IDeclarationMatchProposals with.
 * 
 * This interface is not intended to be implemented, use
 * DeclarationSearchAssistant.getInstance() to get the singleton.
 * 
 * @author emp
 */
public interface IDeclarationSearchAssistant {
	List getProposals(IEditorPart editor, IRegion region);
}

package org.codehaus.groovy.eclipse.codebrowsing;

import java.util.List;

import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorPart;

/**
 * The interface to get IOpenDeclarationProposals with. 
 *
 * @author emp
 */
public interface IOpenDeclarationAssistant {
	List getProposals(IEditorPart editor, IRegion region);
}

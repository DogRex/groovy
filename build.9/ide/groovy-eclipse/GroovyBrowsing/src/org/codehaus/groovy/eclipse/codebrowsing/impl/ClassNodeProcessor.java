package org.codehaus.groovy.eclipse.codebrowsing.impl;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.eclipse.codebrowsing.ASTUtils;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationCategory;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationProposal;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationProposal;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchInfo;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.ISourceCode;
import org.codehaus.groovy.eclipse.codebrowsing.SourceCodeFinder;

/**
 * <pre>
 *   class A extends B implement C, D 
 *         &circ;         &circ;           &circ;  &circ; 
 * </pre>
 * 
 * @author emp
 */
public class ClassNodeProcessor implements IDeclarationSearchProcessor {
	public IDeclarationProposal[] getProposals(IDeclarationSearchInfo info) {
		ClassNode classNode = (ClassNode) info.getASTNode();
		
		// Clicked on the actual node, nothing to do.
		if (classNode == info.getClassNode()) {
			return DeclarationProposal.NONE;
		}

		ISourceCode sourceCode = SourceCodeFinder.find(classNode);
		if (sourceCode != null) {
			return new IDeclarationProposal[] { new DeclarationProposal(
					DeclarationCategory.CLASS, ASTUtils
							.createDisplayString(classNode), sourceCode) };
		}

		return DeclarationProposal.NONE;
	}
}
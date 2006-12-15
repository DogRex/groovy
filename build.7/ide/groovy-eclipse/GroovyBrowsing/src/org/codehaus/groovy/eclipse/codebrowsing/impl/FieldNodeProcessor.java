package org.codehaus.groovy.eclipse.codebrowsing.impl;

import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.eclipse.codebrowsing.ASTUtils;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationCategory;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationProposal;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationProposal;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchInfo;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.ISourceCode;
import org.codehaus.groovy.eclipse.codebrowsing.SourceCodeFinder;

/**
 * @author emp
 */
public class FieldNodeProcessor implements
		IDeclarationSearchProcessor {
	public IDeclarationProposal[] getProposals(IDeclarationSearchInfo info) {
		FieldNode fieldNode = (FieldNode) info.getASTNode();

		if (fieldNode.isDynamicTyped()) {
			return DeclarationProposal.NONE;
		}
		
		ISourceCode sourceCode = SourceCodeFinder.find(fieldNode.getType());
		if (sourceCode != null) {
			return new IDeclarationProposal[] { new DeclarationProposal(
					DeclarationCategory.CLASS, ASTUtils
							.createDisplayString(fieldNode), sourceCode) };
		}

		return DeclarationProposal.NONE;
	}
}
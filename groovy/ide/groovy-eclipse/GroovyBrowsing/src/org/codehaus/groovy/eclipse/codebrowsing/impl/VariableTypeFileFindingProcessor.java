package org.codehaus.groovy.eclipse.codebrowsing.impl;

import java.util.Collections;
import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.eclipse.codebrowsing.ASTUtils;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationMatchProposal;
import org.codehaus.groovy.eclipse.codebrowsing.FileUtils;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationMatchProposal;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchInfo;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchProcessor;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.Region;

/**
 * Finds the file for the variable expression type. 
 * @author emp
 */
public class VariableTypeFileFindingProcessor implements IDeclarationSearchProcessor {
	public List getProposals(IDeclarationSearchInfo info) {
		VariableExpression expr = (VariableExpression)info.getASTNode();
		if (expr.isDynamicTyped() || expr.getAccessedVariable() == null) {
			return Collections.emptyList();
		}
		
		
		ClassNode type = expr.getAccessedVariable().getType();
		// Must have clicked on the variable name, not the type.
		if (!type.getName().endsWith(info.getIdentifier())) {
			return Collections.emptyList();
		}
		
		String filename = type.getName().replace(".", "/");

		IFile[] files = FileUtils.findFilesInWorkspace(filename, new String[] {
				"groovy", "java" });

		if (files.length > 0) {
			IFile file = files[0];

			int offset = FileUtils.findOffsetOfIdentifiers(file, new String[] {
					"class", type.getNameWithoutPackage() });
			if (offset != -1) {
				return ASTUtils.wrapProposal(new DeclarationMatchProposal(
						IDeclarationMatchProposal.CLASS_CATEGORY, ASTUtils
								.createDisplayString(type), file,
						new Region(offset, offset == 0 ? 0 : info
								.getIdentifier().length())));
			}
		}
		
		return Collections.emptyList();
	}
}

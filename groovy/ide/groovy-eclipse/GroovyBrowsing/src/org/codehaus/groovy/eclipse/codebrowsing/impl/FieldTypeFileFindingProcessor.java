package org.codehaus.groovy.eclipse.codebrowsing.impl;

import java.util.Collections;
import java.util.List;

import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.eclipse.codebrowsing.ASTUtils;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationMatchProposal;
import org.codehaus.groovy.eclipse.codebrowsing.FileUtils;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationMatchProposal;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchInfo;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchProcessor;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.Region;

/**
 * Finds the file for the field type if it is in the workspace.
 * 
 * @author emp
 */
public class FieldTypeFileFindingProcessor implements
		IDeclarationSearchProcessor {
	public List getProposals(IDeclarationSearchInfo info) {
		FieldNode fieldNode = (FieldNode) info.getASTNode();

		if (fieldNode.isDynamicTyped()) {
			return Collections.emptyList();
		}

		String filename = fieldNode.getType().getName().replace(".", "/");

		IFile[] files = FileUtils.findFilesInWorkspace(filename, new String[] {
				"groovy", "java" });

		if (files.length > 0) {
			IFile file = files[0];

			int offset = FileUtils.findOffsetOfIdentifiers(file, new String[] {
					"class", fieldNode.getType().getNameWithoutPackage() });
			if (offset != -1) {
				return ASTUtils.wrapProposal(new DeclarationMatchProposal(
						IDeclarationMatchProposal.CLASS_CATEGORY, ASTUtils
								.createDisplayString(fieldNode), file,
						new Region(offset, offset == 0 ? 0 : info
								.getIdentifier().length())));
			}
		}

		return Collections.emptyList();
	}
}

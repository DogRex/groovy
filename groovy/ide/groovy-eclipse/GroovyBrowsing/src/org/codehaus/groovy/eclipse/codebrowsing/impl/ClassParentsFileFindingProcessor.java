package org.codehaus.groovy.eclipse.codebrowsing.impl;

import java.util.Collections;
import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.eclipse.codebrowsing.ASTUtils;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationMatchProposal;
import org.codehaus.groovy.eclipse.codebrowsing.FileUtils;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationMatchProposal;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchInfo;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchProcessor;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.Region;

/*
 * TODO: I can't believe that somewhere buried in the JDT code there isn't some
 * API to do all this stuff? I have looked and looked, but found only very Java
 * centric code. Perhaps create a IJavaElement somehow? Or model the JDT and
 * have some parallel like IGroovyElement. More research required.
 * -emp
 */

/**
 * class A extends B implement C, D ^ ^ ^ If ^ is the identifer, attempts to
 * find the Groovy or Java file, if it is in the workspace.
 * 
 * @author emp
 */
public class ClassParentsFileFindingProcessor implements
		IDeclarationSearchProcessor {
	public List getProposals(IDeclarationSearchInfo info) {
		ClassNode classNode = (ClassNode) info.getASTNode();
		if (classNode == info.getClassNode()) {
			return Collections.emptyList();
		}

		String fileName = classNode.getName().replace('.', '/');

		IFile[] files = FileUtils.findFilesInWorkspace(fileName, new String[] {
				"groovy", "java" });

		if (files.length > 0) {
			// just take the first one for now.
			// TODO: find out if path is in classpath, and get the file that is
			// correct for the class path.
			IFile file = files[0];

			// Possibly reparsing the file and finding the correct place for
			// sure would be better. But this is quick and dirty and should work
			// most of the time.
			int offset = FileUtils.findOffsetOfIdentifiers(file, new String[] {
					"class", info.getIdentifier() });
			if (offset != -1) {
				return ASTUtils.wrapProposal(new DeclarationMatchProposal(
						IDeclarationMatchProposal.CLASS_CATEGORY, ASTUtils
								.createDisplayString(classNode), file,
						new Region(offset, offset == 0 ? 0 : info
								.getIdentifier().length())));
			}
		}

		return Collections.emptyList();
	}
}

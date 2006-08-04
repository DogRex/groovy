package org.codehaus.groovy.eclipse.codebrowsing.impl;

import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationCategory;
import org.codehaus.groovy.eclipse.codebrowsing.FileSourceCode;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationProposal;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchInfo;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationProposal;
import org.codehaus.groovy.eclipse.editor.actions.EditorPartFacade;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

/**
 * @author emp
 */
public class ClassExpressionProcessor implements
		IDeclarationSearchProcessor {
	public IDeclarationProposal[] getProposals(IDeclarationSearchInfo info) {
		EditorPartFacade facade = new EditorPartFacade(info.getEditor());
		ClassExpression expr = (ClassExpression) info.getASTNode();
		ClassNode classNode = null;
		List classes = info.getModuleNode().getClasses();
		for (Iterator iter = classes.iterator(); iter.hasNext();) {
			ClassNode test = (ClassNode) iter.next();
			if (test.getName().equals(expr.getType().getName())) {
				classNode = test;
				break;
			}
		}
		
		// Either metaclass, or base class - add tests laster.
		if (classNode == null) {
			return DeclarationProposal.NONE;
		}

		int startLine = classNode.getLineNumber() - 1;
		int lastLine = classNode.getLastLineNumber();
		int offset = 0;
		int length = 0;
		// TODO: what about when the expression uses a qualified class name?
		for (int i = startLine; i <= lastLine; ++i) {
			try {
				String line = facade.getLine(i);
				int ix = line.indexOf(classNode.getName());
				if (ix >= 0) {
					offset = facade.getOffset(i, ix);
					length = classNode.getName().length();
					break;
				} else {
					ix = line.indexOf(info.getIdentifier());
					if (ix >= 0) {
						offset = facade.getOffset(i, ix);
						length = info.getIdentifier().length();
						break;
					}
				}
			} catch (BadLocationException e) {
				GroovyPlugin.getPlugin().logException("Should not happen", e);
				return DeclarationProposal.NONE;
			}
		}

		IRegion region = new Region(offset, length);
		if (region != null) {
			return new IDeclarationProposal[] { new DeclarationProposal(
					DeclarationCategory.CLASS, expr.getType().getName(),
					new FileSourceCode(facade.getFile(), region)) };
		}

		return DeclarationProposal.NONE;
	}
}

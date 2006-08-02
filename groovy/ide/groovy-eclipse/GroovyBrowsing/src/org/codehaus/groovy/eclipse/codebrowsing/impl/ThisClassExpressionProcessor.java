package org.codehaus.groovy.eclipse.codebrowsing.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.codebrowsing.ASTUtils;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchInfo;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationMatchProposal;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationMatchProposal;
import org.codehaus.groovy.eclipse.editor.actions.EditorPartFacade;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

/**
 * Proposal for a class in this file. This is usually a proposal of a class
 * which is used in a static access to a method or field.
 * 
 * @author emp
 */
public class ThisClassExpressionProcessor implements IDeclarationSearchProcessor {
	public List getProposals(IDeclarationSearchInfo info) {
		EditorPartFacade facade = new EditorPartFacade(info.getEditor());
		ClassExpression expr = (ClassExpression) info.getASTNode();
		ClassNode classNode = null;
		List classes = info.getModuleNode().getClasses();
		for (Iterator iter  = classes.iterator(); iter.hasNext();) {
			classNode = (ClassNode)iter.next();
			if (classNode.getName().equals(expr.getType().getName()))
				break;
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
				return Collections.emptyList();
			}
		}
		
		IRegion region = new Region(offset, length);
		if (region != null) {
			return ASTUtils.wrapProposal(new DeclarationMatchProposal(
					IDeclarationMatchProposal.CLASS_CATEGORY, expr.getType()
							.getName(), facade.getFile(), region));
		}

		return Collections.emptyList();
	}
}

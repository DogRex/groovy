package org.codehaus.groovy.eclipse.codebrowsing.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.MethodPointerExpression;
import org.codehaus.groovy.eclipse.codebrowsing.ASTUtils;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationCategory;
import org.codehaus.groovy.eclipse.codebrowsing.FileSourceCode;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationProposal;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchInfo;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationProposal;
import org.codehaus.groovy.eclipse.editor.actions.EditorPartFacade;
import org.eclipse.jface.text.IRegion;

/**
 * <pre>
 *   a.&amp;b
 *      &circ;
 * </pre>
 * 
 * @author emp
 */
public class MethodPointerExpressionProcessor implements
		IDeclarationSearchProcessor {
	public IDeclarationProposal[] getProposals(IDeclarationSearchInfo info) {
		MethodPointerExpression expr = (MethodPointerExpression) info
				.getASTNode();
		String methodName = expr.getMethodName();
		List nodes = info.getClassNode().getMethods();
		List results = new ArrayList();
		for (Iterator iter = nodes.iterator(); iter.hasNext();) {
			MethodNode methodNode = (MethodNode) iter.next();
			if (methodNode.getName().equals(methodName)) {
				EditorPartFacade facade = new EditorPartFacade(info.getEditor());
				IRegion region = ASTUtils.getRegion(facade, methodNode, info
						.getIdentifier().length());
				if (region != null) {
					results.add(new DeclarationProposal(
							DeclarationCategory.METHOD, ASTUtils
									.createDisplayString(methodNode),
							new FileSourceCode(facade.getFile(), region)));
				}
			}
		}

		return (IDeclarationProposal[]) results
				.toArray(new IDeclarationProposal[results.size()]);
	}
}

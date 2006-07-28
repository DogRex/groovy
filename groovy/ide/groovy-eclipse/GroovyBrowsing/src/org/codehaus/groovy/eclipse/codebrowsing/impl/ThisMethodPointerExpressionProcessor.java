package org.codehaus.groovy.eclipse.codebrowsing.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.MethodPointerExpression;
import org.codehaus.groovy.eclipse.codebrowsing.ASTUtils;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchInfo;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationMatchProposal;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationMatchProposal;
import org.codehaus.groovy.eclipse.editor.actions.EditorPartFacade;
import org.eclipse.jface.text.IRegion;

public class ThisMethodPointerExpressionProcessor implements
		IDeclarationSearchProcessor {
	public List getProposals(IDeclarationSearchInfo info) {
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
					results.add(new DeclarationMatchProposal(
							IDeclarationMatchProposal.METHOD_CATEGORY, ASTUtils
									.createDisplayString(methodNode), facade
									.getFile(), region));
				}
			}
		}

		return results;
	}
}

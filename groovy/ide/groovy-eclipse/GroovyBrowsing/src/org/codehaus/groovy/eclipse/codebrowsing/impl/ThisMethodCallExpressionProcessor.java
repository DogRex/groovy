package org.codehaus.groovy.eclipse.codebrowsing.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.eclipse.codebrowsing.ASTUtils;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchInfo;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationMatchProposal;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationMatchProposal;
import org.codehaus.groovy.eclipse.editor.actions.EditorPartFacade;
import org.eclipse.jface.text.IRegion;

/**
 * Processor for methods in this class.
 * 
 * @author emp
 */
public class ThisMethodCallExpressionProcessor implements
		IDeclarationSearchProcessor {
	public List getProposals(IDeclarationSearchInfo info) {
		MethodCallExpression expr = (MethodCallExpression) info.getASTNode();
		if (expr.getObjectExpression().getText().equals("this")) {
			return createMethodCallProposal(
					IDeclarationMatchProposal.METHOD_CATEGORY, info, info
							.getClassNode(), expr);
		} else {
			String className = expr.getObjectExpression().getText();
			for (Iterator iter = info.getModuleNode().getClasses().iterator(); iter
					.hasNext();) {
				ClassNode classNode = (ClassNode) iter.next();
				if (className.endsWith(classNode.getName())) {
					return createMethodCallProposal(
							IDeclarationMatchProposal.STATIC_METHOD_CATEGORY,
							info, classNode, expr);
				}
			}
		}
		return Collections.emptyList();
	}

	private List createMethodCallProposal(String category,
			IDeclarationSearchInfo info, ClassNode classNode, MethodCallExpression expr) {
		List nodes = classNode.getMethods(expr.getMethod());
		List results = new ArrayList();
		EditorPartFacade facade = new EditorPartFacade(info.getEditor());
		for (Iterator iter = nodes.iterator(); iter.hasNext();) {
			MethodNode methodNode = (MethodNode) iter.next();
			Parameter[] params = methodNode.getParameters();

			if (expr.getArguments() instanceof ArgumentListExpression) {
				ArgumentListExpression args = (ArgumentListExpression) expr
						.getArguments();
				List argExpr = args.getExpressions();
				boolean match = false;
				
				if (argExpr.size() == params.length) {
					match = true;
				} else if (argExpr.size() < params.length) {
					// Check for optional args.
					if (params[argExpr.size()].getInitialExpression() != null) {
						match = true;
					}
				}
				
				if (match == true) {
					IRegion region = ASTUtils.getRegion(facade, methodNode,
							info.getIdentifier().length());
					if (region != null) {
						results.add(new DeclarationMatchProposal(category,
								ASTUtils.createDisplayString(methodNode),
								facade.getFile(), region));
					}
				}
			}
		}
		return results;
	}
}

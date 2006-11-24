package org.codehaus.groovy.eclipse.codebrowsing.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
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
 * @author emp
 */
public class MethodCallExpressionProcessor implements
		IDeclarationSearchProcessor {
	public IDeclarationProposal[] getProposals(IDeclarationSearchInfo info) {
		MethodCallExpression expr = (MethodCallExpression) info.getASTNode();
		if (expr.getObjectExpression().getText().equals("this")) {
			return createMethodCallProposal(DeclarationCategory.METHOD, info,
					info.getClassNode(), expr);
		} else {
			String className = expr.getObjectExpression().getText();
			for (Iterator iter = info.getModuleNode().getClasses().iterator(); iter
					.hasNext();) {
				ClassNode classNode = (ClassNode) iter.next();
				if (className.endsWith(classNode.getName())) {
					return createMethodCallProposal(
							DeclarationCategory.STATIC_METHOD, info, classNode,
							expr);
				}
			}
		}
		return DeclarationProposal.NONE;
	}

	private IDeclarationProposal[] createMethodCallProposal(String category,
			IDeclarationSearchInfo info, ClassNode classNode,
			MethodCallExpression expr) {
		List nodes = classNode.getMethods(expr.getMethodAsString());
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
						results.add(new DeclarationProposal(category, ASTUtils
								.createDisplayString(methodNode),
								new FileSourceCode(facade.getFile(), region)));
					}
				}
			}
		}

		return (IDeclarationProposal[]) results
				.toArray(new IDeclarationProposal[results.size()]);
	}
}

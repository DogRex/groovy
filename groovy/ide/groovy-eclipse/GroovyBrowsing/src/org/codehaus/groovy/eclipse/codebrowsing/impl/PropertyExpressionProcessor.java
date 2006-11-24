package org.codehaus.groovy.eclipse.codebrowsing.impl;

import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
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
public class PropertyExpressionProcessor implements
		IDeclarationSearchProcessor {
	public IDeclarationProposal[] getProposals(IDeclarationSearchInfo info) {
		PropertyExpression expr = (PropertyExpression) info.getASTNode();
		if (expr.getObjectExpression() instanceof VariableExpression) {
			VariableExpression vexpr = (VariableExpression) expr
					.getObjectExpression();
			if (!vexpr.getName().equals("this"))
				return DeclarationProposal.NONE;

			FieldNode fieldNode = info.getClassNode().getField(
					expr.getPropertyAsString());
			if (fieldNode == null)
				return DeclarationProposal.NONE;

			EditorPartFacade facade = new EditorPartFacade(info.getEditor());
			IRegion highlight = ASTUtils.getRegion(facade, fieldNode);
			if (highlight != null) {
				return new IDeclarationProposal[] { new DeclarationProposal(
						DeclarationCategory.FIELD, ASTUtils
								.createDisplayString(fieldNode),
						new FileSourceCode(facade.getFile(), highlight)) };
			}
		}
		return DeclarationProposal.NONE;
	}
}

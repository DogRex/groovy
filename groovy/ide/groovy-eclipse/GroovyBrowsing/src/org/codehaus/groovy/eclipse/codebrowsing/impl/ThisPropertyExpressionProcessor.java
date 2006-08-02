package org.codehaus.groovy.eclipse.codebrowsing.impl;

import java.util.Collections;
import java.util.List;

import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.eclipse.codebrowsing.ASTUtils;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchInfo;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationMatchProposal;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationMatchProposal;
import org.codehaus.groovy.eclipse.editor.actions.EditorPartFacade;
import org.eclipse.jface.text.IRegion;

/**
 * A PropertyExpression processor for simply properties this.name for the
 * current class.
 * 
 * @author emp
 */
public class ThisPropertyExpressionProcessor implements
		IDeclarationSearchProcessor {
	public List getProposals(IDeclarationSearchInfo info) {
		PropertyExpression expr = (PropertyExpression) info.getASTNode();
		if (expr.getObjectExpression() instanceof VariableExpression) {
			VariableExpression vexpr = (VariableExpression) expr
					.getObjectExpression();
			if (!vexpr.getName().equals("this"))
				return Collections.emptyList();

			FieldNode fieldNode = info.getClassNode().getField(
					expr.getProperty());
			if (fieldNode == null)
				return Collections.emptyList();

			EditorPartFacade facade = new EditorPartFacade(info.getEditor());
			IRegion highlight = ASTUtils.getRegion(facade, fieldNode);
			if (highlight != null) {
				return ASTUtils.wrapProposal(new DeclarationMatchProposal(
						IDeclarationMatchProposal.FIELD_CATEGORY, ASTUtils
								.createDisplayString(fieldNode), facade
								.getFile(), highlight));
			}
		}
		return Collections.emptyList();
	}
}

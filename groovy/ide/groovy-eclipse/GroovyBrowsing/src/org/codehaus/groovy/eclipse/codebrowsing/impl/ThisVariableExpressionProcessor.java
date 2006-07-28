package org.codehaus.groovy.eclipse.codebrowsing.impl;

import java.util.List;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.Variable;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.codebrowsing.ASTNodeFinder;
import org.codehaus.groovy.eclipse.codebrowsing.ASTNodeSearchResult;
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
 * Find variables (fields or locals) in this class.
 * 
 * @author emp
 */
public class ThisVariableExpressionProcessor implements
		IDeclarationSearchProcessor {
	public List getProposals(IDeclarationSearchInfo info) {
		VariableExpression expr = (VariableExpression) info.getASTNode();
		Variable var = expr.getAccessedVariable();

		if (var instanceof FieldNode) {
			return processFieldNode(info, (FieldNode) var);
		} else if (var instanceof VariableExpression) {
			return processVariableExpression(info, (VariableExpression) var);
		} else if (var instanceof Parameter) {
			return processParameter(info, expr, (Parameter) var);
		}

		return null;
	}

	private List processFieldNode(IDeclarationSearchInfo info,
			FieldNode fieldNode) {
		EditorPartFacade facade = new EditorPartFacade(info.getEditor());
		IRegion highlight = ASTUtils.getRegion(facade, fieldNode, info
				.getIdentifier().length());
		if (highlight != null) {
			return ASTUtils.wrapProposal(new DeclarationMatchProposal(
					IDeclarationMatchProposal.FIELD_CATEGORY, ASTUtils
							.createDisplayString(fieldNode), facade.getFile(),
					highlight));
		}
		return null;
	}

	private List processVariableExpression(IDeclarationSearchInfo info,
			VariableExpression expr) {
		EditorPartFacade facade = new EditorPartFacade(info.getEditor());
		IRegion highlight = ASTUtils.getRegion(facade, expr, info
				.getIdentifier().length());
		if (highlight != null) {
			return ASTUtils.wrapProposal(new DeclarationMatchProposal(
					IDeclarationMatchProposal.LOCAL_CATEGORY, ASTUtils
							.createDisplayString(expr), facade.getFile(),
					highlight));
		}
		return null;
	}

	private List processParameter(IDeclarationSearchInfo info,
			VariableExpression expr, Parameter param) {
		ASTNodeSearchResult result = ASTNodeFinder.findSurroundingClosure(info
				.getModuleNode(), expr);
		if (result != null) {
			return processClosureOrMethodExpression(info, param, result);
		}

		result = ASTNodeFinder
				.findSurroundingMethod(info.getModuleNode(), expr);
		if (result != null) {
			return processClosureOrMethodExpression(info, param, result);
		}
		return null;
	}

	private List processClosureOrMethodExpression(IDeclarationSearchInfo info,
			Parameter param, ASTNodeSearchResult result) {
		ASTNode expr = result.getASTNode();
		EditorPartFacade facade = new EditorPartFacade(info.getEditor());
		try {
			int offset0 = facade.getOffset(expr.getLineNumber() - 1,
					expr.getColumnNumber() - 1);
			int offset1 = facade.getOffset(expr.getLastLineNumber() - 1,
					expr.getLastColumnNumber() - 1);
			String text = facade.getText(offset0, offset1 - offset0 + 1);
			
			// Closure patch, ugh.
			if (result.getASTNode() instanceof ClosureExpression) {
				offset0 = facade.getLineOffset(expr.getLineNumber() - 1);
				text = facade.getLine(expr.getLineNumber() - 1);
			}
			
			int identOffset = ASTUtils.findIdentifierOffset(text, param
					.getName());
			if (identOffset != -1) {
				Region region = new Region(offset0 + identOffset, param
						.getName().length());
				return ASTUtils.wrapProposal(new DeclarationMatchProposal(
						IDeclarationMatchProposal.METHOD_PARAMETER_CATEGORY,
						param.getName(), facade.getFile(), region));
			}
		} catch (BadLocationException e) {
			GroovyPlugin.getPlugin().logException("Should not happen", e);
		}
		return null;
	}
}
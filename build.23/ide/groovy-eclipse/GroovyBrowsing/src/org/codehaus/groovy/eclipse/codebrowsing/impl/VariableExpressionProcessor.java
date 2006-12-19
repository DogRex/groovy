package org.codehaus.groovy.eclipse.codebrowsing.impl;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.Variable;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.codebrowsing.ASTNodeFinder;
import org.codehaus.groovy.eclipse.codebrowsing.ASTSearchResult;
import org.codehaus.groovy.eclipse.codebrowsing.ASTUtils;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationCategory;
import org.codehaus.groovy.eclipse.codebrowsing.DeclarationProposal;
import org.codehaus.groovy.eclipse.codebrowsing.FileSourceCode;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationProposal;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchInfo;
import org.codehaus.groovy.eclipse.codebrowsing.IDeclarationSearchProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.ISourceCode;
import org.codehaus.groovy.eclipse.codebrowsing.SourceCodeFinder;
import org.codehaus.groovy.eclipse.codebrowsing.TextUtils;
import org.codehaus.groovy.eclipse.editor.actions.EditorPartFacade;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

/**
 * @author emp
 */
public class VariableExpressionProcessor implements IDeclarationSearchProcessor {
	public IDeclarationProposal[] getProposals(IDeclarationSearchInfo info) {
		VariableExpression expr = (VariableExpression) info.getASTNode();

		Variable var = expr.getAccessedVariable();

		if (expr.getName().equals(info.getIdentifier())) {
			// Clicked on the name.
			if (var instanceof FieldNode) {
				return processFieldNode(info, (FieldNode) var);
			} else if (var instanceof VariableExpression) {
				return processVariableExpression(info, (VariableExpression) var);
			} else if (var instanceof Parameter) {
				return processParameter(info, expr, (Parameter) var);
			}
		} else if (var.getType().getName().endsWith(info.getIdentifier())) {
			// Clicked on the type.
			ISourceCode sourceCode = SourceCodeFinder.find(var.getType());

			if (sourceCode != null) {
				return new IDeclarationProposal[] { new DeclarationProposal(
						DeclarationCategory.CLASS, ASTUtils
								.createDisplayString(var.getType()), sourceCode) };
			}

			return DeclarationProposal.NONE;
		}

		return DeclarationProposal.NONE;
	}

	private IDeclarationProposal[] processFieldNode(
			IDeclarationSearchInfo info, FieldNode fieldNode) {
		EditorPartFacade facade = new EditorPartFacade(info.getEditor());
		IRegion region = ASTUtils.getRegion(facade, fieldNode, info
				.getIdentifier().length());
		if (region != null) {
			return new IDeclarationProposal[] { new DeclarationProposal(
					DeclarationCategory.FIELD, ASTUtils
							.createDisplayString(fieldNode),
					new FileSourceCode(facade.getFile(), region)) };
		}
		return DeclarationProposal.NONE;
	}

	private IDeclarationProposal[] processVariableExpression(
			IDeclarationSearchInfo info, VariableExpression expr) {
		EditorPartFacade facade = new EditorPartFacade(info.getEditor());
		IRegion highlight = ASTUtils.getRegion(facade, expr, info
				.getIdentifier().length());
		if (highlight != null) {
			return new IDeclarationProposal[] { new DeclarationProposal(
					DeclarationCategory.LOCAL, ASTUtils
							.createDisplayString(expr), new FileSourceCode(
							facade.getFile(), highlight)) };
		}
		return DeclarationProposal.NONE;
	}

	private IDeclarationProposal[] processParameter(
			IDeclarationSearchInfo info, VariableExpression expr,
			Parameter param) {
		ASTSearchResult result = ASTNodeFinder.findSurroundingClosure(info
				.getModuleNode(), expr);

		if (result == null) {
			result = ASTNodeFinder.findSurroundingMethod(info.getModuleNode(),
					expr);
		}

		if (result != null) {
			return processClosureOrMethodExpression(info, param, result);
		}
		return DeclarationProposal.NONE;
	}

	private IDeclarationProposal[] processClosureOrMethodExpression(
			IDeclarationSearchInfo info, Parameter param, ASTSearchResult result) {
		ASTNode expr = result.getASTNode();
		EditorPartFacade facade = new EditorPartFacade(info.getEditor());
		try {
			int offset0 = facade.getOffset(expr.getLineNumber() - 1, expr
					.getColumnNumber() - 1);
			int offset1 = facade.getOffset(expr.getLastLineNumber() - 1, expr
					.getLastColumnNumber() - 1);
			String text = facade.getText(offset0, offset1 - offset0 + 1);

			// Closure patch, ugh.
			if (result.getASTNode() instanceof ClosureExpression) {
				offset0 = facade.getLineOffset(expr.getLineNumber() - 1);
				text = facade.getLine(expr.getLineNumber() - 1);
			}

			int identOffset = TextUtils.findIdentifierOffset(text, param
					.getName());
			if (identOffset != -1) {
				Region region = new Region(offset0 + identOffset, param
						.getName().length());
				return new IDeclarationProposal[] { new DeclarationProposal(
						DeclarationCategory.METHOD_PARAMETER, param.getName(),
						new FileSourceCode(facade.getFile(), region)) };
			}
		} catch (BadLocationException e) {
			GroovyPlugin.getPlugin().logException("Should not happen", e);
		}
		return DeclarationProposal.NONE;
	}
}
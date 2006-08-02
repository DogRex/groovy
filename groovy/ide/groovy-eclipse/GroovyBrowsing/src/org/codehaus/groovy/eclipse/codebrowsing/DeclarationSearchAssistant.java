package org.codehaus.groovy.eclipse.codebrowsing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.MethodPointerExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.codebrowsing.impl.ClassParentsFileFindingProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.impl.FieldTypeFileFindingProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.impl.ThisClassExpressionProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.impl.ThisMethodCallExpressionProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.impl.ThisMethodPointerExpressionProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.impl.ThisPropertyExpressionProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.impl.ThisVariableExpressionProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.impl.VariableTypeFileFindingProcessor;
import org.codehaus.groovy.eclipse.editor.actions.EditorPartFacade;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorPart;

/**
 * Singleton implementation of IDeclarationSearchAssistant.
 * 
 * @author emp
 */
public class DeclarationSearchAssistant implements IDeclarationSearchAssistant {
	private static IDeclarationSearchAssistant instance;

	public static IDeclarationSearchAssistant getInstance() {
		if (instance == null) {
			instance = new DeclarationSearchAssistant();
		}
		return instance;
	}

	private DeclarationSearchAssistant() {
		DeclarationSearchProcessorRegistry
				.registerProcessor(VariableExpression.class,
						new ThisVariableExpressionProcessor());
		DeclarationSearchProcessorRegistry
				.registerProcessor(PropertyExpression.class,
						new ThisPropertyExpressionProcessor());
		DeclarationSearchProcessorRegistry.registerProcessor(
				MethodCallExpression.class,
				new ThisMethodCallExpressionProcessor());
		DeclarationSearchProcessorRegistry.registerProcessor(
				MethodPointerExpression.class,
				new ThisMethodPointerExpressionProcessor());
		DeclarationSearchProcessorRegistry.registerProcessor(
				ClassExpression.class, new ThisClassExpressionProcessor());
		DeclarationSearchProcessorRegistry.registerProcessor(
				ClassNode.class, new ClassParentsFileFindingProcessor());
		DeclarationSearchProcessorRegistry.registerProcessor(
				FieldNode.class, new FieldTypeFileFindingProcessor());
		DeclarationSearchProcessorRegistry.registerProcessor(
				VariableExpression.class, new VariableTypeFileFindingProcessor());
	}

	public List getProposals(IEditorPart editor, IRegion region) {
		// Setup
		EditorPartFacade facade = new EditorPartFacade(editor);
		String identifier;
		try {
			identifier = facade.getDocument().get(region.getOffset(),
					region.getLength());
		} catch (BadLocationException e) {
			GroovyPlugin.getPlugin().logException("Should not happen", e);
			return null;
		}
		IFile file = facade.getFile();

		List moduleNodes = GroovyModel.getModel().getModuleNodes(file);
		if (moduleNodes == null)
			return null;

		ModuleNode moduleNode = (ModuleNode) moduleNodes.get(0);
		if (moduleNode == null)
			return null;

		Point rowcol;
		try {
			rowcol = facade.getRowCol(region.getOffset());
		} catch (BadLocationException e) {
			GroovyPlugin.getPlugin().logException("Should not happen", e);
			return null;
		}
		++rowcol.x;
		++rowcol.y;

		List results = new ArrayList();
		ASTNodeSearchResult result = ASTNodeFinder.findASTNode(moduleNode,
				identifier, rowcol.y, rowcol.x);
		if (result != null) {
			System.out.println("Found at " + result.getLine() + ", "
					+ result.getColumn() + ": " + result.getASTNode());
			processAST(editor, region, results, result);
		}
		return results;
	}

	private void processAST(IEditorPart editor, IRegion region, List results,
			ASTNodeSearchResult result) {
		ASTNode node = result.getASTNode();
		List processors = DeclarationSearchProcessorRegistry
				.getProcessorsForASTClass(node.getClass());
		if (processors == null)
			return;

		for (Iterator iter = processors.iterator(); iter.hasNext();) {
			IDeclarationSearchProcessor processor = (IDeclarationSearchProcessor) iter
					.next();
			List proposals = processor.getProposals(new DeclarationSearchInfo(
					result, editor, region));
			results.addAll(proposals);
		}
	}
}

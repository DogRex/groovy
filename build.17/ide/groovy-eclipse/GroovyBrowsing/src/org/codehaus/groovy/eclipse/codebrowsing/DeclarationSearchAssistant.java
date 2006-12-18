package org.codehaus.groovy.eclipse.codebrowsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.codehaus.groovy.eclipse.codebrowsing.impl.ClassNodeProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.impl.FieldNodeProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.impl.ClassExpressionProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.impl.MethodCallExpressionProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.impl.MethodPointerExpressionProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.impl.PropertyExpressionProcessor;
import org.codehaus.groovy.eclipse.codebrowsing.impl.VariableExpressionProcessor;
import org.codehaus.groovy.eclipse.editor.actions.EditorPartFacade;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
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
	private static final String GROOVY_CONTEXT_ID = "org.codehaus.groovy";

	private static IDeclarationSearchAssistant instance;

	// [contextId: context]
	private static Map mapContextIdToContext = new HashMap();

	// [contextId : [astClassName : [processors]]
	private static Map mapContextIdToProcessorMap = new HashMap();

	static {
		registerContext(GROOVY_CONTEXT_ID, new GroovyContext());
		
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg
				.getExtensionPoint("org.codehaus.groovy.eclipse.codebrowsing.declarationSearch");
		IExtension[] extensions = ep.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IExtension extension = extensions[i];
			IConfigurationElement[] configElements = extension
					.getConfigurationElements();

			// First get all the contexts.
			for (int j = 0; j < configElements.length; j++) {
				try {
					IConfigurationElement element = configElements[j];
					if (element.getName().equals("searchContext")) {
						IDeclarationSearchContext context = (IDeclarationSearchContext) element
								.createExecutableExtension("class");
						String contextId = element.getAttribute("contextId");
						registerContext(contextId, context);
					}
				} catch (CoreException e) {
					// LOG: where to?
					e.printStackTrace();
				}
			}

			// Now get all the processors.
			for (int j = 0; j < configElements.length; j++) {
				try {
					IConfigurationElement element = configElements[j];
					if (element.getName().equals("searchProcessor")) {
						String contextId = element.getAttribute("contextId");
						String astNodeClassName = element
								.getAttribute("astNodeClass");
						IDeclarationSearchProcessor processor = (IDeclarationSearchProcessor) element
								.createExecutableExtension("class");
						registerProcessor(contextId, astNodeClassName,
								processor);
					}
				} catch (CoreException e) {
					// LOG: where to?
					e.printStackTrace();
				}
			}
		}
	}

	public static void registerProcessor(String contextId,
			String astNodeClassName, IDeclarationSearchProcessor processor) {
		if (contextId.equals("")) {
			contextId = GROOVY_CONTEXT_ID;
		}
		
		Map mapASTClassToProcessors = (Map) mapContextIdToProcessorMap
				.get(contextId);
		List processors = (List) mapASTClassToProcessors.get(astNodeClassName);
		if (processors == null) {
			processors = new ArrayList();
			mapASTClassToProcessors.put(astNodeClassName, processors);
		}

		processors.add(processor);
	}

	public static void registerContext(String contextId,
			IDeclarationSearchContext context) {
		mapContextIdToContext.put(contextId, context);
		mapContextIdToProcessorMap.put(contextId, new HashMap());
	}

	public static IDeclarationSearchAssistant getInstance() {
		if (instance == null) {
			instance = new DeclarationSearchAssistant();
		}
		return instance;
	}

	private DeclarationSearchAssistant() {
		// Register the built in processors.
		registerProcessor(GROOVY_CONTEXT_ID,
				VariableExpression.class.getName(),
				new VariableExpressionProcessor());
		registerProcessor(GROOVY_CONTEXT_ID,
				PropertyExpression.class.getName(),
				new PropertyExpressionProcessor());
		registerProcessor(GROOVY_CONTEXT_ID, MethodCallExpression.class
				.getName(), new MethodCallExpressionProcessor());
		registerProcessor(GROOVY_CONTEXT_ID, MethodPointerExpression.class
				.getName(), new MethodPointerExpressionProcessor());
		registerProcessor(GROOVY_CONTEXT_ID, ClassExpression.class.getName(),
				new ClassExpressionProcessor());
		registerProcessor(GROOVY_CONTEXT_ID, ClassNode.class.getName(),
				new ClassNodeProcessor());
		registerProcessor(GROOVY_CONTEXT_ID, FieldNode.class.getName(),
				new FieldNodeProcessor());
	}

	public IDeclarationProposal[] getProposals(IEditorPart editor,
			IRegion region) {
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

		ASTSearchResult result = ASTNodeFinder.findASTNode(moduleNode,
				identifier, rowcol.y, rowcol.x);
		if (result != null) {
			System.out.println("Found at " + result.getLine() + ", "
					+ result.getColumn() + ": " + result.getASTNode());
			List results = processAST(editor, region, result);
			return (IDeclarationProposal[]) results
			.toArray(new IDeclarationProposal[results.size()]);
		}
		return null;
	}

	private List processAST(IEditorPart editor, IRegion region,
			ASTSearchResult result) {
		List results = new ArrayList();

		ASTNode node = result.getASTNode();
		
		Set contextIds = mapContextIdToContext.keySet();
		for (Iterator iterCtx = contextIds.iterator(); iterCtx.hasNext();) {
			String contextId = (String)iterCtx.next();
			IDeclarationSearchContext context = (IDeclarationSearchContext) mapContextIdToContext.get(contextId);
			if (context.isActiveContext()) {
				Map mapASTClassNameToProcessors = (Map)mapContextIdToProcessorMap.get(contextId);
				List processors = (List)mapASTClassNameToProcessors.get(node.getClass().getName());
				if (processors != null) {
					for (Iterator iter = processors.iterator(); iter.hasNext();) {
						IDeclarationSearchProcessor processor = (IDeclarationSearchProcessor) iter
								.next();
						IDeclarationProposal[] proposals = processor
								.getProposals(new DeclarationSearchInfo(result, editor,
										region));
						results.addAll(Arrays.asList(proposals));
					}
				}
			}
		}
		
		return results;
	}
}

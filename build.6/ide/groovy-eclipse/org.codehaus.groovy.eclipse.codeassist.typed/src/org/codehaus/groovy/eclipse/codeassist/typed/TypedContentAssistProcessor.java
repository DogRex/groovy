package org.codehaus.groovy.eclipse.codeassist.typed;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Variable;
import org.codehaus.groovy.ast.VariableScope;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.eclipse.codeassist.DocumentReverseCharSequence;
import org.codehaus.groovy.eclipse.codeassist.FindASTNode;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class TypedContentAssistProcessor implements IContentAssistProcessor {
	// The regex matches backwards, so it is specified backwards.
	private static final Pattern regex = Pattern.compile("(\\w*)\\.(\\w+)");

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		// Find a method node in which to complete.
		MethodNode methodNode = (MethodNode) FindASTNode.enclosedBy(MethodNode.class, viewer, offset);
		if (methodNode == null) {
			return null;
		}
		
		// Match a possible property access: 'obj.prop'
		DocumentReverseCharSequence seq = new DocumentReverseCharSequence(viewer.getDocument(),
				(offset - 1 > 0) ? offset - 1 : 0, 0);
		Matcher matcher = regex.matcher(seq);
		if (matcher.find()) {
			String prefix = DocumentReverseCharSequence.reverse(matcher.group(1));
			String varName = DocumentReverseCharSequence.reverse(matcher.group(2));
			BlockStatement code = (BlockStatement) methodNode.getCode();
			Variable var = getVariable(code.getVariableScope(), varName);
			if (var == null) {
				return null;
			}

			ClassNode type = var.getType();

			String qualifiedName = type.getName();

			// Dream on ...
			// if (qualifiedName.equals("java.lang.Object")) {
			// qualifiedName = inferType(type);
			// }

			return Completer.completeWithType(qualifiedName, prefix, offset);
		}

		return null;
	}

	private Variable getVariable(VariableScope variableScope, String varName) {
		// TODO: this needs to be replaced with something like this. I find that
		// sometimes the scope doesn't have the var!? (at least that is what I
		// think I see). But the AST does, so walk the AST.
		// FindASTNode.matchingVariableName(method, varName); <-- wish list.
		Variable var = variableScope.getDeclaredVariable(varName);
		if (var != null) {
			return var;
		}

		var = (Variable) variableScope.getReferencedLocalVariables().get(varName);
		if (var != null) {
			return var;
		}

		var = (Variable) variableScope.getReferencedClassVariables().get(varName);

		return var;
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return null;
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		return null;
	}

	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

	public String getErrorMessage() {
		return null;
	}
}

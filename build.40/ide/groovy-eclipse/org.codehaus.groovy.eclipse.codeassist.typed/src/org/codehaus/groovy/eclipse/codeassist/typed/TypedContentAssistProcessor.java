package org.codehaus.groovy.eclipse.codeassist.typed;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.groovy.eclipse.codeassist.CodeAssistUtil;
import org.codehaus.groovy.eclipse.codeassist.DocumentReverseCharSequence;
import org.codehaus.groovy.eclipse.codeassist.FindInSource;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class TypedContentAssistProcessor implements IContentAssistProcessor {
	private static final Pattern regex = Pattern.compile(CodeAssistUtil.reverseRegex("(\\w+)\\.(\\w*)"));

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		// Match a possible property access: 'obj.prop'
		DocumentReverseCharSequence seq = new DocumentReverseCharSequence(viewer.getDocument(),
				(offset - 1 > 0) ? offset - 1 : 0, 0);
		Matcher matcher = regex.matcher(seq);
		if (matcher.find() && matcher.start() <= 1) {
			String rightText = DocumentReverseCharSequence.reverse(matcher.group(1));
			String leftExpression = DocumentReverseCharSequence.reverse(matcher.group(2));
			Class type = FindInSource.variableType(leftExpression, viewer, offset);
			if (type == null) {
				return null;
			}
			return Completer.completeWithType(type, rightText, offset);
		}

		return null;
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

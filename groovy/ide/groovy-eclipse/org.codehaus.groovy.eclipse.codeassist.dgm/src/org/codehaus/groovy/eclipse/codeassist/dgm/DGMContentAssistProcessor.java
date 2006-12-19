package org.codehaus.groovy.eclipse.codeassist.dgm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.groovy.eclipse.codeassist.CodeAssistUtil;
import org.codehaus.groovy.eclipse.codeassist.DocumentReverseCharSequence;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class DGMContentAssistProcessor implements IContentAssistProcessor {
	private Pattern pattern = Pattern.compile(CodeAssistUtil.reverseRegex("(\\w+)\\.(\\w*)"));

	public DGMContentAssistProcessor() {
	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		if (offset == 0) {
			return null;
		}
		
		IDocument doc = viewer.getDocument();
		DocumentReverseCharSequence seq = new DocumentReverseCharSequence(doc, offset - 1, 0);

		Matcher matcher = pattern.matcher(seq);
		if (matcher.find() && matcher.start() <= 1) {
			String rightText = DocumentReverseCharSequence.reverse(matcher.group(1));
			String leftExpression = DocumentReverseCharSequence.reverse(matcher.group(2));
			return Completer.getDGMCompletions(leftExpression, rightText, viewer, offset);
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
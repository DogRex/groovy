package org.codehaus.groovy.eclipse.codeassist.dgm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.groovy.eclipse.codeassist.DocumentReverseCharSequence;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class DGMContentAssistProcessor implements IContentAssistProcessor {
	private Pattern pattern = Pattern.compile("(\\w*)\\.\\w+");

	public DGMContentAssistProcessor() {
	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		IDocument doc = viewer.getDocument();
		DocumentReverseCharSequence seq = new DocumentReverseCharSequence(doc, (offset - 1 > 0) ? offset - 1 : 0, 0);
		Matcher matcher = pattern.matcher(seq);
		if (matcher.find()) {
			String prefix = DocumentReverseCharSequence.reverse(matcher.group(1));
			return Completer.getDGMCompletions(prefix, offset);
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
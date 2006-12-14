package org.codehaus.groovy.eclipse.codeassist.dgm;

import groovy.lang.GroovyShell;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.eclipse.editor.contentAssist.DocumentReverseCharSequence;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;

public class DGMContentAssistProcessor implements IContentAssistProcessor {
	private Image icon = new Image(null, (java.io.InputStream)getClass().getResourceAsStream("icon.png"));
	
	private Pattern pattern = Pattern.compile("(\\w*)\\.\\w+");

	private List displayStrings;

	private List replaceStrings;
	
	public DGMContentAssistProcessor() {
		GroovyShell shell = new GroovyShell();

		InputStream is = getClass().getResourceAsStream("CompletionList.groovy");
		if (is != null) {
			try {
				Map arrays = (Map) shell.evaluate(is);
				displayStrings = (List) arrays.get("displayStrings");
				replaceStrings = (List) arrays.get("replaceStrings");
				is.close();
			} catch (IOException e) {
				// LOG
				// This should never happen.
				e.printStackTrace();
			} catch (MultipleCompilationErrorsException e) {
//				for (Object error : e.getErrorCollector().getErrors()) {
//					System.out.println(error.toString());
//				}
				// LOG and clean up.
				e.printStackTrace();
			}
		}
	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		IDocument doc = viewer.getDocument();
		DocumentReverseCharSequence seq = new DocumentReverseCharSequence(doc, (offset - 1 > 0) ? offset - 1 : 0, 0);
		Matcher matcher = pattern.matcher(seq);
		if (matcher.find()) {
			for (int i = 0; i <= matcher.groupCount(); ++i) {
				System.out.println(matcher.group(i));
			}
			String partialMatch = matcher.group(1);
//			if (partialMatch != null && partialMatch.length() != 0) {
				partialMatch = reverse(partialMatch); // No reverse in jdk?
				partialMatch = partialMatch.toLowerCase();
				return createCompletionProposals(partialMatch, offset);
//			}
		}

		// return new ICompletionProposal[] { new CompletionProposal("hello",
		// offset, 0, 5) };
		return null;
	}

	private ICompletionProposal[] createCompletionProposals(String partialMatch, int offset) {
		List proposals = new ArrayList();
		int replaceLength = partialMatch.length();
		offset -= replaceLength;
		for (int i = 0; i < replaceStrings.size(); ++i) {
			String replaceString = (String) replaceStrings.get(i);
			if (replaceString.toLowerCase().startsWith(partialMatch)) {
				String displayString = (String) displayStrings.get(i);
				int cursorPosition = replaceString.lastIndexOf(')');
				proposals.add(new CompletionProposal(replaceString, offset, replaceLength, cursorPosition, icon,
						displayString, null, null));
			}
		}
		return (ICompletionProposal[]) proposals.toArray(new ICompletionProposal[proposals.size()]);
	}

	private String reverse(String string) {
		char[] reverse = string.toCharArray();
		for (int i = 0; i < reverse.length / 2; i++) {
			char tmp = reverse[i];
			reverse[i] = reverse[reverse.length - 1 - i];
			reverse[reverse.length - 1 - i] = tmp;
		}
		return new String(reverse);
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return null;
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		return null;
		// // TODO: Assuming we want this, if I can find out what this is when
		// Eclipse starts a
		// // completion, then I can use only the correct processors.
		// return new char[] { '.' };
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
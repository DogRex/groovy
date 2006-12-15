package org.codehaus.groovy.eclipse.editor.contentAssist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

/**
 * A set of IContentAssistProcessor.
 * 
 * Each plugin for completion contributes one or more IContentAssistProcessor.
 * For each partition, one ContentAssistProcessorSet contains all the
 * contributed processors.
 * 
 * @author emp
 */
public class ContentAssistProcessorSet implements IContentAssistProcessor {
	private class ContextInformationValidatorSet implements IContextInformationValidator {
		List validators;

		ContextInformationValidatorSet(List validators) {
			this.validators = validators;
		}

		public void install(IContextInformation info, ITextViewer viewer, int offset) {
			for (Iterator iter = validators.iterator(); iter.hasNext();) {
				IContextInformationValidator validator = (IContextInformationValidator) iter.next();
				validator.install(info, viewer, offset);
			}
		}

		public boolean isContextInformationValid(int offset) {
			for (Iterator iter = validators.iterator(); iter.hasNext();) {
				IContextInformationValidator validator = (IContextInformationValidator) iter.next();
				if (validator.isContextInformationValid(offset)) {
					return true;
				}
			}
			return false;
		}
	}

	private List processors = new ArrayList();

	private IContextInformationValidator validator;

	public void addProcessor(IContentAssistProcessor processor) {
		processors.add(processor);
	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		List proposals = new ArrayList();
		for (Iterator iter = processors.iterator(); iter.hasNext();) {
			IContentAssistProcessor processor = (IContentAssistProcessor) iter.next();
			ICompletionProposal[] p = processor.computeCompletionProposals(viewer, offset);
			if (p != null) {
				proposals.addAll(Arrays.asList(p));
			}
		}
		Collections.sort(proposals, new Comparator() {
			public int compare(Object a, Object b) {
				return ((ICompletionProposal)a).getDisplayString().compareTo(((ICompletionProposal)b).getDisplayString());
			}
		});
		return (ICompletionProposal[]) proposals.toArray(new ICompletionProposal[proposals.size()]);
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		List infos = new ArrayList();
		for (Iterator iter = processors.iterator(); iter.hasNext();) {
			IContentAssistProcessor processor = (IContentAssistProcessor) iter.next();
			IContextInformation[] ci = processor.computeContextInformation(viewer, offset);
			if (ci != null) {
				infos.addAll(Arrays.asList(ci));
			}
		}
		return (IContextInformation[]) infos.toArray(new IContextInformation[infos.size()]);
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		// I can dream:
		// chars = []
		// processors.each {
		// def ch = it.getCompletionProposalAutoActivationCharacters()
		// ch?.each { chars << it }
		// }
		// return chars as char[]

		// There must be a easier way?
		List chars = new ArrayList();
		for (Iterator iter = processors.iterator(); iter.hasNext();) {
			IContentAssistProcessor processor = (IContentAssistProcessor) iter.next();
			char[] ch = processor.getCompletionProposalAutoActivationCharacters();
			if (ch != null) {
				for (int i = 0; i < ch.length; ++i) {
					chars.add(new Character(ch[i]));
				}
			}
		}
		char[] ret = new char[chars.size()];
		for (int i = 0; i < ret.length; ++i) {
			ret[i] = ((Character) chars.get(i)).charValue();
		}

		return ret;
	}

	public char[] getContextInformationAutoActivationCharacters() {
		List chars = new ArrayList();
		for (Iterator iter = processors.iterator(); iter.hasNext();) {
			IContentAssistProcessor processor = (IContentAssistProcessor) iter.next();
			char[] ch = processor.getContextInformationAutoActivationCharacters();
			if (ch != null) {
				for (int i = 0; i < ch.length; ++i) {
					chars.add(new Character(ch[i]));
				}
			}
		}
		char[] ret = new char[chars.size()];
		for (int i = 0; i < ret.length; ++i) {
			ret[i] = ((Character) chars.get(i)).charValue();
		}

		return ret;
	}

	public IContextInformationValidator getContextInformationValidator() {
		if (validator == null) {
			List validators = new ArrayList();
			for (Iterator iter = processors.iterator(); iter.hasNext();) {
				IContentAssistProcessor processor = (IContentAssistProcessor) iter.next();
				IContextInformationValidator v = processor.getContextInformationValidator();
				if (v != null) {
					validators.add(v);
				}
			}
			validator = new ContextInformationValidatorSet(validators);
		}

		return validator;
	}

	public String getErrorMessage() {
		for (Iterator iter = processors.iterator(); iter.hasNext();) {
			IContentAssistProcessor processor = (IContentAssistProcessor) iter.next();
			String message = processor.getErrorMessage();
			if (message != null) {
				return message;
			}
		}

		return null;
	}
}

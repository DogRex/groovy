package org.codehaus.groovy.eclipse.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class GroovyConfiguration extends SourceViewerConfiguration {
	
	private GroovyDoubleClickStrategy doubleClickStrategy;
	private GroovyTagScanner tagScanner;
	private GroovyScanner scanner;
	private ColorManager colorManager;

	public GroovyConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			GroovyPartitionScanner.XML_COMMENT,
			GroovyPartitionScanner.XML_TAG };
	}
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new GroovyDoubleClickStrategy();
		return doubleClickStrategy;
	}

	protected GroovyScanner getXMLScanner() {
		if (scanner == null) {
			scanner = new GroovyScanner(colorManager);
			scanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IGroovyColorConstants.DEFAULT))));
		}
		return scanner;
	}
	protected GroovyTagScanner getXMLTagScanner() {
		if (tagScanner == null) {
			tagScanner = new GroovyTagScanner(colorManager);
			tagScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IGroovyColorConstants.DEFAULT))));
		}
		return tagScanner;
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr =
			new DefaultDamagerRepairer(getXMLTagScanner());
		reconciler.setDamager(dr, GroovyPartitionScanner.XML_TAG);
		reconciler.setRepairer(dr, GroovyPartitionScanner.XML_TAG);

		dr = new DefaultDamagerRepairer(getXMLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

//		NonRuleBasedDamagerRepairer ndr =
//			new NonRuleBasedDamagerRepairer(
//				new TextAttribute(
//					colorManager.getColor(IGroovyColorConstants.XML_COMMENT)));
//		reconciler.setDamager(ndr, GroovyPartitionScanner.XML_COMMENT);
//		reconciler.setRepairer(ndr, GroovyPartitionScanner.XML_COMMENT);

		return reconciler;
	}

}
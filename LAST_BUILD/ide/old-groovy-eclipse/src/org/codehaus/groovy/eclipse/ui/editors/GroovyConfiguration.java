package org.codehaus.groovy.eclipse.ui.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class GroovyConfiguration extends SourceViewerConfiguration {
	private ColorManager colors;
	private GroovyCodeScanner scanner;
	
	public GroovyConfiguration(ColorManager colors) {
		this.colors = colors;
		this.scanner = new GroovyCodeScanner(colors);
	}

	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		return new GroovyDoubleClickStrategy();
	}

	private GroovyCodeScanner getScanner() {
		return scanner;
	}
	
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler= new PresentationReconciler();

		// rule for default text
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(
			new TextAttribute(colors.getColor(ColorManager.COMMENT)));
		reconciler.setDamager(ndr, GroovyPartitionScanner.SINGLELINE_COMMENT);
		reconciler.setRepairer(ndr, GroovyPartitionScanner.SINGLELINE_COMMENT);
		
		ndr = new NonRuleBasedDamagerRepairer(
			new TextAttribute(colors.getColor(ColorManager.COMMENT)));
		reconciler.setDamager(ndr, GroovyPartitionScanner.MULTILINE_COMMENT);
		reconciler.setRepairer(ndr, GroovyPartitionScanner.MULTILINE_COMMENT);
		
		ndr = new NonRuleBasedDamagerRepairer(
			new TextAttribute(colors.getColor(ColorManager.STRING)));
		reconciler.setDamager(ndr, GroovyPartitionScanner.STRING);
		reconciler.setRepairer(ndr, GroovyPartitionScanner.STRING);
		
		return reconciler;
	}
}
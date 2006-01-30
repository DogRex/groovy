package org.codehaus.groovy.eclipse.editor;

import java.util.ArrayList;
import java.util.Iterator;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.editor.GroovySourceViewerConfiguration.Hover;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.ui.text.IJavaColorConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.texteditor.MarkerAnnotation;

public class GroovyConfiguration extends SourceViewerConfiguration {
	    
	private GroovyDoubleClickStrategy doubleClickStrategy;
	private GroovyTagScanner tagScanner;
	private ColorManager colorManager;

	/**
	 * Single token scanner.
	 */
	static class SingleTokenScanner extends BufferedRuleBasedScanner {
		public SingleTokenScanner(TextAttribute attribute) {
			setDefaultReturnToken(new Token(attribute));
		}
	}
	
	public GroovyConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { 
                IDocument.DEFAULT_CONTENT_TYPE, 
                GroovyPartitionScanner.GROOVY_MULTILINE_COMMENT,
                GroovyPartitionScanner.GROOVY_MULTILINE_STRINGS,
                GroovyPartitionScanner.GROOVY_SINGLELINE_STRINGS
		};		
	}
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new GroovyDoubleClickStrategy();
		return doubleClickStrategy;
	}

	/*
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredDocumentPartitioning(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return GroovyPlugin.GROOVY_PARTITIONING;
		
	}
	
	protected GroovyTagScanner getGroovyScanner() {
		if (tagScanner == null) {
			tagScanner = new GroovyTagScanner(colorManager);
			tagScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IJavaColorConstants.JAVA_DEFAULT))));
		}
		return tagScanner;
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getGroovyScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr= new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(colorManager.getColor(IJavaColorConstants.JAVA_MULTI_LINE_COMMENT))));
		reconciler.setDamager(dr, GroovyPartitionScanner.GROOVY_MULTILINE_COMMENT);
		reconciler.setRepairer(dr, GroovyPartitionScanner.GROOVY_MULTILINE_COMMENT);

		dr= new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(colorManager.getColor(IJavaColorConstants.JAVA_STRING))));
		reconciler.setDamager(dr, GroovyPartitionScanner.GROOVY_MULTILINE_STRINGS);
		reconciler.setRepairer(dr, GroovyPartitionScanner.GROOVY_MULTILINE_STRINGS);

		
		return reconciler;
	}
    
    public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
        return new MarkerHover();
    }
}
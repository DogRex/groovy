package org.codehaus.groovy.eclipse.editor;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.internal.ui.text.java.JavaAutoIndentStrategy;
import org.eclipse.jdt.internal.ui.text.java.JavaStringAutoIndentStrategy;
import org.eclipse.jdt.internal.ui.text.java.SmartSemicolonAutoEditStrategy;
import org.eclipse.jdt.internal.ui.text.javadoc.JavaDocAutoIndentStrategy;
import org.eclipse.jdt.ui.text.IJavaColorConstants;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

public class GroovyConfiguration extends SourceViewerConfiguration {

	private GroovyDoubleClickStrategy doubleClickStrategy;
	private GroovyTagScanner tagScanner;
	private GroovyMultiLineCommentScanner multiLineCommentScanner;
	private ColorManager colorManager;
	private ITextEditor editor;
	private GroovyStringScanner stringScanner;

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

	protected GroovyMultiLineCommentScanner getMultiLineCommentScanner() {
		if (multiLineCommentScanner == null) {
			multiLineCommentScanner = new GroovyMultiLineCommentScanner();
		}
		return multiLineCommentScanner;
	}

	protected GroovyStringScanner getStringScanner() {
		if (stringScanner == null) {
			stringScanner = new GroovyStringScanner();
		}
		return stringScanner;
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getGroovyScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr= new DefaultDamagerRepairer(getMultiLineCommentScanner());
		reconciler.setDamager(dr, GroovyPartitionScanner.GROOVY_MULTILINE_COMMENT);
		reconciler.setRepairer(dr, GroovyPartitionScanner.GROOVY_MULTILINE_COMMENT);

		dr= new DefaultDamagerRepairer(getStringScanner());
		reconciler.setDamager(dr, GroovyPartitionScanner.GROOVY_MULTILINE_STRINGS);
		reconciler.setRepairer(dr, GroovyPartitionScanner.GROOVY_MULTILINE_STRINGS);

		dr= new DefaultDamagerRepairer(getStringScanner());
		reconciler.setDamager(dr, GroovyPartitionScanner.GROOVY_SINGLELINE_STRINGS);
		reconciler.setRepairer(dr, GroovyPartitionScanner.GROOVY_SINGLELINE_STRINGS);

		return reconciler;
	}

    public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
        return new MarkerHover();
    }
    /*
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getAutoEditStrategies(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
     */
    public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
        // TODO: from what I can see in the debugger, JAVA_DOC and JAVA_STRING
		// are never passed to this method.
    	// It would be nice to have a GROOVY_CHARACTER partition too.
    	String partitioning = getConfiguredDocumentPartitioning(sourceViewer);
        if (IJavaPartitions.JAVA_DOC.equals(contentType) || IJavaPartitions.JAVA_MULTI_LINE_COMMENT.equals(contentType))
            return new IAutoEditStrategy[] { new JavaDocAutoIndentStrategy(partitioning) };
        else if (IJavaPartitions.JAVA_STRING.equals(contentType))
            return new IAutoEditStrategy[] { new SmartSemicolonAutoEditStrategy(partitioning), new JavaStringAutoIndentStrategy(partitioning) };
        else if (IJavaPartitions.JAVA_CHARACTER.equals(contentType) || IDocument.DEFAULT_CONTENT_TYPE.equals(contentType))
            return new IAutoEditStrategy[] { new SmartSemicolonAutoEditStrategy(partitioning), 
        		new JavaAutoIndentStrategy(partitioning, getJavaProject()),
        		new AutoEnclosingPairStrategy(IJavaPartitions.JAVA_CHARACTER) };
        else if (GroovyPartitionScanner.GROOVY_SINGLELINE_STRINGS.equals(contentType))
        	return new IAutoEditStrategy[] { new AutoEnclosingPairStrategy(GroovyPartitionScanner.GROOVY_SINGLELINE_STRINGS) };
        else if (GroovyPartitionScanner.GROOVY_MULTILINE_STRINGS.equals(contentType))
        	return new IAutoEditStrategy[] { new AutoEnclosingPairStrategy(GroovyPartitionScanner.GROOVY_MULTILINE_STRINGS) };
        else
            return new IAutoEditStrategy[] { new JavaAutoIndentStrategy(partitioning, getJavaProject()) };
            //return new IAutoEditStrategy[] { new DefaultIndentLineAutoEditStrategy() };
    }

     private IJavaProject getJavaProject() {
        ITextEditor editor= getEditor();
        if (editor == null)
            return null;
 
        IEditorInput input= editor.getEditorInput();
         
        if (input == null)
            return null;
 
        return  EditorUtility.getJavaProject(input);
    }
	public ITextEditor getEditor() {
		return editor;
	}
	public void setEditor(ITextEditor editor) {
		this.editor = editor;
	}
 
}
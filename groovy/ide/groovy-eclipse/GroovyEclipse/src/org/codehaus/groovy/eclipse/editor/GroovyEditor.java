package org.codehaus.groovy.eclipse.editor;
import java.util.ResourceBundle;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.editor.contentoutline.GroovyContentOutline;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;



public class GroovyEditor extends AbstractDecoratedTextEditor{
    private ColorManager colorManager;

    protected GroovyPairMatcher pairMatcher = new GroovyPairMatcher();

    public GroovyEditor() {
        super();

        setPreferenceStore(new ChainedPreferenceStore(
                new IPreferenceStore[] {
                        getPreferenceStore(),
                        JavaPlugin.getDefault().getPreferenceStore(),
                        GroovyPlugin.getDefault().getPreferenceStore()
                }));

        colorManager = new ColorManager();
        GroovyConfiguration groovyConfiguration = new GroovyConfiguration(colorManager);
        groovyConfiguration.setEditor(this);
        setSourceViewerConfiguration(groovyConfiguration);
    }
    
    /**
     * The GroovyEditor scope is:
     * org.codehaus.groovy.eclipse.editor.GroovyEditorScope
     * GroovyEditor specific key bindings use this as their 'contextId' so that 
     * they are active only when a GroovyEditor has focus.
     */
    protected void initializeKeyBindingScopes() {
        setKeyBindingScopes(new String[] { "org.codehaus.groovy.eclipse.editor.GroovyEditorScope" });
    }
    
    /*
     * @see IAdaptable#getAdapter(java.lang.Class)
     * @since 2.0
     */
    public Object getAdapter(Class adapter) {
        if (adapter.equals(IContentOutlinePage.class)) {
            IFile file = (IFile) getEditorInput().getAdapter(IFile.class);
            if (file!=null) return new GroovyContentOutline(file);
        }
        return super.getAdapter(adapter);
    }
    
    public void dispose() {
        colorManager.dispose();
        super.dispose();
    }
    
    public int getCaretOffset() {
        ISourceViewer viewer = getSourceViewer();
        return viewer.getTextWidget().getCaretOffset();
    }
    
    public void setCaretOffset(int offset) {
        ISourceViewer viewer = getSourceViewer();
        viewer.getTextWidget().setCaretOffset(offset);
    }
    
    protected void editorContextMenuAboutToShow(IMenuManager menu) {
        super.editorContextMenuAboutToShow(menu);
        addAction(menu, "org.codehaus.groovy.eclipse.actions.RunGroovy");
    }
    
    protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
        support.setCharacterPairMatcher(pairMatcher);
        support.setMatchingCharacterPainterPreferenceKeys(
                org.eclipse.jdt.ui.PreferenceConstants.EDITOR_MATCHING_BRACKETS,
                org.eclipse.jdt.ui.PreferenceConstants.EDITOR_MATCHING_BRACKETS_COLOR);
        super.configureSourceViewerDecorationSupport(support);
    }

    protected void createActions()
    {
        super.createActions();
        final Action action = new ContentAssistAction( ResourceBundle.getBundle( GroovyEditor.class.getPackage().getName() + ".messages" ), "ContentAssistProposal.", this );
        action.setActionDefinitionId( ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS );
        setAction( "ContentAssist", action );
    }
}

package org.codehaus.groovy.eclipse.editor.contentAssist;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class GroovyContentAssistProcessor 
implements IContentAssistProcessor
{
    private final IContentAssistProcessor hippie = new HippieProposalProcessor();
    
    public ICompletionProposal[] computeCompletionProposals( final ITextViewer viewer, 
                                                             final int offset )
    {
        final List proposals = new ArrayList();
        final ICompletionProposal[] hippieProposals = hippie.computeCompletionProposals( viewer, offset );
        for( int i = 0; i < hippieProposals.length; i++ )
            proposals.add( hippieProposals[ i ] );
        return ( ICompletionProposal[] )proposals.toArray( new ICompletionProposal[ 0 ] );
    }
    public static String lastWord( final IDocument doc, 
                                   final int offset )
    {
        try
        {
            for( int n = offset - 1; n >= 0; n-- )
            {
                final char c = doc.getChar( n );
                if( !Character.isJavaIdentifierPart( c ) )
                    return doc.get( n + 1, offset - n - 1 );
            }
        }
        catch( final BadLocationException e )
        {
            GroovyPlugin.getPlugin().logException( e.getMessage(), e );
        }
        return "";
    }
    public static String lastIndent( final IDocument doc, 
                                     final int offset )
    {
        try
        {
            int start = offset - 1;
            while( start >= 0 && doc.getChar( start ) != '\n' )
                start--;
            int end = start;
            while( end < offset && Character.isSpaceChar( doc.getChar( end ) ) )
                end++;
            return doc.get( start + 1, end - start - 1 );
        }
        catch( final BadLocationException e )
        {
            GroovyPlugin.getPlugin().logException( e.getMessage(), e );
        }
        return "";
    }
    public IContextInformation[] computeContextInformation( final ITextViewer viewer, final int offset )
    {
        return hippie.computeContextInformation( viewer, offset );
    }
    public char[] getCompletionProposalAutoActivationCharacters()
    {
        return hippie.getCompletionProposalAutoActivationCharacters();
    }
    public char[] getContextInformationAutoActivationCharacters()
    {
        return hippie.getContextInformationAutoActivationCharacters();
    }
    public IContextInformationValidator getContextInformationValidator()
    {
        return hippie.getContextInformationValidator();
    }
    public String getErrorMessage()
    {
        return hippie.getErrorMessage();
    }
}

package org.codehaus.groovy.eclipse.actions;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.builder.GroovyNature;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.codehaus.groovy.eclipse.model.GroovyProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class AddGroovyNatureAction 
implements IObjectActionDelegate
{
    private ISelection selection;

    public void run( final IAction action )
    {
        GroovyPlugin.trace( "AddGroovySupportAction.run()" );
        final GroovyPlugin plugin = GroovyPlugin.getPlugin();
        final IStructuredSelection s = ( IStructuredSelection )selection;
        final Object selected = s.getFirstElement();
        if( !( selected instanceof IProject ) && !( selected instanceof IJavaProject ) )
            return;
        final IProject targetProject = selected instanceof IProject ? ( IProject )selected : ( ( IJavaProject )selected ).getProject();
        try
        {
            if( targetProject.hasNature( GroovyNature.GROOVY_NATURE ) )
                return;
            final GroovyModel model = GroovyModel.getModel();
            final GroovyProject groovyProject = model.getProject( targetProject );
            groovyProject.addGrovyExclusionFilter( targetProject );
            plugin.addGroovyRuntime( targetProject );
            GroovyProject.addGroovyNature( targetProject );
        }
        catch( final CoreException e )
        {
            plugin.logException( "failed to add groovy support", e );
        }
        finally
        {
            plugin.listenToChanges();
        }
    }
    /**
     * @see IEditorActionDelegate#selectionChanged
     */
    public void selectionChanged( final IAction action, 
                                  final ISelection selection )
    {
        // StructuredSelection.elements[0] should be a JavaProject
        this.selection = selection;
    }
    public void setActivePart( final IAction action, 
                               final IWorkbenchPart targetPart )
    {
    }
}

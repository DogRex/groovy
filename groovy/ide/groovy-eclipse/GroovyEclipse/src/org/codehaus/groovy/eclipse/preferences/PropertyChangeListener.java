package org.codehaus.groovy.eclipse.preferences;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.codehaus.groovy.eclipse.model.GroovyProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * The sole purpose of this class is to wait for updates to the 
 * PreferenceConstants.GROOVY_COMPILER_OUTPUT_PATH property in the GroovyPropertyStore
 * for the given project and to tell the GroovyProject to update its groovy output location
 * accordingly.
 */
public class PropertyChangeListener
implements IPropertyChangeListener
{
    private final IProject project;
    
    public PropertyChangeListener( final IResource resource )
    {
        this.project = resource.getProject();
    }
    public void propertyChange( final PropertyChangeEvent event )
    {
        final GroovyProject groovyProject = GroovyModel.getModel().getProject( project );
        if( StringUtils.equals( event.getProperty(), PreferenceConstants.GROOVY_COMPILER_OUTPUT_PATH ) ){
        	GroovyPlugin.trace("About to call groovyProject.setOutputPath() from PropertyChangeListener");
            groovyProject.setOutputPath( ObjectUtils.toString( event.getOldValue(), null ), ObjectUtils.toString( event.getNewValue(), null ) );
        }
    }
}

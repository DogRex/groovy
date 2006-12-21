package org.codehaus.groovy.eclipse.preferences;
import java.util.ResourceBundle;

public class Messages
{
    public final static String RESOURCE_BUNDLE = Messages.class.getPackage().getName() + ".Messages";//$NON-NLS-1$
    private static ResourceBundle resourceBundle = null;
    private static boolean notRead = true;

    public Messages() {}
    public static ResourceBundle getResourceBundle()
    {
        if( !notRead )
            return resourceBundle;
        notRead = false;
        try
        {
            resourceBundle = ResourceBundle.getBundle( RESOURCE_BUNDLE );
            return resourceBundle;
        }
        catch( final Exception e )
        {
            throw new RuntimeException( e );
        }
    }
    public static String getString( final String key )
    {
        try
        {
            return getResourceBundle().getString( key );
        }
        catch( final Exception e )
        {
            return "!" + key + "!";//$NON-NLS-2$ //$NON-NLS-1$
        }
    }
}

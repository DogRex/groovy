package org.codehaus.groovy.eclipse.ui.editors;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class GroovyEditorMessages {

    private static final String RESOURCE_BUNDLE = GroovyEditorMessages.class.getName();

    private static ResourceBundle fgResourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);

    private GroovyEditorMessages() {
    }

    public static String getString(String key) {
        try {
            return fgResourceBundle.getString(key);
        }
        catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }

    public static ResourceBundle getResourceBundle() {
        return fgResourceBundle;
    }
}

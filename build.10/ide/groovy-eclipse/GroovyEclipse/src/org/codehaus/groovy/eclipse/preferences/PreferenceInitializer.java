package org.codehaus.groovy.eclipse.preferences;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.Color;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = GroovyPlugin.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.GROOVY_GENERATE_CLASS_FILES, true);
        store.setDefault( PreferenceConstants.GROOVY_COMPILER_OUTPUT_PATH, "bin-groovy" );
		store.setDefault(PreferenceConstants.GROOVY_LOG_TRACE_MESSAGES_ENABLED, false);
		
		// GJDK Prefs
		store.setDefault(PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_GJDK_ENABLED, true);
		PreferenceConverter.setDefault(store, 
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_GJDK_COLOR, 
				new Color(null, 102,204,255).getRGB());
		
		// Multiline Comment Prefs
		store.setDefault(PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_MULTILINECOMMENTS_ENABLED, true);
		PreferenceConverter.setDefault(store, 
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_MULTILINECOMMENTS_COLOR,
				new Color(null, 204, 0, 0).getRGB());

		// Java Keyword Prefs
		store.setDefault(PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_JAVAKEYWORDS_ENABLED, true);
		PreferenceConverter.setDefault(store, 
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_JAVAKEYWORDS_COLOR,
				new Color(null, 0, 102, 153).getRGB());
		
		// Groovy Keyword Prefs
		store.setDefault(PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_GROOVYKEYWORDS_ENABLED, true);
		PreferenceConverter.setDefault(store, 
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_GROOVYKEYWORDS_COLOR,
				new Color(null, 0, 153, 102).getRGB());
		
		// Java Types Prefs
		store.setDefault(PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_JAVATYPES_ENABLED, true);
		PreferenceConverter.setDefault(store, 
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_JAVATYPES_COLOR,
				new Color(null, 0, 153, 255).getRGB());
		
		// String Prefs
		store.setDefault(PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_STRINGS_ENABLED, true);
		PreferenceConverter.setDefault(store, 
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_STRINGS_COLOR,
				new Color(null, 255, 0, 204).getRGB());
		
		// Number Prefs
		store.setDefault(PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_NUMBERS_ENABLED, true);
		PreferenceConverter.setDefault(store, 
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_NUMBERS_COLOR,
				new Color(null, 255, 0, 0).getRGB());
		
		
	}

}

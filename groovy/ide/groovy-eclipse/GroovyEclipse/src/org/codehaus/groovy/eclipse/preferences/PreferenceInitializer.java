package org.codehaus.groovy.eclipse.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.Color;

import org.codehaus.groovy.eclipse.GroovyPlugin;

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
		IPreferenceStore store = GroovyPlugin.getDefault()
				.getPreferenceStore();
		store.setDefault(PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_GJDK_ENABLED, true);
		store.setDefault(PreferenceConstants.GROOVY_GENERATE_CLASS_FILES, true);
		Color gjdkColor = new Color(null, 0,153,255);
		PreferenceConverter.setDefault(store, PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_GJDK_COLOR, gjdkColor.getRGB());
		store.setDefault(PreferenceConstants.P_CHOICE, "choice2");
		store.setDefault(PreferenceConstants.P_STRING,
				"Default value");
	}

}

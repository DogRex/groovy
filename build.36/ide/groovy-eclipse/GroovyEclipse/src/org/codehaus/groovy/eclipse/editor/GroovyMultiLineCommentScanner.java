package org.codehaus.groovy.eclipse.editor;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.preferences.PreferenceConstants;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class GroovyMultiLineCommentScanner extends RuleBasedScanner {
	public GroovyMultiLineCommentScanner() {
		// get color
		Preferences prefs = GroovyPlugin.getDefault().getPluginPreferences();
		if (prefs.getBoolean(PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_MULTILINECOMMENTS_ENABLED)) {
			IPreferenceStore store = GroovyPlugin.getDefault().getPreferenceStore();
			RGB rgb = PreferenceConverter.getColor(store,PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_MULTILINECOMMENTS_COLOR);
			// create tokens
			IToken token = new Token(new TextAttribute(new Color(null,rgb), null, SWT.NONE));
			setDefaultReturnToken(token);
		}
	}

}

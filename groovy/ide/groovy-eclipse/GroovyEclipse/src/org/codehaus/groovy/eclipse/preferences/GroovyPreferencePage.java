package org.codehaus.groovy.eclipse.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.codehaus.groovy.eclipse.GroovyPlugin;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>,
 * we can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class GroovyPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public GroovyPreferencePage() {
		super(GRID);
		setPreferenceStore(GroovyPlugin.getDefault().getPreferenceStore());
		setDescription("A demonstration of a preference page implementation");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		// GJDK Color Prefs
		addField(new BooleanFieldEditor(
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_GJDK_ENABLED,
				"&Enable GJDK method coloring", getFieldEditorParent()));
		addField(new ColorFieldEditor(
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_GJDK_COLOR,
				"&GJDK method color", getFieldEditorParent()));
		
		// Multiline Comment Color Prefs
		addField(new BooleanFieldEditor(
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_MULTILINECOMMENTS_ENABLED,
				"&Enable multi-line comment coloring", getFieldEditorParent()));
		addField(new ColorFieldEditor(
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_MULTILINECOMMENTS_COLOR,
				"&Multi-line comment color", getFieldEditorParent()));

		// Java Types Comment Color Prefs
		addField(new BooleanFieldEditor(
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_JAVATYPES_ENABLED,
				"&Enable Java types coloring", getFieldEditorParent()));
		addField(new ColorFieldEditor(
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_JAVATYPES_COLOR,
				"&Java types color", getFieldEditorParent()));
		
		// Java Keyword Color Prefs
		addField(new BooleanFieldEditor(
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_JAVAKEYWORDS_ENABLED,
				"&Enable Java keyword coloring", getFieldEditorParent()));
		addField(new ColorFieldEditor(
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_JAVAKEYWORDS_COLOR,
				"&Java Keyeword color", getFieldEditorParent()));
		
		// Groovy Keyword Color Prefs
		addField(new BooleanFieldEditor(
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_GROOVYKEYWORDS_ENABLED,
				"&Groovy keyword coloring", getFieldEditorParent()));
		addField(new ColorFieldEditor(
				PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_GROOVYKEYWORDS_COLOR,
				"&Groovy keyword color", getFieldEditorParent()));
		
		// Generate Class File Pref
		addField(new BooleanFieldEditor(
				PreferenceConstants.GROOVY_GENERATE_CLASS_FILES,
				"&Enable Groovy Compiler Generating Class Files",
				getFieldEditorParent()));
		// Add plugin trace messages to erorr log view
		addField(new BooleanFieldEditor(
				PreferenceConstants.GROOVY_LOG_TRACE_MESSAGES_ENABLED,
				"&Enable Displaying Plugin Trace Messages in Error Log View",
				getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

}
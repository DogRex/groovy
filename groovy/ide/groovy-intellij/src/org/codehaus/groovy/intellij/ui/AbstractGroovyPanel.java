/**
 * License: BSD
 *
 * Copyright (C) 2003 , Travis Kay
 */
package org.codehaus.groovy.intellij.ui;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

import com.intellij.openapi.project.ProjectManager;
import org.codehaus.groovy.intellij.configuration.GroovyConfiguration;
import org.codehaus.groovy.intellij.util.Localizer;

/**
 * @author Travis Kay
 */
public abstract class AbstractGroovyPanel extends JPanel {

    protected GroovyConfiguration getGroovyConfiguration() {

        GroovyConfiguration configuration = (GroovyConfiguration)
                ProjectManager.getInstance().getDefaultProject()
                .getComponent(GroovyConfiguration.class);

        return configuration;

    }

    protected void setBorder(String title) {

        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(3, 3, 3, 3),
                new GradientBorder(title));

        super.setBorder(border);

    }

    protected String getLocalizedString(String key) {
        return Localizer.getLocalizedString(key);
    }

    public abstract void update();

}
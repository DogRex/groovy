/**
 * License: BSD
 *
 * Copyright (C) 2003 , Travis Kay
 */
package org.codehaus.groovy.intellij.actions;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.project.ProjectManager;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;

import org.codehaus.groovy.intellij.configuration.GroovyConfiguration;

/**
 * AbstractGroovyAction provides view and internationalization
 * support for Actions, as well as some helper methods.
 * Subclasses of this Action will only be visible and enabled
 * for files with a '.groovy' extension.
 *
 * @author Travis Kay
 */
public abstract class AbstractGroovyAction extends AnAction {

    public void update(AnActionEvent event) {
        super.update(event);

        VirtualFile vf = getVirtualFile(event);

        if (!vf.getName().endsWith(".groovy")) {
            event.getPresentation().setEnabled(false);
            event.getPresentation().setVisible(false);
        }

    }

    /* Helper method to access a groovy configuration */
    protected GroovyConfiguration getGroovyConfiguration() {

        GroovyConfiguration configuration = (GroovyConfiguration)
                ProjectManager.getInstance().getDefaultProject()
                .getComponent(GroovyConfiguration.class);

        return configuration;

    }

    /* Helper method to get the current groovy classpath */
    protected String getGroovyClassPath() {
        return getGroovyConfiguration().getGroovyClassPath();
    }

    /* Helper method to get the current IDEA VirtualFile */
    protected VirtualFile getVirtualFile(AnActionEvent event) {

        VirtualFile vf = (VirtualFile)
                event.getDataContext().getData(DataConstants.VIRTUAL_FILE);

        return vf;

    }


}
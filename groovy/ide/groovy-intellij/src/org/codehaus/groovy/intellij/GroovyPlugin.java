/**
 * License: BSD
 *
 * Copyright (C) 2003 , Travis Kay
 */
package org.codehaus.groovy.intellij;

import com.intellij.openapi.components.ProjectComponent;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.vfs.*;

import com.intellij.openapi.options.ConfigurationException;

import javax.swing.*;


/**
 * GroovyPlugin for IDEA 3.0 - 3.0.5
 *
 * @author Travis Kay
 */
public class GroovyPlugin implements ProjectComponent {

    public void projectOpened() {
    }

    public void projectClosed() {
    }

    public String getComponentName() {
        return "GroovyPlugin";
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }
}

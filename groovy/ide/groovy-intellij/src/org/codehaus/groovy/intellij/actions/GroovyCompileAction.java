/**
 * License: BSD
 *
 * Copyright (C) 2003 , Travis Kay
 */
package org.codehaus.groovy.intellij.actions;

import java.lang.reflect.Method;

import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectRootManager;

import org.codehaus.groovy.intellij.util.Localizer;

/**
 * AnAction to compile the selected groovy file, either from an
 * active editor or the project / source tree.
 *
 * @author Travis Kay
 */
public class GroovyCompileAction extends AbstractGroovyAction {

    public void actionPerformed(AnActionEvent event) {

        ClassLoader loader = getGroovyConfiguration().getGroovyLoader();

        try {

            Class compilerClass =
                    loader.loadClass("org.codehaus.groovy.tools.FileSystemCompiler");

            Object compiler = compilerClass.newInstance();

            Method cp =
                    compilerClass.getMethod("setClasspath", new Class[]{String.class});

            Method outDir =
                    compilerClass.getMethod("setOutputDir", new Class[]{String.class});

            Method comp =
                    compilerClass.getMethod("compile", new Class[]{String[].class});

            cp.invoke(compiler, new Object[]{getGroovyClassPath()});

            Project project = (Project)
                    event.getDataContext().getData(DataConstants.PROJECT);

            VirtualFile vf =
                    ProjectRootManager.getInstance(project).getOutputPaths()[0];


            outDir.invoke(compiler, new Object[]{vf.getPath()});

            try {

                comp.invoke(compiler,
                        new Object[]{new String[]{getVirtualFile(event).getPath()}});

            } catch (Exception e) {
                Messages.showMessageDialog(e.getMessage(), "Groovy", null);
            }

        } catch (Exception e) {
            Messages.showMessageDialog(
                    Localizer.getLocalizedString("GroovycError"), "Groovy", null);
        }

    }

}
/**
 * License: BSD
 *
 * Copyright (C) 2003 , Travis Kay
 */
package org.codehaus.groovy.intellij.actions;

import java.lang.reflect.Method;
import java.io.StringWriter;
import java.io.PrintWriter;

import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.fileEditor.FileEditorManager;

import org.codehaus.groovy.intellij.util.Localizer;

/**
 * AnAction to evaluate the groovy source selected in the
 * active editor.
 *
 * @author Travis Kay
 */
public class GroovyRunSelectionAction extends AbstractGroovyAction {

    public void update(AnActionEvent event) {
        super.update(event);

        if(!isTextSelected(event)) {
            event.getPresentation().setEnabled(false);
            event.getPresentation().setVisible(false);
        }

    }

    private boolean isTextSelected(AnActionEvent event) {

        String selection = getSelectedText(event);

        boolean selected = true;
        if(selection == null || selection.equals("")) {
            selected = false;
        }

        return selected;

    }

    private String getSelectedText(AnActionEvent event) {

            Project project = (Project)
                    event.getDataContext().getData(DataConstants.PROJECT);

            FileEditorManager fm = FileEditorManager.getInstance(project);

            String selection = fm.getSelectedEditor().getSelectionModel()
                   .getSelectedText();

        return selection;

    }

    public void actionPerformed(AnActionEvent event) {

        try {

            String selection = getSelectedText(event);

                /* class loader with jars from groovy home and project classpath */
                ClassLoader loader = getGroovyConfiguration().getGroovyLoader();

                Thread.currentThread().setContextClassLoader(loader);

                Class clazz = loader.loadClass("groovy.lang.GroovyShell");

                VirtualFile vf = getVirtualFile(event);

                Object shell = clazz.newInstance();

                Method m =
                        clazz.getMethod("evaluate", new Class[]{String.class, String.class});

                Object result = m.invoke(shell, new Object[]{selection, vf.getPath()});

                if (result != null) {
                    Messages.showMessageDialog(
                            result.toString(), "Groovy", null);
                } else {
                    Messages.showMessageDialog(
                            Localizer.getLocalizedString("GroovyEvaluationNoSelctionError"),
                            "Groovy", null);
                }


        } catch (Exception e) {

            String message = e.getMessage();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            Messages.showMessageDialog(
                    Localizer.getLocalizedString("GroovyRunActionError") + message + e.getClass().getName() +
                    sw.toString(), "Groovy", null);

        }

    }

}
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

import org.codehaus.groovy.intellij.util.Localizer;

/**
 * AnAction to run the selected groovy file, either from an
 * active editor or the project / source tree.
 *
 * @author Travis Kay
 */
public class GroovyRunAction extends AbstractGroovyAction {

    public void actionPerformed(AnActionEvent event) {

        try {

            /* class loader with jars from groovy home and project classpath */
            ClassLoader loader = getGroovyConfiguration().getGroovyLoader();

            Thread.currentThread().setContextClassLoader(loader);

            Class clazz = loader.loadClass("groovy.lang.GroovyShell");

            VirtualFile vf = getVirtualFile(event);

            Object groovyShell = clazz.newInstance();

            Method m = clazz.getMethod("run", new Class[]{String.class, String[].class});

            m.invoke(groovyShell, new Object[]{vf.getPath(),
                        new String[]{getGroovyConfiguration().getGroovycOptions()}});

        } catch (Exception e) {

            String message = e.getMessage();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            Messages.showMessageDialog(
                    Localizer.getLocalizedString("GroovyRunActionError") + message + e.getClass().getName() + sw.toString(), "Groovy", null);
        }

    }

}
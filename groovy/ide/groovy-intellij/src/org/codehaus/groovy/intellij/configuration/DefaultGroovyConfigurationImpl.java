/**
 * License: BSD
 *
 * Copyright (C) 2003 , Travis Kay
 */
package org.codehaus.groovy.intellij.configuration;

import java.util.*;

import java.io.File;
import java.io.FileFilter;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;

import javax.swing.*;

import org.jdom.Element;

import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.options.ConfigurationException;

import com.intellij.openapi.vfs.VirtualFile;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.projectRoots.ProjectRootManager;

import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;

import org.codehaus.groovy.intellij.util.Localizer;
import org.codehaus.groovy.intellij.ui.ConfigurationPanel;
import org.codehaus.groovy.intellij.configuration.GroovyConfiguration;

/**
 * @author Travis Kay
 */
public class DefaultGroovyConfigurationImpl implements ProjectComponent, GroovyConfiguration, Configurable,
        JDOMExternalizable {

    private ConfigurationPanel configurationPanel;

    private boolean isModified;
    private Map configurationProperties = new HashMap();

    private final static String GROOVY_HOME = "groovy-home";
    private final static String GROOVYC = "groovyc-options";
    private final static String GROOVY_CP = "groovy-classpath";

    private ClassLoader groovyLoader;

    public DefaultGroovyConfigurationImpl() {

        configurationProperties.put(GROOVY_HOME, "");
        configurationProperties.put(GROOVYC, "");
        configurationProperties.put(GROOVY_CP, "");

    }

    public String getDisplayName() {
        return "Groovy";
    }

    public Icon getIcon() {
        return null;
    }

    public String getHelpTopic() {
        return "Groovy Help";
    }

    public JComponent createComponent() {

        if (configurationPanel == null) {
            configurationPanel = new ConfigurationPanel();
        }

        return configurationPanel;

    }

    public boolean isModified() {
        return isModified;
    }

    public void apply() throws ConfigurationException {

        configurationProperties.put(GROOVY_HOME, getGroovyHome());
        configurationProperties.put(GROOVYC, getGroovycOptions());
        configurationProperties.put(GROOVY_CP, getGroovyClassPath());

    }

    public void reset() {
        configurationPanel.update();
    }

    public void disposeUIResources() {
        configurationPanel = null;
    }

    /* Read in Groovy configuration data. */
    public void readExternal(Element element) throws InvalidDataException {

        List entries = element.getChildren("map_entry");

        for (int i = 0; i < entries.size(); i++) {

            Element entry = (Element) entries.get(i);

            String name = entry.getAttribute("name").getValue();
            String value = entry.getAttribute("value").getValue();

            configurationProperties.put(name, value);

        }

    }

    /* Write out Groovy configuration data. */
    public void writeExternal(Element element) throws WriteExternalException {

        Iterator keyIterator = configurationProperties.keySet().iterator();

        while (keyIterator.hasNext()) {

            String key = (String) keyIterator.next();

            Element entryElement = new Element("map_entry");
            entryElement = entryElement.setAttribute("name", key);

            entryElement =
                    entryElement.setAttribute("value",
                            configurationProperties.get(key).toString());

            element.addContent(entryElement);

        }

    }

    public String getGroovyHome() {
        return (String) configurationProperties.get(GROOVY_HOME);
    }

    public void setGroovyHome(String groovyHome) {
        configurationProperties.put(GROOVY_HOME, groovyHome);
        isModified = true;
    }

    public String getGroovyClassPath() {

        Project project = ProjectManager.getInstance().getDefaultProject();

        VirtualFile[] f = ProjectRootManager.getInstance(project).getFullClassPath();

        StringBuffer b = new StringBuffer();

        for (int i = 0; i < f.length; i++) {
            VirtualFile virtualFile = f[i];
            b.append(virtualFile.getPath() + System.getProperty("path.separator"));

        }

        String cp = b.toString().trim();

        return cp;

    }

    public void setGroovyClassPath(String classpath) {
        configurationProperties.put(GROOVY_CP, classpath);
        isModified = true;
    }

    public ClassLoader getGroovyLoader() {

        if(groovyLoader == null) {

           File groovyHome = new File(getGroovyHome() +
                   System.getProperty("file.separator") + "lib");

            if (!groovyHome.exists()) {
                Messages.showMessageDialog(
                        Localizer.getLocalizedString("GroovyHomeNotFound"),
                        "Groovy", null);
            }

            groovyLoader = new URLClassLoader(getURLPaths(groovyHome));

        }

        return groovyLoader;

    }

    private URL[] getURLPaths(File groovyHome) {

        /* Get all groovy jars */
        File[] paths = groovyHome.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                boolean accepted = false;
                if (pathname.getName().endsWith(".jar")) {
                    accepted = true;
                }
                return accepted;
            }

        });

        URL[] urls = new URL[paths.length];

        for (int i = 0; i < paths.length; i++) {

            try {
                urls[i] = paths[i].toURL();
            } catch (MalformedURLException e) {

                Messages.showMessageDialog(
                        Localizer.getLocalizedString("GroovyClassPathError"),
                        "Groovy", null);

            }

        }

        return urls;

    }

    public String getGroovycOptions() {
        return (String) configurationProperties.get(GROOVYC);
    }

    public void setGroovycOptions(String options) {
        configurationProperties.put(GROOVYC, options);
        isModified = true;
    }

    public void projectOpened() {
    }

    public void projectClosed() {
    }

    public String getComponentName() {
        return "GroovyConfiguration";
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

}
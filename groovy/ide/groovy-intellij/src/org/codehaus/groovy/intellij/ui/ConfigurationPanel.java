/**
 * License: BSD
 *
 * Copyright (C) 2003 , Travis Kay
 */
package org.codehaus.groovy.intellij.ui;

import java.awt.GridLayout;

/**
 * @author Travis Kay
 */
public class ConfigurationPanel extends AbstractGroovyPanel {

    private GroovyHomePanel groovyHomePanel;
    private GroovycOptionsPanel groovycOptionsPanel;

    public ConfigurationPanel() {

        createComponents();
        this.setLayout(new GridLayout(2, 1));

    }

    private void createComponents() {

        groovyHomePanel = new GroovyHomePanel();
        groovycOptionsPanel = new GroovycOptionsPanel();

        this.add(groovyHomePanel);
        this.add(groovycOptionsPanel);

    }

    public void update(){
        groovyHomePanel.update();
        groovycOptionsPanel.update();
    }

    public void reset() {
        groovyHomePanel.reset();
        groovycOptionsPanel.reset();
    }

}
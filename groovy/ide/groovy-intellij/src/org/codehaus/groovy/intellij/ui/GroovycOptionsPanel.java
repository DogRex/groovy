/**
 * License: BSD
 *
 * Copyright (C) 2003 , Travis Kay
 */
package org.codehaus.groovy.intellij.ui;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Travis Kay
 */
public class GroovycOptionsPanel extends AbstractGroovyPanel {

    private JCheckBox strictBox;

    public GroovycOptionsPanel() {

        createComponenets();
        setBorder(getLocalizedString("GroovycOptionsPanel.BorderTitle"));

        strictBox.setEnabled(false);

    }

    public void createComponenets() {

        strictBox = new JCheckBox(getLocalizedString("GroovycOptionsPanel.strict"));
        strictBox.addChangeListener(new Listener());
        this.add(strictBox);

    }

    public void update() {

        if(getGroovyConfiguration().getGroovycOptions().equals("--strict")){
            strictBox.setSelected(true);
        }else {
            strictBox.setSelected(false);
        }

    }

    public void reset() {
        strictBox.setSelected(false);
    }

    class Listener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            boolean strictTypeSafety = strictBox.isSelected();
            if(strictTypeSafety) {
                getGroovyConfiguration().setGroovycOptions("--strict");
            }
        }

    }

}
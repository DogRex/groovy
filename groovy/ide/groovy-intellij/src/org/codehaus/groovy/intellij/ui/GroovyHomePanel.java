/**
 * License: BSD
 *
 * Copyright (C) 2003 , Travis Kay
 */
package org.codehaus.groovy.intellij.ui;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Travis Kay
 */
public class GroovyHomePanel extends AbstractGroovyPanel {

    private JTextField homeTextField;
    private JButton homeButton;

    public GroovyHomePanel() {

        createComponents();
        setBorder(getLocalizedString("GroovyHomePanel.BroderTitle"));

    }

    private void createComponents() {

        this.homeTextField = new JTextField(20);
        this.add(homeTextField);

        this.homeButton = new JButton("...");
        this.add(homeButton);

        this.homeButton.addActionListener(new Listener());

    }

    public void update() {
        homeTextField.setText(getGroovyConfiguration().getGroovyHome());
    }

    public void reset() {
        homeTextField.setText("");
    }

    private class Listener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            JFileChooser fc = new JFileChooser();

            fc.setDialogType(JFileChooser.OPEN_DIALOG);
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            fc.setVisible(true);

            int code = fc.showOpenDialog(GroovyHomePanel.this);

            if (code == JFileChooser.APPROVE_OPTION) {
                homeTextField.setText(fc.getSelectedFile().getPath());
                getGroovyConfiguration().setGroovyHome(fc.getSelectedFile().getPath());
            }

        }

    }

}
package org.codehaus.groovy.intellij.ui;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.font.FontRenderContext;

import javax.swing.border.Border;

/**
 * This class has been taken from the HibernateTools plugin.
 *
 * http://www.intellij.org/twiki/bin/view/Main/HibernateTools
 */
public class GradientBorder implements Border {

    private String title = "Title";
    private Font titleFont = new Font("verdana", Font.BOLD, 12);
    private float ascent = -1;
    private int headerHight = 20;
    private Insets insets = new Insets(headerHight + 5, 5, 5, 5);

    public GradientBorder() {
    }

    public GradientBorder(String title) {
        this.title = title;
    }

    // Implemented methods //////////////////////////////////////////////////////////////////////////////////////////////

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        width--;
        height--;

        Graphics2D g2d = (Graphics2D) g;

        Rectangle rect2 = new Rectangle(x, y, width, headerHight);
        Rectangle rect3 = new Rectangle(x, y, width, height);

        g2d.setPaint(new GradientPaint(x, y, new Color(97, 123, 182), width, y, Color.white));
        g2d.fill(rect2);
        g2d.draw(rect3);

        if (title != null) {
            if (ascent == -1) {
                FontRenderContext frc = g2d.getFontRenderContext();
                TextLayout tl = new TextLayout(title, titleFont, frc);
                ascent = tl.getAscent();
            }
            g2d.setPaint(Color.white);
            g2d.setFont(titleFont);
            g2d.drawString(title, x + insets.left, y + headerHight / 2 + ascent / 4);
        }
    }

    public Insets getBorderInsets(Component c) {
        return insets;
    }

    public boolean isBorderOpaque() {
        return false;
    }

    // End Implemented methods //////////////////////////////////////////////////////////////////////////////////////////

    // Accessors ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Font getTitleFont() {
        return titleFont;
    }

    public void setTitleFont(Font titleFont) {
        this.titleFont = titleFont;
        this.ascent = -1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.ascent = -1;
    }

    // End Accessors ////////////////////////////////////////////////////////////////////////////////////////////////////
}
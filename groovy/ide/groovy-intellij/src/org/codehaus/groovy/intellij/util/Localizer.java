/**
 * License: BSD
 *
 * Copyright (C) 2003 , Travis Kay
 */
package org.codehaus.groovy.intellij.util;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Travis Kay
 */
public class Localizer {

    private static Properties p = new Properties();

    static {

        ClassLoader l = Localizer.class.getClassLoader();

        try {
            p.load(l.getResourceAsStream("resources/groovy-localization.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getLocalizedString(String key) {

        return (String) p.get(key);

    }

}
/**
 * License: BSD
 *
 * Copyright (C) 2003 , Travis Kay
 */
package org.codehaus.groovy.intellij.configuration;

import com.intellij.openapi.options.Configurable;

/**
 * GroovyConfiguratoin is a sub interface of IDEA
 * Configurable, and  privdes access to Groovy
 * specific configuration data.
 *
 * @author Travis Kay
 */
public interface GroovyConfiguration  extends Configurable {

    /**
     * GroovyHome is you groovy install directory
     *
     * @return groovyHome
     */
    public String getGroovyHome();

    public void setGroovyHome(String groovyHome);

    /**
     * GroovyClassPath includes all the jar files in GroovyHome/lib,
     * as well as IDEA's project libraries and output directory.
     * Each classpath entry is delaminated by your environments path
     * separator.
     *
     * ';' - windows , ':' - unix
     *
     * @return groovyClassPath
     */
    public String getGroovyClassPath();

    public void setGroovyClassPath(String classpath);

    /**
     * Groovy Compiler Options
     *
     * @return groovycOptions
     */
    public String getGroovycOptions();

    public void setGroovycOptions(String options);

    /**
     * Gets a ClassLoader that inlcudes all needed classpaths.
     *
     * @return groovyLoader
     */
    public ClassLoader getGroovyLoader();

}
package org.codehaus.groovy.intellij;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.DefaultLogger;

public class GroovyJPlugin implements ApplicationComponent
{
	private static GroovyJPlugin instance;

	private DefaultLogger log = new DefaultLogger("GroovyJ");

    public String getComponentName() {
        return "groovyj.application.plugin";
    }

    public void initComponent() {
        instance = this;
		log.info("GroovyJPlugin.initComponent()");
    }

    public void disposeComponent() {
        
    }

	public static GroovyJPlugin getInstance()
	{
		return instance;
	}
}

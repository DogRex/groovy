package org.codehaus.groovy.intellij;

import com.intellij.openapi.components.ProjectComponent;

/**
 * <p>Création: 14 août 2004</p>
 *
 * @author Guillaume Laforge
 * @cvs.revision $Revision$
 * @cvs.tag $Name$
 * @cvs.author $Author$
 * @cvs.date $Date$
 * @since Release x.x.x
 */
public class GroovyJProjectComponent implements ProjectComponent
{
	private static GroovyJProjectComponent instance;

	public void projectOpened()
	{
	}

	public void projectClosed()
	{
	}

	public String getComponentName()
	{
		return "groovyj.project.plugin";
	}

	public void initComponent()
	{
		instance = this;
	}

	public void disposeComponent()
	{
	}

	public static GroovyJProjectComponent getInstance()
	{
		return instance;
	}
}

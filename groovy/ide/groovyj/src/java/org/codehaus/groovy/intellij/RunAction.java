package org.codehaus.groovy.intellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.io.IOException;

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
public class RunAction extends AnAction
{
	private Logger LOG = Logger.getInstance("GroovyJ.RunAction");

	public void actionPerformed(AnActionEvent e)
	{
		final Project project = (Project) e.getDataContext().getData(DataConstants.PROJECT);
		final StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
		VirtualFile[] selectedFiles = FileEditorManager.getInstance(project).getSelectedFiles();
		if (selectedFiles.length > 0)
		{
			final VirtualFile selectedFile = selectedFiles[0];
			if (!selectedFile.isDirectory())
			{
				File[] classpathForVFile = IdeaToolbox.getClasspathForVFile(project, selectedFile);
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < classpathForVFile.length; i++)
				{
					File file = classpathForVFile[i];
					sb.append(file.getPath() + File.pathSeparatorChar);
				}
				final String classpath = sb.toString();
				LOG.info("Classpath: " + classpath);

				ApplicationManager.getApplication().runReadAction(new Runnable()
				{
					public void run()
					{
						statusBar.setInfo("Running " + selectedFile.getName() + "...");

						CompilerConfiguration configuration = new CompilerConfiguration();
						configuration.setSourceEncoding(selectedFile.getCharset().name());
						configuration.setTargetDirectory(selectedFile.getPath());
/*
						configuration.setOutput();
*/
						configuration.setClasspath(classpath);

						ClassLoader loader = GroovyJPlugin.getInstance().getClass().getClassLoader();
						GroovyShell shell = new GroovyShell(loader, new Binding(), configuration);
						try
						{
							shell.run(selectedFile.getInputStream(), selectedFile.getName(), new String[]{});
						}
						catch (CompilationFailedException e1)
						{
							LOG.error("Run failed: " + e1.getMessage());
						}
						catch (IOException e1)
						{
							LOG.error("Run failed: " + e1.getMessage());
						}
					}
				});

			}
		}
	}
}

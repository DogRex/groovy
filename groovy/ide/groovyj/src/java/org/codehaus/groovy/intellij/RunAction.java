package org.codehaus.groovy.intellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import groovy.lang.GroovyShell;
import groovy.lang.Binding;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.CompilationFailedException;

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
	public void actionPerformed(AnActionEvent e)
	{
		Project project = (Project) e.getDataContext().getData(DataConstants.PROJECT);
		final StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
		VirtualFile[] selectedFiles = FileEditorManager.getInstance(project).getSelectedFiles();
		if (selectedFiles.length > 0)
		{
			final VirtualFile selectedFile = selectedFiles[0];
			if (!selectedFile.isDirectory())
			{
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
						configuration.setClasspath();
*/
						ClassLoader loader = GroovyJPlugin.getInstance().getClass().getClassLoader();
						GroovyShell shell = new GroovyShell(loader, new Binding(), configuration);
						try
						{
							shell.run(selectedFile.getInputStream(), selectedFile.getName(), new String[]{});
						}
						catch (CompilationFailedException e1)
						{
							statusBar.setInfo("Run failed: " + e1.getMessage());
						}
						catch (IOException e1)
						{
							statusBar.setInfo("Run failed: " + e1.getMessage());
						}
					}
				});

			}
		}
	}
}

package org.codehaus.groovy.intellij;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.diagnostic.Logger;

import java.util.ArrayList;
import java.util.zip.ZipFile;
import java.io.File;
import java.io.IOException;

/**
 * <p>Création: 19 août 2004</p>
 *
 * @author Guillaume Laforge
 * @cvs.revision $Revision$
 * @cvs.tag $Name$
 * @cvs.author $Author$
 * @cvs.date $Date$
 * @since Release x.x.x
 */
public class IdeaToolbox
{
	private static Logger LOG = Logger.getInstance("GroovyJ.IdeaToolbox");

	public static File[] getClasspathForVFile(final Project project, final VirtualFile vFile)
	{
		final ModuleManager mgr = ModuleManager.getInstance(project);
		final Module[] modules = mgr.getModules();
		final Module module = modules[0];

		final FileTypeManager ftmgr = FileTypeManager.getInstance();
		final ModuleRootManager mrm = ModuleRootManager.getInstance(module);
		final VirtualFile[] files = mrm.getFiles(OrderRootType.CLASSES);
		final ArrayList paths = new ArrayList(files.length);

		for (int i = 0; i < files.length; i++)
		{
			final VirtualFile virtualFile = files[i];
			final FileType fileType = ftmgr.getFileTypeByFile(virtualFile);

			if (!virtualFile.isValid())
				continue;

			if (!virtualFile.isDirectory() && !fileType.equals(FileType.ARCHIVE))
				continue;

			final File file;

			final VirtualFileSystem fileSystem = virtualFile.getFileSystem();
			if (fileSystem instanceof LocalFileSystem)
				file = new File(virtualFile.getPath().replace('/', File.separatorChar));
			else if (fileSystem instanceof JarFileSystem)
			{
				final JarFileSystem jarFileSystem = (JarFileSystem) fileSystem;
				try
				{
					final ZipFile jarFile = jarFileSystem.getJarFile(virtualFile);
					file = new File(jarFile.getName());
				}
				catch (IOException e)
				{
					LOG.error("Error getting jar vFile: " + e);
					continue;
				}
			}
			else
				continue;

			paths.add(file);
		}
		return (File[]) paths.toArray(new File[paths.size()]);
	}
}

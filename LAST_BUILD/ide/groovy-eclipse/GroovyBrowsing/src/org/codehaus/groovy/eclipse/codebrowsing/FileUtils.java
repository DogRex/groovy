package org.codehaus.groovy.eclipse.codebrowsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

public class FileUtils {
	/**
	 * Given a qualified class name, find the file in the classpath that
	 * implements this class.
	 * 
	 * @param className
	 *            The qualified class name.
	 * @param extensions
	 *            What file extensions to search for.
	 * @return The file or null if no file was found.
	 */
	public static IFile findFileInProject(String classname, String[] extensions) {
		// FIXME: this temporarily accesses the workspace files, not the class
		// path files.
		String filename = classname.replace('.', '/');
		IFile[] files = findFilesInWorkspace(filename, extensions);
		if (files.length > 0) {
			return files[0];
		}
		return null;
	}

	/**
	 * Given a partial path to a file, find all files matching this file with
	 * the given extension. The path separator is '/'.
	 * 
	 * @param fileName
	 * @param extensions
	 * @return
	 */
	public static IFile[] findFilesInWorkspace(String fileName,
			String[] extensions) {
		final List ret = new ArrayList();
		final String[] files = new String[extensions.length];
		for (int i = 0; i < files.length; ++i) {
			files[i] = fileName + "." + extensions[i];
		}

		try {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			root.accept(new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					if (resource.getType() == IResource.FILE) {
						String path = resource.getFullPath().toString();
						for (int i = 0; i < files.length; ++i) {
							if (path.endsWith(files[i])) {
								ret.add(resource);
							}
						}
					}
					return true;
				}
			});
		} catch (CoreException e) {
			GroovyPlugin.getPlugin().logException("Should not happen.", e);
			e.printStackTrace();
		}

		return (IFile[]) ret.toArray(new IFile[ret.size()]);
	}

	// FIXME: implement - just takes the last identfier for now.
	public static int findOffsetOfIdentifiers(BufferedReader reader,
			String[] identifiers) {
		String line = null;
		String identifier = identifiers[identifiers.length - 1]; // FIXME
		try {
			try {
				int offset = 0;
				int delimLength = System.getProperty("line.separator").length();
				while ((line = reader.readLine()) != null) {
					int ix = TextUtils.findIdentifierOffset(line, identifier);
					if (ix != -1) {
						offset += ix;
						return offset;
					}
					offset += line.length() + delimLength;
				}
			} finally {
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static int findOffsetOfIdentifier(BufferedReader reader,
			String identifier) {
		return findOffsetOfIdentifiers(reader, new String[] { identifier });
	}

	public static int findOffsetOfIdentifiers(IFile file, String[] identifiers) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					file.getContents()));
			return findOffsetOfIdentifiers(reader, identifiers);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static int findOffsetOfIdentifier(IFile file, String identifier) {
		return findOffsetOfIdentifiers(file, new String[] { identifier });
	}
}
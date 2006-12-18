package org.codehaus.groovy.eclipse.codebrowsing;

import org.codehaus.groovy.ast.ClassNode;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.Region;

/**
 * Utility class to find source code for different objects.
 * 
 * @author emp
 */
public class SourceCodeFinder {
	public static ISourceCode find(ClassNode type) {
		String filename = type.getName().replace('.', '/');

		IFile file = FileUtils.findFileInProject(filename, new String[] {
				"groovy", "java" });

		if (file != null) {
			String ident = type.getNameWithoutPackage();
			int offset = FileUtils.findOffsetOfIdentifiers(file, new String[] {
					"class", ident });
			if (offset != -1) {
				return new FileSourceCode(file, new Region(offset, ident
						.length()));
			}
		}

		return null;
	}
}

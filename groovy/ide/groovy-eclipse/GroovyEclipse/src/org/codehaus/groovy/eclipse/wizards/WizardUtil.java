/*
 * Created on 27-Jan-2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.codehaus.groovy.eclipse.wizards;

import java.io.InputStream;
import java.io.StringBufferInputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;

/**
 * @author MelamedZ
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class WizardUtil {
	public static IFile createGroovyType(IPackageFragmentRoot root, IPackageFragment pack,String cuName, String source) throws CoreException {

        checkPackageExists(root, pack);
        
		StringBuffer buf = new StringBuffer();
		if(pack.getElementName().length()>0){
			buf.append("package " + pack.getElementName() + ";\n");
		}
		buf.append("\n\n\n");
		buf.append(source);

		IContainer folder = (IContainer) pack.getResource();
		InputStream stream;
		stream = new StringBufferInputStream(buf.toString());
		return createFile(folder, cuName, stream);
	}

	public  static String getSuperName(IPackageFragment pack, String superClass) throws JavaModelException {
		if(superClass != null && superClass.length()>0 && !superClass.equals("java.lang.Object")){
			IType type = JavaModelUtil.findType(pack.getJavaProject(),superClass);
			if(type != null){
				return type.getElementName();
			}
		}
		
		return "";
	}

	private static IFile createFile(IContainer folder, String name, InputStream contents) throws JavaModelException {
		IFile file = folder.getFile(new Path(name));
		try {
			file.create(contents, IResource.FORCE, null);
		} catch (CoreException e) {
			throw new JavaModelException(e);
		}

		return file;
	}

    /**
     * Checks that the specified package fragment exists, and if it doesn't creates it.
     * 
     * @param root source folder
     * @param pack package
     * @throws JavaModelException if an error occurs
     */
    private static void checkPackageExists(IPackageFragmentRoot root, IPackageFragment pack) 
        throws JavaModelException {

        if (pack == null) {
            pack = root.getPackageFragment("");  // default package
        }
        
        if (!pack.exists()) {
            final String packName = pack.getElementName();
            pack = root.createPackageFragment(packName, true, null);
        }       
    }
}

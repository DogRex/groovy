/*
 * Created on Jan 27, 2004
 *  
 */
package org.codehaus.groovy.eclipse.editor.contentoutline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GroovyClassVisitor;
import org.codehaus.groovy.ast.ImportNode;
import org.codehaus.groovy.ast.MetadataNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.PropertyNode;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.swt.graphics.Image;

/**
 * @author zohar melamed
 *  
 */
public abstract class TreeAdapter {
	protected static Object[] empty = new Object[0];
	protected Object[] children = empty;
	protected Object parent;
	protected String name;
	protected int lineNumber;

	Object[] getChildren() {
		return children;
	}
	Object getParent() {
		return parent;
	}
	boolean hasChildren() {
		return children.length > 0;
	}
	abstract Image getImage();
	String getText() {
		return name;
	}
	/**
	 * 
	 */
	int getLineNumber() {
		return lineNumber;
	}
}

class PackageAdapter extends TreeAdapter {
	public PackageAdapter(String packageName, Object parent) {
		this.parent = parent;
		name = packageName==null?"(default-package)":packageName;
		lineNumber = 1;
	}
	Image getImage() {
		return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_PACKAGE);
	}
}

class PropertyAdapter extends TreeAdapter {
	PropertyAdapter(PropertyNode property, Object parent) {
		this.parent = parent;
		name = property.getName();
		children = new Object[1];
		children[0] = new FieldAdapter(property.getField(), this);
		lineNumber = property.getLineNumber();
	}

	Image getImage() {
		return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_UNKNOWN);
	}
}

class FieldAdapter extends TreeAdapter {
	/**
	 * @param node
	 * @param adapter
	 */
	public FieldAdapter(FieldNode node, Object parent) {
		name = node.getName();
		this.parent = parent;
		lineNumber = node.getLineNumber();
	}

	Image getImage() {
		return JavaPluginImages.get(JavaPluginImages.IMG_FIELD_DEFAULT);
	}
}

class MethodAdapter extends TreeAdapter {
	MethodAdapter(MethodNode methodNode, Object parent) {
		this.parent = parent;
		name = methodNode.getName();
		lineNumber = methodNode.getLineNumber();
	}

	Image getImage() {
		return JavaPluginImages.get(JavaPluginImages.IMG_MISC_DEFAULT);
	}
}

class ConstructorAdapter extends TreeAdapter {
	ConstructorAdapter(ClassNode classNode, ConstructorNode ctorNode, Object parent) {
		this.parent = parent;
		name = classNode.getNameWithoutPackage();
		lineNumber = ctorNode.getLineNumber();
	}

	Image getImage() {
		return JavaPluginImages.get(JavaPluginImages.IMG_MISC_DEFAULT);
	}
}



class ImportContainer extends TreeAdapter {
	ImportContainer(ModuleNode moduleNode) {
		name = "import declerations";
		List imports = moduleNode.getImports();
		if (imports.size() > 0) {
			children = new Object[imports.size()];
			int i  = 0;
			for (Iterator iter = imports.iterator(); iter.hasNext();) {
				ImportNode importNode = (ImportNode) iter.next();
				children[i++]= new ImportAdapter(importNode,this);
			}
		}
	}

	Image getImage() {
		return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_IMPCONT);
	}
}

class ImportAdapter extends TreeAdapter {
	ImportAdapter(ImportNode importNode, Object parent) {
		this.parent = parent;
		name = importNode.getClassName();
		lineNumber = importNode.getLineNumber();
	}

	Image getImage() {
		return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_IMPDECL);
	}
}

class ClassAdapter extends TreeAdapter {
	ClassAdapter(ClassNode classNode) {
		name = classNode.getNameWithoutPackage();
		createChildAdapters(classNode);
		lineNumber = classNode.getLineNumber();
	}

	private void createChildAdapters(final ClassNode classNode) {
		final List childrrenList = new ArrayList();
		classNode.visitContents(new GroovyClassVisitor() {
			public void visitClass(ClassNode node) {
			}
			public void visitConstructor(ConstructorNode node) {
				add(new ConstructorAdapter(classNode, node, ClassAdapter.this), node);
			}
			public void visitMethod(MethodNode node) {
				add(new MethodAdapter(node, ClassAdapter.this), node);
			}
			public void visitField(FieldNode node) {
			}
			public void visitProperty(PropertyNode node) {
				add(new PropertyAdapter(node, ClassAdapter.this), node);
			}

			private void add(TreeAdapter adapter, MetadataNode node) {
				if (!node.isSynthetic())
					childrrenList.add(adapter);

			}
		});

		children = childrrenList.toArray();
	}

	Image getImage() {
		return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_CLASS);
	}
}

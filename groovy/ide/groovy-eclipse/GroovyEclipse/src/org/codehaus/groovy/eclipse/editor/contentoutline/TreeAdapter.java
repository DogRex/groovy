/*
 * Created on Jan 27, 2004
 *  
 */
package org.codehaus.groovy.eclipse.editor.contentoutline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GroovyClassVisitor;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.PropertyNode;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.swt.graphics.Image;

/**
 * @author zohar melamed
 *  
 */
public abstract class TreeAdapter {
	private static Map adapters;
	static {
		adapters = new HashMap();
		adapters.put(String.class, new PackageAdapter());
		adapters.put(MethodNode.class, new MethodAdapter());
		adapters.put(ClassNode.class, new ClassAdapter());
		adapters.put(FieldNode.class, new FieldAdapter());
		adapters.put(PropertyNode.class, new PropertyAdapter());
	}
	abstract Object[] getChildren(Object parentElement);
	abstract Object getParent(Object element);
	abstract boolean hasChildren(Object element);
	abstract Image getImage(Object element);
	abstract String getText(Object element);
	static TreeAdapter adapter(Class key) {
		return (TreeAdapter) adapters.get(key);
	}
}

class PackageAdapter extends TreeAdapter {
	Object[] getChildren(Object parentElement) {
		return null;
	}

	Object getParent(Object element) {
		return null;
	}
	boolean hasChildren(Object element) {
		return false;
	}

	Image getImage(Object element) {
		return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_PACKAGE);
	}

	String getText(Object element) {
		return (String) element;
	}
}

class PropertyAdapter extends TreeAdapter{
	Object[] getChildren(Object parentElement) {
		return null;
	}

	Image getImage(Object element) {
		return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_UNKNOWN);
	}

	Object getParent(Object element) {
		//TODO find a way to do this 
		return null;
	}

	String getText(Object element) {
		PropertyNode propertyNode = (PropertyNode) element; 
		return propertyNode.getName();
	}

	boolean hasChildren(Object element) {
		return false;
	}

}
class FieldAdapter extends TreeAdapter{
	Object[] getChildren(Object parentElement) {
		return null;
	}

	Image getImage(Object element) {
		return JavaPluginImages.get(JavaPluginImages.IMG_FIELD_DEFAULT);
	}

	Object getParent(Object element) {
		//TODO find a way to do this
		return null;
	}

	String getText(Object element) {
		FieldNode fieldNode = (FieldNode) element;
		return fieldNode.getName();
	}

	boolean hasChildren(Object element) {
		return false;
	}

}

class MethodAdapter extends TreeAdapter {
	Object[] getChildren(Object parentElement) {
		return null;
	}

	Image getImage(Object element) {
		return JavaPluginImages.get(JavaPluginImages.IMG_MISC_DEFAULT);
	}

	Object getParent(Object element) {
		// TODO find a way to get the enclosing class
		return null;
	}

	String getText(Object element) {
		MethodNode methodNode = (MethodNode) element;
		return methodNode.getName();
	}

	boolean hasChildren(Object element) {
		return false;
	}

}
class ClassAdapter extends TreeAdapter {
	Object[] getChildren(Object parentElement) {
		ClassNode classNode = (ClassNode) parentElement;
		final List children = new ArrayList();
		classNode.visitContents(new GroovyClassVisitor() {
			public void visitClass(ClassNode node) {
			}
			public void visitConstructor(ConstructorNode node) {
				children.add(node);
			}
			public void visitMethod(MethodNode node) {
				children.add(node);
			}
			public void visitField(FieldNode node) {
				children.add(node);
			}
			public void visitProperty(PropertyNode node) {
				children.add(node);
			}
		});

		return children.toArray();
	}

	Object getParent(Object element) {
		ClassNode classNode = (ClassNode) element;
		return classNode.getOuterClass();
	}

	boolean hasChildren(Object element) {
		ClassNode classNode = (ClassNode) element;
		boolean children =
			(classNode.getMethods().size() > 0)
				|| (classNode.getFields().size() > 0)
				|| (classNode.getConstructors().size() > 0)
				|| (classNode.getProperties().size() > 0);
		return children;
	}

	Image getImage(Object element) {
		return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_CLASS);
	}

	String getText(Object element) {
		ClassNode classNode = (ClassNode) element;
		return classNode.getNameWithoutPackage();
	}

}

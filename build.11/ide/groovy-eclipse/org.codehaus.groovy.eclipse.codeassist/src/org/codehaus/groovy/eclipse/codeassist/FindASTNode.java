package org.codehaus.groovy.eclipse.codeassist;

import java.util.ArrayList;
import java.util.Iterator;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.eclipse.editor.actions.EditorPartFacade;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

public class FindASTNode {
	public static ASTNode enclosedBy(Class astNodeClass, ITextViewer viewer, int offset) {
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (part == null) {
			return null;
		}

		EditorPartFacade facade = new EditorPartFacade(part);

		ArrayList moduleNodes = (ArrayList) GroovyModel.getModel().getModuleNodes(facade.getFile());
		if (moduleNodes.size() == 0) {
			return null;
		}
		
//		 TODO: is there always 1? Can it be null.
		ModuleNode node = (ModuleNode) moduleNodes.get(0);

		try {
			Point rowcol = facade.getRowCol(offset);

			if (astNodeClass.equals(ModuleNode.class)) {
				return node;
			} else if (astNodeClass.equals(ClassNode.class)) {
				return findEnclosingClass(node, rowcol.x, rowcol.y);
			} else if (astNodeClass.equals(MethodNode.class)) {
				return findEnclosingMethod(node, rowcol.x, rowcol.y);
			}
		} catch (BadLocationException e) {
			// LOG
			e.printStackTrace();
			return null;
		}
		return null;
	}

	private static ASTNode findEnclosingClass(ModuleNode moduleNode, int x, int y) {
		for (Iterator iter = moduleNode.getClasses().iterator(); iter.hasNext();) {
			ClassNode classNode = (ClassNode) iter.next();
			if (isAnEnclosingASTNode(classNode, x, y)) {
				return classNode;
			}
		}
		return null;
	}

	private static ASTNode findEnclosingMethod(ModuleNode moduleNode, int x, int y) {
		for (Iterator methodIter = moduleNode.getMethods().iterator(); methodIter.hasNext(); ) {
			MethodNode methodNode = (MethodNode) methodIter.next();
			if (isAnEnclosingASTNode(methodNode, x, y)) {
				return methodNode;
			}
		}
		
		ClassNode classNode = (ClassNode) findEnclosingClass(moduleNode, x, y);
		if (classNode != null) {
			for (Iterator methodIter = classNode.getMethods().iterator(); methodIter.hasNext(); ) {
				MethodNode methodNode = (MethodNode) methodIter.next();
				if (isAnEnclosingASTNode(methodNode, x, y)) {
					return methodNode;
				}
			}
		}

		return null;
	}

	public static boolean isAnEnclosingASTNode(ASTNode node, int x, int y) {
		return isAnEnclosingSpan(node.getLineNumber(), node.getColumnNumber(), node.getLastLineNumber(), node
				.getLastColumnNumber(), y, x);
	}

	public static boolean isAnEnclosingSpan(int line0, int col0, int line1, int col1, int refLine, int refCol) {
		if ((line0 == refLine && line1 == refLine && col0 <= refCol && refCol < col1)
				|| (line0 == refLine && refLine < line1 && col0 <= refCol) || (line0 < refLine && refLine < line1)
				|| (line1 == refLine && refCol < col1)) {
			return true;
		}
		return false;
	}
}

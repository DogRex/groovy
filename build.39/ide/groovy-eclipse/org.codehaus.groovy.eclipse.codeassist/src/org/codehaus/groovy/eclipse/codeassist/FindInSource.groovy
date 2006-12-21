package org.codehaus.groovy.eclipse.codeassist

import java.util.ArrayList
import java.util.Iterator

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.ModuleNode
import org.codehaus.groovy.eclipse.editor.actions.EditorPartFacade
import org.codehaus.groovy.eclipse.model.GroovyModel
import org.eclipse.jface.text.BadLocationException
import org.eclipse.jface.text.IRegion
import org.eclipse.jface.text.ITextViewer
import org.eclipse.swt.graphics.Point
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.PlatformUI

/**
 * Find ASTNodes by offset in viewer.
 * The static method names are verbs. e.g. FindInSource.enclosingASTNode(...)
 */
class FindInSource {
	static ASTNode enclosingASTNode(Class astNodeClass, ITextViewer viewer, int offset) {
		def part = PlatformUI.workbench.activeWorkbenchWindow.activePage.activeEditor
		if (part == null) {	return null	}

		// TODO: Is there a way to get a file from an ITextViewer?
		def facade = new EditorPartFacade(part)
		def moduleNodes = GroovyModel.getModel().getModuleNodes(facade.file)
		if (moduleNodes.size() == 0) { return null }
		
		// TODO: is there always 1? Can it be null?
		def moduleNode = moduleNodes.get(0)

		try {
			def rowcol = facade.getRowCol(offset)

			switch (astNodeClass) {
				case ModuleNode:
					return moduleNode
				case ClassNode:
					return moduleNode.classes.find { isAnEnclosingASTNode(it, rowcol.x, rowcol.y) }
				case MethodNode:
					return findEnclosingMethod(moduleNode, viewer, offset, rowcol.x, rowcol.y)
			}
		} catch (BadLocationException e) {
			// LOG
			e.printStackTrace()
			return null
		}
		return null
	}
	
	static Class variableType(String variableName, ITextViewer viewer, int offset) {
		def methodNode = enclosingASTNode(MethodNode.class, viewer, offset)
		if (methodNode == null) { return null }
		
		def code = methodNode.code
		def scope = code.variableScope
		
		def var = scope.getDeclaredVariable(variableName)
		if (!var) { var = scope.referencedLocalVariables[variableName] }
		if (!var) { var = scope.referencedClassVariables[variableName] }
		
		if (var) { return CodeAssistUtil.getClass(var.type.name) }
		return null
	}

	private static ASTNode findEnclosingMethod(ModuleNode moduleNode, ITextViewer viewer, int offset, int x, int y) {
		def methodNode = moduleNode.methods.find { isAnEnclosingASTNode(it, x, y) }
		if (!methodNode) {
			methodNode = moduleNode.classes.
					find { FindInSource.isAnEnclosingASTNode(it, x, y) } ?.methods.
					find { FindInSource.isAnEnclosingASTNode(it, x, y) }
		}
		return methodNode
	}

	static boolean isAnEnclosingASTNode(ASTNode node, int x, int y) {
		return isAnEnclosingSpan(node.lineNumber, node.columnNumber, node.lastLineNumber, node.getLastColumnNumber(), y, x)
	}

	static boolean isAnEnclosingSpan(int line0, int col0, int line1, int col1, int refLine, int refCol) {
		if ((line0 == refLine && line1 == refLine && col0 <= refCol && refCol < col1)
				|| (line0 == refLine && refLine < line1 && col0 <= refCol) || (line0 < refLine && refLine < line1)
				|| (line1 == refLine && refCol < col1)) {
			return true
		}
		return false
	}
}
package org.codehaus.groovy.eclipse.codebrowsing;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorPart;

/**
 * Default implemenatation of IDeclarationSearchInfo.
 * 
 * @author emp
 */
public class DeclarationSearchInfo implements IDeclarationSearchInfo {
	private String identifier;

	private IEditorPart editor;

	private IRegion region;

	private ModuleNode moduleNode;

	private ClassNode classNode;

	private ASTNode astNode;

	public DeclarationSearchInfo(ASTNodeSearchResult result,
			IEditorPart editor, IRegion region) {
		this(result.getIdentifier(), editor, region, result.getModuleNode(),
				result.getClassNode(), result.getASTNode());
	}

	public DeclarationSearchInfo(String identifier, IEditorPart editor,
			IRegion region, ModuleNode moduleNode, ClassNode classNode,
			ASTNode astNode) {
		this.identifier = identifier;
		this.editor = editor;
		this.region = region;
		this.moduleNode = moduleNode;
		this.classNode = classNode;
		this.astNode = astNode;
	}

	public String getIdentifier() {
		return identifier;
	}

	public IEditorPart getEditor() {
		return editor;
	}

	public IRegion getRegion() {
		return region;
	}

	public ModuleNode getModuleNode() {
		return moduleNode;
	}

	public ClassNode getClassNode() {
		return classNode;
	}

	public ASTNode getASTNode() {
		return astNode;
	}
}

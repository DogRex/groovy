package org.codehaus.groovy.eclipse.codebrowsing;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.eclipse.editor.actions.EditorPartFacade;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

public class ASTUtils {
	/**
	 * @param facade
	 * @param node
	 * @return The region, or null if one could not be created for the
	 *         node/editor.
	 */
	public static IRegion getRegion(EditorPartFacade facade, ASTNode node) {
		try {
			int offset0 = facade.getOffset(node.getLineNumber() - 1, node
					.getColumnNumber() - 1);
			int offset1 = facade.getOffset(node.getLastLineNumber() - 1, node
					.getLastColumnNumber() - 1);
			return new Region(offset0, offset1 - offset0);
		} catch (BadLocationException e) {
		}
		return null;
	}

	/**
	 * @param facade
	 * @param first
	 * @param next
	 *            The second ASTNode, which may be null, in which case the first
	 *            node line/column info is used to calculate the region.
	 * @return The region from the first ASTNode to before the next ASTNode.
	 */
	public static IRegion getRegion(EditorPartFacade facade, ASTNode first,
			ASTNode next) {
		if (next == null) {
			return getRegion(facade, first);
		}

		try {
			int offset0 = facade.getOffset(first.getLineNumber() - 1, first
					.getColumnNumber() - 1);
			int offset1 = facade.getOffset(next.getLineNumber() - 1, next
					.getColumnNumber() - 1);
			return new Region(offset0, offset1 - offset0 - 1);
		} catch (BadLocationException e) {
		}
		return null;
	}

	/**
	 * @param facade
	 * @param node
	 * @param length
	 * @return The region with the offset set to the line/column of the node,
	 *         and the length as specified.
	 */
	public static IRegion getRegion(EditorPartFacade facade, ASTNode node,
			int length) {
		try {
			int offset = facade.getOffset(node.getLineNumber() - 1, node
					.getColumnNumber() - 1);
			return new Region(offset, length);
		} catch (BadLocationException e) {
		}
		return null;
	}

	/**
	 * Proposals are returned in a list as there may be more than one proposal.
	 * However, often there is only one, so wrap it in a list.
	 * 
	 * @param proposal
	 * @return The wrapped proposal.
	 */
	public static List wrapProposal(IDeclarationMatchProposal proposal) {
		List result = new ArrayList();
		result.add(proposal);
		return result;
	}

	/**
	 * Creates a string suitable for display. This method should be used
	 * wherever possible for consistent results.
	 * 
	 * @param node
	 * @return The display string.
	 */
	public static String createDisplayString(MethodNode node) {
		StringBuffer sb = new StringBuffer();
		sb.append(node.getName());
		sb.append(createParameterString(node.getParameters()));
		sb.append(" ").append(node.getReturnType().getName());
		sb.append(" ").append(node.getDeclaringClass().getName());
		return sb.toString();
	}

	/**
	 * Creates a string suitable for display. This method should be used
	 * wherever possible for consistent results.
	 * 
	 * @param node
	 * @return The display string.
	 */
	public static String createDisplayString(FieldNode node) {
		StringBuffer sb = new StringBuffer();
		sb.append(node.getName()).append(" ").append(node.getType().getName());
		return sb.toString();
	}

	/**
	 * Creates a string suitable for display. This method should be used
	 * wherever possible for consistent results.
	 * 
	 * @param node
	 * @return The display string.
	 */
	public static String createDisplayString(VariableExpression expr) {
		StringBuffer sb = new StringBuffer();
		sb.append(expr.getName()).append(" ").append(expr.getType().getName());
		return sb.toString();
	}

	public static String createParameterString(Parameter[] parameters) {
		return createParameterString(parameters, ",", true);
	}

	/**
	 * @param parameters
	 * @param delimiter
	 *            The delimiter, normally ','
	 * @param parenthesis
	 *            If true, surround the string with ( )
	 * @return A parameter string.
	 */
	public static String createParameterString(Parameter[] parameters,
			String delimiter, boolean parenthesis) {
		StringBuffer sb = new StringBuffer();
		if (parenthesis)
			sb.append("(");
		for (int i = 0; i < parameters.length; ++i) {
			sb.append(parameters[i].getType().getName());
			sb.append(" ");
			sb.append(parameters[i].getName());
			sb.append(delimiter);
		}
		if (parameters.length > 0)
			sb.setLength(sb.length() - delimiter.length());
		sb.append(")");
		return sb.toString();
	}

	/**
	 * @see #isSurroundingNode(ASTNode, ASTNode)
	 */
	public static boolean isSurroundingNode(ASTNode candidate, ASTNode reference) {
		return isSurroundingNode(candidate, reference.getLineNumber(),
				reference.getColumnNumber());
	}

	/**
	 * Check if one node is surrounded by another. This is not a trivial check,
	 * as nodes can span multiple lines.
	 * 
	 * @param candidate
	 *            The candidate that might surround the reference.
	 * @param reference
	 *            The reference that might be surrounded by the candidate.
	 * @return True if surrounding, else false.
	 */
	public static boolean isSurroundingNode(ASTNode candidate, int line, int col) {
		int cline0 = candidate.getLineNumber();
		int cline1 = candidate.getLastLineNumber();
		int ccol0 = candidate.getColumnNumber();
		int ccol1 = candidate.getLastColumnNumber();

		if ((cline0 == line && cline1 == line && ccol0 <= col && col <= ccol1)
				|| (cline0 == line && line < cline1 && ccol0 <= col)
				|| (cline0 < line && line < cline1)
				|| (cline1 == line && col <= ccol1)) {
			return true;
		}
		return false;
	}

	/**
	 * Given some text, find the offset to the first match of some identifier in
	 * the text.
	 * 
	 * @param text
	 * @param identifier
	 * @return The offset, or -1 if there is no match.
	 */
	public static int findIdentifierOffset(String text, String identifier) {
//		Pattern pattern = Pattern.compile("");
//		Matcher matcher = pattern.matcher(text);
//		if (matcher.find()) {
//			return matcher.start();
//		}
		
		// TODO: really needs a regex, as the method name could be a superset
		// of the identifier name - that goes for other identifiers before the
		// one we are searching for.
		return text.indexOf(identifier);
		
//		return -1;
	}
}
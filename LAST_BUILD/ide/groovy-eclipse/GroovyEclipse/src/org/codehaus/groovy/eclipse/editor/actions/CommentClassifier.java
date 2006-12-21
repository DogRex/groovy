package org.codehaus.groovy.eclipse.editor.actions;

/**
 * Classifies the comments in a string.
 *
 * Given a string representing an area in a document, and its base offset in the 
 * document, classify the comments within the text area.
 *
 * This class was created to help add and remove block comments. Perhaps
 * it has other uses?
 *
 * @author emp
 */
public class CommentClassifier {
	private static final int OPEN = 1;
	private static final int CLOSE = 2;
	
	public static final int NONE = 0;
	
	//	 | /*    */ |
	public static final int SURROUNDING = OPEN | CLOSE;
	
	// | /*         |
	public static final int NOT_CLOSED = OPEN;
	
	// |         */ |
	public static final int NOT_OPEN = CLOSE;
	
	// | */     /*  |
	public static final int STRADLING = ~SURROUNDING;

	int status;
	int ixOpen;
	int ixClose;
	int openCount;
	int closeCount;
	
	public CommentClassifier(String text, int baseOffset) {
		openCount = count("/*", text);
		closeCount = count("*/", text);
		if (openCount > 1 || closeCount > 1) {
			status = NONE;
			ixOpen = ixClose = -1;
			//return
			// Odd, return here throws NPE in compiler
		} else {
			ixOpen = text.indexOf("/*");
			ixClose = text.lastIndexOf("*/");
	
			status = 0;
			if (ixOpen != -1)
				status = OPEN;
			if (ixClose != -1)
				status |= CLOSE;
			
			if (ixOpen != -1 && ixClose != -1 && ixOpen > ixClose)
				status = ~status;
				
			if (ixOpen != -1)
				ixOpen += baseOffset;
				
			if (ixClose != -1)
				ixClose += baseOffset;
		}
	}
	
	private int count(String seq, String text) {
		int count = 0;
		int len = seq.length();
		int ix = -len;
		while ((ix = text.indexOf(seq, ix + len)) != -1)
			++count;
		return count;
	}
}
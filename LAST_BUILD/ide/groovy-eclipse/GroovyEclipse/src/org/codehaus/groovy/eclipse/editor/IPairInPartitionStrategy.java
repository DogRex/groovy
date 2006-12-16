package org.codehaus.groovy.eclipse.editor;

import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;

/**
 * Interface for handling a specific auto pair strategy when in a specific
 * partition. This makes it easier to add strategies for different partitions.
 * 
 * @author emp
 */
public interface IPairInPartitionStrategy {
	/**
	 * Is this strategy active?
	 * 
	 * @return
	 */
	public boolean isActive();

	/**
	 * The user pressed a key that inserts a character, check for auto pair
	 * completion.
	 * 
	 * @param document
	 * @param command
	 */
	public void doInsert(IDocument document, DocumentCommand command);

	/**
	 * The user pressed the backspace key, check for auto pair deletion.
	 * 
	 * @param document
	 * @param command
	 */
	public void doRemove(IDocument document, DocumentCommand command);
}

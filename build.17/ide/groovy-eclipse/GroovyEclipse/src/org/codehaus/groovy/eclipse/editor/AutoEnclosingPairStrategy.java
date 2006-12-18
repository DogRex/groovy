package org.codehaus.groovy.eclipse.editor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;

/**
 * Strategy for automatically closing 'enclosing' pairs in Groovy code. This
 * strategy handles '' "" '''''' """""" () and [].
 * 
 * @author emp
 */
public class AutoEnclosingPairStrategy implements IAutoEditStrategy {
	private static Map mapPartitionToStrategy = new HashMap();

	private String partition;

	static {
		mapPartitionToStrategy.put(IJavaPartitions.JAVA_CHARACTER,
				new CharacterPartitionPairStrategy());
		mapPartitionToStrategy.put(GroovyPartitionScanner.GROOVY_SINGLELINE_STRINGS,
				new SingleLineStringPartitionPairStrategy());
		mapPartitionToStrategy.put(GroovyPartitionScanner.GROOVY_MULTILINE_STRINGS,
				new MultiLineStringPartitionPairStrategy());
	}

	public AutoEnclosingPairStrategy(String partition) {
		this.partition = partition;
	}

	public void customizeDocumentCommand(IDocument document,
			DocumentCommand command) {
		if (command.getCommandCount() == 1) {
			IPairInPartitionStrategy strategy = (IPairInPartitionStrategy) mapPartitionToStrategy
					.get(partition);
			if (strategy == null)
				return;

			if (strategy.isActive() == false)
				return;

			if (command.length == 0) {
				strategy.doInsert(document, command);
			} else if (command.length == 1) {
				strategy.doRemove(document, command);
			}
		}
	}
}

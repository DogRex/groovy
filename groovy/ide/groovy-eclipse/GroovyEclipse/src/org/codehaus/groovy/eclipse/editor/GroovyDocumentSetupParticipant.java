package org.codehaus.groovy.eclipse.editor;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.DefaultPartitioner;

/**
 */
public class GroovyDocumentSetupParticipant  implements IDocumentSetupParticipant {
	
	/**
	 */
	public GroovyDocumentSetupParticipant() {
	}

	/*
	 * @see org.eclipse.core.filebuffers.IDocumentSetupParticipant#setup(org.eclipse.jface.text.IDocument)
	 */
	public void setup(IDocument document) {
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3= (IDocumentExtension3) document;
			IDocumentPartitioner partitioner= new DefaultPartitioner(GroovyPlugin.getPlugin().getGroovyPartitionScanner(), GroovyPartitionScanner.GROOVY_PARTITION_TYPES);
			extension3.setDocumentPartitioner(GroovyPlugin.GROOVY_PARTITIONING, partitioner);
			partitioner.connect(document);
		}
	}
}

package org.codehaus.groovy.eclipse.ui.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.DefaultPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class GroovyDocumentProvider extends FileDocumentProvider {
	private GroovyPartitionScanner scanner;
	
	public GroovyDocumentProvider() {
		scanner = new GroovyPartitionScanner();
	}
	
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner =
				new DefaultPartitioner(
					scanner,
					new String[] {
						GroovyPartitionScanner.DEFAULT,
						GroovyPartitionScanner.MULTILINE_COMMENT,
						GroovyPartitionScanner.SINGLELINE_COMMENT,
						GroovyPartitionScanner.STRING });
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}
package org.codehaus.groovy.eclipse.editor.contentAssist;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;

public class GroovyContentAssistant extends ContentAssistant {
	// <String, ContentAssistProcessorSet>
	private static Map mapPartitionToProcessor = new HashMap();

	static {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint("org.codehaus.groovy.eclipse.contentAssistProcessors");
		IExtension[] extensions = point.getExtensions();
		for (int i = 0; i < extensions.length; ++i) {
			IExtension extension = extensions[i];
			IConfigurationElement[] elements = extension.getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				IConfigurationElement element = elements[j];
				String name = element.getAttribute("name");
				String partition = element.getAttribute("partition");
				try {
					IContentAssistProcessor processor = (IContentAssistProcessor) element
							.createExecutableExtension("processor");
					ContentAssistProcessorSet processorSet = (ContentAssistProcessorSet) mapPartitionToProcessor
							.get(partition);
					if (processorSet == null) {
						processorSet = new ContentAssistProcessorSet();
						mapPartitionToProcessor.put(partition, processorSet);
					}
					processorSet.addProcessor(processor);
					// ... can't wait to write this in Groovy and have nice and
					// clean list/map code.
				} catch (CoreException e) {
					// TODO: eclipse logging. How does it work?
					e.printStackTrace();
				}
			}
		}
	}

	public GroovyContentAssistant() {
		for (Iterator iter = mapPartitionToProcessor.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Entry) iter.next();
			String partition = (String) entry.getKey();
			ContentAssistProcessorSet processorSet = (ContentAssistProcessorSet) entry.getValue();
			setContentAssistProcessor(processorSet, partition);
		}
	}
}

package org.codehaus.groovy.eclipse.codebrowsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry of IDeclarationSearchProcessor instances.
 * 
 * Some of these are registered by the plugin, others are contributed via the
 * extension mechanism.
 * 
 * @author emp
 */
public class DeclarationSearchProcessorRegistry {
	/**
	 * Map from an AST class to a list of search processors.
	 */
	private static Map mapASTClassToProcessors = new HashMap();

	public static void registerProcessor(Class astClass,
			IDeclarationSearchProcessor processor) {
		List processors = (List) mapASTClassToProcessors.get(astClass);
		if (processors == null) {
			processors = new ArrayList();
			mapASTClassToProcessors.put(astClass, processors);
		}
		processors.add(processor);
	}

	public static List getProcessorsForASTClass(Class astClass) {
		return (List) mapASTClassToProcessors.get(astClass);
	}
}

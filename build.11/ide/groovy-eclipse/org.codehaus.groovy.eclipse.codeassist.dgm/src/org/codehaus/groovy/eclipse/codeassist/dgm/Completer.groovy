package org.codehaus.groovy.eclipse.codeassist.dgm

import org.codehaus.groovy.runtime.DefaultGroovyMethods

import org.eclipse.jface.text.contentassist.CompletionProposal
import org.eclipse.jface.text.contentassist.ICompletionProposal

import org.eclipse.swt.graphics.Image;

class Completer {
	private static icon = new Image(null, Completer.class.getResourceAsStream("icon.png"));

	private static displayStrings = []
	private static replaceStrings = []
	                         
	static {
		def methods = DefaultGroovyMethods.methods
		methods = methods.findAll { java.lang.reflect.Modifier.isStatic(it.modifiers) }

		// Operator mappings.
		def operatorMap = ['minus(':'-', 'div(':'/', 'plus(':'+', 'multiply(':'*', 'putAt(':'[]', 'getAt(':'[]', 'leftShift(':'<<', 'mod(':'%', 'negate(':'-',
			'or(':'|', 'xor(':'^', 'and(':'&', 'compareTo(':'==', 'isCase(':'case:', 'toSpeadMap(':'*:']
		
		// Get the display strings.
		displayStrings = methods.collect { method ->
			method.name + '(' + 
					method.parameterTypes.collect { it.simpleName } .join(',') + 
					") - ${method.returnType.simpleName}"
		}
		
		// Convert last arg as Closure to { }
		displayStrings = displayStrings.collect { str ->
			(str =~ /,?Closure\)/).replaceFirst(") { }")
		}
		
		// Add operator info to end where applicable.
		displayStrings = displayStrings.collect { str ->
			def entry = operatorMap.find { entry -> str.startsWith(entry.key) }
			str = !entry ? str: str + " (operator ${entry.value})"
		}
		
		// Create replace strings - remember to convert from GStrings!
		replaceStrings = methods.collect { "${it.name}()".toString() }
	}

	/**
 	 * DGM completions. They are returned without considering type, so all completions for a prefix are shown. 
 	 */
	static ICompletionProposal[] getDGMCompletions(String prefix, int offset) {
 		prefix = prefix.toLowerCase()
 		
 		def length = prefix.length()
 		offset -= length
 		
 		def proposals = []
 		                 
		replaceStrings.eachWithIndex { replaceString, ix ->
			println "$prefix : $replaceString"
			if (replaceString.toLowerCase().startsWith(prefix)) {
				proposals << new CompletionProposal(replaceString, offset, length, replaceString.lastIndexOf(')'), icon, displayStrings[ix], null, null)
			}
		}
 		
 		return proposals as ICompletionProposal[]
	}
}
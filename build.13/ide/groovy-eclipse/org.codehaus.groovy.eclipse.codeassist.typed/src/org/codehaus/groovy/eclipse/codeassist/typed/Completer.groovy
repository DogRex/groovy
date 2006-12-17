package org.codehaus.groovy.eclipse.codeassist.typed

import org.eclipse.jface.text.BadLocationException
import org.eclipse.jface.text.IDocument
import org.eclipse.jface.text.contentassist.CompletionProposal
import org.eclipse.jface.text.contentassist.ICompletionProposal
import org.eclipse.jface.text.contentassist.IContextInformation
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.graphics.Point

/**
 * Completer class.
 *
 * Ideally, this would use the Java completion via org.eclipse.jdt.core.IType. But I don't have time to figure it out
 * right now. Anyone?
 */
class Completer {
	private static EMPTY_PROPOSAL = new ICompletionProposal[0]
	private static mapPrimitiveToWrapper = [
		'byte' : Byte.name,
		'short' : Short.name,
		'char' : Character.name,
		'int' : 'java.lang.Integer', // Grr
		'long' : Long.name,
		'float' : Float.name,
		'double' : Double.name
	]
	                                                        
	static ICompletionProposal[] completeWithType(String typeName, String prefix, int offset) {
		if (mapPrimitiveToWrapper.containsKey(typeName)) {
			typeName = mapPrimitiveToWrapper[typeName]
		}
		
		def cls = makeClass(typeName)
		if (!cls) return EMPTY_PROPOSAL
		
		prefix = prefix.toLowerCase()
		
		def proposals = createPropertyProposals(cls, prefix, offset) + createMethodProposals(cls, prefix, offset)
		
		return proposals as ICompletionProposal[]
	}
	
	private static createPropertyProposals(cls, prefix, offset) {
		def proposals = []
 		def props = cls.getProperties()
 		def replaceLength = prefix.length()
 		offset -= replaceLength
 		props.each { name, type ->
 			if (name.toLowerCase().startsWith(prefix)) {
 				def replace = name
 				def display = name
 				// Groovy can't find c_tor, use our own.
 				proposals << new CompletionProposal(replace, offset, replaceLength, 0, null, display, null, null)
 			}
 		}
		
		return proposals
	}
	
	// TODO: this should be a util for taking a list of methods and making a list of proposals.
	private static createMethodProposals(cls, prefix, offset) {
		def methods = cls.methods
		
		// Create sets of unwanted methods.
		def operators = ["minus(", "div(", "plus(", "multiply(", "putAt(", "getAt(", "leftShift(", "mod(", "negate(",
			"or(", "xor(", "and(", "compareTo(", "isCase(", "toSpeadMap("]
		// Remove methods that need to be removed.
		methods = methods.findAll { method -> !operators.find { method.name.contains(it) } }
		
		methods = methods.findAll { it.name.toLowerCase().startsWith(prefix) }
		
		if (!prefix.startsWith('get')) {
			methods = methods.findAll { !(it.name.startsWith('get') && it.parameterTypes.length == 0) }
		}
		
		def displayStrings = methods.collect { method ->
			method.name + '(' + 
					method.parameterTypes.collect { it.simpleName } .join(',') + 
					") - ${method.returnType.simpleName}"
		}
		
		def replaceStrings = methods.collect { it.name + '()' }
		
		def proposals = [] 
		def replaceLength = prefix.length()
		offset -= replaceLength
		        		
		methods.eachWithIndex { it, ix ->
			proposals << new CompletionProposal(replaceStrings[ix], offset, replaceLength, 
					replaceStrings[ix].lastIndexOf(')'), null, displayStrings[ix], null, null)
		}
		
		return proposals
	}
	
	private static makeClass(def typeName) {
		try {
			return Class.forName(typeName)
		} catch (ClassNotFoundException e) {
			return null
		}
	}
}
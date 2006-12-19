package org.codehaus.groovy.eclipse.codeassist.typed

import org.codehaus.groovy.eclipse.codeassist.CodeAssistUtil
import org.eclipse.jface.text.BadLocationException
import org.eclipse.jface.text.IDocument
import org.eclipse.jface.text.contentassist.CompletionProposal
import org.eclipse.jface.text.contentassist.ICompletionProposal
import org.eclipse.jface.text.contentassist.IContextInformation
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.graphics.Point

/**
 * Typed code compeltion.
 * The methods read as verbs, e.g. Complete.instanceFieldAccess(), Complete.className(), etc.
 */
class Completer {
	private static EMPTY_PROPOSAL = new ICompletionProposal[0]
	
	
	/**
	 * instance.@fie_ld
	 */
	static ICompletionProposal[] instanceFieldAccess(Class type, String rightText, int offset) {
	}
	
	/**
	 * instance.proper_ty
	 */
	static ICompletionProposal[] instancePropertyAccess(Class type, String rightText, int offset) {
		def proposals = []
   		def props = type.getProperties()
   		// TODO: filter for public properties.
  		def replaceLength = rightText.length()
  		offset -= replaceLength
  		props.each { name, typeName ->
  			if (name.toLowerCase().startsWith(rightText)) {
  				def replace = name
  				def display = name
  				// Groovy can't find c_tor, use our own.
  				proposals << new CompletionProposal(replace, offset, replaceLength, 0, null, display, null, null)
  			}
  		}
 		
 		return proposals		
	}
	
	/**
	 * instance.metho_dCall()
	 */
	static ICompletionProposal[] instanceMethodCall(Class type, String rightText, int offset) {
		def methods = type.methods
		
		methods = filterMethods(methods, rightText)
		
		def displayStrings = createDisplayStrings(methods)
		
		def replaceStrings = createReplaceStrings(methods)
		
		createProposals(rightText, offset, displayStrings, replaceStrings, { replaceString -> replaceString.lastIndexOf(')') } )
	}
	
	/**
	 * SomeCla_ss
	 */
	static ICompletionProposal[] className(Class type, String rightText, int offset) {
	}
	
	/**
	 * SomeClass.fie_ld
	 */
	static ICompletionProposal[] staticFieldAccess(Class type, String rightText, int offset) {
	}
	
	/**
	 * SomeClass.proper_ty
	 */
	static ICompletionProposal[] staticPropertyAccess(Class type, String rightText, int offset) {
	}
	
	/**
	 * SomeClass.metho_dCall()
	 */
	static ICompletionProposal[] staticMethodCall(Class type, String rightText, int offset) {
	}
	
	/**
	 * proper_ty
	 */
	static ICompletionProposal[] propertyAccess(Class type, String rightText, int offset) {
	}
	
	/**
	 * metho_dCall()
	 */
	static ICompletionProposal[] methodCall(Class type, String rightText, int offset) {
	}
	                                                       
	static ICompletionProposal[] completeWithType(Class type, String rightText, int offset) {
		if (!type) { return EMPTY_PROPOSAL }
		
		rightText = rightText.toLowerCase()
		
		def proposals = createPropertyProposals(type, rightText, offset) + createMethodProposals(type, rightText, offset)
		
		return proposals as ICompletionProposal[]
	}
	
	private static createPropertyProposals(cls, rightText, offset) {
		def proposals = []
 		def props = cls.getProperties()
 		def replaceLength = rightText.length()
 		offset -= replaceLength
 		props.each { name, type ->
 			if (name.toLowerCase().startsWith(rightText)) {
 				def replace = name
 				def display = name
 				proposals << new CompletionProposal(replace, offset, replaceLength, 0, null, display, null, null)
 			}
 		}
		
		return proposals
	}
	
	// TODO: this should be a util for taking a list of methods and making a list of proposals.
	private static createMethodProposals(cls, rightText, offset) {
		def methods = cls.methods
		
		// Create sets of unwanted methods.
		def operators = ["minus(", "div(", "plus(", "multiply(", "putAt(", "getAt(", "leftShift(", "mod(", "negate(",
			"or(", "xor(", "and(", "compareTo(", "isCase(", "toSpeadMap("]
		// Remove methods that need to be removed.
		methods = methods.findAll { method -> !operators.find { method.name.contains(it) } }
		
		methods = methods.findAll { it.name.toLowerCase().startsWith(rightText) }
		
		if (!rightText.startsWith('get')) {
			methods = methods.findAll { !(it.name.startsWith('get') && it.parameterTypes.length == 0) }
		}
		
		def displayStrings = methods.collect { method ->
			method.name + '(' + 
					method.parameterTypes.collect { it.simpleName } .join(',') + 
					") - ${method.returnType.simpleName}"
		}
		
		def replaceStrings = methods.collect { it.name + '()' }
		
		def proposals = [] 
		def replaceLength = rightText.length()
		offset -= replaceLength
		        		
		methods.eachWithIndex { it, ix ->
			proposals << new CompletionProposal(replaceStrings[ix], offset, replaceLength, 
					replaceStrings[ix].lastIndexOf(')'), null, displayStrings[ix], null, null)
		}
		
		return proposals
	}
		
	/**
	 * cursorOffsetter: { replaceString -> <offset in replace string> }
	 */
	private static createProposals(rightText, offset, displayStrings, replaceStrings, cursorOffsetter, icon=null) {
		def proposals = [] 
		def replaceLength = rightText.length()
		offset -= replaceLength
		        		
		replaceStrings.eachWithIndex { replaceString, ix ->
			proposals << new CompletionProposal(replaceString, offset, replaceLength, 
					cursorOffsetter(replaceString), icon, displayStrings[ix], null, null)
		}
		
		proposals
	}

		
	// TODO: add access, private, protected, etc.
	private static operators = ["minus(", "div(", "plus(", "multiply(", "putAt(", "getAt(", "leftShift(", "mod(", "negate(",
	               		  			"or(", "xor(", "and(", "compareTo(", "isCase(", "toSpeadMap("]
	private static filterMethods(methods, rightText, ignoreGetters=false, ignoreSetters=false) {
		methods = methods.findAll { it.name.toLowerCase().startsWith(rightText) }
		
		if (!rightText.startsWith('get') || ignoreGetters) {
			methods = methods.findAll { !(it.name.startsWith('get') && it.parameterTypes.length == 0) }
		}

		if (!rightText.startsWith('set') || ignoreSetters) {
			methods = methods.findAll { !(it.name.startsWith('set') && it.parameterTypes.length == 0) }
		}

		methods
	}
	
	private static createDisplayStrings(methods) {
		// TODO: operator stuff.
		methods.collect { method ->
			method.name + '(' + 
					method.parameterTypes.collect { it.simpleName } .join(',') + 
					") - ${method.returnType.simpleName}"
		}
	}
	
	private static createReplaceStrings(methods) {
		methods.collect { it.name + '()' }
	}
}
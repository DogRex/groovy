package org.codehaus.groovy.eclipse.codeassist

/**
 * Various utilies useful for code assist plugins.
 */
class CodeAssistUtil {
	 private static mapPrimitiveToWrapper = [
 		'byte' : Byte.name,
 		'short' : Short.name,
 		'char' : Character.name,
 		'int' : Integer.name,
 		'long' : Long.name,
 		'float' : Float.name,
 		'double' : Double.name
 	]
	 
	/**
	 * Returns a class for the given class name. The name may also be a primitive name, int, float, etc. in which
	 * case the wrapper Integer, Float, etc. will be returned.
	 * Returns null is the class cannot be returned, normally because it is not on the class path.
	 */
	static Class getClass(String typeName) {
		if (mapPrimitiveToWrapper.containsKey(typeName)) {
			typeName = mapPrimitiveToWrapper[typeName]
		}
		try {
			return Class.forName(typeName)
		} catch (ClassNotFoundException e) {
			return null
		}
	}
	 
	/**
	 * Reverse a string. Useful when using CodeAssistUtil.reverseRegex() as the results will be reversed.
	 */
	static String reverseString(String str) {
		str.reverse()
	}
	
	/**
	 * Reverse a regex pattern.
	 * java.util.regex can only search forwards (that I know of, please let me be right after all this :O ).
	 * This method reverses a regex pattern so one can match a reversed string. With DocumentReverseCharSequence
	 * this makes it easy to search backwards for patterns in a document.
	 * 
	 * The current use for this method is to seach backwards for interesting code, e.g. closest method declaration,
	 * possible completion patterns, and so on. See the TestCodeAssistUtilReverseRegex.groovy for examples.
	 */
	static String reverseRegex(String str) {
		// Strategy: split the string it largest subgroups and patterns. These are children of root.
		// Process the children similarly until there are only leaves made up of patterns.
		// Swap the patterns in the leafs.
		// Swap the children of each node.
		// Recombine the nodes to create the reverse pattern.
		
		// And now, after all this, someone will tell me that I can in fact match backwards :)
		def buffer = new StringBuffer(str)
		def root = [text:buffer]
		buildSubtree(root, root.text)
		processPatterns(root)
		reverseNodes(root)
		buffer = new StringBuffer()
		combineNodes(root, buffer)
		return buffer
	}
	
	private static buildSubtree(parent, buffer) {
		println "in:buildSubtree"
		def node = [:]
		parent.children = splitChildren(buffer)
		
		if (parent.children == null) {
			return
		}
		
//		 Require 'CodeAssistUtil.' http://jira.codehaus.org/browse/GROOVY-1538
		parent.children.each { CodeAssistUtil.buildSubtree(it, it.text) }
	}
	
	private static processPatterns(node) {
		println "in:processPatterns"
		if (!node.children) {
			node.patterns = extractAndReversePatterns(node.text)
		}
		node.children.each { CodeAssistUtil.processPatterns(it) }
	}
	
	private static reverseNodes(node) {
		println "in:reverseNodes"
		node.children = node.children?.reverse()
		node.children?.each { CodeAssistUtil.reverseNodes(it) }
	}
	
	private static combineNodes(node, result) {
		println "in:combineNodes"
		if (node.leftDelim) {
			result.append(node.leftDelim)
		}
		if (!node.children) {
			result.append(node.patterns)
		} else {
			node.children.each { CodeAssistUtil.combineNodes(it, result) }
		}
		if (node.rightDelim) {
			result.append(node.rightDelim)
		}
	}
	
	// Splits children returning a list of maps with 'text' property for child text.
	// If child is just a pattern, returns null.
	private static splitChildren(buffer) {
		def children = []
		def ix = 0
		def patternCount = 0
		                
		while (ix < buffer.length()) {
			def child = [:]
			if (buffer[ix] == '\\' || buffer[ix] != '(') {
				extractPattern(child, buffer, ix)
				++patternCount
			} else {
				extractGroup(child, buffer, ix)
			}
			// Darn, null isn't zero :(
			//ix += child.text.length() + child.leftDelim?.length() + child.rightDelim?.length()
			ix += child.text.length()
			if (child.leftDelim) ix += child.leftDelim.length()
			if (child.rightDelim) ix += child.rightDelim.length()
			children << child
		}
		
		if (patternCount == 1 && children.size() == 1) {
			println "out:splitChildren:null"
			return null
		}
		
		println "out:splitChilden:list"
		return children
	}
	
	private static extractPattern(child, buffer, ix) {		
		println "in:extractPattern:buffer: " + buffer[ix..-1]
		def regex = /(?:(?:\\)?(?:[\w\s,|$=\[\]]|\.|\\\)|\\\(|\\\{|\\\})+[+*?]?)+/
		def matcher = buffer =~ regex
		matcher.find(ix)
		child.text = matcher.group(0)
		println "out:extractPattern: " + child.text
	}
	
	private static extractGroup(child, buffer, ix) {
		println "in:extractGroup"
		def group = extractRange(buffer, ix, '(', ')')
		println "extract range: " + group 
		def quantifier1 = ''
		def quantifier2 = ''
		def ixCurrent = ix + group.length()
		
		if (ixCurrent < buffer.length() && buffer[ixCurrent] == '{') {
			quantifier1 = extractRange(buffer, ixCurrent, '{', '}')
		}
		println "extract range quantifier 1: " + quantifier1
		ixCurrent += quantifier1.length()
		if (ixCurrent < buffer.length() && buffer[ixCurrent] in ['+', '*', '?']) {
			quantifier2 = buffer[ixCurrent++]
		}
		println "extract range quantifier 2: " + quantifier2
		
		def close = ')' + quantifier1 + quantifier2
		child.leftDelim = group[0..2] == '(?:' ? '(?:' : '('
		child.rightDelim = close
		child.text = buffer[ix + child.leftDelim.length() ..< ix + group.length() - 1]
		println "left delim: " + child.leftDelim
		println "right delim: " + child.rightDelim
		println "child.text:" + child.text
		println "out:extractGroup"
	}
		
	/**
	 * Extract patters from the buffer. Patterns are single characters or
	 * escaped characters, or quantifiers [+-*] {n...} or sets.
	 * Sets and {...} quantifiers are treated specially.
	 * sample: (\(\$abc_\))+(de[123]{3,4})fg+(h*ijk?){1,2}+
	 * regex: (?:(?:\\.)|\w|[\[\{\|])(?:[+*?])?
	 * output: '\(' '\$' 'a' 'b' 'c' '_' '\)' 'd' 'e' '[' '1' '2' '3' '{' '3' '4' 'f' 'g+' 'h*' 'i' 'j' 'k?' '{' '1' '2'
	 */
	static private extractAndReversePatterns(buffer) {
		def patterns = []
		def regex = /(?:\\.|\[.+\]|.)[*+?]?/
		def matcher = buffer =~ regex
		def ix = 0
		while (matcher.find(ix)) {
			patterns << matcher.group(0)
			ix = matcher.end()
		}
		return patterns.reverse().join()
	}
		
	/**
	 * Extract a range of text between open and close delimiter, including the delimiters.
	 * This method takes into account escaped delimiters.
	 */
	private static extractRange(buffer, start, open, close) {
		if (buffer[start] != open) {
			throw new IllegalStateException("Expecting start of a range with '$open'")
		}
		def ix = start + 1
		int openCount = 1
		boolean escape = false
		while (openCount != 0) {
			if (buffer[ix] == '\\') {
				escape = true
			} else if (!escape) {
				if (buffer[ix] == open) ++openCount
				else if (buffer[ix] == close) --openCount
			} else {
				escape = false;
			}
			++ix
		}
		
		return buffer[start..<ix]
	}
}
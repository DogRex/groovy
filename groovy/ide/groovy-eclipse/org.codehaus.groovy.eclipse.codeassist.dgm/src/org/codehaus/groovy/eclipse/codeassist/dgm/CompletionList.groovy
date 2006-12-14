/**
 * Script to create a list of method names suitable for completion.
 * This list is used without any type context, so all completions for a prefix are shown.
 */
package org.codehaus.groovy.eclipse.codeassist.dgm;

import org.codehaus.groovy.runtime.DefaultGroovyMethods

// Get the DGM method names.
def displayStrings = DefaultGroovyMethods.methods.collect { it.toString() }

// Create sets of unwanted methods.
def object = ["hashCode(", "getClass(", "equals(", "toString(", "wait(", "notify(", "notifyAll("]
def operators = ["minus(", "div(", "plus(", "multiply(", "putAt(", "getAt(", "leftShift(", "mod(", "negate(",
	"or(", "xor(", "and(", "compareTo(", "isCase(", "toSpeadMap("]
def remove = object + operators

// Remove methods that need to be removed.
displayStrings = displayStrings.findAll { name -> !remove.find { name.contains(it) } }

// Clean up the strings
displayStrings = displayStrings.collect {
	def str = it - 
	"org.codehaus.groovy.runtime.DefaultGroovyMethods." - 
	/^public static / - 
	/ throws .*$/
	// Remove package a.b.* leaving just the class name.
	str = (str =~ /(\w+\.)*/).replaceAll('')
	// Indicate the first arg is the current instance being completed on.
	str = str.replaceAll(/\([\w\[\]]+/) { a -> "(*${a[1..-1]}*" }
	// Because I just can't help myself - convert Closure to { }
	(str =~ /,?Closure\)/).replaceFirst(") { }")
}

// Move the return type to the end
displayStrings = displayStrings.collect { def ix = it.indexOf(' '); "${it[ix+1..-1]} - ${it[0..<ix]}" }

//names.sort().each { println it }

// This is the final display text.
displayStrings = displayStrings.sort()

// Now to make replacement text. Remove the params. Remove the trailing return type.
def replaceStrings = [displayStrings].flatten() // Hmm, copy method?
replaceStrings = replaceStrings.collect {
	str = (it =~ /\(.*\)/).replaceFirst("()")
	(str =~ /\).*$/).replaceFirst(")")
}

// Make sure there are no GStrings.
displayStrings = displayStrings.collect { it.toString() }
replaceStrings = replaceStrings.collect { it.toString() }

// Return the final list - but first look at the above code and be Groovily happy!
// And if you are not, port it to Java.
[displayStrings:displayStrings, replaceStrings:replaceStrings]
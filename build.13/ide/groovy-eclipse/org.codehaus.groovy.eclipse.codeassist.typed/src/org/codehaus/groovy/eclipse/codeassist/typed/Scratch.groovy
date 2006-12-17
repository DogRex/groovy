package org.codehaus.groovy.eclipse.codeassist.typed


class Scratch {
	int c = 0
	
	def doit() {
		def a = "a" // DeclarationExpression - line/col is good
		a.leng // PropertyExpression
		a.length().toStr
		b = 10
		c = b
		return null
	}
	
	def doit2() {
	}
}
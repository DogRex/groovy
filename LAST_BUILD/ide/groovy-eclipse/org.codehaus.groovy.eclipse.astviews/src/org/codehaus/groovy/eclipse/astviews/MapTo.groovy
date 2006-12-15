package org.codehaus.groovy.eclipse.astviews

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*

class MapTo {
	// TODO: no syntax errors in Eclipse if the package for the below is not imported. Why?
	static mapToNames = [
      	(ClassNode) : { "${it.class.simpleName} : ${it.name}" },
      	(ExpressionStatement) : { "${it.class.simpleName} : ${it.expression.text}" },
      	(FieldNode) : { it.name },
      	(MethodNode) : { it.name },
      	(ModuleNode) : { it.description },
      	(Parameter) : { it.name },
    ]	
	
	static String names(Object value) {
		def cls = value.getClass()
		def getter = mapToNames[cls]
		while (getter == null && cls != null) {
			cls = cls.superclass
			getter = mapToNames[cls]
		}
		return getter?.call(value)
	}
}
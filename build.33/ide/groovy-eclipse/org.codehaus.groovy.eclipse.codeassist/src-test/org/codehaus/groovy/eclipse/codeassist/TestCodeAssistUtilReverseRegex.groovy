package org.codehaus.groovy.eclipse.codeassist

class TestCodeAssistUtilReverseRegex extends GroovyTestCase {
	void testSimple() {
		println "*** testSimple ***"
		def fwd = /abc+\w?/
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		assert fwd.length() == bwd.length()
		
		def tests = ["abc", "abccc", "abcD", 'abcccD']
		def reversed = tests.collect { it.reverse() }
		
		tests.each { assert it ==~ fwd }
		reversed.each { assert it ==~ bwd }
	}
	
	void testSimple2() {
		println "*** testSimple2 ***"
		def fwd = /(\w+)\.\w*/
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		println bwd
		assert fwd.length() == bwd.length()
	}
	
	void testTopLevelGroup() {
		println "*** testTopLevelGroup ***"
		def fwd = /(abcd)/
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		assert fwd.length() == bwd.length()
		
		def test = 'abcd'
		assert test ==~ fwd
		assert test.reverse() ==~ bwd
	}
	
	void testTwoGroups() {
		println "*** testTwoGroups ***"
		def fwd = /(abcd)(ef)/
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		assert fwd.length() == bwd.length()
		
		def test = 'abcdef'
		assert test ==~ fwd
		assert test.reverse() ==~ bwd
	}
	
	void testNestedGroups() {
		println "*** testNestedGroups ***"
		def fwd = /((abcd) ef)gh(i)/
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		assert fwd.length() == bwd.length()
		
		def test = 'abcd efghi'
		assert test ==~ fwd
		assert test.reverse() ==~ bwd
	}
	
	void testDelimeters() {
		println "*** testDelimeters ***"
		def fwd = /(?:(abc+d)? ef*){1,2}+gh(i){1}?/
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		assert fwd.length() == bwd.length()
		
		def test = 'abcd efghi'
		assert test ==~ fwd
		assert test.reverse() ==~ bwd
	}
	
	void testGroupedSet() {
		println "*** testSimpleSet ***"
		def fwd = /[def]/
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		assert fwd.length() == bwd.length()
		
		def test = 'e'
		assert test =~ fwd
		assert test.reverse() =~ bwd
	}
	
	void testQuantifiedGroup() {
		println "*** testQuantifiedGroup ***"
		def fwd = /(abc)+/
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		assert fwd.length() == bwd.length()
		
		def test = 'abcabc'
		assert test =~ fwd
		assert test.reverse() =~ bwd
	}
	
	void testQuantifiedSet() {
		println "*** testQuantifiedSet ***"
		def fwd = /[def]*/
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		assert fwd.length() == bwd.length()
		
		def test = 'efedee'
		assert test =~ fwd
		assert test.reverse() =~ bwd
	}
	
	void testQuantifiedEscapedSet() {
		println "*** testQuantifiedEscapedSet ***"
		def fwd = /\([def]*\)/
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		assert fwd.length() == bwd.length()
		
		def test = '(efedee)'
		assert test =~ fwd
		assert test.reverse() =~ bwd
	}
	
	void testMethod() {
		println "*** testMethod ***"
		def modifiers = /((?:abstract|def|final|native|private|protected|public|static|synchronized)\s+)+/
		def typeSpec = /(?:\w+\s+)?/
		def methodName = /(\w|\$)+\s*/
		def params = /\([\w\,\s]*\)/		

		def fwd = modifiers + typeSpec + methodName + params 
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		assert fwd.length() == bwd.length()
		
		println fwd
		println bwd
		
		def test = "static String reverseRegex(String str)"
		def reversed = test.reverse()
		
		assert test ==~ fwd
		assert reversed ==~ bwd
	}
	
	void testClass() {
		println "*** testClass ***"
		
		def test = "final class CodeAssistUtil" 
			
	}
	
	void testImport() {
		println "*** testImport ***"
		
		def fwd = /import\s((\w+)(\.\w*)*)?/
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		assert fwd.length() == bwd.length()
		
		def test1 = "import org.codehaus.groovy.eclipse.codeassist.CodeAssistUtil"
		def test2 = "import org.codehaus.groovy.eclipse."
		def test3 = "import "
		
		assert test1 =~ fwd
		assert test2 =~ fwd
		assert test3 =~ fwd
		
		assert test1.reverse() =~ bwd
		assert test2.reverse() =~ bwd
		assert test3.reverse() =~ bwd
	}
	
	void testWithEquals() {
		println "*** testWithEquals ***"
		def fwd = /a = b/
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		assert fwd.length() == bwd.length()
		
		def test = 'a = b'
		assert test =~ fwd
		assert test.reverse() =~ bwd
	}
	
	void testWithEqualsAndSpace() {
		println "*** testWithEquals ***"
		def fwd = /a\s*=\s*b/
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		assert fwd.length() == bwd.length()
		
		def test = 'a=   b'
		assert test =~ fwd
		assert test.reverse() =~ bwd
	}
	
	void testWithEqualsAndSpaceAndEscapedBraces() {
		println "*** testWithEquals ***"
		def fwd = /a\s*=\s*\{b\}/
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		assert fwd.length() == bwd.length()
		
		def test = 'a={b}'
		assert test =~ fwd
		assert test.reverse() =~ bwd
	}
	
	void testNestedOptionalGroups() {
		println "*** testNestedOptionalGroups ***"
		def fwd = /a(?:b(?:c(?:d)?)?)?/
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		assert fwd.length() == bwd.length()
		
		def test = 'abcd'
		assert test =~ fwd
		assert test.reverse() =~ bwd
	}
	
	void testGrailsInConstraints() {
		println "*** testGrailsConstraints ***"
		
		def fwd = /static\s*contraints\s*=\s*\{\s*(?:(\w+)\s*\(\s*(?:(\w+)\s*\:\s*(\w+)?)?)?/
		def bwd = CodeAssistUtil.reverseRegex(fwd)
		assert fwd.length() == bwd.length()
		
		def test = "static  contraints={name(blank :false"
		
		assert test =~ fwd
		assert test.reverse() =~ bwd
	}
}

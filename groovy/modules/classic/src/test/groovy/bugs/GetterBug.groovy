/**
 * @version $Revision$
 */
class GetterBug extends GroovyTestCase {
     
    String foo
    
    String getFoo() {
    	if (foo == null) { 
    		foo = "James"
    	}
    	return foo
    }
    
    void setFoo(String foo) {
    	this.foo = foo
   	}
    
    void testTypedGetterAndSetter() {
    	println "Running test"
    	
    	value = getFoo()
    	
    	println "Value is ${value}"
    	
    	assert value == "James"
    	
    	setFoo("Bob")
    	
    	value = getFoo()
    	
    	assert value == "Bob"
    }
    
    bar
    
    getBar() {
    	if (this.bar == null) {
    		this.bar = "James"
    	}
    	bar
    }
    
    void setBar(bar) {
    	this.bar = bar
    }
    
    
    void testUntypedGetterAndSetter() {
    	println "Running test"
    	
    	value = getBar()
    	
    	println "Value is ${value}"
    	
    	assert value == "James"
    	
    	setBar("Bob")
    	
    	value = getBar()
    	
    	assert value == "Bob"
    }
    
}
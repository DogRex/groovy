class CastTest extends GroovyTestCase {

    Short b = 1
    
    void testCast() {
        x = (Short) 5

        println("Cast Integer to ${x} with type ${x.class}")
        
        assert x.class == Short
        
        methodWithShort(x)
    }
    
    void testImplicitCast() {
        Short x = 6
        
        println("Created ${x} with type ${x.class}")
        
        assert x.class == Short : "Type is ${x.class}"
        
		methodWithShort(x)
        
        x = 7
        
        println("Updated ${x} with type ${x.class}")
        
        assert x.class == Short : "Type is ${x.class}"
    }

    void testImplicitCastOfField() {

        println("Field is ${b} with type ${b.class}")
        
        assert b.class == Short : "Type is ${b.class}"
        
        b = 5
        
        println("Updated field ${b} with type ${b.class}")
 
        assert b.class == Short : "Type is ${b.class}"
    }

    void methodWithShort(Short s) {
        println("Called with ${s} with type ${s.class}")
        assert s.class == Short
    }
}

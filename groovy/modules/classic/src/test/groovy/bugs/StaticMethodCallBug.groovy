package groovy.bugs

/** 
 * @version $Revision$
 */
class StaticMethodCallBug extends GroovyTestCase {

    void testBug() {
        value = TestSupport.mockStaticMethod()
        assert value == "cheese"
    }
    
    void testStaticProperty() {
        value = TestSupport.mockStaticProperty
        assert value == "cheese"
    }
}

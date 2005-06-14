/**
 * Expose how to deal with multi-dimensional Arrays until this is supported at the language level.
 * @author Dierk Koenig
 */
 
package groovy;

public class MultiDimArraysTest extends GroovyTestCase {

    void notSupported_testCallTwoDimStringArray(){
        def someArrayOfStringArrays =  new SomeClass().anArrayOfStringArrays()
        assert 1 == someArrayOfStringArrays.size()
    }
    
    void testCallTwoDimStringArrayWorkaround(){
        def someArrayOfStringArrays =  new SomeClass().anArrayOfStringArraysWorkaround()
        assert 1 == someArrayOfStringArrays.size()
        assert "whatever" == someArrayOfStringArrays[0][0]
        for (i in 0..<someArrayOfStringArrays.size()) {
            assert someArrayOfStringArrays[i]
        }
    }

    // todo: make this pass
    void Failing_testCallTwoDimStringArrayWorkaroundWithNull(){
        def someArrayOfStringArrays =  new SomeClass().anArrayOfStringArraysWorkaround()
        assert 1 == someArrayOfStringArrays.size()
        assert "whatever" == someArrayOfStringArrays[0][0]
        someArrayOfStringArrays.each(){ assert it}                // throws NPE !!
    }

    void testInsideGroovyMultiDimReplacement(){
        Object[] someArrayOfStringArrays = [["a","a","a"],["b","b","b",null]]
        assert "a" == someArrayOfStringArrays[0][0]
        someArrayOfStringArrays.each(){ assert it}
    }
}
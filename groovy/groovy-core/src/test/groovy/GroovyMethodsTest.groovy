import java.io.InputStreamReader
import java.awt.Dimension
import org.codehaus.groovy.runtime.typehandling.GroovyCastException

/** 
 * Tests the various new Groovy methods
 * 
 * @author <a href="mailto:james@coredevelopers.net">James Strachan</a>
 * @author Guillaume Laforge
 * @author Dierk Koenig
 * @author Paul King
 * @version $Revision$
 */
class GroovyMethodsTest extends GroovyTestCase {
    void testCollect() {
        assert [2, 4, 6].collect { it * 2} == [4, 8, 12]

        def answer = [2, 4, 6].collect(new Vector()) { it * 2}

        assert answer[0] == 4
        assert answer[1] == 8
        assert answer[2] == 12

        assert [1:'a', 2:'b', 3:'c'].collect{k,v -> k + v} == ['1a','2b','3c']

        assert [1:'a', 2:'b', 3:'c'].collect{it.getKey() + "*" + it.getValue()} == ['1*a','2*b','3*c']

    }

    void testAsCoercion() {
        def d0 = new Dimension(100, 200)
        assert d0 == new Dimension(width:100, height:200)
        assert d0 == [100,200] as Dimension
        assert d0 == [width:100, height:200] as Dimension
    }

    void testSum() {
    	assert [].sum() == null
    	assert [1].sum() == 1
    	assert [1, 2, 3].sum() == 6

    	assert [].sum() {it.length()} == 0
    	assert ["abc"].sum() {it.length()} == 3
    	assert ["a", "bc", "def"].sum() {it.length()} == 6
    }

    void testJoin() {
        assert [2, 4, 6].join("-") == "2-4-6"
        assert ["edam", "cheddar", "brie"].join(", ") == 'edam, cheddar, brie'

        println( ["abc", 5, 2.34].join(", ") )
    }

    void testTimes() {
        def count = 0
        5.times { i -> count = count + i }
        assert count == 10

        count = 0
        def temp = 5
        temp.times { i -> count = count + i }

        assert count == 10
    }

    void testArraySubscript() {
        def list = [1, 2, 3, 4]
        def array = list.toArray()

        def value = array[2]

        assert value == 3

        array[0] = 9

       assert array[0] == 9
    }

    void testToCharacterMethod() {
        def s = 'c'
        def x = s.toCharacter()

        assert x instanceof Character
    }

    void testListGrep() {
        def list = ["James", "Bob", "Guillaume", "Sam"]
        def answer = list.grep(~".*a.*")

        assert answer == ["James", "Guillaume", "Sam"]

        answer = list.grep(~"B.b")

        assert answer == ["Bob"]
    }

    void testCollectionToList() {
        def c = [1, 2, 3, 4, 5] // but it's a list
        def l = c.toList()
        assert l.containsAll(c)
        assert c.size() == l.size()
    }

    void testCollectionAsList() {
        Integer[] nums = [1, 2, 3, 4, 5]
        def numList = nums as List
        nums.each{ assert numList.contains(it) }
        assert nums.size() == numList.size()
    }

    void testCollectionAsLinkedList() {
        Integer[] nums = [1, 2, 3, 4, 5]
        def numList = nums as LinkedList
        nums.each{ assert numList.contains(it) }
        assert nums.size() == numList.size()
        assert numList.class == LinkedList.class
    }

    void testArrayListAsLinkedList() {
        ArrayList nums = [1, 2, 3, 4, 5]
        shouldFail(GroovyCastException.class) {
            def numList = nums as LinkedList
        }
    }

    void testFileSize() {
        assert new File('project.properties').size()
    }

    void testMatcherSize() {
        assertEquals 3, ( 'aaa' =~ /./ ).count
        assertEquals 3, ( 'aaa' =~ /./ ).size()
        assertEquals 1, ( 'a' =~ /./ ).size()
        assertEquals 0, ( 'a' =~ /x/ ).size()
    }

    void testJoinString() {
        String[] arr = ["a", "b", "c", "d"]
        def joined = arr.join(", ")

        assert joined == "a, b, c, d"
    }

    void testReverseEach() {
        def l = ["cheese", "loves", "Guillaume"]
        def expected = ["Guillaume", "loves", "cheese"]

        def answer = []
        l.reverseEach{ answer << it }

        assert answer == expected
    }

    void testGrep() {
        def list = ["Guillaume", "loves", "cheese"]

        def answer = list.grep(~".*ee.*")
        assert answer == ["cheese"]

        list = [123, "abc", 4.56]
        answer = list.grep(String)
        assert answer == ["abc"]

        list = [4, 2, 7, 3, 6, 2]
        answer = list.grep(2..3)
        assert answer == [2, 3, 2]
    }

    void testMapGetWithDefault() {
        def map = [:]

        assert map.foo == null

        map.get("foo", []).add(123)

        assert map.foo == [123]

        map.get("bar", [:]).get("xyz", [:]).cheese = 123

        assert map.bar.xyz.cheese == 123
        assert map.size() == 2
    }

    String getCmd() {
        def cmd = "ls -l"
        if (System.properties.'os.name'.contains('Win')) {
            cmd = "cmd /c dir"
        }
        return cmd
    }

    void testExecuteCommandLineProcessUsingAString() {

        println "executing command: ${cmd}"

        def process = cmd.execute()

        // lets have an easier way to do this!
        def count = 0

        println "Read the following lines..."

        /** @todo we should simplify the following line!!! */
        new InputStreamReader(process.in).eachLine { line ->
            println line
            ++count
        }
        println ""

        process.waitFor()
        def value = process.exitValue()
        println "Exit value of command line is ${value}"

        assert count > 1
    }

    /*
    void testExecuteCommandLineProcessAndUseWaitForOrKill_FAILS_ON_WINDOWS() {
        if (System.properties.'os.name'.contains('Windows') && notYetImplemented()) return

        println "executing command: ${cmd}"

        def process = cmd.execute()

        process.consumeProcessOutput()
        process.waitForOrKill(1000)
        def value = process.exitValue()
        println "Exit value of command line is ${value}"

        process = cmd.execute()

        process.consumeProcessOutput()
        process.waitForOrKill(10) // This fails on RLW's workstation with parameter 1, >=8 is required.
        value = process.exitValue()
        println "Exit value of command line is ${value}"
        
    }
    */
    
    void testExecuteCommandLineUnderWorkingDirectory_FAILS() { if (notYetImplemented()) return

        def envp = java.util.Array.newInstance(String, 0)
        def workDir = new File(".")

        println "executing command: ${cmd} under the directory ${workDir.canonicalPath}"

        def process = cmd.execute(envp, workDir)

        // lets have an easier way to do this!
        def count = 0

        println "Read the following lines under the directory ${workDir} ..."

        /** @todo we should simplify the following line!!! */
        new InputStreamReader(process.in).eachLine { line ->
            println line
            ++count
        }
        println ""

        process.waitFor()
        def value = process.exitValue()
        println "Exit value of command line is ${value}"

        assert count > 1
    }
    
    void testDisplaySystemProperties() {
        println "System properties are..."
        def properties = System.properties
        def keys = properties.keySet().sort()
        for (k in keys) {
            println "${k} = ${properties[k]}"
        }
    }

    void testMax() {
        assert [-5, -3, -1, 0, 2, 4].max{ it * it } == -5
    }

    void testMin() {
        assert [-5, -3, -1, 0, 2, 4].min{ it * it } == 0
    }
    
    void testSort() {
        assert [-5, -3, -1, 0, 2, 4].sort { it*it } == [0, -1, 2, -3, 4, -5]
    }

    void testReplaceAllClosure() {
        assert "1 a 2 b 3 c 4".replaceAll("\\p{Digit}") { it * 2 } == "11 a 22 b 33 c 44"
    }

    void testObjectSleep(){
        long start = System.currentTimeMillis()
        sleep 1000
        long slept = System.currentTimeMillis() - start
        long epsilon = 100
        assertTrue("should have slept >= 1s but was ${slept}ms", slept >= 1000 - epsilon)
    }

    void testObjectSleepInterrupted(){
        def interruptor = new groovy.TestInterruptor(Thread.currentThread())
        new Thread(interruptor).start()
        long start = System.currentTimeMillis()
        sleep 1000
        long slept = System.currentTimeMillis() - start
        long epsilon = 100
        assertTrue("should have slept >= 1s but was ${slept}ms", slept >= 1000-epsilon)
    }
    void testObjectSleepWithOnInterruptHandler(){
        def log = ''
        def interruptor = new groovy.TestInterruptor(Thread.currentThread())
        new Thread(interruptor).start()
        long start = System.currentTimeMillis()
        sleep(1000){ log += it.toString() }
        long slept = System.currentTimeMillis() - start
        assert slept < 1000, "should have been interrupted but slept ${slept}ms > 2s"
        assertEquals 'java.lang.InterruptedException: sleep interrupted', log.toString()
    }

    void testObjectIdentity() {
        def a = new Object()
        def b = a
        assert a.is(b)
        assert ! a.is(null)
        assert ! 1.is(2)
        // naive impl would fall for this trap
        assert ! new WackyHashCode().is(new WackyHashCode())
    }

    void testGroupByList() {
        def expected = [Integer: [1, 2], String: ["a", "b"], BigDecimal: [3.5, 4.6]]
        def list = [1, "a", 2, "b", 3.5, 4.6]
        def result = list.groupBy{ it.class }
        assert [1, 2] == result[Integer]
        assert ["a", "b"] == result[String]
        assert [3.5, 4.6] == result[BigDecimal]
        assert 3 == result.size()
    }

/* Removed for 1.0, to be discussed for 1.1
    void testGroupByMap() {
		def expectedKeys = [Integer: [1, 3], String: [2, 4], BigDecimal: [5, 6]]
		def expectedVals = [Integer: [1, 2], String: ["a", "b"], BigDecimal: [3.5, 4.6]]
		def map = [1:1, 2:"a", 3:2, 4:"b", 5:3.5, 6:4.6]
		def result = map.groupBy{ it.value.class }
		assert expectedKeys.Integer == result[Integer].collect{it.key}
		assert expectedVals.Integer == result[Integer].collect{it.value}
		assert expectedKeys.String == result[String].collect{it.key}
		assert expectedVals.String == result[String].collect{it.value}
		assert expectedKeys.BigDecimal == result[BigDecimal].collect{it.key}
		assert expectedVals.BigDecimal == result[BigDecimal].collect{it.value}
		assert 3 == result.size()
    }
*/
    
    def leftCol  = ["2"]
    def rightCol = ["1","2","3"]

    void testList() {
      def lst  = [] as LinkedList
      doIt(lst)
    }

    void testSet() {
      def set  = [] as HashSet 
      doIt(set)
    }
                    
    void testVector() {
      def vctr  = [] as Vector
      doIt(vctr)
    }

    void doIt(col) {
      col.clear();
      col.addAll(leftCol);
      // not really concerned about  correctness, rather that the method can be called, however..
      assert col.intersect(rightCol) == ["2"]
    }
}

class WackyHashCode {
    int hashCode(){ return 1;}
}

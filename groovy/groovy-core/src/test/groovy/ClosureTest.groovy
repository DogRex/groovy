/** 
 * Tests Closures in Groovy
 * 
 * @author <a href="mailto:james@coredevelopers.net">James Strachan</a>
 * @version $Revision$
 */
class ClosureTest extends GroovyTestCase {

    def count

    void testSimpleBlockCall() {
        count = 0

        def block = {owner-> owner.incrementCallCount() }

        assertClosure(block)
        assert count == 1

        assertClosure({owner-> owner.incrementCallCount() })
        assert count == 2
    }

    void testVariableLengthParameterList() {

        def c1 = {Object[] args -> args.each{count += it}}

        count = 0
        c1(1, 2, 3)
        assert count == 6

        count = 0
        c1(1)
        assert count == 1

        count = 0
        c1([1, 2, 3] as Object[])
        assert count == 6

        def c2 = {a, Object[] args -> count += a; args.each{count += it}}

        count = 0
        c2(1, 2, 3)
        assert count == 6

        count = 0
        c2(1)
        assert count == 1

        count = 0
        c2(1, [2, 3] as Object[])
        assert count == 6
    }

    void testBlockAsParameter() {
        count = 0

        callBlock(5, {owner-> owner.incrementCallCount() })
        assert count == 6

        callBlock2(5, {owner-> owner.incrementCallCount() })
        assert count == 12
    }
  
    void testMethodClosure() {
        def block = this.&incrementCallCount

        count = 0

        block.call()

        assert count == 1

        block = System.out.&println

        block.call("I just invoked a closure!")
    }
  
    def incrementCallCount() {
        //System.out.println("invoked increment method!")
        count = count + 1
    }

    def assertClosure(Closure block) {
        assert block != null
        block.call(this)
    }

    protected void callBlock(Integer num, Closure block) {
        for ( i in 0..num ) {
            block.call(this)
        }
    }

    protected void callBlock2(num, block) {
        for ( i in 0..num ) {
            block.call(this)
        }
    }


    int numAgents = 4
    boolean testDone = false

    void testIntFieldAccess() {
        def agents = new ArrayList();
        numAgents.times {
            TinyAgent btn = new TinyAgent()
            testDone = true
            btn.x = numAgents
            agents.add(btn)
        }
        assert agents.size() == numAgents
    }

    void testWithIndex() {
        def str = ''
        def sum = 0
        ['a','b','c','d'].eachWithIndex { item, index -> str += item; sum += index }
        assert str == 'abcd' && sum == 6
    }
}

public class TinyAgent {
    int x
}


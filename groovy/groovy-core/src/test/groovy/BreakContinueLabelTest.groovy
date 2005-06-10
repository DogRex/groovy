/** 
 * @author Dierk Koenig
 */
class BreakContinueLabelTest extends GroovyTestCase {

    void testDeclareSimpleLabel() {
        label_1: assert true
        // todo: make this pass
        // label_2:
        // assert true
    }
    void testBreakLabelInSimpleForLoop() {
        label_1: for (i in [1]) {
            break label_1
            assert false
        }
    }
    // todo: make this pass
    void todo_testBreakLabelInNestedForLoop() {
        label: for (i in [1]) {
            for (j in [1]){
                break label
                assert false, 'did not break inner loop'
            }
            assert false, 'did not break outer loop'
        }
    }

    void testUnlabelledBreakInNestedForLoop() {
        reached = false
        for (i in [1]) {
            for (j in [1]){
                break
                assert false, 'did not break inner loop'
            }
            reached = true
        }
        assert reached, 'must not break outer loop'
    }

    void testBreakLabelInSimpleWhileLoop() {
        label_1: while (true) {
            break label_1
            assert false
        }
    }
    // todo: make this pass
    void todo_testBreakLabelInNestedWhileLoop() {
        count = 0
        label: while (count < 1) {
            count++
            while (true){
                break label
                assert false, 'did not break inner loop'
            }
            assert false, 'did not break outer loop'
        }
    }
    // todo: make this pass
    void todo_testBreakLabelInNestedMixedForAndWhileLoop() {
        count = 0
        label_1: while (count < 1) {
            count++
            for (i in [1]){
                break label_1
                assert false, 'did not break inner loop'
            }
            assert false, 'did not break outer loop'
        }
        label_2: for (i in [1]) {
            while (true){
                break label_2
                assert false, 'did not break inner loop'
            }
            assert false, 'did not break outer loop'
        }
    }

    void testUnlabelledContinueInNestedForLoop() {
        log = ''
        for (i in [1,2]) {
            log += i
            for (j in [3,4]){
                if (j==3) continue
                log += j
            }
        }
        assertEquals '1424',log
    }
    void testContinueLabelInNestedForLoop() {
        log = ''
        label: for (i in [1,2]) {
            log += i
            for (j in [3,4]){
                if (j==4) continue label
                log += j
            }
        }
        assertEquals '1323',log
    }
}
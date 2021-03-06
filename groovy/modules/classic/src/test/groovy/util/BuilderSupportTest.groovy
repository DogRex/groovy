package groovy.util

import groovy.lang.MissingMethodException

/**
   Testing BuilderSupport and reveal how calling
   methods on it result in implementation callbacks.
   Using the SpoofBuilder (see below) to call it in various ways
   and analyze the "spoofed" logs.
   This is especially useful when designing subclasses of BuilderSupport.
   @author Dierk Koenig
**/


class BuilderSupportTest extends GroovyTestCase{
   void testSimpleNode() {
      b = new SpoofBuilder()
      assert b.log == []
      node = b.foo()
      assert b.log == ['create_with_name','foo','node_completed',null, node]
   }

   void testSimpleNodeWithValue() {
      b = new SpoofBuilder()
      node = b.foo('value')
      assert b.log == ['create_with_name_and_value','foo','value', 'node_completed',null,node]
   }

   void testSimpleNodeWithOneAttribute() {
      b = new SpoofBuilder()
      node = b.foo(name:'value')
      assert b.log == [
         'create_with_name_and_map','foo', 'name','value', 'node_completed',null,'x']
   }

   void testSimpleNodeWithClosure() {
      b = new SpoofBuilder()
      b.foo(){
         b.bar()
      }
      assert b.log == [
         'create_with_name','foo',
            'create_with_name','bar',
         'set_parent', 'x', 'x',
            'node_completed','x','x',
         'node_completed',null,'x']
   }

   void testSimpleNodeWithOneAttributeAndValue() {
      b = new SpoofBuilder()
      node = b.foo(bar:'baz', 'value')
      assert b.log == ['create_with_name_map_and_value', 'foo', 'bar', 'baz','value', 'node_completed',null,node]
   }

   void testSimpleNodeWithValueAndOneAttribute() {
      b = new SpoofBuilder()
      node = b.foo('value', bar:'baz')
      assert b.log == ['create_with_name_map_and_value', 'foo', 'bar', 'baz','value', 'node_completed',null,node]
   }

   void testSimpleNodeWithOneAttributeAndValueAndClosure() {
      b = new SpoofBuilder()
      node = b.foo(bar:'baz', 'value') { 1 }
      assert b.log == ['create_with_name_map_and_value', 'foo', 'bar', 'baz','value', 'node_completed',null,node]
   }

   void testSimpleNodeWithValueAndOneAttributeAndClosure() {
      b = new SpoofBuilder()
      node = b.foo('value', bar:'baz') { 1 }
      assert b.log == ['create_with_name_map_and_value', 'foo', 'bar', 'baz','value', 'node_completed',null,node]
   }

   void testSimpleNodeTwoValues() {
      b = new SpoofBuilder()
      shouldFail(MissingMethodException, {node = b.foo('arg1', 'arg2')})
   }

   void testSimpleNodeTwoValuesClosure() {
      b = new SpoofBuilder()
      shouldFail(MissingMethodException, {node = b.foo('arg1', 'arg2') { 1 } })
   }

   void testSimpleNodeThreeValues() {
      b = new SpoofBuilder()
      shouldFail(MissingMethodException, {node = b.foo('arg1', 'arg2', 'arg3') })
   }

   void testSimpleNodeFourValues() {
      b = new SpoofBuilder()
      shouldFail(MissingMethodException, {node = b.foo('arg1', 'arg2', 'arg3', 'arg4') })
   }
   
   void testNodeInClosureFourValues() {
      b = new SpoofBuilder()
      shouldFail(MissingMethodException, {node = b.foo() { b.baz('arg1', 'arg2', 'arg3', 'arg4') }})
   }
}

/**
   The SpoofBuilder is a sample instance of the abstract BuilderSupport class 
   that does nothing but logging how it was called, returning 'x' for each node.
**/
class SpoofBuilder extends BuilderSupport{
    log = []
    protected void setParent(Object parent, Object child){
        log << "set_parent"
        log << parent
        log << child
    }
    protected Object createNode(Object name){
        log << 'create_with_name'
        log <<  name
        return 'x'
    }
    protected Object createNode(Object name, Object value){
        log << 'create_with_name_and_value'
        log << name
        log << value
        return 'x'
    }
    protected Object createNode(Object name, Map attributes){
        log << 'create_with_name_and_map'
        log << name
        attributes.each{entry| log << entry.key; log << entry.value}
        return 'x'
    }
    protected Object createNode(Object name, Map attributes, Object value){
        log << 'create_with_name_map_and_value'
        log << name
        attributes.each{entry| log << entry.key; log << entry.value}
        log << value
        return 'x'
    }
    protected void nodeCompleted(Object parent, Object node) {
        log << 'node_completed'
        log << parent
        log << node
    }
}

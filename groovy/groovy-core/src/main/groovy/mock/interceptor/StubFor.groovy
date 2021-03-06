package groovy.mock.interceptor

/**
    Facade over the Stubbing details.
    A Stub's expectation is sequence independent and use of verify() is left to the user.
    @See MockFor.
    @author Dierk Koenig
*/

class StubFor {

    MockProxyMetaClass proxy
    Demand demand
    def expect

    StubFor(Class clazz) {
        proxy = MockProxyMetaClass.make(clazz)
        demand = new Demand()
        expect = new LooseExpectation(demand)
        proxy.interceptor = new MockInterceptor(expectation: expect)
    }

    void use(Closure closure) {
        proxy.use closure
    }

    void use(GroovyObject obj, Closure closure) {
        proxy.use obj, closure
    }
}
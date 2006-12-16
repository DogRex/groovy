import groovy.net.soap.SoapServer
import groovy.net.soap.SoapClient

//
// Create a SoapServer on localhost using port 6969.
// Register serice represented by by the Groovy script MathService.groovy
//
class TestList extends GroovyTestCase {

    void testSoapClient() {
        def server = new SoapServer("localhost", 6980)
        server.setNode("SoftwareRepository")
        System.out.println("start Server & run tests")
        server.start()

        sleep 2000

        try {
            def proxy = new SoapClient("http://localhost:6980/SoftwareRepositoryInterface?wsdl")
            def result = proxy.list()
            assert (result.size() == 3.0)
            assert (result[1] == "Groovy-1.O-RC1")

            result = proxy.priceList()
            assert (result[2] == 3.0)
        } finally {
            server.stop()
        }
    }
}

import groovy.net.soap.SoapClient

import org.codehaus.xfire.XFire
import org.codehaus.xfire.XFireFactory
import org.codehaus.xfire.server.http.XFireHttpServer
import org.codehaus.xfire.service.Service
import org.codehaus.xfire.service.invoker.ObjectInvoker
import org.codehaus.xfire.service.binding.ObjectServiceFactory

import StringsServiceImpl
import StringsService

//
// Create a SoapClient using http://localhost:8090/PersonService?wsdl as WSDL
// then it queries the method findPerson which returns one Person object
// 
class Test4 extends GroovyTestCase {

    void testSoapClient() {
        def serviceFactory = new ObjectServiceFactory()
        def service = serviceFactory.create(StringsService.class)
        // Set the implementation class
        service.setProperty(ObjectInvoker.SERVICE_IMPL_CLASS, StringsServiceImpl.class)
        
        // Register the service in the ServiceRegistry
        def xfire = XFireFactory.newInstance().getXFire()
        xfire.getServiceRegistry().register(service)
        
        // Start the HTTP server
        def server = new XFireHttpServer()
        server.setPort(6969)
        System.out.println("start Server & run tests")
        server.start()

        sleep 2000

        try {
            def proxy = new SoapClient("http://localhost:6969/StringsService?wsdl")

            def result = proxy.concat("Guillaume_", "Alleon aka tog")
            assert (result == "Guillaume_Alleon aka tog")

            proxy.foo("this is the content !", "filename.txt")

        } finally {
            server.stop()
        }
    }
}

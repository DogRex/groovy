package groovy.xml

/**
 * Test the building of namespaced XML using GroovyMarkup
 */
class NamespaceNodeTest extends TestXmlSupport {
    
    void testTree_FAILS() { if (notYetImplemented()) return
        def builder = NodeBuilder.newInstance()
        def xmlns = new NamespaceBuilder(builder)
        
        def xsd = xmlns.namespace('http://www.w3.org/2001/XMLSchema', 'xsd')
        
        def root = xsd.schema(xmlns:['foo':'http://someOtherNamespace']) {
          annotation {
              documentation("Purchase order schema for Example.com.")
              //documentation(xmlns=[xml.lang:'en']) ["Purchase order schema for Example.com."]
          }
          element(name:'purchaseOrder', type:'PurchaseOrderType')
          element(name:'comment', type:'xsd:string')
          complexType(name:'PurchaseOrderType') {
            sequence {
              element(name:'shipTo', type:'USAddress')
              element(name:'billTo', type:'USAddress')
              element(minOccurs:'0', ref:'comment')
              element(name:'items', type:'Items')
            }
            attribute(name:'orderDate', type:'xsd:date')
          }
          complexType(name:'USAddress') {
            sequence {
              element(name:'name', type:'xsd:string')
              element(name:'street', type:'xsd:string')
              element(name:'city', type:'xsd:string')
              element(name:'state', type:'xsd:string')
              element(name:'zip', type:'xsd:decimal')
            }
            attribute(fixed:'US', name:'country', type:'xsd:NMTOKEN')
          }
          complexType(name:'Items') {
            sequence {
              element(maxOccurs:'unbounded', minOccurs:'0', name:'item') {
                complexType {
                  sequence {
                    element(name:'productName', type:'xsd:string')
                    element(name:'quantity') {
                      simpleType {
                        restriction(base:'xsd:positiveInteger') {
                          maxExclusive(value:'100')
                        }
                      }
                    }
                    element(name:'USPrice', type:'xsd:decimal')
                    element(minOccurs:'0', ref:'comment')
                    element(minOccurs:'0', name:'shipDate', type:'xsd:date')
                  }
                  attribute(name:'partNum', type:'SKU', use:'required')
                }
              }
            }
          }
          /* Stock Keeping Unit, a code for identifying products */
          simpleType(name:'SKU') {
            restriction(base:'xsd:string') {
              pattern(value:'\\d{3}-[A-Z]{2}')
            }
          }
        }        
        assert root != null
        
        assertGPaths(root)
    }
    
    void assertGPaths(Node root) {
    		Namespace xsd = new Namespace('http://www.w3.org/2001/XMLSchema', 'xsd')

	    def children = root.children()
	    println "has children $children"
	        		
    		def name = root.name()
    		println "name is of type ${name.getClass()} with value $name"

		root.children().each { println "has a child with name ${it.name()} and content $it" }
		    		
    		def foo = xsd.annotation
    		println "qname is $foo"
    		println "qname url is $foo.namespaceURI"
    		println "qname prefix is $foo.prefix"
    		println "qname localPart is $foo.localPart"
    		
    		def a = root[xsd.annotation]
    		println "Found results $a"
    		
    		
    		def e = root[xsd.annotation][xsd.documentation]
    		
    		String text = e.text()
    		println "Found element: $e with text: $text"
    		assert text == "Purchase order schema for Example.com."
    }
}
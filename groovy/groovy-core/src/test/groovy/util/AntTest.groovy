package groovy.util

import java.io.File
import org.apache.tools.ant.Project
import org.apache.tools.ant.ProjectHelper
import groovy.xml.NamespaceBuilder


class AntTest extends GroovyTestCase {
    
    void testAnt() {
        def ant = new AntBuilder()

        // lets just call one task
        ant.echo("hello")
        
        // here"s an example of a block of Ant inside GroovyMarkup
        ant.sequential {
            echo("inside sequential")
            
            def myDir = "target/AntTest/"
            
            mkdir(dir:myDir) 
            copy(todir:myDir) {
                fileset(dir:"src/test") {
                    include(name:"**/*.groovy")
                }
            }
            
            echo("done")
        }
        
        // now lets do some normal Groovy again
        def file = new File("target/AntTest/groovy/util/AntTest.groovy")
        assert file.exists()
    }
    
    void testFileIteration() {
        def ant = new AntBuilder()
        
        // lets create a scanner of filesets
        def scanner = ant.fileScanner {
            fileset(dir:"src/test") {
                include(name:"**/Ant*.groovy")
            }
        }
        
        // now lets iterate over 
        def found = false
        for (f in scanner) {
            println("Found file ${f}")
            
            found = true
            
            assert f instanceof File
            assert f.name.endsWith(".groovy")
        }
        assert found
    }
    
    void testJunitTask() {
        def ant = new AntBuilder()
        
        ant.junit {
            test(name:'groovy.util.SomethingThatDoesNotExist')
        }
    }
    
    void testPathBuilding() {
        def ant = new AntBuilder()
        
        def value = ant.path {
            fileset(dir:"xdocs") {
                include(name:"*.wiki")
            }
        }

        assert value != null
        assertEquals org.apache.tools.ant.types.Path, value.getClass()
    }

    void testTaskContainerExecutionSequence() {
        SpoofTaskContainer.getSpoof().length = 0

        def antFile = new File("src/test/groovy/util/AntTest.xml")
        assertTrue "Couldn't find ant test script", antFile.exists()

		// run it with ant, to be sure that our assumptions are correct
		def project = new Project()
		project.init()
		ProjectHelper.projectHelper.parse(project, antFile)
		project.executeTarget(project.defaultTarget);
		
        def expectedSpoof =
"""SpoofTaskContainer ctor
in addTask
configuring UnknownElement
SpoofTask ctor
begin SpoofTaskContainer execute
begin SpoofTask execute
tag name from wrapper: spoof
attributes map from wrapper: ["foo":"123"]
param foo: 123
end SpoofTask execute
end SpoofTaskContainer execute
"""
		println SpoofTaskContainer.getSpoof().toString()
        assertEquals expectedSpoof, SpoofTaskContainer.getSpoof().toString()
        SpoofTaskContainer.spoof.length = 0

        def ant = new AntBuilder()
        def PATH = 'task.path'

		// and now run it with the AntBuilder        
        ant.path(id:PATH) {ant.pathelement(location:'classes')}
        ['spoofcontainer': SpoofTaskContainer, 'spoof': SpoofTask].each{ pair ->
            ant.taskdef(name:pair.key, classname: pair.value.name, classpathref: PATH)
        }
        ant.spoofcontainer(){
            ant.spoof(foo: 123)
        }
        assertEquals expectedSpoof, SpoofTaskContainer.getSpoof().toString()
        
        // now run it with AntBuilder using Namespaces (test for GROOVY-1070)
        def antNS = new AntBuilder()
        SpoofTaskContainer.resetSpoof()

		// and now run it with the AntBuilder        
        antNS.path(id:PATH) {antNS.pathelement(location:'classes')}
        ['spoofcontainer': SpoofTaskContainer, 'spoof': SpoofTask].each{ pair ->
            antNS.taskdef(name:pair.key, classname: pair.value.name, classpathref: PATH, 
                          uri: 'testNS')
        }
		def testNS = NamespaceBuilder.newInstance(antNS,"testNS","testNSprefix");
        testNS.spoofcontainer(){
            testNS.spoof(foo: 123)
        }
        assertEquals expectedSpoof, SpoofTaskContainer.getSpoof().toString()
    }

    /** Checks that we can access dynamically (through Ant's property task) defined properties in Groovy scriptlets */
    void testDynamicProperties() {
        def antBuilder = new AntBuilder()

        antBuilder.property(name: "testProp1", value: "TEST 1")
        antBuilder.taskdef(name:"groovy", classname:"org.codehaus.groovy.ant.Groovy")
        antBuilder.groovy("""
            ant.property(name: "testProp2", value: "TEST 2")

            assert properties.testProp1 == project.properties.testProp1
            assert properties.testProp2 == project.properties.testProp2
        """)
    }
    
    /**
    * Tests that the AntBuilder can handle conditions (conditions aren't tasks)
    * (test for GROOVY-824)
    */
    void testCondition() {
        def ant = new AntBuilder()
        ant.condition(property: "containsHi") {
        	contains([string: "hi", substring: "hi"])
        }
        assertEquals "true", ant.project.properties["containsHi"]

        ant.condition(property: "equalsHi", else: "false") {
        	Equals([arg1: "hi", arg2: "bye"])
        }
        assertEquals "false", ant.project.properties["equalsHi"]
    }
}

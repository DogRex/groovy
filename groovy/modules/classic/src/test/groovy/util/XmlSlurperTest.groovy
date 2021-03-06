package org.codehaus.groovy.sandbox.util

class XmlSlurperTest extends GroovyTestCase {
    
    void testXmlParser() {
        text = """
<characters>
    <character id="1" name="Wallace">
    	<likes>cheese</likes>
    </character>
    <character id="2" name="Gromit">
	    <likes>sleep</likes>
    </character>
</characters>
"""
        
        node = new XmlSlurper().parseText(text);
        
        assert node != null
        assert node.children().size() == 2 : "Children ${node.children()}"
        
        characters = node.character
        
        for (c in characters) {
            println c['@name']
        }
        
        assert characters.size() == 2
        
        assert node.character.likes.size() == 2 : "Likes ${node.character.likes}"
        
        // lets find Gromit
        gromit = node.character.find { it['@id'] == '2' }
        assert gromit != null : "Should have found Gromit!"
        assert gromit['@name'] == "Gromit"
        
        
        // lets find what Wallace likes in 1 query
        answer = node.character.find { it['@id'] == '1' }.likes[0].text()
        assert answer == "cheese"
    }
}

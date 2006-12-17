package org.codehaus.groovy.eclipse.test;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{
    public static Test suite()
    {
        final TestSuite suite = new TestSuite( "Test for " + AllTests.class.getPackage().getName() );
        //$JUnit-BEGIN$
        suite.addTestSuite( GroovyMarkerTestCase.class );
        suite.addTestSuite( GroovyPluginTestCase.class );
        suite.addTestSuite( GroovyModelTestCase.class );
        suite.addTestSuite( GroovyContentOutlineTestCase.class );
        //$JUnit-END$
        return suite;
    }
}

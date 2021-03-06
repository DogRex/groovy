/**
 *
 * Copyright 2004 James Strachan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/
package org.codehaus.groovy.antlr;

import groovy.ui.GroovyMain;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.framework.AssertionFailedError;
import junit.framework.Protectable;

import java.io.File;

/**
 * A helper class for making a test suite of a group of Groovy script names
 *
 * @version $Revision$
 */
public class TestSuiteSupport implements Test, Protectable {
    private String fullName;

    public int countTestCases() {
        return 1;
    }

    public void run(TestResult result) {
        result.startTest(this);
        result.runProtected(this, this);
        result.endTest(this);
    }

    public void protect() throws Throwable {
        System.out.println("Running test: " + fullName);
        GroovyMain.main(new String[]{fullName});

        // TODO how do we extract any JUnit test failures?
    }

    public String toString() {
        return "testCase for: " + fullName;
    }

    /**
     * Adds the named test scripts within the given directory to the suite
     */
    protected static void addTests(TestSuite suite, String directory, String[] names) {
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            String fullName = directory + name;

            addTest(suite, fullName);
        }
    }

    /**
     * Adds a single test case or recurses into a directory adding all the test cases
     */
    protected static void addTests(TestSuite suite, File file, String[] excludedTests) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File child = files[i];
                addTests(suite, child, excludedTests);
            }
        }
        else {
            String path = file.getPath();
            if (path.endsWith("Test.groovy") || path.endsWith("Bug.groovy") ) {
                // lets check it doesn't end with any of the excluded tests
                for (int i = 0; i < excludedTests.length; i++) {
                    String excludedTest = excludedTests[i];
                    if (path.endsWith(excludedTest)) {
                        return;
                    }
                }
                addTest(suite, path);
            }
        }
    }

    protected static void addTest(TestSuite suite, String fullName) {
        suite.addTest(new GroovyMainTestSupport(fullName));
        /*
        TestSuiteSupport childTest = new TestSuiteSupport();
        childTest.fullName = fullName;
        TestSuite childSuite = new TestSuite(fullName);

        childSuite.addTest(childTest);
        suite.addTest(childSuite);
        */
    }
}

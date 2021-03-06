/*
  $Id$

   Copyright (c) 2004 Jeremy Rayner. All Rights Reserved.

   Jeremy Rayner makes no representations or warranties about
   the fitness of this software for any particular purpose,
   including the implied warranty of merchantability.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.javanicus.bpwj;

import junit.framework.*;
import sjm.parse.*;
import sjm.parse.tokens.*;

public class QualifiedIdentifierTest extends TestCase {
    private ParserFacade parser;

    public void setUp() {
        parser = new ParserFacade(new JavaParser().qualifiedIdentifier());
        System.out.println("parser:" + parser);
    }

    public void testEmptyExpression() {
        assertNull(parser.parse(""));
    }

    public void testValid() {
        assertFalse(parser.parse("foo").hasMoreElements());
        assertFalse(parser.parse("foo.bar").hasMoreElements());
        assertFalse(parser.parse("foo.bar.mooky").hasMoreElements());
    }

    public void testInvalid() {
        assertNull(parser.parse("foo*bar"));
        assertNull(parser.parse("123.456"));
    }
    public void testErrorMessages() {
        try {
            Assembly a = parser.parse("foo*bar");
            //todo fail("invalid " + a);
        } catch (Exception e) {
            assertTrue(e.getMessage().indexOf("Expected: .") > -1);
        }
    }

}

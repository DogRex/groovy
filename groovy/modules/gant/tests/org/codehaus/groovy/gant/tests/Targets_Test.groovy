//  Gant -- A Groovy build tool based on scripting Ant tasks
//
//  Copyright © 2006 Russel Winder <russel@russel.org.uk>
//
//  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
//  compliance with the License. You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software distributed under the License is
//  distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
//  implied. See the License for the specific language governing permissions and limitations under the
//  License.

package org.codehaus.groovy.gant.tests

import org.codehaus.groovy.gant.infrastructure.Gant

/**
 *  A test to ensure that the target listing works. 
 *
 *  @author Russel Winder <russel@russel.org.uk>
 *  @version $Revision$ $Date$
 */
final class Targets_Test extends GantTestCase {
  final coreScript = '''
task ( something : "Do something." ) { }
task ( somethingElse : "Do something else." ) { }
'''
  void testSomething ( ) {
    System.setIn ( new StringBufferInputStream ( coreScript ) )
    Gant.main ( [ '-T' ,  '-f' ,  '-' ] as String[] )
    assertEquals ( '''gant something  --  Do something.
gant somethingElse  --  Do something else.
''' , output.toString ( ) ) 
  }
  void testSomethingAndClean ( ) {
    System.setIn ( new StringBufferInputStream ( 'includeTargets << new File ( "source/org/codehaus/groovy/gant/targets/clean.gant" )\n' + coreScript ) )
    Gant.main ( [ '-T' ,  '-f' ,  '-' ] as String[] )
    assertEquals ( '''gant clean  --  Action the cleaning.
gant clobber  --  Action the clobbering.  Do the cleaning first.
gant something  --  Do something.
gant somethingElse  --  Do something else.
''' , output.toString ( ) ) 
  }
  void testGStrings ( ) {
    System.setIn ( new StringBufferInputStream ( '''
def theWord = 'The Word'
task ( something : "Do ${theWord}." ) { }
task ( somethingElse : "Do ${theWord}." ) { }
''' ) )
    Gant.main ( [ '-T' ,  '-f' ,  '-' ] as String[] )
    assertEquals ( '''gant something  --  Do The Word.
gant somethingElse  --  Do The Word.
''' , output.toString ( ) ) 
  }
  void testDefaultSomething ( ) {
    System.setIn ( new StringBufferInputStream ( '''
task ( something : "Do something." ) { }
task ( somethingElse : "Do something else." ) { }
task ( 'default' : 'Default is something.' ) { something ( ) }
''' ) )
    Gant.main ( [ '-T' ,  '-f' ,  '-' ] as String[] )
    assertEquals ( '''gant -- Default is something.
gant something  --  Do something.
gant somethingElse  --  Do something else.
''' , output.toString ( ) ) 
  }  
}

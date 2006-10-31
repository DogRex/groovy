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
final class DryRun_Test extends GantTestCase {
  void setUp ( ) {
    super.setUp ( )
    System.setIn ( new StringBufferInputStream ( '''
task ( something : "Do something." ) { Ant.echo ( message : "Did something." ) }
task ( somethingElse : "Do something else." ) { Ant.echo ( message : "Did something else." ) }
''' ) )  }
    
  void testMissingDefault ( ) {
    Gant.main ( [ '-n' ,  '-f' ,  '-'  ] as String[] )
    assertEquals ( 'Target default does not exist.\n' , output.toString ( ) )
  }
  void testMissingNamedTarget ( ) {
    Gant.main ( [ '-n' ,  '-f' ,  '-'  , 'blah'] as String[] )
    assertEquals ( " [property] environment : 'environment'\nTarget blah does not exist.\n" , output.toString ( ) ) 
  }
  void testSomething ( ) {
    Gant.main ( [ '-n' ,  '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( " [property] environment : 'environment'\n     [echo] message : 'Did something.'\n" , output.toString ( ) ) 
  }
  void testSomethingElse ( ) {
    Gant.main ( [ '-n' ,  '-f' ,  '-'  , 'somethingElse'] as String[] )
    assertEquals ( " [property] environment : 'environment'\n     [echo] message : 'Did something else.'\n" , output.toString ( ) ) 
  }
}

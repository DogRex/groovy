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

import gant.Gant

/**
 *  A test to ensure that the target listing works. 
 *
 *  @author Russel Winder <russel@russel.org.uk>
 *  @version $Revision$ $Date$
 */
final class ToolMetaClassLookup_Test extends GantTestCase {
  void setUp ( ) {
    super.setUp ( )
    System.setIn ( new StringBufferInputStream ( '''
includeTool << gant.tools.Subdirectories
task ( something : 'Do something.' ) { Subdirectories.runSubprocess ( "echo yes" , new File ( "source" ) ) }
task ( "default" : "Default is something." ) { something ( ) }
''' ) )  }

  void testDefault ( ) {
    Gant.main ( [ '-f' ,  '-'  ] as String[] )
    assertEquals ( 'yes\n' , output.toString ( ) ) 
  }
  void testBlah ( ) {
    Gant.main ( [ '-f' ,  '-'  , 'blah'] as String[] )
    assertEquals ( 'Target blah does not exist.\n' , output.toString ( ) ) 
  }
  void testSomething ( ) {
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'yes\n' , output.toString ( ) ) 
  }
}

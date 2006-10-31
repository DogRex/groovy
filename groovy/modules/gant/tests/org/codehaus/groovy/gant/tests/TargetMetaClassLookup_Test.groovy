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
 *  A test to ensure that the targets method lookup works. 
 *
 *  @author Russel Winder <russel@russel.org.uk>
 *  @version $Revision$ $Date$
 */
final class TargetMetaClassLookup_Test extends GantTestCase {
  void setUp ( ) {
    super.setUp ( )
    System.setIn ( new StringBufferInputStream ( '''
includeTargets << new File ( 'source/org/codehaus/groovy/gant/targets/clean.gant' )
cleanPattern << "**/*~"
task ( something : "Do something." ) { Ant.echo ( message : "Did something." ) }
task ( "default" : "Default is something." ) { something ( ) }
''' ) )  }
    
  //  It seems that the same org.codehaus.groovy.gant.targets.Clean instance is used for all
  //  tests in this class whuich is a bit sad becaus it means that there is an accumulatiopn of
  //  **/*~ patterns, 1 for each test method as addCleanPattern gets executed for each test.  So
  //  it is crucial to know when testClean is run to know what the output will be.  Put it first
  //  in the hope it will be run first.

  void testClean ( ) {
    //  Have to do this dry run or the result is indeterminate.
    Gant.main ( [ '-n' , '-f' ,  '-'  , 'clean' ] as String[] )
    assertEquals ( '''   [delete] quiet : 'false'
  [fileset] defaultexcludes : 'no' , includes : '**/*~' , dir : '.'
''' , output.toString ( ) )
  }
  void testDefault ( ) {
    Gant.main ( [ '-f' ,  '-'  ] as String[] )
    assertEquals ( " [property] environment : 'environment'\n     [echo] message : 'Did something.'\n" , output.toString ( ) ) 
  }
  void testBlah ( ) {
    Gant.main ( [ '-f' ,  '-'  , 'blah' ] as String[] )
    assertEquals ( " [property] environment : 'environment'\nTarget blah does not exist.\n" , output.toString ( ) ) 
  }
  void testSomething ( ) {
    Gant.main ( [ '-f' ,  '-'  , 'something' ] as String[] )
    assertEquals ( " [property] environment : 'environment'\n     [echo] message : 'Did something.'\n" , output.toString ( ) ) 
  }
}

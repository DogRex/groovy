//  Gant -- A Groovy build tool based on scripting Ant tasks
//
//  Copyright © 2006 Russel Winder <russel@russel.org.uk>
//
//  This library is free software; you can redistribute it and/or modify it under the terms of
//  the GNU Lesser General Public License as published by the Free Software Foundation; either
//  version 2.1 of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
//  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//  See the GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License along with this
//  library; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
//  Boston, MA 02110-1301 USA

package org.codehaus.groovy.gant.tests

import org.codehaus.groovy.gant.infrastructure.Gant

/**
 *  A test to ensure that the target listing works. 
 *
 *  @author Russel Winder
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
    assertEquals ( 'Target blah does not exist.\n' , output.toString ( ) ) 
  }
  void testSomething ( ) {
    Gant.main ( [ '-n' ,  '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( "     [echo] message : 'Did something.'\n" , output.toString ( ) ) 
  }
  void testSomethingElse ( ) {
    Gant.main ( [ '-n' ,  '-f' ,  '-'  , 'somethingElse'] as String[] )
    assertEquals ( "     [echo] message : 'Did something else.'\n" , output.toString ( ) ) 
  }
}

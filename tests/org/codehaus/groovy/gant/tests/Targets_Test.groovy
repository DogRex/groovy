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
final class Targets_Test extends GantTestCase {
  void testSomething ( ) {
    System.setIn ( new StringBufferInputStream ( '''
class build {
  Task something ( ) {
    description ( "Do something." )
  }
  Task somethingElse ( ) {
    description ( "Do something else." )
  }
}
''' ) )
    Gant.main ( [ '-T' ,  '-f' ,  '-' ] as String[] )
    assertEquals ( '''gant something  --  Do something.
gant somethingElse  --  Do something else.
''' , output.toString ( ) ) 
  }
  
  void testSomethingAndClean ( ) {
    System.setIn ( new StringBufferInputStream ( '''
class build {
  build ( ) { includeTargets ( org.codehaus.groovy.gant.targets.Clean ) }
  Task something ( ) {
    description ( "Do something." )
  }
  Task somethingElse ( ) {
    description ( "Do something else." )
  }
}
''' ) )
    Gant.main ( [ '-T' ,  '-f' ,  '-' ] as String[] )
    assertEquals ( '''gant clean  --  Action the cleaning.
gant clobber  --  Action the clobbering.  Does the cleaning first.
gant something  --  Do something.
gant somethingElse  --  Do something else.
''' , output.toString ( ) ) 
  }
}

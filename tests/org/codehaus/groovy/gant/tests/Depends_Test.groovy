//  Gant -- A Groovy build tool based on scripting Ant tasks
//
//  Copyright (C) 2006 Russel Winder <russel@russel.org.uk>
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
 *  A test for the depends processing, i.e. make sure the depends calls the method when appropriate and not
 *  when appropriate.
 *
 *  @author Russel Winder
 *  @version $Revision: 4090 $ $Date: 2006-10-01 16:38:57 +0100 (Sun, 01 Oct 2006) $
 */
final class Depends_Test extends GantTestCase {
  void testNone ( ) {
    System.setIn ( new StringBufferInputStream ( '''
task ( noneDoit : '' ) { println ( 'done.' ) }
task ( noneDoA : '' ) { noneDoit ( ) }
task ( noneDoB : '' ) { noneDoit ( ) }
task ( noneDoC : '' ) { noneDoit ( ) }
task ( noneDoAll : '' ) { noneDoA ( ) ; noneDoB ( ) ; noneDoC ( ) }
''' ) )
    Gant.main ( [ '-f' , '-' , 'noneDoAll' ] as String[] )
    assertEquals ( '''done.
done.
done.
''' , output.toString ( ) ) 
  }
  void testMixed ( ) {
    System.setIn ( new StringBufferInputStream ( '''
task ( mixedDoit : '' ) { println ( 'done.' ) }
task ( mixedDoA : '' ) { depends ( mixedDoit ) }
task ( mixedDoB : '' ) { mixedDoit ( ) }
task ( mixedDoC : '' ) { depends ( mixedDoit ) }
task ( mixedDoAll : '' ) { mixedDoA ( ) ; mixedDoB ( ) ; mixedDoC ( ) }
''' ) )
    Gant.main ( [ '-f' , '-' , 'mixedDoAll' ] as String[] )

    System.err.println ( "testMixed: `" + output + "'" )

    assertEquals ( '''done.
done.
''' , output.toString ( ) ) 
  }
  void testAll ( ) {
    System.setIn ( new StringBufferInputStream ( '''
task ( allDoit : '' ) { println ( 'done.' ) }
task ( allDoA : '' ) { depends ( allDoit ) }
task ( allDoB : '' ) { depends ( allDoit ) }
task ( allDoC : '' ) { depends ( allDoit ) }
task ( allDoAll : '' ) { allDoA ( ) ; allDoB ( ) ; allDoC ( ) }
''' ) )
    Gant.main ( [ '-f' , '-' , 'allDoAll' ] as String[] )

    System.err.println ( "testAll: `" + output + "'" )

    assertEquals ( 'done.\n' , output.toString ( ) ) 
  }
  void testNotClosure ( ) {
    System.setIn ( new StringBufferInputStream ( '''
task ( notClosure : '' ) { depends ( 'notClosure' ) }
''' ) )
    Gant.main ( [ '-f' , '-' , 'notClosure' ] as String[] )
    assertEquals ( 'depends called with non-Closure argument.\n' , output.toString ( ) )
  }

}

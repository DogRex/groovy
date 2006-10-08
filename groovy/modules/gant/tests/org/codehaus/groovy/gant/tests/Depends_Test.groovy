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

//  There is a pitfall here.  Because the ExecutionGantMetaClass uses static data values and because there
//  is only a signle ExecutionGantMetaClass for all the tests in a given test class we have to be carefule
//  using the depends that we do not have cros test method data coupling.  To avoid separate classes just
//  ensure that different mathod namers are used in each test.

final class Depends_Test extends GantTestCase {
  void testNone ( ) {
    System.setIn ( new StringBufferInputStream ( '''
class build {
  Task noneDoit ( ) { println ( 'done.' ) }
  Task noneDoA ( ) { noneDoit ( ) }
  Task noneDoB ( ) { noneDoit ( ) }
  Task noneDoC ( ) { noneDoit ( ) }
  Task noneDoAll ( ) { noneDoA ( ) ; noneDoB ( ) ; noneDoC ( ) }
}
''' ) )
    Gant.main ( [ '-f' , '-' , 'noneDoAll' ] as String[] )
    assertEquals ( '''done.
done.
done.
''' , output.toString ( ) ) 
  }
  void testMixed ( ) {
    System.setIn ( new StringBufferInputStream ( '''
class build {
  Task mixedDoit ( ) { println ( 'done.' ) }
  Task mixedDoA ( ) { depends ( 'mixedDoit' ) }
  Task mixedDoB ( ) { mixedDoit ( ) }
  Task mixedDoC ( ) { depends ( 'mixedDoit' ) }
  Task mixedDoAll ( ) { mixedDoA ( ) ; mixedDoB ( ) ; mixedDoC ( ) }
}
''' ) )
    Gant.main ( [ '-f' , '-' , 'mixedDoAll' ] as String[] )
    assertEquals ( '''done.
done.
''' , output.toString ( ) ) 
  }
  void testAll ( ) {
    System.setIn ( new StringBufferInputStream ( '''
class build {
  Task allDoit ( ) { println ( 'done.' ) }
  Task allDoA ( ) { depends ( 'allDoit' ) }
  Task allDoB ( ) { depends ( 'allDoit' ) }
  Task allDoC ( ) { depends ( 'allDoit' ) }
  Task allDoAll ( ) { allDoA ( ) ; allDoB ( ) ; allDoC ( ) }
}
''' ) )
    Gant.main ( [ '-f' , '-' , 'allDoAll' ] as String[] )
    assertEquals ( 'done.\n' , output.toString ( ) ) 
  }
}

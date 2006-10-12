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
 *  A test to ensure that using standard Groovy functions works.
 *
 *  @author Russel Winder
 *  @version $Revision$ $Date$
 */
final class CallPrint_Test extends GantTestCase {
  void testSystemOutPrintln ( ) {
    System.setIn ( new StringBufferInputStream ( '''
task ( systemOutPrintln : "Do something." ) { System.out.println ( "Hello World" ) }
''' ) )
    Gant.main ( [ '-f' , '-' , 'systemOutPrintln' ] as String[] )
    assertEquals ( '''Hello World
''' , output.toString ( ) ) 
  }
  void testPrintln ( ) {
    System.setIn ( new StringBufferInputStream ( '''
task ( testPrintln : "Do something." ) { println ( "Hello World" ) }
''' ) )
    Gant.main ( [ '-f' , '-' , 'testPrintln' ] as String[] )
    assertEquals ( '''Hello World
''' , output.toString ( ) ) 
  }
  void testMessage ( ) {
    System.setIn ( new StringBufferInputStream ( '''
task ( testMessage : "Do something." ) { message ( 'message' , 'A message.' ) }
''' ) )
    Gant.main ( [ '-f' , '-' , 'testMessage' ] as String[] )
    assertEquals ( '  [message] A message.\n' , output.toString ( ) ) 
  }
}

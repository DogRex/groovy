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
 *  A test to ensure that the target listing works. 
 *
 *  @author Russel Winder
 *  @version $Revision$ $Date$
 */
final class ToolMetaClassLookup_Test extends GantTestCase {
  void setUp ( ) {
    super.setUp ( )
    System.setIn ( new StringBufferInputStream ( '''
class build {
  def build ( ) {
    includeTool ( org.codehaus.groovy.gant.tools.Subdirectories )
  }
  Task something ( ) {
    description ( "Do something." )
    Subdirectories.runSubprocess ( "echo yes" , new File ( "source" ) )
  }
  public Task "default" ( ) {
    description ( "Default is something." )
    something ( )
  }
}
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

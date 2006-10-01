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
 *  @version $Revision: 4087 $ $Date: 2006-10-01 10:15:08 +0100 (Sun, 01 Oct 2006) $
 */
final class TestFileInclude_Test extends GantTestCase {
  def includeFileName = '/tmp/Blah.gant'
  def buildFileTool =  """
class build {
  def build ( ) { includeTool (  '${includeFileName}' ) }
  Task something ( ) { Blah.flob ( ) }
  public Task 'default' ( ) { something ( ) }
}
"""
  def buildFileTargets = buildFileTool.replace ( 'Tool' , 'Targets' ).replace ( 'Blah.flob' , 'flob' )
  TestFileInclude_Test ( ) {
    ( new File ( includeFileName ) ).write('''
import org.apache.tools.ant.Task
class Blah {
  Task flob ( ) { println ( 'flobbed.' ) ; null }
}
''' )
  }
  void testDefaultTools ( ) {
    System.setIn ( new StringBufferInputStream ( buildFileTool ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testFlobTools ( ) {
    System.setIn ( new StringBufferInputStream ( buildFileTool ) )
    Gant.main ( [ '-f' ,  '-'  , 'flob'] as String[] )
    assertEquals ( 'Target flob does not exist.\n' , output.toString ( ) ) 
  }
  void testSomethingTools ( ) {
    System.setIn ( new StringBufferInputStream ( buildFileTool ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testDefaultTargets ( ) {
    System.setIn ( new StringBufferInputStream ( buildFileTargets ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testFlobTargets ( ) {
    System.setIn ( new StringBufferInputStream ( buildFileTargets ) )
    Gant.main ( [  '-f' ,  '-'  , 'flob'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testSomethingTargets ( ) {
    System.setIn ( new StringBufferInputStream ( buildFileTargets ) )
    Gant.main ( [  '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testNoFile ( ) {
    System.setIn ( new StringBufferInputStream ( buildFileTool.replace ( 'Blah' , 'Burble' ) ) )
    try { Gant.main ( [ '-f' ,  '-'  , 'flob'] as String[] ) }
    catch ( RuntimeException re ) { return }
    fail ( 'Should have got a RuntimeException but didn\'t.' )
  }
}

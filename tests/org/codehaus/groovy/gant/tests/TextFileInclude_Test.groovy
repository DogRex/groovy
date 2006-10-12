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
final class TextFileInclude_Test extends GantTestCase {
  def toolClassName = 'Blah'
  def toolFilePath = "/tmp/${toolClassName}.groovy"
  def toolClassText =  """
class ${toolClassName} {
  def ${toolClassName} ( Map environment ) { }
  def flob ( ) { println ( 'flobbed.' ) }
}
"""
  def toolBuildScript =  """
includeTool <<  new File ( '${toolFilePath}' )
task ( something : '' ) { ${toolClassName}.flob ( ) }
task ( 'default' : '' ) { something ( ) }
"""
  def targetsFilePath = '/tmp/targets.gant'
  def targetsScriptText =  '''
task ( flob : '' ) { println ( 'flobbed.' ) }
''' 
  def targetsBuildScript =  """
includeTargets <<  new File ( '${targetsFilePath}' )
task ( something : '' ) { flob ( ) }
task ( 'default' : '' ) { something ( ) }
"""
  TextFileInclude_Test ( ) {
    ( new File ( "${toolFilePath}" ) ).write( toolClassText )
    ( new File ( "${targetsFilePath}" ) ).write( targetsScriptText )
  }
  void testDefaultTools ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScript ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testFlobTools ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScript ) )
    Gant.main ( [ '-f' ,  '-'  , 'flob'] as String[] )
    assertEquals ( 'Target flob does not exist.\n' , output.toString ( ) ) 
  }
  void testSomethingTools ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScript ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testDefaultTargets ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScript ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testFlobTargets ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScript ) )
    Gant.main ( [  '-f' ,  '-'  , 'flob'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testSomethingTargets ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScript ) )
    Gant.main ( [  '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testNoFile ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScript.replace ( 'Blah' , 'Burble' ) ) )
    try { Gant.main ( [ '-f' ,  '-'  , 'flob'] as String[] ) }
    catch ( FileNotFoundException fnfe ) { return }
    fail ( 'Should have got a FileNotFoundException but didn\'t.' )
  }
}

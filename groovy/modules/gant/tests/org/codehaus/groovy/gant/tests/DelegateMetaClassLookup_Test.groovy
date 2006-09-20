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

import groovy.lang.MissingMethodException

import org.codehaus.groovy.gant.infrastructure.Gant

/**
 *  A test to ensure that the target listing works. 
 *
 *  @author Russel Winder
 *  @version $LastChangedRevision$ $LastChangedDate$
 */
final class DelegateMetaClassLookup_Test extends GantTestCase {
  void setUp ( ) {
    super.setUp ( )
    System.setIn ( new StringBufferInputStream ( '''
class build {
  def build ( ) {
    include ( org.codehaus.groovy.gant.targets.Clean )
    addCleanPattern ( "**/*~" )
  }
  Task something ( ) {
    description ( "Do something." )
    ant.echo ( message : "Did something." )
  }
  public Task "default" ( ) {
    description ( "Default is something." )
    something ( )
  }
}
''' ) )  }
    
  //  It seems that the same org.codehaus.groovy.gant.targets.Clean instance is used for all
  //  tests in this class whuich is a bit sad becaus it means that there is an accumulatiopn of
  //  **/*~ patterns, 1 for each test method as addCleanPattern gets executed for each test.  So
  //  it is crucial to know when testClean is run to know what the output will be.  Put it first
  //  in the hope it will be run first.

  void testClean ( ) {
    Gant.main ( [ '-n' ,  '-f' ,  '-'  , 'clean'] as String[] )
    assertEquals ( """   [delete] quiet : 'false'
  [fileset] defaultexcludes : 'no' , includes : ',**/*~' , dir : '.'
""" , output.toString ( ) ) 
  }
  void testDefault ( ) {
    Gant.main ( [ '-n' ,  '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( "     [echo] message : 'Did something.'\n" , output.toString ( ) ) 
  }
  void testBlah ( ) {
    Gant.main ( [ '-n' ,  '-f' ,  '-'  , 'blah'] as String[] )
    assertEquals ( 'Target blah does not exist.\n' , output.toString ( ) ) 
  }
  void testSomething ( ) {
    Gant.main ( [ '-n' ,  '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( "     [echo] message : 'Did something.'\n" , output.toString ( ) ) 
  }
}

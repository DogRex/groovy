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
  final coreScript = '''
task ( something : "Do something." ) { }
task ( somethingElse : "Do something else." ) { }
'''
  void testSomething ( ) {
    System.setIn ( new StringBufferInputStream ( coreScript ) )
    Gant.main ( [ '-T' ,  '-f' ,  '-' ] as String[] )
    assertEquals ( '''gant something  --  Do something.
gant somethingElse  --  Do something else.
''' , output.toString ( ) ) 
  }
  void testSomethingAndClean ( ) {
    System.setIn ( new StringBufferInputStream ( 'includeTargets << new File ( "source/org/codehaus/groovy/gant/targets/clean.gant" )\n' + coreScript ) )
    Gant.main ( [ '-T' ,  '-f' ,  '-' ] as String[] )
    assertEquals ( '''gant clean  --  Action the cleaning.
gant clobber  --  Action the clobbering.  Do the cleaning first.
gant something  --  Do something.
gant somethingElse  --  Do something else.
''' , output.toString ( ) ) 
  }
  void testGStrings ( ) {
    System.setIn ( new StringBufferInputStream ( '''
def theWord = 'The Word'
task ( something : "Do ${theWord}." ) { }
task ( somethingElse : "Do ${theWord}." ) { }
''' ) )
    Gant.main ( [ '-T' ,  '-f' ,  '-' ] as String[] )
    assertEquals ( '''gant something  --  Do The Word.
gant somethingElse  --  Do The Word.
''' , output.toString ( ) ) 
  }
  
}

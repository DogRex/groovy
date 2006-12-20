//  Gant -- A Groovy build tool based on scripting Ant tasks
//
//  Copyright © 2006 Russel Winder <russel@russel.org.uk>
//
//  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
//  compliance with the License. You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software distributed under the License is
//  distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
//  implied. See the License for the specific language governing permissions and limitations under the
//  License.

package org.codehaus.groovy.gant.tests

import gant.Gant

/**
 *  A test for the depends processing, i.e. make sure the depends calls the method when appropriate and not
 *  when appropriate.
 *
 *  @author Russel Winder <russel@russel.org.uk>
 *  @version $Revision$ $Date$
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
    assertEquals ( 'done.\n' , output.toString ( ) ) 
  }
  void testMultiple ( ) {
    System.setIn ( new StringBufferInputStream ( '''
task ( multipleDoit : '' ) { println ( 'done.' ) }
task ( multipleDoA : '' ) { depends ( multipleDoit ) }
task ( multipleDoB : '' ) { depends ( multipleDoit ) }
task ( multipleDoC : '' ) { depends ( multipleDoit ) }
task ( multipleDoAll : '' ) { depends ( multipleDoA , multipleDoB , multipleDoC ) }
''' ) )
    Gant.main ( [ '-f' , '-' , 'multipleDoAll' ] as String[] )
    assertEquals ( 'done.\n' , output.toString ( ) ) 
  }
  void testList ( ) {
    System.setIn ( new StringBufferInputStream ( '''
task ( listDoit : '' ) { println ( 'done.' ) }
task ( listDoA : '' ) { depends ( listDoit ) }
task ( listDoB : '' ) { depends ( listDoit ) }
task ( listDoC : '' ) { depends ( listDoit ) }
task ( listDoAll : '' ) { depends ( [ listDoA , listDoB , listDoC ] ) }
''' ) )
    Gant.main ( [ '-f' , '-' , 'listDoAll' ] as String[] )
    assertEquals ( 'done.\n' , output.toString ( ) ) 
  }
  void testNotClosure ( ) {
    System.setIn ( new StringBufferInputStream ( '''
task ( notClosure : '' ) { depends ( 'notClosure' ) }
''' ) )
    Gant.main ( [ '-f' , '-' , 'notClosure' ] as String[] )
    assertEquals ( 'depends called with an argument (notClosure) that is not a Closure or List of Closures.\n' , output.toString ( ) )
  }
  void testNotListClosure ( ) {
    System.setIn ( new StringBufferInputStream ( '''
task ( notListClosure : '' ) { depends ( [ 'notClosure' ] ) }
''' ) )
    Gant.main ( [ '-f' , '-' , 'notListClosure' ] as String[] )
    assertEquals ( 'depends called with List argument that contains an item (notClosure) that is not a Closure.\n' , output.toString ( ) )
  }
  void testOutOfOrder ( ) {
    System.setIn ( new StringBufferInputStream ( '''
task ( outOfOrderDoAll : '' ) { depends ( outOfOrderDoA , outOfOrderDoB , outOfOrderDoC ) }
task ( outOfOrderDoC : '' ) { depends ( outOfOrderDoit ) }
task ( outOfOrderDoB : '' ) { depends ( outOfOrderDoit ) }
task ( outOfOrderDoA : '' ) { depends ( outOfOrderDoit ) }
task ( outOfOrderDoit : '' ) { println ( 'done.' ) }
''' ) )
    Gant.main ( [ '-f' , '-' , 'outOfOrderDoAll' ] as String[] )
    assertEquals ( 'done.\n' , output.toString ( ) )
  }
  void testOutOfOrderList ( ) {
    System.setIn ( new StringBufferInputStream ( '''
task ( outOfOrderListDoAll : '' ) { depends ( [ outOfOrderListDoA , outOfOrderListDoB , outOfOrderListDoC ] ) }
task ( outOfOrderListDoC : '' ) { depends ( outOfOrderListDoit ) }
task ( outOfOrderListDoB : '' ) { depends ( outOfOrderListDoit ) }
task ( outOfOrderListDoA : '' ) { depends ( outOfOrderListDoit ) }
task ( outOfOrderListDoit : '' ) { println ( 'done.' ) }
''' ) )
    Gant.main ( [ '-f' , '-' , 'outOfOrderListDoAll' ] as String[] )
    assertEquals ( 'done.\n' , output.toString ( ) )
  }
  void testSameTargetAndFileName ( ) {
    //  Having a target of the same name as the script being compiled is fine until the task name is used in
    //  a depend.  At this point the class name not the name in the binding is picked up and all hell breaks
    //  loose.  Standard input is compiled as class standard_input.
    System.setIn ( new StringBufferInputStream ( '''
task ( standard_input , '' ) { println ( 'done.' ) }
task ( startingPoint , '' ) { depends ( standard_input ) }
''' ) )
    try { Gant.main ( [ '-f' , '-' , 'startingPoint' ] as String[] ) }
    catch ( MissingMethodException mme ) { return ; }
    fail ( 'Should have got a MissingMethodException but didn\'t.' )
  }
}

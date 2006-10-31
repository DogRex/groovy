//  Gant --- a Groovy build tool based on scripting Ant tasks
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

package org.codehaus.groovy.gant.infrastructure ;

import java.util.HashSet ;
import java.util.Iterator ;
import java.util.List ;

import groovy.lang.Binding ;
import groovy.lang.Closure ;
import groovy.lang.DelegatingMetaClass ;
import groovy.lang.MetaClassRegistry ;
import groovy.lang.MissingPropertyException ;

/**
 *  This class is the custom metaclass used for supporting execution of build descriptions in Gant.
 *
 *  <p>This metaclass is only here to deal with <code>depends</code> method calls.  To process these
 *  properly, all closures from the binding called during execution of the Gant specification must be logged
 *  so that when a depends happens the full closure call hiistory is available.</p>
 *
 *  @author Russel Winder <russel@russel.org.uk>
 *  @version $Revision$ $Date$
 */
class GantMetaClass extends DelegatingMetaClass {
  private final static HashSet methodsInvoked = new HashSet ( ) ;
  private final Binding binding ;
  protected GantMetaClass ( final Class theClass , final Binding binding ) {
    //  NB getIntance is the name of the method !
    super ( MetaClassRegistry.getIntance ( 0 ).getMetaClass ( theClass ) ) ;
    this.binding = binding ;
  }
  private Object processClosure ( final Closure closure ) {
    if ( ! methodsInvoked.contains ( closure ) ) {
      methodsInvoked.add ( closure ) ;         
      return closure.call ( ) ;
    }
    return null ;
  }
  public Object invokeMethod ( final Object object , final String methodName , final Object[] arguments ) {
    Object returnObject = null ;
    if ( methodName.equals ( "depends" ) ) {
      for ( int i = 0 ; i < arguments.length ; ++i ) {
        if ( arguments[i] instanceof Closure ) { returnObject = processClosure ( (Closure) arguments[i] ) ; }
        else if ( arguments[i] instanceof List ) {
          Iterator iterator = ( (List) arguments[i] ).iterator ( ) ;
          while ( iterator.hasNext ( ) ) {
            Object item = iterator.next ( ) ;
            if ( item instanceof Closure ) { returnObject = processClosure ( (Closure) item ) ; }
            else { throw new RuntimeException ( "depends called with List argument that contains an item (" + item + ") that is not a Closure." ) ; }
          }
        }
        else { throw new RuntimeException ( "depends called with an argument (" + arguments[i] + ") that is not a Closure or List of Closures." ) ; }
      }
    }
    else {
      returnObject = super.invokeMethod ( object , methodName , arguments ) ;
      try {
        final Closure closure = (Closure) binding.getVariable ( methodName ) ;
        if ( closure != null ) { methodsInvoked.add ( closure ) ; }
      }
      catch ( final MissingPropertyException mpe ) { }      
    }
    return returnObject ;
  }
}

//  Gant --- a Groovy build tool based on scripting Ant tasks
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

package org.codehaus.groovy.gant.infrastructure ;

import java.util.HashSet ;
import java.util.Iterator ;

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
 *  @author Russel Winder
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
  public Object invokeMethod ( final Object object , final String methodName , final Object[] arguments ) {
    Object returnObject = null ;
    if ( methodName.equals ( "depends" ) ) {
      for ( int i = 0 ; i < arguments.length ; ++i ) {
        if ( arguments[i] instanceof Closure ) {
          final Closure closure = ( Closure) arguments[i] ;
          if ( ! methodsInvoked.contains ( closure ) ) {
            methodsInvoked.add ( closure ) ;         
            returnObject = closure.call ( ) ;
          }
        }
        else { throw new RuntimeException ( "depends called with non-Closure argument." ) ; }
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

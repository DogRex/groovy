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

import java.util.ArrayList ;
import java.util.Iterator ;

import groovy.lang.GroovyObject ;
import groovy.lang.DelegatingMetaClass ;
import groovy.lang.MissingMethodException ;

import org.codehaus.groovy.runtime.InvokerHelper ;

/**
 *  This class is the custom metaclass used for supporting execution of build descriptions in
 *  Gant.
 *
 *  <p>This metaclass is really only here to deal with <code>include</code> and
 *  <code>description</code>method calls and the handling of the delegates specified in
 *  <code>include</code> calls, all others are passed to the superclass.</p>
 *
 *  <p>A separate instance of this class is used as the metaclass for each delegate as well
 *  there being a distinct instance for the user's build class metaclass.  This is needed as any
 *  and all of these classes can have tasks with descriptions.  Static data is used as a shared
 *  state between all instances.  In particular, the list of delegates and the boolean that
 *  determines whether the user build class or a delegate is currently being searched &ndash;
 *  delegates cannot delegate!</p>
 *
 *  @author Russel Winder
 *  @version $LastChangedRevision$ $LastChangedDate$
 */
public final class ExecutionMetaClass extends DelegatingMetaClass {
  private final static ArrayList delegates = new ArrayList ( ) ;
  private static boolean isDelegated = false ;
  public ExecutionMetaClass ( final Class theClass ) {
    super ( InvokerHelper.getMetaClass ( theClass ) ) ;
  }
  public Object invokeMethod ( final Object object , final String methodName , final Object[] arguments ) {
    Object returnObject = null ;
    if ( methodName.equals ( "include" ) ) {
      for ( int i = 0 ; i < arguments.length ; ++i ) {
        final Class delegateClass = (Class) arguments[i] ;
        try {
          final GroovyObject delegate = (GroovyObject) delegateClass.newInstance ( ) ;
          delegate.setMetaClass ( new ExecutionMetaClass ( delegate.getClass ( ) ) ) ;
          delegates.add ( delegate ) ;
        }
        catch ( final InstantiationException ie ) { throw new RuntimeException ( "InstantiationException" ) ; }  
        catch ( final IllegalAccessException iae ) { throw new RuntimeException ( "IllegalAccessException" ) ; }  
      }
    }
    else if ( methodName.equals ( "description" ) ) { }
    else {
      try { returnObject = super.invokeMethod ( object , methodName , arguments ) ; }
      catch ( final MissingMethodException mme_a ) {
        if ( ! isDelegated ) {
          isDelegated = true ;
          Iterator i = delegates.iterator ( ) ;
          while ( i.hasNext ( ) ) {
            GroovyObject delegate = (GroovyObject) i.next ( )  ;
            try {
              returnObject = delegate.invokeMethod ( methodName , arguments ) ;
              isDelegated = false ;
              return returnObject ;
            }
            catch ( final MissingMethodException mme_b ) { }
          }
          isDelegated = false ;
        }
        throw mme_a ;
      }
    }
    return returnObject ;
  }
}

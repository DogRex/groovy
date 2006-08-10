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

import java.beans.IntrospectionException ;
import java.util.ArrayList ;
import java.util.Iterator ;

import java.lang.reflect.Method ;

import groovy.lang.GroovyObject ;
import groovy.lang.MetaClassImpl ;
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
 *  @author Russel Winder
 *  @version $LastChangedRevision$ $LastChangedDate$
 */
public final class ExecutionMetaClass extends MetaClassImpl {
  private final static ArrayList delegates = new ArrayList ( ) ;
  public ExecutionMetaClass ( final Class theClass ) throws IntrospectionException {
    super ( InvokerHelper.getInstance ( ).getMetaRegistry ( ) , theClass ) ;
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
        catch ( final IntrospectionException ie ) { throw new RuntimeException ( "IntrospectionException" ) ; }
      }
    }
    else if ( methodName.equals ( "description" ) ) { }
    else {

      //System.err.println ( "Attempting " + object.getClass ( ).getName ( ) + "." + methodName ) ;

      try { returnObject = super.invokeMethod ( object , methodName , arguments ) ; }
      catch ( final MissingMethodException mme_a ) {
        Iterator i = delegates.iterator ( ) ;
        while ( i.hasNext ( ) ) {
          GroovyObject delegate = (GroovyObject) i.next ( )  ;
          
          try {

            //System.err.println ( "  Delegate attempting " + delegate.getClass ( ).getName ( ) + "." + methodName ) ;
            
            returnObject = delegate.invokeMethod ( methodName , arguments ) ;
            return returnObject ;
          }
          catch ( final MissingMethodException mme_b ) { }
        }
        throw mme_a ;
      }
    }
    return returnObject ;
  }
}

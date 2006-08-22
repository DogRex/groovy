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
import java.util.Map ;
import java.util.TreeMap ;

import java.lang.reflect.Method ;

import groovy.lang.GroovyObject ;
import groovy.lang.DelegatingMetaClass ;

import org.codehaus.groovy.runtime.InvokerHelper ;

import org.apache.tools.ant.Task ;

/**
 *  This class is the custom metaclass used for supporting creating tasks lists in Gant.
 *
 *  <p>The sole purpose of this metaclass is to process a call of the
 *  <code>retrieveAllDescriptions</code> method.  If it is used for any other purpose the
 *  behaviour is undefined.</p>
 *
 *  <p>The algorithm here is to search the top-level class and all included classes (delegates)
 *  for methods that return type <code>org.apache.tools.ant.Task</code> (should be nullary
 *  methods but this is not yet checked) and then to execute the method.  The first statement of
 *  the method is assumed to be a call to the method description with a single
 *  <code>String</code> parameter.  The method call is trapped and the method name and
 *  description stored in a <code>Map</code>.  Execution of the method is then terminated by
 *  throwing an exception which is caught and ignored but in a way that means the method call
 *  terminates.When all methods have been executed the <code>Map</code> containing all the
 *  descriptions is returned.</p>
 *
 *  <p>Known "mis-feature": Things go totally haywire if the first statements of a target method
 *  are not calls to method of the class to the <code>GantBuilder</code> object.</p>
 *
 *  <p>This metaclass is implemented so that an instance can safely be shared by objects of the
 *  associated class or delegates of any class &ndash; normally you would expect separate
 *  instances of a metaclass for each class of object since the metaclass knows the class it was
 *  instantiated for.  This is doen to avoid proliferation of classes and objects, i.e. to keep
 *  the implementation of the algorithm within this one class and so easier to maintain.</p>
 *
 *  @author Russel Winder
 *  @version $LastChangedRevision$ $LastChangedDate$
 */
public final class TargetListMetaClass extends DelegatingMetaClass {
  private final ArrayList delegates = new ArrayList ( ) ;
  private final Map documentation = new TreeMap ( ) ;
  private String temporaryMethodName ;
  private boolean findingTargets = false ;
  private class TerminateExecutionException extends RuntimeException { }
  public TargetListMetaClass ( final Class theClass ) {
    super ( InvokerHelper.getMetaClass ( theClass ) ) ;
  }
  private void getTargetsOf ( final Object object ) {
    final Method[] methods = object.getClass ( ).getMethods ( ) ;
    for ( int i = 0 ; i < methods.length ; ++i ) {
      Method method = methods[i] ;
      if ( method.getReturnType ( ) == Task.class ) {
        temporaryMethodName = method.getName ( ) ;
        findingTargets = true ;
        // Java 5 way: try { method.invoke ( object ) ; }
        try { method.invoke ( object , null ) ; }
        catch ( Exception e ) { }
        findingTargets = false ;
      }
    }
  }
  public Object invokeMethod ( final Object object , final String methodName , final Object[] arguments ) {
    Object returnObject = null ;
    if ( methodName.equals ( "include" ) ) {
      for ( int i = 0 ; i < arguments.length ; ++i ) {
        final Class delegateClass = (Class) arguments[i] ;
        try {
          final GroovyObject delegate = (GroovyObject ) delegateClass.newInstance ( ) ;
          delegate.setMetaClass ( this ) ; // Yes it is unusual to use a metaclass for the wrong class but...
          delegates.add ( delegate ) ;
        }
        catch ( final InstantiationException ie ) { throw new RuntimeException ( "InstantiationException" ) ; }  
        catch ( final IllegalAccessException ie ) { throw new RuntimeException ( "IllegalAccessException" ) ; }  
      }
    }
    else if ( methodName.equals ( "description" ) ) {
      String description = "<No description>" ;
       if ( arguments.length > 0 ) { description = (String) arguments[0] ; }
      documentation.put ( temporaryMethodName , description ) ;
      throw new TerminateExecutionException ( ) ;
    }
    else if ( methodName.equals ( "retrieveAllDescriptions" ) ) {
      if ( theClass != object.getClass ( ) ) { throw new RuntimeException ( "retrieveAllDescriptions failure." ) ; }
      getTargetsOf ( object ) ;
      Iterator i = delegates.iterator ( ) ;
      while ( i.hasNext ( ) ) { getTargetsOf ( i.next ( ) ) ; }
      returnObject = documentation ;
    }
    else if ( findingTargets ) { throw new TerminateExecutionException ( ) ; }
    return returnObject ;
  }
}

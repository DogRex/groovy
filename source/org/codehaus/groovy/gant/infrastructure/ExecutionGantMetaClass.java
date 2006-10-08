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
import java.util.HashMap ;
import java.util.Iterator ;

import groovy.lang.GroovyObject ;
import groovy.lang.MissingMethodException ;

/**
 *  This class is the custom metaclass used for supporting execution of build descriptions in Gant.
 *
 *  <p>This metaclass is really only here to deal with <code>includeTargets</code>, <cide>import</code>, and
 *  <code>description</code>method calls and the handling of the target delegates specified in
 *  <code>includeTargets</code> calls and the tools specified in <code>import</code> calls, all others are passed
 *  to the superclass.</p>
 *
 *  <p>The includeTargets mechanism is for target names so included classes are flattened with the build class so
 *  that all the methods appear in the class.  The import mechanism is for tools not targets.  Tools are not
 *  flattened except that only the last component of the fully qualified name is used as the properties
 *  mechanism is employed to access the tool objects.  Targets are accessed by direct lookup in
 *  <code>invokeMethod</code> whilst tools use the <code>getProperty</code> method for access.</p>
 *
 *  <p>A separate instance of this class is used as the metaclass for each delegate as well there being a
 *  distinct instance for the user's build class metaclass.  This is needed as any and all of these classes
 *  can have tasks with descriptions.  Static data is used as a shared state between all instances.  In
 *  particular, the list of delegates and the boolean that determines whether the user build class or a
 *  delegate is currently being searched &ndash; delegates cannot delegate!</p>
 *
 *  @author Russel Winder
 *  @version $Revision$ $Date$
 */
public final class ExecutionGantMetaClass extends GantMetaClass {
  private final static ArrayList delegates = new ArrayList ( ) ;
  private final static HashMap imports = new HashMap ( ) ;
  private static boolean isDelegated = false ;
  private final static ArrayList methodsInvoked = new ArrayList ( ) ;
  public ExecutionGantMetaClass ( final Class theClass ) { super ( theClass ) ; }
  protected void installClass ( final String methodName , final Class theClass ) {
    try {
      final GroovyObject theObject = (GroovyObject) theClass.newInstance ( ) ;
      theObject.setMetaClass ( new ExecutionGantMetaClass ( theClass ) ) ;
      if ( methodName.equals ( "includeTargets" ) ) { delegates.add ( theObject ) ; }
      else {
        final String theClassName = theObject.getClass ( ).getName ( ) ;
        imports.put ( theClassName.substring ( theClassName.lastIndexOf ( '.' ) + 1 ) , theObject ) ;
      }
    }
    catch ( final InstantiationException ie ) { throw new RuntimeException ( "InstantiationException" ) ; }  
    catch ( final IllegalAccessException iae ) { throw new RuntimeException ( "IllegalAccessException" ) ; }  
  }
  public Object invokeMethod ( final Object object , final String methodName , final Object[] arguments ) {
    Object returnObject = null ;
    if ( methodName.equals ( "includeTargets" ) || methodName.equals ( "includeTool" ) ) { processInclude ( methodName , arguments ) ; }
    else if ( methodName.equals ( "description" ) ) { }
    else if ( methodName.equals ( "depends" ) ) {
      for ( int i = 0 ; i < arguments.length ; ++i ) {
        final String dependentMethod = (String) arguments[i] ;
        if ( ! methodsInvoked.contains ( dependentMethod ) ) { invokeMethod ( object , dependentMethod , null ) ; }
      }
    }
    else if ( methodName.equals ( "message" ) ) {
      String keyword = "[" + (String) arguments[0] + "]" ;
      final Object message = arguments[1] ;
      final int length = keyword.length ( ) ;
      final int width = 11 ;
      if ( length < width ) { keyword = "           ".substring ( 0 , width - length ) + keyword ; }
      System.out.println ( keyword + " " + message ) ;
    }
    else {
      try {
        returnObject = super.invokeMethod ( object , methodName , arguments ) ;
        methodsInvoked.add ( methodName ) ;
      }
      catch ( final MissingMethodException mme_a ) {
        if ( ! isDelegated ) {
          isDelegated = true ;
          Iterator i = delegates.iterator ( ) ;
          while ( i.hasNext ( ) ) {
            GroovyObject delegate = (GroovyObject) i.next ( )  ;
            try {
              returnObject = delegate.invokeMethod ( methodName , arguments ) ;
              isDelegated = false ;
              methodsInvoked.add ( methodName ) ;
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
  public Object getProperty ( final Object object , final String property ) {
    return imports.containsKey ( property ) ? imports.get ( property ) : super.getProperty ( object , property ) ;
  }
}

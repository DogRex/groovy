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

package org.codehaus.groovy.gant.infrastructure ;

import java.util.Iterator ;
import java.util.Map ;

import groovy.lang.Closure ;
import groovy.lang.GroovyObjectSupport ;
import groovy.util.AntBuilder ;

import org.apache.tools.ant.Project;

/**
 *  This class is a sub-class of <code>AntBuilder</code> to provide dry-run capability and to deal with all
 *  the verbosity issues.
 *
 *  @author Russel Winder <russel@russel.org.uk>
 *  @version $Revision$ $Date$
 */
public class GantBuilder extends AntBuilder {
  public Object invokeMethod ( final String name , final Object arguments ) {
    if ( GantState.dryRun ) {
      if ( GantState.verbosity > GantState.SILENT ) {
        int padding = 9 - name.length ( ) ;
        if ( padding < 0 ) { padding = 0 ; }
        System.out.print ( "         ".substring ( 0 , padding ) + "[" + name + "] ") ;
        final Object[] args = (Object[]) arguments ;
        if ( args[0] instanceof Map ) {
          final Iterator i = ( (Map) args[0] ).entrySet ( ).iterator ( ) ;
          while ( i.hasNext ( ) ) {
            final Map.Entry e = (Map.Entry) i.next ( ) ;
            System.out.print ( e.getKey ( ) + " : '" + e.getValue ( ) + "'" ) ;
            if ( i.hasNext ( ) ) { System.out.print ( " , " ) ; }
          }
          System.out.println ( ) ;
          if ( args.length == 2 ) { ( (Closure) args[1] ).call ( ) ; }
        }
        else if ( args[0] instanceof Closure ) { System.out.println ( ) ; ( (Closure) args[0] ).call ( ) ; }
        else { throw new RuntimeException ( "Unexpected type of parameter to method " + name ) ; }
      }
      return null ;
    }
    return super.invokeMethod ( name , arguments ) ;
  }
}

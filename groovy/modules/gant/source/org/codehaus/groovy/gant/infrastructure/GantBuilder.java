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

import groovy.lang.GroovyObjectSupport ;

/**
 *  A class to provide access to the <code>GantBuilder</code> object that actual does all the
 *  Ant work.  Needed for building tools,
 *  e.g. <code>org.codehaus.groovy.gant.target.Clean</code>.
 *
 *  @author Russel Winder <russel@russel.org.uk>
 *  @version $LastChangedRevision$ $LastChangedDate$
 */
public abstract class GantBuilder extends GroovyObjectSupport {
  public static String targetList = "TargetList" ;
  public static String execution = "Execution" ;
  private static GantBuilder builder = null ;
  public static GantBuilder createInstance ( final String type ) {
    if ( builder != null ) { throw new RuntimeException ( "Attempt to reinitialize GantBuilder." ) ; }
    if ( type.equals ( targetList ) ) { builder = new TargetListGantBuilder ( ) ; }
    else if ( type.equals ( execution ) ) { builder = new ExecutionGantBuilder ( ) ; }
    else { throw new RuntimeException ( "Unknown GantBuilder type `" + type + "'" ) ; }
    return builder ;
  }
  public static GantBuilder getInstance ( ) { return builder ; }
  public abstract Object invokeMethod ( final String name , final Object arguments ) ;
}

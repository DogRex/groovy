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

/**
 *  An instance of this class is effectively just a Proxy for an <code>AntBuilder</code> object
 *  to be used when creating target lists so as to avoid any actions actually happening.
 *
 *  @author Russel Winder <russel@russel.org.uk>
 *  @version $Revision$ $Date$
 */
final class TargetListGantBuilder extends GantBuilder {
  public Object invokeMethod ( final String name , final Object arguments ) { return null ; }
}

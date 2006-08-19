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
 *  A class to hold the global shared state for a run of Gant.  We need this because parts of
 *  Gant are written in Java and parts in Groovy and it is not possible to compile them all at
 *  the same time so we must avoid any references to Groovy classes in the Java parts so that
 *  the Java can be compiled and then the Groovy compiled.
 *
 *  @author Russel Winder
 *  @version $LastChangedRevision$ $LastChangedDate$
 */
class GantState {
  public final static int SILENT = 0 , QUIET = 1 , NORMAL = 2 , VERBOSE = 3 ;
  static int verbosity = NORMAL ;
  
  static boolean dryRun = false ;
}

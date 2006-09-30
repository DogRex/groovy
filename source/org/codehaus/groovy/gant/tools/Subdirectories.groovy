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

package org.codehaus.groovy.gant.tools

import org.codehaus.groovy.gant.infrastructure.GantState

/**
 *  A class providing methods for executing processes in all subdirectories of the working directory
 *  for use in Gant scripts.  This is not really a target but a target support method.
 *
 *  <p>Requires Java SE 5 as it used <code>ProcessBuilder</code>.</p>
 *
 *  @author Russel Winder
 *  @version $Revision$ $Date$
 */
final class Subdirectories {
  private void runSubprocess ( String directory , String command ) {
    if ( GantState.verbosity > GantState.NORMAL ) { System.out.println "\n============ ${directory} ================" }
    //  If we allowed ourselves Java SE 5.0 then we could use ProcessBuilder but we restrict ourselves to Java 1.4.
    //def process = ( new ProcessBuilder ( [ 'sh' , '-c' , command ] )).directory ( directory ).start ( )
    def process = command.execute ( null , directory )
    if ( GantState.verbosity > GantState.QUIET ) { process.in.eachLine { line -> println line } }
  }
  void forAllSubdirectoriesRun ( String command ) {
    ( new File ( '.' ) ).eachDir { directory -> runSubprocess ( directory , command ) }
  }
  void forAllSubdirectoriesAnt ( String target ) { forAllSubdirectoriesRun ( 'ant ' + target ) }
  void forAllSubdirectoriesGant ( String target ) { forAllSubdirectoriesRun ( 'gant ' + target ) }  
}

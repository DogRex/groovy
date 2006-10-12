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

package org.codehaus.groovy.gant.targets

import org.codehaus.groovy.gant.infrastructure.GantBuilder

/**
 *  A class to provide clean and clobber actions for Gant build scripts.  Maintains separate lists of
 *  Ant pattern specifications and directory names for clean and for clobber.  The lists are used as the
 *  specifications when the clean or clobber methods are called.
 *
 *  @author Russel Winder
 *  @version $Revision$ $Date$
 */
final class Clean {
  def cleanPattern = []
  def cleanDirectory = []
  def clobberPattern = []
  def clobberDirectory = []
  private performPatternAction ( List l ) {
    if ( l.size ( ) > 0 ) {
      environment.Ant.delete ( quiet : 'false' ) {
        fileset ( dir : '.' , includes : l.join ( ',' ) , defaultexcludes : 'no' )
      }
    }
  }
  private performDirectoryAction ( List l ) {
    l.each { item -> environment.Ant.delete ( dir : item , quiet : 'false' )
    }
  }
  private Map environment 
  Clean ( Map environment ) {
    this.environment = environment
    environment.task ( clean : 'Action the cleaning.' ) {
      performPatternAction ( cleanPatternList )
      performDirectoryAction ( cleanDirectoryList )
    }
    environment.task ( clobber : 'Action the clobbering.  Does the cleaning first.' ) {
      depends ( clean )
      performPatternAction ( clobberPatternList )
      performDirectoryAction ( clobberDirectoryList )
    }
  }
}

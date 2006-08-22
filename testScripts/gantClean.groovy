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

/**
 *  A class showing an example build script for Gant.  This shows how tasks are specified and
 *  dependents specified as procedure calls. It shows how the constructor can be used to set up
 *  materials for included bits and pieces.  Note how the clean and clobber target are included
 *  from a predefined class.  Also shows how to deal with target names that are groovy reserved
 *  words!  It is also interesting to see how targets are documented using hte description
 *  method call.
 *
 *  @author Russel Winder
 *  @version $LastChangedRevision$ $LastChangedDate$
 */
class build {
  public build ( ) {
    include ( org.codehaus.groovy.gant.targets.Clean )
    addCleanPattern ( [ '**/*~' ,  '**/*.bak' ] )
    addCleanDirectory ( 'build' )
  }
  public Task 'default' ( ) { // Parse fails if public removed.  Why?
    description ( 'The default target.' )
    System.out.println ( 'Default' )
    clean ( )
    otherStuff ( )
  }
  Task otherStuff ( ) {
    description ( 'Other stuff' )
    System.out.println ( 'OtherStuff' )
    clean ( )
  }
}
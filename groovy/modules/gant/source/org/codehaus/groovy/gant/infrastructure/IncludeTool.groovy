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

package org.codehaus.groovy.gant.infrastructure

/**
 *  An instance of this class is provided to each Gant script for including tools.  A tool is a class that
 *  provides Gant related facilities.  The class must has a single parameter constructor which is a
 *  <code>Map</code>.  The map contains a binding of various useful things, in particualr there is always an
 *  entry 'Ant' to give access to the global static instance of <code>AntBuilder</code>.
 *
 *  @author Russel Winder
 *  @version $Revision$ $Date$
 */
class IncludeTool extends AbstractInclude {
  IncludeTool ( binding , groovyShell ) { super ( binding , groovyShell ) }
  def leftShift ( Class theClass ) {
    def className = theClass.name
    def index = className.lastIndexOf ( '.' ) + 1
    binding.setVariable ( className[index..-1] , createInstance ( theClass ) )
    this
  }
  def leftShift ( File file ) {
    def className = file.name
    className = className[ 0 ..< className.lastIndexOf ( '.' ) ]
    def theClass = groovyShell.evaluate ( file.text + " ; return ${className}" )
    binding.setVariable ( className , createInstance ( theClass ) )
    this
  }
  def leftShift ( GString s ) {
    throw new RuntimeException ( 'Implement << in IncludeTools for type GString.' ) 
    this
  }
  def leftShift ( String s ) {
    throw new RuntimeException ( 'Implement << in IncludeTools for type String.' ) 
    this
  }
  def leftShift ( List l ) { l.each { item -> this << item } ; this }
  def leftShift ( Object o ) {
    throw new RuntimeException ( 'Ignoring includeTool of type ' + o.class.name )
    this
  }
}

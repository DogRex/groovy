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
 *  An instance of this class is provided to each Gant script for including targets.  Targets can be
 *  provided by Gant (sub)scripts or Groovy or Java classes.
 *
 *  @author Russel Winder
 *  @version $Revision$ $Date$
 */
class IncludeTargets extends AbstractInclude {
  IncludeTargets ( binding , groovyShell ) { super ( binding , groovyShell ) }
  def leftShift ( Class theClass ) {
    throw new RuntimeException ( 'Implement << in IncludeTargets for type Class.' ) 
    this
  }
  def leftShift ( File f ) { groovyShell.evaluate ( f ) ; this }
  def leftShift ( GString s ) { groovyShell.evaluate ( s ) ; this }
  def leftShift ( String s ) { groovyShell.evaluate ( s ) ; this }
  def leftShift ( List l ) { l.each { item -> this << item } ; this }
  def leftShift ( Object o ) {
    throw new RuntimeException ( 'Ignoring includeTargets of type ' + o.class.name )
    this
  }
}

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

/**
 *  A class to provide support for using Ivy.  Assumes the ivy jar files are in $GROOVY_HOME.
 *
 *  @author Russel Winder
 *  @version $Revision$ $Date$
 */
final class Ivy {
  private final Binding binding ;
  private final classpath = 'ivy.class.path'
  Ivy ( final Binding binding ) {
    this.binding = binding ;
    /*
     *  This is what we want to do:

    binding.Ant.path ( id : classpath ) { fileset ( dir : System.getenv ( ).GROOVY_HOME , includes : 'ivy*.jar' ) }

    *  but this causes real hassles when using JDK versions prior to 1.5.  But we know that groovy.home is a
    *  property in the AntBuilder so just use that.
    */
    binding.Ant.path ( id : classpath ) { fileset ( dir : binding.Ant.project.properties.'groovy.home' , includes : 'ivy*.jar' ) }
    binding.Ant.taskdef ( resource : 'fr/jayasoft/ivy/ant/antlib.xml' , classpathref : classpath )
  }
  void cachepath ( map ) { binding.Ant.cachepath ( map ) }
  void configure ( map ) { binding.Ant.configure ( map ) }
  void publish ( map ) { binding.Ant.publish ( map ) }
  void report ( map ) { binding.Ant.report ( map ) }
  void resolve ( map ) { binding.Ant.resolve ( map ) }
  void retrieve ( map ) { binding.Ant.retrieve ( map ) }
}

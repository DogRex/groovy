#! /usr/bin/env groovy

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

//  Author : Russel Winder
//  $Revision$
//  $Date$

//  This Groovy script is for installing the Gant bits and pieces in the situation where the person doing
//  the install chooses not to extract the zip or tarball directly into $GROOVY_HOME.

def ant = new AntBuilder ( )
def theEnvironment = 'environment'

ant.property ( environment : theEnvironment ) 
def groovyHome = ant.project.properties.'groovy.home'

ant.copy ( todir : groovyHome ) {
  fileset ( dir : 'source' , includes : 'bin/gant*' )
  fileset ( dir : '.' , includes : 'lib/gant*.jar' )
}
ant.chmod ( perm : 'a+x' ) {
  fileset ( dir : groovyHome + '/bin' , includes : 'gant*' )
}

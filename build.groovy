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
 *  The Gant build script for use in building and installing Gant if Gant is already installed
 *  usably in some way.
 *
 *  @author Russel Winder
 *  @version $LastChangedRevision$ $LastChangedDate$
 */
class build {
  private final version = '0.1.0-SNAPSHOT'
  private final sourceDirectory = 'source'
  private final testsDirectory = 'tests'  
  private final groovyHome = System.getenv ( 'GROOVY_HOME' ) ; { assert groovyHome != '' }
  private final buildDirectory = 'build'
  private final buildClassesDirectory = buildDirectory + '/classes' 
  private final buildTestsDirectory = buildDirectory + '/tests' 
  private final buildLibDirectory = buildDirectory + '/lib'
  private final buildReportsDirectory = buildDirectory + '/reports'                                                              
  def build ( ) {
    include ( org.codehaus.groovy.gant.targets.Clean )
    addCleanPattern ( '**/*~' )
    addCleanDirectory ( buildDirectory )
    ant.path ( id : 'compilePath' ) { ant.fileset ( dir : groovyHome + '/lib' , includes : '*.jar' ) }
    ant.taskdef ( name : 'groovyc' , classname : 'org.codehaus.groovy.ant.Groovyc' , classpathref : 'compilePath' )
  }
  Task initialize ( ) {
    description ( 'Initialize prior to a build' )
    ant.mkdir ( dir : buildClassesDirectory )
    ant.mkdir ( dir : buildTestsDirectory )
    ant.mkdir ( dir : buildLibDirectory )
    ant.mkdir ( dir : buildReportsDirectory )
  }
  Task compile ( ) {
    description ( 'Compile everything needed.' )
    initialize ( )
    ant.javac ( srcdir : sourceDirectory , destDir : buildClassesDirectory , source : '1.4' , debug : 'on' , debuglevel : 'lines,vars,source' , classpathref : 'compilePath' )
    ant.groovyc ( srcdir : sourceDirectory , destDir : buildClassesDirectory , classpath : buildClassesDirectory )
    ant.jar ( destfile : buildLibDirectory + "/gant-${version}.jar" , basedir : buildClassesDirectory , includes : 'org/**' )
  }
  Task compileTests ( ) {
    description ( 'Compile all the tests.' )
    compile ( )
    ant.groovyc ( srcdir : testsDirectory , destdir : buildTestsDirectory , classpath : buildClassesDirectory )
  }
  Task test ( ) {
    description ( 'Test a build.' )
    compileTests ( )
    ant.junit ( printsummary : 'yes' ) {
      ant.formatter ( type : 'plain' )
      ant.batchtest ( fork : 'yes' , todir : buildReportsDirectory ) {
        ant.fileset ( dir : buildTestsDirectory , includes : '**/*_Test.class' )
      }
      ant.classpath {
        ant.path ( refid : 'compilePath' )
        ant.pathelement ( location : buildClassesDirectory )
        ant.pathelement ( location : buildTestsDirectory )
      }
    }
  }
  Task install ( ) {
    description ( 'Compile everything and install it to ' + groovyHome + '.' )
    compile ( )
    ant.copy ( todir : groovyHome ) {
      ant.fileset ( dir : sourceDirectory , includes : 'bin/gant*' )
      ant.fileset ( dir : buildDirectory , includes : 'lib/gant*.jar' )
    }
    ant.chmod ( perm : 'a+x' ) {
      ant.fileset ( dir : groovyHome + '/bin' , includes : 'gant*' )
    }
  }
  public Task 'default' ( ) { // Parse fails if public removed.  Why?
    description ( 'The default target, currently test.' )
    test ( )
  }
}

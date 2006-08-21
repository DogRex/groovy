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
 *  This class provides infrastructure and an executable command for using Groovy + AntBuilder
 *  as a build tool in a way similar to Rake and SCons.  However, where Rake and SCons are
 *  dependency programming systems based on Ruby and Python respectively, Gant is simply a way
 *  of scripting Ant tasks; the Ant tasks do all the dependency management.
 *
 *  <p>A Gant build specification file (default name build.groovy) is assumed to contain one or
 *  more classes where the first is the main class whose public nullary functions returning a
 *  <code>Task</code> are the targets -- in the Ant sense.  Dependencies between targets are
 *  handled as function calls within functions. Execution of Ant tasks is by calling methods on
 *  the object called `ant', which is predefined as an <code>GantBuilder</code> instance.</p>
 *
 *  <p>On execution of the gant command, the Gant build specification will be injected with a
 *  number of features.  An object called `ant' is automatically created so methods can use this
 *  object to get access to the Ant tasks without having to create an object explicitly.  A
 *  method called `description' is also injected.  If there is a call to this function (which
 *  takes a single String parameter) is the first statement in a method then it becomes the `one
 *  liner' documentation for the task.  This is used by the `gant -T' / `gant --targets' command
 *  to present a list of all the documented targets.</p>
 *
 *  <p>A trivial example build specification is:</p>
 *
 *  <pre>
 *        
 *        class build {
 *          public Task 'default' ( ) { // Parse fails if public removed.  Why?
 *            description ( 'The default target.' )
 *            clean ( )
 *            otherStuff ( )
 *          }
 *          Task otherStuff ( ) {
 *            description ('Other stuff' )
 *            clean ( )
 *          }
 *          Task clean ( ) {
 *            description ( 'Clean the directory and subdirectories' )
 *            ant.delete ( dir : 'build' , quiet : 'true' )
 *            ant.delete ( quiet : 'true' ) {
 *              ant.fileset ( dir : '.' , includes : '** /*~'  , defaultexcludes : 'no' )
 *            }
 *          }
 *        }
 *
 * </pre>
 *
 *  <p><em>Note that there is an space between the two asterisks and the solidus in the fileset
 *  line that should notbe there, we have to have it in the source because asterisk followed by
 *  solidus is end of comment in Groovy</em></p>
 *
 *  <p>Clearly this does not show the true power of using Groovy instead of XML for specifying
 *  builds, but hopefully you get the idea.</p>
 *
 *  @author Russel Winder <russel@russel.org.uk>
 *  @version $LastChangedRevision$ $LastChangedDate$
 */
final class Gant {

  private buildFileName = 'build.groovy'
  private buildFileText = ''
  private List gantLib ; {
    def item = System.getenv ( ).GANTLIB ;
    if ( item == null ) { gantLib = [] }
    else { gantLib = Arrays.asList ( item.split ( System.properties.'path.separator' ) ) }
  }

  private Gant ( ) { }
  private Class compileBuildFile ( final String metaClassType ) {
    def buildClassOpening = ''
    def buildClassName = ''
    final javaIdentifierRegexAsString = /\b\p{javaJavaIdentifierStart}(?:\p{javaJavaIdentifierPart})*\b/
    final javaQualifiedNameRegexAsString = /\b${javaIdentifierRegexAsString}(?:[.\/]${javaIdentifierRegexAsString})*\b/
    buildFileText.eachMatch ( /(?:(?:public|final))*[ \t\n]*class[ \t\n]*(${javaIdentifierRegexAsString})[ \t\n]*(?:extends[ \t\n]*${javaQualifiedNameRegexAsString})*[ \t\n]*\{/ ) { classOpening , className ->
      buildClassOpening = classOpening
      buildClassName = className
    }
    assert buildClassOpening != ''
    assert buildClassName != ''
    buildFileText = "import org.codehaus.groovy.gant.infrastructure.GantBuilder; import org.apache.tools.ant.Task;" +
     buildFileText.replace ( buildClassOpening , buildClassOpening +
                             "private final ant = GantBuilder.createInstance ( \"${metaClassType}\" ) ; { setMetaClass ( new org.codehaus.groovy.gant.infrastructure.${metaClassType}MetaClass ( ${buildClassName} ) ) }" )
    def buildClassClass = ( new GroovyShell ( ) ).evaluate ( buildFileText + '; return ' + buildClassName + '.class'  )
    assert buildClassClass != null
    return buildClassClass
  }
  private targetList ( targets ) {
    def documentation = new TreeMap ( )
    def buildObject = compileBuildFile ( GantBuilder.targetList ).newInstance ( )
    for ( p in ( (Map) buildObject.retrieveAllDescriptions ( ) ).entrySet ( ) ) { println ( 'gant ' + p.getKey ( ) + '  --  ' + p.getValue ( ) ) }
  }
  private dispatch ( targets ) {
    def buildObject = compileBuildFile ( GantBuilder.execution ).newInstance ( )
    if ( targets.size ( ) > 0 ) { targets.each { target ->
        try { buildObject.invokeMethod ( target , null ) }
        catch ( MissingMethodException mme ) {
          println ( ( mme.method == target ) ? "Target ${mme.method} does not exist." : "Could not execute method ${mme.method}." )
        }
      }
    }
    else { buildObject.'default' ( ) }
  }
  private process ( args ) {
    def cli = new CliBuilder ( usage : 'gant [option]* [target]*' , writer : new PrintWriter ( System.out ) )
    cli.f ( longOpt : 'gantfile' , args : 1 , argName : 'build-file' , 'Use the named build file instead of the default, build.groovy.' )
    cli.h ( longOpt : 'help' , 'Print out this message.' )
    cli.l ( longOpt : 'gantlib' , args : 1 , argName : 'library' , 'A directory that contains classes to be used as extra Gant modules,' )
    cli.n ( longOpt : 'dry-run' , 'Do not actually action any tasks.' )
    cli.p ( longOpt : 'projecthelp' , 'Print out a list of the possible targets.' )
    cli.q ( longOpt : 'quiet' , 'Do not print out much when executing.' )
    cli.s ( longOpt : 'silent' , 'Print out nothing when executing.' )
    cli.v ( longOpt : 'verbose' , 'Print lots of extra information.' )
    cli.T ( longOpt : 'targets' , 'Print out a list of the possible targets.' )
    cli.V ( longOpt : 'version' , 'Print lots of extra information.' )
    def options = cli.parse ( args )
    // options is supposed to be null if there has been any error.  CliBuilder is supposed to
    // have dealt with all ParseExceptions (by printing a usage message) but this is patently
    // not the case and especially so for unexpected single character options which just seem to
    // dissappear.  Experiment indicates that Commons CLI is at fault here, see GROOVY-1455.
    // Then there is the problem with multicharacter options with a single hyphen -- the
    // behaviour is most bizarre!
    if ( options == null ) { println ( 'Error in processing command line options.' ) ; return }
    def function = 'dispatch'
    if ( options.f ) { buildFileName = options.f }
    if ( options.h ) { cli.usage ( ) ; return }
    if ( options.l ) { gantLib = options.l.split ( System.properties.'path.separator' ) }
    if ( options.n ) { GantState.dryRun = true }
    if ( options.p || options.T ) { function = 'targetList' }
    if ( options.q ) { GantState.verbosity = GantState.QUIET }
    if ( options.s ) { GantState.verbosity = GantState.SILENT }
    if ( options.v ) { GantState.verbosity = GantState.VERBOSE }
    if ( options.V ) { println 'Gant version 0.1.0' ; return }
    def targets = options.arguments ( )
    //  We need to deal with unknown options, which should have been unprocessed by CliBuilder.
    //  We know though that unexpected single charactere options get absorbed by Commons CLI.
    //  There is also a serious problem with multicharacter options with a single minus, they
    //  behave very strangely indeed.
    def gotUnknownOptions = false ;
    targets.each { target ->
      if ( target[0] == '-' ) {
        println ( 'Unknown option: ' + target ) 
        gotUnknownOptions = true
      }
    }
    if ( gotUnknownOptions ) { cli.usage ( ) ; return ; }
    if ( buildFileName == '-' ) { buildFileText = System.in.text }
    else {
      def file = new File ( buildFileName ) 
      if ( ! file.isFile ( ) ) {
        println ( 'Cannot open file ' + buildFileName )
        return
      }
      buildFileText =  ( new File ( buildFileName ) ).text
    }
    assert buildFileText != ''
    invokeMethod ( function , targets )
  }
  public static main ( args ) { ( new Gant ( ) ).process ( args ) }
}

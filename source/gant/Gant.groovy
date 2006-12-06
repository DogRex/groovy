//  Gant -- A Groovy build tool based on scripting Ant tasks
//
//  Copyright © 2006 Russel Winder <russel@russel.org.uk>
//
//  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
//  compliance with the License. You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software distributed under the License is
//  distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
//  implied. See the License for the specific language governing permissions and limitations under the
//  License.

package gant

import org.codehaus.groovy.control.MultipleCompilationErrorsException

import org.codehaus.groovy.gant.GantBuilder
import org.codehaus.groovy.gant.GantMetaClass
import org.codehaus.groovy.gant.GantState
import org.codehaus.groovy.gant.IncludeTargets
import org.codehaus.groovy.gant.IncludeTool

/**
 *  This class provides infrastructure and an executable command for using Groovy + AntBuilder as a build
 *  tool in a way similar to Rake and SCons.  However, where Rake and SCons are dependency programming
 *  systems based on Ruby and Python respectively, Gant is simply a way of scripting Ant tasks; the Ant
 *  tasks do all the dependency management.
 *
 *  <p>A Gant build specification file (default name build.gant) is assumed to contain one or more classes
 *  where the first is the main class whose public nullary functions returning a <code>Task</code> are the
 *  targets -- in the Ant sense.  Dependencies between targets are handled as function calls within
 *  functions. Execution of Ant tasks is by calling methods on the object called `ant', which is predefined
 *  as an <code>GantBuilder</code> instance.</p>
 *
 *  <p>On execution of the gant command, the Gant build specification will be injected with a number of
 *  features.  An object called `ant' is automatically created so methods can use this object to get access
 *  to the Ant tasks without having to create an object explicitly.  A method called `description' is also
 *  injected.  If there is a call to this function (which takes a single String parameter) is the first
 *  statement in a method then it becomes the `one liner' documentation for the task.  This is used by the
 *  `gant -T' / `gant --targets' command to present a list of all the documented targets.</p>
 *
 *  <p>NB In the following example some extra spaces have had to be introduced because some of the patterns
 *  look like comment ends:-(</p>
 * 
 *  <p>A trivial example build specification is:</p>
 *
 *  <pre>
 *      task ( 'default' : 'The default target.' ) {
 *        clean ( )
 *        otherStuff ( )
 *      }
 *      task ( otherStuff : 'Other stuff' ) {
 *        clean ( )
 *      }
 *      task ( clean : 'Clean the directory and subdirectories' ) {
 *        Ant.delete ( dir : 'build' , quiet : 'true' )
 *        Ant.delete ( quiet : 'true' ) { fileset ( dir : '.' , includes : '** /*~,** /*.bak'  , defaultexcludes : 'false' ) }
 *      }
 * </pre>
 *
 *  <p>or, using some a ready made targets class:</p>
 *
 *  <pre>
 *      includeTargets << org.codehaus.groovy.gant.targets.Clean
 *      cleanPattern << [ '** / *~' , '** / *.bak' ]
 *      cleanDirectory << 'build'
 *      task ( 'default' : 'The default target.' ) {
 *        clean ( )
 *        otherStuff ( )
 *      }
 *      task ( otherStuff : 'Other stuff' ) {
 *        clean ( )
 *      }
 *
 *  <p><em>Note that there is an space between the two asterisks and the solidus in the fileset line that
 *  should notbe there, we have to have it in the source because asterisk followed by solidus is end of
 *  comment in Groovy</em></p>
 *
 *  @author Russel Winder <russel@russel.org.uk>
 *  @version $Revision$ $Date$
 */
final class Gant {
  private buildFileName = 'build.gant'
  private buildClassName = buildFileName.replaceAll ( '\\.' , '_' ) 
  private final Map taskDescriptions = new TreeMap ( ) 
  private final binding = new Binding ( )
  private final groovyShell = new GroovyShell ( binding )
  private final task = { map , closure ->
    def taskName = map.keySet ( ).iterator ( ).next ( )
    def taskDescription = map.get ( taskName )
    if ( taskDescription ) { taskDescriptions.put ( taskName , taskDescription ) }
    closure.metaClass = new GantMetaClass ( closure.class , binding )                            
    binding.setVariable ( taskName , closure )
  }
  private final message = { tag , message ->
    def padding = 9 - tag.length ( )
    if ( padding < 0 ) { padding = 0 }
    println ( "           ".substring ( 0 , padding ) + '[' + tag + '] ' + message )
  }
  private def ant = new GantBuilder ( )
  private List gantLib ; {
    /*
     *  This is what we want to do:

    def item = System.getenv ( ).GANTLIB ;

     *  but it causes hassles in JDK versions prior to 1.5.  To quote Graeme Rocher "This method was
     *  deprecated in Java 1.2,1.3,1.4 but then undeprecated in Java 5".  Alex Shneyderman proposed the
     *  alternate based on calling Ant.
     */
    ant.property ( environment : 'environment' )
    def item = ant.project.properties.'environment.GANTLIB'
    if ( item == null ) { gantLib = [] }
    else { gantLib = Arrays.asList ( item.split ( System.properties.'path.separator' ) ) }
  }
  private Gant ( ) {
    binding.setVariable ( 'gantLib' , gantLib )
    binding.setVariable ( 'Ant' , ant )
    binding.setVariable ( 'groovyShell' , groovyShell )
    binding.setVariable ( 'includeTargets' , new IncludeTargets ( binding ) )
    binding.setVariable ( 'includeTool' ,  new IncludeTool ( binding ) )
    binding.setVariable ( 'task' , task )
    binding.setVariable ( 'message' , message )
 }
  private targetList ( targets ) {
    def message = taskDescriptions['default']
    if ( message != null ) { println ( 'gant -- ' + message) }
    for ( p in taskDescriptions.entrySet ( ) ) { if ( p.key != 'default' ) { println ( 'gant ' + p.key + '  --  ' + p.value ) } }
  }
  private printDispatchExceptionMessage ( target , method , message ) {
    println ( ( target == method ) ? "Target ${method} does not exist." : "Could not execute method ${method}.\n${message}" )
  }
  private dispatch ( targets ) {
    try {
      if ( targets.size ( ) > 0 ) {
        targets.each { target ->
          try { binding.getVariable ( target ).run ( ) }
          catch ( MissingPropertyException mme ) { printDispatchExceptionMessage ( target , mme.property , mme.message ) }
        }
      }
      else {
        try { binding.getVariable ( 'default' ).run ( ) }
        catch ( MissingPropertyException mme ) { printDispatchExceptionMessage ( 'default' , mme.property , mme.message ) }
      }
    }
    catch ( Exception e ) { println ( e.message ) }
  }
  private process ( args ) {
    def cli = new CliBuilder ( usage : 'gant [option]* [target]*' , writer : new PrintWriter ( System.out ) )
    cli.f ( longOpt : 'gantfile' , args : 1 , argName : 'build-file' , 'Use the named build file instead of the default, build.gant.' )
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
    // options is supposed to be null if there has been any error.  CliBuilder is supposed to have dealt
    // with all ParseExceptions (by printing a usage message) but this is patently not the case and
    // especially so for unexpected single character options which just seem to cause chaos.  Experiment
    // indicates that Commons CLI is at fault here, see GROOVY-1455.  Then there is the problem with
    // unprocessed options with a single hyphen -- the behaviour is most bizarre!
    if ( options == null ) { println ( 'Error in processing command line options.' ) ; return }
    def function = 'dispatch'
    if ( options.f ) { buildFileName = options.f ; buildClassName = buildFileName.replaceAll ( '\\.' , '_' ) }
    if ( options.h ) { cli.usage ( ) ; return }
    if ( options.l ) { gantLib = options.l.split ( System.properties.'path.separator' ) }
    if ( options.n ) { GantState.dryRun = true }
    if ( options.p || options.T ) { function = 'targetList' }
    if ( options.q ) { GantState.verbosity = GantState.QUIET }
    if ( options.s ) { GantState.verbosity = GantState.SILENT }
    if ( options.v ) { GantState.verbosity = GantState.VERBOSE }
    if ( options.V ) { println 'Gant version 0.2.2' ; return }
    //  options.arguments( ) is expected to return a single element list with a string of the unprocessed
    //  command line entries.  Unprocessed -- options are passed through as expected but unprocessed single
    //  - options cause chaos.  The unprocessed option string is passed without the - sign and a whole slew
    //  of extra entries appear int he list.  Oh well.
    def arguments = options.arguments ( )
    def targets = arguments.size ( ) == 0 ? [] : Arrays.asList ( arguments[0].split ( ' ' ) )
    //  We need to deal with unknown options, which should have been unprocessed by CliBuilder.  We have to
    //  suffer that single - options are problematic.
    def gotUnknownOptions = false ;
    targets.each { target ->
      if ( target[0] == '-' ) {
        println ( 'Unknown option: ' + target ) 
        gotUnknownOptions = true
      }
    }
    if ( gotUnknownOptions ) { cli.usage ( ) ; return ; }
    def buildFileText = ''
    if ( buildFileName == '-' ) {
      buildFileText = System.in.text
      buildClassName = "standard_input"
    }
    else {
      def file = new File ( buildFileName ) 
      if ( ! file.isFile ( ) ) {
        println ( 'Cannot open file ' + buildFileName )
        return
      }
      buildFileText =  ( new File ( buildFileName ) ).text
    }
    groovyShell.evaluate ( buildFileText , buildClassName )
    invokeMethod ( function , targets )
  }
  public static main ( args ) { ( new Gant ( ) ).process ( args ) }
}

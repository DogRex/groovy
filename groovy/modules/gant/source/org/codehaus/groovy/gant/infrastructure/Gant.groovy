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
 *  more classes where the first is the main class whose public nullary functions are the
 *  targets -- in the Ant sense.  Dependencies between targets are handled as function calls
 *  within functions. Execution of Ant tasks is by calling methods on the object called `ant',
 *  which is predefined as an <code>AntBuilder</code> instance.</p>
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
 *          public Target 'default' ( ) { // Parse fails if public removed.  Why?
 *            description ( 'The default target.' )
 *            clean ( )
 *            otherStuff ( )
 *          }
 *          Target otherStuff ( ) {
 *            description ('Other stuff' )
 *            clean ( )
 *          }
 *          Target clean ( ) {
 *            description ( 'Clean the directory and subdirectories' )
 *            ant.delete ( dir : 'build' , quiet : 'true' )
 *            ant.delete ( quiet : 'true' ) {
 *              ant.fileset ( dir : '.' , includes : '** /*~'  , defaultexcludes : 'no' )
 *            }
 *            null // Need this to avoid an ant call being the return value.
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
  private final List gantLib ; {
    def item = System.getenv ( ).GANTLIB ;
    if ( item == null ) { gantLib = [] }
    else { gantLib = Arrays.asList ( item.split ( System.properties.'path.separator' ) ) }
  }
  private final javaIdentifierRegexAsString = /\b\p{javaJavaIdentifierStart}(?:\p{javaJavaIdentifierPart})*\b/
  private final javaQualifiedNameRegexAsString = /\b${javaIdentifierRegexAsString}(?:[.\/]${javaIdentifierRegexAsString})*\b/
  private Gant ( ) { }
  private Class compileBuildFile ( final String metaClassName ) {
    def buildClassOpening = ''
    def buildClassName = ''
    buildFileText.eachMatch ( /(?:(?:public|final))*[ \t\n]*class[ \t\n]*(${javaIdentifierRegexAsString})[ \t\n]*(?:extends[ \t\n]*${javaQualifiedNameRegexAsString})*[ \t\n]*\{/ ) { classOpening , className ->
      buildClassOpening = classOpening
      buildClassName = className
    }
    assert buildClassOpening != ''
    assert buildClassName != ''
    buildFileText = buildFileText.replace ( buildClassOpening , buildClassOpening + """
private final ant = new AntBuilder ( ) ; {
  setMetaClass ( new org.codehaus.groovy.gant.infrastructure.${metaClassName} ( ${buildClassName} ) )
}
""")
    def buildClassClass = ( new GroovyShell ( ) ).evaluate ( buildFileText + '; return ' + buildClassName + '.class'  )
    assert buildClassClass != null
    return buildClassClass
  }
  private targetList ( targets ) {
    def documentation = new TreeMap ( )
    def buildClassClass = compileBuildFile ( 'TargetListMetaClass' )
    def buildObject = buildClassClass.newInstance ( )
    for ( p in ( (Map) buildObject.retrieveAllDescriptions ( ) ).entrySet ( ) ) { println ( 'gant ' + p.getKey ( ) + '  --  ' + p.getValue ( ) ) }
  }
  private dispatch ( targets ) {
    def buildClassClass = compileBuildFile ( 'ExecutionMetaClass' )
    def buildObject = buildClassClass.newInstance ( )
    if ( targets.size ( ) > 0 ) { targets.each { target ->
        try { buildObject.invokeMethod ( target , null ) }
        catch ( MissingMethodException mme ) {
          println ( "Target ${target} does not exist." )
        }                            
      }
    }
    else { buildObject.'default' ( ) }
  }
  private process ( args ) {
    def i = 0
    def targets = []
    def function = 'dispatch'

    def cli = new CliBuilder ( usage : 'gant [option]* [target]*' , writer : new PrintWriter ( System.out ) )
    cli.f ( longOpt : 'gantfile' , args : 1 , argName : 'build-file' , 'Use the named build file instead of the default, build.groovy.' )
    cli.h ( longOpt : 'help' , 'Print out this message.' )
    cli.l ( longOpt : 'gantlib' , args : 1 , argName : 'library' , 'A directory that contains classes to be used as extra Gant modules,' )
    cli.q ( longOpt : 'quiet' , 'Do not print out much when executing.' )
    cli.p ( longOpt : 'projecthelp' , 'Print out a list of the possible targets.' )
    cli.s ( longOpt : 'silent' , 'Print out nothing when executing.' )
    cli.T ( longOpt : 'targets' , 'Print out a list of the possible targets.' )
    def options = cli.parse ( args )
    if ( options.h ) { cli.usage ( ) ; return }
    if ( options.f ) { buildFileName = options.f }
    if ( options.l ) { gantLib = options.l }
    if ( options.T ) { function = 'targetList' }
    targets = options.arguments ( )

    /*  
    while ( i < args.size ( ) ) { 
      switch ( args[i] ) {
        case '-f' : buildFileName = args[++i] ; break
        case ~'--gantfile=[^ \t\n]*' : buildFileName = args[i][( args[i].indexOf ( '=' ) +1 )..-1] ; break
        case '-l' : gantLib = args[++i] ; break
        case ~'--gantlib=[^ \t\n]*' : gantLib = args[i][( args[i].indexOf ( '=' ) +1 )..-1].split ( ';' ) ; break
        case ~'(-h|--help)' : return
        case ~'(-q|--quiet|-s|--silent)' : break
        case ~'(-T|--targets|-p|--projecthelp)' : function = 'targetList' ; break
        case ~'-[^ \t\n]*' : println 'Unknown option: ' + args[i] ; return
        default : targets += args[i] ; break
      }
      ++i
    }
    */
    def file = new File ( buildFileName ) 
    if ( ! file.isFile ( ) ) {
      println ( 'Cannot open file ' + buildFileName ) 
    }
    else {
      buildFileText =  'import org.codehaus.groovy.gant.infrastructure.Target\n\n' + ( new File ( buildFileName ) ).text
      assert buildFileText != ''
      invokeMethod ( function , targets )
    }
  }
  public static main ( args ) { ( new Gant ( ) ).process ( args ) }
}

//  Gant --- a Groovy build tool based on scripting Ant tasks
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

package org.codehaus.groovy.gant.infrastructure ;

import java.io.BufferedReader ;
import java.io.File ;
import java.io.FileNotFoundException ;
import java.io.FileReader ;
import java.io.IOException ;

import groovy.lang.DelegatingMetaClass ;
import groovy.lang.GroovyShell ;
import groovy.lang.MetaClassRegistry ;

/**
 *  This abstract class is a code sharing class for subclasses.
 *
 *  @author Russel Winder
 *  @version $Revision$ $Date$
 */
abstract class GantMetaClass extends DelegatingMetaClass {
  protected GantMetaClass ( final Class theClass ) {
    //  NB getIntance is the name of the method !
    super ( MetaClassRegistry.getIntance ( 0 ).getMetaClass ( theClass ) ) ;
  }
  protected Class readAndCompile ( final String path ) {
    final File file = new File ( path ) ;
    if ( file.canRead ( ) && file.isFile ( ) ) {
      final StringBuffer buffer = new StringBuffer ( ) ;
      buffer.append ( "import org.apache.tools.ant.Task ; " ) ;
      try {
        final BufferedReader reader = new BufferedReader ( new FileReader ( file ) ) ;
        while ( true ) {
          final String line = reader.readLine ( ) ;
          if ( line == null ) { break ; }
          buffer.append ( line + "\n" ) ;
        }
      }
      catch ( final FileNotFoundException fnfe ) { throw new RuntimeException ( "Could not find file " + path ) ; }
      catch ( final IOException ioe ) { throw new RuntimeException ( "Could not read the file file " + path ) ; }
      if ( buffer.length ( ) > 0 ) {
        String className = file.getName ( ) ;
        className = className.substring ( 0 , className.lastIndexOf ( '.' ) ) ;
        return (Class) ( ( new GroovyShell ( ) ).evaluate ( buffer.toString ( ) + "; return " + className + ".class"  ) ) ;
      }
    }
    throw new RuntimeException ( path + " is not a readable plain file." ) ;
  }
  protected abstract void installClass ( String methodName , Class theClass ) ;
  protected void processInclude ( final String methodName , final Object[] arguments ) {
    for ( int i = 0 ; i < arguments.length ; ++i ) {
      final Object theArgument = arguments[i] ;
      if ( theArgument instanceof Class ) { installClass ( methodName , (Class) theArgument ) ; }
      else if ( theArgument instanceof String ) { installClass ( methodName , readAndCompile ( (String) theArgument ) ) ; }
      else { throw new RuntimeException ( theArgument + "is not of type Class or String." ) ; }
    }
  }
}

/*
 * Created on Jan 14, 2004
 * @author zohar melamed
 *
 */
package org.codehaus.groovy.eclipse.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.syntax.lexer.FileCharStream;
import org.codehaus.groovy.tools.GroovyClass;
import org.eclipse.core.runtime.Platform;

public class EclipseFileSystemCompiler 
{
	private EclipseCompiler compiler;
	private File outputDir;

	public EclipseFileSystemCompiler()
	{
		this.compiler = new EclipseCompiler();
		String verbose = Platform.getDebugOption("org.codehaus.groovy.eclipse/compiler/verbose"); //$NON-NLS-1$
		if (verbose != null && verbose.equalsIgnoreCase("true")){
			compiler.setVerbose(true);
		}
		
		String debug = Platform.getDebugOption("org.codehaus.groovy.eclipse/compiler/debug"); //$NON-NLS-1$
		if (debug != null && debug.equalsIgnoreCase("true")){
			compiler.setDebug(true);
		}
		
		
	}

	public void setVerbose(boolean verbose)
	{
		compiler.setVerbose(verbose);
	}
	
	protected EclipseCompiler getCompiler()
	{
		return this.compiler;
	}

	public void setOutputDir(String outputDir)
	{
		setOutputDir( new File( outputDir ) );
	}

	public void setOutputDir(File outputDir)
	{
		this.outputDir = outputDir;
	}

	public File getOutputDir()
	{
		return this.outputDir;
	}

	public void setClasspath(String classpath)
	throws Exception
	{
		getCompiler().setClasspath( classpath );
	}


	public CompileUnit compile(File file, boolean generateClassFiles)throws Exception{
		return compile(new File[]{file}, generateClassFiles);
	}
	
	public CompileUnit compile(File[] files, boolean generateClassFiles)
	throws Exception
	{
		FileCharStream[] fileCharStreams = new FileCharStream[ files.length ];

		for ( int i = 0 ; i < fileCharStreams.length ; ++i )
		{
			fileCharStreams[ i ] = new FileCharStream( files[ i ] );
		}
		
		List result = new ArrayList();
		compiler.setVerbose(true);
		CompileUnit compileUnit = compiler.compile( fileCharStreams , result, generateClassFiles);
		for (Iterator iter = result.iterator(); iter.hasNext();) {
			GroovyClass element = (GroovyClass) iter.next();
			dumpClassFile( element );
		}
		return compileUnit;
	}

	protected void dumpClassFile(GroovyClass groovyClass)
	throws IOException
	{
		byte[] bytes = groovyClass.getBytes();

		File outputFile = createOutputFile( groovyClass.getName() );

		if ( ! outputFile.getParentFile().exists() )
		{
			outputFile.getParentFile().mkdirs();
		}


		FileOutputStream outputStream = new FileOutputStream( outputFile );
		
		try
		{
			outputStream.write( bytes,
					0,
					bytes.length );
		}
		finally
		{
			outputStream.close();
		}
	}

	protected File createOutputFile(String className)
	{
		String path = className.replace( '.',
				File.separatorChar ) + ".class" ;

		return new File( getOutputDir(),
				path );
	}

}

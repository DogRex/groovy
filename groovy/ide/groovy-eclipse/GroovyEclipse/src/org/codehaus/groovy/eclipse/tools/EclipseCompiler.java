/*
	public CompileUnit compile(CharStream[] sources, List results)
	throws Exception
	{
		CSTNode[] compilationUnits = new CSTNode[ sources.length ];

		for ( int i = 0 ; i < sources.length ; ++i )
		{
			try
			{
				compilationUnits[ i ] = stageOneCompile( sources[ i ] );
			}
			catch (Exception e)
			{
				if ( e instanceof SyntaxException )
				{
					((SyntaxException)e).setSourceLocator( sources[ i ].getDescription() );
				}
				this.errors.add( e );
			}
			finally
			{
					sources[ i ].close();
			}
		}

		if ( ! this.errors.isEmpty() )
		{
			MultiException exception = new MultiException( (Exception[]) this.errors.toArray( EMPTY_EXCEPTION_ARRAY ) );
			this.errors.clear();
			throw exception; 
		}


		CompileUnit unit = new CompileUnit();
		
		for ( int i = 0 ; i < compilationUnits.length ; ++i )
		{
			stageThreeCompile( unit, compilationUnits[ i ], sources[ i ] );
		}
		
		stageFourCompile(results, unit);

		return unit;
	}
	*/



/*
 * Created on 14-Jan-2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.codehaus.groovy.eclipse.tools;


import groovy.lang.CompilerConfig;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.codehaus.groovy.GroovyException;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.classgen.ClassGenerator;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.classgen.Verifier;
import org.codehaus.groovy.syntax.lexer.CharStream;
import org.codehaus.groovy.syntax.lexer.Lexer;
import org.codehaus.groovy.syntax.lexer.LexerTokenStream;
import org.codehaus.groovy.syntax.parser.ASTBuilder;
import org.codehaus.groovy.syntax.parser.CSTNode;
import org.codehaus.groovy.syntax.parser.Parser;
import org.codehaus.groovy.tools.CompilationFailuresException;
import org.codehaus.groovy.tools.CompilerBugException;
import org.codehaus.groovy.tools.CompilerClassLoader;
import org.codehaus.groovy.tools.ExceptionCollector;
import org.codehaus.groovy.tools.GroovyClass;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.DumpClassVisitor;

/**
 *  Controls the compilation process, from source to class generation.
 */

public class EclipseCompiler {
	private static final Exception[] EMPTY_EXCEPTION_ARRAY = new Exception[0];

	private Verifier verifier; // Verifies and completes ASTs before byte code generation
	private CompilerClassLoader classLoader; // Our class loader
	private CompilerConfig config = new CompilerConfig(); // compiler configuration
	private boolean verbose = false; // If set, extra output is generated
	private boolean debug = false; // If set, debugging output is generated

	private int maximumParseFailuresPerFile = 10; // Limits the number of parse errors before giving up
	private int maximumFailuresPerCompile = 15; // Limits the number of total errors before giving up

	//---------------------------------------------------------------------------
	// CONSTRUCTORS AND SETTINGS

	/**
	 *  Initializes the compiler.
	 */

	public EclipseCompiler () {
		this.verifier = new Verifier();
		this.classLoader = new CompilerClassLoader();
	}

	/**
	 *  Returns the compiler's class loader.
	 */

	protected CompilerClassLoader getClassLoader() {
		return this.classLoader;
	}

	/**
	 *  Controls the output verbosity.
	 */

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 *  Controls the presence of debugging output.
	 */

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 *  Adds additional paths to the class loader's search path.
	 */

	public void setClasspath(String classpath) throws Exception {
		StringTokenizer paths = new StringTokenizer(classpath, File.pathSeparator);

		while (paths.hasMoreTokens()) {
			getClassLoader().addPath(paths.nextToken());
		}
	}

	public CompilerConfig getConfig() {
		return config;
	}

	public void setConfig(CompilerConfig config) {
		this.config = config;
	}

	//---------------------------------------------------------------------------
	// COMPILATION

	/**
	 *  Compiles a set of <code>{@link CharStream}</code> sources.  Collects exceptions
	 *  during processing and throws a <code>{@link CompilationFailuresException}</code>
	 *  on error.  Other exceptions are bugs that need to be caught and encapsulated.
	 */

	
	public CompileUnit compile(CharStream[] sources, List classes) throws GroovyException {
		CompilationFailuresException failures = new CompilationFailuresException();

		//
		// First up, get at list of source files for error reporting
		// purposes.

		String[] descriptors = new String[sources.length];

		for (int i = 0; i < sources.length; ++i) {
			descriptors[i] = sources[i].getDescription();
			if (descriptors[i] == null) {
				descriptors[i] = "unknown" + i;
			}
		}

		//
		// Next, run parse the sources, producing a CST for each.

		CSTNode[] csts = new CSTNode[sources.length];

		for (int i = 0; i < sources.length; ++i) {
			try {
				csts[i] = parseSource(sources[i], descriptors[i]);
			}
			catch (ExceptionCollector e) {
				if (!e.isEmpty()) {
					failures.add(descriptors[i], e);
				}

				if (failures.total() > maximumFailuresPerCompile) {
					throw failures;
				}
			}
			catch( Exception e ) {
				throw new CompilerBugException( descriptors[i], "parse", e );
			}
			finally {
				try {
					sources[i].close();
				}
				catch (Exception e) {
				}
			}
		}

		//
		// If there were parse errors, bail out.

		if (!failures.isEmpty()) {
			throw failures;
		}

		//
		// Next, compile the CSTs to ASTs, and from there to classes.

		CompileUnit unit = new CompileUnit(getClassLoader(), config);
		

		for (int i = 0; i < csts.length; ++i) {
			try {
				ModuleNode ast = buildAST(csts[i], descriptors[i]);
				unit.addModule(ast);
			}
			catch (ExceptionCollector e) {
				if (!e.isEmpty()) {
					failures.add(descriptors[i], e);
				}
			}
			catch( Exception e ) {
				throw new CompilerBugException( descriptors[i], "AST creation", e );
			}
		}

		for (Iterator iter = unit.getModules().iterator(); iter.hasNext();) {
			ModuleNode module = (ModuleNode) iter.next();
			try {
				Iterator classNodes = module.getClasses().iterator();
				while (classNodes.hasNext()) {
					ClassNode classNode = (ClassNode) classNodes.next();

					if (verbose) {
						System.out.println("Generating class: " + classNode.getName());
					}

					classes.addAll(generateClasses(new GeneratorContext(unit), classNode, module.getDescription()));
				}
			}
			catch (ExceptionCollector e) {
				if (!e.isEmpty()) {
					failures.add(module.getDescription(), e);
				}
			}
			catch( Exception e ) {
				throw new CompilerBugException( module.getDescription(), "class generation", e );
			}
		}

		if (!failures.isEmpty()) {
			throw failures;
		}

		return unit;
	}

	/**
	 *  Parses a <code>CharStream</code> source, producing a Concrete
	 *  Syntax Tree (CST).  Lexing and parsing errors will be collected in 
	 *  an <code>ExceptionCollector</code>.
	 */

	protected CSTNode parseSource(CharStream charStream, String descriptor) throws ExceptionCollector, Exception {
		CSTNode tree = null;

		if (verbose) {
			System.out.println("Parsing: " + descriptor);
		}

		ExceptionCollector collector = new ExceptionCollector(maximumParseFailuresPerFile);

		try {
			Lexer lexer = new Lexer(charStream);
			Parser parser = new Parser(new LexerTokenStream(lexer), collector);

			collector.throwUnlessEmpty();
			tree = parser.compilationUnit();
		}
		catch (ExceptionCollector e) {
			collector.merge(e, false);
		}
		catch (GroovyException e) {
			collector.add(e, false);
		}

		collector.throwUnlessEmpty();

		return tree;
	}

	/**
	 *  Creates an Abstract Syntax Tree (AST) from the CST.
	 */

	protected ModuleNode buildAST(CSTNode cst, String descriptor) throws Exception {
		//        ExceptionCollector collector = new ExceptionCollector();

		ASTBuilder astBuilder = new ASTBuilder(getClassLoader()); // , collector );

		ModuleNode module = astBuilder.build(cst);
		module.setDescription(descriptor);

		//        collector.throwUnlessEmpty();
		return module;
	}

	/**
	 *  Generates a class from an AST.
	 */

	protected ArrayList generateClasses(GeneratorContext context, ClassNode classNode, String descriptor)
	throws Exception {
		ArrayList results = new ArrayList();
		ClassGenerator classGenerator = null;
		//        ExceptionCollector collector

		//
		// First, ensure the AST is in proper shape.

		verifier.visitClass(classNode);

		if (debug) {
			DumpClassVisitor dumpVisitor = new DumpClassVisitor(new PrintWriter(new OutputStreamWriter(System.out)));

			classGenerator = new ClassGenerator(context, dumpVisitor, getClassLoader(), descriptor);
			classGenerator.visitClass(classNode);
		}
		else {
			ClassWriter classWriter = new ClassWriter(true);

			classGenerator = new ClassGenerator(context, classWriter, getClassLoader(), descriptor);
			classGenerator.visitClass(classNode);

			byte[] bytes = classWriter.toByteArray();

			results.add(new GroovyClass(classNode.getName(), bytes));
		}

		LinkedList innerClasses = classGenerator.getInnerClasses();

		while (!innerClasses.isEmpty()) {
			results.addAll(generateClasses(context, (ClassNode) innerClasses.removeFirst(), descriptor));
		}

		return results;
		// return (GroovyClass[]) results.toArray( GroovyClass.EMPTY_ARRAY );
	}
}

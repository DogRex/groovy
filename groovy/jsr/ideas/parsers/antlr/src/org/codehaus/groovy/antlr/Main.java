package org.codehaus.groovy.antlr;
import java.io.*;
import antlr.collections.AST;
import antlr.collections.impl.*;
import antlr.debug.misc.*;
import antlr.*;
import com.thoughtworks.xstream.XStream;
import java.awt.event.*;

class Main {

	static boolean showTree = false;
    static boolean xml = false;
	static boolean verbose = false;
    public static void main(String[] args) {
		// Use a try/catch block for parser exceptions
		try {
			// if we have at least one command-line argument
			if (args.length > 0 ) {
				System.err.println("Parsing...");

				// for each directory/file specified on the command line
				for(int i=0; i< args.length;i++) {
					if ( args[i].equals("-showtree") ) {
						showTree = true;
					}
                    else if ( args[i].equals("-xml") ) {
                        xml = true;
                    }
					else if ( args[i].equals("-verbose") ) {
						verbose = true;
					}
					else if ( args[i].equals("-trace") ) {
						GroovyRecognizer.tracing = true;
						GroovyLexer.tracing = true;
					}
					else if ( args[i].equals("-traceParser") ) {
						GroovyRecognizer.tracing = true;
					}
					else if ( args[i].equals("-traceLexer") ) {
						GroovyLexer.tracing = true;
					}
					else {
						doFile(new File(args[i])); // parse it
					}
				} }
			else
				System.err.println("Usage: java -jar groovyc.jar [-showtree] [-xml] [-verbose] [-trace{,Lexer,Parser}]"+
                                   "<directory or file name>");
		}
		catch(Exception e) {
			System.err.println("exception: "+e);
			e.printStackTrace(System.err);   // so we can get stack trace
		}
	}


	// This method decides what action to take based on the type of
	//   file we are looking at
	public static void doFile(File f)
							  throws Exception {
		// If this is a directory, walk each file/dir in that directory
		if (f.isDirectory()) {
			String files[] = f.list();
			for(int i=0; i < files.length; i++)
				doFile(new File(f, files[i]));
		}

		// otherwise, if this is a groovy file, parse it!
		else if (f.getName().endsWith(".groovy")) {
			System.err.println(" --- "+f.getAbsolutePath());
			// parseFile(f.getName(), new FileInputStream(f));
			parseFile(f.getName(), new BufferedReader(new FileReader(f)));
		}
	}

	// Here's where we do the real work...
	public static void parseFile(String f, Reader r)
								 throws Exception {
		try {
			// Create a parser that reads from the scanner
			GroovyRecognizer parser = GroovyRecognizer.make(r);
			parser.setFilename(f);

			// start parsing at the compilationUnit rule
			parser.compilationUnit();
			
			System.out.println("parseFile "+f+" => "+parser.getAST());

			// do something with the tree
			doTreeAction(f, parser.getAST(), parser.getTokenNames());
		}
		catch (Exception e) {
			System.err.println("parser exception: "+e);
			e.printStackTrace();   // so we can get stack trace		
		}
	}
	
	public static void doTreeAction(String f, AST t, String[] tokenNames) {
		if ( t==null ) return;
		if ( showTree ) {
			((CommonAST)t).setVerboseStringConversion(true, tokenNames);
			ASTFactory factory = new ASTFactory();
			AST r = factory.create(0,"AST ROOT");
			r.setFirstChild(t);
			final ASTFrame frame = new ASTFrame("Groovy AST", r);
			frame.setVisible(true);
			frame.addWindowListener(
				new WindowAdapter() {
                   public void windowClosing (WindowEvent e) {
                       frame.setVisible(false); // hide the Frame
                       frame.dispose();
                       System.exit(0);
                   }
		        }
			);
			if (verbose)  System.out.println(t.toStringList());
		}
        if ( xml ) {
			((CommonAST)t).setVerboseStringConversion(true, tokenNames);
			ASTFactory factory = new ASTFactory();
			AST r = factory.create(0,"AST ROOT");
			r.setFirstChild(t);
            XStream xstream = new XStream();
            xstream.alias("ast", CommonAST.class);
			try {
                xstream.toXML(r,new FileWriter(f + ".xml"));
                System.out.println("Written AST to " + f + ".xml");
            } catch (Exception e) {
                System.out.println("couldn't write to " + f + ".xml");
                e.printStackTrace();
            }
			//if (verbose)  System.out.println(t.toStringList());
		}
	/*@todo
		GroovyTreeParser tparse = new GroovyTreeParser();
		try {
			tparse.compilationUnit(t);
			if (verbose)  System.out.println("successful walk of result AST for "+f);
		}
		catch (RecognitionException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	@todo*/

	}
}


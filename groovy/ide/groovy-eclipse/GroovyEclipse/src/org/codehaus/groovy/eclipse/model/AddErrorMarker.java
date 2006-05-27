/*
 * Copyright 2003(c)  Zohar Melamed
 * All rights reserved.


 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
 statements and notices.  Redistributions must also contain a
 copy of this document.

 2. Redistributions in binary form must reproduce the
 above copyright notice, this list of conditions and the
 following disclaimer in the documentation and/or other
 materials provided with the distribution.

 3. Due credit should be given to The Codehaus and Contributors
 http://timtam.codehaus.org/

 THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 *
 * Created on Mar 23, 2004
 *
 */

package org.codehaus.groovy.eclipse.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.control.ErrorCollector;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.syntax.SyntaxException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author MelamedZ
 * @author Alex Kramer
 */
class AddErrorMarker implements IWorkspaceRunnable {
	/**
	 * Constant for an empty char array
	 */
	public static final char[] NO_CHAR = new char[0];

	private static final int DEFAULT_READING_SIZE = 8192;

	private IFile fFile;
	private List fileList;  // list of IFile objects
	private Exception e;

	private List fileLineNumberList;

	/**
	 * 
	 * @param resource the file associated with the marker to be created.
	 * @param e exception for which to create a marker.
	 */
	public AddErrorMarker(List fileList, Exception e) {
		super();
		this.fileList = fileList;
		this.e = e;
	}
	private List getFileLineNumberList(IFile fFile){
		char[] fileContent = getPlainContent(fFile);
		List list = new ArrayList();
		list.add(new Integer(0));
		for (int i = 0; i < fileContent.length; i++) {
			if (fileContent[i]=='\n') {
				list.add(new Integer(i));
			}
		}
		return list;
	}
	/**
	 * Get the files content as String (Note: uses file.getCharset() to determine
	 * the streams character set)
	 * 
	 * @param file
	 * @return
	 */
	public static char[] getPlainContent(IFile file) {
		InputStream is = null;
		try {
			if (file.exists()) {
				is = file.getContents();
				return getInputStreamAsCharArray(is, -1, file.getCharset());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	/**
	 * Returns the given input stream's contents as a character array. If a length
	 * is specified (ie. if length != -1), only length chars are returned.
	 * Otherwise all chars in the stream are returned. Note this doesn't close the
	 * stream.
	 * 
	 * @throws IOException
	 *           if a problem occured reading the stream.
	 */
	public static String getInputStreamAsString(InputStream stream, String encoding) throws IOException {
		char[] array = getInputStreamAsCharArray(stream, -1, encoding);
		if (array != null) {
			return new String(array);
		}
		return null;
	}

	/**
	 * Returns the given input stream's contents as a character array. If a length
	 * is specified (ie. if length != -1), only length chars are returned.
	 * Otherwise all chars in the stream are returned. Note this doesn't close the
	 * stream.
	 * 
	 * @throws IOException
	 *           if a problem occured reading the stream.
	 */
	public static char[] getInputStreamAsCharArray(InputStream stream, int length, String encoding) throws IOException {
		InputStreamReader reader = null;
		reader = encoding == null ? new InputStreamReader(stream) : new InputStreamReader(stream, encoding);
		char[] contents;
		if (length == -1) {
			contents = NO_CHAR;
			int contentsLength = 0;
			int amountRead = -1;
			do {
				int amountRequested = Math.max(stream.available(), DEFAULT_READING_SIZE); // read at least 8K
				// resize contents if needed
				if (contentsLength + amountRequested > contents.length) {
					System.arraycopy(contents, 0, contents = new char[contentsLength + amountRequested], 0, contentsLength);
				}

				// read as many chars as possible
				amountRead = reader.read(contents, contentsLength, amountRequested);

				if (amountRead > 0) {
					// remember length of contents
					contentsLength += amountRead;
				}
			} while (amountRead != -1);

			// resize contents if necessary
			if (contentsLength < contents.length) {
				System.arraycopy(contents, 0, contents = new char[contentsLength], 0, contentsLength);
			}
		} else {
			contents = new char[length];
			int len = 0;
			int readSize = 0;
			while ((readSize != -1) && (len != length)) {
				// See PR 1FMS89U
				// We record first the read size. In this case len is the actual read size.
				len += readSize;
				readSize = reader.read(contents, len, length - len);
			}
			// See PR 1FMS89U
			// Now we need to resize in case the default encoding used more than one
			// byte for each character
			if (len != length)
				System.arraycopy(contents, 0, (contents = new char[len]), 0, len);
		}

		return contents;
	}

	public void run(IProgressMonitor monitor) throws CoreException {
		if (e instanceof MultipleCompilationErrorsException) {
			Map fileNameIFileMap = new HashMap();
			for(Iterator iter = fileList.iterator();iter.hasNext();){
				IFile f = (IFile)iter.next();
				fileNameIFileMap.put(f.getLocation().toOSString(),f);
			}
			MultipleCompilationErrorsException multiple = (MultipleCompilationErrorsException) e;
			ErrorCollector collector = multiple.getErrorCollector();
			for (int i = 0; i < collector.getErrorCount(); ++i) {
				SyntaxException exception = collector.getSyntaxError(i);
				// This replaces all double backslash with a single backslash
				// Yes - it takes 4 backslash to equal one for the Java regular expressions
				String key = exception.getSourceLocator().replaceAll("\\\\\\\\","\\\\");
				fFile = (IFile) fileNameIFileMap.get(key);
				fileLineNumberList = getFileLineNumberList(fFile);
				markerFromException(collector.getException(i));
			}
		} else {
			fFile = (IFile) fileList.get(0);
			markerFromException(e);
		}
	}

	/**
	 * @param exception
	 */
	private void markerFromException(Exception exception) throws CoreException {
		int line = 0, startCol = 0, endCol = 0;
		StringWriter trace;
		if (exception instanceof SyntaxException) {
			SyntaxException se = (SyntaxException) exception;
			line = se.getLine();
			startCol = se.getStartColumn();
			endCol = se.getEndColumn();
			trace = new StringWriter();
			se.printStackTrace(new PrintWriter(trace));
			createMarker(IMarker.SEVERITY_ERROR, exception.getMessage(), trace.toString(), line, startCol, endCol);
		} else {
			trace = new StringWriter();
			exception.printStackTrace(new PrintWriter(trace));
			createMarker(IMarker.SEVERITY_ERROR, exception.getMessage(), trace.toString(), line, -1, endCol);
		}
	}

	private void createMarker(int severity, String message, String stackTrace, int line, int startCol, int endCol)
			throws CoreException { 
		GroovyPlugin.trace("creating marker in " + fFile.getName());
		IMarker marker = fFile.createMarker(GroovyProject.GROOVY_ERROR_MARKER); //$NON-NLS-1$
		Map map = new HashMap(3);
		map.put(IMarker.SEVERITY, new Integer(severity));
		map.put(IMarker.LINE_NUMBER, new Integer(line));
		map.put(IMarker.MESSAGE, message);
		Integer i;
		if (startCol>=0 && fileLineNumberList.size()>line-1) {
			// calculate the start- and end-offset of the error
			i = (Integer) fileLineNumberList.get(line-1);
		  map.put(IMarker.CHAR_START, new Integer(i.intValue()+startCol));
		  map.put(IMarker.CHAR_END, new Integer(i.intValue()+endCol));
		}
		map.put("trace", stackTrace); //$NON-NLS-1$
		marker.setAttributes(map);
	}
}
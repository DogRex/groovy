package org.codehaus.groovy.eclipse.codebrowsing;

/**
 * Get the current declaration search context.
 * 
 * This is an optional interface to implement. Most declaration search
 * processors will use the global context. However some declaration searches are
 * specific to some context.
 * 
 * For example, a Grails application can have declaration search processors
 * implemented for navigation between domain, view and controller classes. These
 * have no purpose in any other projects. An implementation of
 * IDeclarationSearchContext that determines if the current project is a Grails
 * project is created to limit the scope of the Grails specific search
 * processors.
 * 
 * @author emp
 */
public interface IDeclarationSearchContext {
	public String getCurrentContext();
}
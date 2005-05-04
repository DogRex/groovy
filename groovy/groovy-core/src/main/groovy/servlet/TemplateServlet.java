/*
 * $Id$
 * 
 * Copyright 2003 (C) James Strachan and Bob Mcwhirter. All Rights Reserved.
 * 
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The name "groovy" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Codehaus. For
 * written permission, please contact info@codehaus.org.
 * 
 * 4. Products derived from this Software may not be called "groovy" nor may
 * "groovy" appear in their names without prior written permission of The
 * Codehaus. "groovy" is a registered trademark of The Codehaus.
 * 
 * 5. Due credit should be given to The Codehaus - http://groovy.codehaus.org/
 * 
 * THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 */
package groovy.servlet;

import groovy.lang.Binding;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import groovy.text.TemplateEngine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A generic servlet for templates.
 * 
 * It wraps a <code>groovy.text.TemplateEngine</code> to process HTTP
 * requests. By default, it uses the
 * <code>groovy.text.SimpleTemplateEngine</code> which interprets JSP-like (or
 * Canvas-like) templates. <br>
 * <br>
 * 
 * Example <code>HelloWorld.template</code>:
 * 
 * <pre><code>
 * 
 *  &lt;html&gt;
 *  &lt;body&gt;
 *  &lt;% 3.times { %&gt;
 *  Hello World!
 * <br>
 * 
 *  &lt;% } %&gt;
 *  &lt;/body&gt;
 *  &lt;/html&gt; 
 *  
 * </code></pre>
 * 
 * <br>
 * <br>
 * 
 * Note: <br>
 * Automatic binding of context variables and request (form) parameters is
 * disabled by default. You can enable it by setting the servlet config init
 * parameters to <code>true</code>.
 * 
 * <pre><code>
 * bindDefaultVariables = init(&quot;bindDefaultVariables&quot;, false);
 * bindRequestParameters = init(&quot;bindRequestParameters&quot;, false);
 * </code></pre>
 * 
 * @author <a mailto:sormuras@web.de>Christian Stein </a>
 * @author Guillaume Laforge
 * @version 1.3
 */
public class TemplateServlet extends HttpServlet {

    public static final String DEFAULT_CONTENT_TYPE = "text/html";

    private ServletContext servletContext;

    protected TemplateEngine templateEngine;

    /**
     * Initializes the servlet.
     * 
     * @param config
     *            Passed by the servlet container.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        /*
         * Save the context.
         */
        this.servletContext = config.getServletContext();

        /*
         * BEGIN
         */
        String className = getClass().getName();
        servletContext.log("Initializing on " + className + "...");

        /*
         * Get TemplateEngine instance.
         */
        this.templateEngine = createTemplateEngine(config);
        if (templateEngine == null) { throw new RuntimeException("Template engine not instantiated."); }

        /*
         * END;
         */
        String engineName = templateEngine.getClass().getName();
        servletContext.log(className + " initialized on " + engineName + ".");
    }

    /**
     * Convient evaluation of boolean configuration parameters.
     * 
     * @return <code>true</code> or <code>false</code>.
     * @param config
     *            Servlet configuration passed by the servlet container.
     * @param param
     *            Name of the paramter to look up.
     * @param value
     *            Default value if parameter name is not set.
     */
    protected boolean init(ServletConfig config, String param, boolean value) {
        String string = config.getInitParameter(param);
        if (string == null) { return value; }
        return Boolean.valueOf(string).booleanValue();
    }

    /**
     * Creates the template engine.
     * 
     * Called by {@link #init(ServletConfig)} and returns just <code>
     * SimpleTemplateEngine()</code> if the init parameter <code>templateEngine</code>
     * is not set.
     * 
     * @return The underlying template engine.
     * @param config
     *            This serlvet configuration passed by the container.
     * @see #createTemplateEngine(javax.servlet.ServletConfig)
     */
    protected TemplateEngine createTemplateEngine(ServletConfig config) {
        String templateEngineClassName = config.getInitParameter("templateEngine");
        if (templateEngineClassName == null) {
            return new SimpleTemplateEngine();
        }
        try {
            return (TemplateEngine) Class.forName(templateEngineClassName).newInstance();
        } catch (InstantiationException e) {
            servletContext.log("Could not instantiate template engine: " + templateEngineClassName, e);
        } catch (IllegalAccessException e) {
            servletContext.log("Could not access template engine class: " + templateEngineClassName, e);
        } catch (ClassNotFoundException e) {
            servletContext.log("Could not find template engine class: " + templateEngineClassName, e);
       }
        return null;
    }

    /**
     * Delegates to {@link #doRequest(HttpServletRequest, HttpServletResponse)}.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequest(request, response);
    }

    /**
     * Delegates to {@link #doRequest(HttpServletRequest, HttpServletResponse)}.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequest(request, response);
    }

    /**
     * Processes all requests by dispatching to helper methods.
     * 
     * TODO Outline the algorithm. Although the method names are well-chosen. :)
     * 
     * @param request
     *            The http request.
     * @param response
     *            The http response.
     * @throws ServletException
     *             ...
     */
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        Binding binding = null;

        try {

            /*
             * Create binding.
             */
            binding = new ServletBinding(request, response, servletContext);

            /*
             * Set default content type.
             */
            setContentType(request, response);

            /*
             * Create the template by its engine.
             */
            Template template = handleRequest(request, response, binding);

            /*
             * Let the template, that is groovy, do the merge.
             */
            merge(template, binding, response);

        }
        catch (Exception exception) {

            /*
             * Call exception handling hook.
             */
            error(request, response, exception);

        }
        finally {

            /*
             * Indicate we are finally done with this request.
             */
            requestDone(request, response, binding);

        }

    }

    /**
     * Sets {@link #DEFAULT_CONTENT_TYPE}.
     * 
     * @param request
     *            The HTTP request.
     * @param response
     *            The HTTP response.
     */
    protected void setContentType(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType(DEFAULT_CONTENT_TYPE);

    }

    /**
     * Default request handling. <br>
     * 
     * Leaving Velocity behind again. The template, actually the Groovy code in
     * it, could handle/process the entire request. Good or not? This depends on
     * you! :)<br>
     * 
     * Anyway, here no exception is thrown -- but it's strongly recommended to
     * override this method in derived class and do the real processing against
     * the model inside it. The template should be used, like Velocity
     * templates, to produce the view, the html page. Again, it's up to you!
     * 
     * @return The template that will be merged.
     * @param request
     *            The HTTP request.
     * @param response
     *            The HTTP response.
     * @param binding
     *            The application context.
     * @throws Exception
     */
    protected Template handleRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            Binding binding)
    throws Exception {
        /*
         * Delegate to getTemplate(String).
         */
        return getTemplate(request);
    }

    /**
     * Gets the template by its name.
     * 
     * @return The template that will be merged.
     * @param request
     *            The HttpServletRequest.
     * @throws Exception
     *             Any exception.
     */
    protected Template getTemplate(HttpServletRequest request) throws Exception {

        /*
         * If its an include we need to get the included path, not the main request path.
         */
        String path = (String) request.getAttribute("javax.servlet.include.servlet_path");
        if (path == null) {
            path = request.getServletPath();
        }

        /*
         * Delegate to resolveTemplateName(String). Twice if necessary.
         */
        URL url = resolveTemplateName(path);
        if (url == null) {
            url = resolveTemplateName(request.getRequestURI());
        }

        /*
         * Template not found?
         */
        if (url == null) {
            String uri = request.getRequestURI();
            servletContext.log("Resource \"" + uri + "\" not found.");
            throw new FileNotFoundException(uri);
        }

        /*
         * Delegate to getTemplate(URL).
         */
        return getTemplate(url);

    }

    /**
     * Locate template and convert its location to an URL.
     * 
     * @return The URL pointing to the resource... the template.
     * @param templateName
     *            The name of the template.
     * @throws Exception
     *             Any exception.
     */
    protected URL resolveTemplateName(String templateName) throws Exception {

        /*
         * Try servlet context resource facility.
         * 
         * Good for names pointing to templates relatively to the servlet
         * context.
         */
        URL url = servletContext.getResource(templateName);
        if (url != null) { return url; }

        /*
         * Precedence: Context classloader, Class classloader
         * (those classloaders will delegate to the system classloader)
         */
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        url = classLoader.getResource(templateName);
        if (url != null) { return url; }

        /*
         * Try the class loader, that loaded this class.
         * 
         * Good for templates located within the class path.
         * 
         */
        url = getClass().getResource(templateName);
        if (url != null) { return url; }

        /*
         * Still, still here? Just return null.
         */
        return null;

    }

    /**
     * Gets the template by its url.
     * 
     * @return The template that will be merged.
     * @param templateURL
     *            The url of the template.
     * @throws Exception
     *             Any exception.
     */
    protected Template getTemplate(URL templateURL) throws Exception {

        /*
         * Let the engine create the template from given URL.
         * 
         * TODO Is createTemplate(Reader); faster? Fail safer?
         */
        return templateEngine.createTemplate(templateURL);

    }

    /**
     * Merges the template and writes response.
     * 
     * @param template
     *            The template that will be merged... now!
     * @param binding
     *            The application context.
     * @param response
     *            The HTTP response.
     * @throws Exception
     *             Any exception.
     */
    protected void merge(Template template, Binding binding, HttpServletResponse response) throws Exception {

        /*
         * Set binding and write response.
         */
        template.make(binding.getVariables()).writeTo((Writer) binding.getVariable("out"));

    }

    /**
     * Simply sends an internal server error page (code 500).
     * 
     * @param request
     *            The HTTP request.
     * @param response
     *            The HTTP response.
     * @param exception
     *            The cause.
     */
    protected void error(HttpServletRequest request, HttpServletResponse response, Exception exception) {

        try {
            response.sendError(500, exception.getMessage());
        }
        catch (IOException ioException) {
            servletContext.log("Should not happen.", ioException);
        }

    }

    /**
     * Called one request is processed.
     * 
     * This clean-up hook is always called, even if there was an exception
     * flying around and the error method was executed.
     * 
     * @param request
     *            The HTTP request.
     * @param response
     *            The HTTP response.
     * @param binding
     *            The application context.
     */
    protected void requestDone(HttpServletRequest request, HttpServletResponse response, Binding binding) {

        /*
         * Nothing to clean up.
         */
        return;

    }

}
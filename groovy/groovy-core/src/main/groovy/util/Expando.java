/*
 $Id$

 Copyright 2003 (C) James Strachan and Bob Mcwhirter. All Rights Reserved.

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

 3. The name "groovy" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Codehaus.  For written permission,
    please contact info@codehaus.org.

 4. Products derived from this Software may not be called "groovy"
    nor may "groovy" appear in their names without prior written
    permission of The Codehaus. "groovy" is a registered
    trademark of The Codehaus.

 5. Due credit should be given to The Codehaus -
    http://groovy.codehaus.org/

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

 */
package groovy.util;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaExpandoProperty;
import groovy.lang.MissingPropertyException;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Represents a dynamically expandable bean.
 * 
 * @author <a href="mailto:james@coredevelopers.net">James Strachan</a>
 * @author Hein Meling
 * @author Pilho Kim
 * @version $Revision$
 */
public class Expando extends GroovyObjectSupport {

    private Map expandoProperties;

    public Expando() {
    }

    public Expando(Map expandoProperties) {
        this.expandoProperties = expandoProperties;
    }

    /**
     * @return the dynamically expanded properties
     */
    public Map getProperties() {
        if (expandoProperties == null) {
            expandoProperties = createMap();
        }
        return expandoProperties;
    }

    public List getMetaPropertyValues() {
        // run through all our current properties and create MetaProperty objects
        List ret = new ArrayList();
        Iterator itr = getProperties().entrySet().iterator();
        while(itr.hasNext()) {
            Entry entry = (Entry) itr.next();
            ret.add(new MetaExpandoProperty(entry));
        }
		
        return ret;
    }

    public Object getProperty(String property) {
        // always use the expando properties first
        Object result = getProperties().get(property);
        if (result!=null) return result;
        try {
            return super.getProperty(property);
        }
        catch (MissingPropertyException e) {}
        return null;
    }

    public void setProperty(String property, Object newValue) {
        // always use the expando properties
        getProperties().put(property, newValue);
    }

    public Object invokeMethod(String name, Object args) {
        try {
            return super.invokeMethod(name, args);
        }
        catch (GroovyRuntimeException e) {
            // br should get a "native" property match first. getProperty includes such fall-back logic
            Object value = this.getProperty(name);
            if (value instanceof Closure) {
                Closure closure = (Closure) value;
                closure.setDelegate(this);
                return closure.call((Object[]) args);
            }
            else {
                throw e;
            }
        }
        
    }
    
    /**
     * This allows toString to be overridden by a closure <i>field</i> method attached
     * to the expando object.
     * 
     * @see java.lang.Object#toString()
     */
     public String toString() {
        Object method = getProperties().get("toString");
        if (method != null && method instanceof Closure) {
            // invoke overridden toString closure method
            Closure closure = (Closure) method;
            closure.setDelegate(this);
            return closure.call().toString();
        } else {
            return expandoProperties.toString();
        }
     }

    /**
     * This allows equals to be overridden by a closure <i>field</i> method attached
     * to the expando object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        Object method = getProperties().get("equals");
        if (method != null && method instanceof Closure) {
            // invoke overridden equals closure method
            Closure closure = (Closure) method;
            closure.setDelegate(this);
            Boolean ret = (Boolean) closure.call(obj);
            return ret.booleanValue();
        } else {
            return super.equals(obj);
        }
    }

    /**
     * This allows hashCode to be overridden by a closure <i>field</i> method attached
     * to the expando object.
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        Object method = getProperties().get("hashCode");
        if (method != null && method instanceof Closure) {
            // invoke overridden hashCode closure method
            Closure closure = (Closure) method;
            closure.setDelegate(this);
            Integer ret = (Integer) closure.call();
            return ret.intValue();
        } else {
            return super.hashCode();
        }
    }

    /**
     * Factory method to create a new Map used to store the expando properties map
     * @return a newly created Map implementation
     */
    protected Map createMap() {
        return new HashMap();
    }

}

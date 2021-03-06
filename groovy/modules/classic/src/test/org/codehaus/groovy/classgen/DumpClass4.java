/*
 * $Id$
 * 
 * Copyright 2003 (C) James Strachan and Bob Mcwhirter. All Rights Reserved.
 * 
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *  1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *  2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *  3. The name "groovy" must not be used to endorse or promote products
 * derived from this Software without prior written permission of The Codehaus.
 * For written permission, please contact info@codehaus.org.
 *  4. Products derived from this Software may not be called "groovy" nor may
 * "groovy" appear in their names without prior written permission of The
 * Codehaus. "groovy" is a registered trademark of The Codehaus.
 *  5. Due credit should be given to The Codehaus - http://groovy.codehaus.org/
 * 
 * THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 *  
 */

package org.codehaus.groovy.classgen;

import groovy.lang.Reference;

import org.codehaus.groovy.runtime.InvokerHelper;

/**
 * This is a scratch class used to experiment with ASM to see what kind of
 * stuff is output for normal Java code
 * 
 * @author <a href="mailto:james@coredevelopers.net">James Strachan</a>
 * @version $Revision$
 */
public class DumpClass4 {

    public DumpClass4() {
    }

    public static void main(String[] args) {
        Object foo = InvokerHelper.invokeConstructorOf(DumpClass4.class, null);
    }

    public void run() {
        synchronized (this) {
            InvokerHelper.invokeMethod(this, "foo", null);
        }
        InvokerHelper.invokeMethod(this, "bar", null);
    }

    public void throwException() {
        throw (RuntimeException) InvokerHelper.invokeConstructor("java.lang.RuntimeException", "Hello");
    }

    public void switchTest(int i) {
        String x = "foo";

        switch (i) {
            case 1 :
                x = "1";
                break;
            case 2 :
                x = "2";
            case 3 :
                x = "3";
                break;
            default :
                x = "default";
        }
        System.out.println(x);
    }

    public Object createReferenceTest() {
        Reference foo = new Reference();
        foo.set(new Integer(12));
        return foo.get();
    }

    public static void makeInstance() {
        InvokerHelper.invokeConstructorOf(DumpClass4.class, null);
    }

    public void makeNestedArray() {
        InvokerHelper.invokeMethod(
            "outer",
            "foo",
            new Object[] { InvokerHelper.invokeMethod("inner", "plus", new Object[] { "1" })
        });
    }

    public void makeNestedEmptyArray() {
        InvokerHelper
            .invokeMethod("outer", "foo", new Object[] { InvokerHelper.invokeMethod("inner", "plus", new Object[] {
            })
            });
    }

    public Object makeAnotherArray(Object a, Object b) {
        return new Object[] { a, b };
    }

    public void tryFinally() {
        try {
            System.out.println("Try");
        }
        finally {
            System.out.println("Finally");
        }
    }

    public void tryCatchFinally() {
        try {
            System.out.println("Try");
        }
        catch (Exception e) {
            System.out.println("Catch");
        }
        finally {
            System.out.println("Finally");
        }
    }

    public void emptyTryCatch() {
        try {
        }
        catch (Throwable t) {
        }
    }
    
    public void usePrimitiveType() {
        System.out.println(int.class);
    }
    
    public String testSuperCall() {
        return super.toString();
    }
    
    public Object createReference(Object foo) {
        return new Reference(foo);
    }
}

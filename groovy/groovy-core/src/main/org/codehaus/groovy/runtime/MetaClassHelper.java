/*
 * Copyright 2005 John G. Wilson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.codehaus.groovy.runtime;

import groovy.lang.Closure;
import groovy.lang.GString;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaMethod;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author John Wilson
 *
 */

public class MetaClassHelper {

    public static final Object[] EMPTY_ARRAY = {};
    public static Class[] EMPTY_TYPE_ARRAY = {};
    protected static final Object[] ARRAY_WITH_NULL = { null };
    protected static final Logger log = Logger.getLogger(MetaClassHelper.class.getName());
    private static final int MAX_ARG_LEN = 12;
    
    public static boolean accessibleToConstructor(final Class at, final Constructor constructor) {
        boolean accessible = false;
        if (Modifier.isPublic(constructor.getModifiers())) {
            accessible = true;
        }
        else if (Modifier.isPrivate(constructor.getModifiers())) {
            accessible = at.getName().equals(constructor.getName());
        }
        else if ( Modifier.isProtected(constructor.getModifiers()) ) {
            if ( at.getPackage() == null && constructor.getDeclaringClass().getPackage() == null ) {
                accessible = true;
            }
            else if ( at.getPackage() == null && constructor.getDeclaringClass().getPackage() != null ) {
                accessible = false;
            }
            else if ( at.getPackage() != null && constructor.getDeclaringClass().getPackage() == null ) {
                accessible = false;
            }
            else if ( at.getPackage().equals(constructor.getDeclaringClass().getPackage()) ) {
                accessible = true;
            }
            else {
                boolean flag = false;
                Class clazz = at;
                while ( !flag && clazz != null ) {
                    if (clazz.equals(constructor.getDeclaringClass()) ) {
                        flag = true;
                        break;
                    }
                    if (clazz.equals(Object.class) ) {
                        break;
                    }
                    clazz = clazz.getSuperclass();
                }
                accessible = flag;
            }
        }
        else {
            if ( at.getPackage() == null && constructor.getDeclaringClass().getPackage() == null ) {
                accessible = true;
            }
            else if ( at.getPackage() == null && constructor.getDeclaringClass().getPackage() != null ) {
                accessible = false;
            }
            else if ( at.getPackage() != null && constructor.getDeclaringClass().getPackage() == null ) {
                accessible = false;
            }
            else if ( at.getPackage().equals(constructor.getDeclaringClass().getPackage()) ) {
                accessible = true;
            }
        }
        return accessible;
    }
    
    public static Object[] asWrapperArray(Object parameters, Class componentType) {
        Object[] ret=null;
        if (componentType == boolean.class) {
            boolean[] array = (boolean[]) parameters;
            ret = new Object[array.length];
            for (int i=0; i<array.length; i++) {
                ret[i] = new Boolean(array[i]);
            }
        } else if (componentType == char.class) {
            char[] array = (char[]) parameters;
            ret = new Object[array.length];
            for (int i=0; i<array.length; i++) {
                ret[i] = new Character(array[i]);
            }
        } else if (componentType == byte.class) {
            byte[] array = (byte[]) parameters;
            ret = new Object[array.length];
            for (int i=0; i<array.length; i++) {
                ret[i] = new Byte(array[i]);
            }
        } else if (componentType == int.class) {
            int[] array = (int[]) parameters;
            ret = new Object[array.length];
            for (int i=0; i<array.length; i++) {
                ret[i] = new Integer(array[i]);
            }
        } else if (componentType == short.class) {
            short[] array = (short[]) parameters;
            ret = new Object[array.length];
            for (int i=0; i<array.length; i++) {
                ret[i] = new Short(array[i]);
            }
        } else if (componentType == long.class) {
            long[] array = (long[]) parameters;
            ret = new Object[array.length];
            for (int i=0; i<array.length; i++) {
                ret[i] = new Long(array[i]);
            }
        } else if (componentType == double.class) {
            double[] array = (double[]) parameters;
            ret = new Object[array.length];
            for (int i=0; i<array.length; i++) {
                ret[i] = new Double(array[i]);
            }
        } else if (componentType == float.class) {
            float[] array = (float[]) parameters;
            ret = new Object[array.length];
            for (int i=0; i<array.length; i++) {
                ret[i] = new Float(array[i]);
            }
        }
        
        return ret;
    }
    
    
    /**
     * @param list
     * @param parameterType
     * @return
     */
    public static Object asPrimitiveArray(List list, Class parameterType) {
        Class arrayType = parameterType.getComponentType();
        Object objArray = Array.newInstance(arrayType, list.size());
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            if (arrayType.isPrimitive()) {
                if (obj instanceof Integer) {
                    Array.setInt(objArray, i, ((Integer) obj).intValue());
                }
                else if (obj instanceof Double) {
                    Array.setDouble(objArray, i, ((Double) obj).doubleValue());
                }
                else if (obj instanceof Boolean) {
                    Array.setBoolean(objArray, i, ((Boolean) obj).booleanValue());
                }
                else if (obj instanceof Long) {
                    Array.setLong(objArray, i, ((Long) obj).longValue());
                }
                else if (obj instanceof Float) {
                    Array.setFloat(objArray, i, ((Float) obj).floatValue());
                }
                else if (obj instanceof Character) {
                    Array.setChar(objArray, i, ((Character) obj).charValue());
                }
                else if (obj instanceof Byte) {
                    Array.setByte(objArray, i, ((Byte) obj).byteValue());
                }
                else if (obj instanceof Short) {
                    Array.setShort(objArray, i, ((Short) obj).shortValue());
                }
            }
            else {
                Array.set(objArray, i, obj);
            }
        }
        return objArray;
    }
    
    protected static Class autoboxType(Class type) {
        if (type.isPrimitive()) {
            if (type == int.class) {
                return Integer.class;
            }
            else if (type == double.class) {
                return Double.class;
            }
            else if (type == long.class) {
                return Long.class;
            }
            else if (type == boolean.class) {
                return Boolean.class;
            }
            else if (type == float.class) {
                return Float.class;
            }
            else if (type == char.class) {
                return Character.class;
            }
            else if (type == byte.class) {
                return Byte.class;
            }
            else if (type == short.class) {
                return Short.class;
            }
        }
        return type;
    }
    
    public static int calculateParameterDistance(Class[] arguments, Class[] parameters) {
        int dist=0;
        for (int i=0; i<arguments.length; i++) {
            if (parameters[i]==arguments[i]) continue;
            
            if (parameters[i].isInterface()) {
                dist+=3;
                continue;
            }
            
            if (arguments[i]!=null) {
                if (autoboxType(parameters[i]) == autoboxType(arguments[i])){
                    // type is not equal, but boxed types are. Increase distance 
                    // by 1 to reflect the change in type
                    dist +=1;
                    continue;
                }
                if (arguments[i].isPrimitive() || parameters[i].isPrimitive()) {
                    // type is not equal, increase distance by 2 to reflect
                    // the change in type
                    dist+=2;
                    continue;
                }
                
                // add one to dist to be sure interfaces are prefered
                dist++;
                Class clazz = arguments[i];
                while (clazz!=null) {
                    if (clazz==parameters[i]) break;
                    if (clazz==GString.class && parameters[i]==String.class) {
                        dist+=2;
                        break;
                    }
                    clazz = clazz.getSuperclass();
                    dist+=3;
                }
            } else {
                // choose the distance to Object if a parameter is null
                // this will mean that Object is prefered over a more
                // specific type
                // remove one to dist to be sure Object is prefered
                dist--;
                Class clazz = parameters[i];
                while (clazz!=Object.class) {
                    clazz = clazz.getSuperclass();
                    dist+=2;
                }
            }
        }
        return dist;
    }
    
    public static String capitalize(String property) {
        return property.substring(0, 1).toUpperCase() + property.substring(1, property.length());
    }
    
    /**
     * @return the method with 1 parameter which takes the most general type of
     *         object (e.g. Object)
     */
    public static Object chooseEmptyMethodParams(List methods) {
        for (Iterator iter = methods.iterator(); iter.hasNext();) {
            Object method = iter.next();
            Class[] paramTypes = getParameterTypes(method);
            int paramLength = paramTypes.length;
            if (paramLength == 0) {
                return method;
            }
        }
        return null;
    }
    
    /**
     * @return the method with 1 parameter which takes the most general type of
     *         object (e.g. Object) ignoring primitve types
     */
    public static Object chooseMostGeneralMethodWith1NullParam(List methods) {
        // lets look for methods with 1 argument which matches the type of the
        // arguments
        Class closestClass = null;
        Object answer = null;
        
        for (Iterator iter = methods.iterator(); iter.hasNext();) {
            Object method = iter.next();
            Class[] paramTypes = getParameterTypes(method);
            int paramLength = paramTypes.length;
            if (paramLength == 1) {
                Class theType = paramTypes[0];
                if (theType.isPrimitive()) continue;
                if (closestClass == null || isAssignableFrom(theType, closestClass)) {
                    closestClass = theType;
                    answer = method;
                }
            }
        }
        return answer;
    }
    
    /**
     * Coerces any GString instances into Strings
     *
     * @return true if some coercion was done.
     */
    public static boolean coerceGStrings(Object[] arguments) {
        boolean coerced = false;
        for (int i = 0, size = arguments.length; i < size; i++) {
            Object argument = arguments[i];
            if (argument instanceof GString) {
                arguments[i] = argument.toString();
                coerced = true;
            } else if (argument instanceof GString[]) {
            	GString[] gstrings = (GString[]) arguments[i];
            	String[] strings = new String[gstrings.length];
            	for (int j=0; j<gstrings.length; j++) {
            		if (gstrings[j]==null) continue;
            		strings[j]=gstrings[j].toString();
            	}
            	arguments[i] = strings;
            	coerced=true;
            }
        }
        return coerced;
    }
    
    protected static Object[] coerceNumbers(MetaMethod method, Object[] arguments) {
        Object[] ans = null;
        boolean coerced = false; // to indicate that at least one param is coerced
        
        Class[] params = method.getParameterTypes();
        
        if (params.length != arguments.length) {
            return null;
        }
        
        ans = new Object[arguments.length];
        
        for (int i = 0, size = arguments.length; i < size; i++) {
            Object argument = arguments[i];
            Class param = params[i];
            if ((Number.class.isAssignableFrom(param) || param.isPrimitive()) && argument instanceof Number) { // Number types
                if (param == Byte.class || param == Byte.TYPE ) {
                    ans[i] = new Byte(((Number)argument).byteValue());
                    coerced = true; continue;
                }
                if (param == Double.class || param == Double.TYPE) {
                    ans[i] = new Double(((Number)argument).doubleValue());
                    coerced = true; continue;
                }
                if (param == Float.class || param == Float.TYPE) {
                    ans[i] = new Float(((Number)argument).floatValue());
                    coerced = true; continue;
                }
                if (param == Integer.class || param == Integer.TYPE) {
                    ans[i] = new Integer(((Number)argument).intValue());
                    coerced = true; continue;
                }
                if (param == Long.class || param == Long.TYPE) {
                    ans[i] = new Long(((Number)argument).longValue());
                    coerced = true; continue;
                }
                if (param == Short.class || param == Short.TYPE) {
                    ans[i] = new Short(((Number)argument).shortValue());
                    coerced = true; continue;
                }
                if (param == BigDecimal.class ) {
                    ans[i] = new BigDecimal(((Number)argument).doubleValue());
                    coerced = true; continue;
                }
                if (param == BigInteger.class) {
                    ans[i] = new BigInteger(String.valueOf(((Number)argument).longValue()));
                    coerced = true; continue;
                }
            }
            else if (param.isArray() && argument.getClass().isArray()) {
                Class paramElem = param.getComponentType();
                if (paramElem.isPrimitive()) {
                    if (paramElem == boolean.class && argument.getClass().getName().equals("[Ljava.lang.Boolean;")) {
                        ans[i] = InvokerHelper.convertToBooleanArray(argument);
                        coerced = true;
                        continue;
                    }
                    if (paramElem == byte.class && argument.getClass().getName().equals("[Ljava.lang.Byte;")) {
                        ans[i] = InvokerHelper.convertToByteArray(argument);
                        coerced = true;
                        continue;
                    }
                    if (paramElem == char.class && argument.getClass().getName().equals("[Ljava.lang.Character;")) {
                        ans[i] = InvokerHelper.convertToCharArray(argument);
                        coerced = true;
                        continue;
                    }
                    if (paramElem == short.class && argument.getClass().getName().equals("[Ljava.lang.Short;")) {
                        ans[i] = InvokerHelper.convertToShortArray(argument);
                        coerced = true;
                        continue;
                    }
                    if (paramElem == int.class && argument.getClass().getName().equals("[Ljava.lang.Integer;")) {
                        ans[i] = InvokerHelper.convertToIntArray(argument);
                        coerced = true;
                        continue;
                    }
                    if (paramElem == long.class
                            && argument.getClass().getName().equals("[Ljava.lang.Long;")
                            && argument.getClass().getName().equals("[Ljava.lang.Integer;")
                    ) {
                        ans[i] = InvokerHelper.convertToLongArray(argument);
                        coerced = true;
                        continue;
                    }
                    if (paramElem == float.class
                            && argument.getClass().getName().equals("[Ljava.lang.Float;")
                            && argument.getClass().getName().equals("[Ljava.lang.Integer;")
                    ) {
                        ans[i] = InvokerHelper.convertToFloatArray(argument);
                        coerced = true;
                        continue;
                    }
                    if (paramElem == double.class &&
                            argument.getClass().getName().equals("[Ljava.lang.Double;") &&
                            argument.getClass().getName().equals("[Ljava.lang.BigDecimal;") &&
                            argument.getClass().getName().equals("[Ljava.lang.Float;")) {
                        ans[i] = InvokerHelper.convertToDoubleArray(argument);
                        coerced = true;
                        continue;
                    }
                }
            }
        }
        return coerced ? ans : null;
    }
    
    /**
     * @return true if a method of the same matching prototype was found in the
     *         list
     */
    public static boolean containsMatchingMethod(List list, MetaMethod method) {
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            MetaMethod aMethod = (MetaMethod) iter.next();
            Class[] params1 = aMethod.getParameterTypes();
            Class[] params2 = method.getParameterTypes();
            if (params1.length == params2.length) {
                boolean matches = true;
                for (int i = 0; i < params1.length; i++) {
                    if (params1[i] != params2[i]) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * param instance array to the type array
     * @param args
     * @return
     */
    public static Class[] convertToTypeArray(Object[] args) {
        if (args == null)
            return null;
        int s = args.length;
        Class[] ans = new Class[s];
        for (int i = 0; i < s; i++) {
            Object o = args[i];
            if (o != null) {
                ans[i] = o.getClass();
            } else {
                ans[i] = null;
            }
        }
        return ans;
    }
    
    /**
     * @param listenerType
     *            the interface of the listener to proxy
     * @param listenerMethodName
     *            the name of the method in the listener API to call the
     *            closure on
     * @param closure
     *            the closure to invoke on the listenerMethodName method
     *            invocation
     * @return a dynamic proxy which calls the given closure on the given
     *         method name
     */
    public static Object createListenerProxy(Class listenerType, final String listenerMethodName, final Closure closure) {
        InvocationHandler handler = new ClosureListener(listenerMethodName, closure);
        return Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[] { listenerType }, handler);
    }
    
    public static Object doConstructorInvoke(Constructor constructor, Object[] argumentArray) {
        if (log.isLoggable(Level.FINER)){
            logMethodCall(constructor.getDeclaringClass(), constructor.getName(), argumentArray);
        }
        
        try {
            // the following patch was provided by Mori Kouhei to fix JIRA 435
            /* but it opens the ctor up to everyone, so it is no longer private!
             final Constructor ctor = constructor;
             AccessController.doPrivileged(new PrivilegedAction() {
             public Object run() {
             ctor.setAccessible(ctor.getDeclaringClass().equals(theClass));
             return null;
             }
             });
             */
            // end of patch
            
            return constructor.newInstance(argumentArray);
        }
        catch (InvocationTargetException e) {
            /*Throwable t = e.getTargetException();
             if (t instanceof Error) {
             Error error = (Error) t;
             throw error;
             }
             if (t instanceof RuntimeException) {
             RuntimeException runtimeEx = (RuntimeException) t;
             throw runtimeEx;
             }*/
            throw new InvokerInvocationException(e);
        }
        catch (IllegalArgumentException e) {
            if (coerceGStrings(argumentArray)) {
                try {
                    return constructor.newInstance(argumentArray);
                }
                catch (Exception e2) {
                    // allow fall through
                }
            }
            throw new GroovyRuntimeException(
                    "failed to invoke constructor: "
                    + constructor
                    + " with arguments: "
                    + InvokerHelper.toString(argumentArray)
                    + " reason: "
                    + e);
        }
        catch (IllegalAccessException e) {
            throw new GroovyRuntimeException(
                    "could not access constructor: "
                    + constructor
                    + " with arguments: "
                    + InvokerHelper.toString(argumentArray)
                    + " reason: "
                    + e);
        }
        catch (Exception e) {
            throw new GroovyRuntimeException(
                    "failed to invoke constructor: "
                    + constructor
                    + " with arguments: "
                    + InvokerHelper.toString(argumentArray)
                    + " reason: "
                    + e,
                    e);
        }
    }
    
    private static Object makeCommonArray(Object[] arguments, int offset) {
    	// arguments.leght>0 && !=null
    	Class baseClass = null;
    	for (int i = offset; i < arguments.length; i++) {
			if (arguments[i]==null) continue;
			Class argClass = arguments[i].getClass();
			if (baseClass==null) {
				baseClass = argClass;
			} else {
				for (;baseClass!=Object.class; baseClass=baseClass.getSuperclass()){
					if (baseClass.isAssignableFrom(argClass)) break;
				}
			}
		}
    	Object result = makeArray(null,baseClass,arguments.length-offset);
    	System.arraycopy(arguments,offset,result,0,arguments.length-offset);
    	return result;
    }
    
    private static Object makeArray(Object obj, Class secondary, int length) {
    	Class baseClass = secondary;
    	if (obj!=null) {
    		baseClass = obj.getClass();
    	}
    	/*if (GString.class.isAssignableFrom(baseClass)) {
    		baseClass = GString.class;
    	}*/
    	return Array.newInstance(baseClass,length);
    }
    
    /**
     * this method is called when the number of arguments to a method is greater than 1
     * and if the method is a vargs method. This method will then transform the given
     * arguments to make the method callable
     * 
     * @param argumentArray the arguments used to call the method
     * @param paramTypes the types of the paramters the method takes
     */
    private static Object[] fitToVargs(Object[] argumentArray, Class[] paramTypes) {
    	Class vargsClass = autoboxType(paramTypes[paramTypes.length-1].getComponentType());
    	
    	// paramTypes.length is at last 1
    	if (paramTypes.length==1) {
    		// the method takes all arguments in one array
    		// if argumentArray length is 1 we have to look into the array 
    		// and see if it is the same type as the parameters component
    		// type. If so, we have to wrap the argument in an array
    		if (argumentArray.length==1) {
    			Object argument = argumentArray[0];
    			if (argument== null || !argument.getClass().isArray()){
    				// no array, so wrap it
    				Object result = makeArray(argument,vargsClass,1);
    				System.arraycopy(argumentArray,0,result,0,1);
    				argumentArray[0] = result;
    				return argumentArray;
    			} else {
    				// argument is an array no convertion is needed
    				return argumentArray;
    			}
    		} else {
	    		// argumentArray is too big, create a new array and copy all
	    		// parameters in the new Array. Since we are in the special case
	    		// that the number of parameters is 1, this means argumentArray 
	    		// has to be stored in an array, but we have to make sure the type
    			// is correct
    			Object result = makeCommonArray(argumentArray,0);
	    		return new Object[]{result};
    		}
    	} else {
    		// the method takes more than one argument, so all
    		// arguments before the alst one are normal arguments and the 
    		// last argument is the vargs argument. so we test first if
    		// the number of arguments used to call the method is equal the 
    		// number of parameters the method takes
    		
    		if (argumentArray.length==paramTypes.length) {
    			// the number of arguments is correct, but if the last argument 
    			// is no array we have to wrap it in a array
    			Object lastArgument = argumentArray[argumentArray.length-1];
    			if (lastArgument==null || !lastArgument.getClass().isArray()) {
    				// no array so wrap it
    				Object result = makeArray(lastArgument,vargsClass,1);
    				System.arraycopy(argumentArray,argumentArray.length-1,result,0,1);
    				argumentArray[argumentArray.length-1]=result;
    				return argumentArray;
    			} else {
    				// nothing to do!
    				return argumentArray;
    			}
    		} else if (argumentArray.length>paramTypes.length) {
    			// the number of arguments is too big, wrap all exceeding elements
    			// in an array, but keep the old elements
    			Object[] newArgs = new Object[paramTypes.length];
    			// copy arguments that are not a varg
    			System.arraycopy(argumentArray,0,newArgs,0,paramTypes.length-1);
    			// create a new array for the vargs and copy them
    			int numberOfVargs = argumentArray.length-paramTypes.length;
    			//TODO: what about GString here?
    			Object vargs = makeCommonArray(argumentArray,paramTypes.length-1);
    			newArgs[newArgs.length-1] = vargs;
    			return newArgs;
    		}
    	}
    	
    	return argumentArray;
    }
    
    public static Object doMethodInvoke(Object object, MetaMethod method, Object[] argumentArray) {
        Class[] paramTypes = method.getParameterTypes();
        try {
            if (argumentArray == null) {
                argumentArray = EMPTY_ARRAY;
            } else if (paramTypes.length == 1 && argumentArray.length == 0) {
                if (isVargsMethod(paramTypes,argumentArray))
                    argumentArray = new Object[]{Array.newInstance(paramTypes[0].getComponentType(),0)};
                else
                    argumentArray = ARRAY_WITH_NULL;
            } else if (isVargsMethod(paramTypes,argumentArray)) {
            	argumentArray = fitToVargs(argumentArray, paramTypes);
            }
            return method.invoke(object, argumentArray);
        }
        catch (ClassCastException e) {
            if (coerceGStrings(argumentArray)) {
                try {
                    return doMethodInvoke(object, method, argumentArray);
                }
                catch (Exception e2) {
                    // allow fall through
                }
            }
            throw new GroovyRuntimeException(
                    "failed to invoke method: "
                    + method
                    + " on: "
                    + object
                    + " with arguments: "
                    + InvokerHelper.toString(argumentArray)
                    + " reason: "
                    + e,
                    e);
        }
        catch (InvocationTargetException e) {
            /*Throwable t = e.getTargetException();
             if (t instanceof Error) {
             Error error = (Error) t;
             throw error;
             }
             if (t instanceof RuntimeException) {
             RuntimeException runtimeEx = (RuntimeException) t;
             throw runtimeEx;
             }*/
            throw new InvokerInvocationException(e);
        }
        catch (IllegalAccessException e) {
            throw new GroovyRuntimeException(
                    "could not access method: "
                    + method
                    + " on: "
                    + object
                    + " with arguments: "
                    + InvokerHelper.toString(argumentArray)
                    + " reason: "
                    + e,
                    e);
        }
        catch (IllegalArgumentException e) {
            if (coerceGStrings(argumentArray)) {
                try {
                    return doMethodInvoke(object, method, argumentArray);
                }
                catch (Exception e2) {
                    // allow fall through
                }
            }
            Object[] args = coerceNumbers(method, argumentArray);
            if (args != null && !Arrays.equals(argumentArray,args)) {
                try {
                    return doMethodInvoke(object, method, args);
                }
                catch (Exception e3) {
                    // allow fall through
                }
            }
            throw new GroovyRuntimeException(
                    "failed to invoke method: "
                    + method
                    + " on: "
                    + object
                    + " with arguments: "
                    + InvokerHelper.toString(argumentArray)
                    + "reason: "
                    + e
            );
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new GroovyRuntimeException(
                    "failed to invoke method: "
                    + method
                    + " on: "
                    + object
                    + " with arguments: "
                    + InvokerHelper.toString(argumentArray)
                    + " reason: "
                    + e,
                    e);
        }
    }
    
    protected static String getClassName(Object object) {
        return (object instanceof Class) ? ((Class)object).getName() : object.getClass().getName();
    }
    
    /**
     * Returns a callable object for the given method name on the object.
     * The object acts like a Closure in that it can be called, like a closure
     * and passed around - though really its a method pointer, not a closure per se.
     */
    public static Closure getMethodPointer(Object object, String methodName) {
        return new MethodClosure(object, methodName);
    }
    
    public static Class[] getParameterTypes(Object methodOrConstructor) {
        if (methodOrConstructor instanceof MetaMethod) {
            MetaMethod method = (MetaMethod) methodOrConstructor;
            return method.getParameterTypes();
        }
        if (methodOrConstructor instanceof Method) {
            Method method = (Method) methodOrConstructor;
            return method.getParameterTypes();
        }
        if (methodOrConstructor instanceof Constructor) {
            Constructor constructor = (Constructor) methodOrConstructor;
            return constructor.getParameterTypes();
        }
        throw new IllegalArgumentException("Must be a Method or Constructor");
    }
   
    protected static boolean isAssignableFrom(Class classToTransformTo, Class classToTransformFrom) {
        if (classToTransformFrom==null) return true;
        classToTransformTo = autoboxType(classToTransformTo);
        classToTransformFrom = autoboxType(classToTransformFrom);
        
        if (classToTransformTo == classToTransformFrom) {
        	return true;
        }
        // note: there is not coercion for boolean and char. Range matters, precision doesn't
        else if (classToTransformTo == Integer.class) {
        	if (	classToTransformFrom == Integer.class
        			|| classToTransformFrom == Short.class
        			|| classToTransformFrom == Byte.class)
        	return true;
        }
        else if (classToTransformTo == Double.class) {
        	if (	classToTransformFrom == Double.class
        			|| classToTransformFrom == Integer.class
        			|| classToTransformFrom == Long.class
        			|| classToTransformFrom == Short.class
        			|| classToTransformFrom == Byte.class
        			|| classToTransformFrom == Float.class
                    || classToTransformFrom == BigDecimal.class
                    || classToTransformFrom == BigInteger.class)
        	return true;
        }
        else if (classToTransformTo == BigDecimal.class) {
            if (    classToTransformFrom == Double.class
                    || classToTransformFrom == Integer.class
                    || classToTransformFrom == Long.class
                    || classToTransformFrom == Short.class
                    || classToTransformFrom == Byte.class
                    || classToTransformFrom == Float.class
                    || classToTransformFrom == BigDecimal.class
                    || classToTransformFrom == BigInteger.class)
            return true;
        }
        else if (classToTransformTo == BigInteger.class) {
            if (    classToTransformFrom == Integer.class
                    || classToTransformFrom == Long.class
                    || classToTransformFrom == Short.class
                    || classToTransformFrom == Byte.class
                    || classToTransformFrom == BigInteger.class)
            return true;
        }
        else if (classToTransformTo == Long.class) {
        	if (	classToTransformFrom == Long.class
        			|| classToTransformFrom == Integer.class
        			|| classToTransformFrom == Short.class
        			|| classToTransformFrom == Byte.class)
        	return true;
        }
        else if (classToTransformTo == Float.class) {
        	if (	classToTransformFrom == Float.class
        			|| classToTransformFrom == Integer.class
        			|| classToTransformFrom == Long.class
        			|| classToTransformFrom == Short.class
        			|| classToTransformFrom == Byte.class)
        	return true;
        }
        else if (classToTransformTo == Short.class) {
        	if (	classToTransformFrom == Short.class
        			|| classToTransformFrom == Byte.class)
        	return true;
        }
        else if (classToTransformTo==String.class) {
            if (	classToTransformFrom == String.class ||
            		GString.class.isAssignableFrom(classToTransformFrom))
            return true;
        }
        
        boolean answer = classToTransformTo.isAssignableFrom(classToTransformFrom);
        return answer;
    }
    
    public static boolean isGenericSetMethod(MetaMethod method) {
        return (method.getName().equals("set"))
        && method.getParameterTypes().length == 2;
    }
    
    protected static boolean isSuperclass(Class claszz, Class superclass) {
        while (claszz!=null) {
            if (claszz==superclass) return true;
            claszz = claszz.getSuperclass();
        }
        return false;
    }
    
    public static boolean isValidMethod(Class[] paramTypes, Class[] arguments, boolean includeCoerce) {
        if (arguments == null) {
            return true;
        }
        int size = arguments.length;
        
        if (   (size>=paramTypes.length || size==paramTypes.length-1)
                && paramTypes.length>0
                && paramTypes[paramTypes.length-1].isArray())
        {
            // first check normal number of parameters
            for (int i = 0; i < paramTypes.length-1; i++) {
                if (isAssignableFrom(paramTypes[i], arguments[i])) continue;
                return false;
            }
            // check varged
            Class clazz = paramTypes[paramTypes.length-1].getComponentType();
            for (int i=paramTypes.length; i<size; i++) {
                if (isAssignableFrom(clazz, arguments[i])) continue;
                return false;
            }
            return true;
        } else if (paramTypes.length == size) {
            // lets check the parameter types match
            for (int i = 0; i < size; i++) {
                if (isAssignableFrom(paramTypes[i], arguments[i])) continue;
                return false;
            }
            return true;
        } else if (paramTypes.length == 1 && size == 0) {
            return true;
        }
        return false;
        
    }
    
    public static boolean isValidMethod(Object method, Class[] arguments, boolean includeCoerce) {
        Class[] paramTypes = getParameterTypes(method);
        return isValidMethod(paramTypes, arguments, includeCoerce);
    }
    
    public static boolean isVargsMethod(Class[] paramTypes, Object[] arguments) {
        if (paramTypes.length==0) return false;
        if (!paramTypes[paramTypes.length-1].isArray()) return false;
        // -1 because the varg part is optional
        if (paramTypes.length-1==arguments.length) return true;
        if (paramTypes.length-1>arguments.length) return false;
        if (arguments.length>paramTypes.length) return true;
        
        // only case left is arguments.length==paramTypes.length
        Object last = arguments[arguments.length-1];
        if (last==null) return true;
        Class clazz = last.getClass();
        if (clazz.equals(paramTypes[paramTypes.length-1])) return false;
        
        return true;
    }
    
    public static void logMethodCall(Object object, String methodName, Object[] arguments) {
        String className = getClassName(object);
        String logname = "methodCalls." + className + "." + methodName;
        Logger objLog = Logger.getLogger(logname);
        if (! objLog.isLoggable(Level.FINER)) return;
        StringBuffer msg = new StringBuffer(methodName);
        msg.append("(");
        if (arguments != null){
            for (int i = 0; i < arguments.length;) {
                msg.append(normalizedValue(arguments[i]));
                if (++i < arguments.length) { msg.append(","); }
            }
        }
        msg.append(")");
        objLog.logp(Level.FINER, className, msg.toString(), "called from MetaClass.invokeMethod");
    }
    
    protected static String normalizedValue(Object argument) {
        String value;
        try {
            value = argument.toString();
            if (value.length() > MAX_ARG_LEN){
                value = value.substring(0,MAX_ARG_LEN-2) + "..";
            }
            if (argument instanceof String){
                value = "\'"+value+"\'";
            }
        } catch (Exception e) {
            value = shortName(argument);
        }
        return value;
    }
    
    public static boolean parametersAreCompatible(Class[] arguments, Class[] parameters) {
        if (arguments.length!=parameters.length) return false;
        for (int i=0; i<arguments.length; i++) {
            if (!isAssignableFrom(parameters[i],arguments[i])) return false;
        }
        return true;
    }
    
    protected static String shortName(Object object) {
        if (object == null || object.getClass()==null) return "unknownClass";
        String name = getClassName(object);
        if (name == null) return "unknownClassName"; // *very* defensive...
        int lastDotPos = name.lastIndexOf('.');
        if (lastDotPos < 0 || lastDotPos >= name.length()-1) return name;
        return name.substring(lastDotPos+1);
    }
    
    public static Class[] wrap(Class[] classes) {
        Class[] wrappedArguments = new Class[classes.length];
        for (int i = 0; i < wrappedArguments.length; i++) {
            Class c = classes[i];
            if (c==null) continue;
            if (c.isPrimitive()) {
                if (c==Integer.TYPE) {
                    c=Integer.class;
                } else if (c==Byte.TYPE) {
                    c=Byte.class;
                } else if (c==Long.TYPE) {
                    c=Long.class;
                } else if (c==Double.TYPE) {
                    c=Double.class;
                } else if (c==Float.TYPE) {
                    c=Float.class;
                }
            } else if (isSuperclass(c,GString.class)) {
                c = String.class;
            }
            wrappedArguments[i]=c;
        }
        return wrappedArguments;
    }
}

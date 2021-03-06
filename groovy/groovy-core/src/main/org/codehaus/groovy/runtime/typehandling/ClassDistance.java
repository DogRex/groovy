/*
 * Class for calculating "distances" between classes. Such a distance
 * is not a real distance to something but should be seen as the order
 * classes and interfaces are choosen for method selection. The class
 * will keep a weak cache and recalculate the distances on demand. 
 */
package org.codehaus.groovy.runtime.typehandling;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.WeakHashMap;

public class ClassDistance {
    private static WeakHashMap classDistances;
    
    private static class Entry {
        
    }
    
    private static class LinearEntry  extends Entry{
        Class[] entries;
        void concat(Class[] c,LinearEntry le){
            entries = new Class[c.length+le.entries.length];
            System.arraycopy(c,0,entries,0,c.length);
            System.arraycopy(le.entries,0,entries,c.length,le.entries.length);
        }
        void concat(Class c,LinearEntry le){
            entries = new Class[1+le.entries.length];
            entries[0] = c;
            System.arraycopy(le.entries,0,entries,1,le.entries.length);
        }
    }
    
    static {
        classDistances = new WeakHashMap();
        initialPopulate();
    }
    
    private static void initialPopulate() {
        // int, double, byte, float, BigInteger, BigDecimal, long, short
        // GString, char
        
        
        LinearEntry object = new LinearEntry();
        object.entries = new Class[]{Object.class};
        classDistances.put(Object.class,object);
        
        LinearEntry number = new LinearEntry();
        number.concat(new Class[]{Number.class,Serializable.class},object);
        classDistances.put(Number.class,number);

        LinearEntry compareableNumber = new LinearEntry();
        compareableNumber.concat(Comparable.class,number);
        
        LinearEntry binteger = new LinearEntry();
        binteger.concat(new Class[]{BigInteger.class, BigDecimal.class}, compareableNumber);
        classDistances.put(BigInteger.class,object);
        
        LinearEntry bdec = new LinearEntry();
        binteger.concat(new Class[]{BigDecimal.class, BigInteger.class}, compareableNumber);
        classDistances.put(BigDecimal.class,object);
        
        
        
        // byte:
        LinearEntry start = new LinearEntry();
        start.entries =  new Class[]{
                byte.class, Byte.class, short.class, Short.class,
                int.class, Integer.class, long.class, Long.class,
                BigInteger.class,
                float.class, Float.class,  double.class, Double.class, 
                BigDecimal.class,
                Number.class,Object.class};
        classDistances.put(byte.class,start);
        
        // short:
        start = new LinearEntry();
        start.entries =  new Class[]{
                short.class, Short.class,
                int.class, Integer.class, long.class, Long.class,
                BigInteger.class,
                float.class, Float.class,  double.class, Double.class, 
                BigDecimal.class,
                Number.class,Object.class};
        classDistances.put(short.class,start);
        
        // int:
        start = new LinearEntry();
        start.entries =  new Class[]{
                int.class, Integer.class, long.class, Long.class,
                BigInteger.class,
                float.class, Float.class,  double.class, Double.class, 
                BigDecimal.class,
                Number.class,Object.class};
        classDistances.put(int.class,start);
        
        // long:
        start = new LinearEntry();
        start.entries =  new Class[]{
                long.class, Long.class,
                BigInteger.class,
                float.class, Float.class,  double.class, Double.class, 
                BigDecimal.class,
                Number.class,Object.class};
        classDistances.put(long.class,start);
        
        // Biginteger:
        start = new LinearEntry();
        start.entries =  new Class[]{
                BigInteger.class,
                float.class, Float.class,  double.class, Double.class, 
                BigDecimal.class,
                Number.class,Object.class};
        classDistances.put(long.class,start);
        
        // float:
        start = new LinearEntry();
        start.entries =  new Class[]{ 
                byte.class, Byte.class, short.class, Short.class,
                int.class, Integer.class, long.class, Long.class,
                BigInteger.class,
                float.class, Float.class,  double.class, Double.class, 
                BigDecimal.class,
                Number.class,Object.class};
        classDistances.put(float.class,start);
        
        // double:
        start = new LinearEntry();
        start.entries =  new Class[]{ 
                double.class,
                Double.class, BigDecimal.class,
                Number.class,Object.class};
        classDistances.put(double.class,start);

    }
    
    private synchronized static void popultate(Class clazz) {
        if (classDistances.get(clazz) != null) return;
        
    }
    
}

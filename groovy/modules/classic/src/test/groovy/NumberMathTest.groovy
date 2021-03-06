import java.math.BigDecimal;
import java.math.BigInteger;

/** 
 * Basic NumberMath test.
 * @see org.codehaus.groovy.runtime.NumberMath
 */
class NumberMathTest extends GroovyTestCase {

    void testPromotions() {
    	C = '1'.toCharacter();
    	B = new Byte("1");
    	I = new Integer(1);
    	L = new Long(1);
    	F = new Float("1.0");
    	D = new Double("1.0");
    	BI = new BigInteger("1");
    	BD = new BigDecimal("1.0");
    	
    	//+, -, and * all promote the same way, so sample the matrix
    	assert C+B instanceof Integer;
    	assert C-BD instanceof BigDecimal;
    	assert B+F instanceof Double;
    	assert B+I instanceof Integer;
    	
    	assert I+I instanceof Integer;
    	assert I-F instanceof Double;
    	assert I*D instanceof Double;
    	assert I+BI instanceof BigInteger;
    	assert I-BD instanceof BigDecimal;
    	
    	assert F*L instanceof Double;
    	assert D+L instanceof Double;
    	assert BI-L instanceof BigInteger;
    	assert BD*L instanceof BigDecimal;
    	
    	assert F+F instanceof Double;
    	assert F-BI instanceof Double;
    	assert F*BD instanceof Double;
    	
    	assert F+D instanceof Double;
    	assert BI-D instanceof Double;
    	assert BD*D instanceof Double;
    	
    	assert BI+BI instanceof BigInteger;
    	assert BD-BI instanceof BigDecimal;
    	assert BD*BD instanceof BigDecimal;
    	
    	//Division (/) promotes differently so change the expected results:
    	assert I/I instanceof BigDecimal;
    	assert I/F instanceof Double;
    	assert I/D instanceof Double;
    	assert I/BI instanceof BigDecimal;
    	assert I/BD instanceof BigDecimal;
    	
    	assert F/L instanceof Double;
    	assert D/L instanceof Double;
    	assert BI/L instanceof BigDecimal;
    	assert BD/L instanceof BigDecimal;
    	
    	assert F/F instanceof Double;
    	assert F/BI instanceof Double;
    	assert F/BD instanceof Double;
    	
    	assert F/D instanceof Double;
    	assert BI/D instanceof Double;
    	assert BD/D instanceof Double;
    	
    	assert BI/BI instanceof BigDecimal;
    	assert BD/BI instanceof BigDecimal;
    	assert BD/BD instanceof BigDecimal;
    }
    
    void testOperations() {
    	I1 = new Integer(1);
    	I2 = new Integer(2);
    	I3 = new Integer(3);
    	L1 = new Long(1);
    	L2 = new Long(2);
    	L3 = new Long(3);
    	F1 = new Float("1.0");
    	F2 = new Float("2.0");
    	D1 = new Double("1.0");
    	D2 = new Double("2.0");
    	BI1 = new BigInteger("1");
    	BI2 = new BigInteger("2");
    	BD1 = new BigDecimal("1.0");
    	BD2 = new BigDecimal("2.0");
    	BD20 = new BigDecimal("2.00");

    	
    	assert I1/I2 instanceof BigDecimal;
    	assert I1/I2 == new BigDecimal("0.5");

    	assert I1\I2 instanceof Integer;
    	assert I1\I2 == 0;

    	assert I3\I2 instanceof Integer;
    	assert I3\I2 == 1;
    	
    	assert L1\I2 instanceof Long;
    	assert L1\I2 == 0;

    	assert L3\L2 instanceof Long;
    	assert L3\L2 == 1;
    	
    	assert BI1\BI2 instanceof BigInteger;
    	assert BI1\BI2 == 0;
    	
    	assert I1/I3 instanceof BigDecimal;
    	assert I1/I3 == new BigDecimal("0.3333333333");
    	
    	assert I2/I3 instanceof BigDecimal;
    	assert I2/I3 == new BigDecimal("0.6666666667");
    	    	
    	assert I1/BD2 instanceof BigDecimal;
    	
    	//Test keeping max scale of (L, R or 10)
    	BBD1 = new BigDecimal("0.12345678901234567");
    	assert BD1 + BBD1 == new BigDecimal("1.12345678901234567");

    	BBD2 = new BigDecimal(".000000000000000008");
    	assert BBD1 + BBD2 == new BigDecimal("0.123456789012345678");
	}
	
	void testUnsupportedIntDivision() {
	   	try {
    		1.0 \ 3;
    	} catch (UnsupportedOperationException uoe) {
    		return
    	}
    	fail("Should catch an UnsupportedOperationException")
    	
	   	try {
    		1.0G \ 3;
    	} catch (UnsupportedOperationException uoe) {
    		return
    	}
    	fail("Should catch an UnsupportedOperationException")
	} 
}

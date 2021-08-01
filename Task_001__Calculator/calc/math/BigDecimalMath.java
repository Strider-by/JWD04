package calc.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalMath 
{
    private BigDecimalMath() {
        
    }
    
    public static BigDecimal pow(BigDecimal x, BigDecimal y) {
        // due to core Java BigDecimal implementation limits, BigDecimal.pow(...) method 
        // accepts only int value in range of [0, 999999999] as power parameter

        if(!canBeTakenAsPow(y)) {
            throw new ArithmeticException("The power parameter should be in range of [0, 999999999]");
        }

        try {
            return x.pow(y.intValueExact());
        } catch (ArithmeticException ex) {
            throw new ArithmeticException("Sorry, this operation cannot be executed, "
                    + "power parameter seems to be out of range");
        }
    }

    
    public static boolean canBeTakenAsPow(BigDecimal value) {
        BigDecimal upperBound = BigDecimal.valueOf(999999999);
        BigDecimal lowerBound = BigDecimal.ZERO;
        
        return (value.compareTo(upperBound) <= 0 && value.compareTo(lowerBound) >= 0);
    }
    
    public static boolean rootCanBeTaken(BigDecimal value, BigDecimal pow) {
        return Double.isFinite(value.doubleValue())
                && Double.isFinite(pow.doubleValue());
    }
    
    public static BigDecimal root(BigDecimal value, BigDecimal pow) {
        BigDecimal result = BigDecimal.valueOf(
                Math.pow(value.doubleValue(), 1 / pow.doubleValue()));
        
        return result;
    }
    
    public static boolean isIntegerConvertable(BigDecimal value) {
        // checking if there is fractional part
        BigDecimal intPart = value.setScale(0, RoundingMode.DOWN);
        if(value.compareTo(intPart) != 0) {
            return false;
        }
        
        // checking if the value fits Integer values range 
        BigDecimal upperBound = BigDecimal.valueOf(Integer.MAX_VALUE);
        BigDecimal lowerBound = BigDecimal.valueOf(Integer.MIN_VALUE);
        return (value.compareTo(upperBound) <= 0 && value.compareTo(lowerBound) >= 0);
    }
    
}

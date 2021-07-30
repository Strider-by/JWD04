package calc.math;

import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;



public enum Operation {
    
    ADDITION("addition", "+", "",
            (x, y) -> true, // no restrictions
            BigDecimal::add),
    
    SUBSTRACTION("substraction", "-", "",
            (x, y) -> true, // no restrictions
            BigDecimal::subtract),
    
    MULTIPLICATION("multiplication", "*", "",
            (x, y) -> true, // no restrictions
            BigDecimal::multiply),
    
    DIVISION("division", "/", "diveder can not be equal to 0",
            (x, y) -> y.compareTo(BigDecimal.ZERO) != 0,
            BigDecimal::divide),
    
    INTEGER_DIVISION("integer division", "//", DIVISION.errorMsg,
            DIVISION.paramsCheck,
            BigDecimal::divideToIntegralValue),

    REMAINDER_OF_THE_DIVISION("remainder of the division", "%", DIVISION.errorMsg,
            DIVISION.paramsCheck,
            BigDecimal::remainder),
    
    EXPONENTIATION("exponentiation", "^", "power can be in range of [0: 999999999]",
            (val, power) -> {
                // checking if power is in range of [0: 999999999]
                boolean powFitsTheBounds = BigDecimalMath.canBeTakenAsPow(power);
                // checking for value and power not to be 0 simultaneously
                boolean doubleZeroCheckPassed = val.compareTo(BigDecimal.ZERO) != 0
                    || power.compareTo(BigDecimal.ZERO) != 0;
                
                return powFitsTheBounds && doubleZeroCheckPassed;
            },
            BigDecimalMath::pow);

    
    public final String name;
    public final String alias;
    public final BiPredicate<BigDecimal, BigDecimal> paramsCheck;
    public final String errorMsg;
    public final BiFunction<BigDecimal, BigDecimal, BigDecimal> function;
    
    Operation(String name, String alias, String errorMsg, BiPredicate<BigDecimal, BigDecimal> paramsCheck, 
            BiFunction<BigDecimal, BigDecimal, BigDecimal> function) {
        this.name = name;
        this.alias = alias;
        this.paramsCheck = paramsCheck;
        this.errorMsg = errorMsg;
        this.function = function;
    }
    
    public boolean canBeCalculated(BigDecimal x, BigDecimal y) {
        return this.paramsCheck.test(x, y);
    }
    
    public BigDecimal calculate(BigDecimal x, BigDecimal y) {
        return this.function.apply(x, y);
    }
}


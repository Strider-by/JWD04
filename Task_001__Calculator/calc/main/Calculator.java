package calc.main;

import java.util.Scanner;
import calc.math.Operation;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Calculator {

    
    private static final Map<String, Operation> OPERATIONS;
    private static final Map<String, Command> COMMANDS;
    private final Scanner sc = new Scanner(System.in);
    private static final String INTRO = "Calculator v. 1.0";
    private static final String AVAILIBLE_OPERATIONS_HELP_LIST;
    private static final String AVAILIBLE_COMMANDS_HELP_LIST;
    private static final String DELIMETER = "------------------------------------------------------------------------";
    
    
    BigDecimal operand1 = null;
    Operation operation = null;
    BigDecimal operand2 = null;
    State state = State.OPERAND_1_TO_BE_REQUESTED;

    
    
    static {
        
        
        OPERATIONS = Arrays.stream(Operation.values())
                .collect(Collectors.toMap(
                                operation -> operation.alias, Function.identity(),
                                (existing, replacement) -> {
                                    throw new RuntimeException("repeated operation alias");
                                },
                                LinkedHashMap::new));
        
        COMMANDS = Arrays.stream(Command.values())
                .collect(Collectors.toMap(
                                command -> command.alias, Function.identity(),
                                (existing, replacement) -> {
                                    throw new RuntimeException("repeated command alias");
                                },
                                LinkedHashMap::new));
        
        AVAILIBLE_OPERATIONS_HELP_LIST = Arrays.stream(Operation.values())
                        .map(operation -> String.format("  %-2s %s", operation.alias, operation.name))
                        .reduce((acc, newLine) -> acc + "\n" + newLine)
                        .orElse("---");
        
        AVAILIBLE_COMMANDS_HELP_LIST = Arrays.stream(Command.values())
                        .map(operation -> "  " + operation.alias)
                        .reduce((acc, newLine) -> acc + "\n" + newLine)
                        .orElse("---");
        

    }

    public static void main(String[] args) {
        
        Calculator app = new Calculator();
        app.run();
        
    }
    
    private void run() {
        print(INTRO);
        showInfo();
        
        
        boolean exitRequested = false;
        
        while(!exitRequested) {
            
            switch(state) {
                case OPERAND_1_TO_BE_REQUESTED:
                case OPERAND_2_TO_BE_REQUESTED:
                    requestOperand();
                    break;
                case OPERATION_TO_BE_REQUESTED:
                    requestOperation();
                    break;
                case CALCULATION_TO_BE_EXECUTED:
                    calculate();
                    break;
                case ANSWER_TO_BE_PRINTED:
                    printAnswer();
                    break;
                default:
                    print("Oops! Something went wrong! This branch of code"
                            + "should not be executed.");
                    break;

            }
        }
    }
    
    private void requestOperand() {
        System.out.print("Enter decimal number or application command:  ");
        String input = sc.nextLine();
        
        if(isCommand(input)) {
            executeCommand(input);
            return;
        }
        
        try {
            BigDecimal number = new BigDecimal(input);
            if(state == State.OPERAND_1_TO_BE_REQUESTED) {
                operand1 = number;
                state = State.OPERATION_TO_BE_REQUESTED;
            } else {
                operand2 = number;
                state = State.CALCULATION_TO_BE_EXECUTED;
            }
        } catch(Exception ex) {
                showInvalidValueMsg();
        }
    }
    
    private void requestOperation() {
        System.out.print("Enter operation alias or application command: ");
        String input = sc.nextLine();
        
        if(isCommand(input)) {
            executeCommand(input);
            return;
        }
        
        if(isOperation(input)) {
            operation = OPERATIONS.get(input);
            state = State.OPERAND_2_TO_BE_REQUESTED;
            return;
        }
        
        showInvalidValueMsg();
    }
    
    private void calculate() {
        if(!operation.canBeCalculated(operand1, operand2)) {
                print("Sorry, " + operation.errorMsg);
                indent(1);
                state = State.OPERAND_2_TO_BE_REQUESTED;
                return;
            }
            
            operand1 = operation.calculate(operand1, operand2);
            state = State.ANSWER_TO_BE_PRINTED;
    }
    
    private void printAnswer() {
        print(">>  answer is:  " + operand1.stripTrailingZeros().toPlainString());
        state = State.OPERATION_TO_BE_REQUESTED;
    }
    
    private void reset() {
        operand1 = null;
        operation = null;
        operand2 = null;
        state = State.OPERAND_1_TO_BE_REQUESTED;
    }

    
    private static boolean isCommand(String s) {
        return COMMANDS.containsKey(s.toLowerCase());
    }
    
    private static boolean isOperation(String s) {
        return OPERATIONS.containsKey(s.toLowerCase());
    }
    
    private void executeCommand(String alias) {
        Command command = COMMANDS.get(alias);
        
        switch(command) {
            case HELP:
                showInfo();
                break;
            case RESET:
                reset();
                indent(1);
                break;
            case EXIT:
                System.exit(0);
                break;
            default:
                print("Sorry, this command is yet to be implemented.");
                break;
        }
    }

    private static void showInfo() {
        print(DELIMETER);
        
        print("Availible commands:");
        print(AVAILIBLE_COMMANDS_HELP_LIST);
        indent(1);
        print("Availible operations:");
        print(AVAILIBLE_OPERATIONS_HELP_LIST);
        print(DELIMETER);
        indent(1);
    }
    
    private static void showInvalidValueMsg() {
        print("This is not a valid value. Type \"help\" if you need to see command list.");
        indent(1);
    }

    private static void print(Object... lines) {
        for (Object o : lines) {
            System.out.println(o);
        }
    }
    
    private static void indent(int lines) {
        for(int i = 0; i < lines; i++) {
            System.out.println();
        }
    }
    
    
    enum State {
        OPERAND_1_TO_BE_REQUESTED,
        OPERATION_TO_BE_REQUESTED,
        OPERAND_2_TO_BE_REQUESTED,
        CALCULATION_TO_BE_EXECUTED,
        ANSWER_TO_BE_PRINTED;
    }


}

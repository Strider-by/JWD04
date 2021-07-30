package calc.main;

public enum Command {
    HELP("help"),
    RESET("reset"),
    // UNDO("undo"),
    EXIT("exit");
    
    public final String alias;
    
    Command(String alias) {
        this.alias = alias;
    }
    
}

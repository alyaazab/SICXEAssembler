import java.util.HashMap;

public class SymbolTable {

    private static SymbolTable ourInstance = new SymbolTable();

    public static SymbolTable getInstance() {
        return ourInstance;
    }

    private SymbolTable() {
    }

    private HashMap<String, Symbol> symTab = new HashMap<>();

    public Symbol getSymbol(String sym){
        return symTab.get(sym);
    }

    public void addToSymTab(String symName, Symbol symbol){
        symTab.put(symName, symbol);
    }

    public void addToSymTab(String symName, int address, int length, boolean relocatable){
        Symbol symbol = new Symbol(symName, address, length, relocatable);
        symTab.put(symName, symbol);
    }
}

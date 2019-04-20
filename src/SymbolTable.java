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

    public void addToSymTab(String symName, String value, int length){
        Symbol symbol = new Symbol(value, length);
        symTab.put(symName, symbol);
    }
}

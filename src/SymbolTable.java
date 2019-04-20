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

    public void addToSymTab(String symName, int value, int length, boolean relocatable){
        Symbol symbol = new Symbol(symName, value, length, relocatable);
        symTab.put(symName, symbol);
    }

    public void preloadRegistersToSymTab() {
        addToSymTab("A", new Symbol("A", 0, 0, false));
        addToSymTab("X", new Symbol("X", 1, 0, false));
        addToSymTab("L", new Symbol("L", 2, 0, false));
        addToSymTab("B", new Symbol("B", 3, 0, false));
        addToSymTab("S", new Symbol("S", 4, 0, false));
        addToSymTab("T", new Symbol("T", 5, 0, false));
        addToSymTab("F", new Symbol("F", 6, 0, false));
        addToSymTab("PC", new Symbol("PC", 8, 0, false));
        addToSymTab("SW", new Symbol("SW", 9, 0, false));
    }
}

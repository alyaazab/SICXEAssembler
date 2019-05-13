package symbol;

import java.util.HashMap;

public class SymbolTable {

    private static SymbolTable ourInstance = new SymbolTable();

    public static SymbolTable getInstance() {
        return ourInstance;
    }

    private SymbolTable() {
    }

    public HashMap<String, Symbol> getSymTab() {
        return symTab;
    }

    private HashMap<String, Symbol> symTab = new HashMap<>();

    public Symbol getSymbol(String sym){
        return symTab.get(sym);
    }


    public boolean addToSymTab(String symName, int value, int length, boolean relocatable){
        if(symTab.get(symName) != null) return false;
        Symbol symbol = new Symbol(symName, value, length, relocatable);
        symTab.put(symName, symbol);
        return true;
    }

}

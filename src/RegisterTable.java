import java.util.HashMap;

public class RegisterTable {
    private static RegisterTable ourInstance = new RegisterTable();

    public static RegisterTable getInstance() {
        return ourInstance;
    }

    private RegisterTable() {
    }


    private HashMap<String, Register> regTable = new HashMap<>();

    public HashMap<String, Register> getRegTable() {
        return regTable;
    }


    public void fillRegisterTable(){
        regTable.put("A", new Register(0, 16777215));
        regTable.put("X", new Register(1, 16777215));
        regTable.put("L", new Register(2, 16777215));
        regTable.put("B", new Register(3, 16777215));
        regTable.put("S", new Register(4, 16777215));
        regTable.put("T", new Register(5, 16777215));
        regTable.put("F", new Register(6, 16777215));
        regTable.put("PC", new Register(8, 16777215));
        regTable.put("SW", new Register(9, 16777215));
    }
}

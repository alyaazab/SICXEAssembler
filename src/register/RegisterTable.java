package register;

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
        regTable.put("a", new Register(0, 16777215));
        regTable.put("x", new Register(1, 16777215));
        regTable.put("l", new Register(2, 16777215));
        regTable.put("b", new Register(3, 16777215));
        regTable.put("s", new Register(4, 16777215));
        regTable.put("t", new Register(5, 16777215));
        regTable.put("f", new Register(6, 16777215));
        regTable.put("pc", new Register(8, 16777215));
        regTable.put("sw", new Register(9, 16777215));
    }
}

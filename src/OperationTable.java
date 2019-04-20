import java.util.HashMap;

public class OperationTable {

    private static HashMap<String, Operation> opTable = new HashMap<>();

    public static void fillOpTable(){
        // TODO: add to HashMap
        addToOpTable("add", "add", 3, "18", 3);
        addToOpTable("+add", "+add", 4, "18", 4);
    }

    public static HashMap<String, Operation> getOptable(){
        return opTable;
    }

    public static Operation getOperation(String op){
        return opTable.get(op);
    }

    private static void addToOpTable(String op, String operationMnemonic, int lengthOfInstruction, String binaryCode, int format){
        opTable.put(op,new Operation(operationMnemonic, lengthOfInstruction, binaryCode, format));
    }

}

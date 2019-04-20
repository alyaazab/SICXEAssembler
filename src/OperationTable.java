import java.util.HashMap;

public class OperationTable {

    private static HashMap<String, Operation> opTable = new HashMap<>();

    public static void fillOpTable(){
        // TODO: add to HashMap
    }

    public static HashMap<String, Operation> getOptable(){
        return opTable;
    }

    public static Operation getOperation(String op){
        return opTable.get(op);
    }
}

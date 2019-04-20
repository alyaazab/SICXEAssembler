import java.util.HashMap;

public class OperationTable {

    private static HashMap<String, Operation> opTable = new HashMap<>();

    public static void fillOpTable(){
        // TODO: add to HashMap
        opTable.put("add", new Operation("add", 3, "18", 3));
        opTable.put("+add", new Operation("+add", 4, "18", 4));
        opTable.put("addr", new Operation("addr", 2, "90", 2));
        opTable.put("and", new Operation("and", 3, "40", 3));
        opTable.put("+and", new Operation("+and", 4, "40", 4));
        opTable.put("clear", new Operation("clear", 2, "4", 2));
        opTable.put("comp", new Operation("comp", 3, "28", 3));
        opTable.put("+comp", new Operation("+comp", 4, "28", 4));
        opTable.put("compr", new Operation("compr", 2, "A0", 2));
        opTable.put("div", new Operation("div", 3, "24", 3));
        opTable.put("+div", new Operation("+div", 4, "24", 4));
        opTable.put("divr", new Operation("divr", 2, "9C", 2));
        opTable.put("j", new Operation("j", 3, "3C", 3));
        opTable.put("+j", new Operation("+j", 4, "3C", 4));
        opTable.put("jeq", new Operation("jeq", 3, "30", 3));
        opTable.put("+jeq", new Operation("+jeq", 4, "30", 4));
        opTable.put("jgt", new Operation("jgt", 3, "34", 3));
        opTable.put("+jgt", new Operation("+jgt", 4, "34", 4));
        opTable.put("jlt", new Operation("jlt", 3, "38", 3));
        opTable.put("+jlt", new Operation("+jlt", 4, "38", 4));
        opTable.put("jsub", new Operation("jsub", 3, "48", 3));
        opTable.put("+jsub", new Operation("+jsub", 4, "48", 4));
        opTable.put("lda", new Operation("lda", 3, "00", 3));
        opTable.put("+lda", new Operation("+lda", 4, "00", 4));
        opTable.put("ldb", new Operation("ldb", 3, "68", 3));
        opTable.put("+ldb", new Operation("+ldb", 4, "68", 4));
        opTable.put("ldch", new Operation("ldch", 3, "50", 3));
        opTable.put("+ldch", new Operation("+ldch", 4, "50", 4));
        opTable.put("ldl", new Operation("ldl", 3, "08", 3));
        opTable.put("+ldl", new Operation("+ldl", 4, "08", 4));
        opTable.put("lds", new Operation("lds", 3, "6C", 3));
        opTable.put("+lds", new Operation("+lds", 4, "6C", 4));
        opTable.put("ldt", new Operation("ldt", 3, "74", 3));
        opTable.put("+ldt", new Operation("+ldt", 4, "74", 4));

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

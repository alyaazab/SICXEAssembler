import java.util.HashMap;

public class OperationTable {

    private static HashMap<String, Operation> opTable = new HashMap<>();

    public static void fillOpTable(){
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
        opTable.put("ldx", new Operation("ldx", 3, "04", 3));
        opTable.put("+ldx", new Operation("+ldx", 4, "04", 4));
        opTable.put("mul", new Operation("mul", 3, "20", 3));
        opTable.put("+mul", new Operation("+mul", 4, "20", 4));
        opTable.put("mulr", new Operation("mulr", 2, "98", 2));
        opTable.put("or", new Operation("or", 3, "44", 3));
        opTable.put("+or", new Operation("+or", 4, "44", 4));
        opTable.put("rd", new Operation("rd", 3, "D8", 3));
        opTable.put("+rd", new Operation("+rd", 4, "D8", 4));
        opTable.put("rmo", new Operation("rmo", 2, "AC", 2));
        opTable.put("rsub", new Operation("rsub", 3, "4C", 3));
        opTable.put("+rsub", new Operation("+rsub", 4, "4C", 4));
        opTable.put("shiftl", new Operation("shiftl", 2, "A4", 2));
        opTable.put("shiftr", new Operation("shiftr", 2, "A8", 2));
        opTable.put("sta", new Operation("sta", 3, "0C", 3));
        opTable.put("+sta", new Operation("+sta", 4, "0C", 4));
        opTable.put("stb", new Operation("stb", 3, "78", 3));
        opTable.put("+stb", new Operation("+stb", 4, "78", 4));
        opTable.put("stch", new Operation("stch", 3, "54", 3));
        opTable.put("+stch", new Operation("+stch", 4, "54", 4));
        opTable.put("stl", new Operation("stl", 3, "14", 3));
        opTable.put("+stl", new Operation("+stl", 4, "14", 4));
        opTable.put("sts", new Operation("sts", 3, "7C", 3));
        opTable.put("+sts", new Operation("+sts", 4, "7C", 4));
        opTable.put("stt", new Operation("stt", 3, "84", 3));
        opTable.put("+stt", new Operation("+stt", 4, "84", 4));
        opTable.put("stx", new Operation("stx", 3, "10", 3));
        opTable.put("+stx", new Operation("+stx", 4, "10", 4));
        opTable.put("sub", new Operation("sub", 3, "1C", 3));
        opTable.put("+sub", new Operation("+sub", 4, "1C", 4));
        opTable.put("subr", new Operation("subr", 2, "94", 2));
        opTable.put("td", new Operation("td", 3, "E0", 3));
        opTable.put("+td", new Operation("+td", 4, "E0", 4));
        opTable.put("tix", new Operation("tix", 3,"2C", 3));
        opTable.put("+tix", new Operation("+tix", 4,"2C", 4));
        opTable.put("tixr", new Operation("tixr", 2, "B8", 2));
        opTable.put("wd", new Operation("wd", 3, "DC", 3));
        opTable.put("+wd", new Operation("+wd", 4, "DC", 4));
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
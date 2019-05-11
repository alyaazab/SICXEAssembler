import java.util.ArrayList;

public class Line {

    private int address;
    private String labelField;
    private String operationField;
    private String operandField;
    private String comment;
    private ArrayList<Integer> errorIndexList;
    private Operation operation;

    public Line(int address, String labelField, String operationField, String operandField, String comment,
                ArrayList<Integer> errorIndexList, Operation operation) {
        this.address = address;
        this.labelField = labelField;
        this.operationField = operationField;
        this.operandField = operandField;
        this.comment = comment;
        this.errorIndexList = errorIndexList;
        this.operation = operation;
    }


    public void setAddress(int address) {
        this.address = address;
    }

    public String getComment() {
        return comment;
    }

    public ArrayList<Integer> getErrorIndexList() {
        return errorIndexList;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getOperandField() {
        return operandField;
    }

    @Override
    public String toString() {
        String str = "";

        if(!this.operationField.equals("")) {
            str = str + Integer.toHexString(address);
            str = leftPad(str).toUpperCase();

            str = str + "\t\t" + labelField + "\t" + operationField + "\t" + operandField;
        }
        else if(!this.comment.equals("")) {
            str = str + "\t\t\t" + comment;
        }
        else
            str = str + "\t\t\t" + labelField + "\t" + "        " + "\t" + operandField;
        if (errorIndexList.size() > 0){
            str = str + "\n";
            for (Integer integer : errorIndexList) {
                String error = Error.getError(integer);
                str = str + " " + error + "\n";
            }
        }
        return str;
    }

    private static String leftPad(String str){

        String padString = "000000";

        if(str.length() < 6 )
            return padString.substring(str.length()) + str;
        else
            return str;
    }

}
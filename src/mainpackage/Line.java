package mainpackage;

import operation.Operation;

import java.util.ArrayList;

public class Line {

    private int address;
    private String labelField;
    private String operationField;
    private String operandField;
    private String comment;
    private ArrayList<Integer> errorIndexList;
    private Operation operation;
    private String objectCode = "";

    public boolean isBaseRegisterSet() {
        return baseRegisterSet;
    }

    public void setBaseRegisterSet(boolean baseRegisterSet) {
        this.baseRegisterSet = baseRegisterSet;
    }

    private boolean baseRegisterSet;

    public Line(int address, String labelField, String operationField, String operandField, String comment,
                ArrayList<Integer> errorIndexList, Operation operation, boolean baseRegisterSet) {
        this.address = address;
        this.labelField = labelField;
        this.operationField = operationField;
        this.operandField = operandField;
        this.comment = comment;
        this.errorIndexList = errorIndexList;
        this.operation = operation;
        this.baseRegisterSet = baseRegisterSet;
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

    public int getAddress() {
        return address;
    }

    public String getLabelField() {
        return labelField;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectCode() {
        return objectCode;
    }

    @Override
    public String toString() {
        String str = "";

        if(!this.operationField.equals("")) {
            str = str + Integer.toHexString(address);
            str = leftPad(str).toUpperCase() + " " + objectCode;

            str = str + "\t\t\t" + labelField + "\t\t" + operationField + "\t\t" + operandField;
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
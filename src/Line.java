import com.sun.deploy.util.StringUtils;

import java.util.ArrayList;

public class Line {

    private int address;
    private String labelField;
    private String operationField;
    private String operandField;
    private String comment;
    private ArrayList<Integer> errorIndexList;

    public Line(int address, String labelField, String operationField, String operandField, String comment, ArrayList<Integer> errorIndexList) {
        this.address = address;
        this.labelField = labelField;
        this.operationField = operationField;
        this.operandField = operandField;
        this.comment = comment;
        this.errorIndexList = errorIndexList;
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

    @Override
    public String toString() {
        String str = "";

        if(this.operationField != "") {
            str = str + Integer.toHexString(address);
            str = leftPad(str).toUpperCase();

            str = str + "\t\t" + labelField + "\t" + operationField + "\t" + operandField;
        }
        else {
            str = str + "\t\t\t" + comment;
        }
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
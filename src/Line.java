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


    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public String getLabelField() {
        return labelField;
    }

    public void setLabelField(String labelField) {
        this.labelField = labelField;
    }

    public String getOperationField() {
        return operationField;
    }

    public void setOperationField(String operationField) {
        this.operationField = operationField;
    }

    public String getOperandField() {
        return operandField;
    }

    public void setOperandField(String operandField) {
        this.operandField = operandField;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<Integer> getErrorIndexList() {
        return errorIndexList;
    }

    public void setErrorIndexList(ArrayList<Integer> errorIndexList) {
        this.errorIndexList = errorIndexList;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        if(this.operationField != null) {
            str.append(Integer.toHexString(address)).append("\t");
            str.append(labelField).append(" ");
            str.append(operationField).append("  ");
            str.append(operandField);
        }
        else {
            str.append("\t").append(comment);
        }
        if (errorIndexList.size() > 0){
            str.append("\n");
            for (Integer integer : errorIndexList) {
                String error = Error.getError(integer);
                str.append(" ").append(error).append("\n");
            }
        }
        return str.toString();
    }
}
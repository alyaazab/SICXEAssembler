public class Line {

    private int address;
    private String labelField;
    private String operationField;
    private String operandField;
    private String[] errors;

    public Line(int address, String labelField, String operationField, String operandField, String[] errors) {
        this.address = address;
        this.labelField = labelField;
        this.operationField = operationField;
        this.operandField = operandField;
        this.errors = errors;
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

    public String[] getErrors() {
        return errors;
    }

    public void setErrors(String[] errors) {
        this.errors = errors;
    }
}
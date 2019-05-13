package operation;

public class Operation {

    private String operationMnemonic;
    private int lengthOfInstruction;
    private String binaryCode;
    private int format;
    private int hasOperand;
    private int hasLabel;


    public Operation() {
    }


    public Operation(String operationMnemonic, int lengthOfInstruction, String binaryCode,
                     int format, int hasOperand, int hasLabel) {
        this.operationMnemonic = operationMnemonic;
        this.lengthOfInstruction = lengthOfInstruction;
        this.binaryCode = binaryCode;
        this.format = format;
        this.hasOperand = hasOperand;
        this.hasLabel = hasLabel;
    }

    public String getOperationMnemonic() {
        return operationMnemonic;
    }

    public void setOperationMnemonic(String operationMnemonic) {
        this.operationMnemonic = operationMnemonic;
    }

    public int getLengthOfInstruction() {
        return lengthOfInstruction;
    }

    public void setLengthOfInstruction(int lengthOfInstruction) {
        this.lengthOfInstruction = lengthOfInstruction;
    }

    public String getBinaryCode() {
        return binaryCode;
    }

    public void setBinaryCode(String binaryCode) {
        this.binaryCode = binaryCode;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int isHasOperand() {
        return hasOperand;
    }

    public void setHasOperand(int hasOperand) {
        this.hasOperand = hasOperand;
    }

    public int isHasLabel() {
        return hasLabel;
    }

    public void setHasLabel(int hasLabel) {
        this.hasLabel = hasLabel;
    }

    @Override
    public String toString() {

        return operationMnemonic;
    }
}

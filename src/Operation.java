public class Operation {

    private String operationMnemonic;
    private int lengthOfInstruction;
    private String binaryCode;
    private int format;

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
}

public class Line {

    private String label;
    private String opcode;
    private String operand;

    public Line(String label, String opcode, String operand) {
        this.label = label;
        this.opcode = opcode;
        this.operand = operand;
    }

    public Line(String opcode, String operand) {

        this.opcode = opcode;
        this.operand = operand;
    }

    public Line(String opcode) {
        this.opcode = opcode;
    }



    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }
}

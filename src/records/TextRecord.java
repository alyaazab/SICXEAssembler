package records;

public class TextRecord extends Record{

    public TextRecord() {
        this.startOfLine = "T";
    }

    @Override
    public String toString() {
        return startOfLine + address + "^" + textLength + "^" + objectCodesString;
    }
}

package records;

public class HeaderRecord extends Record {

    public HeaderRecord() {
        this.startOfLine = "H";
    }

    @Override
    public String toString() {
        return startOfLine + name + "^" + address + "^" + programLength;
    }
}

package records;

public class EndRecord extends Record {

    public EndRecord() {
        this.startOfLine = "E";
    }

    @Override
    public String toString() {
        return startOfLine + address;
    }
}

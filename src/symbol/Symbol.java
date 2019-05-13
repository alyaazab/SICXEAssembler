package symbol;

public class Symbol {

    private int value;
    private int length;
    private boolean relocatable;
    private String label;


    public Symbol(String label, int value, int length, boolean relocatable) {
        this.label = label;
        this.value = value;
        this.length = length;
        this.relocatable = relocatable;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("\n%-20s%-20d%-23d%-20b", label, value, length, relocatable);

    }
}

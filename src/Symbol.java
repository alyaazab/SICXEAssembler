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

    @Override
    public String toString() {
        return label + " " + value + " " + length + " " + relocatable;
    }
}
